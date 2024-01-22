package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ProductSwap.
 */
@Entity
@Table(name = "product_swap")
public class ProductSwap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Lob
    @Column(name = "source_variants")
    private String sourceVariants;

    @Lob
    @Column(name = "destination_variants")
    private String destinationVariants;

    @Column(name = "updated_first_order")
    private Boolean updatedFirstOrder;

    @Column(name = "check_for_every_recurring_order")
    private Boolean checkForEveryRecurringOrder;

    @Lob
    @Column(name = "name")
    private String name;

    @Column(name = "change_next_order_date_by")
    private Integer changeNextOrderDateBy;

    @Column(name = "for_billing_cycle")
    private Integer forBillingCycle;

    @Column(name = "carry_discount_forward")
    private Boolean carryDiscountForward;

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

    public ProductSwap shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSourceVariants() {
        return sourceVariants;
    }

    public ProductSwap sourceVariants(String sourceVariants) {
        this.sourceVariants = sourceVariants;
        return this;
    }

    public void setSourceVariants(String sourceVariants) {
        this.sourceVariants = sourceVariants;
    }

    public String getDestinationVariants() {
        return destinationVariants;
    }

    public ProductSwap destinationVariants(String destinationVariants) {
        this.destinationVariants = destinationVariants;
        return this;
    }

    public void setDestinationVariants(String destinationVariants) {
        this.destinationVariants = destinationVariants;
    }

    public Boolean isUpdatedFirstOrder() {
        return updatedFirstOrder;
    }

    public ProductSwap updatedFirstOrder(Boolean updatedFirstOrder) {
        this.updatedFirstOrder = updatedFirstOrder;
        return this;
    }

    public void setUpdatedFirstOrder(Boolean updatedFirstOrder) {
        this.updatedFirstOrder = updatedFirstOrder;
    }

    public Boolean isCheckForEveryRecurringOrder() {
        return checkForEveryRecurringOrder;
    }

    public ProductSwap checkForEveryRecurringOrder(Boolean checkForEveryRecurringOrder) {
        this.checkForEveryRecurringOrder = checkForEveryRecurringOrder;
        return this;
    }

    public void setCheckForEveryRecurringOrder(Boolean checkForEveryRecurringOrder) {
        this.checkForEveryRecurringOrder = checkForEveryRecurringOrder;
    }

    public String getName() {
        return name;
    }

    public ProductSwap name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChangeNextOrderDateBy() {
        return changeNextOrderDateBy;
    }

    public ProductSwap changeNextOrderDateBy(Integer changeNextOrderDateBy) {
        this.changeNextOrderDateBy = changeNextOrderDateBy;
        return this;
    }

    public void setChangeNextOrderDateBy(Integer changeNextOrderDateBy) {
        this.changeNextOrderDateBy = changeNextOrderDateBy;
    }

    public Integer getForBillingCycle() {
        return forBillingCycle;
    }

    public ProductSwap forBillingCycle(Integer forBillingCycle) {
        this.forBillingCycle = forBillingCycle;
        return this;
    }

    public void setForBillingCycle(Integer forBillingCycle) {
        this.forBillingCycle = forBillingCycle;
    }

    public Boolean isCarryDiscountForward() {
        return carryDiscountForward;
    }

    public ProductSwap carryDiscountForward(Boolean carryDiscountForward) {
        this.carryDiscountForward = carryDiscountForward;
        return this;
    }

    public void setCarryDiscountForward(Boolean carryDiscountForward) {
        this.carryDiscountForward = carryDiscountForward;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductSwap)) {
            return false;
        }
        return id != null && id.equals(((ProductSwap) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductSwap{" +
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
