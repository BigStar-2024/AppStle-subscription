package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.MembershipDiscountProducts} entity.
 */
public class MembershipDiscountProductsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private Long membershipDiscountId;

    private Long productId;

    private String productTitle;

    
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

    public Long getMembershipDiscountId() {
        return membershipDiscountId;
    }

    public void setMembershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscountProductsDTO)) {
            return false;
        }

        return id != null && id.equals(((MembershipDiscountProductsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscountProductsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", membershipDiscountId=" + getMembershipDiscountId() +
            ", productId=" + getProductId() +
            ", productTitle='" + getProductTitle() + "'" +
            "}";
    }
}
