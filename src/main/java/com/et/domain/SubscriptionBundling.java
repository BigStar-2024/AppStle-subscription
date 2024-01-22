package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.BuildABoxRedirect;

import com.et.domain.enumeration.ProductViewSettings;

import com.et.domain.enumeration.BuildABoxType;

/**
 * A SubscriptionBundling.
 */
@Entity
@Table(name = "subscription_bundling")
public class SubscriptionBundling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "subscription_bundling_enabled")
    private Boolean subscriptionBundlingEnabled;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "min_product_count")
    private Integer minProductCount;

    @Column(name = "max_product_count")
    private Integer maxProductCount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "unique_ref")
    private String uniqueRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "bundle_redirect")
    private BuildABoxRedirect bundleRedirect;

    @Column(name = "custom_redirect_url")
    private String customRedirectURL;

    @Column(name = "min_order_amount")
    private Double minOrderAmount;

    @Lob
    @Column(name = "tiered_discount")
    private String tieredDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_view_style")
    private ProductViewSettings productViewStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "build_a_box_type")
    private BuildABoxType buildABoxType;

    @Lob
    @Column(name = "single_product_settings")
    private String singleProductSettings;

    @Lob
    @Column(name = "subscription_group")
    private String subscriptionGroup;

    @Lob
    @Column(name = "bundle_top_html")
    private String bundleTopHtml;

    @Lob
    @Column(name = "bundle_bottom_html")
    private String bundleBottomHtml;

    @Column(name = "proceed_to_checkout_button_text")
    private String proceedToCheckoutButtonText;

    @Column(name = "choose_products_text")
    private String chooseProductsText;

    @Column(name = "name")
    private String name;

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

    public SubscriptionBundling shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isSubscriptionBundlingEnabled() {
        return subscriptionBundlingEnabled;
    }

    public SubscriptionBundling subscriptionBundlingEnabled(Boolean subscriptionBundlingEnabled) {
        this.subscriptionBundlingEnabled = subscriptionBundlingEnabled;
        return this;
    }

    public void setSubscriptionBundlingEnabled(Boolean subscriptionBundlingEnabled) {
        this.subscriptionBundlingEnabled = subscriptionBundlingEnabled;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public SubscriptionBundling subscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getMinProductCount() {
        return minProductCount;
    }

    public SubscriptionBundling minProductCount(Integer minProductCount) {
        this.minProductCount = minProductCount;
        return this;
    }

    public void setMinProductCount(Integer minProductCount) {
        this.minProductCount = minProductCount;
    }

    public Integer getMaxProductCount() {
        return maxProductCount;
    }

    public SubscriptionBundling maxProductCount(Integer maxProductCount) {
        this.maxProductCount = maxProductCount;
        return this;
    }

    public void setMaxProductCount(Integer maxProductCount) {
        this.maxProductCount = maxProductCount;
    }

    public Double getDiscount() {
        return discount;
    }

    public SubscriptionBundling discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getUniqueRef() {
        return uniqueRef;
    }

    public SubscriptionBundling uniqueRef(String uniqueRef) {
        this.uniqueRef = uniqueRef;
        return this;
    }

    public void setUniqueRef(String uniqueRef) {
        this.uniqueRef = uniqueRef;
    }

    public BuildABoxRedirect getBundleRedirect() {
        return bundleRedirect;
    }

    public SubscriptionBundling bundleRedirect(BuildABoxRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
        return this;
    }

    public void setBundleRedirect(BuildABoxRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
    }

    public String getCustomRedirectURL() {
        return customRedirectURL;
    }

    public SubscriptionBundling customRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
        return this;
    }

    public void setCustomRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public SubscriptionBundling minOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
        return this;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getTieredDiscount() {
        return tieredDiscount;
    }

    public SubscriptionBundling tieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
        return this;
    }

    public void setTieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
    }

    public ProductViewSettings getProductViewStyle() {
        return productViewStyle;
    }

    public SubscriptionBundling productViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
        return this;
    }

    public void setProductViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    public BuildABoxType getBuildABoxType() {
        return buildABoxType;
    }

    public SubscriptionBundling buildABoxType(BuildABoxType buildABoxType) {
        this.buildABoxType = buildABoxType;
        return this;
    }

    public void setBuildABoxType(BuildABoxType buildABoxType) {
        this.buildABoxType = buildABoxType;
    }

    public String getSingleProductSettings() {
        return singleProductSettings;
    }

    public SubscriptionBundling singleProductSettings(String singleProductSettings) {
        this.singleProductSettings = singleProductSettings;
        return this;
    }

    public void setSingleProductSettings(String singleProductSettings) {
        this.singleProductSettings = singleProductSettings;
    }

    public String getSubscriptionGroup() {
        return subscriptionGroup;
    }

    public void setSubscriptionGroup(String subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
    }

    public String getBundleTopHtml() {
        return bundleTopHtml;
    }

    public SubscriptionBundling bundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
        return this;
    }

    public void setBundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
    }

    public String getBundleBottomHtml() {
        return bundleBottomHtml;
    }

    public SubscriptionBundling bundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
        return this;
    }

    public void setBundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
    }

    public String getProceedToCheckoutButtonText() {
        return proceedToCheckoutButtonText;
    }

    public SubscriptionBundling proceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
        return this;
    }

    public void setProceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
    }

    public String getChooseProductsText() {
        return chooseProductsText;
    }

    public SubscriptionBundling chooseProductsText(String chooseProductsText) {
        this.chooseProductsText = chooseProductsText;
        return this;
    }

    public void setChooseProductsText(String chooseProductsText) {
        this.chooseProductsText = chooseProductsText;
    }

    public String getName() {
        return name;
    }

    public SubscriptionBundling name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBundling)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionBundling) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionBundling{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", subscriptionBundlingEnabled='" + isSubscriptionBundlingEnabled() + "'" +
            ", subscriptionId=" + getSubscriptionId() +
            ", minProductCount=" + getMinProductCount() +
            ", maxProductCount=" + getMaxProductCount() +
            ", discount=" + getDiscount() +
            ", uniqueRef='" + getUniqueRef() + "'" +
            ", bundleRedirect='" + getBundleRedirect() + "'" +
            ", customRedirectURL='" + getCustomRedirectURL() + "'" +
            ", minOrderAmount=" + getMinOrderAmount() +
            ", tieredDiscount='" + getTieredDiscount() + "'" +
            ", productViewStyle='" + getProductViewStyle() + "'" +
            ", buildABoxType='" + getBuildABoxType() + "'" +
            ", singleProductSettings='" + getSingleProductSettings() + "'" +
            ", subscriptionGroup='" + getSubscriptionGroup() + "'" +
            ", bundleTopHtml='" + getBundleTopHtml() + "'" +
            ", bundleBottomHtml='" + getBundleBottomHtml() + "'" +
            ", proceedToCheckoutButtonText='" + getProceedToCheckoutButtonText() + "'" +
            ", chooseProductsText='" + getChooseProductsText() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
