package com.et.web.rest.vm.payment;

public enum DigitalWallet {
    APPLE_PAY("APPLE_PAY"),
    ANDROID_PAY("ANDROID_PAY"),
    GOOGLE_PAY("GOOGLE_PAY"),
    SHOPIFY_PAY("SHOPIFY_PAY"),
    $UNKNOWN("$UNKNOWN");

    private final String rawValue;

    private DigitalWallet(String rawValue) {
        this.rawValue = rawValue;
    }

    public String rawValue() {
        return this.rawValue;
    }
}
