package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.BulkAutomation;
import com.et.repository.BulkAutomationRepository;
import com.et.service.BulkAutomationService;
import com.et.service.dto.BulkAutomationDTO;
import com.et.service.mapper.BulkAutomationMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.et.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.et.domain.enumeration.BulkAutomationType;
/**
 * Integration tests for the {@link BulkAutomationResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BulkAutomationResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final BulkAutomationType DEFAULT_AUTOMATION_TYPE = BulkAutomationType.EXPORT;
    private static final BulkAutomationType UPDATED_AUTOMATION_TYPE = BulkAutomationType.MIGRATION;

    private static final Boolean DEFAULT_RUNNING = false;
    private static final Boolean UPDATED_RUNNING = true;

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_REQUEST_INFO = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_INFO = "BBBBBBBBBB";

    @Autowired
    private BulkAutomationRepository bulkAutomationRepository;

    @Autowired
    private BulkAutomationMapper bulkAutomationMapper;

    @Autowired
    private BulkAutomationService bulkAutomationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBulkAutomationMockMvc;

    private BulkAutomation bulkAutomation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BulkAutomation createEntity(EntityManager em) {
        BulkAutomation bulkAutomation = new BulkAutomation()
            .shop(DEFAULT_SHOP)
            .automationType(DEFAULT_AUTOMATION_TYPE)
            .running(DEFAULT_RUNNING)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .requestInfo(DEFAULT_REQUEST_INFO)
            .errorInfo(DEFAULT_ERROR_INFO);
        return bulkAutomation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BulkAutomation createUpdatedEntity(EntityManager em) {
        BulkAutomation bulkAutomation = new BulkAutomation()
            .shop(UPDATED_SHOP)
            .automationType(UPDATED_AUTOMATION_TYPE)
            .running(UPDATED_RUNNING)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .requestInfo(UPDATED_REQUEST_INFO)
            .errorInfo(UPDATED_ERROR_INFO);
        return bulkAutomation;
    }

    @BeforeEach
    public void initTest() {
        bulkAutomation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBulkAutomation() throws Exception {
        int databaseSizeBeforeCreate = bulkAutomationRepository.findAll().size();
        // Create the BulkAutomation
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(bulkAutomation);
        restBulkAutomationMockMvc.perform(post("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isCreated());

        // Validate the BulkAutomation in the database
        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeCreate + 1);
        BulkAutomation testBulkAutomation = bulkAutomationList.get(bulkAutomationList.size() - 1);
        assertThat(testBulkAutomation.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testBulkAutomation.getAutomationType()).isEqualTo(DEFAULT_AUTOMATION_TYPE);
        assertThat(testBulkAutomation.isRunning()).isEqualTo(DEFAULT_RUNNING);
        assertThat(testBulkAutomation.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testBulkAutomation.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testBulkAutomation.getRequestInfo()).isEqualTo(DEFAULT_REQUEST_INFO);
        assertThat(testBulkAutomation.getErrorInfo()).isEqualTo(DEFAULT_ERROR_INFO);
    }

    @Test
    @Transactional
    public void createBulkAutomationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bulkAutomationRepository.findAll().size();

        // Create the BulkAutomation with an existing ID
        bulkAutomation.setId(1L);
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(bulkAutomation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBulkAutomationMockMvc.perform(post("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BulkAutomation in the database
        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulkAutomationRepository.findAll().size();
        // set the field null
        bulkAutomation.setShop(null);

        // Create the BulkAutomation, which fails.
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(bulkAutomation);


        restBulkAutomationMockMvc.perform(post("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isBadRequest());

        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutomationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulkAutomationRepository.findAll().size();
        // set the field null
        bulkAutomation.setAutomationType(null);

        // Create the BulkAutomation, which fails.
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(bulkAutomation);


        restBulkAutomationMockMvc.perform(post("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isBadRequest());

        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBulkAutomations() throws Exception {
        // Initialize the database
        bulkAutomationRepository.saveAndFlush(bulkAutomation);

        // Get all the bulkAutomationList
        restBulkAutomationMockMvc.perform(get("/api/bulk-automations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulkAutomation.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].automationType").value(hasItem(DEFAULT_AUTOMATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].running").value(hasItem(DEFAULT_RUNNING.booleanValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].requestInfo").value(hasItem(DEFAULT_REQUEST_INFO.toString())))
            .andExpect(jsonPath("$.[*].errorInfo").value(hasItem(DEFAULT_ERROR_INFO.toString())));
    }
    
    @Test
    @Transactional
    public void getBulkAutomation() throws Exception {
        // Initialize the database
        bulkAutomationRepository.saveAndFlush(bulkAutomation);

        // Get the bulkAutomation
        restBulkAutomationMockMvc.perform(get("/api/bulk-automations/{id}", bulkAutomation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bulkAutomation.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.automationType").value(DEFAULT_AUTOMATION_TYPE.toString()))
            .andExpect(jsonPath("$.running").value(DEFAULT_RUNNING.booleanValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.requestInfo").value(DEFAULT_REQUEST_INFO.toString()))
            .andExpect(jsonPath("$.errorInfo").value(DEFAULT_ERROR_INFO.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBulkAutomation() throws Exception {
        // Get the bulkAutomation
        restBulkAutomationMockMvc.perform(get("/api/bulk-automations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBulkAutomation() throws Exception {
        // Initialize the database
        bulkAutomationRepository.saveAndFlush(bulkAutomation);

        int databaseSizeBeforeUpdate = bulkAutomationRepository.findAll().size();

        // Update the bulkAutomation
        BulkAutomation updatedBulkAutomation = bulkAutomationRepository.findById(bulkAutomation.getId()).get();
        // Disconnect from session so that the updates on updatedBulkAutomation are not directly saved in db
        em.detach(updatedBulkAutomation);
        updatedBulkAutomation
            .shop(UPDATED_SHOP)
            .automationType(UPDATED_AUTOMATION_TYPE)
            .running(UPDATED_RUNNING)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .requestInfo(UPDATED_REQUEST_INFO)
            .errorInfo(UPDATED_ERROR_INFO);
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(updatedBulkAutomation);

        restBulkAutomationMockMvc.perform(put("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isOk());

        // Validate the BulkAutomation in the database
        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeUpdate);
        BulkAutomation testBulkAutomation = bulkAutomationList.get(bulkAutomationList.size() - 1);
        assertThat(testBulkAutomation.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testBulkAutomation.getAutomationType()).isEqualTo(UPDATED_AUTOMATION_TYPE);
        assertThat(testBulkAutomation.isRunning()).isEqualTo(UPDATED_RUNNING);
        assertThat(testBulkAutomation.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testBulkAutomation.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testBulkAutomation.getRequestInfo()).isEqualTo(UPDATED_REQUEST_INFO);
        assertThat(testBulkAutomation.getErrorInfo()).isEqualTo(UPDATED_ERROR_INFO);
    }

    @Test
    @Transactional
    public void updateNonExistingBulkAutomation() throws Exception {
        int databaseSizeBeforeUpdate = bulkAutomationRepository.findAll().size();

        // Create the BulkAutomation
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationMapper.toDto(bulkAutomation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulkAutomationMockMvc.perform(put("/api/bulk-automations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bulkAutomationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BulkAutomation in the database
        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBulkAutomation() throws Exception {
        // Initialize the database
        bulkAutomationRepository.saveAndFlush(bulkAutomation);

        int databaseSizeBeforeDelete = bulkAutomationRepository.findAll().size();

        // Delete the bulkAutomation
        restBulkAutomationMockMvc.perform(delete("/api/bulk-automations/{id}", bulkAutomation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BulkAutomation> bulkAutomationList = bulkAutomationRepository.findAll();
        assertThat(bulkAutomationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
