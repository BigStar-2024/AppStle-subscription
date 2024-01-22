package com.et.api.appstle.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointEarnRule {
    private Long id;

    private String shop;

    private String type;

    private String status;

    private String historicPurchaseDuration;

    private Integer basePoints;

    private Boolean includeSubtotal;

    private Boolean includeTax;

    private String excludeCollections;

    private String name;

    private String customerNotification;

    private ZonedDateTime createAt;

    private ZonedDateTime updateAt;

    private String rewardInterval;

    private Long productId;

    private Long collectionId;

    private Long variantId;

    private String productData;

    private Long triggeredCount;

    private Integer pointsProvided;

    private String collectionData;

    private String customerFacingLabel;

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

    public String getHistoricPurchaseDuration() {
        return historicPurchaseDuration;
    }

    public void setHistoricPurchaseDuration(String historicPurchaseDuration) {
        this.historicPurchaseDuration = historicPurchaseDuration;
    }

    public Integer getBasePoints() {
        return basePoints;
    }

    public void setBasePoints(Integer basePoints) {
        this.basePoints = basePoints;
    }

    public Boolean getIncludeSubtotal() {
        return includeSubtotal;
    }

    public void setIncludeSubtotal(Boolean includeSubtotal) {
        this.includeSubtotal = includeSubtotal;
    }

    public Boolean getIncludeTax() {
        return includeTax;
    }

    public void setIncludeTax(Boolean includeTax) {
        this.includeTax = includeTax;
    }

    public String getExcludeCollections() {
        return excludeCollections;
    }

    public void setExcludeCollections(String excludeCollections) {
        this.excludeCollections = excludeCollections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerNotification() {
        return customerNotification;
    }

    public void setCustomerNotification(String customerNotification) {
        this.customerNotification = customerNotification;
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

    public String getRewardInterval() {
        return rewardInterval;
    }

    public void setRewardInterval(String rewardInterval) {
        this.rewardInterval = rewardInterval;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
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

    public Long getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(Long triggeredCount) {
        this.triggeredCount = triggeredCount;
    }

    public Integer getPointsProvided() {
        return pointsProvided;
    }

    public void setPointsProvided(Integer pointsProvided) {
        this.pointsProvided = pointsProvided;
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
}
