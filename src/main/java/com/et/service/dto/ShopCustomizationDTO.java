package com.et.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.et.domain.ShopCustomization} entity.
 */
public class ShopCustomizationDTO implements Serializable {

    private Long id;

    private String shop;

    private Long labelId;

    private String value;


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

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopCustomizationDTO shopCustomizationDTO = (ShopCustomizationDTO) o;
        if (shopCustomizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopCustomizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopCustomizationDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", labelId=" + getLabelId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
