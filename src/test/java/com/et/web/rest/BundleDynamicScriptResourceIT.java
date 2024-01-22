package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.BundleDynamicScript;
import com.et.repository.BundleDynamicScriptRepository;
import com.et.service.BundleDynamicScriptService;
import com.et.service.dto.BundleDynamicScriptDTO;
import com.et.service.mapper.BundleDynamicScriptMapper;

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
 * Integration tests for the {@link BundleDynamicScriptResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BundleDynamicScriptResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_DYNAMIC_SCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_DYNAMIC_SCRIPT = "BBBBBBBBBB";

    @Autowired
    private BundleDynamicScriptRepository bundleDynamicScriptRepository;

    @Autowired
    private BundleDynamicScriptMapper bundleDynamicScriptMapper;

    @Autowired
    private BundleDynamicScriptService bundleDynamicScriptService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBundleDynamicScriptMockMvc;

    private BundleDynamicScript bundleDynamicScript;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BundleDynamicScript createEntity(EntityManager em) {
        BundleDynamicScript bundleDynamicScript = new BundleDynamicScript()
            .shop(DEFAULT_SHOP)
            .dynamicScript(DEFAULT_DYNAMIC_SCRIPT);
        return bundleDynamicScript;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BundleDynamicScript createUpdatedEntity(EntityManager em) {
        BundleDynamicScript bundleDynamicScript = new BundleDynamicScript()
            .shop(UPDATED_SHOP)
            .dynamicScript(UPDATED_DYNAMIC_SCRIPT);
        return bundleDynamicScript;
    }

    @BeforeEach
    public void initTest() {
        bundleDynamicScript = createEntity(em);
    }

    @Test
    @Transactional
    public void createBundleDynamicScript() throws Exception {
        int databaseSizeBeforeCreate = bundleDynamicScriptRepository.findAll().size();
        // Create the BundleDynamicScript
        BundleDynamicScriptDTO bundleDynamicScriptDTO = bundleDynamicScriptMapper.toDto(bundleDynamicScript);
        restBundleDynamicScriptMockMvc.perform(post("/api/bundle-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bundleDynamicScriptDTO)))
            .andExpect(status().isCreated());

        // Validate the BundleDynamicScript in the database
        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeCreate + 1);
        BundleDynamicScript testBundleDynamicScript = bundleDynamicScriptList.get(bundleDynamicScriptList.size() - 1);
        assertThat(testBundleDynamicScript.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testBundleDynamicScript.getDynamicScript()).isEqualTo(DEFAULT_DYNAMIC_SCRIPT);
    }

    @Test
    @Transactional
    public void createBundleDynamicScriptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bundleDynamicScriptRepository.findAll().size();

        // Create the BundleDynamicScript with an existing ID
        bundleDynamicScript.setId(1L);
        BundleDynamicScriptDTO bundleDynamicScriptDTO = bundleDynamicScriptMapper.toDto(bundleDynamicScript);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBundleDynamicScriptMockMvc.perform(post("/api/bundle-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bundleDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BundleDynamicScript in the database
        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = bundleDynamicScriptRepository.findAll().size();
        // set the field null
        bundleDynamicScript.setShop(null);

        // Create the BundleDynamicScript, which fails.
        BundleDynamicScriptDTO bundleDynamicScriptDTO = bundleDynamicScriptMapper.toDto(bundleDynamicScript);


        restBundleDynamicScriptMockMvc.perform(post("/api/bundle-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bundleDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBundleDynamicScripts() throws Exception {
        // Initialize the database
        bundleDynamicScriptRepository.saveAndFlush(bundleDynamicScript);

        // Get all the bundleDynamicScriptList
        restBundleDynamicScriptMockMvc.perform(get("/api/bundle-dynamic-scripts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bundleDynamicScript.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].dynamicScript").value(hasItem(DEFAULT_DYNAMIC_SCRIPT.toString())));
    }
    
    @Test
    @Transactional
    public void getBundleDynamicScript() throws Exception {
        // Initialize the database
        bundleDynamicScriptRepository.saveAndFlush(bundleDynamicScript);

        // Get the bundleDynamicScript
        restBundleDynamicScriptMockMvc.perform(get("/api/bundle-dynamic-scripts/{id}", bundleDynamicScript.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bundleDynamicScript.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.dynamicScript").value(DEFAULT_DYNAMIC_SCRIPT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBundleDynamicScript() throws Exception {
        // Get the bundleDynamicScript
        restBundleDynamicScriptMockMvc.perform(get("/api/bundle-dynamic-scripts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBundleDynamicScript() throws Exception {
        // Initialize the database
        bundleDynamicScriptRepository.saveAndFlush(bundleDynamicScript);

        int databaseSizeBeforeUpdate = bundleDynamicScriptRepository.findAll().size();

        // Update the bundleDynamicScript
        BundleDynamicScript updatedBundleDynamicScript = bundleDynamicScriptRepository.findById(bundleDynamicScript.getId()).get();
        // Disconnect from session so that the updates on updatedBundleDynamicScript are not directly saved in db
        em.detach(updatedBundleDynamicScript);
        updatedBundleDynamicScript
            .shop(UPDATED_SHOP)
            .dynamicScript(UPDATED_DYNAMIC_SCRIPT);
        BundleDynamicScriptDTO bundleDynamicScriptDTO = bundleDynamicScriptMapper.toDto(updatedBundleDynamicScript);

        restBundleDynamicScriptMockMvc.perform(put("/api/bundle-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bundleDynamicScriptDTO)))
            .andExpect(status().isOk());

        // Validate the BundleDynamicScript in the database
        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeUpdate);
        BundleDynamicScript testBundleDynamicScript = bundleDynamicScriptList.get(bundleDynamicScriptList.size() - 1);
        assertThat(testBundleDynamicScript.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testBundleDynamicScript.getDynamicScript()).isEqualTo(UPDATED_DYNAMIC_SCRIPT);
    }

    @Test
    @Transactional
    public void updateNonExistingBundleDynamicScript() throws Exception {
        int databaseSizeBeforeUpdate = bundleDynamicScriptRepository.findAll().size();

        // Create the BundleDynamicScript
        BundleDynamicScriptDTO bundleDynamicScriptDTO = bundleDynamicScriptMapper.toDto(bundleDynamicScript);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBundleDynamicScriptMockMvc.perform(put("/api/bundle-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bundleDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BundleDynamicScript in the database
        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBundleDynamicScript() throws Exception {
        // Initialize the database
        bundleDynamicScriptRepository.saveAndFlush(bundleDynamicScript);

        int databaseSizeBeforeDelete = bundleDynamicScriptRepository.findAll().size();

        // Delete the bundleDynamicScript
        restBundleDynamicScriptMockMvc.perform(delete("/api/bundle-dynamic-scripts/{id}", bundleDynamicScript.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BundleDynamicScript> bundleDynamicScriptList = bundleDynamicScriptRepository.findAll();
        assertThat(bundleDynamicScriptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
