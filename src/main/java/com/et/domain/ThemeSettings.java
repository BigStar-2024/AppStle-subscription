package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.ShopifyThemeInstallationVersion;

import com.et.domain.enumeration.PlacementPosition;

import com.et.domain.enumeration.WidgetTemplateType;

/**
 * A ThemeSettings.
 */
@Entity
@Table(name = "theme_settings")
public class ThemeSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "skip_setting_theme", nullable = false)
    private Boolean skip_setting_theme;

    @Column(name = "theme_v_2_saved")
    private Boolean themeV2Saved;

    @Column(name = "theme_name")
    private String themeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "shopify_theme_installation_version")
    private ShopifyThemeInstallationVersion shopifyThemeInstallationVersion;

    @Lob
    @Column(name = "selected_selector")
    private String selectedSelector;

    @Lob
    @Column(name = "subscription_link_selector")
    private String subscriptionLinkSelector;

    @Lob
    @Column(name = "custom_javascript")
    private String customJavascript;

    @Enumerated(EnumType.STRING)
    @Column(name = "placement")
    private PlacementPosition placement;

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

    @Column(name = "disable_loading_jquery")
    private Boolean disableLoadingJquery;

    @Lob
    @Column(name = "quick_view_click_selector")
    private String quickViewClickSelector;

    @Lob
    @Column(name = "landing_page_price_selector")
    private String landingPagePriceSelector;

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

    @Column(name = "enable_cart_widget_feature")
    private Boolean enableCartWidgetFeature;

    @Column(name = "enable_slow_script_load")
    private Boolean enableSlowScriptLoad;

    @Column(name = "script_load_delay")
    private Integer scriptLoadDelay;

    @Column(name = "format_money_override")
    private Boolean formatMoneyOverride;

    @Enumerated(EnumType.STRING)
    @Column(name = "widget_template_type")
    private WidgetTemplateType widgetTemplateType;

    @Lob
    @Column(name = "widget_template_html")
    private String widgetTemplateHtml;

    @Column(name = "cart_hidden_attributes_selector")
    private String cartHiddenAttributesSelector;

    @Column(name = "script_attributes")
    private String scriptAttributes;

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

    public ThemeSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isSkip_setting_theme() {
        return skip_setting_theme;
    }

    public ThemeSettings skip_setting_theme(Boolean skip_setting_theme) {
        this.skip_setting_theme = skip_setting_theme;
        return this;
    }

    public void setSkip_setting_theme(Boolean skip_setting_theme) {
        this.skip_setting_theme = skip_setting_theme;
    }

    public Boolean isThemeV2Saved() {
        return themeV2Saved;
    }

    public ThemeSettings themeV2Saved(Boolean themeV2Saved) {
        this.themeV2Saved = themeV2Saved;
        return this;
    }

    public void setThemeV2Saved(Boolean themeV2Saved) {
        this.themeV2Saved = themeV2Saved;
    }

    public String getThemeName() {
        return themeName;
    }

    public ThemeSettings themeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public ShopifyThemeInstallationVersion getShopifyThemeInstallationVersion() {
        return shopifyThemeInstallationVersion;
    }

    public ThemeSettings shopifyThemeInstallationVersion(ShopifyThemeInstallationVersion shopifyThemeInstallationVersion) {
        this.shopifyThemeInstallationVersion = shopifyThemeInstallationVersion;
        return this;
    }

    public void setShopifyThemeInstallationVersion(ShopifyThemeInstallationVersion shopifyThemeInstallationVersion) {
        this.shopifyThemeInstallationVersion = shopifyThemeInstallationVersion;
    }

    public String getSelectedSelector() {
        return selectedSelector;
    }

    public ThemeSettings selectedSelector(String selectedSelector) {
        this.selectedSelector = selectedSelector;
        return this;
    }

    public void setSelectedSelector(String selectedSelector) {
        this.selectedSelector = selectedSelector;
    }

    public String getSubscriptionLinkSelector() {
        return subscriptionLinkSelector;
    }

    public ThemeSettings subscriptionLinkSelector(String subscriptionLinkSelector) {
        this.subscriptionLinkSelector = subscriptionLinkSelector;
        return this;
    }

    public void setSubscriptionLinkSelector(String subscriptionLinkSelector) {
        this.subscriptionLinkSelector = subscriptionLinkSelector;
    }

    public String getCustomJavascript() {
        return customJavascript;
    }

    public ThemeSettings customJavascript(String customJavascript) {
        this.customJavascript = customJavascript;
        return this;
    }

    public void setCustomJavascript(String customJavascript) {
        this.customJavascript = customJavascript;
    }

    public PlacementPosition getPlacement() {
        return placement;
    }

    public ThemeSettings placement(PlacementPosition placement) {
        this.placement = placement;
        return this;
    }

    public void setPlacement(PlacementPosition placement) {
        this.placement = placement;
    }

    public PlacementPosition getSubscriptionLinkPlacement() {
        return subscriptionLinkPlacement;
    }

    public ThemeSettings subscriptionLinkPlacement(PlacementPosition subscriptionLinkPlacement) {
        this.subscriptionLinkPlacement = subscriptionLinkPlacement;
        return this;
    }

    public void setSubscriptionLinkPlacement(PlacementPosition subscriptionLinkPlacement) {
        this.subscriptionLinkPlacement = subscriptionLinkPlacement;
    }

    public String getPriceSelector() {
        return priceSelector;
    }

    public ThemeSettings priceSelector(String priceSelector) {
        this.priceSelector = priceSelector;
        return this;
    }

    public void setPriceSelector(String priceSelector) {
        this.priceSelector = priceSelector;
    }

    public PlacementPosition getPricePlacement() {
        return pricePlacement;
    }

    public ThemeSettings pricePlacement(PlacementPosition pricePlacement) {
        this.pricePlacement = pricePlacement;
        return this;
    }

    public void setPricePlacement(PlacementPosition pricePlacement) {
        this.pricePlacement = pricePlacement;
    }

    public String getBadgeTop() {
        return badgeTop;
    }

    public ThemeSettings badgeTop(String badgeTop) {
        this.badgeTop = badgeTop;
        return this;
    }

    public void setBadgeTop(String badgeTop) {
        this.badgeTop = badgeTop;
    }

    public Boolean isDisableLoadingJquery() {
        return disableLoadingJquery;
    }

    public ThemeSettings disableLoadingJquery(Boolean disableLoadingJquery) {
        this.disableLoadingJquery = disableLoadingJquery;
        return this;
    }

    public void setDisableLoadingJquery(Boolean disableLoadingJquery) {
        this.disableLoadingJquery = disableLoadingJquery;
    }

    public String getQuickViewClickSelector() {
        return quickViewClickSelector;
    }

    public ThemeSettings quickViewClickSelector(String quickViewClickSelector) {
        this.quickViewClickSelector = quickViewClickSelector;
        return this;
    }

    public void setQuickViewClickSelector(String quickViewClickSelector) {
        this.quickViewClickSelector = quickViewClickSelector;
    }

    public String getLandingPagePriceSelector() {
        return landingPagePriceSelector;
    }

    public ThemeSettings landingPagePriceSelector(String landingPagePriceSelector) {
        this.landingPagePriceSelector = landingPagePriceSelector;
        return this;
    }

    public void setLandingPagePriceSelector(String landingPagePriceSelector) {
        this.landingPagePriceSelector = landingPagePriceSelector;
    }

    public String getCartRowSelector() {
        return cartRowSelector;
    }

    public ThemeSettings cartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
        return this;
    }

    public void setCartRowSelector(String cartRowSelector) {
        this.cartRowSelector = cartRowSelector;
    }

    public String getCartLineItemSelector() {
        return cartLineItemSelector;
    }

    public ThemeSettings cartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
        return this;
    }

    public void setCartLineItemSelector(String cartLineItemSelector) {
        this.cartLineItemSelector = cartLineItemSelector;
    }

    public String getCartLineItemPerQuantityPriceSelector() {
        return cartLineItemPerQuantityPriceSelector;
    }

    public ThemeSettings cartLineItemPerQuantityPriceSelector(String cartLineItemPerQuantityPriceSelector) {
        this.cartLineItemPerQuantityPriceSelector = cartLineItemPerQuantityPriceSelector;
        return this;
    }

    public void setCartLineItemPerQuantityPriceSelector(String cartLineItemPerQuantityPriceSelector) {
        this.cartLineItemPerQuantityPriceSelector = cartLineItemPerQuantityPriceSelector;
    }

    public String getCartLineItemTotalPriceSelector() {
        return cartLineItemTotalPriceSelector;
    }

    public ThemeSettings cartLineItemTotalPriceSelector(String cartLineItemTotalPriceSelector) {
        this.cartLineItemTotalPriceSelector = cartLineItemTotalPriceSelector;
        return this;
    }

    public void setCartLineItemTotalPriceSelector(String cartLineItemTotalPriceSelector) {
        this.cartLineItemTotalPriceSelector = cartLineItemTotalPriceSelector;
    }

    public String getCartLineItemSellingPlanNameSelector() {
        return cartLineItemSellingPlanNameSelector;
    }

    public ThemeSettings cartLineItemSellingPlanNameSelector(String cartLineItemSellingPlanNameSelector) {
        this.cartLineItemSellingPlanNameSelector = cartLineItemSellingPlanNameSelector;
        return this;
    }

    public void setCartLineItemSellingPlanNameSelector(String cartLineItemSellingPlanNameSelector) {
        this.cartLineItemSellingPlanNameSelector = cartLineItemSellingPlanNameSelector;
    }

    public String getCartSubTotalSelector() {
        return cartSubTotalSelector;
    }

    public ThemeSettings cartSubTotalSelector(String cartSubTotalSelector) {
        this.cartSubTotalSelector = cartSubTotalSelector;
        return this;
    }

    public void setCartSubTotalSelector(String cartSubTotalSelector) {
        this.cartSubTotalSelector = cartSubTotalSelector;
    }

    public String getCartLineItemPriceSelector() {
        return cartLineItemPriceSelector;
    }

    public ThemeSettings cartLineItemPriceSelector(String cartLineItemPriceSelector) {
        this.cartLineItemPriceSelector = cartLineItemPriceSelector;
        return this;
    }

    public void setCartLineItemPriceSelector(String cartLineItemPriceSelector) {
        this.cartLineItemPriceSelector = cartLineItemPriceSelector;
    }

    public Boolean isEnableCartWidgetFeature() {
        return enableCartWidgetFeature;
    }

    public ThemeSettings enableCartWidgetFeature(Boolean enableCartWidgetFeature) {
        this.enableCartWidgetFeature = enableCartWidgetFeature;
        return this;
    }

    public void setEnableCartWidgetFeature(Boolean enableCartWidgetFeature) {
        this.enableCartWidgetFeature = enableCartWidgetFeature;
    }

    public Boolean isEnableSlowScriptLoad() {
        return enableSlowScriptLoad;
    }

    public ThemeSettings enableSlowScriptLoad(Boolean enableSlowScriptLoad) {
        this.enableSlowScriptLoad = enableSlowScriptLoad;
        return this;
    }

    public void setEnableSlowScriptLoad(Boolean enableSlowScriptLoad) {
        this.enableSlowScriptLoad = enableSlowScriptLoad;
    }

    public Integer getScriptLoadDelay() {
        return scriptLoadDelay;
    }

    public ThemeSettings scriptLoadDelay(Integer scriptLoadDelay) {
        this.scriptLoadDelay = scriptLoadDelay;
        return this;
    }

    public void setScriptLoadDelay(Integer scriptLoadDelay) {
        this.scriptLoadDelay = scriptLoadDelay;
    }

    public Boolean isFormatMoneyOverride() {
        return formatMoneyOverride;
    }

    public ThemeSettings formatMoneyOverride(Boolean formatMoneyOverride) {
        this.formatMoneyOverride = formatMoneyOverride;
        return this;
    }

    public void setFormatMoneyOverride(Boolean formatMoneyOverride) {
        this.formatMoneyOverride = formatMoneyOverride;
    }

    public WidgetTemplateType getWidgetTemplateType() {
        return widgetTemplateType;
    }

    public ThemeSettings widgetTemplateType(WidgetTemplateType widgetTemplateType) {
        this.widgetTemplateType = widgetTemplateType;
        return this;
    }

    public void setWidgetTemplateType(WidgetTemplateType widgetTemplateType) {
        this.widgetTemplateType = widgetTemplateType;
    }

    public String getWidgetTemplateHtml() {
        return widgetTemplateHtml;
    }

    public ThemeSettings widgetTemplateHtml(String widgetTemplateHtml) {
        this.widgetTemplateHtml = widgetTemplateHtml;
        return this;
    }

    public void setWidgetTemplateHtml(String widgetTemplateHtml) {
        this.widgetTemplateHtml = widgetTemplateHtml;
    }

    public String getCartHiddenAttributesSelector() {
        return cartHiddenAttributesSelector;
    }

    public ThemeSettings cartHiddenAttributesSelector(String cartHiddenAttributesSelector) {
        this.cartHiddenAttributesSelector = cartHiddenAttributesSelector;
        return this;
    }

    public void setCartHiddenAttributesSelector(String cartHiddenAttributesSelector) {
        this.cartHiddenAttributesSelector = cartHiddenAttributesSelector;
    }

    public String getScriptAttributes() {
        return scriptAttributes;
    }

    public ThemeSettings scriptAttributes(String scriptAttributes) {
        this.scriptAttributes = scriptAttributes;
        return this;
    }

    public void setScriptAttributes(String scriptAttributes) {
        this.scriptAttributes = scriptAttributes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeSettings)) {
            return false;
        }
        return id != null && id.equals(((ThemeSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", skip_setting_theme='" + isSkip_setting_theme() + "'" +
            ", themeV2Saved='" + isThemeV2Saved() + "'" +
            ", themeName='" + getThemeName() + "'" +
            ", shopifyThemeInstallationVersion='" + getShopifyThemeInstallationVersion() + "'" +
            ", selectedSelector='" + getSelectedSelector() + "'" +
            ", subscriptionLinkSelector='" + getSubscriptionLinkSelector() + "'" +
            ", customJavascript='" + getCustomJavascript() + "'" +
            ", placement='" + getPlacement() + "'" +
            ", subscriptionLinkPlacement='" + getSubscriptionLinkPlacement() + "'" +
            ", priceSelector='" + getPriceSelector() + "'" +
            ", pricePlacement='" + getPricePlacement() + "'" +
            ", badgeTop='" + getBadgeTop() + "'" +
            ", disableLoadingJquery='" + isDisableLoadingJquery() + "'" +
            ", quickViewClickSelector='" + getQuickViewClickSelector() + "'" +
            ", landingPagePriceSelector='" + getLandingPagePriceSelector() + "'" +
            ", cartRowSelector='" + getCartRowSelector() + "'" +
            ", cartLineItemSelector='" + getCartLineItemSelector() + "'" +
            ", cartLineItemPerQuantityPriceSelector='" + getCartLineItemPerQuantityPriceSelector() + "'" +
            ", cartLineItemTotalPriceSelector='" + getCartLineItemTotalPriceSelector() + "'" +
            ", cartLineItemSellingPlanNameSelector='" + getCartLineItemSellingPlanNameSelector() + "'" +
            ", cartSubTotalSelector='" + getCartSubTotalSelector() + "'" +
            ", cartLineItemPriceSelector='" + getCartLineItemPriceSelector() + "'" +
            ", enableCartWidgetFeature='" + isEnableCartWidgetFeature() + "'" +
            ", enableSlowScriptLoad='" + isEnableSlowScriptLoad() + "'" +
            ", scriptLoadDelay=" + getScriptLoadDelay() +
            ", formatMoneyOverride='" + isFormatMoneyOverride() + "'" +
            ", widgetTemplateType='" + getWidgetTemplateType() + "'" +
            ", widgetTemplateHtml='" + getWidgetTemplateHtml() + "'" +
            ", cartHiddenAttributesSelector='" + getCartHiddenAttributesSelector() + "'" +
            ", scriptAttributes='" + getScriptAttributes() + "'" +
            "}";
    }
}
