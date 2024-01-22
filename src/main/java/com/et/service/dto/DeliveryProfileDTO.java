package com.et.service.dto;

import com.et.domain.DeliveryProfile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link DeliveryProfile} entity.
 */
public class DeliveryProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private String deliveryProfileId;

    private Set<String> sellerGroupIds = new HashSet<>();
    private String name;


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

    public String getDeliveryProfileId() {
        return deliveryProfileId;
    }

    public void setDeliveryProfileId(String deliveryProfileId) {
        this.deliveryProfileId = deliveryProfileId;
    }

    public Set<String> getSellerGroupIds() {
        return sellerGroupIds;
    }

    public void setSellerGroupIds(Set<String> sellerGroupIds) {
        this.sellerGroupIds = sellerGroupIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((DeliveryProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryProfileDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", deliveryProfileId='" + getDeliveryProfileId() + "'" +
            "}";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
