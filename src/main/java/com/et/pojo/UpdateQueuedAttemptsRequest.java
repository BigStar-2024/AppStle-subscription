package com.et.pojo;

public class UpdateQueuedAttemptsRequest {

    private String shop;
    private Long contractId;
    private int delayInSeconds;

    public UpdateQueuedAttemptsRequest() {
    }

    public UpdateQueuedAttemptsRequest(String shop, Long contractId, int delayInSeconds) {
        this.shop = shop;
        this.contractId = contractId;
        this.delayInSeconds = delayInSeconds;
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

    public int getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(int delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }
}
