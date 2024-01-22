package com.et.web.rest.vm;

import com.apollographql.apollo.api.Input;
import com.shopify.java.graphql.client.type.CountryCode;
import com.shopify.java.graphql.client.type.DeliveryProvinceInput;

import java.util.List;

public class DeliveryCountryInfo {

    private String code;
    private boolean restOfWorld;
    private String provinceCode;
    private boolean shouldIncludeAllProvince;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRestOfWorld() {
        return restOfWorld;
    }

    public void setRestOfWorld(boolean restOfWorld) {
        this.restOfWorld = restOfWorld;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public boolean isShouldIncludeAllProvince() {
        return shouldIncludeAllProvince;
    }

    public void setShouldIncludeAllProvince(boolean shouldIncludeAllProvince) {
        this.shouldIncludeAllProvince = shouldIncludeAllProvince;
    }
}
