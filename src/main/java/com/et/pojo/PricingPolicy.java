package com.et.pojo;

import java.util.List;

public class PricingPolicy {
    private String basePrice;
    private List<CycleDiscount> cycleDiscounts;

    public PricingPolicy() {}

    public PricingPolicy(String basePrice, List<CycleDiscount> cycleDiscounts) {
        this.basePrice = basePrice;
        this.cycleDiscounts = cycleDiscounts;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public List<CycleDiscount> getCycleDiscounts() {
        return cycleDiscounts;
    }

    public void setCycleDiscounts(List<CycleDiscount> cycleDiscounts) {
        this.cycleDiscounts = cycleDiscounts;
    }
}
