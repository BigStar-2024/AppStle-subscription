package com.et.pojo;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productId",
    "sku",
    "title",
    "quantity",
    "variantId",
    "variantTitle",
    "variantImage",
    "currentPrice",
    "sellingPlanId",
    "sellingPlanName",
    "lineId"
})
public class SubscriptionProductInfo implements Serializable
{
    private final static long serialVersionUID = -8968200641577267450L;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("title")
    private String title;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("variantId")
    private String variantId;

    @JsonProperty("variantTitle")
    private String variantTitle;

    @JsonProperty("variantImage")
    private String variantImage;

    @JsonProperty("currentPrice")
    private String currentPrice;

    @JsonProperty("sellingPlanId")
    private String sellingPlanId;

    @JsonProperty("sellingPlanName")
    private String sellingPlanName;

    @JsonProperty("lineId")
    private String lineId;
    private String discountedPrice;
    private Boolean pricingPolicyUpdated;
    private PricingPolicy pricingPolicy;
    private String basePrice;


    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("variantId")
    public String getVariantId() {
        return variantId;
    }

    @JsonProperty("variantId")
    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    @JsonProperty("variantTitle")
    public String getVariantTitle() {
        return variantTitle;
    }

    @JsonProperty("variantTitle")
    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    @JsonProperty("variantImage")
    public String getVariantImage() {
        return variantImage;
    }

    @JsonProperty("variantImage")
    public void setVariantImage(String variantImage) {
        this.variantImage = variantImage;
    }

    @JsonProperty("currentPrice")
    public String getCurrentPrice() {
        return currentPrice;
    }

    @JsonProperty("currentPrice")
    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    @JsonProperty("sellingPlanId")
    public String getSellingPlanId() {
        return sellingPlanId;
    }

    @JsonProperty("sellingPlanId")
    public void setSellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    @JsonProperty("sellingPlanName")
    public String getSellingPlanName() {
        return sellingPlanName;
    }

    @JsonProperty("sellingPlanName")
    public void setSellingPlanName(String sellingPlanName) {
        this.sellingPlanName = sellingPlanName;
    }

    @JsonProperty("lineId")
    public String getLineId() {
        return lineId;
    }

    @JsonProperty("lineId")
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("productId", productId)
            .append("sku", sku)
            .append("title", title)
            .append("quantity", quantity)
            .append("variantId", variantId)
            .append("variantTitle", variantTitle)
            .append("variantImage", variantImage)
            .append("currentPrice", currentPrice)
            .append("sellingPlanId", sellingPlanId)
            .append("sellingPlanName", sellingPlanName)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SubscriptionProductInfo)) return false;

        SubscriptionProductInfo that = (SubscriptionProductInfo) o;

        return new EqualsBuilder().append(productId, that.productId).append(sku, that.sku).append(title, that.title).append(quantity, that.quantity).append(variantId, that.variantId).append(variantTitle, that.variantTitle).append(variantImage, that.variantImage).append(currentPrice, that.currentPrice).append(sellingPlanId, that.sellingPlanId).append(sellingPlanName, that.sellingPlanName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(productId).append(sku).append(title).append(quantity).append(variantId).append(variantTitle).append(variantImage).append(currentPrice).append(sellingPlanId).append(sellingPlanName).toHashCode();
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setPricingPolicyUpdated(Boolean pricingPolicyUpdated) {
        this.pricingPolicyUpdated = pricingPolicyUpdated;
    }

    public Boolean getPricingPolicyUpdated() {
        return pricingPolicyUpdated;
    }

    public void setPricingPolicy(PricingPolicy pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }

    public PricingPolicy getPricingPolicy() {
        return pricingPolicy;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getBasePrice() {
        return basePrice;
    }
}
