package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.PlacementPosition;

/**
 * A ThemeCode.
 */
@Entity
@Table(name = "theme_code")
public class ThemeCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "theme_name", nullable = false)
    private String themeName;

    @NotNull
    @Column(name = "theme_name_friendly", nullable = false)
    private String themeNameFriendly;

    @Column(name = "theme_store_id")
    private Integer themeStoreId;

    @Lob
    @Column(name = "add_to_cart_selector")
    private String addToCartSelector;

    @Lob
    @Column(name = "subscription_link_selector")
    private String subscriptionLinkSelector;

    @Enumerated(EnumType.STRING)
    @Column(name = "add_to_cart_placement")
    private PlacementPosition addToCartPlacement;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_link_placement")
    private PlacementPosition subscriptionLinkPlacement;

    @Lob
    @Column(name = "price_selector")
    private String priceSelector;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_placement")
    private PlacementPosition pricePlacement;

    @Lob
    @Column(name = "badge_top")
    private String badgeTop;

    @Lob
    @Column(name = "cart_row_selector")
    private String cartRowSelector;

    @Lob
    @Column(name = "cart_line_item_selector")
    private String cartLineItemSelector;

    @Lob
    @Column(name = "cart_line_item_per_quantity_price_selector")
    private String cartLineItemPerQuantityPriceSelector;

    @Lob
    @Column(name = "cart_line_item_total_price_selector")
    private String cartLineItemTotalPriceSelector;

    @Lob
    @Column(name = "cart_line_item_selling_plan_name_selector")
    private String cartLineItemSellingPlanNameSelector;

    @Lob
    @Column(name = "cart_sub_total_selector")
    private String cartSubTotalSelector;

    @Lob
    @Column(name = "cart_line_item_price_selector")
    private String cartLineItemPriceSelector;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public ThemeCode themeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeNameFriendly() {
        return themeNameFriendly;
    }

    public ThemeCode themeNameFriendly(String themeNameFriendly) {
        this.themeNameFriendly = themeNameFriendly;
        return this;
    }

    public void setThemeNameFriendly(String themeNameFriendly) {
        this.themeNameFriendly = themeNameFriendly;
    }

    public Integer getThemeStoreId() {
        return themeStoreId;
    }

    public ThemeCode themeStoreId(Integer themeStoreId) {
        this.themeStoreId = themeStoreId;
        return this;
    }

    public void setThemeStoreId(Integer themeStoreId) {
        this.themeStoreId = themeStoreId;
    }

    public String getAddToCartSelector() {
        return addToCartSelector;
    }

    public ThemeCode addToCartSelector(String addToCartSelector) {
        this.addToCartSelector = addToCartSelector;
        return this;
    }

    public void setAddToCartSelector(String addToCartSelector) {
        this.addToCartSelector = addToCartSelector;
    }

    public String getSubscriptionLinkSelector() {
        return subscriptionLinkSelector;
    }

    public ThemeCode subscriptionLinkSelector(String subscriptionLinkSelector) {
        this.subscriptionLinkSelector = subscriptionLinkSelector;
        return this;
    }

    public void setSubscriptionLinkSelector(String subscriptionLinkSelector) {
        this.subscriptionLinkSelector = subscriptionLinkSelector;
    }

    public PlacementPosition getAddToCartPlacement() {
        return addToCartPlacement;
    }

    public ThemeCode addToCartPlacement(PlacementPosition addToCartPlacement) {
        this.addToCartPlacement = addToCartPlacement;
        return this;
    }

    public void setAddToCartPlacement(PlacementPosition addToCartPlacement) {
        this.addToCartPlacement = addToCartPlacement;
    }

    public PlacementPosition getSubscriptionLinkPlacement() {
        return subscriptionLinkPlacement;
    }

    public ThemeCode subscriptionLinkPlacement(PlacementPosition subscriptionLinkPlacement) {
        this.subscriptionLinkPlacement = subscriptionLinkPlacement;
        return this;
    }

    public void setSubscriptionLinkPlacement(PlacementPosition subscriptionLinkPlacement) {
        this.subscriptionLinkPlacement = subscriptionLinkPlacement;
    }

    public String getPriceSelector() {
        return priceSelector;
    }

    public ThemeCode priceSelector(String priceSelector) {
        this.priceSelector = priceSelector;
        return this;
    }

    public void setPriceSelector(String priceSelector) {
        this.priceSelector = priceSelector;
    }

    public PlacementPosition getPricePlacement() {
        return pricePlacement;
    }

    public ThemeCode pricePlacement(PlacementPosition pricePlacement) {
        this.pricePlacement = pricePlacement;
        return this;
    }

    public void setPricePlacement(PlacementPosition pricePlacement) {
        this.pricePlacement = pricePlacement;
    }

    public String getBadgeTop() {
        return badgeTop;
    }

    public ThemeCode badgeTop(String badgeTop) {
        this.badgeTop = badgeTop;
        return this;
    }

    public void setBadgeTop(String badgeTop) {
        this.badgeTop = badgeTop;
    }

    public String getCartRowSelector() {
        return cartRowSelector;
    }

    public ThemeCode cartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
        return this;
    }

    public void setCartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
    }

    public String getCartLineItemSelector() {
        return cartLineItemSelector;
    }

    public ThemeCode cartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
        return this;
    }

    public void setCartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
    }

    public String getCartLineItemPerQuantityPriceSelector() {
        return cartLineItemPerQuantityPriceSelector;
    }

    public ThemeCode cartLineItemPerQuantityPriceSelector(String cartLineItemPerQuantityPriceSelector) {
        this.cartLineItemPerQuantityPriceSelector = cartLineItemPerQuantityPriceSelector;
        return this;
    }

    public void setCartLineItemPerQuantityPriceSelector(String cartLineItemPerQuantityPriceSelector) {
        this.cartLineItemPerQuantityPriceSelector = cartLineItemPerQuantityPriceSelector;
    }

    public String getCartLineItemTotalPriceSelector() {
        return cartLineItemTotalPriceSelector;
    }

    public ThemeCode cartLineItemTotalPriceSelector(String cartLineItemTotalPriceSelector) {
        this.cartLineItemTotalPriceSelector = cartLineItemTotalPriceSelector;
        return this;
    }

    public void setCartLineItemTotalPriceSelector(String cartLineItemTotalPriceSelector) {
        this.cartLineItemTotalPriceSelector = cartLineItemTotalPriceSelector;
    }

    public String getCartLineItemSellingPlanNameSelector() {
        return cartLineItemSellingPlanNameSelector;
    }

    public ThemeCode cartLineItemSellingPlanNameSelector(String cartLineItemSellingPlanNameSelector) {
        this.cartLineItemSellingPlanNameSelector = cartLineItemSellingPlanNameSelector;
        return this;
    }

    public void setCartLineItemSellingPlanNameSelector(String cartLineItemSellingPlanNameSelector) {
        this.cartLineItemSellingPlanNameSelector = cartLineItemSellingPlanNameSelector;
    }

    public String getCartSubTotalSelector() {
        return cartSubTotalSelector;
    }

    public ThemeCode cartSubTotalSelector(String cartSubTotalSelector) {
        this.cartSubTotalSelector = cartSubTotalSelector;
        return this;
    }

    public void setCartSubTotalSelector(String cartSubTotalSelector) {
        this.cartSubTotalSelector = cartSubTotalSelector;
    }

    public String getCartLineItemPriceSelector() {
        return cartLineItemPriceSelector;
    }

    public ThemeCode cartLineItemPriceSelector(String cartLineItemPriceSelector) {
        this.cartLineItemPriceSelector = cartLineItemPriceSelector;
        return this;
    }

    public void setCartLineItemPriceSelector(String cartLineItemPriceSelector) {
        this.cartLineItemPriceSelector = cartLineItemPriceSelector;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeCode)) {
            return false;
        }
        return id != null && id.equals(((ThemeCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ThemeCode{" +
            "id=" + getId() +
            ", themeName='" + getThemeName() + "'" +
            ", themeNameFriendly='" + getThemeNameFriendly() + "'" +
            ", themeStoreId=" + getThemeStoreId() +
            ", addToCartSelector='" + getAddToCartSelector() + "'" +
            ", subscriptionLinkSelector='" + getSubscriptionLinkSelector() + "'" +
            ", addToCartPlacement='" + getAddToCartPlacement() + "'" +
            ", subscriptionLinkPlacement='" + getSubscriptionLinkPlacement() + "'" +
            ", priceSelector='" + getPriceSelector() + "'" +
            ", pricePlacement='" + getPricePlacement() + "'" +
            ", badgeTop='" + getBadgeTop() + "'" +
            ", cartRowSelector='" + getCartRowSelector() + "'" +
            ", cartLineItemSelector='" + getCartLineItemSelector() + "'" +
            ", cartLineItemPerQuantityPriceSelector='" + getCartLineItemPerQuantityPriceSelector() + "'" +
            ", cartLineItemTotalPriceSelector='" + getCartLineItemTotalPriceSelector() + "'" +
            ", cartLineItemSellingPlanNameSelector='" + getCartLineItemSellingPlanNameSelector() + "'" +
            ", cartSubTotalSelector='" + getCartSubTotalSelector() + "'" +
            ", cartLineItemPriceSelector='" + getCartLineItemPriceSelector() + "'" +
            "}";
    }
}
