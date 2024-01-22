package com.et.liquid;

import java.util.HashSet;
import java.util.Set;

public class SubscriptionContract {

    private Long id;
    private Set<Long> sellingPlanIds = new HashSet<>();
    private Set<Long> variantIds = new HashSet<>();
    private Set<String> sellingPlanNames = new HashSet<>();
    private Set<String> variantNames = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getSellingPlanIds() {
        return sellingPlanIds;
    }

    public void setSellingPlanIds(Set<Long> sellingPlanIds) {
        this.sellingPlanIds = sellingPlanIds;
    }

    public Set<Long> getVariantIds() {
        return variantIds;
    }

    public void setVariantIds(Set<Long> variantIds) {
        this.variantIds = variantIds;
    }

    @Override
    public String toString() {
        return "SubscriptionContract{" +
                "id=" + id +
                '}';
    }

    public void setSellingPlanNames(Set<String> sellingPlanNames) {
        this.sellingPlanNames = sellingPlanNames;
    }

    public Set<String> getSellingPlanNames() {
        return sellingPlanNames;
    }

    public void setVariantNames(Set<String> variantNames) {
        this.variantNames = variantNames;
    }

    public Set<String> getVariantNames() {
        return variantNames;
    }
}
