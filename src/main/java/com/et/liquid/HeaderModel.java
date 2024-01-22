package com.et.liquid;


import com.et.api.shopify.product.Product;
import com.et.api.shopify.product.Variant;
import com.et.api.shopify.shop.Shop;

public class HeaderModel {
    private Variant variant;
    private Shop shop;
    private Product product;

    public HeaderModel(Variant variant, Shop shop, Product product) {
        this.variant = variant;
        this.shop = shop;
        this.product = product;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
