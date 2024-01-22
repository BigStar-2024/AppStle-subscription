package com.et.pojo.bulkAutomation;

public class ReplaceRemovedVariantRequest {

    private String shop;
    private Long contractId;
    private String title;
    private Long newVariantId;
    private boolean isLast;

    public ReplaceRemovedVariantRequest() {
    }

    public ReplaceRemovedVariantRequest(String shop, Long contractId, String title, Long newVariantId) {
        this.shop = shop;
        this.contractId = contractId;
        this.title = title;
        this.newVariantId = newVariantId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNewVariantId() {
        return newVariantId;
    }

    public void setNewVariantId(Long newVariantId) {
        this.newVariantId = newVariantId;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
