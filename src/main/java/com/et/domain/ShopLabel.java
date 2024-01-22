package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ShopLabel.
 */
@Entity
@Table(name = "shop_label")
public class ShopLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Lob
    @Column(name = "labels")
    private String labels;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public ShopLabel shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getLabels() {
        return labels;
    }

    public ShopLabel labels(String labels) {
        this.labels = labels;
        return this;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopLabel)) {
            return false;
        }
        return id != null && id.equals(((ShopLabel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopLabel{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", labels='" + getLabels() + "'" +
            "}";
    }
}
