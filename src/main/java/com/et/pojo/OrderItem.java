package com.et.pojo;

import com.et.web.rest.vm.AttributeInfo;

import java.util.List;

public class OrderItem {

    private int quantity;
    private String title;
    private String price;
    private String imageUrl;
    private Boolean isTaxable;
    private String sellingPlanName;
    private String variantTitle;
    private boolean subscriptionProduct;
    private List<AttributeInfo> customAttributes;
    private String variantSku;

    public String getVariantTitle() {
        return variantTitle;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    public String getSellingPlanName() {
        return sellingPlanName;
    }

    public void setSellingPlanName(String sellingPlanName) {
        this.sellingPlanName = sellingPlanName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getTaxable() {
        return isTaxable;
    }

    public void setTaxable(Boolean taxable) {
        isTaxable = taxable;
    }

    public List<AttributeInfo> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<AttributeInfo> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public boolean isSubscriptionProduct() {
        return subscriptionProduct;
    }

    public void setSubscriptionProduct(boolean subscriptionProduct) {
        this.subscriptionProduct = subscriptionProduct;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "quantity=" + quantity +
            ", title='" + title + '\'' +
            ", price='" + price + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", isTaxable=" + isTaxable +
            ", sellingPlanName='" + sellingPlanName + '\'' +
            ", variantTitle='" + variantTitle + '\'' +
            ", subscriptionProduct=" + subscriptionProduct +
            ", customAttributes=" + customAttributes +
            ", variantSku='" + variantSku + '\'' +
            '}';
    }
}
