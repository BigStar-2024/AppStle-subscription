package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.ShopCustomizationService;
import com.et.service.dto.CustomerPortalSettingsV2DTO;
import com.et.utils.CommonUtils;
import com.et.utils.ShopLabelUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.CustomerPortalSettingsDTO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.CustomerPortalSettings}.
 */
@RestController
@Api(tags = "Customer Portal Settings Resource")
public class CustomerPortalSettingsResource {

    private final Logger log = LoggerFactory.getLogger(CustomerPortalSettingsResource.class);

    private static final String ENTITY_NAME = "customerPortalSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopLabelUtils shopLabelUtils;

    @Autowired
    ShopCustomizationService shopCustomizationService;

    public CustomerPortalSettingsResource(CustomerPortalSettingsService customerPortalSettingsService) {
        this.customerPortalSettingsService = customerPortalSettingsService;
    }

    /**
     * {@code POST  /api/customer-portal-settings} : Create a new customerPortalSettings.
     *
     * @param customerPortalSettingsDTO the customerPortalSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerPortalSettingsDTO, or with status {@code 400 (Bad Request)} if the customerPortalSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/customer-portal-settings")
    public ResponseEntity<CustomerPortalSettingsDTO> createCustomerPortalSettings(@Valid @RequestBody CustomerPortalSettingsDTO customerPortalSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerPortalSettings : {}", customerPortalSettingsDTO);
        if (customerPortalSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerPortalSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        JSONObject customerPortalSettingJson = new JSONObject();
        setCustomerPortalJsonFromDTO(customerPortalSettingsDTO, customerPortalSettingJson);

        customerPortalSettingsDTO.setCustomerPortalSettingJson(customerPortalSettingJson.toString());

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPortalSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CustomerPortalSettingsDTO result = customerPortalSettingsService.save(customerPortalSettingsDTO);

        CustomerPortalSettingsV2DTO customerPortalSettingsV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
        }, result.getCustomerPortalSettingJson());

        setCustomerPortalDTOFromV2DTO(result, customerPortalSettingsV2DTO);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.cp.", "CUSTOMER_PORTAL", customerPortalSettingsDTO.getAdditionalProperties());

        return ResponseEntity.created(new URI("/api/customer-portal-settings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Customer Portal Settings Created.", ""))
            .body(result);
    }

    private void setCustomerPortalJsonFromDTO(CustomerPortalSettingsDTO customerPortalSettingsDTO, JSONObject customerPortalSettingJson) {
        customerPortalSettingJson.put("shippingLabelText", customerPortalSettingsDTO.getShippingLabelText());
        customerPortalSettingJson.put("failureText", customerPortalSettingsDTO.getFailureText());
        customerPortalSettingJson.put("chooseDifferentProductText", customerPortalSettingsDTO.getChooseDifferentProductText());
        customerPortalSettingJson.put("chooseDifferentProductActionText", customerPortalSettingsDTO.getChooseDifferentProductActionText());
        customerPortalSettingJson.put("confirmSkipFulfillmentBtnText", customerPortalSettingsDTO.getConfirmSkipFulfillmentBtnText());
        customerPortalSettingJson.put("confirmSkipOrder", customerPortalSettingsDTO.getConfirmSkipOrder());
        customerPortalSettingJson.put("skipFulfillmentButtonText", customerPortalSettingsDTO.getSkipFulfillmentButtonText());
        customerPortalSettingJson.put("confirmCommonText", customerPortalSettingsDTO.getConfirmCommonText());
        customerPortalSettingJson.put("orderNowDescriptionText", customerPortalSettingsDTO.getOrderNowDescriptionText());
        customerPortalSettingJson.put("discountDetailsTitleText", customerPortalSettingsDTO.getDiscountDetailsTitleText());

        customerPortalSettingJson.put("emailAddressText", customerPortalSettingsDTO.getEmailAddressText());
        customerPortalSettingJson.put("emailMagicLinkText", customerPortalSettingsDTO.getEmailMagicLinkText());
        customerPortalSettingJson.put("retriveMagicLinkText", customerPortalSettingsDTO.getRetriveMagicLinkText());
        customerPortalSettingJson.put("validEmailMessage", customerPortalSettingsDTO.getValidEmailMessage());
        customerPortalSettingJson.put("sendEmailText", customerPortalSettingsDTO.getSendEmailText());
        customerPortalSettingJson.put("saveButtonText", customerPortalSettingsDTO.getSaveButtonText());
        customerPortalSettingJson.put("orderDateText", customerPortalSettingsDTO.getOrderDateText());
        customerPortalSettingJson.put("address1LabelText", customerPortalSettingsDTO.getAddress1LabelText());
        customerPortalSettingJson.put("address2LabelText", customerPortalSettingsDTO.getAddress2LabelText());
        customerPortalSettingJson.put("companyLabelText", customerPortalSettingsDTO.getCompanyLabelText());
        customerPortalSettingJson.put("cityLabelText", customerPortalSettingsDTO.getCityLabelText());
        customerPortalSettingJson.put("countryLabelText", customerPortalSettingsDTO.getCountryLabelText());
        customerPortalSettingJson.put("firstNameLabelText", customerPortalSettingsDTO.getFirstNameLabelText());
        customerPortalSettingJson.put("lastNameLabelText", customerPortalSettingsDTO.getLastNameLabelText());
        customerPortalSettingJson.put("phoneLabelText", customerPortalSettingsDTO.getPhoneLabelText());
        customerPortalSettingJson.put("provinceLabelText", customerPortalSettingsDTO.getProvinceLabelText());
        customerPortalSettingJson.put("zipLabelText", customerPortalSettingsDTO.getZipLabelText());
        customerPortalSettingJson.put("addressHeaderTitleText", customerPortalSettingsDTO.getAddressHeaderTitleText());
        customerPortalSettingJson.put("changeShippingAddressFlag", customerPortalSettingsDTO.getChangeShippingAddressFlag());
        customerPortalSettingJson.put("updateEditShippingButtonText", customerPortalSettingsDTO.getUpdateEditShippingButtonText());
        customerPortalSettingJson.put("cancelEditShippingButtonText", customerPortalSettingsDTO.getCancelEditShippingButtonText());
        customerPortalSettingJson.put("pauseSubscriptionText", customerPortalSettingsDTO.getPauseSubscriptionText());
        customerPortalSettingJson.put("resumeSubscriptionText", customerPortalSettingsDTO.getResumeSubscriptionText());
        customerPortalSettingJson.put("pauseBadgeText", customerPortalSettingsDTO.getPauseBadgeText());
        customerPortalSettingJson.put("discountNoteTitle", customerPortalSettingsDTO.getDiscountNoteTitle());
        customerPortalSettingJson.put("initialDiscountNoteDescription", customerPortalSettingsDTO.getInitialDiscountNoteDescription());
        customerPortalSettingJson.put("afterCycleDiscountNoteDescription", customerPortalSettingsDTO.getAfterCycleDiscountNoteDescription());
        customerPortalSettingJson.put("productRemovedTooltip", customerPortalSettingsDTO.getProductRemovedTooltip());
        customerPortalSettingJson.put("deliveryPriceText", customerPortalSettingsDTO.getDeliveryPriceText());
        customerPortalSettingJson.put("shippingOptionText", customerPortalSettingsDTO.getShippingOptionText());
        customerPortalSettingJson.put("nextDeliveryDate", customerPortalSettingsDTO.getNextDeliveryDate());
        customerPortalSettingJson.put("everyLabelText", customerPortalSettingsDTO.getEveryLabelText());
        customerPortalSettingJson.put("expiredTokenText", customerPortalSettingsDTO.getExpiredTokenText());
        customerPortalSettingJson.put("portalLoginLinkText", customerPortalSettingsDTO.getPortalLoginLinkText());
        customerPortalSettingJson.put("localeDate", customerPortalSettingsDTO.getLocaleDate());
        customerPortalSettingJson.put("customerIdText", customerPortalSettingsDTO.getCustomerIdText());
        customerPortalSettingJson.put("helloNameText", customerPortalSettingsDTO.getHelloNameText());
        customerPortalSettingJson.put("goBackButtonText", customerPortalSettingsDTO.getGoBackButtonText());
        customerPortalSettingJson.put("changeVariantLabelText", customerPortalSettingsDTO.getChangeVariantLabelText());
        customerPortalSettingJson.put("provinceCodeLabelText", customerPortalSettingsDTO.getProvinceCodeLabelText());
        customerPortalSettingJson.put("countryCodeLabelText", customerPortalSettingsDTO.getCountryCodeLabelText());
        customerPortalSettingJson.put("pleaseWaitLoaderText", customerPortalSettingsDTO.getPleaseWaitLoaderText());
        customerPortalSettingJson.put("cancelSubscriptionMinimumBillingIterationsMessage", customerPortalSettingsDTO.getCancelSubscriptionMinimumBillingIterationsMessage());
        customerPortalSettingJson.put("topHtml", customerPortalSettingsDTO.getTopHtml());
        customerPortalSettingJson.put("bottomHtml", customerPortalSettingsDTO.getBottomHtml());
        customerPortalSettingJson.put("discountCodeText", customerPortalSettingsDTO.getDiscountCodeText());
        customerPortalSettingJson.put("discountCodeApplyButtonText", customerPortalSettingsDTO.getDiscountCodeApplyButtonText());
        customerPortalSettingJson.put("applySellingPlanBasedDiscount", customerPortalSettingsDTO.getApplySellingPlanBasedDiscount());
        customerPortalSettingJson.put("applySubscriptionDiscountForOtp", customerPortalSettingsDTO.getApplySubscriptionDiscountForOtp());
        customerPortalSettingJson.put("applySubscriptionDiscount", customerPortalSettingsDTO.isApplySubscriptionDiscount());
        customerPortalSettingJson.put("removeDiscountCodeAutomatically", customerPortalSettingsDTO.getRemoveDiscountCodeAutomatically());
        customerPortalSettingJson.put("removeDiscountCodeLabel", customerPortalSettingsDTO.getRemoveDiscountCodeLabel());
        customerPortalSettingJson.put("subscriptionDiscountTypeUnit", customerPortalSettingsDTO.getSubscriptionDiscountTypeUnit());
        customerPortalSettingJson.put("subscriptionDiscount", customerPortalSettingsDTO.getSubscriptionDiscount());
        customerPortalSettingJson.put("upSellMessage", customerPortalSettingsDTO.getUpSellMessage());
        customerPortalSettingJson.put("freezeUpdateSubscriptionMessage", customerPortalSettingsDTO.getFreezeUpdateSubscriptionMessage());
        customerPortalSettingJson.put("requireFieldMessage", customerPortalSettingsDTO.getRequireFieldMessage());
        customerPortalSettingJson.put("validNumberRequiredMessage", customerPortalSettingsDTO.getValidNumberRequiredMessage());
        customerPortalSettingJson.put("variantLbl", customerPortalSettingsDTO.getVariantLbl());
        customerPortalSettingJson.put("priceLbl", customerPortalSettingsDTO.getPriceLbl());
        customerPortalSettingJson.put("oneTimePurchaseOnlyText", customerPortalSettingsDTO.getOneTimePurchaseOnlyText());
        customerPortalSettingJson.put("orderNowText", customerPortalSettingsDTO.getOrderNowText());
        customerPortalSettingJson.put("rescheduleText", customerPortalSettingsDTO.getRescheduleText());
        customerPortalSettingJson.put("popUpSuccessMessage", customerPortalSettingsDTO.getPopUpSuccessMessage());
        customerPortalSettingJson.put("popUpErrorMessage", customerPortalSettingsDTO.getPopUpErrorMessage());
        customerPortalSettingJson.put("upcomingOrderPlaceNowAlertText", customerPortalSettingsDTO.getUpcomingOrderPlaceNowAlertText());
        customerPortalSettingJson.put("upcomingOrderSkipAlertText", customerPortalSettingsDTO.getUpcomingOrderSkipAlertText());
        customerPortalSettingJson.put("deliveryFrequencyText", customerPortalSettingsDTO.getDeliveryFrequencyText());
        customerPortalSettingJson.put("editDeliveryInternalText", customerPortalSettingsDTO.getEditDeliveryInternalText());
        customerPortalSettingJson.put("maxCycleText", customerPortalSettingsDTO.getMaxCycleText());
        customerPortalSettingJson.put("minCycleText", customerPortalSettingsDTO.getMinCycleText());
        customerPortalSettingJson.put("selectProductToAdd", customerPortalSettingsDTO.getSelectProductToAdd());
        customerPortalSettingJson.put("searchProductBtnText", customerPortalSettingsDTO.getSearchProductBtnText());
        customerPortalSettingJson.put("areyousureCommonMessageText", customerPortalSettingsDTO.getAreyousureCommonMessageText());
        customerPortalSettingJson.put("editCommonText", customerPortalSettingsDTO.getEditCommonText());
        customerPortalSettingJson.put("viewMoreText", customerPortalSettingsDTO.getViewMoreText());
        customerPortalSettingJson.put("variantLblText", customerPortalSettingsDTO.getVariantLblText());
        customerPortalSettingJson.put("totalLblText", customerPortalSettingsDTO.getTotalLblText());
        customerPortalSettingJson.put("deleteProductTitleText", customerPortalSettingsDTO.getDeleteProductTitleText());
        customerPortalSettingJson.put("greetingText", customerPortalSettingsDTO.getGreetingText());
        customerPortalSettingJson.put("productLblText", customerPortalSettingsDTO.getProductLblText());
        customerPortalSettingJson.put("hasBeenRemovedText", customerPortalSettingsDTO.getHasBeenRemovedText());
        customerPortalSettingJson.put("orderTotalText", customerPortalSettingsDTO.getOrderTotalText());
        customerPortalSettingJson.put("addDiscountCodeText", customerPortalSettingsDTO.getAddDiscountCodeText());
        customerPortalSettingJson.put("addDiscountCodeAlertText", customerPortalSettingsDTO.getAddDiscountCodeAlertText());
        customerPortalSettingJson.put("removeDiscountCodeAlertText", customerPortalSettingsDTO.getRemoveDiscountCodeAlertText());
        customerPortalSettingJson.put("shopPayLblText", customerPortalSettingsDTO.getShopPayLblText());
        customerPortalSettingJson.put("paypalLblText", customerPortalSettingsDTO.getPaypalLblText());
        customerPortalSettingJson.put("unknownPaymentReachoutUsText", customerPortalSettingsDTO.getUnknownPaymentReachoutUsText());
        customerPortalSettingJson.put("addToOrderLabelText", customerPortalSettingsDTO.getAddToOrderLabelText());
        customerPortalSettingJson.put("upcomingTabTitle", customerPortalSettingsDTO.getUpcomingTabTitle());
        customerPortalSettingJson.put("scheduledTabTitle", customerPortalSettingsDTO.getScheduledTabTitle());
        customerPortalSettingJson.put("historyTabTitle", customerPortalSettingsDTO.getHistoryTabTitle());
        customerPortalSettingJson.put("noOrderNotAvailableMessage", customerPortalSettingsDTO.getNoOrderNotAvailableMessage());
        customerPortalSettingJson.put("continueText", customerPortalSettingsDTO.getContinueText());
        customerPortalSettingJson.put("confirmSwapText", customerPortalSettingsDTO.getConfirmSwapText());
        customerPortalSettingJson.put("confirmAddProduct", customerPortalSettingsDTO.getConfirmAddProduct());

        customerPortalSettingJson.put("subscriptionContractFreezeMessage", customerPortalSettingsDTO.getSubscriptionContractFreezeMessage());
        customerPortalSettingJson.put("preventCancellationBeforeDaysMessage", customerPortalSettingsDTO.getPreventCancellationBeforeDaysMessage());
        customerPortalSettingJson.put("discountRecurringCycleLimitOnCancellation", customerPortalSettingsDTO.getDiscountRecurringCycleLimitOnCancellation());
        customerPortalSettingJson.put("discountAccordionTitle", customerPortalSettingsDTO.getDiscountAccordionTitle());
        customerPortalSettingJson.put("discountMessageOnCancellation", customerPortalSettingsDTO.getDiscountMessageOnCancellation());
        customerPortalSettingJson.put("discountPercentageOnCancellation", customerPortalSettingsDTO.getDiscountPercentageOnCancellation());
        customerPortalSettingJson.put("offerDiscountOnCancellation", customerPortalSettingsDTO.getOfferDiscountOnCancellation());
        customerPortalSettingJson.put("enableSkipFulFillment", customerPortalSettingsDTO.getEnableSkipFulFillment());
        customerPortalSettingJson.put("magicLinkEmailFlag", customerPortalSettingsDTO.getMagicLinkEmailFlag());

        customerPortalSettingJson.put("frequencyChangeWarningTitle", customerPortalSettingsDTO.getFrequencyChangeWarningTitle());
        customerPortalSettingJson.put("frequencyChangeWarningDescription", customerPortalSettingsDTO.getFrequencyChangeWarningDescription());
        customerPortalSettingJson.put("variantIdsToFreezeEditRemove", customerPortalSettingsDTO.getVariantIdsToFreezeEditRemove());
        customerPortalSettingJson.put("preventCancellationBeforeDays", customerPortalSettingsDTO.getPreventCancellationBeforeDays());
        customerPortalSettingJson.put("disAllowVariantIdsForOneTimeProductAdd", customerPortalSettingsDTO.getDisAllowVariantIdsForOneTimeProductAdd());
        customerPortalSettingJson.put("disAllowVariantIdsForSubscriptionProductAdd", customerPortalSettingsDTO.getDisAllowVariantIdsForSubscriptionProductAdd());
        customerPortalSettingJson.put("hideAddSubscriptionProductSection", customerPortalSettingsDTO.getHideAddSubscriptionProductSection());
        customerPortalSettingJson.put("allowOnlyOneTimeProductOnAddProductFlag", customerPortalSettingsDTO.getAllowOnlyOneTimeProductOnAddProductFlag());
        customerPortalSettingJson.put("discountCouponRemoveText", customerPortalSettingsDTO.getDiscountCouponRemoveText());
        customerPortalSettingJson.put("pleaseSelectText", customerPortalSettingsDTO.getPleaseSelectText());
        customerPortalSettingJson.put("shippingAddressNotAvailableText", customerPortalSettingsDTO.getShippingAddressNotAvailableText());
        customerPortalSettingJson.put("discountCouponNotAppliedText", customerPortalSettingsDTO.getDiscountCouponNotAppliedText());
        customerPortalSettingJson.put("sellingPlanNameText", customerPortalSettingsDTO.getSellingPlanNameText());
        customerPortalSettingJson.put("shopPayPaymentUpdateText", customerPortalSettingsDTO.getShopPayPaymentUpdateText());

        customerPortalSettingJson.put("selectProductLabelText", customerPortalSettingsDTO.getSelectProductLabelText());
        customerPortalSettingJson.put("purchaseOptionLabelText", customerPortalSettingsDTO.getPurchaseOptionLabelText());
        customerPortalSettingJson.put("finishLabelText", customerPortalSettingsDTO.getFinishLabelText());
        customerPortalSettingJson.put("nextBtnText", customerPortalSettingsDTO.getNextBtnText());
        customerPortalSettingJson.put("previousBtnText", customerPortalSettingsDTO.getPreviousBtnText());
        customerPortalSettingJson.put("closeBtnText", customerPortalSettingsDTO.getCloseBtnText());
        customerPortalSettingJson.put("deleteConfirmationMsgText", customerPortalSettingsDTO.getDeleteConfirmationMsgText());
        customerPortalSettingJson.put("deleteMsgText", customerPortalSettingsDTO.getDeleteMsgText());
        customerPortalSettingJson.put("yesBtnText", customerPortalSettingsDTO.getYesBtnText());
        customerPortalSettingJson.put("noBtnText", customerPortalSettingsDTO.getNoBtnText());
        customerPortalSettingJson.put("oneTimePurchaseNoteText", customerPortalSettingsDTO.getOneTimePurchaseNoteText());
        customerPortalSettingJson.put("clickHereText", customerPortalSettingsDTO.getClickHereText());
        customerPortalSettingJson.put("productAddMessageText", customerPortalSettingsDTO.getProductAddMessageText());
        customerPortalSettingJson.put("choosePurchaseOptionLabelText", customerPortalSettingsDTO.getChoosePurchaseOptionLabelText());
        customerPortalSettingJson.put("oneTimePurchaseMessageText", customerPortalSettingsDTO.getOneTimePurchaseMessageText());
        customerPortalSettingJson.put("contractUpdateMessageText", customerPortalSettingsDTO.getContractUpdateMessageText());
        customerPortalSettingJson.put("oneTimePurchaseDisplayMessageText", customerPortalSettingsDTO.getOneTimePurchaseDisplayMessageText());
        customerPortalSettingJson.put("addProductFinishedMessageText", customerPortalSettingsDTO.getAddProductFinishedMessageText());
        customerPortalSettingJson.put("contractErrorMessageText", customerPortalSettingsDTO.getContractErrorMessageText());
        customerPortalSettingJson.put("addToSubscriptionTitleCP", customerPortalSettingsDTO.getAddToSubscriptionTitleCP());
        customerPortalSettingJson.put("oneTimePurchaseTitleCP", customerPortalSettingsDTO.getOneTimePurchaseTitleCP());
        customerPortalSettingJson.put("seeMoreProductBtnText", customerPortalSettingsDTO.getSeeMoreProductBtnText());
        customerPortalSettingJson.put("viewAttributeLabelText", customerPortalSettingsDTO.getViewAttributeLabelText());
        customerPortalSettingJson.put("attributeNameLabelText", customerPortalSettingsDTO.getAttributeNameLabelText());
        customerPortalSettingJson.put("swapProductLabelText", customerPortalSettingsDTO.getSwapProductLabelText());
        customerPortalSettingJson.put("swapProductSearchBarText", customerPortalSettingsDTO.getSwapProductSearchBarText());
        customerPortalSettingJson.put("enableSwapProductFeature", customerPortalSettingsDTO.getEnableSwapProductFeature());
        customerPortalSettingJson.put("enableTabletForceView", customerPortalSettingsDTO.getEnableTabletForceView());
        customerPortalSettingJson.put("swapProductBtnText", customerPortalSettingsDTO.getSwapProductBtnText());
        customerPortalSettingJson.put("attributeValue", customerPortalSettingsDTO.getAttributeValue());
        customerPortalSettingJson.put("addNewButtonText", customerPortalSettingsDTO.getAddNewButtonText());
        customerPortalSettingJson.put("attributeHeadingText", customerPortalSettingsDTO.getAttributeHeadingText());
        customerPortalSettingJson.put("enableViewAttributes", customerPortalSettingsDTO.getEnableViewAttributes());
        customerPortalSettingJson.put("enableEditOrderNotes", customerPortalSettingsDTO.getEnableEditOrderNotes());
        customerPortalSettingJson.put("showSellingPlanFrequencies", customerPortalSettingsDTO.getShowSellingPlanFrequencies());
        customerPortalSettingJson.put("totalPricePerDeliveryText", customerPortalSettingsDTO.getTotalPricePerDeliveryText());
        customerPortalSettingJson.put("fulfilledText", customerPortalSettingsDTO.getFulfilledText());
        customerPortalSettingJson.put("dateFormat", customerPortalSettingsDTO.getDateFormat());
        customerPortalSettingJson.put("discountCouponAppliedText", customerPortalSettingsDTO.getDiscountCouponAppliedText());
        customerPortalSettingJson.put("subscriptionPausedMessageText", customerPortalSettingsDTO.getSubscriptionPausedMessageText());
        customerPortalSettingJson.put("subscriptionActivatedMessageText", customerPortalSettingsDTO.getSubscriptionActivatedMessageText());
        customerPortalSettingJson.put("unableToUpdateSubscriptionStatusMessageText", customerPortalSettingsDTO.getUnableToUpdateSubscriptionStatusMessageText());
        customerPortalSettingJson.put("selectCancellationReasonLabelText", customerPortalSettingsDTO.getSelectCancellationReasonLabelText());
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessTitleText", customerPortalSettingsDTO.getUpcomingOrderChangePopupSuccessTitleText());
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessDescriptionText", customerPortalSettingsDTO.getUpcomingOrderChangePopupSuccessDescriptionText());
        customerPortalSettingJson.put("upcomingOrderChangePopupSuccessClosebtnText", customerPortalSettingsDTO.getUpcomingOrderChangePopupSuccessClosebtnText());
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureDescriptionText", customerPortalSettingsDTO.getUpcomingOrderChangePopupFailureDescriptionText());
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureClosebtnText", customerPortalSettingsDTO.getUpcomingOrderChangePopupFailureClosebtnText());
        customerPortalSettingJson.put("upcomingOrderChangePopupFailureTitleText", customerPortalSettingsDTO.getUpcomingOrderChangePopupFailureTitleText());

        customerPortalSettingJson.put("enableSplitContract",customerPortalSettingsDTO.getEnableSplitContract());
        customerPortalSettingJson.put("splitContractMessage",customerPortalSettingsDTO.getSplitContractMessage());
        customerPortalSettingJson.put("splitContractText",customerPortalSettingsDTO.getSplitContractText());

        customerPortalSettingJson.put("redeemRewardsTextV2",customerPortalSettingsDTO.getRedeemRewardsTextV2());
        customerPortalSettingJson.put("rewardsTextV2",customerPortalSettingsDTO.getRewardsTextV2());
        customerPortalSettingJson.put("yourRewardsTextV2",customerPortalSettingsDTO.getYourRewardsTextV2());
        customerPortalSettingJson.put("yourAvailableRewardsPointsTextV2",customerPortalSettingsDTO.getYourAvailableRewardsPointsTextV2());

        customerPortalSettingJson.put("cancellationDateTitleText",customerPortalSettingsDTO.getCancellationDateTitleText());
        customerPortalSettingJson.put("selectedCancellationReasonTitleText",customerPortalSettingsDTO.getSelectedCancellationReasonTitleText());
        customerPortalSettingJson.put("cancellationNoteTitleText",customerPortalSettingsDTO.getCancellationNoteTitleText());

        customerPortalSettingJson.put("selectSplitMethodLabelText",customerPortalSettingsDTO.getSelectSplitMethodLabelText());
        customerPortalSettingJson.put("splitWithOrderPlacedSelectOptionText",customerPortalSettingsDTO.getSplitWithOrderPlacedSelectOptionText());
        customerPortalSettingJson.put("splitWithoutOrderPlacedSelectOptionText",customerPortalSettingsDTO.getSplitWithoutOrderPlacedSelectOptionText());
        customerPortalSettingJson.put("contractCancelledBadgeText",customerPortalSettingsDTO.getContractCancelledBadgeText());

        customerPortalSettingJson.put("chooseAnotherPaymentMethodTitleText",customerPortalSettingsDTO.getChooseAnotherPaymentMethodTitleText());
        customerPortalSettingJson.put("selectPaymentMethodTitleText",customerPortalSettingsDTO.getSelectPaymentMethodTitleText());
        customerPortalSettingJson.put("changePaymentMessage",customerPortalSettingsDTO.getChangePaymentMessage());
        customerPortalSettingJson.put("updatePaymentMethodTitleText",customerPortalSettingsDTO.getUpdatePaymentMethodTitleText());
        customerPortalSettingJson.put("productFilterConfig",customerPortalSettingsDTO.getProductFilterConfig());
        customerPortalSettingJson.put("reschedulingPolicies",customerPortalSettingsDTO.getReschedulingPolicies());

        customerPortalSettingJson.put("upcomingTabHeaderHTML",customerPortalSettingsDTO.getUpcomingTabHeaderHTML());
        customerPortalSettingJson.put("schedulesTabHeaderHTML",customerPortalSettingsDTO.getSchedulesTabHeaderHTML());
        customerPortalSettingJson.put("historyTabHeaderHTML",customerPortalSettingsDTO.getHistoryTabHeaderHTML());

        customerPortalSettingJson.put("allowedProductIdsForOneTimeProductAdd", customerPortalSettingsDTO.getAllowedProductIdsForOneTimeProductAdd());

    }

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    /**
     * {@code PUT  /api/customer-portal-settings} : Updates an existing customerPortalSettings.
     *
     * @param customerPortalSettingsDTO the customerPortalSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerPortalSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the customerPortalSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerPortalSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/customer-portal-settings")
    public ResponseEntity<CustomerPortalSettingsDTO> updateCustomerPortalSettings(@Valid @RequestBody CustomerPortalSettingsDTO customerPortalSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerPortalSettings : {}", customerPortalSettingsDTO);

        if (customerPortalSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPortalSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        JSONObject customerPortalSettingJson = new JSONObject();
        setCustomerPortalJsonFromDTO(customerPortalSettingsDTO, customerPortalSettingJson);

        customerPortalSettingsDTO.setCustomerPortalSettingJson(customerPortalSettingJson.toString());

        CustomerPortalSettingsDTO result = customerPortalSettingsService.save(customerPortalSettingsDTO);
        for (Map.Entry<String, Object> entry : customerPortalSettingsDTO.getAdditionalProperties().entrySet()) {
            result.setAdditionalProperty(entry.getKey(), entry.getValue());
        }

        CustomerPortalSettingsV2DTO customerPortalSettingsV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
        }, result.getCustomerPortalSettingJson());

        setCustomerPortalDTOFromV2DTO(result, customerPortalSettingsV2DTO);

        if (ObjectUtils.isNotEmpty(customerPortalSettingsDTO.getShopCustomizationData())) {
            shopCustomizationService.updateShopCustomizationData(customerPortalSettingsDTO.getShopCustomizationData(), shop);
        }

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.cp.", "CUSTOMER_PORTAL", customerPortalSettingsDTO.getAdditionalProperties());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Customer Portal Settings Updated.", ""))
            .body(result);
    }

    private void setCustomerPortalDTOFromV2DTO(CustomerPortalSettingsDTO result, CustomerPortalSettingsV2DTO customerPortalSettingsV2DTO) {

        try {
            BeanUtils.copyProperties(result, customerPortalSettingsV2DTO);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
        }

        result.setShippingLabelText(customerPortalSettingsV2DTO.getShippingLabelText());
        result.setOrderDateText(customerPortalSettingsV2DTO.getOrderDateText());
        result.setSaveButtonText(customerPortalSettingsV2DTO.getSaveButtonText());
        result.setFailureText(customerPortalSettingsV2DTO.getFailureText());
        result.setEmailAddressText(customerPortalSettingsV2DTO.getEmailAddressText());
        result.setEmailMagicLinkText(customerPortalSettingsV2DTO.getEmailMagicLinkText());
        result.setRetriveMagicLinkText(customerPortalSettingsV2DTO.getRetriveMagicLinkText());
        result.setValidEmailMessage(customerPortalSettingsV2DTO.getValidEmailMessage());
        result.setSendEmailText(customerPortalSettingsV2DTO.getSendEmailText());
        result.setChooseDifferentProductActionText(customerPortalSettingsV2DTO.getChooseDifferentProductActionText());
        result.setChooseDifferentProductText(customerPortalSettingsV2DTO.getChooseDifferentProductText());
        result.setConfirmSkipFulfillmentBtnText(customerPortalSettingsV2DTO.getConfirmSkipFulfillmentBtnText());
        result.setConfirmSkipOrder(customerPortalSettingsV2DTO.getConfirmSkipOrder());
        result.setSkipFulfillmentButtonText(customerPortalSettingsV2DTO.getSkipFulfillmentButtonText());
        result.setConfirmCommonText(customerPortalSettingsV2DTO.getConfirmCommonText());
        result.setOrderNowDescriptionText(customerPortalSettingsV2DTO.getOrderNowDescriptionText());
        result.setDiscountDetailsTitleText(customerPortalSettingsV2DTO.getDiscountDetailsTitleText());
        result.setAddress1LabelText(customerPortalSettingsV2DTO.getAddress1LabelText());
        result.setAddress2LabelText(customerPortalSettingsV2DTO.getAddress2LabelText());
        result.setCompanyLabelText(customerPortalSettingsV2DTO.getCompanyLabelText());
        result.setCityLabelText(customerPortalSettingsV2DTO.getCityLabelText());
        result.setCountryLabelText(customerPortalSettingsV2DTO.getCountryLabelText());
        result.setFirstNameLabelText(customerPortalSettingsV2DTO.getFirstNameLabelText());
        result.setLastNameLabelText(customerPortalSettingsV2DTO.getLastNameLabelText());
        result.setProvinceLabelText(customerPortalSettingsV2DTO.getProvinceLabelText());
        result.setPhoneLabelText(customerPortalSettingsV2DTO.getPhoneLabelText());
        result.setZipLabelText(customerPortalSettingsV2DTO.getZipLabelText());
        result.setAddressHeaderTitleText(customerPortalSettingsV2DTO.getAddressHeaderTitleText());
        result.setChangeShippingAddressFlag(customerPortalSettingsV2DTO.getChangeShippingAddressFlag());
        result.setUpdateEditShippingButtonText(customerPortalSettingsV2DTO.getUpdateEditShippingButtonText());
        result.setCancelEditShippingButtonText(customerPortalSettingsV2DTO.getCancelEditShippingButtonText());
        result.setPauseSubscriptionText(customerPortalSettingsV2DTO.getPauseSubscriptionText());
        result.setResumeSubscriptionText(customerPortalSettingsV2DTO.getResumeSubscriptionText());
        result.setPauseBadgeText(customerPortalSettingsV2DTO.getPauseBadgeText());
        result.setDiscountNoteTitle(customerPortalSettingsV2DTO.getDiscountNoteTitle());
        result.setInitialDiscountNoteDescription(customerPortalSettingsV2DTO.getInitialDiscountNoteDescription());
        result.setAfterCycleDiscountNoteDescription(customerPortalSettingsV2DTO.getAfterCycleDiscountNoteDescription());
        result.setProductRemovedTooltip(customerPortalSettingsV2DTO.getProductRemovedTooltip());
        result.setDeliveryPriceText(customerPortalSettingsV2DTO.getDeliveryPriceText());
        result.setShippingOptionText(customerPortalSettingsV2DTO.getShippingOptionText());
        result.setNextDeliveryDate(customerPortalSettingsV2DTO.getNextDeliveryDate());
        result.setEveryLabelText(customerPortalSettingsV2DTO.getEveryLabelText());
        result.setExpiredTokenText(customerPortalSettingsV2DTO.getExpiredTokenText());
        result.setPortalLoginLinkText(customerPortalSettingsV2DTO.getPortalLoginLinkText());
        result.setLocaleDate(customerPortalSettingsV2DTO.getLocaleDate());
        result.setCustomerIdText(customerPortalSettingsV2DTO.getCustomerIdText());
        result.setHelloNameText(customerPortalSettingsV2DTO.getHelloNameText());
        result.setGoBackButtonText(customerPortalSettingsV2DTO.getGoBackButtonText());
        result.setChangeVariantLabelText(customerPortalSettingsV2DTO.getChangeVariantLabelText());
        result.setProvinceCodeLabelText(customerPortalSettingsV2DTO.getProvinceCodeLabelText());
        result.setCountryCodeLabelText(customerPortalSettingsV2DTO.getCountryCodeLabelText());
        result.setPleaseWaitLoaderText(customerPortalSettingsV2DTO.getPleaseWaitLoaderText());
        result.setCancelSubscriptionMinimumBillingIterationsMessage(customerPortalSettingsV2DTO.getCancelSubscriptionMinimumBillingIterationsMessage());
        result.setTopHtml(customerPortalSettingsV2DTO.getTopHtml());
        result.setBottomHtml(customerPortalSettingsV2DTO.getBottomHtml());
        result.setDiscountCodeText(customerPortalSettingsV2DTO.getDiscountCodeText());
        result.setDiscountCodeApplyButtonText(customerPortalSettingsV2DTO.getDiscountCodeApplyButtonText());
        result.setApplySellingPlanBasedDiscount(customerPortalSettingsV2DTO.getApplySellingPlanBasedDiscount());
        result.setApplySubscriptionDiscountForOtp(customerPortalSettingsV2DTO.getApplySubscriptionDiscountForOtp());
        result.setApplySubscriptionDiscount(customerPortalSettingsV2DTO.isApplySubscriptionDiscount());
        result.setRemoveDiscountCodeAutomatically(customerPortalSettingsV2DTO.getRemoveDiscountCodeAutomatically());
        result.setRemoveDiscountCodeLabel(customerPortalSettingsV2DTO.getRemoveDiscountCodeLabel());
        result.setEnableSplitContract(customerPortalSettingsV2DTO.getEnableSplitContract());
        result.setSplitContractMessage(customerPortalSettingsV2DTO.getSplitContractMessage());
        result.setSplitContractText(customerPortalSettingsV2DTO.getSplitContractText());
        result.setSubscriptionDiscountTypeUnit(customerPortalSettingsV2DTO.getSubscriptionDiscountTypeUnit());
        result.setSubscriptionDiscount(customerPortalSettingsV2DTO.getSubscriptionDiscount());
        result.setUpSellMessage(customerPortalSettingsV2DTO.getUpSellMessage());
        result.setFreezeUpdateSubscriptionMessage(customerPortalSettingsV2DTO.getFreezeUpdateSubscriptionMessage());
        result.setSubscriptionContractFreezeMessage(customerPortalSettingsV2DTO.getSubscriptionContractFreezeMessage());
        result.setPreventCancellationBeforeDaysMessage(customerPortalSettingsV2DTO.getPreventCancellationBeforeDaysMessage());
        result.setDiscountRecurringCycleLimitOnCancellation(customerPortalSettingsV2DTO.getDiscountRecurringCycleLimitOnCancellation());
        result.setDiscountMessageOnCancellation(customerPortalSettingsV2DTO.getDiscountMessageOnCancellation());
        result.setDiscountPercentageOnCancellation(customerPortalSettingsV2DTO.getDiscountPercentageOnCancellation());
        result.setOfferDiscountOnCancellation(customerPortalSettingsV2DTO.getOfferDiscountOnCancellation());
        result.setEnableSkipFulFillment(customerPortalSettingsV2DTO.getEnableSkipFulFillment());
        result.setMagicLinkEmailFlag(customerPortalSettingsV2DTO.getMagicLinkEmailFlag());

        result.setFrequencyChangeWarningTitle(customerPortalSettingsV2DTO.getFrequencyChangeWarningTitle());
        result.setFrequencyChangeWarningDescription(customerPortalSettingsV2DTO.getFrequencyChangeWarningDescription());
        result.setVariantIdsToFreezeEditRemove(customerPortalSettingsV2DTO.getVariantIdsToFreezeEditRemove());
        result.setPreventCancellationBeforeDays(customerPortalSettingsV2DTO.getPreventCancellationBeforeDays());
        result.setDisAllowVariantIdsForOneTimeProductAdd(customerPortalSettingsV2DTO.getDisAllowVariantIdsForOneTimeProductAdd());
        result.setDisAllowVariantIdsForSubscriptionProductAdd(customerPortalSettingsV2DTO.getDisAllowVariantIdsForSubscriptionProductAdd());
        result.setHideAddSubscriptionProductSection(customerPortalSettingsV2DTO.getHideAddSubscriptionProductSection());
        result.setAllowOnlyOneTimeProductOnAddProductFlag(customerPortalSettingsV2DTO.getAllowOnlyOneTimeProductOnAddProductFlag());
        result.setDiscountCouponRemoveText(customerPortalSettingsV2DTO.getDiscountCouponRemoveText());
        result.setPleaseSelectText(customerPortalSettingsV2DTO.getPleaseSelectText());
        result.setShippingAddressNotAvailableText(customerPortalSettingsV2DTO.getShippingAddressNotAvailableText());
        result.setDiscountCouponNotAppliedText(customerPortalSettingsV2DTO.getDiscountCouponNotAppliedText());
        result.setSellingPlanNameText(customerPortalSettingsV2DTO.getSellingPlanNameText());
        result.setShopPayPaymentUpdateText(customerPortalSettingsV2DTO.getShopPayPaymentUpdateText());

        result.setSelectProductLabelText(customerPortalSettingsV2DTO.getSelectProductLabelText());
        result.setPurchaseOptionLabelText(customerPortalSettingsV2DTO.getPurchaseOptionLabelText());
        result.setFinishLabelText(customerPortalSettingsV2DTO.getFinishLabelText());
        result.setNextBtnText(customerPortalSettingsV2DTO.getNextBtnText());
        result.setPreviousBtnText(customerPortalSettingsV2DTO.getPreviousBtnText());
        result.setCloseBtnText(customerPortalSettingsV2DTO.getCloseBtnText());
        result.setDeleteConfirmationMsgText(customerPortalSettingsV2DTO.getDeleteConfirmationMsgText());
        result.setDeleteMsgText(customerPortalSettingsV2DTO.getDeleteMsgText());
        result.setYesBtnText(customerPortalSettingsV2DTO.getYesBtnText());
        result.setNoBtnText(customerPortalSettingsV2DTO.getNoBtnText());
        result.setOneTimePurchaseNoteText(customerPortalSettingsV2DTO.getOneTimePurchaseNoteText());
        result.setClickHereText(customerPortalSettingsV2DTO.getClickHereText());
        result.setProductAddMessageText(customerPortalSettingsV2DTO.getProductAddMessageText());
        result.setChoosePurchaseOptionLabelText(customerPortalSettingsV2DTO.getChoosePurchaseOptionLabelText());
        result.setOneTimePurchaseMessageText(customerPortalSettingsV2DTO.getOneTimePurchaseMessageText());
        result.setContractUpdateMessageText(customerPortalSettingsV2DTO.getContractUpdateMessageText());
        result.setOneTimePurchaseDisplayMessageText(customerPortalSettingsV2DTO.getOneTimePurchaseDisplayMessageText());
        result.setAddProductFinishedMessageText(customerPortalSettingsV2DTO.getAddProductFinishedMessageText());
        result.setContractErrorMessageText(customerPortalSettingsV2DTO.getContractErrorMessageText());
        result.setAddToSubscriptionTitleCP(customerPortalSettingsV2DTO.getAddToSubscriptionTitleCP());
        result.setOneTimePurchaseTitleCP(customerPortalSettingsV2DTO.getOneTimePurchaseTitleCP());
        result.setSeeMoreProductBtnText(customerPortalSettingsV2DTO.getSeeMoreProductBtnText());
        result.setViewAttributeLabelText(customerPortalSettingsV2DTO.getViewAttributeLabelText());
        result.setAttributeNameLabelText(customerPortalSettingsV2DTO.getAttributeNameLabelText());
        result.setEnableSwapProductFeature(customerPortalSettingsV2DTO.getEnableSwapProductFeature());
        result.setSwapProductSearchBarText(customerPortalSettingsV2DTO.getSwapProductSearchBarText());
        result.setSwapProductLabelText(customerPortalSettingsV2DTO.getSwapProductLabelText());
        result.setSwapProductBtnText(customerPortalSettingsV2DTO.getSwapProductBtnText());
        result.setAttributeValue(customerPortalSettingsV2DTO.getAttributeValue());
        result.setAddNewButtonText(customerPortalSettingsV2DTO.getAddNewButtonText());
        result.setAttributeHeadingText(customerPortalSettingsV2DTO.getAttributeHeadingText());
        result.setEnableViewAttributes(customerPortalSettingsV2DTO.getEnableViewAttributes());
        result.setEnableEditOrderNotes(customerPortalSettingsV2DTO.getEnableEditOrderNotes());
        result.setShowSellingPlanFrequencies(customerPortalSettingsV2DTO.getShowSellingPlanFrequencies());
        result.setTotalPricePerDeliveryText(customerPortalSettingsV2DTO.getTotalPricePerDeliveryText());
        result.setFulfilledText(customerPortalSettingsV2DTO.getFulfilledText());
        result.setDateFormat(customerPortalSettingsV2DTO.getDateFormat());
        result.setDiscountCouponAppliedText(customerPortalSettingsV2DTO.getDiscountCouponAppliedText());
        result.setSubscriptionPausedMessageText(customerPortalSettingsV2DTO.getSubscriptionPausedMessageText());
        result.setSubscriptionActivatedMessageText(customerPortalSettingsV2DTO.getSubscriptionActivatedMessageText());
        result.setUnableToUpdateSubscriptionStatusMessageText(customerPortalSettingsV2DTO.getUnableToUpdateSubscriptionStatusMessageText());
        result.setSelectCancellationReasonLabelText(customerPortalSettingsV2DTO.getSelectCancellationReasonLabelText());
        result.setUpcomingOrderChangePopupSuccessTitleText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessTitleText());
        result.setUpcomingOrderChangePopupSuccessDescriptionText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessDescriptionText());
        result.setUpcomingOrderChangePopupSuccessClosebtnText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessClosebtnText());
        result.setUpcomingOrderChangePopupFailureTitleText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureTitleText());
        result.setUpcomingOrderChangePopupFailureDescriptionText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureDescriptionText());
        result.setUpcomingOrderChangePopupFailureClosebtnText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureClosebtnText());
        result.setReschedulingPolicies(customerPortalSettingsV2DTO.getReschedulingPolicies());

        result.setUpcomingTabHeaderHTML(customerPortalSettingsV2DTO.getUpcomingTabHeaderHTML());
        result.setSchedulesTabHeaderHTML(customerPortalSettingsV2DTO.getSchedulesTabHeaderHTML());
        result.setHistoryTabHeaderHTML(customerPortalSettingsV2DTO.getHistoryTabHeaderHTML());

        result.setAllowedProductIdsForOneTimeProductAdd(customerPortalSettingsV2DTO.getAllowedProductIdsForOneTimeProductAdd());

    }

    /**
     * {@code GET  /api/customer-portal-settings} : get all the customerPortalSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerPortalSettings in body.
     */
    @GetMapping("/api/customer-portal-settings")
    public List<CustomerPortalSettingsDTO> getAllCustomerPortalSettings() {
        log.debug("REST request to get all CustomerPortalSettings");
        Optional<CustomerPortalSettingsDTO> customerPortalSettingsDTOOptional = customerPortalSettingsService.findByShop(SecurityUtils.getCurrentUserLogin().get());
        List<CustomerPortalSettingsDTO> result = new ArrayList<>();

        customerPortalSettingsDTOOptional.ifPresent(result::add);

        return result;
    }

