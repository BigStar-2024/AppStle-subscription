package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.ProductSelectionOption;

/**
 * A CustomerPortalSettings.
 */
@Entity
@Table(name = "customer_portal_settings")
public class CustomerPortalSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "order_frequency_text", nullable = false)
    private String orderFrequencyText;

    @NotNull
    @Column(name = "total_products_text", nullable = false)
    private String totalProductsText;

    @NotNull
    @Column(name = "next_order_text", nullable = false)
    private String nextOrderText;

    @NotNull
    @Column(name = "status_text", nullable = false)
    private String statusText;

    @NotNull
    @Column(name = "cancel_subscription_btn_text", nullable = false)
    private String cancelSubscriptionBtnText;

    @NotNull
    @Column(name = "no_subscription_message", nullable = false)
    private String noSubscriptionMessage;

    @NotNull
    @Column(name = "subscription_no_text", nullable = false)
    private String subscriptionNoText;

    @NotNull
    @Column(name = "update_payment_message", nullable = false)
    private String updatePaymentMessage;

    @NotNull
    @Column(name = "card_last_four_digit_text", nullable = false)
    private String cardLastFourDigitText;

    @NotNull
    @Column(name = "card_expiry_text", nullable = false)
    private String cardExpiryText;

    @NotNull
    @Column(name = "card_holder_name_text", nullable = false)
    private String cardHolderNameText;

    @NotNull
    @Column(name = "card_type_text", nullable = false)
    private String cardTypeText;

    @NotNull
    @Column(name = "payment_method_type_text", nullable = false)
    private String paymentMethodTypeText;

    @NotNull
    @Column(name = "cancel_accordion_title", nullable = false)
    private String cancelAccordionTitle;

    @NotNull
    @Column(name = "payment_detail_accordion_title", nullable = false)
    private String paymentDetailAccordionTitle;

    @NotNull
    @Column(name = "upcoming_order_accordion_title", nullable = false)
    private String upcomingOrderAccordionTitle;

    @NotNull
    @Column(name = "payment_info_text", nullable = false)
    private String paymentInfoText;

    @NotNull
    @Column(name = "update_payment_btn_text", nullable = false)
    private String updatePaymentBtnText;

    @NotNull
    @Column(name = "next_order_date_lbl", nullable = false)
    private String nextOrderDateLbl;

    @NotNull
    @Column(name = "status_lbl", nullable = false)
    private String statusLbl;

    @NotNull
    @Column(name = "quantity_lbl", nullable = false)
    private String quantityLbl;

    @NotNull
    @Column(name = "amount_lbl", nullable = false)
    private String amountLbl;

    @NotNull
    @Column(name = "order_no_lbl", nullable = false)
    private String orderNoLbl;

    @NotNull
    @Column(name = "edit_frequency_btn_text", nullable = false)
    private String editFrequencyBtnText;

    @NotNull
    @Column(name = "cancel_freq_btn_text", nullable = false)
    private String cancelFreqBtnText;

    @NotNull
    @Column(name = "update_freq_btn_text", nullable = false)
    private String updateFreqBtnText;

    @Column(name = "pause_resume_sub")
    private Boolean pauseResumeSub;

    @Column(name = "change_next_order_date")
    private Boolean changeNextOrderDate;

    @Column(name = "cancel_sub")
    private Boolean cancelSub;

    @Column(name = "change_order_frequency")
    private Boolean changeOrderFrequency;

    @Column(name = "create_additional_order")
    private Boolean createAdditionalOrder;

    @Column(name = "manage_subscription_button_text")
    private String manageSubscriptionButtonText;

    @Column(name = "edit_change_order_btn_text")
    private String editChangeOrderBtnText;

    @Column(name = "cancel_change_order_btn_text")
    private String cancelChangeOrderBtnText;

    @Column(name = "update_change_order_btn_text")
    private String updateChangeOrderBtnText;

    @Column(name = "edit_product_button_text")
    private String editProductButtonText;

    @Column(name = "delete_button_text")
    private String deleteButtonText;

    @Column(name = "update_button_text")
    private String updateButtonText;

    @Column(name = "cancel_button_text")
    private String cancelButtonText;

    @Column(name = "add_product_button_text")
    private String addProductButtonText;

    @Column(name = "add_product_label_text")
    private String addProductLabelText;

    @Column(name = "active_badge_text")
    private String activeBadgeText;

    @Column(name = "close_badge_text")
    private String closeBadgeText;

    @Column(name = "skip_order_button_text")
    private String skipOrderButtonText;

    @Column(name = "product_label_text")
    private String productLabelText;

    @Column(name = "see_more_details_text")
    private String seeMoreDetailsText;

    @Column(name = "hide_details_text")
    private String hideDetailsText;

    @Column(name = "product_in_subscription_text")
    private String productInSubscriptionText;

    @Column(name = "edit_quantity_label_text")
    private String EditQuantityLabelText;

    @Column(name = "sub_total_label_text")
    private String subTotalLabelText;

    @Column(name = "payment_notification_text")
    private String paymentNotificationText;

    @Column(name = "edit_product_flag")
    private Boolean editProductFlag;

    @Column(name = "delete_product_flag")
    private Boolean deleteProductFlag;

    @Column(name = "show_shipment")
    private Boolean showShipment;

    @Column(name = "add_additional_product")
    private Boolean addAdditionalProduct;

    @Column(name = "success_text")
    private String successText;

    @Column(name = "cancel_subscription_confirm_prepaid_text")
    private String cancelSubscriptionConfirmPrepaidText;

    @Column(name = "cancel_subscription_confirm_pay_as_you_go_text")
    private String cancelSubscriptionConfirmPayAsYouGoText;

    @Column(name = "cancel_subscription_prepaid_button_text")
    private String cancelSubscriptionPrepaidButtonText;

    @Column(name = "cancel_subscription_pay_as_you_go_button_text")
    private String cancelSubscriptionPayAsYouGoButtonText;

    @Column(name = "upcoming_fulfillment_text")
    private String upcomingFulfillmentText;

    @Column(name = "credit_card_text")
    private String creditCardText;

    @Column(name = "ending_with_text")
    private String endingWithText;

    @Column(name = "week_text")
    private String weekText;

    @Column(name = "day_text")
    private String dayText;

    @Column(name = "month_text")
    private String monthText;

    @Column(name = "year_text")
    private String yearText;

    @Column(name = "skip_badge_text")
    private String skipBadgeText;

    @Column(name = "queue_badge_text")
    private String queueBadgeText;

    @Lob
    @Column(name = "customer_portal_setting_json")
    private String customerPortalSettingJson;

    @Column(name = "order_note_flag")
    private Boolean orderNoteFlag;

    @Column(name = "order_note_text")
    private String orderNoteText;

    @Column(name = "use_url_with_customer_id")
    private Boolean useUrlWithCustomerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_selection_option")
    private ProductSelectionOption productSelectionOption;

    @Column(name = "include_out_of_stock_product")
    private Boolean includeOutOfStockProduct;

    @Column(name = "open_badge_text")
    private String openBadgeText;

    @Column(name = "update_shipment_billing_date")
    private Boolean updateShipmentBillingDate;

    @Column(name = "discount_code")
    private Boolean discountCode;

    @Column(name = "freeze_order_till_min_cycle")
    private Boolean freezeOrderTillMinCycle;

    @Column(name = "add_one_time_product")
    private Boolean addOneTimeProduct;

    @Column(name = "allow_order_now")
    private Boolean allowOrderNow;

    @Column(name = "min_qty_to_allow_during_add_product")
    private Integer minQtyToAllowDuringAddProduct;

    @Column(name = "allow_split_contract")
    private Boolean allowSplitContract;

    @Column(name = "enable_edit_attributes")
    private Boolean enableEditAttributes;

    @Column(name = "enable_swap_product_variant")
    private Boolean enableSwapProductVariant;

    @Column(name = "enable_redirect_my_account_button")
    private Boolean enableRedirectMyAccountButton;

    @Column(name = "enable_allow_only_one_discount_code")
    private Boolean enableAllowOnlyOneDiscountCode;

    @Column(name = "enable_redirect_to_product_page")
    private Boolean enableRedirectToProductPage;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Boolean isEnableRedirectMyAccountButton() {
        return enableRedirectMyAccountButton;
    }

    public CustomerPortalSettings enableRedirectMyAccountButton(Boolean enableRedirectMyAccountButton) {
        this.enableRedirectMyAccountButton = enableRedirectMyAccountButton;
        return this;
    }

    public void setEnableRedirectMyAccountButton(Boolean enableRedirectMyAccountButton) {
        this.enableRedirectMyAccountButton = enableRedirectMyAccountButton;
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

    public CustomerPortalSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getOrderFrequencyText() {
        return orderFrequencyText;
    }

    public CustomerPortalSettings orderFrequencyText(String orderFrequencyText) {
        this.orderFrequencyText = orderFrequencyText;
        return this;
    }

    public void setOrderFrequencyText(String orderFrequencyText) {
        this.orderFrequencyText = orderFrequencyText;
    }

    public String getTotalProductsText() {
        return totalProductsText;
    }

    public CustomerPortalSettings totalProductsText(String totalProductsText) {
        this.totalProductsText = totalProductsText;
        return this;
    }

    public void setTotalProductsText(String totalProductsText) {
        this.totalProductsText = totalProductsText;
    }

    public String getNextOrderText() {
        return nextOrderText;
    }

    public CustomerPortalSettings nextOrderText(String nextOrderText) {
        this.nextOrderText = nextOrderText;
        return this;
    }

    public void setNextOrderText(String nextOrderText) {
        this.nextOrderText = nextOrderText;
    }

    public String getStatusText() {
        return statusText;
    }

    public CustomerPortalSettings statusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCancelSubscriptionBtnText() {
        return cancelSubscriptionBtnText;
    }

    public CustomerPortalSettings cancelSubscriptionBtnText(String cancelSubscriptionBtnText) {
        this.cancelSubscriptionBtnText = cancelSubscriptionBtnText;
        return this;
    }

    public void setCancelSubscriptionBtnText(String cancelSubscriptionBtnText) {
        this.cancelSubscriptionBtnText = cancelSubscriptionBtnText;
    }

    public String getNoSubscriptionMessage() {
        return noSubscriptionMessage;
    }

    public CustomerPortalSettings noSubscriptionMessage(String noSubscriptionMessage) {
        this.noSubscriptionMessage = noSubscriptionMessage;
        return this;
    }

    public void setNoSubscriptionMessage(String noSubscriptionMessage) {
        this.noSubscriptionMessage = noSubscriptionMessage;
    }

    public String getSubscriptionNoText() {
        return subscriptionNoText;
    }

    public CustomerPortalSettings subscriptionNoText(String subscriptionNoText) {
        this.subscriptionNoText = subscriptionNoText;
        return this;
    }

    public void setSubscriptionNoText(String subscriptionNoText) {
        this.subscriptionNoText = subscriptionNoText;
    }

    public String getUpdatePaymentMessage() {
        return updatePaymentMessage;
    }

    public CustomerPortalSettings updatePaymentMessage(String updatePaymentMessage) {
        this.updatePaymentMessage = updatePaymentMessage;
        return this;
    }

    public void setUpdatePaymentMessage(String updatePaymentMessage) {
        this.updatePaymentMessage = updatePaymentMessage;
    }

    public String getCardLastFourDigitText() {
        return cardLastFourDigitText;
    }

    public CustomerPortalSettings cardLastFourDigitText(String cardLastFourDigitText) {
        this.cardLastFourDigitText = cardLastFourDigitText;
        return this;
    }

    public void setCardLastFourDigitText(String cardLastFourDigitText) {
        this.cardLastFourDigitText = cardLastFourDigitText;
    }

    public String getCardExpiryText() {
        return cardExpiryText;
    }

    public CustomerPortalSettings cardExpiryText(String cardExpiryText) {
        this.cardExpiryText = cardExpiryText;
        return this;
    }

    public void setCardExpiryText(String cardExpiryText) {
        this.cardExpiryText = cardExpiryText;
    }

    public String getCardHolderNameText() {
        return cardHolderNameText;
    }

    public CustomerPortalSettings cardHolderNameText(String cardHolderNameText) {
        this.cardHolderNameText = cardHolderNameText;
        return this;
    }

    public void setCardHolderNameText(String cardHolderNameText) {
        this.cardHolderNameText = cardHolderNameText;
    }

    public String getCardTypeText() {
        return cardTypeText;
    }

    public CustomerPortalSettings cardTypeText(String cardTypeText) {
        this.cardTypeText = cardTypeText;
        return this;
    }

    public void setCardTypeText(String cardTypeText) {
        this.cardTypeText = cardTypeText;
    }

    public String getPaymentMethodTypeText() {
        return paymentMethodTypeText;
    }

    public CustomerPortalSettings paymentMethodTypeText(String paymentMethodTypeText) {
        this.paymentMethodTypeText = paymentMethodTypeText;
        return this;
    }

    public void setPaymentMethodTypeText(String paymentMethodTypeText) {
        this.paymentMethodTypeText = paymentMethodTypeText;
    }

    public String getCancelAccordionTitle() {
        return cancelAccordionTitle;
    }

    public CustomerPortalSettings cancelAccordionTitle(String cancelAccordionTitle) {
        this.cancelAccordionTitle = cancelAccordionTitle;
        return this;
    }

    public void setCancelAccordionTitle(String cancelAccordionTitle) {
        this.cancelAccordionTitle = cancelAccordionTitle;
    }

    public String getPaymentDetailAccordionTitle() {
        return paymentDetailAccordionTitle;
    }

    public CustomerPortalSettings paymentDetailAccordionTitle(String paymentDetailAccordionTitle) {
        this.paymentDetailAccordionTitle = paymentDetailAccordionTitle;
        return this;
    }

    public void setPaymentDetailAccordionTitle(String paymentDetailAccordionTitle) {
        this.paymentDetailAccordionTitle = paymentDetailAccordionTitle;
    }

    public String getUpcomingOrderAccordionTitle() {
        return upcomingOrderAccordionTitle;
    }

    public CustomerPortalSettings upcomingOrderAccordionTitle(String upcomingOrderAccordionTitle) {
        this.upcomingOrderAccordionTitle = upcomingOrderAccordionTitle;
        return this;
    }

    public void setUpcomingOrderAccordionTitle(String upcomingOrderAccordionTitle) {
        this.upcomingOrderAccordionTitle = upcomingOrderAccordionTitle;
    }

    public String getPaymentInfoText() {
        return paymentInfoText;
    }

    public CustomerPortalSettings paymentInfoText(String paymentInfoText) {
        this.paymentInfoText = paymentInfoText;
        return this;
    }

    public void setPaymentInfoText(String paymentInfoText) {
        this.paymentInfoText = paymentInfoText;
    }

    public String getUpdatePaymentBtnText() {
        return updatePaymentBtnText;
    }

    public CustomerPortalSettings updatePaymentBtnText(String updatePaymentBtnText) {
        this.updatePaymentBtnText = updatePaymentBtnText;
        return this;
    }

    public void setUpdatePaymentBtnText(String updatePaymentBtnText) {
        this.updatePaymentBtnText = updatePaymentBtnText;
    }

    public String getNextOrderDateLbl() {
        return nextOrderDateLbl;
    }

    public CustomerPortalSettings nextOrderDateLbl(String nextOrderDateLbl) {
        this.nextOrderDateLbl = nextOrderDateLbl;
        return this;
    }

    public void setNextOrderDateLbl(String nextOrderDateLbl) {
        this.nextOrderDateLbl = nextOrderDateLbl;
    }

    public String getStatusLbl() {
        return statusLbl;
    }

    public CustomerPortalSettings statusLbl(String statusLbl) {
        this.statusLbl = statusLbl;
        return this;
    }

    public void setStatusLbl(String statusLbl) {
        this.statusLbl = statusLbl;
    }

    public String getQuantityLbl() {
        return quantityLbl;
    }

    public CustomerPortalSettings quantityLbl(String quantityLbl) {
        this.quantityLbl = quantityLbl;
        return this;
    }

    public void setQuantityLbl(String quantityLbl) {
        this.quantityLbl = quantityLbl;
    }

    public String getAmountLbl() {
        return amountLbl;
    }

    public CustomerPortalSettings amountLbl(String amountLbl) {
        this.amountLbl = amountLbl;
        return this;
    }

    public void setAmountLbl(String amountLbl) {
        this.amountLbl = amountLbl;
    }

    public String getOrderNoLbl() {
        return orderNoLbl;
    }

    public CustomerPortalSettings orderNoLbl(String orderNoLbl) {
        this.orderNoLbl = orderNoLbl;
        return this;
    }

    public void setOrderNoLbl(String orderNoLbl) {
        this.orderNoLbl = orderNoLbl;
    }

    public String getEditFrequencyBtnText() {
        return editFrequencyBtnText;
    }

    public CustomerPortalSettings editFrequencyBtnText(String editFrequencyBtnText) {
        this.editFrequencyBtnText = editFrequencyBtnText;
        return this;
    }

    public void setEditFrequencyBtnText(String editFrequencyBtnText) {
        this.editFrequencyBtnText = editFrequencyBtnText;
    }

    public String getCancelFreqBtnText() {
        return cancelFreqBtnText;
    }

    public CustomerPortalSettings cancelFreqBtnText(String cancelFreqBtnText) {
        this.cancelFreqBtnText = cancelFreqBtnText;
        return this;
    }

    public void setCancelFreqBtnText(String cancelFreqBtnText) {
        this.cancelFreqBtnText = cancelFreqBtnText;
    }

    public String getUpdateFreqBtnText() {
        return updateFreqBtnText;
    }

    public CustomerPortalSettings updateFreqBtnText(String updateFreqBtnText) {
        this.updateFreqBtnText = updateFreqBtnText;
        return this;
    }

    public void setUpdateFreqBtnText(String updateFreqBtnText) {
        this.updateFreqBtnText = updateFreqBtnText;
    }

    public Boolean isPauseResumeSub() {
        return pauseResumeSub;
    }

    public CustomerPortalSettings pauseResumeSub(Boolean pauseResumeSub) {
        this.pauseResumeSub = pauseResumeSub;
        return this;
    }

    public void setPauseResumeSub(Boolean pauseResumeSub) {
        this.pauseResumeSub = pauseResumeSub;
    }

    public Boolean isChangeNextOrderDate() {
        return changeNextOrderDate;
    }

    public CustomerPortalSettings changeNextOrderDate(Boolean changeNextOrderDate) {
        this.changeNextOrderDate = changeNextOrderDate;
        return this;
    }

    public void setChangeNextOrderDate(Boolean changeNextOrderDate) {
        this.changeNextOrderDate = changeNextOrderDate;
    }

    public Boolean isCancelSub() {
        return cancelSub;
    }

    public CustomerPortalSettings cancelSub(Boolean cancelSub) {
        this.cancelSub = cancelSub;
        return this;
    }

    public void setCancelSub(Boolean cancelSub) {
        this.cancelSub = cancelSub;
    }

    public Boolean isChangeOrderFrequency() {
        return changeOrderFrequency;
    }

    public CustomerPortalSettings changeOrderFrequency(Boolean changeOrderFrequency) {
        this.changeOrderFrequency = changeOrderFrequency;
        return this;
    }

    public void setChangeOrderFrequency(Boolean changeOrderFrequency) {
        this.changeOrderFrequency = changeOrderFrequency;
    }

    public Boolean isCreateAdditionalOrder() {
        return createAdditionalOrder;
    }

    public CustomerPortalSettings createAdditionalOrder(Boolean createAdditionalOrder) {
        this.createAdditionalOrder = createAdditionalOrder;
        return this;
    }

    public void setCreateAdditionalOrder(Boolean createAdditionalOrder) {
        this.createAdditionalOrder = createAdditionalOrder;
    }

    public String getManageSubscriptionButtonText() {
        return manageSubscriptionButtonText;
    }

    public CustomerPortalSettings manageSubscriptionButtonText(String manageSubscriptionButtonText) {
        this.manageSubscriptionButtonText = manageSubscriptionButtonText;
        return this;
    }

    public void setManageSubscriptionButtonText(String manageSubscriptionButtonText) {
        this.manageSubscriptionButtonText = manageSubscriptionButtonText;
    }

    public String getEditChangeOrderBtnText() {
        return editChangeOrderBtnText;
    }

    public CustomerPortalSettings editChangeOrderBtnText(String editChangeOrderBtnText) {
        this.editChangeOrderBtnText = editChangeOrderBtnText;
        return this;
    }

    public void setEditChangeOrderBtnText(String editChangeOrderBtnText) {
        this.editChangeOrderBtnText = editChangeOrderBtnText;
    }

    public String getCancelChangeOrderBtnText() {
        return cancelChangeOrderBtnText;
    }

    public CustomerPortalSettings cancelChangeOrderBtnText(String cancelChangeOrderBtnText) {
        this.cancelChangeOrderBtnText = cancelChangeOrderBtnText;
        return this;
    }

    public void setCancelChangeOrderBtnText(String cancelChangeOrderBtnText) {
        this.cancelChangeOrderBtnText = cancelChangeOrderBtnText;
    }

    public String getUpdateChangeOrderBtnText() {
        return updateChangeOrderBtnText;
    }

    public CustomerPortalSettings updateChangeOrderBtnText(String updateChangeOrderBtnText) {
        this.updateChangeOrderBtnText = updateChangeOrderBtnText;
        return this;
    }

    public void setUpdateChangeOrderBtnText(String updateChangeOrderBtnText) {
        this.updateChangeOrderBtnText = updateChangeOrderBtnText;
    }

    public String getEditProductButtonText() {
        return editProductButtonText;
    }

    public CustomerPortalSettings editProductButtonText(String editProductButtonText) {
        this.editProductButtonText = editProductButtonText;
        return this;
    }

    public void setEditProductButtonText(String editProductButtonText) {
        this.editProductButtonText = editProductButtonText;
    }

    public String getDeleteButtonText() {
        return deleteButtonText;
    }

    public CustomerPortalSettings deleteButtonText(String deleteButtonText) {
        this.deleteButtonText = deleteButtonText;
        return this;
    }

    public void setDeleteButtonText(String deleteButtonText) {
        this.deleteButtonText = deleteButtonText;
    }

    public String getUpdateButtonText() {
        return updateButtonText;
    }

    public CustomerPortalSettings updateButtonText(String updateButtonText) {
        this.updateButtonText = updateButtonText;
        return this;
    }

    public void setUpdateButtonText(String updateButtonText) {
        this.updateButtonText = updateButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public CustomerPortalSettings cancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        return this;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public String getAddProductButtonText() {
        return addProductButtonText;
    }

    public CustomerPortalSettings addProductButtonText(String addProductButtonText) {
        this.addProductButtonText = addProductButtonText;
        return this;
    }

    public void setAddProductButtonText(String addProductButtonText) {
        this.addProductButtonText = addProductButtonText;
    }

    public String getAddProductLabelText() {
        return addProductLabelText;
    }

    public CustomerPortalSettings addProductLabelText(String addProductLabelText) {
        this.addProductLabelText = addProductLabelText;
        return this;
    }

    public void setAddProductLabelText(String addProductLabelText) {
        this.addProductLabelText = addProductLabelText;
    }

    public String getActiveBadgeText() {
        return activeBadgeText;
    }

    public CustomerPortalSettings activeBadgeText(String activeBadgeText) {
        this.activeBadgeText = activeBadgeText;
        return this;
    }

    public void setActiveBadgeText(String activeBadgeText) {
        this.activeBadgeText = activeBadgeText;
    }

    public String getCloseBadgeText() {
        return closeBadgeText;
    }

    public CustomerPortalSettings closeBadgeText(String closeBadgeText) {
        this.closeBadgeText = closeBadgeText;
        return this;
    }

    public void setCloseBadgeText(String closeBadgeText) {
        this.closeBadgeText = closeBadgeText;
    }

    public String getSkipOrderButtonText() {
        return skipOrderButtonText;
    }

    public CustomerPortalSettings skipOrderButtonText(String skipOrderButtonText) {
        this.skipOrderButtonText = skipOrderButtonText;
        return this;
    }

    public void setSkipOrderButtonText(String skipOrderButtonText) {
        this.skipOrderButtonText = skipOrderButtonText;
    }

    public String getProductLabelText() {
        return productLabelText;
    }

    public CustomerPortalSettings productLabelText(String productLabelText) {
        this.productLabelText = productLabelText;
        return this;
    }

    public void setProductLabelText(String productLabelText) {
        this.productLabelText = productLabelText;
    }

    public String getSeeMoreDetailsText() {
        return seeMoreDetailsText;
    }

    public CustomerPortalSettings seeMoreDetailsText(String seeMoreDetailsText) {
        this.seeMoreDetailsText = seeMoreDetailsText;
        return this;
    }

    public void setSeeMoreDetailsText(String seeMoreDetailsText) {
        this.seeMoreDetailsText = seeMoreDetailsText;
    }

    public String getHideDetailsText() {
        return hideDetailsText;
    }

    public CustomerPortalSettings hideDetailsText(String hideDetailsText) {
        this.hideDetailsText = hideDetailsText;
        return this;
    }

    public void setHideDetailsText(String hideDetailsText) {
        this.hideDetailsText = hideDetailsText;
    }

    public String getProductInSubscriptionText() {
        return productInSubscriptionText;
    }

    public CustomerPortalSettings productInSubscriptionText(String productInSubscriptionText) {
        this.productInSubscriptionText = productInSubscriptionText;
        return this;
    }

    public void setProductInSubscriptionText(String productInSubscriptionText) {
        this.productInSubscriptionText = productInSubscriptionText;
    }

    public String getEditQuantityLabelText() {
        return EditQuantityLabelText;
    }

    public CustomerPortalSettings EditQuantityLabelText(String EditQuantityLabelText) {
        this.EditQuantityLabelText = EditQuantityLabelText;
        return this;
    }

    public void setEditQuantityLabelText(String EditQuantityLabelText) {
        this.EditQuantityLabelText = EditQuantityLabelText;
    }

    public String getSubTotalLabelText() {
        return subTotalLabelText;
    }

    public CustomerPortalSettings subTotalLabelText(String subTotalLabelText) {
        this.subTotalLabelText = subTotalLabelText;
        return this;
    }

    public void setSubTotalLabelText(String subTotalLabelText) {
        this.subTotalLabelText = subTotalLabelText;
    }

    public String getPaymentNotificationText() {
        return paymentNotificationText;
    }

    public CustomerPortalSettings paymentNotificationText(String paymentNotificationText) {
        this.paymentNotificationText = paymentNotificationText;
        return this;
    }

    public void setPaymentNotificationText(String paymentNotificationText) {
        this.paymentNotificationText = paymentNotificationText;
    }

    public Boolean isEditProductFlag() {
        return editProductFlag;
    }

    public CustomerPortalSettings editProductFlag(Boolean editProductFlag) {
        this.editProductFlag = editProductFlag;
        return this;
    }

    public void setEditProductFlag(Boolean editProductFlag) {
        this.editProductFlag = editProductFlag;
    }

    public Boolean isDeleteProductFlag() {
        return deleteProductFlag;
    }

    public CustomerPortalSettings deleteProductFlag(Boolean deleteProductFlag) {
        this.deleteProductFlag = deleteProductFlag;
        return this;
    }

    public void setDeleteProductFlag(Boolean deleteProductFlag) {
        this.deleteProductFlag = deleteProductFlag;
    }

    public Boolean isShowShipment() {
        return showShipment;
    }

    public CustomerPortalSettings showShipment(Boolean showShipment) {
        this.showShipment = showShipment;
        return this;
    }

    public void setShowShipment(Boolean showShipment) {
        this.showShipment = showShipment;
    }

    public Boolean isAddAdditionalProduct() {
        return addAdditionalProduct;
    }

    public CustomerPortalSettings addAdditionalProduct(Boolean addAdditionalProduct) {
        this.addAdditionalProduct = addAdditionalProduct;
        return this;
    }

    public void setAddAdditionalProduct(Boolean addAdditionalProduct) {
        this.addAdditionalProduct = addAdditionalProduct;
    }

    public String getSuccessText() {
        return successText;
    }

    public CustomerPortalSettings successText(String successText) {
        this.successText = successText;
        return this;
    }

    public void setSuccessText(String successText) {
        this.successText = successText;
    }

    public String getCancelSubscriptionConfirmPrepaidText() {
        return cancelSubscriptionConfirmPrepaidText;
    }

    public CustomerPortalSettings cancelSubscriptionConfirmPrepaidText(String cancelSubscriptionConfirmPrepaidText) {
        this.cancelSubscriptionConfirmPrepaidText = cancelSubscriptionConfirmPrepaidText;
        return this;
    }

    public void setCancelSubscriptionConfirmPrepaidText(String cancelSubscriptionConfirmPrepaidText) {
        this.cancelSubscriptionConfirmPrepaidText = cancelSubscriptionConfirmPrepaidText;
    }

    public String getCancelSubscriptionConfirmPayAsYouGoText() {
        return cancelSubscriptionConfirmPayAsYouGoText;
    }

    public CustomerPortalSettings cancelSubscriptionConfirmPayAsYouGoText(String cancelSubscriptionConfirmPayAsYouGoText) {
        this.cancelSubscriptionConfirmPayAsYouGoText = cancelSubscriptionConfirmPayAsYouGoText;
        return this;
    }

    public void setCancelSubscriptionConfirmPayAsYouGoText(String cancelSubscriptionConfirmPayAsYouGoText) {
        this.cancelSubscriptionConfirmPayAsYouGoText = cancelSubscriptionConfirmPayAsYouGoText;
    }

    public String getCancelSubscriptionPrepaidButtonText() {
        return cancelSubscriptionPrepaidButtonText;
    }

    public CustomerPortalSettings cancelSubscriptionPrepaidButtonText(String cancelSubscriptionPrepaidButtonText) {
        this.cancelSubscriptionPrepaidButtonText = cancelSubscriptionPrepaidButtonText;
        return this;
    }

    public void setCancelSubscriptionPrepaidButtonText(String cancelSubscriptionPrepaidButtonText) {
        this.cancelSubscriptionPrepaidButtonText = cancelSubscriptionPrepaidButtonText;
    }

    public String getCancelSubscriptionPayAsYouGoButtonText() {
        return cancelSubscriptionPayAsYouGoButtonText;
    }

    public CustomerPortalSettings cancelSubscriptionPayAsYouGoButtonText(String cancelSubscriptionPayAsYouGoButtonText) {
        this.cancelSubscriptionPayAsYouGoButtonText = cancelSubscriptionPayAsYouGoButtonText;
        return this;
    }

    public void setCancelSubscriptionPayAsYouGoButtonText(String cancelSubscriptionPayAsYouGoButtonText) {
        this.cancelSubscriptionPayAsYouGoButtonText = cancelSubscriptionPayAsYouGoButtonText;
    }

    public String getUpcomingFulfillmentText() {
        return upcomingFulfillmentText;
    }

    public CustomerPortalSettings upcomingFulfillmentText(String upcomingFulfillmentText) {
        this.upcomingFulfillmentText = upcomingFulfillmentText;
        return this;
    }

    public void setUpcomingFulfillmentText(String upcomingFulfillmentText) {
        this.upcomingFulfillmentText = upcomingFulfillmentText;
    }

    public String getCreditCardText() {
        return creditCardText;
    }

    public CustomerPortalSettings creditCardText(String creditCardText) {
        this.creditCardText = creditCardText;
        return this;
    }

    public void setCreditCardText(String creditCardText) {
        this.creditCardText = creditCardText;
    }

    public String getEndingWithText() {
        return endingWithText;
    }

    public CustomerPortalSettings endingWithText(String endingWithText) {
        this.endingWithText = endingWithText;
        return this;
    }

    public void setEndingWithText(String endingWithText) {
        this.endingWithText = endingWithText;
    }

    public String getWeekText() {
        return weekText;
    }

    public CustomerPortalSettings weekText(String weekText) {
        this.weekText = weekText;
        return this;
    }

    public void setWeekText(String weekText) {
        this.weekText = weekText;
    }

    public String getDayText() {
        return dayText;
    }

    public CustomerPortalSettings dayText(String dayText) {
        this.dayText = dayText;
        return this;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getMonthText() {
        return monthText;
    }

    public CustomerPortalSettings monthText(String monthText) {
        this.monthText = monthText;
        return this;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public String getYearText() {
        return yearText;
    }

    public CustomerPortalSettings yearText(String yearText) {
        this.yearText = yearText;
        return this;
    }

    public void setYearText(String yearText) {
        this.yearText = yearText;
    }

    public String getSkipBadgeText() {
        return skipBadgeText;
    }

    public CustomerPortalSettings skipBadgeText(String skipBadgeText) {
        this.skipBadgeText = skipBadgeText;
        return this;
    }

    public void setSkipBadgeText(String skipBadgeText) {
        this.skipBadgeText = skipBadgeText;
    }

    public String getQueueBadgeText() {
        return queueBadgeText;
    }

    public CustomerPortalSettings queueBadgeText(String queueBadgeText) {
        this.queueBadgeText = queueBadgeText;
        return this;
    }

    public void setQueueBadgeText(String queueBadgeText) {
        this.queueBadgeText = queueBadgeText;
    }

    public String getCustomerPortalSettingJson() {
        return customerPortalSettingJson;
    }

    public CustomerPortalSettings customerPortalSettingJson(String customerPortalSettingJson) {
        this.customerPortalSettingJson = customerPortalSettingJson;
        return this;
    }

    public void setCustomerPortalSettingJson(String customerPortalSettingJson) {
        this.customerPortalSettingJson = customerPortalSettingJson;
    }

    public Boolean isOrderNoteFlag() {
        return orderNoteFlag;
    }

    public CustomerPortalSettings orderNoteFlag(Boolean orderNoteFlag) {
        this.orderNoteFlag = orderNoteFlag;
        return this;
    }

    public void setOrderNoteFlag(Boolean orderNoteFlag) {
        this.orderNoteFlag = orderNoteFlag;
    }

    public String getOrderNoteText() {
        return orderNoteText;
    }

    public CustomerPortalSettings orderNoteText(String orderNoteText) {
        this.orderNoteText = orderNoteText;
        return this;
    }

    public void setOrderNoteText(String orderNoteText) {
        this.orderNoteText = orderNoteText;
    }

    public Boolean isUseUrlWithCustomerId() {
        return useUrlWithCustomerId;
    }

    public CustomerPortalSettings useUrlWithCustomerId(Boolean useUrlWithCustomerId) {
        this.useUrlWithCustomerId = useUrlWithCustomerId;
        return this;
    }

    public void setUseUrlWithCustomerId(Boolean useUrlWithCustomerId) {
        this.useUrlWithCustomerId = useUrlWithCustomerId;
    }

    public ProductSelectionOption getProductSelectionOption() {
        return productSelectionOption;
    }

    public CustomerPortalSettings productSelectionOption(ProductSelectionOption productSelectionOption) {
        this.productSelectionOption = productSelectionOption;
        return this;
    }

    public void setProductSelectionOption(ProductSelectionOption productSelectionOption) {
        this.productSelectionOption = productSelectionOption;
    }

    public Boolean isIncludeOutOfStockProduct() {
        return includeOutOfStockProduct;
    }

    public CustomerPortalSettings includeOutOfStockProduct(Boolean includeOutOfStockProduct) {
        this.includeOutOfStockProduct = includeOutOfStockProduct;
        return this;
    }

    public void setIncludeOutOfStockProduct(Boolean includeOutOfStockProduct) {
        this.includeOutOfStockProduct = includeOutOfStockProduct;
    }

    public String getOpenBadgeText() {
        return openBadgeText;
    }

    public CustomerPortalSettings openBadgeText(String openBadgeText) {
        this.openBadgeText = openBadgeText;
        return this;
    }

    public void setOpenBadgeText(String openBadgeText) {
        this.openBadgeText = openBadgeText;
    }

    public Boolean isUpdateShipmentBillingDate() {
        return updateShipmentBillingDate;
    }

    public CustomerPortalSettings updateShipmentBillingDate(Boolean updateShipmentBillingDate) {
        this.updateShipmentBillingDate = updateShipmentBillingDate;
        return this;
    }

    public void setUpdateShipmentBillingDate(Boolean updateShipmentBillingDate) {
        this.updateShipmentBillingDate = updateShipmentBillingDate;
    }

    public Boolean isDiscountCode() {
        return discountCode;
    }

    public CustomerPortalSettings discountCode(Boolean discountCode) {
        this.discountCode = discountCode;
        return this;
    }

    public void setDiscountCode(Boolean discountCode) {
        this.discountCode = discountCode;
    }

    public Boolean isFreezeOrderTillMinCycle() {
        return freezeOrderTillMinCycle;
    }

    public CustomerPortalSettings freezeOrderTillMinCycle(Boolean freezeOrderTillMinCycle) {
        this.freezeOrderTillMinCycle = freezeOrderTillMinCycle;
        return this;
    }

    public void setFreezeOrderTillMinCycle(Boolean freezeOrderTillMinCycle) {
        this.freezeOrderTillMinCycle = freezeOrderTillMinCycle;
    }

    public Boolean isAddOneTimeProduct() {
        return addOneTimeProduct;
    }

    public CustomerPortalSettings addOneTimeProduct(Boolean addOneTimeProduct) {
        this.addOneTimeProduct = addOneTimeProduct;
        return this;
    }

    public void setAddOneTimeProduct(Boolean addOneTimeProduct) {
        this.addOneTimeProduct = addOneTimeProduct;
    }

    public Boolean isAllowOrderNow() {
        return allowOrderNow;
    }

    public CustomerPortalSettings allowOrderNow(Boolean allowOrderNow) {
        this.allowOrderNow = allowOrderNow;
        return this;
    }

    public void setAllowOrderNow(Boolean allowOrderNow) {
        this.allowOrderNow = allowOrderNow;
    }

    public Boolean isAllowSplitContract() {
        return allowSplitContract;
    }

    public CustomerPortalSettings allowSplitContract(Boolean allowSplitContract) {
        this.allowSplitContract = allowSplitContract;
        return this;
    }

    public void setAllowSplitContract(Boolean allowSplitContract) {
        this.allowSplitContract = allowSplitContract;
    }

    public Boolean isEnableEditAttributes() {
        return enableEditAttributes;
    }

    public CustomerPortalSettings enableEditAttributes(Boolean enableEditAttributes) {
        this.enableEditAttributes = enableEditAttributes;
        return this;
    }

    public void setEnableEditAttributes(Boolean enableEditAttributes) {
        this.enableEditAttributes = enableEditAttributes;
    }

    public Integer getMinQtyToAllowDuringAddProduct() {
        return minQtyToAllowDuringAddProduct;
    }

    public CustomerPortalSettings minQtyToAllowDuringAddProduct(Integer minQtyToAllowDuringAddProduct) {
        this.minQtyToAllowDuringAddProduct = minQtyToAllowDuringAddProduct;
        return this;
    }

    public void setMinQtyToAllowDuringAddProduct(Integer minQtyToAllowDuringAddProduct) {
        this.minQtyToAllowDuringAddProduct = minQtyToAllowDuringAddProduct;
    }

    public Boolean getEnableSwapProductVariant() {
        return enableSwapProductVariant;
    }

    public CustomerPortalSettings enableSwapProductVariant(Boolean enableSwapProductVariant) {
        this.enableSwapProductVariant = enableSwapProductVariant;
        return this;
    }

    public void setEnableSwapProductVariant(Boolean enableSwapProductVariant) {
        this.enableSwapProductVariant = enableSwapProductVariant;
    }

    public Boolean isEnableAllowOnlyOneDiscountCode() {
        return enableAllowOnlyOneDiscountCode;
    }

    public CustomerPortalSettings enableAllowOnlyOneDiscountCode(Boolean enableAllowOnlyOneDiscountCode) {
        this.enableAllowOnlyOneDiscountCode = enableAllowOnlyOneDiscountCode;
        return this;
    }

    public void setEnableAllowOnlyOneDiscountCode(Boolean enableAllowOnlyOneDiscountCode) {
        this.enableAllowOnlyOneDiscountCode = enableAllowOnlyOneDiscountCode;
    }

    public Boolean isEnableRedirectToProductPage() {
        return enableRedirectToProductPage;
    }

    public CustomerPortalSettings enableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
        return this;
    }

    public void setEnableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerPortalSettings)) {
            return false;
        }
        return id != null && id.equals(((CustomerPortalSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerPortalSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", orderFrequencyText='" + getOrderFrequencyText() + "'" +
            ", totalProductsText='" + getTotalProductsText() + "'" +
            ", nextOrderText='" + getNextOrderText() + "'" +
            ", statusText='" + getStatusText() + "'" +
            ", cancelSubscriptionBtnText='" + getCancelSubscriptionBtnText() + "'" +
            ", noSubscriptionMessage='" + getNoSubscriptionMessage() + "'" +
            ", subscriptionNoText='" + getSubscriptionNoText() + "'" +
            ", updatePaymentMessage='" + getUpdatePaymentMessage() + "'" +
            ", cardLastFourDigitText='" + getCardLastFourDigitText() + "'" +
            ", cardExpiryText='" + getCardExpiryText() + "'" +
            ", cardHolderNameText='" + getCardHolderNameText() + "'" +
            ", cardTypeText='" + getCardTypeText() + "'" +
            ", paymentMethodTypeText='" + getPaymentMethodTypeText() + "'" +
            ", cancelAccordionTitle='" + getCancelAccordionTitle() + "'" +
            ", paymentDetailAccordionTitle='" + getPaymentDetailAccordionTitle() + "'" +
            ", upcomingOrderAccordionTitle='" + getUpcomingOrderAccordionTitle() + "'" +
            ", paymentInfoText='" + getPaymentInfoText() + "'" +
            ", updatePaymentBtnText='" + getUpdatePaymentBtnText() + "'" +
            ", nextOrderDateLbl='" + getNextOrderDateLbl() + "'" +
            ", statusLbl='" + getStatusLbl() + "'" +
            ", quantityLbl='" + getQuantityLbl() + "'" +
            ", amountLbl='" + getAmountLbl() + "'" +
            ", orderNoLbl='" + getOrderNoLbl() + "'" +
            ", editFrequencyBtnText='" + getEditFrequencyBtnText() + "'" +
            ", cancelFreqBtnText='" + getCancelFreqBtnText() + "'" +
            ", updateFreqBtnText='" + getUpdateFreqBtnText() + "'" +
            ", pauseResumeSub='" + isPauseResumeSub() + "'" +
            ", changeNextOrderDate='" + isChangeNextOrderDate() + "'" +
            ", cancelSub='" + isCancelSub() + "'" +
            ", changeOrderFrequency='" + isChangeOrderFrequency() + "'" +
            ", createAdditionalOrder='" + isCreateAdditionalOrder() + "'" +
            ", manageSubscriptionButtonText='" + getManageSubscriptionButtonText() + "'" +
            ", editChangeOrderBtnText='" + getEditChangeOrderBtnText() + "'" +
            ", cancelChangeOrderBtnText='" + getCancelChangeOrderBtnText() + "'" +
            ", updateChangeOrderBtnText='" + getUpdateChangeOrderBtnText() + "'" +
            ", editProductButtonText='" + getEditProductButtonText() + "'" +
            ", deleteButtonText='" + getDeleteButtonText() + "'" +
            ", updateButtonText='" + getUpdateButtonText() + "'" +
            ", cancelButtonText='" + getCancelButtonText() + "'" +
            ", addProductButtonText='" + getAddProductButtonText() + "'" +
            ", addProductLabelText='" + getAddProductLabelText() + "'" +
            ", activeBadgeText='" + getActiveBadgeText() + "'" +
            ", closeBadgeText='" + getCloseBadgeText() + "'" +
            ", skipOrderButtonText='" + getSkipOrderButtonText() + "'" +
            ", productLabelText='" + getProductLabelText() + "'" +
            ", seeMoreDetailsText='" + getSeeMoreDetailsText() + "'" +
            ", hideDetailsText='" + getHideDetailsText() + "'" +
            ", productInSubscriptionText='" + getProductInSubscriptionText() + "'" +
            ", EditQuantityLabelText='" + getEditQuantityLabelText() + "'" +
            ", subTotalLabelText='" + getSubTotalLabelText() + "'" +
            ", paymentNotificationText='" + getPaymentNotificationText() + "'" +
            ", editProductFlag='" + isEditProductFlag() + "'" +
            ", deleteProductFlag='" + isDeleteProductFlag() + "'" +
            ", showShipment='" + isShowShipment() + "'" +
            ", addAdditionalProduct='" + isAddAdditionalProduct() + "'" +
            ", successText='" + getSuccessText() + "'" +
            ", cancelSubscriptionConfirmPrepaidText='" + getCancelSubscriptionConfirmPrepaidText() + "'" +
            ", cancelSubscriptionConfirmPayAsYouGoText='" + getCancelSubscriptionConfirmPayAsYouGoText() + "'" +
            ", cancelSubscriptionPrepaidButtonText='" + getCancelSubscriptionPrepaidButtonText() + "'" +
            ", cancelSubscriptionPayAsYouGoButtonText='" + getCancelSubscriptionPayAsYouGoButtonText() + "'" +
            ", upcomingFulfillmentText='" + getUpcomingFulfillmentText() + "'" +
            ", creditCardText='" + getCreditCardText() + "'" +
            ", endingWithText='" + getEndingWithText() + "'" +
            ", weekText='" + getWeekText() + "'" +
            ", dayText='" + getDayText() + "'" +
            ", monthText='" + getMonthText() + "'" +
            ", yearText='" + getYearText() + "'" +
            ", skipBadgeText='" + getSkipBadgeText() + "'" +
            ", queueBadgeText='" + getQueueBadgeText() + "'" +
            ", customerPortalSettingJson='" + getCustomerPortalSettingJson() + "'" +
            ", orderNoteFlag='" + isOrderNoteFlag() + "'" +
            ", orderNoteText='" + getOrderNoteText() + "'" +
            ", useUrlWithCustomerId='" + isUseUrlWithCustomerId() + "'" +
            ", productSelectionOption='" + getProductSelectionOption() + "'" +
            ", includeOutOfStockProduct='" + isIncludeOutOfStockProduct() + "'" +
            ", openBadgeText='" + getOpenBadgeText() + "'" +
            ", updateShipmentBillingDate='" + isUpdateShipmentBillingDate() + "'" +
            ", discountCode='" + isDiscountCode() + "'" +
            ", freezeOrderTillMinCycle='" + isFreezeOrderTillMinCycle() + "'" +
            ", addOneTimeProduct='" + isAddOneTimeProduct() + "'" +
            ", allowOrderNow='" + isAllowOrderNow() + "'" +
            ", allowSplitContract='" + isAllowSplitContract() + "'" +
            ", enableEditAttributes='" + isEnableEditAttributes() + "'" +
            ", minQtyToAllowDuringAddProduct=" + getMinQtyToAllowDuringAddProduct() +
            ", enableSwapProductVariant='" + getEnableSwapProductVariant() + "'" +
            ", isEnableRedirectMyAccountButton='" + isEnableRedirectMyAccountButton() + "'" +
            ", enableAllowOnlyOneDiscountCode='" + isEnableAllowOnlyOneDiscountCode() + "'" +
            ", enableRedirectToProductPage='" + isEnableRedirectToProductPage() + "'" +
            "}";
    }
}
