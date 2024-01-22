package com.et.web.rest.vm;

public class ProductBoughtStats {

    private Long productId;
    private long count;
    private double percentageOfAllPurchases;
    private String title;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getPercentageOfAllPurchases() {
        return percentageOfAllPurchases;
    }

    public void setPercentageOfAllPurchases(double percentageOfAllPurchases) {
        this.percentageOfAllPurchases = percentageOfAllPurchases;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
