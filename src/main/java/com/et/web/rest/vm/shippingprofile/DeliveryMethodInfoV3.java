package com.et.web.rest.vm.shippingprofile;

import com.shopify.java.graphql.client.type.CurrencyCode;
import java.io.Serializable;

public class DeliveryMethodInfoV3 implements Serializable {
    private String name;
    private double amount;
    private String deliveryConditionType = "PRICE";
    private CurrencyCode currencyCode;
    private Double minValue;
    private Double maxValue;
    private String weightUnit;
    private String definitionType;
    private String carrierServiceId;
    private Double carrierPercentageFee = 0.0d;
    private Double carrierFixedFee = 0.0d;

    public DeliveryMethodInfoV3() {
    }

    public DeliveryMethodInfoV3(String name, double amount, String deliveryConditionType, CurrencyCode currencyCode, Double minValue, Double maxValue, String weightUnit, String definitionType, String carrierServiceId, Double carrierPercentageFee, Double carrierFixedFee) {
        this.name = name;
        this.amount = amount;
        this.deliveryConditionType = deliveryConditionType;
        this.currencyCode = currencyCode;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.weightUnit = weightUnit;
        this.definitionType = definitionType;
        this.carrierServiceId = carrierServiceId;
        this.carrierPercentageFee = carrierPercentageFee;
        this.carrierFixedFee = carrierFixedFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDeliveryConditionType() {
        return deliveryConditionType;
    }

    public void setDeliveryConditionType(String deliveryConditionType) {
        this.deliveryConditionType = deliveryConditionType;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getDefinitionType() {
        return definitionType;
    }

    public void setDefinitionType(String definitionType) {
        this.definitionType = definitionType;
    }

    public String getCarrierServiceId() {
        return carrierServiceId;
    }

    public void setCarrierServiceId(String carrierServiceId) {
        this.carrierServiceId = carrierServiceId;
    }

    public Double getCarrierPercentageFee() {
        return carrierPercentageFee;
    }

    public void setCarrierPercentageFee(Double carrierPercentageFee) {
        this.carrierPercentageFee = carrierPercentageFee;
    }

    public Double getCarrierFixedFee() {
        return carrierFixedFee;
    }

    public void setCarrierFixedFee(Double carrierFixedFee) {
        this.carrierFixedFee = carrierFixedFee;
    }
}
