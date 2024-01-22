package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionCustomCss;
import com.et.repository.SubscriptionCustomCssRepository;
import com.et.service.SubscriptionCustomCssService;
import com.et.service.dto.SubscriptionCustomCssDTO;
import com.et.service.mapper.SubscriptionCustomCssMapper;

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
 * Integration tests for the {@link SubscriptionCustomCssResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubscriptionCustomCssResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_CSS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_CSS = "BBBBBBBBBB";

    @Autowired
    private SubscriptionCustomCssRepository subscriptionCustomCssRepository;

    @Autowired
    private SubscriptionCustomCssMapper subscriptionCustomCssMapper;

    @Autowired
    private SubscriptionCustomCssService subscriptionCustomCssService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionCustomCssMockMvc;

    private SubscriptionCustomCss subscriptionCustomCss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionCustomCss createEntity(EntityManager em) {
        SubscriptionCustomCss subscriptionCustomCss = new SubscriptionCustomCss()
            .shop(DEFAULT_SHOP)
            .customCss(DEFAULT_CUSTOM_CSS);
        return subscriptionCustomCss;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionCustomCss createUpdatedEntity(EntityManager em) {
        SubscriptionCustomCss subscriptionCustomCss = new SubscriptionCustomCss()
            .shop(UPDATED_SHOP)
            .customCss(UPDATED_CUSTOM_CSS);
        return subscriptionCustomCss;
    }

    @BeforeEach
    public void initTest() {
        subscriptionCustomCss = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionCustomCss() throws Exception {
        int databaseSizeBeforeCreate = subscriptionCustomCssRepository.findAll().size();
        // Create the SubscriptionCustomCss
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssMapper.toDto(subscriptionCustomCss);
        restSubscriptionCustomCssMockMvc.perform(post("/api/subscription-custom-csses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionCustomCssDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionCustomCss in the database
        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionCustomCss testSubscriptionCustomCss = subscriptionCustomCssList.get(subscriptionCustomCssList.size() - 1);
        assertThat(testSubscriptionCustomCss.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSubscriptionCustomCss.getCustomCss()).isEqualTo(DEFAULT_CUSTOM_CSS);
    }

    @Test
    @Transactional
    public void createSubscriptionCustomCssWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionCustomCssRepository.findAll().size();

        // Create the SubscriptionCustomCss with an existing ID
        subscriptionCustomCss.setId(1L);
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssMapper.toDto(subscriptionCustomCss);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionCustomCssMockMvc.perform(post("/api/subscription-custom-csses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionCustomCssDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionCustomCss in the database
        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionCustomCssRepository.findAll().size();
        // set the field null
        subscriptionCustomCss.setShop(null);

        // Create the SubscriptionCustomCss, which fails.
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssMapper.toDto(subscriptionCustomCss);


        restSubscriptionCustomCssMockMvc.perform(post("/api/subscription-custom-csses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionCustomCssDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionCustomCsses() throws Exception {
        // Initialize the database
        subscriptionCustomCssRepository.saveAndFlush(subscriptionCustomCss);

        // Get all the subscriptionCustomCssList
        restSubscriptionCustomCssMockMvc.perform(get("/api/subscription-custom-csses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionCustomCss.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].customCss").value(hasItem(DEFAULT_CUSTOM_CSS.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionCustomCss() throws Exception {
        // Initialize the database
        subscriptionCustomCssRepository.saveAndFlush(subscriptionCustomCss);

        // Get the subscriptionCustomCss
        restSubscriptionCustomCssMockMvc.perform(get("/api/subscription-custom-csses/{id}", subscriptionCustomCss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionCustomCss.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.customCss").value(DEFAULT_CUSTOM_CSS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSubscriptionCustomCss() throws Exception {
        // Get the subscriptionCustomCss
        restSubscriptionCustomCssMockMvc.perform(get("/api/subscription-custom-csses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionCustomCss() throws Exception {
        // Initialize the database
        subscriptionCustomCssRepository.saveAndFlush(subscriptionCustomCss);

        int databaseSizeBeforeUpdate = subscriptionCustomCssRepository.findAll().size();

        // Update the subscriptionCustomCss
        SubscriptionCustomCss updatedSubscriptionCustomCss = subscriptionCustomCssRepository.findById(subscriptionCustomCss.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionCustomCss are not directly saved in db
        em.detach(updatedSubscriptionCustomCss);
        updatedSubscriptionCustomCss
            .shop(UPDATED_SHOP)
            .customCss(UPDATED_CUSTOM_CSS);
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssMapper.toDto(updatedSubscriptionCustomCss);

        restSubscriptionCustomCssMockMvc.perform(put("/api/subscription-custom-csses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionCustomCssDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionCustomCss in the database
        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionCustomCss testSubscriptionCustomCss = subscriptionCustomCssList.get(subscriptionCustomCssList.size() - 1);
        assertThat(testSubscriptionCustomCss.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSubscriptionCustomCss.getCustomCss()).isEqualTo(UPDATED_CUSTOM_CSS);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionCustomCss() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionCustomCssRepository.findAll().size();

        // Create the SubscriptionCustomCss
        SubscriptionCustomCssDTO subscriptionCustomCssDTO = subscriptionCustomCssMapper.toDto(subscriptionCustomCss);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionCustomCssMockMvc.perform(put("/api/subscription-custom-csses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionCustomCssDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionCustomCss in the database
        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionCustomCss() throws Exception {
        // Initialize the database
        subscriptionCustomCssRepository.saveAndFlush(subscriptionCustomCss);

        int databaseSizeBeforeDelete = subscriptionCustomCssRepository.findAll().size();

        // Delete the subscriptionCustomCss
        restSubscriptionCustomCssMockMvc.perform(delete("/api/subscription-custom-csses/{id}", subscriptionCustomCss.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionCustomCss> subscriptionCustomCssList = subscriptionCustomCssRepository.findAll();
        assertThat(subscriptionCustomCssList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
