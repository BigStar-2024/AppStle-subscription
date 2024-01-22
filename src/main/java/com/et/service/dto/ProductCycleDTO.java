package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.ProductCycle} entity.
 */
public class ProductCycleDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private String sourceProduct;

    private String destinationProducts;

    
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

    public String getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public String getDestinationProducts() {
        return destinationProducts;
    }

    public void setDestinationProducts(String destinationProducts) {
        this.destinationProducts = destinationProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCycleDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductCycleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCycleDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", sourceProduct='" + getSourceProduct() + "'" +
            ", destinationProducts='" + getDestinationProducts() + "'" +
            "}";
    }
}
