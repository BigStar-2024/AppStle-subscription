package com.et.service.impl;

import com.amazonaws.util.IOUtils;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.klaviyo.Klaviyo;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.product.GetProductsPaginatedResponse;
import com.et.api.shopify.product.Product;
import com.et.api.shopify.shop.Shop;
import com.et.api.utils.CurrencyFormattingUtils;
import com.et.constant.Constants;
import com.et.constant.EmailTemplateConstants;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.liquid.*;
import com.et.pojo.EmailTemplateSettingPropertyInfo;
import com.et.pojo.OrderItem;
import com.et.repository.*;
import com.et.security.SecurityUtils;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.EmailTemplateSettingService;
import com.et.service.MailChimpService;
import com.et.service.MailgunService;
import com.et.service.KlaviyoService;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.utils.CommonEmailUtils;
import com.et.utils.CommonUtils;
import com.et.utils.ShopInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link EmailTemplateSetting}.
 */
@Service
@Transactional
public class EmailTemplateSettingServiceImpl implements EmailTemplateSettingService {

    public static final String PAY_AS_YOU_GO = "PAY_AS_YOU_GO";
    public static final String PREPAID = "PREPAID";
    private final Logger log = LoggerFactory.getLogger(EmailTemplateSettingServiceImpl.class);

    @Autowired
    private final EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private LiquidUtils liquidUtils;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private MailChimpService mailChimpService;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private KlaviyoService klaviyoService;

    @Value("classpath:base-mail-template.html")
    private Resource baseMailTemplate;

    public EmailTemplateSettingServiceImpl(EmailTemplateSettingRepository emailTemplateSettingRepository) {
        this.emailTemplateSettingRepository = emailTemplateSettingRepository;
    }

