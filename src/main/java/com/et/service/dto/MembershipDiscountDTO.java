package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.MembershipDiscount} entity.
 */
public class MembershipDiscountDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private String title;

    private Double discount;

    private String customerTags;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getCustomerTags() {
        return customerTags;
    }

    public void setCustomerTags(String customerTags) {
        this.customerTags = customerTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscountDTO)) {
            return false;
        }

        return id != null && id.equals(((MembershipDiscountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscountDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", title='" + getTitle() + "'" +
            ", discount=" + getDiscount() +
            ", customerTags='" + getCustomerTags() + "'" +
            "}";
    }
}
