package com.et.api.appstle.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointRedeemRule {
    private Long id;

    private String shop;

    private String name;

    private String type;

    private String status;

    private Integer redeemPoints;

    private String discountType;

    private Double discount;

    private Double minimumPurchaseAmount;

    private ZonedDateTime createAt;

    private ZonedDateTime updateAt;

    private Boolean allowSubscriptionProduct;

    private Double maximumShippingRate;

    private Long productId;

    private Long variantId;

    private String productData;

    private Long collectionId;

    private String collectionData;

    private String customerFacingLabel;

    private Long triggeredCount;
    private Integer pointsCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(Integer redeemPoints) {
        this.redeemPoints = redeemPoints;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getAllowSubscriptionProduct() {
        return allowSubscriptionProduct;
    }

    public void setAllowSubscriptionProduct(Boolean allowSubscriptionProduct) {
        this.allowSubscriptionProduct = allowSubscriptionProduct;
    }

    public Double getMaximumShippingRate() {
        return maximumShippingRate;
    }

    public void setMaximumShippingRate(Double maximumShippingRate) {
        this.maximumShippingRate = maximumShippingRate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionData() {
        return collectionData;
    }

    public void setCollectionData(String collectionData) {
        this.collectionData = collectionData;
    }

    public String getCustomerFacingLabel() {
        return customerFacingLabel;
    }

    public void setCustomerFacingLabel(String customerFacingLabel) {
        this.customerFacingLabel = customerFacingLabel;
    }

    public Long getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(Long triggeredCount) {
        this.triggeredCount = triggeredCount;
    }

    public Integer getPointsCost() {
        return pointsCost;
    }

    public void setPointsCost(Integer pointsCost) {
        this.pointsCost = pointsCost;
    }
}
