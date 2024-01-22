package com.et.web.rest.vm;

import java.util.List;
import java.util.Objects;

public class BABSingleProductDTO {

    private Long productId;
    private Long variantId;
    private String productTitle;
    private String productHandle;
    private String variantTitle;

    private Integer minQuantity;
    private Integer maxQuantity;

    private List<BABSingleSourceProductDTO> sourceProducts;

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

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public List<BABSingleSourceProductDTO> getSourceProducts() {
        return sourceProducts;
    }

    public void setSourceProducts(List<BABSingleSourceProductDTO> sourceProducts) {
        this.sourceProducts = sourceProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BABSingleProductDTO that = (BABSingleProductDTO) o;
        return Objects.equals(productId, that.productId) && Objects.equals(variantId, that.variantId) && Objects.equals(productTitle, that.productTitle) && Objects.equals(productHandle, that.productHandle) && Objects.equals(variantTitle, that.variantTitle) && Objects.equals(minQuantity, that.minQuantity) && Objects.equals(maxQuantity, that.maxQuantity) && Objects.equals(sourceProducts, that.sourceProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, variantId, productTitle, productHandle, variantTitle, minQuantity, maxQuantity, sourceProducts);
    }

    @Override
    public String toString() {
        return "BABSingleProductDTO{" +
                "productId=" + productId +
                ", variantId=" + variantId +
                ", productTitle='" + productTitle + '\'' +
                ", productHandle='" + productHandle + '\'' +
                ", variantTitle='" + variantTitle + '\'' +
                ", minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", sourceProducts=" + sourceProducts +
                '}';
    }
}
