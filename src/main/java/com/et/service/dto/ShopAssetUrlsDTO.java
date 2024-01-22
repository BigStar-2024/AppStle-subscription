package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.et.domain.ShopAssetUrls} entity.
 */
public class ShopAssetUrlsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private String vendorJavascript;

    private String vendorCss;

    private String customerJavascript;

    private String customerCss;

    private String bundleJavascript;

    private String bundleCss;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getVendorJavascript() {
        return vendorJavascript;
    }

    public void setVendorJavascript(String vendorJavascript) {
        this.vendorJavascript = vendorJavascript;
    }

    public String getVendorCss() {
        return vendorCss;
    }

    public void setVendorCss(String vendorCss) {
        this.vendorCss = vendorCss;
    }

    public String getCustomerJavascript() {
        return customerJavascript;
    }

    public void setCustomerJavascript(String customerJavascript) {
        this.customerJavascript = customerJavascript;
    }

    public String getCustomerCss() {
        return customerCss;
    }

    public void setCustomerCss(String customerCss) {
        this.customerCss = customerCss;
    }

    public String getBundleJavascript() {
        return bundleJavascript;
    }

    public void setBundleJavascript(String bundleJavascript) {
        this.bundleJavascript = bundleJavascript;
    }

    public String getBundleCss() {
        return bundleCss;
    }

    public void setBundleCss(String bundleCss) {
        this.bundleCss = bundleCss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopAssetUrlsDTO shopAssetUrlsDTO = (ShopAssetUrlsDTO) o;
        if (shopAssetUrlsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopAssetUrlsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopAssetUrlsDTO{" +
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
