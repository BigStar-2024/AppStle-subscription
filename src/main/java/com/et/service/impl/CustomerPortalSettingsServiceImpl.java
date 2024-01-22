package com.et.service.impl;

import com.et.domain.CustomerPortalSettings;
import com.et.pojo.LabelValueInfo;
import com.et.repository.CustomerPortalSettingsRepository;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.ShopLabelService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.service.dto.CustomerPortalSettingsV2DTO;
import com.et.service.mapper.CustomerPortalSettingsMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CustomerPortalSettings}.
 */
@Service
@Transactional
public class CustomerPortalSettingsServiceImpl implements CustomerPortalSettingsService {

    private final Logger log = LoggerFactory.getLogger(CustomerPortalSettingsServiceImpl.class);

    @Value("classpath:templates/label-migration.properties")
    Resource labelMigrationProperties;

    private final CustomerPortalSettingsRepository customerPortalSettingsRepository;

    private final CustomerPortalSettingsMapper customerPortalSettingsMapper;

    private final ShopLabelService shopLabelService;


    public CustomerPortalSettingsServiceImpl(CustomerPortalSettingsRepository customerPortalSettingsRepository,
                                             CustomerPortalSettingsMapper customerPortalSettingsMapper,
                                             ShopLabelService shopLabelService) {
        this.customerPortalSettingsRepository = customerPortalSettingsRepository;
        this.customerPortalSettingsMapper = customerPortalSettingsMapper;
        this.shopLabelService = shopLabelService;
    }

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Override
    public CustomerPortalSettingsDTO save(CustomerPortalSettingsDTO customerPortalSettingsDTO) {
        log.debug("Request to save CustomerPortalSettings : {}", customerPortalSettingsDTO);
        CustomerPortalSettings customerPortalSettings = customerPortalSettingsMapper.toEntity(customerPortalSettingsDTO);
        customerPortalSettings = customerPortalSettingsRepository.save(customerPortalSettings);
        return customerPortalSettingsMapper.toDto(customerPortalSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerPortalSettingsDTO> findAll() {
        log.debug("Request to get all CustomerPortalSettings");
        return customerPortalSettingsRepository.findAll().stream()
            .map(customerPortalSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerPortalSettingsDTO> findOne(Long id) {
        log.debug("Request to get CustomerPortalSettings : {}", id);
        return customerPortalSettingsRepository.findById(id)
            .map(customerPortalSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerPortalSettings : {}", id);
        customerPortalSettingsRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerPortalSettingsDTO> findByShop(String shop) {
        Optional<CustomerPortalSettingsDTO> customerPortalSettingsDTO = customerPortalSettingsRepository.findByShop(shop)
            .map(customerPortalSettingsMapper::toDto);

        CustomerPortalSettingsV2DTO customerPortalSettingsV2DTO = new CustomerPortalSettingsV2DTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            if (customerPortalSettingsDTO.isEmpty()) {
                return customerPortalSettingsDTO;
            }
            customerPortalSettingsV2DTO = objectMapper.readValue(customerPortalSettingsDTO.get().getCustomerPortalSettingJson(), CustomerPortalSettingsV2DTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BeanUtils.copyProperties(customerPortalSettingsDTO.get(), customerPortalSettingsV2DTO);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
        }

        customerPortalSettingsDTO.get().setShippingLabelText(customerPortalSettingsV2DTO.getShippingLabelText());
        customerPortalSettingsDTO.get().setFailureText(customerPortalSettingsV2DTO.getFailureText());
        customerPortalSettingsDTO.get().setEmailAddressText(customerPortalSettingsV2DTO.getEmailAddressText());
        customerPortalSettingsDTO.get().setEmailMagicLinkText(customerPortalSettingsV2DTO.getEmailMagicLinkText());
        customerPortalSettingsDTO.get().setRetriveMagicLinkText(customerPortalSettingsV2DTO.getRetriveMagicLinkText());
        customerPortalSettingsDTO.get().setValidEmailMessage(customerPortalSettingsV2DTO.getValidEmailMessage());
        customerPortalSettingsDTO.get().setSendEmailText(customerPortalSettingsV2DTO.getSendEmailText());
        customerPortalSettingsDTO.get().setChooseDifferentProductActionText(customerPortalSettingsV2DTO.getChooseDifferentProductActionText());
        customerPortalSettingsDTO.get().setChooseDifferentProductText(customerPortalSettingsV2DTO.getChooseDifferentProductText());
        customerPortalSettingsDTO.get().setConfirmSkipFulfillmentBtnText(customerPortalSettingsV2DTO.getConfirmSkipFulfillmentBtnText());
        customerPortalSettingsDTO.get().setConfirmSkipOrder(customerPortalSettingsV2DTO.getConfirmSkipOrder());
        customerPortalSettingsDTO.get().setSkipFulfillmentButtonText(customerPortalSettingsV2DTO.getSkipFulfillmentButtonText());
        customerPortalSettingsDTO.get().setConfirmCommonText(customerPortalSettingsV2DTO.getConfirmCommonText());
        customerPortalSettingsDTO.get().setOrderNowDescriptionText(customerPortalSettingsV2DTO.getOrderNowDescriptionText());
        customerPortalSettingsDTO.get().setDiscountDetailsTitleText(customerPortalSettingsV2DTO.getDiscountDetailsTitleText());
        customerPortalSettingsDTO.get().setSaveButtonText(customerPortalSettingsV2DTO.getSaveButtonText());
        customerPortalSettingsDTO.get().setOrderDateText(customerPortalSettingsV2DTO.getOrderDateText());
        customerPortalSettingsDTO.get().setAddress1LabelText(customerPortalSettingsV2DTO.getAddress1LabelText());
        customerPortalSettingsDTO.get().setAddress2LabelText(customerPortalSettingsV2DTO.getAddress2LabelText());
        customerPortalSettingsDTO.get().setCompanyLabelText(customerPortalSettingsV2DTO.getCompanyLabelText());
        customerPortalSettingsDTO.get().setCityLabelText(customerPortalSettingsV2DTO.getCityLabelText());
        customerPortalSettingsDTO.get().setCountryLabelText(customerPortalSettingsV2DTO.getCountryLabelText());
        customerPortalSettingsDTO.get().setFirstNameLabelText(customerPortalSettingsV2DTO.getFirstNameLabelText());
        customerPortalSettingsDTO.get().setLastNameLabelText(customerPortalSettingsV2DTO.getLastNameLabelText());
        customerPortalSettingsDTO.get().setProvinceLabelText(customerPortalSettingsV2DTO.getProvinceLabelText());
        customerPortalSettingsDTO.get().setPhoneLabelText(customerPortalSettingsV2DTO.getPhoneLabelText());
        customerPortalSettingsDTO.get().setZipLabelText(customerPortalSettingsV2DTO.getZipLabelText());
        customerPortalSettingsDTO.get().setAddressHeaderTitleText(customerPortalSettingsV2DTO.getAddressHeaderTitleText());
        customerPortalSettingsDTO.get().setChangeShippingAddressFlag(customerPortalSettingsV2DTO.getChangeShippingAddressFlag());
        customerPortalSettingsDTO.get().setUpdateEditShippingButtonText(customerPortalSettingsV2DTO.getUpdateEditShippingButtonText());
        customerPortalSettingsDTO.get().setCancelEditShippingButtonText(customerPortalSettingsV2DTO.getCancelEditShippingButtonText());
        customerPortalSettingsDTO.get().setPauseSubscriptionText(customerPortalSettingsV2DTO.getPauseSubscriptionText());
        customerPortalSettingsDTO.get().setResumeSubscriptionText(customerPortalSettingsV2DTO.getResumeSubscriptionText());
        customerPortalSettingsDTO.get().setPauseBadgeText(customerPortalSettingsV2DTO.getPauseBadgeText());
        customerPortalSettingsDTO.get().setDiscountNoteTitle(customerPortalSettingsV2DTO.getDiscountNoteTitle());
        customerPortalSettingsDTO.get().setInitialDiscountNoteDescription(customerPortalSettingsV2DTO.getInitialDiscountNoteDescription());
        customerPortalSettingsDTO.get().setAfterCycleDiscountNoteDescription(customerPortalSettingsV2DTO.getAfterCycleDiscountNoteDescription());
        customerPortalSettingsDTO.get().setProductRemovedTooltip(customerPortalSettingsV2DTO.getProductRemovedTooltip());
        customerPortalSettingsDTO.get().setDeliveryPriceText(customerPortalSettingsV2DTO.getDeliveryPriceText());
        customerPortalSettingsDTO.get().setShippingOptionText(customerPortalSettingsV2DTO.getShippingOptionText());
        customerPortalSettingsDTO.get().setNextDeliveryDate(customerPortalSettingsV2DTO.getNextDeliveryDate());
        customerPortalSettingsDTO.get().setEveryLabelText(customerPortalSettingsV2DTO.getEveryLabelText());
        customerPortalSettingsDTO.get().setExpiredTokenText(customerPortalSettingsV2DTO.getExpiredTokenText());
        customerPortalSettingsDTO.get().setPortalLoginLinkText(customerPortalSettingsV2DTO.getPortalLoginLinkText());
        customerPortalSettingsDTO.get().setLocaleDate(customerPortalSettingsV2DTO.getLocaleDate());
        customerPortalSettingsDTO.get().setCustomerIdText(customerPortalSettingsV2DTO.getCustomerIdText());
        customerPortalSettingsDTO.get().setHelloNameText(customerPortalSettingsV2DTO.getHelloNameText());
        customerPortalSettingsDTO.get().setGoBackButtonText(customerPortalSettingsV2DTO.getGoBackButtonText());
        customerPortalSettingsDTO.get().setChangeVariantLabelText(customerPortalSettingsV2DTO.getChangeVariantLabelText());
        customerPortalSettingsDTO.get().setProvinceCodeLabelText(customerPortalSettingsV2DTO.getProvinceCodeLabelText());
        customerPortalSettingsDTO.get().setCountryCodeLabelText(customerPortalSettingsV2DTO.getCountryCodeLabelText());
        customerPortalSettingsDTO.get().setPleaseWaitLoaderText(customerPortalSettingsV2DTO.getPleaseWaitLoaderText());
        customerPortalSettingsDTO.get().setCancelSubscriptionMinimumBillingIterationsMessage(customerPortalSettingsV2DTO.getCancelSubscriptionMinimumBillingIterationsMessage());

        customerPortalSettingsDTO.get().setTopHtml(customerPortalSettingsV2DTO.getTopHtml());
        customerPortalSettingsDTO.get().setBottomHtml(customerPortalSettingsV2DTO.getBottomHtml());

        customerPortalSettingsDTO.get().setDiscountCodeText(customerPortalSettingsV2DTO.getDiscountCodeText());
        customerPortalSettingsDTO.get().setDiscountCodeApplyButtonText(customerPortalSettingsV2DTO.getDiscountCodeApplyButtonText());
        customerPortalSettingsDTO.get().setApplySellingPlanBasedDiscount(customerPortalSettingsV2DTO.getApplySellingPlanBasedDiscount());
        customerPortalSettingsDTO.get().setApplySubscriptionDiscountForOtp(customerPortalSettingsV2DTO.getApplySubscriptionDiscountForOtp());
        customerPortalSettingsDTO.get().setApplySubscriptionDiscount(customerPortalSettingsV2DTO.isApplySubscriptionDiscount());
        customerPortalSettingsDTO.get().setRemoveDiscountCodeAutomatically(customerPortalSettingsV2DTO.getRemoveDiscountCodeAutomatically());
        customerPortalSettingsDTO.get().setRemoveDiscountCodeLabel(customerPortalSettingsV2DTO.getRemoveDiscountCodeLabel());
        customerPortalSettingsDTO.get().setEnableSplitContract(customerPortalSettingsV2DTO.getEnableSplitContract());
        customerPortalSettingsDTO.get().setSplitContractMessage(customerPortalSettingsV2DTO.getSplitContractMessage());
        customerPortalSettingsDTO.get().setSplitContractText(customerPortalSettingsV2DTO.getSplitContractText());
        customerPortalSettingsDTO.get().setSubscriptionDiscountTypeUnit(customerPortalSettingsV2DTO.getSubscriptionDiscountTypeUnit());
        customerPortalSettingsDTO.get().setSubscriptionDiscount(customerPortalSettingsV2DTO.getSubscriptionDiscount());
        customerPortalSettingsDTO.get().setUpSellMessage(customerPortalSettingsV2DTO.getUpSellMessage());

        customerPortalSettingsDTO.get().setRequireFieldMessage(customerPortalSettingsV2DTO.getRequireFieldMessage());
        customerPortalSettingsDTO.get().setValidNumberRequiredMessage(customerPortalSettingsV2DTO.getValidNumberRequiredMessage());
        customerPortalSettingsDTO.get().setVariantLbl(customerPortalSettingsV2DTO.getVariantLbl());
        customerPortalSettingsDTO.get().setPriceLbl(customerPortalSettingsV2DTO.getPriceLbl());
        customerPortalSettingsDTO.get().setOneTimePurchaseOnlyText(customerPortalSettingsV2DTO.getOneTimePurchaseOnlyText());
        customerPortalSettingsDTO.get().setRescheduleText(customerPortalSettingsV2DTO.getRescheduleText());
        customerPortalSettingsDTO.get().setPopUpSuccessMessage(customerPortalSettingsV2DTO.getPopUpSuccessMessage());
        customerPortalSettingsDTO.get().setPopUpErrorMessage(customerPortalSettingsV2DTO.getPopUpErrorMessage());

        customerPortalSettingsDTO.get().setOrderNowText(customerPortalSettingsV2DTO.getOrderNowText());
        customerPortalSettingsDTO.get().setUpcomingOrderPlaceNowAlertText(customerPortalSettingsV2DTO.getUpcomingOrderPlaceNowAlertText());
        customerPortalSettingsDTO.get().setUpcomingOrderSkipAlertText(customerPortalSettingsV2DTO.getUpcomingOrderSkipAlertText());
        customerPortalSettingsDTO.get().setDeliveryFrequencyText(customerPortalSettingsV2DTO.getDeliveryFrequencyText());
        customerPortalSettingsDTO.get().setEditDeliveryInternalText(customerPortalSettingsV2DTO.getEditDeliveryInternalText());

        customerPortalSettingsDTO.get().setMaxCycleText(customerPortalSettingsV2DTO.getMaxCycleText());
        customerPortalSettingsDTO.get().setMinCycleText(customerPortalSettingsV2DTO.getMinCycleText());
        customerPortalSettingsDTO.get().setSelectProductToAdd(customerPortalSettingsV2DTO.getSelectProductToAdd());
        customerPortalSettingsDTO.get().setSearchProductBtnText(customerPortalSettingsV2DTO.getSearchProductBtnText());
        customerPortalSettingsDTO.get().setAreyousureCommonMessageText(customerPortalSettingsV2DTO.getAreyousureCommonMessageText());
        customerPortalSettingsDTO.get().setEditCommonText(customerPortalSettingsV2DTO.getEditCommonText());
        customerPortalSettingsDTO.get().setViewMoreText(customerPortalSettingsV2DTO.getViewMoreText());
        customerPortalSettingsDTO.get().setVariantLblText(customerPortalSettingsV2DTO.getVariantLblText());
        customerPortalSettingsDTO.get().setTotalLblText(customerPortalSettingsV2DTO.getTotalLblText());
        customerPortalSettingsDTO.get().setDeleteProductTitleText(customerPortalSettingsV2DTO.getDeleteProductTitleText());
        customerPortalSettingsDTO.get().setGreetingText(customerPortalSettingsV2DTO.getGreetingText());
        customerPortalSettingsDTO.get().setProductLblText(customerPortalSettingsV2DTO.getProductLblText());
        customerPortalSettingsDTO.get().setHasBeenRemovedText(customerPortalSettingsV2DTO.getHasBeenRemovedText());
        customerPortalSettingsDTO.get().setOrderTotalText(customerPortalSettingsV2DTO.getOrderTotalText());
        customerPortalSettingsDTO.get().setAddDiscountCodeText(customerPortalSettingsV2DTO.getAddDiscountCodeText());
        customerPortalSettingsDTO.get().setAddDiscountCodeAlertText(customerPortalSettingsV2DTO.getAddDiscountCodeAlertText());
        customerPortalSettingsDTO.get().setRemoveDiscountCodeAlertText(customerPortalSettingsV2DTO.getRemoveDiscountCodeAlertText());
        customerPortalSettingsDTO.get().setPleaseWaitLoaderText(customerPortalSettingsV2DTO.getPleaseWaitLoaderText());
        customerPortalSettingsDTO.get().setShopPayLblText(customerPortalSettingsV2DTO.getShopPayLblText());
        customerPortalSettingsDTO.get().setPaypalLblText(customerPortalSettingsV2DTO.getPaypalLblText());
        customerPortalSettingsDTO.get().setUnknownPaymentReachoutUsText(customerPortalSettingsV2DTO.getUnknownPaymentReachoutUsText());
        customerPortalSettingsDTO.get().setAddToOrderLabelText(customerPortalSettingsV2DTO.getAddToOrderLabelText());
        customerPortalSettingsDTO.get().setUpcomingTabTitle(customerPortalSettingsV2DTO.getUpcomingTabTitle());
        customerPortalSettingsDTO.get().setScheduledTabTitle(customerPortalSettingsV2DTO.getScheduledTabTitle());
        customerPortalSettingsDTO.get().setHistoryTabTitle(customerPortalSettingsV2DTO.getHistoryTabTitle());
        customerPortalSettingsDTO.get().setNoOrderNotAvailableMessage(customerPortalSettingsV2DTO.getNoOrderNotAvailableMessage());
        customerPortalSettingsDTO.get().setContinueText(customerPortalSettingsV2DTO.getContinueText());
        customerPortalSettingsDTO.get().setConfirmSwapText(customerPortalSettingsV2DTO.getConfirmSwapText());
        customerPortalSettingsDTO.get().setConfirmAddProduct(customerPortalSettingsV2DTO.getConfirmAddProduct());

        customerPortalSettingsDTO.get().setSubscriptionContractFreezeMessage(customerPortalSettingsV2DTO.getSubscriptionContractFreezeMessage());
        customerPortalSettingsDTO.get().setPreventCancellationBeforeDaysMessage(customerPortalSettingsV2DTO.getPreventCancellationBeforeDaysMessage());
        customerPortalSettingsDTO.get().setDiscountRecurringCycleLimitOnCancellation(customerPortalSettingsV2DTO.getDiscountRecurringCycleLimitOnCancellation());
        customerPortalSettingsDTO.get().setDiscountAccordionTitle(customerPortalSettingsV2DTO.getDiscountAccordionTitle());
        customerPortalSettingsDTO.get().setDiscountMessageOnCancellation(customerPortalSettingsV2DTO.getDiscountMessageOnCancellation());
        customerPortalSettingsDTO.get().setDiscountPercentageOnCancellation(customerPortalSettingsV2DTO.getDiscountPercentageOnCancellation());
        customerPortalSettingsDTO.get().setOfferDiscountOnCancellation(customerPortalSettingsV2DTO.getOfferDiscountOnCancellation());
        customerPortalSettingsDTO.get().setEnableSkipFulFillment(customerPortalSettingsV2DTO.getEnableSkipFulFillment());
        customerPortalSettingsDTO.get().setMagicLinkEmailFlag(customerPortalSettingsV2DTO.getMagicLinkEmailFlag());

        customerPortalSettingsDTO.get().setFrequencyChangeWarningTitle(customerPortalSettingsV2DTO.getFrequencyChangeWarningTitle());
        customerPortalSettingsDTO.get().setFrequencyChangeWarningDescription(customerPortalSettingsV2DTO.getFrequencyChangeWarningDescription());
        customerPortalSettingsDTO.get().setVariantIdsToFreezeEditRemove(customerPortalSettingsV2DTO.getVariantIdsToFreezeEditRemove());
        customerPortalSettingsDTO.get().setPreventCancellationBeforeDays(customerPortalSettingsV2DTO.getPreventCancellationBeforeDays());
        customerPortalSettingsDTO.get().setDisAllowVariantIdsForOneTimeProductAdd(customerPortalSettingsV2DTO.getDisAllowVariantIdsForOneTimeProductAdd());
        customerPortalSettingsDTO.get().setDisAllowVariantIdsForSubscriptionProductAdd(customerPortalSettingsV2DTO.getDisAllowVariantIdsForSubscriptionProductAdd());
        customerPortalSettingsDTO.get().setHideAddSubscriptionProductSection(customerPortalSettingsV2DTO.getHideAddSubscriptionProductSection());
        customerPortalSettingsDTO.get().setAllowOnlyOneTimeProductOnAddProductFlag(customerPortalSettingsV2DTO.getAllowOnlyOneTimeProductOnAddProductFlag());
        customerPortalSettingsDTO.get().setDiscountCouponRemoveText(customerPortalSettingsV2DTO.getDiscountCouponRemoveText());
        customerPortalSettingsDTO.get().setPleaseSelectText(customerPortalSettingsV2DTO.getPleaseSelectText());
        customerPortalSettingsDTO.get().setShippingAddressNotAvailableText(customerPortalSettingsV2DTO.getShippingAddressNotAvailableText());
        customerPortalSettingsDTO.get().setDiscountCouponNotAppliedText(customerPortalSettingsV2DTO.getDiscountCouponNotAppliedText());
        customerPortalSettingsDTO.get().setSellingPlanNameText(customerPortalSettingsV2DTO.getSellingPlanNameText());
        customerPortalSettingsDTO.get().setShopPayPaymentUpdateText(customerPortalSettingsV2DTO.getShopPayPaymentUpdateText());

        customerPortalSettingsDTO.get().setSelectProductLabelText(customerPortalSettingsV2DTO.getSelectProductLabelText());
        customerPortalSettingsDTO.get().setPurchaseOptionLabelText(customerPortalSettingsV2DTO.getPurchaseOptionLabelText());
        customerPortalSettingsDTO.get().setFinishLabelText(customerPortalSettingsV2DTO.getFinishLabelText());
        customerPortalSettingsDTO.get().setNextBtnText(customerPortalSettingsV2DTO.getNextBtnText());
        customerPortalSettingsDTO.get().setPreviousBtnText(customerPortalSettingsV2DTO.getPreviousBtnText());
        customerPortalSettingsDTO.get().setCloseBtnText(customerPortalSettingsV2DTO.getCloseBtnText());
        customerPortalSettingsDTO.get().setDeleteConfirmationMsgText(customerPortalSettingsV2DTO.getDeleteConfirmationMsgText());
        customerPortalSettingsDTO.get().setDeleteMsgText(customerPortalSettingsV2DTO.getDeleteMsgText());
        customerPortalSettingsDTO.get().setYesBtnText(customerPortalSettingsV2DTO.getYesBtnText());
        customerPortalSettingsDTO.get().setNoBtnText(customerPortalSettingsV2DTO.getNoBtnText());
        customerPortalSettingsDTO.get().setOneTimePurchaseNoteText(customerPortalSettingsV2DTO.getOneTimePurchaseNoteText());
        customerPortalSettingsDTO.get().setClickHereText(customerPortalSettingsV2DTO.getClickHereText());
        customerPortalSettingsDTO.get().setProductAddMessageText(customerPortalSettingsV2DTO.getProductAddMessageText());
        customerPortalSettingsDTO.get().setChoosePurchaseOptionLabelText(customerPortalSettingsV2DTO.getChoosePurchaseOptionLabelText());
        customerPortalSettingsDTO.get().setOneTimePurchaseMessageText(customerPortalSettingsV2DTO.getOneTimePurchaseMessageText());
        customerPortalSettingsDTO.get().setContractUpdateMessageText(customerPortalSettingsV2DTO.getContractUpdateMessageText());
        customerPortalSettingsDTO.get().setOneTimePurchaseDisplayMessageText(customerPortalSettingsV2DTO.getOneTimePurchaseDisplayMessageText());
        customerPortalSettingsDTO.get().setAddProductFinishedMessageText(customerPortalSettingsV2DTO.getAddProductFinishedMessageText());
        customerPortalSettingsDTO.get().setContractErrorMessageText(customerPortalSettingsV2DTO.getContractErrorMessageText());
        customerPortalSettingsDTO.get().setAddToSubscriptionTitleCP(customerPortalSettingsV2DTO.getAddToSubscriptionTitleCP());
        customerPortalSettingsDTO.get().setOneTimePurchaseTitleCP(customerPortalSettingsV2DTO.getOneTimePurchaseTitleCP());
        customerPortalSettingsDTO.get().setSeeMoreProductBtnText(customerPortalSettingsV2DTO.getSeeMoreProductBtnText());
        customerPortalSettingsDTO.get().setViewAttributeLabelText(customerPortalSettingsV2DTO.getViewAttributeLabelText());
        customerPortalSettingsDTO.get().setAttributeNameLabelText(customerPortalSettingsV2DTO.getAttributeNameLabelText());
        customerPortalSettingsDTO.get().setSwapProductBtnText(customerPortalSettingsV2DTO.getSwapProductBtnText());
        customerPortalSettingsDTO.get().setSwapProductLabelText(customerPortalSettingsV2DTO.getSwapProductLabelText());
        customerPortalSettingsDTO.get().setSwapProductSearchBarText(customerPortalSettingsV2DTO.getSwapProductSearchBarText());
        customerPortalSettingsDTO.get().setEnableSwapProductFeature(customerPortalSettingsV2DTO.getEnableSwapProductFeature());
        customerPortalSettingsDTO.get().setEnableTabletForceView(customerPortalSettingsV2DTO.getEnableTabletForceView());
        customerPortalSettingsDTO.get().setAttributeValue(customerPortalSettingsV2DTO.getAttributeValue());
        customerPortalSettingsDTO.get().setAddNewButtonText(customerPortalSettingsV2DTO.getAddNewButtonText());
        customerPortalSettingsDTO.get().setAttributeHeadingText(customerPortalSettingsV2DTO.getAttributeHeadingText());
        customerPortalSettingsDTO.get().setEnableViewAttributes(customerPortalSettingsV2DTO.getEnableViewAttributes());
        customerPortalSettingsDTO.get().setEnableEditOrderNotes(customerPortalSettingsV2DTO.getEnableEditOrderNotes());
        customerPortalSettingsDTO.get().setShowSellingPlanFrequencies(customerPortalSettingsV2DTO.getShowSellingPlanFrequencies());
        customerPortalSettingsDTO.get().setTotalPricePerDeliveryText(customerPortalSettingsV2DTO.getTotalPricePerDeliveryText());
        customerPortalSettingsDTO.get().setFulfilledText(customerPortalSettingsV2DTO.getFulfilledText());
        customerPortalSettingsDTO.get().setDateFormat(customerPortalSettingsV2DTO.getDateFormat());
        customerPortalSettingsDTO.get().setDiscountCouponAppliedText(customerPortalSettingsV2DTO.getDiscountCouponAppliedText());
        customerPortalSettingsDTO.get().setSubscriptionPausedMessageText(customerPortalSettingsV2DTO.getSubscriptionPausedMessageText());
        customerPortalSettingsDTO.get().setSubscriptionActivatedMessageText(customerPortalSettingsV2DTO.getSubscriptionActivatedMessageText());
        customerPortalSettingsDTO.get().setUnableToUpdateSubscriptionStatusMessageText(customerPortalSettingsV2DTO.getUnableToUpdateSubscriptionStatusMessageText());
        customerPortalSettingsDTO.get().setSelectCancellationReasonLabelText(customerPortalSettingsV2DTO.getSelectCancellationReasonLabelText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupSuccessTitleText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessTitleText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupSuccessDescriptionText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessDescriptionText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupSuccessClosebtnText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupSuccessClosebtnText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupFailureTitleText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureTitleText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupFailureDescriptionText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureDescriptionText());
        customerPortalSettingsDTO.get().setUpcomingOrderChangePopupFailureClosebtnText(customerPortalSettingsV2DTO.getUpcomingOrderChangePopupFailureClosebtnText());

        customerPortalSettingsDTO.get().setRedeemRewardsTextV2(customerPortalSettingsV2DTO.getRedeemRewardsTextV2());
        customerPortalSettingsDTO.get().setRewardsTextV2(customerPortalSettingsV2DTO.getRewardsTextV2());
        customerPortalSettingsDTO.get().setYourRewardsTextV2(customerPortalSettingsV2DTO.getYourRewardsTextV2());
        customerPortalSettingsDTO.get().setYourAvailableRewardsPointsTextV2(customerPortalSettingsV2DTO.getYourAvailableRewardsPointsTextV2());

        customerPortalSettingsDTO.get().setCancellationDateTitleText(customerPortalSettingsV2DTO.getCancellationDateTitleText());
        customerPortalSettingsDTO.get().setSelectedCancellationReasonTitleText(customerPortalSettingsV2DTO.getSelectedCancellationReasonTitleText());
        customerPortalSettingsDTO.get().setCancellationNoteTitleText(customerPortalSettingsV2DTO.getCancellationNoteTitleText());

        customerPortalSettingsDTO.get().setSelectSplitMethodLabelText(customerPortalSettingsV2DTO.getSelectSplitMethodLabelText());
        customerPortalSettingsDTO.get().setSplitWithOrderPlacedSelectOptionText(customerPortalSettingsV2DTO.getSplitWithOrderPlacedSelectOptionText());
        customerPortalSettingsDTO.get().setSplitWithoutOrderPlacedSelectOptionText(customerPortalSettingsV2DTO.getSplitWithoutOrderPlacedSelectOptionText());
        customerPortalSettingsDTO.get().setContractCancelledBadgeText(customerPortalSettingsV2DTO.getContractCancelledBadgeText());

        customerPortalSettingsDTO.get().setChooseAnotherPaymentMethodTitleText(customerPortalSettingsV2DTO.getChooseAnotherPaymentMethodTitleText());
        customerPortalSettingsDTO.get().setSelectPaymentMethodTitleText(customerPortalSettingsV2DTO.getSelectPaymentMethodTitleText());
        customerPortalSettingsDTO.get().setChangePaymentMessage(customerPortalSettingsV2DTO.getChangePaymentMessage());
        customerPortalSettingsDTO.get().setUpdatePaymentMethodTitleText(customerPortalSettingsV2DTO.getUpdatePaymentMethodTitleText());
        customerPortalSettingsDTO.get().setProductFilterConfig(customerPortalSettingsV2DTO.getProductFilterConfig());
        customerPortalSettingsDTO.get().setReschedulingPolicies(customerPortalSettingsV2DTO.getReschedulingPolicies());

        customerPortalSettingsDTO.get().setUpcomingTabHeaderHTML(customerPortalSettingsV2DTO.getUpcomingTabHeaderHTML());
        customerPortalSettingsDTO.get().setSchedulesTabHeaderHTML(customerPortalSettingsV2DTO.getSchedulesTabHeaderHTML());
        customerPortalSettingsDTO.get().setHistoryTabHeaderHTML(customerPortalSettingsV2DTO.getHistoryTabHeaderHTML());

        customerPortalSettingsDTO.get().setAllowedProductIdsForOneTimeProductAdd(customerPortalSettingsV2DTO.getAllowedProductIdsForOneTimeProductAdd());

        Map<String, LabelValueInfo> shopLabels = shopLabelService.getShopLabels(shop);
        Map<String, String> customerPortalLabels = shopLabels.entrySet().stream().filter(e -> e.getValue().getGroups().contains("CUSTOMER_PORTAL")).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));

        for (Map.Entry<String, String> entry : customerPortalLabels.entrySet()) {
            String[] split = StringUtils.split(entry.getKey(), ".");
            customerPortalSettingsDTO.get().setAdditionalProperty(split[split.length - 1], entry.getValue());
        }

        customerPortalSettingsDTO.get().setFreezeUpdateSubscriptionMessage(customerPortalSettingsV2DTO.getFreezeUpdateSubscriptionMessage() != null ? customerPortalSettingsV2DTO.getFreezeUpdateSubscriptionMessage() : Optional.ofNullable(customerPortalSettingsDTO.get().getAdditionalProperties().get("freezeUpdateSubscriptionMessageV2")).orElse("").toString());
        return customerPortalSettingsDTO;
    }

    @Override
    public void deleteByShop(String shop) {
        customerPortalSettingsRepository.deleteByShop(shop);
    }

    @Override
    public List<CustomerPortalSettingsDTO> findAllByShop(String shop) {
        return customerPortalSettingsRepository.findAllByShop(shop).stream()
            .map(customerPortalSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
