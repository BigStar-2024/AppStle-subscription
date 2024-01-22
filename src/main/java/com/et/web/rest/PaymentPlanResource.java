package com.et.web.rest;


import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.recurringcharge.*;
import com.et.constant.Constants;
import com.et.domain.*;
import com.et.domain.enumeration.BasePlan;
import com.et.domain.enumeration.BasedOn;
import com.et.domain.enumeration.BillingType;
import com.et.domain.enumeration.PaymentPlanEvent;
import com.et.repository.ShopInfoRepository;
import com.et.repository.SubscriptionBundlingRepository;
import com.et.security.SecurityUtils;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.utils.SlackField;
import com.et.utils.SlackService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.BillingUrlVM;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import com.shopify.java.graphql.client.queries.CreateAppSubscriptionMutation;
import com.shopify.java.graphql.client.type.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.et.constant.Constants.SHOPIFY_DOMAIN_SUFFIX;

@RestController
@RequestMapping("/api")
public class PaymentPlanResource {
    public static final HashSet<String> TEST_STORE_PLANS = Sets.newHashSet("affiliate", "partner_test", "staff");

    private final Logger log = LoggerFactory.getLogger(PaymentPlanResource.class);

    private static final String ENTITY_NAME = "paymentPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${oauth.base-uri}")
    private String oauthBaseUri;

    private final PaymentPlanService paymentPlanService;

    private final PlanInfoService planInfoService;

    private final SocialConnectionService socialConnectionService;

    private final ShopInfoRepository shopInfoRepository;

    @Autowired
    private CancellationManagementService cancellationManagementService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private ProductSwapService productSwapService;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private DeliveryProfileService deliveryProfileService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private ActivityUpdatesSettingsService activityUpdatesSettingsService;

    @Autowired
    private SubscriptionBundlingRepository subscriptionBundlingRepository;

    @Autowired
    private PlanInfoDiscountService planInfoDiscountService;

    @Autowired
    private CommonUtils commonUtils;

    public PaymentPlanResource(PaymentPlanService paymentPlanService, PlanInfoService planInfoService, SocialConnectionService socialConnectionService, ShopInfoRepository shopInfoRepository) {
        this.paymentPlanService = paymentPlanService;
        this.planInfoService = planInfoService;
        this.socialConnectionService = socialConnectionService;
        this.shopInfoRepository = shopInfoRepository;
    }

    @PostMapping("/payment-plans")
    public ResponseEntity<PaymentPlan> createPaymentPlan(@Valid @RequestBody PaymentPlan paymentPlan) throws URISyntaxException {
        log.debug("REST request to save PaymentPlan : {}", paymentPlan);
        if (paymentPlan.getId() != null) {
            throw new BadRequestAlertException("A new paymentPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (!shop.equals(paymentPlan.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        PaymentPlan result = paymentPlanService.save(paymentPlan);
        return ResponseEntity.created(new URI("/api/payment-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/payment-plans")
    public ResponseEntity<PaymentPlan> updatePaymentPlan(@Valid @RequestBody PaymentPlan paymentPlan) throws URISyntaxException {
        log.debug("REST request to update PaymentPlan : {}", paymentPlan);
        if (paymentPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (!shop.equals(paymentPlan.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        PaymentPlan result = paymentPlanService.save(paymentPlan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentPlan.getId().toString()))
            .body(result);
    }

    @GetMapping("/payment-plans")
    public List<PaymentPlan> getAllPaymentPlans() {
        log.debug("REST request to get all PaymentPlans");
        PaymentPlan paymentPlan = paymentPlanService.findByShop(SecurityUtils.getCurrentUserLogin().get()).get();
        List<PaymentPlan> paymentPlans = new ArrayList<>();
        paymentPlans.add(paymentPlan);
        return paymentPlans;
    }

    @GetMapping("/payment-plans/{id}")
    public ResponseEntity<PaymentPlan> getPaymentPlan(@PathVariable Long id) {
        log.debug("REST request to get PaymentPlan : {}", id);
        Optional<PaymentPlan> paymentPlan = paymentPlanService.findByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseUtil.wrapOrNotFound(paymentPlan);
    }

    @GetMapping("/shop-payment-plan")
    public ResponseEntity<PaymentPlan> getPaymentPlanByShop() throws IOException {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        Optional<PaymentPlan> paymentPlanOptional = paymentPlanService.findByShop(shopName);

        return paymentPlanOptional.map(response -> ResponseEntity.ok().body(response))
            .orElse(new ResponseEntity<>(new PaymentPlan(), HttpStatus.OK));
    }

    @GetMapping("/payment-plans/payment-plan-limit-information")
    public PlanLimitInformation getPlanLimitInformation() throws IOException {
        return paymentPlanService.getPlanLimitInformation(SecurityUtils.getCurrentUserLogin().get());
    }

    @GetMapping("/payment-plans/test-charge-information")
    public ResponseEntity<Boolean> getTestChargeInformation() {
        return ResponseEntity.ok().body(paymentPlanService.getTestChargeInformation(SecurityUtils.getCurrentUserLogin().get()));
    }

    public void deleteRecurringCharges(ShopifyAPI api) {
        GetRecurringChargesResponse recurringCharges = api.getRecurringCharges();
        for (RecurringApplicationChargeResponse recurringApplicationChargeResponse : recurringCharges.getRecurring_application_charges()) {
            try {
                api.deleteRecurringCharge(recurringApplicationChargeResponse.getId());
            } catch (Exception ignored) {
                String a = "b";
            }
        }
    }

    @GetMapping("/shop-billing-confirmation-url/{planId}")
    public ResponseEntity<BillingUrlVM> getBillingConfirmationUrl(@PathVariable Long planId,
                                                                  @RequestParam(value = "apikey", required = false) String apikey,
                                                                  @RequestParam(value = "ignoreDaysSpent", required = true, defaultValue = "false") boolean ignoreDaysSpent,
                                                                  @RequestParam(value = "discountCode", required = false) String discountCode,
                                                                  @RequestParam(value = "trialDays", required = false) Integer trialDays,
                                                                  @RequestParam(value = "host", required = false, defaultValue = "") String host) throws Exception {

        String shopName = null;
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            ShopInfoDTO shopInfoDTO = shopInfoService.findByApiKey(apikey).get();
            shopName = shopInfoDTO.getShop();
        }

        PlanInfoDTO planInfoDTO = planInfoService.findOne(planId).get();

        if (StringUtils.isNotBlank(discountCode) && planInfoDiscountService.isValidDiscountCode(discountCode)) {
            PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountService.findByDiscountCode(discountCode).get();

            planInfoDTO = planInfoService.createPlanInfoForDiscount(planInfoDTO, planInfoDiscountDTO);
        }


        Optional<PaymentPlan> optionalPaymentPlan = paymentPlanService.findByShop(shopName);

        trialDays = Optional.ofNullable(trialDays).orElse(planInfoDTO.getTrialDays());

        long daysSpent = 0;
        if (optionalPaymentPlan.isPresent()) {
            daysSpent = ChronoUnit.DAYS.between(optionalPaymentPlan.get().getActivationDate(), ZonedDateTime.now());
        }

        try {
            AdditionalDetailsDTO additionalDetailsDTO = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<>() {
                },
                planInfoDTO.getAdditionalDetails()
            );

            AdditionalDetailsDTO customerAdditionalDetailsDTO = optionalPaymentPlan.isPresent() ? CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<>() {
                },
                optionalPaymentPlan.get().getAdditionalDetails()
            ) : additionalDetailsDTO;

            if (optionalPaymentPlan.isPresent() && !(BasedOn.SUBSCRIPTION_ORDER_AMOUNT.equals(optionalPaymentPlan.get().getBasedOn()) || customerAdditionalDetailsDTO.getSubscriptionOrderAmount() != null)) {
                if (customerAdditionalDetailsDTO.getEnableBundling() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableBundling()).orElse(true)) {
                    List<SubscriptionBundling> subscriptionBundlings = subscriptionBundlingRepository.findByShop(shopName);
                    if (!subscriptionBundlings.isEmpty() && subscriptionBundlings.stream().filter(s -> s.isSubscriptionBundlingEnabled()).findAny().isPresent()) {
                        throw new BadRequestAlertException("Please disable Subscription Bundling and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "subscriptionBundlingexists");
                    }
                }

                if (customerAdditionalDetailsDTO.getEnableProductSwapAutomation() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableProductSwapAutomation()).orElse(true)) {
                    List<ProductSwapDTO> productSwapDTOList = productSwapService.findByShop(shopName);
                    if (!productSwapDTOList.isEmpty()) {
                        throw new BadRequestAlertException("Please disable Product Swap automation and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "productswapexists");
                    }
                }

                if (customerAdditionalDetailsDTO.getEnableCustomEmailDomain() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableCustomEmailDomain()).orElse(true)) {
                    ShopInfoDTO shopInfo = shopInfoService.findByShop(shopName).get();
                    if (StringUtils.isNoneBlank(shopInfo.getEmailCustomDomain())) {
                        throw new BadRequestAlertException("Please disable Custom Email Domain and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "customemaildomainexists");
                    }
                }

                if (customerAdditionalDetailsDTO.getEnableCustomEmailHtml() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableCustomEmailHtml()).orElse(true)) {
                    List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingService.findByShop(shopName);
                    if (!emailTemplateSettingList.isEmpty()) {
                        for (EmailTemplateSetting emailTemplateSetting : emailTemplateSettingList) {
                            if (StringUtils.isNoneBlank(emailTemplateSetting.getHtml())) {
                                throw new BadRequestAlertException("Please disable Custom Email HTML and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "customemailhtmlexists");
                            }
                        }
                    }
                }

                if (customerAdditionalDetailsDTO.getEnableShippingProfiles() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableShippingProfiles()).orElse(true)) {
                    List<DeliveryProfileDTO> deliveryProfileDTOList = deliveryProfileService.findByShop(shopName);
                    if (!deliveryProfileDTOList.isEmpty()) {
                        throw new BadRequestAlertException("Please disable Shipping Profiles and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "shippingprofilesexists");
                    }
                }

                if (customerAdditionalDetailsDTO.getEnableSummaryReports() != null && !Optional.ofNullable(additionalDetailsDTO.getEnableSummaryReports()).orElse(true)) {
                    Optional<ActivityUpdatesSettings> activityUpdatesSettings = activityUpdatesSettingsService.findByShop(shopName);
                    if (activityUpdatesSettings.isPresent() && activityUpdatesSettings.get().isSummaryReportEnabled()) {
                        throw new BadRequestAlertException("Please disable Summary Reports and other functionality which is not available in your selected downgrade plan.", ENTITY_NAME, "activityUpdatesSettingsexists");
                    }
                }
            }
        } catch (Exception ex) {
            if (ex instanceof BadRequestAlertException) {
                throw ex;
            }
            log.error("Something went wrong while changing plan=" + ExceptionUtils.getStackTrace(ex) + " shop=" + shopName);
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shopName);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shopName);

        if (planInfoDTO.getPrice() == 0.0) {

            updatePaymentEntity(shopName, planId, planInfoDTO.getId(), trialDays, null);

            BillingUrlVM billingUrlVM = new BillingUrlVM(oauthBaseUri, true);

            deleteRecurringCharges(api);

            return ResponseEntity.ok().body(billingUrlVM);
        }

        String confirmationUrl = createRecurringCharge(shopName, api, shopifyGraphqlClient, planInfoDTO, daysSpent, ignoreDaysSpent, trialDays, host);

        BillingUrlVM billingUrlVM = new BillingUrlVM(confirmationUrl);
        return ResponseEntity.ok().body(billingUrlVM);
    }

    @GetMapping("/activatecharge")
    public RedirectView activateCharge(@RequestParam("shop") String shop, @RequestParam("charge_id") Long chargeId,
                                       @RequestParam(value = "planId") Long planId, @RequestParam(value = "trialDays") Integer trialDays,
                                       @RequestParam(value = "host", required = false) String host) throws IOException {

        if(!shop.endsWith(SHOPIFY_DOMAIN_SUFFIX)) {
            shop = shop + SHOPIFY_DOMAIN_SUFFIX;
        }

        log.info("shop=" + shop + " chargeId=" + Optional.ofNullable(chargeId).orElse(0L) + " planId=" + planId + " trialDays=" + trialDays);

        Optional<PlanInfoDTO> planInfoOptional = planInfoService.findOne(planId);

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        if (planInfoOptional.isPresent() && planInfoOptional.get().getBillingType().equals(BillingType.SHOPIFY_ANNUAL)) {

            GetRecurringChargeResponse recurringChargeResponse = api.getRecurringCharge(chargeId);

            if (recurringChargeResponse.getRecurring_application_charge().getStatus().equalsIgnoreCase("active")) {
                updatePaymentEntity(shop, planId, chargeId, trialDays, recurringChargeResponse.getRecurring_application_charge().getTrial_ends_on());
            }

        } else {


            GetRecurringChargesResponse recurringCharges = api.getRecurringCharges();

            for (RecurringApplicationChargeResponse recurringApplicationCharge : recurringCharges
                .getRecurring_application_charges()) {

                Long recurringApplicationChargeId = recurringApplicationCharge.getId();

                if (!recurringApplicationChargeId.equals(chargeId)
                    || !recurringApplicationCharge.getStatus().equalsIgnoreCase("active")) {
                    continue;
                }

                ActivateRecurringChargeRequest activateRecurringChargeRequest = new ActivateRecurringChargeRequest();
                activateRecurringChargeRequest.setRecurring_application_charge(recurringApplicationCharge);
                ActivateRecurringChargeResponse activateRecurringChargeResponse = api.activateRecurringChargeRequest(activateRecurringChargeRequest);

                updatePaymentEntity(shop, planId, recurringApplicationChargeId, trialDays, activateRecurringChargeResponse.getRecurring_application_charge().getTrial_ends_on());
            }
        }

        log.info("host=" + Optional.ofNullable(host).orElse(""));
        return new RedirectView(oauthBaseUri + "?shop=" + shop + Optional.ofNullable(host).map(h -> "&host=" + h).orElse(""), false);
    }

    public String createRecurringCharge(String shop, ShopifyAPI api, ShopifyGraphqlClient shopifyGraphqlClient, PlanInfoDTO planInfoDTO, long daysSpent, boolean ignoreDaysSpent, Integer trialDays, String host) throws Exception {

        boolean isTest = false;

        if (TEST_STORE_PLANS.contains(api.getShopInfo().getShop().getPlanName().toLowerCase()) || shop.equals("vedix-us-dev.myshopify.com")) {
            isTest = true;
        }

        Integer remainingTrialDays = Optional.ofNullable(trialDays).orElse(14);

        if (!ignoreDaysSpent) {
            remainingTrialDays = remainingTrialDays - (int) daysSpent;
        }

        remainingTrialDays = remainingTrialDays > 0 ? remainingTrialDays : 0;


        AppSubscriptionLineItemInput.Builder appSubscriptionLineItemInputBuilder = AppSubscriptionLineItemInput.builder();

        AppPlanInput.Builder appPlanInputBuilder = AppPlanInput.builder();

        AppRecurringPricingInput.Builder appRecurringPricingInputBuilder = AppRecurringPricingInput.builder();
        appRecurringPricingInputBuilder
            .interval(
                planInfoDTO.getBillingType() == BillingType.SHOPIFY_ANNUAL
                    ? AppPricingInterval.ANNUAL
                    : AppPricingInterval.EVERY_30_DAYS
            )
            .price(MoneyInput.builder().amount(planInfoDTO.getPrice()).currencyCode(CurrencyCode.USD).build());

        appPlanInputBuilder.appRecurringPricingDetails(appRecurringPricingInputBuilder.build());

        if (planInfoDTO.getBillingType() == BillingType.SHOPIFY_USAGE) {
            int cappedAmount = 40;
            String transactionFee = "0.50";
            String fixedPrice = "$" + "0";
            String maxAmount = String.valueOf(cappedAmount);

            String terms = "Thank you for choosing Appstle Subscriptions! You have chosen the FREE plan. The breakdown of your monthly bill will be as follows: Fixed monthly fee - " + fixedPrice + " Transaction fee - " + transactionFee + "% of each subscription order, to a maximum of $" + maxAmount + ". This means that for every recurring order processed through Appstle, we will charge " + transactionFee + "% of the transaction value. However, we will not charge more than $" + cappedAmount + " as fee, in any given month! For example, if you sell $8000 as subscriptions in a month, through Appstle, our fee will be " + transactionFee + "% of it - $40. If you sell $20,000 as subscriptions the next month, our fee will not proportionately increase, and stay at $" + cappedAmount + " (the maximum)!\n" +
                "\n" +
                "You can view the detailed bill on your Shopify Admin billing section. Please reach out to us at subscription-support@appstle.com, or just ping us through the chat widget on our app, if you have any questions about the billing, or want to discuss any other feature.";

            AppUsagePricingInput.Builder appUsagePricingInputBuilder = AppUsagePricingInput.builder();
            appUsagePricingInputBuilder.cappedAmount(MoneyInput.builder().amount(appUsagePricingInputBuilder).currencyCode(CurrencyCode.USD).build());
            appUsagePricingInputBuilder.terms(terms);

            appPlanInputBuilder.appUsagePricingDetails(appUsagePricingInputBuilder.build());
        }

        appSubscriptionLineItemInputBuilder.plan(appPlanInputBuilder.build());

        List<AppSubscriptionLineItemInput> lineItems = List.of(appSubscriptionLineItemInputBuilder.build());

        StringBuilder returnUrlString = new StringBuilder();
        returnUrlString
            .append(oauthBaseUri)
            .append("/api/activatecharge?shop=")
            .append(shop.replace(SHOPIFY_DOMAIN_SUFFIX, ""))
            .append("&planId=")
            .append(planInfoDTO.getId())
            .append("&trialDays=")
            .append(trialDays)
            .append("&host=")
            .append(Optional.ofNullable(host).orElse(""));


        String confirmationUrl = null;

        CreateAppSubscriptionMutation createAppSubscriptionMutation = new CreateAppSubscriptionMutation(
            planInfoDTO.getName(),
            lineItems,
            isTest,
            remainingTrialDays,
            AppSubscriptionReplacementBehavior.APPLY_IMMEDIATELY,
            returnUrlString
        );

        Response<Optional<CreateAppSubscriptionMutation.Data>> createAppSubscriptionResponse = shopifyGraphqlClient.getOptionalMutationResponse(createAppSubscriptionMutation);

        if (!CollectionUtils.isEmpty(createAppSubscriptionResponse.getErrors())) {
            log.error("Error while creating app billing plan. shop={}, error={}", shop, StringEscapeUtils.escapeJson(createAppSubscriptionResponse.getErrors().get(0).getMessage()));
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(createAppSubscriptionResponse.getErrors().get(0).getMessage()), "", "");
        }

        List<CreateAppSubscriptionMutation.UserError> userErrors = Objects.requireNonNull(createAppSubscriptionResponse.getData())
            .map(d -> d.getAppSubscriptionCreate()
                .map(CreateAppSubscriptionMutation.AppSubscriptionCreate::getUserErrors).orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());


        if (!userErrors.isEmpty()) {
            log.error("User Error while creating app billing plan. shop={}, error={}", shop, StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()));
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }

        if (
            createAppSubscriptionResponse.getData() != null &&
                createAppSubscriptionResponse.getData().isPresent() &&
                createAppSubscriptionResponse.getData().get().getAppSubscriptionCreate().isPresent() &&
                createAppSubscriptionResponse.getData().get().getAppSubscriptionCreate().get().getConfirmationUrl().isPresent()
        ) {
            confirmationUrl = createAppSubscriptionResponse.getData().get()
                .getAppSubscriptionCreate().get()
                .getConfirmationUrl().get()
                .toString();
        }

        return confirmationUrl;
    }

    private void updatePaymentEntity(String shop, Long planId, Long chargeId, Integer trialDays, Date trialEndsOn) throws IOException {
        PlanInfoDTO planInfoDTO = planInfoService.findOne(planId).get();
        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        boolean isTestCharge = true;
        try {
            isTestCharge = BooleanUtils.isTrue(api.getRecurringCharge(chargeId).getRecurring_application_charge().isTest());
        } catch (Exception ex) {

        }

        Optional<PaymentPlan> paymentPlanOptional = paymentPlanService.findByShop(shop);

        PaymentPlanEvent paymentPlanEvent = null;
        Double oldPrice = null;
        BasePlan oldBasePlan = null;
        ZonedDateTime oldActivationDate = null;

        if (paymentPlanOptional.isEmpty()) {
            paymentPlanEvent = PaymentPlanEvent.APP_INSTALLED;
        } else {
            PaymentPlan existingPlan = paymentPlanOptional.get();
            oldBasePlan = existingPlan.getBasePlan();
            oldPrice = existingPlan.getPrice();
            oldActivationDate = existingPlan.getActivationDate();
            if (planInfoDTO.getPrice() >= oldPrice) {
                paymentPlanEvent = PaymentPlanEvent.UPGRADE;
            } else {
                paymentPlanEvent = PaymentPlanEvent.DOWNGRADE;
            }
        }

        applyCommissionForAffiliate(shop, chargeId, planId, paymentPlanOptional.map(PaymentPlan::getPlanInfo).map(PlanInfo::getId).orElse(null));

        PaymentPlan paymentPlan = paymentPlanOptional.orElseGet(PaymentPlan::new);

        paymentPlan.setShop(shop);
        paymentPlan.setRecurringChargeId(chargeId);
        paymentPlan.setChargeActivated(true);
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        paymentPlan.setActivationDate(utcNow);
        paymentPlan.setBilledDate(utcNow);
        paymentPlan.setAdditionalDetails(planInfoDTO.getAdditionalDetails());
        paymentPlan.setTrialDays(trialDays);
        paymentPlan.setPrice(planInfoDTO.getPrice());
        paymentPlan.setName(planInfoDTO.getName());
        paymentPlan.setPlanType(planInfoDTO.getPlanType());
        paymentPlan.setBillingType(planInfoDTO.getBillingType());
        paymentPlan.setBasedOn(planInfoDTO.getBasedOn());
        paymentPlan.setFeatures(planInfoDTO.getFeatures());
        paymentPlan.setTestCharge(isTestCharge);
        paymentPlan.setBasePlan(planInfoDTO.getBasePlan());
        paymentPlan.setPaymentPlanEvent(paymentPlanEvent);
        paymentPlan.setShopifyPlanName(shopInfoDTO.getShopifyPlanName());
        paymentPlan.setShopifyPlanDisplayName(shopInfoDTO.getShopifyPlanDisplayName());

        if (paymentPlanOptional.isEmpty()) {
            paymentPlan.setPaymentPlanEvent(PaymentPlanEvent.APP_INSTALLED);
        } else {
            if (planInfoDTO.getPrice() >= oldPrice) {
                paymentPlan.setPaymentPlanEvent(PaymentPlanEvent.UPGRADE);
            } else {
                paymentPlan.setPaymentPlanEvent(PaymentPlanEvent.DOWNGRADE);
            }

            paymentPlan.setOldBasePlan(oldBasePlan);
            paymentPlan.setOldActivationDate(oldActivationDate);
            paymentPlan.setOldPrice(oldPrice);
        }

        if (trialEndsOn != null) {
            paymentPlan.setTrialEndsOn(ZonedDateTime.ofInstant(trialEndsOn.toInstant(), ZoneId.systemDefault()));
        }
        paymentPlan.setValidCharge(true);
        paymentPlan.setShopFrozen(false);

        PlanInfo planInfo = new PlanInfo();
        planInfo.setId(planId);
        paymentPlan.setPlanInfo(planInfo);
        paymentPlan.setPaymentPlanEventTime(utcNow);

        PaymentPlan savedPaymentPlan = paymentPlanService.save(paymentPlan);

        if (StringUtils.isNotEmpty(paymentPlan.getShopifyPlanDisplayName()) && paymentPlan.getShopifyPlanDisplayName().equals("Shopify Plus")) {
            SlackService.sendMessage(
                "Shopify Plus",
                SlackService.SlackChannel.NotificationEnterprisePlan,
                new SlackField("shop", shop));
        } else if (paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE) || paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE_ANNUAL)) {
            SlackService.sendMessage(
                "Enterprise Plan",
                SlackService.SlackChannel.NotificationEnterprisePlan,
                new SlackField("shop", shop));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        log.info(objectMapper.writeValueAsString(savedPaymentPlan));
    }

    private void applyCommissionForAffiliate(String shop, Long chargeId, Long planId, Long previousPlanId) {
        PlanInfoDTO planInfo = planInfoService.findOne(planId).get();
        PlanInfoDTO previousPlanInfo = previousPlanId == null ? null : planInfoService.findOne(previousPlanId).orElse(null);

        try {
            /*HashMap<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("uid", shop);
            bodyMap.put("event_id", chargeId);
            bodyMap.put("amount", getMonthlyPrice(planInfo));
            bodyMap.put("mrr", getMonthlyPrice(planInfo));
            bodyMap.put("plan", planInfo.getName());
            bodyMap.put("currency", "USD");
            bodyMap.put("start_recurring_reward_in", planInfo.getTrialDays());
            bodyMap.put("recurring_reward_frequency", 30);

            if (previousPlanInfo == null || previousPlanInfo.getName().equalsIgnoreCase("FREE")) {
                bodyMap.put("max_recurring", 6);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", Constants.FIRST_PROMOTER_API_KEY);
            HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<HashMap<String, Object>>(bodyMap, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FirstPromoterSaleResponse> exchange = restTemplate
                .exchange("https://firstpromoter.com/api/v1/track/sale", HttpMethod.POST, requestEntity, FirstPromoterSaleResponse.class);*/


            HttpResponse<String> response = Unirest.post("https://firstpromoter.com/api/v1/track/sale")
                .header("x-api-key", Constants.FIRST_PROMOTER_API_KEY)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("uid", shop)
                .field("currency", "USD")
                .field("event_id", chargeId.toString())
                .field("plan", planInfo.getName())
                .field("amount", getMonthlyPrice(planInfo).toString())
                .asString();
        } catch (Exception ex) {
            log.error("An error occurred while Sending Commission. ex=" + ExceptionUtils.getStackTrace(ex), ex);
        }
    }

    private Double getMonthlyPrice(PlanInfoDTO planInfo) {
        return planInfo.getPrice() * 100;
    }
}
