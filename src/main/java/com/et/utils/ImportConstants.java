package com.et.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ImportConstants {


    public static final List<String> SUBSCRIPTION_DATA_DEFAULT_HEADERS_V2 = new ArrayList<>(Arrays.asList("ID", "Status", "Customer ID", "Customer Email", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID"));
    public static final List<String> SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1 = new ArrayList<>(Arrays.asList("ID", "Status", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID", "Customer Email"));

    public static final List<String> CUSTOMER_DATA_STRIPE_HEADERS = new ArrayList<>(Arrays.asList("id", "Email", "Card ID"));
    public static final List<String> CUSTOMER_DATA_BRAINTREE_HEADERS = new ArrayList<>(Arrays.asList("id", "Email", "Payment Method Token"));
    public static final List<String> PRODUCT_DATA_HEADERS = new ArrayList<>(Arrays.asList("external_product_id", "title", "Plan name"));
    public static final List<String> CUSTOMER_DATA_PAYPAL_HEADERS = new ArrayList<>(Arrays.asList("Email", "Paypal ID"));
    public static final List<String> CUSTOMER_DATA_AUTHORIZE_NET_HEADERS = new ArrayList<>(Arrays.asList("Email", "Customer Payment Profile Id", "Customer Profile Id"));

}
