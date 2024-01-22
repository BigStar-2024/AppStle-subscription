package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.RedirectOption;

import com.et.domain.enumeration.PlacementPosition;

/**
 * A BundleSetting.
 */
@Entity
@Table(name = "bundle_setting")
public class BundleSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "show_on_product_page")
    private Boolean showOnProductPage;

    @Column(name = "show_multiple_on_product_page")
    private Boolean showMultipleOnProductPage;

    @Column(name = "action_button_color")
    private String actionButtonColor;

    @Column(name = "action_button_font_color")
    private String actionButtonFontColor;

    @Column(name = "product_title_color")
    private String productTitleColor;

    @Column(name = "product_price_color")
    private String productPriceColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "redirect_to")
    private RedirectOption redirectTo;

    @Column(name = "show_product_price")
    private Boolean showProductPrice;

    @Column(name = "one_time_discount")
    private Boolean oneTimeDiscount;

    @Column(name = "show_discount_in_cart")
    private Boolean showDiscountInCart;

    @Column(name = "selector")
    private String selector;

    @Enumerated(EnumType.STRING)
    @Column(name = "placement")
    private PlacementPosition placement;

    @Lob
    @Column(name = "custom_css")
    private String customCss;

    @Column(name = "variant")
    private String variant;

    @Column(name = "delivery_frequency")
    private String deliveryFrequency;

    @Column(name = "per_delivery")
    private String perDelivery;

    @Column(name = "discount_popup_header")
    private String discountPopupHeader;

    @Column(name = "discount_popup_amount")
    private String discountPopupAmount;

    @Column(name = "discount_popup_checkout_message")
    private String discountPopupCheckoutMessage;

    @Column(name = "discount_popup_buy")
    private String discountPopupBuy;

    @Column(name = "discount_popup_no")
    private String discountPopupNo;

    @Column(name = "show_discount_popup")
    private Boolean showDiscountPopup;

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

    public BundleSetting shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isShowOnProductPage() {
        return showOnProductPage;
    }

    public BundleSetting showOnProductPage(Boolean showOnProductPage) {
        this.showOnProductPage = showOnProductPage;
        return this;
    }

    public void setShowOnProductPage(Boolean showOnProductPage) {
        this.showOnProductPage = showOnProductPage;
    }

    public Boolean isShowMultipleOnProductPage() {
        return showMultipleOnProductPage;
    }

    public BundleSetting showMultipleOnProductPage(Boolean showMultipleOnProductPage) {
        this.showMultipleOnProductPage = showMultipleOnProductPage;
        return this;
    }

    public void setShowMultipleOnProductPage(Boolean showMultipleOnProductPage) {
        this.showMultipleOnProductPage = showMultipleOnProductPage;
    }

    public String getActionButtonColor() {
        return actionButtonColor;
    }

    public BundleSetting actionButtonColor(String actionButtonColor) {
        this.actionButtonColor = actionButtonColor;
        return this;
    }

    public void setActionButtonColor(String actionButtonColor) {
        this.actionButtonColor = actionButtonColor;
    }

    public String getActionButtonFontColor() {
        return actionButtonFontColor;
    }

    public BundleSetting actionButtonFontColor(String actionButtonFontColor) {
        this.actionButtonFontColor = actionButtonFontColor;
        return this;
    }

    public void setActionButtonFontColor(String actionButtonFontColor) {
        this.actionButtonFontColor = actionButtonFontColor;
    }

    public String getProductTitleColor() {
        return productTitleColor;
    }

    public BundleSetting productTitleColor(String productTitleColor) {
        this.productTitleColor = productTitleColor;
        return this;
    }

    public void setProductTitleColor(String productTitleColor) {
        this.productTitleColor = productTitleColor;
    }

    public String getProductPriceColor() {
        return productPriceColor;
    }

    public BundleSetting productPriceColor(String productPriceColor) {
        this.productPriceColor = productPriceColor;
        return this;
    }

    public void setProductPriceColor(String productPriceColor) {
        this.productPriceColor = productPriceColor;
    }

    public RedirectOption getRedirectTo() {
        return redirectTo;
    }

    public BundleSetting redirectTo(RedirectOption redirectTo) {
        this.redirectTo = redirectTo;
        return this;
    }

    public void setRedirectTo(RedirectOption redirectTo) {
        this.redirectTo = redirectTo;
    }

    public Boolean isShowProductPrice() {
        return showProductPrice;
    }

    public BundleSetting showProductPrice(Boolean showProductPrice) {
        this.showProductPrice = showProductPrice;
        return this;
    }

    public void setShowProductPrice(Boolean showProductPrice) {
        this.showProductPrice = showProductPrice;
    }

    public Boolean isOneTimeDiscount() {
        return oneTimeDiscount;
    }

    public BundleSetting oneTimeDiscount(Boolean oneTimeDiscount) {
        this.oneTimeDiscount = oneTimeDiscount;
        return this;
    }

    public void setOneTimeDiscount(Boolean oneTimeDiscount) {
        this.oneTimeDiscount = oneTimeDiscount;
    }

    public Boolean isShowDiscountInCart() {
        return showDiscountInCart;
    }

    public BundleSetting showDiscountInCart(Boolean showDiscountInCart) {
        this.showDiscountInCart = showDiscountInCart;
        return this;
    }

    public void setShowDiscountInCart(Boolean showDiscountInCart) {
        this.showDiscountInCart = showDiscountInCart;
    }

    public String getSelector() {
        return selector;
    }

    public BundleSetting selector(String selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public PlacementPosition getPlacement() {
        return placement;
    }

    public BundleSetting placement(PlacementPosition placement) {
        this.placement = placement;
        return this;
    }

    public void setPlacement(PlacementPosition placement) {
        this.placement = placement;
    }

    public String getCustomCss() {
        return customCss;
    }

    public BundleSetting customCss(String customCss) {
        this.customCss = customCss;
        return this;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getVariant() {
        return variant;
    }

    public BundleSetting variant(String variant) {
        this.variant = variant;
        return this;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getDeliveryFrequency() {
        return deliveryFrequency;
    }

    public BundleSetting deliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
        return this;
    }

    public void setDeliveryFrequency(String deliveryFrequency) {
        this.deliveryFrequency = deliveryFrequency;
    }

    public String getPerDelivery() {
        return perDelivery;
    }

    public BundleSetting perDelivery(String perDelivery) {
        this.perDelivery = perDelivery;
        return this;
    }

    public void setPerDelivery(String perDelivery) {
        this.perDelivery = perDelivery;
    }

    public String getDiscountPopupHeader() {
        return discountPopupHeader;
    }

    public BundleSetting discountPopupHeader(String discountPopupHeader) {
        this.discountPopupHeader = discountPopupHeader;
        return this;
    }

    public void setDiscountPopupHeader(String discountPopupHeader) {
        this.discountPopupHeader = discountPopupHeader;
    }

    public String getDiscountPopupAmount() {
        return discountPopupAmount;
    }

    public BundleSetting discountPopupAmount(String discountPopupAmount) {
        this.discountPopupAmount = discountPopupAmount;
        return this;
    }

    public void setDiscountPopupAmount(String discountPopupAmount) {
        this.discountPopupAmount = discountPopupAmount;
    }

    public String getDiscountPopupCheckoutMessage() {
        return discountPopupCheckoutMessage;
    }

    public BundleSetting discountPopupCheckoutMessage(String discountPopupCheckoutMessage) {
        this.discountPopupCheckoutMessage = discountPopupCheckoutMessage;
        return this;
    }

    public void setDiscountPopupCheckoutMessage(String discountPopupCheckoutMessage) {
        this.discountPopupCheckoutMessage = discountPopupCheckoutMessage;
    }

    public String getDiscountPopupBuy() {
        return discountPopupBuy;
    }

    public BundleSetting discountPopupBuy(String discountPopupBuy) {
        this.discountPopupBuy = discountPopupBuy;
        return this;
    }

    public void setDiscountPopupBuy(String discountPopupBuy) {
        this.discountPopupBuy = discountPopupBuy;
    }

    public String getDiscountPopupNo() {
        return discountPopupNo;
    }

    public BundleSetting discountPopupNo(String discountPopupNo) {
        this.discountPopupNo = discountPopupNo;
        return this;
    }

    public void setDiscountPopupNo(String discountPopupNo) {
        this.discountPopupNo = discountPopupNo;
    }

    public Boolean isShowDiscountPopup() {
        return showDiscountPopup;
    }

    public BundleSetting showDiscountPopup(Boolean showDiscountPopup) {
        this.showDiscountPopup = showDiscountPopup;
        return this;
    }

    public void setShowDiscountPopup(Boolean showDiscountPopup) {
        this.showDiscountPopup = showDiscountPopup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleSetting)) {
            return false;
        }
        return id != null && id.equals(((BundleSetting) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BundleSetting{" +
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
