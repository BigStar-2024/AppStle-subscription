package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A MembershipDiscountProducts.
 */
@Entity
@Table(name = "membership_discount_products")
public class MembershipDiscountProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "membership_discount_id")
    private Long membershipDiscountId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_title")
    private String productTitle;

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

    public MembershipDiscountProducts shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getMembershipDiscountId() {
        return membershipDiscountId;
    }

    public MembershipDiscountProducts membershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
        return this;
    }

    public void setMembershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
    }

    public Long getProductId() {
        return productId;
    }

    public MembershipDiscountProducts productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public MembershipDiscountProducts productTitle(String productTitle) {
        this.productTitle = productTitle;
        return this;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscountProducts)) {
            return false;
        }
        return id != null && id.equals(((MembershipDiscountProducts) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscountProducts{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", membershipDiscountId=" + getMembershipDiscountId() +
            ", productId=" + getProductId() +
            ", productTitle='" + getProductTitle() + "'" +
            "}";
    }
}
