package com.et.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionGroupV2DTO implements Serializable {

    private Long id;

    private Long productCount;

    private Long productVariantCount;

    List<FrequencyInfoDTO> subscriptionPlans = new ArrayList<>();

    private String groupName;

    private String productIds;

    private String productId; //for extension

    private String variantIds;

    private String accessoryProductIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public Long getProductVariantCount() {
        return productVariantCount;
    }

    public void setProductVariantCount(Long productVariantCount) {
        this.productVariantCount = productVariantCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public List<FrequencyInfoDTO> getSubscriptionPlans() {
        return subscriptionPlans;
    }

    public void setSubscriptionPlans(List<FrequencyInfoDTO> subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }

    public String getProductId() {
        return productId;
    }

    public void c(String productId) {
        this.productId = productId;
    }

    public String getAccessoryProductIds() {
        return accessoryProductIds;
    }

    public void setAccessoryProductIds(String accessoryProductIds) {
        this.accessoryProductIds = accessoryProductIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionGroupV2DTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionGroupV2DTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "SubscriptionGroupV2DTO{" +
            "id=" + id +
            ", productCount=" + productCount +
            ", productVariantCount=" + productVariantCount +
            ", subscriptionPlans=" + subscriptionPlans +
            ", groupName='" + groupName + '\'' +
            ", productIds='" + productIds + '\'' +
            ", productId='" + productId + '\'' +
            ", variantIds='" + variantIds + '\'' +
            ", accessoryProductIds='" + getAccessoryProductIds() + "'" +
            '}';
    }
}
