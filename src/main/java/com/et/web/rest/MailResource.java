package com.et.web.rest;

import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.shop.Shop;
import com.et.domain.EmailTemplateSetting;
import com.et.domain.SocialConnection;
import com.et.service.EmailTemplateSettingService;
import com.et.service.MailgunService;
import com.et.service.SocialConnectionService;
import com.et.utils.CommonUtils;
import com.et.web.rest.vm.SendMailVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MailResource {

    private final Logger log = LoggerFactory.getLogger(MailResource.class);

    private final MailgunService mailgunService;

    private final EmailTemplateSettingService emailTemplateSettingService;

    private final SocialConnectionService socialConnectionService;

    private final CommonUtils commonUtils;

    public MailResource(MailgunService mailgunService, EmailTemplateSettingService emailTemplateSettingService, SocialConnectionService socialConnectionService, CommonUtils commonUtils) {
        this.mailgunService = mailgunService;
        this.emailTemplateSettingService = emailTemplateSettingService;
        this.socialConnectionService = socialConnectionService;
        this.commonUtils = commonUtils;
    }

    @PostMapping("/send-mail")
    public ResponseEntity<List<String>> sendMail(@Valid @RequestBody SendMailVM sendMailVM) {
        log.info("Send mail request : {}", sendMailVM);

        List<String> errors = new ArrayList<>();
        Optional<EmailTemplateSetting> emailTemplateSetting = this.emailTemplateSettingService.findOne(sendMailVM.getEmailTemplateSettingId());
        if (emailTemplateSetting.isEmpty()) {
            errors.add("Invalid email template setting id");
        }

        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(sendMailVM.getShopName());
        if (socialConnection.isEmpty()) {
            errors.add("Invalid shop name.");
        }

        if (errors.isEmpty() && socialConnection.isPresent() && emailTemplateSetting.isPresent()) {
            ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(socialConnection.get().getUserId());
            Shop shopDetails = shopifyRestApi.getShopInfo().getShop();
            mailgunService.sendEmail(sendMailVM.getCustomerEmailAddress(), sendMailVM.getSubject(), sendMailVM.getHtmlBody(), emailTemplateSetting.get(), sendMailVM.getShopName(), shopDetails);
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/send-raw-email")
    public ResponseEntity<List<String>> sendRawEmail(@Valid @RequestBody SendMailVM sendMailVM) {
        List<String> errors = new ArrayList<>();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByUserId(sendMailVM.getShopName());
        if (socialConnection.isEmpty()) {
            errors.add("Invalid shop name.");
        }

        if (errors.isEmpty() && socialConnection.isPresent()) {
            mailgunService.sendRawEmail(sendMailVM.getCustomerEmailAddress(), sendMailVM.getSubject(), sendMailVM.getHtmlBody(), sendMailVM.getShopName());
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.badRequest().body(errors);
    }
}
