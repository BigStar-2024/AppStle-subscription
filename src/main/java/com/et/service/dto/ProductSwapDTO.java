package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.ProductSwap} entity.
 */
public class ProductSwapDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String sourceVariants;

    @Lob
    private String destinationVariants;

    private Boolean updatedFirstOrder;

    private Boolean checkForEveryRecurringOrder;

    @Lob
    private String name;

    private Integer changeNextOrderDateBy;

    private Integer forBillingCycle;

    private Boolean carryDiscountForward;

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

    public String getSourceVariants() {
        return sourceVariants;
    }

    public void setSourceVariants(String sourceVariants) {
        this.sourceVariants = sourceVariants;
    }

    public String getDestinationVariants() {
        return destinationVariants;
    }

    public void setDestinationVariants(String destinationVariants) {
        this.destinationVariants = destinationVariants;
    }

    public Boolean isUpdatedFirstOrder() {
        return updatedFirstOrder;
    }

    public void setUpdatedFirstOrder(Boolean updatedFirstOrder) {
        this.updatedFirstOrder = updatedFirstOrder;
    }

    public Boolean isCheckForEveryRecurringOrder() {
        return checkForEveryRecurringOrder;
    }

    public void setCheckForEveryRecurringOrder(Boolean checkForEveryRecurringOrder) {
        this.checkForEveryRecurringOrder = checkForEveryRecurringOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChangeNextOrderDateBy() {
        return changeNextOrderDateBy;
    }

    public void setChangeNextOrderDateBy(Integer changeNextOrderDateBy) {
        this.changeNextOrderDateBy = changeNextOrderDateBy;
    }

    public Integer getForBillingCycle() {
        return forBillingCycle;
    }

    public void setForBillingCycle(Integer forBillingCycle) {
        this.forBillingCycle = forBillingCycle;
    }

    public Boolean isCarryDiscountForward() {
        return carryDiscountForward;
    }

    public void setCarryDiscountForward(Boolean carryDiscountForward) {
        this.carryDiscountForward = carryDiscountForward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductSwapDTO productSwapDTO = (ProductSwapDTO) o;
        if (productSwapDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productSwapDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductSwapDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", sourceVariants='" + getSourceVariants() + "'" +
            ", destinationVariants='" + getDestinationVariants() + "'" +
            ", updatedFirstOrder='" + isUpdatedFirstOrder() + "'" +
            ", checkForEveryRecurringOrder='" + isCheckForEveryRecurringOrder() + "'" +
            ", name='" + getName() + "'" +
            ", changeNextOrderDateBy=" + getChangeNextOrderDateBy() +
            ", forBillingCycle=" + getForBillingCycle() +
            ", carryDiscountForward='" + isCarryDiscountForward() + "'" +
            "}";
    }
}
