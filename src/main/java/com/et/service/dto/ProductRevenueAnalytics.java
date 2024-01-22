package com.et.service.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductRevenueAnalytics {
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
    @JsonProperty("totalProductPrice")
    private Long totalProductPrice = 0l;
    @JsonProperty("totalProductQuantity")
    private Long totalProductQuantity = 0l;
    @JsonProperty("cancellationRate")
    private Double cancellationRate = 0D;
    @JsonProperty("approvalRate")
    private Double approvalRate = 0D;
    @JsonProperty("churnRate")
    private Double churnRate = 0D;

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

    @JsonProperty("totalProductPrice")
    public Long getTotalProductPrice() {
        return totalProductPrice;
    }

    @JsonProperty("totalProductPrice")
    public void setTotalProductPrice(Long totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    @JsonProperty("totalProductQuantity")
    public Long getTotalProductQuantity() {
        return totalProductQuantity;
    }

    @JsonProperty("totalProductQuantity")
    public void setTotalProductQuantity(Long totalProductQuantity) {
        this.totalProductQuantity = totalProductQuantity;
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

    @JsonProperty("cancellationRate")
    public Double getCancellationRate() {
        return cancellationRate;
    }

    @JsonProperty("cancellationRate")
    public void setCancellationRate(Double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    @JsonProperty("approvalRate")
    public Double getApprovalRate() {
        return approvalRate;
    }

    @JsonProperty("approvalRate")
    public void setApprovalRate(Double approvalRate) {
        this.approvalRate = approvalRate;
    }

    @JsonProperty("churnRate")
    public Double getChurnRate() {
        return churnRate;
    }

    @JsonProperty("churnRate")
    public void setChurnRate(Double churnRate) {
        this.churnRate = churnRate;
    }

}
