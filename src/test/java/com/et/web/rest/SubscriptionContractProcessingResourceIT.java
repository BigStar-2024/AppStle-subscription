package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionContractProcessing;
import com.et.repository.SubscriptionContractProcessingRepository;
import com.et.service.SubscriptionContractProcessingService;
import com.et.service.dto.SubscriptionContractProcessingDTO;
import com.et.service.mapper.SubscriptionContractProcessingMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubscriptionContractProcessingResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubscriptionContractProcessingResourceIT {

    private static final Long DEFAULT_CONTRACT_ID = 1L;
    private static final Long UPDATED_CONTRACT_ID = 2L;

    private static final Integer DEFAULT_ATTEMPT_COUNT = 1;
    private static final Integer UPDATED_ATTEMPT_COUNT = 2;

    @Autowired
    private SubscriptionContractProcessingRepository subscriptionContractProcessingRepository;

    @Autowired
    private SubscriptionContractProcessingMapper subscriptionContractProcessingMapper;

    @Autowired
    private SubscriptionContractProcessingService subscriptionContractProcessingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionContractProcessingMockMvc;

    private SubscriptionContractProcessing subscriptionContractProcessing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractProcessing createEntity(EntityManager em) {
        SubscriptionContractProcessing subscriptionContractProcessing = new SubscriptionContractProcessing()
            .contractId(DEFAULT_CONTRACT_ID)
            .attemptCount(DEFAULT_ATTEMPT_COUNT);
        return subscriptionContractProcessing;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractProcessing createUpdatedEntity(EntityManager em) {
        SubscriptionContractProcessing subscriptionContractProcessing = new SubscriptionContractProcessing()
            .contractId(UPDATED_CONTRACT_ID)
            .attemptCount(UPDATED_ATTEMPT_COUNT);
        return subscriptionContractProcessing;
    }

    @BeforeEach
    public void initTest() {
        subscriptionContractProcessing = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionContractProcessing() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractProcessingRepository.findAll().size();
        // Create the SubscriptionContractProcessing
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO = subscriptionContractProcessingMapper.toDto(subscriptionContractProcessing);
        restSubscriptionContractProcessingMockMvc.perform(post("/api/subscription-contract-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractProcessingDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionContractProcessing in the database
        List<SubscriptionContractProcessing> subscriptionContractProcessingList = subscriptionContractProcessingRepository.findAll();
        assertThat(subscriptionContractProcessingList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionContractProcessing testSubscriptionContractProcessing = subscriptionContractProcessingList.get(subscriptionContractProcessingList.size() - 1);
        assertThat(testSubscriptionContractProcessing.getContractId()).isEqualTo(DEFAULT_CONTRACT_ID);
        assertThat(testSubscriptionContractProcessing.getAttemptCount()).isEqualTo(DEFAULT_ATTEMPT_COUNT);
    }

    @Test
    @Transactional
    public void createSubscriptionContractProcessingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractProcessingRepository.findAll().size();

        // Create the SubscriptionContractProcessing with an existing ID
        subscriptionContractProcessing.setId(1L);
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO = subscriptionContractProcessingMapper.toDto(subscriptionContractProcessing);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionContractProcessingMockMvc.perform(post("/api/subscription-contract-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractProcessingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractProcessing in the database
        List<SubscriptionContractProcessing> subscriptionContractProcessingList = subscriptionContractProcessingRepository.findAll();
        assertThat(subscriptionContractProcessingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSubscriptionContractProcessings() throws Exception {
        // Initialize the database
        subscriptionContractProcessingRepository.saveAndFlush(subscriptionContractProcessing);

        // Get all the subscriptionContractProcessingList
        restSubscriptionContractProcessingMockMvc.perform(get("/api/subscription-contract-processings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionContractProcessing.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractId").value(hasItem(DEFAULT_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].attemptCount").value(hasItem(DEFAULT_ATTEMPT_COUNT)));
    }
    
    @Test
    @Transactional
    public void getSubscriptionContractProcessing() throws Exception {
        // Initialize the database
        subscriptionContractProcessingRepository.saveAndFlush(subscriptionContractProcessing);

        // Get the subscriptionContractProcessing
        restSubscriptionContractProcessingMockMvc.perform(get("/api/subscription-contract-processings/{id}", subscriptionContractProcessing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionContractProcessing.getId().intValue()))
            .andExpect(jsonPath("$.contractId").value(DEFAULT_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.attemptCount").value(DEFAULT_ATTEMPT_COUNT));
    }
    @Test
    @Transactional
    public void getNonExistingSubscriptionContractProcessing() throws Exception {
        // Get the subscriptionContractProcessing
        restSubscriptionContractProcessingMockMvc.perform(get("/api/subscription-contract-processings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionContractProcessing() throws Exception {
        // Initialize the database
        subscriptionContractProcessingRepository.saveAndFlush(subscriptionContractProcessing);

        int databaseSizeBeforeUpdate = subscriptionContractProcessingRepository.findAll().size();

        // Update the subscriptionContractProcessing
        SubscriptionContractProcessing updatedSubscriptionContractProcessing = subscriptionContractProcessingRepository.findById(subscriptionContractProcessing.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionContractProcessing are not directly saved in db
        em.detach(updatedSubscriptionContractProcessing);
        updatedSubscriptionContractProcessing
            .contractId(UPDATED_CONTRACT_ID)
            .attemptCount(UPDATED_ATTEMPT_COUNT);
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO = subscriptionContractProcessingMapper.toDto(updatedSubscriptionContractProcessing);

        restSubscriptionContractProcessingMockMvc.perform(put("/api/subscription-contract-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractProcessingDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionContractProcessing in the database
        List<SubscriptionContractProcessing> subscriptionContractProcessingList = subscriptionContractProcessingRepository.findAll();
        assertThat(subscriptionContractProcessingList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionContractProcessing testSubscriptionContractProcessing = subscriptionContractProcessingList.get(subscriptionContractProcessingList.size() - 1);
        assertThat(testSubscriptionContractProcessing.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testSubscriptionContractProcessing.getAttemptCount()).isEqualTo(UPDATED_ATTEMPT_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionContractProcessing() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionContractProcessingRepository.findAll().size();

        // Create the SubscriptionContractProcessing
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO = subscriptionContractProcessingMapper.toDto(subscriptionContractProcessing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionContractProcessingMockMvc.perform(put("/api/subscription-contract-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractProcessingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractProcessing in the database
        List<SubscriptionContractProcessing> subscriptionContractProcessingList = subscriptionContractProcessingRepository.findAll();
        assertThat(subscriptionContractProcessingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionContractProcessing() throws Exception {
        // Initialize the database
        subscriptionContractProcessingRepository.saveAndFlush(subscriptionContractProcessing);

        int databaseSizeBeforeDelete = subscriptionContractProcessingRepository.findAll().size();

        // Delete the subscriptionContractProcessing
        restSubscriptionContractProcessingMockMvc.perform(delete("/api/subscription-contract-processings/{id}", subscriptionContractProcessing.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionContractProcessing> subscriptionContractProcessingList = subscriptionContractProcessingRepository.findAll();
        assertThat(subscriptionContractProcessingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
