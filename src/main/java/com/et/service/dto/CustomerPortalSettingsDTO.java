package com.et.service.dto;

import java.io.Serializable;
import javax.persistence.Lob;

import com.et.domain.enumeration.ProductSelectionOption;
import com.et.domain.enumeration.SubscriptionDiscountTypeUnit;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A DTO for the {@link com.et.domain.CustomerPortalSettings} entity.
 */
public class CustomerPortalSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private String orderFrequencyText;

    @NotNull
    private String totalProductsText;

    @NotNull
    private String nextOrderText;

    @NotNull
    private String statusText;

    @NotNull
    private String cancelSubscriptionBtnText;

    @NotNull
    private String noSubscriptionMessage;

    @NotNull
    private String subscriptionNoText;

    @NotNull
    private String updatePaymentMessage;

    @NotNull
    private String cardLastFourDigitText;

    @NotNull
    private String cardExpiryText;

    @NotNull
    private String cardHolderNameText;

    @NotNull
    private String cardTypeText;

    @NotNull
    private String paymentMethodTypeText;

    @NotNull
    private String cancelAccordionTitle;

    @NotNull
    private String paymentDetailAccordionTitle;

    @NotNull
    private String upcomingOrderAccordionTitle;

    @NotNull
    private String paymentInfoText;

    @NotNull
    private String updatePaymentBtnText;

    @NotNull
    private String nextOrderDateLbl;

    @NotNull
    private String statusLbl;

    @NotNull
    private String quantityLbl;

    @NotNull
    private String amountLbl;

    @NotNull
    private String orderNoLbl;

    @NotNull
    private String editFrequencyBtnText;

    @NotNull
    private String cancelFreqBtnText;

    @NotNull
    private String updateFreqBtnText;


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    private Boolean pauseResumeSub;

    private Boolean changeNextOrderDate;

    private Boolean cancelSub;

    private Boolean changeOrderFrequency;

    private Boolean createAdditionalOrder;

    private String manageSubscriptionButtonText;

    private String editChangeOrderBtnText;

    private String cancelChangeOrderBtnText;

    private String updateChangeOrderBtnText;

    private String editProductButtonText;

    private String deleteButtonText;

    private String updateButtonText;

    private String cancelButtonText;

    private String addProductButtonText;

    private String addProductLabelText;

    private String activeBadgeText;

    private String closeBadgeText;

    private String skipOrderButtonText;

    private String productLabelText;

    private String seeMoreDetailsText;

    private String hideDetailsText;

    private String productInSubscriptionText;

    private String EditQuantityLabelText;

    private String subTotalLabelText;

    private String paymentNotificationText;

    private Boolean editProductFlag;

    private Boolean deleteProductFlag;

    private Boolean showShipment;

    private Boolean magicLinkEmailFlag;

    private Boolean addAdditionalProduct;

    private String successText;

    private String cancelSubscriptionConfirmPrepaidText;

    private String cancelSubscriptionConfirmPayAsYouGoText;

    private String cancelSubscriptionPrepaidButtonText;

    private String cancelSubscriptionPayAsYouGoButtonText;

    private String upcomingFulfillmentText;

    private String creditCardText;

    private String endingWithText;

    private String weekText;

    private String dayText;

    private String monthText;

    private String yearText;

    private String skipBadgeText;

    private String queueBadgeText;

    private String shippingLabelText;

    private String failureText;

    private String sendEmailText;

    private String chooseDifferentProductActionText;
    private String chooseDifferentProductText;
    private String confirmSkipFulfillmentBtnText;
    private String confirmSkipOrder;
    private String skipFulfillmentButtonText;
    private String confirmCommonText;
    private String orderNowDescriptionText;
    private String discountDetailsTitleText;

    private String emailAddressText;

    private String emailMagicLinkText;

    private String retriveMagicLinkText;

    private String validEmailMessage;

    private String saveButtonText;

    private String orderDateText;

    private String address1LabelText;

    private String address2LabelText;

    private String companyLabelText;

    private String cityLabelText;

    private String countryLabelText;

    private String firstNameLabelText;

    private String lastNameLabelText;

    private String phoneLabelText;

    private String provinceLabelText;

    private String zipLabelText;

    private String addressHeaderTitleText;

    private Boolean changeShippingAddressFlag;

    private String updateEditShippingButtonText;

    private String cancelEditShippingButtonText;

    private String pauseSubscriptionText;

    private String resumeSubscriptionText;

    private String pauseBadgeText;

    @Lob
    private String customerPortalSettingJson;

    @Lob
    private String productFilterConfig;

    private String discountNoteTitle;

    private String initialDiscountNoteDescription;

    private String afterCycleDiscountNoteDescription;

    private String productRemovedTooltip;

    private String deliveryPriceText;

    private String shippingOptionText;

    private String nextDeliveryDate;

    private String everyLabelText;

    private Boolean orderNoteFlag;

    private String orderNoteText;

    private Boolean useUrlWithCustomerId;

    private String expiredTokenText;

    private String portalLoginLinkText;

    private String localeDate;

    private ProductSelectionOption productSelectionOption;

    private Boolean includeOutOfStockProduct;

    private String customerIdText;

    private String helloNameText;

    private String goBackButtonText;

    private String changeVariantLabelText;

    private String provinceCodeLabelText;

    private String countryCodeLabelText;

    private String pleaseWaitLoaderText;

    private String openBadgeText;

    private String cancelSubscriptionMinimumBillingIterationsMessage;

    private String topHtml;

    private String bottomHtml;

    private Boolean updateShipmentBillingDate;

    private String discountCodeText;

    private String discountCodeApplyButtonText;

    private Boolean discountCode;

    private Boolean applySellingPlanBasedDiscount;

    private Boolean applySubscriptionDiscountForOtp;

    private Boolean applySubscriptionDiscount;

    private Boolean removeDiscountCodeAutomatically;

    private String removeDiscountCodeLabel;

    private Boolean enableSplitContract;

    private String splitContractMessage;

    private String splitContractText;

    private SubscriptionDiscountTypeUnit subscriptionDiscountTypeUnit;

    private Double subscriptionDiscount;

    private String upSellMessage;

    private Boolean freezeOrderTillMinCycle;

    private String freezeUpdateSubscriptionMessage;

    private String subscriptionContractFreezeMessage;

    private String preventCancellationBeforeDaysMessage;

    private Boolean offerDiscountOnCancellation;

    private Boolean enableSkipFulFillment;

    private String discountPercentageOnCancellation;

    private String discountMessageOnCancellation;

    private String discountRecurringCycleLimitOnCancellation;

    private String frequencyChangeWarningTitle;
    private String frequencyChangeWarningDescription;

    private String discountCouponRemoveText;
    private String pleaseSelectText;

    private String shippingAddressNotAvailableText;

    private String sellingPlanNameText;

    private String discountCouponNotAppliedText;

    private String shopPayPaymentUpdateText;

    private Boolean addOneTimeProduct;

    private Boolean allowOrderNow;

    private String selectProductLabelText;

    private String purchaseOptionLabelText;

    private String finishLabelText;

    private String nextBtnText;

    private String previousBtnText;

    private String closeBtnText;

    private String deleteConfirmationMsgText;

    private String deleteMsgText;

    private String yesBtnText;

    private String noBtnText;

    private String oneTimePurchaseNoteText;

    private String clickHereText;

    private String productAddMessageText;

    private String choosePurchaseOptionLabelText;

    private String oneTimePurchaseMessageText;

    private String contractUpdateMessageText;

    private String oneTimePurchaseDisplayMessageText;

    private String addProductFinishedMessageText;

    private String contractErrorMessageText;

    private String addToSubscriptionTitleCP;

    private String oneTimePurchaseTitleCP;

    private String seeMoreProductBtnText;

    private String viewAttributeLabelText;

    private String attributeNameLabelText;

    private String swapProductLabelText;

    private String swapProductSearchBarText;

    private Boolean enableSwapProductFeature;

    private Boolean enableTabletForceView;

    private String swapProductBtnText;

    private String attributeValue;

    private String addNewButtonText;

    private String attributeHeadingText;

    private Boolean enableViewAttributes;

    private Boolean enableEditOrderNotes;

    private Boolean showSellingPlanFrequencies;

    private String totalPricePerDeliveryText;

    private String fulfilledText;

    private String dateFormat;

    private String discountCouponAppliedText;

    private String subscriptionPausedMessageText;

    private String subscriptionActivatedMessageText;

    private String unableToUpdateSubscriptionStatusMessageText;

    private String selectCancellationReasonLabelText;

    private String upcomingOrderChangePopupSuccessTitleText;

    private String upcomingOrderChangePopupSuccessDescriptionText;

    private String upcomingOrderChangePopupSuccessClosebtnText;

    private String upcomingOrderChangePopupFailureTitleText;

    private String upcomingOrderChangePopupFailureDescriptionText;

    private String upcomingOrderChangePopupFailureClosebtnText;

    private String variantIdsToFreezeEditRemove;

    private String preventCancellationBeforeDays;

    private String disAllowVariantIdsForOneTimeProductAdd;

    private String disAllowVariantIdsForSubscriptionProductAdd;

    private Boolean hideAddSubscriptionProductSection;

    private Boolean allowOnlyOneTimeProductOnAddProductFlag;

    private String requireFieldMessage;
    private String validNumberRequiredMessage;
    private String variantLbl;
    private String priceLbl;
    private String oneTimePurchaseOnlyText;
    private String rescheduleText;
    private String popUpSuccessMessage;
    private String popUpErrorMessage;
    private String orderNowText;
    private String upcomingOrderPlaceNowAlertText;
    private String upcomingOrderSkipAlertText;
    private String deliveryFrequencyText;
    private String editDeliveryInternalText;
    private String maxCycleText;
    private String minCycleText;
    private String selectProductToAdd;
    private String searchProductBtnText;
    private String areyousureCommonMessageText;
    private String editCommonText;
    private String viewMoreText;
    private String variantLblText;
    private String totalLblText;
    private String deleteProductTitleText;
    private String greetingText;
    private String productLblText;
    private String hasBeenRemovedText;
    private String orderTotalText;
    private String addDiscountCodeText;
    private String addDiscountCodeAlertText;
    private String removeDiscountCodeAlertText;
    private String shopPayLblText;
    private String paypalLblText;
    private String unknownPaymentReachoutUsText;
    private String addToOrderLabelText;
    private String upcomingTabTitle;
    private String scheduledTabTitle;
    private String historyTabTitle;
    private String noOrderNotAvailableMessage;
    private String continueText;
    private String confirmSwapText;
    private String confirmAddProduct;
    private Integer minQtyToAllowDuringAddProduct;
    private Boolean allowSplitContract;

    private Boolean enableSwapProductVariant;

    private Boolean enableEditAttributes;

    private String discountAccordionTitle;

    private String redeemRewardsTextV2;
    private String rewardsTextV2;
    private String yourRewardsTextV2;
    private String yourAvailableRewardsPointsTextV2;

    private String cancellationDateTitleText;
    private String selectedCancellationReasonTitleText;
    private String cancellationNoteTitleText;

    private String selectSplitMethodLabelText;
    private String splitWithOrderPlacedSelectOptionText;
    private String splitWithoutOrderPlacedSelectOptionText;
    private String contractCancelledBadgeText;

    private String chooseAnotherPaymentMethodTitleText;
    private String selectPaymentMethodTitleText;
    private String changePaymentMessage;
    private String updatePaymentMethodTitleText;
    private Boolean enableRedirectMyAccountButton;
    private String reschedulingPolicies;
    private Boolean enableRedirectToProductPage;

    private Boolean enableAllowOnlyOneDiscountCode;

    private String upcomingTabHeaderHTML;
    private String schedulesTabHeaderHTML;
    private String historyTabHeaderHTML;
    private String allowedProductIdsForOneTimeProductAdd;

    public String getUpcomingTabHeaderHTML() {
        return upcomingTabHeaderHTML;
    }

    public void setUpcomingTabHeaderHTML(String upcomingTabHeaderHTML) {
        this.upcomingTabHeaderHTML = upcomingTabHeaderHTML;
    }

    public String getSchedulesTabHeaderHTML() {
        return schedulesTabHeaderHTML;
    }

    public void setSchedulesTabHeaderHTML(String schedulesTabHeaderHTML) {
        this.schedulesTabHeaderHTML = schedulesTabHeaderHTML;
    }

    public String getHistoryTabHeaderHTML() {
        return historyTabHeaderHTML;
    }

    public void setHistoryTabHeaderHTML(String historyTabHeaderHTML) {
        this.historyTabHeaderHTML = historyTabHeaderHTML;
    }

    public Boolean getEnableRedirectToProductPage() {
        return enableRedirectToProductPage;
    }

    public void setEnableRedirectToProductPage(Boolean enableRedirectToProductPage) {
        this.enableRedirectToProductPage = enableRedirectToProductPage;
    }

    public String getReschedulingPolicies() {
        return reschedulingPolicies;
    }

    public void setReschedulingPolicies(String reschedulingPolicies) {
        this.reschedulingPolicies = reschedulingPolicies;
    }

    public String getProductFilterConfig() {
        return productFilterConfig;
    }

    public void setProductFilterConfig(String productFilterConfig) {
        this.productFilterConfig = productFilterConfig;
    }

    public Boolean isEnableRedirectMyAccountButton() {
        return enableRedirectMyAccountButton;
    }

    public void setEnableRedirectMyAccountButton(Boolean enableRedirectMyAccountButton) {
        this.enableRedirectMyAccountButton = enableRedirectMyAccountButton;
    }


    private List<UpdateShopCustomizationRequest> shopCustomizationData;

    public List<UpdateShopCustomizationRequest> getShopCustomizationData() {
        return shopCustomizationData;
    }

    public void setShopCustomizationData(List<UpdateShopCustomizationRequest> shopCustomizationData) {
        this.shopCustomizationData = shopCustomizationData;
    }

    public String getUpdatePaymentMethodTitleText() {
        return updatePaymentMethodTitleText;
    }

    public void setUpdatePaymentMethodTitleText(String updatePaymentMethodTitleText) {
        this.updatePaymentMethodTitleText = updatePaymentMethodTitleText;
    }

    public String getChooseAnotherPaymentMethodTitleText() {
        return chooseAnotherPaymentMethodTitleText;
    }

    public void setChooseAnotherPaymentMethodTitleText(String chooseAnotherPaymentMethodTitleText) {
        this.chooseAnotherPaymentMethodTitleText = chooseAnotherPaymentMethodTitleText;
    }

    public String getSelectPaymentMethodTitleText() {
        return selectPaymentMethodTitleText;
    }

    public void setSelectPaymentMethodTitleText(String selectPaymentMethodTitleText) {
        this.selectPaymentMethodTitleText = selectPaymentMethodTitleText;
    }

    public String getChangePaymentMessage() {
        return changePaymentMessage;
    }

    public void setChangePaymentMessage(String changePaymentMessage) {
        this.changePaymentMessage = changePaymentMessage;
    }

    public String getSelectSplitMethodLabelText() {
        return selectSplitMethodLabelText;
    }

    public void setSelectSplitMethodLabelText(String selectSplitMethodLabelText) {
        this.selectSplitMethodLabelText = selectSplitMethodLabelText;
    }

    public String getSplitWithOrderPlacedSelectOptionText() {
        return splitWithOrderPlacedSelectOptionText;
    }

    public void setSplitWithOrderPlacedSelectOptionText(String splitWithOrderPlacedSelectOptionText) {
        this.splitWithOrderPlacedSelectOptionText = splitWithOrderPlacedSelectOptionText;
    }

    public String getSplitWithoutOrderPlacedSelectOptionText() {
        return splitWithoutOrderPlacedSelectOptionText;
    }

    public void setSplitWithoutOrderPlacedSelectOptionText(String splitWithoutOrderPlacedSelectOptionText) {
        this.splitWithoutOrderPlacedSelectOptionText = splitWithoutOrderPlacedSelectOptionText;
    }

    public String getContractCancelledBadgeText() {
        return contractCancelledBadgeText;
    }

    public void setContractCancelledBadgeText(String contractCancelledBadgeText) {
        this.contractCancelledBadgeText = contractCancelledBadgeText;
    }

    public String getCancellationDateTitleText() {
        return cancellationDateTitleText;
    }

    public void setCancellationDateTitleText(String cancellationDateTitleText) {
        this.cancellationDateTitleText = cancellationDateTitleText;
    }

    public String getSelectedCancellationReasonTitleText() {
        return selectedCancellationReasonTitleText;
    }

    public void setSelectedCancellationReasonTitleText(String selectedCancellationReasonTitleText) {
        this.selectedCancellationReasonTitleText = selectedCancellationReasonTitleText;
    }

    public String getCancellationNoteTitleText() {
        return cancellationNoteTitleText;
    }

    public void setCancellationNoteTitleText(String cancellationNoteTitleText) {
        this.cancellationNoteTitleText = cancellationNoteTitleText;
    }

    public String getRedeemRewardsTextV2() {
        return redeemRewardsTextV2;
    }

    public void setRedeemRewardsTextV2(String redeemRewardsTextV2) {
        this.redeemRewardsTextV2 = redeemRewardsTextV2;
    }

    public String getRewardsTextV2() {
        return rewardsTextV2;
    }

    public void setRewardsTextV2(String rewardsTextV2) {
        this.rewardsTextV2 = rewardsTextV2;
    }

    public String getYourRewardsTextV2() {
        return yourRewardsTextV2;
    }

    public void setYourRewardsTextV2(String yourRewardsTextV2) {
        this.yourRewardsTextV2 = yourRewardsTextV2;
    }

    public String getYourAvailableRewardsPointsTextV2() {
        return yourAvailableRewardsPointsTextV2;
    }

    public void setYourAvailableRewardsPointsTextV2(String yourAvailableRewardsPointsTextV2) {
        this.yourAvailableRewardsPointsTextV2 = yourAvailableRewardsPointsTextV2;
    }

    public String getRequireFieldMessage() {
        return requireFieldMessage;
    }

    public void setRequireFieldMessage(String requireFieldMessage) {
        this.requireFieldMessage = requireFieldMessage;
    }

    public String getValidNumberRequiredMessage() {
        return validNumberRequiredMessage;
    }

    public void setValidNumberRequiredMessage(String validNumberRequiredMessage) {
        this.validNumberRequiredMessage = validNumberRequiredMessage;
    }

    public String getVariantLbl() {
        return variantLbl;
    }

    public void setVariantLbl(String variantLbl) {
        this.variantLbl = variantLbl;
    }

    public String getPriceLbl() {
        return priceLbl;
    }

    public void setPriceLbl(String priceLbl) {
        this.priceLbl = priceLbl;
    }

    public String getOneTimePurchaseOnlyText() {
        return oneTimePurchaseOnlyText;
    }

    public void setOneTimePurchaseOnlyText(String oneTimePurchaseOnlyText) {
        this.oneTimePurchaseOnlyText = oneTimePurchaseOnlyText;
    }

    public String getRescheduleText() {
        return rescheduleText;
    }

    public String getPopUpSuccessMessage() {
        return popUpSuccessMessage;
    }

    public void setPopUpSuccessMessage(String popUpSuccessMessage) {
        this.popUpSuccessMessage = popUpSuccessMessage;
    }

    public String getPopUpErrorMessage() {
        return popUpErrorMessage;
    }

    public void setPopUpErrorMessage(String popUpErrorMessage) {
        this.popUpErrorMessage = popUpErrorMessage;
    }

    public void setRescheduleText(String rescheduleText) {
        this.rescheduleText = rescheduleText;
    }

    public String getOrderNowText() {
        return orderNowText;
    }

    public void setOrderNowText(String orderNowText) {
        this.orderNowText = orderNowText;
    }

    public String getUpcomingOrderPlaceNowAlertText() {
        return upcomingOrderPlaceNowAlertText;
    }

    public void setUpcomingOrderPlaceNowAlertText(String upcomingOrderPlaceNowAlertText) {
        this.upcomingOrderPlaceNowAlertText = upcomingOrderPlaceNowAlertText;
    }

    public String getUpcomingOrderSkipAlertText() {
        return upcomingOrderSkipAlertText;
    }

    public void setUpcomingOrderSkipAlertText(String upcomingOrderSkipAlertText) {
        this.upcomingOrderSkipAlertText = upcomingOrderSkipAlertText;
    }

    public String getDeliveryFrequencyText() {
        return deliveryFrequencyText;
    }

    public void setDeliveryFrequencyText(String deliveryFrequencyText) {
        this.deliveryFrequencyText = deliveryFrequencyText;
    }

    public String getEditDeliveryInternalText() {
        return editDeliveryInternalText;
    }

    public void setEditDeliveryInternalText(String editDeliveryInternalText) {
        this.editDeliveryInternalText = editDeliveryInternalText;
    }

    public String getMaxCycleText() {
        return maxCycleText;
    }

    public void setMaxCycleText(String maxCycleText) {
        this.maxCycleText = maxCycleText;
    }

    public String getMinCycleText() {
        return minCycleText;
    }

    public void setMinCycleText(String minCycleText) {
        this.minCycleText = minCycleText;
    }

    public String getSelectProductToAdd() {
        return selectProductToAdd;
    }

    public void setSelectProductToAdd(String selectProductToAdd) {
        this.selectProductToAdd = selectProductToAdd;
    }

    public String getSearchProductBtnText() {
        return searchProductBtnText;
    }

    public void setSearchProductBtnText(String searchProductBtnText) {
        this.searchProductBtnText = searchProductBtnText;
    }

    public String getAreyousureCommonMessageText() {
        return areyousureCommonMessageText;
    }

    public void setAreyousureCommonMessageText(String areyousureCommonMessageText) {
        this.areyousureCommonMessageText = areyousureCommonMessageText;
    }

    public String getEditCommonText() {
        return editCommonText;
    }

    public void setEditCommonText(String editCommonText) {
        this.editCommonText = editCommonText;
    }

    public String getViewMoreText() {
        return viewMoreText;
    }

    public void setViewMoreText(String viewMoreText) {
        this.viewMoreText = viewMoreText;
    }

    public String getVariantLblText() {
        return variantLblText;
    }

    public void setVariantLblText(String variantLblText) {
        this.variantLblText = variantLblText;
    }

    public String getTotalLblText() {
        return totalLblText;
    }

    public void setTotalLblText(String totalLblText) {
        this.totalLblText = totalLblText;
    }

    public String getDeleteProductTitleText() {
        return deleteProductTitleText;
    }

    public void setDeleteProductTitleText(String deleteProductTitleText) {
        this.deleteProductTitleText = deleteProductTitleText;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }

    public String getProductLblText() {
        return productLblText;
    }

    public void setProductLblText(String productLblText) {
        this.productLblText = productLblText;
    }

    public String getHasBeenRemovedText() {
        return hasBeenRemovedText;
    }

    public void setHasBeenRemovedText(String hasBeenRemovedText) {
        this.hasBeenRemovedText = hasBeenRemovedText;
    }

    public String getOrderTotalText() {
        return orderTotalText;
    }

    public void setOrderTotalText(String orderTotalText) {
        this.orderTotalText = orderTotalText;
    }

    public String getAddDiscountCodeText() {
        return addDiscountCodeText;
    }

    public void setAddDiscountCodeText(String addDiscountCodeText) {
        this.addDiscountCodeText = addDiscountCodeText;
    }

    public String getAddDiscountCodeAlertText() {
        return addDiscountCodeAlertText;
    }

    public void setAddDiscountCodeAlertText(String addDiscountCodeAlertText) {
        this.addDiscountCodeAlertText = addDiscountCodeAlertText;
    }

    public String getRemoveDiscountCodeAlertText() {
        return removeDiscountCodeAlertText;
    }

    public void setRemoveDiscountCodeAlertText(String removeDiscountCodeAlertText) {
        this.removeDiscountCodeAlertText = removeDiscountCodeAlertText;
    }

    public String getShopPayLblText() {
        return shopPayLblText;
    }

    public void setShopPayLblText(String shopPayLblText) {
        this.shopPayLblText = shopPayLblText;
    }

    public String getPaypalLblText() {
        return paypalLblText;
    }

    public void setPaypalLblText(String paypalLblText) {
        this.paypalLblText = paypalLblText;
    }

    public String getUnknownPaymentReachoutUsText() {
        return unknownPaymentReachoutUsText;
    }

    public void setUnknownPaymentReachoutUsText(String unknownPaymentReachoutUsText) {
        this.unknownPaymentReachoutUsText = unknownPaymentReachoutUsText;
    }

    public String getAddToOrderLabelText() {
        return addToOrderLabelText;
    }

    public void setAddToOrderLabelText(String addToOrderLabelText) {
        this.addToOrderLabelText = addToOrderLabelText;
    }

    public String getUpcomingTabTitle() {
        return upcomingTabTitle;
    }

    public void setUpcomingTabTitle(String upcomingTabTitle) {
        this.upcomingTabTitle = upcomingTabTitle;
    }

    public String getScheduledTabTitle() {
        return scheduledTabTitle;
    }

    public void setScheduledTabTitle(String scheduledTabTitle) {
        this.scheduledTabTitle = scheduledTabTitle;
    }

    public String getHistoryTabTitle() {
        return historyTabTitle;
    }

    public void setHistoryTabTitle(String historyTabTitle) {
        this.historyTabTitle = historyTabTitle;
    }

    public String getNoOrderNotAvailableMessage() {
        return noOrderNotAvailableMessage;
    }

    public void setNoOrderNotAvailableMessage(String noOrderNotAvailableMessage) {
        this.noOrderNotAvailableMessage = noOrderNotAvailableMessage;
    }

    public String getContinueText() {
        return continueText;
    }

    public void setContinueText(String continueText) {
        this.continueText = continueText;
    }

    public String getConfirmSwapText() {
        return confirmSwapText;
    }

    public void setConfirmSwapText(String confirmSwapText) {
        this.confirmSwapText = confirmSwapText;
    }

    public String getConfirmAddProduct() {
        return confirmAddProduct;
    }

    public void setConfirmAddProduct(String confirmAddProduct) {
        this.confirmAddProduct = confirmAddProduct;
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

    public String getOrderFrequencyText() {
        return orderFrequencyText;
    }

    public void setOrderFrequencyText(String orderFrequencyText) {
        this.orderFrequencyText = orderFrequencyText;
    }

    public String getTotalProductsText() {
        return totalProductsText;
    }

    public void setTotalProductsText(String totalProductsText) {
        this.totalProductsText = totalProductsText;
    }

    public String getNextOrderText() {
        return nextOrderText;
    }

    public void setNextOrderText(String nextOrderText) {
        this.nextOrderText = nextOrderText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCancelSubscriptionBtnText() {
        return cancelSubscriptionBtnText;
    }

    public void setCancelSubscriptionBtnText(String cancelSubscriptionBtnText) {
        this.cancelSubscriptionBtnText = cancelSubscriptionBtnText;
    }

    public String getNoSubscriptionMessage() {
        return noSubscriptionMessage;
    }

    public void setNoSubscriptionMessage(String noSubscriptionMessage) {
        this.noSubscriptionMessage = noSubscriptionMessage;
    }

    public String getSubscriptionNoText() {
        return subscriptionNoText;
    }

    public void setSubscriptionNoText(String subscriptionNoText) {
        this.subscriptionNoText = subscriptionNoText;
    }

    public String getUpdatePaymentMessage() {
        return updatePaymentMessage;
    }

    public void setUpdatePaymentMessage(String updatePaymentMessage) {
        this.updatePaymentMessage = updatePaymentMessage;
    }

    public String getCardLastFourDigitText() {
        return cardLastFourDigitText;
    }

    public void setCardLastFourDigitText(String cardLastFourDigitText) {
        this.cardLastFourDigitText = cardLastFourDigitText;
    }

    public String getCardExpiryText() {
        return cardExpiryText;
    }

    public void setCardExpiryText(String cardExpiryText) {
        this.cardExpiryText = cardExpiryText;
    }

    public String getCardHolderNameText() {
        return cardHolderNameText;
    }

    public void setCardHolderNameText(String cardHolderNameText) {
        this.cardHolderNameText = cardHolderNameText;
    }

    public String getCardTypeText() {
        return cardTypeText;
    }

    public void setCardTypeText(String cardTypeText) {
        this.cardTypeText = cardTypeText;
    }

    public String getPaymentMethodTypeText() {
        return paymentMethodTypeText;
    }

    public void setPaymentMethodTypeText(String paymentMethodTypeText) {
        this.paymentMethodTypeText = paymentMethodTypeText;
    }

    public String getCancelAccordionTitle() {
        return cancelAccordionTitle;
    }

    public void setCancelAccordionTitle(String cancelAccordionTitle) {
        this.cancelAccordionTitle = cancelAccordionTitle;
    }

    public String getPaymentDetailAccordionTitle() {
        return paymentDetailAccordionTitle;
    }

    public void setPaymentDetailAccordionTitle(String paymentDetailAccordionTitle) {
        this.paymentDetailAccordionTitle = paymentDetailAccordionTitle;
    }

    public String getUpcomingOrderAccordionTitle() {
        return upcomingOrderAccordionTitle;
    }

    public void setUpcomingOrderAccordionTitle(String upcomingOrderAccordionTitle) {
        this.upcomingOrderAccordionTitle = upcomingOrderAccordionTitle;
    }

    public String getPaymentInfoText() {
        return paymentInfoText;
    }

    public void setPaymentInfoText(String paymentInfoText) {
        this.paymentInfoText = paymentInfoText;
    }

    public String getUpdatePaymentBtnText() {
        return updatePaymentBtnText;
    }

    public void setUpdatePaymentBtnText(String updatePaymentBtnText) {
        this.updatePaymentBtnText = updatePaymentBtnText;
    }

    public String getNextOrderDateLbl() {
        return nextOrderDateLbl;
    }

    public void setNextOrderDateLbl(String nextOrderDateLbl) {
        this.nextOrderDateLbl = nextOrderDateLbl;
    }

    public String getStatusLbl() {
        return statusLbl;
    }

    public void setStatusLbl(String statusLbl) {
        this.statusLbl = statusLbl;
    }

    public String getQuantityLbl() {
        return quantityLbl;
    }

    public void setQuantityLbl(String quantityLbl) {
        this.quantityLbl = quantityLbl;
    }

    public String getAmountLbl() {
        return amountLbl;
    }

    public void setAmountLbl(String amountLbl) {
        this.amountLbl = amountLbl;
    }

    public String getOrderNoLbl() {
        return orderNoLbl;
    }

    public void setOrderNoLbl(String orderNoLbl) {
        this.orderNoLbl = orderNoLbl;
    }

    public String getEditFrequencyBtnText() {
        return editFrequencyBtnText;
    }

    public void setEditFrequencyBtnText(String editFrequencyBtnText) {
        this.editFrequencyBtnText = editFrequencyBtnText;
    }

    public String getCancelFreqBtnText() {
        return cancelFreqBtnText;
    }

    public void setCancelFreqBtnText(String cancelFreqBtnText) {
        this.cancelFreqBtnText = cancelFreqBtnText;
    }

    public String getUpdateFreqBtnText() {
        return updateFreqBtnText;
    }

    public void setUpdateFreqBtnText(String updateFreqBtnText) {
        this.updateFreqBtnText = updateFreqBtnText;
    }

    public Boolean isPauseResumeSub() {
        return pauseResumeSub;
    }

    public void setPauseResumeSub(Boolean pauseResumeSub) {
        this.pauseResumeSub = pauseResumeSub;
    }

    public Boolean isChangeNextOrderDate() {
        return changeNextOrderDate;
    }

    public void setChangeNextOrderDate(Boolean changeNextOrderDate) {
        this.changeNextOrderDate = changeNextOrderDate;
    }

    public Boolean isCancelSub() {
        return cancelSub;
    }

    public void setCancelSub(Boolean cancelSub) {
        this.cancelSub = cancelSub;
    }

    public Boolean isChangeOrderFrequency() {
        return changeOrderFrequency;
    }

    public void setChangeOrderFrequency(Boolean changeOrderFrequency) {
        this.changeOrderFrequency = changeOrderFrequency;
    }

    public Boolean isCreateAdditionalOrder() {
        return createAdditionalOrder;
    }

    public void setCreateAdditionalOrder(Boolean createAdditionalOrder) {
        this.createAdditionalOrder = createAdditionalOrder;
    }

    public String getManageSubscriptionButtonText() {
        return manageSubscriptionButtonText;
    }

    public void setManageSubscriptionButtonText(String manageSubscriptionButtonText) {
        this.manageSubscriptionButtonText = manageSubscriptionButtonText;
    }

    public String getEditChangeOrderBtnText() {
        return editChangeOrderBtnText;
    }

    public void setEditChangeOrderBtnText(String editChangeOrderBtnText) {
        this.editChangeOrderBtnText = editChangeOrderBtnText;
    }

    public String getCancelChangeOrderBtnText() {
        return cancelChangeOrderBtnText;
    }

    public void setCancelChangeOrderBtnText(String cancelChangeOrderBtnText) {
        this.cancelChangeOrderBtnText = cancelChangeOrderBtnText;
    }

    public String getUpdateChangeOrderBtnText() {
        return updateChangeOrderBtnText;
    }

    public void setUpdateChangeOrderBtnText(String updateChangeOrderBtnText) {
        this.updateChangeOrderBtnText = updateChangeOrderBtnText;
    }

    public String getEditProductButtonText() {
        return editProductButtonText;
    }

    public void setEditProductButtonText(String editProductButtonText) {
        this.editProductButtonText = editProductButtonText;
    }

    public String getDeleteButtonText() {
        return deleteButtonText;
    }

    public void setDeleteButtonText(String deleteButtonText) {
        this.deleteButtonText = deleteButtonText;
    }

    public String getUpdateButtonText() {
        return updateButtonText;
    }

    public void setUpdateButtonText(String updateButtonText) {
        this.updateButtonText = updateButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public String getAddProductButtonText() {
        return addProductButtonText;
    }

    public void setAddProductButtonText(String addProductButtonText) {
        this.addProductButtonText = addProductButtonText;
    }

    public String getAddProductLabelText() {
        return addProductLabelText;
    }

    public void setAddProductLabelText(String addProductLabelText) {
        this.addProductLabelText = addProductLabelText;
    }

    public String getActiveBadgeText() {
        return activeBadgeText;
    }

    public void setActiveBadgeText(String activeBadgeText) {
        this.activeBadgeText = activeBadgeText;
    }

    public String getCloseBadgeText() {
        return closeBadgeText;
    }

    public void setCloseBadgeText(String closeBadgeText) {
        this.closeBadgeText = closeBadgeText;
    }

    public String getSkipOrderButtonText() {
        return skipOrderButtonText;
    }

    public void setSkipOrderButtonText(String skipOrderButtonText) {
        this.skipOrderButtonText = skipOrderButtonText;
    }

    public String getProductLabelText() {
        return productLabelText;
    }

    public void setProductLabelText(String productLabelText) {
        this.productLabelText = productLabelText;
    }

    public String getSeeMoreDetailsText() {
        return seeMoreDetailsText;
    }

    public void setSeeMoreDetailsText(String seeMoreDetailsText) {
        this.seeMoreDetailsText = seeMoreDetailsText;
    }

    public String getHideDetailsText() {
        return hideDetailsText;
    }

    public void setHideDetailsText(String hideDetailsText) {
        this.hideDetailsText = hideDetailsText;
    }

    public String getProductInSubscriptionText() {
        return productInSubscriptionText;
    }

    public void setProductInSubscriptionText(String productInSubscriptionText) {
        this.productInSubscriptionText = productInSubscriptionText;
    }

    public String getEditQuantityLabelText() {
        return EditQuantityLabelText;
    }

    public void setEditQuantityLabelText(String EditQuantityLabelText) {
        this.EditQuantityLabelText = EditQuantityLabelText;
    }

    public String getSubTotalLabelText() {
        return subTotalLabelText;
    }

    public void setSubTotalLabelText(String subTotalLabelText) {
        this.subTotalLabelText = subTotalLabelText;
    }

    public String getPaymentNotificationText() {
        return paymentNotificationText;
    }

    public void setPaymentNotificationText(String paymentNotificationText) {
        this.paymentNotificationText = paymentNotificationText;
    }

    public Boolean isEditProductFlag() {
        return editProductFlag;
    }

    public void setEditProductFlag(Boolean editProductFlag) {
        this.editProductFlag = editProductFlag;
    }

    public Boolean isDeleteProductFlag() {
        return deleteProductFlag;
    }

    public void setDeleteProductFlag(Boolean deleteProductFlag) {
        this.deleteProductFlag = deleteProductFlag;
    }

    public Boolean isShowShipment() {
        return showShipment;
    }

    public void setShowShipment(Boolean showShipment) {
        this.showShipment = showShipment;
    }

    public Boolean getMagicLinkEmailFlag() {
        return magicLinkEmailFlag;
    }

    public void setMagicLinkEmailFlag(Boolean magicLinkEmailFlag) {
        this.magicLinkEmailFlag = magicLinkEmailFlag;
    }

    public Boolean isAddAdditionalProduct() {
        return addAdditionalProduct;
    }

    public void setAddAdditionalProduct(Boolean addAdditionalProduct) {
        this.addAdditionalProduct = addAdditionalProduct;
    }

    public String getSuccessText() {
        return successText;
    }

    public void setSuccessText(String successText) {
        this.successText = successText;
    }

    public String getCancelSubscriptionConfirmPrepaidText() {
        return cancelSubscriptionConfirmPrepaidText;
    }

    public void setCancelSubscriptionConfirmPrepaidText(String cancelSubscriptionConfirmPrepaidText) {
        this.cancelSubscriptionConfirmPrepaidText = cancelSubscriptionConfirmPrepaidText;
    }

    public String getCancelSubscriptionConfirmPayAsYouGoText() {
        return cancelSubscriptionConfirmPayAsYouGoText;
    }

    public void setCancelSubscriptionConfirmPayAsYouGoText(String cancelSubscriptionConfirmPayAsYouGoText) {
        this.cancelSubscriptionConfirmPayAsYouGoText = cancelSubscriptionConfirmPayAsYouGoText;
    }

    public String getCancelSubscriptionPrepaidButtonText() {
        return cancelSubscriptionPrepaidButtonText;
    }

    public void setCancelSubscriptionPrepaidButtonText(String cancelSubscriptionPrepaidButtonText) {
        this.cancelSubscriptionPrepaidButtonText = cancelSubscriptionPrepaidButtonText;
    }

    public String getCancelSubscriptionPayAsYouGoButtonText() {
        return cancelSubscriptionPayAsYouGoButtonText;
    }

    public void setCancelSubscriptionPayAsYouGoButtonText(String cancelSubscriptionPayAsYouGoButtonText) {
        this.cancelSubscriptionPayAsYouGoButtonText = cancelSubscriptionPayAsYouGoButtonText;
    }

    public String getUpcomingFulfillmentText() {
        return upcomingFulfillmentText;
    }

    public void setUpcomingFulfillmentText(String upcomingFulfillmentText) {
        this.upcomingFulfillmentText = upcomingFulfillmentText;
    }

    public String getCreditCardText() {
        return creditCardText;
    }

    public void setCreditCardText(String creditCardText) {
        this.creditCardText = creditCardText;
    }

    public String getEndingWithText() {
        return endingWithText;
    }

    public void setEndingWithText(String endingWithText) {
        this.endingWithText = endingWithText;
    }

    public String getWeekText() {
        return weekText;
    }

    public void setWeekText(String weekText) {
        this.weekText = weekText;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getMonthText() {
        return monthText;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public String getYearText() {
        return yearText;
    }

    public void setYearText(String yearText) {
        this.yearText = yearText;
    }

    public String getSkipBadgeText() {
        return skipBadgeText;
    }

    public void setSkipBadgeText(String skipBadgeText) {
        this.skipBadgeText = skipBadgeText;
    }

    public String getQueueBadgeText() {
        return queueBadgeText;
    }

    public void setQueueBadgeText(String queueBadgeText) {
        this.queueBadgeText = queueBadgeText;
    }

    public String getCustomerPortalSettingJson() {
        return customerPortalSettingJson;
    }

    public void setCustomerPortalSettingJson(String customerPortalSettingJson) {
        this.customerPortalSettingJson = customerPortalSettingJson;
    }

    public String getShippingLabelText() {
        return shippingLabelText;
    }

    public void setShippingLabelText(String shippingLabelText) {
        this.shippingLabelText = shippingLabelText;
    }


    public String getFailureText() {
        return failureText;
    }

    public void setFailureText(String failureText) {
        this.failureText = failureText;
    }

    public String getSendEmailText() {
        return sendEmailText;
    }

    public void setSendEmailText(String sendEmailText) {
        this.sendEmailText = sendEmailText;
    }

    public String getChooseDifferentProductActionText() {
        return chooseDifferentProductActionText;
    }

    public void setChooseDifferentProductActionText(String chooseDifferentProductActionText) {
        this.chooseDifferentProductActionText = chooseDifferentProductActionText;
    }

    public String getChooseDifferentProductText() {
        return chooseDifferentProductText;
    }

    public void setChooseDifferentProductText(String chooseDifferentProductText) {
        this.chooseDifferentProductText = chooseDifferentProductText;
    }

    public String getConfirmSkipFulfillmentBtnText() {
        return confirmSkipFulfillmentBtnText;
    }

    public void setConfirmSkipFulfillmentBtnText(String confirmSkipFulfillmentBtnText) {
        this.confirmSkipFulfillmentBtnText = confirmSkipFulfillmentBtnText;
    }

    public String getConfirmSkipOrder() {
        return confirmSkipOrder;
    }

    public void setConfirmSkipOrder(String confirmSkipOrder) {
        this.confirmSkipOrder = confirmSkipOrder;
    }

    public String getSkipFulfillmentButtonText() {
        return skipFulfillmentButtonText;
    }

    public void setSkipFulfillmentButtonText(String skipFulfillmentButtonText) {
        this.skipFulfillmentButtonText = skipFulfillmentButtonText;
    }

    public String getConfirmCommonText() {
        return confirmCommonText;
    }

    public void setConfirmCommonText(String confirmCommonText) {
        this.confirmCommonText = confirmCommonText;
    }

    public String getOrderNowDescriptionText() {
        return orderNowDescriptionText;
    }

    public void setOrderNowDescriptionText(String orderNowDescriptionText) {
        this.orderNowDescriptionText = orderNowDescriptionText;
    }

    public String getDiscountDetailsTitleText() {
        return discountDetailsTitleText;
    }

    public void setDiscountDetailsTitleText(String discountDetailsTitleText) {
        this.discountDetailsTitleText = discountDetailsTitleText;
    }

    public String getEmailAddressText() {
        return emailAddressText;
    }

    public void setEmailAddressText(String emailAddressText) {
        this.emailAddressText = emailAddressText;
    }

    public String getEmailMagicLinkText() {
        return emailMagicLinkText;
    }

    public void setEmailMagicLinkText(String emailMagicLinkText) {
        this.emailMagicLinkText = emailMagicLinkText;
    }

    public String getRetriveMagicLinkText() {
        return retriveMagicLinkText;
    }

    public void setRetriveMagicLinkText(String retriveMagicLinkText) {
        this.retriveMagicLinkText = retriveMagicLinkText;
    }

    public String getValidEmailMessage() {
        return validEmailMessage;
    }

    public void setValidEmailMessage(String validEmailMessage) {
        this.validEmailMessage = validEmailMessage;
    }

    public String getSaveButtonText() {
        return saveButtonText;
    }

    public void setSaveButtonText(String saveButtonText) {
        this.saveButtonText = saveButtonText;
    }

    public String getOrderDateText() {
        return orderDateText;
    }

    public void setOrderDateText(String orderDateText) {
        this.orderDateText = orderDateText;
    }

    public String getAddress1LabelText() {
        return address1LabelText;
    }

    public void setAddress1LabelText(String address1LabelText) {
        this.address1LabelText = address1LabelText;
    }

    public String getAddress2LabelText() {
        return address2LabelText;
    }

    public void setAddress2LabelText(String address2LabelText) {
        this.address2LabelText = address2LabelText;
    }

    public String getCompanyLabelText() {
        return companyLabelText;
    }

    public void setCompanyLabelText(String companyLabelText) {
        this.companyLabelText = companyLabelText;
    }

    public String getCityLabelText() {
        return cityLabelText;
    }

    public void setCityLabelText(String cityLabelText) {
        this.cityLabelText = cityLabelText;
    }

    public String getCountryLabelText() {
        return countryLabelText;
    }

    public void setCountryLabelText(String countryLabelText) {
        this.countryLabelText = countryLabelText;
    }

    public String getFirstNameLabelText() {
        return firstNameLabelText;
    }

    public void setFirstNameLabelText(String firstNameLabelText) {
        this.firstNameLabelText = firstNameLabelText;
    }

    public String getLastNameLabelText() {
        return lastNameLabelText;
    }

    public void setLastNameLabelText(String lastNameLabelText) {
        this.lastNameLabelText = lastNameLabelText;
    }

    public String getPhoneLabelText() {
        return phoneLabelText;
    }

    public void setPhoneLabelText(String phoneLabelText) {
        this.phoneLabelText = phoneLabelText;
    }

    public String getProvinceLabelText() {
        return provinceLabelText;
    }

    public void setProvinceLabelText(String provinceLabelText) {
        this.provinceLabelText = provinceLabelText;
    }

    public String getZipLabelText() {
        return zipLabelText;
    }

    public void setZipLabelText(String zipLabelText) {
        this.zipLabelText = zipLabelText;
    }

    public String getAddressHeaderTitleText() {
        return addressHeaderTitleText;
    }

    public void setAddressHeaderTitleText(String addressHeaderTitleText) {
        this.addressHeaderTitleText = addressHeaderTitleText;
    }

    public Boolean getChangeShippingAddressFlag() {
        return changeShippingAddressFlag;
    }

    public void setChangeShippingAddressFlag(Boolean changeShippingAddressFlag) {
        this.changeShippingAddressFlag = changeShippingAddressFlag;
    }

    public String getUpdateEditShippingButtonText() {
        return updateEditShippingButtonText;
    }

    public void setUpdateEditShippingButtonText(String updateEditShippingButtonText) {
        this.updateEditShippingButtonText = updateEditShippingButtonText;
    }

    public String getCancelEditShippingButtonText() {
        return cancelEditShippingButtonText;
    }

    public void setCancelEditShippingButtonText(String cancelEditShippingButtonText) {
        this.cancelEditShippingButtonText = cancelEditShippingButtonText;
    }

    public String getPauseSubscriptionText() {
        return pauseSubscriptionText;
    }

    public void setPauseSubscriptionText(String pauseSubscriptionText) {
        this.pauseSubscriptionText = pauseSubscriptionText;
    }

    public String getResumeSubscriptionText() {
        return resumeSubscriptionText;
    }

    public void setResumeSubscriptionText(String resumeSubscriptionText) {
        this.resumeSubscriptionText = resumeSubscriptionText;
    }

    public String getPauseBadgeText() {
        return pauseBadgeText;
    }

    public void setPauseBadgeText(String pauseBadgeText) {
        this.pauseBadgeText = pauseBadgeText;
    }

    public String getDiscountNoteTitle() {
        return discountNoteTitle;
    }

    public void setDiscountNoteTitle(String discountNoteTitle) {
        this.discountNoteTitle = discountNoteTitle;
    }

    public String getInitialDiscountNoteDescription() {
        return initialDiscountNoteDescription;
    }

    public void setInitialDiscountNoteDescription(String initialDiscountNoteDescription) {
        this.initialDiscountNoteDescription = initialDiscountNoteDescription;
    }

    public String getAfterCycleDiscountNoteDescription() {
        return afterCycleDiscountNoteDescription;
    }

    public void setAfterCycleDiscountNoteDescription(String afterCycleDiscountNoteDescription) {
        this.afterCycleDiscountNoteDescription = afterCycleDiscountNoteDescription;
    }

    public String getProductRemovedTooltip() {
        return productRemovedTooltip;
    }

    public void setProductRemovedTooltip(String productRemovedTooltip) {
        this.productRemovedTooltip = productRemovedTooltip;
    }

    public String getDeliveryPriceText() {
        return deliveryPriceText;
    }

    public void setDeliveryPriceText(String deliveryPriceText) {
        this.deliveryPriceText = deliveryPriceText;
    }

    public String getShippingOptionText() {
        return shippingOptionText;
    }

    public void setShippingOptionText(String shippingOptionText) {
        this.shippingOptionText = shippingOptionText;
    }

    public Boolean getPauseResumeSub() {
        return pauseResumeSub;
    }

    public Boolean getChangeNextOrderDate() {
        return changeNextOrderDate;
    }

    public Boolean getCancelSub() {
        return cancelSub;
    }

    public Boolean getChangeOrderFrequency() {
        return changeOrderFrequency;
    }

    public Boolean getCreateAdditionalOrder() {
        return createAdditionalOrder;
    }

    public Boolean getEditProductFlag() {
        return editProductFlag;
    }

    public Boolean getDeleteProductFlag() {
        return deleteProductFlag;
    }

    public Boolean getShowShipment() {
        return showShipment;
    }

    public Boolean getAddAdditionalProduct() {
        return addAdditionalProduct;
    }

    public String getNextDeliveryDate() {
        return nextDeliveryDate;
    }

    public void setNextDeliveryDate(String nextDeliveryDate) {
        this.nextDeliveryDate = nextDeliveryDate;
    }

    public String getEveryLabelText() {
        return everyLabelText;
    }

    public void setEveryLabelText(String everyLabelText) {
        this.everyLabelText = everyLabelText;
    }

    public Boolean getOrderNoteFlag() {
        return orderNoteFlag;
    }

    public void setOrderNoteFlag(Boolean orderNoteFlag) {
        this.orderNoteFlag = orderNoteFlag;
    }

    public String getOrderNoteText() {
        return orderNoteText;
    }

    public void setOrderNoteText(String orderNoteText) {
        this.orderNoteText = orderNoteText;
    }

    public Boolean isUseUrlWithCustomerId() {
        return useUrlWithCustomerId;
    }

    public void setUseUrlWithCustomerId(Boolean useUrlWithCustomerId) {
        this.useUrlWithCustomerId = useUrlWithCustomerId;
    }

    public String getExpiredTokenText() {
        return expiredTokenText;
    }

    public void setExpiredTokenText(String expiredTokenText) {
        this.expiredTokenText = expiredTokenText;
    }

    public String getPortalLoginLinkText() {
        return portalLoginLinkText;
    }

    public void setPortalLoginLinkText(String portalLoginLinkText) {
        this.portalLoginLinkText = portalLoginLinkText;
    }

    public String getLocaleDate() {
        return localeDate;
    }

    public void setLocaleDate(String localeDate) {
        this.localeDate = localeDate;
    }

    public ProductSelectionOption getProductSelectionOption() {
        return productSelectionOption;
    }

    public void setProductSelectionOption(ProductSelectionOption productSelectionOption) {
        this.productSelectionOption = productSelectionOption;
    }

    public Boolean isIncludeOutOfStockProduct() {
        return includeOutOfStockProduct;
    }

    public void setIncludeOutOfStockProduct(Boolean includeOutOfStockProduct) {
        this.includeOutOfStockProduct = includeOutOfStockProduct;
    }

    public String getCustomerIdText() {
        return customerIdText;
    }

    public void setCustomerIdText(String customerIdText) {
        this.customerIdText = customerIdText;
    }

    public String getHelloNameText() {
        return helloNameText;
    }

    public void setHelloNameText(String helloNameText) {
        this.helloNameText = helloNameText;
    }

    public String getGoBackButtonText() {
        return goBackButtonText;
    }

    public void setGoBackButtonText(String goBackButtonText) {
        this.goBackButtonText = goBackButtonText;
    }

    public String getChangeVariantLabelText() {
        return changeVariantLabelText;
    }

    public void setChangeVariantLabelText(String changeVariantLabelText) {
        this.changeVariantLabelText = changeVariantLabelText;
    }

    public String getProvinceCodeLabelText() {
        return provinceCodeLabelText;
    }

    public void setProvinceCodeLabelText(String provinceCodeLabelText) {
        this.provinceCodeLabelText = provinceCodeLabelText;
    }

    public String getCountryCodeLabelText() {
        return countryCodeLabelText;
    }

    public void setCountryCodeLabelText(String countryCodeLabelText) {
        this.countryCodeLabelText = countryCodeLabelText;
    }

    public String getPleaseWaitLoaderText() {
        return pleaseWaitLoaderText;
    }

    public void setPleaseWaitLoaderText(String pleaseWaitLoaderText) {
        this.pleaseWaitLoaderText = pleaseWaitLoaderText;
    }


    public String getOpenBadgeText() {
        return openBadgeText;
    }

    public void setOpenBadgeText(String openBadgeText) {
        this.openBadgeText = openBadgeText;
    }

    public String getCancelSubscriptionMinimumBillingIterationsMessage() {
        return cancelSubscriptionMinimumBillingIterationsMessage;
    }

    public void setCancelSubscriptionMinimumBillingIterationsMessage(String cancelSubscriptionMinimumBillingIterationsMessage) {
        this.cancelSubscriptionMinimumBillingIterationsMessage = cancelSubscriptionMinimumBillingIterationsMessage;
    }

    public String getTopHtml() {
        return topHtml;
    }

    public void setTopHtml(String topHtml) {
        this.topHtml = topHtml;
    }

    public String getBottomHtml() {
        return bottomHtml;
    }

    public void setBottomHtml(String bottomHtml) {
        this.bottomHtml = bottomHtml;
    }

    public Boolean isUpdateShipmentBillingDate() {
        return updateShipmentBillingDate;
    }

    public void setUpdateShipmentBillingDate(Boolean updateShipmentBillingDate) {
        this.updateShipmentBillingDate = updateShipmentBillingDate;
    }

    public String getDiscountCodeText() {
        return discountCodeText;
    }

    public void setDiscountCodeText(String discountCodeText) {
        this.discountCodeText = discountCodeText;
    }

    public String getDiscountCodeApplyButtonText() {
        return discountCodeApplyButtonText;
    }

    public void setDiscountCodeApplyButtonText(String discountCodeApplyButtonText) {
        this.discountCodeApplyButtonText = discountCodeApplyButtonText;
    }

    public Boolean isDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(Boolean discountCode) {
        this.discountCode = discountCode;
    }

    public Boolean getApplySellingPlanBasedDiscount() {
        return applySellingPlanBasedDiscount;
    }

    public void setApplySellingPlanBasedDiscount(Boolean applySellingPlanBasedDiscount) {
        this.applySellingPlanBasedDiscount = applySellingPlanBasedDiscount;
    }

    public Boolean getApplySubscriptionDiscountForOtp() {
        return applySubscriptionDiscountForOtp;
    }

    public void setApplySubscriptionDiscountForOtp(Boolean applySubscriptionDiscountForOtp) {
        this.applySubscriptionDiscountForOtp = applySubscriptionDiscountForOtp;
    }

    public Boolean isApplySubscriptionDiscount() {
        return applySubscriptionDiscount;
    }

    public void setApplySubscriptionDiscount(Boolean applySubscriptionDiscount) {
        this.applySubscriptionDiscount = applySubscriptionDiscount;
    }

    public Boolean getRemoveDiscountCodeAutomatically() {
        return removeDiscountCodeAutomatically;
    }

    public void setRemoveDiscountCodeAutomatically(Boolean removeDiscountCodeAutomatically) {
        this.removeDiscountCodeAutomatically = removeDiscountCodeAutomatically;
    }

    public SubscriptionDiscountTypeUnit getSubscriptionDiscountTypeUnit() {
        return subscriptionDiscountTypeUnit;
    }

    public void setSubscriptionDiscountTypeUnit(SubscriptionDiscountTypeUnit subscriptionDiscountTypeUnit) {
        this.subscriptionDiscountTypeUnit = subscriptionDiscountTypeUnit;
    }

    public Double getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(Double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public String getUpSellMessage() {
        return upSellMessage;
    }

    public void setUpSellMessage(String upSellMessage) {
        this.upSellMessage = upSellMessage;
    }

    public Boolean isFreezeOrderTillMinCycle() {
        return freezeOrderTillMinCycle;
    }

    public void setFreezeOrderTillMinCycle(Boolean freezeOrderTillMinCycle) {
        this.freezeOrderTillMinCycle = freezeOrderTillMinCycle;
    }

    public String getFreezeUpdateSubscriptionMessage() {
        return freezeUpdateSubscriptionMessage;
    }

    public void setFreezeUpdateSubscriptionMessage(String freezeUpdateSubscriptionMessage) {
        this.freezeUpdateSubscriptionMessage = freezeUpdateSubscriptionMessage;
    }

    public String getSubscriptionContractFreezeMessage() {
        return subscriptionContractFreezeMessage;
    }

    public void setSubscriptionContractFreezeMessage(String subscriptionContractFreezeMessage) {
        this.subscriptionContractFreezeMessage = subscriptionContractFreezeMessage;
    }

    public String getPreventCancellationBeforeDaysMessage() {
        return preventCancellationBeforeDaysMessage;
    }

    public void setPreventCancellationBeforeDaysMessage(String preventCancellationBeforeDaysMessage) {
        this.preventCancellationBeforeDaysMessage = preventCancellationBeforeDaysMessage;
    }


    public Boolean getOfferDiscountOnCancellation() {
        return offerDiscountOnCancellation;
    }

    public void setOfferDiscountOnCancellation(Boolean offerDiscountOnCancellation) {
        this.offerDiscountOnCancellation = offerDiscountOnCancellation;
    }

    public Boolean getEnableSkipFulFillment() {
        return enableSkipFulFillment;
    }

    public void setEnableSkipFulFillment(Boolean enableSkipFulFillment) {
        this.enableSkipFulFillment = enableSkipFulFillment;
    }

    public String getDiscountPercentageOnCancellation() {
        return discountPercentageOnCancellation;
    }

    public void setDiscountPercentageOnCancellation(String discountPercentageOnCancellation) {
        this.discountPercentageOnCancellation = discountPercentageOnCancellation;
    }

    public String getDiscountMessageOnCancellation() {
        return discountMessageOnCancellation;
    }

    public void setDiscountMessageOnCancellation(String discountMessageOnCancellation) {
        this.discountMessageOnCancellation = discountMessageOnCancellation;
    }

    public String getDiscountRecurringCycleLimitOnCancellation() {
        return discountRecurringCycleLimitOnCancellation;
    }

    public void setDiscountRecurringCycleLimitOnCancellation(String discountRecurringCycleLimitOnCancellation) {
        this.discountRecurringCycleLimitOnCancellation = discountRecurringCycleLimitOnCancellation;
    }

    public String getFrequencyChangeWarningTitle() {
        return frequencyChangeWarningTitle;
    }

    public String getDiscountCouponRemoveText() {
        return discountCouponRemoveText;
    }

    public void setDiscountCouponRemoveText(String discountCouponRemoveText) {
        this.discountCouponRemoveText = discountCouponRemoveText;
    }

    public String getPleaseSelectText() {
        return pleaseSelectText;
    }

    public String getShippingAddressNotAvailableText() {
        return shippingAddressNotAvailableText;
    }

    public String getSellingPlanNameText() {
        return sellingPlanNameText;
    }

    public void setSellingPlanNameText(String sellingPlanNameText) {
        this.sellingPlanNameText = sellingPlanNameText;
    }

    public void setShippingAddressNotAvailableText(String shippingAddressNotAvailableText) {
        this.shippingAddressNotAvailableText = shippingAddressNotAvailableText;
    }

    public String getDiscountCouponNotAppliedText() {
        return discountCouponNotAppliedText;
    }

    public void setDiscountCouponNotAppliedText(String discountCouponNotAppliedText) {
        this.discountCouponNotAppliedText = discountCouponNotAppliedText;
    }

    public String getShopPayPaymentUpdateText() {
        return shopPayPaymentUpdateText;
    }

    public void setShopPayPaymentUpdateText(String shopPayPaymentUpdateText) {
        this.shopPayPaymentUpdateText = shopPayPaymentUpdateText;
    }

    public void setPleaseSelectText(String pleaseSelectText) {
        this.pleaseSelectText = pleaseSelectText;
    }

    public void setFrequencyChangeWarningTitle(String frequencyChangeWarningTitle) {
        this.frequencyChangeWarningTitle = frequencyChangeWarningTitle;
    }

    public String getFrequencyChangeWarningDescription() {
        return frequencyChangeWarningDescription;
    }

    public void setFrequencyChangeWarningDescription(String frequencyChangeWarningDescription) {
        this.frequencyChangeWarningDescription = frequencyChangeWarningDescription;
    }

    public Boolean isAddOneTimeProduct() {
        return addOneTimeProduct;
    }

    public void setAddOneTimeProduct(Boolean addOneTimeProduct) {
        this.addOneTimeProduct = addOneTimeProduct;
    }

    public Boolean isAllowOrderNow() {
        return allowOrderNow;
    }

    public void setAllowOrderNow(Boolean allowOrderNow) {
        this.allowOrderNow = allowOrderNow;
    }

    public String getSelectProductLabelText() {
        return selectProductLabelText;
    }

    public void setSelectProductLabelText(String selectProductLabelText) {
        this.selectProductLabelText = selectProductLabelText;
    }

    public String getPurchaseOptionLabelText() {
        return purchaseOptionLabelText;
    }

    public void setPurchaseOptionLabelText(String purchaseOptionLabelText) {
        this.purchaseOptionLabelText = purchaseOptionLabelText;
    }

    public String getFinishLabelText() {
        return finishLabelText;
    }

    public void setFinishLabelText(String finishLabelText) {
        this.finishLabelText = finishLabelText;
    }

    public String getNextBtnText() {
        return nextBtnText;
    }

    public void setNextBtnText(String nextBtnText) {
        this.nextBtnText = nextBtnText;
    }

    public String getPreviousBtnText() {
        return previousBtnText;
    }

    public void setPreviousBtnText(String previousBtnText) {
        this.previousBtnText = previousBtnText;
    }

    public String getCloseBtnText() {
        return closeBtnText;
    }

    public void setCloseBtnText(String closeBtnText) {
        this.closeBtnText = closeBtnText;
    }

    public String getDeleteConfirmationMsgText() {
        return deleteConfirmationMsgText;
    }

    public void setDeleteConfirmationMsgText(String deleteConfirmationMsgText) {
        this.deleteConfirmationMsgText = deleteConfirmationMsgText;
    }

    public String getDeleteMsgText() {
        return deleteMsgText;
    }

    public void setDeleteMsgText(String deleteMsgText) {
        this.deleteMsgText = deleteMsgText;
    }

    public String getYesBtnText() {
        return yesBtnText;
    }

    public void setYesBtnText(String yesBtnText) {
        this.yesBtnText = yesBtnText;
    }

    public String getNoBtnText() {
        return noBtnText;
    }

    public void setNoBtnText(String noBtnText) {
        this.noBtnText = noBtnText;
    }

    public String getOneTimePurchaseNoteText() {
        return oneTimePurchaseNoteText;
    }

    public void setOneTimePurchaseNoteText(String oneTimePurchaseNoteText) {
        this.oneTimePurchaseNoteText = oneTimePurchaseNoteText;
    }

    public String getClickHereText() {
        return clickHereText;
    }

    public void setClickHereText(String clickHereText) {
        this.clickHereText = clickHereText;
    }

    public String getProductAddMessageText() {
        return productAddMessageText;
    }

    public void setProductAddMessageText(String productAddMessageText) {
        this.productAddMessageText = productAddMessageText;
    }

    public String getChoosePurchaseOptionLabelText() {
        return choosePurchaseOptionLabelText;
    }

    public void setChoosePurchaseOptionLabelText(String choosePurchaseOptionLabelText) {
        this.choosePurchaseOptionLabelText = choosePurchaseOptionLabelText;
    }

    public String getOneTimePurchaseMessageText() {
        return oneTimePurchaseMessageText;
    }

    public void setOneTimePurchaseMessageText(String oneTimePurchaseMessageText) {
        this.oneTimePurchaseMessageText = oneTimePurchaseMessageText;
    }

    public String getContractUpdateMessageText() {
        return contractUpdateMessageText;
    }

    public void setContractUpdateMessageText(String contractUpdateMessageText) {
        this.contractUpdateMessageText = contractUpdateMessageText;
    }

    public String getOneTimePurchaseDisplayMessageText() {
        return oneTimePurchaseDisplayMessageText;
    }

    public void setOneTimePurchaseDisplayMessageText(String oneTimePurchaseDisplayMessageText) {
        this.oneTimePurchaseDisplayMessageText = oneTimePurchaseDisplayMessageText;
    }

    public String getAddProductFinishedMessageText() {
        return addProductFinishedMessageText;
    }

    public void setAddProductFinishedMessageText(String addProductFinishedMessageText) {
        this.addProductFinishedMessageText = addProductFinishedMessageText;
    }

    public String getContractErrorMessageText() {
        return contractErrorMessageText;
    }

    public void setContractErrorMessageText(String contractErrorMessageText) {
        this.contractErrorMessageText = contractErrorMessageText;
    }

    public String getAddToSubscriptionTitleCP() {
        return addToSubscriptionTitleCP;
    }

    public void setAddToSubscriptionTitleCP(String addToSubscriptionTitleCP) {
        this.addToSubscriptionTitleCP = addToSubscriptionTitleCP;
    }

    public String getOneTimePurchaseTitleCP() {
        return oneTimePurchaseTitleCP;
    }

    public void setOneTimePurchaseTitleCP(String oneTimePurchaseTitleCP) {
        this.oneTimePurchaseTitleCP = oneTimePurchaseTitleCP;
    }

    public String getSeeMoreProductBtnText() {
        return seeMoreProductBtnText;
    }

    public void setSeeMoreProductBtnText(String seeMoreProductBtnText) {
        this.seeMoreProductBtnText = seeMoreProductBtnText;
    }


    public String getViewAttributeLabelText() {
        return viewAttributeLabelText;
    }

    public void setViewAttributeLabelText(String viewAttributeLabelText) {
        this.viewAttributeLabelText = viewAttributeLabelText;
    }

    public String getAttributeNameLabelText() {
        return attributeNameLabelText;
    }

    public void setAttributeNameLabelText(String attributeNameLabelText) {
        this.attributeNameLabelText = attributeNameLabelText;
    }

    public String getSwapProductLabelText() {
        return swapProductLabelText;
    }

    public void setSwapProductLabelText(String swapProductLabelText) {
        this.swapProductLabelText = swapProductLabelText;
    }

    public String getSwapProductSearchBarText() {
        return swapProductSearchBarText;
    }

    public void setSwapProductSearchBarText(String swapProductSearchBarText) {
        this.swapProductSearchBarText = swapProductSearchBarText;
    }

    public Boolean getEnableSwapProductFeature() {
        return enableSwapProductFeature;
    }

    public void setEnableSwapProductFeature(Boolean enableSwapProductFeature) {
        this.enableSwapProductFeature = enableSwapProductFeature;
    }

    public Boolean getEnableTabletForceView() {
        return enableTabletForceView;
    }

    public void setEnableTabletForceView(Boolean enableTabletForceView) {
        this.enableTabletForceView = enableTabletForceView;
    }

    public String getSwapProductBtnText() {
        return swapProductBtnText;
    }

    public void setSwapProductBtnText(String swapProductBtnText) {
        this.swapProductBtnText = swapProductBtnText;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAddNewButtonText() {
        return addNewButtonText;
    }

    public void setAddNewButtonText(String addNewButtonText) {
        this.addNewButtonText = addNewButtonText;
    }

    public String getAttributeHeadingText() {
        return attributeHeadingText;
    }

    public void setAttributeHeadingText(String attributeHeadingText) {
        this.attributeHeadingText = attributeHeadingText;
    }

    public Boolean getEnableViewAttributes() {
        return enableViewAttributes;
    }

    public void setEnableViewAttributes(Boolean enableViewAttributes) {
        this.enableViewAttributes = enableViewAttributes;
    }

    public Boolean getEnableEditOrderNotes() {
        return enableEditOrderNotes;
    }

    public void setEnableEditOrderNotes(Boolean enableEditOrderNotes) {
        this.enableEditOrderNotes = enableEditOrderNotes;
    }

    public Boolean getShowSellingPlanFrequencies() {
        return showSellingPlanFrequencies;
    }

    public void setShowSellingPlanFrequencies(Boolean showSellingPlanFrequencies) {
        this.showSellingPlanFrequencies = showSellingPlanFrequencies;
    }

    public String getTotalPricePerDeliveryText() {
        return totalPricePerDeliveryText;
    }

    public void setTotalPricePerDeliveryText(String totalPricePerDeliveryText) {
        this.totalPricePerDeliveryText = totalPricePerDeliveryText;
    }

    public String getFulfilledText() {
        return fulfilledText;
    }

    public void setFulfilledText(String fulfilledText) {
        this.fulfilledText = fulfilledText;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDiscountCouponAppliedText() {
        return discountCouponAppliedText;
    }

    public void setDiscountCouponAppliedText(String discountCouponAppliedText) {
        this.discountCouponAppliedText = discountCouponAppliedText;
    }

    public String getSubscriptionPausedMessageText() {
        return subscriptionPausedMessageText;
    }

    public void setSubscriptionPausedMessageText(String subscriptionPausedMessageText) {
        this.subscriptionPausedMessageText = subscriptionPausedMessageText;
    }

    public String getSubscriptionActivatedMessageText() {
        return subscriptionActivatedMessageText;
    }

    public void setSubscriptionActivatedMessageText(String subscriptionActivatedMessageText) {
        this.subscriptionActivatedMessageText = subscriptionActivatedMessageText;
    }

    public String getUnableToUpdateSubscriptionStatusMessageText() {
        return unableToUpdateSubscriptionStatusMessageText;
    }

    public void setUnableToUpdateSubscriptionStatusMessageText(String unableToUpdateSubscriptionStatusMessageText) {
        this.unableToUpdateSubscriptionStatusMessageText = unableToUpdateSubscriptionStatusMessageText;
    }

    public String getSelectCancellationReasonLabelText() {
        return selectCancellationReasonLabelText;
    }

    public void setSelectCancellationReasonLabelText(String selectCancellationReasonLabelText) {
        this.selectCancellationReasonLabelText = selectCancellationReasonLabelText;
    }

    public String getUpcomingOrderChangePopupSuccessTitleText() {
        return upcomingOrderChangePopupSuccessTitleText;
    }

    public void setUpcomingOrderChangePopupSuccessTitleText(String upcomingOrderChangePopupSuccessTitleText) {
        this.upcomingOrderChangePopupSuccessTitleText = upcomingOrderChangePopupSuccessTitleText;
    }

    public String getUpcomingOrderChangePopupSuccessDescriptionText() {
        return upcomingOrderChangePopupSuccessDescriptionText;
    }

    public void setUpcomingOrderChangePopupSuccessDescriptionText(String upcomingOrderChangePopupSuccessDescriptionText) {
        this.upcomingOrderChangePopupSuccessDescriptionText = upcomingOrderChangePopupSuccessDescriptionText;
    }

    public String getUpcomingOrderChangePopupSuccessClosebtnText() {
        return upcomingOrderChangePopupSuccessClosebtnText;
    }

    public void setUpcomingOrderChangePopupSuccessClosebtnText(String upcomingOrderChangePopupSuccessClosebtnText) {
        this.upcomingOrderChangePopupSuccessClosebtnText = upcomingOrderChangePopupSuccessClosebtnText;
    }

    public String getUpcomingOrderChangePopupFailureTitleText() {
        return upcomingOrderChangePopupFailureTitleText;
    }

    public void setUpcomingOrderChangePopupFailureTitleText(String upcomingOrderChangePopupFailureTitleText) {
        this.upcomingOrderChangePopupFailureTitleText = upcomingOrderChangePopupFailureTitleText;
    }

    public String getUpcomingOrderChangePopupFailureDescriptionText() {
        return upcomingOrderChangePopupFailureDescriptionText;
    }

    public void setUpcomingOrderChangePopupFailureDescriptionText(String upcomingOrderChangePopupFailureDescriptionText) {
        this.upcomingOrderChangePopupFailureDescriptionText = upcomingOrderChangePopupFailureDescriptionText;
    }

    public String getUpcomingOrderChangePopupFailureClosebtnText() {
        return upcomingOrderChangePopupFailureClosebtnText;
    }

    public void setUpcomingOrderChangePopupFailureClosebtnText(String upcomingOrderChangePopupFailureClosebtnText) {
        this.upcomingOrderChangePopupFailureClosebtnText = upcomingOrderChangePopupFailureClosebtnText;
    }

    public String getVariantIdsToFreezeEditRemove() {
        return variantIdsToFreezeEditRemove;
    }

    public void setVariantIdsToFreezeEditRemove(String variantIdsToFreezeEditRemove) {
        this.variantIdsToFreezeEditRemove = variantIdsToFreezeEditRemove;
    }

    public String getPreventCancellationBeforeDays() {
        return preventCancellationBeforeDays;
    }

    public void setPreventCancellationBeforeDays(String preventCancellationBeforeDays) {
        this.preventCancellationBeforeDays = preventCancellationBeforeDays;
    }

    public String getDisAllowVariantIdsForOneTimeProductAdd() {
        return disAllowVariantIdsForOneTimeProductAdd;
    }

    public void setDisAllowVariantIdsForOneTimeProductAdd(String disAllowVariantIdsForOneTimeProductAdd) {
        this.disAllowVariantIdsForOneTimeProductAdd = disAllowVariantIdsForOneTimeProductAdd;
    }

    public String getDisAllowVariantIdsForSubscriptionProductAdd() {
        return disAllowVariantIdsForSubscriptionProductAdd;
    }

    public void setDisAllowVariantIdsForSubscriptionProductAdd(String disAllowVariantIdsForSubscriptionProductAdd) {
        this.disAllowVariantIdsForSubscriptionProductAdd = disAllowVariantIdsForSubscriptionProductAdd;
    }

    public Boolean getHideAddSubscriptionProductSection() {
        return hideAddSubscriptionProductSection;
    }

    public void setHideAddSubscriptionProductSection(Boolean hideAddSubscriptionProductSection) {
        this.hideAddSubscriptionProductSection = hideAddSubscriptionProductSection;
    }

    public Boolean getAllowOnlyOneTimeProductOnAddProductFlag() {
        return allowOnlyOneTimeProductOnAddProductFlag;
    }

    public void setAllowOnlyOneTimeProductOnAddProductFlag(Boolean allowOnlyOneTimeProductOnAddProductFlag) {
        this.allowOnlyOneTimeProductOnAddProductFlag = allowOnlyOneTimeProductOnAddProductFlag;
    }

    public Integer getMinQtyToAllowDuringAddProduct() {
        return minQtyToAllowDuringAddProduct;
    }

    public void setMinQtyToAllowDuringAddProduct(Integer minQtyToAllowDuringAddProduct) {
        this.minQtyToAllowDuringAddProduct = minQtyToAllowDuringAddProduct;
    }

    public Boolean isAllowSplitContract() {
        return allowSplitContract;
    }

    public void setAllowSplitContract(Boolean allowSplitContract) {
        this.allowSplitContract = allowSplitContract;
    }

    public Boolean isEnableEditAttributes() {
        return enableEditAttributes;
    }

    public void setEnableEditAttributes(Boolean enableEditAttributes) {
        this.enableEditAttributes = enableEditAttributes;
    }

    public String getRemoveDiscountCodeLabel() {
        return removeDiscountCodeLabel;
    }

    public void setRemoveDiscountCodeLabel(String removeDiscountCodeLabel) {
        this.removeDiscountCodeLabel = removeDiscountCodeLabel;
    }

    public Boolean getEnableSplitContract() {
        return enableSplitContract;
    }

    public void setEnableSplitContract(Boolean enableSplitContract) {
        this.enableSplitContract = enableSplitContract;
    }

    public String getSplitContractMessage() {
        return splitContractMessage;
    }

    public void setSplitContractMessage(String splitContractMessage) {
        this.splitContractMessage = splitContractMessage;
    }

    public String getSplitContractText() {
        return splitContractText;
    }

    public void setSplitContractText(String splitContractText) {
        this.splitContractText = splitContractText;
    }

    public Boolean getEnableSwapProductVariant() {
        return enableSwapProductVariant;
    }

    public void setEnableSwapProductVariant(Boolean enableSwapProductVariant) {
        this.enableSwapProductVariant = enableSwapProductVariant;
    }

    public String getDiscountAccordionTitle() {
        return discountAccordionTitle;
    }

    public void setDiscountAccordionTitle(String discountAccordionTitle) {
        this.discountAccordionTitle = discountAccordionTitle;
    }

    public Boolean isEnableAllowOnlyOneDiscountCode() {
        return enableAllowOnlyOneDiscountCode;
    }

    public void setEnableAllowOnlyOneDiscountCode(Boolean enableAllowOnlyOneDiscountCode) {
        this.enableAllowOnlyOneDiscountCode = enableAllowOnlyOneDiscountCode;
    }

    public String getAllowedProductIdsForOneTimeProductAdd() {
        return allowedProductIdsForOneTimeProductAdd;
    }

    public void setAllowedProductIdsForOneTimeProductAdd(String allowedProductIdsForOneTimeProductAdd) {
        this.allowedProductIdsForOneTimeProductAdd = allowedProductIdsForOneTimeProductAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerPortalSettingsDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomerPortalSettingsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "CustomerPortalSettingsDTO{" +
            "id=" + id +
            ", shop='" + shop + '\'' +
            ", orderFrequencyText='" + orderFrequencyText + '\'' +
            ", totalProductsText='" + totalProductsText + '\'' +
            ", nextOrderText='" + nextOrderText + '\'' +
            ", statusText='" + statusText + '\'' +
            ", cancelSubscriptionBtnText='" + cancelSubscriptionBtnText + '\'' +
            ", noSubscriptionMessage='" + noSubscriptionMessage + '\'' +
            ", subscriptionNoText='" + subscriptionNoText + '\'' +
            ", updatePaymentMessage='" + updatePaymentMessage + '\'' +
            ", cardLastFourDigitText='" + cardLastFourDigitText + '\'' +
            ", cardExpiryText='" + cardExpiryText + '\'' +
            ", cardHolderNameText='" + cardHolderNameText + '\'' +
            ", cardTypeText='" + cardTypeText + '\'' +
            ", paymentMethodTypeText='" + paymentMethodTypeText + '\'' +
            ", cancelAccordionTitle='" + cancelAccordionTitle + '\'' +
            ", paymentDetailAccordionTitle='" + paymentDetailAccordionTitle + '\'' +
            ", upcomingOrderAccordionTitle='" + upcomingOrderAccordionTitle + '\'' +
            ", paymentInfoText='" + paymentInfoText + '\'' +
            ", updatePaymentBtnText='" + updatePaymentBtnText + '\'' +
            ", nextOrderDateLbl='" + nextOrderDateLbl + '\'' +
            ", statusLbl='" + statusLbl + '\'' +
            ", quantityLbl='" + quantityLbl + '\'' +
            ", amountLbl='" + amountLbl + '\'' +
            ", orderNoLbl='" + orderNoLbl + '\'' +
            ", editFrequencyBtnText='" + editFrequencyBtnText + '\'' +
            ", cancelFreqBtnText='" + cancelFreqBtnText + '\'' +
            ", updateFreqBtnText='" + updateFreqBtnText + '\'' +
            ", additionalProperties=" + additionalProperties +
            ", pauseResumeSub=" + pauseResumeSub +
            ", changeNextOrderDate=" + changeNextOrderDate +
            ", cancelSub=" + cancelSub +
            ", changeOrderFrequency=" + changeOrderFrequency +
            ", createAdditionalOrder=" + createAdditionalOrder +
            ", manageSubscriptionButtonText='" + manageSubscriptionButtonText + '\'' +
            ", editChangeOrderBtnText='" + editChangeOrderBtnText + '\'' +
            ", cancelChangeOrderBtnText='" + cancelChangeOrderBtnText + '\'' +
            ", updateChangeOrderBtnText='" + updateChangeOrderBtnText + '\'' +
            ", editProductButtonText='" + editProductButtonText + '\'' +
            ", deleteButtonText='" + deleteButtonText + '\'' +
            ", updateButtonText='" + updateButtonText + '\'' +
            ", cancelButtonText='" + cancelButtonText + '\'' +
            ", addProductButtonText='" + addProductButtonText + '\'' +
            ", addProductLabelText='" + addProductLabelText + '\'' +
            ", activeBadgeText='" + activeBadgeText + '\'' +
            ", closeBadgeText='" + closeBadgeText + '\'' +
            ", skipOrderButtonText='" + skipOrderButtonText + '\'' +
            ", productLabelText='" + productLabelText + '\'' +
            ", seeMoreDetailsText='" + seeMoreDetailsText + '\'' +
            ", hideDetailsText='" + hideDetailsText + '\'' +
            ", productInSubscriptionText='" + productInSubscriptionText + '\'' +
            ", EditQuantityLabelText='" + EditQuantityLabelText + '\'' +
            ", subTotalLabelText='" + subTotalLabelText + '\'' +
            ", paymentNotificationText='" + paymentNotificationText + '\'' +
            ", editProductFlag=" + editProductFlag +
            ", deleteProductFlag=" + deleteProductFlag +
            ", showShipment=" + showShipment +
            ", magicLinkEmailFlag=" + magicLinkEmailFlag +
            ", addAdditionalProduct=" + addAdditionalProduct +
            ", successText='" + successText + '\'' +
            ", cancelSubscriptionConfirmPrepaidText='" + cancelSubscriptionConfirmPrepaidText + '\'' +
            ", cancelSubscriptionConfirmPayAsYouGoText='" + cancelSubscriptionConfirmPayAsYouGoText + '\'' +
            ", cancelSubscriptionPrepaidButtonText='" + cancelSubscriptionPrepaidButtonText + '\'' +
            ", cancelSubscriptionPayAsYouGoButtonText='" + cancelSubscriptionPayAsYouGoButtonText + '\'' +
            ", upcomingFulfillmentText='" + upcomingFulfillmentText + '\'' +
            ", creditCardText='" + creditCardText + '\'' +
            ", endingWithText='" + endingWithText + '\'' +
            ", weekText='" + weekText + '\'' +
            ", dayText='" + dayText + '\'' +
            ", monthText='" + monthText + '\'' +
            ", yearText='" + yearText + '\'' +
            ", skipBadgeText='" + skipBadgeText + '\'' +
            ", queueBadgeText='" + queueBadgeText + '\'' +
            ", shippingLabelText='" + shippingLabelText + '\'' +
            ", failureText='" + failureText + '\'' +
            ", sendEmailText='" + sendEmailText + '\'' +
            ", chooseDifferentProductActionText='" + chooseDifferentProductActionText + '\'' +
            ", chooseDifferentProductText='" + chooseDifferentProductText + '\'' +
            ", confirmSkipFulfillmentBtnText='" + confirmSkipFulfillmentBtnText + '\'' +
            ", confirmSkipOrder='" + confirmSkipOrder + '\'' +
            ", skipFulfillmentButtonText='" + skipFulfillmentButtonText + '\'' +
            ", confirmCommonText='" + confirmCommonText + '\'' +
            ", orderNowDescriptionText='" + orderNowDescriptionText + '\'' +
            ", discountDetailsTitleText='" + discountDetailsTitleText + '\'' +
            ", emailAddressText='" + emailAddressText + '\'' +
            ", emailMagicLinkText='" + emailMagicLinkText + '\'' +
            ", retriveMagicLinkText='" + retriveMagicLinkText + '\'' +
            ", validEmailMessage='" + validEmailMessage + '\'' +
            ", saveButtonText='" + saveButtonText + '\'' +
            ", orderDateText='" + orderDateText + '\'' +
            ", address1LabelText='" + address1LabelText + '\'' +
            ", address2LabelText='" + address2LabelText + '\'' +
            ", companyLabelText='" + companyLabelText + '\'' +
            ", cityLabelText='" + cityLabelText + '\'' +
            ", countryLabelText='" + countryLabelText + '\'' +
            ", firstNameLabelText='" + firstNameLabelText + '\'' +
            ", lastNameLabelText='" + lastNameLabelText + '\'' +
            ", phoneLabelText='" + phoneLabelText + '\'' +
            ", provinceLabelText='" + provinceLabelText + '\'' +
            ", zipLabelText='" + zipLabelText + '\'' +
            ", addressHeaderTitleText='" + addressHeaderTitleText + '\'' +
            ", changeShippingAddressFlag=" + changeShippingAddressFlag +
            ", updateEditShippingButtonText='" + updateEditShippingButtonText + '\'' +
            ", cancelEditShippingButtonText='" + cancelEditShippingButtonText + '\'' +
            ", pauseSubscriptionText='" + pauseSubscriptionText + '\'' +
            ", resumeSubscriptionText='" + resumeSubscriptionText + '\'' +
            ", pauseBadgeText='" + pauseBadgeText + '\'' +
            ", customerPortalSettingJson='" + customerPortalSettingJson + '\'' +
            ", discountNoteTitle='" + discountNoteTitle + '\'' +
            ", initialDiscountNoteDescription='" + initialDiscountNoteDescription + '\'' +
            ", afterCycleDiscountNoteDescription='" + afterCycleDiscountNoteDescription + '\'' +
            ", productRemovedTooltip='" + productRemovedTooltip + '\'' +
            ", deliveryPriceText='" + deliveryPriceText + '\'' +
            ", shippingOptionText='" + shippingOptionText + '\'' +
            ", nextDeliveryDate='" + nextDeliveryDate + '\'' +
            ", everyLabelText='" + everyLabelText + '\'' +
            ", orderNoteFlag=" + orderNoteFlag +
            ", orderNoteText='" + orderNoteText + '\'' +
            ", useUrlWithCustomerId=" + useUrlWithCustomerId +
            ", expiredTokenText='" + expiredTokenText + '\'' +
            ", portalLoginLinkText='" + portalLoginLinkText + '\'' +
            ", localeDate='" + localeDate + '\'' +
            ", productSelectionOption=" + productSelectionOption +
            ", includeOutOfStockProduct=" + includeOutOfStockProduct +
            ", customerIdText='" + customerIdText + '\'' +
            ", helloNameText='" + helloNameText + '\'' +
            ", goBackButtonText='" + goBackButtonText + '\'' +
            ", changeVariantLabelText='" + changeVariantLabelText + '\'' +
            ", provinceCodeLabelText='" + provinceCodeLabelText + '\'' +
            ", countryCodeLabelText='" + countryCodeLabelText + '\'' +
            ", pleaseWaitLoaderText='" + pleaseWaitLoaderText + '\'' +
            ", openBadgeText='" + openBadgeText + '\'' +
            ", cancelSubscriptionMinimumBillingIterationsMessage='" + cancelSubscriptionMinimumBillingIterationsMessage + '\'' +
            ", topHtml='" + topHtml + '\'' +
            ", bottomHtml='" + bottomHtml + '\'' +
            ", updateShipmentBillingDate=" + updateShipmentBillingDate +
            ", discountCodeText='" + discountCodeText + '\'' +
            ", discountCodeApplyButtonText='" + discountCodeApplyButtonText + '\'' +
            ", discountCode=" + discountCode +
            ", applySellingPlanBasedDiscount=" + applySellingPlanBasedDiscount +
            ", applySubscriptionDiscountForOtp=" + applySubscriptionDiscountForOtp +
            ", applySubscriptionDiscount=" + applySubscriptionDiscount +
            ", removeDiscountCodeAutomatically=" + removeDiscountCodeAutomatically +
            ", removeDiscountCodeLabel='" + removeDiscountCodeLabel + '\'' +
            ", enableSplitContract=" + enableSplitContract +
            ", splitContractMessage='" + splitContractMessage + '\'' +
            ", splitContractText='" + splitContractText + '\'' +
            ", subscriptionDiscountTypeUnit=" + subscriptionDiscountTypeUnit +
            ", subscriptionDiscount=" + subscriptionDiscount +
            ", upSellMessage='" + upSellMessage + '\'' +
            ", freezeOrderTillMinCycle=" + freezeOrderTillMinCycle +
            ", freezeUpdateSubscriptionMessage='" + freezeUpdateSubscriptionMessage + '\'' +
            ", subscriptionContractFreezeMessage='" + subscriptionContractFreezeMessage + '\'' +
            ", preventCancellationBeforeDaysMessage='" + preventCancellationBeforeDaysMessage + '\'' +
            ", offerDiscountOnCancellation=" + offerDiscountOnCancellation +
            ", enableSkipFulFillment=" + enableSkipFulFillment +
            ", discountPercentageOnCancellation='" + discountPercentageOnCancellation + '\'' +
            ", discountMessageOnCancellation='" + discountMessageOnCancellation + '\'' +
            ", discountRecurringCycleLimitOnCancellation='" + discountRecurringCycleLimitOnCancellation + '\'' +
            ", frequencyChangeWarningTitle='" + frequencyChangeWarningTitle + '\'' +
            ", frequencyChangeWarningDescription='" + frequencyChangeWarningDescription + '\'' +
            ", discountCouponRemoveText='" + discountCouponRemoveText + '\'' +
            ", pleaseSelectText='" + pleaseSelectText + '\'' +
            ", shippingAddressNotAvailableText='" + shippingAddressNotAvailableText + '\'' +
            ", sellingPlanNameText='" + sellingPlanNameText + '\'' +
            ", discountCouponNotAppliedText='" + discountCouponNotAppliedText + '\'' +
            ", shopPayPaymentUpdateText='" + shopPayPaymentUpdateText + '\'' +
            ", addOneTimeProduct=" + addOneTimeProduct +
            ", allowOrderNow=" + allowOrderNow +
            ", selectProductLabelText='" + selectProductLabelText + '\'' +
            ", purchaseOptionLabelText='" + purchaseOptionLabelText + '\'' +
            ", finishLabelText='" + finishLabelText + '\'' +
            ", nextBtnText='" + nextBtnText + '\'' +
            ", previousBtnText='" + previousBtnText + '\'' +
            ", closeBtnText='" + closeBtnText + '\'' +
            ", deleteConfirmationMsgText='" + deleteConfirmationMsgText + '\'' +
            ", deleteMsgText='" + deleteMsgText + '\'' +
            ", yesBtnText='" + yesBtnText + '\'' +
            ", noBtnText='" + noBtnText + '\'' +
            ", oneTimePurchaseNoteText='" + oneTimePurchaseNoteText + '\'' +
            ", clickHereText='" + clickHereText + '\'' +
            ", productAddMessageText='" + productAddMessageText + '\'' +
            ", choosePurchaseOptionLabelText='" + choosePurchaseOptionLabelText + '\'' +
            ", oneTimePurchaseMessageText='" + oneTimePurchaseMessageText + '\'' +
            ", contractUpdateMessageText='" + contractUpdateMessageText + '\'' +
            ", oneTimePurchaseDisplayMessageText='" + oneTimePurchaseDisplayMessageText + '\'' +
            ", addProductFinishedMessageText='" + addProductFinishedMessageText + '\'' +
            ", contractErrorMessageText='" + contractErrorMessageText + '\'' +
            ", addToSubscriptionTitleCP='" + addToSubscriptionTitleCP + '\'' +
            ", oneTimePurchaseTitleCP='" + oneTimePurchaseTitleCP + '\'' +
            ", seeMoreProductBtnText='" + seeMoreProductBtnText + '\'' +
            ", viewAttributeLabelText='" + viewAttributeLabelText + '\'' +
            ", attributeNameLabelText='" + attributeNameLabelText + '\'' +
            ", swapProductLabelText='" + swapProductLabelText + '\'' +
            ", swapProductSearchBarText='" + swapProductSearchBarText + '\'' +
            ", enableSwapProductFeature=" + enableSwapProductFeature +
            ", enableTabletForceView=" + enableTabletForceView +
            ", swapProductBtnText='" + swapProductBtnText + '\'' +
            ", attributeValue='" + attributeValue + '\'' +
            ", addNewButtonText='" + addNewButtonText + '\'' +
            ", attributeHeadingText='" + attributeHeadingText + '\'' +
            ", enableViewAttributes=" + enableViewAttributes +
            ", enableEditOrderNotes=" + enableEditOrderNotes +
            ", showSellingPlanFrequencies=" + showSellingPlanFrequencies +
            ", totalPricePerDeliveryText='" + totalPricePerDeliveryText + '\'' +
            ", fulfilledText='" + fulfilledText + '\'' +
            ", dateFormat='" + dateFormat + '\'' +
            ", discountCouponAppliedText='" + discountCouponAppliedText + '\'' +
            ", subscriptionPausedMessageText='" + subscriptionPausedMessageText + '\'' +
            ", subscriptionActivatedMessageText='" + subscriptionActivatedMessageText + '\'' +
            ", unableToUpdateSubscriptionStatusMessageText='" + unableToUpdateSubscriptionStatusMessageText + '\'' +
            ", selectCancellationReasonLabelText='" + selectCancellationReasonLabelText + '\'' +
            ", upcomingOrderChangePopupSuccessTitleText='" + upcomingOrderChangePopupSuccessTitleText + '\'' +
            ", upcomingOrderChangePopupSuccessDescriptionText='" + upcomingOrderChangePopupSuccessDescriptionText + '\'' +
            ", upcomingOrderChangePopupSuccessClosebtnText='" + upcomingOrderChangePopupSuccessClosebtnText + '\'' +
            ", upcomingOrderChangePopupFailureTitleText='" + upcomingOrderChangePopupFailureTitleText + '\'' +
            ", upcomingOrderChangePopupFailureDescriptionText='" + upcomingOrderChangePopupFailureDescriptionText + '\'' +
            ", upcomingOrderChangePopupFailureClosebtnText='" + upcomingOrderChangePopupFailureClosebtnText + '\'' +
            ", variantIdsToFreezeEditRemove='" + variantIdsToFreezeEditRemove + '\'' +
            ", preventCancellationBeforeDays='" + preventCancellationBeforeDays + '\'' +
            ", disAllowVariantIdsForOneTimeProductAdd='" + disAllowVariantIdsForOneTimeProductAdd + '\'' +
            ", hideAddSubscriptionProductSection=" + hideAddSubscriptionProductSection +
            ", allowOnlyOneTimeProductOnAddProductFlag=" + allowOnlyOneTimeProductOnAddProductFlag +
            ", requireFieldMessage='" + requireFieldMessage + '\'' +
            ", validNumberRequiredMessage='" + validNumberRequiredMessage + '\'' +
            ", variantLbl='" + variantLbl + '\'' +
            ", priceLbl='" + priceLbl + '\'' +
            ", oneTimePurchaseOnlyText='" + oneTimePurchaseOnlyText + '\'' +
            ", rescheduleText='" + rescheduleText + '\'' +
            ", popUpSuccessMessage='" + popUpSuccessMessage + '\'' +
            ", popUpErrorMessage='" + popUpErrorMessage + '\'' +
            ", orderNowText='" + orderNowText + '\'' +
            ", upcomingOrderPlaceNowAlertText='" + upcomingOrderPlaceNowAlertText + '\'' +
            ", upcomingOrderSkipAlertText='" + upcomingOrderSkipAlertText + '\'' +
            ", deliveryFrequencyText='" + deliveryFrequencyText + '\'' +
            ", editDeliveryInternalText='" + editDeliveryInternalText + '\'' +
            ", maxCycleText='" + maxCycleText + '\'' +
            ", minCycleText='" + minCycleText + '\'' +
            ", selectProductToAdd='" + selectProductToAdd + '\'' +
            ", searchProductBtnText='" + searchProductBtnText + '\'' +
            ", areyousureCommonMessageText='" + areyousureCommonMessageText + '\'' +
            ", editCommonText='" + editCommonText + '\'' +
            ", viewMoreText='" + viewMoreText + '\'' +
            ", variantLblText='" + variantLblText + '\'' +
            ", totalLblText='" + totalLblText + '\'' +
            ", deleteProductTitleText='" + deleteProductTitleText + '\'' +
            ", greetingText='" + greetingText + '\'' +
            ", productLblText='" + productLblText + '\'' +
            ", hasBeenRemovedText='" + hasBeenRemovedText + '\'' +
            ", orderTotalText='" + orderTotalText + '\'' +
            ", addDiscountCodeText='" + addDiscountCodeText + '\'' +
            ", addDiscountCodeAlertText='" + addDiscountCodeAlertText + '\'' +
            ", removeDiscountCodeAlertText='" + removeDiscountCodeAlertText + '\'' +
            ", shopPayLblText='" + shopPayLblText + '\'' +
            ", paypalLblText='" + paypalLblText + '\'' +
            ", unknownPaymentReachoutUsText='" + unknownPaymentReachoutUsText + '\'' +
            ", addToOrderLabelText='" + addToOrderLabelText + '\'' +
            ", upcomingTabTitle='" + upcomingTabTitle + '\'' +
            ", scheduledTabTitle='" + scheduledTabTitle + '\'' +
            ", historyTabTitle='" + historyTabTitle + '\'' +
            ", noOrderNotAvailableMessage='" + noOrderNotAvailableMessage + '\'' +
            ", continueText='" + continueText + '\'' +
            ", confirmSwapText='" + confirmSwapText + '\'' +
            ", confirmAddProduct='" + confirmAddProduct + '\'' +
            ", minQtyToAllowDuringAddProduct=" + minQtyToAllowDuringAddProduct +
            ", allowSplitContract=" + allowSplitContract +
            ", enableEditAttributes=" + enableEditAttributes +
            ", enableSwapProductVariant='" + enableSwapProductVariant + "'" +
            ", discountAccordionTitle='" + discountAccordionTitle + "'" +
            ", redeemRewardsTextV2='" + redeemRewardsTextV2 + "'" +
            ", rewardsTextV2='" + rewardsTextV2 + "'" +
            ", yourRewardsTextV2='" + yourRewardsTextV2 + "'" +
            ", yourAvailableRewardsPointsTextV2='" + yourAvailableRewardsPointsTextV2 + "'" +
            ", cancellationDateTitleText='" + cancellationDateTitleText + "'" +
            ", selectedCancellationReasonTitleText='" + selectedCancellationReasonTitleText + "'" +
            ", cancellationNoteTitleText='" + cancellationNoteTitleText + "'" +
            ", selectSplitMethodLabelText='" + selectSplitMethodLabelText + "'" +
            ", splitWithOrderPlacedSelectOptionText='" + splitWithOrderPlacedSelectOptionText + "'" +
            ", splitWithoutOrderPlacedSelectOptionText='" + splitWithoutOrderPlacedSelectOptionText + "'" +
            ", contractCancelledBadgeText='" + contractCancelledBadgeText + "'" +
            ", chooseAnotherPaymentMethodTitleText='" + chooseAnotherPaymentMethodTitleText + "'" +
            ", selectPaymentMethodTitleText='" + selectPaymentMethodTitleText + "'" +
            ", changePaymentMessage='" + changePaymentMessage + "'" +
            ", updatePaymentMethodTitleText='" + updatePaymentMethodTitleText + "'" +
            ", enableRedirectMyAccountButton='" + enableRedirectMyAccountButton + "'" +
            ", reschedulingPolicies='" + reschedulingPolicies + "'" +
            ", enableAllowOnlyOneDiscountCode='" + enableAllowOnlyOneDiscountCode + "'" +
            ", allowedProductIdsForOneTimeProductAdd='" + allowedProductIdsForOneTimeProductAdd + "'" +
            ", disAllowVariantIdsForSubscriptionProductAdd='" + disAllowVariantIdsForSubscriptionProductAdd + "'" +
            '}';
    }
}
