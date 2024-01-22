package com.et.web.rest.vm;

public class CustomerTokenInfo {

    private Long customerId;
    private String token;
    private String appstleExternalToken;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppstleExternalToken() {
        return appstleExternalToken;
    }

    public void setAppstleExternalToken(String appstleExternalToken) {
        this.appstleExternalToken = appstleExternalToken;
    }
}
