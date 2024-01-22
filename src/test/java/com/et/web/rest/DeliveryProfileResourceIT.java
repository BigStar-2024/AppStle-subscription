package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.DeliveryProfile;
import com.et.repository.DeliveryProfileRepository;
import com.et.service.DeliveryProfileService;
import com.et.service.dto.DeliveryProfileDTO;
import com.et.service.mapper.DeliveryProfileMapper;

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
 * Integration tests for the {@link DeliveryProfileResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DeliveryProfileResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_PROFILE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_PROFILE_ID = "BBBBBBBBBB";

    @Autowired
    private DeliveryProfileRepository deliveryProfileRepository;

    @Autowired
    private DeliveryProfileMapper deliveryProfileMapper;

    @Autowired
    private DeliveryProfileService deliveryProfileService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryProfileMockMvc;

    private DeliveryProfile deliveryProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryProfile createEntity(EntityManager em) {
        DeliveryProfile deliveryProfile = new DeliveryProfile()
            .shop(DEFAULT_SHOP)
            .deliveryProfileId(DEFAULT_DELIVERY_PROFILE_ID);
        return deliveryProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryProfile createUpdatedEntity(EntityManager em) {
        DeliveryProfile deliveryProfile = new DeliveryProfile()
            .shop(UPDATED_SHOP)
            .deliveryProfileId(UPDATED_DELIVERY_PROFILE_ID);
        return deliveryProfile;
    }

    @BeforeEach
    public void initTest() {
        deliveryProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliveryProfile() throws Exception {
        int databaseSizeBeforeCreate = deliveryProfileRepository.findAll().size();
        // Create the DeliveryProfile
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(deliveryProfile);
        restDeliveryProfileMockMvc.perform(post("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the DeliveryProfile in the database
        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryProfile testDeliveryProfile = deliveryProfileList.get(deliveryProfileList.size() - 1);
        assertThat(testDeliveryProfile.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testDeliveryProfile.getDeliveryProfileId()).isEqualTo(DEFAULT_DELIVERY_PROFILE_ID);
    }

    @Test
    @Transactional
    public void createDeliveryProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliveryProfileRepository.findAll().size();

        // Create the DeliveryProfile with an existing ID
        deliveryProfile.setId(1L);
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(deliveryProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryProfileMockMvc.perform(post("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryProfile in the database
        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryProfileRepository.findAll().size();
        // set the field null
        deliveryProfile.setShop(null);

        // Create the DeliveryProfile, which fails.
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(deliveryProfile);


        restDeliveryProfileMockMvc.perform(post("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isBadRequest());

        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeliveryProfileIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryProfileRepository.findAll().size();
        // set the field null
        deliveryProfile.setDeliveryProfileId(null);

        // Create the DeliveryProfile, which fails.
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(deliveryProfile);


        restDeliveryProfileMockMvc.perform(post("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isBadRequest());

        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeliveryProfiles() throws Exception {
        // Initialize the database
        deliveryProfileRepository.saveAndFlush(deliveryProfile);

        // Get all the deliveryProfileList
        restDeliveryProfileMockMvc.perform(get("/api/delivery-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].deliveryProfileId").value(hasItem(DEFAULT_DELIVERY_PROFILE_ID)));
    }
    
    @Test
    @Transactional
    public void getDeliveryProfile() throws Exception {
        // Initialize the database
        deliveryProfileRepository.saveAndFlush(deliveryProfile);

        // Get the deliveryProfile
        restDeliveryProfileMockMvc.perform(get("/api/delivery-profiles/{id}", deliveryProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryProfile.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.deliveryProfileId").value(DEFAULT_DELIVERY_PROFILE_ID));
    }
    @Test
    @Transactional
    public void getNonExistingDeliveryProfile() throws Exception {
        // Get the deliveryProfile
        restDeliveryProfileMockMvc.perform(get("/api/delivery-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryProfile() throws Exception {
        // Initialize the database
        deliveryProfileRepository.saveAndFlush(deliveryProfile);

        int databaseSizeBeforeUpdate = deliveryProfileRepository.findAll().size();

        // Update the deliveryProfile
        DeliveryProfile updatedDeliveryProfile = deliveryProfileRepository.findById(deliveryProfile.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryProfile are not directly saved in db
        em.detach(updatedDeliveryProfile);
        updatedDeliveryProfile
            .shop(UPDATED_SHOP)
            .deliveryProfileId(UPDATED_DELIVERY_PROFILE_ID);
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(updatedDeliveryProfile);

        restDeliveryProfileMockMvc.perform(put("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isOk());

        // Validate the DeliveryProfile in the database
        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeUpdate);
        DeliveryProfile testDeliveryProfile = deliveryProfileList.get(deliveryProfileList.size() - 1);
        assertThat(testDeliveryProfile.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testDeliveryProfile.getDeliveryProfileId()).isEqualTo(UPDATED_DELIVERY_PROFILE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliveryProfile() throws Exception {
        int databaseSizeBeforeUpdate = deliveryProfileRepository.findAll().size();

        // Create the DeliveryProfile
        DeliveryProfileDTO deliveryProfileDTO = deliveryProfileMapper.toDto(deliveryProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryProfileMockMvc.perform(put("/api/delivery-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deliveryProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryProfile in the database
        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeliveryProfile() throws Exception {
        // Initialize the database
        deliveryProfileRepository.saveAndFlush(deliveryProfile);

        int databaseSizeBeforeDelete = deliveryProfileRepository.findAll().size();

        // Delete the deliveryProfile
        restDeliveryProfileMockMvc.perform(delete("/api/delivery-profiles/{id}", deliveryProfile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryProfile> deliveryProfileList = deliveryProfileRepository.findAll();
        assertThat(deliveryProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
