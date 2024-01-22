package com.et.web.rest.vm;

import java.util.Objects;

public class BABSingleSourceProductDTO {

    private Long productId;
    private Long variantId;
    private String productTitle;
    private String productHandle;
    private String variantTitle;


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

    public String getProductHandle() {
        return productHandle;
    }

    public void setProductHandle(String productHandle) {
        this.productHandle = productHandle;
    }

    public String getVariantTitle() {
        return variantTitle;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BABSingleSourceProductDTO that = (BABSingleSourceProductDTO) o;
        return Objects.equals(productId, that.productId) && Objects.equals(variantId, that.variantId) && Objects.equals(productTitle, that.productTitle) && Objects.equals(productHandle, that.productHandle) && Objects.equals(variantTitle, that.variantTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, variantId, productTitle, productHandle, variantTitle);
    }

    @Override
    public String toString() {
        return "BABSingleSourceProductDTO{" +
            "productId=" + productId +
            ", variantId=" + variantId +
            ", productTitle='" + productTitle + '\'' +
            ", productHandle='" + productHandle + '\'' +
            ", variantTitle='" + variantTitle + '\'' +
            '}';
    }
}
