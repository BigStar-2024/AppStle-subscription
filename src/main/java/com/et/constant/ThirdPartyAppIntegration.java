package com.et.constant;

import java.util.Arrays;
import java.util.Optional;

public enum ThirdPartyAppIntegration {
    SHIP_INSURE("OW4ZMNXTNZD6");

    private String appKey;

    private ThirdPartyAppIntegration (String appKey) {
        this.appKey = appKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public static Optional<ThirdPartyAppIntegration> findByAppKey(String appKey) {
        return Arrays.stream(ThirdPartyAppIntegration.values())
            .filter(tpai -> tpai.getAppKey().equalsIgnoreCase(appKey))
            .findFirst();
    }
}
