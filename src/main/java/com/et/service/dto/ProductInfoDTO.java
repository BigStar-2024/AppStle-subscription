package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.ProductInfo} entity.
 */
public class ProductInfoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private Long productId;

    private String productTitle;

    private String productHandle;

    
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductHandle() {
        return productHandle;
    }

    public void setProductHandle(String productHandle) {
        this.productHandle = productHandle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", productId=" + getProductId() +
            ", productTitle='" + getProductTitle() + "'" +
            ", productHandle='" + getProductHandle() + "'" +
            "}";
    }
}
