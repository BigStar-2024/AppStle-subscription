package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.CustomerPortalMode;
import com.et.domain.enumeration.ScriptVersion;
import com.et.domain.enumeration.BuildBoxVersion;
import com.et.domain.enumeration.OrderDayOfWeek;

/**
 * A DTO for the {@link com.et.domain.ShopInfo} entity.
 */
public class ShopInfoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private String apiKey;

    private String shopTimeZone;

    private String shopifyPlanDisplayName;

    private String shopifyPlanName;

    private Long searchResultPageId;

    private String publicDomain;

    private String moneyFormat;

    private String currency;

    private Boolean onboardingSeen;

    @NotNull
    private Boolean devEnabled;

    private String firstTimeOrderTag;

    private String recurringOrderTag;

    private String customerActiveSubscriptionTag;

    private String customerInActiveSubscriptionTag;

    private String customerPausedSubscriptionTag;

    private String emailCustomDomain;

    private String loyaltyLionToken;

    private String loyaltyLionSecret;

    private Integer loyaltyLionPoints;

    private Integer loyaltyLionMultiplier;

    private Boolean loyaltyLionEnabled;

    private String growaveClientId;

    private String growaveSecret;

    private Integer growavePoints;

    private Integer growaveMultiplier;

    private Boolean growaveEnabled;

    private String yotpoApiKey;

    private String yotpoGuid;

    private Integer yotpoPoints;

    private Integer yotpoMultiplier;

    private Boolean yotpoEnabled;

    private String klaviyoPublicApiKey;

    private String klaviyoApiKey;

    private String omniSendApiKey;

    private String manageSubscriptionsUrl;

    private String zapierSubscriptionCreatedUrl;

    private String zapierSubscriptionUpdatedUrl;

    private String zapierRecurringOrderPlacedUrl;

    private String zapierCustomerSubscriptionsUpdatedUrl;

    private String zapierNextOrderUpdatedUrl;

    private String zapierOrderFrequencyUpdatedUrl;

    private String zapierShippingAddressUpdatedUrl;

    private String zapierSubscriptionCanceledUrl;

    private String zapierSubscriptionPausedUrl;

    private String zapierSubscriptionProductAddedUrl;

    private String zapierSubscriptionProductRemovedUrl;

    private String zapierSubscriptionActivatedUrl;

    private String zapierSubscriptionTransactionFailedUrl;

    private String zapierSubscriptionUpcomingOrderUrl;

    private String zapierSubscriptionExpiringCreditCardUrl;

    private Boolean advancedCustomerPortal;

    private Boolean transferOrderNotesToSubscription;

    private Boolean transferOrderNoteAttributesToSubscription;

    private Boolean transferOrderLineItemAttributesToSubscription;

    private CustomerPortalMode customerPortalMode;

    private String recurringOrderTime;

    private Integer recurringOrderHour;

    private Integer recurringOrderMinute;

    private Double zoneOffsetHours;

    private Double zoneOffsetMinutes;

    private Integer localOrderHour;

    private Integer localOrderMinute;

    private Boolean stopImportProcess;

    private Boolean disableShippingPricingAutoCalculation;

    private Boolean allowV1FormCustomerPortal;

    private Boolean enableChangeFromNextBillingDate;

    private Boolean enableInventoryCheck;

    private Boolean whiteListed;

    private Boolean enableAddJSInterceptor;

    private Boolean reBuyEnabled;

    private Boolean priceSyncEnabled;

    private Boolean discountSyncEnabled;

    private Boolean skuSyncEnabled;

    private String klaviyoListId;

    private ScriptVersion scriptVersion;

    private BuildBoxVersion buildBoxVersion;

    private String nextOrderDateAttributeKey;

    private String nextOrderDateAttributeFormat;

    @Lob
    private String shopifyWebhookUrl;

    private Boolean verifiedEmailCustomDomain;

    private OrderDayOfWeek localOrderDayOfWeek;

    private String storeFrontAccessToken;

    private Boolean enableWebhook;

    private Boolean onBoardingPaymentSeen;

    private Boolean changeNextOrderDateOnBillingAttempt;

    private Boolean keepLineAttributes;

    private Boolean passwordEnabled;

    private String countryCode;

    private String countryName;

    @Lob
    private String overwriteSetting;

    private String mailchimpApiKey;

    private Boolean carryForwardLastOrderNote;

    private Boolean allowLocalDelivery;

    private Boolean allowLocalPickup;

    private Boolean useShopifyAssets;

    private Boolean zapietEnabled;

    private Boolean mechanicEnabled;

    private Boolean enablePauseContractsAfterMaximumOrders;

    private String appstleLoyaltyApiKey;

    private String eberLoyaltyApiKey;

    private Integer eberLoyaltyPoints;

    private Integer eberLoyaltyMultiplier;

    private Boolean shopifyFlowEnabled;

    private Boolean overwriteAnchorDay;

    private Boolean enableChangeBillingPlan;

    private String lockBillingPlanComments;

    private Boolean eberLoyaltyEnabled;

    private Integer upcomingEmailBufferDays;

    private String inventoryLocations;

    private String shipperHqApiKey;

    private String shipperHqAuthCode;

    private String shipperHqAccessToken;

    private String ianaTimeZone;

    private Boolean shipInsureEnabled;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getShopTimeZone() {
        return shopTimeZone;
    }

    public void setShopTimeZone(String shopTimeZone) {
        this.shopTimeZone = shopTimeZone;
    }

    public String getShopifyPlanDisplayName() {
        return shopifyPlanDisplayName;
    }

    public void setShopifyPlanDisplayName(String shopifyPlanDisplayName) {
        this.shopifyPlanDisplayName = shopifyPlanDisplayName;
    }

    public String getShopifyPlanName() {
        return shopifyPlanName;
    }

    public void setShopifyPlanName(String shopifyPlanName) {
        this.shopifyPlanName = shopifyPlanName;
    }

    public Long getSearchResultPageId() {
        return searchResultPageId;
    }

    public void setSearchResultPageId(Long searchResultPageId) {
        this.searchResultPageId = searchResultPageId;
    }

    public String getPublicDomain() {
        return publicDomain;
    }

    public void setPublicDomain(String publicDomain) {
        this.publicDomain = publicDomain;
    }

    public String getMoneyFormat() {
        return moneyFormat;
    }

    public void setMoneyFormat(String moneyFormat) {
        this.moneyFormat = moneyFormat;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean isOnboardingSeen() {
        return onboardingSeen;
    }

    public void setOnboardingSeen(Boolean onboardingSeen) {
        this.onboardingSeen = onboardingSeen;
    }

    public Boolean isDevEnabled() {
        return devEnabled;
    }

    public void setDevEnabled(Boolean devEnabled) {
        this.devEnabled = devEnabled;
    }

    public String getFirstTimeOrderTag() {
        return firstTimeOrderTag;
    }

    public void setFirstTimeOrderTag(String firstTimeOrderTag) {
        this.firstTimeOrderTag = firstTimeOrderTag;
    }

    public String getRecurringOrderTag() {
        return recurringOrderTag;
    }

    public void setRecurringOrderTag(String recurringOrderTag) {
        this.recurringOrderTag = recurringOrderTag;
    }

    public String getCustomerActiveSubscriptionTag() {
        return customerActiveSubscriptionTag;
    }

    public void setCustomerActiveSubscriptionTag(String customerActiveSubscriptionTag) {
        this.customerActiveSubscriptionTag = customerActiveSubscriptionTag;
    }

    public String getCustomerInActiveSubscriptionTag() {
        return customerInActiveSubscriptionTag;
    }

    public void setCustomerInActiveSubscriptionTag(String customerInActiveSubscriptionTag) {
        this.customerInActiveSubscriptionTag = customerInActiveSubscriptionTag;
    }

    public String getCustomerPausedSubscriptionTag() {
        return customerPausedSubscriptionTag;
    }

    public void setCustomerPausedSubscriptionTag(String customerPausedSubscriptionTag) {
        this.customerPausedSubscriptionTag = customerPausedSubscriptionTag;
    }

    public String getEmailCustomDomain() {
        return emailCustomDomain;
    }

    public void setEmailCustomDomain(String emailCustomDomain) {
        this.emailCustomDomain = emailCustomDomain;
    }

    public String getLoyaltyLionToken() {
        return loyaltyLionToken;
    }

    public void setLoyaltyLionToken(String loyaltyLionToken) {
        this.loyaltyLionToken = loyaltyLionToken;
    }

    public String getLoyaltyLionSecret() {
        return loyaltyLionSecret;
    }

    public void setLoyaltyLionSecret(String loyaltyLionSecret) {
        this.loyaltyLionSecret = loyaltyLionSecret;
    }

    public Integer getLoyaltyLionPoints() {
        return loyaltyLionPoints;
    }

    public void setLoyaltyLionPoints(Integer loyaltyLionPoints) {
        this.loyaltyLionPoints = loyaltyLionPoints;
    }

    public Integer getLoyaltyLionMultiplier() {
        return loyaltyLionMultiplier;
    }

    public void setLoyaltyLionMultiplier(Integer loyaltyLionMultiplier) {
        this.loyaltyLionMultiplier = loyaltyLionMultiplier;
    }

    public Boolean isLoyaltyLionEnabled() {
        return loyaltyLionEnabled;
    }

    public void setLoyaltyLionEnabled(Boolean loyaltyLionEnabled) {
        this.loyaltyLionEnabled = loyaltyLionEnabled;
    }

    public String getGrowaveClientId() {
        return growaveClientId;
    }

    public void setGrowaveClientId(String growaveClientId) {
        this.growaveClientId = growaveClientId;
    }

    public String getGrowaveSecret() {
        return growaveSecret;
    }

    public void setGrowaveSecret(String growaveSecret) {
        this.growaveSecret = growaveSecret;
    }

    public Integer getGrowavePoints() {
        return growavePoints;
    }

    public void setGrowavePoints(Integer growavePoints) {
        this.growavePoints = growavePoints;
    }

    public Integer getGrowaveMultiplier() {
        return growaveMultiplier;
    }

    public void setGrowaveMultiplier(Integer growaveMultiplier) {
        this.growaveMultiplier = growaveMultiplier;
    }

    public Boolean isGrowaveEnabled() {
        return growaveEnabled;
    }

    public void setGrowaveEnabled(Boolean growaveEnabled) {
        this.growaveEnabled = growaveEnabled;
    }

    public String getYotpoApiKey() {
        return yotpoApiKey;
    }

    public void setYotpoApiKey(String yotpoApiKey) {
        this.yotpoApiKey = yotpoApiKey;
    }

    public String getYotpoGuid() {
        return yotpoGuid;
    }

    public void setYotpoGuid(String yotpoGuid) {
        this.yotpoGuid = yotpoGuid;
    }

    public Integer getYotpoPoints() {
        return yotpoPoints;
    }

    public void setYotpoPoints(Integer yotpoPoints) {
        this.yotpoPoints = yotpoPoints;
    }

    public Integer getYotpoMultiplier() {
        return yotpoMultiplier;
    }

    public void setYotpoMultiplier(Integer yotpoMultiplier) {
        this.yotpoMultiplier = yotpoMultiplier;
    }

    public Boolean isYotpoEnabled() {
        return yotpoEnabled;
    }

    public void setYotpoEnabled(Boolean yotpoEnabled) {
        this.yotpoEnabled = yotpoEnabled;
    }

    public String getKlaviyoPublicApiKey() {
        return klaviyoPublicApiKey;
    }

    public void setKlaviyoPublicApiKey(String klaviyoPublicApiKey) {
        this.klaviyoPublicApiKey = klaviyoPublicApiKey;
    }

    public String getKlaviyoApiKey() {
        return klaviyoApiKey;
    }

    public void setKlaviyoApiKey(String klaviyoApiKey) {
        this.klaviyoApiKey = klaviyoApiKey;
    }

    public String getOmniSendApiKey() {
        return omniSendApiKey;
    }

    public void setOmniSendApiKey(String omniSendApiKey) {
        this.omniSendApiKey = omniSendApiKey;
    }

    public String getManageSubscriptionsUrl() {
        return manageSubscriptionsUrl;
    }

    public void setManageSubscriptionsUrl(String manageSubscriptionsUrl) {
        this.manageSubscriptionsUrl = manageSubscriptionsUrl;
    }

    public String getZapierSubscriptionCreatedUrl() {
        return zapierSubscriptionCreatedUrl;
    }

    public void setZapierSubscriptionCreatedUrl(String zapierSubscriptionCreatedUrl) {
        this.zapierSubscriptionCreatedUrl = zapierSubscriptionCreatedUrl;
    }

    public String getZapierSubscriptionUpdatedUrl() {
        return zapierSubscriptionUpdatedUrl;
    }

    public void setZapierSubscriptionUpdatedUrl(String zapierSubscriptionUpdatedUrl) {
        this.zapierSubscriptionUpdatedUrl = zapierSubscriptionUpdatedUrl;
    }

    public String getZapierRecurringOrderPlacedUrl() {
        return zapierRecurringOrderPlacedUrl;
    }

    public void setZapierRecurringOrderPlacedUrl(String zapierRecurringOrderPlacedUrl) {
        this.zapierRecurringOrderPlacedUrl = zapierRecurringOrderPlacedUrl;
    }

    public String getZapierCustomerSubscriptionsUpdatedUrl() {
        return zapierCustomerSubscriptionsUpdatedUrl;
    }

    public void setZapierCustomerSubscriptionsUpdatedUrl(String zapierCustomerSubscriptionsUpdatedUrl) {
        this.zapierCustomerSubscriptionsUpdatedUrl = zapierCustomerSubscriptionsUpdatedUrl;
    }

    public String getZapierNextOrderUpdatedUrl() {
        return zapierNextOrderUpdatedUrl;
    }

    public void setZapierNextOrderUpdatedUrl(String zapierNextOrderUpdatedUrl) {
        this.zapierNextOrderUpdatedUrl = zapierNextOrderUpdatedUrl;
    }

    public String getZapierOrderFrequencyUpdatedUrl() {
        return zapierOrderFrequencyUpdatedUrl;
    }

    public void setZapierOrderFrequencyUpdatedUrl(String zapierOrderFrequencyUpdatedUrl) {
        this.zapierOrderFrequencyUpdatedUrl = zapierOrderFrequencyUpdatedUrl;
    }

    public String getZapierShippingAddressUpdatedUrl() {
        return zapierShippingAddressUpdatedUrl;
    }

    public void setZapierShippingAddressUpdatedUrl(String zapierShippingAddressUpdatedUrl) {
        this.zapierShippingAddressUpdatedUrl = zapierShippingAddressUpdatedUrl;
    }

    public String getZapierSubscriptionCanceledUrl() {
        return zapierSubscriptionCanceledUrl;
    }

    public void setZapierSubscriptionCanceledUrl(String zapierSubscriptionCanceledUrl) {
        this.zapierSubscriptionCanceledUrl = zapierSubscriptionCanceledUrl;
    }

    public String getZapierSubscriptionPausedUrl() {
        return zapierSubscriptionPausedUrl;
    }

    public void setZapierSubscriptionPausedUrl(String zapierSubscriptionPausedUrl) {
        this.zapierSubscriptionPausedUrl = zapierSubscriptionPausedUrl;
    }

    public String getZapierSubscriptionProductAddedUrl() {
        return zapierSubscriptionProductAddedUrl;
    }

    public void setZapierSubscriptionProductAddedUrl(String zapierSubscriptionProductAddedUrl) {
        this.zapierSubscriptionProductAddedUrl = zapierSubscriptionProductAddedUrl;
    }

    public String getZapierSubscriptionProductRemovedUrl() {
        return zapierSubscriptionProductRemovedUrl;
    }

    public void setZapierSubscriptionProductRemovedUrl(String zapierSubscriptionProductRemovedUrl) {
        this.zapierSubscriptionProductRemovedUrl = zapierSubscriptionProductRemovedUrl;
    }

    public String getZapierSubscriptionActivatedUrl() {
        return zapierSubscriptionActivatedUrl;
    }

    public void setZapierSubscriptionActivatedUrl(String zapierSubscriptionActivatedUrl) {
        this.zapierSubscriptionActivatedUrl = zapierSubscriptionActivatedUrl;
    }

    public String getZapierSubscriptionTransactionFailedUrl() {
        return zapierSubscriptionTransactionFailedUrl;
    }

    public void setZapierSubscriptionTransactionFailedUrl(String zapierSubscriptionTransactionFailedUrl) {
        this.zapierSubscriptionTransactionFailedUrl = zapierSubscriptionTransactionFailedUrl;
    }

    public String getZapierSubscriptionUpcomingOrderUrl() {
        return zapierSubscriptionUpcomingOrderUrl;
    }

    public void setZapierSubscriptionUpcomingOrderUrl(String zapierSubscriptionUpcomingOrderUrl) {
        this.zapierSubscriptionUpcomingOrderUrl = zapierSubscriptionUpcomingOrderUrl;
    }

    public String getZapierSubscriptionExpiringCreditCardUrl() {
        return zapierSubscriptionExpiringCreditCardUrl;
    }

    public void setZapierSubscriptionExpiringCreditCardUrl(String zapierSubscriptionExpiringCreditCardUrl) {
        this.zapierSubscriptionExpiringCreditCardUrl = zapierSubscriptionExpiringCreditCardUrl;
    }

    public Boolean isAdvancedCustomerPortal() {
        return advancedCustomerPortal;
    }

    public void setAdvancedCustomerPortal(Boolean advancedCustomerPortal) {
        this.advancedCustomerPortal = advancedCustomerPortal;
    }

    public Boolean isTransferOrderNotesToSubscription() {
        return transferOrderNotesToSubscription;
    }

    public void setTransferOrderNotesToSubscription(Boolean transferOrderNotesToSubscription) {
        this.transferOrderNotesToSubscription = transferOrderNotesToSubscription;
    }

    public Boolean isTransferOrderNoteAttributesToSubscription() {
        return transferOrderNoteAttributesToSubscription;
    }

    public void setTransferOrderNoteAttributesToSubscription(Boolean transferOrderNoteAttributesToSubscription) {
        this.transferOrderNoteAttributesToSubscription = transferOrderNoteAttributesToSubscription;
    }

    public Boolean isTransferOrderLineItemAttributesToSubscription() {
        return transferOrderLineItemAttributesToSubscription;
    }

    public void setTransferOrderLineItemAttributesToSubscription(Boolean transferOrderLineItemAttributesToSubscription) {
        this.transferOrderLineItemAttributesToSubscription = transferOrderLineItemAttributesToSubscription;
    }

    public CustomerPortalMode getCustomerPortalMode() {
        return customerPortalMode;
    }

    public void setCustomerPortalMode(CustomerPortalMode customerPortalMode) {
        this.customerPortalMode = customerPortalMode;
    }

    public String getRecurringOrderTime() {
        return recurringOrderTime;
    }

    public void setRecurringOrderTime(String recurringOrderTime) {
        this.recurringOrderTime = recurringOrderTime;
    }

    public Integer getRecurringOrderHour() {
        return recurringOrderHour;
    }

    public void setRecurringOrderHour(Integer recurringOrderHour) {
        this.recurringOrderHour = recurringOrderHour;
    }

    public Integer getRecurringOrderMinute() {
        return recurringOrderMinute;
    }

    public void setRecurringOrderMinute(Integer recurringOrderMinute) {
        this.recurringOrderMinute = recurringOrderMinute;
    }

    public Double getZoneOffsetHours() {
        return zoneOffsetHours;
    }

    public void setZoneOffsetHours(Double zoneOffsetHours) {
        this.zoneOffsetHours = zoneOffsetHours;
    }

    public Double getZoneOffsetMinutes() {
        return zoneOffsetMinutes;
    }

    public void setZoneOffsetMinutes(Double zoneOffsetMinutes) {
        this.zoneOffsetMinutes = zoneOffsetMinutes;
    }

    public Integer getLocalOrderHour() {
        return localOrderHour;
    }

    public void setLocalOrderHour(Integer localOrderHour) {
        this.localOrderHour = localOrderHour;
    }

    public Integer getLocalOrderMinute() {
        return localOrderMinute;
    }

    public void setLocalOrderMinute(Integer localOrderMinute) {
        this.localOrderMinute = localOrderMinute;
    }

    public Boolean isStopImportProcess() {
        return stopImportProcess;
    }

    public void setStopImportProcess(Boolean stopImportProcess) {
        this.stopImportProcess = stopImportProcess;
    }

    public Boolean isDisableShippingPricingAutoCalculation() {
        return disableShippingPricingAutoCalculation;
    }

    public void setDisableShippingPricingAutoCalculation(Boolean disableShippingPricingAutoCalculation) {
        this.disableShippingPricingAutoCalculation = disableShippingPricingAutoCalculation;
    }

    public Boolean isAllowV1FormCustomerPortal() {
        return allowV1FormCustomerPortal;
    }

    public void setAllowV1FormCustomerPortal(Boolean allowV1FormCustomerPortal) {
        this.allowV1FormCustomerPortal = allowV1FormCustomerPortal;
    }

    public Boolean isEnableChangeFromNextBillingDate() {
        return enableChangeFromNextBillingDate;
    }

    public void setEnableChangeFromNextBillingDate(Boolean enableChangeFromNextBillingDate) {
        this.enableChangeFromNextBillingDate = enableChangeFromNextBillingDate;
    }

    public Boolean isEnableInventoryCheck() {
        return enableInventoryCheck;
    }

    public void setEnableInventoryCheck(Boolean enableInventoryCheck) {
        this.enableInventoryCheck = enableInventoryCheck;
    }

    public Boolean isWhiteListed() {
        return whiteListed;
    }

    public void setWhiteListed(Boolean whiteListed) {
        this.whiteListed = whiteListed;
    }

    public Boolean isEnableAddJSInterceptor() {
        return enableAddJSInterceptor;
    }

    public void setEnableAddJSInterceptor(Boolean enableAddJSInterceptor) {
        this.enableAddJSInterceptor = enableAddJSInterceptor;
    }

    public Boolean isReBuyEnabled() {
        return reBuyEnabled;
    }

    public void setReBuyEnabled(Boolean reBuyEnabled) {
        this.reBuyEnabled = reBuyEnabled;
    }

    public Boolean isPriceSyncEnabled() {
        return priceSyncEnabled;
    }

    public void setPriceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
    }

    public Boolean isDiscountSyncEnabled() {
        return discountSyncEnabled;
    }

    public void setDiscountSyncEnabled(Boolean discountSyncEnabled) {
        this.discountSyncEnabled = discountSyncEnabled;
    }

    public Boolean isSkuSyncEnabled() {
        return skuSyncEnabled;
    }

    public void setSkuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
    }

    public String getKlaviyoListId() {
        return klaviyoListId;
    }

    public void setKlaviyoListId(String klaviyoListId) {
        this.klaviyoListId = klaviyoListId;
    }

    public ScriptVersion getScriptVersion() {
        return scriptVersion;
    }

    public void setScriptVersion(ScriptVersion scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public BuildBoxVersion getBuildBoxVersion() {
        return buildBoxVersion;
    }

    public void setBuildBoxVersion(BuildBoxVersion buildBoxVersion) {
        this.buildBoxVersion = buildBoxVersion;
    }

    public String getNextOrderDateAttributeKey() {
        return nextOrderDateAttributeKey;
    }

    public void setNextOrderDateAttributeKey(String nextOrderDateAttributeKey) {
        this.nextOrderDateAttributeKey = nextOrderDateAttributeKey;
    }

    public String getNextOrderDateAttributeFormat() {
        return nextOrderDateAttributeFormat;
    }

    public void setNextOrderDateAttributeFormat(String nextOrderDateAttributeFormat) {
        this.nextOrderDateAttributeFormat = nextOrderDateAttributeFormat;
    }

    public String getShopifyWebhookUrl() {
        return shopifyWebhookUrl;
    }

    public void setShopifyWebhookUrl(String shopifyWebhookUrl) {
        this.shopifyWebhookUrl = shopifyWebhookUrl;
    }

    public Boolean isVerifiedEmailCustomDomain() {
        return verifiedEmailCustomDomain;
    }

    public void setVerifiedEmailCustomDomain(Boolean verifiedEmailCustomDomain) {
        this.verifiedEmailCustomDomain = verifiedEmailCustomDomain;
    }

    public OrderDayOfWeek getLocalOrderDayOfWeek() {
        return localOrderDayOfWeek;
    }

    public void setLocalOrderDayOfWeek(OrderDayOfWeek localOrderDayOfWeek) {
        this.localOrderDayOfWeek = localOrderDayOfWeek;
    }

    public String getStoreFrontAccessToken() {
        return storeFrontAccessToken;
    }

    public void setStoreFrontAccessToken(String storeFrontAccessToken) {
        this.storeFrontAccessToken = storeFrontAccessToken;
    }

    public Boolean isEnableWebhook() {
        return enableWebhook;
    }

    public void setEnableWebhook(Boolean enableWebhook) {
        this.enableWebhook = enableWebhook;
    }

    public Boolean isOnBoardingPaymentSeen() {
        return onBoardingPaymentSeen;
    }

    public void setOnBoardingPaymentSeen(Boolean onBoardingPaymentSeen) {
        this.onBoardingPaymentSeen = onBoardingPaymentSeen;
    }

    public Boolean isChangeNextOrderDateOnBillingAttempt() {
        return changeNextOrderDateOnBillingAttempt;
    }

    public void setChangeNextOrderDateOnBillingAttempt(Boolean changeNextOrderDateOnBillingAttempt) {
        this.changeNextOrderDateOnBillingAttempt = changeNextOrderDateOnBillingAttempt;
    }

    public Boolean isKeepLineAttributes() {
        return keepLineAttributes;
    }

    public void setKeepLineAttributes(Boolean keepLineAttributes) {
        this.keepLineAttributes = keepLineAttributes;
    }

    public Boolean isPasswordEnabled() {
        return passwordEnabled;
    }

    public void setPasswordEnabled(Boolean passwordEnabled) {
        this.passwordEnabled = passwordEnabled;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getOverwriteSetting() {
        return overwriteSetting;
    }

    public void setOverwriteSetting(String overwriteSetting) {
        this.overwriteSetting = overwriteSetting;
    }

    public String getMailchimpApiKey() {
        return mailchimpApiKey;
    }

    public void setMailchimpApiKey(String mailchimpApiKey) {
        this.mailchimpApiKey = mailchimpApiKey;
    }

    public Boolean isCarryForwardLastOrderNote() {
        return carryForwardLastOrderNote;
    }

    public void setCarryForwardLastOrderNote(Boolean carryForwardLastOrderNote) {
        this.carryForwardLastOrderNote = carryForwardLastOrderNote;
    }

    public Boolean isAllowLocalDelivery() {
        return allowLocalDelivery;
    }

    public void setAllowLocalDelivery(Boolean allowLocalDelivery) {
        this.allowLocalDelivery = allowLocalDelivery;
    }

    public Boolean isAllowLocalPickup() {
        return allowLocalPickup;
    }

    public void setAllowLocalPickup(Boolean allowLocalPickup) {
        this.allowLocalPickup = allowLocalPickup;
    }

    public Boolean isUseShopifyAssets() {
        return useShopifyAssets;
    }

    public void setUseShopifyAssets(Boolean useShopifyAssets) {
        this.useShopifyAssets = useShopifyAssets;
    }

    public Boolean isZapietEnabled() {
        return zapietEnabled;
    }

    public void setZapietEnabled(Boolean zapietEnabled) {
        this.zapietEnabled = zapietEnabled;
    }

    public Boolean isMechanicEnabled() {
        return mechanicEnabled;
    }

    public void setMechanicEnabled(Boolean mechanicEnabled) {
        this.mechanicEnabled = mechanicEnabled;
    }

    public Boolean isEnablePauseContractsAfterMaximumOrders() {
        return enablePauseContractsAfterMaximumOrders;
    }

    public void setEnablePauseContractsAfterMaximumOrders(Boolean enablePauseContractsAfterMaximumOrders) {
        this.enablePauseContractsAfterMaximumOrders = enablePauseContractsAfterMaximumOrders;
    }

    public String getAppstleLoyaltyApiKey() {
        return appstleLoyaltyApiKey;
    }

    public void setAppstleLoyaltyApiKey(String appstleLoyaltyApiKey) {
        this.appstleLoyaltyApiKey = appstleLoyaltyApiKey;
    }

    public String getEberLoyaltyApiKey() {
        return eberLoyaltyApiKey;
    }

    public void setEberLoyaltyApiKey(String eberLoyaltyApiKey) {
        this.eberLoyaltyApiKey = eberLoyaltyApiKey;
    }

    public Integer getEberLoyaltyPoints() {
        return eberLoyaltyPoints;
    }

    public void setEberLoyaltyPoints(Integer eberLoyaltyPoints) {
        this.eberLoyaltyPoints = eberLoyaltyPoints;
    }

    public Integer getEberLoyaltyMultiplier() {
        return eberLoyaltyMultiplier;
    }

    public void setEberLoyaltyMultiplier(Integer eberLoyaltyMultiplier) {
        this.eberLoyaltyMultiplier = eberLoyaltyMultiplier;
    }

    public Boolean isShopifyFlowEnabled() {
        return shopifyFlowEnabled;
    }

    public void setShopifyFlowEnabled(Boolean shopifyFlowEnabled) {
        this.shopifyFlowEnabled = shopifyFlowEnabled;
    }

    public Boolean isOverwriteAnchorDay() {
        return overwriteAnchorDay;
    }

    public void setOverwriteAnchorDay(Boolean overwriteAnchorDay) {
        this.overwriteAnchorDay = overwriteAnchorDay;
    }

    public Boolean isEnableChangeBillingPlan() {
        return enableChangeBillingPlan;
    }

    public void setEnableChangeBillingPlan(Boolean enableChangeBillingPlan) {
        this.enableChangeBillingPlan = enableChangeBillingPlan;
    }

    public String getLockBillingPlanComments() {
        return lockBillingPlanComments;
    }

    public void setLockBillingPlanComments(String lockBillingPlanComments) {
        this.lockBillingPlanComments = lockBillingPlanComments;
    }

    public Boolean isEberLoyaltyEnabled() {
        return eberLoyaltyEnabled;
    }

    public void setEberLoyaltyEnabled(Boolean eberLoyaltyEnabled) {
        this.eberLoyaltyEnabled = eberLoyaltyEnabled;
    }

    public Integer getUpcomingEmailBufferDays() {
        return upcomingEmailBufferDays;
    }

    public void setUpcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
    }

    public String getInventoryLocations() {
        return inventoryLocations;
    }

    public void setInventoryLocations(String inventoryLocations) {
        this.inventoryLocations = inventoryLocations;
    }

    public String getShipperHqApiKey() {
        return shipperHqApiKey;
    }

    public void setShipperHqApiKey(String shipperHqApiKey) {
        this.shipperHqApiKey = shipperHqApiKey;
    }

    public String getShipperHqAuthCode() {
        return shipperHqAuthCode;
    }

    public void setShipperHqAuthCode(String shipperHqAuthCode) {
        this.shipperHqAuthCode = shipperHqAuthCode;
    }

    public String getShipperHqAccessToken() {
        return shipperHqAccessToken;
    }

    public void setShipperHqAccessToken(String shipperHqAccessToken) {
        this.shipperHqAccessToken = shipperHqAccessToken;
    }

    public String getIanaTimeZone() {
        return ianaTimeZone;
    }

    public void setIanaTimeZone(String ianaTimeZone) {
        this.ianaTimeZone = ianaTimeZone;
    }

    public Boolean isShipInsureEnabled() {
        return shipInsureEnabled;
    }

    public void setShipInsureEnabled(Boolean shipInsureEnabled) {
        this.shipInsureEnabled = shipInsureEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((ShopInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", shopTimeZone='" + getShopTimeZone() + "'" +
            ", shopifyPlanDisplayName='" + getShopifyPlanDisplayName() + "'" +
            ", shopifyPlanName='" + getShopifyPlanName() + "'" +
            ", searchResultPageId=" + getSearchResultPageId() +
            ", publicDomain='" + getPublicDomain() + "'" +
            ", moneyFormat='" + getMoneyFormat() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", onboardingSeen='" + isOnboardingSeen() + "'" +
            ", devEnabled='" + isDevEnabled() + "'" +
            ", firstTimeOrderTag='" + getFirstTimeOrderTag() + "'" +
            ", recurringOrderTag='" + getRecurringOrderTag() + "'" +
            ", customerActiveSubscriptionTag='" + getCustomerActiveSubscriptionTag() + "'" +
            ", customerInActiveSubscriptionTag='" + getCustomerInActiveSubscriptionTag() + "'" +
            ", customerPausedSubscriptionTag='" + getCustomerPausedSubscriptionTag() + "'" +
            ", emailCustomDomain='" + getEmailCustomDomain() + "'" +
            ", loyaltyLionToken='" + getLoyaltyLionToken() + "'" +
            ", loyaltyLionSecret='" + getLoyaltyLionSecret() + "'" +
            ", loyaltyLionPoints=" + getLoyaltyLionPoints() +
            ", loyaltyLionMultiplier=" + getLoyaltyLionMultiplier() +
            ", loyaltyLionEnabled='" + isLoyaltyLionEnabled() + "'" +
            ", growaveClientId='" + getGrowaveClientId() + "'" +
            ", growaveSecret='" + getGrowaveSecret() + "'" +
            ", growavePoints=" + getGrowavePoints() +
            ", growaveMultiplier=" + getGrowaveMultiplier() +
            ", growaveEnabled='" + isGrowaveEnabled() + "'" +
            ", yotpoApiKey='" + getYotpoApiKey() + "'" +
            ", yotpoGuid='" + getYotpoGuid() + "'" +
            ", yotpoPoints=" + getYotpoPoints() +
            ", yotpoMultiplier=" + getYotpoMultiplier() +
            ", yotpoEnabled='" + isYotpoEnabled() + "'" +
            ", klaviyoPublicApiKey='" + getKlaviyoPublicApiKey() + "'" +
            ", klaviyoApiKey='" + getKlaviyoApiKey() + "'" +
            ", omniSendApiKey='" + getOmniSendApiKey() + "'" +
            ", manageSubscriptionsUrl='" + getManageSubscriptionsUrl() + "'" +
            ", zapierSubscriptionCreatedUrl='" + getZapierSubscriptionCreatedUrl() + "'" +
            ", zapierSubscriptionUpdatedUrl='" + getZapierSubscriptionUpdatedUrl() + "'" +
            ", zapierRecurringOrderPlacedUrl='" + getZapierRecurringOrderPlacedUrl() + "'" +
            ", zapierCustomerSubscriptionsUpdatedUrl='" + getZapierCustomerSubscriptionsUpdatedUrl() + "'" +
            ", zapierNextOrderUpdatedUrl='" + getZapierNextOrderUpdatedUrl() + "'" +
            ", zapierOrderFrequencyUpdatedUrl='" + getZapierOrderFrequencyUpdatedUrl() + "'" +
            ", zapierShippingAddressUpdatedUrl='" + getZapierShippingAddressUpdatedUrl() + "'" +
            ", zapierSubscriptionCanceledUrl='" + getZapierSubscriptionCanceledUrl() + "'" +
            ", zapierSubscriptionPausedUrl='" + getZapierSubscriptionPausedUrl() + "'" +
            ", zapierSubscriptionProductAddedUrl='" + getZapierSubscriptionProductAddedUrl() + "'" +
            ", zapierSubscriptionProductRemovedUrl='" + getZapierSubscriptionProductRemovedUrl() + "'" +
            ", zapierSubscriptionActivatedUrl='" + getZapierSubscriptionActivatedUrl() + "'" +
            ", zapierSubscriptionTransactionFailedUrl='" + getZapierSubscriptionTransactionFailedUrl() + "'" +
            ", zapierSubscriptionUpcomingOrderUrl='" + getZapierSubscriptionUpcomingOrderUrl() + "'" +
            ", zapierSubscriptionExpiringCreditCardUrl='" + getZapierSubscriptionExpiringCreditCardUrl() + "'" +
            ", advancedCustomerPortal='" + isAdvancedCustomerPortal() + "'" +
            ", transferOrderNotesToSubscription='" + isTransferOrderNotesToSubscription() + "'" +
            ", transferOrderNoteAttributesToSubscription='" + isTransferOrderNoteAttributesToSubscription() + "'" +
            ", transferOrderLineItemAttributesToSubscription='" + isTransferOrderLineItemAttributesToSubscription() + "'" +
            ", customerPortalMode='" + getCustomerPortalMode() + "'" +
            ", recurringOrderTime='" + getRecurringOrderTime() + "'" +
            ", recurringOrderHour=" + getRecurringOrderHour() +
            ", recurringOrderMinute=" + getRecurringOrderMinute() +
            ", zoneOffsetHours=" + getZoneOffsetHours() +
            ", zoneOffsetMinutes=" + getZoneOffsetMinutes() +
            ", localOrderHour=" + getLocalOrderHour() +
            ", localOrderMinute=" + getLocalOrderMinute() +
            ", stopImportProcess='" + isStopImportProcess() + "'" +
            ", disableShippingPricingAutoCalculation='" + isDisableShippingPricingAutoCalculation() + "'" +
            ", allowV1FormCustomerPortal='" + isAllowV1FormCustomerPortal() + "'" +
            ", enableChangeFromNextBillingDate='" + isEnableChangeFromNextBillingDate() + "'" +
            ", enableInventoryCheck='" + isEnableInventoryCheck() + "'" +
            ", whiteListed='" + isWhiteListed() + "'" +
            ", enableAddJSInterceptor='" + isEnableAddJSInterceptor() + "'" +
            ", reBuyEnabled='" + isReBuyEnabled() + "'" +
            ", priceSyncEnabled='" + isPriceSyncEnabled() + "'" +
            ", discountSyncEnabled='" + isDiscountSyncEnabled() + "'" +
            ", skuSyncEnabled='" + isSkuSyncEnabled() + "'" +
            ", klaviyoListId='" + getKlaviyoListId() + "'" +
            ", scriptVersion='" + getScriptVersion() + "'" +
            ", buildBoxVersion='" + getBuildBoxVersion() + "'" +
            ", nextOrderDateAttributeKey='" + getNextOrderDateAttributeKey() + "'" +
            ", nextOrderDateAttributeFormat='" + getNextOrderDateAttributeFormat() + "'" +
            ", shopifyWebhookUrl='" + getShopifyWebhookUrl() + "'" +
            ", verifiedEmailCustomDomain='" + isVerifiedEmailCustomDomain() + "'" +
            ", localOrderDayOfWeek='" + getLocalOrderDayOfWeek() + "'" +
            ", storeFrontAccessToken='" + getStoreFrontAccessToken() + "'" +
            ", enableWebhook='" + isEnableWebhook() + "'" +
            ", onBoardingPaymentSeen='" + isOnBoardingPaymentSeen() + "'" +
            ", changeNextOrderDateOnBillingAttempt='" + isChangeNextOrderDateOnBillingAttempt() + "'" +
            ", keepLineAttributes='" + isKeepLineAttributes() + "'" +
            ", passwordEnabled='" + isPasswordEnabled() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            ", overwriteSetting='" + getOverwriteSetting() + "'" +
            ", mailchimpApiKey='" + getMailchimpApiKey() + "'" +
            ", carryForwardLastOrderNote='" + isCarryForwardLastOrderNote() + "'" +
            ", allowLocalDelivery='" + isAllowLocalDelivery() + "'" +
            ", allowLocalPickup='" + isAllowLocalPickup() + "'" +
            ", useShopifyAssets='" + isUseShopifyAssets() + "'" +
            ", zapietEnabled='" + isZapietEnabled() + "'" +
            ", mechanicEnabled='" + isMechanicEnabled() + "'" +
            ", enablePauseContractsAfterMaximumOrders='" + isEnablePauseContractsAfterMaximumOrders() + "'" +
            ", appstleLoyaltyApiKey='" + getAppstleLoyaltyApiKey() + "'" +
            ", eberLoyaltyApiKey='" + getEberLoyaltyApiKey() + "'" +
            ", eberLoyaltyPoints=" + getEberLoyaltyPoints() +
            ", eberLoyaltyMultiplier=" + getEberLoyaltyMultiplier() +
            ", shopifyFlowEnabled='" + isShopifyFlowEnabled() + "'" +
            ", overwriteAnchorDay='" + isOverwriteAnchorDay() + "'" +
            ", enableChangeBillingPlan='" + isEnableChangeBillingPlan() + "'" +
            ", lockBillingPlanComments='" + getLockBillingPlanComments() + "'" +
            ", eberLoyaltyEnabled='" + isEberLoyaltyEnabled() + "'" +
            ", upcomingEmailBufferDays=" + getUpcomingEmailBufferDays() +
            ", inventoryLocations='" + getInventoryLocations() + "'" +
            ", shipperHqApiKey='" + getShipperHqApiKey() + "'" +
            ", shipperHqAuthCode='" + getShipperHqAuthCode() + "'" +
            ", shipperHqAccessToken='" + getShipperHqAccessToken() + "'" +
            ", ianaTimeZone='" + getIanaTimeZone() + "'" +
            ", shipInsureEnabled='" + isShipInsureEnabled() + "'" +
            "}";
    }
}
