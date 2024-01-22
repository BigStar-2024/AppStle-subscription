package com.et.web.rest.vm;

public class ZapierSubscriptionVM {

    private String hookUrl;

    public String getHookUrl() {
        return hookUrl;
    }

    public void setHookUrl(String hookUrl) {
        this.hookUrl = hookUrl;
    }

    @Override
    public String toString() {
        return "SubscriptionVM{" +
            "hookUrl='" + hookUrl + '\'' +
            '}';
    }
}
