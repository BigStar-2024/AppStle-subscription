package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SubscriptionGroupPlan.
 */
@Entity
@Table(name = "subscription_group_plan")
public class SubscriptionGroupPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "product_variant_count")
    private Integer productVariantCount;

    @Lob
    @Column(name = "info_json")
    private String infoJson;

    @Lob
    @Column(name = "product_ids")
    private String productIds;

    @Lob
    @Column(name = "variant_ids")
    private String variantIds;

    @Lob
    @Column(name = "variant_product_ids")
    private String variantProductIds;

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

    public SubscriptionGroupPlan shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getGroupName() {
        return groupName;
    }

    public SubscriptionGroupPlan groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public SubscriptionGroupPlan subscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public SubscriptionGroupPlan productCount(Integer productCount) {
        this.productCount = productCount;
        return this;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getProductVariantCount() {
        return productVariantCount;
    }

    public SubscriptionGroupPlan productVariantCount(Integer productVariantCount) {
        this.productVariantCount = productVariantCount;
        return this;
    }

    public void setProductVariantCount(Integer productVariantCount) {
        this.productVariantCount = productVariantCount;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public SubscriptionGroupPlan infoJson(String infoJson) {
        this.infoJson = infoJson;
        return this;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }

    public String getProductIds() {
        return productIds;
    }

    public SubscriptionGroupPlan productIds(String productIds) {
        this.productIds = productIds;
        return this;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getVariantIds() {
        return variantIds;
    }

    public SubscriptionGroupPlan variantIds(String variantIds) {
        this.variantIds = variantIds;
        return this;
    }

    public void setVariantIds(String variantIds) {
        this.variantIds = variantIds;
    }

    public String getVariantProductIds() {
        return variantProductIds;
    }

    public SubscriptionGroupPlan variantProductIds(String variantProductIds) {
        this.variantProductIds = variantProductIds;
        return this;
    }

    public void setVariantProductIds(String variantProductIds) {
        this.variantProductIds = variantProductIds;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionGroupPlan)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionGroupPlan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionGroupPlan{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", groupName='" + getGroupName() + "'" +
            ", subscriptionId=" + getSubscriptionId() +
            ", productCount=" + getProductCount() +
            ", productVariantCount=" + getProductVariantCount() +
            ", infoJson='" + getInfoJson() + "'" +
            ", productIds='" + getProductIds() + "'" +
            ", variantIds='" + getVariantIds() + "'" +
            ", variantProductIds='" + getVariantProductIds() + "'" +
            "}";
    }
}
