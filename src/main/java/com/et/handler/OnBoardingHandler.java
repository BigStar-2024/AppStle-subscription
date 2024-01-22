package com.et.handler;

import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.recurringcharge.RecurringApplicationChargeResponse;
import com.et.api.shopify.shop.Shop;
import com.et.api.shopify.theme.GetThemesResponse;
import com.et.api.shopify.webhook.WebHookUtils;
import com.et.constant.EmailTemplateConstants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.repository.CustomerPortalSettingsRepository;
import com.et.repository.ThemeCodeRepository;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.utils.*;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.et.constant.Constants.*;
import static com.et.utils.SubscribeItScriptUtils.PUBLISHED_ROLE;


@Service
@Transactional
public class OnBoardingHandler {

    private final Logger log = LoggerFactory.getLogger(OnBoardingHandler.class);

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private WebHookUtils webHookUtils;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private ThemeSettingsService themeSettingsService;

    @Autowired
    private ThemeCodeRepository themeCodeRepository;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    @Autowired
    private SubscriptionCustomCssService subscriptionCustomCssService;

    @Autowired
    private DunningManagementService dunningManagementService;

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private SmsTemplateSettingService smsTemplateSettingService;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private BundleSettingService bundleSettingService;

    @Autowired
    private CartWidgetSettingsService cartWidgetSettingsService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopLabelService shopLabelService;

    @Autowired
    private OnboardingInfoService onboardingInfoService;

    public void handle(String shop, String accessToken, String providerId) throws IOException {

        try {
            MDC.put("shop", shop);

            ShopifyAPI api = new ShopifyWithRateLimiter(accessToken, shop);

            try {
                List<RecurringApplicationChargeResponse> recurring_application_charges = api.getRecurringCharges().getRecurring_application_charges();

                boolean isValidPlanPresent = false;
                for (RecurringApplicationChargeResponse recurring_application_charge : recurring_application_charges) {
                    if (recurring_application_charge.getStatus().equals("active")) {
                        isValidPlanPresent = true;
                    }
                }

                if (!isValidPlanPresent) {
                    paymentPlanService.deleteByShop(shop);
                }
            } catch (Exception ex) {
                log.info("An error occurred while checking payment plan. Ex=" + ExceptionUtils.getStackTrace(ex));
            }

            saveSocialConnection(accessToken, shop, providerId);

            insertDefaultData(shop, api);
            tryFindingThemeForTheStore(shop, api);

            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    createWebHooks(api);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MDC.clear();
        }
    }

    public void tryFindingThemeForTheStore(String shop, ShopifyAPI api) {
        log.info("Setting Theme data for shop={}", shop);

        try {
            GetThemesResponse themesResponse = api.getThemes();
            for (GetThemesResponse.BasicThemeInfo theme : themesResponse.getThemes()) {
                if (theme.getRole().equals(PUBLISHED_ROLE)) {
                    Long theme_store_id = theme.getTheme_store_id();
                    String themeName = "floating-button";

                    if (theme_store_id != null) {
                        Optional<ThemeCode> themeCodeOptional = themeCodeRepository.findByThemeStoreId(Math.toIntExact(theme_store_id));
                        if (themeCodeOptional.isPresent()) {
                            themeName = themeCodeOptional.get().getThemeName();
                        }
                    }

                    try {
                        Optional<ThemeCode> themeCodeByThemeName = themeCodeRepository.findByThemeName(themeName);
                        if (!themeCodeByThemeName.isPresent()) {
                            SlackService.sendMessage(
                                "Subscription new unidentified theme",
                                SlackService.SlackChannel.NotificationSC,
                                new SlackField("shop", shop),
                                new SlackField("theme", theme.getName()),
                                new SlackField("theme_store_id", Optional.ofNullable(theme.getTheme_store_id()).orElse(-1L).toString()));
                        } else {
                            SlackService.sendMessage("Subscription identified theme",
                                SlackService.SlackChannel.NotificationSC,
                                new SlackField("shop", shop),
                                new SlackField("theme", theme.getName()));
                        }
                    } catch (Exception e) {
                        log.info("Error while identifying Theme for shop={}, error={}", shop, ExceptionUtils.getStackTrace(e));
                    }

                    publishJavascriptForTheme(shop, themeName);
                }
            }
        } catch (Exception ex) {
            log.info("Error while setting Theme data for shop={}, error={}", shop, ExceptionUtils.getStackTrace(ex));
        }

        log.info("Theme data setup done for shop={}", shop);
    }

    private void publishJavascriptForTheme(String shop, String themeName) {
        log.info("Saving theme settings for shop={}, themeName={}", shop, themeName);

        ThemeSettings themeSettings = new ThemeSettings();
        themeSettings.setShop(shop);
        themeSettings.setThemeName(themeName);
        themeSettings.setSkip_setting_theme(false);
        themeSettings.setThemeV2Saved(false);
        themeSettings.setShopifyThemeInstallationVersion(ShopifyThemeInstallationVersion.V2);
        themeSettings.setWidgetTemplateType(WidgetTemplateType.WIDGET_TYPE_1);
        themeSettings.setEnableSlowScriptLoad(true);
        Optional<ThemeSettings> themeSettingsOptional = themeSettingsService.findByShop(shop);
        if (themeSettingsOptional.isPresent()) {
            ThemeSettings themeSettings1 = themeSettingsOptional.get();
            themeSettings1.setThemeName(themeName);
            themeSettingsService.save(themeSettings1);
        } else {
            themeSettingsService.save(themeSettings);
        }
        log.info("theme settings saved for shop={}, themeName={}", shop, themeName);

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
    }

    private void insertDefaultData(String shop, ShopifyAPI api) throws IOException {
        try {
            log.info("Inserting default data for shop={}", shop);
            insertShopSettings(shop);
            Shop shopDetails = api.getShopInfo().getShop();
            insertShopInfo(shop, shopDetails);
            insertSubscriptionWidgetSettings(shop);
            insertCustomerPortalSettings(shop);
            insertSubscriotionCustomCss(shop);
            insertEmailTemplateSetting(shop, shopDetails);
            insertBulkEmailsTemplateSetting(shop, shopDetails);
            insertCancellationManagement(shop);
            insertSmsTemplateSetting(shop);
            insertDunningManagement(shop);
            insertSubscriptionBundleSetting(shop);
            insertCartWidgetSetting(shop);
            insertBundleSetting(shop);
            insertShopAssetUrls(shop);
            insertOnboardingInfo(shop);
            try {
                shopLabelService.addDefaultLabelsForShop(shop);
            } catch (Exception ex) {
                log.info("Error while adding default shop labels for shop={}, error={}", shop, ExceptionUtils.getStackTrace(ex));
            }
            log.info("Default data inserted for shop={}", shop);
        } catch (Exception ex) {
            log.info("Error while inserting default data for shop={}, error={}", shop, ExceptionUtils.getStackTrace(ex));
        }
    }

    @Autowired
    private ShopAssetUrlsService shopAssetUrlsService;

    private void insertShopAssetUrls(String shop) {
        shopAssetUrlsService.deleteByShop(shop);

        ShopAssetUrlsDTO shopAssetUrlsDTO = new ShopAssetUrlsDTO();
        shopAssetUrlsDTO.setShop(shop);
        shopAssetUrlsService.save(shopAssetUrlsDTO);
    }

    private void insertBundleSetting(String shop) {
        bundleSettingService.deleteByShop(shop);
        BundleSettingDTO bundleSetting = new BundleSettingDTO();
        bundleSetting.setShop(shop);
        bundleSetting.setShowOnProductPage(true);
        bundleSetting.setShowProductPrice(true);
        bundleSetting.setPlacement(PlacementPosition.AFTER);
        bundleSetting.setVariant("Variant");
        bundleSetting.setDeliveryFrequency("Delivery Frequency");
        bundleSetting.setPerDelivery("/Delivery");
        bundleSetting.setDiscountPopupHeader("you got!");
        bundleSetting.setDiscountPopupCheckoutMessage("Apply Discount and go to checkout ?");
        bundleSetting.setDiscountPopupBuy("Buy Now");
        bundleSetting.setDiscountPopupNo("No");
        bundleSetting.setShowDiscountPopup(true);
        bundleSettingService.save(bundleSetting);
    }

