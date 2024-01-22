
package com.et.web.rest.vm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.et.domain.enumeration.DiscountTypeUnit;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productId",
    "planTitle",
    "deliveryFrequency",
    "frequencyPlanName",
    "discountEnabled",
    "discountOffer",
    "deliveryFrequencyType",
    "discountType",
    "sellingPlanGroupId"
})
public class SubscriptionGroupModificationRequest implements Serializable
{

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("sellingPlanGroupId")
    private String sellingPlanGroupId;
    @JsonProperty("planTitle")
    private String planTitle;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -554999649085192810L;

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("planTitle")
    public String getPlanTitle() {
        return planTitle;
    }

    @JsonProperty("planTitle")
    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    @JsonProperty("deliveryFrequency")
    public String getDeliveryFrequency() {
        return deliveryFrequency;
    }

    @JsonProperty("deliveryFrequency")
    public void setDeliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    @JsonProperty("frequencyPlanName")
    public String getFrequencyPlanName() {
        return frequencyPlanName;
    }

    @JsonProperty("frequencyPlanName")
    public void setFrequencyPlanName(String frequencyPlanName) {
        this.frequencyPlanName = frequencyPlanName;
    }

    @JsonProperty("discountEnabled")
    public Boolean getDiscountEnabled() {
        return discountEnabled;
    }

    @JsonProperty("discountEnabled")
    public void setDiscountEnabled(Boolean discountEnabled) {
        this.discountEnabled = discountEnabled;
    }

    @JsonProperty("discountOffer")
    public String getDiscountOffer() {
        return discountOffer;
    }

    @JsonProperty("discountOffer")
    public void setDiscountOffer(String discountOffer) {
        this.discountOffer = discountOffer;
    }

    @JsonProperty("deliveryFrequencyType")
    public FrequencyIntervalUnit getDeliveryFrequencyType() {
        return deliveryFrequencyType;
    }

    @JsonProperty("deliveryFrequencyType")
    public void setDeliveryFrequencyType(FrequencyIntervalUnit deliveryFrequencyType) {
        this.deliveryFrequencyType = deliveryFrequencyType;
    }

    @JsonProperty("discountType")
    public DiscountTypeUnit getDiscountType() {
        return discountType;
    }

    @JsonProperty("discountType")
    public void setDiscountType(DiscountTypeUnit discountType) {
        this.discountType = discountType;
    }

    @JsonProperty("sellingPlanGroupId")
    public String getSellingPlanGroupId() {
        return sellingPlanGroupId;
    }

    @JsonProperty("sellingPlanGroupId")
    public void setSellingPlanGroupId(String sellingPlanGroupId) {
        this.sellingPlanGroupId = sellingPlanGroupId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("productId", productId).append("planTitle", planTitle).append("deliveryFrequency", deliveryFrequency).append("frequencyPlanName", frequencyPlanName).append("discountEnabled", discountEnabled).append("discountOffer", discountOffer).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(productId).append(deliveryFrequency).append(discountOffer).append(additionalProperties).append(planTitle).append(frequencyPlanName).append(discountEnabled).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SubscriptionGroupModificationRequest) == false) {
            return false;
        }
        SubscriptionGroupModificationRequest rhs = ((SubscriptionGroupModificationRequest) other);
        return new EqualsBuilder().append(productId, rhs.productId).append(deliveryFrequency, rhs.deliveryFrequency).append(discountOffer, rhs.discountOffer).append(additionalProperties, rhs.additionalProperties).append(planTitle, rhs.planTitle).append(frequencyPlanName, rhs.frequencyPlanName).append(discountEnabled, rhs.discountEnabled).isEquals();
    }

}
