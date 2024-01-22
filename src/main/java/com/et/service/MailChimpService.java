package com.et.service;

import com.et.api.mailchimp.MailchimpApi;
import com.et.api.mailchimp.model.Message;
import com.et.api.mailchimp.model.RecipientInformation;
import com.et.api.mailchimp.model.SenderDomain;
import com.et.domain.EmailTemplateSetting;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailChimpService {

    private final Logger logger = LoggerFactory.getLogger(MailChimpService.class);

    public List<SenderDomain> getDomains(String shop, String apiKey) throws Exception{
        logger.info("Getting all mailchimp sender domain info for shop: {}", shop);

        MailchimpApi mailchimpApi = new MailchimpApi();
        return mailchimpApi.getDomains(apiKey);
    }

    public void sendMail(String customerEmailAddress, String subject, String htmlBody, EmailTemplateSetting emailTemplateSetting, String shopName, String apiKey) throws JsonProcessingException {
        logger.info("sending Mailchimp email for customerEmail=" + customerEmailAddress + " shop=" + shopName + " emailSettingType=" + emailTemplateSetting.getEmailSettingType());

        if (BooleanUtils.isTrue(emailTemplateSetting.isSendBCCEmailFlag()) && !org.apache.commons.lang3.StringUtils.isBlank(emailTemplateSetting.getBccEmail())) {
            customerEmailAddress = emailTemplateSetting.getBccEmail();
        } else if (BooleanUtils.isTrue(emailTemplateSetting.isSendBCCEmailFlag())) {
            logger.info("isSendBCCEmailFlag flag was enabled but no bcc email was found.");
            return;
        }

        Message message = buildMessage(customerEmailAddress, htmlBody, subject, emailTemplateSetting);

        MailchimpApi mailchimpApi = new MailchimpApi();
        Object[] response = mailchimpApi.sendEmail(apiKey, message);
        logger.debug("Mailchimp service response: {}", response);
    }

    private Message buildMessage(String customerEmailAddress, String htmlBody, String subject, EmailTemplateSetting emailTemplateSetting){
        Message message = new Message();
        message.setHtml(htmlBody);
        message.setSubject(subject);
        if(StringUtils.isNotBlank(emailTemplateSetting.getFromEmail())){
            String[] from = emailTemplateSetting.getFromEmail().split("<");
            if(from.length > 1){
                message.setFrom_email(from[1].replace(">", "").trim());
                message.setFrom_name(from[0].trim());
            }else{
                message.setFrom_email(from[0].trim());
            }
        }
        message.setTo(List.of(new RecipientInformation(customerEmailAddress)));
        if(StringUtils.isNotBlank(emailTemplateSetting.getBccEmail())){
            message.setBcc_address(emailTemplateSetting.getBccEmail());
        }
        return message;
    }
}
