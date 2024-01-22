package com.et.service.dto;

public class TieredDiscountDTO {

    private String discountBasedOn;
    private Integer quantity;
    private Double discount;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDiscountBasedOn() {
        return discountBasedOn;
    }

    public void setDiscountBasedOn(String discountBasedOn) {
        this.discountBasedOn = discountBasedOn;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "TieredDiscountDTO{" +
            "discountBasedOn='" + discountBasedOn + '\'' +
            ", quantity=" + quantity +
            ", discount=" + discount +
            '}';
    }
}
