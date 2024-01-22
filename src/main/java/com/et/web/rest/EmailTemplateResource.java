package com.et.web.rest;

import com.et.domain.EmailTemplate;
import com.et.security.SecurityUtils;
import com.et.service.EmailTemplateService;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.EmailTemplate}.
 */
@RestController
@RequestMapping("/api")
public class EmailTemplateResource {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateResource.class);

    private static final String ENTITY_NAME = "emailTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailTemplateService emailTemplateService;

    public EmailTemplateResource(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    /**
     * {@code POST  /email-templates} : Create a new emailTemplate.
     *
     * @param emailTemplate the emailTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailTemplate, or with status {@code 400 (Bad Request)} if the emailTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/email-templates")
    public ResponseEntity<EmailTemplate> createEmailTemplate(@RequestBody EmailTemplate emailTemplate) throws URISyntaxException {
        log.debug("REST request to save EmailTemplate : {}", emailTemplate);
        if (emailTemplate.getId() != null) {
            throw new BadRequestAlertException("A new emailTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }

        EmailTemplate result = emailTemplateService.save(emailTemplate);
        return ResponseEntity.created(new URI("/api/email-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /email-templates} : Updates an existing emailTemplate.
     *
     * @param emailTemplate the emailTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailTemplate,
     * or with status {@code 400 (Bad Request)} if the emailTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PutMapping("/email-templates")
    public ResponseEntity<EmailTemplate> updateEmailTemplate(@RequestBody EmailTemplate emailTemplate) throws URISyntaxException {
        log.debug("REST request to update EmailTemplate : {}", emailTemplate);
        if (emailTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmailTemplate result = emailTemplateService.save(emailTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emailTemplate.getId().toString()))
            .body(result);
    }
*/
    /**
     * {@code GET  /email-templates} : get all the emailTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailTemplates in body.
     */
    @GetMapping("/email-templates")
    public List<EmailTemplate> getAllEmailTemplates() {
        log.debug("REST request to get all EmailTemplates");
        return emailTemplateService.findAll();
    }

    /**
     * {@code GET  /email-templates/:id} : get the "id" emailTemplate.
     *
     * @param id the id of the emailTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-templates/{id}")
    public ResponseEntity<EmailTemplate> getEmailTemplate(@PathVariable Long id) {
        log.debug("REST request to get EmailTemplate : {}", id);
        Optional<EmailTemplate> emailTemplate = emailTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailTemplate);
    }

    /**
     * {@code DELETE  /email-templates/:id} : delete the "id" emailTemplate.
     *
     * @param id the id of the emailTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/email-templates/{id}")
    public ResponseEntity<Void> deleteEmailTemplate(@PathVariable Long id) {
        log.debug("REST request to delete EmailTemplate : {}", id);
        emailTemplateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
