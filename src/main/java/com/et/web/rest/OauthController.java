package com.et.web.rest;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.apollographql.apollo.api.Response;
import com.et.api.firstpromoter.FirstPromoterSaleResponse;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.intercom.CreateContactResponse;
import com.et.api.intercom.GetContactResponse;
import com.et.api.intercom.IntercomUtil;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.shop.Shop;
import com.et.constant.Constants;
import com.et.domain.Authority;
import com.et.domain.SocialConnection;
import com.et.domain.User;
import com.et.handler.OnBoardingHandler;
import com.et.repository.AuthorityRepository;
import com.et.repository.UserRepository;
import com.et.security.AuthoritiesConstants;
import com.et.security.jwt.TokenProvider;
import com.et.service.ShopInfoService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.utils.SlackField;
import com.et.utils.SlackService;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.shopify.java.graphql.client.queries.ShopQuery;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.et.constant.Constants.DEFAULT_LANGUAGE;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private static final String OAUTH_CALLBACK = "/oauth/%s/callback";
    public static final String INTERCOM_TOKEN = "dG9rOmQyMWM4OWU1XzdmN2JfNGVjN184NWZhXzQ5MWI2NWNmYjNlZDoxOjA=";
    private final Logger log = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${oauth.base-uri}")
    private String oauthBaseUri;

    @Autowired
    private OnBoardingHandler onBoardingHandler;

    @Autowired
    private CommonUtils commonUtils;


    private RestTemplate restTemplate;

    @GetMapping("/{providerId}/authenticate")
    public void authenticate(@PathVariable("providerId") String providerId,
                             @RequestParam("shop") String shop,
                             @RequestParam(value = "hmac", required = false) String hmac,
                             @RequestParam(value = "timestamp", required = false) Long timestamp,
                             @CookieValue(name = "_fprom_tid", required = false) String firstPromoterTrackingId,
                             @RequestParam(value = "host", required = false) String host,
                             @RequestParam(value = "embedded", required = false) String embedded,
                             HttpServletResponse response)
        throws IOException {

        log.info("{providerId}/authenticate is called, shop="
            + shop
            + " firstPromoterTrackingId="
            + Optional.ofNullable(firstPromoterTrackingId).orElse("")
            + " embedded="
            + Optional.ofNullable(embedded).orElse(""));
        oauthFirstCall(providerId, shop, response, null, null, host);
    }


    @GetMapping("/{providerId}/authenticate/subscriptions")
    public void authenticate1(@PathVariable("providerId") String providerId, @RequestParam("shop") String shop,
                              @RequestParam(value = "hmac", required = false) String hmac,
                              @RequestParam(value = "timestamp", required = false) Long timestamp,
                              @RequestParam(value = "customer_id", required = false) Long customerId,
                              @RequestParam(value = "id", required = false) Long subscriptionId,
                              @RequestParam(value = "host", required = false) String host,
                              @RequestParam(value = "embedded", required = false) String embedded,
                              HttpServletResponse response)
        throws IOException {

        log.info("{providerId}/authenticate/subscriptions is called, shop="
            + shop
            + " embedded="
            + Optional.ofNullable(embedded).orElse(""));

        oauthFirstCall(providerId, shop, response, customerId, subscriptionId, host);
    }

    private void oauthFirstCall(String providerId, String shop, HttpServletResponse response, Long customerId, Long subscriptionId, String host) throws IOException {
        Map<String, OAuth2ClientProperties.Registration> registrationMap = oAuth2ClientProperties.getRegistration();
        OAuth2ClientProperties.Registration registration = registrationMap.get(providerId);
        String clientSecret = registration.getClientSecret();
        String clientId = registration.getClientId();

        String baseUrl = oauthBaseUri;
        String providerCallback = String.format(OAUTH_CALLBACK, providerId);
        String callbackUrl = String.format("%s%s", baseUrl, providerCallback);

        try {
            if (host != null) {
                log.info("setting host cookie for shop=" + shop + " host=" + host);
                setHostCookie(response, host);
            }
        } catch (Exception e) {

        }

        final OAuth20Service service = new ServiceBuilder(clientId).apiSecret(clientSecret)
            .withScope(String.join(",", registration.getScope())).callback(callbackUrl).build(new ShopifyOauth(shop));

        String authorizationUrl = null;

        Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByProviderIdAndUserId(providerId, shop);

        if (socialConnectionOptional.isPresent()) {
            ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
            Boolean accessTokenValid = api.isAccessTokenValid();

            if (accessTokenValid && host != null) {
                authorizationUrl = oauthBaseUri + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse(""); //service.getAuthorizationUrl();
            } else {
                authorizationUrl = service.getAuthorizationUrl();
            }
        } else {
            authorizationUrl = service.getAuthorizationUrl();
        }


        log.info("Redirecting to: " + authorizationUrl);

        if (customerId != null) {

            /*if (shop.equals("inspiredgo.myshopify.com")) {
                response.sendRedirect("https://" + shop);
                return;
            } else {
                response.sendRedirect(authorizationUrl + "&state=" + customerId);
                log.info("generated url=" + authorizationUrl);
                return;
            }*/

            if (host == null) {
                response.sendRedirect(authorizationUrl + "&state=" + customerId);
            } else if(subscriptionId != null) {
                authorizationUrl = oauthBaseUri + "/dashboards/subscription/" + subscriptionId + "/detail" + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse("");
                response.sendRedirect(authorizationUrl);
            } else {
                authorizationUrl = oauthBaseUri + "/dashboards/customers/" + customerId + "/edit" + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse("");
                response.sendRedirect(authorizationUrl);
            }

            log.info("generated url=" + authorizationUrl);
            return;

            /*Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByProviderIdAndUserId(providerId, shop);

            if (socialConnectionOptional.isPresent()) {
                SocialConnection socialConnection = socialConnectionOptional.get();
                ShopifyWithRateLimiter api = new ShopifyWithRateLimiter(socialConnection.getAccessToken(), socialConnection.getUserId());
                Boolean isValidAccessToken = api.isAccessTokenValid();
                if (isValidAccessToken) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(shop);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(shop,
                        userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    String jwt = tokenProvider.createToken(authenticationToken, false);

                    setShopCookie(response, authenticationToken);
                    response.addCookie(getSocialAuthenticationCookie(jwt));
                    response.sendRedirect(oauthBaseUri + "#/dashboards/customers/" + customerId + "/edit");
                }
                return;
            }*/
        }

        log.info("generated url=" + authorizationUrl);

        String contentSecurityValue = "frame-ancestors https://" + shop + " https://admin.shopify.com;";
        response.addHeader("Content-Security-Policy", contentSecurityValue);

        response.sendRedirect(authorizationUrl);
    }

    @Autowired
    private ShopInfoService shopInfoService;

    @GetMapping("/{providerId}/callback")
    public RedirectView callback(@PathVariable("providerId") String providerId,
                                 @RequestParam("code") String code,
                                 @RequestParam("shop") String shop,
                                 @RequestParam(value = "hmac", required = false) String hmac,
                                 @RequestParam(value = "timestamp", required = false) Long timestamp,
                                 @RequestParam(value = "state", required = false) String state,
                                 @RequestParam(value = "host", required = false) String host,
                                 @RequestParam(value = "embedded", required = false) String embedded,
                                 @CookieValue(name = "_fprom_tid", required = false) String firstPromoterTrackingId,
                                 HttpServletResponse response)
        throws Exception {

        log.info("callback is called, shop="
            + shop
            + " firstPromoterTrackingId="
            + Optional.ofNullable(firstPromoterTrackingId).orElse("")
            + " host="
            + host
            + " embedded="
            + Optional.ofNullable(embedded).orElse(""));

        try {
            if (host != null) {
                log.info("setting host cookie for shop=" + shop + " host=" + host);
                setHostCookie(response, host);
            }
        } catch (Exception e) {

        }

        Map<String, OAuth2ClientProperties.Registration> registrationMap = oAuth2ClientProperties.getRegistration();
        OAuth2ClientProperties.Registration registration = registrationMap.get(providerId);
        String clientSecret = registration.getClientSecret();
        String clientId = registration.getClientId();

        Optional<User> user = userRepository.findOneByLogin(shop);
        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId(providerId,
            shop);
        final OAuth20Service service = new ServiceBuilder(clientId).apiSecret(clientSecret)
            .build(new ShopifyOauth(shop));

        log.info("Requesting accesstoken for shop=" + shop + " code=" + code);
        OAuth2AccessToken accessToken = service.getAccessToken(code);
        String accessTokenValue = accessToken.getAccessToken();

        log.info("AccessToken received for shop=" + shop + " accessToken=" + accessTokenValue);

        boolean doesValidSocialTokenExists = socialConnection.isPresent() && socialConnection.get().getAccessToken().equalsIgnoreCase(accessTokenValue);

        if (!doesValidSocialTokenExists && socialConnection.isPresent()) {
            log.info("oldToken=" + socialConnection.map(SocialConnection::getAccessToken).orElse("EMPTY") + " newToken=" + accessTokenValue + " shop=" + shop);
        }

        if (!user.isPresent() || !doesValidSocialTokenExists) {
            if (!user.isPresent()) {
                log.info("user is not present, shop=" + shop);
                signUserUp(accessTokenValue, shop);

                if (firstPromoterTrackingId != null) {
                    try {
                        HashMap<String, Object> bodyMap = new HashMap<>();
                        bodyMap.put("uid", shop);
                        bodyMap.put("tid", firstPromoterTrackingId);

                        HttpHeaders headers = new HttpHeaders();
                        headers.add("x-api-key", Constants.FIRST_PROMOTER_API_KEY);
                        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<HashMap<String, Object>>(bodyMap, headers);

                        restTemplate = new RestTemplate();
                        ResponseEntity<FirstPromoterSaleResponse> exchange = restTemplate.exchange(
                            "https://firstpromoter.com/api/v1/track/signup", HttpMethod.POST, requestEntity,
                            FirstPromoterSaleResponse.class);

                        if (exchange.getStatusCode().is2xxSuccessful()) {
                            FirstPromoterSaleResponse body = exchange.getBody();
                        }
                    } catch (Exception ex) {
                        log.error("An error occurred while registering lead on First promotion.", ex);
                    }
                }
            }

            log.info("Executing onboarding handler for shop=" + shop);

            onBoardingHandler.handle(shop, accessTokenValue, providerId);

            ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
            Shop shopInfo = api.getShopInfo().getShop();

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
            ShopQuery shopQuery = new ShopQuery();
            Response<Optional<ShopQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopQuery);
            Optional<Boolean> eligibleForSubscriptionsOptional = optionalQueryResponse.getData().map(s1 -> s1.getShop().getFeatures().isEligibleForSubscriptions());

            try {
                SlackService.sendMessage("🐐 " + providerId,
                    SlackService.SlackChannel.NotificationSC,
                    new SlackField("Shop", shop),
                    new SlackField("shopify_plan", shopInfo.getPlanName()),
                    new SlackField("Message", "Subscription installed"));


                // get order count and send slack notification
                Long productCount = api.getProductCount().getCount();
                SlackService.sendMessage("🐐 " + providerId,
                    SlackService.SlackChannel.NotificationSC,
                    new SlackField("Shop", shop),
                    new SlackField("Total products under this shop", Long.toString(productCount)),
                    new SlackField("Eligible for subscription", eligibleForSubscriptionsOptional.orElse(false).toString()),
                    new SlackField("customerEmail", shopInfo.getCustomerEmail()),
                    new SlackField("shopEmail", shopInfo.getEmail()),
                    new SlackField("intercom_url", findIntercomUrl(shop)));

            } catch (Exception ex) {
                log.error("An error occurred while sending slack message", ex);
            }
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(shop);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(shop,
            userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, false);

        setShopCookie(response, authenticationToken);
        response.addCookie(getSocialAuthenticationCookie(jwt));

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByShop(shop);
        if (shopInfoOptional.isPresent() && user.isPresent()) {
            if (shopInfoOptional.get().getShopifyPlanName().equalsIgnoreCase("partner_test")
                && !Optional.ofNullable(shopInfoOptional.get().isWhiteListed()).orElse(false)
                && !user.get().getEmail().equalsIgnoreCase("admin@appstle.com")) {
                return new RedirectView("/404.html", false);
            }
        }

        String contentSecurityValue = "frame-ancestors https://" + shop + " https://admin.shopify.com;";
        response.addHeader("Content-Security-Policy", contentSecurityValue);

        if (state != null) {
            return new RedirectView(oauthBaseUri + "/dashboards/customers/" + state + "/edit" + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse(""), false);
        }

        return new RedirectView(oauthBaseUri + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse(""), false);
    }

    private String findIntercomUrl(String shop) {
        try {
            GetContactResponse intercomResponse = IntercomUtil.findUser(shop, INTERCOM_TOKEN);

            String url = null;
            if (intercomResponse.getData().size() > 0) {
                url = "https://app.intercom.com/a/apps/x4xb7xfy/users/" + intercomResponse.getData().get(0).getId();
            } else {
                CreateContactResponse contact = IntercomUtil.createUser(shop, INTERCOM_TOKEN);
                url = "https://app.intercom.com/a/apps/x4xb7xfy/users/" + contact.getId();
            }

            return url;
        } catch (Exception e) {
            return "";
        }
    }

    private Cookie getSocialAuthenticationCookie(String token) {
        Cookie socialAuthCookie = new Cookie("jhi-authenticationToken", token);
        socialAuthCookie.setPath("/");
        socialAuthCookie.setMaxAge(10);
        return socialAuthCookie;
    }

    private void setShopCookie(HttpServletResponse httpServletResponse, Authentication authentication) {
        Cookie shopCookie = new Cookie("jhi-shop", authentication.getName());
        shopCookie.setPath("/");
        shopCookie.setMaxAge(10);
        httpServletResponse.addCookie(shopCookie);
    }

    private void setHostCookie(HttpServletResponse httpServletResponse, String host) {
        Cookie hostCookie = new Cookie("shopify-host", host);
        hostCookie.setPath("/");
        hostCookie.setMaxAge(10);
        httpServletResponse.addCookie(hostCookie);
    }

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private User signUserUp(String accessToken, String shop) {

        ShopifyAPI api = new ShopifyWithRateLimiter(accessToken, shop);
        Shop shopDetails = api.getShopInfo().getShop();

        String email = Optional.ofNullable(shopDetails.getCustomerEmail()).orElse(shopDetails.getEmail());
        String userName = shop;

        if (!StringUtils.isBlank(userName)) {
            userName = userName.toLowerCase(Locale.ENGLISH);
        }

        String login = userName;

        String passwordDecoded = RandomStringUtils.random(10, true, true);//userName + "_" + "spoof";
        mayBeTryAddingItemToDynamo(userName, passwordDecoded);

        String encryptedPassword = passwordEncoder.encode(passwordDecoded);
        Set<Authority> authorities = new HashSet<>(1);

        authorities.add(authorityRepository.findById(AuthoritiesConstants.USER).get());

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(StringUtils.isEmpty(shopDetails.getName()) ? "UNKNOWN" : shopDetails.getName());
        newUser.setLastName(null);
        newUser.setEmail(email);
        newUser.setActivated(true);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(DEFAULT_LANGUAGE);
        newUser.setImageUrl(null);

        User user = userRepository.save(newUser);

        return user;
    }

    public void mayBeTryAddingItemToDynamo(String userName, String passwordDecoded) {
        try {

            Map<String, AttributeValue> item = new HashMap<>();

            AttributeValue passwordAttributeValue = new AttributeValue();
            passwordAttributeValue.setS(passwordDecoded);
            item.put("password", passwordAttributeValue);

            AttributeValue shopAttributeValue = new AttributeValue();
            shopAttributeValue.setS(userName);
            item.put("shop", shopAttributeValue);

            PutRequest putRequest = new PutRequest();
            putRequest.setItem(item);

            WriteRequest writeRequest = new WriteRequest();
            writeRequest.setPutRequest(putRequest);

            List<WriteRequest> writeRequests = new ArrayList<>();
            writeRequests.add(writeRequest);

            BatchWriteItemRequest batchWriteItemRequest = new BatchWriteItemRequest();
            batchWriteItemRequest.addRequestItemsEntry("shop_secrets", writeRequests);

            amazonDynamoDB.batchWriteItem(batchWriteItemRequest);
        } catch (Exception ex) {
            String a = "b";
        }
    }
}
