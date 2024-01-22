package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionGroupPlan;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.service.mapper.SubscriptionGroupPlanMapper;

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

/**
 * Integration tests for the {@link SubscriptionGroupPlanResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubscriptionGroupPlanResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SUBSCRIPTION_ID = 1L;
    private static final Long UPDATED_SUBSCRIPTION_ID = 2L;

    private static final Integer DEFAULT_PRODUCT_COUNT = 1;
    private static final Integer UPDATED_PRODUCT_COUNT = 2;

    private static final Integer DEFAULT_PRODUCT_VARIANT_COUNT = 1;
    private static final Integer UPDATED_PRODUCT_VARIANT_COUNT = 2;

    private static final String DEFAULT_INFO_JSON = "AAAAAAAAAA";
    private static final String UPDATED_INFO_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_IDS = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_IDS = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESSORY_PRODUCT_IDS = "AAAAAAAAAA";
    private static final String UPDATED_ACCESSORY_PRODUCT_IDS = "BBBBBBBBBB";

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    @Autowired
    private SubscriptionGroupPlanMapper subscriptionGroupPlanMapper;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionGroupPlanMockMvc;

    private SubscriptionGroupPlan subscriptionGroupPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionGroupPlan createEntity(EntityManager em) {
        SubscriptionGroupPlan subscriptionGroupPlan = new SubscriptionGroupPlan()
            .shop(DEFAULT_SHOP)
            .groupName(DEFAULT_GROUP_NAME)
            .subscriptionId(DEFAULT_SUBSCRIPTION_ID)
            .productCount(DEFAULT_PRODUCT_COUNT)
            .productVariantCount(DEFAULT_PRODUCT_VARIANT_COUNT)
            .infoJson(DEFAULT_INFO_JSON)
            .productIds(DEFAULT_PRODUCT_IDS);
        return subscriptionGroupPlan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionGroupPlan createUpdatedEntity(EntityManager em) {
        SubscriptionGroupPlan subscriptionGroupPlan = new SubscriptionGroupPlan()
            .shop(UPDATED_SHOP)
            .groupName(UPDATED_GROUP_NAME)
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .productCount(UPDATED_PRODUCT_COUNT)
            .productVariantCount(UPDATED_PRODUCT_VARIANT_COUNT)
            .infoJson(UPDATED_INFO_JSON)
            .productIds(UPDATED_PRODUCT_IDS);
        return subscriptionGroupPlan;
    }

    @BeforeEach
    public void initTest() {
        subscriptionGroupPlan = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionGroupPlan() throws Exception {
        int databaseSizeBeforeCreate = subscriptionGroupPlanRepository.findAll().size();
        // Create the SubscriptionGroupPlan
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanMapper.toDto(subscriptionGroupPlan);
        restSubscriptionGroupPlanMockMvc.perform(post("/api/subscription-group-plans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionGroupPlanDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionGroupPlan in the database
        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionGroupPlan testSubscriptionGroupPlan = subscriptionGroupPlanList.get(subscriptionGroupPlanList.size() - 1);
        assertThat(testSubscriptionGroupPlan.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSubscriptionGroupPlan.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testSubscriptionGroupPlan.getSubscriptionId()).isEqualTo(DEFAULT_SUBSCRIPTION_ID);
        assertThat(testSubscriptionGroupPlan.getProductCount()).isEqualTo(DEFAULT_PRODUCT_COUNT);
        assertThat(testSubscriptionGroupPlan.getProductVariantCount()).isEqualTo(DEFAULT_PRODUCT_VARIANT_COUNT);
        assertThat(testSubscriptionGroupPlan.getInfoJson()).isEqualTo(DEFAULT_INFO_JSON);
        assertThat(testSubscriptionGroupPlan.getProductIds()).isEqualTo(DEFAULT_PRODUCT_IDS);
    }

    @Test
    @Transactional
    public void createSubscriptionGroupPlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionGroupPlanRepository.findAll().size();

        // Create the SubscriptionGroupPlan with an existing ID
        subscriptionGroupPlan.setId(1L);
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanMapper.toDto(subscriptionGroupPlan);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionGroupPlanMockMvc.perform(post("/api/subscription-group-plans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionGroupPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionGroupPlan in the database
        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionGroupPlanRepository.findAll().size();
        // set the field null
        subscriptionGroupPlan.setShop(null);

        // Create the SubscriptionGroupPlan, which fails.
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanMapper.toDto(subscriptionGroupPlan);


        restSubscriptionGroupPlanMockMvc.perform(post("/api/subscription-group-plans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionGroupPlanDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionGroupPlans() throws Exception {
        // Initialize the database
        subscriptionGroupPlanRepository.saveAndFlush(subscriptionGroupPlan);

        // Get all the subscriptionGroupPlanList
        restSubscriptionGroupPlanMockMvc.perform(get("/api/subscription-group-plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionGroupPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].subscriptionId").value(hasItem(DEFAULT_SUBSCRIPTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].productCount").value(hasItem(DEFAULT_PRODUCT_COUNT)))
            .andExpect(jsonPath("$.[*].productVariantCount").value(hasItem(DEFAULT_PRODUCT_VARIANT_COUNT)))
            .andExpect(jsonPath("$.[*].infoJson").value(hasItem(DEFAULT_INFO_JSON.toString())))
            .andExpect(jsonPath("$.[*].productIds").value(hasItem(DEFAULT_PRODUCT_IDS.toString())))
            .andExpect(jsonPath("$.[*].accessoryProductIds").value(hasItem(DEFAULT_ACCESSORY_PRODUCT_IDS.toString())));
    }

    @Test
    @Transactional
    public void getSubscriptionGroupPlan() throws Exception {
        // Initialize the database
        subscriptionGroupPlanRepository.saveAndFlush(subscriptionGroupPlan);

        // Get the subscriptionGroupPlan
        restSubscriptionGroupPlanMockMvc.perform(get("/api/subscription-group-plans/{id}", subscriptionGroupPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionGroupPlan.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.subscriptionId").value(DEFAULT_SUBSCRIPTION_ID.intValue()))
            .andExpect(jsonPath("$.productCount").value(DEFAULT_PRODUCT_COUNT))
            .andExpect(jsonPath("$.productVariantCount").value(DEFAULT_PRODUCT_VARIANT_COUNT))
            .andExpect(jsonPath("$.infoJson").value(DEFAULT_INFO_JSON.toString()))
            .andExpect(jsonPath("$.productIds").value(DEFAULT_PRODUCT_IDS.toString()))
            .andExpect(jsonPath("$.accessoryProductIds").value(DEFAULT_ACCESSORY_PRODUCT_IDS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSubscriptionGroupPlan() throws Exception {
        // Get the subscriptionGroupPlan
        restSubscriptionGroupPlanMockMvc.perform(get("/api/subscription-group-plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionGroupPlan() throws Exception {
        // Initialize the database
        subscriptionGroupPlanRepository.saveAndFlush(subscriptionGroupPlan);

        int databaseSizeBeforeUpdate = subscriptionGroupPlanRepository.findAll().size();

        // Update the subscriptionGroupPlan
        SubscriptionGroupPlan updatedSubscriptionGroupPlan = subscriptionGroupPlanRepository.findById(subscriptionGroupPlan.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionGroupPlan are not directly saved in db
        em.detach(updatedSubscriptionGroupPlan);
        updatedSubscriptionGroupPlan
            .shop(UPDATED_SHOP)
            .groupName(UPDATED_GROUP_NAME)
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .productCount(UPDATED_PRODUCT_COUNT)
            .productVariantCount(UPDATED_PRODUCT_VARIANT_COUNT)
            .infoJson(UPDATED_INFO_JSON)
            .productIds(UPDATED_PRODUCT_IDS);
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanMapper.toDto(updatedSubscriptionGroupPlan);

        restSubscriptionGroupPlanMockMvc.perform(put("/api/subscription-group-plans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionGroupPlanDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionGroupPlan in the database
        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionGroupPlan testSubscriptionGroupPlan = subscriptionGroupPlanList.get(subscriptionGroupPlanList.size() - 1);
        assertThat(testSubscriptionGroupPlan.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSubscriptionGroupPlan.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testSubscriptionGroupPlan.getSubscriptionId()).isEqualTo(UPDATED_SUBSCRIPTION_ID);
        assertThat(testSubscriptionGroupPlan.getProductCount()).isEqualTo(UPDATED_PRODUCT_COUNT);
        assertThat(testSubscriptionGroupPlan.getProductVariantCount()).isEqualTo(UPDATED_PRODUCT_VARIANT_COUNT);
        assertThat(testSubscriptionGroupPlan.getInfoJson()).isEqualTo(UPDATED_INFO_JSON);
        assertThat(testSubscriptionGroupPlan.getProductIds()).isEqualTo(UPDATED_PRODUCT_IDS);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionGroupPlan() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionGroupPlanRepository.findAll().size();

        // Create the SubscriptionGroupPlan
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanMapper.toDto(subscriptionGroupPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionGroupPlanMockMvc.perform(put("/api/subscription-group-plans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionGroupPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionGroupPlan in the database
        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionGroupPlan() throws Exception {
        // Initialize the database
        subscriptionGroupPlanRepository.saveAndFlush(subscriptionGroupPlan);

        int databaseSizeBeforeDelete = subscriptionGroupPlanRepository.findAll().size();

        // Delete the subscriptionGroupPlan
        restSubscriptionGroupPlanMockMvc.perform(delete("/api/subscription-group-plans/{id}", subscriptionGroupPlan.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findAll();
        assertThat(subscriptionGroupPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
