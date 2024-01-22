package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.CancellationManagement;
import com.et.repository.CancellationManagementRepository;
import com.et.service.CancellationManagementService;
import com.et.service.dto.CancellationManagementDTO;
import com.et.service.mapper.CancellationManagementMapper;

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

import com.et.domain.enumeration.CancellationTypeStatus;
/**
 * Integration tests for the {@link CancellationManagementResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CancellationManagementResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final CancellationTypeStatus DEFAULT_CANCELLATION_TYPE = CancellationTypeStatus.CANCEL_IMMEDIATELY;
    private static final CancellationTypeStatus UPDATED_CANCELLATION_TYPE = CancellationTypeStatus.CANCELLATION_INSTRUCTIONS;

    private static final String DEFAULT_CANCELLATION_INSTRUCTIONS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCELLATION_INSTRUCTIONS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCELLATION_REASONS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CANCELLATION_REASONS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_PAUSE_INSTRUCTIONS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PAUSE_INSTRUCTIONS_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAUSE_DURATION_CYCLE = 1;
    private static final Integer UPDATED_PAUSE_DURATION_CYCLE = 2;

    private static final Boolean DEFAULT_ENABLE_DISCOUNT_EMAIL = false;
    private static final Boolean UPDATED_ENABLE_DISCOUNT_EMAIL = true;

    private static final String DEFAULT_DISCOUNT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DISCOUNT_EMAIL_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private CancellationManagementRepository cancellationManagementRepository;

    @Autowired
    private CancellationManagementMapper cancellationManagementMapper;

    @Autowired
    private CancellationManagementService cancellationManagementService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCancellationManagementMockMvc;

    private CancellationManagement cancellationManagement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CancellationManagement createEntity(EntityManager em) {
        CancellationManagement cancellationManagement = new CancellationManagement()
            .shop(DEFAULT_SHOP)
            .cancellationType(DEFAULT_CANCELLATION_TYPE)
            .cancellationInstructionsText(DEFAULT_CANCELLATION_INSTRUCTIONS_TEXT)
            .cancellationReasonsJSON(DEFAULT_CANCELLATION_REASONS_JSON)
            .pauseInstructionsText(DEFAULT_PAUSE_INSTRUCTIONS_TEXT)
            .pauseDurationCycle(DEFAULT_PAUSE_DURATION_CYCLE)
            .enableDiscountEmail(DEFAULT_ENABLE_DISCOUNT_EMAIL)
            .discountEmailAddress(DEFAULT_DISCOUNT_EMAIL_ADDRESS);
        return cancellationManagement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CancellationManagement createUpdatedEntity(EntityManager em) {
        CancellationManagement cancellationManagement = new CancellationManagement()
            .shop(UPDATED_SHOP)
            .cancellationType(UPDATED_CANCELLATION_TYPE)
            .cancellationInstructionsText(UPDATED_CANCELLATION_INSTRUCTIONS_TEXT)
            .cancellationReasonsJSON(UPDATED_CANCELLATION_REASONS_JSON)
            .pauseInstructionsText(UPDATED_PAUSE_INSTRUCTIONS_TEXT)
            .pauseDurationCycle(UPDATED_PAUSE_DURATION_CYCLE)
            .enableDiscountEmail(UPDATED_ENABLE_DISCOUNT_EMAIL)
            .discountEmailAddress(UPDATED_DISCOUNT_EMAIL_ADDRESS);
        return cancellationManagement;
    }

    @BeforeEach
    public void initTest() {
        cancellationManagement = createEntity(em);
    }

    @Test
    @Transactional
    public void createCancellationManagement() throws Exception {
        int databaseSizeBeforeCreate = cancellationManagementRepository.findAll().size();
        // Create the CancellationManagement
        CancellationManagementDTO cancellationManagementDTO = cancellationManagementMapper.toDto(cancellationManagement);
        restCancellationManagementMockMvc.perform(post("/api/cancellation-managements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cancellationManagementDTO)))
            .andExpect(status().isCreated());

        // Validate the CancellationManagement in the database
        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeCreate + 1);
        CancellationManagement testCancellationManagement = cancellationManagementList.get(cancellationManagementList.size() - 1);
        assertThat(testCancellationManagement.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testCancellationManagement.getCancellationType()).isEqualTo(DEFAULT_CANCELLATION_TYPE);
        assertThat(testCancellationManagement.getCancellationInstructionsText()).isEqualTo(DEFAULT_CANCELLATION_INSTRUCTIONS_TEXT);
        assertThat(testCancellationManagement.getCancellationReasonsJSON()).isEqualTo(DEFAULT_CANCELLATION_REASONS_JSON);
        assertThat(testCancellationManagement.getPauseInstructionsText()).isEqualTo(DEFAULT_PAUSE_INSTRUCTIONS_TEXT);
        assertThat(testCancellationManagement.getPauseDurationCycle()).isEqualTo(DEFAULT_PAUSE_DURATION_CYCLE);
        assertThat(testCancellationManagement.isEnableDiscountEmail()).isEqualTo(DEFAULT_ENABLE_DISCOUNT_EMAIL);
        assertThat(testCancellationManagement.getDiscountEmailAddress()).isEqualTo(DEFAULT_DISCOUNT_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void createCancellationManagementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cancellationManagementRepository.findAll().size();

        // Create the CancellationManagement with an existing ID
        cancellationManagement.setId(1L);
        CancellationManagementDTO cancellationManagementDTO = cancellationManagementMapper.toDto(cancellationManagement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCancellationManagementMockMvc.perform(post("/api/cancellation-managements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cancellationManagementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CancellationManagement in the database
        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = cancellationManagementRepository.findAll().size();
        // set the field null
        cancellationManagement.setShop(null);

        // Create the CancellationManagement, which fails.
        CancellationManagementDTO cancellationManagementDTO = cancellationManagementMapper.toDto(cancellationManagement);


        restCancellationManagementMockMvc.perform(post("/api/cancellation-managements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cancellationManagementDTO)))
            .andExpect(status().isBadRequest());

        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCancellationManagements() throws Exception {
        // Initialize the database
        cancellationManagementRepository.saveAndFlush(cancellationManagement);

        // Get all the cancellationManagementList
        restCancellationManagementMockMvc.perform(get("/api/cancellation-managements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancellationManagement.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].cancellationType").value(hasItem(DEFAULT_CANCELLATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cancellationInstructionsText").value(hasItem(DEFAULT_CANCELLATION_INSTRUCTIONS_TEXT.toString())))
            .andExpect(jsonPath("$.[*].cancellationReasonsJSON").value(hasItem(DEFAULT_CANCELLATION_REASONS_JSON.toString())))
            .andExpect(jsonPath("$.[*].pauseInstructionsText").value(hasItem(DEFAULT_PAUSE_INSTRUCTIONS_TEXT.toString())))
            .andExpect(jsonPath("$.[*].pauseDurationCycle").value(hasItem(DEFAULT_PAUSE_DURATION_CYCLE)))
            .andExpect(jsonPath("$.[*].enableDiscountEmail").value(hasItem(DEFAULT_ENABLE_DISCOUNT_EMAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].discountEmailAddress").value(hasItem(DEFAULT_DISCOUNT_EMAIL_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getCancellationManagement() throws Exception {
        // Initialize the database
        cancellationManagementRepository.saveAndFlush(cancellationManagement);

        // Get the cancellationManagement
        restCancellationManagementMockMvc.perform(get("/api/cancellation-managements/{id}", cancellationManagement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cancellationManagement.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.cancellationType").value(DEFAULT_CANCELLATION_TYPE.toString()))
            .andExpect(jsonPath("$.cancellationInstructionsText").value(DEFAULT_CANCELLATION_INSTRUCTIONS_TEXT.toString()))
            .andExpect(jsonPath("$.cancellationReasonsJSON").value(DEFAULT_CANCELLATION_REASONS_JSON.toString()))
            .andExpect(jsonPath("$.pauseInstructionsText").value(DEFAULT_PAUSE_INSTRUCTIONS_TEXT.toString()))
            .andExpect(jsonPath("$.pauseDurationCycle").value(DEFAULT_PAUSE_DURATION_CYCLE))
            .andExpect(jsonPath("$.enableDiscountEmail").value(DEFAULT_ENABLE_DISCOUNT_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.discountEmailAddress").value(DEFAULT_DISCOUNT_EMAIL_ADDRESS));
    }
    @Test
    @Transactional
    public void getNonExistingCancellationManagement() throws Exception {
        // Get the cancellationManagement
        restCancellationManagementMockMvc.perform(get("/api/cancellation-managements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCancellationManagement() throws Exception {
        // Initialize the database
        cancellationManagementRepository.saveAndFlush(cancellationManagement);

        int databaseSizeBeforeUpdate = cancellationManagementRepository.findAll().size();

        // Update the cancellationManagement
        CancellationManagement updatedCancellationManagement = cancellationManagementRepository.findById(cancellationManagement.getId()).get();
        // Disconnect from session so that the updates on updatedCancellationManagement are not directly saved in db
        em.detach(updatedCancellationManagement);
        updatedCancellationManagement
            .shop(UPDATED_SHOP)
            .cancellationType(UPDATED_CANCELLATION_TYPE)
            .cancellationInstructionsText(UPDATED_CANCELLATION_INSTRUCTIONS_TEXT)
            .cancellationReasonsJSON(UPDATED_CANCELLATION_REASONS_JSON)
            .pauseInstructionsText(UPDATED_PAUSE_INSTRUCTIONS_TEXT)
            .pauseDurationCycle(UPDATED_PAUSE_DURATION_CYCLE)
            .enableDiscountEmail(UPDATED_ENABLE_DISCOUNT_EMAIL)
            .discountEmailAddress(UPDATED_DISCOUNT_EMAIL_ADDRESS);
        CancellationManagementDTO cancellationManagementDTO = cancellationManagementMapper.toDto(updatedCancellationManagement);

        restCancellationManagementMockMvc.perform(put("/api/cancellation-managements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cancellationManagementDTO)))
            .andExpect(status().isOk());

        // Validate the CancellationManagement in the database
        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeUpdate);
        CancellationManagement testCancellationManagement = cancellationManagementList.get(cancellationManagementList.size() - 1);
        assertThat(testCancellationManagement.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testCancellationManagement.getCancellationType()).isEqualTo(UPDATED_CANCELLATION_TYPE);
        assertThat(testCancellationManagement.getCancellationInstructionsText()).isEqualTo(UPDATED_CANCELLATION_INSTRUCTIONS_TEXT);
        assertThat(testCancellationManagement.getCancellationReasonsJSON()).isEqualTo(UPDATED_CANCELLATION_REASONS_JSON);
        assertThat(testCancellationManagement.getPauseInstructionsText()).isEqualTo(UPDATED_PAUSE_INSTRUCTIONS_TEXT);
        assertThat(testCancellationManagement.getPauseDurationCycle()).isEqualTo(UPDATED_PAUSE_DURATION_CYCLE);
        assertThat(testCancellationManagement.isEnableDiscountEmail()).isEqualTo(UPDATED_ENABLE_DISCOUNT_EMAIL);
        assertThat(testCancellationManagement.getDiscountEmailAddress()).isEqualTo(UPDATED_DISCOUNT_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingCancellationManagement() throws Exception {
        int databaseSizeBeforeUpdate = cancellationManagementRepository.findAll().size();

        // Create the CancellationManagement
        CancellationManagementDTO cancellationManagementDTO = cancellationManagementMapper.toDto(cancellationManagement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancellationManagementMockMvc.perform(put("/api/cancellation-managements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cancellationManagementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CancellationManagement in the database
        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCancellationManagement() throws Exception {
        // Initialize the database
        cancellationManagementRepository.saveAndFlush(cancellationManagement);

        int databaseSizeBeforeDelete = cancellationManagementRepository.findAll().size();

        // Delete the cancellationManagement
        restCancellationManagementMockMvc.perform(delete("/api/cancellation-managements/{id}", cancellationManagement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CancellationManagement> cancellationManagementList = cancellationManagementRepository.findAll();
        assertThat(cancellationManagementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
