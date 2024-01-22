package com.et.pojo;

import com.et.domain.enumeration.ActivityLogEventSource;

import java.util.List;
import java.util.Map;

public class ReplaceVariantsInfoV3 {

    private String shop;
    private Long contractId;
    private List<Long> oldVariants;
    private Map<Long, Integer>  newVariants;
    private Integer quantity;
    private String oldLineId;
    private ActivityLogEventSource eventSource;
    private Boolean carryForwardDiscount;

    public ReplaceVariantsInfoV3() {
    }

    public ReplaceVariantsInfoV3(String shop, Long contractId, List<Long> oldVariants, Map<Long, Integer> newVariants, Boolean carryForwardDiscount) {
        this.shop = shop;
        this.contractId = contractId;
        this.oldVariants = oldVariants;
        this.newVariants = newVariants;
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

    public List<Long> getOldVariants() {
        return oldVariants;
    }

    public void setOldVariants(List<Long> oldVariants) {
        this.oldVariants = oldVariants;
    }

    public Map<Long, Integer> getNewVariants() {
        return newVariants;
    }

    public void setNewVariants(Map<Long, Integer> newVariants) {
        this.newVariants = newVariants;
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

    public Boolean getCarryForwardDiscount() {
        return carryForwardDiscount;
    }

    public void setCarryForwardDiscount(Boolean carryForwardDiscount) {
        this.carryForwardDiscount = carryForwardDiscount;
    }
}
