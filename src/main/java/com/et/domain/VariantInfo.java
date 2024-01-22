package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A VariantInfo.
 */
@Entity
@Table(name = "variant_info")
public class VariantInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "variant_title")
    private String variantTitle;

    @Column(name = "sku")
    private String sku;

    @Column(name = "variant_price")
    private String variantPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public VariantInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getProductId() {
        return productId;
    }

    public VariantInfo productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public VariantInfo variantId(Long variantId) {
        this.variantId = variantId;
        return this;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public VariantInfo productTitle(String productTitle) {
        this.productTitle = productTitle;
        return this;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getVariantTitle() {
        return variantTitle;
    }

    public VariantInfo variantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
        return this;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    public String getSku() {
        return sku;
    }

    public VariantInfo sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVariantPrice() {
        return variantPrice;
    }

    public VariantInfo variantPrice(String variantPrice) {
        this.variantPrice = variantPrice;
        return this;
    }

    public void setVariantPrice(String variantPrice) {
        this.variantPrice = variantPrice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VariantInfo)) {
            return false;
        }
        return id != null && id.equals(((VariantInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VariantInfo{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", productId=" + getProductId() +
            ", variantId=" + getVariantId() +
            ", productTitle='" + getProductTitle() + "'" +
            ", variantTitle='" + getVariantTitle() + "'" +
            ", sku='" + getSku() + "'" +
            ", variantPrice='" + getVariantPrice() + "'" +
            "}";
    }
}
