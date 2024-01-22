package com.et.pojo.bulkAutomation;

public class UpdateLinePriceRequest {

    private String shop;
    private Long contractId;
    private Long variantId;
    private Double price;
    private boolean isLast ;

    public UpdateLinePriceRequest() {
    }

    public UpdateLinePriceRequest(String shop, Long contractId, Long variantId, Double price) {
        this.shop = shop;
        this.contractId = contractId;
        this.variantId = variantId;
        this.price = price;
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

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
