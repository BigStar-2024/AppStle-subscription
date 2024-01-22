package com.et.liquid;


import com.et.api.shopify.shop.Shop;

public class GenericSmsModel {

    private Shop shop;
    private Customer customer;
    private String manageSubscriptionUrl;
    private String lastFourDigits;
    private String nextOrderDate;

    public GenericSmsModel(String shopName, Customer customer, String manageSubscriptionUrl, String lastFourDigits, String nextOrderDate) {
        this.lastFourDigits = lastFourDigits;
        Shop shop1 = new Shop();
        shop1.setName(shopName);
        this.shop = shop1;
        this.customer = customer;
        this.manageSubscriptionUrl = manageSubscriptionUrl;
        this.nextOrderDate = nextOrderDate;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getManageSubscriptionUrl() {
        return manageSubscriptionUrl;
    }

    public void setManageSubscriptionUrl(String manageSubscriptionUrl) {
        this.manageSubscriptionUrl = manageSubscriptionUrl;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(String nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

}
