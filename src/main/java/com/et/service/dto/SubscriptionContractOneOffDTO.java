package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.SubscriptionContractOneOff} entity.
 */
public class SubscriptionContractOneOffDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private Long billingAttemptId;

    private Long subscriptionContractId;

    private Long variantId;

    private String variantHandle;

    private Integer quantity;

    private Double price;


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

    public Long getBillingAttemptId() {
        return billingAttemptId;
    }

    public void setBillingAttemptId(Long billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getVariantHandle() {
        return variantHandle;
    }

    public void setVariantHandle(String variantHandle) {
        this.variantHandle = variantHandle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractOneOffDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionContractOneOffDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractOneOffDTO{" +
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
