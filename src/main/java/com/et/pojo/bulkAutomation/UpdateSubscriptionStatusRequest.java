package com.et.pojo.bulkAutomation;

public class UpdateSubscriptionStatusRequest {

    private String shop;
    private Long contractId;
    private String status;
    private boolean isLast;

    public UpdateSubscriptionStatusRequest() {
    }

    public UpdateSubscriptionStatusRequest(String shop, Long contractId, String status) {
        this.shop = shop;
        this.contractId = contractId;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
