package com.et.web.rest;

import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.shop.Shop;
import com.et.domain.EmailTemplateSetting;
import com.et.domain.enumeration.EmailSettingType;
import com.et.pojo.EmailTemplateSettingPropertyInfo;
import com.et.security.SecurityUtils;
import com.et.service.EmailTemplateSettingService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.EmailTemplateSetting}.
 */
@RestController
@RequestMapping("/api")
public class EmailTemplateSettingResource {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateSettingResource.class);

    private static final String ENTITY_NAME = "emailTemplateSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private CommonUtils commonUtils;

    public EmailTemplateSettingResource(EmailTemplateSettingService emailTemplateSettingService) {
        this.emailTemplateSettingService = emailTemplateSettingService;
    }

    /**
     * {@code POST  /email-template-settings} : Create a new emailTemplateSetting.
     *
     * @param emailTemplateSetting the emailTemplateSetting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailTemplateSetting, or with status {@code 400 (Bad Request)} if the emailTemplateSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/email-template-settings")
    public ResponseEntity<EmailTemplateSetting> createEmailTemplateSetting(@Valid @RequestBody EmailTemplateSetting emailTemplateSetting) throws URISyntaxException {
        log.debug("REST request to save EmailTemplateSetting : {}", emailTemplateSetting);
        if (emailTemplateSetting.getId() != null) {
            throw new BadRequestAlertException("A new emailTemplateSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailTemplateSetting result = emailTemplateSettingService.save(emailTemplateSetting);
        return ResponseEntity.created(new URI("/api/email-template-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /email-template-settings} : Updates an existing emailTemplateSetting.
     *
     * @param emailTemplateSetting the emailTemplateSetting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailTemplateSetting,
     * or with status {@code 400 (Bad Request)} if the emailTemplateSetting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailTemplateSetting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/email-template-settings")
    public ResponseEntity<EmailTemplateSetting> updateEmailTemplateSetting(@Valid @RequestBody EmailTemplateSetting emailTemplateSetting) throws URISyntaxException {
        log.debug("REST request to update EmailTemplateSetting : {}", emailTemplateSetting);
        if (emailTemplateSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(emailTemplateSetting.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        EmailTemplateSetting result = emailTemplateSettingService.save(emailTemplateSetting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Email Template Setting Updated.", emailTemplateSetting.getId().toString()))
            .body(result);
    }

    @PutMapping("email-template-settings/update-bulk-email-templates-property")
    public ResponseEntity<List<EmailTemplateSetting>> updateBulkEmailTemplatesProperty(@RequestBody EmailTemplateSettingPropertyInfo propertyInfo) {

        if (propertyInfo.getPropertyName() == null) {
            throw new BadRequestAlertException("Invalid id", "propertyName", "namenull");
        }

        if (propertyInfo.getPropertyValue() == null) {
            throw new BadRequestAlertException("Invalid id", "propertyValue", "idnull");
        }

        List<EmailTemplateSetting> result = emailTemplateSettingService.updateBulkEmailTemplatesSettingByProperty(propertyInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Email Template Setting Updated Successfully.", propertyInfo.getPropertyName()))
            .body(result);
    }

    /**
     * {@code GET  /email-template-settings} : get all the emailTemplateSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailTemplateSettings in body.
     */
    @GetMapping("/email-template-settings")
    public List<EmailTemplateSetting> getAllEmailTemplateSettings() {
        log.debug("REST request to get all EmailTemplateSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return emailTemplateSettingService.findByShop(shop);
    }

    @PostMapping("/email-template-settings/preview")
    public String previewSubscriptionCreatedEmail(@RequestBody EmailTemplateSetting emailTemplateSetting) {
        log.debug("REST request to get preview of EmailTemplateSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return emailTemplateSettingService.getSubscriptionCreatedEmailString(shop, emailTemplateSetting);
    }

    @PostMapping("/email-template-settings/send-test-mail/{emailTemplateSettingId}")
    public Boolean sendTestMail(@PathVariable("emailTemplateSettingId") Long emailTemplateSettingId,
                                                  @RequestParam("emailId") String emailId) throws Exception {
        log.debug("REST request to send test mail");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return emailTemplateSettingService.sendTestMail(shop, emailId, emailTemplateSettingId);
    }

    @PostMapping("/email-template-settings/send-bulk-mails/{emailTemplateSettingId}")
    public Boolean sendBulkMails(@PathVariable("emailTemplateSettingId") Long emailTemplateSettingId) throws Exception {
        log.debug("REST request to send bulk emails");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return emailTemplateSettingService.sendBulkMails(emailTemplateSettingId, shop);
    }

    @PostMapping("/email-template-settings/reset/{emailTemplateSettingId}")
    public Boolean resetEmailTemplateSettings(@PathVariable("emailTemplateSettingId") Long emailTemplateSettingId,
                                @RequestParam("emailSettingType") String emailSettingType) throws Exception {
        log.debug("REST request to reset email template settings");

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
        Shop shopDetails = api.getShopInfo().getShop();
        return emailTemplateSettingService.resetEmailTemplateSettings(EmailSettingType.valueOf(emailSettingType), emailTemplateSettingId, shop, shopDetails);
    }

    /**
     * {@code GET  /email-template-settings/:id} : get the "id" emailTemplateSetting.
     *
     * @param id the id of the emailTemplateSetting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailTemplateSetting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-template-settings/{id}")
    public ResponseEntity<EmailTemplateSetting> getEmailTemplateSetting(@PathVariable Long id) {
        log.debug("REST request to get EmailTemplateSetting : {}", id);
        Optional<EmailTemplateSetting> emailTemplateSetting = emailTemplateSettingService.findOne(id);

        String shop = commonUtils.getShop();
        if(emailTemplateSetting.isPresent() && !shop.equals(emailTemplateSetting.get().getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(emailTemplateSetting);
    }

    /**
     * {@code DELETE  /email-template-settings/:id} : delete the "id" emailTemplateSetting.
     *
     * @param id the id of the emailTemplateSetting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/email-template-settings/{id}")
    public ResponseEntity<Void> deleteEmailTemplateSetting(@PathVariable Long id) {
        log.debug("REST request to delete EmailTemplateSetting : {}", id);
        emailTemplateSettingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
