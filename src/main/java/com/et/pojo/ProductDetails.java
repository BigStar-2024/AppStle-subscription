package com.et.pojo;

import java.util.Objects;

public class ProductDetails {
    private Long id;
    private String title;
    private Long variantId;
    private String variantTitle;

    public ProductDetails(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public ProductDetails(Long id, String title, Long variantId, String variantTitle) {
        this.id = id;
        this.title = title;
        this.variantId = variantId;
        this.variantTitle = variantTitle;
    }

    public ProductDetails() {
    }

    public ProductDetails(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
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
        ProductDetails that = (ProductDetails) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(variantId, that.variantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, variantId);
    }
}
