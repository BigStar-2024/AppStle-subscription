package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.BundleStatus;

import com.et.domain.enumeration.BundleDiscountType;

import com.et.domain.enumeration.BundleLevel;

import com.et.domain.enumeration.BundleDiscountCondition;

import com.et.domain.enumeration.BundleType;

/**
 * A BundleRule.
 */
@Entity
@Table(name = "bundle_rule")
public class BundleRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price_summary")
    private String priceSummary;

    @Column(name = "action_button_text")
    private String actionButtonText;

    @Column(name = "action_button_description")
    private String actionButtonDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BundleStatus status;

    @Column(name = "show_bundle_widget")
    private Boolean showBundleWidget;

    @Column(name = "customer_include_tags")
    private String customerIncludeTags;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private BundleDiscountType discountType;

    @Column(name = "discount_value")
    private Double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "bundle_level")
    private BundleLevel bundleLevel;

    @Lob
    @Column(name = "products")
    private String products;

    @Lob
    @Column(name = "variants")
    private String variants;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_condition")
    private BundleDiscountCondition discountCondition;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "bundle_type")
    private BundleType bundleType;

    @Column(name = "show_combined_selling_plan")
    private Boolean showCombinedSellingPlan;

    @Column(name = "select_subscription_by_default")
    private Boolean selectSubscriptionByDefault;

    @Column(name = "minimum_number_of_items")
    private Integer minimumNumberOfItems;

    @Column(name = "maximum_number_of_items")
    private Integer maximumNumberOfItems;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public BundleRule shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public BundleRule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public BundleRule title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public BundleRule description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceSummary() {
        return priceSummary;
    }

    public BundleRule priceSummary(String priceSummary) {
        this.priceSummary = priceSummary;
        return this;
    }

    public void setPriceSummary(String priceSummary) {
        this.priceSummary = priceSummary;
    }

    public String getActionButtonText() {
        return actionButtonText;
    }

    public BundleRule actionButtonText(String actionButtonText) {
        this.actionButtonText = actionButtonText;
        return this;
    }

    public void setActionButtonText(String actionButtonText) {
        this.actionButtonText = actionButtonText;
    }

    public String getActionButtonDescription() {
        return actionButtonDescription;
    }

    public BundleRule actionButtonDescription(String actionButtonDescription) {
        this.actionButtonDescription = actionButtonDescription;
        return this;
    }

    public void setActionButtonDescription(String actionButtonDescription) {
        this.actionButtonDescription = actionButtonDescription;
    }

    public BundleStatus getStatus() {
        return status;
    }

    public BundleRule status(BundleStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BundleStatus status) {
        this.status = status;
    }

    public Boolean isShowBundleWidget() {
        return showBundleWidget;
    }

    public BundleRule showBundleWidget(Boolean showBundleWidget) {
        this.showBundleWidget = showBundleWidget;
        return this;
    }

    public void setShowBundleWidget(Boolean showBundleWidget) {
        this.showBundleWidget = showBundleWidget;
    }

    public String getCustomerIncludeTags() {
        return customerIncludeTags;
    }

    public BundleRule customerIncludeTags(String customerIncludeTags) {
        this.customerIncludeTags = customerIncludeTags;
        return this;
    }

    public void setCustomerIncludeTags(String customerIncludeTags) {
        this.customerIncludeTags = customerIncludeTags;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public BundleRule startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public BundleRule endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public BundleDiscountType getDiscountType() {
        return discountType;
    }

    public BundleRule discountType(BundleDiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(BundleDiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public BundleRule discountValue(Double discountValue) {
        this.discountValue = discountValue;
        return this;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public BundleLevel getBundleLevel() {
        return bundleLevel;
    }

    public BundleRule bundleLevel(BundleLevel bundleLevel) {
        this.bundleLevel = bundleLevel;
        return this;
    }

    public void setBundleLevel(BundleLevel bundleLevel) {
        this.bundleLevel = bundleLevel;
    }

    public String getProducts() {
        return products;
    }

    public BundleRule products(String products) {
        this.products = products;
        return this;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getVariants() {
        return variants;
    }

    public BundleRule variants(String variants) {
        this.variants = variants;
        return this;
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public BundleDiscountCondition getDiscountCondition() {
        return discountCondition;
    }

    public BundleRule discountCondition(BundleDiscountCondition discountCondition) {
        this.discountCondition = discountCondition;
        return this;
    }

    public void setDiscountCondition(BundleDiscountCondition discountCondition) {
        this.discountCondition = discountCondition;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public BundleRule sequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
        return this;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public BundleType getBundleType() {
        return bundleType;
    }

    public BundleRule bundleType(BundleType bundleType) {
        this.bundleType = bundleType;
        return this;
    }

    public void setBundleType(BundleType bundleType) {
        this.bundleType = bundleType;
    }

    public Boolean isShowCombinedSellingPlan() {
        return showCombinedSellingPlan;
    }

    public BundleRule showCombinedSellingPlan(Boolean showCombinedSellingPlan) {
        this.showCombinedSellingPlan = showCombinedSellingPlan;
        return this;
    }

    public void setShowCombinedSellingPlan(Boolean showCombinedSellingPlan) {
        this.showCombinedSellingPlan = showCombinedSellingPlan;
    }

    public Boolean isSelectSubscriptionByDefault() {
        return selectSubscriptionByDefault;
    }

    public BundleRule selectSubscriptionByDefault(Boolean selectSubscriptionByDefault) {
        this.selectSubscriptionByDefault = selectSubscriptionByDefault;
        return this;
    }

    public void setSelectSubscriptionByDefault(Boolean selectSubscriptionByDefault) {
        this.selectSubscriptionByDefault = selectSubscriptionByDefault;
    }

    public Integer getMinimumNumberOfItems() {
        return minimumNumberOfItems;
    }

    public BundleRule minimumNumberOfItems(Integer minimumNumberOfItems) {
        this.minimumNumberOfItems = minimumNumberOfItems;
        return this;
    }

    public void setMinimumNumberOfItems(Integer minimumNumberOfItems) {
        this.minimumNumberOfItems = minimumNumberOfItems;
    }

    public Integer getMaximumNumberOfItems() {
        return maximumNumberOfItems;
    }

    public BundleRule maximumNumberOfItems(Integer maximumNumberOfItems) {
        this.maximumNumberOfItems = maximumNumberOfItems;
        return this;
    }

    public void setMaximumNumberOfItems(Integer maximumNumberOfItems) {
        this.maximumNumberOfItems = maximumNumberOfItems;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public BundleRule maxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleRule)) {
            return false;
        }
        return id != null && id.equals(((BundleRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BundleRule{" +
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
