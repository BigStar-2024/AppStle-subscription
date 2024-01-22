package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ShopLabel;
import com.et.repository.ShopLabelRepository;
import com.et.service.ShopLabelService;
import com.et.service.dto.ShopLabelDTO;
import com.et.service.mapper.ShopLabelMapper;

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
 * Integration tests for the {@link ShopLabelResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ShopLabelResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_LABELS = "AAAAAAAAAA";
    private static final String UPDATED_LABELS = "BBBBBBBBBB";

    @Autowired
    private ShopLabelRepository shopLabelRepository;

    @Autowired
    private ShopLabelMapper shopLabelMapper;

    @Autowired
    private ShopLabelService shopLabelService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShopLabelMockMvc;

    private ShopLabel shopLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopLabel createEntity(EntityManager em) {
        ShopLabel shopLabel = new ShopLabel()
            .shop(DEFAULT_SHOP)
            .labels(DEFAULT_LABELS);
        return shopLabel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopLabel createUpdatedEntity(EntityManager em) {
        ShopLabel shopLabel = new ShopLabel()
            .shop(UPDATED_SHOP)
            .labels(UPDATED_LABELS);
        return shopLabel;
    }

    @BeforeEach
    public void initTest() {
        shopLabel = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopLabel() throws Exception {
        int databaseSizeBeforeCreate = shopLabelRepository.findAll().size();
        // Create the ShopLabel
        ShopLabelDTO shopLabelDTO = shopLabelMapper.toDto(shopLabel);
        restShopLabelMockMvc.perform(post("/api/shop-labels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopLabelDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopLabel in the database
        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeCreate + 1);
        ShopLabel testShopLabel = shopLabelList.get(shopLabelList.size() - 1);
        assertThat(testShopLabel.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testShopLabel.getLabels()).isEqualTo(DEFAULT_LABELS);
    }

    @Test
    @Transactional
    public void createShopLabelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopLabelRepository.findAll().size();

        // Create the ShopLabel with an existing ID
        shopLabel.setId(1L);
        ShopLabelDTO shopLabelDTO = shopLabelMapper.toDto(shopLabel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopLabelMockMvc.perform(post("/api/shop-labels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopLabelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopLabel in the database
        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopLabelRepository.findAll().size();
        // set the field null
        shopLabel.setShop(null);

        // Create the ShopLabel, which fails.
        ShopLabelDTO shopLabelDTO = shopLabelMapper.toDto(shopLabel);


        restShopLabelMockMvc.perform(post("/api/shop-labels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopLabelDTO)))
            .andExpect(status().isBadRequest());

        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShopLabels() throws Exception {
        // Initialize the database
        shopLabelRepository.saveAndFlush(shopLabel);

        // Get all the shopLabelList
        restShopLabelMockMvc.perform(get("/api/shop-labels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].labels").value(hasItem(DEFAULT_LABELS.toString())));
    }
    
    @Test
    @Transactional
    public void getShopLabel() throws Exception {
        // Initialize the database
        shopLabelRepository.saveAndFlush(shopLabel);

        // Get the shopLabel
        restShopLabelMockMvc.perform(get("/api/shop-labels/{id}", shopLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shopLabel.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.labels").value(DEFAULT_LABELS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingShopLabel() throws Exception {
        // Get the shopLabel
        restShopLabelMockMvc.perform(get("/api/shop-labels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopLabel() throws Exception {
        // Initialize the database
        shopLabelRepository.saveAndFlush(shopLabel);

        int databaseSizeBeforeUpdate = shopLabelRepository.findAll().size();

        // Update the shopLabel
        ShopLabel updatedShopLabel = shopLabelRepository.findById(shopLabel.getId()).get();
        // Disconnect from session so that the updates on updatedShopLabel are not directly saved in db
        em.detach(updatedShopLabel);
        updatedShopLabel
            .shop(UPDATED_SHOP)
            .labels(UPDATED_LABELS);
        ShopLabelDTO shopLabelDTO = shopLabelMapper.toDto(updatedShopLabel);

        restShopLabelMockMvc.perform(put("/api/shop-labels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopLabelDTO)))
            .andExpect(status().isOk());

        // Validate the ShopLabel in the database
        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeUpdate);
        ShopLabel testShopLabel = shopLabelList.get(shopLabelList.size() - 1);
        assertThat(testShopLabel.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testShopLabel.getLabels()).isEqualTo(UPDATED_LABELS);
    }

    @Test
    @Transactional
    public void updateNonExistingShopLabel() throws Exception {
        int databaseSizeBeforeUpdate = shopLabelRepository.findAll().size();

        // Create the ShopLabel
        ShopLabelDTO shopLabelDTO = shopLabelMapper.toDto(shopLabel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopLabelMockMvc.perform(put("/api/shop-labels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shopLabelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopLabel in the database
        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopLabel() throws Exception {
        // Initialize the database
        shopLabelRepository.saveAndFlush(shopLabel);

        int databaseSizeBeforeDelete = shopLabelRepository.findAll().size();

        // Delete the shopLabel
        restShopLabelMockMvc.perform(delete("/api/shop-labels/{id}", shopLabel.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopLabel> shopLabelList = shopLabelRepository.findAll();
        assertThat(shopLabelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
