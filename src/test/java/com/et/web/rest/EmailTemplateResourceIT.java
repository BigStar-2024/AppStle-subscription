package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.EmailTemplate;
import com.et.repository.EmailTemplateRepository;
import com.et.service.EmailTemplateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.et.domain.enumeration.EmailSettingType;
/**
 * Integration tests for the {@link EmailTemplateResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EmailTemplateResourceIT {

    private static final EmailSettingType DEFAULT_EMAIL_TYPE = EmailSettingType.SUBSCRIPTION_CREATED;
    private static final EmailSettingType UPDATED_EMAIL_TYPE = EmailSettingType.TRANSACTION_FAILED;

    private static final String DEFAULT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_HTML = "BBBBBBBBBB";

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailTemplateMockMvc;

    private EmailTemplate emailTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailTemplate createEntity(EntityManager em) {
        EmailTemplate emailTemplate = new EmailTemplate()
            .emailType(DEFAULT_EMAIL_TYPE)
            .html(DEFAULT_HTML);
        return emailTemplate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailTemplate createUpdatedEntity(EntityManager em) {
        EmailTemplate emailTemplate = new EmailTemplate()
            .emailType(UPDATED_EMAIL_TYPE)
            .html(UPDATED_HTML);
        return emailTemplate;
    }

    @BeforeEach
    public void initTest() {
        emailTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailTemplate() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateRepository.findAll().size();
        // Create the EmailTemplate
        restEmailTemplateMockMvc.perform(post("/api/email-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isCreated());

        // Validate the EmailTemplate in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        EmailTemplate testEmailTemplate = emailTemplateList.get(emailTemplateList.size() - 1);
        assertThat(testEmailTemplate.getEmailType()).isEqualTo(DEFAULT_EMAIL_TYPE);
        assertThat(testEmailTemplate.getHtml()).isEqualTo(DEFAULT_HTML);
    }

    @Test
    @Transactional
    public void createEmailTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateRepository.findAll().size();

        // Create the EmailTemplate with an existing ID
        emailTemplate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailTemplateMockMvc.perform(post("/api/email-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the EmailTemplate in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmailTemplates() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);

        // Get all the emailTemplateList
        restEmailTemplateMockMvc.perform(get("/api/email-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailType").value(hasItem(DEFAULT_EMAIL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML.toString())));
    }
    
    @Test
    @Transactional
    public void getEmailTemplate() throws Exception {
        // Initialize the database
        emailTemplateRepository.saveAndFlush(emailTemplate);

        // Get the emailTemplate
        restEmailTemplateMockMvc.perform(get("/api/email-templates/{id}", emailTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailTemplate.getId().intValue()))
            .andExpect(jsonPath("$.emailType").value(DEFAULT_EMAIL_TYPE.toString()))
            .andExpect(jsonPath("$.html").value(DEFAULT_HTML.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEmailTemplate() throws Exception {
        // Get the emailTemplate
        restEmailTemplateMockMvc.perform(get("/api/email-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailTemplate() throws Exception {
        // Initialize the database
        emailTemplateService.save(emailTemplate);

        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();

        // Update the emailTemplate
        EmailTemplate updatedEmailTemplate = emailTemplateRepository.findById(emailTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedEmailTemplate are not directly saved in db
        em.detach(updatedEmailTemplate);
        updatedEmailTemplate
            .emailType(UPDATED_EMAIL_TYPE)
            .html(UPDATED_HTML);

        restEmailTemplateMockMvc.perform(put("/api/email-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmailTemplate)))
            .andExpect(status().isOk());

        // Validate the EmailTemplate in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeUpdate);
        EmailTemplate testEmailTemplate = emailTemplateList.get(emailTemplateList.size() - 1);
        assertThat(testEmailTemplate.getEmailType()).isEqualTo(UPDATED_EMAIL_TYPE);
        assertThat(testEmailTemplate.getHtml()).isEqualTo(UPDATED_HTML);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailTemplate() throws Exception {
        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailTemplateMockMvc.perform(put("/api/email-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the EmailTemplate in the database
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmailTemplate() throws Exception {
        // Initialize the database
        emailTemplateService.save(emailTemplate);

        int databaseSizeBeforeDelete = emailTemplateRepository.findAll().size();

        // Delete the emailTemplate
        restEmailTemplateMockMvc.perform(delete("/api/email-templates/{id}", emailTemplate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailTemplate> emailTemplateList = emailTemplateRepository.findAll();
        assertThat(emailTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
