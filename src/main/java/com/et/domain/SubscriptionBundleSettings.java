package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.BundleRedirect;

/**
 * A SubscriptionBundleSettings.
 */
@Entity
@Table(name = "subscription_bundle_settings")
public class SubscriptionBundleSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "selected_frequency_label_text", nullable = false)
    private String selectedFrequencyLabelText;

    @NotNull
    @Column(name = "add_button_text", nullable = false)
    private String addButtonText;

    @NotNull
    @Column(name = "select_minimum_product_button_text", nullable = false)
    private String selectMinimumProductButtonText;

    @NotNull
    @Column(name = "products_to_proceed_text", nullable = false)
    private String productsToProceedText;

    @NotNull
    @Column(name = "proceed_to_checkout_button_text", nullable = false)
    private String proceedToCheckoutButtonText;

    @NotNull
    @Column(name = "my_delivery_text", nullable = false)
    private String myDeliveryText;

    @Lob
    @Column(name = "bundle_top_html")
    private String bundleTopHtml;

    @Lob
    @Column(name = "bundle_bottom_html")
    private String bundleBottomHtml;

    @Column(name = "failed_to_add_title_text")
    private String failedToAddTitleText;

    @Column(name = "ok_btn_text")
    private String okBtnText;

    @Column(name = "failed_to_add_msg_text")
    private String failedToAddMsgText;

    @Column(name = "button_color")
    private String buttonColor;

    @Column(name = "background_color")
    private String backgroundColor;

    @Column(name = "page_background_color")
    private String pageBackgroundColor;

    @Column(name = "button_background_color")
    private String buttonBackgroundColor;

    @Column(name = "product_title_font_color")
    private String ProductTitleFontColor;

    @Column(name = "variant_not_available")
    private String variantNotAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "bundle_redirect")
    private BundleRedirect bundleRedirect;

    @Column(name = "is_bundle_without_scroll")
    private Boolean isBundleWithoutScroll;

    @Column(name = "description_length")
    private Integer descriptionLength;

    @Column(name = "currency_switcher_class_name")
    private String currencySwitcherClassName;

    @Column(name = "product_price_format_field")
    private String productPriceFormatField;

    @Column(name = "custom_redirect_url")
    private String customRedirectURL;

    @Column(name = "view_product")
    private String viewProduct;

    @Column(name = "product_details")
    private String productDetails;

    @Column(name = "edit_quantity")
    private String editQuantity;

    @Column(name = "cart")
    private String cart;

    @Column(name = "shopping_cart")
    private String shoppingCart;

    @Column(name = "title")
    private String title;

    @Column(name = "tiered_discount")
    private String tieredDiscount;

    @Column(name = "subtotal")
    private String subtotal;

    @Column(name = "checkout_message")
    private String checkoutMessage;

    @Column(name = "continue_shopping")
    private String continueShopping;

    @Column(name = "spend_amount_get_discount")
    private String spendAmountGetDiscount;

    @Column(name = "buy_quantity_get_discount")
    private String buyQuantityGetDiscount;

    @Column(name = "remove_item")
    private String removeItem;

    @Column(name = "show_compare_at_price")
    private Boolean showCompareAtPrice;

    @Column(name = "enable_redirect_to_product_page")
    private Boolean enableRedirectToProductPage = true;

    @Column(name = "save_discount_text")
    private String saveDiscountText;

    @Column(name = "disable_product_description")
    private Boolean disableProductDescription;

    @Column(name = "enable_display_product_vendor")
    private Boolean enableDisplayProductVendor;

    @Column(name = "enable_display_product_type")
    private Boolean enableDisplayProductType;

    @Column(name = "enable_custom_advanced_fields")
    private Boolean enableCustomAdvancedFields;

    @Column(name = "hide_product_search_box")
    private Boolean hideProductSearchBox;

    @Lob
    @Column(name = "product_filter_config")
    private String productFilterConfig;

    @Column(name = "enable_product_detail_button")
    private Boolean enableProductDetailButton;

    @Column(name = "enable_show_product_base_price")
    private Boolean enableShowProductBasePrice;

    @Column(name = "enable_clear_cart_selected_products")
    private Boolean enableClearCartSelectedProducts;

    @Column(name = "enable_skiey_bab_header")
    private Boolean enableSkieyBABHeader;

    @Column(name = "enable_opening_sidebar")
    private Boolean enableOpeningSidebar;

    @Lob
    @Column(name = "right_sidebar_html")
    private String rightSidebarHTML;

    @Lob
    @Column(name = "left_sidebar_html")
    private String leftSidebarHTML;

    @Column(name = "is_merge_into_single_bab_variant_dropdown")
    private Boolean isMergeIntoSingleBABVariantDropdown;

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

    public SubscriptionBundleSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSelectedFrequencyLabelText() {
        return selectedFrequencyLabelText;
    }

    public SubscriptionBundleSettings selectedFrequencyLabelText(String selectedFrequencyLabelText) {
        this.selectedFrequencyLabelText = selectedFrequencyLabelText;
        return this;
    }

    public void setSelectedFrequencyLabelText(String selectedFrequencyLabelText) {
        this.selectedFrequencyLabelText = selectedFrequencyLabelText;
    }

    public String getAddButtonText() {
        return addButtonText;
    }

    public SubscriptionBundleSettings addButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
        return this;
    }

    public void setAddButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
    }

    public String getSelectMinimumProductButtonText() {
        return selectMinimumProductButtonText;
    }

    public SubscriptionBundleSettings selectMinimumProductButtonText(String selectMinimumProductButtonText) {
        this.selectMinimumProductButtonText = selectMinimumProductButtonText;
        return this;
    }

    public void setSelectMinimumProductButtonText(String selectMinimumProductButtonText) {
        this.selectMinimumProductButtonText = selectMinimumProductButtonText;
    }

    public String getProductsToProceedText() {
        return productsToProceedText;
    }

    public SubscriptionBundleSettings productsToProceedText(String productsToProceedText) {
        this.productsToProceedText = productsToProceedText;
        return this;
    }

    public void setProductsToProceedText(String productsToProceedText) {
        this.productsToProceedText = productsToProceedText;
    }

    public String getProceedToCheckoutButtonText() {
        return proceedToCheckoutButtonText;
    }

    public SubscriptionBundleSettings proceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
        return this;
    }

    public void setProceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
    }

    public String getMyDeliveryText() {
        return myDeliveryText;
    }

    public SubscriptionBundleSettings myDeliveryText(String myDeliveryText) {
        this.myDeliveryText = myDeliveryText;
        return this;
    }

    public void setMyDeliveryText(String myDeliveryText) {
        this.myDeliveryText = myDeliveryText;
    }

    public String getBundleTopHtml() {
        return bundleTopHtml;
    }

    public SubscriptionBundleSettings bundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
        return this;
    }

    public void setBundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
    }

    public String getBundleBottomHtml() {
        return bundleBottomHtml;
    }

    public SubscriptionBundleSettings bundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
        return this;
    }

    public void setBundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
    }

    public String getFailedToAddTitleText() {
        return failedToAddTitleText;
    }

    public SubscriptionBundleSettings failedToAddTitleText(String failedToAddTitleText) {
        this.failedToAddTitleText = failedToAddTitleText;
        return this;
    }

    public void setFailedToAddTitleText(String failedToAddTitleText) {
        this.failedToAddTitleText = failedToAddTitleText;
    }

    public String getOkBtnText() {
        return okBtnText;
    }

    public SubscriptionBundleSettings okBtnText(String okBtnText) {
        this.okBtnText = okBtnText;
        return this;
    }

    public void setOkBtnText(String okBtnText) {
        this.okBtnText = okBtnText;
    }

    public String getFailedToAddMsgText() {
        return failedToAddMsgText;
    }

    public SubscriptionBundleSettings failedToAddMsgText(String failedToAddMsgText) {
        this.failedToAddMsgText = failedToAddMsgText;
        return this;
    }

    public void setFailedToAddMsgText(String failedToAddMsgText) {
        this.failedToAddMsgText = failedToAddMsgText;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public SubscriptionBundleSettings buttonColor(String buttonColor) {
        this.buttonColor = buttonColor;
        return this;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public SubscriptionBundleSettings backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getPageBackgroundColor() {
        return pageBackgroundColor;
    }

    public SubscriptionBundleSettings pageBackgroundColor(String pageBackgroundColor) {
        this.pageBackgroundColor = pageBackgroundColor;
        return this;
    }

    public void setPageBackgroundColor(String pageBackgroundColor) {
        this.pageBackgroundColor = pageBackgroundColor;
    }

    public String getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    public SubscriptionBundleSettings buttonBackgroundColor(String buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
        return this;
    }

    public void setButtonBackgroundColor(String buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
    }

    public String getProductTitleFontColor() {
        return ProductTitleFontColor;
    }

    public SubscriptionBundleSettings ProductTitleFontColor(String ProductTitleFontColor) {
        this.ProductTitleFontColor = ProductTitleFontColor;
        return this;
    }

    public void setProductTitleFontColor(String ProductTitleFontColor) {
        this.ProductTitleFontColor = ProductTitleFontColor;
    }

    public String getVariantNotAvailable() {
        return variantNotAvailable;
    }

    public SubscriptionBundleSettings variantNotAvailable(String variantNotAvailable) {
        this.variantNotAvailable = variantNotAvailable;
        return this;
    }

    public void setVariantNotAvailable(String variantNotAvailable) {
        this.variantNotAvailable = variantNotAvailable;
    }

    public BundleRedirect getBundleRedirect() {
        return bundleRedirect;
    }

    public SubscriptionBundleSettings bundleRedirect(BundleRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
        return this;
    }

    public void setBundleRedirect(BundleRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
    }

    public Boolean isIsBundleWithoutScroll() {
        return isBundleWithoutScroll;
    }

    public SubscriptionBundleSettings isBundleWithoutScroll(Boolean isBundleWithoutScroll) {
        this.isBundleWithoutScroll = isBundleWithoutScroll;
        return this;
    }

    public void setIsBundleWithoutScroll(Boolean isBundleWithoutScroll) {
        this.isBundleWithoutScroll = isBundleWithoutScroll;
    }

    public Integer getDescriptionLength() {
        return descriptionLength;
    }

    public SubscriptionBundleSettings descriptionLength(Integer descriptionLength) {
        this.descriptionLength = descriptionLength;
        return this;
    }

    public void setDescriptionLength(Integer descriptionLength) {
        this.descriptionLength = descriptionLength;
    }

    public String getCurrencySwitcherClassName() {
        return currencySwitcherClassName;
    }

    public SubscriptionBundleSettings currencySwitcherClassName(String currencySwitcherClassName) {
        this.currencySwitcherClassName = currencySwitcherClassName;
        return this;
    }

    public void setCurrencySwitcherClassName(String currencySwitcherClassName) {
        this.currencySwitcherClassName = currencySwitcherClassName;
    }

    public String getProductPriceFormatField() {
        return productPriceFormatField;
    }

    public SubscriptionBundleSettings productPriceFormatField(String productPriceFormatField) {
        this.productPriceFormatField = productPriceFormatField;
        return this;
    }

    public void setProductPriceFormatField(String productPriceFormatField) {
        this.productPriceFormatField = productPriceFormatField;
    }

    public String getCustomRedirectURL() {
        return customRedirectURL;
    }

    public SubscriptionBundleSettings customRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
        return this;
    }

    public void setCustomRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
    }

    public String getViewProduct() {
        return viewProduct;
    }

    public SubscriptionBundleSettings viewProduct(String viewProduct) {
        this.viewProduct = viewProduct;
        return this;
    }

    public void setViewProduct(String viewProduct) {
        this.viewProduct = viewProduct;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public SubscriptionBundleSettings productDetails(String productDetails) {
        this.productDetails = productDetails;
        return this;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getEditQuantity() {
        return editQuantity;
    }

    public SubscriptionBundleSettings editQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
        return this;
    }

    public void setEditQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
    }

    public String getCart() {
        return cart;
    }

    public SubscriptionBundleSettings cart(String cart) {
        this.cart = cart;
        return this;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getShoppingCart() {
        return shoppingCart;
    }

    public SubscriptionBundleSettings shoppingCart(String shoppingCart) {
        this.shoppingCart = shoppingCart;
        return this;
    }

    public void setShoppingCart(String shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public String getTitle() {
        return title;
    }

    public SubscriptionBundleSettings title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTieredDiscount() {
        return tieredDiscount;
    }

    public SubscriptionBundleSettings tieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
        return this;
    }

    public void setTieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public SubscriptionBundleSettings subtotal(String subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getCheckoutMessage() {
        return checkoutMessage;
    }

    public SubscriptionBundleSettings checkoutMessage(String checkoutMessage) {
        this.checkoutMessage = checkoutMessage;
        return this;
    }

    public void setCheckoutMessage(String checkoutMessage) {
        this.checkoutMessage = checkoutMessage;
    }

    public String getContinueShopping() {
        return continueShopping;
    }

    public SubscriptionBundleSettings continueShopping(String continueShopping) {
        this.continueShopping = continueShopping;
        return this;
    }

    public void setContinueShopping(String continueShopping) {
        this.continueShopping = continueShopping;
    }

    public String getSpendAmountGetDiscount() {
        return spendAmountGetDiscount;
    }

    public SubscriptionBundleSettings spendAmountGetDiscount(String spendAmountGetDiscount) {
        this.spendAmountGetDiscount = spendAmountGetDiscount;
        return this;
    }

    public void setSpendAmountGetDiscount(String spendAmountGetDiscount) {
        this.spendAmountGetDiscount = spendAmountGetDiscount;
    }

    public String getBuyQuantityGetDiscount() {
        return buyQuantityGetDiscount;
    }

    public SubscriptionBundleSettings buyQuantityGetDiscount(String buyQuantityGetDiscount) {
        this.buyQuantityGetDiscount = buyQuantityGetDiscount;
        return this;
    }

    public void setBuyQuantityGetDiscount(String buyQuantityGetDiscount) {
        this.buyQuantityGetDiscount = buyQuantityGetDiscount;
    }

    public String getRemoveItem() {
        return removeItem;
    }

    public SubscriptionBundleSettings removeItem(String removeItem) {
        this.removeItem = removeItem;
        return this;
    }

    public void setRemoveItem(String removeItem) {
        this.removeItem = removeItem;
    }

    public Boolean isShowCompareAtPrice() {
        return showCompareAtPrice;
    }

    public SubscriptionBundleSettings showCompareAtPrice(Boolean showCompareAtPrice) {
        this.showCompareAtPrice = showCompareAtPrice;
        return this;
    }

    public void setShowCompareAtPrice(Boolean showCompareAtPrice) {
        this.showCompareAtPrice = showCompareAtPrice;
    }

    public Boolean getEnableRedirectToProductPage() {
        return enableRedirectToProductPage;
    }

    public SubscriptionBundleSettings enableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
        return this;
    }

    public void setEnableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
    }

    public String getSaveDiscountText() {
        return saveDiscountText;
    }

    public SubscriptionBundleSettings saveDiscountText(String saveDiscountText) {
        this.saveDiscountText = saveDiscountText;
        return this;
    }

    public void setSaveDiscountText(String saveDiscountText) {
        this.saveDiscountText = saveDiscountText;
    }

    public Boolean isDisableProductDescription() {
        return disableProductDescription;
    }

    public SubscriptionBundleSettings disableProductDescription(Boolean disableProductDescription) {
        this.disableProductDescription = disableProductDescription;
        return this;
    }

    public void setDisableProductDescription(Boolean disableProductDescription) {
        this.disableProductDescription = disableProductDescription;
    }

    public Boolean isEnableDisplayProductVendor() {
        return enableDisplayProductVendor;
    }

    public SubscriptionBundleSettings enableDisplayProductVendor(Boolean enableDisplayProductVendor) {
        this.enableDisplayProductVendor = enableDisplayProductVendor;
        return this;
    }

    public void setEnableDisplayProductVendor(Boolean enableDisplayProductVendor) {
        this.enableDisplayProductVendor = enableDisplayProductVendor;
    }

    public Boolean isEnableDisplayProductType() {
        return enableDisplayProductType;
    }

    public SubscriptionBundleSettings enableDisplayProductType(Boolean enableDisplayProductType) {
        this.enableDisplayProductType = enableDisplayProductType;
        return this;
    }

    public void setEnableDisplayProductType(Boolean enableDisplayProductType) {
        this.enableDisplayProductType = enableDisplayProductType;
    }

    public Boolean isEnableCustomAdvancedFields() {
        return enableCustomAdvancedFields;
    }

    public SubscriptionBundleSettings enableCustomAdvancedFields(Boolean enableCustomAdvancedFields) {
        this.enableCustomAdvancedFields = enableCustomAdvancedFields;
        return this;
    }

    public void setEnableCustomAdvancedFields(Boolean enableCustomAdvancedFields) {
        this.enableCustomAdvancedFields = enableCustomAdvancedFields;
    }

    public Boolean isHideProductSearchBox() {
        return hideProductSearchBox;
    }

    public SubscriptionBundleSettings hideProductSearchBox(Boolean hideProductSearchBox) {
        this.hideProductSearchBox = hideProductSearchBox;
        return this;
    }

    public void setHideProductSearchBox(Boolean hideProductSearchBox) {
        this.hideProductSearchBox = hideProductSearchBox;
    }

    public String getProductFilterConfig() {
        return productFilterConfig;
    }

    public SubscriptionBundleSettings productFilterConfig(String productFilterConfig) {
        this.productFilterConfig = productFilterConfig;
        return this;
    }

    public void setProductFilterConfig(String productFilterConfig) {
        this.productFilterConfig = productFilterConfig;
    }

    public Boolean isEnableProductDetailButton() {
        return enableProductDetailButton;
    }

    public SubscriptionBundleSettings enableProductDetailButton(Boolean enableProductDetailButton) {
        this.enableProductDetailButton = enableProductDetailButton;
        return this;
    }

    public void setEnableProductDetailButton(Boolean enableProductDetailButton) {
        this.enableProductDetailButton = enableProductDetailButton;
    }

    public Boolean isEnableShowProductBasePrice() {
        return enableShowProductBasePrice;
    }

    public SubscriptionBundleSettings enableShowProductBasePrice(Boolean enableShowProductBasePrice) {
        this.enableShowProductBasePrice = enableShowProductBasePrice;
        return this;
    }

    public void setEnableShowProductBasePrice(Boolean enableShowProductBasePrice) {
        this.enableShowProductBasePrice = enableShowProductBasePrice;
    }

    public Boolean isEnableClearCartSelectedProducts() {
        return enableClearCartSelectedProducts;
    }

    public SubscriptionBundleSettings enableClearCartSelectedProducts(Boolean enableClearCartSelectedProducts) {
        this.enableClearCartSelectedProducts = enableClearCartSelectedProducts;
        return this;
    }

    public void setEnableClearCartSelectedProducts(Boolean enableClearCartSelectedProducts) {
        this.enableClearCartSelectedProducts = enableClearCartSelectedProducts;
    }

    public Boolean isEnableSkieyBABHeader() {
        return enableSkieyBABHeader;
    }

    public SubscriptionBundleSettings enableSkieyBABHeader(Boolean enableSkieyBABHeader) {
        this.enableSkieyBABHeader = enableSkieyBABHeader;
        return this;
    }

    public void setEnableSkieyBABHeader(Boolean enableSkieyBABHeader) {
        this.enableSkieyBABHeader = enableSkieyBABHeader;
    }

    public Boolean isEnableOpeningSidebar() {
        return enableOpeningSidebar;
    }

    public SubscriptionBundleSettings enableOpeningSidebar(Boolean enableOpeningSidebar) {
        this.enableOpeningSidebar = enableOpeningSidebar;
        return this;
    }

    public void setEnableOpeningSidebar(Boolean enableOpeningSidebar) {
        this.enableOpeningSidebar = enableOpeningSidebar;
    }

    public String getRightSidebarHTML() {
        return rightSidebarHTML;
    }

    public SubscriptionBundleSettings rightSidebarHTML(String rightSidebarHTML) {
        this.rightSidebarHTML = rightSidebarHTML;
        return this;
    }

    public void setRightSidebarHTML(String rightSidebarHTML) {
        this.rightSidebarHTML = rightSidebarHTML;
    }

    public String getLeftSidebarHTML() {
        return leftSidebarHTML;
    }

    public SubscriptionBundleSettings leftSidebarHTML(String leftSidebarHTML) {
        this.leftSidebarHTML = leftSidebarHTML;
        return this;
    }

    public void setLeftSidebarHTML(String leftSidebarHTML) {
        this.leftSidebarHTML = leftSidebarHTML;
    }

    public Boolean isIsMergeIntoSingleBABVariantDropdown() {
        return isMergeIntoSingleBABVariantDropdown;
    }

    public SubscriptionBundleSettings isMergeIntoSingleBABVariantDropdown(Boolean isMergeIntoSingleBABVariantDropdown) {
        this.isMergeIntoSingleBABVariantDropdown = isMergeIntoSingleBABVariantDropdown;
        return this;
    }

    public void setIsMergeIntoSingleBABVariantDropdown(Boolean isMergeIntoSingleBABVariantDropdown) {
        this.isMergeIntoSingleBABVariantDropdown = isMergeIntoSingleBABVariantDropdown;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBundleSettings)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionBundleSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionBundleSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", selectedFrequencyLabelText='" + getSelectedFrequencyLabelText() + "'" +
            ", addButtonText='" + getAddButtonText() + "'" +
            ", selectMinimumProductButtonText='" + getSelectMinimumProductButtonText() + "'" +
            ", productsToProceedText='" + getProductsToProceedText() + "'" +
            ", proceedToCheckoutButtonText='" + getProceedToCheckoutButtonText() + "'" +
            ", myDeliveryText='" + getMyDeliveryText() + "'" +
            ", bundleTopHtml='" + getBundleTopHtml() + "'" +
            ", bundleBottomHtml='" + getBundleBottomHtml() + "'" +
            ", failedToAddTitleText='" + getFailedToAddTitleText() + "'" +
            ", okBtnText='" + getOkBtnText() + "'" +
            ", failedToAddMsgText='" + getFailedToAddMsgText() + "'" +
            ", buttonColor='" + getButtonColor() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            ", pageBackgroundColor='" + getPageBackgroundColor() + "'" +
            ", buttonBackgroundColor='" + getButtonBackgroundColor() + "'" +
            ", ProductTitleFontColor='" + getProductTitleFontColor() + "'" +
            ", variantNotAvailable='" + getVariantNotAvailable() + "'" +
            ", bundleRedirect='" + getBundleRedirect() + "'" +
            ", isBundleWithoutScroll='" + isIsBundleWithoutScroll() + "'" +
            ", descriptionLength=" + getDescriptionLength() +
            ", currencySwitcherClassName='" + getCurrencySwitcherClassName() + "'" +
            ", productPriceFormatField='" + getProductPriceFormatField() + "'" +
            ", customRedirectURL='" + getCustomRedirectURL() + "'" +
            ", viewProduct='" + getViewProduct() + "'" +
            ", productDetails='" + getProductDetails() + "'" +
            ", editQuantity='" + getEditQuantity() + "'" +
            ", cart='" + getCart() + "'" +
            ", shoppingCart='" + getShoppingCart() + "'" +
            ", title='" + getTitle() + "'" +
            ", tieredDiscount='" + getTieredDiscount() + "'" +
            ", subtotal='" + getSubtotal() + "'" +
            ", checkoutMessage='" + getCheckoutMessage() + "'" +
            ", continueShopping='" + getContinueShopping() + "'" +
            ", spendAmountGetDiscount='" + getSpendAmountGetDiscount() + "'" +
            ", buyQuantityGetDiscount='" + getBuyQuantityGetDiscount() + "'" +
            ", removeItem='" + getRemoveItem() + "'" +
            ", showCompareAtPrice='" + isShowCompareAtPrice() + "'" +
            ", enableRedirectToProductPage='" + getEnableRedirectToProductPage() + "'" +
            ", saveDiscountText='" + getSaveDiscountText() + "'" +
            ", disableProductDescription='" + isDisableProductDescription() + "'" +
            ", enableDisplayProductVendor='" + isEnableDisplayProductVendor() + "'" +
            ", enableDisplayProductType='" + isEnableDisplayProductType() + "'" +
            ", enableCustomAdvancedFields='" + isEnableCustomAdvancedFields() + "'" +
            ", hideProductSearchBox='" + isHideProductSearchBox() + "'" +
            ", productFilterConfig='" + getProductFilterConfig() + "'" +
            ", enableProductDetailButton='" + isEnableProductDetailButton() + "'" +
            ", enableShowProductBasePrice='" + isEnableShowProductBasePrice() + "'" +
            ", enableClearCartSelectedProducts='" + isEnableClearCartSelectedProducts() + "'" +
            ", enableSkieyBABHeader='" + isEnableSkieyBABHeader() + "'" +
            ", enableOpeningSidebar='" + isEnableOpeningSidebar() + "'" +
            ", rightSidebarHTML='" + getRightSidebarHTML() + "'" +
            ", leftSidebarHTML='" + getLeftSidebarHTML() + "'" +
            ", isMergeIntoSingleBABVariantDropdown='" + isIsMergeIntoSingleBABVariantDropdown() + "'" +
            "}";
    }
}
