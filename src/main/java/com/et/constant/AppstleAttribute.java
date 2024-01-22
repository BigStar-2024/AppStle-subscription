package com.et.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AppstleAttribute {
    ORDER_DATE("_order-date"),
    MIN_CYCLES("_min-cycles"),
    MAX_CYCLES("_max-cycles"),
    MIN_QUANTITY("_min-quantity"),
    MAX_QUANTITY("_max-quantity"),
    BB_ID("_appstle-bb-id"),
    PRODUCTS("products"),
    ONE_TIME_PRODUCT("_appstle-one-time-product"),
    FREE_PRODUCT("_appstle-free-product"),
    FIRST_ORDER_ID("_appstle-first-order-id"),
    FIRST_ORDER_NAME("_appstle-first-order-name"),
    PRODUCT_SKU("_appstle-bb-product-sku");

    private String key;

    private static  List<String> ALL_KEYS = Arrays.stream(AppstleAttribute.values()).map(AppstleAttribute::getKey).collect(Collectors.toList());

    private AppstleAttribute(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static List<String> getAllKeys() {
        return ALL_KEYS;
    }
}
