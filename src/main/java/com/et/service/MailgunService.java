package com.et.service;


import com.amazonaws.util.StringUtils;
import com.et.api.shopify.shop.Shop;
import com.et.constant.MailgunProperties;
import com.et.domain.EmailTemplateSetting;
import com.et.service.dto.ShopInfoDTO;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
public class MailgunService {

    private static final Logger log = LoggerFactory.getLogger(MailgunService.class);

    private final MailgunProperties mailgunProperties;

    @Autowired
    private ShopInfoService shopInfoService;

    public MailgunService(MailgunProperties mailgunProperties) {
        this.mailgunProperties = mailgunProperties;
    }

    public void sendEmail(String customerEmailAddress, String subject, String htmlBody, EmailTemplateSetting setting, String shopName, Shop shop) {

        log.info("sending Mailgun email for customerEmail=" + customerEmailAddress + " shop=" + shopName + " emailSettingType=" + setting.getEmailSettingType());

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shopName).get();

        String domain = !StringUtils.isNullOrEmpty(shopInfoDTO.getEmailCustomDomain()) && BooleanUtils.isTrue(shopInfoDTO.isVerifiedEmailCustomDomain())
            ? shopInfoDTO.getEmailCustomDomain() : mailgunProperties.getDomain();

        MultipartBody multipartBody;

        if (BooleanUtils.isTrue(setting.isSendBCCEmailFlag()) && !org.apache.commons.lang3.StringUtils.isBlank(setting.getBccEmail())) {
            customerEmailAddress = setting.getBccEmail();
        } else if (BooleanUtils.isTrue(setting.isSendBCCEmailFlag())) {
            log.info("isSendBCCEmailFlag flag was enabled but no bcc email was found.");
            return;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(setting.getBccEmail())) {
            multipartBody = Unirest.post(mailgunProperties.getBaseUrl() + domain + "/messages")
                .basicAuth("api", mailgunProperties.getApiKey()).field("from", setting.getFromEmail())
                .field("to", customerEmailAddress).field("subject", subject).field("html", htmlBody).field("o:tag", shopName);
        } else {
            multipartBody = Unirest.post(mailgunProperties.getBaseUrl() + domain + "/messages")
                .basicAuth("api", mailgunProperties.getApiKey()).field("from", setting.getFromEmail()).field("bcc", setting.getBccEmail())
                .field("to", customerEmailAddress).field("subject", subject).field("html", htmlBody).field("o:tag", shopName);
        }

        if (setting.getFromEmail().contains("subscription-support@appstle.com")) {
            multipartBody = multipartBody.field("h:Reply-To", shop.getEmail());
        }

        if (!org.apache.commons.lang3.StringUtils.isBlank(setting.getReplyTo())) {
            multipartBody = multipartBody.field("h:Reply-To", setting.getReplyTo());
        }
        HttpResponse<JsonNode> httpResponse = multipartBody.asJson();
        log.debug("Mailgun service response: {}", httpResponse.getBody().toString());
    }

    public void sendEmailWithAttachment(File attachment, String subject, String htmlBody, String fromEmail, String shopName, String shopEmail) {

        MultipartBody multipartBody = Unirest.post(mailgunProperties.getBaseUrl() + mailgunProperties.getDomain() + "/messages")
            .basicAuth("api", mailgunProperties.getApiKey()).field("from", fromEmail)
            .field("to", shopEmail).field("subject", subject).field("html", htmlBody).field("o:tag", shopName).field("h:Reply-To", fromEmail).field("attachment", attachment);

        HttpResponse<JsonNode> httpResponse = multipartBody.asJson();
        log.debug("Mailgun service response: {}", httpResponse.getBody().toString());
    }


    public void sendRawEmail(String emailAddress, String subject, String htmlBody, String shopName) {
        sendRawEmail(emailAddress, subject, htmlBody, shopName, "Appstle <noreply@appstle.com>");
    }

    public void sendRawEmail(String emailAddress, String subject, String htmlBody, String shopName, String from) {

        log.info("sending raw Mailgun email for emailAddress=" + emailAddress + " shop=" + shopName);

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shopName).get();

        from = Optional.ofNullable(from).orElse("Appstle <noreply@appstle.com>");

        String domain = !StringUtils.isNullOrEmpty(shopInfoDTO.getEmailCustomDomain()) && BooleanUtils.isTrue(shopInfoDTO.isVerifiedEmailCustomDomain())
            ? shopInfoDTO.getEmailCustomDomain() : mailgunProperties.getDomain();

        MultipartBody multipartBody = Unirest.post(mailgunProperties.getBaseUrl() + domain + "/messages")
            .basicAuth("api", mailgunProperties.getApiKey())
            .field("from", from)
            .field("to", emailAddress)
            .field("subject", subject)
            .field("html", htmlBody)
            .field("o:tag", shopName);

        HttpResponse<JsonNode> httpResponse = multipartBody.asJson();

        log.debug("Mailgun service response: {}", httpResponse.getBody().toString());
    }
}
