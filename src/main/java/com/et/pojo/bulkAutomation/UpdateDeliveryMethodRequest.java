package com.et.pojo.bulkAutomation;

public class UpdateDeliveryMethodRequest {

    private String shop;
    private Long contractId;
    private String deliveryMethodName;
    private boolean isLast;

    public UpdateDeliveryMethodRequest() {
    }

    public UpdateDeliveryMethodRequest(String shop, Long contractId, String deliveryMethodName) {
        this.shop = shop;
        this.contractId = contractId;
        this.deliveryMethodName = deliveryMethodName;
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

    public String getDeliveryMethodName() {
        return deliveryMethodName;
    }

    public void setDeliveryMethodName(String deliveryMethodName) {
        this.deliveryMethodName = deliveryMethodName;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
