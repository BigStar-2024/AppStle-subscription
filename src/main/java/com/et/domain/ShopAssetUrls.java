package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ShopAssetUrls.
 */
@Entity
@Table(name = "shop_asset_urls")
public class ShopAssetUrls implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "vendor_javascript")
    private String vendorJavascript;

    @Column(name = "vendor_css")
    private String vendorCss;

    @Column(name = "customer_javascript")
    private String customerJavascript;

    @Column(name = "customer_css")
    private String customerCss;

    @Column(name = "bundle_javascript")
    private String bundleJavascript;

    @Column(name = "bundle_css")
    private String bundleCss;

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

    public ShopAssetUrls shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getVendorJavascript() {
        return vendorJavascript;
    }

    public ShopAssetUrls vendorJavascript(String vendorJavascript) {
        this.vendorJavascript = vendorJavascript;
        return this;
    }

    public void setVendorJavascript(String vendorJavascript) {
        this.vendorJavascript = vendorJavascript;
    }

    public String getVendorCss() {
        return vendorCss;
    }

    public ShopAssetUrls vendorCss(String vendorCss) {
        this.vendorCss = vendorCss;
        return this;
    }

    public void setVendorCss(String vendorCss) {
        this.vendorCss = vendorCss;
    }

    public String getCustomerJavascript() {
        return customerJavascript;
    }

    public ShopAssetUrls customerJavascript(String customerJavascript) {
        this.customerJavascript = customerJavascript;
        return this;
    }

    public void setCustomerJavascript(String customerJavascript) {
        this.customerJavascript = customerJavascript;
    }

    public String getCustomerCss() {
        return customerCss;
    }

    public ShopAssetUrls customerCss(String customerCss) {
        this.customerCss = customerCss;
        return this;
    }

    public void setCustomerCss(String customerCss) {
        this.customerCss = customerCss;
    }

    public String getBundleJavascript() {
        return bundleJavascript;
    }

    public ShopAssetUrls bundleJavascript(String bundleJavascript) {
        this.bundleJavascript = bundleJavascript;
        return this;
    }

    public void setBundleJavascript(String bundleJavascript) {
        this.bundleJavascript = bundleJavascript;
    }

    public String getBundleCss() {
        return bundleCss;
    }

    public ShopAssetUrls bundleCss(String bundleCss) {
        this.bundleCss = bundleCss;
        return this;
    }

    public void setBundleCss(String bundleCss) {
        this.bundleCss = bundleCss;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopAssetUrls)) {
            return false;
        }
        return id != null && id.equals(((ShopAssetUrls) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShopAssetUrls{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", vendorJavascript='" + getVendorJavascript() + "'" +
            ", vendorCss='" + getVendorCss() + "'" +
            ", customerJavascript='" + getCustomerJavascript() + "'" +
            ", customerCss='" + getCustomerCss() + "'" +
            ", bundleJavascript='" + getBundleJavascript() + "'" +
            ", bundleCss='" + getBundleCss() + "'" +
            "}";
    }
}
