package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A CustomerPortalDynamicScript.
 */
@Entity
@Table(name = "customer_portal_dynamic_script")
public class CustomerPortalDynamicScript implements Serializable {

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

    public CustomerPortalDynamicScript shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDynamicScript() {
        return dynamicScript;
    }

    public CustomerPortalDynamicScript dynamicScript(String dynamicScript) {
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
        if (!(o instanceof CustomerPortalDynamicScript)) {
            return false;
        }
        return id != null && id.equals(((CustomerPortalDynamicScript) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerPortalDynamicScript{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", dynamicScript='" + getDynamicScript() + "'" +
            "}";
    }
}
