package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Lob;
import com.et.domain.enumeration.BundleRedirect;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the {@link com.et.domain.SubscriptionBundleSettings} entity.
 */
public class SubscriptionBundleSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private String selectedFrequencyLabelText;

    @NotNull
    private String addButtonText;

    @NotNull
    private String selectMinimumProductButtonText;

    @NotNull
    private String productsToProceedText;

    @NotNull
    private String proceedToCheckoutButtonText;

    @NotNull
    private String myDeliveryText;

    @Lob
    private String bundleTopHtml;

    @Lob
    private String bundleBottomHtml;

    private String failedToAddTitleText;

    private String okBtnText;

    private String failedToAddMsgText;

    private String buttonColor;

    private String backgroundColor;

    private String pageBackgroundColor;

    private String buttonBackgroundColor;

    private String ProductTitleFontColor;

    private String variantNotAvailable;

    private BundleRedirect bundleRedirect;

    private Boolean isBundleWithoutScroll;

    private Integer descriptionLength;

    private String currencySwitcherClassName;

    private String productPriceFormatField;

    private String customRedirectURL;

    private String viewProduct;

    private String productDetails;

    private String editQuantity;

    private String cart;

    private String shoppingCart;

    private String title;

    private String tieredDiscount;

    private String subtotal;

    private String checkoutMessage;

    private String continueShopping;

    private String spendAmountGetDiscount;

    private String buyQuantityGetDiscount;

    private String removeItem;

    private Boolean showCompareAtPrice;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private Boolean enableRedirectToProductPage;

    private String saveDiscountText;

    private Boolean disableProductDescription;

    private Boolean enableDisplayProductVendor;

    private Boolean enableDisplayProductType;

    private Boolean enableCustomAdvancedFields;

    private Boolean hideProductSearchBox;

    @Lob
    private String productFilterConfig;

    private Boolean enableProductDetailButton;


    private Boolean enableShowProductBasePrice;

    private Boolean enableClearCartSelectedProducts;

    private Boolean enableSkieyBABHeader;

    private Boolean enableOpeningSidebar;

    @Lob
    private String rightSidebarHTML;

    @Lob
    private String leftSidebarHTML;

    private Boolean isMergeIntoSingleBABVariantDropdown;

    public Boolean getHideProductSearchBox() {
        return hideProductSearchBox;
    }

    public void setHideProductSearchBox(Boolean hideProductSearchBox) {
        this.hideProductSearchBox = hideProductSearchBox;
    }

    public SubscriptionBundleSettingsDTO hideProductSearchBox(Boolean hideProductSearchBox) {
        this.hideProductSearchBox = hideProductSearchBox;
        return this;
    }

    public Boolean getEnableCustomAdvancedFields() {
        return enableCustomAdvancedFields;
    }

    public SubscriptionBundleSettingsDTO enableCustomAdvancedFields(Boolean enableCustomAdvancedFields) {
        this.enableCustomAdvancedFields = enableCustomAdvancedFields;
        return this;
    }

    public void setEnableCustomAdvancedFields(Boolean enableCustomAdvancedFields) {
        this.enableCustomAdvancedFields = enableCustomAdvancedFields;
    }

    private List<UpdateShopCustomizationRequest> shopCustomizationData;

    public List<UpdateShopCustomizationRequest> getShopCustomizationData() {
        return shopCustomizationData;
    }

    public void setShopCustomizationData(List<UpdateShopCustomizationRequest> shopCustomizationData) {
        this.shopCustomizationData = shopCustomizationData;
    }

    public Boolean isEnableDisplayProductVendor() {
        return enableDisplayProductVendor;
    }

    public SubscriptionBundleSettingsDTO enableDisplayProductVendor(Boolean enableDisplayProductVendor) {
        this.enableDisplayProductVendor = enableDisplayProductVendor;
        return this;
    }

    public void setEnableDisplayProductVendor(Boolean enableDisplayProductVendor) {
        this.enableDisplayProductVendor = enableDisplayProductVendor;
    }

    public Boolean isEnableDisplayProductType() {
        return enableDisplayProductType;
    }

    public SubscriptionBundleSettingsDTO enableDisplayProductType(Boolean enableDisplayProductType) {
        this.enableDisplayProductType = enableDisplayProductType;
        return this;
    }

    public void setEnableDisplayProductType(Boolean enableDisplayProductType) {
        this.enableDisplayProductType = enableDisplayProductType;
    }

    public Boolean isDisableProductDescription() {
        return disableProductDescription;
    }

    public SubscriptionBundleSettingsDTO disableProductDescription(Boolean disableProductDescription) {
        this.disableProductDescription = disableProductDescription;
        return this;
    }

    public void setDisableProductDescription(Boolean disableProductDescription) {
        this.disableProductDescription = disableProductDescription;
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

    public String getSelectedFrequencyLabelText() {
        return selectedFrequencyLabelText;
    }

    public void setSelectedFrequencyLabelText(String selectedFrequencyLabelText) {
        this.selectedFrequencyLabelText = selectedFrequencyLabelText;
    }

    public String getAddButtonText() {
        return addButtonText;
    }

    public void setAddButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
    }

    public String getSelectMinimumProductButtonText() {
        return selectMinimumProductButtonText;
    }

    public void setSelectMinimumProductButtonText(String selectMinimumProductButtonText) {
        this.selectMinimumProductButtonText = selectMinimumProductButtonText;
    }

    public String getProductsToProceedText() {
        return productsToProceedText;
    }

    public void setProductsToProceedText(String productsToProceedText) {
        this.productsToProceedText = productsToProceedText;
    }

    public String getProceedToCheckoutButtonText() {
        return proceedToCheckoutButtonText;
    }

    public void setProceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
    }

    public String getMyDeliveryText() {
        return myDeliveryText;
    }

    public void setMyDeliveryText(String myDeliveryText) {
        this.myDeliveryText = myDeliveryText;
    }

    public String getBundleTopHtml() {
        return bundleTopHtml;
    }

    public void setBundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
    }

    public String getBundleBottomHtml() {
        return bundleBottomHtml;
    }

    public void setBundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
    }

    public String getFailedToAddTitleText() {
        return failedToAddTitleText;
    }

    public void setFailedToAddTitleText(String failedToAddTitleText) {
        this.failedToAddTitleText = failedToAddTitleText;
    }

    public String getOkBtnText() {
        return okBtnText;
    }

    public void setOkBtnText(String okBtnText) {
        this.okBtnText = okBtnText;
    }

    public String getFailedToAddMsgText() {
        return failedToAddMsgText;
    }

    public void setFailedToAddMsgText(String failedToAddMsgText) {
        this.failedToAddMsgText = failedToAddMsgText;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getPageBackgroundColor() {
        return pageBackgroundColor;
    }

    public void setPageBackgroundColor(String pageBackgroundColor) {
        this.pageBackgroundColor = pageBackgroundColor;
    }

    public String getButtonBackgroundColor() {
        return buttonBackgroundColor;
    }

    public void setButtonBackgroundColor(String buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
    }

    public String getProductTitleFontColor() {
        return ProductTitleFontColor;
    }

    public void setProductTitleFontColor(String ProductTitleFontColor) {
        this.ProductTitleFontColor = ProductTitleFontColor;
    }

    public String getVariantNotAvailable() {
        return variantNotAvailable;
    }

    public void setVariantNotAvailable(String variantNotAvailable) {
        this.variantNotAvailable = variantNotAvailable;
    }

    public BundleRedirect getBundleRedirect() {
        return bundleRedirect;
    }

    public void setBundleRedirect(BundleRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
    }

    public Boolean isIsBundleWithoutScroll() {
        return isBundleWithoutScroll;
    }

    public void setIsBundleWithoutScroll(Boolean isBundleWithoutScroll) {
        this.isBundleWithoutScroll = isBundleWithoutScroll;
    }

    public Integer getDescriptionLength() {
        return descriptionLength;
    }

    public void setDescriptionLength(Integer descriptionLength) {
        this.descriptionLength = descriptionLength;
    }

    public String getCurrencySwitcherClassName() {
        return currencySwitcherClassName;
    }

    public void setCurrencySwitcherClassName(String currencySwitcherClassName) {
        this.currencySwitcherClassName = currencySwitcherClassName;
    }

    public String getProductPriceFormatField() {
        return productPriceFormatField;
    }

    public void setProductPriceFormatField(String productPriceFormatField) {
        this.productPriceFormatField = productPriceFormatField;
    }

    public String getCustomRedirectURL() {
        return customRedirectURL;
    }

    public void setCustomRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
    }

    public String getViewProduct() {
        return viewProduct;
    }

    public void setViewProduct(String viewProduct) {
        this.viewProduct = viewProduct;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getEditQuantity() {
        return editQuantity;
    }

    public void setEditQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(String shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTieredDiscount() {
        return tieredDiscount;
    }

    public void setTieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getCheckoutMessage() {
        return checkoutMessage;
    }

    public void setCheckoutMessage(String checkoutMessage) {
        this.checkoutMessage = checkoutMessage;
    }

    public String getContinueShopping() {
        return continueShopping;
    }

    public void setContinueShopping(String continueShopping) {
        this.continueShopping = continueShopping;
    }

    public String getSpendAmountGetDiscount() {
        return spendAmountGetDiscount;
    }

    public void setSpendAmountGetDiscount(String spendAmountGetDiscount) {
        this.spendAmountGetDiscount = spendAmountGetDiscount;
    }

    public String getBuyQuantityGetDiscount() {
        return buyQuantityGetDiscount;
    }

    public void setBuyQuantityGetDiscount(String buyQuantityGetDiscount) {
        this.buyQuantityGetDiscount = buyQuantityGetDiscount;
    }

    public String getRemoveItem() {
        return removeItem;
    }

    public void setRemoveItem(String removeItem) {
        this.removeItem = removeItem;
    }

    public Boolean isShowCompareAtPrice() {
        return showCompareAtPrice;
    }

    public void setShowCompareAtPrice(Boolean showCompareAtPrice) {
        this.showCompareAtPrice = showCompareAtPrice;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Boolean getEnableRedirectToProductPage() {
        return enableRedirectToProductPage;
    }

    public void setEnableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
    }

    public String getSaveDiscountText() {
        return saveDiscountText;
    }

    public void setSaveDiscountText(String saveDiscountText) {
        this.saveDiscountText = saveDiscountText;
    }

    public String getProductFilterConfig() {
        return productFilterConfig;
    }

    public void setProductFilterConfig(String productFilterConfig) {
        this.productFilterConfig = productFilterConfig;
    }

    public Boolean isEnableProductDetailButton() {
        return enableProductDetailButton;
    }

    public void setEnableProductDetailButton(Boolean enableProductDetailButton) {
        this.enableProductDetailButton = enableProductDetailButton;
    }

    public Boolean isEnableShowProductBasePrice() {
        return enableShowProductBasePrice;
    }

    public void setEnableShowProductBasePrice(Boolean enableShowProductBasePrice) {
        this.enableShowProductBasePrice = enableShowProductBasePrice;
    }

    public Boolean isEnableClearCartSelectedProducts() {
        return enableClearCartSelectedProducts;
    }

    public void setEnableClearCartSelectedProducts(Boolean enableClearCartSelectedProducts) {
        this.enableClearCartSelectedProducts = enableClearCartSelectedProducts;
    }

    public Boolean isEnableSkieyBABHeader() {
        return enableSkieyBABHeader;
    }

    public void setEnableSkieyBABHeader(Boolean enableSkieyBABHeader) {
        this.enableSkieyBABHeader = enableSkieyBABHeader;
    }

    public Boolean isEnableOpeningSidebar() {
        return enableOpeningSidebar;
    }

    public void setEnableOpeningSidebar(Boolean enableOpeningSidebar) {
        this.enableOpeningSidebar = enableOpeningSidebar;
    }

    public String getRightSidebarHTML() {
        return rightSidebarHTML;
    }

    public void setRightSidebarHTML(String rightSidebarHTML) {
        this.rightSidebarHTML = rightSidebarHTML;
    }

    public String getLeftSidebarHTML() {
        return leftSidebarHTML;
    }

    public void setLeftSidebarHTML(String leftSidebarHTML) {
        this.leftSidebarHTML = leftSidebarHTML;
    }

    public Boolean isIsMergeIntoSingleBABVariantDropdown() {
        return isMergeIntoSingleBABVariantDropdown;
    }

    public void setIsMergeIntoSingleBABVariantDropdown(Boolean isMergeIntoSingleBABVariantDropdown) {
        this.isMergeIntoSingleBABVariantDropdown = isMergeIntoSingleBABVariantDropdown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBundleSettingsDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionBundleSettingsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionBundleSettingsDTO{" +
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
            ", enableCustomAdvancedFields='" + getEnableCustomAdvancedFields() + "'" +
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