    @Override
    public EmailTemplateSetting save(EmailTemplateSetting emailTemplateSetting) {
        log.debug("Request to save EmailTemplateSetting : {}", emailTemplateSetting);
        return emailTemplateSettingRepository.save(emailTemplateSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailTemplateSetting> findAll(Pageable pageable) {
        log.debug("Request to get all EmailTemplateSettings");
        return emailTemplateSettingRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmailTemplateSetting> findOne(Long id) {
        log.debug("Request to get EmailTemplateSetting : {}", id);
        return emailTemplateSettingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailTemplateSetting : {}", id);
        emailTemplateSettingRepository.deleteById(id);
    }

    @Override
    public void deleteByShop(String shop) {
        emailTemplateSettingRepository.deleteByShop(shop);
    }

    @Override
    public List<EmailTemplateSetting> findByShop(String shop) {
        return emailTemplateSettingRepository.findByShop(shop);
    }

    @Override
    public void deleteNotSupportedTemplates(String shop) {
        try {
            List<EmailSettingType> supportedTypes = Arrays.asList(EmailSettingType.values());
            emailTemplateSettingRepository.deleteByShopAndEmailSettingTypeNotIn(shop, supportedTypes);
        } catch (Exception e) {
            log.info("Error while deleting not supported email templates. ex={}", ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public String getSubscriptionCreatedEmailString(String shop, EmailTemplateSetting emailTemplateSetting) {
        try {
            SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findFirstByShopAndStatus(shop, "active");

            if(subscriptionContractDetails == null) {
                subscriptionContractDetails = subscriptionContractDetailsRepository.findFirstByShop(shop);
            }

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
            ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

            return getSubscriptionCreatedEmailString(emailTemplateSetting, shopifyGraphqlClient, api, subscriptionContractDetails, shop);

        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                if (((HttpClientErrorException) e).getStatusCode().value() == 429) {
                    return null;
                }
            }
            log.error("Error while getting email preview. shop={}, emailTemplateSetting={}, ex={}", shop, emailTemplateSetting, ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public Boolean sendTestMail(String shop, String emailAddress, Long emailTemplateSettingId) throws Exception {

        EmailTemplateSetting emailTemplateSetting = emailTemplateSettingRepository.findById(emailTemplateSettingId).get();

        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findFirstByShopAndStatus(shop, "active");

        if(subscriptionContractDetails == null) {
            subscriptionContractDetails = subscriptionContractDetailsRepository.findFirstByShop(shop);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        EmailTemplateBuilder emailTemplateBuilder = new EmailTemplateBuilder(
                emailTemplateSetting,
                subscriptionContractDetails,
                shop,
                shopifyGraphqlClient,
                api).invoke();
        Shop shopDetails = emailTemplateBuilder.getShopDetails();
        String subject = emailTemplateBuilder.getSubject();
        String html = emailTemplateBuilder.getHtml();

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        if (!StringUtils.isBlank(shopInfo.getKlaviyoApiKey()) && !StringUtils.isBlank(shopInfo.getKlaviyoPublicApiKey())) {
            Klaviyo klaviyoAPI = new Klaviyo(shopInfo.getKlaviyoApiKey(), shopInfo.getKlaviyoPublicApiKey());
            if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.EXPIRING_CREDIT_CARD) {
                klaviyoAPI.triggerExpiringCreditCardEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CREATED) {
                klaviyoAPI.triggerCreateSubscriptionEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.TRANSACTION_FAILED) {
                klaviyoAPI.triggerTransactionFailedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.UPCOMING_ORDER) {
                klaviyoAPI.triggerUpcomingOrderEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SHIPPING_ADDRESS_UPDATED) {
                klaviyoAPI.triggerShippingAddressUpdatedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_FREQUENCY_UPDATED) {
                klaviyoAPI.triggerOrderFrequencyUpdatedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.NEXT_ORDER_DATE_UPDATED) {
                klaviyoAPI.triggerNextOrderUpdatedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PAUSED) {
                klaviyoAPI.triggerSubscriptionPausedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_CANCELLED) {
                klaviyoAPI.triggerSubscriptionCanceledEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_RESUMED) {
                klaviyoAPI.triggerSubscriptionResumeEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED) {
                klaviyoAPI.triggerSubscriptionProductAddedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED) {
                klaviyoAPI.triggerSubscriptionProductRemovedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED) {
                klaviyoAPI.triggerSubscriptionProductReplacedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SECURITY_CHALLENGE) {
                klaviyoAPI.triggerSecurityChallengeEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK) {
                klaviyoAPI.triggerSubscriptionManagementLinkEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.ORDER_SKIPPED) {
                klaviyoAPI.triggerOrderSkippedEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.OUT_OF_STOCK) {
                klaviyoAPI.triggerOutOfStockEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.BULK_ALL_SUBSCRIBERS) {
                klaviyoAPI.triggerBulkAllSubscriberEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY) {
                klaviyoAPI.triggerBulkActiveSubscriberOnlyEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY) {
                klaviyoAPI.triggerBulkCancelledSubscriberOnlyEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY) {
                klaviyoAPI.triggerBulkPausedSubscriberOnlyEvent(emailAddress, emailTemplateBuilder.getModel());
            } else if (emailTemplateSetting.getEmailSettingType() == EmailSettingType.TRANSACTION_SUCCESS) {
                klaviyoAPI.triggerTransactionSuccessEvent(emailAddress, emailTemplateBuilder.getModel());
            }
        } else if (StringUtils.isNotBlank(shopInfo.getMailchimpApiKey())){
            mailChimpService.sendMail(emailAddress, subject, html, emailTemplateSetting, shop, shopInfo.getMailchimpApiKey());
        } else {
            mailgunService.sendEmail(emailAddress, subject, html, emailTemplateSetting, shop, shopDetails);
        }
        return true;
    }

    @Override
    public Boolean resetEmailTemplateSettings(EmailSettingType emailSettingType, Long emailTemplateSettingId, String shop, Shop shopDetails) throws Exception {

        if (emailSettingType == EmailSettingType.UPCOMING_ORDER) {
            EmailTemplateSetting emailTemplateSetting = buildUpcomingOrderEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.EXPIRING_CREDIT_CARD) {
            EmailTemplateSetting emailTemplateSetting = buildExpiringCreditCardEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_CREATED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionCreatedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.TRANSACTION_FAILED) {
            EmailTemplateSetting emailTemplateSetting = buildTransactionFailedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionManagementLinkEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.ORDER_SKIPPED) {
            EmailTemplateSetting emailTemplateSetting = buildOrderSkippedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.NEXT_ORDER_DATE_UPDATED) {
            EmailTemplateSetting emailTemplateSetting = buildNextOrderDateUpdatedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.ORDER_FREQUENCY_UPDATED) {
            EmailTemplateSetting emailTemplateSetting = buildOrderFrequencyUpdatedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_RESUMED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionResumedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SECURITY_CHALLENGE) {
            EmailTemplateSetting emailTemplateSetting = buildSecurityChallengeEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SHIPPING_ADDRESS_UPDATED) {
            EmailTemplateSetting emailTemplateSetting = buildShippingAddressUpdatedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_CANCELLED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionCanceledEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_PAUSED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionPausedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionProductAddedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionProductRemovedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED) {
            EmailTemplateSetting emailTemplateSetting = buildSubscriptionProductReplacedEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.OUT_OF_STOCK) {
            EmailTemplateSetting emailTemplateSetting = buildEmailSettingForOutOfStock(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.BULK_ALL_SUBSCRIBERS) {
            EmailTemplateSetting emailTemplateSetting = buildEmailSettingForAllSubscribers(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY) {
            EmailTemplateSetting emailTemplateSetting = buildEmailSettingForActiveSubscribersOnly(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY) {
            EmailTemplateSetting emailTemplateSetting = buildEmailSettingForPausedSubscribersOnly(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY) {
            EmailTemplateSetting emailTemplateSetting = buildEmailSettingForCancelledSubscribersOnly(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        } else if (emailSettingType == EmailSettingType.TRANSACTION_SUCCESS) {
            EmailTemplateSetting emailTemplateSetting = buildTransactionSuccessEmailSetting(shop, shopDetails);
            emailTemplateSetting.setId(emailTemplateSettingId);
            emailTemplateSettingRepository.save(emailTemplateSetting);
            return true;
        }

        return false;
    }


    public EmailTemplateSetting buildSubscriptionManagementLinkEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_MANAGEMENT_LINK);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    public void setCommonDefaultValueForEmailTemplateSetting(EmailTemplateSetting emailTemplateSetting, String shop) {
        emailTemplateSetting.setLogo(EmailTemplateConstants.LOGO);
        emailTemplateSetting.setShippingAddressText(EmailTemplateConstants.SHIPPING_ADDRESS_TEXT);
        emailTemplateSetting.setBillingAddressText(EmailTemplateConstants.BILLING_ADDRESS_TEXT);
        emailTemplateSetting.setNextOrderdateText(EmailTemplateConstants.NEXT_ORDER_DATE_TEXT);
        emailTemplateSetting.setPaymentMethodText(EmailTemplateConstants.PAYMENT_METHOD_TEXT);
        emailTemplateSetting.setManageSubscriptionButtonColor(EmailTemplateConstants.MANAGE_SUBSCRIPTION_BUTTON_COLOR);
        emailTemplateSetting.setManageSubscriptionButtonText(EmailTemplateConstants.MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        emailTemplateSetting.setManageSubscriptionButtonTextColor(EmailTemplateConstants.MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR);
        emailTemplateSetting.setEndingInText(EmailTemplateConstants.ENDING_IN_TEXT);
        emailTemplateSetting.setQuantityText(EmailTemplateConstants.QUANTITY_TEXT);
        emailTemplateSetting.setLogoHeight(EmailTemplateConstants.LOGO_HEIGHT);
        emailTemplateSetting.setLogoWidth(EmailTemplateConstants.LOGO_WIDTH);
        emailTemplateSetting.setLogoAlignment(EmailTemplateConstants.LOGO_ALIGNMENT);
        emailTemplateSetting.setThanksImageHeight(EmailTemplateConstants.THANKS_IMAGE_HEIGHT);
        emailTemplateSetting.setThanksImageWidth(EmailTemplateConstants.THANKS_IMAGE_WIDTH);
        emailTemplateSetting.setThanksImageAlignment(EmailTemplateConstants.THANKS_IMAGE_ALIGNMENT);
        emailTemplateSetting.setShippingAddress(EmailTemplateConstants.SHIPPING_ADDRESS_FORMAT_TEXT);
        emailTemplateSetting.setBillingAddress(EmailTemplateConstants.BILLING_ADDRESS_FORMAT_TEXT);
        emailTemplateSetting.setSellingPlanNameText(EmailTemplateConstants.SELLING_PLAN_NAME_TEXT);
        emailTemplateSetting.setVariantSkuText(EmailTemplateConstants.VARIANT_SKU_TEXT);

        emailTemplateSetting.setShop(shop);
        emailTemplateSetting.setSendEmailDisabled(false);
    }

    @Override
    public  List<EmailTemplateSetting> updateBulkEmailTemplatesSettingByProperty(EmailTemplateSettingPropertyInfo settingPropertyInfo) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<EmailTemplateSetting> emailTemplateSettingList = findByShop(shop);
        emailTemplateSettingList = emailTemplateSettingList.stream().map(emailTemplateSetting -> setEmailTemplatesSettingByProperty(emailTemplateSetting, settingPropertyInfo)).collect(Collectors.toList());
        return emailTemplateSettingRepository.saveAll(emailTemplateSettingList);
    }

    private EmailTemplateSetting setEmailTemplatesSettingByProperty(EmailTemplateSetting emailTemplateSetting, EmailTemplateSettingPropertyInfo settingPropertyInfo) {
        switch (settingPropertyInfo.getPropertyName()) {
            case "fromEmail":
                emailTemplateSetting.setFromEmail(settingPropertyInfo.getPropertyValue());
                break;
            case "logo":
                emailTemplateSetting.setLogo(settingPropertyInfo.getPropertyValue());
                break;
            case "logoHeight":
                emailTemplateSetting.setLogoHeight(settingPropertyInfo.getPropertyValue());
                break;
            case "logoWidth":
                emailTemplateSetting.setLogoWidth(settingPropertyInfo.getPropertyValue());
                break;
            case "headingTextColor":
                emailTemplateSetting.setHeadingTextColor(settingPropertyInfo.getPropertyValue());
                break;
            case "contentTextColor":
                emailTemplateSetting.setContentTextColor(settingPropertyInfo.getPropertyValue());
                break;
            case "manageSubscriptionButtonTextColor":
                emailTemplateSetting.setManageSubscriptionButtonTextColor(settingPropertyInfo.getPropertyValue());
                break;
            case "manageSubscriptionButtonColor":
                emailTemplateSetting.setManageSubscriptionButtonColor(settingPropertyInfo.getPropertyValue());
                break;
            case "footerTextColor":
                emailTemplateSetting.setFooterTextColor(settingPropertyInfo.getPropertyValue());
                break;
            case "logoAlignment":
                emailTemplateSetting.setLogoAlignment(settingPropertyInfo.getPropertyValue());
                break;
            default:
                break;
        }
        return emailTemplateSetting;
    }


    public EmailTemplateSetting buildTransactionFailedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.TRANSACTION_FAILED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.TRANSACTION_FAILED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.TRANSACTION_FAILED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.TRANSACTION_FAILED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.TRANSACTION_FAILED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.TRANSACTION_FAILED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.TRANSACTION_FAILED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.TRANSACTION_FAILED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.TRANSACTION_FAILED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.TRANSACTION_FAILED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.TRANSACTION_FAILED);
        emailTemplateSetting.setTextImageUrl(EmailTemplateConstants.OHNO_IMAGE_URL);
        emailTemplateSetting.setHeadingImageUrl(EmailTemplateConstants.TRANSACTION_FAILED_HEADING_IMAGE_URL);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    public EmailTemplateSetting buildTransactionSuccessEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.TRANSACTION_SUCCESS_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.TRANSACTION_SUCCESS_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.TRANSACTION_SUCCESS_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.TRANSACTION_SUCCESS_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.TRANSACTION_SUCCESS_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.TRANSACTION_SUCCESS_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.TRANSACTION_SUCCESS_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.TRANSACTION_SUCCESS_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.TRANSACTION_SUCCESS_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.TRANSACTION_SUCCESS_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.TRANSACTION_SUCCESS);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }



    public EmailTemplateSetting buildSubscriptionCreatedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_CREATED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_CREATED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_CREATED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_CREATED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_CREATED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_CREATED);
        emailTemplateSetting.setTextImageUrl(EmailTemplateConstants.THANKS_IMAGE_URL);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    public EmailTemplateSetting buildExpiringCreditCardEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.EXPIRING_CREDIT_CARD_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.EXPIRING_CREDIT_CARD_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.EXPIRING_CREDIT_CARD_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.EXPIRING_CREDIT_CARD_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.EXPIRING_CREDIT_CARD_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.EXPIRING_CREDIT_CARD);
        emailTemplateSetting.setTextImageUrl(EmailTemplateConstants.EXPIRING_IMAGE_URL);
        emailTemplateSetting.setHeadingImageUrl(EmailTemplateConstants.EXPIRING_CREDIT_CARD_HEADING_IMAGE_URL);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    public EmailTemplateSetting buildUpcomingOrderEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.UPCOMING_ORDER_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.UPCOMING_ORDER_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.UPCOMING_ORDER_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.UPCOMING_ORDER_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.UPCOMING_ORDER_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.UPCOMING_ORDER_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.UPCOMING_ORDER_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.UPCOMING_ORDER_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.UPCOMING_ORDER_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.UPCOMING_ORDER_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.UPCOMING_ORDER);
        emailTemplateSetting.setTextImageUrl(EmailTemplateConstants.UPCOMING_IMAGE_URL);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.UPCOMING_ORDER_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildShippingAddressUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SHIPPING_ADDRESS_UPDATED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SHIPPING_ADDRESS_UPDATED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildOrderSkippedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.ORDER_SKIPPED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.ORDER_SKIPPED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.ORDER_SKIPPED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.ORDER_SKIPPED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.ORDER_SKIPPED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.ORDER_SKIPPED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.ORDER_SKIPPED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.ORDER_SKIPPED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.ORDER_SKIPPED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.ORDER_SKIPPED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.ORDER_SKIPPED);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildNextOrderDateUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.NEXT_ORDER_DATE_UPDATED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.NEXT_ORDER_DATE_UPDATED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildOrderFrequencyUpdatedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.ORDER_FREQUENCY_UPDATED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.ORDER_FREQUENCY_UPDATED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionResumedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_RESUMED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_RESUMED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_RESUMED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_RESUMED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_RESUMED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_RESUMED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_RESUMED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSecurityChallengeEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SECURITY_CHALLENGE_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SECURITY_CHALLENGE_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SECURITY_CHALLENGE_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SECURITY_CHALLENGE_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SECURITY_CHALLENGE_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SECURITY_CHALLENGE_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SECURITY_CHALLENGE_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SECURITY_CHALLENGE_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SECURITY_CHALLENGE_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SECURITY_CHALLENGE_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SECURITY_CHALLENGE);
        emailTemplateSetting.setHeadingImageUrl(EmailTemplateConstants.SECURITY_CHALLENGE_HEADING_IMAGE_URL);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionCanceledEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_CANCELED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_CANCELED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_CANCELED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_CANCELED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_CANCELED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_CANCELLED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_CANCELED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionPausedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_PAUSED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_PAUSED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_PAUSED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_PAUSED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_PAUSED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_PAUSED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_PAUSED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionProductAddedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_ADDED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionProductRemovedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REMOVED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildSubscriptionProductReplacedEmailSetting(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.SUBSCRIPTION_PRODUCT_REPLACED);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.SUBSCRIPTION_PRODUCT_REPLACED_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }



    public String getSubscriptionCreatedEmailString(
            EmailTemplateSetting emailTemplateSetting,
            ShopifyGraphqlClient shopifyGraphqlClient,
            ShopifyAPI api,
            SubscriptionContractDetails subscriptionContractDetails,
            String shop) throws Exception {

        EmailTemplateBuilder emailTemplateBuilder = new EmailTemplateBuilder(
                emailTemplateSetting,
                subscriptionContractDetails,
                shop,
                shopifyGraphqlClient,
                api).invoke();
        String html = emailTemplateBuilder.getHtml();
        return html;
    }

    private ZonedDateTime getNextBillingDate(Optional<String> nextBillingDateOptional) {

        if (nextBillingDateOptional.isEmpty()) {
            return null;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
        return ZonedDateTime.parse(nextBillingDateOptional.get(), dateTimeFormatter);
    }

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    @Autowired
    private CommonEmailUtils commonEmailUtils;

    @Autowired
    private CommonUtils commonUtils;

    private class EmailTemplateBuilder {
        private EmailTemplateSetting emailTemplateSetting;
        private SubscriptionContractDetails subscriptionContractDetails;
        private String shop;
        private ShopifyGraphqlClient shopifyGraphqlClient;
        private ShopifyAPI api;
        private Shop shopDetails;
        private String subject;
        private String html;
        private EmailModel model;

        public EmailTemplateBuilder(EmailTemplateSetting emailTemplateSetting, SubscriptionContractDetails subscriptionContractDetails, String shop, ShopifyGraphqlClient shopifyGraphqlClient, ShopifyAPI api) {
            this.emailTemplateSetting = emailTemplateSetting;
            this.subscriptionContractDetails = subscriptionContractDetails;
            this.shop = shop;
            this.shopifyGraphqlClient = shopifyGraphqlClient;
            this.api = api;
        }

        public EmailModel getModel() {
            return model;
        }

        public void setModel(EmailModel model) {
            this.model = model;
        }

        public Shop getShopDetails() {
            return shopDetails;
        }

        public String getSubject() {
            return subject;
        }

        public String getHtml() {
            return html;
        }

        public EmailTemplateBuilder invoke() throws Exception {
            shopDetails = api.getShopInfo().getShop();
            EmailModel model = new EmailModel();

            SubjectModel subjectModel = new SubjectModel(shopDetails);
            subject = liquidUtils.getValue(subjectModel, emailTemplateSetting.getSubject());

            ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
            CustomerPortalSettingsDTO customerPortalSettings = customerPortalSettingsService.findByShop(shop).get();
            CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

            if (subscriptionContractDetails != null) {

                Optional<CustomerPayment> customerPaymentOptional = customerPaymentRepository.findTop1ByCustomerId(subscriptionContractDetails.getCustomerId());

                Customer customer = new Customer(subscriptionContractDetails.getCustomerName(), subscriptionContractDetails.getCustomerFirstName(), subscriptionContractDetails.getCustomerLastName(), customerPaymentOptional.map(CustomerPayment::getCustomerUid).orElse(null), subscriptionContractDetails.getCustomerEmail());
                model = commonEmailUtils.buildEmailModel(shopifyGraphqlClient, shopDetails, emailTemplateSetting, subscriptionContractDetails.getGraphSubscriptionContractId(), shopInfo, customer, subscriptionContractDetails.getSubscriptionContractId(), null, null, subscriptionContractDetails.getCancellationFeedback(), subscriptionContractDetails.getCancellationNote(), subscriptionContractDetails.getOrderName());

                CustomerPayment customerPayment = null;

                if (customerPaymentOptional.isPresent()) {
                    customerPayment = customerPaymentOptional.get();
                } else {
                    customerPayment = new CustomerPayment();
                    customerPayment.setShop(shop);
                    customerPayment.setCustomerId(subscriptionContractDetails.getCustomerId());
                    customerPayment.setAdminGraphqlApiCustomerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + subscriptionContractDetails.getCustomerId());
                    customerPayment.setCustomerUid(CommonUtils.generateRandomUid());
                    customerPayment.setTokenCreatedTime(ZonedDateTime.now());
                    customerPayment.setCardExpiryNotificationCounter(0L);

                    customerPayment = customerPaymentRepository.save(customerPayment);
                }

                String customerToken = customerPayment.getCustomerUid();

                model.setCustomer_token(customerToken);

                String manageSubscriptionLinkURL = "https://" + shopInfo.getPublicDomain() + "/" + shopInfoUtils.getManageSubscriptionUrl(shop) + "/cp/" + customerToken;

                if (emailTemplateSetting.getManageSubscriptionButtonUrl() == null) {
                    model.setManageSubscriptionLink(manageSubscriptionLinkURL);
                } else {
                    model.setManageSubscriptionLink(emailTemplateSetting.getManageSubscriptionButtonUrl());
                }
                model.setSubject(subject);

                commonUtils.getImageDimensions(emailTemplateSetting, model);

                this.model = model;
            } else {

                model.setShop(shopDetails);
                model.setHeading_image_url(emailTemplateSetting.getHeadingImageUrl());

                List<OrderItem> orderItems = new ArrayList<>();
                String planType = PAY_AS_YOU_GO;
                GetProductsPaginatedResponse products = api.getProducts(1);
                if (!products.getOriginalResponse().getProducts().isEmpty()) {
                    Product product = products.getOriginalResponse().getProducts().get(0);
                    OrderItem orderItem = new OrderItem();
                    if (product.getImage() != null) {
                        orderItem.setImageUrl(Optional.ofNullable(product.getImage().getSrc()).orElse("https://cdn.shopify.com/s/files/1/0533/2089/files/placeholder-images-collection-2_large.png?format=jpg&quality=90&v=1530129132"));
                    } else {
                        orderItem.setImageUrl("https://cdn.shopify.com/s/files/1/0533/2089/files/placeholder-images-collection-2_large.png?format=jpg&quality=90&v=1530129132");
                    }
                    String price = currencyFormattingUtils.formatPrice(Double.parseDouble(product.getVariants().get(0).getPrice())).replaceAll(Constants.HTML_TAG_REGEX, "");
                    orderItem.setPrice(price);
                    orderItem.setQuantity(1);
                    orderItem.setTitle(product.getTitle());
                    orderItem.setTaxable(product.getVariants().get(0).getTaxable());
                    orderItems.add(orderItem);
                } else {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setImageUrl("https://cdn.shopify.com/s/files/1/0533/2089/files/placeholder-images-collection-2_large.png?format=jpg&quality=90&v=1530129132");
                    String price = currencyFormattingUtils.formatPrice(Double.parseDouble("20")).replaceAll(Constants.HTML_TAG_REGEX, "");
                    orderItem.setPrice(price);
                    orderItem.setQuantity(1);
                    orderItem.setTitle("Test Product");
                    orderItem.setTaxable(Boolean.TRUE);
                    orderItems.add(orderItem);
                }

                String lastFourDigits = "4242";
                Customer customer = new Customer("John Doe", "John", "Doe", "fwefwefw", "john@gmail.com");
                model.setCustomer(customer);

                List<EmailSettingType> blankNextOrderDate = List.of(EmailSettingType.SUBSCRIPTION_CANCELLED, EmailSettingType.SUBSCRIPTION_PAUSED);
                ZonedDateTime nextBillingDate = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(2);
                String nextOrderDate = blankNextOrderDate.contains(emailTemplateSetting.getEmailSettingType()) ? "-" : CommonUtils.convertDateInLanguage(nextBillingDate, customerPortalSettings.getLocaleDate());
                ZonedDateTime orderCreatedAt = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(2);

                BodyModel bodyModel = new BodyModel(shopDetails, customer, lastFourDigits, nextOrderDate, "1 Day", Optional.ofNullable(subscriptionContractDetails).map(d -> d.getSubscriptionContractId()).orElse(1111111L), planType, null, "1", "6",  CommonUtils.convertDateInLanguage(orderCreatedAt, customerPortalSettings.getLocaleDate()), null, null);

                if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.OUT_OF_STOCK)) {
                    List<String> outOfStockProducts = new ArrayList<>();
                    outOfStockProducts.add("Product1");
                    outOfStockProducts.add("Product2");
                    bodyModel.setOutOfStockProducts(outOfStockProducts);
                }
                String body = liquidUtils.getValue(bodyModel, emailTemplateSetting.getContent());

                String shippingAddress = "";
                if(!StringUtils.isBlank(emailTemplateSetting.getShippingAddress())) {
                    ShippingAddressModel shippingAddressModel = new ShippingAddressModel("Infinite Loop", "Cupertino", "CA", "95014", "John", "Doe", "", "United States", "US", "+1-202-555-0133", "", "", "California", "SHIPPING");
                    model.setShipping_first_name(shippingAddressModel.getShipping_first_name());
                    model.setShipping_last_name(shippingAddressModel.getShipping_last_name());
                    model.setShipping_address1(shippingAddressModel.getShipping_address1());
                    model.setShipping_address2(shippingAddressModel.getShipping_address2());
                    model.setShipping_city(shippingAddressModel.getShipping_city());
                    model.setShipping_province_code(shippingAddressModel.getShipping_province_code());
                    model.setShipping_zip(shippingAddressModel.getShipping_zip());
                    model.setShipping_country(shippingAddressModel.getShipping_country());
                    model.setShipping_country_code(shippingAddressModel.getShipping_country_code());
                    model.setShipping_province(shippingAddressModel.getShipping_province());
                    shippingAddress = liquidUtils.getValue(shippingAddressModel, emailTemplateSetting.getShippingAddress());
                }

                BillingAddressModel billingAddressModel = new BillingAddressModel("Infinite Loop", "Cupertino", "CA", "95014", "John Doe", "United States", "US", "California");
                model.setBilling_full_name(billingAddressModel.getBilling_full_name());
                model.setBilling_address1(billingAddressModel.getBilling_address1());
                model.setBilling_city(billingAddressModel.getBilling_city());
                model.setBilling_province_code(billingAddressModel.getBilling_province_code());
                model.setBilling_zip(billingAddressModel.getBilling_zip());
                model.setBilling_country(billingAddressModel.getBilling_country());
                model.setBilling_country_code(billingAddressModel.getBilling_country_code());
                model.setBilling_province(billingAddressModel.getBilling_province());
                String billingAddress = liquidUtils.getValue(billingAddressModel, emailTemplateSetting.getBillingAddress());
                model.setBillingAddress(billingAddress);

                List<String> formattedShippingAddress = new ArrayList<>();
                formattedShippingAddress.add("Infinite Loop");
                formattedShippingAddress.add("Cupertino, CA 95014");

                List<String> formattedBillingAddress = new ArrayList<>();
                formattedBillingAddress.add("Infinite Loop");
                formattedBillingAddress.add("Cupertino, CA 95014");

                model.setSubject(subject);
                model.setHeader_text_color(emailTemplateSetting.getHeadingTextColor());
                model.setText_color(emailTemplateSetting.getContentTextColor());
                model.setFooter_text_color(emailTemplateSetting.getFooterTextColor());
                model.setFooter_link_color(emailTemplateSetting.getFooterLinkColor());
                model.setHeading(emailTemplateSetting.getHeading());
                model.setLogo_url(emailTemplateSetting.getLogo());
                model.setLogoHeight(emailTemplateSetting.getLogoHeight());
                model.setLogoWidth(emailTemplateSetting.getLogoWidth());
                model.setLogoAlignment(emailTemplateSetting.getLogoAlignment());
                model.setThanksImageHeight(emailTemplateSetting.getThanksImageHeight());
                model.setThanksImageWidth(emailTemplateSetting.getThanksImageWidth());
                model.setThanksImageAlignment(emailTemplateSetting.getThanksImageAlignment());
                model.setOrderItems(orderItems);
                model.setBody_content(body);
                model.setFooter(emailTemplateSetting.getFooterText());
                model.setMaskedCardNumber(Optional.ofNullable(emailTemplateSetting.getEndingInText() + " ").orElse("Ending in ") + lastFourDigits);
                model.setNextOrderDate(nextOrderDate);
                model.setPlanType(planType);
                model.setManageSubscriptionLink("");
                model.setShipping_address_text(emailTemplateSetting.getShippingAddressText());
                model.setBilling_address_text(emailTemplateSetting.getBillingAddressText());
                model.setNext_orderdate_text(emailTemplateSetting.getNextOrderdateText());
                model.setPayment_method_text(emailTemplateSetting.getPaymentMethodText());
                model.setManage_subscription_button_color(emailTemplateSetting.getManageSubscriptionButtonColor());
                model.setManage_subscription_button_text(emailTemplateSetting.getManageSubscriptionButtonText());
                model.setManage_subscription_button_text_color(emailTemplateSetting.getManageSubscriptionButtonTextColor());
                model.setText_image_url(emailTemplateSetting.getTextImageUrl());
                model.setQuantity_text(emailTemplateSetting.getQuantityText());
                model.setShippingAddress(shippingAddress);
                model.setSelling_plan_name_text("Selling Plan");
                model.setVariant_sku_text("SKU");

                String cardLogo = "https://brand.mastercard.com//content/dam/mccom/brandcenter/thumbnails/mastercard_circles_92px_2x.png";
                String cardBrand = "visa";
                if (cardBrand.toLowerCase().contains("visa")) {
                    cardLogo = "http://www.credit-card-logos.com/images/visa_credit-card-logos/visa_logo_3.gif";
                } else if (cardBrand.toLowerCase().contains("ame")) {
                    cardLogo = "http://www.credit-card-logos.com/images/american_express_credit-card-logos/american_express_logo_3.gif";
                } else if (cardBrand.toLowerCase().contains("discover")) {
                    cardLogo = "http://www.credit-card-logos.com/images/discover_credit-card-logos/discover_network1.jpg";
                }
                model.setCardLogo(cardLogo);

                List<String> bodyLines = Arrays.stream(Optional.ofNullable(model.getBody_content()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
                model.setBodyLines(bodyLines);

                List<String> shippingLines = Arrays.stream(Optional.ofNullable(model.getShippingAddress()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
                model.setShippingLines(shippingLines);

                List<String> billingLines = Arrays.stream(Optional.ofNullable(model.getBillingAddress()).orElse("").split("(\r\n|\n)")).collect(Collectors.toList());
                model.setBillingLines(billingLines);

                this.model = model;
            }

            String emailTemplateHtml = StringUtils.isBlank(emailTemplateSetting.getHtml()) ? IOUtils.toString(baseMailTemplate.getInputStream()) : emailTemplateSetting.getHtml();

            this.html = liquidUtils.getValue(model, emailTemplateHtml);
            return this;
        }
    }

    @Override
    public EmailTemplateSetting buildEmailSettingForAllSubscribers(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.BULK_ALL_SUBSCRIBERS);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.BULK_ALL_SUBSCRIBERS_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildEmailSettingForActiveSubscribersOnly(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.BULK_ACTIVE_SUBSCRIBERS_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildEmailSettingForPausedSubscribersOnly(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.BULK_PAUSED_SUBSCRIBERS_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildEmailSettingForCancelledSubscribersOnly(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.BULK_CANCELLED_SUBSCRIBERS_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Override
    public EmailTemplateSetting buildEmailSettingForOutOfStock(String shop, Shop shopDetails) throws IOException {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting();
        emailTemplateSetting.setSubject(EmailTemplateConstants.OUT_OF_STOCK_SUBJECT);

        String fromEmail = shopDetails.getName() + " <" + shopDetails.getEmail() + ">";
        emailTemplateSetting.setFromEmail(fromEmail);
        emailTemplateSetting.setTemplateBackgroundColor(EmailTemplateConstants.OUT_OF_STOCK_TEMPLATE_BACKGROUND_COLOR);
        emailTemplateSetting.setHeading(EmailTemplateConstants.OUT_OF_STOCK_HEADING);
        emailTemplateSetting.setHeadingTextColor(EmailTemplateConstants.OUT_OF_STOCK_HEADING_TEXT_COLOR);
        emailTemplateSetting.setContentTextColor(EmailTemplateConstants.OUT_OF_STOCK_CONTENT_TEXT_COLOR);
        emailTemplateSetting.setContentLinkColor(EmailTemplateConstants.OUT_OF_STOCK_CONTENT_LINK_COLOR);
        emailTemplateSetting.setContent(EmailTemplateConstants.OUT_OF_STOCK_CONTENT_TEXT);
        emailTemplateSetting.setFooterTextColor(EmailTemplateConstants.OUT_OF_STOCK_FOOTER_TEXT_COLOR);
        emailTemplateSetting.setFooterLinkColor(EmailTemplateConstants.OUT_OF_STOCK_FOOTER_LINK_COLOR);
        emailTemplateSetting.setFooterText(EmailTemplateConstants.OUT_OF_STOCK_FOOTER_TEXT);
        emailTemplateSetting.setEmailSettingType(EmailSettingType.OUT_OF_STOCK);
        emailTemplateSetting.setUpcomingOrderEmailBuffer(EmailTemplateConstants.OUT_OF_STOCK_EMAIL_BUFFER);
        setCommonDefaultValueForEmailTemplateSetting(emailTemplateSetting, shop);

        return emailTemplateSetting;
    }

    @Async
    @Override
    public Boolean sendBulkMails(Long emailTemplateSettingId, String shop) throws Exception {

        EmailTemplateSetting emailTemplateSetting = emailTemplateSettingRepository.findById(emailTemplateSettingId).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();


        List<SubscriptionContractDetails> subscriptionContractDetailsList = new ArrayList<>();

        if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.BULK_ALL_SUBSCRIBERS)){
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
        } else if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.BULK_ACTIVE_SUBSCRIBERS_ONLY)){
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndStatus(shop, "active");
        } else if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.BULK_PAUSED_SUBSCRIBERS_ONLY)){
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndStatus(shop, "paused");
        } else if(emailTemplateSetting.getEmailSettingType().equals(EmailSettingType.BULK_CANCELLED_SUBSCRIBERS_ONLY)){
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndStatus(shop, "cancelled");
        }

        for (SubscriptionContractDetails contractDetails: subscriptionContractDetailsList) {
            if(StringUtils.isNotEmpty(contractDetails.getCustomerEmail())) {
                EmailTemplateBuilder emailTemplateBuilder = new EmailTemplateBuilder(
                    emailTemplateSetting,
                    contractDetails,
                    shop,
                    shopifyGraphqlClient,
                    api).invoke();
                if (!org.apache.commons.lang3.StringUtils.isBlank(shopInfo.getKlaviyoApiKey()) && !org.apache.commons.lang3.StringUtils.isBlank(shopInfo.getKlaviyoPublicApiKey())) {
                    klaviyoService.sendMail(emailTemplateSetting, shopInfo, emailTemplateBuilder.getModel(), contractDetails.getCustomerEmail());
                } else {
                    mailgunService.sendEmail(contractDetails.getCustomerEmail(), emailTemplateBuilder.getSubject(), emailTemplateBuilder.getHtml(), emailTemplateSetting, shop, emailTemplateBuilder.getShopDetails());
                }
            }
            commonUtils.writeActivityLog(
                shop,
                emailTemplateSettingId,
                ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS,
                ActivityLogEventSource.SYSTEM_EVENT,
                ActivityLogEventType.BULK_EMAIL,
                ActivityLogStatus.SUCCESS
            );
        }

        return true;
    }
}
