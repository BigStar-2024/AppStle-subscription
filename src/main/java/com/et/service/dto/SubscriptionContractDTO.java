package com.et.service.dto;

import com.shopify.java.graphql.client.type.SellingPlanInterval;
import com.shopify.java.graphql.client.type.SubscriptionContractSubscriptionStatus;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public class SubscriptionContractDTO {

    @NotNull
    private String customerId;

    @NotNull
    private String paymentMethodId;

    @NotNull
    private SubscriptionContractSubscriptionStatus status;

    @NotNull
    private ZonedDateTime nextBillingDate;

    @NotNull
    private SellingPlanInterval billingIntervalType;

    @NotNull
    private Integer billingIntervalCount;

    private Integer maxCycles;

    private Integer minCycles;

    private SellingPlanInterval deliveryIntervalType;

    private Integer deliveryIntervalCount;

    private String deliveryFirstName;

    private String deliveryLastName;

    private String deliveryAddress1;

    private String deliveryAddress2;

    private String deliveryProvinceCode;

    private String deliveryCity;

    private String deliveryZip;

    private String deliveryCountryCode;

    private String deliveryPhone;

    private Double deliveryPriceAmount;

    private String currencyCode;

    List<SubscriptionContractLineDTO> lines;

    public String getCustomerId() {
        return customerId;
    }

    public SubscriptionContractDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public SubscriptionContractDTO setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public SubscriptionContractSubscriptionStatus getStatus() {
        return status;
    }

    public SubscriptionContractDTO setStatus(SubscriptionContractSubscriptionStatus status) {
        this.status = status;
        return this;
    }

    public ZonedDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public SubscriptionContractDTO setNextBillingDate(ZonedDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
        return this;
    }

    public SellingPlanInterval getBillingIntervalType() {
        return billingIntervalType;
    }

    public SubscriptionContractDTO setBillingIntervalType(SellingPlanInterval billingIntervalType) {
        this.billingIntervalType = billingIntervalType;
        return this;
    }

    public Integer getBillingIntervalCount() {
        return billingIntervalCount;
    }

    public SubscriptionContractDTO setBillingIntervalCount(Integer billingIntervalCount) {
        this.billingIntervalCount = billingIntervalCount;
        return this;
    }

    public Integer getMaxCycles() {
        return maxCycles;
    }

    public SubscriptionContractDTO setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
        return this;
    }

    public Integer getMinCycles() {
        return minCycles;
    }

    public SubscriptionContractDTO setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
        return this;
    }

    public SellingPlanInterval getDeliveryIntervalType() {
        return deliveryIntervalType;
    }

    public SubscriptionContractDTO setDeliveryIntervalType(SellingPlanInterval deliveryIntervalType) {
        this.deliveryIntervalType = deliveryIntervalType;
        return this;
    }

    public Integer getDeliveryIntervalCount() {
        return deliveryIntervalCount;
    }

    public SubscriptionContractDTO setDeliveryIntervalCount(Integer deliveryIntervalCount) {
        this.deliveryIntervalCount = deliveryIntervalCount;
        return this;
    }

    public String getDeliveryFirstName() {
        return deliveryFirstName;
    }

    public SubscriptionContractDTO setDeliveryFirstName(String deliveryFirstName) {
        this.deliveryFirstName = deliveryFirstName;
        return this;
    }

    public String getDeliveryLastName() {
        return deliveryLastName;
    }

    public SubscriptionContractDTO setDeliveryLastName(String deliveryLastName) {
        this.deliveryLastName = deliveryLastName;
        return this;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public SubscriptionContractDTO setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
        return this;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public SubscriptionContractDTO setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
        return this;
    }

    public String getDeliveryProvinceCode() {
        return deliveryProvinceCode;
    }

    public SubscriptionContractDTO setDeliveryProvinceCode(String deliveryProvinceCode) {
        this.deliveryProvinceCode = deliveryProvinceCode;
        return this;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public SubscriptionContractDTO setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
        return this;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public SubscriptionContractDTO setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
        return this;
    }

    public String getDeliveryCountryCode() {
        return deliveryCountryCode;
    }

    public SubscriptionContractDTO setDeliveryCountryCode(String deliveryCountryCode) {
        this.deliveryCountryCode = deliveryCountryCode;
        return this;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public SubscriptionContractDTO setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
        return this;
    }

    public Double getDeliveryPriceAmount() {
        return deliveryPriceAmount;
    }

    public SubscriptionContractDTO setDeliveryPriceAmount(Double deliveryPriceAmount) {
        this.deliveryPriceAmount = deliveryPriceAmount;
        return this;
    }

    public List<SubscriptionContractLineDTO> getLines() {
        return lines;
    }

    public SubscriptionContractDTO setLines(List<SubscriptionContractLineDTO> lines) {
        this.lines = lines;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
