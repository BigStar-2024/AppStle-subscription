package com.et.constant;

/**
 * Application constants.
 */
public final class Constants {

    public static final String LATEST_BUILD_TIME = System.getProperty("latest.build.time", String.valueOf(System.currentTimeMillis()));
    public static final String APPSTLE_SUBSCRIPTION_BASE_URL = "https://subscription-admin.appstle.com";

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String SUBSCRIPTION_PROVIDER = "subscription";

    public static final String APP_UNINSTALLED = "app/uninstalled";
    public static final String SUBSCRIPTION_CREATE = "subscription_contracts/create";
    public static final String SUBSCRIPTION_UPDATE = "subscription_contracts/update";
    public static final String SUBSCRIPTION_BILLING_ATTEMPTS_SUCCESS = "subscription_billing_attempts/success";
    public static final String SUBSCRIPTION_BILLING_ATTEMPTS_FAILURE = "subscription_billing_attempts/failure";
    public static final String SUBSCRIPTION_BILLING_ATTEMPTS_CHALLENGED = "subscription_billing_attempts/challenged";

    public static final String CUSTOMER_PAYMENT_METHODS_CREATE = "customer_payment_methods/create";
    public static final String CUSTOMER_PAYMENT_METHODS_UPDATE = "customer_payment_methods/update";
    public static final String CUSTOMER_PAYMENT_METHODS_REVOKE = "customer_payment_methods/revoke";

    public static final String THEME_CREATE = "themes/create";
    public static final String THEME_UPDATE = "themes/update";
    public static final String THEME_PUBLISH = "themes/publish";

    public static final String ANALYTICS_INDEX = "analytics";
    public static final String CannyPrivateKey = "23450d2f-5c6c-c28b-d7e0-ddc5ad88124c";

    public static final String DATE_TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_STAMP_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String FIRST_PROMOTER_API_KEY = "a787c5648caf50847884c18626772c10";

    public static final String HTML_TAG_REGEX = "\\<.*?\\>";

    public static final String SPRING_PROFILE_STAGING = "staging";
    public static final String SPRING_PROFILE_SWAGGER = "swagger";
    public static final String SPRING_PROFILE_JOBS = "jobs";

    public static final String SHOPIFY_DOMAIN_SUFFIX = ".myshopify.com";

    public static final String JS_VENDORS = APPSTLE_SUBSCRIPTION_BASE_URL + "/app/vendors.chunk.js";
    public static final String JS_BUNDLE = APPSTLE_SUBSCRIPTION_BASE_URL + "/app/bundles-v2.bundle.js";
    public static final String CSS_VENDORS = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/vendors.css";
    public static final String CSS_BUNDLE = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/bundles-v2.css";
    public static final String JS_CUSTOMER = APPSTLE_SUBSCRIPTION_BASE_URL + "/app/customer-v3.bundle.js";
    public static final String CSS_CUSTOMER = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/customer-v3.css";


    public static final String JS_APPSTLE_MENU = APPSTLE_SUBSCRIPTION_BASE_URL + "/app/appstle-menu.bundle.js";
    public static final String CSS_APPSTLE_MENU = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/appstle-menu.css";

    public static final String JS_APPSTLE_SUBSCRIPTION = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription.js";
    public static final String JS_APPSTLE_SUBSCRIPTION_MIN = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription.min.js";
    public static final String JS_APPSTLE_SUBSCRIPTION_V2 = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription-v2.js";
    public static final String JS_APPSTLE_SUBSCRIPTION_V2_MIN = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription-v2.min.js";
    public static final String JS_APPSTLE_SUBSCRIPTION_WO_MUSTACHE = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription-without-mustache.js";
    public static final String JS_APPSTLE_SUBSCRIPTION_UPDATED = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-subscription_updated.js";
    public static final String JS_APPSTLE_BUNDLE_V1 = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-bundle-v1.js";
    public static final String JS_APPSTLE_BUNDLE_V1_MIN = APPSTLE_SUBSCRIPTION_BASE_URL + "/content/js/appstle-bundle-v1.min.js";
}
