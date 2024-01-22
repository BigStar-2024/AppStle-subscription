package com.et.service.dto;

import com.et.domain.ProductInfo;
import com.et.domain.SubscriptionBundling;
import com.et.domain.SubscriptionGroupPlan;

import java.util.List;

public class SubscriptionBundlingResponse {

    private SubscriptionBundling bundle;
    private List<SubscriptionGroupPlan> subscriptions;
    private List<ProductInfo> products;
    private List<String> variants;

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
    }

    public SubscriptionBundling getBundle() {
        return bundle;
    }

    public void setBundle(SubscriptionBundling bundle) {
        this.bundle = bundle;
    }

    public List<SubscriptionGroupPlan> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionGroupPlan> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "SubscriptionBundlingResponse{" +
            "bundle=" + bundle +
            ", subscriptions=" + subscriptions +
            ", products=" + products +
            ", variants=" + variants +
            '}';
    }
}
