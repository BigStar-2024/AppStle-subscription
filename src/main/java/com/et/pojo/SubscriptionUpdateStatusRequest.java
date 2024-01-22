package com.et.pojo;

public class SubscriptionUpdateStatusRequest {

    private String shop;
    private Long contractId;
    private String status;
    private String waitTillTimestamp;

    public SubscriptionUpdateStatusRequest() {
    }

    public SubscriptionUpdateStatusRequest(String shop, Long contractId, String status, String waitTillTimestamp) {
        this.shop = shop;
        this.contractId = contractId;
        this.status = status;
        this.waitTillTimestamp = waitTillTimestamp;
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

    public String getWaitTillTimestamp() {
        return waitTillTimestamp;
    }

    public void setWaitTillTimestamp(String waitTillTimestamp) {
        this.waitTillTimestamp = waitTillTimestamp;
    }
}