    /**
     * {@code GET  /api/customer-portal-settings/:id} : get the "id" customerPortalSettings.
     *
     * @param id the id of the customerPortalSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerPortalSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    //TODO: Fix Security Issue
    @GetMapping(value = {"/api/customer-portal-settings/{id}", "/subscriptions/cp/api/customer-portal-settings/{id}"})
    @CrossOrigin
    public ResponseEntity<CustomerPortalSettingsDTO> getCustomerPortalSettings(@PathVariable String id, HttpServletRequest request) {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();


        Optional<CustomerPortalSettingsDTO> customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shopName);

        return ResponseUtil.wrapOrNotFound(customerPortalSettingsDTO);
    }

    @GetMapping("/api/external/v2/customer-portal-settings/{id}")
    @ApiOperation("Control Customer Portal Settings")
    @CrossOrigin
    public ResponseEntity<CustomerPortalSettingsDTO> getCustomerPortalSettingsV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) {
//        log.debug("REST request to get CustomerPortalSettings : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/customer-portal-settings/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<CustomerPortalSettingsDTO> customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(customerPortalSettingsDTO);

    }
    /**
     * {@code DELETE  /api/customer-portal-settings/:id} : delete the "id" customerPortalSettings.
     *
     * @param id the id of the customerPortalSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/customer-portal-settings/{id}")
    public ResponseEntity<Void> deleteCustomerPortalSettings(@PathVariable Long id) {
        log.debug("REST request to delete CustomerPortalSettings : {}", id);
        customerPortalSettingsService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Customer Portal Settings Deleted.", "")).build();
    }
}
