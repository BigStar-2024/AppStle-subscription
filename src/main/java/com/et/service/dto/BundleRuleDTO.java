package com.et.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.BundleStatus;
import com.et.domain.enumeration.BundleDiscountType;
import com.et.domain.enumeration.BundleLevel;
import com.et.domain.enumeration.BundleDiscountCondition;
import com.et.domain.enumeration.BundleType;

/**
 * A DTO for the {@link com.et.domain.BundleRule} entity.
 */
public class BundleRuleDTO implements Serializable {

    private Long id;

    private String shop;

    private String name;

    private String title;

    private String description;

    private String priceSummary;

    private String actionButtonText;

    private String actionButtonDescription;

    private BundleStatus status;

    private Boolean showBundleWidget;

    private String customerIncludeTags;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private BundleDiscountType discountType;

    private Double discountValue;

    private BundleLevel bundleLevel;

    @Lob
    private String products;

    @Lob
    private String variants;

    private BundleDiscountCondition discountCondition;

    private Integer sequenceNo;

    private BundleType bundleType;

    private Boolean showCombinedSellingPlan;

    private Boolean selectSubscriptionByDefault;

    private Integer minimumNumberOfItems;

    private Integer maximumNumberOfItems;

    private Integer maxQuantity;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceSummary() {
        return priceSummary;
    }

    public void setPriceSummary(String priceSummary) {
        this.priceSummary = priceSummary;
    }

    public String getActionButtonText() {
        return actionButtonText;
    }

    public void setActionButtonText(String actionButtonText) {
        this.actionButtonText = actionButtonText;
    }

    public String getActionButtonDescription() {
        return actionButtonDescription;
    }

    public void setActionButtonDescription(String actionButtonDescription) {
        this.actionButtonDescription = actionButtonDescription;
    }

    public BundleStatus getStatus() {
        return status;
    }

    public void setStatus(BundleStatus status) {
        this.status = status;
    }

    public Boolean isShowBundleWidget() {
        return showBundleWidget;
    }

    public void setShowBundleWidget(Boolean showBundleWidget) {
        this.showBundleWidget = showBundleWidget;
    }

    public String getCustomerIncludeTags() {
        return customerIncludeTags;
    }

    public void setCustomerIncludeTags(String customerIncludeTags) {
        this.customerIncludeTags = customerIncludeTags;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public BundleDiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(BundleDiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public BundleLevel getBundleLevel() {
        return bundleLevel;
    }

    public void setBundleLevel(BundleLevel bundleLevel) {
        this.bundleLevel = bundleLevel;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getVariants() {
        return variants;
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public BundleDiscountCondition getDiscountCondition() {
        return discountCondition;
    }

    public void setDiscountCondition(BundleDiscountCondition discountCondition) {
        this.discountCondition = discountCondition;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BundleType getBundleType() {
        return bundleType;
    }

    public void setBundleType(BundleType bundleType) {
        this.bundleType = bundleType;
    }

    public Boolean isShowCombinedSellingPlan() {
        return showCombinedSellingPlan;
    }

    public void setShowCombinedSellingPlan(Boolean showCombinedSellingPlan) {
        this.showCombinedSellingPlan = showCombinedSellingPlan;
    }

    public Boolean isSelectSubscriptionByDefault() {
        return selectSubscriptionByDefault;
    }

    public void setSelectSubscriptionByDefault(Boolean selectSubscriptionByDefault) {
        this.selectSubscriptionByDefault = selectSubscriptionByDefault;
    }

    public Integer getMinimumNumberOfItems() {
        return minimumNumberOfItems;
    }

    public void setMinimumNumberOfItems(Integer minimumNumberOfItems) {
        this.minimumNumberOfItems = minimumNumberOfItems;
    }

    public Integer getMaximumNumberOfItems() {
        return maximumNumberOfItems;
    }

    public void setMaximumNumberOfItems(Integer maximumNumberOfItems) {
        this.maximumNumberOfItems = maximumNumberOfItems;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleRuleDTO)) {
            return false;
        }

        return id != null && id.equals(((BundleRuleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BundleRuleDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", priceSummary='" + getPriceSummary() + "'" +
            ", actionButtonText='" + getActionButtonText() + "'" +
            ", actionButtonDescription='" + getActionButtonDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", showBundleWidget='" + isShowBundleWidget() + "'" +
            ", customerIncludeTags='" + getCustomerIncludeTags() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", bundleLevel='" + getBundleLevel() + "'" +
            ", products='" + getProducts() + "'" +
            ", variants='" + getVariants() + "'" +
            ", discountCondition='" + getDiscountCondition() + "'" +
            ", sequenceNo=" + getSequenceNo() +
            ", bundleType='" + getBundleType() + "'" +
            ", showCombinedSellingPlan='" + isShowCombinedSellingPlan() + "'" +
            ", selectSubscriptionByDefault='" + isSelectSubscriptionByDefault() + "'" +
            ", minimumNumberOfItems=" + getMinimumNumberOfItems() +
            ", maximumNumberOfItems=" + getMaximumNumberOfItems() +
            ", maxQuantity=" + getMaxQuantity() +
            "}";
    }
}
