package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ProductCycle.
 */
@Entity
@Table(name = "product_cycle")
public class ProductCycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "source_product")
    private String sourceProduct;

    @Column(name = "destination_products")
    private String destinationProducts;

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

    public ProductCycle shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSourceProduct() {
        return sourceProduct;
    }

    public ProductCycle sourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
        return this;
    }

    public void setSourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public String getDestinationProducts() {
        return destinationProducts;
    }

    public ProductCycle destinationProducts(String destinationProducts) {
        this.destinationProducts = destinationProducts;
        return this;
    }

    public void setDestinationProducts(String destinationProducts) {
        this.destinationProducts = destinationProducts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCycle)) {
            return false;
        }
        return id != null && id.equals(((ProductCycle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCycle{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", sourceProduct='" + getSourceProduct() + "'" +
            ", destinationProducts='" + getDestinationProducts() + "'" +
            "}";
    }
}
