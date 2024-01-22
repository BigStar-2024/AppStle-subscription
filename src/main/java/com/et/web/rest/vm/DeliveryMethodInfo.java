package com.et.web.rest.vm;

import com.shopify.java.graphql.client.type.CurrencyCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeliveryMethodInfo {
    private double amount;
    private CurrencyCode currencyCode;
    private String carrierServiceId;
    private String name;
    //TODO Rate type boolean ownrate/carrierrate
    private Boolean carrierRate = false;
    private String deliveryConditionType = "PRICE";
    private List<PriceCondition> priceConditions = new ArrayList<>();
    private List<WeightCondition> weightConditions = new ArrayList<>();

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Collection<PriceCondition> getPriceConditions() {
        return priceConditions;
    }

    public void setPriceConditions(List<PriceCondition> priceConditions) {
        this.priceConditions = priceConditions;
    }

    public List<WeightCondition> getWeightConditions() {
        return weightConditions;
    }

    public void setWeightConditions(List<WeightCondition> weightConditions) {
        this.weightConditions = weightConditions;
    }

    public String getCarrierServiceId() {
        return carrierServiceId;
    }

    public void setCarrierServiceId(String carrierServiceId) {
        this.carrierServiceId = carrierServiceId;
    }

    public Boolean getCarrierRate() {
        return carrierRate;
    }

    public void setCarrierRate(Boolean carrierRate) {
        this.carrierRate = carrierRate;
    }

    public String getDeliveryConditionType() {
        return deliveryConditionType;
    }

    public void setDeliveryConditionType(String deliveryConditionType) {
        this.deliveryConditionType = deliveryConditionType;
    }
}
