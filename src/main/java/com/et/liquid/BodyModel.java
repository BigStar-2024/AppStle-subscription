package com.et.liquid;


import com.et.api.shopify.shop.Shop;
import com.et.web.rest.vm.AttributeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyModel {
    private String challengeUrl;
    private String planType;
    private Shop shop;
    private Customer customer;
    private String lastFourDigits;
    private String nextOrderDate;
    private String frequency_of_subscription;
    private Long subscriptionContractId;
    private String minCycles;
    private String maxCycles;
    private List<String> productsAdded;
    private List<String> productsRemoved;
    private List<String> outOfStockProducts;
    private String orderCreatedDate;
    private List<AttributeInfo> allAttributes = new ArrayList<>();
    private Map<String, String> customAttributes = new HashMap<>();
    private String orderId;

    public BodyModel(Shop shop, Customer customer, String lastFourDigits, String nextOrderDate, String frequency_of_subscription,
                     Long subscriptionContractId, String planType, String challengeUrl, String minCycles, String maxCycles, String orderCreatedDate,
                     List<AttributeInfo> allAttributes, Map<String, String> customAttributes) {
        this.shop = shop;
        this.customer = customer;
        this.lastFourDigits = lastFourDigits;
        this.nextOrderDate = nextOrderDate;
        this.frequency_of_subscription = frequency_of_subscription;
        this.subscriptionContractId = subscriptionContractId;
        this.planType = planType;
        this.challengeUrl = challengeUrl;
        this.minCycles = minCycles;
        this.maxCycles = maxCycles;
        this.orderCreatedDate = orderCreatedDate;
        this.allAttributes = allAttributes;
        this.customAttributes = customAttributes;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(String nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public String getFrequency_of_subscription() {
        return frequency_of_subscription;
    }

    public void setFrequency_of_subscription(String frequency_of_subscription) {
        this.frequency_of_subscription = frequency_of_subscription;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getChallengeUrl() {
        return challengeUrl;
    }

    public void setChallengeUrl(String challengeUrl) {
        this.challengeUrl = challengeUrl;
    }

    public String getMinCycles() {
        return minCycles;
    }

    public void setMinCycles(String minCycles) {
        this.minCycles = minCycles;
    }

    public String getMaxCycles() {
        return maxCycles;
    }

    public void setMaxCycles(String maxCycles) {
        this.maxCycles = maxCycles;
    }

    public List<String> getProductsAdded() {
        return productsAdded;
    }

    public void setProductsAdded(List<String> productsAdded) {
        this.productsAdded = productsAdded;
    }

    public List<String> getProductsRemoved() {
        return productsRemoved;
    }

    public void setProductsRemoved(List<String> productsRemoved) {
        this.productsRemoved = productsRemoved;
    }

    public List<String> getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(List<String> outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public String getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(String orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public List<AttributeInfo> getAllAttributes() {
        return allAttributes;
    }

    public void setAllAttributes(List<AttributeInfo> allAttributes) {
        this.allAttributes = allAttributes;
    }

    public Map<String, String> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(Map<String, String> customAttriubtes) {
        this.customAttributes = customAttriubtes;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