    @Autowired
    private CancellationManagementService cancellationManagementService;

    private void insertCancellationManagement(String shop) {
        cancellationManagementService.deleteByShop(shop);

        CancellationManagementDTO cancellationManagementDTO = new CancellationManagementDTO();
        cancellationManagementDTO.setShop(shop);
        cancellationManagementDTO.setCancellationType(CancellationTypeStatus.CANCEL_IMMEDIATELY);
        cancellationManagementDTO.setCancellationInstructionsText("We certainly don't want you to cancel your subscription. How about we apply a temporary pause on the subscription of {{pauseDurationCycle}} number of cycles. After that, you can reach back to us to re-activate the subscription or you can activate the subscription from your customer portal.");
        cancellationManagementService.save(cancellationManagementDTO);
    }

    public void insertSmsTemplateSetting(String shop) {
        smsTemplateSettingService.deleteByShop(shop);

        SmsTemplateSettingDTO subscriptionCreatedSmsSetting = buildSubscriptionCreatedSmsSetting(shop);
        SmsTemplateSettingDTO transactionFailedSmsSetting = buildTransactionFailedSmsSetting(shop);
        SmsTemplateSettingDTO expiringCreditCardSmsSetting = buildExpiringCreditCardSmsSetting(shop);
        SmsTemplateSettingDTO upComingOrderSmsSetting = buildUpcomingOrderSmsSetting(shop);

        smsTemplateSettingService.save(subscriptionCreatedSmsSetting);
        smsTemplateSettingService.save(transactionFailedSmsSetting);
        smsTemplateSettingService.save(expiringCreditCardSmsSetting);
        smsTemplateSettingService.save(upComingOrderSmsSetting);
    }

    private SmsTemplateSettingDTO buildSubscriptionCreatedSmsSetting(String shop) {
        SmsTemplateSettingDTO smsTemplateSettingDTO = new SmsTemplateSettingDTO();

        smsTemplateSettingDTO.setSmsSettingType(SmsSettingType.SUBSCRIPTION_CREATED);
        smsTemplateSettingDTO.setShop(shop);
        smsTemplateSettingDTO.setSendSmsDisabled(true);
        smsTemplateSettingDTO.setStopReplySMS(false);
        smsTemplateSettingDTO.setSmsContent("Hello {{ customer.display_name }},\n" +
            "\n" +
            "Thank you for setting up a new auto delivery by subscribing to our product!\n" +
            "\n" +
            "Please visit below link to see the details of your subscription.\n" +
            "\n" +
            "Manage subscription URL : {{ manageSubscriptionUrl }}\n" +
            "\n" +
            "Thanks!\n" +
            "{{ shop.name }}");

        return smsTemplateSettingDTO;
    }

    private SmsTemplateSettingDTO buildTransactionFailedSmsSetting(String shop) {
        SmsTemplateSettingDTO smsTemplateSettingDTO = new SmsTemplateSettingDTO();

        smsTemplateSettingDTO.setSmsSettingType(SmsSettingType.TRANSACTION_FAILED);
        smsTemplateSettingDTO.setShop(shop);
        smsTemplateSettingDTO.setSendSmsDisabled(true);
        smsTemplateSettingDTO.setStopReplySMS(false);
        smsTemplateSettingDTO.setSmsContent("Hi {{ customer.display_name }},\n" +
            "\n" +
            "We are writing to let you know that the auto renew on your subscribed product with our store did not go through. Please visit the manage subscription section (link below), to update your payment details.\n" +
            "\n" +
            "Manage subscription URL : {{ manageSubscriptionUrl }}\n" +
            "\n" +
            "Sometimes issuing banks decline the charge if the name or account details entered do not match the bankÃ¢â‚¬â„¢s records.\n" +
            "\n" +
            "Thanks!\n" +
            "{{ shop.name }}");

        return smsTemplateSettingDTO;
    }

    private SmsTemplateSettingDTO buildExpiringCreditCardSmsSetting(String shop) {
        SmsTemplateSettingDTO smsTemplateSettingDTO = new SmsTemplateSettingDTO();

        smsTemplateSettingDTO.setSmsSettingType(SmsSettingType.EXPIRING_CREDIT_CARD);
        smsTemplateSettingDTO.setShop(shop);
        smsTemplateSettingDTO.setSendSmsDisabled(true);
        smsTemplateSettingDTO.setStopReplySMS(false);
        smsTemplateSettingDTO.setSmsContent("Hi {{ customer.display_name }},\n" +
            " \n" +
            "Your credit card ending in {{ lastFourDigits }} is expiring before your upcoming delivery. Please go to your manage subscription section (link below) to update your payment details.\n" +
            " \n" +
            "Manage subscription URL : {{ manageSubscriptionUrl }}");

        return smsTemplateSettingDTO;
    }

    private SmsTemplateSettingDTO buildUpcomingOrderSmsSetting(String shop) {
        SmsTemplateSettingDTO smsTemplateSettingDTO = new SmsTemplateSettingDTO();

        smsTemplateSettingDTO.setSmsSettingType(SmsSettingType.UPCOMING_ORDER);
        smsTemplateSettingDTO.setShop(shop);
        smsTemplateSettingDTO.setSendSmsDisabled(true);
        smsTemplateSettingDTO.setStopReplySMS(false);
        smsTemplateSettingDTO.setSmsContent("Hi {{ customer.display_name }},\n" +
            " \n" +
            "Your next subscription order will be automatically placed on {{ nextOrderDate }}.\n" +
            " \n" +
            "Please review your upcoming order using the manage subscription section (link below).\n" +
            "\n" +
            "Manage subscription link : {{ manageSubscriptionUrl }}\n" +
            "\n" +
            "Thanks!\n" +
            "{{ shop.name }}");

        return smsTemplateSettingDTO;
    }

    public void insertDunningManagement(String shop) {
        Optional<DunningManagementDTO> oneByShop = dunningManagementService.findOneByShop(shop);
        DunningManagementDTO dunningManagementDTO = oneByShop.orElse(new DunningManagementDTO());
        dunningManagementDTO.setShop(shop);
        dunningManagementDTO.setDaysBeforeRetrying(DaysBeforeRetrying.ONE_DAY);
        dunningManagementDTO.setMaxNumberOfFailures(MaxNumberOfFailures.CANCEL_SUBSCRIPTION);
        dunningManagementDTO.setRetryAttempts(RetryAttempts.EIGHT_ATTEMPTS);
        dunningManagementService.save(dunningManagementDTO);
    }

    public void insertEmailTemplateSetting(String shop, Shop shopDetails) throws IOException {

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingService.findByShop(shop);

        if (!CollectionUtils.isEmpty(emailTemplateSettingList)) {
            emailTemplateSettingService.deleteNotSupportedTemplates(shop);
        }

        EmailTemplateSetting subscriptionCreatedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_CREATED)
            .orElse(emailTemplateSettingService.buildSubscriptionCreatedEmailSetting(shop, shopDetails));

