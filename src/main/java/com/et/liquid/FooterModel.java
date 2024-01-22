package com.et.liquid;


import com.et.api.shopify.product.Product;
import com.et.api.shopify.product.Variant;
import com.et.api.shopify.shop.Shop;

public class FooterModel {
    private Variant variant;
    private String link_color;
    private Shop shop;
    private Product product;
    private Customer customer;

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public String getLink_color() {
        return link_color;
    }

    public void setLink_color(String link_color) {
        this.link_color = link_color;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
