package com.et.pojo.bulkAutomation;

public class HideSubscriptionsRequest {

    private String shop;
    private Long contractId;
    private boolean isLast;

    public HideSubscriptionsRequest() {
    }

    public HideSubscriptionsRequest(String shop, Long contractId) {
        this.shop = shop;
        this.contractId = contractId;
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

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
