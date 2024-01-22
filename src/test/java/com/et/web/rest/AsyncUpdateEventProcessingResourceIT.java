package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.AsyncUpdateEventProcessing;
import com.et.repository.AsyncUpdateEventProcessingRepository;
import com.et.service.AsyncUpdateEventProcessingService;
import com.et.service.dto.AsyncUpdateEventProcessingDTO;
import com.et.service.mapper.AsyncUpdateEventProcessingMapper;

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

/**
 * Integration tests for the {@link AsyncUpdateEventProcessingResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AsyncUpdateEventProcessingResourceIT {

    private static final Long DEFAULT_SUBSCRIPTION_CONTRACT_ID = 1L;
    private static final Long UPDATED_SUBSCRIPTION_CONTRACT_ID = 2L;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TAG_MODEL_JSON = "AAAAAAAAAA";
    private static final String UPDATED_TAG_MODEL_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_TIME_ORDER_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_TIME_ORDER_TAGS = "BBBBBBBBBB";

    @Autowired
    private AsyncUpdateEventProcessingRepository asyncUpdateEventProcessingRepository;

    @Autowired
    private AsyncUpdateEventProcessingMapper asyncUpdateEventProcessingMapper;

    @Autowired
    private AsyncUpdateEventProcessingService asyncUpdateEventProcessingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAsyncUpdateEventProcessingMockMvc;

    private AsyncUpdateEventProcessing asyncUpdateEventProcessing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsyncUpdateEventProcessing createEntity(EntityManager em) {
        AsyncUpdateEventProcessing asyncUpdateEventProcessing = new AsyncUpdateEventProcessing()
            .subscriptionContractId(DEFAULT_SUBSCRIPTION_CONTRACT_ID)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .tagModelJson(DEFAULT_TAG_MODEL_JSON)
            .firstTimeOrderTags(DEFAULT_FIRST_TIME_ORDER_TAGS);
        return asyncUpdateEventProcessing;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsyncUpdateEventProcessing createUpdatedEntity(EntityManager em) {
        AsyncUpdateEventProcessing asyncUpdateEventProcessing = new AsyncUpdateEventProcessing()
            .subscriptionContractId(UPDATED_SUBSCRIPTION_CONTRACT_ID)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .tagModelJson(UPDATED_TAG_MODEL_JSON)
            .firstTimeOrderTags(UPDATED_FIRST_TIME_ORDER_TAGS);
        return asyncUpdateEventProcessing;
    }

    @BeforeEach
    public void initTest() {
        asyncUpdateEventProcessing = createEntity(em);
    }

    @Test
    @Transactional
    public void createAsyncUpdateEventProcessing() throws Exception {
        int databaseSizeBeforeCreate = asyncUpdateEventProcessingRepository.findAll().size();
        // Create the AsyncUpdateEventProcessing
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingMapper.toDto(asyncUpdateEventProcessing);
        restAsyncUpdateEventProcessingMockMvc.perform(post("/api/async-update-event-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asyncUpdateEventProcessingDTO)))
            .andExpect(status().isCreated());

        // Validate the AsyncUpdateEventProcessing in the database
        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeCreate + 1);
        AsyncUpdateEventProcessing testAsyncUpdateEventProcessing = asyncUpdateEventProcessingList.get(asyncUpdateEventProcessingList.size() - 1);
        assertThat(testAsyncUpdateEventProcessing.getSubscriptionContractId()).isEqualTo(DEFAULT_SUBSCRIPTION_CONTRACT_ID);
        assertThat(testAsyncUpdateEventProcessing.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testAsyncUpdateEventProcessing.getTagModelJson()).isEqualTo(DEFAULT_TAG_MODEL_JSON);
        assertThat(testAsyncUpdateEventProcessing.getFirstTimeOrderTags()).isEqualTo(DEFAULT_FIRST_TIME_ORDER_TAGS);
    }

    @Test
    @Transactional
    public void createAsyncUpdateEventProcessingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = asyncUpdateEventProcessingRepository.findAll().size();

        // Create the AsyncUpdateEventProcessing with an existing ID
        asyncUpdateEventProcessing.setId(1L);
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingMapper.toDto(asyncUpdateEventProcessing);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAsyncUpdateEventProcessingMockMvc.perform(post("/api/async-update-event-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asyncUpdateEventProcessingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AsyncUpdateEventProcessing in the database
        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSubscriptionContractIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = asyncUpdateEventProcessingRepository.findAll().size();
        // set the field null
        asyncUpdateEventProcessing.setSubscriptionContractId(null);

        // Create the AsyncUpdateEventProcessing, which fails.
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingMapper.toDto(asyncUpdateEventProcessing);


        restAsyncUpdateEventProcessingMockMvc.perform(post("/api/async-update-event-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asyncUpdateEventProcessingDTO)))
            .andExpect(status().isBadRequest());

        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAsyncUpdateEventProcessings() throws Exception {
        // Initialize the database
        asyncUpdateEventProcessingRepository.saveAndFlush(asyncUpdateEventProcessing);

        // Get all the asyncUpdateEventProcessingList
        restAsyncUpdateEventProcessingMockMvc.perform(get("/api/async-update-event-processings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asyncUpdateEventProcessing.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionContractId").value(hasItem(DEFAULT_SUBSCRIPTION_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))))
            .andExpect(jsonPath("$.[*].tagModelJson").value(hasItem(DEFAULT_TAG_MODEL_JSON.toString())))
            .andExpect(jsonPath("$.[*].firstTimeOrderTags").value(hasItem(DEFAULT_FIRST_TIME_ORDER_TAGS)));
    }
    
    @Test
    @Transactional
    public void getAsyncUpdateEventProcessing() throws Exception {
        // Initialize the database
        asyncUpdateEventProcessingRepository.saveAndFlush(asyncUpdateEventProcessing);

        // Get the asyncUpdateEventProcessing
        restAsyncUpdateEventProcessingMockMvc.perform(get("/api/async-update-event-processings/{id}", asyncUpdateEventProcessing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asyncUpdateEventProcessing.getId().intValue()))
            .andExpect(jsonPath("$.subscriptionContractId").value(DEFAULT_SUBSCRIPTION_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.lastUpdated").value(sameInstant(DEFAULT_LAST_UPDATED)))
            .andExpect(jsonPath("$.tagModelJson").value(DEFAULT_TAG_MODEL_JSON.toString()))
            .andExpect(jsonPath("$.firstTimeOrderTags").value(DEFAULT_FIRST_TIME_ORDER_TAGS));
    }
    @Test
    @Transactional
    public void getNonExistingAsyncUpdateEventProcessing() throws Exception {
        // Get the asyncUpdateEventProcessing
        restAsyncUpdateEventProcessingMockMvc.perform(get("/api/async-update-event-processings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsyncUpdateEventProcessing() throws Exception {
        // Initialize the database
        asyncUpdateEventProcessingRepository.saveAndFlush(asyncUpdateEventProcessing);

        int databaseSizeBeforeUpdate = asyncUpdateEventProcessingRepository.findAll().size();

        // Update the asyncUpdateEventProcessing
        AsyncUpdateEventProcessing updatedAsyncUpdateEventProcessing = asyncUpdateEventProcessingRepository.findById(asyncUpdateEventProcessing.getId()).get();
        // Disconnect from session so that the updates on updatedAsyncUpdateEventProcessing are not directly saved in db
        em.detach(updatedAsyncUpdateEventProcessing);
        updatedAsyncUpdateEventProcessing
            .subscriptionContractId(UPDATED_SUBSCRIPTION_CONTRACT_ID)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .tagModelJson(UPDATED_TAG_MODEL_JSON)
            .firstTimeOrderTags(UPDATED_FIRST_TIME_ORDER_TAGS);
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingMapper.toDto(updatedAsyncUpdateEventProcessing);

        restAsyncUpdateEventProcessingMockMvc.perform(put("/api/async-update-event-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asyncUpdateEventProcessingDTO)))
            .andExpect(status().isOk());

        // Validate the AsyncUpdateEventProcessing in the database
        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeUpdate);
        AsyncUpdateEventProcessing testAsyncUpdateEventProcessing = asyncUpdateEventProcessingList.get(asyncUpdateEventProcessingList.size() - 1);
        assertThat(testAsyncUpdateEventProcessing.getSubscriptionContractId()).isEqualTo(UPDATED_SUBSCRIPTION_CONTRACT_ID);
        assertThat(testAsyncUpdateEventProcessing.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testAsyncUpdateEventProcessing.getTagModelJson()).isEqualTo(UPDATED_TAG_MODEL_JSON);
        assertThat(testAsyncUpdateEventProcessing.getFirstTimeOrderTags()).isEqualTo(UPDATED_FIRST_TIME_ORDER_TAGS);
    }

    @Test
    @Transactional
    public void updateNonExistingAsyncUpdateEventProcessing() throws Exception {
        int databaseSizeBeforeUpdate = asyncUpdateEventProcessingRepository.findAll().size();

        // Create the AsyncUpdateEventProcessing
        AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingMapper.toDto(asyncUpdateEventProcessing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsyncUpdateEventProcessingMockMvc.perform(put("/api/async-update-event-processings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asyncUpdateEventProcessingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AsyncUpdateEventProcessing in the database
        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAsyncUpdateEventProcessing() throws Exception {
        // Initialize the database
        asyncUpdateEventProcessingRepository.saveAndFlush(asyncUpdateEventProcessing);

        int databaseSizeBeforeDelete = asyncUpdateEventProcessingRepository.findAll().size();

        // Delete the asyncUpdateEventProcessing
        restAsyncUpdateEventProcessingMockMvc.perform(delete("/api/async-update-event-processings/{id}", asyncUpdateEventProcessing.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AsyncUpdateEventProcessing> asyncUpdateEventProcessingList = asyncUpdateEventProcessingRepository.findAll();
        assertThat(asyncUpdateEventProcessingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
