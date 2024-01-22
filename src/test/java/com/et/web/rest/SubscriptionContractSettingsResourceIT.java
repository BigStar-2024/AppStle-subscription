package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionContractSettings;
import com.et.repository.SubscriptionContractSettingsRepository;
import com.et.service.SubscriptionContractSettingsService;
import com.et.service.dto.SubscriptionContractSettingsDTO;
import com.et.service.mapper.SubscriptionContractSettingsMapper;

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

import com.et.domain.enumeration.FrequencyIntervalUnit;
/**
 * Integration tests for the {@link SubscriptionContractSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubscriptionContractSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENDS_ON_COUNT = 1;
    private static final Integer UPDATED_ENDS_ON_COUNT = 2;

    private static final FrequencyIntervalUnit DEFAULT_ENDS_ON_INTERVAL = FrequencyIntervalUnit.DAY;
    private static final FrequencyIntervalUnit UPDATED_ENDS_ON_INTERVAL = FrequencyIntervalUnit.WEEK;

    @Autowired
    private SubscriptionContractSettingsRepository subscriptionContractSettingsRepository;

    @Autowired
    private SubscriptionContractSettingsMapper subscriptionContractSettingsMapper;

    @Autowired
    private SubscriptionContractSettingsService subscriptionContractSettingsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionContractSettingsMockMvc;

    private SubscriptionContractSettings subscriptionContractSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractSettings createEntity(EntityManager em) {
        SubscriptionContractSettings subscriptionContractSettings = new SubscriptionContractSettings()
            .shop(DEFAULT_SHOP)
            .productId(DEFAULT_PRODUCT_ID)
            .endsOnCount(DEFAULT_ENDS_ON_COUNT)
            .endsOnInterval(DEFAULT_ENDS_ON_INTERVAL);
        return subscriptionContractSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractSettings createUpdatedEntity(EntityManager em) {
        SubscriptionContractSettings subscriptionContractSettings = new SubscriptionContractSettings()
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .endsOnCount(UPDATED_ENDS_ON_COUNT)
            .endsOnInterval(UPDATED_ENDS_ON_INTERVAL);
        return subscriptionContractSettings;
    }

    @BeforeEach
    public void initTest() {
        subscriptionContractSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionContractSettings() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractSettingsRepository.findAll().size();
        // Create the SubscriptionContractSettings
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);
        restSubscriptionContractSettingsMockMvc.perform(post("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionContractSettings in the database
        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionContractSettings testSubscriptionContractSettings = subscriptionContractSettingsList.get(subscriptionContractSettingsList.size() - 1);
        assertThat(testSubscriptionContractSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSubscriptionContractSettings.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testSubscriptionContractSettings.getEndsOnCount()).isEqualTo(DEFAULT_ENDS_ON_COUNT);
        assertThat(testSubscriptionContractSettings.getEndsOnInterval()).isEqualTo(DEFAULT_ENDS_ON_INTERVAL);
    }

    @Test
    @Transactional
    public void createSubscriptionContractSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractSettingsRepository.findAll().size();

        // Create the SubscriptionContractSettings with an existing ID
        subscriptionContractSettings.setId(1L);
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionContractSettingsMockMvc.perform(post("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractSettings in the database
        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionContractSettingsRepository.findAll().size();
        // set the field null
        subscriptionContractSettings.setShop(null);

        // Create the SubscriptionContractSettings, which fails.
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);


        restSubscriptionContractSettingsMockMvc.perform(post("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndsOnCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionContractSettingsRepository.findAll().size();
        // set the field null
        subscriptionContractSettings.setEndsOnCount(null);

        // Create the SubscriptionContractSettings, which fails.
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);


        restSubscriptionContractSettingsMockMvc.perform(post("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionContractSettings() throws Exception {
        // Initialize the database
        subscriptionContractSettingsRepository.saveAndFlush(subscriptionContractSettings);

        // Get all the subscriptionContractSettingsList
        restSubscriptionContractSettingsMockMvc.perform(get("/api/subscription-contract-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionContractSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].endsOnCount").value(hasItem(DEFAULT_ENDS_ON_COUNT)))
            .andExpect(jsonPath("$.[*].endsOnInterval").value(hasItem(DEFAULT_ENDS_ON_INTERVAL.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionContractSettings() throws Exception {
        // Initialize the database
        subscriptionContractSettingsRepository.saveAndFlush(subscriptionContractSettings);

        // Get the subscriptionContractSettings
        restSubscriptionContractSettingsMockMvc.perform(get("/api/subscription-contract-settings/{id}", subscriptionContractSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionContractSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.endsOnCount").value(DEFAULT_ENDS_ON_COUNT))
            .andExpect(jsonPath("$.endsOnInterval").value(DEFAULT_ENDS_ON_INTERVAL.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSubscriptionContractSettings() throws Exception {
        // Get the subscriptionContractSettings
        restSubscriptionContractSettingsMockMvc.perform(get("/api/subscription-contract-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionContractSettings() throws Exception {
        // Initialize the database
        subscriptionContractSettingsRepository.saveAndFlush(subscriptionContractSettings);

        int databaseSizeBeforeUpdate = subscriptionContractSettingsRepository.findAll().size();

        // Update the subscriptionContractSettings
        SubscriptionContractSettings updatedSubscriptionContractSettings = subscriptionContractSettingsRepository.findById(subscriptionContractSettings.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionContractSettings are not directly saved in db
        em.detach(updatedSubscriptionContractSettings);
        updatedSubscriptionContractSettings
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .endsOnCount(UPDATED_ENDS_ON_COUNT)
            .endsOnInterval(UPDATED_ENDS_ON_INTERVAL);
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(updatedSubscriptionContractSettings);

        restSubscriptionContractSettingsMockMvc.perform(put("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionContractSettings in the database
        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionContractSettings testSubscriptionContractSettings = subscriptionContractSettingsList.get(subscriptionContractSettingsList.size() - 1);
        assertThat(testSubscriptionContractSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSubscriptionContractSettings.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testSubscriptionContractSettings.getEndsOnCount()).isEqualTo(UPDATED_ENDS_ON_COUNT);
        assertThat(testSubscriptionContractSettings.getEndsOnInterval()).isEqualTo(UPDATED_ENDS_ON_INTERVAL);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionContractSettings() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionContractSettingsRepository.findAll().size();

        // Create the SubscriptionContractSettings
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO = subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionContractSettingsMockMvc.perform(put("/api/subscription-contract-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractSettings in the database
        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionContractSettings() throws Exception {
        // Initialize the database
        subscriptionContractSettingsRepository.saveAndFlush(subscriptionContractSettings);

        int databaseSizeBeforeDelete = subscriptionContractSettingsRepository.findAll().size();

        // Delete the subscriptionContractSettings
        restSubscriptionContractSettingsMockMvc.perform(delete("/api/subscription-contract-settings/{id}", subscriptionContractSettings.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionContractSettings> subscriptionContractSettingsList = subscriptionContractSettingsRepository.findAll();
        assertThat(subscriptionContractSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
