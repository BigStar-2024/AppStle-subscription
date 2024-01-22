package com.et.service;

import com.et.api.shopify.shop.Shop;
import com.et.constant.EmailTemplateConstants;
import com.et.domain.EmailTemplateSetting;

import com.et.domain.enumeration.EmailSettingType;
import com.et.pojo.EmailTemplateSettingPropertyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link EmailTemplateSetting}.
 */
public interface EmailTemplateSettingService {

    /**
     * Save a emailTemplateSetting.
     *
     * @param emailTemplateSetting the entity to save.
     * @return the persisted entity.
     */
    EmailTemplateSetting save(EmailTemplateSetting emailTemplateSetting);

    /**
     * Get all the emailTemplateSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmailTemplateSetting> findAll(Pageable pageable);


    /**
     * Get the "id" emailTemplateSetting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailTemplateSetting> findOne(Long id);

    /**
     * Delete the "id" emailTemplateSetting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByShop(String shop);

    List<EmailTemplateSetting> findByShop(String shop);

    void deleteNotSupportedTemplates(String shop);

    String getSubscriptionCreatedEmailString(String shop, EmailTemplateSetting emailTemplateSetting);

    Boolean sendTestMail(String shop, String emailId, Long emailTemplateSettingId) throws Exception;

    Boolean resetEmailTemplateSettings(EmailSettingType emailSettingType, Long emailTemplateSettingId, String shop, Shop shopDetails) throws Exception;

    EmailTemplateSetting buildSubscriptionCreatedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildUpcomingOrderEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildExpiringCreditCardEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildTransactionFailedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildTransactionSuccessEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionManagementLinkEmailSetting(String shop, Shop shopDetails) throws IOException;

    void setCommonDefaultValueForEmailTemplateSetting(EmailTemplateSetting emailTemplateSetting, String shop);

    List<EmailTemplateSetting> updateBulkEmailTemplatesSettingByProperty(EmailTemplateSettingPropertyInfo settingPropertyInfo);

    EmailTemplateSetting buildShippingAddressUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildOrderSkippedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildNextOrderDateUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildOrderFrequencyUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionResumedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSecurityChallengeEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionCanceledEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionPausedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionProductAddedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionProductRemovedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildSubscriptionProductReplacedEmailSetting(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildEmailSettingForAllSubscribers(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildEmailSettingForActiveSubscribersOnly(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildEmailSettingForPausedSubscribersOnly(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildEmailSettingForCancelledSubscribersOnly(String shop, Shop shopDetails) throws IOException;

    EmailTemplateSetting buildEmailSettingForOutOfStock(String shop, Shop shopDetails) throws IOException;

    @Async
    Boolean sendBulkMails(Long emailTemplateSettingId, String shop) throws Exception;
}
