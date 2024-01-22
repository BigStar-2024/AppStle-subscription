package com.et.service;


import com.et.api.klaviyo.Klaviyo;
import com.et.domain.EmailTemplateSetting;
import com.et.domain.ShopInfo;
import com.et.domain.enumeration.EmailSettingType;
import com.et.liquid.EmailModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KlaviyoService {

    private final Logger logger = LoggerFactory.getLogger(KlaviyoService.class);

    public void sendMail(EmailTemplateSetting emailTemplateSetting, ShopInfo shopInfo, EmailModel model, String customerEmail) throws JsonProcessingException {
        logger.info("sending Klaviyo email for customerEmail=" + customerEmail + " shop=" + shopInfo.getShop() + " emailSettingType=" + emailTemplateSetting.getEmailSettingType());
        Klaviyo klaviyoAPI = new Klaviyo(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoPublicApiKey());

        if (BooleanUtils.isTrue(emailTemplateSetting.isSendBCCEmailFlag()) && !org.apache.commons.lang3.StringUtils.isBlank(emailTemplateSetting.getBccEmail())) {
            customerEmail = emailTemplateSetting.getBccEmail();
        } else if (BooleanUtils.isTrue(emailTemplateSetting.isSendBCCEmailFlag())) {
            logger.info("isSendBCCEmailFlag flag was enabled but no bcc email was found.");
            return;
        }

        if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.EXPIRING_CREDIT_CARD) {
            klaviyoAPI.triggerExpiringCreditCardEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CREATED) {
            klaviyoAPI.triggerCreateSubscriptionEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.TRANSACTION_FAILED) {
            klaviyoAPI.triggerTransactionFailedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.UPCOMING_ORDER) {
            klaviyoAPI.triggerUpcomingOrderEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SECURITY_CHALLENGE) {
            klaviyoAPI.triggerSecurityChallengeEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SHIPPING_ADDRESS_UPDATED) {
            klaviyoAPI.triggerShippingAddressUpdatedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_FREQUENCY_UPDATED) {
            klaviyoAPI.triggerOrderFrequencyUpdatedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.NEXT_ORDER_DATE_UPDATED) {
            klaviyoAPI.triggerNextOrderUpdatedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PAUSED) {
            klaviyoAPI.triggerSubscriptionPausedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CANCELLED) {
            klaviyoAPI.triggerSubscriptionCanceledEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_RESUMED) {
            klaviyoAPI.triggerSubscriptionResumeEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED) {
            klaviyoAPI.triggerSubscriptionProductAddedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED) {
            klaviyoAPI.triggerSubscriptionProductRemovedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED) {
            klaviyoAPI.triggerSubscriptionProductReplacedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK) {
            klaviyoAPI.triggerSubscriptionManagementLinkEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_SKIPPED) {
            klaviyoAPI.triggerOrderSkippedEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.OUT_OF_STOCK) {
            klaviyoAPI.triggerOutOfStockEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType()==EmailSettingType.BULK_ALL_SUBSCRIBERS) {
            klaviyoAPI.triggerBulkAllSubscriberEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType()==EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY) {
            klaviyoAPI.triggerBulkActiveSubscriberOnlyEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType()==EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY) {
            klaviyoAPI.triggerBulkCancelledSubscriberOnlyEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType()==EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY) {
            klaviyoAPI.triggerBulkPausedSubscriberOnlyEvent(customerEmail, model);
        } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.TRANSACTION_SUCCESS) {
             klaviyoAPI.triggerTransactionSuccessEvent(customerEmail, model);
        }
    }
}
