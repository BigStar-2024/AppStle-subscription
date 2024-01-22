package com.et.service.dto;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.SubscriptionWidgetSettings} entity.
 */
public class SubscriptionWidgetSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private String oneTimePurchaseText;

    private String deliveryText;

    private String purchaseOptionsText;

    private String subscriptionOptionText;

    private String sellingPlanSelectTitle;

    private String tooltipTitle;

    @Lob
    private String tooltipDesctiption;

    private String subscriptionWidgetMarginTop;

    private String subscriptionWidgetMarginBottom;

    private String subscriptionWrapperBorderWidth;

    private String subscriptionWrapperBorderColor;

    private String circleBorderColor;

    private String dotBackgroundColor;

    private String selectPaddingTop;

    private String selectPaddingBottom;

    private String selectPaddingLeft;

    private String selectPaddingRight;

    private String selectBorderWidth;

    private String selectBorderStyle;

    private String selectBorderColor;

    private String selectBorderRadius;

    private String tooltipSubscriptionSvgFill;

    private String tooltipColor;

    private String tooltipBackgroundColor;

    private String tooltipBorderTopColorBorderTopColor;

    private String subscriptionFinalPriceColor;

    private String subscriptionWidgetTextColor;

    private String orderStatusManageSubscriptionTitle;

    private String orderStatusManageSubscriptionDescription;

    private String orderStatusManageSubscriptionButtonText;

    private Boolean showTooltipOnClick;

    private Boolean subscriptionOptionSelectedByDefault;

    private Boolean widgetEnabled;

    private Boolean showTooltip;

    private String sellingPlanTitleText;

    private String oneTimePriceText;

    private String selectedPayAsYouGoSellingPlanPriceText;

    private String selectedPrepaidSellingPlanPriceText;

    private String selectedDiscountFormat;

    private String manageSubscriptionBtnFormat;

    @Lob
    private String tooltipDescriptionOnPrepaidPlan;

    @Lob
    private String tooltipDescriptionOnMultipleDiscount;

    @Lob
    private String tooltipDescriptionCustomization;

    private Boolean showStaticTooltip;

    private Boolean showAppstleLink;

    @Lob
    private String subscriptionPriceDisplayText;

    private Boolean sortByDefaultSequence;

    private Boolean showSubOptionBeforeOneTime;

    private Boolean showCheckoutSubscriptionBtn;

    private String totalPricePerDeliveryText;

    private Boolean widgetEnabledOnSoldVariant;

    private Boolean enableCartWidgetFeature;

    private Boolean switchRadioButtonWidget;

    private String formMappingAttributeName;

    private String formMappingAttributeSelector;

    private String quickViewModalPollingSelector;

    private String updatePriceOnQuantityChange;

    private String widgetParentSelector;

    private String quantitySelector;

    private String loyaltyDetailsLabelText;

    @Lob
    private String loyaltyPerkDescriptionText;

    private Boolean detectVariantFromURLParams;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private List<UpdateShopCustomizationRequest> shopCustomizationData;

    private Boolean disableQueryParamsUpdate;


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

    public String getOneTimePurchaseText() {
        return oneTimePurchaseText;
    }

    public void setOneTimePurchaseText(String oneTimePurchaseText) {
        this.oneTimePurchaseText = oneTimePurchaseText;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }

    public String getPurchaseOptionsText() {
        return purchaseOptionsText;
    }

    public void setPurchaseOptionsText(String purchaseOptionsText) {
        this.purchaseOptionsText = purchaseOptionsText;
    }

    public String getSubscriptionOptionText() {
        return subscriptionOptionText;
    }

    public void setSubscriptionOptionText(String subscriptionOptionText) {
        this.subscriptionOptionText = subscriptionOptionText;
    }

    public String getSellingPlanSelectTitle() {
        return sellingPlanSelectTitle;
    }

    public void setSellingPlanSelectTitle(String sellingPlanSelectTitle) {
        this.sellingPlanSelectTitle = sellingPlanSelectTitle;
    }

    public String getTooltipTitle() {
        return tooltipTitle;
    }

    public void setTooltipTitle(String tooltipTitle) {
        this.tooltipTitle = tooltipTitle;
    }

    public String getTooltipDesctiption() {
        return tooltipDesctiption;
    }

    public void setTooltipDesctiption(String tooltipDesctiption) {
        this.tooltipDesctiption = tooltipDesctiption;
    }

    public String getSubscriptionWidgetMarginTop() {
        return subscriptionWidgetMarginTop;
    }

    public void setSubscriptionWidgetMarginTop(String subscriptionWidgetMarginTop) {
        this.subscriptionWidgetMarginTop = subscriptionWidgetMarginTop;
    }

    public String getSubscriptionWidgetMarginBottom() {
        return subscriptionWidgetMarginBottom;
    }

    public void setSubscriptionWidgetMarginBottom(String subscriptionWidgetMarginBottom) {
        this.subscriptionWidgetMarginBottom = subscriptionWidgetMarginBottom;
    }

    public String getSubscriptionWrapperBorderWidth() {
        return subscriptionWrapperBorderWidth;
    }

    public void setSubscriptionWrapperBorderWidth(String subscriptionWrapperBorderWidth) {
        this.subscriptionWrapperBorderWidth = subscriptionWrapperBorderWidth;
    }

    public String getSubscriptionWrapperBorderColor() {
        return subscriptionWrapperBorderColor;
    }

    public void setSubscriptionWrapperBorderColor(String subscriptionWrapperBorderColor) {
        this.subscriptionWrapperBorderColor = subscriptionWrapperBorderColor;
    }

    public String getCircleBorderColor() {
        return circleBorderColor;
    }

    public void setCircleBorderColor(String circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    public String getDotBackgroundColor() {
        return dotBackgroundColor;
    }

    public void setDotBackgroundColor(String dotBackgroundColor) {
        this.dotBackgroundColor = dotBackgroundColor;
    }

    public String getSelectPaddingTop() {
        return selectPaddingTop;
    }

    public void setSelectPaddingTop(String selectPaddingTop) {
        this.selectPaddingTop = selectPaddingTop;
    }

    public String getSelectPaddingBottom() {
        return selectPaddingBottom;
    }

    public void setSelectPaddingBottom(String selectPaddingBottom) {
        this.selectPaddingBottom = selectPaddingBottom;
    }

    public String getSelectPaddingLeft() {
        return selectPaddingLeft;
    }

    public void setSelectPaddingLeft(String selectPaddingLeft) {
        this.selectPaddingLeft = selectPaddingLeft;
    }

    public String getSelectPaddingRight() {
        return selectPaddingRight;
    }

    public void setSelectPaddingRight(String selectPaddingRight) {
        this.selectPaddingRight = selectPaddingRight;
    }

    public String getSelectBorderWidth() {
        return selectBorderWidth;
    }

    public void setSelectBorderWidth(String selectBorderWidth) {
        this.selectBorderWidth = selectBorderWidth;
    }

    public String getSelectBorderStyle() {
        return selectBorderStyle;
    }

    public void setSelectBorderStyle(String selectBorderStyle) {
        this.selectBorderStyle = selectBorderStyle;
    }

    public String getSelectBorderColor() {
        return selectBorderColor;
    }

    public void setSelectBorderColor(String selectBorderColor) {
        this.selectBorderColor = selectBorderColor;
    }

    public String getSelectBorderRadius() {
        return selectBorderRadius;
    }

    public void setSelectBorderRadius(String selectBorderRadius) {
        this.selectBorderRadius = selectBorderRadius;
    }

    public String getTooltipSubscriptionSvgFill() {
        return tooltipSubscriptionSvgFill;
    }

    public void setTooltipSubscriptionSvgFill(String tooltipSubscriptionSvgFill) {
        this.tooltipSubscriptionSvgFill = tooltipSubscriptionSvgFill;
    }

    public String getTooltipColor() {
        return tooltipColor;
    }

    public void setTooltipColor(String tooltipColor) {
        this.tooltipColor = tooltipColor;
    }

    public String getTooltipBackgroundColor() {
        return tooltipBackgroundColor;
    }

    public void setTooltipBackgroundColor(String tooltipBackgroundColor) {
        this.tooltipBackgroundColor = tooltipBackgroundColor;
    }

    public String getTooltipBorderTopColorBorderTopColor() {
        return tooltipBorderTopColorBorderTopColor;
    }

    public void setTooltipBorderTopColorBorderTopColor(String tooltipBorderTopColorBorderTopColor) {
        this.tooltipBorderTopColorBorderTopColor = tooltipBorderTopColorBorderTopColor;
    }

    public String getSubscriptionFinalPriceColor() {
        return subscriptionFinalPriceColor;
    }

    public void setSubscriptionFinalPriceColor(String subscriptionFinalPriceColor) {
        this.subscriptionFinalPriceColor = subscriptionFinalPriceColor;
    }

    public String getSubscriptionWidgetTextColor() {
        return subscriptionWidgetTextColor;
    }

    public void setSubscriptionWidgetTextColor(String subscriptionWidgetTextColor) {
        this.subscriptionWidgetTextColor = subscriptionWidgetTextColor;
    }

    public String getOrderStatusManageSubscriptionTitle() {
        return orderStatusManageSubscriptionTitle;
    }

    public void setOrderStatusManageSubscriptionTitle(String orderStatusManageSubscriptionTitle) {
        this.orderStatusManageSubscriptionTitle = orderStatusManageSubscriptionTitle;
    }

    public String getOrderStatusManageSubscriptionDescription() {
        return orderStatusManageSubscriptionDescription;
    }

    public void setOrderStatusManageSubscriptionDescription(String orderStatusManageSubscriptionDescription) {
        this.orderStatusManageSubscriptionDescription = orderStatusManageSubscriptionDescription;
    }

    public String getOrderStatusManageSubscriptionButtonText() {
        return orderStatusManageSubscriptionButtonText;
    }

    public void setOrderStatusManageSubscriptionButtonText(String orderStatusManageSubscriptionButtonText) {
        this.orderStatusManageSubscriptionButtonText = orderStatusManageSubscriptionButtonText;
    }

    public Boolean isShowTooltipOnClick() {
        return showTooltipOnClick;
    }

    public void setShowTooltipOnClick(Boolean showTooltipOnClick) {
        this.showTooltipOnClick = showTooltipOnClick;
    }

    public Boolean isSubscriptionOptionSelectedByDefault() {
        return subscriptionOptionSelectedByDefault;
    }

    public void setSubscriptionOptionSelectedByDefault(Boolean subscriptionOptionSelectedByDefault) {
        this.subscriptionOptionSelectedByDefault = subscriptionOptionSelectedByDefault;
    }

    public Boolean isWidgetEnabled() {
        return widgetEnabled;
    }

    public void setWidgetEnabled(Boolean widgetEnabled) {
        this.widgetEnabled = widgetEnabled;
    }

    public Boolean isShowTooltip() {
        return showTooltip;
    }

    public void setShowTooltip(Boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    public String getSellingPlanTitleText() {
        return sellingPlanTitleText;
    }

    public void setSellingPlanTitleText(String sellingPlanTitleText) {
        this.sellingPlanTitleText = sellingPlanTitleText;
    }

    public String getOneTimePriceText() {
        return oneTimePriceText;
    }

    public void setOneTimePriceText(String oneTimePriceText) {
        this.oneTimePriceText = oneTimePriceText;
    }

    public String getSelectedPayAsYouGoSellingPlanPriceText() {
        return selectedPayAsYouGoSellingPlanPriceText;
    }

    public void setSelectedPayAsYouGoSellingPlanPriceText(String selectedPayAsYouGoSellingPlanPriceText) {
        this.selectedPayAsYouGoSellingPlanPriceText = selectedPayAsYouGoSellingPlanPriceText;
    }

    public String getSelectedPrepaidSellingPlanPriceText() {
        return selectedPrepaidSellingPlanPriceText;
    }

    public void setSelectedPrepaidSellingPlanPriceText(String selectedPrepaidSellingPlanPriceText) {
        this.selectedPrepaidSellingPlanPriceText = selectedPrepaidSellingPlanPriceText;
    }

    public String getSelectedDiscountFormat() {
        return selectedDiscountFormat;
    }

    public void setSelectedDiscountFormat(String selectedDiscountFormat) {
        this.selectedDiscountFormat = selectedDiscountFormat;
    }

    public String getManageSubscriptionBtnFormat() {
        return manageSubscriptionBtnFormat;
    }

    public void setManageSubscriptionBtnFormat(String manageSubscriptionBtnFormat) {
        this.manageSubscriptionBtnFormat = manageSubscriptionBtnFormat;
    }

    public String getTooltipDescriptionOnPrepaidPlan() {
        return tooltipDescriptionOnPrepaidPlan;
    }

    public void setTooltipDescriptionOnPrepaidPlan(String tooltipDescriptionOnPrepaidPlan) {
        this.tooltipDescriptionOnPrepaidPlan = tooltipDescriptionOnPrepaidPlan;
    }

    public String getTooltipDescriptionOnMultipleDiscount() {
        return tooltipDescriptionOnMultipleDiscount;
    }

    public void setTooltipDescriptionOnMultipleDiscount(String tooltipDescriptionOnMultipleDiscount) {
        this.tooltipDescriptionOnMultipleDiscount = tooltipDescriptionOnMultipleDiscount;
    }

    public String getTooltipDescriptionCustomization() {
        return tooltipDescriptionCustomization;
    }

    public void setTooltipDescriptionCustomization(String tooltipDescriptionCustomization) {
        this.tooltipDescriptionCustomization = tooltipDescriptionCustomization;
    }

    public Boolean isShowStaticTooltip() {
        return showStaticTooltip;
    }

    public void setShowStaticTooltip(Boolean showStaticTooltip) {
        this.showStaticTooltip = showStaticTooltip;
    }

    public Boolean isShowAppstleLink() {
        return showAppstleLink;
    }

    public void setShowAppstleLink(Boolean showAppstleLink) {
        this.showAppstleLink = showAppstleLink;
    }

    public String getSubscriptionPriceDisplayText() {
        return subscriptionPriceDisplayText;
    }

    public void setSubscriptionPriceDisplayText(String subscriptionPriceDisplayText) {
        this.subscriptionPriceDisplayText = subscriptionPriceDisplayText;
    }

    public Boolean isSortByDefaultSequence() {
        return sortByDefaultSequence;
    }

    public void setSortByDefaultSequence(Boolean sortByDefaultSequence) {
        this.sortByDefaultSequence = sortByDefaultSequence;
    }

    public Boolean isShowSubOptionBeforeOneTime() {
        return showSubOptionBeforeOneTime;
    }

    public void setShowSubOptionBeforeOneTime(Boolean showSubOptionBeforeOneTime) {
        this.showSubOptionBeforeOneTime = showSubOptionBeforeOneTime;
    }

    public Boolean isShowCheckoutSubscriptionBtn() {
        return showCheckoutSubscriptionBtn;
    }

    public void setShowCheckoutSubscriptionBtn(Boolean showCheckoutSubscriptionBtn) {
        this.showCheckoutSubscriptionBtn = showCheckoutSubscriptionBtn;
    }

    public String getTotalPricePerDeliveryText() {
        return totalPricePerDeliveryText;
    }

    public void setTotalPricePerDeliveryText(String totalPricePerDeliveryText) {
        this.totalPricePerDeliveryText = totalPricePerDeliveryText;
    }

    public Boolean isWidgetEnabledOnSoldVariant() {
        return widgetEnabledOnSoldVariant;
    }

    public void setWidgetEnabledOnSoldVariant(Boolean widgetEnabledOnSoldVariant) {
        this.widgetEnabledOnSoldVariant = widgetEnabledOnSoldVariant;
    }

    public Boolean isEnableCartWidgetFeature() {
        return enableCartWidgetFeature;
    }

    public void setEnableCartWidgetFeature(Boolean enableCartWidgetFeature) {
        this.enableCartWidgetFeature = enableCartWidgetFeature;
    }

    public Boolean isSwitchRadioButtonWidget() {
        return switchRadioButtonWidget;
    }

    public void setSwitchRadioButtonWidget(Boolean switchRadioButtonWidget) {
        this.switchRadioButtonWidget = switchRadioButtonWidget;
    }

    public String getFormMappingAttributeName() {
        return formMappingAttributeName;
    }

    public void setFormMappingAttributeName(String formMappingAttributeName) {
        this.formMappingAttributeName = formMappingAttributeName;
    }

    public String getFormMappingAttributeSelector() {
        return formMappingAttributeSelector;
    }

    public void setFormMappingAttributeSelector(String formMappingAttributeSelector) {
        this.formMappingAttributeSelector = formMappingAttributeSelector;
    }

    public String getQuickViewModalPollingSelector() {
        return quickViewModalPollingSelector;
    }

    public void setQuickViewModalPollingSelector(String quickViewModalPollingSelector) {
        this.quickViewModalPollingSelector = quickViewModalPollingSelector;
    }

    public String getUpdatePriceOnQuantityChange() {
        return updatePriceOnQuantityChange;
    }

    public void setUpdatePriceOnQuantityChange(String updatePriceOnQuantityChange) {
        this.updatePriceOnQuantityChange = updatePriceOnQuantityChange;
    }

    public String getWidgetParentSelector() {
        return widgetParentSelector;
    }

    public void setWidgetParentSelector(String widgetParentSelector) {
        this.widgetParentSelector = widgetParentSelector;
    }

    public String getQuantitySelector() {
        return quantitySelector;
    }

    public void setQuantitySelector(String quantitySelector) {
        this.quantitySelector = quantitySelector;
    }

    public String getLoyaltyDetailsLabelText() {
        return loyaltyDetailsLabelText;
    }

    public void setLoyaltyDetailsLabelText(String loyaltyDetailsLabelText) {
        this.loyaltyDetailsLabelText = loyaltyDetailsLabelText;
    }

    public String getLoyaltyPerkDescriptionText() {
        return loyaltyPerkDescriptionText;
    }

    public void setLoyaltyPerkDescriptionText(String loyaltyPerkDescriptionText) {
        this.loyaltyPerkDescriptionText = loyaltyPerkDescriptionText;
    }

    public Boolean isDetectVariantFromURLParams() {
        return detectVariantFromURLParams;
    }

    public void setDetectVariantFromURLParams(Boolean detectVariantFromURLParams) {
        this.detectVariantFromURLParams = detectVariantFromURLParams;
    }

    public Boolean isDisableQueryParamsUpdate() {
        return disableQueryParamsUpdate;
    }

    public void setDisableQueryParamsUpdate(Boolean disableQueryParamsUpdate) {
        this.disableQueryParamsUpdate = disableQueryParamsUpdate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name,value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionWidgetSettingsDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionWidgetSettingsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionWidgetSettingsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", oneTimePurchaseText='" + getOneTimePurchaseText() + "'" +
            ", deliveryText='" + getDeliveryText() + "'" +
            ", purchaseOptionsText='" + getPurchaseOptionsText() + "'" +
            ", subscriptionOptionText='" + getSubscriptionOptionText() + "'" +
            ", sellingPlanSelectTitle='" + getSellingPlanSelectTitle() + "'" +
            ", tooltipTitle='" + getTooltipTitle() + "'" +
            ", tooltipDesctiption='" + getTooltipDesctiption() + "'" +
            ", subscriptionWidgetMarginTop='" + getSubscriptionWidgetMarginTop() + "'" +
            ", subscriptionWidgetMarginBottom='" + getSubscriptionWidgetMarginBottom() + "'" +
            ", subscriptionWrapperBorderWidth='" + getSubscriptionWrapperBorderWidth() + "'" +
            ", subscriptionWrapperBorderColor='" + getSubscriptionWrapperBorderColor() + "'" +
            ", circleBorderColor='" + getCircleBorderColor() + "'" +
            ", dotBackgroundColor='" + getDotBackgroundColor() + "'" +
            ", selectPaddingTop='" + getSelectPaddingTop() + "'" +
            ", selectPaddingBottom='" + getSelectPaddingBottom() + "'" +
            ", selectPaddingLeft='" + getSelectPaddingLeft() + "'" +
            ", selectPaddingRight='" + getSelectPaddingRight() + "'" +
            ", selectBorderWidth='" + getSelectBorderWidth() + "'" +
            ", selectBorderStyle='" + getSelectBorderStyle() + "'" +
            ", selectBorderColor='" + getSelectBorderColor() + "'" +
            ", selectBorderRadius='" + getSelectBorderRadius() + "'" +
            ", tooltipSubscriptionSvgFill='" + getTooltipSubscriptionSvgFill() + "'" +
            ", tooltipColor='" + getTooltipColor() + "'" +
            ", tooltipBackgroundColor='" + getTooltipBackgroundColor() + "'" +
            ", tooltipBorderTopColorBorderTopColor='" + getTooltipBorderTopColorBorderTopColor() + "'" +
            ", subscriptionFinalPriceColor='" + getSubscriptionFinalPriceColor() + "'" +
            ", subscriptionWidgetTextColor='" + getSubscriptionWidgetTextColor() + "'" +
            ", orderStatusManageSubscriptionTitle='" + getOrderStatusManageSubscriptionTitle() + "'" +
            ", orderStatusManageSubscriptionDescription='" + getOrderStatusManageSubscriptionDescription() + "'" +
            ", orderStatusManageSubscriptionButtonText='" + getOrderStatusManageSubscriptionButtonText() + "'" +
            ", showTooltipOnClick='" + isShowTooltipOnClick() + "'" +
            ", subscriptionOptionSelectedByDefault='" + isSubscriptionOptionSelectedByDefault() + "'" +
            ", widgetEnabled='" + isWidgetEnabled() + "'" +
            ", showTooltip='" + isShowTooltip() + "'" +
            ", sellingPlanTitleText='" + getSellingPlanTitleText() + "'" +
            ", oneTimePriceText='" + getOneTimePriceText() + "'" +
            ", selectedPayAsYouGoSellingPlanPriceText='" + getSelectedPayAsYouGoSellingPlanPriceText() + "'" +
            ", selectedPrepaidSellingPlanPriceText='" + getSelectedPrepaidSellingPlanPriceText() + "'" +
            ", selectedDiscountFormat='" + getSelectedDiscountFormat() + "'" +
            ", manageSubscriptionBtnFormat='" + getManageSubscriptionBtnFormat() + "'" +
            ", tooltipDescriptionOnPrepaidPlan='" + getTooltipDescriptionOnPrepaidPlan() + "'" +
            ", tooltipDescriptionOnMultipleDiscount='" + getTooltipDescriptionOnMultipleDiscount() + "'" +
            ", tooltipDescriptionCustomization='" + getTooltipDescriptionCustomization() + "'" +
            ", showStaticTooltip='" + isShowStaticTooltip() + "'" +
            ", showAppstleLink='" + isShowAppstleLink() + "'" +
            ", subscriptionPriceDisplayText='" + getSubscriptionPriceDisplayText() + "'" +
            ", sortByDefaultSequence='" + isSortByDefaultSequence() + "'" +
            ", showSubOptionBeforeOneTime='" + isShowSubOptionBeforeOneTime() + "'" +
            ", showCheckoutSubscriptionBtn='" + isShowCheckoutSubscriptionBtn() + "'" +
            ", totalPricePerDeliveryText='" + getTotalPricePerDeliveryText() + "'" +
            ", widgetEnabledOnSoldVariant='" + isWidgetEnabledOnSoldVariant() + "'" +
            ", enableCartWidgetFeature='" + isEnableCartWidgetFeature() + "'" +
            ", switchRadioButtonWidget='" + isSwitchRadioButtonWidget() + "'" +
            ", formMappingAttributeName='" + getFormMappingAttributeName() + "'" +
            ", formMappingAttributeSelector='" + getFormMappingAttributeSelector() + "'" +
            ", quickViewModalPollingSelector='" + getQuickViewModalPollingSelector() + "'" +
            ", updatePriceOnQuantityChange='" + getUpdatePriceOnQuantityChange() + "'" +
            ", widgetParentSelector='" + getWidgetParentSelector() + "'" +
            ", quantitySelector='" + getQuantitySelector() + "'" +
            ", loyaltyDetailsLabelText='" + getLoyaltyDetailsLabelText() + "'" +
            ", loyaltyPerkDescriptionText='" + getLoyaltyPerkDescriptionText() + "'" +
            ", detectVariantFromURLParams='" + isDetectVariantFromURLParams() + "'" +
            ", disableQueryParamsUpdate='" + isDisableQueryParamsUpdate() + "'" +
            "}";
    }
}
