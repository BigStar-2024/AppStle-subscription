package com.et.pojo.bulkAutomation;

import java.time.ZonedDateTime;

public class UpdateNextBillingDateRequest {

    private String shop;
    private Long contractId;
    private ZonedDateTime nextBillingDate;
    private boolean isLast ;

    public UpdateNextBillingDateRequest() {
    }

    public UpdateNextBillingDateRequest(String shop, Long contractId, ZonedDateTime nextBillingDate) {
        this.shop = shop;
        this.contractId = contractId;
        this.nextBillingDate = nextBillingDate;
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

    public ZonedDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(ZonedDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
