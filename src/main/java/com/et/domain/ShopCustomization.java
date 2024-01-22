package com.et.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ShopCustomization.
 */
@Entity
@Table(name = "shop_customization")
public class ShopCustomization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop")
    private String shop;

    @Column(name = "label_id")
    private Long labelId;

    @Column(name = "value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public ShopCustomization shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getLabelId() {
        return labelId;
    }

    public ShopCustomization labelId(Long labelId) {
        this.labelId = labelId;
        return this;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getValue() {
        return value;
    }

    public ShopCustomization value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopCustomization)) {
            return false;
        }
        return id != null && id.equals(((ShopCustomization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShopCustomization{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", labelId=" + getLabelId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
