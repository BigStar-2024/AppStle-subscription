package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.SubscriptionGroupPlan} entity.
 */
public class SubscriptionGroupPlanDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private String groupName;

    private Long subscriptionId;

    private Integer productCount;

    private Integer productVariantCount;

    @Lob
    private String infoJson;

    @Lob
    private String productIds;

    @Lob
    private String variantIds;

    @Lob
    private String variantProductIds;

    
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getProductVariantCount() {
        return productVariantCount;
    }

    public void setProductVariantCount(Integer productVariantCount) {
        this.productVariantCount = productVariantCount;
    }

    public String getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getVariantIds() {
        return variantIds;
    }

    public void setVariantIds(String variantIds) {
        this.variantIds = variantIds;
    }

    public String getVariantProductIds() {
        return variantProductIds;
    }

    public void setVariantProductIds(String variantProductIds) {
        this.variantProductIds = variantProductIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionGroupPlanDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionGroupPlanDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionGroupPlanDTO{" +
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
