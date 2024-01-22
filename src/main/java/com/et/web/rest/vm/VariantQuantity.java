package com.et.web.rest.vm;

public class VariantQuantity {
    private Long variantId;
    private Integer quantity;
    private String title;
    private String image;
    private String productTitle;
    private String productId;
    private String variantTitle;

    public VariantQuantity() {
    }

    public VariantQuantity(Long variantId, Integer quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public VariantQuantity(Long variantId, Integer quantity, String productTitle) {
        this.variantId = variantId;
        this.quantity = quantity;
        this.productTitle = productTitle;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    public String getVariantTitle() {
        return variantTitle;
    }
}
