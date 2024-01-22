package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Lob;
import com.et.domain.enumeration.RedirectOption;
import com.et.domain.enumeration.PlacementPosition;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the {@link com.et.domain.BundleSetting} entity.
 */
public class BundleSettingDTO implements Serializable {

    private Long id;

    private String shop;

    private Boolean showOnProductPage;

    private Boolean showMultipleOnProductPage;

    private String actionButtonColor;

    private String actionButtonFontColor;

    private String productTitleColor;

    private String productPriceColor;

    private RedirectOption redirectTo;

    private Boolean showProductPrice;

    private Boolean oneTimeDiscount;

    private Boolean showDiscountInCart;

    private String selector;

    private PlacementPosition placement;

    @Lob
    private String customCss;

    private String variant;

    private String deliveryFrequency;

    private String perDelivery;

    private String discountPopupHeader;

    private String discountPopupAmount;

    private String discountPopupCheckoutMessage;

    private String discountPopupBuy;

    private String discountPopupNo;

    private Boolean showDiscountPopup;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private List<UpdateShopCustomizationRequest> shopCustomizationData;

    public List<UpdateShopCustomizationRequest> getShopCustomizationData() {
        return shopCustomizationData;
    }

    public void setShopCustomizationData(List<UpdateShopCustomizationRequest> shopCustomizationData) {
        this.shopCustomizationData = shopCustomizationData;
    }


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

    public Boolean isShowOnProductPage() {
        return showOnProductPage;
    }

    public void setShowOnProductPage(Boolean showOnProductPage) {
        this.showOnProductPage = showOnProductPage;
    }

    public Boolean isShowMultipleOnProductPage() {
        return showMultipleOnProductPage;
    }

    public void setShowMultipleOnProductPage(Boolean showMultipleOnProductPage) {
        this.showMultipleOnProductPage = showMultipleOnProductPage;
    }

    public String getActionButtonColor() {
        return actionButtonColor;
    }

    public void setActionButtonColor(String actionButtonColor) {
        this.actionButtonColor = actionButtonColor;
    }

    public String getActionButtonFontColor() {
        return actionButtonFontColor;
    }

    public void setActionButtonFontColor(String actionButtonFontColor) {
        this.actionButtonFontColor = actionButtonFontColor;
    }

    public String getProductTitleColor() {
        return productTitleColor;
    }

    public void setProductTitleColor(String productTitleColor) {
        this.productTitleColor = productTitleColor;
    }

    public String getProductPriceColor() {
        return productPriceColor;
    }

    public void setProductPriceColor(String productPriceColor) {
        this.productPriceColor = productPriceColor;
    }

    public RedirectOption getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(RedirectOption redirectTo) {
        this.redirectTo = redirectTo;
    }

    public Boolean isShowProductPrice() {
        return showProductPrice;
    }

    public void setShowProductPrice(Boolean showProductPrice) {
        this.showProductPrice = showProductPrice;
    }

    public Boolean isOneTimeDiscount() {
        return oneTimeDiscount;
    }

    public void setOneTimeDiscount(Boolean oneTimeDiscount) {
        this.oneTimeDiscount = oneTimeDiscount;
    }

    public Boolean isShowDiscountInCart() {
        return showDiscountInCart;
    }

    public void setShowDiscountInCart(Boolean showDiscountInCart) {
        this.showDiscountInCart = showDiscountInCart;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public PlacementPosition getPlacement() {
        return placement;
    }

    public void setPlacement(PlacementPosition placement) {
        this.placement = placement;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public void setDeliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getPerDelivery() {
        return perDelivery;
    }

    public void setPerDelivery(String perDelivery) {
        this.perDelivery = perDelivery;
    }

    public String getDiscountPopupHeader() {
        return discountPopupHeader;
    }

    public void setDiscountPopupHeader(String discountPopupHeader) {
        this.discountPopupHeader = discountPopupHeader;
    }

    public String getDiscountPopupAmount() {
        return discountPopupAmount;
    }

    public void setDiscountPopupAmount(String discountPopupAmount) {
        this.discountPopupAmount = discountPopupAmount;
    }

    public String getDiscountPopupCheckoutMessage() {
        return discountPopupCheckoutMessage;
    }

    public void setDiscountPopupCheckoutMessage(String discountPopupCheckoutMessage) {
        this.discountPopupCheckoutMessage = discountPopupCheckoutMessage;
    }

    public String getDiscountPopupBuy() {
        return discountPopupBuy;
    }

    public void setDiscountPopupBuy(String discountPopupBuy) {
        this.discountPopupBuy = discountPopupBuy;
    }

    public String getDiscountPopupNo() {
        return discountPopupNo;
    }

    public void setDiscountPopupNo(String discountPopupNo) {
        this.discountPopupNo = discountPopupNo;
    }

    public Boolean isShowDiscountPopup() {
        return showDiscountPopup;
    }

    public void setShowDiscountPopup(Boolean showDiscountPopup) {
        this.showDiscountPopup = showDiscountPopup;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleSettingDTO)) {
            return false;
        }

        return id != null && id.equals(((BundleSettingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BundleSettingDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", showOnProductPage='" + isShowOnProductPage() + "'" +
            ", showMultipleOnProductPage='" + isShowMultipleOnProductPage() + "'" +
            ", actionButtonColor='" + getActionButtonColor() + "'" +
            ", actionButtonFontColor='" + getActionButtonFontColor() + "'" +
            ", productTitleColor='" + getProductTitleColor() + "'" +
            ", productPriceColor='" + getProductPriceColor() + "'" +
            ", redirectTo='" + getRedirectTo() + "'" +
            ", showProductPrice='" + isShowProductPrice() + "'" +
            ", oneTimeDiscount='" + isOneTimeDiscount() + "'" +
            ", showDiscountInCart='" + isShowDiscountInCart() + "'" +
            ", selector='" + getSelector() + "'" +
            ", placement='" + getPlacement() + "'" +
            ", customCss='" + getCustomCss() + "'" +
            ", variant='" + getVariant() + "'" +
            ", deliveryFrequency='" + getDeliveryFrequency() + "'" +
            ", perDelivery='" + getPerDelivery() + "'" +
            ", discountPopupHeader='" + getDiscountPopupHeader() + "'" +
            ", discountPopupAmount='" + getDiscountPopupAmount() + "'" +
            ", discountPopupCheckoutMessage='" + getDiscountPopupCheckoutMessage() + "'" +
            ", discountPopupBuy='" + getDiscountPopupBuy() + "'" +
            ", discountPopupNo='" + getDiscountPopupNo() + "'" +
            ", showDiscountPopup='" + isShowDiscountPopup() + "'" +
            "}";
    }
}
