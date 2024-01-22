package com.et.liquid;


import com.et.api.shopify.shop.Shop;

public class SubjectModel {
    private Shop shop;

    public SubjectModel(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
