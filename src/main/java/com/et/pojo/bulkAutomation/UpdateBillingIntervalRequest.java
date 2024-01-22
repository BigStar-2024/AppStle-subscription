package com.et.pojo.bulkAutomation;

import com.shopify.java.graphql.client.type.SellingPlanInterval;

public class UpdateBillingIntervalRequest{

    private String shop;
    private Long contractId;
    private int billingIntervalCount;
    private SellingPlanInterval billingInterval;
    private boolean isLast;

    public UpdateBillingIntervalRequest(){

    }

    public UpdateBillingIntervalRequest(String shop, Long contractId, int billingIntervalCount, SellingPlanInterval billingInterval) {
        this.shop = shop;
        this.contractId = contractId;
        this.billingIntervalCount = billingIntervalCount;
        this.billingInterval = billingInterval;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public int getBillingIntervalCount() {
        return billingIntervalCount;
    }

    public void setBillingIntervalCount(int billingIntervalCount) {
        this.billingIntervalCount = billingIntervalCount;
    }

    public SellingPlanInterval getBillingInterval() {
        return billingInterval;
    }

    public void setBillingInterval(SellingPlanInterval billingInterval) {
        this.billingInterval = billingInterval;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
