package com.et.web.rest;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.data.SubscriptionContractData;
import com.et.api.graphql.data.SubscriptionContractInfo;
import com.et.api.graphql.data.SubscriptionCustomerData;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.api.utils.CurrencyFormattingUtils;
import com.et.api.utils.CurrencyUtils;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.api.utils.SubscriptionUtils;
import com.et.constant.Constants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.handler.OnBoardingHandler;
import com.et.pojo.*;
import com.et.repository.*;
import com.et.security.SecurityUtils;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.utils.*;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.CUSTOMER_ID_PREFIX;
import static com.et.api.constants.ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX;
import static java.util.Objects.requireNonNull;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionContractDetails}.
 */
@RestController
@Api(tags = "Subscription Contract Details Resource")
public class SubscriptionContractDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractDetailsResource.class);

    public static final String ENTITY_NAME = "subscriptionContractDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionContractDetailsService subscriptionContractDetailsService;

    private final SubscriptionContractService subscriptionContractService;

    private final ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    private final SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    private final SubscriptionCustomerService subscriptionCustomerService;

    private final CommonUtils commonUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Autowired
    private CommonEmailUtils commonEmailUtils;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private OnBoardingHandler handler;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private SubscriptionGroupService subscriptionGroupService;

    @Autowired
    private AwsUtils awsUtils;

    @Autowired
    private BulkAutomationService bulkAutomationService;


    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public SubscriptionContractDetailsResource(SubscriptionContractDetailsService subscriptionContractDetailsService, SubscriptionContractService subscriptionContractService,
                                               ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService, SubscriptionBillingAttemptService subscriptionBillingAttemptService,
                                               SubscriptionCustomerService subscriptionCustomerService, CommonUtils commonUtils) {
        this.subscriptionContractDetailsService = subscriptionContractDetailsService;
        this.subscriptionContractService = subscriptionContractService;
        this.shopifyGraphqlSubscriptionContractService = shopifyGraphqlSubscriptionContractService;
        this.subscriptionBillingAttemptService = subscriptionBillingAttemptService;
        this.subscriptionCustomerService = subscriptionCustomerService;
        this.commonUtils = commonUtils;
    }

    /**
     * {@code POST  /subscription-contract-details} : Create a new subscriptionContractDetails.
     *
     * @param subscriptionContractDetailsDTO the subscriptionContractDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionContractDetailsDTO, or with status {@code 400 (Bad Request)} if the subscriptionContractDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-contract-details")
    public ResponseEntity<SubscriptionContractDetailsDTO> createSubscriptionContractDetails(@Valid @RequestBody SubscriptionContractDetailsDTO subscriptionContractDetailsDTO, HttpServletRequest request) throws URISyntaxException {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to save SubscriptionContractDetails : {}", RequestURL, subscriptionContractDetailsDTO);
        if (subscriptionContractDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionContractDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionContractDetailsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractDetailsDTO result = subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
        return ResponseEntity.created(new URI("/api/subscription-contract-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-contract-details} : Updates an existing subscriptionContractDetails.
     *
     * @param subscriptionContractDetailsDTO the subscriptionContractDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionContractDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionContractDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionContractDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping(value = {"/api/subscription-contract-details", "/subscriptions/cp/api/subscription-contract-details"})
    public ResponseEntity<SubscriptionContractDetailsDTO> updateSubscriptionContractDetails(@Valid @RequestBody SubscriptionContractDetailsDTO subscriptionContractDetailsDTO, HttpServletRequest request) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to update SubscriptionContractDetails : {}", RequestURL, subscriptionContractDetailsDTO);
        if (subscriptionContractDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        if (!shop.equals(subscriptionContractDetailsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractDetailsDTO oldSubscriptionContractDetailsDTOData = subscriptionContractDetailsService.findByContractId(subscriptionContractDetailsDTO.getSubscriptionContractId()).get();

        SubscriptionContractDetailsDTO result = subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

        if (result.isAllowDeliveryPriceOverride() != oldSubscriptionContractDetailsDTOData.isAllowDeliveryPriceOverride()) {
            Map<String, Object> map = new HashMap<>();
            map.put("oldValue", BooleanUtils.isTrue(oldSubscriptionContractDetailsDTOData.isAllowDeliveryPriceOverride()));
            map.put("newValue", BooleanUtils.isTrue(result.isAllowDeliveryPriceOverride()));
            commonUtils.writeActivityLog(subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.MERCHANT_PORTAL, ActivityLogEventType.DELIVERY_PRICE_OVERRIDE_CHANGED, ActivityLogStatus.SUCCESS, map);
            commonUtils.mayBeUpdateShippingPriceAsync(subscriptionContractDetailsDTO.getSubscriptionContractId(), shop);
        }

        if (!StringUtils.equals(oldSubscriptionContractDetailsDTOData.getOrderNote(), result.getOrderNote())) {

            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

            subscriptionDraftInputBuilder.note(result.getOrderNote());

            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionDraftInput);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Contract Details Updated.", ""))
            .body(result);
    }

    /**
     * {@code GET  /subscription-contract-details} : get all the subscriptionContractDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionContractDetails in body.
     */
    @GetMapping("/api/subscription-contract-details")
    public ResponseEntity<List<SubscriptionContractDetailsDTO>> getAllSubscriptionContractDetails(
        @RequestParam(name = "fromCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromCreatedDate,
        @RequestParam(name = "toCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toCreatedDate,
        @RequestParam(name = "fromUpdatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromUpdatedDate,
        @RequestParam(name = "toUpdatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toUpdatedDate,
        @RequestParam(name = "fromNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromNextDate,
        @RequestParam(name = "toNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toNextDate,
        @RequestParam(name = "subscriptionContractId", required = false) String subscriptionContractId,
        @RequestParam(name = "customerName", required = false) String customerName,
        @RequestParam(name = "orderName", required = false) String orderName,
        @RequestParam(name = "status", required = false) String status,
        @RequestParam(name = "billingPolicyIntervalCount", required = false) Integer billingPolicyIntervalCount,
        @RequestParam(name = "billingPolicyInterval", required = false) String billingPolicyInterval,
        @RequestParam(name = "planType", required = false) String planType,
        @RequestParam(name = "recordType", required = false) String recordType,
        @RequestParam(name = "productId", required = false) Long productId,
        @RequestParam(name = "variantId", required = false) Long variantId,
        @RequestParam(name = "sellingPlanIds", required = false) String sellingPlanIds,
        @RequestParam(name = "minOrderAmount", required = false) Double minOrderAmount,
        @RequestParam(name = "maxOrderAmount", required = false) Double maxOrderAmount,
        Pageable pageable) {
        log.debug("REST request to get a page of SubscriptionContractDetails");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        List<String> gqlSellingPlanIds = new ArrayList<>();
        if(StringUtils.isNotBlank(sellingPlanIds)) {
            gqlSellingPlanIds = Arrays.stream(sellingPlanIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(ShopifyGraphQLUtils::getGraphQLSellingPlanId).collect(Collectors.toList());
        }

        //Check for duplicate subscriptions before fetching list
//        subscriptionContractDetailsService.maybeRemoveDuplicateContracts(shop);

        //Get subscriptions list
        Page<SubscriptionContractDetailsDTO> page = subscriptionContractDetailsService.findByShop(pageable, shop,
            fromCreatedDate, toCreatedDate,
            fromUpdatedDate, toUpdatedDate,
            fromNextDate, toNextDate,
            subscriptionContractId, customerName,
            orderName, status,
            billingPolicyIntervalCount, billingPolicyInterval,
            planType, recordType,
            productId, variantId, gqlSellingPlanIds, minOrderAmount, maxOrderAmount);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-contract-details")
    @ApiOperation("Get Subscription List")
    public ResponseEntity<List<SubscriptionContractDetailsDTO>> getAllSubscriptionContractDetails(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("From Create Date") @RequestParam(name = "fromCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromCreatedDate,
        @ApiParam("To Create Date") @RequestParam(name = "toCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toCreatedDate,
        @ApiParam("From Updated Date") @RequestParam(name = "fromUpdatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromUpdatedDate,
        @ApiParam("To Updated Date") @RequestParam(name = "toUpdatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toUpdatedDate,
        @ApiParam("From Next Order Date") @RequestParam(name = "fromNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromNextDate,
        @ApiParam("To Next Order Date") @RequestParam(name = "toNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toNextDate,
        @ApiParam("Subscription Contract ID") @RequestParam(name = "subscriptionContractId", required = false) String subscriptionContractId,
        @ApiParam("Customer Name or Email") @RequestParam(name = "customerName", required = false) String customerName,
        @ApiParam("Order No") @RequestParam(name = "orderName", required = false) String orderName,
        @ApiParam("Status") @RequestParam(name = "status", required = false) String status,
        @ApiParam("Billing Policy Interval Count") @RequestParam(name = "billingPolicyIntervalCount", required = false) Integer billingPolicyIntervalCount,
        @ApiParam("Billing Policy Interval") @RequestParam(name = "billingPolicyInterval", required = false) String billingPolicyInterval,
        @ApiParam("Plan Type") @RequestParam(name = "planType", required = false) String planType,
        @ApiParam("Record Type") @RequestParam(name = "recordType", required = false) String recordType,
        @ApiParam("Product Id") @RequestParam(name = "productId", required = false) Long productId,
        @ApiParam("Variant Id") @RequestParam(name = "variantId", required = false) Long variantId,
        @ApiParam("Selling Plan Id") @RequestParam(name = "sellingPlanId", required = false) Long sellingPlanId,
        @ApiParam("Min Order Amount") @RequestParam(name = "minOrderAmount", required = false) Double minOrderAmount,
        @ApiParam("Max Order Amount") @RequestParam(name = "maxOrderAmount", required = false) Double maxOrderAmount,
        @ApiParam("Page Info") Pageable pageable) {
        log.debug("REST request to get a page of SubscriptionContractDetails");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Page<SubscriptionContractDetailsDTO> page = subscriptionContractDetailsService.findByShop(pageable, shop,
            fromCreatedDate, toCreatedDate,
            fromUpdatedDate, toUpdatedDate,
            fromNextDate, toNextDate,
            subscriptionContractId, customerName,
            orderName, status,
            billingPolicyIntervalCount, billingPolicyInterval,
            planType, recordType,
            productId, variantId, sellingPlanId, minOrderAmount, maxOrderAmount);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/api/subscription-contract-details/without-pagination")
    public List<SubscriptionContractDetailsDTO> getAllSubscriptionContractDetails() {
        log.debug("REST request to get all SubscriptionContractDetails");
        return subscriptionContractDetailsService.findByShop(SecurityUtils.getCurrentUserLogin().get());
    }

    @GetMapping("/api/subscription-contract-details/customers")
    public ResponseEntity<List<CustomerInfo>> getAllSubscriptionContractDetailsByShop(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "email", required = false) String email,
        @RequestParam(value = "activeMoreThanOneSubscription", required = false, defaultValue = "false") Boolean activeMoreThanOneSubscription,
        Pageable pageable
    ) {
        log.debug("REST request to get page of Customers");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Page<CustomerInfo> page = subscriptionContractDetailsService.findByShopGroupByCustomerIdPaginated(pageable, shop, name, email, activeMoreThanOneSubscription);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-contract-details/customers")
    @ApiOperation("Get Customers List")
    public ResponseEntity<List<CustomerInfo>> getAllSubscriptionContractDetailsByShop(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Customer name") @RequestParam(name = "name", required = false) String name,
        @ApiParam("Customer email") @RequestParam(name = "email", required = false) String email,
        @ApiParam("Has multiple subscriptions") @RequestParam(value = "activeMoreThanOneSubscription", required = false, defaultValue = "false") Boolean activeMoreThanOneSubscription,
        @ApiParam("Page Info") Pageable pageable
    ) {
        log.debug("External request to get page of Customers");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Page<CustomerInfo> page = subscriptionContractDetailsService.findByShopGroupByCustomerIdPaginated(pageable, shop, name, email, activeMoreThanOneSubscription);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-contract-details/:id} : get the "id" subscriptionContractDetails.
     *
     * @param id the id of the subscriptionContractDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionContractDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/subscription-contract-details/{id}")
    public ResponseEntity<SubscriptionContractDetailsDTO> getSubscriptionContractDetails(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionContractDetails : {}", id);
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (subscriptionContractDetailsDTO.isPresent()) {
            if (!shop.equals(subscriptionContractDetailsDTO.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionContractDetailsDTO);
    }


    @GetMapping("/api/subscription-contract-details/subscription-contract-details-by-contract-id/{contractId}")
    public ResponseEntity<SubscriptionContractDetailsDTO> getSubscriptionContractDetailsByContractId(@PathVariable Long contractId) throws IOException {
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (subscriptionContractDetailsDTO.isPresent()) {
            if (!shop.equals(subscriptionContractDetailsDTO.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionContractDetailsDTO);
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contract-details/current-cycle/{contractId}", "/subscriptions/cp/api/subscription-contract-details/current-cycle/{contractId}"})
    public ResponseEntity<Integer> getCurrentCycleOfSubscriptionContract(@PathVariable Long contractId) throws IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        int totalCycles = 1;
        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.SUCCESS);
        totalCycles = totalCycles + subscriptionBillingAttempts.size();

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(totalCycles));
    }

    /**
     * {@code DELETE  /subscription-contract-details/:id} : delete the "id" subscriptionContractDetails.
     *
     * @param id the id of the subscriptionContractDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-contract-details/{id}")
    public ResponseEntity<Void> deleteSubscriptionContractDetails(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete SubscriptionContractDetails : {}", id);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request to delete SubscriptionContractDetails : {} and shop : {} ", RequestURL, id, shop);

        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = subscriptionContractDetailsService.findOne(id);

        if (subscriptionContractDetailsDTOOptional.isPresent()) {
            if (!shop.equals(subscriptionContractDetailsDTOOptional.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        subscriptionContractDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    @DeleteMapping("/api/subscription-contract-details/by-contraction-id/{contractId}")
    public ResponseEntity<Void> deleteSubscriptionContractFromDb(@PathVariable Long contractId) {
        log.debug("REST request to delete SubscriptionContractDetails contractId : {}", contractId);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);

        if (subscriptionContractDetailsDTOOptional.isPresent()) {
            if (!shop.equals(subscriptionContractDetailsDTOOptional.get().getShop())) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        subscriptionContractDetailsService.deleteByContractId(contractId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, contractId.toString())).build();
    }


    /**
     * {@code GET  /subscription-contracts} : get all subscriptionContracts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of SubscriptionContracts in body.
     */
    @GetMapping("/api/subscription-contracts")
    public SubscriptionContractData getAllSubscriptionContracts(@RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                                                @RequestParam(value = "cursor", required = false) String cursor) throws Exception {
        log.debug("REST request to get all SubscriptionContracts");
        return subscriptionContractService.findShopSubscriptionContracts(commonUtils.getShop(), next, cursor);
    }

    @GetMapping("/api/subscription-contracts/{contractId}")
    public ResponseEntity<SubscriptionContractInfo> getSubscriptionContract(@PathVariable Long contractId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionContractInfo> subscriptionContractInfoOptional = subscriptionContractService.findShopSubscriptionContractById(contractId, shop);
        return ResponseUtil.wrapOrNotFound(subscriptionContractInfoOptional);
    }

    @GetMapping(value = {"/api/subscription-contracts/contract/{contractId}", "/subscriptions/cp/api/subscription-contracts/contract/{contractId}"})
    @CrossOrigin
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> getSubscriptionContractRaw(@PathVariable Long contractId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getSubscriptionContractRawInternal(contractId, shop, commonUtils.prepareShopifyGraphqlClient(shop));
    }

    private ResponseEntity<SubscriptionContractQuery.SubscriptionContract> getSubscriptionContractRawInternal(@PathVariable Long contractId, String shop) throws Exception {
        return getSubscriptionContractRawInternal(contractId, shop, commonUtils.prepareShopifyGraphqlClient(shop));
    }

    private ResponseEntity<SubscriptionContractQuery.SubscriptionContract> getSubscriptionContractRawInternal(@PathVariable Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    /**
     * {@code DELETE  /subscription-contracts/:id} : delete the "id" SubscriptionContract.
     *
     * @param id the id of the SubscriptionContract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @CrossOrigin
    @DeleteMapping(value = {"/api/subscription-contracts/{id}", "/subscriptions/cp/api/subscription-contracts/{id}"})
    public ResponseEntity<Void> deleteSubscriptionContract(
        @PathVariable Long id,
        @RequestParam(value = "shop", required = false) String shopName,
        @RequestParam(value = "cancellationFeedback", required = false) String cancellationFeedback,
        @RequestParam(value = "cancellationNote", required = false) String cancellationNote,
        HttpServletRequest request
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} REST request calling API /subscription-contracts to delete SubscriptionContract of id : {} and shop : {} and cancellationFeedback : {}", RequestURL, id, shopName, cancellationFeedback);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(id);

        if (isExternal) {
            commonUtils.checkAttributeBasedMinCycles(shopName, id);
        }
        subscriptionContractService.delete(shopName, id, false, cancellationFeedback, cancellationNote, commonUtils.prepareShopifyGraphqlClient(shopName), isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        sendMailForSubscriptionCanceled(id, shopName);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Cancelled.", ""))
            .build();
    }

    @CrossOrigin
    @DeleteMapping("/api/external/v2/subscription-contracts/{id}")
    @ApiOperation("Cancel Subscription Contract")
    public ResponseEntity<Void> deleteSubscriptionContractV2(
        @ApiParam("Id") @PathVariable Long id,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Cancellation Feedback") @RequestParam(value = "cancellationFeedback", required = false) String cancellationFeedback,
        @ApiParam("Cancellation Note") @RequestParam(value = "cancellationNote", required = false) String cancellationNote,
        HttpServletRequest request
    ) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request calling API /external/v2/subscription-contracts to delete SubscriptionContract of id : {} and shop : {} and cancellationFeedback : {}", RequestURL, id, shop, cancellationFeedback);
        subscriptionContractService.delete(shop, id, false, cancellationFeedback, cancellationNote, commonUtils.prepareShopifyGraphqlClient(shop), ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        sendMailForSubscriptionCanceled(id, shop);

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Cancelled.", ""))
            .build();

    }

    @DeleteMapping("/api/subscription-contracts/internal/{id}")
    public ResponseEntity<Void> deleteSubscriptionContractInternal(@PathVariable Long id,
                                                                   @RequestParam(value = "cancellationFeedback", required = false) String cancellationFeedback,
                                                                   @RequestParam(value = "cancellationNote", required = false) String cancellationNote,
                                                                   HttpServletRequest request) throws Exception {
        log.debug("REST request to delete SubscriptionContract : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to calling API /subscription-contracts/internal to delete internal SubscriptionContract id : {} and shop : {} and cancellationFeedback : {}", RequestURL, id, shop, cancellationFeedback);
        //commonUtils.checkAttributeBasedMinCycles(shop, id);
        subscriptionContractService.delete(shop, id, true, cancellationFeedback, cancellationNote, commonUtils.prepareShopifyGraphqlClient(shop), ActivityLogEventSource.MERCHANT_PORTAL);
        sendMailForSubscriptionCanceled(id, shop);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Cancelled.", ""))
            .build();
    }

    private void sendMailForSubscriptionCanceled(Long id, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(id).get();

        if (subscriptionContractDetails.getStatus().equalsIgnoreCase("cancelled")) {
            commonEmailUtils.sendSubscriptionUpdateEmail(
                shopifyGraphqlClient,
                commonUtils.prepareShopifyResClient(shop),
                subscriptionContractDetails,
                EmailSettingType.SUBSCRIPTION_CANCELLED);
        }
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-payment-method", "/subscriptions/cp/api/subscription-contracts-update-payment-method"})
    public void updatePaymentMethod(HttpServletRequest request, @RequestParam("contractId") Long contractId, @RequestParam(value = "shop", required = false) String shopName) throws Exception {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info(RequestURL + " Request URL for calling API /subscription-contracts-update-payment-method to subscription contracts update payment of contactId : " + contractId + " and shop : " + shopName);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        subscriptionContractDetailsService.updatePaymentInfo(contractId, shopName);
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-existing-payment-method", "/subscriptions/cp/api/subscription-contracts-update-existing-payment-method"})
    public void updateExistingPaymentMethod(@RequestParam("contractId") Long contractId, @RequestParam(value = "shop", required = false) String shopName, @RequestParam("paymentMethodId") String paymentMethodId) throws Exception {
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        }
        boolean isExternal = commonUtils.isExternal();
        ActivityLogEventSource eventSource = isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL;
        if (StringUtils.isEmpty(paymentMethodId)) {
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetails = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);
        if (subscriptionContractDetails.isPresent()) {
            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
            subscriptionDraftInputBuilder.paymentMethodId(paymentMethodId); // Enter Payment method id
            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();
            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shopName);
            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContract(shopifyGraphqlClient, shopName, subscriptionContractDetails.get().getSubscriptionContractId(), subscriptionDraftInput);
            if (!subscriptionContractUpdateResult.isSuccess()) {
                throw new BadRequestAlertException(subscriptionContractUpdateResult.getErrorMessage(), "", "");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("newPaymentMethodId", paymentMethodId);
            commonUtils.writeActivityLog(shopName, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.SWITCH_PAYMENT_METHODS, ActivityLogStatus.SUCCESS, map);
        }
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-payment-method")
    @ApiOperation("Update Payment Method from Customer Portal")
    public void updatePaymentMethodV2(HttpServletRequest request, @ApiParam("Contract ID") @RequestParam("contractId") Long contractId, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-payment-method api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info(RequestURL + " Request URL for calling API /external/v2/subscription-contracts-update-payment-method to subscription contracts update payment of contactId : " + contractId + " and shop : " + shop);
        subscriptionContractDetailsService.updatePaymentInfo(contractId, shop);
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-apply-discount", "/subscriptions/cp/api/subscription-contracts-apply-discount"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> applyDiscountCode(
        @RequestParam("contractId") Long contractId,
        @RequestParam("discountCode") String discountCode,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.validateCustomerRequestForContract(contractId);

        boolean isExternal = commonUtils.isExternal();

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        return applyDiscountCodeInfo(contractId, shopName, discountCode, RequestURL, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-apply-discount")
    @ApiOperation("Apply Discount Code from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> applyDiscountCodeV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Discount Code") @RequestParam("discountCode") String discountCode,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-apply-discount api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request for {} Calling API /external/v2/subscription-contracts-apply-discount for shopify graphql for update subscription contract {} with discountCode {}", RequestURL, shop, contractId, discountCode);
        return applyDiscountCodeInfo(contractId, shop, discountCode, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

    }

    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> applyDiscountCodeInfo(Long contractId, String shop, String discountCode, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        if(subscriptionContractQueryResponse.getData().isPresent()) {
            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractQueryResponse.getData().get().getSubscriptionContract();
            if(subscriptionContract.isPresent()) {
                String discountId = (String) subscriptionContract.get().getDiscounts().getEdges().stream().map(SubscriptionContractQuery.Edge2::getNode).filter((node) -> {
                    return node.getTitle().isPresent() && ((String) node.getTitle().get()).equals(discountCode);
                }).findFirst().map(SubscriptionContractQuery.Node2::getId).orElse(null);
                if(StringUtils.isNotEmpty(discountId)) {
                    throw new BadRequestAlertException("Discount is already applied", "", "");
                }
            }
        }
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
//        log.debug("{} Response received from graphql update subscription contract {} ", shop, contractId);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                SubscriptionDraftDiscountCodeApplyMutation subscriptionDraftDiscountCodeApplyMutation = new SubscriptionDraftDiscountCodeApplyMutation(optionalDraftId.get(), discountCode);
                Response<Optional<SubscriptionDraftDiscountCodeApplyMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountCodeApplyMutation);

                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                List<SubscriptionDraftDiscountCodeApplyMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftDiscountCodeApply().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), ENTITY_NAME, "idexists");
                }

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                }

                Map<String, Object> map = new HashMap<>();
                map.put("discountType", "DISCOUNT CODE");
                map.put("discountCode", discountCode);

                commonUtils.writeActivityLog(shopifyGraphqlClient.getShop(), contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.DISCOUNT_APPLIED, ActivityLogStatus.SUCCESS, map);

            }

        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-add-discount", "/subscriptions/cp/api/subscription-contracts-add-discount"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addDiscount(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "percentage", required = false) Integer percentage,
        @RequestParam(value = "discountTitle", required = false) String discountTitle,
        @RequestParam(value = "recurringCycleLimit", required = false) Integer recurringCycleLimit,
        @RequestParam(value = "appliesOnEachItem", required = false) Boolean appliesOnEachItem,
        @RequestParam(value = "amount", required = false) Double amount,
        @RequestParam(value = "discountType", required = false) String discountType,
        HttpServletRequest request
    ) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return addDiscountInfo(contractId, shop, percentage, discountTitle, recurringCycleLimit, appliesOnEachItem, amount, discountType, RequestURL, ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-add-discount")
    @ApiOperation("Add Discount from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addDiscountV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Discount in Percentage") @RequestParam(value = "percentage", required = false) Integer percentage,
        @ApiParam("Discount Title") @RequestParam(value = "discountTitle", required = false) String discountTitle,
        @ApiParam("Recurring Cycle Limit") @RequestParam(value = "recurringCycleLimit", required = false) Integer recurringCycleLimit,
        @ApiParam("Applicable for each item?") @RequestParam(value = "appliesOnEachItem", required = false) Boolean appliesOnEachItem,
        @ApiParam("Discount in Amount") @RequestParam(value = "amount", required = false) Double amount,
        @ApiParam("Discount Type") @RequestParam(value = "discountType", required = false) String discountType,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-add-discount api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for {} Calling API /external/v2/subscription-contracts-add-discount of shopify graphql for update subscription contract : {} and percentage : {} and discountTitle : {} and recurringCycleLimit : {} and appliesOnEachItem : {} and amount : {} and discountType : {}", RequestURL, shop, contractId, percentage, discountTitle, recurringCycleLimit, appliesOnEachItem, amount, discountType);
        return addDiscountInfo(contractId, shop, percentage, discountTitle, recurringCycleLimit, appliesOnEachItem, amount, discountType, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

    }

    private ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addDiscountInfo(Long contractId, String shop, Integer percentage, String discountTitle, Integer recurringCycleLimit, Boolean appliesOnEachItem, Double amount, String discountType, String requestURL, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.info("{} REST request for {} Response received from graphql update subscription contract {} ", requestURL, shop, contractId);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                String draftId = optionalDraftId.get();
                subscriptionContractDetailsService.applyDiscount(
                    contractId,
                    percentage,
                    discountTitle,
                    recurringCycleLimit,
                    appliesOnEachItem,
                    amount,
                    discountType,
                    shopifyGraphqlClient,
                    draftId,
                    new ArrayList<>(),
                    eventSource);

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

            }

        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-remove-discount", "/subscriptions/cp/api/subscription-contracts-remove-discount"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> removeDiscount(
        @RequestParam("contractId") Long contractId,
        @RequestParam("discountId") String discountId,
        HttpServletRequest request
    ) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveDiscount(contractId, shopName, discountId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-remove-discount")
    @ApiOperation("Remove Discount from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> removeDiscountV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Discount ID") @RequestParam("discountId") String discountId,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-remove-discount api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("{} REST request for {} Calling API /external/v2/subscription-contracts-remove-discount of shopify graphql for update subscription contract : {} and discountId : {}", RequestURL, shop, contractId, discountId);
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveDiscount(contractId, shop, discountId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping(value = {"/api/v2/subscription-contracts-add-line-item", "/subscriptions/cp/api/v2/subscription-contracts-add-line-item"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addLineItem(
        @RequestParam(value = "contractId", required = false) Long contractId,
        @RequestParam(value = "quantity", required = false) Integer quantity,
        @RequestParam(value = "variantId", required = false) String productVariantId,
        @RequestParam(value = "isOneTimeProduct", required = false, defaultValue = "false") Boolean isOneTimeProduct,
        @RequestBody(required = false) LineItemInfo lineItemInfo,
        HttpServletRequest request
    ) throws Exception {
        boolean isExternal = commonUtils.isExternal();

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        if (isExternal) {
            if(BooleanUtils.isTrue(isOneTimeProduct)) {
                commonUtils.checkCustomerPermissions(shopName, "addOneTimeProductVariant");
            } else {
                commonUtils.checkCustomerPermissions(shopName, "addProductVariant");
            }
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract;
        if(Objects.nonNull(lineItemInfo)){
            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsAddLineItem(lineItemInfo.getContractId(), shopName, lineItemInfo.getQuantity(), lineItemInfo.getVariantId(), isOneTimeProduct, RequestURL, lineItemInfo.getCustomAttributes(), isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        }else{
            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsAddLineItem(contractId, shopName, quantity, productVariantId, isOneTimeProduct, RequestURL, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-add-line-item")
    @ApiOperation("Add Item to Subscription from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addLineItemV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Quantity of Product") @RequestParam("quantity") Integer quantity,
        @ApiParam("Variant ID of Product") @RequestParam("variantId") String productVariantId,
        @ApiParam("Is One Time Product?") @RequestParam(value = "isOneTimeProduct", required = false, defaultValue = "false") Boolean isOneTimeProduct,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-add-line-item api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for {} Calling API /external/v2/subscription-contracts-add-line-item of shopify graphql for update subscription contract : {} and quantity : {} and productVariantId : {}", RequestURL, shop, contractId, quantity, productVariantId);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsAddLineItem(contractId, shop, quantity, productVariantId, isOneTimeProduct, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-add-line-items")
    @ApiOperation("Add Item to Subscription from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addLineItems(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("New product IDs with Quantity") @RequestBody Map<String, Integer>  newVariants,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v3 RequestURL: {} /external/v2/subscription-contracts-add-line-items api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        for (Map.Entry<String, Integer> set : newVariants.entrySet()) {

            if(set.getValue().equals(0) || ObjectUtils.isEmpty(set.getValue())) {
                set.setValue(1);
            }

            log.info("{} REST request for {} Calling API /external/v2/subscription-contracts-add-line-items of shopify graphql for update subscription contract : {} and quantity : {} and productVariantId : {}", RequestURL, shop, contractId, set.getValue(), set.getKey());

            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsAddLineItem(contractId, shop, set.getValue(), set.getKey(), false, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-add-line-item")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addLineItem(
        @RequestParam("contractId") Long contractId,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("variantId") String productVariantId,
        @RequestParam("price") Double price,
        @RequestParam(value = "isOneTimeProduct", required = false, defaultValue = "false") Boolean isOneTimeProduct,
        HttpServletRequest request
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractAddLineItem(contractId, shop, quantity, productVariantId, price, isOneTimeProduct, RequestURL, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contract-add-line-item")
    @ApiOperation("Add Item to Subscription")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> addLineItemV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Quantity of Product") @RequestParam("quantity") Integer quantity,
        @ApiParam("Variant of Product") @RequestParam("variantId") String productVariantId,
        @ApiParam("Price of Product") @RequestParam("price") Double price,
        @ApiParam("Is One Time Product?") @RequestParam(value = "isOneTimeProduct", required = false, defaultValue = "false") Boolean isOneTimeProduct,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contract-add-line-item api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for {} Calling API /external/v2/subscription-contract-add-line-item of shopify graphql for update subscription contract : {} and quantity : {} and productVariantId : {} and price : {}", RequestURL, shop, contractId, quantity, productVariantId, price);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractAddLineItemWithNoDiscount(contractId, shop, quantity, productVariantId, price, isOneTimeProduct, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-remove-line-item", "/subscriptions/cp/api/subscription-contracts-remove-line-item"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> removeLineItem(
        @RequestParam("contractId") Long contractId,
        @RequestParam("lineId") String lineId,
        @RequestParam(value = "removeDiscount", defaultValue = "true") Boolean removeDiscount,
        HttpServletRequest request
    ) throws Exception {
        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId, lineId);
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract;
        if(removeDiscount){
            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveLineItemWithRetry(contractId, shopName, lineId, RequestURL, removeDiscount, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        }else{
            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveLineItem(contractId, shopName, lineId, RequestURL, removeDiscount, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-remove-line-item")
    @ApiOperation("Remove Item from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> removeLineItemV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Product ID") @RequestParam("lineId") String lineId,
        @ApiParam("Remove discount") @RequestParam(value = "removeDiscount", required = false, defaultValue = "true") Boolean removeDiscount,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-remove-line-item api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request to {} Calling API /external/v2/subscription-contracts-remove-line-item of shopify graphql for update subscription contract : {} and lineId : {}", RequestURL, shop, contractId, lineId);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveLineItemWithRetry(contractId, shop, lineId, RequestURL, true, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-remove-line-items")
    @ApiOperation("Remove Line Items from Subscription")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> removeLineItems(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Line Ids")@RequestBody List<String> lineIds,
        @ApiParam("Remove discount") @RequestParam(value = "removeDiscount", defaultValue = "false") Boolean removeDiscount,
        HttpServletRequest request
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v3 RequestURL: {} /external/v2/subscription-contracts-remove-line-items api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = null;

        for (String lineId : lineIds) {

            log.info("{} REST request to {} Calling API /external/v2/subscription-contracts-remove-line-items of shopify graphql for update subscription contract : {} and lineId : {}", RequestURL, shop, contractId, lineId);
            commonUtils.checkAttributeBasedMinCycles(shop, contractId, lineId);

            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsRemoveLineItem(contractId, shop, lineId, RequestURL, removeDiscount, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        }
         return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-line-item-quantity", "/subscriptions/cp/api/subscription-contracts-update-line-item-quantity"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemQuantity(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /external/v2/subscription-contracts-update-line-item-quantity for update subscription of shop : {} with contractId : {} and quantity : {} and lineId : {}", shop, contractId, quantity, lineId);

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            commonUtils.checkAttributeBasedMinCycles(shop, contractId, lineId);
            commonUtils.checkCustomerPermissions(shop, "editProduct");
        }
        subscriptionContractDetailsService.subscriptionContractUpdateLineItemQuantity(contractId, shop, quantity, lineId, commonUtils.isExternal() ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item-quantity")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemQuantityV2(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "api_key", required = true) String apiKey,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-line-item-quantity api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} Request URL for calling API /external/v2/subscription-contracts-update-line-item-quantity for update subscription of shop : {} with contractId : {} and quantity : {} and lineId : {}", RequestURL, shop, contractId, quantity, lineId);
        subscriptionContractDetailsService.subscriptionContractUpdateLineItemQuantity(contractId, shop, quantity, lineId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-line-item-price")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemPrice(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam("basePrice") Double basePrice,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /subscription-contracts-update-line-item-price for update subscription of shop : {} with contractId : {} and basePrice : {} and lineId : {}", shop, contractId, basePrice, lineId);

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            commonUtils.checkAttributeBasedMinCycles(shop, contractId, lineId);
        }

        subscriptionContractDetailsService.subscriptionContractUpdateLineItemPrice(contractId, shop, basePrice, lineId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);

        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item-price")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemPriceV2(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "api_key", required = true) String apiKey,
        @RequestParam("basePrice") Double basePrice,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-line-item-price api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} Request URL for calling API /external/v2/subscription-contracts-update-line-item-price for update subscription of shop : {} with contractId : {} and basePrice : {} and lineId : {}", RequestURL, shop, contractId, basePrice, lineId);
        subscriptionContractDetailsService.subscriptionContractUpdateLineItemPrice(contractId, shop, basePrice, lineId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-line-item-pricing-policy")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemPricingPolicy(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam("lineId") String lineId,
        @RequestParam("basePrice") Double basePrice,
        @RequestBody(required = false) List<AppstleCycle> cycles
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /subscription-contracts-update-line-item-pricing-policy for update subscription of " +
                "shop : {} with contractId : {} and basePrice : {} and cycles : {} and lineId : {}",
            shop, contractId, basePrice, cycles, lineId);

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
        }

        subscriptionContractDetailsService.subscriptionContractUpdateLineItemPricingPolicy(shop, contractId, lineId,
            basePrice, cycles,
            isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item-pricing-policy")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemPricingPolicyV2(
        HttpServletRequest request,
        @RequestParam(value = "api_key", required = true) String apiKey,
        @RequestParam("contractId") Long contractId,
        @RequestParam("lineId") String lineId,
        @RequestParam("basePrice") Double basePrice,
        @RequestBody(required = false) List<AppstleCycle> cycles
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("Request URL for calling API /external/v2/subscription-contracts-update-line-item-pricing-policy for update subscription of " +
                "shop : {} with contractId : {} and basePrice : {} and cycles : {} and lineId : {}",
            shop, contractId, basePrice, cycles, lineId);


        subscriptionContractDetailsService.subscriptionContractUpdateLineItemPricingPolicy(shop, contractId, lineId,
            basePrice, cycles, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-line-item-selling-plan")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemSellingPlan(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "sellingPlanId", required = false) Long sellingPlanId,
        @RequestParam(value = "sellingPlanName", required = false) String sellingPlanName,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /external/v2/subscription-contracts-update-line-item-selling-plan for update subscription of shop : {} with contractId : {} and sellingPlanId : {} and sellingPlanName : {} and lineId : {}", shop, contractId, sellingPlanId, sellingPlanName, lineId);

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
        }

        if (!ObjectUtils.anyNotNull(sellingPlanId, sellingPlanName)) {
            throw new BadRequestAlertException("Both Selling Plan Id and Selling Plan Name cannot be null", ENTITY_NAME, "sellingPlanRequired");
        }

        subscriptionContractDetailsService.subscriptionContractUpdateLineItemSellingPlan(contractId, shop, sellingPlanId, sellingPlanName, lineId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item-selling-plan")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemSellingPlanV2(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "api_key", required = true) String apiKey,
        @RequestParam(value = "sellingPlanId", required = false) Long sellingPlanId,
        @RequestParam(value = "sellingPlanName", required = false) String sellingPlanName,
        @RequestParam("lineId") String lineId
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-line-item-selling-plan api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} Request URL for calling API /external/v2/subscription-contracts-update-line-item-selling-plan for update subscription of shop : {} with contractId : {} and sellingPlanId : {} and sellingPlanName : {} and lineId : {}", RequestURL, shop, contractId, sellingPlanId, sellingPlanName, lineId);

        if (!ObjectUtils.anyNotNull(sellingPlanId, sellingPlanName)) {
            throw new BadRequestAlertException("Both Selling Plan Id and Selling Plan Name cannot be null", ENTITY_NAME, "sellingPlanRequired");
        }

        subscriptionContractDetailsService.subscriptionContractUpdateLineItemSellingPlan(contractId, shop, sellingPlanId, sellingPlanName, lineId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-freeze-status-detail", "/subscriptions/cp/api/subscription-contracts-freeze-status-detail"})
    public FreezeStatusWrapper subscriptionContractFreezeStatusDetails(@RequestParam("contractId") Long contractId) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /external/v2/subscription-contracts-freeze-status for update subscription of shop : {} with contractId : {}", shop, contractId);
        FreezeStatusWrapper freezeStatusWrapper = new FreezeStatusWrapper();
        try {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            freezeStatusWrapper.setFreezeStatus(Boolean.FALSE);
            freezeStatusWrapper.setErrorMessage("");
            return freezeStatusWrapper;
        } catch (Exception ex) {
            if (ex instanceof BadRequestAlertException) {
                String errorTitle = ((BadRequestAlertException) ex).getTitle();
                freezeStatusWrapper.setErrorMessage(errorTitle);
            }
            freezeStatusWrapper.setFreezeStatus(Boolean.TRUE);
            return freezeStatusWrapper;
        }
    }


    @CrossOrigin
    @PutMapping("/api/subscription-contracts-freeze-status")
    public Boolean subscriptionContractFreezeStatus(@RequestParam("contractId") Long contractId) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Request URL for calling API /external/v2/subscription-contracts-freeze-status for update subscription of shop : {} with contractId : {}", shop, contractId);
        try {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            return false;
        } catch (Exception ex) {
            return true;
        }
    }


    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-line-item")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItem(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "shop", required = false) String shopName,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("variantId") String productVariantId,
        @RequestParam("lineId") String lineId,
        @RequestParam("price") Double price,
        @RequestParam(value = "isPricePerUnit", required = false, defaultValue = "false") boolean isPricePerUnit,
        @RequestParam(value = "sellingPlanName", required = false) String sellingPlanName
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} Request URL for calling API /subscription-contracts-update-line-item for update subscription of shop : {} with contractId : {} and quantity : {} and productVariantId : {} and lineId : {} and price : {}", RequestURL, shopName, contractId, quantity, productVariantId, lineId, price);
        }
        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId, lineId);
        }
        if (price == null) {
            subscriptionContractDetailsService.subscriptionContractUpdateLineItemQuantity(contractId, shopName, quantity, lineId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        } else {
            subscriptionContractDetailsService.subscriptionContractUpdateLineItem(contractId, shopName, quantity, productVariantId, lineId, sellingPlanName, price, isPricePerUnit, RequestURL, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        }
        return getSubscriptionContractRawInternal(contractId, shopName);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item")
    @ApiOperation("Update Product from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemV2(
        HttpServletRequest request,
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Quantity of Product") @RequestParam("quantity") Integer quantity,
        @ApiParam("Variant ID of Product") @RequestParam("variantId") String productVariantId,
        @ApiParam("Line ID") @RequestParam("lineId") String lineId,
        @ApiParam("Price of Product") @RequestParam("price") Double price,
        @ApiParam("Is Price per Unit") @RequestParam(value = "isPricePerUnit", required = false, defaultValue = "false") boolean isPricePerUnit,
        @ApiParam("Selling Plan Name") @RequestParam(value = "sellingPlanName", required = false) String sellingPlanName
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-line-item api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} Request URL for calling API /external/v2/subscription-contracts-update-line-item for update subscription of shop : {} with contractId : {} and quantity : {} and productVariantId : {} and lineId : {} and price : {}", RequestURL, shop, contractId, quantity, productVariantId, lineId, price);

        if (price == null) {
            subscriptionContractDetailsService.subscriptionContractUpdateLineItemQuantity(contractId, shop, quantity, lineId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        } else {
            subscriptionContractDetailsService.subscriptionContractUpdateLineItem(contractId, shop, quantity, productVariantId, lineId, sellingPlanName, price, isPricePerUnit, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        }
        return getSubscriptionContractRawInternal(contractId, shop);
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-line-item-attributes", "/subscriptions/cp/api/subscription-contracts-update-line-item-attributes"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemAttributes(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam("lineId") String lineId,
        @RequestBody List<AttributeInfo> attributeInfoList
    ) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v1 /subscription-contracts-update-line-item RequestURL: {}, shop: {}", RequestURL, shop);

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            commonUtils.checkAttributeBasedMinCycles(shop, contractId, lineId);
        }
        return subscriptionContractUpdateLineItemAttributes(contractId, shop, lineId, attributeInfoList);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-line-item-attributes")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateLineItemAttributesV2(
        HttpServletRequest request,
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "api_key") String apiKey,
        @RequestParam("lineId") String lineId,
        @RequestBody List<AttributeInfo> attributeInfoList
    ) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-line-item api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} Request URL for calling API /external/v2/subscription-contracts-update-line-item for update subscription of shop : {} with contractId : {} and lineId : {}", RequestURL, shop, contractId, lineId);

        return subscriptionContractUpdateLineItemAttributes(contractId, shop, lineId, attributeInfoList);
    }

    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> subscriptionContractUpdateLineItemAttributes(Long contractId, String shop, String lineId, List<AttributeInfo> attributeInfoList) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());

            if (optionalDraftId.isPresent()) {
                String draftId = optionalDraftId.get();
                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                List<AttributeInput> customAttributes = new ArrayList<>();

                for (AttributeInfo attributeInfo : attributeInfoList) {
                    customAttributes.add(AttributeInput.builder().key(attributeInfo.getKey()).value(attributeInfo.getValue()).build());
                }

                SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                    .customAttributes(customAttributes)
                    .build();

                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                if (optionalMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                }

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (optionalDraftCommitResponse.hasErrors()) {
                    throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                }

                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                }
            }

        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-billing-interval", "/subscriptions/cp/api/subscription-contracts-update-billing-interval"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateBillingInterval(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "shop", required = false) String shopName,
        @RequestParam("intervalCount") int billingIntervalCount,
        @RequestParam("interval") SellingPlanInterval billingInterval,
        HttpServletRequest request) throws Exception {
        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} REST request for calling API /subscription-contracts-update-billing-interval to update billing interval of contractId : {} and shop : {} and billingIntervalCount : {} and billingInterval : {}", RequestURL, contractId, shopName, billingIntervalCount, billingInterval);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId);
        }

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shopName, billingIntervalCount, billingInterval, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-billing-interval-by-selling-plan", "/subscriptions/cp/api/subscription-contracts-update-billing-interval-by-selling-plan"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateBillingIntervalBySellingPlan(
        @RequestParam("contractId") Long contractId,
        @RequestParam("sellingPlanId") Long sellingPlanId,
        HttpServletRequest request) throws Exception {
        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shopName = commonUtils.getShop();

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} REST request for calling API /subscription-contracts-update-billing-interval-by-selling-plan to update billing interval of contractId : {} and shop : {} and selling plan id : {}", RequestURL, contractId, shopName, sellingPlanId);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId);
        }

        FrequencyInfoDTO frequencyInfoDTO = subscriptionGroupService.getSellingPlanById(shopName, sellingPlanId);

        if(Objects.nonNull(frequencyInfoDTO)) {
            SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shopName, frequencyInfoDTO.getBillingFrequencyCount(), SellingPlanInterval.safeValueOf(frequencyInfoDTO.getBillingFrequencyInterval().toString()), isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
            subscriptionContract = subscriptionContractDetailsService.updateContractSellingPlanAndPricingPolicy(shopName, subscriptionContract, frequencyInfoDTO);
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
        }else{
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(null));
        }
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-frequency-by-selling-plan", "/subscriptions/cp/api/subscription-contracts-update-frequency-by-selling-plan"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateFrequencyBySellingPlan(
        @RequestParam("contractId") Long contractId,
        @RequestParam("sellingPlanId") Long sellingPlanId,
        HttpServletRequest request) throws Exception {
        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        String shopName = commonUtils.getShop();

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} REST request for calling API /subscription-contracts-update-frequency-by-selling-plan to update frequency of contractId : {} and shop : {} and selling plan id : {}", RequestURL, contractId, shopName, sellingPlanId);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId);
        }

        FrequencyInfoDTO frequencyInfoDTO = subscriptionGroupService.getSellingPlanById(shopName, sellingPlanId);

        if(Objects.nonNull(frequencyInfoDTO)) {
            SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shopName, frequencyInfoDTO.getBillingFrequencyCount(), SellingPlanInterval.safeValueOf(frequencyInfoDTO.getBillingFrequencyInterval().toString()), isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
            subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateDeliveryInterval(contractId, shopName, frequencyInfoDTO.getFrequencyCount(), SellingPlanInterval.safeValueOf(frequencyInfoDTO.getFrequencyInterval().toString()), isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
            subscriptionContract = subscriptionContractDetailsService.updateContractSellingPlanAndPricingPolicy(shopName, subscriptionContract, frequencyInfoDTO);
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
        }else{
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(null));
        }
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-billing-interval")
    @ApiOperation("Update Contract Billing Interval from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateBillingIntervalV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Billing Interval Count") @RequestParam("intervalCount") int billingIntervalCount,
        @ApiParam("Billing Interval Type") @RequestParam("interval") SellingPlanInterval billingInterval,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-billing-interval api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-billing-interval to update billing interval of contractId : {} and shop : {} and billingIntervalCount : {} and billingInterval : {}", RequestURL, contractId, shop, billingIntervalCount, billingInterval);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shop, billingIntervalCount, billingInterval, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    private ZonedDateTime setNextDayOfWeek(ZonedDateTime nextBillingDate, OrderDayOfWeek localOrderDayOfWeek) {
        ZonedDateTime adjustedDate = null;
        switch (localOrderDayOfWeek) {
            case MONDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                break;
            case TUESDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
                break;
            case WEDNESDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
                break;
            case THURSDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
                break;
            case FRIDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
                break;
            case SATURDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
                break;
            case SUNDAY:
                adjustedDate = nextBillingDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
        }
        return adjustedDate;
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-delivery-interval")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateDeliveryInterval(
        @RequestParam("contractId") Long contractId,
        @RequestParam("deliveryIntervalCount") int deliveryIntervalCount,
        @RequestParam("deliveryInterval") SellingPlanInterval deliveryInterval,
        HttpServletRequest request) throws Exception {
        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateDeliveryInterval(contractId, shopName, deliveryIntervalCount, deliveryInterval, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-delivery-interval")
    @ApiOperation("Update Delivery Interval from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateDeliveryIntervalV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Delivery Interval Count") @RequestParam("deliveryIntervalCount") int deliveryIntervalCount,
        @ApiParam("Delivery Interval Type") @RequestParam("deliveryInterval") SellingPlanInterval deliveryInterval,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-delivery-interval api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-delivery-interval to update delivery interval of contractId : {} and shop : {} and deliveryIntervalCount : {} and deliveryInterval : {}", RequestURL, contractId, shop, deliveryIntervalCount, deliveryInterval);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateDeliveryInterval(contractId, shop, deliveryIntervalCount, deliveryInterval, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-min-cycles")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateMinCycles(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "minCycles", required = false) Integer minCycles,
        HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        log.info("Request to update min cycles of contractId : {} and shop : {}", contractId, shopName);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractUpdateMinCycles(contractId, shopName, minCycles, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-min-cycles")
    @ApiOperation("Update Minimum Number of Cycles from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateMinCyclesV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Minimum Number of Orders") @RequestParam(value = "minCycles", required = false) Integer minCycles,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-min-cycles api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request calling API for /external/v2/subscription-contracts-update-min-cycles to update min cycles of contractId : {} and shop : {}", RequestURL, contractId, shop);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractUpdateMinCycles(contractId, shop, minCycles, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }


    @CrossOrigin
    @PutMapping("/api/subscription-contracts-update-max-cycles")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateMaxCycles(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "maxCycles", required = false) Integer maxCycles,
        HttpServletRequest request) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        log.info("Request to update max cycles of contractId : {} and shop : {}", contractId, shopName);

        boolean isExternal = commonUtils.isExternal();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateMaxCycles(contractId, shopName, maxCycles, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-max-cycles")
    @ApiOperation("Update Maximum Number of Order from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateMaxCyclesV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Maximum Number of Orders") @RequestParam(value = "maxCycles", required = false) Integer maxCycles,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-max-cycles api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-max-cycles to update max cycles of contractId : {} and shop : {} and maxCycles : {}", RequestURL, contractId, shop, maxCycles);
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractsUpdateMaxCycles(contractId, shop, maxCycles, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @Autowired
    private ShopInfoService shopInfoService;

    @PutMapping(value = {"/api/subscription-contracts-update-delivery-price", "/api/external/v2/subscription-contracts-update-delivery-price"})
    @CrossOrigin
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateDeliveryPrice(
        @RequestParam("contractId") Long contractId,
        @RequestParam("deliveryPrice") Double deliveryPrice,
        @RequestParam(value = "api_key", required = false) String apiKey,
        HttpServletRequest request) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = null;

        boolean isExternal = false;
        if (StringUtils.isNotBlank(apiKey)) {
            Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
            if (shopInfoOptional.isPresent()) {
                shop = shopInfoOptional.get().getShop();
                isExternal = true;
            }
        }

        if (StringUtils.isBlank(shop)) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        }

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to update delivery price of contractId : {} and shop : {}", RequestURL, contractId, shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        Double oldDeliveryPrice =
            requireNonNull(subscriptionContractQueryResponse.getData())
                .map(d -> d.getSubscriptionContract()
                    .map(SubscriptionContractQuery.SubscriptionContract::getDeliveryPrice)
                    .map(SubscriptionContractQuery.DeliveryPrice::getAmount)
                    .map(Object::toString)
                    .map(Double::parseDouble)
                    .orElse(0.0D))
                .orElse(0.0D);

        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        subscriptionDraftInputBuilder.deliveryPrice(deliveryPrice);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("oldDeliveryPrice", oldDeliveryPrice);
        map.put("newDeliveryPrice", deliveryPrice);
        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL, ActivityLogEventType.MANUAL_DELIVERY_PRICE_UPDATED, ActivityLogStatus.SUCCESS, map);

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }

    /*@PutMapping("/api/subscription-contracts-update-delivery-method")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateDeliveryMethod(
        @RequestParam("contractId") Long contractId,
        @RequestParam("deliveryPrice") Double deliveryPrice,
        HttpServletRequest request) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request for calling API /subscription-contracts-update-delivery-method to update delivery method of contractId : {} and shop : {} and deliveryPrice : {}", RequestURL, contractId, shop, deliveryPrice);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

        SubscriptionDeliveryMethodShippingInput subscriptionDeliveryMethodShippingInput = SubscriptionDeliveryMethodShippingInput.builder().shippingOption(SubscriptionDeliveryMethodShippingOptionInput.builder().carrierServiceId("").build()).build();
        SubscriptionDeliveryMethodInput build = SubscriptionDeliveryMethodInput.builder().shipping(subscriptionDeliveryMethodShippingInput).build();
        subscriptionDraftInputBuilder.deliveryMethod(build);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContract(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

        if (!subscriptionContractUpdateResult.isSuccess()) {
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }

        return getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
    }*/

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @PutMapping("/api/subscription-contracts-update-delivery-method")
    @CrossOrigin
    public ResponseEntity<Void> updateDeliveryMethod(
        @RequestParam("contractId") Long contractId,
        @RequestParam("delivery-method-name") String deliveryMethodName) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = subscriptionContractDetailsService.updateDeliveryMethod(shop, contractId, deliveryMethodName, ActivityLogEventSource.MERCHANT_PORTAL);

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription " + "Delivery Method Updated.", ""))
            .build();
    }

    @PutMapping(value = {"/api/subscription-contracts-update-status", "/subscriptions/cp/api/subscription-contracts-update-status"})
    @CrossOrigin
    public ResponseEntity<String> updateStatus(
        @RequestParam("contractId") Long contractId,
        @RequestParam("status") String status,
        @RequestParam(value = "pauseDurationCycle", required = false) Integer pauseDurationCycle,
        HttpServletRequest request) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        commonUtils.validateCustomerRequestForContract(contractId);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
            commonUtils.checkAttributeBasedMinCycles(shop, contractId);
            if (status.equals("PAUSED") || status.equals("ACTIVE")) {
                commonUtils.checkCustomerPermissions(shop, "pauseResumeSub");
            }
        }

        subscriptionContractDetailsService.subscriptionContractUpdateStatus(contractId, status, shop, pauseDurationCycle, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription " + status + ".", ""))
            .body("{\"status\": \"" + status + "\"}");
    }

    @Autowired
    private PaymentPlanService paymentPlanService;

    @PutMapping("/api/external/v2/subscription-contracts-update-status")
    @CrossOrigin
    @ApiOperation("Update Contract Status")
    public ResponseEntity<Void> updateStatusV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam(value = "Status", example = "ACTIVE, CANCELLED, PAUSED") @RequestParam("status") String status,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-status api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        /*Optional<PaymentPlan> paymentPlanOptional = paymentPlanService.findByShop(shopInfoDTO.get().getShop());
        if (paymentPlanOptional.isPresent()) {
            PaymentPlan paymentPlan = paymentPlanOptional.get();

            String paymentPlanNameLowerCase = Optional.ofNullable(paymentPlan.getPlanInfo()).map(PlanInfo::getName).orElse("UNKNOWN").toLowerCase();

            if (!paymentPlanNameLowerCase.contains("enterprise") && !paymentPlanNameLowerCase.contains("unknown")) {
                throw new BadRequestAlertException("You are not on a valid plan to use external APIs", "", "");
            }
        }*/

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-status to update status of contractId : {} and shop : {} and status : {}", RequestURL, contractId, shop, status);

        subscriptionContractDetailsService.subscriptionContractUpdateStatus(contractId, status, shop, null, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription " + status + ".", ""))
            .build();
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-billing-date", "/subscriptions/cp/api/subscription-contracts-update-billing-date"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateNextBillingDate(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "shop", required = false) String shopName,
        @RequestParam("nextBillingDate") ZonedDateTime nextBillingDate,
        HttpServletRequest request) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("{} REST request for calling API /subscription-contracts-update-billing-dat to update next billing date of contractId : {} and shop : {} and nextBillingDate : {}", RequestURL, contractId, shopName, nextBillingDate);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
            commonUtils.checkAttributeBasedMinCycles(shopName, contractId);
        }
        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractByContractId = subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shopName, nextBillingDate, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);

        HttpHeaders httpHeaders = HeaderUtil.createAlert(applicationName, "Subscription Contract updated successfully.", "");

        log.info("shop=" + shopName + " nextOrderDate=" + nextBillingDate + " contractId=" + contractId);
        return ResponseUtil.wrapOrNotFound(subscriptionContractByContractId, httpHeaders);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-billing-date")
    @ApiOperation("Update Next Billing Date from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateNextBillingDateV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam(value = "Next Billing Date", example = "2022-09-08T03:30:00.000Z in UTC") @RequestParam("nextBillingDate") ZonedDateTime nextBillingDate,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-billing-date api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-billing-dat to update next billing date of contractId : {} and shop : {} and nextBillingDate : {}", RequestURL, contractId, shop, nextBillingDate);

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractByContractId = subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shop, nextBillingDate, ActivityLogEventSource.MERCHANT_EXTERNAL_API);

        HttpHeaders httpHeaders = HeaderUtil.createAlert(applicationName, "Subscription Contract updated successfully.", "");

        log.info("shop=" + shop + " nextOrderDate=" + nextBillingDate + " contractId=" + contractId);
        return ResponseUtil.wrapOrNotFound(subscriptionContractByContractId, httpHeaders);
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-order-note/{contractId}", "/subscriptions/cp/api/subscription-contracts-update-order-note/{contractId}"})
    public ResponseEntity<Boolean> updateOrderNote(
        @PathVariable("contractId") Long contractId,
        @RequestParam("orderNote") String orderNote,
        HttpServletRequest request) throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId).get();

        if (!subscriptionContractDetailsDTO.getShop().equals(shopName)) {
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        return subscriptionContractUpdatedOrderNote(contractId, orderNote);
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-order-note/{contractId}")
    @ApiOperation("Update Order Note from Customer Portal")
    public ResponseEntity<Boolean> updateOrderNoteV2(
        @ApiParam("Contract ID") @PathVariable("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Updated Order Note") @RequestParam("orderNote") String orderNote,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-order-note/{contractId} api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-order-note to update order note of contractId : {} and shop : {} and orderNote : {}", RequestURL, contractId, shop, orderNote);
        return subscriptionContractUpdatedOrderNote(contractId, orderNote);
    }

    private ResponseEntity<Boolean> subscriptionContractUpdatedOrderNote(Long contractId, String orderNote) {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId).get();

        subscriptionContractDetailsDTO.setOrderNote(orderNote);

        subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

        HttpHeaders httpHeaders = HeaderUtil.createAlert(applicationName, "Order Note updated successfully.", "");
        return ResponseUtil.wrapOrNotFound(Optional.of(true), httpHeaders);
    }


    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contracts-update-shipping-address", "/subscriptions/cp/api/subscription-contracts-update-shipping-address"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateShippingAddress(
        @RequestParam("contractId") Long contractId,
        @RequestBody ChangeShippingAddressVM changeShippingAddressVM,
        HttpServletRequest request) throws Exception {

        boolean isExternal = commonUtils.isExternal();
        commonUtils.validateCustomerRequestForContract(contractId);

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractUpdateShippingAddress(contractId, shopName, changeShippingAddressVM, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> subscriptionContractUpdateShippingAddressAll(Long contractId, String shop, ChangeShippingAddressVM changeShippingAddressVM, ActivityLogEventSource eventSource) throws Exception {
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractUpdateShippingAddress(contractId, shop, changeShippingAddressVM, eventSource);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    @CrossOrigin
    @PutMapping("/api/external/v2/subscription-contracts-update-shipping-address")
    @ApiOperation("Update Shipping Address from Customer Portal")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> updateShippingAddressV2(
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Shipping Address View Model") @RequestBody ChangeShippingAddressVM changeShippingAddressVM,
        HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-update-shipping-address api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-update-shipping-address to update shipping address of contractId : {} and shop : {} and changeShippingAddressVM : {}", RequestURL, contractId, shop, changeShippingAddressVM);
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.subscriptionContractUpdateShippingAddress(contractId, shop, changeShippingAddressVM, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subscriptionContract));
    }

    /**
     * {@code GET  /customers} : get customerData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the customerData in body.
     */
    @GetMapping("/api/subscription-customers")
    public SubscriptionCustomerData getAllSubscriptionCustomers(@RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                                                @RequestParam(value = "cursor", required = false) String cursor) throws Exception {
        log.debug("REST request to get subscription contract customers");
        return subscriptionCustomerService.findAllSubscriptionContractCustomers(commonUtils.getShop(), next, cursor);
    }

    /**
     * {@code GET  /subscription-customers/:id} : get the "id" SubscriptionCustomer.
     *
     * @param id the id of the SubscriptionGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SubscriptionGroup, or with status {@code 404 (Not Found)}.
     */

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    //TODO: FIX Security Issue
    @GetMapping("/subscriptions/cp/api/subscription-customers/{id}")
    @CrossOrigin
    public SubscriptionContactCustomerWithCursorQuery.Customer getSubscriptionCustomer(@PathVariable String id,
                                                                                       @RequestParam(value = "shop", required = false) String shop,
                                                                                       @RequestParam(value = "cursor", required = false) String cursor, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");


        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
            Optional<String> optionallCustomerToken = commonUtils.getCustomerToken();
            boolean isExternal = commonUtils.isExternal();
            log.info("isExternal=" + isExternal + " token=" + optionallCustomerToken.orElse("") + " shop=" + shop);
        } else {
            log.info("REST request v1 /subscription-customers/{id} RequestURL: {}, shop: {}", RequestURL, shop);
            throw new BadRequestAlertException("", "", "");
        }

        return getSubscriptionCustomerByShop(id, shop, cursor);
    }

    @GetMapping("/api/subscription-customers/{id}")
    public SubscriptionContactCustomerWithCursorQuery.Customer getSubscriptionCustomerForMp(@PathVariable String id, @RequestParam(value = "shop", required = false) String shop, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-customers/{id} RequestURL: {}, shop: {}", RequestURL, shop);
            throw new BadRequestAlertException("", "", "");
        }

        return getSubscriptionCustomerByShopForMP(id, shop);
    }

    @GetMapping("/api/external/v2/subscription-customers/{id}")
    @CrossOrigin
    @ApiOperation("Get Customer Details")
    public SubscriptionContactCustomerWithCursorQuery.Customer getSubscriptionCustomerV2(@ApiParam("Customer Id") @PathVariable String id, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, @ApiParam("Cursor") @RequestParam(value = "cursor", required = false) String cursor,  HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-customers/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-customers to get customer details for customerID: {} and shop : {}", RequestURL, id, shop);
        return getSubscriptionCustomerByShop(id, shop, cursor);
    }

    private SubscriptionContactCustomerWithCursorQuery.Customer getSubscriptionCustomerByShop(String id, String shop, String cursor) throws Exception {
        SubscriptionContactCustomerWithCursorQuery.Customer singleSubscriptionCustomer = null;
        if (NumberUtils.isCreatable(id)) {
            singleSubscriptionCustomer = subscriptionCustomerService.findSingleSubscriptionCustomer(shop, Long.parseLong(id), cursor);
        } else {
            Optional<Long> customerIdByCustomerUid = customerPaymentRepository.findCustomerIdByCustomerUid(id);
            commonUtils.throwExceptionIfCustomerIdNotFound(customerIdByCustomerUid);
            singleSubscriptionCustomer = subscriptionCustomerService.findSingleSubscriptionCustomer(shop, customerIdByCustomerUid.get(), cursor);
        }
        return singleSubscriptionCustomer;
    }

    private SubscriptionContactCustomerWithCursorQuery.Customer getSubscriptionCustomerByShopForMP(String id, String shop) throws Exception {
        SubscriptionContactCustomerWithCursorQuery.Customer singleSubscriptionCustomer = null;
        if (NumberUtils.isCreatable(id)) {
            singleSubscriptionCustomer = subscriptionCustomerService.findSingleSubscriptionCustomerForMP(shop, Long.parseLong(id));
        } else {
            Optional<Long> customerIdByCustomerUid = customerPaymentRepository.findCustomerIdByCustomerUid(id);
            commonUtils.throwExceptionIfCustomerIdNotFound(customerIdByCustomerUid);
            singleSubscriptionCustomer = subscriptionCustomerService.findSingleSubscriptionCustomerForMP(shop, customerIdByCustomerUid.get());
        }
        return singleSubscriptionCustomer;
    }

    @GetMapping("/api/subscription-customers/sync-info/{customerId}")
    public void syncCustomerInfo(@PathVariable Long customerId) throws Exception {
        log.debug("REST request to sync subscription contract customer info from shopify: {}", customerId);

        String shop = null;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-customers/sync-info/{customerId}, shop: {}", shop);
            throw new BadRequestAlertException("", "", "");
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CustomerBriefQuery customerBriefQuery = new CustomerBriefQuery(CUSTOMER_ID_PREFIX + customerId);
        Response<Optional<CustomerBriefQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerBriefQuery);

        if (response.getData().isPresent() && response.getData().get().getCustomer().isPresent()) {
            CustomerBriefQuery.Customer customer = response.getData().get().getCustomer().get();

            subscriptionContractDetailsRepository.updateCustomerInfo(
                customerId,
                customer.getEmail().orElse(null),
                customer.getDisplayName(),
                customer.getFirstName().orElse(null),
                customer.getLastName().orElse(null),
                customer.getPhone().orElse(null));
        }
    }


    @GetMapping("/api/external/v2/subscription-customers/sync-info/{customerId}")
    @CrossOrigin
    @ApiOperation("Sync customer details from Shopify")
    public void syncCustomerInfo(@ApiParam("Customer Id") @PathVariable Long customerId,
                                 @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                 HttpServletRequest request) throws Exception {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-customers/sync-info/{customerId} api_key: {}", RequestURL, apiKey);

        String shop = null;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v2 /subscription-customers/sync-info/{customerId}, shop: {}", shop);
            throw new BadRequestAlertException("", "", "");
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CustomerBriefQuery customerBriefQuery = new CustomerBriefQuery(CUSTOMER_ID_PREFIX + customerId);
        Response<Optional<CustomerBriefQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerBriefQuery);

        if (response.getData().isPresent() && response.getData().get().getCustomer().isPresent()) {
            CustomerBriefQuery.Customer customer = response.getData().get().getCustomer().get();

            subscriptionContractDetailsRepository.updateCustomerInfo(
                customerId,
                customer.getEmail().orElse(null),
                customer.getDisplayName(),
                customer.getFirstName().orElse(null),
                customer.getLastName().orElse(null),
                customer.getPhone().orElse(null));
        }
    }


    @PostMapping("/api/subscription-customers/update-customer-info")
    public ResponseEntity<Void> updateCustomerInfo(@RequestBody CustomerInfoVM customerInfoVM) throws Exception {
        String shop = null;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-customers/sync-info/{customerId}, shop: {}", shop);
            throw new BadRequestAlertException("", "", "");
        }
        subscriptionContractDetailsRepository.updateCustomerInfo(
            customerInfoVM.getCustomerId(),
            customerInfoVM.getCustomerEmail(),
            customerInfoVM.getCustomerFirstName() + " " + customerInfoVM.getCustomerLastName(),
            customerInfoVM.getCustomerFirstName(),
            customerInfoVM.getCustomerLastName());

        /*ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        Customer customer = new Customer();
        customer.setId(customerInfoVM.getCustomerId());
        customer.setFirstName(customerInfoVM.getCustomerFirstName());
        customer.setLastName(customerInfoVM.getCustomerLastName());
        customer.setEmail(customerInfoVM.getCustomerEmail());
        updateCustomerRequest.setCustomer(customer);
        api.updateCustomer(updateCustomerRequest);*/
        return ResponseEntity.created(new URI("/api/subscription-customers/update-customer-info"))
            .headers(HeaderUtil.createAlert(applicationName, "Customer information updated.", ""))
            .body(null);
    }

    //TODO: FIX Security Issue
    @GetMapping(value = {"/api/subscription-customers/valid/{id}", "/subscriptions/cp/api/subscription-customers/valid/{id}"})
    @CrossOrigin
    public Set<Long> getValidSubscriptionCustomer(@PathVariable String id, @RequestParam(value = "shop", required = false) String shop, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shop = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-customers/valid/{id} RequestURL: {}, shop: {}", RequestURL, shop);
            throw new BadRequestAlertException("", "", "");
        }

        return validSubscriptionCustomer(id, shop);
    }

    @GetMapping("/api/external/v2/subscription-customers/valid/{id}")
    @CrossOrigin
    @ApiOperation("Get Valid Contracts")
    public Set<Long> getValidSubscriptionCustomerV2(@ApiParam("Id") @PathVariable String id, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-customers/valid/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-customers/valid to get valid contract for id: {} and shop : {}", RequestURL, id, shop);
        return validSubscriptionCustomer(id, shop);
    }

    @NotNull
    private Set<Long> validSubscriptionCustomer(String id, String shop) {
        if (NumberUtils.isCreatable(id)) {
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findByShopAndCustomerId(shop, Long.parseLong(id));
            return subscriptionContractDetailsDTOS.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toSet());
        } else {
            Optional<Long> customerIdByCustomerUid = customerPaymentRepository.findCustomerIdByCustomerUid(id);
            commonUtils.throwExceptionIfCustomerIdNotFound(customerIdByCustomerUid);
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findByShopAndCustomerId(shop, customerIdByCustomerUid.get());
            return subscriptionContractDetailsDTOS.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toSet());
        }
    }

    @GetMapping(value = {"/api/subscription-customers-detail/valid/{id}", "/subscriptions/cp/api/subscription-customers-detail/valid/{id}"})
    @CrossOrigin
    public List<SubscriptionContractDetailsDTO> getValidSubscriptionCustomerDetails(@PathVariable String id, HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return vaildSubscriptionDetail(id, shop);
    }

    @GetMapping("/api/external/v2/subscription-customers-detail/valid/{id}")
    @CrossOrigin
    @ApiOperation("Get Customer Details")
    public List<SubscriptionContractDetailsDTO> getValidSubscriptionCustomerDetailsV2(@ApiParam("Id") @PathVariable String id, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {
        log.debug("REST request to get single subscription contract customer : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-customers-detail/valid/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-customers-details/valid to get customer details for customerID: {} and shop : {}", RequestURL, id, shop);
        return vaildSubscriptionDetail(id, shop);
    }

    private List<SubscriptionContractDetailsDTO> vaildSubscriptionDetail(String id, String shop) {
        if (NumberUtils.isCreatable(id)) {
            long customerId = Long.parseLong(id);
            return subscriptionContractDetailsService.findByShopAndCustomerId(shop, customerId);
        } else {
            Optional<Long> customerIdByCustomerUid = customerPaymentRepository.findCustomerIdByCustomerUid(id);
            commonUtils.throwExceptionIfCustomerIdNotFound(customerIdByCustomerUid);
            Long customerId = customerIdByCustomerUid.get();
            return subscriptionContractDetailsService.findByShopAndCustomerId(shop, customerId);
        }
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contracts/contract-external/{contractId}", "/subscriptions/cp/api/subscription-contracts/contract-external/{contractId}"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> getSubscriptionContractRawExternal(@PathVariable Long contractId, @RequestParam(value = "shop", required = false) String shopName, HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /subscription-contracts/contract-external/{contractId} RequestURL: {}, shop: {}", RequestURL, shopName);
            throw new BadRequestAlertException("", "", "");
        }

        commonUtils.validateCustomerRequestForContract(contractId);

        return getSubscriptionContractRawInternal(contractId, shopName, commonUtils.prepareShopifyGraphqlClient(shopName));
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-contracts/contract-external/{contractId}")
    @ApiOperation("Get Subscription Contract Details")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> getSubscriptionContractRawExternalV2(@ApiParam("Contract ID") @PathVariable Long contractId, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts/contract-external/{contractId} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts/contract-external to get contract details for contractId: {} and shop : {}", RequestURL, contractId, shop);
        return getSubscriptionContractRawInternal(contractId, shop, commonUtils.prepareShopifyGraphqlClient(shop));
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contract-details/customer/{contractId}", "/subscriptions/cp/api/subscription-contract-details/customer/{contractId}"})
    public ResponseEntity<SubscriptionContractDetailsDTO> getSubscriptionOrderNote(@PathVariable Long contractId, HttpServletRequest request) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);
        return ResponseUtil.wrapOrNotFound(subscriptionContractDetailsDTO);
    }

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @GetMapping("/api/manage-subscription-link/{id}")
    public TokenDetails getManageSubscriptionLink(@PathVariable String id) throws Exception {
        log.debug("REST request to get single manage subscription link for customer : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return getManageSubscriptionLink(shop, id);
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/manage-subscription-link/{customerId}")
    @ApiOperation("Get Manage Subscription link for given customer")
    public TokenDetails getManageSubscriptionLink(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                  @ApiParam("Customer ID") @PathVariable String customerId, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/manage-subscription-link/{customerId} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return getManageSubscriptionLink(shop, customerId);
    }

    private TokenDetails getManageSubscriptionLink(String shop, String customerId) {
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        Optional<CustomerPayment> customerPaymentOptional = customerPaymentRepository.findTop1ByCustomerIdAndShop(Long.parseLong(customerId), shop);

        if (customerPaymentOptional.isEmpty()) {
            CustomerPaymentDTO customerPaymentInfo = new CustomerPaymentDTO();
            customerPaymentInfo.setShop(shop);
            customerPaymentInfo.setCustomerId(Long.parseLong(customerId));
            customerPaymentInfo.setAdminGraphqlApiCustomerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId);
            customerPaymentInfo.setCustomerUid(CommonUtils.generateRandomUid());
            customerPaymentInfo.setTokenCreatedTime(ZonedDateTime.now());
            customerPaymentInfo.setCardExpiryNotificationCounter(0L);

            customerPaymentService.save(customerPaymentInfo);
            customerPaymentOptional = customerPaymentRepository.findTop1ByCustomerId(Long.parseLong(customerId));
        }

        if (customerPaymentOptional.get().getTokenCreatedTime() == null) {
            customerPaymentOptional.get().setTokenCreatedTime(ZonedDateTime.now());
            customerPaymentService.save(customerPaymentOptional.get());
        }

        String customerToken = customerPaymentOptional.get().getCustomerUid();
        ZonedDateTime tokenCreatedTime = customerPaymentOptional.get().getTokenCreatedTime();

        ZonedDateTime tokenExpirationTime = tokenCreatedTime.plusDays(7L);

        String manageSubscriptionLink = "https://" + shopInfo.getPublicDomain() + "/" + shopInfoUtils.getManageSubscriptionUrl(shop) + "/cp/" + customerToken;

        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setManageSubscriptionLink(manageSubscriptionLink);
        tokenDetails.setTokenExpirationTime(tokenExpirationTime);

        return tokenDetails;
    }

    @GetMapping("/api/subscription-contract-details/by-contract-id/{contractId}")
    public ResponseEntity<SubscriptionContractDetailsDTO> getSubscriptionContractDetails(@PathVariable String contractId) {
        log.debug("REST request to get SubscriptionContractDetails by contractId : {}", contractId);
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(Long.parseLong(contractId));
        return ResponseUtil.wrapOrNotFound(subscriptionContractDetailsDTO);
    }

    @GetMapping("/api/subscription-contract-details/export/all")
    public ResponseEntity<Void> exportToCSV(@RequestParam(value = "emailId", required = true) String emailId,
                                            @RequestParam(name = "fromCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromCreatedDate,
                                            @RequestParam(name = "toCreatedDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toCreatedDate,
                                            @RequestParam(name = "fromNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime fromNextDate,
                                            @RequestParam(name = "toNextDate", required = false) @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) ZonedDateTime toNextDate,
                                            @RequestParam(name = "subscriptionContractId", required = false) String subscriptionContractId,
                                            @RequestParam(name = "customerName", required = false) String customerName,
                                            @RequestParam(name = "orderName", required = false) String orderName,
                                            @RequestParam(name = "status", required = false) String status,
                                            @RequestParam(name = "billingPolicyIntervalCount", required = false) Integer billingPolicyIntervalCount,
                                            @RequestParam(name = "billingPolicyInterval", required = false) String billingPolicyInterval,
                                            @RequestParam(name = "planType", required = false) String planType,
                                            @RequestParam(name = "recordType", required = false) String recordType,
                                            @RequestParam(name = "productId", required = false) Long productId,
                                            @RequestParam(name = "variantId", required = false) Long variantId,
                                            @RequestParam(name = "sellingPlanIds", required = false) String sellingPlanIds,
                                            @RequestParam(name = "minOrderAmount", required = false) Double minOrderAmount,
                                            @RequestParam(name = "maxOrderAmount", required = false) Double maxOrderAmount
    ) throws IOException {
        log.debug("REST request to get SubscriptionContractDetails export : {}");
        System.out.println("Email == " + emailId);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<User> user = userRepository.findOneByLogin(shop);

        int pageSize = 1000;
        int pageNumber = 0;

        List<String> gqlSellingPlanIds = new ArrayList<>();
        if(StringUtils.isNotBlank(sellingPlanIds)) {
            gqlSellingPlanIds = Arrays.stream(sellingPlanIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(ShopifyGraphQLUtils::getGraphQLSellingPlanId).collect(Collectors.toList());
        }

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.EXPORT);
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());
        if (BooleanUtils.isNotTrue(bulkAutomationDTO.isRunning())) {
            ExportInputRequest exportInputRequest = new ExportInputRequest(shop, emailId,
                fromCreatedDate, toCreatedDate,
                fromNextDate, toNextDate,
                subscriptionContractId, customerName,
                orderName, status,
                billingPolicyIntervalCount, billingPolicyInterval,
                planType, recordType,
                productId, variantId, gqlSellingPlanIds, minOrderAmount, maxOrderAmount,
                pageSize, pageNumber);
            //CompletableFuture.runAsync(() -> subscriptionContractDetailsService.exportSubscriptionContracts(exportInputRequest));
            awsUtils.startExportExecution(exportInputRequest);

            OBJECT_MAPPER.registerModule(new JavaTimeModule());

            bulkAutomationDTO.setShop(shop);
            bulkAutomationDTO.setAutomationType(BulkAutomationType.EXPORT);
            bulkAutomationDTO.setRunning(true);
            bulkAutomationDTO.setStartTime(ZonedDateTime.now());
            bulkAutomationDTO.setEndTime(null);
            bulkAutomationDTO.setRequestInfo(OBJECT_MAPPER.writeValueAsString(exportInputRequest));
            bulkAutomationDTO.setErrorInfo(null);
            bulkAutomationService.save(bulkAutomationDTO);

            return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + user.get().getEmail(), "")).build();
        } else {
            throw new BadRequestAlertException("An export is already in process for current shop.", applicationName, "");
        }
    }

    @GetMapping("/api/subscription-contract-details/analytics/{contractId}")
    public ResponseEntity<ContractAnalytics> getSubscriptionContractAnalyticsByContractId(@PathVariable String contractId) {
        log.debug("REST request to get SubscriptionContract Analytics by contractId : {}", contractId);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<ContractAnalytics> contractAnalytics = subscriptionBillingAttemptRepository.findTotalOrderAndTotalRevenueByContractId(Long.parseLong(contractId));

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());
        String formattedTotalOrderRevenue = currencyFormattingUtils.formatPrice(Optional.ofNullable(contractAnalytics.get().getTotalOrderAmount()).orElse(0.00));
        contractAnalytics.get().setTotalOrderRevenue(formattedTotalOrderRevenue);

        return ResponseUtil.wrapOrNotFound(contractAnalytics);
    }

    @GetMapping("/api/subscription-contract-details/productDelivery-analytics")
    public List<ProductDeliveryAnalytics> getProductDeliveryAnalytics(@RequestParam String filterBy,
                                                                      @RequestParam Long days,
                                                                      @RequestParam ZonedDateTime fromDay,
                                                                      @RequestParam ZonedDateTime toDay) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get ProductDeliveryAnalytics for shop : {}", shop);

        List<ProductDeliveryAnalytics> productDeliveryAnalyticsList = subscriptionContractDetailsService.getProductDeliveryAnalytics(shop);
        return productDeliveryAnalyticsList;
    }

    @GetMapping("/api/subscription-contract-details/export-delivery-forecast")
    public ResponseEntity<Void> productDeliveryAnalyticsExportToCsv(@RequestParam(value = "emailId", required = true) String email) {
        String shop = commonUtils.getShop();
        log.debug("REST request to export ProductDeliveryAnalytics for shop : {}", shop);

        CompletableFuture.runAsync(() -> subscriptionContractDetailsService.exportProductDeliveryAnalytics(email, shop));

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + email, "")).build();
    }

    @GetMapping("/api/subscription-contract-details/productRevenue-analytics")
    public List<ProductRevenueAnalytics> getProductRevenueAnalytics(@RequestParam String filterBy,
                                                                    @RequestParam Long days,
                                                                    @RequestParam ZonedDateTime fromDay,
                                                                    @RequestParam ZonedDateTime toDay) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get ProductRevenueAnalytics for shop : {}", shop);

        List<ProductRevenueAnalytics> productRevenueAnalyticsList = null;
        try {
            productRevenueAnalyticsList = subscriptionContractDetailsService.getProductRevenueAnalytics(shop, filterBy, days, fromDay, toDay);
        } catch (Exception ex) {
            productRevenueAnalyticsList = new ArrayList<>();
        }
        return productRevenueAnalyticsList;
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contract-details/subscription-fulfillments/{contractId}", "/subscriptions/cp/api/subscription-contract-details/subscription-fulfillments/{contractId}"})
    public ResponseEntity<OrderFulfillmentsQuery.Order> getSubscriptionFulfillments(@PathVariable Long contractId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get ProductDeliveryAnalytics for shop : {}", shop);

        return getSubscriptionFulfillments(contractId, shop);
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-contract-details/subscription-fulfillments/{contractId}")
    @ApiOperation("Get Subscription Fulfillment from Customer Portal")
    public ResponseEntity<OrderFulfillmentsQuery.Order> getSubscriptionFulfillmentsExternalV2(@ApiParam("Contract ID") @PathVariable Long contractId,
                                                                                              @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts/contract-external/{contractId} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contract-details/subscription-fulfillments to get subscription fulfillment for contractId: {} and shop : {}", RequestURL, contractId, shop);
        return getSubscriptionFulfillments(contractId, shop);
    }

    private ResponseEntity<OrderFulfillmentsQuery.Order> getSubscriptionFulfillments(Long contractId, String shop) throws Exception {
        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId).get();
        List<SubscriptionBillingAttempt> successfulBillingAttempts = subscriptionBillingAttemptRepository.findByContractIdAndStatusAndShop(contractId, BillingAttemptStatus.SUCCESS, shop);

        Optional<SubscriptionBillingAttempt> lastSuccessfulBillingAttemptOptional = successfulBillingAttempts.stream().sorted((o1, o2) -> o2.getBillingDate().compareTo(o1.getBillingDate())).findFirst();

        String orderId = lastSuccessfulBillingAttemptOptional.map(SubscriptionBillingAttempt::getGraphOrderId).orElse(subscriptionContractDetails.getGraphOrderId());

        if (orderId == null) {
            return null;
        }

        Optional<OrderFulfillmentsQuery.Order> orderOptional = getOrderFulfillments(shop, ShopifyGraphQLUtils.getOrderId(orderId));

        ResponseEntity<OrderFulfillmentsQuery.Order> orderResponseEntity = ResponseUtil.wrapOrNotFound(orderOptional);

        return orderResponseEntity;
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contract-details/subscription-fulfillments/order/{orderId}", "/subscriptions/cp/api/subscription-contract-details/subscription-fulfillments/order/{orderId}"})
    public ResponseEntity<OrderFulfillmentsQuery.Order> getSubscriptionFulfillmentsForOrder(@PathVariable Long orderId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get ProductDeliveryAnalytics for shop : {}", shop);

        Optional<OrderFulfillmentsQuery.Order> orderOptional = getOrderFulfillments(shop, orderId);

        return ResponseUtil.wrapOrNotFound(orderOptional);
    }

    private Optional<OrderFulfillmentsQuery.Order> getOrderFulfillments(String shop, Long orderId) throws Exception {
        if (orderId == null) {
            return Optional.empty();
        }

        OrderFulfillmentsQuery orderFulfillmentsQuery = new OrderFulfillmentsQuery(ShopifyGraphQLUtils.getGraphQLOrderId(orderId), Input.optional(null));
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<OrderFulfillmentsQuery.Data>> orderFulfillmentsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderFulfillmentsQuery);
        Optional<OrderFulfillmentsQuery.Order> orderOptional = orderFulfillmentsQueryResponse.getData().flatMap(OrderFulfillmentsQuery.Data::getOrder);

        return orderOptional;
    }

    @CrossOrigin
    @PutMapping(value = {"/api/subscription-contract-details/subscription-fulfillment/reschedule", "/subscriptions/cp/api/subscription-contract-details/subscription-fulfillment/reschedule"})
    public ResponseEntity<Void> rescheduleOrderFulfillment(@RequestParam String fulfillmentId, @RequestParam ZonedDateTime deliveryDate) throws Exception {
        String shop = commonUtils.getShop();
        log.debug("REST request to Reschedule order fulfillment for shop : {}", shop);

        subscriptionContractDetailsService.rescheduleOrderFulfillment(shop, fulfillmentId, deliveryDate);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-contract-details/billing-interval")
    @ApiOperation("Get Subscription Contract Billing Interval")
    public List<FrequencyInfoDTO> getSubscriptionContractBillingInterval(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                         @ApiParam("Selling Plan ID") @RequestParam(value = "sellingPlanIds", required = true) String sellingPlanIds,
                                                                         HttpServletRequest request) {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contract-details/billing-interval api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contract-details/billing-interval to get customer details for sellingPlan: {} and shop : {}", RequestURL, sellingPlanIds, shop);
        List<FrequencyInfoDTO> frequencyInfoDTOList = new ArrayList<>();
        Set<String> sellingPlanIdsArray = Arrays
            .stream(StringUtils.split(Optional.ofNullable(sellingPlanIds).orElse(StringUtils.EMPTY), ","))
            .map(String::trim).map(s -> s.equals("") ? "null" : s)
            .collect(Collectors.toSet());

        List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);

        for (SubscriptionGroupPlanDTO subscriptionGroupPlanDTO : subscriptionGroupPlanDTOS) {
            SubscriptionGroupV2DTO subscriptionGroupV2DTO = getSubscribeInfoFromJSON(subscriptionGroupPlanDTO.getInfoJson());

            for (FrequencyInfoDTO subscriptionPlan : subscriptionGroupV2DTO.getSubscriptionPlans()) {
                if (sellingPlanIdsArray.contains(subscriptionPlan.getId())) {
                    frequencyInfoDTOList.addAll(subscriptionGroupV2DTO.getSubscriptionPlans());
                    break;
                }
            }
        }
        return frequencyInfoDTOList;
    }

    private SubscriptionGroupV2DTO getSubscribeInfoFromJSON(String subscribedInfoJson) {
        SubscriptionGroupV2DTO toReturn = CommonUtils.fromJSONIgnoreUnknownProperty(
            new TypeReference<>() {
            },
            subscribedInfoJson
        );
        return Optional.ofNullable(toReturn).orElse(new SubscriptionGroupV2DTO());
    }


    @Autowired
    private SubscriptionContractDetailsQueryService subscriptionContractDetailsQueryService;

    /*@GetMapping("/api/subscription-contract-details-with-filter")
    public ResponseEntity<List<SubscriptionContractDetailsDTO>> getAllSubscriptionContractDetails(SubscriptionContractDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SubscriptionContractDetails by criteria: {}", criteria);
        Page<SubscriptionContractDetailsDTO> page = subscriptionContractDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    @GetMapping("/api/subscription-contract-details/resend-email")
    public ResponseEntity resendEmail(@RequestParam("contractId") Long contractId, @RequestParam("emailSettingType") EmailSettingType emailSettingType) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);

        Optional<EmailTemplateSetting> emailTemplateSetting = emailTemplateSettingRepository.findByShopAndEmailSettingType(shop, emailSettingType);

        if (subscriptionContractDetails.isPresent() && emailTemplateSetting.isPresent()) {

            if (emailTemplateSetting.get().isSendEmailDisabled()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sending Emails for this type is disabled");
            }

            try {
                commonEmailUtils.sendSubscriptionUpdateEmail(
                    commonUtils.prepareShopifyGraphqlClient(shop),
                    commonUtils.prepareShopifyResClient(shop),
                    subscriptionContractDetails.get(),
                    emailSettingType
                );
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body("Email triggered successfully.");
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contracts-email-magic-link", "/subscriptions/cp/api/subscription-contracts-email-magic-link"})
    public ResponseEntity<String> emailMagicLink(@RequestParam("shop") String shop, @RequestParam("email") String email) throws Exception {

        sendMagicLinkEmail(shop, email);

        return ResponseEntity.ok().body("Email triggered successfully.");
    }

    @GetMapping("/api/external/v2/subscription-contracts-email-magic-link")
    @CrossOrigin
    @ApiOperation("Send magic link email to customer")
    public ResponseEntity<String> emailMagicLinkV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                   @ApiParam("Customer email") @RequestParam("email") String email, HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contracts-email-magic-link api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.info("{} REST request for calling API /external/v2/subscription-contracts-email-magic-link to send magic link email to customer: {} and shop : {}", RequestURL, email, shop);
        sendMagicLinkEmail(shop, email);
        return ResponseEntity.ok().body("Email triggered successfully.");
    }

    private void sendMagicLinkEmail(String shop, String email) throws Exception {

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndCustomerEmail(shop, email.trim());

        if (CollectionUtils.isEmpty(subscriptionContractDetailsList)) {
            throw new BadRequestAlertException("Customer Email does not exist", "", "invalidCustomer");
        }

        subscriptionContractDetailsList.sort(Comparator.comparing(SubscriptionContractDetails::getStatus));

        Optional<EmailTemplateSetting> emailTemplateSetting = emailTemplateSettingRepository.findByShopAndEmailSettingType(shop, EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK);

        if (emailTemplateSetting.isEmpty()) {
            throw new BadRequestAlertException("Email template not found", "", "");
        }

        if (emailTemplateSetting.get().isSendEmailDisabled()) {
            throw new BadRequestAlertException("Sending Emails for this type is disabled", "", "");
        }

        commonEmailUtils.sendSubscriptionUpdateEmail(
            commonUtils.prepareShopifyGraphqlClient(shop),
            commonUtils.prepareShopifyResClient(shop),
            subscriptionContractDetailsList.get(0),
            EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK
        );
    }

    @PostMapping("/api/subscription-contract-details/create-subscription-contract")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> createSubscriptionContract(@RequestBody SubscriptionContractDTO subscriptionContractDTO) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.debug("REST request to create Subscription Contract for shop : {}", shop);

        SubscriptionContractQuery.SubscriptionContract createdSubscriptionContract = createSubscriptionContract(shop, subscriptionContractDTO);

        return ResponseEntity.created(new URI("/api/subscription-contract-details/" + createdSubscriptionContract.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Subscription contract created successfully.", ENTITY_NAME))
            .body(createdSubscriptionContract);
    }

    @PostMapping("/api/external/v2/subscription-contract-details/create-subscription-contract")
    @ApiOperation("Create subscription contract")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> createSubscriptionContractV2(@ApiParam("Your API Key") @RequestParam(value = "api_key") String apiKey,
                                                                                                       @RequestBody SubscriptionContractDTO subscriptionContractDTO) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        log.debug("/external/v2/subscription-contract-details/create-subscription-contract request to create Subscription Contract for shop : {}", shop);

        SubscriptionContractQuery.SubscriptionContract createdSubscriptionContract = createSubscriptionContract(shop, subscriptionContractDTO);

        return ResponseEntity.created(new URI("/api/subscription-contract-details/" + createdSubscriptionContract.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Subscription contract created successfully.", ENTITY_NAME))
            .body(createdSubscriptionContract);
    }

    private SubscriptionContractQuery.SubscriptionContract createSubscriptionContract(String shop, SubscriptionContractDTO subscriptionContractDTO) {
        try {

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            SubscriptionContractCreateInput.Builder subscriptionContractCreateInputBuilder = SubscriptionContractCreateInput.builder();

            String customerId = ShopifyGraphQLUtils.getGraphQLCustomerId(subscriptionContractDTO.getCustomerId());
            subscriptionContractCreateInputBuilder.customerId(customerId);

            ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
            CurrencyCode currencyCode = StringUtils.isNotBlank(subscriptionContractDTO.getCurrencyCode()) ? CurrencyCode.valueOf(subscriptionContractDTO.getCurrencyCode()) : CurrencyCode.valueOf(shopInfoDTO.getCurrency());
            subscriptionContractCreateInputBuilder.currencyCode(currencyCode);

            ZonedDateTime nextBillingDate = subscriptionContractDTO.getNextBillingDate();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
            String formattedNextBillingDate = dateTimeFormatter.format(nextBillingDate);
            subscriptionContractCreateInputBuilder.nextBillingDate(formattedNextBillingDate);

            SubscriptionDraftInput.Builder contractBuilder = SubscriptionDraftInput.builder();

            String paymentMethodId = ShopifyGraphQLUtils.getGraphQLCustomerPaymentMethodId(subscriptionContractDTO.getPaymentMethodId());

            if (StringUtils.isBlank(paymentMethodId)) {
                List<String> paymentMethodIds = getPaymentMethodList(customerId, shopifyGraphqlClient);

                if (paymentMethodIds.size() == 0) {
                    throw new BadRequestAlertException("No Payment methods found for the customer", ENTITY_NAME, "createSubscriptionContractError");
                } else if (paymentMethodIds.size() > 1) {
                    throw new BadRequestAlertException("Multiple Payment methods found for the customer", ENTITY_NAME, "createSubscriptionContractError");
                }

                contractBuilder.paymentMethodId(paymentMethodIds.get(0));
            } else {
                contractBuilder.paymentMethodId(paymentMethodId);
            }

            contractBuilder.status(Optional.ofNullable(subscriptionContractDTO.getStatus()).orElse(SubscriptionContractSubscriptionStatus.PAUSED));

            SubscriptionBillingPolicyInput.Builder billingPolicyBuilder = SubscriptionBillingPolicyInput.builder();

            billingPolicyBuilder.interval(subscriptionContractDTO.getBillingIntervalType());
            billingPolicyBuilder.intervalCount(subscriptionContractDTO.getBillingIntervalCount());
            billingPolicyBuilder.minCycles(subscriptionContractDTO.getMinCycles());
            billingPolicyBuilder.maxCycles(subscriptionContractDTO.getMaxCycles());

            contractBuilder.billingPolicy(billingPolicyBuilder.build());

            SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder = SubscriptionDeliveryPolicyInput.builder();


            if (subscriptionContractDTO.getDeliveryIntervalType() != null && subscriptionContractDTO.getDeliveryIntervalCount() != null) {
                deliveryPolicyBuilder.interval(subscriptionContractDTO.getDeliveryIntervalType());
                deliveryPolicyBuilder.intervalCount(subscriptionContractDTO.getDeliveryIntervalCount());
            } else {
                deliveryPolicyBuilder.interval(subscriptionContractDTO.getBillingIntervalType());
                deliveryPolicyBuilder.intervalCount(subscriptionContractDTO.getBillingIntervalCount());
            }

            contractBuilder.deliveryPolicy(deliveryPolicyBuilder.build());

            SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
            SubscriptionDeliveryMethodShippingInput.Builder shippingBuilder = SubscriptionDeliveryMethodShippingInput.builder();
            MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();

            addressBuilder.firstName(subscriptionContractDTO.getDeliveryFirstName());
            addressBuilder.lastName(subscriptionContractDTO.getDeliveryLastName());
            addressBuilder.address1(subscriptionContractDTO.getDeliveryAddress1());
            addressBuilder.address2(subscriptionContractDTO.getDeliveryAddress2());
            addressBuilder.city(Optional.ofNullable(subscriptionContractDTO.getDeliveryCity()).orElse(""));
            addressBuilder.provinceCode(subscriptionContractDTO.getDeliveryProvinceCode());
            CountryCode deliveryCountryCode = null;
            if (StringUtils.isNotBlank(subscriptionContractDTO.getDeliveryCountryCode())) {
                deliveryCountryCode = CountryCode.valueOf(subscriptionContractDTO.getDeliveryCountryCode().toUpperCase());
                addressBuilder.countryCode(deliveryCountryCode);
            }

            String deliveryZip = subscriptionContractDTO.getDeliveryZip();

            if (CountryCode.US.equals(deliveryCountryCode)) {
                if (!org.springframework.util.StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                    deliveryZip = "0" + deliveryZip;
                }
            }

            addressBuilder.zip(deliveryZip);
            addressBuilder.phone(subscriptionContractDTO.getDeliveryPhone());

            shippingBuilder.address(addressBuilder.build());
            deliveryMethodBuilder.shipping(shippingBuilder.build());

            contractBuilder.deliveryMethod(deliveryMethodBuilder.build());

            double deliveryPrice = Optional.ofNullable(subscriptionContractDTO.getDeliveryPriceAmount()).orElse(0d);
            contractBuilder.deliveryPrice(deliveryPrice);

            subscriptionContractCreateInputBuilder.contract(contractBuilder.build());

            SubscriptionContractCreateInput subscriptionContractCreateInput = subscriptionContractCreateInputBuilder.build();

            SubscriptionContractCreateMutation subscriptionContractCreateMutation = new SubscriptionContractCreateMutation(subscriptionContractCreateInput);
            Response<Optional<SubscriptionContractCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractCreateMutation);

            String draftId = Objects.requireNonNull(optionalMutationResponse.getData()).flatMap(s -> s.getSubscriptionContractCreate().flatMap(c -> c.getDraft().map(SubscriptionContractCreateMutation.Draft::getId))).orElse(null);

            if (draftId == null) {
                if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors());
                    throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                }

                List<SubscriptionContractCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getSubscriptionContractCreate().map(SubscriptionContractCreateMutation.SubscriptionContractCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.info("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()));
                    throw new BadRequestAlertException(userErrors.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                }
            }
            if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionContractDTO.getLines())) {
                for (SubscriptionContractLineDTO line : subscriptionContractDTO.getLines()) {
                    SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput.builder();
                    subscriptionLineInputBuilder.quantity(line.getQuantity());
                    String productVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(line.getVariantId());
                    subscriptionLineInputBuilder.productVariantId(productVariantId);
                    if (StringUtils.isNotEmpty(line.getSellingPlanId())) {
                        int totalCycles = 1;
                        double price = line.getUnitPrice();
                        Double currentPrice = line.getCurrentPrice();
                        double fulfillmentCountMultiplier = subscriptionContractDTO.getBillingIntervalCount() / subscriptionContractDTO.getDeliveryIntervalCount();
                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();
                        subscriptionLineInputBuilder.sellingPlanId(line.getSellingPlanId());
                        FrequencyInfoDTO frequencyInfoDTO = subscriptionGroupService.getSellingPlanById(shop, ShopifyGraphQLUtils.getSellingPlanId(line.getSellingPlanId()));
                        subscriptionLineInputBuilder.sellingPlanId(frequencyInfoDTO.getId());
                        subscriptionLineInputBuilder.sellingPlanName(frequencyInfoDTO.getFrequencyName());
                        DiscountTypeUnit discountType = null;
                        Double discountOffer = null;

                        if (frequencyInfoDTO.getAfterCycle2() != null && totalCycles >= frequencyInfoDTO.getAfterCycle2()) {
                            discountType = frequencyInfoDTO.getDiscountType2();
                            discountOffer = frequencyInfoDTO.getDiscountOffer2();
                        } else if (!frequencyInfoDTO.isFreeTrialEnabled() && frequencyInfoDTO.getAfterCycle1() != null && totalCycles >= frequencyInfoDTO.getAfterCycle1()) {
                            discountType = frequencyInfoDTO.getDiscountType();
                            discountOffer = frequencyInfoDTO.getDiscountOffer();
                        }

                        DecimalFormat df = new DecimalFormat("#.##");

                        if (discountOffer != null) {
                            if (DiscountTypeUnit.PERCENTAGE.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, price * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
                            } else if (DiscountTypeUnit.FIXED.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, (price * fulfillmentCountMultiplier) - discountOffer)));
                            } else if (DiscountTypeUnit.PRICE.equals(discountType)) {
                                currentPrice = Double.parseDouble(df.format(Math.max(0, (discountOffer * fulfillmentCountMultiplier))));
                            }
                        }

                        if (currentPrice == null) {
                            currentPrice = line.getUnitPrice();
                        }

                        buildCycleDiscount(frequencyInfoDTO.getAfterCycle1(), frequencyInfoDTO.getDiscountType(), frequencyInfoDTO.getDiscountOffer(), price, fulfillmentCountMultiplier, cycleDiscounts);
                        buildCycleDiscount(frequencyInfoDTO.getAfterCycle2(), frequencyInfoDTO.getDiscountType2(), frequencyInfoDTO.getDiscountOffer2(), price, fulfillmentCountMultiplier, cycleDiscounts);
                        if (!org.springframework.util.CollectionUtils.isEmpty(cycleDiscounts)) {
                            SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder()
                                .basePrice(price)
                                .cycleDiscounts(cycleDiscounts)
                                .build();
                            subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInput);
                        }
                        subscriptionLineInputBuilder.currentPrice(currentPrice);
                    } else {
                        if (!currencyCode.equals(CurrencyCode.valueOf(shopInfoDTO.getCurrency()))) {
                            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(CurrencyUtils.getCountryCode(currencyCode)).build();
                            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(productVariantId, contextualPricingContext);
                            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            Object price = requireNonNull(productVariantResponse.getData())
                                .flatMap(e -> e.getProductVariant()
                                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                                .orElse(null);
                            line.setCurrentPrice(Double.parseDouble(requireNonNull(price).toString()));
                        }

                        if (line.getCurrentPrice() != null) {
                            subscriptionLineInputBuilder.currentPrice(line.getCurrentPrice());
                        } else {
                            int intervalMultiplier = 1;
                            if (subscriptionContractDTO.getDeliveryIntervalType() != null && subscriptionContractDTO.getDeliveryIntervalCount() != null) {
                                intervalMultiplier = subscriptionContractDTO.getBillingIntervalCount() / subscriptionContractDTO.getDeliveryIntervalCount();
                            }
                            subscriptionLineInputBuilder.currentPrice(line.getUnitPrice() * intervalMultiplier);
                        }
                    }

                    SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder.build();
                    SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
                    Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse1 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                    if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse1.getErrors())) {
                        log.info("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage());
                        throw new BadRequestAlertException(optionalMutationResponse1.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                    }


                    List<SubscriptionDraftLineAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse1.getData()).map(d -> d.getSubscriptionDraftLineAdd().map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                    if (!userErrors.isEmpty()) {
                        log.info("userErrors=" + userErrors);
                        throw new BadRequestAlertException(userErrors.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
                    }
                }
            }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);


            if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse2.getErrors())) {
                log.info("optionalMutationResponse2.getErrors()=" + optionalMutationResponse2.getErrors());
                throw new BadRequestAlertException(optionalMutationResponse2.getErrors().get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
            }

            List<SubscriptionDraftCommitMutation.UserError> userErrors1 = Objects.requireNonNull(optionalMutationResponse2.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors1.isEmpty()) {
                log.info("userErrors1=" + userErrors1);
                throw new BadRequestAlertException(userErrors1.get(0).getMessage(), ENTITY_NAME, "createSubscriptionContractError");
            }

            String graphContractId = optionalMutationResponse2.getData().flatMap(d -> d.getSubscriptionDraftCommit().flatMap(e -> e.getContract().map(SubscriptionDraftCommitMutation.Contract::getId))).orElse(null);

            long numContractId = Long.parseLong(graphContractId.replace(ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

            log.info("Manually created contract with contract id : {} and shop : {}", numContractId, shop);

            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = new SubscriptionContractDetailsDTO();
            subscriptionContractDetailsDTO.setShop(shop);
            subscriptionContractDetailsDTO.setGraphSubscriptionContractId(graphContractId);
            subscriptionContractDetailsDTO.setSubscriptionContractId(numContractId);
            subscriptionContractDetailsDTO.setStopUpComingOrderEmail(false);
            subscriptionContractDetailsDTO.setPausedFromActive(false);

            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

            return getSubscriptionContractRawInternal(numContractId, shop).getBody();

        } catch (Exception ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "createSubscriptionContractError");
        }
    }

    private void buildCycleDiscount(Integer afterCycle, DiscountTypeUnit discountTypeUnit, Double discountOffer, Double price, double fulfillmentCountMultiplier, List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts) {
        if (afterCycle == null || discountTypeUnit == null || discountOffer == null) {
            return;
        }
        cycleDiscounts.add(buildCycleDiscountInput(price, afterCycle, discountTypeUnit, discountOffer, fulfillmentCountMultiplier));
    }

    private SubscriptionPricingPolicyCycleDiscountsInput buildCycleDiscountInput(Double basePrice, Integer afterCycle, DiscountTypeUnit discountTypeUnit, Double discountOffer, double fulfillmentCountMultiplier) {
        if (afterCycle == null) {
            return null;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        SubscriptionPricingPolicyCycleDiscountsInput.Builder subscriptionPricingPolicyCycleDiscountsInputBuilder =
            SubscriptionPricingPolicyCycleDiscountsInput.builder();
        subscriptionPricingPolicyCycleDiscountsInputBuilder.afterCycle(afterCycle);

        if (discountTypeUnit == DiscountTypeUnit.PERCENTAGE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, basePrice * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
        } else if (discountTypeUnit == DiscountTypeUnit.FIXED) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, (basePrice * fulfillmentCountMultiplier) - discountOffer)));
        } if (discountTypeUnit == DiscountTypeUnit.PRICE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, discountOffer * fulfillmentCountMultiplier)));
        }

        return subscriptionPricingPolicyCycleDiscountsInputBuilder.build();
    }

    //                    subscriptionLineInputBuilder.pricingPolicy()



    private List<String> getPaymentMethodList(String gqlCustomerId, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(gqlCustomerId, Input.optional(false));

        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

        List<CustomerPaymentMethodsQuery.Node> paymentMethods = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        for (CustomerPaymentMethodsQuery.Node paymentMethod : paymentMethods) {
            String a = "b";
        }

        List<String> paymentMethodIds = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode().getId()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (paymentMethodIds.size() > 1) {

            Set<String> last4DigitsSet = new HashSet<>();
            paymentMethodIds = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().filter(e -> {


                if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerCreditCard) {
                    String last4Digits = ((CustomerPaymentMethodsQuery.AsCustomerCreditCard) e.getNode().getInstrument().get()).getLastDigits();

                    if (last4DigitsSet.contains(last4Digits)) {
                        return false;
                    }
                    last4DigitsSet.add(last4Digits);
                    return true;
                } else if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerShopPayAgreement) {
                    String last4Digits = ((CustomerPaymentMethodsQuery.AsCustomerShopPayAgreement) e.getNode().getInstrument().get()).getLastDigits();

                    if (last4DigitsSet.contains(last4Digits)) {
                        return false;
                    }
                    last4DigitsSet.add(last4Digits);
                    return true;
                } else {
                    return false;
                }
            }).map(e -> e.getNode().getId()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            while (paymentMethodIds.size() > 1) {
                paymentMethodIds.remove(paymentMethodIds.size() - 1);
            }
        }

        return paymentMethodIds;
    }

    @GetMapping("/api/subscription-contract-details/shopify/customer/search")
    public ResponseEntity<CustomerSearchQuery.Customers> searchShopifyCustomers(@RequestParam("searchText") String searchText,
                                                                                @RequestParam(value = "cursor", required = false) String cursor) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return ResponseEntity.ok().body(subscriptionCustomerService.searchShopifyCustomers(shop, searchText, cursor));
    }

    @GetMapping("/api/subscription-contract-details/shopify/customer/{customerId}")
    public ResponseEntity<CustomerQuery.Customer> getShopifyCustomerDetails(@PathVariable("customerId") Long customerId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return ResponseEntity.ok().body(subscriptionCustomerService.getShopifyCustomerDetailsById(shop, customerId));
    }

    @GetMapping(value = {"/api/subscription-contract-details/shopify/customer/{customerId}/payment-methods", "/subscriptions/cp/api/subscription-contract-details/shopify/customer/{customerId}/payment-methods"})
    public ResponseEntity<CustomerPaymentMethodsQuery.PaymentMethods> getShopifyCustomerPaymentDetails(@PathVariable("customerId") Long customerId) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return ResponseEntity.ok().body(subscriptionCustomerService.getShopifyCustomerPaymentDetailsById(shop, customerId));
    }

    @PostMapping(value = {"/api/subscription-contract-details/split-existing-contract", "/subscriptions/cp/api/subscription-contract-details/split-existing-contract"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> splitExistingContract(@RequestParam("contractId") Long contractId,
                                                                                                @RequestParam(value = "isSplitContract", required = false, defaultValue = "False") boolean isSplitContract,
                                                                                                @RequestParam(value = "attemptBilling", required = false, defaultValue = "False") boolean attemptBilling,
                                                                                                @RequestBody List<String> lineIds) throws Exception {
        log.info("Rest Request to split Subscription contract {}", contractId);
        String shop = commonUtils.getShop();
        boolean isExternal = commonUtils.isExternal();
        ActivityLogEventSource eventSource = isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL;
        SubscriptionContractQuery.SubscriptionContract newSubscriptionContract = subscriptionContractDetailsService.splitExistingContract(shop, contractId, lineIds, isSplitContract, eventSource);
        subscriptionContractDetailsService.updateNewContractDetails(shop, ShopifyGraphQLUtils.getSubscriptionContractId(newSubscriptionContract.getId()), contractId, isSplitContract, attemptBilling, eventSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "New subscription created successfully.", ""))
            .body(newSubscriptionContract);
    }

    @CrossOrigin
    @ApiOperation("Split/Duplicate existing contract")
    @PostMapping("/api/external/v2/subscription-contract-details/split-existing-contract")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> splitExistingContract(
                                                                                                @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                                                @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
                                                                                                @ApiParam("Remove selected lines from original Contract") @RequestParam(value = "isSplitContract", required = false, defaultValue = "False") boolean isSplitContract,
                                                                                                @ApiParam("Attempt Billing Of New Contract")@RequestParam(value = "attemptBilling", required = false, defaultValue = "False") boolean attemptBilling,
                                                                                                @ApiParam("Line Ids")@RequestBody List<String> lineIds,
                                                                                                HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/split-existing-contract api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract newSubscriptionContract = subscriptionContractDetailsService.splitExistingContract(shop, contractId, lineIds, isSplitContract, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        subscriptionContractDetailsService.updateNewContractDetails(shop, ShopifyGraphQLUtils.getSubscriptionContractId(newSubscriptionContract.getId()), contractId, isSplitContract, attemptBilling, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "New subscription created successfully.", ""))
            .body(newSubscriptionContract);
    }

    @PostMapping(value = {"/api/subscription-contract-details/replace-variants-v2", "/subscriptions/cp/api/subscription-contract-details/replace-variants-v2"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> replaceVariantsV2(@RequestParam("contractId") Long contractId,
                                                                                            @RequestParam("newVariantId") String newVariantId,
                                                                                            @RequestParam("quantity") Integer quantity,
                                                                                            @RequestParam("oldLineId") String oldLineId) throws Exception {
        log.info("Rest Request to replace variants for Subscription contract {}", contractId);
        String shop = commonUtils.getShop();
        boolean isExternal = commonUtils.isExternal();
        ActivityLogEventSource eventSource = isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL;
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.replaceVariantsV2WithRetry(shop, contractId, null, List.of(ShopifyGraphQLUtils.getVariantId(newVariantId)), quantity, oldLineId, true, eventSource, 2);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription variants replaced successfully.", ""))
            .body(subscriptionContract);
    }

    @PostMapping(value = {"/api/subscription-contract-details/replace-variants-v3", "/subscriptions/cp/api/subscription-contract-details/replace-variants-v3"})
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> replaceVariantsV3(@RequestBody ReplaceVariantsInfoV3 replaceVariantsInfoV3) throws Exception {
        log.info("Rest Request to replace variants for Subscription contract {}", replaceVariantsInfoV3.getContractId());
        String shop = commonUtils.getShop();
        boolean isExternal = commonUtils.isExternal();

        if(isExternal) {
            commonUtils.checkCustomerPermissions(shop, "swapProductVariant");
        }

        ActivityLogEventSource eventSource = isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL;
        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.replaceVariantsV3WithRetry(shop, replaceVariantsInfoV3.getContractId(), replaceVariantsInfoV3.getOldVariants(), replaceVariantsInfoV3.getNewVariants(), replaceVariantsInfoV3.getOldLineId(), true, eventSource, 2);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription variants replaced successfully.", ""))
            .body(subscriptionContract);
    }

    @CrossOrigin
    @ApiOperation("Replace variants")
    @PostMapping("/api/external/v2/subscription-contract-details/replace-variants-v3")
    public ResponseEntity<SubscriptionContractQuery.SubscriptionContract> replaceVariantsV3(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @RequestBody ReplaceVariantsInfoV3 replaceVariantsInfoV3,
        HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/replace-variants-v3api_key: {}", RequestURL, apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.replaceVariantsV3WithRetry(shop, replaceVariantsInfoV3.getContractId(), replaceVariantsInfoV3.getOldVariants(), replaceVariantsInfoV3.getNewVariants(), replaceVariantsInfoV3.getOldLineId(), true, ActivityLogEventSource.MERCHANT_EXTERNAL_API, 2);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription variants replaced successfully.", ""))
            .body(subscriptionContract);
    }

    @PostMapping("/api/update-custom-note-attributes")
    public void updateOrderNoteAttributes(@Valid @RequestBody UpdateAttributesRequest updateAttributesRequest) throws IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        updateOrderNoteAttributesCommon(updateAttributesRequest, shop);
    }

    private void updateOrderNoteAttributesCommon(UpdateAttributesRequest updateAttributesRequest, String shop) {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
        try {
            List<AttributeInput> attributeInputList = new ArrayList<>();
            updateAttributesRequest.getCustomAttributesList().forEach(attribute -> {
                AttributeInput attributeInput = AttributeInput.builder()
                    .key(attribute.getKey())
                    .value(attribute.getValue())
                    .build();
                attributeInputList.add(attributeInput);
            });

            OrderNoteAttributesWrapper orderNoteAttributesWrapper = new OrderNoteAttributesWrapper();
            orderNoteAttributesWrapper.setOrderNoteAttributesList(updateAttributesRequest.getCustomAttributesList());

            Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsOptional = subscriptionContractDetailsService.getSubscriptionByContractId(updateAttributesRequest.getSubscriptionContractId());
            if (subscriptionContractDetailsOptional.isPresent()) {
                SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsOptional.get();
                subscriptionContractDetailsDTO.setOrderNoteAttributes(OBJECT_MAPPER.writeValueAsString(orderNoteAttributesWrapper));
                subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
            }

            subscriptionDraftInputBuilder.customAttributes(attributeInputList);
            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();
            shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, updateAttributesRequest.getSubscriptionContractId(), subscriptionDraftInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-contracts-is-overwrite-anchor-day", "/subscriptions/cp/api/subscription-contracts-is-overwrite-anchor-day"})
    public Boolean isOverwriteAnchorDay(
        @RequestParam("contractId") Long contractId,
        @RequestParam(value = "intervalCount", required = false) Integer billingIntervalCount,
        @RequestParam(value = "interval", required = false) SellingPlanInterval billingInterval,
        @RequestParam(value = "nextBillingDate", required = false) ZonedDateTime nextBillingDate) throws Exception {

        if (ObjectUtils.allNull(billingInterval, nextBillingDate)) {
            throw new BadRequestAlertException("Billing interval and Billing interval count or next billing date is required", "", "");
        }

        String shop = commonUtils.getShop();

        return subscriptionContractDetailsService.isOverwriteAnchorDay(contractId, shop, nextBillingDate, billingIntervalCount, billingInterval);
    }

    @PostMapping("/api/external/v2/update-custom-note-attributes")
    @ApiOperation("Create subscription contract")
    public void updateOrderNoteAttributes(@ApiParam("Your API Key") @RequestParam(value = "api_key") String apiKey,
                                                                                                    @Valid @RequestBody UpdateAttributesRequest updateAttributesRequest) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        updateOrderNoteAttributesCommon(updateAttributesRequest, shop);
    }

}