        EmailTemplateSetting transactionFailedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.TRANSACTION_FAILED)
            .orElse(emailTemplateSettingService.buildTransactionFailedEmailSetting(shop, shopDetails));

        EmailTemplateSetting expiringCreditCardEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.EXPIRING_CREDIT_CARD)
            .orElse(emailTemplateSettingService.buildExpiringCreditCardEmailSetting(shop, shopDetails));

        EmailTemplateSetting upComingOrderEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.UPCOMING_ORDER)
            .orElse(emailTemplateSettingService.buildUpcomingOrderEmailSetting(shop, shopDetails));

        EmailTemplateSetting shippingAddressUpdatedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SHIPPING_ADDRESS_UPDATED)
            .orElse(emailTemplateSettingService.buildShippingAddressUpdatedEmailSetting(shop, shopDetails));

        EmailTemplateSetting orderFrequencyUpdatedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.ORDER_FREQUENCY_UPDATED)
            .orElse(emailTemplateSettingService.buildOrderFrequencyUpdatedEmailSetting(shop, shopDetails));

        EmailTemplateSetting nextOrderDateUpdatedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.NEXT_ORDER_DATE_UPDATED)
            .orElse(emailTemplateSettingService.buildNextOrderDateUpdatedEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionPausedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_PAUSED)
            .orElse(emailTemplateSettingService.buildSubscriptionPausedEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionCanceledEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_CANCELLED)
            .orElse(emailTemplateSettingService.buildSubscriptionCanceledEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionResumedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_RESUMED)
            .orElse(emailTemplateSettingService.buildSubscriptionResumedEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionProductAddedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED)
            .orElse(emailTemplateSettingService.buildSubscriptionProductAddedEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionProductRemovedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED)
            .orElse(emailTemplateSettingService.buildSubscriptionProductRemovedEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionProductReplacedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED)
            .orElse(emailTemplateSettingService.buildSubscriptionProductReplacedEmailSetting(shop, shopDetails));

        EmailTemplateSetting securityChallengeEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SECURITY_CHALLENGE)
            .orElse(emailTemplateSettingService.buildSecurityChallengeEmailSetting(shop, shopDetails));

        EmailTemplateSetting subscriptionMgmtLinkEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK)
            .orElse(emailTemplateSettingService.buildSubscriptionManagementLinkEmailSetting(shop, shopDetails));

        EmailTemplateSetting orderSkippedEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.ORDER_SKIPPED)
            .orElse(emailTemplateSettingService.buildOrderSkippedEmailSetting(shop, shopDetails));

        EmailTemplateSetting outOfStockEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.OUT_OF_STOCK)
            .orElse(emailTemplateSettingService.buildEmailSettingForOutOfStock(shop, shopDetails));
        EmailTemplateSetting transactionSuccessEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.TRANSACTION_SUCCESS)
            .orElse(emailTemplateSettingService.buildTransactionSuccessEmailSetting(shop, shopDetails));

        emailTemplateSettingService.save(subscriptionCreatedEmailSetting);
        emailTemplateSettingService.save(transactionFailedEmailSetting);
        emailTemplateSettingService.save(expiringCreditCardEmailSetting);
        emailTemplateSettingService.save(upComingOrderEmailSetting);
        emailTemplateSettingService.save(shippingAddressUpdatedEmailSetting);
        emailTemplateSettingService.save(orderFrequencyUpdatedEmailSetting);
        emailTemplateSettingService.save(nextOrderDateUpdatedEmailSetting);
        emailTemplateSettingService.save(subscriptionPausedEmailSetting);
        emailTemplateSettingService.save(subscriptionCanceledEmailSetting);
        emailTemplateSettingService.save(subscriptionResumedEmailSetting);
        emailTemplateSettingService.save(subscriptionProductAddedEmailSetting);
        emailTemplateSettingService.save(subscriptionProductRemovedEmailSetting);
        emailTemplateSettingService.save(subscriptionProductReplacedEmailSetting);
        emailTemplateSettingService.save(securityChallengeEmailSetting);
        emailTemplateSettingService.save(subscriptionMgmtLinkEmailSetting);
        emailTemplateSettingService.save(orderSkippedEmailSetting);
        emailTemplateSettingService.save(outOfStockEmailSetting);
        emailTemplateSettingService.save(transactionSuccessEmailSetting);
    }


    public void insertBulkEmailsTemplateSetting(String shop, Shop shopDetails) throws IOException {

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingService.findByShop(shop);

        if (!CollectionUtils.isEmpty(emailTemplateSettingList)) {
            emailTemplateSettingService.deleteNotSupportedTemplates(shop);
        }

        EmailTemplateSetting bulkAllSubscribersEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.BULK_ALL_SUBSCRIBERS)
            .orElse(emailTemplateSettingService.buildEmailSettingForAllSubscribers(shop, shopDetails));

        EmailTemplateSetting bulkActiveSubscribersEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY)
            .orElse(emailTemplateSettingService.buildEmailSettingForActiveSubscribersOnly(shop, shopDetails));

        EmailTemplateSetting bulkPausedSubscribersEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY)
            .orElse(emailTemplateSettingService.buildEmailSettingForPausedSubscribersOnly(shop, shopDetails));

        EmailTemplateSetting bulkCancelledSubscribersEmailSetting = findByEmailType(emailTemplateSettingList, EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY)
            .orElse(emailTemplateSettingService.buildEmailSettingForCancelledSubscribersOnly(shop, shopDetails));

        emailTemplateSettingService.save(bulkAllSubscribersEmailSetting);
        emailTemplateSettingService.save(bulkActiveSubscribersEmailSetting);
        emailTemplateSettingService.save(bulkPausedSubscribersEmailSetting);
        emailTemplateSettingService.save(bulkCancelledSubscribersEmailSetting);
    }

    private Optional<EmailTemplateSetting> findByEmailType(List<EmailTemplateSetting> emailTemplateSettingList, EmailSettingType emailSettingType) {
        return emailTemplateSettingList
            .stream()
            .filter(s -> s.getEmailSettingType().equals(emailSettingType))
            .findFirst();
    }


    private void insertOnboardingInfo(String shop) {
        onboardingInfoService.deleteByShop(shop);

        Optional<OnboardingInfoDTO> onboardingInfoDTOOptional = onboardingInfoService.findByShop(shop);
        OnboardingInfoDTO onboardingInfoDTO = onboardingInfoDTOOptional.orElseGet(OnboardingInfoDTO::new);
        onboardingInfoDTO.setShop(shop);
        onboardingInfoDTO.setUncompletedChecklistSteps(List.of(OnboardingChecklistStep.values()));
        onboardingInfoDTO.setCompletedChecklistSteps(List.of());
        onboardingInfoDTO.setChecklistCompleted(false);
        onboardingInfoService.save(onboardingInfoDTO);
    }

    private void insertSubscriotionCustomCss(String shop) {

        List<SubscriptionCustomCssDTO> subscriptionCustomCssDTOList = subscriptionCustomCssService.findAllByShop(shop);

        if (subscriptionCustomCssDTOList.size() > 1) {
            subscriptionCustomCssService.deleteByShop(shop);
        }

        Optional<SubscriptionCustomCssDTO> subscriptionCustomCssDTOOptional = subscriptionCustomCssService.findByShop(shop);
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssDTOOptional.orElseGet(SubscriptionCustomCssDTO::new);
        subscriptionCustomCssDTO.setShop(shop);
        subscriptionCustomCssService.save(subscriptionCustomCssDTO);
    }

    private void insertSubscriptionWidgetSettings(String shop) {

        List<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTOList = subscriptionWidgetSettingsService.findAllByShop(shop);

        if (subscriptionWidgetSettingsDTOList.size() > 1) {
            subscriptionWidgetSettingsService.deleteByShop(shop);
        }

        Optional<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTOOptional = subscriptionWidgetSettingsService.findByShop(shop);

        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsDTOOptional.orElseGet(SubscriptionWidgetSettingsDTO::new);

        subscriptionWidgetSettingsDTO.setShop(shop);
        subscriptionWidgetSettingsDTO.setDeliveryText("delivery");
        subscriptionWidgetSettingsDTO.setOneTimePurchaseText("One Time Purchase");
        subscriptionWidgetSettingsDTO.setPurchaseOptionsText("Purchase Options");
        subscriptionWidgetSettingsDTO.setSellingPlanSelectTitle("DELIVERY FREQUENCY");
        subscriptionWidgetSettingsDTO.setSubscriptionOptionText("Subscribe and save");
        subscriptionWidgetSettingsDTO.setTooltipTitle("Subscription detail");
        subscriptionWidgetSettingsDTO.setTooltipDesctiption("<strong>Have complete control of your subscriptions</strong><br/><br/>Skip, reschedule, edit, or cancel deliveries anytime, based on your needs.");
        subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionDescription("Continue to your account to view and manage your subscriptions. Please use the same email address that you used to buy the subscription.");
        subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionTitle("Subscription");
        subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionButtonText("Manage your subscription");
        subscriptionWidgetSettingsDTO.setShowTooltipOnClick(false);
        subscriptionWidgetSettingsDTO.setSubscriptionOptionSelectedByDefault(false);
        subscriptionWidgetSettingsDTO.setWidgetEnabled(true);
        subscriptionWidgetSettingsDTO.setShowTooltip(true);
        subscriptionWidgetSettingsDTO.setSellingPlanTitleText("{{sellingPlanName}} ({{sellingPlanPrice}}/delivery)");
        subscriptionWidgetSettingsDTO.setOneTimePriceText("{{price}}");
        subscriptionWidgetSettingsDTO.setSelectedPayAsYouGoSellingPlanPriceText("{{price}}");
        subscriptionWidgetSettingsDTO.setSelectedPrepaidSellingPlanPriceText(" {{totalPrice}}");
        subscriptionWidgetSettingsDTO.setSelectedDiscountFormat("SAVE {{selectedDiscountPercentage}}");
        subscriptionWidgetSettingsDTO.setManageSubscriptionBtnFormat("<a href='" + shopInfoUtils.getManageSubscriptionUrl(shop) + "' class='appstle_manageSubBtn' ><button class='btn' style='padding: 2px 20px'>Manage Subscription</button><a><br><br>");
        subscriptionWidgetSettingsDTO.setTooltipDescriptionOnPrepaidPlan("<b>Prepaid Plan Details</b></br> Total price: {{totalPrice}} ( Price for every delivery: {{pricePerDelivery}})");
        subscriptionWidgetSettingsDTO.setTooltipDescriptionOnMultipleDiscount("<b>Discount Details</b></br> Initial discount is {{discountOne}} and then {{discountTwo}}");
        subscriptionWidgetSettingsDTO.setTooltipDescriptionCustomization("{{{defaultTooltipDescription}}} </br>  {{{prepaidDetails}}} </br> {{{discountDetails}}}");
        subscriptionWidgetSettingsDTO.setShowStaticTooltip(false);
        subscriptionWidgetSettingsDTO.setShowAppstleLink(true);
        subscriptionWidgetSettingsDTO.setShowCheckoutSubscriptionBtn(true);
        subscriptionWidgetSettingsDTO.setTotalPricePerDeliveryText("{{prepaidPerDeliveryPrice}}/delivery");
        subscriptionWidgetSettingsDTO.setWidgetEnabledOnSoldVariant(false);
        subscriptionWidgetSettingsDTO.setEnableCartWidgetFeature(false);
        subscriptionWidgetSettingsDTO.setSwitchRadioButtonWidget(true);
        subscriptionWidgetSettingsService.save(subscriptionWidgetSettingsDTO);
    }

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private CustomerPortalSettingsRepository customerPortalSettingsRepository;

    private void insertCustomerPortalSettings(String shop) {

        List<CustomerPortalSettingsDTO> customerPortalSettingsDTOList = customerPortalSettingsService.findAllByShop(shop);

        if (customerPortalSettingsDTOList.size() > 1) {
            customerPortalSettingsService.deleteByShop(shop);
        }

        Optional<CustomerPortalSettings> customerPortalSettingsOptional = customerPortalSettingsRepository.findByShop(shop);

        Optional<CustomerPortalSettingsDTO> customerPortalSettingsDTOOptional = Optional.empty();
        if (customerPortalSettingsOptional.isPresent()) {
            customerPortalSettingsDTOOptional = customerPortalSettingsService.findByShop(shop);
        }

        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsDTOOptional.orElse(new CustomerPortalSettingsDTO());
        customerPortalSettingsDTO.setOrderFrequencyText("Order frequency");
        customerPortalSettingsDTO.setTotalProductsText("Total Products");
        customerPortalSettingsDTO.setSubscriptionNoText("Subscription");
        customerPortalSettingsDTO.setNextOrderText("Next Order");
        customerPortalSettingsDTO.setStatusText("Status");
        customerPortalSettingsDTO.setShop(shop);
        customerPortalSettingsDTO.setCancelSubscriptionBtnText("Cancel Subscription");
        customerPortalSettingsDTO.setNoSubscriptionMessage("No subscriptions found. Once you subscribe to products, you can see your subscriptions here.");
        customerPortalSettingsDTO.setUpdatePaymentMessage("We've sent you an email with instructions on how to update your payment details. If you do not receive mail please try again.");
        customerPortalSettingsDTO.setCardLastFourDigitText("Card Last 4 Digit");
        customerPortalSettingsDTO.setCardExpiryText("Card Expiry");
        customerPortalSettingsDTO.setCardHolderNameText("Card Holder Name");
        customerPortalSettingsDTO.setCardTypeText("Card Type");
        customerPortalSettingsDTO.setPaymentMethodTypeText("Payment Method Type");
        customerPortalSettingsDTO.setCancelAccordionTitle("Cancel Subscription");
        customerPortalSettingsDTO.setPaymentDetailAccordionTitle("Payment Details");
        customerPortalSettingsDTO.setUpcomingOrderAccordionTitle("Upcoming Order");
        customerPortalSettingsDTO.setPaymentInfoText("Payment Info");
        customerPortalSettingsDTO.setUpdatePaymentBtnText("Update Payment");
        customerPortalSettingsDTO.setNextOrderDateLbl("Next Order Date");
        customerPortalSettingsDTO.setStatusLbl("Status");
        customerPortalSettingsDTO.setQuantityLbl("Quantity");
        customerPortalSettingsDTO.setAmountLbl("Amount");
        customerPortalSettingsDTO.setOrderNoLbl("ORDER");
        customerPortalSettingsDTO.setEditFrequencyBtnText("Edit Frequency");
        customerPortalSettingsDTO.setCancelFreqBtnText("Cancel");
        customerPortalSettingsDTO.setUpdateFreqBtnText("Update");
        customerPortalSettingsDTO.setPauseResumeSub(true);
        customerPortalSettingsDTO.setChangeNextOrderDate(true);
        customerPortalSettingsDTO.setCancelSub(true);
        customerPortalSettingsDTO.setChangeOrderFrequency(true);
        customerPortalSettingsDTO.setCreateAdditionalOrder(false);
        customerPortalSettingsDTO.setAllowOrderNow(true);
        customerPortalSettingsDTO.setManageSubscriptionButtonText("Manage Subscription");
        customerPortalSettingsDTO.setEditChangeOrderBtnText("Edit");
        customerPortalSettingsDTO.setCancelChangeOrderBtnText("Cancel");
        customerPortalSettingsDTO.setUpdateChangeOrderBtnText("Update");
        customerPortalSettingsDTO.setEditProductButtonText("Edit");
        customerPortalSettingsDTO.setDeleteButtonText("Delete");
        customerPortalSettingsDTO.setUpdateButtonText("Update");
        customerPortalSettingsDTO.setCancelButtonText("Cancel");
        customerPortalSettingsDTO.setAddProductButtonText("Add Product");
        customerPortalSettingsDTO.setAddProductLabelText("Search a product to add");
        customerPortalSettingsDTO.setActiveBadgeText("Active");
        customerPortalSettingsDTO.setCloseBadgeText("Closed");
        customerPortalSettingsDTO.setSkipOrderButtonText("Skip Order");
        customerPortalSettingsDTO.setProductLabelText("Products");
        customerPortalSettingsDTO.setSeeMoreDetailsText("See more details");
        customerPortalSettingsDTO.setHideDetailsText("Hide Details");
        customerPortalSettingsDTO.setProductInSubscriptionText("Product in this Subscription");
        customerPortalSettingsDTO.setEditQuantityLabelText("Edit Quantity");
        customerPortalSettingsDTO.setSubTotalLabelText("subtotal");
        customerPortalSettingsDTO.setPaymentNotificationText("You will receive an email from Shopify to update your payment info.");
        customerPortalSettingsDTO.setEditProductFlag(true);
        customerPortalSettingsDTO.setDeleteProductFlag(true);
        customerPortalSettingsDTO.setShowShipment(true);
        customerPortalSettingsDTO.setAddAdditionalProduct(true);
        customerPortalSettingsDTO.setMinQtyToAllowDuringAddProduct(1);
        customerPortalSettingsDTO.setSuccessText("Success");
        customerPortalSettingsDTO.setCancelSubscriptionConfirmPrepaidText("Are you sure to cancel the prepaid subscription?");
        customerPortalSettingsDTO.setCancelSubscriptionConfirmPayAsYouGoText("Are you sure to cancel the subscription?");
        customerPortalSettingsDTO.setCancelSubscriptionPrepaidButtonText("Cancel Prepaid Subscription");
        customerPortalSettingsDTO.setCancelSubscriptionPayAsYouGoButtonText("Cancel Subscription");
        customerPortalSettingsDTO.setUpcomingFulfillmentText("Upcoming Fulfillment");
        customerPortalSettingsDTO.setCreditCardText("Credit Card");
        customerPortalSettingsDTO.setEndingWithText("Ending With");
        customerPortalSettingsDTO.setWeekText("Week");
        customerPortalSettingsDTO.setDayText("Days");
        customerPortalSettingsDTO.setMonthText("Month");
        customerPortalSettingsDTO.setYearText("Year");
        customerPortalSettingsDTO.setSkipBadgeText("Skipped");
        customerPortalSettingsDTO.setQueueBadgeText("Queued");
        customerPortalSettingsDTO.setOrderNoteFlag(true);
        customerPortalSettingsDTO.setOrderNoteText("Order Note");
        customerPortalSettingsDTO.setUseUrlWithCustomerId(true);
        customerPortalSettingsDTO.setProductSelectionOption(ProductSelectionOption.PRODUCTS_FROM_ALL_PLANS);
        customerPortalSettingsDTO.setOpenBadgeText("Open");
        customerPortalSettingsDTO.setIncludeOutOfStockProduct(true);
        customerPortalSettingsDTO.setUpdateShipmentBillingDate(true);
        customerPortalSettingsDTO.setDiscountCode(true);
        customerPortalSettingsDTO.setFreezeOrderTillMinCycle(true);
        customerPortalSettingsDTO.setAddOneTimeProduct(true);
        JSONObject customerPortalSettingJson = new JSONObject();
        customerPortalSettingJson.put("shippingLabelText", "Product Shipping Info");
        customerPortalSettingJson.put("failureText", "Failure");
        customerPortalSettingJson.put("sendEmailText", "Send Email");
        customerPortalSettingJson.put("chooseDifferentProductActionText", "Choose different product Action");
        customerPortalSettingJson.put("chooseDifferentProductText", "Choose different product");
        customerPortalSettingJson.put("confirmSkipFulfillmentBtnText", "Are you sure you want to skip this order?");
        customerPortalSettingJson.put("confirmSkipOrder", "Confirm Skip Order");
        customerPortalSettingJson.put("skipFulfillmentButtonText", "Skip Fulfillment");
        customerPortalSettingJson.put("confirmCommonText", "Confirm");
        customerPortalSettingJson.put("orderNowdescriptionText", "It may take a moment for your subscription to update after a manual order is placed. Placing multiple manual orders will create multiple billing attempts.");
        customerPortalSettingJson.put("discountDetailsTitleText", "Discount Details");
        customerPortalSettingJson.put("emailAddressText", "Email address");
        customerPortalSettingJson.put("emailMagicLinkText", "Email Magic Link");
        customerPortalSettingJson.put("retriveMagicLinkText", "Retrieve Magic Link");
        customerPortalSettingJson.put("validEmailMessage", "Please enter valid email");
        customerPortalSettingJson.put("saveButtonText", "Save");
        customerPortalSettingJson.put("orderDateText", "Order Date");
        customerPortalSettingJson.put("address1LabelText", "Address Line 1");
        customerPortalSettingJson.put("address2LabelText", "Address Line 2");
        customerPortalSettingJson.put("companyLabelText", "Company");
        customerPortalSettingJson.put("cityLabelText", "City");
        customerPortalSettingJson.put("countryLabelText", "Country");
        customerPortalSettingJson.put("firstNameLabelText", "First Name");
        customerPortalSettingJson.put("lastNameLabelText", "Last Name");
        customerPortalSettingJson.put("phoneLabelText", "Phone Number");
        customerPortalSettingJson.put("provinceLabelText", "Province/State");
        customerPortalSettingJson.put("zipLabelText", "Zip");
        customerPortalSettingJson.put("addressHeaderTitleText", "Shipping Address");
        customerPortalSettingJson.put("changeShippingAddressFlag", true);
        customerPortalSettingJson.put("updateEditShippingButtonText", "Update");
        customerPortalSettingJson.put("cancelEditShippingButtonText", "Cancel");
        customerPortalSettingJson.put("pauseSubscriptionText", "Pause Subscription");
        customerPortalSettingJson.put("resumeSubscriptionText", "Resume Subscription");
        customerPortalSettingJson.put("pauseBadgeText", "PAUSED");
        customerPortalSettingJson.put("discountNoteTitle", "About Discount");
        customerPortalSettingJson.put("initialDiscountNoteDescription", "Initial price of the product is {{initialProductPrice}} <span class='badge' style='background-color:#9f5858; color: #FFF; font-size: 10px;'>{{initialDiscount}} off</span> </br>");
        customerPortalSettingJson.put("afterCycleDiscountNoteDescription", "After {{numberOfOrderCycle}} Cycle the product price will be {{afterCycleProductPrice}} <span class='badge' style='background-color:#9f5858; color: #FFF; font-size: 10px;'>{{afterCycleDiscount}} off </span>");
        customerPortalSettingJson.put("productRemovedTooltip", "Your Product has been removed. Please contact store.");
        customerPortalSettingJson.put("deliveryPriceText", "Delivery Price");
        customerPortalSettingJson.put("shippingOptionText", "Shipping Option");
        customerPortalSettingJson.put("nextDeliveryDate", "Next Delivery Date");
        customerPortalSettingJson.put("everyLabelText", "Every");
        customerPortalSettingJson.put("expiredTokenText", "Your magic link has expired. Please access Subscription Portal by logging into your account using the same email that you used to buy subscription.");
        customerPortalSettingJson.put("portalLoginLinkText", "Account Link");
        customerPortalSettingJson.put("localeDate", "en-US");
        customerPortalSettingJson.put("customerIdText", "Customer Id");
        customerPortalSettingJson.put("helloNameText", "Hello");
        customerPortalSettingJson.put("goBackButtonText", "Go Back");
        customerPortalSettingJson.put("changeVariantLabelText", "Change Variant");
        customerPortalSettingJson.put("provinceCodeLabelText", "Province Code");
        customerPortalSettingJson.put("countryCodeLabelText", "Country Code");
        customerPortalSettingJson.put("pleaseWaitLoaderText", "Please Wait..");
        customerPortalSettingJson.put("cancelSubscriptionMinimumBillingIterationsMessage", "Subscription requires minimum billing iterations of {{minCycles}} before it can be cancelled.");
        customerPortalSettingJson.put("topHtml", "");
        customerPortalSettingJson.put("bottomHtml", "");
        customerPortalSettingJson.put("discountCodeText", "Enter a discount code");
        customerPortalSettingJson.put("discountCodeApplyButtonText", "Apply");
        customerPortalSettingJson.put("applySellingPlanBasedDiscount", false);
        customerPortalSettingJson.put("applySubscriptionDiscountForOtp", false);
        customerPortalSettingJson.put("applySubscriptionDiscount", false);
        customerPortalSettingJson.put("removeDiscountCodeAutomatically", false);
        customerPortalSettingJson.put("removeDiscountCodeLabel", "This product is tied to a discount, please remove the discount before trying to remove the product.");
        customerPortalSettingJson.put("enableSplitContract", false);
        customerPortalSettingJson.put("splitContractMessage", "Once you confirm the split contract. A new, separate contract will be created.");
        customerPortalSettingJson.put("splitContractText", "Split Contract");
        customerPortalSettingJson.put("subscriptionDiscountTypeUnit", "PERCENTAGE");
        customerPortalSettingJson.put("subscriptionDiscount", 0D);
        customerPortalSettingJson.put("upSellMessage", "Upsell Discount");
        customerPortalSettingJson.put("freezeUpdateSubscriptionMessage ", "Subscription cannot be modified until {{minCycles}} orders have completed.");
        customerPortalSettingJson.put("requireFieldMessage", "Please provide proper value.");
        customerPortalSettingJson.put("validNumberRequiredMessage", "Please enter valid number");
        customerPortalSettingJson.put("variantLbl", "Variant:");
        customerPortalSettingJson.put("priceLbl", "Price:");
        customerPortalSettingJson.put("oneTimePurchaseOnlyText", "Added as one time purchase only");
        customerPortalSettingJson.put("rescheduleText", "Reschedule");
        customerPortalSettingJson.put("popUpSuccessMessage", "Success!");
        customerPortalSettingJson.put("popUpErrorMessage", "Operation Failed. Please try again.");
        customerPortalSettingJson.put("orderNowText", "Order Now");
        customerPortalSettingJson.put("upcomingOrderPlaceNowAlertText", "Are you sure that you want to replace your upcoming order now?");
        customerPortalSettingJson.put("upcomingOrderSkipAlertText", "Are you sure that you want to skip the upcoming order?");
        customerPortalSettingJson.put("deliveryFrequencyText", "Delivery Frequency");
        customerPortalSettingJson.put("editDeliveryInternalText", "Edit Delivery Interval");
        customerPortalSettingJson.put("maxCycleText", "Max Cycle");
        customerPortalSettingJson.put("minCycleText", "Min Cycle");
        customerPortalSettingJson.put("selectProductToAdd", "Please Select a product to add.");
        customerPortalSettingJson.put("searchProductBtnText", "Search Product");
        customerPortalSettingJson.put("areyousureCommonMessageText", "Are you sure?");
        customerPortalSettingJson.put("editCommonText", "Edit");
        customerPortalSettingJson.put("viewMoreText", "View more");
        customerPortalSettingJson.put("variantLblText", "Variant");
        customerPortalSettingJson.put("totalLblText", "Total");
        customerPortalSettingJson.put("deleteProductTitleText", "Delete Product");
        customerPortalSettingJson.put("greetingText", "There,");
        customerPortalSettingJson.put("productLblText", "Product");
        customerPortalSettingJson.put("hasBeenRemovedText", "has been removed");
        customerPortalSettingJson.put("orderTotalText", "Order Total");
        customerPortalSettingJson.put("addDiscountCodeText", "Add Discount Codes");
        customerPortalSettingJson.put("addDiscountCodeAlertText", "Are you sure you want to add discount code");
        customerPortalSettingJson.put("removeDiscountCodeAlertText", "Are you sure you want to remove the discount code");
        customerPortalSettingJson.put("shopPayLblText", "ShopPay");
        customerPortalSettingJson.put("paypalLblText", "Paypal");
        customerPortalSettingJson.put("unknownPaymentReachoutUsText", "Unknown. Please reach out us.");
        customerPortalSettingJson.put("addToOrderLabelText", "Add to order");
        customerPortalSettingJson.put("upcomingTabTitle", "Upcoming");
        customerPortalSettingJson.put("scheduledTabTitle", "Scheduled");
        customerPortalSettingJson.put("historyTabTitle", "History");
        customerPortalSettingJson.put("noOrderNotAvailableMessage", "No Order Note Added");
        customerPortalSettingJson.put("continueText", "Continue");
        customerPortalSettingJson.put("confirmSwapText", "Confirm Swap");
        customerPortalSettingJson.put("confirmAddProduct", "Confirm Add Product");

        customerPortalSettingJson.put("subscriptionContractFreezeMessage", "Your subscription contract has been paused.");
        customerPortalSettingJson.put("preventCancellationBeforeDaysMessage", "You can not cancel/pause the subscription before {{preventDays}} days from your next order date.");
        customerPortalSettingJson.put("discountRecurringCycleLimitOnCancellation", false);
        customerPortalSettingJson.put("discountAccordionTitle", "Apply Discount");
        customerPortalSettingJson.put("discountMessageOnCancellation", "We don't want to see you go! We would like to offer you a {{discountAmount}}% discount.");
        customerPortalSettingJson.put("discountPercentageOnCancellation", "");
        customerPortalSettingJson.put("offerDiscountOnCancellation", "");
        customerPortalSettingJson.put("enableSkipFulFillment", false);
        customerPortalSettingJson.put("magicLinkEmailFlag", false);
        customerPortalSettingJson.put("frequencyChangeWarningTitle", "Are You Sure?");
        customerPortalSettingJson.put("frequencyChangeWarningDescription", "If you change the frequency, it may change your next order date. (Also, if there is a ONE TIME PURCHASE product added in the subscription contract, we would encourage you to first remove that product then change this delivery frequency and then you can re-add the ONE TIME PURCHASE product)");
        customerPortalSettingJson.put("variantIdsToFreezeEditRemove", "");
        customerPortalSettingJson.put("preventCancellationBeforeDays", "");
        customerPortalSettingJson.put("disAllowVariantIdsForOneTimeProductAdd", "");
        customerPortalSettingJson.put("disAllowVariantIdsForSubscriptionProductAdd", "");
        customerPortalSettingJson.put("hideAddSubscriptionProductSection", false);
        customerPortalSettingJson.put("allowOnlyOneTimeProductOnAddProductFlag", true);
        customerPortalSettingJson.put("discountCouponRemoveText", "Remove");
        customerPortalSettingJson.put("pleaseSelectText", "Please select");
        customerPortalSettingJson.put("shippingAddressNotAvailableText", "Not Available");
        customerPortalSettingJson.put("discountCouponNotAppliedText", "Discount Coupon not applied");
        customerPortalSettingJson.put("sellingPlanNameText", "Selling Plan Name");
        customerPortalSettingJson.put("shopPayPaymentUpdateText", "You are using Shop Pay, please use this article for updating the payment methods. <a href='https://help.shop.app/hc/en-us/articles/4412203886996-How-do-I-manage-my-subscription-orders-with-Shop-Pay-' target='_blank'>click here</a>");
        customerPortalSettingJson.put("selectProductLabelText", "Select Product");
        customerPortalSettingJson.put("purchaseOptionLabelText", "Purchase Option");
        customerPortalSettingJson.put("finishLabelText", "Finish");
        customerPortalSettingJson.put("nextBtnText", "Next");
        customerPortalSettingJson.put("previousBtnText", "Previous");
        customerPortalSettingJson.put("closeBtnText", "Close");
        customerPortalSettingJson.put("deleteConfirmationMsgText", "Are you sure?");
        customerPortalSettingJson.put("deleteMsgText", "You are about to delete this product.");
        customerPortalSettingJson.put("yesBtnText", "Yes");
        customerPortalSettingJson.put("noBtnText", "No");
        customerPortalSettingJson.put("oneTimePurchaseNoteText", "* indicates One Time Purchase");
        customerPortalSettingJson.put("clickHereText", "Click here");
        customerPortalSettingJson.put("productAddMessageText", "to add products in this contract.");
        customerPortalSettingJson.put("choosePurchaseOptionLabelText", "Choose a Purchase Option");
        customerPortalSettingJson.put("oneTimePurchaseMessageText", "Add a product to your subscription for just one order.");
        customerPortalSettingJson.put("contractUpdateMessageText", "Your contract updated successfully");
        customerPortalSettingJson.put("oneTimePurchaseDisplayMessageText", "*One time purchase will be displayed under Upcoming order section.");
        customerPortalSettingJson.put("addProductFinishedMessageText", "Finished!");
        customerPortalSettingJson.put("contractErrorMessageText", "Error. Please contact merchant");
        customerPortalSettingJson.put("addToSubscriptionTitleCP", "Add to Subscription");
        customerPortalSettingJson.put("oneTimePurchaseTitleCP", "One Time Purchase");
        customerPortalSettingJson.put("seeMoreProductBtnText", "See More...");
        customerPortalSettingJson.put("viewAttributeLabelText", "View Attributes");
        customerPortalSettingJson.put("attributeNameLabelText", "Attribute Name");
        customerPortalSettingJson.put("swapProductLabelText", "to swap the current product.");
        customerPortalSettingJson.put("swapProductSearchBarText", "Search a product to swap");
        customerPortalSettingJson.put("enableSwapProductFeature", true);
        customerPortalSettingJson.put("enableTabletForceView", false);
        customerPortalSettingJson.put("swapProductBtnText", "Swap Product");
        customerPortalSettingJson.put("attributeValue", "Attribute Value");
        customerPortalSettingJson.put("addNewButtonText", "Add New");
        customerPortalSettingJson.put("attributeHeadingText", "Attributes");
        customerPortalSettingJson.put("enableViewAttributes", true);
        customerPortalSettingJson.put("enableEditOrderNotes", true);
        customerPortalSettingJson.put("showSellingPlanFrequencies", false);
        customerPortalSettingJson.put("totalPricePerDeliveryText", "{{prepaidPerDeliveryPrice}}/delivery");
        customerPortalSettingJson.put("fulfilledText", "fulfilled");
        customerPortalSettingJson.put("dateFormat", "dd-MM-yyyy");
        customerPortalSettingJson.put("discountCouponAppliedText", "Discount Coupon Applied");
        customerPortalSettingJson.put("subscriptionPausedMessageText", "Subscription paused");
        customerPortalSettingJson.put("subscriptionActivatedMessageText", "Subscription activated");
        customerPortalSettingJson.put("unableToUpdateSubscriptionStatusMessageText", "Unable to update subscription status");
        customerPortalSettingJson.put("selectCancellationReasonLabelText", "Select Cancellation Reason");
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessTitleText", "Successfully Updated!");
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessDescriptionText", "Your subscription has been updated! Your changes can be reviewed in the Upcoming Orders section.");
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessClosebtnText", "Okay");
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureTitleText", "Try Again!");
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureDescriptionText", "Something went wrong! Please try again.");
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureClosebtnText", "Close");

        customerPortalSettingJson.put("redeemRewardsTextV2", "Redeem Rewards");
        customerPortalSettingJson.put("rewardsTextV2", "Rewards");
        customerPortalSettingJson.put("yourRewardsTextV2", "Your Rewards");
        customerPortalSettingJson.put("yourAvailableRewardsPointsTextV2", "Your available rewards points");

        customerPortalSettingJson.put("cancellationDateTitleText", "Cancellation Date");
        customerPortalSettingJson.put("selectedCancellationReasonTitleText", "Selected Cancellation Reason");
        customerPortalSettingJson.put("cancellationNoteTitleText", "Cancellation Note");

        customerPortalSettingJson.put("selectSplitMethodLabelText", "Select Split Method");
        customerPortalSettingJson.put("splitWithOrderPlacedSelectOptionText", "Split with order placed");
        customerPortalSettingJson.put("splitWithoutOrderPlacedSelectOptionText", "Split without order placed");
        customerPortalSettingJson.put("contractCancelledBadgeText", "Contract Cancelled");

        customerPortalSettingJson.put("chooseAnotherPaymentMethodTitleText", "Choose another payment method");
        customerPortalSettingJson.put("selectPaymentMethodTitleText", "Select Payment Method");
        customerPortalSettingJson.put("changePaymentMessage", "The selected payment method has been updated successfully.");
        customerPortalSettingJson.put("updatePaymentMethodTitleText", "Update payment method.");
        customerPortalSettingJson.put("reschedulingPolicies", "");

        customerPortalSettingJson.put("allowedProductIdsForOneTimeProductAdd", "");


        customerPortalSettingsDTO.setCustomerPortalSettingJson(customerPortalSettingJson.toString());

        customerPortalSettingsService.save(customerPortalSettingsDTO);
    }

    @Autowired
    private SubscriptionBundleSettingsService subscriptionBundleSettingsService;

    private void insertSubscriptionBundleSetting(String shop) {
        subscriptionBundleSettingsService.deleteByShop(shop);

        SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO = new SubscriptionBundleSettingsDTO();
        subscriptionBundleSettingsDTO.setShop(shop);
        subscriptionBundleSettingsDTO.setSelectedFrequencyLabelText("Selected Frequency");
        subscriptionBundleSettingsDTO.setAddButtonText("Add");
        subscriptionBundleSettingsDTO.setSelectMinimumProductButtonText("Please select {{minProduct}} minimum product");
        subscriptionBundleSettingsDTO.setProductsToProceedText("Please select products to proceed");
        subscriptionBundleSettingsDTO.setProceedToCheckoutButtonText("Proceed to checkout");
        subscriptionBundleSettingsDTO.setMyDeliveryText("My {{selectedSellingPlanDisplayName}} delivery");
        subscriptionBundleSettingsDTO.setFailedToAddTitleText("Failed to add");
        subscriptionBundleSettingsDTO.setOkBtnText("Ok");
        subscriptionBundleSettingsDTO.setFailedToAddMsgText("Your cart can't have more than {{maxProduct}} products");
        subscriptionBundleSettingsDTO.setButtonColor("#ffffff");
        subscriptionBundleSettingsDTO.setBackgroundColor("#fffbe7");
        subscriptionBundleSettingsDTO.setPageBackgroundColor("#f6f6f7");
        subscriptionBundleSettingsDTO.setButtonBackgroundColor("#3a3a3a");
        subscriptionBundleSettingsDTO.setProductTitleFontColor("#3a3a3a");
        subscriptionBundleSettingsDTO.setVariantNotAvailable("Not Available");
        subscriptionBundleSettingsDTO.setBundleRedirect(BundleRedirect.CART);
        subscriptionBundleSettingsDTO.setDescriptionLength(200);
        subscriptionBundleSettingsDTO.setProductPriceFormatField("{{price}}");

        subscriptionBundleSettingsDTO.setViewProduct("View Product");
        subscriptionBundleSettingsDTO.setProductDetails("Product Details");
        subscriptionBundleSettingsDTO.setEditQuantity("Edit Quantity");
        subscriptionBundleSettingsDTO.setCart("Cart");
        subscriptionBundleSettingsDTO.setShoppingCart("Shopping Cart");

        subscriptionBundleSettingsDTO.setTitle("Build-A-Box");
        subscriptionBundleSettingsDTO.setTieredDiscount("Tiered Discount");
        subscriptionBundleSettingsDTO.setSubtotal("Subtotal");
        subscriptionBundleSettingsDTO.setCheckoutMessage("Shipping and taxes calculated at checkout.");
        subscriptionBundleSettingsDTO.setContinueShopping("Continue Shopping");
        subscriptionBundleSettingsDTO.setSpendAmountGetDiscount("Spend {{amount}} get {{percent}}% discount");
        subscriptionBundleSettingsDTO.setBuyQuantityGetDiscount("Buy {{quantity}} get {{percent}}% discount");
        subscriptionBundleSettingsDTO.setRemoveItem("Remove");
        subscriptionBundleSettingsDTO.setShowCompareAtPrice(true);
        subscriptionBundleSettingsDTO.setHideProductSearchBox(false);
        subscriptionBundleSettingsService.save(subscriptionBundleSettingsDTO);
    }

    private void insertCartWidgetSetting(String shop) {
        cartWidgetSettingsService.deleteByShop(shop);

        CartWidgetSettings cartWidgetSettings = new CartWidgetSettings();
        cartWidgetSettings.setShop(shop);
        cartWidgetSettings.setEnable_cart_widget_settings(false);
        //TODO: need to verify value whether it's null or has default value.
        cartWidgetSettings.setCartWidgetSettingApproach(CartWidgetSettingApproach.V1);
        cartWidgetSettings.setCartFormSelector("form[action-'/cart']");
        cartWidgetSettings.setAppstelCustomeSelector("[data-appstel-selector]");

        cartWidgetSettingsService.save(cartWidgetSettings);
    }

    public ShopInfoDTO insertShopInfo(String shop, Shop shopDetails) {

        List<ShopInfoDTO> shopInfoDTOList = shopInfoService.findAllByShop(shop);

        if (shopInfoDTOList.size() > 1) {
            shopInfoService.deleteByShop(shop);
        }

        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(shop);

        ShopInfoDTO shopInfo = shopInfoDTOOptional.orElse(new ShopInfoDTO());
        shopInfo.setShop(shop);
        shopInfo.setTransferOrderNotesToSubscription(true);
        shopInfo.setTransferOrderNoteAttributesToSubscription(true);
        shopInfo.setTransferOrderLineItemAttributesToSubscription(true);
        shopInfo.setDisableShippingPricingAutoCalculation(false);
        shopInfo.setStopImportProcess(false);
        shopInfo.setEnableChangeFromNextBillingDate(false);
        shopInfo.setApiKey(CommonUtils.generateRandomUid());

        String timezone = shopDetails.getTimezone();

        timezone = timezone.contains("(GMT-05:00) Eastern Time (US & Canada)") ? "(GMT-05:00) EST" : timezone;
        shopInfo.setShopTimeZone(timezone);
        shopInfo.setIanaTimeZone(shopDetails.getIanaTimezone());
        shopInfo.setManageSubscriptionsUrl("apps/subscriptions");
        shopInfo.setCustomerPortalMode(CustomerPortalMode.V3);
        shopInfo.setBuildBoxVersion(BuildBoxVersion.V2);
        shopInfo.setEnableInventoryCheck(false);

        String moneyFormat = shopDetails.getMoneyFormat();
        String currency = shopDetails.getCurrency();
        shopInfo.setMoneyFormat(moneyFormat);
        shopInfo.setCurrency(currency);
        shopInfo.setShop(shop);
        shopInfo.setPublicDomain(Optional.ofNullable(shopDetails.getDomain()).orElse(shop));
        shopInfo.setShopifyPlanDisplayName(shopDetails.getPlanDisplayName());
        shopInfo.setShopifyPlanName(shopDetails.getPlanName());
        shopInfo.setOnboardingSeen(false);
        shopInfo.setDevEnabled(false);
        shopInfo.setAdvancedCustomerPortal(false);
        shopInfo.setRecurringOrderTag("appstle_subscription_recurring_order");
        shopInfo.setFirstTimeOrderTag("appstle_subscription_first_order");
        shopInfo.setCustomerActiveSubscriptionTag("appstle_subscription_active_customer");
        shopInfo.setCustomerInActiveSubscriptionTag("appstle_subscription_inactive_customer");
        shopInfo.setCustomerPausedSubscriptionTag("appstle_subscription_paused_customer");
        shopInfo.setEnableAddJSInterceptor(false);
        shopInfo.setReBuyEnabled(false);
        shopInfo.setPriceSyncEnabled(false);
        shopInfo.setSkuSyncEnabled(false);
        shopInfo.setCountryCode(shopDetails.getCountryCode());
        shopInfo.setCountryName(shopDetails.getCountryName());
        shopInfo.setCarryForwardLastOrderNote(true);
        shopInfo.setChangeNextOrderDateOnBillingAttempt(true);
        shopInfo.setAllowLocalDelivery(true);
        shopInfo.setAllowLocalPickup(true);
        shopInfo.setUseShopifyAssets(true);
        shopInfo.setOverwriteAnchorDay(false);
        shopInfo.setEnableChangeBillingPlan(true);
        shopInfo.setEnablePauseContractsAfterMaximumOrders(true);
        return shopInfoService.save(shopInfo);
    }

    private void insertShopSettings(String shop) {

        shopSettingsService.deleteByShop(shop);

        ShopSettingsDTO shopSettingsDTO = new ShopSettingsDTO();
        shopSettingsDTO.setTaggingEnabled(true);
        shopSettingsDTO.setShop(shop);
        shopSettingsDTO.setDelayTagging(false);
        shopSettingsDTO.setOrderStatus(ShopSettingsOrderStatus.ANY);
        shopSettingsDTO.setPaymentStatus(PaymentStatus.ANY);
        shopSettingsDTO.setFulfillmentStatus(FulfillmentStatus.ANY);

        shopSettingsService.save(shopSettingsDTO);
    }

    public void createWebHooks(ShopifyAPI api) {
        String appUninstalledWebHookUrl = "https://qcjb0o5vx3.execute-api.us-west-1.amazonaws.com/prod";

        String subscriptionWebHookUrl1 = "https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/";
        String subscriptionWebHookUrl2 = "https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/";
        String subscriptionWebHookUrl3 = "https://diofxgtte3.execute-api.us-west-1.amazonaws.com/prod/";

        String subscriptionWebhookUrl = getWebhookUrl();

        webHookUtils.mayBeCreateWebHookFor(api, APP_UNINSTALLED, appUninstalledWebHookUrl);

        webHookUtils.mayBeCreateWebHookFor(api, SUBSCRIPTION_CREATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, SUBSCRIPTION_UPDATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, SUBSCRIPTION_BILLING_ATTEMPTS_SUCCESS, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, SUBSCRIPTION_BILLING_ATTEMPTS_FAILURE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, SUBSCRIPTION_BILLING_ATTEMPTS_CHALLENGED, subscriptionWebhookUrl);

        webHookUtils.mayBeCreateWebHookFor(api, CUSTOMER_PAYMENT_METHODS_CREATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, CUSTOMER_PAYMENT_METHODS_UPDATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, CUSTOMER_PAYMENT_METHODS_REVOKE, subscriptionWebhookUrl);

        webHookUtils.mayBeCreateWebHookFor(api, THEME_CREATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, THEME_UPDATE, subscriptionWebhookUrl);
        webHookUtils.mayBeCreateWebHookFor(api, THEME_PUBLISH, subscriptionWebhookUrl);

        Optional<ShopInfoDTO> shopInfoDTOOptional = shopInfoService.findByShop(api.getShopName());

        if (shopInfoDTOOptional.isPresent()) {
            ShopInfoDTO shopInfoDTO = shopInfoDTOOptional.get();
            shopInfoDTO.setShopifyWebhookUrl(subscriptionWebhookUrl);
            shopInfoService.save(shopInfoDTO);
        }
    }

    public String getWebhookUrl() {
        int i = RandomUtils.nextInt(1, 20);
        String subscriptionWebhookUrl = "";
        if (i == 1) {
            subscriptionWebhookUrl = "https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 2) {
            subscriptionWebhookUrl = "https://diofxgtte3.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 3) {
            subscriptionWebhookUrl = "https://gniqfezgd6.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 4) {
            subscriptionWebhookUrl = "https://suelatsbbj.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 5) {
            subscriptionWebhookUrl = "https://5ck69yoyik.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 6) {
            subscriptionWebhookUrl = "https://me8b3wutq9.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 7) {
            subscriptionWebhookUrl = "https://lkx82lxy4j.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 8) {
            subscriptionWebhookUrl = "https://abmipjqf49.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 9) {
            subscriptionWebhookUrl = "https://fnz8bpypm3.execute-api.us-west-1.amazonaws.com/prod";
        } else if (i == 10) {
            subscriptionWebhookUrl = "https://4ocwqfnyv0.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 11) {
            subscriptionWebhookUrl = "https://4ioos330ke.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 12) {
            subscriptionWebhookUrl = "https://4pylgr6wh7.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 13) {
            subscriptionWebhookUrl = "https://0q3mgk1b1d.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 14) {
            subscriptionWebhookUrl = "https://zyijohov59.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 15) {
            subscriptionWebhookUrl = "https://dnurnfnmp4.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 16) {
            subscriptionWebhookUrl = "https://d19lf5zev1.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 17) {
            subscriptionWebhookUrl = "https://pli08v0fyf.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 18) {
            subscriptionWebhookUrl = "https://2sj2au5dm7.execute-api.us-west-1.amazonaws.com/prod/";
        } else if (i == 19) {
            subscriptionWebhookUrl = "https://2iht129ox2.execute-api.us-west-1.amazonaws.com/prod/";
        }

        return subscriptionWebhookUrl;
    }

    private void saveSocialConnection(String accessToken, String userId, String providerId) {

        socialConnectionService.removeFromCache(userId);
        Optional<SocialConnection> optionalSocialConnection = socialConnectionService
            .findByProviderIdAndUserId(providerId, userId);

        if (optionalSocialConnection.isEmpty()) {
            SocialConnection socialConnection = new SocialConnection();
            socialConnection.setUserId(userId);
            socialConnection.setAccessToken(accessToken);
            socialConnection.setProverId(providerId);
            socialConnection.setGraphqlRateLimit(2);
            socialConnection.setRestRateLimit(2);
            socialConnectionService.save(socialConnection);
        } else {
            SocialConnection socialConnection = optionalSocialConnection.get();
            socialConnection.setAccessToken(accessToken);
            socialConnectionService.save(socialConnection);
        }
    }
}
