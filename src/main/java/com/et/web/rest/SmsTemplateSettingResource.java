package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.SmsTemplateSettingService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SmsTemplateSettingDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SmsTemplateSetting}.
 */
@RestController
@RequestMapping("/api")
public class SmsTemplateSettingResource {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateSettingResource.class);

    private static final String ENTITY_NAME = "smsTemplateSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsTemplateSettingService smsTemplateSettingService;

    public SmsTemplateSettingResource(SmsTemplateSettingService smsTemplateSettingService) {
        this.smsTemplateSettingService = smsTemplateSettingService;
    }

    /**
     * {@code POST  /sms-template-settings} : Create a new smsTemplateSetting.
     *
     * @param smsTemplateSettingDTO the smsTemplateSettingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsTemplateSettingDTO, or with status {@code 400 (Bad Request)} if the smsTemplateSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-template-settings")
    public ResponseEntity<SmsTemplateSettingDTO> createSmsTemplateSetting(@Valid @RequestBody SmsTemplateSettingDTO smsTemplateSettingDTO) throws URISyntaxException {
        log.debug("REST request to save SmsTemplateSetting : {}", smsTemplateSettingDTO);
        if (smsTemplateSettingDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsTemplateSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if(!shop.equals(smsTemplateSettingDTO.getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsTemplateSettingDTO result = smsTemplateSettingService.save(smsTemplateSettingDTO);
        return ResponseEntity.created(new URI("/api/sms-template-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-template-settings} : Updates an existing smsTemplateSetting.
     *
     * @param smsTemplateSettingDTO the smsTemplateSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateSettingDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateSettingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-template-settings")
    public ResponseEntity<SmsTemplateSettingDTO> updateSmsTemplateSetting(@Valid @RequestBody SmsTemplateSettingDTO smsTemplateSettingDTO) throws URISyntaxException {
        log.debug("REST request to update SmsTemplateSetting : {}", smsTemplateSettingDTO);
        if (smsTemplateSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = SecurityUtils.getCurrentUserLogin().get();
        if(!shop.equals(smsTemplateSettingDTO.getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsTemplateSettingDTO result = smsTemplateSettingService.save(smsTemplateSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Sms Template Setting Updated.", smsTemplateSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sms-template-settings} : get all the smsTemplateSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsTemplateSettings in body.
     */
    @GetMapping("/sms-template-settings")
    public List<SmsTemplateSettingDTO> getAllSmsTemplateSettings() {
        log.debug("REST request to get all SmsTemplateSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return smsTemplateSettingService.findAllByShop(shop);
    }

    /**
     * {@code GET  /sms-template-settings/:id} : get the "id" smsTemplateSetting.
     *
     * @param id the id of the smsTemplateSettingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsTemplateSettingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-template-settings/{id}")
    public ResponseEntity<SmsTemplateSettingDTO> getSmsTemplateSetting(@PathVariable Long id) {
        log.debug("REST request to get SmsTemplateSetting : {}", id);
        Optional<SmsTemplateSettingDTO> smsTemplateSettingDTO = smsTemplateSettingService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if(smsTemplateSettingDTO.isPresent() && !shop.equals(smsTemplateSettingDTO.get().getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(smsTemplateSettingDTO);
    }

    /**
     * {@code DELETE  /sms-template-settings/:id} : delete the "id" smsTemplateSetting.
     *
     * @param id the id of the smsTemplateSettingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-template-settings/{id}")
    public ResponseEntity<Void> deleteSmsTemplateSetting(@PathVariable Long id) {
        log.debug("REST request to delete SmsTemplateSetting : {}", id);
        Optional<SmsTemplateSettingDTO> smsTemplateSettingDTO = smsTemplateSettingService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if(smsTemplateSettingDTO.isPresent() && !shop.equals(smsTemplateSettingDTO.get().getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        smsTemplateSettingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/sms-template-settings/preview")
    public String previewSms(@RequestBody SmsTemplateSettingDTO smsTemplateSettingDTO) {
        try {
            log.debug("REST request to get preview of SmsTemplateSettings");
            if (smsTemplateSettingDTO.getSmsSettingType() == null) {
                return StringUtils.EMPTY;
            }
            return smsTemplateSettingService.getSmsContent(smsTemplateSettingDTO);
        } catch (Exception ex) {
            return StringUtils.EMPTY;
        }
    }

    @PostMapping("/sms-template-settings/send-test-sms/{smsTemplateId}")
    public Boolean sendTestMail(@PathVariable("smsTemplateId") Long smsTemplateId,
                                @RequestParam("phone") String phone) throws Exception {
        log.debug("REST request to send test sms");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return smsTemplateSettingService.sendTestSMS(smsTemplateId, phone);
    }
}
