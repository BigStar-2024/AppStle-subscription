package com.et.pojo;

import com.et.domain.enumeration.ActivityLogEventSource;

import java.util.List;

public class ReplaceVariantsInfo {

    private String shop;
    private Long contractId;
    private List<Long> oldVariantIdList;
    private List<Long> newVariantIdList;
    private Integer quantity;
    private String oldLineId;
    private ActivityLogEventSource eventSource;

    private Boolean carryForwardDiscount;

    public ReplaceVariantsInfo() {
    }

    public ReplaceVariantsInfo(String shop, Long contractId, List<Long> oldVariantIdList, List<Long> newVariantIdList, Boolean carryForwardDiscount) {
        this.shop = shop;
        this.contractId = contractId;
        this.oldVariantIdList = oldVariantIdList;
        this.newVariantIdList = newVariantIdList;
        this.carryForwardDiscount = carryForwardDiscount;
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

    public List<Long> getOldVariantIdList() {
        return oldVariantIdList;
    }

    public void setOldVariantIdList(List<Long> oldVariantIdList) {
        this.oldVariantIdList = oldVariantIdList;
    }

    public List<Long> getNewVariantIdList() {
        return newVariantIdList;
    }

    public void setNewVariantIdList(List<Long> newVariantIdList) {
        this.newVariantIdList = newVariantIdList;
    }

    public Boolean getCarryForwardDiscount() {
        return carryForwardDiscount;
    }

    public void setCarryForwardDiscount(Boolean carryForwardDiscount) {
        this.carryForwardDiscount = carryForwardDiscount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOldLineId() {
        return oldLineId;
    }

    public void setOldLineId(String oldLineId) {
        this.oldLineId = oldLineId;
    }

    public ActivityLogEventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(ActivityLogEventSource eventSource) {
        this.eventSource = eventSource;
    }
}
