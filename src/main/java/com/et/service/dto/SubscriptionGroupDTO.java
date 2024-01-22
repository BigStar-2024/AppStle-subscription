package com.et.service.dto;

import java.io.Serializable;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.et.domain.enumeration.DiscountTypeUnit;

/**
 * A DTO for the {@link com.et.domain.SubscriptionGroup} entity.
 */
public class SubscriptionGroupDTO implements Serializable {

    private Long id;

    private Long productCount;

    private Long productVariantCount;

    private Integer frequencyCount;

    private FrequencyIntervalUnit frequencyInterval;

    private String frequencyName;

    private String groupName;

    private String productIds;

    private Double discountOffer;

    private DiscountTypeUnit discountType;

    private Boolean discountEnabled;


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

    public Integer getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(Integer frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public FrequencyIntervalUnit getFrequencyInterval() {
        return frequencyInterval;
    }

    public void setFrequencyInterval(FrequencyIntervalUnit frequencyInterval) {
        this.frequencyInterval = frequencyInterval;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
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

    public Double getDiscountOffer() {
        return discountOffer;
    }

    public void setDiscountOffer(Double discountOffer) {
        this.discountOffer = discountOffer;
    }

    public DiscountTypeUnit getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeUnit discountType) {
        this.discountType = discountType;
    }

    public Boolean isDiscountEnabled() {
        return discountEnabled;
    }

    public void setDiscountEnabled(Boolean discountEnabled) {
        this.discountEnabled = discountEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionGroupDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionGroupDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionGroupDTO{" +
            "id=" + getId() +
            ", productCount=" + getProductCount() +
            ", productVariantCount=" + getProductVariantCount() +
            ", frequencyCount=" + getFrequencyCount() +
            ", frequencyInterval='" + getFrequencyInterval() + "'" +
            ", frequencyName='" + getFrequencyName() + "'" +
            ", groupName='" + getGroupName() + "'" +
            ", productIds='" + getProductIds() + "'" +
            ", discountOffer=" + getDiscountOffer() +
            ", discountType='" + getDiscountType() + "'" +
            ", discountEnabled='" + isDiscountEnabled() + "'" +
            "}";
    }
}
