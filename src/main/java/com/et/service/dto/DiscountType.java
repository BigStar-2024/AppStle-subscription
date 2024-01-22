package com.et.service.dto;

import com.et.domain.enumeration.DiscountTypeUnit;

public enum DiscountType {
    PERCENTAGE, FIXED, PRICE, SHIPPING, FREE_PRODUCT;

    public DiscountTypeUnit toDiscountTypeUnit() {
        switch(this) {
            case PERCENTAGE:
                return DiscountTypeUnit.PERCENTAGE;

            case FIXED:
                return DiscountTypeUnit.FIXED;

            case PRICE:
                return DiscountTypeUnit.PRICE;

            default:
                return null;
        }
    }
}
