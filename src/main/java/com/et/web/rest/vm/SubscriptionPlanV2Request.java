package com.et.web.rest.vm;

import com.et.domain.enumeration.DiscountTypeUnit;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SubscriptionPlanV2Request {

    @JsonProperty("deliveryFrequency")
    private String deliveryFrequency;
    @JsonProperty("frequencyPlanName")
    private String frequencyPlanName;
    @JsonProperty("discountEnabled")
    private Boolean discountEnabled;
    @JsonProperty("discountOffer")
    private String discountOffer;
    @JsonProperty("deliveryFrequencyType")
    private FrequencyIntervalUnit deliveryFrequencyType = FrequencyIntervalUnit.DAY;
    @JsonProperty("discountType")
    @JsonDeserialize(using = DiscountTypeDeserializer.class)
    private DiscountTypeUnit discountType = DiscountTypeUnit.PERCENTAGE;

    public String getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getFrequencyPlanName() {
        return frequencyPlanName;
    }

    public void setFrequencyPlanName(String frequencyPlanName) {
        this.frequencyPlanName = frequencyPlanName;
    }

    public Boolean getDiscountEnabled() {
        return discountEnabled;
    }

    public void setDiscountEnabled(Boolean discountEnabled) {
        this.discountEnabled = discountEnabled;
    }

    public String getDiscountOffer() {
        return discountOffer;
    }

    public void setDiscountOffer(String discountOffer) {
        this.discountOffer = discountOffer;
    }

    public FrequencyIntervalUnit getDeliveryFrequencyType() {
        return deliveryFrequencyType;
    }

    public void setDeliveryFrequencyType(FrequencyIntervalUnit deliveryFrequencyType) {
        this.deliveryFrequencyType = deliveryFrequencyType;
    }

    public DiscountTypeUnit getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeUnit discountType) {
        this.discountType = discountType;
    }
}
