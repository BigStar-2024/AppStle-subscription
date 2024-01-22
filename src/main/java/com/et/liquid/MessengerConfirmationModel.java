package com.et.liquid;

import com.et.api.shopify.product.Product;
import com.et.api.shopify.shop.Shop;

public class MessengerConfirmationModel {
    private Product product;
    private Shop shop;

    public MessengerConfirmationModel(Product product, Shop shop) {
        this.product = product;
        this.shop = shop;
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
}
