package com.et.service.dto;

import com.et.domain.ProductInfo;
import com.et.domain.SubscriptionBundling;
import com.et.domain.SubscriptionGroupPlan;

import java.util.List;
import java.util.Map;

public class SubscriptionBundlingResponseV3 {

    private SubscriptionBundling bundle;
    private Map<Long, List<ProductInfo>> subscriptionPlanProductInfoMap;
    private Map<Long, List<String>> subscriptionPlanVariantMap;
    private List<SubscriptionGroupPlan> subscriptionGroupPlans;
    private SubscriptionGroupPlan subscription;
    private List<ProductInfo> products;
    private List<String> variants;

    public SubscriptionBundling getBundle() {
        return bundle;
    }

    public void setBundle(SubscriptionBundling bundle) {
        this.bundle = bundle;
    }

    public Map<Long, List<ProductInfo>> getSubscriptionPlanProductInfoMap() {
        return subscriptionPlanProductInfoMap;
    }

    public void setSubscriptionPlanProductInfoMap(Map<Long, List<ProductInfo>> subscriptionPlanProductInfoMap) {
        this.subscriptionPlanProductInfoMap = subscriptionPlanProductInfoMap;
    }

    public Map<Long, List<String>> getSubscriptionPlanVariantMap() {
        return subscriptionPlanVariantMap;
    }

    public void setSubscriptionPlanVariantMap(Map<Long, List<String>> subscriptionPlanVariantMap) {
        this.subscriptionPlanVariantMap = subscriptionPlanVariantMap;
    }

    public List<SubscriptionGroupPlan> getSubscriptionGroupPlans() {
        return subscriptionGroupPlans;
    }

    public void setSubscriptionGroupPlans(List<SubscriptionGroupPlan> subscriptionGroupPlans) {
        this.subscriptionGroupPlans = subscriptionGroupPlans;
    }

    public SubscriptionGroupPlan getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionGroupPlan subscription) {
        this.subscription = subscription;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
    }

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "SubscriptionBundlingResponseV2{" +
            "bundle=" + bundle +
            ", subscriptionPlanProductInfoMap=" + subscriptionPlanProductInfoMap +
            ", subscriptionPlanVariantMap=" + subscriptionPlanVariantMap +
            ", subscriptionGroupPlans=" + subscriptionGroupPlans +
            ", subscription=" + subscription +
            ", products=" + products +
            ", variants=" + variants +
            '}';
    }
}
