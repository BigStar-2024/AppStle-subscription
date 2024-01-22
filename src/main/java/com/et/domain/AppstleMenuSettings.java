package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.ProductViewSettings;

/**
 * A AppstleMenuSettings.
 */
@Entity
@Table(name = "appstle_menu_settings")
public class AppstleMenuSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Lob
    @Column(name = "filter_menu")
    private String filterMenu;

    @Column(name = "menu_url")
    private String menuUrl;

    @Lob
    @Column(name = "menu_style")
    private String menuStyle;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "handle")
    private String handle;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_view_style")
    private ProductViewSettings productViewStyle;

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

    public AppstleMenuSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getFilterMenu() {
        return filterMenu;
    }

    public AppstleMenuSettings filterMenu(String filterMenu) {
        this.filterMenu = filterMenu;
        return this;
    }

    public void setFilterMenu(String filterMenu) {
        this.filterMenu = filterMenu;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public AppstleMenuSettings menuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
        return this;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuStyle() {
        return menuStyle;
    }

    public AppstleMenuSettings menuStyle(String menuStyle) {
        this.menuStyle = menuStyle;
        return this;
    }

    public void setMenuStyle(String menuStyle) {
        this.menuStyle = menuStyle;
    }

    public Boolean isActive() {
        return active;
    }

    public AppstleMenuSettings active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getHandle() {
        return handle;
    }

    public AppstleMenuSettings handle(String handle) {
        this.handle = handle;
        return this;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public ProductViewSettings getProductViewStyle() {
        return productViewStyle;
    }

    public AppstleMenuSettings productViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
        return this;
    }

    public void setProductViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppstleMenuSettings)) {
            return false;
        }
        return id != null && id.equals(((AppstleMenuSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppstleMenuSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", filterMenu='" + getFilterMenu() + "'" +
            ", menuUrl='" + getMenuUrl() + "'" +
            ", menuStyle='" + getMenuStyle() + "'" +
            ", active='" + isActive() + "'" +
            ", handle='" + getHandle() + "'" +
            ", productViewStyle='" + getProductViewStyle() + "'" +
            "}";
    }
}
