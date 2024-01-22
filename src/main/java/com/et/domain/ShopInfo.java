package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.CustomerPortalMode;

import com.et.domain.enumeration.ScriptVersion;

import com.et.domain.enumeration.BuildBoxVersion;

import com.et.domain.enumeration.OrderDayOfWeek;

/**
 * A ShopInfo.
 */
@Entity
@Table(name = "shop_info")
public class ShopInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "shop_time_zone")
    private String shopTimeZone;

    @Column(name = "shopify_plan_display_name")
    private String shopifyPlanDisplayName;

    @Column(name = "shopify_plan_name")
    private String shopifyPlanName;

    @Column(name = "search_result_page_id")
    private Long searchResultPageId;

    @Column(name = "public_domain")
    private String publicDomain;

    @Column(name = "money_format")
    private String moneyFormat;

    @Column(name = "currency")
    private String currency;

    @Column(name = "onboarding_seen")
    private Boolean onboardingSeen;

    @NotNull
    @Column(name = "dev_enabled", nullable = false)
    private Boolean devEnabled;

    @Column(name = "first_time_order_tag")
    private String firstTimeOrderTag;

    @Column(name = "recurring_order_tag")
    private String recurringOrderTag;

    @Column(name = "customer_active_subscription_tag")
    private String customerActiveSubscriptionTag;

    @Column(name = "customer_in_active_subscription_tag")
    private String customerInActiveSubscriptionTag;

    @Column(name = "customer_paused_subscription_tag")
    private String customerPausedSubscriptionTag;

    @Column(name = "email_custom_domain")
    private String emailCustomDomain;

    @Column(name = "loyalty_lion_token")
    private String loyaltyLionToken;

    @Column(name = "loyalty_lion_secret")
    private String loyaltyLionSecret;

    @Column(name = "loyalty_lion_points")
    private Integer loyaltyLionPoints;

    @Column(name = "loyalty_lion_multiplier")
    private Integer loyaltyLionMultiplier;

    @Column(name = "loyalty_lion_enabled")
    private Boolean loyaltyLionEnabled;

    @Column(name = "growave_client_id")
    private String growaveClientId;

    @Column(name = "growave_secret")
    private String growaveSecret;

    @Column(name = "growave_points")
    private Integer growavePoints;

    @Column(name = "growave_multiplier")
    private Integer growaveMultiplier;

    @Column(name = "growave_enabled")
    private Boolean growaveEnabled;

    @Column(name = "yotpo_api_key")
    private String yotpoApiKey;

    @Column(name = "yotpo_guid")
    private String yotpoGuid;

    @Column(name = "yotpo_points")
    private Integer yotpoPoints;

    @Column(name = "yotpo_multiplier")
    private Integer yotpoMultiplier;

    @Column(name = "yotpo_enabled")
    private Boolean yotpoEnabled;

    @Column(name = "klaviyo_public_api_key")
    private String klaviyoPublicApiKey;

    @Column(name = "klaviyo_api_key")
    private String klaviyoApiKey;

    @Column(name = "omni_send_api_key")
    private String omniSendApiKey;

    @Column(name = "manage_subscriptions_url")
    private String manageSubscriptionsUrl;

    @Column(name = "zapier_subscription_created_url")
    private String zapierSubscriptionCreatedUrl;

    @Column(name = "zapier_subscription_updated_url")
    private String zapierSubscriptionUpdatedUrl;

    @Column(name = "zapier_recurring_order_placed_url")
    private String zapierRecurringOrderPlacedUrl;

    @Column(name = "zapier_customer_subscriptions_updated_url")
    private String zapierCustomerSubscriptionsUpdatedUrl;

    @Column(name = "zapier_next_order_updated_url")
    private String zapierNextOrderUpdatedUrl;

    @Column(name = "zapier_order_frequency_updated_url")
    private String zapierOrderFrequencyUpdatedUrl;

    @Column(name = "zapier_shipping_address_updated_url")
    private String zapierShippingAddressUpdatedUrl;

    @Column(name = "zapier_subscription_canceled_url")
    private String zapierSubscriptionCanceledUrl;

    @Column(name = "zapier_subscription_paused_url")
    private String zapierSubscriptionPausedUrl;

    @Column(name = "zapier_subscription_product_added_url")
    private String zapierSubscriptionProductAddedUrl;

    @Column(name = "zapier_subscription_product_removed_url")
    private String zapierSubscriptionProductRemovedUrl;

    @Column(name = "zapier_subscription_activated_url")
    private String zapierSubscriptionActivatedUrl;

    @Column(name = "zapier_subscription_transaction_failed_url")
    private String zapierSubscriptionTransactionFailedUrl;

    @Column(name = "zapier_subscription_upcoming_order_url")
    private String zapierSubscriptionUpcomingOrderUrl;

    @Column(name = "zapier_subscription_expiring_credit_card_url")
    private String zapierSubscriptionExpiringCreditCardUrl;

    @Column(name = "advanced_customer_portal")
    private Boolean advancedCustomerPortal;

    @Column(name = "transfer_order_notes_to_subscription")
    private Boolean transferOrderNotesToSubscription;

    @Column(name = "transfer_order_note_attributes_to_subscription")
    private Boolean transferOrderNoteAttributesToSubscription;

    @Column(name = "transfer_order_line_item_attributes_to_subscription")
    private Boolean transferOrderLineItemAttributesToSubscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_portal_mode")
    private CustomerPortalMode customerPortalMode;

    @Column(name = "recurring_order_time")
    private String recurringOrderTime;

    @Column(name = "recurring_order_hour")
    private Integer recurringOrderHour;

    @Column(name = "recurring_order_minute")
    private Integer recurringOrderMinute;

    @Column(name = "zone_offset_hours")
    private Double zoneOffsetHours;

    @Column(name = "zone_offset_minutes")
    private Double zoneOffsetMinutes;

    @Column(name = "local_order_hour")
    private Integer localOrderHour;

    @Column(name = "local_order_minute")
    private Integer localOrderMinute;

    @Column(name = "stop_import_process")
    private Boolean stopImportProcess;

    @Column(name = "disable_shipping_pricing_auto_calculation")
    private Boolean disableShippingPricingAutoCalculation;

    @Column(name = "allow_v_1_form_customer_portal")
    private Boolean allowV1FormCustomerPortal;

    @Column(name = "enable_change_from_next_billing_date")
    private Boolean enableChangeFromNextBillingDate;

    @Column(name = "enable_inventory_check")
    private Boolean enableInventoryCheck;

    @Column(name = "white_listed")
    private Boolean whiteListed;

    @Column(name = "enable_add_js_interceptor")
    private Boolean enableAddJSInterceptor;

    @Column(name = "re_buy_enabled")
    private Boolean reBuyEnabled;

    @Column(name = "price_sync_enabled")
    private Boolean priceSyncEnabled;

    @Column(name = "discount_sync_enabled")
    private Boolean discountSyncEnabled;

    @Column(name = "sku_sync_enabled")
    private Boolean skuSyncEnabled;

    @Column(name = "klaviyo_list_id")
    private String klaviyoListId;

    @Enumerated(EnumType.STRING)
    @Column(name = "script_version")
    private ScriptVersion scriptVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "build_box_version")
    private BuildBoxVersion buildBoxVersion;

    @Column(name = "next_order_date_attribute_key")
    private String nextOrderDateAttributeKey;

    @Column(name = "next_order_date_attribute_format")
    private String nextOrderDateAttributeFormat;

    @Lob
    @Column(name = "shopify_webhook_url")
    private String shopifyWebhookUrl;

    @Column(name = "verified_email_custom_domain")
    private Boolean verifiedEmailCustomDomain;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_order_day_of_week")
    private OrderDayOfWeek localOrderDayOfWeek;

    @Column(name = "store_front_access_token")
    private String storeFrontAccessToken;

    @Column(name = "enable_webhook")
    private Boolean enableWebhook;

    @Column(name = "on_boarding_payment_seen")
    private Boolean onBoardingPaymentSeen;

    @Column(name = "change_next_order_date_on_billing_attempt")
    private Boolean changeNextOrderDateOnBillingAttempt;

    @Column(name = "keep_line_attributes")
    private Boolean keepLineAttributes;

    @Column(name = "password_enabled")
    private Boolean passwordEnabled = false;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Lob
    @Column(name = "overwrite_setting")
    private String overwriteSetting;

    @Column(name = "mailchimp_api_key")
    private String mailchimpApiKey;

    @Column(name = "carry_forward_last_order_note")
    private Boolean carryForwardLastOrderNote;

    @Column(name = "allow_local_delivery")
    private Boolean allowLocalDelivery;

    @Column(name = "allow_local_pickup")
    private Boolean allowLocalPickup;

    @Column(name = "use_shopify_assets")
    private Boolean useShopifyAssets;

    @Column(name = "zapiet_enabled")
    private Boolean zapietEnabled;

    @Column(name = "mechanic_enabled")
    private Boolean mechanicEnabled;

    @Column(name = "enable_pause_contracts_after_maximum_orders")
    private Boolean enablePauseContractsAfterMaximumOrders;

    @Column(name = "appstle_loyalty_api_key")
    private String appstleLoyaltyApiKey;

    @Column(name = "eber_loyalty_api_key")
    private String eberLoyaltyApiKey;

    @Column(name = "eber_loyalty_points")
    private Integer eberLoyaltyPoints;

    @Column(name = "eber_loyalty_multiplier")
    private Integer eberLoyaltyMultiplier;

    @Column(name = "shopify_flow_enabled")
    private Boolean shopifyFlowEnabled;

    @Column(name = "overwrite_anchor_day")
    private Boolean overwriteAnchorDay;

    @Column(name = "enable_change_billing_plan")
    private Boolean enableChangeBillingPlan;

    @Column(name = "lock_billing_plan_comments")
    private String lockBillingPlanComments;

    @Column(name = "eber_loyalty_enabled")
    private Boolean eberLoyaltyEnabled;

    @Column(name = "upcoming_email_buffer_days")
    private Integer upcomingEmailBufferDays;

    @Column(name = "inventory_locations")
    private String inventoryLocations;

    @Column(name = "shipper_hq_api_key")
    private String shipperHqApiKey;

    @Column(name = "shipper_hq_auth_code")
    private String shipperHqAuthCode;

    @Column(name = "shipper_hq_access_token")
    private String shipperHqAccessToken;

    @Column(name = "iana_time_zone")
    private String ianaTimeZone;

    @Column(name = "ship_insure_enabled")
    private Boolean shipInsureEnabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public ShopInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getApiKey() {
        return apiKey;
    }

    public ShopInfo apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getShopTimeZone() {
        return shopTimeZone;
    }

    public ShopInfo shopTimeZone(String shopTimeZone) {
        this.shopTimeZone = shopTimeZone;
        return this;
    }

    public void setShopTimeZone(String shopTimeZone) {
        this.shopTimeZone = shopTimeZone;
    }

    public String getShopifyPlanDisplayName() {
        return shopifyPlanDisplayName;
    }

    public ShopInfo shopifyPlanDisplayName(String shopifyPlanDisplayName) {
        this.shopifyPlanDisplayName = shopifyPlanDisplayName;
        return this;
    }

    public void setShopifyPlanDisplayName(String shopifyPlanDisplayName) {
        this.shopifyPlanDisplayName = shopifyPlanDisplayName;
    }

    public String getShopifyPlanName() {
        return shopifyPlanName;
    }

    public ShopInfo shopifyPlanName(String shopifyPlanName) {
        this.shopifyPlanName = shopifyPlanName;
        return this;
    }

    public void setShopifyPlanName(String shopifyPlanName) {
        this.shopifyPlanName = shopifyPlanName;
    }

    public Long getSearchResultPageId() {
        return searchResultPageId;
    }

    public ShopInfo searchResultPageId(Long searchResultPageId) {
        this.searchResultPageId = searchResultPageId;
        return this;
    }

    public void setSearchResultPageId(Long searchResultPageId) {
        this.searchResultPageId = searchResultPageId;
    }

    public String getPublicDomain() {
        return publicDomain;
    }

    public ShopInfo publicDomain(String publicDomain) {
        this.publicDomain = publicDomain;
        return this;
    }

    public void setPublicDomain(String publicDomain) {
        this.publicDomain = publicDomain;
    }

    public String getMoneyFormat() {
        return moneyFormat;
    }

    public ShopInfo moneyFormat(String moneyFormat) {
        this.moneyFormat = moneyFormat;
        return this;
    }

    public void setMoneyFormat(String moneyFormat) {
        this.moneyFormat = moneyFormat;
    }

    public String getCurrency() {
        return currency;
    }

    public ShopInfo currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean isOnboardingSeen() {
        return onboardingSeen;
    }

    public ShopInfo onboardingSeen(Boolean onboardingSeen) {
        this.onboardingSeen = onboardingSeen;
        return this;
    }

    public void setOnboardingSeen(Boolean onboardingSeen) {
        this.onboardingSeen = onboardingSeen;
    }

    public Boolean isDevEnabled() {
        return devEnabled;
    }

    public ShopInfo devEnabled(Boolean devEnabled) {
        this.devEnabled = devEnabled;
        return this;
    }

    public void setDevEnabled(Boolean devEnabled) {
        this.devEnabled = devEnabled;
    }

    public String getFirstTimeOrderTag() {
        return firstTimeOrderTag;
    }

    public ShopInfo firstTimeOrderTag(String firstTimeOrderTag) {
        this.firstTimeOrderTag = firstTimeOrderTag;
        return this;
    }

    public void setFirstTimeOrderTag(String firstTimeOrderTag) {
        this.firstTimeOrderTag = firstTimeOrderTag;
    }

    public String getRecurringOrderTag() {
        return recurringOrderTag;
    }

    public ShopInfo recurringOrderTag(String recurringOrderTag) {
        this.recurringOrderTag = recurringOrderTag;
        return this;
    }

    public void setRecurringOrderTag(String recurringOrderTag) {
        this.recurringOrderTag = recurringOrderTag;
    }

    public String getCustomerActiveSubscriptionTag() {
        return customerActiveSubscriptionTag;
    }

    public ShopInfo customerActiveSubscriptionTag(String customerActiveSubscriptionTag) {
        this.customerActiveSubscriptionTag = customerActiveSubscriptionTag;
        return this;
    }

    public void setCustomerActiveSubscriptionTag(String customerActiveSubscriptionTag) {
        this.customerActiveSubscriptionTag = customerActiveSubscriptionTag;
    }

    public String getCustomerInActiveSubscriptionTag() {
        return customerInActiveSubscriptionTag;
    }

    public ShopInfo customerInActiveSubscriptionTag(String customerInActiveSubscriptionTag) {
        this.customerInActiveSubscriptionTag = customerInActiveSubscriptionTag;
        return this;
    }

    public void setCustomerInActiveSubscriptionTag(String customerInActiveSubscriptionTag) {
        this.customerInActiveSubscriptionTag = customerInActiveSubscriptionTag;
    }

    public String getCustomerPausedSubscriptionTag() {
        return customerPausedSubscriptionTag;
    }

    public ShopInfo customerPausedSubscriptionTag(String customerPausedSubscriptionTag) {
        this.customerPausedSubscriptionTag = customerPausedSubscriptionTag;
        return this;
    }

    public void setCustomerPausedSubscriptionTag(String customerPausedSubscriptionTag) {
        this.customerPausedSubscriptionTag = customerPausedSubscriptionTag;
    }

    public String getEmailCustomDomain() {
        return emailCustomDomain;
    }

    public ShopInfo emailCustomDomain(String emailCustomDomain) {
        this.emailCustomDomain = emailCustomDomain;
        return this;
    }

    public void setEmailCustomDomain(String emailCustomDomain) {
        this.emailCustomDomain = emailCustomDomain;
    }

    public String getLoyaltyLionToken() {
        return loyaltyLionToken;
    }

    public ShopInfo loyaltyLionToken(String loyaltyLionToken) {
        this.loyaltyLionToken = loyaltyLionToken;
        return this;
    }

    public void setLoyaltyLionToken(String loyaltyLionToken) {
        this.loyaltyLionToken = loyaltyLionToken;
    }

    public String getLoyaltyLionSecret() {
        return loyaltyLionSecret;
    }

    public ShopInfo loyaltyLionSecret(String loyaltyLionSecret) {
        this.loyaltyLionSecret = loyaltyLionSecret;
        return this;
    }

    public void setLoyaltyLionSecret(String loyaltyLionSecret) {
        this.loyaltyLionSecret = loyaltyLionSecret;
    }

    public Integer getLoyaltyLionPoints() {
        return loyaltyLionPoints;
    }

    public ShopInfo loyaltyLionPoints(Integer loyaltyLionPoints) {
        this.loyaltyLionPoints = loyaltyLionPoints;
        return this;
    }

    public void setLoyaltyLionPoints(Integer loyaltyLionPoints) {
        this.loyaltyLionPoints = loyaltyLionPoints;
    }

    public Integer getLoyaltyLionMultiplier() {
        return loyaltyLionMultiplier;
    }

    public ShopInfo loyaltyLionMultiplier(Integer loyaltyLionMultiplier) {
        this.loyaltyLionMultiplier = loyaltyLionMultiplier;
        return this;
    }

    public void setLoyaltyLionMultiplier(Integer loyaltyLionMultiplier) {
        this.loyaltyLionMultiplier = loyaltyLionMultiplier;
    }

    public Boolean isLoyaltyLionEnabled() {
        return loyaltyLionEnabled;
    }

    public ShopInfo loyaltyLionEnabled(Boolean loyaltyLionEnabled) {
        this.loyaltyLionEnabled = loyaltyLionEnabled;
        return this;
    }

    public void setLoyaltyLionEnabled(Boolean loyaltyLionEnabled) {
        this.loyaltyLionEnabled = loyaltyLionEnabled;
    }

    public String getGrowaveClientId() {
        return growaveClientId;
    }

    public ShopInfo growaveClientId(String growaveClientId) {
        this.growaveClientId = growaveClientId;
        return this;
    }

    public void setGrowaveClientId(String growaveClientId) {
        this.growaveClientId = growaveClientId;
    }

    public String getGrowaveSecret() {
        return growaveSecret;
    }

    public ShopInfo growaveSecret(String growaveSecret) {
        this.growaveSecret = growaveSecret;
        return this;
    }

    public void setGrowaveSecret(String growaveSecret) {
        this.growaveSecret = growaveSecret;
    }

    public Integer getGrowavePoints() {
        return growavePoints;
    }

    public ShopInfo growavePoints(Integer growavePoints) {
        this.growavePoints = growavePoints;
        return this;
    }

    public void setGrowavePoints(Integer growavePoints) {
        this.growavePoints = growavePoints;
    }

    public Integer getGrowaveMultiplier() {
        return growaveMultiplier;
    }

    public ShopInfo growaveMultiplier(Integer growaveMultiplier) {
        this.growaveMultiplier = growaveMultiplier;
        return this;
    }

    public void setGrowaveMultiplier(Integer growaveMultiplier) {
        this.growaveMultiplier = growaveMultiplier;
    }

    public Boolean isGrowaveEnabled() {
        return growaveEnabled;
    }

    public ShopInfo growaveEnabled(Boolean growaveEnabled) {
        this.growaveEnabled = growaveEnabled;
        return this;
    }

    public void setGrowaveEnabled(Boolean growaveEnabled) {
        this.growaveEnabled = growaveEnabled;
    }

    public String getYotpoApiKey() {
        return yotpoApiKey;
    }

    public ShopInfo yotpoApiKey(String yotpoApiKey) {
        this.yotpoApiKey = yotpoApiKey;
        return this;
    }

    public void setYotpoApiKey(String yotpoApiKey) {
        this.yotpoApiKey = yotpoApiKey;
    }

    public String getYotpoGuid() {
        return yotpoGuid;
    }

    public ShopInfo yotpoGuid(String yotpoGuid) {
        this.yotpoGuid = yotpoGuid;
        return this;
    }

    public void setYotpoGuid(String yotpoGuid) {
        this.yotpoGuid = yotpoGuid;
    }

    public Integer getYotpoPoints() {
        return yotpoPoints;
    }

    public ShopInfo yotpoPoints(Integer yotpoPoints) {
        this.yotpoPoints = yotpoPoints;
        return this;
    }

    public void setYotpoPoints(Integer yotpoPoints) {
        this.yotpoPoints = yotpoPoints;
    }

    public Integer getYotpoMultiplier() {
        return yotpoMultiplier;
    }

    public ShopInfo yotpoMultiplier(Integer yotpoMultiplier) {
        this.yotpoMultiplier = yotpoMultiplier;
        return this;
    }

    public void setYotpoMultiplier(Integer yotpoMultiplier) {
        this.yotpoMultiplier = yotpoMultiplier;
    }

    public Boolean isYotpoEnabled() {
        return yotpoEnabled;
    }

    public ShopInfo yotpoEnabled(Boolean yotpoEnabled) {
        this.yotpoEnabled = yotpoEnabled;
        return this;
    }

    public void setYotpoEnabled(Boolean yotpoEnabled) {
        this.yotpoEnabled = yotpoEnabled;
    }

    public String getKlaviyoPublicApiKey() {
        return klaviyoPublicApiKey;
    }

    public ShopInfo klaviyoPublicApiKey(String klaviyoPublicApiKey) {
        this.klaviyoPublicApiKey = klaviyoPublicApiKey;
        return this;
    }

    public void setKlaviyoPublicApiKey(String klaviyoPublicApiKey) {
        this.klaviyoPublicApiKey = klaviyoPublicApiKey;
    }

    public String getKlaviyoApiKey() {
        return klaviyoApiKey;
    }

    public ShopInfo klaviyoApiKey(String klaviyoApiKey) {
        this.klaviyoApiKey = klaviyoApiKey;
        return this;
    }

    public void setKlaviyoApiKey(String klaviyoApiKey) {
        this.klaviyoApiKey = klaviyoApiKey;
    }

    public String getOmniSendApiKey() {
        return omniSendApiKey;
    }

    public ShopInfo omniSendApiKey(String omniSendApiKey) {
        this.omniSendApiKey = omniSendApiKey;
        return this;
    }

    public void setOmniSendApiKey(String omniSendApiKey) {
        this.omniSendApiKey = omniSendApiKey;
    }

    public String getManageSubscriptionsUrl() {
        return manageSubscriptionsUrl;
    }

    public ShopInfo manageSubscriptionsUrl(String manageSubscriptionsUrl) {
        this.manageSubscriptionsUrl = manageSubscriptionsUrl;
        return this;
    }

    public void setManageSubscriptionsUrl(String manageSubscriptionsUrl) {
        this.manageSubscriptionsUrl = manageSubscriptionsUrl;
    }

    public String getZapierSubscriptionCreatedUrl() {
        return zapierSubscriptionCreatedUrl;
    }

    public ShopInfo zapierSubscriptionCreatedUrl(String zapierSubscriptionCreatedUrl) {
        this.zapierSubscriptionCreatedUrl = zapierSubscriptionCreatedUrl;
        return this;
    }

    public void setZapierSubscriptionCreatedUrl(String zapierSubscriptionCreatedUrl) {
        this.zapierSubscriptionCreatedUrl = zapierSubscriptionCreatedUrl;
    }

    public String getZapierSubscriptionUpdatedUrl() {
        return zapierSubscriptionUpdatedUrl;
    }

    public ShopInfo zapierSubscriptionUpdatedUrl(String zapierSubscriptionUpdatedUrl) {
        this.zapierSubscriptionUpdatedUrl = zapierSubscriptionUpdatedUrl;
        return this;
    }

    public void setZapierSubscriptionUpdatedUrl(String zapierSubscriptionUpdatedUrl) {
        this.zapierSubscriptionUpdatedUrl = zapierSubscriptionUpdatedUrl;
    }

    public String getZapierRecurringOrderPlacedUrl() {
        return zapierRecurringOrderPlacedUrl;
    }

    public ShopInfo zapierRecurringOrderPlacedUrl(String zapierRecurringOrderPlacedUrl) {
        this.zapierRecurringOrderPlacedUrl = zapierRecurringOrderPlacedUrl;
        return this;
    }

    public void setZapierRecurringOrderPlacedUrl(String zapierRecurringOrderPlacedUrl) {
        this.zapierRecurringOrderPlacedUrl = zapierRecurringOrderPlacedUrl;
    }

    public String getZapierCustomerSubscriptionsUpdatedUrl() {
        return zapierCustomerSubscriptionsUpdatedUrl;
    }

    public ShopInfo zapierCustomerSubscriptionsUpdatedUrl(String zapierCustomerSubscriptionsUpdatedUrl) {
        this.zapierCustomerSubscriptionsUpdatedUrl = zapierCustomerSubscriptionsUpdatedUrl;
        return this;
    }

    public void setZapierCustomerSubscriptionsUpdatedUrl(String zapierCustomerSubscriptionsUpdatedUrl) {
        this.zapierCustomerSubscriptionsUpdatedUrl = zapierCustomerSubscriptionsUpdatedUrl;
    }

    public String getZapierNextOrderUpdatedUrl() {
        return zapierNextOrderUpdatedUrl;
    }

    public ShopInfo zapierNextOrderUpdatedUrl(String zapierNextOrderUpdatedUrl) {
        this.zapierNextOrderUpdatedUrl = zapierNextOrderUpdatedUrl;
        return this;
    }

    public void setZapierNextOrderUpdatedUrl(String zapierNextOrderUpdatedUrl) {
        this.zapierNextOrderUpdatedUrl = zapierNextOrderUpdatedUrl;
    }

    public String getZapierOrderFrequencyUpdatedUrl() {
        return zapierOrderFrequencyUpdatedUrl;
    }

    public ShopInfo zapierOrderFrequencyUpdatedUrl(String zapierOrderFrequencyUpdatedUrl) {
        this.zapierOrderFrequencyUpdatedUrl = zapierOrderFrequencyUpdatedUrl;
        return this;
    }

    public void setZapierOrderFrequencyUpdatedUrl(String zapierOrderFrequencyUpdatedUrl) {
        this.zapierOrderFrequencyUpdatedUrl = zapierOrderFrequencyUpdatedUrl;
    }

    public String getZapierShippingAddressUpdatedUrl() {
        return zapierShippingAddressUpdatedUrl;
    }

    public ShopInfo zapierShippingAddressUpdatedUrl(String zapierShippingAddressUpdatedUrl) {
        this.zapierShippingAddressUpdatedUrl = zapierShippingAddressUpdatedUrl;
        return this;
    }

    public void setZapierShippingAddressUpdatedUrl(String zapierShippingAddressUpdatedUrl) {
        this.zapierShippingAddressUpdatedUrl = zapierShippingAddressUpdatedUrl;
    }

    public String getZapierSubscriptionCanceledUrl() {
        return zapierSubscriptionCanceledUrl;
    }

    public ShopInfo zapierSubscriptionCanceledUrl(String zapierSubscriptionCanceledUrl) {
        this.zapierSubscriptionCanceledUrl = zapierSubscriptionCanceledUrl;
        return this;
    }

    public void setZapierSubscriptionCanceledUrl(String zapierSubscriptionCanceledUrl) {
        this.zapierSubscriptionCanceledUrl = zapierSubscriptionCanceledUrl;
    }

    public String getZapierSubscriptionPausedUrl() {
        return zapierSubscriptionPausedUrl;
    }

    public ShopInfo zapierSubscriptionPausedUrl(String zapierSubscriptionPausedUrl) {
        this.zapierSubscriptionPausedUrl = zapierSubscriptionPausedUrl;
        return this;
    }

    public void setZapierSubscriptionPausedUrl(String zapierSubscriptionPausedUrl) {
        this.zapierSubscriptionPausedUrl = zapierSubscriptionPausedUrl;
    }

    public String getZapierSubscriptionProductAddedUrl() {
        return zapierSubscriptionProductAddedUrl;
    }

    public ShopInfo zapierSubscriptionProductAddedUrl(String zapierSubscriptionProductAddedUrl) {
        this.zapierSubscriptionProductAddedUrl = zapierSubscriptionProductAddedUrl;
        return this;
    }

    public void setZapierSubscriptionProductAddedUrl(String zapierSubscriptionProductAddedUrl) {
        this.zapierSubscriptionProductAddedUrl = zapierSubscriptionProductAddedUrl;
    }

    public String getZapierSubscriptionProductRemovedUrl() {
        return zapierSubscriptionProductRemovedUrl;
    }

    public ShopInfo zapierSubscriptionProductRemovedUrl(String zapierSubscriptionProductRemovedUrl) {
        this.zapierSubscriptionProductRemovedUrl = zapierSubscriptionProductRemovedUrl;
        return this;
    }

    public void setZapierSubscriptionProductRemovedUrl(String zapierSubscriptionProductRemovedUrl) {
        this.zapierSubscriptionProductRemovedUrl = zapierSubscriptionProductRemovedUrl;
    }

    public String getZapierSubscriptionActivatedUrl() {
        return zapierSubscriptionActivatedUrl;
    }

    public ShopInfo zapierSubscriptionActivatedUrl(String zapierSubscriptionActivatedUrl) {
        this.zapierSubscriptionActivatedUrl = zapierSubscriptionActivatedUrl;
        return this;
    }

    public void setZapierSubscriptionActivatedUrl(String zapierSubscriptionActivatedUrl) {
        this.zapierSubscriptionActivatedUrl = zapierSubscriptionActivatedUrl;
    }

    public String getZapierSubscriptionTransactionFailedUrl() {
        return zapierSubscriptionTransactionFailedUrl;
    }

    public ShopInfo zapierSubscriptionTransactionFailedUrl(String zapierSubscriptionTransactionFailedUrl) {
        this.zapierSubscriptionTransactionFailedUrl = zapierSubscriptionTransactionFailedUrl;
        return this;
    }

    public void setZapierSubscriptionTransactionFailedUrl(String zapierSubscriptionTransactionFailedUrl) {
        this.zapierSubscriptionTransactionFailedUrl = zapierSubscriptionTransactionFailedUrl;
    }

    public String getZapierSubscriptionUpcomingOrderUrl() {
        return zapierSubscriptionUpcomingOrderUrl;
    }

    public ShopInfo zapierSubscriptionUpcomingOrderUrl(String zapierSubscriptionUpcomingOrderUrl) {
        this.zapierSubscriptionUpcomingOrderUrl = zapierSubscriptionUpcomingOrderUrl;
        return this;
    }

    public void setZapierSubscriptionUpcomingOrderUrl(String zapierSubscriptionUpcomingOrderUrl) {
        this.zapierSubscriptionUpcomingOrderUrl = zapierSubscriptionUpcomingOrderUrl;
    }

    public String getZapierSubscriptionExpiringCreditCardUrl() {
        return zapierSubscriptionExpiringCreditCardUrl;
    }

    public ShopInfo zapierSubscriptionExpiringCreditCardUrl(String zapierSubscriptionExpiringCreditCardUrl) {
        this.zapierSubscriptionExpiringCreditCardUrl = zapierSubscriptionExpiringCreditCardUrl;
        return this;
    }

    public void setZapierSubscriptionExpiringCreditCardUrl(String zapierSubscriptionExpiringCreditCardUrl) {
        this.zapierSubscriptionExpiringCreditCardUrl = zapierSubscriptionExpiringCreditCardUrl;
    }

    public Boolean isAdvancedCustomerPortal() {
        return advancedCustomerPortal;
    }

    public ShopInfo advancedCustomerPortal(Boolean advancedCustomerPortal) {
        this.advancedCustomerPortal = advancedCustomerPortal;
        return this;
    }

    public void setAdvancedCustomerPortal(Boolean advancedCustomerPortal) {
        this.advancedCustomerPortal = advancedCustomerPortal;
    }

    public Boolean isTransferOrderNotesToSubscription() {
        return transferOrderNotesToSubscription;
    }

    public ShopInfo transferOrderNotesToSubscription(Boolean transferOrderNotesToSubscription) {
        this.transferOrderNotesToSubscription = transferOrderNotesToSubscription;
        return this;
    }

    public void setTransferOrderNotesToSubscription(Boolean transferOrderNotesToSubscription) {
        this.transferOrderNotesToSubscription = transferOrderNotesToSubscription;
    }

    public Boolean isTransferOrderNoteAttributesToSubscription() {
        return transferOrderNoteAttributesToSubscription;
    }

    public ShopInfo transferOrderNoteAttributesToSubscription(Boolean transferOrderNoteAttributesToSubscription) {
        this.transferOrderNoteAttributesToSubscription = transferOrderNoteAttributesToSubscription;
        return this;
    }

    public void setTransferOrderNoteAttributesToSubscription(Boolean transferOrderNoteAttributesToSubscription) {
        this.transferOrderNoteAttributesToSubscription = transferOrderNoteAttributesToSubscription;
    }

    public Boolean isTransferOrderLineItemAttributesToSubscription() {
        return transferOrderLineItemAttributesToSubscription;
    }

    public ShopInfo transferOrderLineItemAttributesToSubscription(Boolean transferOrderLineItemAttributesToSubscription) {
        this.transferOrderLineItemAttributesToSubscription = transferOrderLineItemAttributesToSubscription;
        return this;
    }

    public void setTransferOrderLineItemAttributesToSubscription(Boolean transferOrderLineItemAttributesToSubscription) {
        this.transferOrderLineItemAttributesToSubscription = transferOrderLineItemAttributesToSubscription;
    }

    public CustomerPortalMode getCustomerPortalMode() {
        return customerPortalMode;
    }

    public ShopInfo customerPortalMode(CustomerPortalMode customerPortalMode) {
        this.customerPortalMode = customerPortalMode;
        return this;
    }

    public void setCustomerPortalMode(CustomerPortalMode customerPortalMode) {
        this.customerPortalMode = customerPortalMode;
    }

    public String getRecurringOrderTime() {
        return recurringOrderTime;
    }

    public ShopInfo recurringOrderTime(String recurringOrderTime) {
        this.recurringOrderTime = recurringOrderTime;
        return this;
    }

    public void setRecurringOrderTime(String recurringOrderTime) {
        this.recurringOrderTime = recurringOrderTime;
    }

    public Integer getRecurringOrderHour() {
        return recurringOrderHour;
    }

    public ShopInfo recurringOrderHour(Integer recurringOrderHour) {
        this.recurringOrderHour = recurringOrderHour;
        return this;
    }

    public void setRecurringOrderHour(Integer recurringOrderHour) {
        this.recurringOrderHour = recurringOrderHour;
    }

    public Integer getRecurringOrderMinute() {
        return recurringOrderMinute;
    }

    public ShopInfo recurringOrderMinute(Integer recurringOrderMinute) {
        this.recurringOrderMinute = recurringOrderMinute;
        return this;
    }

    public void setRecurringOrderMinute(Integer recurringOrderMinute) {
        this.recurringOrderMinute = recurringOrderMinute;
    }

    public Double getZoneOffsetHours() {
        return zoneOffsetHours;
    }

    public ShopInfo zoneOffsetHours(Double zoneOffsetHours) {
        this.zoneOffsetHours = zoneOffsetHours;
        return this;
    }

    public void setZoneOffsetHours(Double zoneOffsetHours) {
        this.zoneOffsetHours = zoneOffsetHours;
    }

    public Double getZoneOffsetMinutes() {
        return zoneOffsetMinutes;
    }

    public ShopInfo zoneOffsetMinutes(Double zoneOffsetMinutes) {
        this.zoneOffsetMinutes = zoneOffsetMinutes;
        return this;
    }

    public void setZoneOffsetMinutes(Double zoneOffsetMinutes) {
        this.zoneOffsetMinutes = zoneOffsetMinutes;
    }

    public Integer getLocalOrderHour() {
        return localOrderHour;
    }

    public ShopInfo localOrderHour(Integer localOrderHour) {
        this.localOrderHour = localOrderHour;
        return this;
    }

    public void setLocalOrderHour(Integer localOrderHour) {
        this.localOrderHour = localOrderHour;
    }

    public Integer getLocalOrderMinute() {
        return localOrderMinute;
    }

    public ShopInfo localOrderMinute(Integer localOrderMinute) {
        this.localOrderMinute = localOrderMinute;
        return this;
    }

    public void setLocalOrderMinute(Integer localOrderMinute) {
        this.localOrderMinute = localOrderMinute;
    }

    public Boolean isStopImportProcess() {
        return stopImportProcess;
    }

    public ShopInfo stopImportProcess(Boolean stopImportProcess) {
        this.stopImportProcess = stopImportProcess;
        return this;
    }

    public void setStopImportProcess(Boolean stopImportProcess) {
        this.stopImportProcess = stopImportProcess;
    }

    public Boolean isDisableShippingPricingAutoCalculation() {
        return disableShippingPricingAutoCalculation;
    }

    public ShopInfo disableShippingPricingAutoCalculation(Boolean disableShippingPricingAutoCalculation) {
        this.disableShippingPricingAutoCalculation = disableShippingPricingAutoCalculation;
        return this;
    }

    public void setDisableShippingPricingAutoCalculation(Boolean disableShippingPricingAutoCalculation) {
        this.disableShippingPricingAutoCalculation = disableShippingPricingAutoCalculation;
    }

    public Boolean isAllowV1FormCustomerPortal() {
        return allowV1FormCustomerPortal;
    }

    public ShopInfo allowV1FormCustomerPortal(Boolean allowV1FormCustomerPortal) {
        this.allowV1FormCustomerPortal = allowV1FormCustomerPortal;
        return this;
    }

    public void setAllowV1FormCustomerPortal(Boolean allowV1FormCustomerPortal) {
        this.allowV1FormCustomerPortal = allowV1FormCustomerPortal;
    }

    public Boolean isEnableChangeFromNextBillingDate() {
        return enableChangeFromNextBillingDate;
    }

    public ShopInfo enableChangeFromNextBillingDate(Boolean enableChangeFromNextBillingDate) {
        this.enableChangeFromNextBillingDate = enableChangeFromNextBillingDate;
        return this;
    }

    public void setEnableChangeFromNextBillingDate(Boolean enableChangeFromNextBillingDate) {
        this.enableChangeFromNextBillingDate = enableChangeFromNextBillingDate;
    }

    public Boolean isEnableInventoryCheck() {
        return enableInventoryCheck;
    }

    public ShopInfo enableInventoryCheck(Boolean enableInventoryCheck) {
        this.enableInventoryCheck = enableInventoryCheck;
        return this;
    }

    public void setEnableInventoryCheck(Boolean enableInventoryCheck) {
        this.enableInventoryCheck = enableInventoryCheck;
    }

    public Boolean isWhiteListed() {
        return whiteListed;
    }

    public ShopInfo whiteListed(Boolean whiteListed) {
        this.whiteListed = whiteListed;
        return this;
    }

    public void setWhiteListed(Boolean whiteListed) {
        this.whiteListed = whiteListed;
    }

    public Boolean isEnableAddJSInterceptor() {
        return enableAddJSInterceptor;
    }

    public ShopInfo enableAddJSInterceptor(Boolean enableAddJSInterceptor) {
        this.enableAddJSInterceptor = enableAddJSInterceptor;
        return this;
    }

    public void setEnableAddJSInterceptor(Boolean enableAddJSInterceptor) {
        this.enableAddJSInterceptor = enableAddJSInterceptor;
    }

    public Boolean isReBuyEnabled() {
        return reBuyEnabled;
    }

    public ShopInfo reBuyEnabled(Boolean reBuyEnabled) {
        this.reBuyEnabled = reBuyEnabled;
        return this;
    }

    public void setReBuyEnabled(Boolean reBuyEnabled) {
        this.reBuyEnabled = reBuyEnabled;
    }

    public Boolean isPriceSyncEnabled() {
        return priceSyncEnabled;
    }

    public ShopInfo priceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
        return this;
    }

    public void setPriceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
    }

    public Boolean isDiscountSyncEnabled() {
        return discountSyncEnabled;
    }

    public ShopInfo discountSyncEnabled(Boolean discountSyncEnabled) {
        this.discountSyncEnabled = discountSyncEnabled;
        return this;
    }

    public void setDiscountSyncEnabled(Boolean discountSyncEnabled) {
        this.discountSyncEnabled = discountSyncEnabled;
    }

    public Boolean isSkuSyncEnabled() {
        return skuSyncEnabled;
    }

    public ShopInfo skuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
        return this;
    }

    public void setSkuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
    }

    public String getKlaviyoListId() {
        return klaviyoListId;
    }

    public ShopInfo klaviyoListId(String klaviyoListId) {
        this.klaviyoListId = klaviyoListId;
        return this;
    }

    public void setKlaviyoListId(String klaviyoListId) {
        this.klaviyoListId = klaviyoListId;
    }

    public ScriptVersion getScriptVersion() {
        return scriptVersion;
    }

    public ShopInfo scriptVersion(ScriptVersion scriptVersion) {
        this.scriptVersion = scriptVersion;
        return this;
    }

    public void setScriptVersion(ScriptVersion scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public BuildBoxVersion getBuildBoxVersion() {
        return buildBoxVersion;
    }

    public ShopInfo buildBoxVersion(BuildBoxVersion buildBoxVersion) {
        this.buildBoxVersion = buildBoxVersion;
        return this;
    }

    public void setBuildBoxVersion(BuildBoxVersion buildBoxVersion) {
        this.buildBoxVersion = buildBoxVersion;
    }

    public String getNextOrderDateAttributeKey() {
        return nextOrderDateAttributeKey;
    }

    public ShopInfo nextOrderDateAttributeKey(String nextOrderDateAttributeKey) {
        this.nextOrderDateAttributeKey = nextOrderDateAttributeKey;
        return this;
    }

    public void setNextOrderDateAttributeKey(String nextOrderDateAttributeKey) {
        this.nextOrderDateAttributeKey = nextOrderDateAttributeKey;
    }

    public String getNextOrderDateAttributeFormat() {
        return nextOrderDateAttributeFormat;
    }

    public ShopInfo nextOrderDateAttributeFormat(String nextOrderDateAttributeFormat) {
        this.nextOrderDateAttributeFormat = nextOrderDateAttributeFormat;
        return this;
    }

    public void setNextOrderDateAttributeFormat(String nextOrderDateAttributeFormat) {
        this.nextOrderDateAttributeFormat = nextOrderDateAttributeFormat;
    }

    public String getShopifyWebhookUrl() {
        return shopifyWebhookUrl;
    }

    public ShopInfo shopifyWebhookUrl(String shopifyWebhookUrl) {
        this.shopifyWebhookUrl = shopifyWebhookUrl;
        return this;
    }

    public void setShopifyWebhookUrl(String shopifyWebhookUrl) {
        this.shopifyWebhookUrl = shopifyWebhookUrl;
    }

    public Boolean isVerifiedEmailCustomDomain() {
        return verifiedEmailCustomDomain;
    }

    public ShopInfo verifiedEmailCustomDomain(Boolean verifiedEmailCustomDomain) {
        this.verifiedEmailCustomDomain = verifiedEmailCustomDomain;
        return this;
    }

    public void setVerifiedEmailCustomDomain(Boolean verifiedEmailCustomDomain) {
        this.verifiedEmailCustomDomain = verifiedEmailCustomDomain;
    }

    public OrderDayOfWeek getLocalOrderDayOfWeek() {
        return localOrderDayOfWeek;
    }

    public ShopInfo localOrderDayOfWeek(OrderDayOfWeek localOrderDayOfWeek) {
        this.localOrderDayOfWeek = localOrderDayOfWeek;
        return this;
    }

    public void setLocalOrderDayOfWeek(OrderDayOfWeek localOrderDayOfWeek) {
        this.localOrderDayOfWeek = localOrderDayOfWeek;
    }

    public String getStoreFrontAccessToken() {
        return storeFrontAccessToken;
    }

    public ShopInfo storeFrontAccessToken(String storeFrontAccessToken) {
        this.storeFrontAccessToken = storeFrontAccessToken;
        return this;
    }

    public void setStoreFrontAccessToken(String storeFrontAccessToken) {
        this.storeFrontAccessToken = storeFrontAccessToken;
    }

    public Boolean isEnableWebhook() {
        return enableWebhook;
    }

    public ShopInfo enableWebhook(Boolean enableWebhook) {
        this.enableWebhook = enableWebhook;
        return this;
    }

    public void setEnableWebhook(Boolean enableWebhook) {
        this.enableWebhook = enableWebhook;
    }

    public Boolean isOnBoardingPaymentSeen() {
        return onBoardingPaymentSeen;
    }

    public ShopInfo onBoardingPaymentSeen(Boolean onBoardingPaymentSeen) {
        this.onBoardingPaymentSeen = onBoardingPaymentSeen;
        return this;
    }

    public void setOnBoardingPaymentSeen(Boolean onBoardingPaymentSeen) {
        this.onBoardingPaymentSeen = onBoardingPaymentSeen;
    }

    public Boolean isChangeNextOrderDateOnBillingAttempt() {
        return changeNextOrderDateOnBillingAttempt;
    }

    public ShopInfo changeNextOrderDateOnBillingAttempt(Boolean changeNextOrderDateOnBillingAttempt) {
        this.changeNextOrderDateOnBillingAttempt = changeNextOrderDateOnBillingAttempt;
        return this;
    }

    public void setChangeNextOrderDateOnBillingAttempt(Boolean changeNextOrderDateOnBillingAttempt) {
        this.changeNextOrderDateOnBillingAttempt = changeNextOrderDateOnBillingAttempt;
    }

    public Boolean isKeepLineAttributes() {
        return keepLineAttributes;
    }

    public ShopInfo keepLineAttributes(Boolean keepLineAttributes) {
        this.keepLineAttributes = keepLineAttributes;
        return this;
    }

    public void setKeepLineAttributes(Boolean keepLineAttributes) {
        this.keepLineAttributes = keepLineAttributes;
    }

    public Boolean isPasswordEnabled() {
        return passwordEnabled;
    }

    public ShopInfo passwordEnabled(Boolean passwordEnabled) {
        this.passwordEnabled = passwordEnabled;
        return this;
    }

    public void setPasswordEnabled(Boolean passwordEnabled) {
        this.passwordEnabled = passwordEnabled;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public ShopInfo countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public ShopInfo countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getOverwriteSetting() {
        return overwriteSetting;
    }

    public ShopInfo overwriteSetting(String overwriteSetting) {
        this.overwriteSetting = overwriteSetting;
        return this;
    }

    public void setOverwriteSetting(String overwriteSetting) {
        this.overwriteSetting = overwriteSetting;
    }

    public String getMailchimpApiKey() {
        return mailchimpApiKey;
    }

    public ShopInfo mailchimpApiKey(String mailchimpApiKey) {
        this.mailchimpApiKey = mailchimpApiKey;
        return this;
    }

    public void setMailchimpApiKey(String mailchimpApiKey) {
        this.mailchimpApiKey = mailchimpApiKey;
    }

    public Boolean isCarryForwardLastOrderNote() {
        return carryForwardLastOrderNote;
    }

    public ShopInfo carryForwardLastOrderNote(Boolean carryForwardLastOrderNote) {
        this.carryForwardLastOrderNote = carryForwardLastOrderNote;
        return this;
    }

    public void setCarryForwardLastOrderNote(Boolean carryForwardLastOrderNote) {
        this.carryForwardLastOrderNote = carryForwardLastOrderNote;
    }

    public Boolean isAllowLocalDelivery() {
        return allowLocalDelivery;
    }

    public ShopInfo allowLocalDelivery(Boolean allowLocalDelivery) {
        this.allowLocalDelivery = allowLocalDelivery;
        return this;
    }

    public void setAllowLocalDelivery(Boolean allowLocalDelivery) {
        this.allowLocalDelivery = allowLocalDelivery;
    }

    public Boolean isAllowLocalPickup() {
        return allowLocalPickup;
    }

    public ShopInfo allowLocalPickup(Boolean allowLocalPickup) {
        this.allowLocalPickup = allowLocalPickup;
        return this;
    }

    public void setAllowLocalPickup(Boolean allowLocalPickup) {
        this.allowLocalPickup = allowLocalPickup;
    }

    public Boolean isUseShopifyAssets() {
        return useShopifyAssets;
    }

    public ShopInfo useShopifyAssets(Boolean useShopifyAssets) {
        this.useShopifyAssets = useShopifyAssets;
        return this;
    }

    public void setUseShopifyAssets(Boolean useShopifyAssets) {
        this.useShopifyAssets = useShopifyAssets;
    }

    public Boolean isZapietEnabled() {
        return zapietEnabled;
    }

    public ShopInfo zapietEnabled(Boolean zapietEnabled) {
        this.zapietEnabled = zapietEnabled;
        return this;
    }

    public void setZapietEnabled(Boolean zapietEnabled) {
        this.zapietEnabled = zapietEnabled;
    }

    public Boolean isMechanicEnabled() {
        return mechanicEnabled;
    }

    public ShopInfo mechanicEnabled(Boolean mechanicEnabled) {
        this.mechanicEnabled = mechanicEnabled;
        return this;
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

    public ShopInfo appstleLoyaltyApiKey(String appstleLoyaltyApiKey) {
        this.appstleLoyaltyApiKey = appstleLoyaltyApiKey;
        return this;
    }

    public void setAppstleLoyaltyApiKey(String appstleLoyaltyApiKey) {
        this.appstleLoyaltyApiKey = appstleLoyaltyApiKey;
    }

    public String getEberLoyaltyApiKey() {
        return eberLoyaltyApiKey;
    }

    public ShopInfo eberLoyaltyApiKey(String eberLoyaltyApiKey) {
        this.eberLoyaltyApiKey = eberLoyaltyApiKey;
        return this;
    }

    public void setEberLoyaltyApiKey(String eberLoyaltyApiKey) {
        this.eberLoyaltyApiKey = eberLoyaltyApiKey;
    }

    public Integer getEberLoyaltyPoints() {
        return eberLoyaltyPoints;
    }

    public ShopInfo eberLoyaltyPoints(Integer eberLoyaltyPoints) {
        this.eberLoyaltyPoints = eberLoyaltyPoints;
        return this;
    }

    public void setEberLoyaltyPoints(Integer eberLoyaltyPoints) {
        this.eberLoyaltyPoints = eberLoyaltyPoints;
    }

    public Integer getEberLoyaltyMultiplier() {
        return eberLoyaltyMultiplier;
    }

    public ShopInfo eberLoyaltyMultiplier(Integer eberLoyaltyMultiplier) {
        this.eberLoyaltyMultiplier = eberLoyaltyMultiplier;
        return this;
    }

    public void setEberLoyaltyMultiplier(Integer eberLoyaltyMultiplier) {
        this.eberLoyaltyMultiplier = eberLoyaltyMultiplier;
    }

    public Boolean isShopifyFlowEnabled() {
        return shopifyFlowEnabled;
    }

    public ShopInfo shopifyFlowEnabled(Boolean shopifyFlowEnabled) {
        this.shopifyFlowEnabled = shopifyFlowEnabled;
        return this;
    }

    public void setShopifyFlowEnabled(Boolean shopifyFlowEnabled) {
        this.shopifyFlowEnabled = shopifyFlowEnabled;
    }

    public Boolean isOverwriteAnchorDay() {
        return overwriteAnchorDay;
    }

    public ShopInfo overwriteAnchorDay(Boolean overwriteAnchorDay) {
        this.overwriteAnchorDay = overwriteAnchorDay;
        return this;
    }

    public void setOverwriteAnchorDay(Boolean overwriteAnchorDay) {
        this.overwriteAnchorDay = overwriteAnchorDay;
    }

    public Boolean isEnableChangeBillingPlan() {
        return enableChangeBillingPlan;
    }

    public ShopInfo enableChangeBillingPlan(Boolean enableChangeBillingPlan) {
        this.enableChangeBillingPlan = enableChangeBillingPlan;
        return this;
    }

    public void setEnableChangeBillingPlan(Boolean enableChangeBillingPlan) {
        this.enableChangeBillingPlan = enableChangeBillingPlan;
    }

    public String getLockBillingPlanComments() {
        return lockBillingPlanComments;
    }

    public ShopInfo lockBillingPlanComments(String lockBillingPlanComments) {
        this.lockBillingPlanComments = lockBillingPlanComments;
        return this;
    }

    public void setLockBillingPlanComments(String lockBillingPlanComments) {
        this.lockBillingPlanComments = lockBillingPlanComments;
    }

    public Boolean isEberLoyaltyEnabled() {
        return eberLoyaltyEnabled;
    }

    public ShopInfo eberLoyaltyEnabled(Boolean eberLoyaltyEnabled) {
        this.eberLoyaltyEnabled = eberLoyaltyEnabled;
        return this;
    }

    public void setEberLoyaltyEnabled(Boolean eberLoyaltyEnabled) {
        this.eberLoyaltyEnabled = eberLoyaltyEnabled;
    }

    public Integer getUpcomingEmailBufferDays() {
        return upcomingEmailBufferDays;
    }

    public ShopInfo upcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
        return this;
    }

    public void setUpcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
    }

    public String getInventoryLocations() {
        return inventoryLocations;
    }

    public ShopInfo inventoryLocations(String inventoryLocations) {
        this.inventoryLocations = inventoryLocations;
        return this;
    }

    public void setInventoryLocations(String inventoryLocations) {
        this.inventoryLocations = inventoryLocations;
    }

    public String getShipperHqApiKey() {
        return shipperHqApiKey;
    }

    public ShopInfo shipperHqApiKey(String shipperHqApiKey) {
        this.shipperHqApiKey = shipperHqApiKey;
        return this;
    }

    public void setShipperHqApiKey(String shipperHqApiKey) {
        this.shipperHqApiKey = shipperHqApiKey;
    }

    public String getShipperHqAuthCode() {
        return shipperHqAuthCode;
    }

    public ShopInfo shipperHqAuthCode(String shipperHqAuthCode) {
        this.shipperHqAuthCode = shipperHqAuthCode;
        return this;
    }

    public void setShipperHqAuthCode(String shipperHqAuthCode) {
        this.shipperHqAuthCode = shipperHqAuthCode;
    }

    public String getShipperHqAccessToken() {
        return shipperHqAccessToken;
    }

    public ShopInfo shipperHqAccessToken(String shipperHqAccessToken) {
        this.shipperHqAccessToken = shipperHqAccessToken;
        return this;
    }

    public void setShipperHqAccessToken(String shipperHqAccessToken) {
        this.shipperHqAccessToken = shipperHqAccessToken;
    }

    public String getIanaTimeZone() {
        return ianaTimeZone;
    }

    public ShopInfo ianaTimeZone(String ianaTimeZone) {
        this.ianaTimeZone = ianaTimeZone;
        return this;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopInfo)) {
            return false;
        }
        return id != null && id.equals(((ShopInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopInfo{" +
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
