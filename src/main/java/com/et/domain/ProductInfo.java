package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ProductInfo.
 */
@Entity
@Table(name = "product_info")
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "product_handle")
    private String productHandle;

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

    public ProductInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductInfo productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public ProductInfo productTitle(String productTitle) {
        this.productTitle = productTitle;
        return this;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductHandle() {
        return productHandle;
    }

    public ProductInfo productHandle(String productHandle) {
        this.productHandle = productHandle;
        return this;
    }

    public void setProductHandle(String productHandle) {
        this.productHandle = productHandle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInfo)) {
            return false;
        }
        return id != null && id.equals(((ProductInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInfo{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", productId=" + getProductId() +
            ", productTitle='" + getProductTitle() + "'" +
            ", productHandle='" + getProductHandle() + "'" +
            "}";
    }
}
