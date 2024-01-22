package com.et.liquid;


import com.et.api.shopify.product.Product;
import com.et.api.shopify.product.Variant;
import com.et.api.shopify.shop.Shop;

public class GenericMessageModel {
    private Variant variant;
    private Product product;
    private Shop shop;
    private String shortProductUrl;

    public GenericMessageModel(Product product, Shop shop, String shortProductUrl, Variant variant) {
        this.product = product;
        this.shop = shop;
        this.shortProductUrl = shortProductUrl;
        this.variant = variant;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getShortProductUrl() {
        return shortProductUrl;
    }

    public void setShortProductUrl(String shortProductUrl) {
        this.shortProductUrl = shortProductUrl;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}
