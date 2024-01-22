package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.et.domain.VariantInfo} entity.
 */
public class VariantInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private Long productId;

    private Long variantId;

    private String productTitle;

    private String variantTitle;

    private String sku;

    private String variantPrice;


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

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getVariantTitle() {
        return variantTitle;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(String variantPrice) {
        this.variantPrice = variantPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VariantInfoDTO variantInfoDTO = (VariantInfoDTO) o;
        if (variantInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), variantInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VariantInfoDTO{" +
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
