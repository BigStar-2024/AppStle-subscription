package com.et.pojo;

public class CycleDiscount {
    private int afterCycle;

    private String computedPrice;

    private String adjustmentType;

    private String adjustmentValue;

    public CycleDiscount() {}

    public CycleDiscount(int afterCycle, String computedPrice, String adjustmentType, String adjustmentValue) {
        this.afterCycle = afterCycle;
        this.computedPrice = computedPrice;
        this.adjustmentType = adjustmentType;
        this.adjustmentValue = adjustmentValue;
    }

    public int getAfterCycle() {
        return afterCycle;
    }

    public void setAfterCycle(int afterCycle) {
        this.afterCycle = afterCycle;
    }

    public String getComputedPrice() {
        return computedPrice;
    }

    public void setComputedPrice(String computedPrice) {
        this.computedPrice = computedPrice;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getAdjustmentValue() {
        return adjustmentValue;
    }

    public void setAdjustmentValue(String adjustmentValue) {
        this.adjustmentValue = adjustmentValue;
    }
}
