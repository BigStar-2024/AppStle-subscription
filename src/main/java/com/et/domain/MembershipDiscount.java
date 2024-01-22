package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A MembershipDiscount.
 */
@Entity
@Table(name = "membership_discount")
public class MembershipDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "title")
    private String title;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "customer_tags")
    private String customerTags;

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

    public MembershipDiscount shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTitle() {
        return title;
    }

    public MembershipDiscount title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDiscount() {
        return discount;
    }

    public MembershipDiscount discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getCustomerTags() {
        return customerTags;
    }

    public MembershipDiscount customerTags(String customerTags) {
        this.customerTags = customerTags;
        return this;
    }

    public void setCustomerTags(String customerTags) {
        this.customerTags = customerTags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscount)) {
            return false;
        }
        return id != null && id.equals(((MembershipDiscount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscount{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", title='" + getTitle() + "'" +
            ", discount=" + getDiscount() +
            ", customerTags='" + getCustomerTags() + "'" +
            "}";
    }
}
