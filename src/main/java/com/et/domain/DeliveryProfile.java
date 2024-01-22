package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A DeliveryProfile.
 */
@Entity
@Table(name = "delivery_profile")
public class DeliveryProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "delivery_profile_id", nullable = false)
    private String deliveryProfileId;

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

    public DeliveryProfile shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDeliveryProfileId() {
        return deliveryProfileId;
    }

    public DeliveryProfile deliveryProfileId(String deliveryProfileId) {
        this.deliveryProfileId = deliveryProfileId;
        return this;
    }

    public void setDeliveryProfileId(String deliveryProfileId) {
        this.deliveryProfileId = deliveryProfileId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryProfile)) {
            return false;
        }
        return id != null && id.equals(((DeliveryProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryProfile{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", deliveryProfileId='" + getDeliveryProfileId() + "'" +
            "}";
    }
}
