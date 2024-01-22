package com.et.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 */
public class ProductFieldDTO implements Serializable {

    private Long id;

    private String shop;

    private String title;

    private Boolean enabled;


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

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductFieldDTO productFieldDTO = (ProductFieldDTO) o;
        if (productFieldDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productFieldDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductFieldDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", title='" + getTitle() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
