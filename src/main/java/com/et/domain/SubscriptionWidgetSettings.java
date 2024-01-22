package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SubscriptionWidgetSettings.
 */
@Entity
@Table(name = "subscription_widget_settings")
public class SubscriptionWidgetSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "one_time_purchase_text")
    private String oneTimePurchaseText;

    @Column(name = "delivery_text")
    private String deliveryText;

    @Column(name = "purchase_options_text")
    private String purchaseOptionsText;

    @Column(name = "subscription_option_text")
    private String subscriptionOptionText;

    @Column(name = "selling_plan_select_title")
    private String sellingPlanSelectTitle;

    @Column(name = "tooltip_title")
    private String tooltipTitle;

    @Lob
    @Column(name = "tooltip_desctiption")
    private String tooltipDesctiption;

    @Column(name = "subscription_widget_margin_top")
    private String subscriptionWidgetMarginTop;

    @Column(name = "subscription_widget_margin_bottom")
    private String subscriptionWidgetMarginBottom;

    @Column(name = "subscription_wrapper_border_width")
    private String subscriptionWrapperBorderWidth;

    @Column(name = "subscription_wrapper_border_color")
    private String subscriptionWrapperBorderColor;

    @Column(name = "circle_border_color")
    private String circleBorderColor;

    @Column(name = "dot_background_color")
    private String dotBackgroundColor;

    @Column(name = "select_padding_top")
    private String selectPaddingTop;

    @Column(name = "select_padding_bottom")
    private String selectPaddingBottom;

    @Column(name = "select_padding_left")
    private String selectPaddingLeft;

    @Column(name = "select_padding_right")
    private String selectPaddingRight;

    @Column(name = "select_border_width")
    private String selectBorderWidth;

    @Column(name = "select_border_style")
    private String selectBorderStyle;

    @Column(name = "select_border_color")
    private String selectBorderColor;

    @Column(name = "select_border_radius")
    private String selectBorderRadius;

    @Column(name = "tooltip_subscription_svg_fill")
    private String tooltipSubscriptionSvgFill;

    @Column(name = "tooltip_color")
    private String tooltipColor;

    @Column(name = "tooltip_background_color")
    private String tooltipBackgroundColor;

    @Column(name = "tooltip_border_top_color_border_top_color")
    private String tooltipBorderTopColorBorderTopColor;

    @Column(name = "subscription_final_price_color")
    private String subscriptionFinalPriceColor;

    @Column(name = "subscription_widget_text_color")
    private String subscriptionWidgetTextColor;

    @Column(name = "order_status_manage_subscription_title")
    private String orderStatusManageSubscriptionTitle;

    @Column(name = "order_status_manage_subscription_description")
    private String orderStatusManageSubscriptionDescription;

    @Column(name = "order_status_manage_subscription_button_text")
    private String orderStatusManageSubscriptionButtonText;

    @Column(name = "show_tooltip_on_click")
    private Boolean showTooltipOnClick;

    @Column(name = "subscription_option_selected_by_default")
    private Boolean subscriptionOptionSelectedByDefault;

    @Column(name = "widget_enabled")
    private Boolean widgetEnabled;

    @Column(name = "show_tooltip")
    private Boolean showTooltip;

    @Column(name = "selling_plan_title_text")
    private String sellingPlanTitleText;

    @Column(name = "one_time_price_text")
    private String oneTimePriceText;

    @Column(name = "selected_pay_as_you_go_selling_plan_price_text")
    private String selectedPayAsYouGoSellingPlanPriceText;

    @Column(name = "selected_prepaid_selling_plan_price_text")
    private String selectedPrepaidSellingPlanPriceText;

    @Column(name = "selected_discount_format")
    private String selectedDiscountFormat;

    @Column(name = "manage_subscription_btn_format")
    private String manageSubscriptionBtnFormat;

    @Lob
    @Column(name = "tooltip_description_on_prepaid_plan")
    private String tooltipDescriptionOnPrepaidPlan;

    @Lob
    @Column(name = "tooltip_description_on_multiple_discount")
    private String tooltipDescriptionOnMultipleDiscount;

    @Lob
    @Column(name = "tooltip_description_customization")
    private String tooltipDescriptionCustomization;

    @Column(name = "show_static_tooltip")
    private Boolean showStaticTooltip;

    @Column(name = "show_appstle_link")
    private Boolean showAppstleLink;

    @Lob
    @Column(name = "subscription_price_display_text")
    private String subscriptionPriceDisplayText;

    @Column(name = "sort_by_default_sequence")
    private Boolean sortByDefaultSequence;

    @Column(name = "show_sub_option_before_one_time")
    private Boolean showSubOptionBeforeOneTime;

    @Column(name = "show_checkout_subscription_btn")
    private Boolean showCheckoutSubscriptionBtn;

    @Column(name = "total_price_per_delivery_text")
    private String totalPricePerDeliveryText;

    @Column(name = "widget_enabled_on_sold_variant")
    private Boolean widgetEnabledOnSoldVariant;

    @Column(name = "enable_cart_widget_feature")
    private Boolean enableCartWidgetFeature;

    @Column(name = "switch_radio_button_widget")
    private Boolean switchRadioButtonWidget;

    @Column(name = "form_mapping_attribute_name")
    private String formMappingAttributeName;

    @Column(name = "form_mapping_attribute_selector")
    private String formMappingAttributeSelector;

    @Column(name = "quick_view_modal_polling_selector")
    private String quickViewModalPollingSelector;

    @Column(name = "update_price_on_quantity_change")
    private String updatePriceOnQuantityChange;

    @Column(name = "widget_parent_selector")
    private String widgetParentSelector;

    @Column(name = "quantity_selector")
    private String quantitySelector;

    @Column(name = "loyalty_details_label_text")
    private String loyaltyDetailsLabelText;

    @Lob
    @Column(name = "loyalty_perk_description_text")
    private String loyaltyPerkDescriptionText;

    @Column(name = "detect_variant_from_url_params")
    private Boolean detectVariantFromURLParams;

    @Column(name = "disable_query_params_update")
    private Boolean disableQueryParamsUpdate;

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

    public SubscriptionWidgetSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getOneTimePurchaseText() {
        return oneTimePurchaseText;
    }

    public SubscriptionWidgetSettings oneTimePurchaseText(String oneTimePurchaseText) {
        this.oneTimePurchaseText = oneTimePurchaseText;
        return this;
    }

    public void setOneTimePurchaseText(String oneTimePurchaseText) {
        this.oneTimePurchaseText = oneTimePurchaseText;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public SubscriptionWidgetSettings deliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
        return this;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }

    public String getPurchaseOptionsText() {
        return purchaseOptionsText;
    }

    public SubscriptionWidgetSettings purchaseOptionsText(String purchaseOptionsText) {
        this.purchaseOptionsText = purchaseOptionsText;
        return this;
    }

    public void setPurchaseOptionsText(String purchaseOptionsText) {
        this.purchaseOptionsText = purchaseOptionsText;
    }

    public String getSubscriptionOptionText() {
        return subscriptionOptionText;
    }

    public SubscriptionWidgetSettings subscriptionOptionText(String subscriptionOptionText) {
        this.subscriptionOptionText = subscriptionOptionText;
        return this;
    }

    public void setSubscriptionOptionText(String subscriptionOptionText) {
        this.subscriptionOptionText = subscriptionOptionText;
    }

    public String getSellingPlanSelectTitle() {
        return sellingPlanSelectTitle;
    }

    public SubscriptionWidgetSettings sellingPlanSelectTitle(String sellingPlanSelectTitle) {
        this.sellingPlanSelectTitle = sellingPlanSelectTitle;
        return this;
    }

    public void setSellingPlanSelectTitle(String sellingPlanSelectTitle) {
        this.sellingPlanSelectTitle = sellingPlanSelectTitle;
    }

    public String getTooltipTitle() {
        return tooltipTitle;
    }

    public SubscriptionWidgetSettings tooltipTitle(String tooltipTitle) {
        this.tooltipTitle = tooltipTitle;
        return this;
    }

    public void setTooltipTitle(String tooltipTitle) {
        this.tooltipTitle = tooltipTitle;
    }

    public String getTooltipDesctiption() {
        return tooltipDesctiption;
    }

    public SubscriptionWidgetSettings tooltipDesctiption(String tooltipDesctiption) {
        this.tooltipDesctiption = tooltipDesctiption;
        return this;
    }

    public void setTooltipDesctiption(String tooltipDesctiption) {
        this.tooltipDesctiption = tooltipDesctiption;
    }

    public String getSubscriptionWidgetMarginTop() {
        return subscriptionWidgetMarginTop;
    }

    public SubscriptionWidgetSettings subscriptionWidgetMarginTop(String subscriptionWidgetMarginTop) {
        this.subscriptionWidgetMarginTop = subscriptionWidgetMarginTop;
        return this;
    }

    public void setSubscriptionWidgetMarginTop(String subscriptionWidgetMarginTop) {
        this.subscriptionWidgetMarginTop = subscriptionWidgetMarginTop;
    }

    public String getSubscriptionWidgetMarginBottom() {
        return subscriptionWidgetMarginBottom;
    }

    public SubscriptionWidgetSettings subscriptionWidgetMarginBottom(String subscriptionWidgetMarginBottom) {
        this.subscriptionWidgetMarginBottom = subscriptionWidgetMarginBottom;
        return this;
    }

    public void setSubscriptionWidgetMarginBottom(String subscriptionWidgetMarginBottom) {
        this.subscriptionWidgetMarginBottom = subscriptionWidgetMarginBottom;
    }

    public String getSubscriptionWrapperBorderWidth() {
        return subscriptionWrapperBorderWidth;
    }

    public SubscriptionWidgetSettings subscriptionWrapperBorderWidth(String subscriptionWrapperBorderWidth) {
        this.subscriptionWrapperBorderWidth = subscriptionWrapperBorderWidth;
        return this;
    }

    public void setSubscriptionWrapperBorderWidth(String subscriptionWrapperBorderWidth) {
        this.subscriptionWrapperBorderWidth = subscriptionWrapperBorderWidth;
    }

    public String getSubscriptionWrapperBorderColor() {
        return subscriptionWrapperBorderColor;
    }

    public SubscriptionWidgetSettings subscriptionWrapperBorderColor(String subscriptionWrapperBorderColor) {
        this.subscriptionWrapperBorderColor = subscriptionWrapperBorderColor;
        return this;
    }

    public void setSubscriptionWrapperBorderColor(String subscriptionWrapperBorderColor) {
        this.subscriptionWrapperBorderColor = subscriptionWrapperBorderColor;
    }

    public String getCircleBorderColor() {
        return circleBorderColor;
    }

    public SubscriptionWidgetSettings circleBorderColor(String circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
        return this;
    }

    public void setCircleBorderColor(String circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    public String getDotBackgroundColor() {
        return dotBackgroundColor;
    }

    public SubscriptionWidgetSettings dotBackgroundColor(String dotBackgroundColor) {
        this.dotBackgroundColor = dotBackgroundColor;
        return this;
    }

    public void setDotBackgroundColor(String dotBackgroundColor) {
        this.dotBackgroundColor = dotBackgroundColor;
    }

    public String getSelectPaddingTop() {
        return selectPaddingTop;
    }

    public SubscriptionWidgetSettings selectPaddingTop(String selectPaddingTop) {
        this.selectPaddingTop = selectPaddingTop;
        return this;
    }

    public void setSelectPaddingTop(String selectPaddingTop) {
        this.selectPaddingTop = selectPaddingTop;
    }

    public String getSelectPaddingBottom() {
        return selectPaddingBottom;
    }

    public SubscriptionWidgetSettings selectPaddingBottom(String selectPaddingBottom) {
        this.selectPaddingBottom = selectPaddingBottom;
        return this;
    }

    public void setSelectPaddingBottom(String selectPaddingBottom) {
        this.selectPaddingBottom = selectPaddingBottom;
    }

    public String getSelectPaddingLeft() {
        return selectPaddingLeft;
    }

    public SubscriptionWidgetSettings selectPaddingLeft(String selectPaddingLeft) {
        this.selectPaddingLeft = selectPaddingLeft;
        return this;
    }

    public void setSelectPaddingLeft(String selectPaddingLeft) {
        this.selectPaddingLeft = selectPaddingLeft;
    }

    public String getSelectPaddingRight() {
        return selectPaddingRight;
    }

    public SubscriptionWidgetSettings selectPaddingRight(String selectPaddingRight) {
        this.selectPaddingRight = selectPaddingRight;
        return this;
    }

    public void setSelectPaddingRight(String selectPaddingRight) {
        this.selectPaddingRight = selectPaddingRight;
    }

    public String getSelectBorderWidth() {
        return selectBorderWidth;
    }

    public SubscriptionWidgetSettings selectBorderWidth(String selectBorderWidth) {
        this.selectBorderWidth = selectBorderWidth;
        return this;
    }

    public void setSelectBorderWidth(String selectBorderWidth) {
        this.selectBorderWidth = selectBorderWidth;
    }

    public String getSelectBorderStyle() {
        return selectBorderStyle;
    }

    public SubscriptionWidgetSettings selectBorderStyle(String selectBorderStyle) {
        this.selectBorderStyle = selectBorderStyle;
        return this;
    }

    public void setSelectBorderStyle(String selectBorderStyle) {
        this.selectBorderStyle = selectBorderStyle;
    }

    public String getSelectBorderColor() {
        return selectBorderColor;
    }

    public SubscriptionWidgetSettings selectBorderColor(String selectBorderColor) {
        this.selectBorderColor = selectBorderColor;
        return this;
    }

    public void setSelectBorderColor(String selectBorderColor) {
        this.selectBorderColor = selectBorderColor;
    }

    public String getSelectBorderRadius() {
        return selectBorderRadius;
    }

    public SubscriptionWidgetSettings selectBorderRadius(String selectBorderRadius) {
        this.selectBorderRadius = selectBorderRadius;
        return this;
    }

    public void setSelectBorderRadius(String selectBorderRadius) {
        this.selectBorderRadius = selectBorderRadius;
    }

    public String getTooltipSubscriptionSvgFill() {
        return tooltipSubscriptionSvgFill;
    }

    public SubscriptionWidgetSettings tooltipSubscriptionSvgFill(String tooltipSubscriptionSvgFill) {
        this.tooltipSubscriptionSvgFill = tooltipSubscriptionSvgFill;
        return this;
    }

    public void setTooltipSubscriptionSvgFill(String tooltipSubscriptionSvgFill) {
        this.tooltipSubscriptionSvgFill = tooltipSubscriptionSvgFill;
    }

    public String getTooltipColor() {
        return tooltipColor;
    }

    public SubscriptionWidgetSettings tooltipColor(String tooltipColor) {
        this.tooltipColor = tooltipColor;
        return this;
    }

    public void setTooltipColor(String tooltipColor) {
        this.tooltipColor = tooltipColor;
    }

    public String getTooltipBackgroundColor() {
        return tooltipBackgroundColor;
    }

    public SubscriptionWidgetSettings tooltipBackgroundColor(String tooltipBackgroundColor) {
        this.tooltipBackgroundColor = tooltipBackgroundColor;
        return this;
    }

    public void setTooltipBackgroundColor(String tooltipBackgroundColor) {
        this.tooltipBackgroundColor = tooltipBackgroundColor;
    }

    public String getTooltipBorderTopColorBorderTopColor() {
        return tooltipBorderTopColorBorderTopColor;
    }

    public SubscriptionWidgetSettings tooltipBorderTopColorBorderTopColor(String tooltipBorderTopColorBorderTopColor) {
        this.tooltipBorderTopColorBorderTopColor = tooltipBorderTopColorBorderTopColor;
        return this;
    }

    public void setTooltipBorderTopColorBorderTopColor(String tooltipBorderTopColorBorderTopColor) {
        this.tooltipBorderTopColorBorderTopColor = tooltipBorderTopColorBorderTopColor;
    }

    public String getSubscriptionFinalPriceColor() {
        return subscriptionFinalPriceColor;
    }

    public SubscriptionWidgetSettings subscriptionFinalPriceColor(String subscriptionFinalPriceColor) {
        this.subscriptionFinalPriceColor = subscriptionFinalPriceColor;
        return this;
    }

    public void setSubscriptionFinalPriceColor(String subscriptionFinalPriceColor) {
        this.subscriptionFinalPriceColor = subscriptionFinalPriceColor;
    }

    public String getSubscriptionWidgetTextColor() {
        return subscriptionWidgetTextColor;
    }

    public SubscriptionWidgetSettings subscriptionWidgetTextColor(String subscriptionWidgetTextColor) {
        this.subscriptionWidgetTextColor = subscriptionWidgetTextColor;
        return this;
    }

    public void setSubscriptionWidgetTextColor(String subscriptionWidgetTextColor) {
        this.subscriptionWidgetTextColor = subscriptionWidgetTextColor;
    }

    public String getOrderStatusManageSubscriptionTitle() {
        return orderStatusManageSubscriptionTitle;
    }

    public SubscriptionWidgetSettings orderStatusManageSubscriptionTitle(String orderStatusManageSubscriptionTitle) {
        this.orderStatusManageSubscriptionTitle = orderStatusManageSubscriptionTitle;
        return this;
    }

    public void setOrderStatusManageSubscriptionTitle(String orderStatusManageSubscriptionTitle) {
        this.orderStatusManageSubscriptionTitle = orderStatusManageSubscriptionTitle;
    }

    public String getOrderStatusManageSubscriptionDescription() {
        return orderStatusManageSubscriptionDescription;
    }

    public SubscriptionWidgetSettings orderStatusManageSubscriptionDescription(String orderStatusManageSubscriptionDescription) {
        this.orderStatusManageSubscriptionDescription = orderStatusManageSubscriptionDescription;
        return this;
    }

    public void setOrderStatusManageSubscriptionDescription(String orderStatusManageSubscriptionDescription) {
        this.orderStatusManageSubscriptionDescription = orderStatusManageSubscriptionDescription;
    }

    public String getOrderStatusManageSubscriptionButtonText() {
        return orderStatusManageSubscriptionButtonText;
    }

    public SubscriptionWidgetSettings orderStatusManageSubscriptionButtonText(String orderStatusManageSubscriptionButtonText) {
        this.orderStatusManageSubscriptionButtonText = orderStatusManageSubscriptionButtonText;
        return this;
    }

    public void setOrderStatusManageSubscriptionButtonText(String orderStatusManageSubscriptionButtonText) {
        this.orderStatusManageSubscriptionButtonText = orderStatusManageSubscriptionButtonText;
    }

    public Boolean isShowTooltipOnClick() {
        return showTooltipOnClick;
    }

    public SubscriptionWidgetSettings showTooltipOnClick(Boolean showTooltipOnClick) {
        this.showTooltipOnClick = showTooltipOnClick;
        return this;
    }

    public void setShowTooltipOnClick(Boolean showTooltipOnClick) {
        this.showTooltipOnClick = showTooltipOnClick;
    }

    public Boolean isSubscriptionOptionSelectedByDefault() {
        return subscriptionOptionSelectedByDefault;
    }

    public SubscriptionWidgetSettings subscriptionOptionSelectedByDefault(Boolean subscriptionOptionSelectedByDefault) {
        this.subscriptionOptionSelectedByDefault = subscriptionOptionSelectedByDefault;
        return this;
    }

    public void setSubscriptionOptionSelectedByDefault(Boolean subscriptionOptionSelectedByDefault) {
        this.subscriptionOptionSelectedByDefault = subscriptionOptionSelectedByDefault;
    }

    public Boolean isWidgetEnabled() {
        return widgetEnabled;
    }

    public SubscriptionWidgetSettings widgetEnabled(Boolean widgetEnabled) {
        this.widgetEnabled = widgetEnabled;
        return this;
    }

    public void setWidgetEnabled(Boolean widgetEnabled) {
        this.widgetEnabled = widgetEnabled;
    }

    public Boolean isShowTooltip() {
        return showTooltip;
    }

    public SubscriptionWidgetSettings showTooltip(Boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    public void setShowTooltip(Boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    public String getSellingPlanTitleText() {
        return sellingPlanTitleText;
    }

    public SubscriptionWidgetSettings sellingPlanTitleText(String sellingPlanTitleText) {
        this.sellingPlanTitleText = sellingPlanTitleText;
        return this;
    }

    public void setSellingPlanTitleText(String sellingPlanTitleText) {
        this.sellingPlanTitleText = sellingPlanTitleText;
    }

    public String getOneTimePriceText() {
        return oneTimePriceText;
    }

    public SubscriptionWidgetSettings oneTimePriceText(String oneTimePriceText) {
        this.oneTimePriceText = oneTimePriceText;
        return this;
    }

    public void setOneTimePriceText(String oneTimePriceText) {
        this.oneTimePriceText = oneTimePriceText;
    }

    public String getSelectedPayAsYouGoSellingPlanPriceText() {
        return selectedPayAsYouGoSellingPlanPriceText;
    }

    public SubscriptionWidgetSettings selectedPayAsYouGoSellingPlanPriceText(String selectedPayAsYouGoSellingPlanPriceText) {
        this.selectedPayAsYouGoSellingPlanPriceText = selectedPayAsYouGoSellingPlanPriceText;
        return this;
    }

    public void setSelectedPayAsYouGoSellingPlanPriceText(String selectedPayAsYouGoSellingPlanPriceText) {
        this.selectedPayAsYouGoSellingPlanPriceText = selectedPayAsYouGoSellingPlanPriceText;
    }

    public String getSelectedPrepaidSellingPlanPriceText() {
        return selectedPrepaidSellingPlanPriceText;
    }

    public SubscriptionWidgetSettings selectedPrepaidSellingPlanPriceText(String selectedPrepaidSellingPlanPriceText) {
        this.selectedPrepaidSellingPlanPriceText = selectedPrepaidSellingPlanPriceText;
        return this;
    }

    public void setSelectedPrepaidSellingPlanPriceText(String selectedPrepaidSellingPlanPriceText) {
        this.selectedPrepaidSellingPlanPriceText = selectedPrepaidSellingPlanPriceText;
    }

    public String getSelectedDiscountFormat() {
        return selectedDiscountFormat;
    }

    public SubscriptionWidgetSettings selectedDiscountFormat(String selectedDiscountFormat) {
        this.selectedDiscountFormat = selectedDiscountFormat;
        return this;
    }

    public void setSelectedDiscountFormat(String selectedDiscountFormat) {
        this.selectedDiscountFormat = selectedDiscountFormat;
    }

    public String getManageSubscriptionBtnFormat() {
        return manageSubscriptionBtnFormat;
    }

    public SubscriptionWidgetSettings manageSubscriptionBtnFormat(String manageSubscriptionBtnFormat) {
        this.manageSubscriptionBtnFormat = manageSubscriptionBtnFormat;
        return this;
    }

    public void setManageSubscriptionBtnFormat(String manageSubscriptionBtnFormat) {
        this.manageSubscriptionBtnFormat = manageSubscriptionBtnFormat;
    }

    public String getTooltipDescriptionOnPrepaidPlan() {
        return tooltipDescriptionOnPrepaidPlan;
    }

    public SubscriptionWidgetSettings tooltipDescriptionOnPrepaidPlan(String tooltipDescriptionOnPrepaidPlan) {
        this.tooltipDescriptionOnPrepaidPlan = tooltipDescriptionOnPrepaidPlan;
        return this;
    }

    public void setTooltipDescriptionOnPrepaidPlan(String tooltipDescriptionOnPrepaidPlan) {
        this.tooltipDescriptionOnPrepaidPlan = tooltipDescriptionOnPrepaidPlan;
    }

    public String getTooltipDescriptionOnMultipleDiscount() {
        return tooltipDescriptionOnMultipleDiscount;
    }

    public SubscriptionWidgetSettings tooltipDescriptionOnMultipleDiscount(String tooltipDescriptionOnMultipleDiscount) {
        this.tooltipDescriptionOnMultipleDiscount = tooltipDescriptionOnMultipleDiscount;
        return this;
    }

    public void setTooltipDescriptionOnMultipleDiscount(String tooltipDescriptionOnMultipleDiscount) {
        this.tooltipDescriptionOnMultipleDiscount = tooltipDescriptionOnMultipleDiscount;
    }

    public String getTooltipDescriptionCustomization() {
        return tooltipDescriptionCustomization;
    }

    public SubscriptionWidgetSettings tooltipDescriptionCustomization(String tooltipDescriptionCustomization) {
        this.tooltipDescriptionCustomization = tooltipDescriptionCustomization;
        return this;
    }

    public void setTooltipDescriptionCustomization(String tooltipDescriptionCustomization) {
        this.tooltipDescriptionCustomization = tooltipDescriptionCustomization;
    }

    public Boolean isShowStaticTooltip() {
        return showStaticTooltip;
    }

    public SubscriptionWidgetSettings showStaticTooltip(Boolean showStaticTooltip) {
        this.showStaticTooltip = showStaticTooltip;
        return this;
    }

    public void setShowStaticTooltip(Boolean showStaticTooltip) {
        this.showStaticTooltip = showStaticTooltip;
    }

    public Boolean isShowAppstleLink() {
        return showAppstleLink;
    }

    public SubscriptionWidgetSettings showAppstleLink(Boolean showAppstleLink) {
        this.showAppstleLink = showAppstleLink;
        return this;
    }

    public void setShowAppstleLink(Boolean showAppstleLink) {
        this.showAppstleLink = showAppstleLink;
    }

    public String getSubscriptionPriceDisplayText() {
        return subscriptionPriceDisplayText;
    }

    public SubscriptionWidgetSettings subscriptionPriceDisplayText(String subscriptionPriceDisplayText) {
        this.subscriptionPriceDisplayText = subscriptionPriceDisplayText;
        return this;
    }

    public void setSubscriptionPriceDisplayText(String subscriptionPriceDisplayText) {
        this.subscriptionPriceDisplayText = subscriptionPriceDisplayText;
    }

    public Boolean isSortByDefaultSequence() {
        return sortByDefaultSequence;
    }

    public SubscriptionWidgetSettings sortByDefaultSequence(Boolean sortByDefaultSequence) {
        this.sortByDefaultSequence = sortByDefaultSequence;
        return this;
    }

    public void setSortByDefaultSequence(Boolean sortByDefaultSequence) {
        this.sortByDefaultSequence = sortByDefaultSequence;
    }

    public Boolean isShowSubOptionBeforeOneTime() {
        return showSubOptionBeforeOneTime;
    }

    public SubscriptionWidgetSettings showSubOptionBeforeOneTime(Boolean showSubOptionBeforeOneTime) {
        this.showSubOptionBeforeOneTime = showSubOptionBeforeOneTime;
        return this;
    }

    public void setShowSubOptionBeforeOneTime(Boolean showSubOptionBeforeOneTime) {
        this.showSubOptionBeforeOneTime = showSubOptionBeforeOneTime;
    }

    public Boolean isShowCheckoutSubscriptionBtn() {
        return showCheckoutSubscriptionBtn;
    }

    public SubscriptionWidgetSettings showCheckoutSubscriptionBtn(Boolean showCheckoutSubscriptionBtn) {
        this.showCheckoutSubscriptionBtn = showCheckoutSubscriptionBtn;
        return this;
    }

    public void setShowCheckoutSubscriptionBtn(Boolean showCheckoutSubscriptionBtn) {
        this.showCheckoutSubscriptionBtn = showCheckoutSubscriptionBtn;
    }

    public String getTotalPricePerDeliveryText() {
        return totalPricePerDeliveryText;
    }

    public SubscriptionWidgetSettings totalPricePerDeliveryText(String totalPricePerDeliveryText) {
        this.totalPricePerDeliveryText = totalPricePerDeliveryText;
        return this;
    }

    public void setTotalPricePerDeliveryText(String totalPricePerDeliveryText) {
        this.totalPricePerDeliveryText = totalPricePerDeliveryText;
    }

    public Boolean isWidgetEnabledOnSoldVariant() {
        return widgetEnabledOnSoldVariant;
    }

    public SubscriptionWidgetSettings widgetEnabledOnSoldVariant(Boolean widgetEnabledOnSoldVariant) {
        this.widgetEnabledOnSoldVariant = widgetEnabledOnSoldVariant;
        return this;
    }

    public void setWidgetEnabledOnSoldVariant(Boolean widgetEnabledOnSoldVariant) {
        this.widgetEnabledOnSoldVariant = widgetEnabledOnSoldVariant;
    }

    public Boolean isEnableCartWidgetFeature() {
        return enableCartWidgetFeature;
    }

    public SubscriptionWidgetSettings enableCartWidgetFeature(Boolean enableCartWidgetFeature) {
        this.enableCartWidgetFeature = enableCartWidgetFeature;
        return this;
    }

    public void setEnableCartWidgetFeature(Boolean enableCartWidgetFeature) {
        this.enableCartWidgetFeature = enableCartWidgetFeature;
    }

    public Boolean isSwitchRadioButtonWidget() {
        return switchRadioButtonWidget;
    }

    public SubscriptionWidgetSettings switchRadioButtonWidget(Boolean switchRadioButtonWidget) {
        this.switchRadioButtonWidget = switchRadioButtonWidget;
        return this;
    }

    public void setSwitchRadioButtonWidget(Boolean switchRadioButtonWidget) {
        this.switchRadioButtonWidget = switchRadioButtonWidget;
    }

    public String getFormMappingAttributeName() {
        return formMappingAttributeName;
    }

    public SubscriptionWidgetSettings formMappingAttributeName(String formMappingAttributeName) {
        this.formMappingAttributeName = formMappingAttributeName;
        return this;
    }

    public void setFormMappingAttributeName(String formMappingAttributeName) {
        this.formMappingAttributeName = formMappingAttributeName;
    }

    public String getFormMappingAttributeSelector() {
        return formMappingAttributeSelector;
    }

    public SubscriptionWidgetSettings formMappingAttributeSelector(String formMappingAttributeSelector) {
        this.formMappingAttributeSelector = formMappingAttributeSelector;
        return this;
    }

    public void setFormMappingAttributeSelector(String formMappingAttributeSelector) {
        this.formMappingAttributeSelector = formMappingAttributeSelector;
    }

    public String getQuickViewModalPollingSelector() {
        return quickViewModalPollingSelector;
    }

    public SubscriptionWidgetSettings quickViewModalPollingSelector(String quickViewModalPollingSelector) {
        this.quickViewModalPollingSelector = quickViewModalPollingSelector;
        return this;
    }

    public void setQuickViewModalPollingSelector(String quickViewModalPollingSelector) {
        this.quickViewModalPollingSelector = quickViewModalPollingSelector;
    }

    public String getUpdatePriceOnQuantityChange() {
        return updatePriceOnQuantityChange;
    }

    public SubscriptionWidgetSettings updatePriceOnQuantityChange(String updatePriceOnQuantityChange) {
        this.updatePriceOnQuantityChange = updatePriceOnQuantityChange;
        return this;
    }

    public void setUpdatePriceOnQuantityChange(String updatePriceOnQuantityChange) {
        this.updatePriceOnQuantityChange = updatePriceOnQuantityChange;
    }

    public String getWidgetParentSelector() {
        return widgetParentSelector;
    }

    public SubscriptionWidgetSettings widgetParentSelector(String widgetParentSelector) {
        this.widgetParentSelector = widgetParentSelector;
        return this;
    }

    public void setWidgetParentSelector(String widgetParentSelector) {
        this.widgetParentSelector = widgetParentSelector;
    }

    public String getQuantitySelector() {
        return quantitySelector;
    }

    public SubscriptionWidgetSettings quantitySelector(String quantitySelector) {
        this.quantitySelector = quantitySelector;
        return this;
    }

    public void setQuantitySelector(String quantitySelector) {
        this.quantitySelector = quantitySelector;
    }

    public String getLoyaltyDetailsLabelText() {
        return loyaltyDetailsLabelText;
    }

    public SubscriptionWidgetSettings loyaltyDetailsLabelText(String loyaltyDetailsLabelText) {
        this.loyaltyDetailsLabelText = loyaltyDetailsLabelText;
        return this;
    }

    public void setLoyaltyDetailsLabelText(String loyaltyDetailsLabelText) {
        this.loyaltyDetailsLabelText = loyaltyDetailsLabelText;
    }

    public String getLoyaltyPerkDescriptionText() {
        return loyaltyPerkDescriptionText;
    }

    public SubscriptionWidgetSettings loyaltyPerkDescriptionText(String loyaltyPerkDescriptionText) {
        this.loyaltyPerkDescriptionText = loyaltyPerkDescriptionText;
        return this;
    }

    public void setLoyaltyPerkDescriptionText(String loyaltyPerkDescriptionText) {
        this.loyaltyPerkDescriptionText = loyaltyPerkDescriptionText;
    }

    public Boolean isDetectVariantFromURLParams() {
        return detectVariantFromURLParams;
    }

    public SubscriptionWidgetSettings detectVariantFromURLParams(Boolean detectVariantFromURLParams) {
        this.detectVariantFromURLParams = detectVariantFromURLParams;
        return this;
    }

    public void setDetectVariantFromURLParams(Boolean detectVariantFromURLParams) {
        this.detectVariantFromURLParams = detectVariantFromURLParams;
    }

    public Boolean isDisableQueryParamsUpdate() {
        return disableQueryParamsUpdate;
    }

    public SubscriptionWidgetSettings disableQueryParamsUpdate(Boolean disableQueryParamsUpdate) {
        this.disableQueryParamsUpdate = disableQueryParamsUpdate;
        return this;
    }

    public void setDisableQueryParamsUpdate(Boolean disableQueryParamsUpdate) {
        this.disableQueryParamsUpdate = disableQueryParamsUpdate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionWidgetSettings)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionWidgetSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionWidgetSettings{" +
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
