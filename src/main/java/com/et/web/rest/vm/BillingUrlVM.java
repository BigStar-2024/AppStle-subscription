package com.et.web.rest.vm;

public class BillingUrlVM {

    private String billingUrl;
    private boolean free;

    public BillingUrlVM(String billingUrl, boolean free) {
        this.billingUrl = billingUrl;
        this.free = free;
    }

    public BillingUrlVM(String billingUrl) {
        this.billingUrl = billingUrl;
    }

    public String getBillingUrl() {
        return billingUrl;
    }

    public void setBillingUrl(String billingUrl) {
        this.billingUrl = billingUrl;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
