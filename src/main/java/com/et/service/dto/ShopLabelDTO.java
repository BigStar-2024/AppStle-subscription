package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.ShopLabel} entity.
 */
public class ShopLabelDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String labels;

    
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

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopLabelDTO)) {
            return false;
        }

        return id != null && id.equals(((ShopLabelDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopLabelDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", labels='" + getLabels() + "'" +
            "}";
    }
}
