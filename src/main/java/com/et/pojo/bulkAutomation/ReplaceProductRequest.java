package com.et.pojo.bulkAutomation;

import java.util.List;

public class ReplaceProductRequest {

    private String shop;
    private Long contractId;
    private List<Long> newVariantIds;
    private List<Long> oldVariantIds;
    private boolean isLast;

    public ReplaceProductRequest() {
    }

    public ReplaceProductRequest(String shop, Long contractId, List<Long> newVariantIds, List<Long> oldVariantIds) {
        this.shop = shop;
        this.contractId = contractId;
        this.newVariantIds = newVariantIds;
        this.oldVariantIds = oldVariantIds;
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

    public List<Long> getNewVariantIds() {
        return newVariantIds;
    }

    public void setNewVariantIds(List<Long> newVariantIds) {
        this.newVariantIds = newVariantIds;
    }

    public List<Long> getOldVariantIds() {
        return oldVariantIds;
    }

    public void setOldVariantIds(List<Long> oldVariantIds) {
        this.oldVariantIds = oldVariantIds;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
