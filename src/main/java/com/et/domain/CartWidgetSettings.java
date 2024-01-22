package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.CartWidgetSettingApproach;

import com.et.domain.enumeration.PlacementPosition;

/**
 * A CartWidgetSettings.
 */
@Entity
@Table(name = "cart_widget_settings")
public class CartWidgetSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "enable_cart_widget_settings", nullable = false)
    private Boolean enable_cart_widget_settings;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_widget_setting_approach")
    private CartWidgetSettingApproach cartWidgetSettingApproach;

    @Lob
    @Column(name = "cart_row_selector")
    private String cartRowSelector;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_row_placement")
    private PlacementPosition cartRowPlacement;

    @Lob
    @Column(name = "cart_line_item_selector")
    private String cartLineItemSelector;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_line_item_placement")
    private PlacementPosition cartLineItemPlacement;

    @Lob
    @Column(name = "cart_form_selector")
    private String cartFormSelector;

    @Lob
    @Column(name = "appstel_custome_selector")
    private String appstelCustomeSelector;

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

    public CartWidgetSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isEnable_cart_widget_settings() {
        return enable_cart_widget_settings;
    }

    public CartWidgetSettings enable_cart_widget_settings(Boolean enable_cart_widget_settings) {
        this.enable_cart_widget_settings = enable_cart_widget_settings;
        return this;
    }

    public void setEnable_cart_widget_settings(Boolean enable_cart_widget_settings) {
        this.enable_cart_widget_settings = enable_cart_widget_settings;
    }

    public CartWidgetSettingApproach getCartWidgetSettingApproach() {
        return cartWidgetSettingApproach;
    }

    public CartWidgetSettings cartWidgetSettingApproach(CartWidgetSettingApproach cartWidgetSettingApproach) {
        this.cartWidgetSettingApproach = cartWidgetSettingApproach;
        return this;
    }

    public void setCartWidgetSettingApproach(CartWidgetSettingApproach cartWidgetSettingApproach) {
        this.cartWidgetSettingApproach = cartWidgetSettingApproach;
    }

    public String getCartRowSelector() {
        return cartRowSelector;
    }

    public CartWidgetSettings cartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
        return this;
    }

    public void setCartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
    }

    public PlacementPosition getCartRowPlacement() {
        return cartRowPlacement;
    }

    public CartWidgetSettings cartRowPlacement(PlacementPosition cartRowPlacement) {
        this.cartRowPlacement = cartRowPlacement;
        return this;
    }

    public void setCartRowPlacement(PlacementPosition cartRowPlacement) {
        this.cartRowPlacement = cartRowPlacement;
    }

    public String getCartLineItemSelector() {
        return cartLineItemSelector;
    }

    public CartWidgetSettings cartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
        return this;
    }

    public void setCartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
    }

    public PlacementPosition getCartLineItemPlacement() {
        return cartLineItemPlacement;
    }

    public CartWidgetSettings cartLineItemPlacement(PlacementPosition cartLineItemPlacement) {
        this.cartLineItemPlacement = cartLineItemPlacement;
        return this;
    }

    public void setCartLineItemPlacement(PlacementPosition cartLineItemPlacement) {
        this.cartLineItemPlacement = cartLineItemPlacement;
    }

    public String getCartFormSelector() {
        return cartFormSelector;
    }

    public CartWidgetSettings cartFormSelector(String cartFormSelector) {
        this.cartFormSelector = cartFormSelector;
        return this;
    }

    public void setCartFormSelector(String cartFormSelector) {
        this.cartFormSelector = cartFormSelector;
    }

    public String getAppstelCustomeSelector() {
        return appstelCustomeSelector;
    }

    public CartWidgetSettings appstelCustomeSelector(String appstelCustomeSelector) {
        this.appstelCustomeSelector = appstelCustomeSelector;
        return this;
    }

    public void setAppstelCustomeSelector(String appstelCustomeSelector) {
        this.appstelCustomeSelector = appstelCustomeSelector;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartWidgetSettings)) {
            return false;
        }
        return id != null && id.equals(((CartWidgetSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartWidgetSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", enable_cart_widget_settings='" + isEnable_cart_widget_settings() + "'" +
            ", cartWidgetSettingApproach='" + getCartWidgetSettingApproach() + "'" +
            ", cartRowSelector='" + getCartRowSelector() + "'" +
            ", cartRowPlacement='" + getCartRowPlacement() + "'" +
            ", cartLineItemSelector='" + getCartLineItemSelector() + "'" +
            ", cartLineItemPlacement='" + getCartLineItemPlacement() + "'" +
            ", cartFormSelector='" + getCartFormSelector() + "'" +
            ", appstelCustomeSelector='" + getAppstelCustomeSelector() + "'" +
            "}";
    }
}
