package com.et.service.dto;

public class AppstleCycle {
    private Integer afterCycle;
    private DiscountType discountType;
    private Double value;
    private Long freeVariantId;
    private String freeProductHandle;

    public Integer getAfterCycle() {
        return afterCycle;
    }

    public void setAfterCycle(Integer afterCycle) {
        this.afterCycle = afterCycle;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getFreeVariantId() {
        return freeVariantId;
    }

    public void setFreeVariantId(Long freeVariantId) {
        this.freeVariantId = freeVariantId;
    }

    public String getFreeProductHandle() {
        return freeProductHandle;
    }

    public void setFreeProductHandle(String freeProductHandle) {
        this.freeProductHandle = freeProductHandle;
    }
}
