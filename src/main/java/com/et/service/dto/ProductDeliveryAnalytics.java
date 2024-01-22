package com.et.service.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDeliveryAnalytics {

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("title")
    private String title;
    @JsonProperty("variantId")
    private String variantId;
    @JsonProperty("variantTitle")
    private String variantTitle;
    @JsonProperty("variantImage")
    private String variantImage;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("currentPrice")
    private Double currentPrice;
    @JsonProperty("sellingPlanId")
    private String sellingPlanId;
    @JsonProperty("sellingPlanName")
    private String sellingPlanName;
    @JsonProperty("deliveryInNext7Days")
    private Long deliveryInNext7Days = 0l;
    @JsonProperty("deliveryInNext30Days")
    private Long deliveryInNext30Days = 0l;
    @JsonProperty("deliveryInNext90Days")
    private Long deliveryInNext90Days = 0l;
    @JsonProperty("deliveryInNext365Days")
    private Long deliveryInNext365Days = 0l;

    @JsonProperty("discountedPrice")
    private String discountedPrice;
    @JsonProperty("basePrice")
    private String basePrice;

    @JsonProperty("discountedPrice")
    public String getDiscountedPrice() {
        return discountedPrice;
    }

    @JsonProperty("discountedPrice")
    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    @JsonProperty("basePrice")
    public String getBasePrice() {
        return basePrice;
    }

    @JsonProperty("basePrice")
    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
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

    @JsonProperty("quantity")
    public Long getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("sellingPlanId")
    public void setSellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    @JsonProperty("sellingPlanId")
    public String getSellingPlanId() {
        return sellingPlanId;
    }

    @JsonProperty("sellingPlanName")
    public String getSellingPlanName() {
        return sellingPlanName;
    }

    @JsonProperty("sellingPlanName")
    public void setSellingPlanName(String sellingPlanName) {
        this.sellingPlanName = sellingPlanName;
    }

    @JsonProperty("deliveryInNext7Days")
    public Long getDeliveryInNext7Days() {
        return deliveryInNext7Days;
    }

    @JsonProperty("deliveryInNext7Days")
    public void setDeliveryInNext7Days(Long deliveryInNext7Days) {
        this.deliveryInNext7Days = deliveryInNext7Days;
    }

    @JsonProperty("deliveryInNext30Days")
    public Long getDeliveryInNext30Days() {
        return deliveryInNext30Days;
    }

    @JsonProperty("deliveryInNext30Days")
    public void setDeliveryInNext30Days(Long deliveryInNext30Days) {
        this.deliveryInNext30Days = deliveryInNext30Days;
    }

    @JsonProperty("deliveryInNext90Days")
    public Long getDeliveryInNext90Days() {
        return deliveryInNext90Days;
    }

    @JsonProperty("deliveryInNext365Days")
    public Long getDeliveryInNext365Days() {
        return deliveryInNext365Days;
    }

    @JsonProperty("deliveryInNext365Days")
    public void setDeliveryInNext365Days(Long deliveryInNext365Days) {
        this.deliveryInNext365Days = deliveryInNext365Days;
    }

    @JsonProperty("deliveryInNext90Days")
    public void setDeliveryInNext90Days(Long deliveryInNext90Days) {
        this.deliveryInNext90Days = deliveryInNext90Days;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVariantImage() {
        return variantImage;
    }

    public void setVariantImage(String variantImage) {
        this.variantImage = variantImage;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.variantTitle == null)? 0 :this.variantTitle.hashCode()));
        result = ((result* 31)+((this.quantity == null)? 0 :this.quantity.hashCode()));
        result = ((result* 31)+((this.productId == null)? 0 :this.productId.hashCode()));
        result = ((result* 31)+((this.deliveryInNext90Days == null)? 0 :this.deliveryInNext90Days.hashCode()));
        result = ((result* 31)+((this.variantId == null)? 0 :this.variantId.hashCode()));
        result = ((result* 31)+((this.deliveryInNext7Days == null)? 0 :this.deliveryInNext7Days.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.deliveryInNext30Days == null)? 0 :this.deliveryInNext30Days.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDeliveryAnalytics that = (ProductDeliveryAnalytics) o;
        return Objects.equals(productId, that.productId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(variantId, that.variantId) &&
            Objects.equals(variantTitle, that.variantTitle) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(deliveryInNext7Days, that.deliveryInNext7Days) &&
            Objects.equals(deliveryInNext30Days, that.deliveryInNext30Days) &&
            Objects.equals(deliveryInNext90Days, that.deliveryInNext90Days);
    }
}
