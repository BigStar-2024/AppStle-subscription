package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SubscriptionContractOneOff.
 */
@Entity
@Table(name = "subscription_contract_one_off")
public class SubscriptionContractOneOff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "billing_attempt_id")
    private Long billingAttemptId;

    @Column(name = "subscription_contract_id")
    private Long subscriptionContractId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "variant_handle")
    private String variantHandle;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

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

    public SubscriptionContractOneOff shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getBillingAttemptId() {
        return billingAttemptId;
    }

    public SubscriptionContractOneOff billingAttemptId(Long billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
        return this;
    }

    public void setBillingAttemptId(Long billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public SubscriptionContractOneOff subscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
        return this;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public SubscriptionContractOneOff variantId(Long variantId) {
        this.variantId = variantId;
        return this;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getVariantHandle() {
        return variantHandle;
    }

    public SubscriptionContractOneOff variantHandle(String variantHandle) {
        this.variantHandle = variantHandle;
        return this;
    }

    public void setVariantHandle(String variantHandle) {
        this.variantHandle = variantHandle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public SubscriptionContractOneOff quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public SubscriptionContractOneOff price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractOneOff)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionContractOneOff) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionContractOneOff{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", billingAttemptId=" + getBillingAttemptId() +
            ", subscriptionContractId=" + getSubscriptionContractId() +
            ", variantId=" + getVariantId() +
            ", variantHandle='" + getVariantHandle() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            "}";
    }
}
