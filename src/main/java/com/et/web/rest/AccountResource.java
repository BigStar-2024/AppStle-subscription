package com.et.web.rest;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.et.domain.PaymentPlan;
import com.et.domain.PlanInfo;
import com.et.domain.User;
import com.et.repository.PaymentPlanRepository;
import com.et.repository.SocialConnectionRepository;
import com.et.repository.UserRepository;
import com.et.security.SecurityUtils;
import com.et.service.MailService;
import com.et.service.UserService;
import com.et.service.dto.PasswordChangeDTO;
import com.et.service.dto.UserDTO;
import com.et.web.rest.errors.EmailAlreadyUsedException;
import com.et.web.rest.errors.EmailNotFoundException;
import com.et.web.rest.errors.InvalidPasswordException;
import com.et.web.rest.errors.LoginAlreadyUsedException;
import com.et.web.rest.vm.KeyAndPasswordVM;
import com.et.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.et.constant.Constants.ANALYTICS_INDEX;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    @Autowired
    private PaymentPlanRepository paymentPlanRepository;

    @Autowired
    private SocialConnectionRepository socialConnectionRepository;


    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    /*@PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }*/

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() throws IOException {
        Optional<UserDTO> userDTOOptional = userService.getUserWithAuthorities()
            .map(UserDTO::new);

        if (userDTOOptional.isPresent()) {
            UserDTO user = userDTOOptional.get();
            String shop = user.getLogin();
            Optional<PaymentPlan> paymentPlanOptional = paymentPlanRepository.findByShop(shop);


            if (paymentPlanOptional.isPresent()) {

                user.setPaymentPlanAvailable(true);

                PaymentPlan paymentPlan = paymentPlanOptional.get();

                PlanInfo planInfo = paymentPlan.getPlanInfo();

                user.setPlanName(planInfo.getName());
                user.setPlanId(planInfo.getId());
                if (paymentPlan.getPlanInfo().isArchived()) {
                    user.setCustomPlanApply(true);
                }
            } else {
                user.setPaymentPlanAvailable(false);
                user.setCustomPlanApply(false);
            }

            try {
                HashMap<String, AttributeValue> map = new HashMap<>();
                map.put("shop", new AttributeValue(shop));
                GetItemResult itemResult = amazonDynamoDB.getItem(new GetItemRequest("shop_secrets", map));
                AttributeValue password = itemResult.getItem().get("password");
                user.setDynamodbPassword(password.getS());
                String a = "b";
            } catch (Exception ex) {

            }

            try {
                log.info("adding getAuthorities");
                // Add authorities
                if(user.getAuthorities() != null) {
                    log.info("adding getAuthorities() != null");
                    user.getAuthorities().addAll(SecurityUtils.getAuthorities());
                    log.info("final Authorities=" + user.getAuthorities());
                } else {
                    log.info("adding getAuthorities() == null");
                    user.setAuthorities(SecurityUtils.getAuthorities());
                    log.info("final Authorities=" + user.getAuthorities());
                }
            } catch (Exception ex) {

            }
        }

        return userDTOOptional
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    private double getSum(MultiSearchResponse.Item response, String subAggregationKey) {
        Aggregations aggregations = response.getResponse().getAggregations();
        Aggregation recommendation = aggregations.get("searchAnalytics");

        if (!(recommendation instanceof ParsedFilter)) {
            return 0L;
        }
        Aggregations aggregations1 = ((ParsedFilter) recommendation).getAggregations();
        Map<String, Aggregation> asMap = aggregations1.getAsMap();

        if (!asMap.containsKey(subAggregationKey)) {
            return 0L;
        }
        Aggregation aggregation = asMap.get(subAggregationKey);

        if (!(aggregation instanceof ParsedSum)) {
            return 0;
        }
        double value = ((ParsedSum) aggregation).getValue();
        return value;

    }

    private SearchRequest buildAggregationRequest(String shop, AggregationBuilder subAggregation) {
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(shop, subAggregation);

        SearchRequest searchRequest = new SearchRequest(ANALYTICS_INDEX);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private SearchSourceBuilder buildSearchSourceBuilder(String shop, AggregationBuilder subAggregation) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(
            AggregationBuilders.filter("searchAnalytics", QueryBuilders.termQuery("api_key", shop))
                .subAggregation(subAggregation));
        return searchSourceBuilder;
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        /*Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }*/
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     * @throws EmailNotFoundException {@code 400 (Bad Request)} if the email address is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        mailService.sendPasswordResetMail(
            userService.requestPasswordReset(mail)
                .orElseThrow(EmailNotFoundException::new)
        );
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
