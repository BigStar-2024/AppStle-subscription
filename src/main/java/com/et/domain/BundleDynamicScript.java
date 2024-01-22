package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A BundleDynamicScript.
 */
@Entity
@Table(name = "bundle_dynamic_script")
public class BundleDynamicScript implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Lob
    @Column(name = "dynamic_script")
    private String dynamicScript;

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

    public BundleDynamicScript shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDynamicScript() {
        return dynamicScript;
    }

    public BundleDynamicScript dynamicScript(String dynamicScript) {
        this.dynamicScript = dynamicScript;
        return this;
    }

    public void setDynamicScript(String dynamicScript) {
        this.dynamicScript = dynamicScript;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleDynamicScript)) {
            return false;
        }
        return id != null && id.equals(((BundleDynamicScript) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BundleDynamicScript{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", dynamicScript='" + getDynamicScript() + "'" +
            "}";
    }
}
