package com.et.pojo.bulkAutomation;

public class UpdateDeliveryPriceRequest {

    private String shop;
    private Long contractId;
    private Double deliveryPrice;
    private boolean isLast ;

    public UpdateDeliveryPriceRequest(){

    }

    public UpdateDeliveryPriceRequest(String shop, Long contractId, Double deliveryPrice) {
        this.shop = shop;
        this.contractId = contractId;
        this.deliveryPrice = deliveryPrice;
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

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
