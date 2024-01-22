package com.et.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 */
public class ProductAutoCompleteResponseDTO implements Serializable {

    private Long id;

    private Long productId;

    private String name;

    private String imageUrl;

    @NotNull
    private String shop;


    private Long offersId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getOffersId() {
        return offersId;
    }

    public void setOffersId(Long offerId) {
        this.offersId = offerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductAutoCompleteResponseDTO productAutoCompleteResponseDTO = (ProductAutoCompleteResponseDTO) o;
        if (productAutoCompleteResponseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productAutoCompleteResponseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferProductDTO{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", name='" + getName() + "'" +
            ", shop='" + getShop() + "'" +
            ", offers=" + getOffersId() +
            "}";
    }
}
