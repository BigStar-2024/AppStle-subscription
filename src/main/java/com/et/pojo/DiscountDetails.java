package com.et.pojo;

import com.et.domain.enumeration.DiscountTypeUnit;

public class DiscountDetails {

    private Double discountOffer;

    private Integer afterCycle;

    private DiscountTypeUnit discountType;

    public Double getDiscountOffer() {
        return discountOffer;
    }

    public void setDiscountOffer(Double discountOffer) {
        this.discountOffer = discountOffer;
    }

    public Integer getAfterCycle() {
        return afterCycle;
    }

    public void setAfterCycle(Integer afterCycle) {
        this.afterCycle = afterCycle;
    }

    public DiscountTypeUnit getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeUnit discountType) {
        this.discountType = discountType;
    }
}
