package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.CustomerPortalDynamicScript;
import com.et.repository.CustomerPortalDynamicScriptRepository;
import com.et.service.CustomerPortalDynamicScriptService;
import com.et.service.dto.CustomerPortalDynamicScriptDTO;
import com.et.service.mapper.CustomerPortalDynamicScriptMapper;

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
 * Integration tests for the {@link CustomerPortalDynamicScriptResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CustomerPortalDynamicScriptResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_DYNAMIC_SCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_DYNAMIC_SCRIPT = "BBBBBBBBBB";

    @Autowired
    private CustomerPortalDynamicScriptRepository customerPortalDynamicScriptRepository;

    @Autowired
    private CustomerPortalDynamicScriptMapper customerPortalDynamicScriptMapper;

    @Autowired
    private CustomerPortalDynamicScriptService customerPortalDynamicScriptService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerPortalDynamicScriptMockMvc;

    private CustomerPortalDynamicScript customerPortalDynamicScript;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerPortalDynamicScript createEntity(EntityManager em) {
        CustomerPortalDynamicScript customerPortalDynamicScript = new CustomerPortalDynamicScript()
            .shop(DEFAULT_SHOP)
            .dynamicScript(DEFAULT_DYNAMIC_SCRIPT);
        return customerPortalDynamicScript;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerPortalDynamicScript createUpdatedEntity(EntityManager em) {
        CustomerPortalDynamicScript customerPortalDynamicScript = new CustomerPortalDynamicScript()
            .shop(UPDATED_SHOP)
            .dynamicScript(UPDATED_DYNAMIC_SCRIPT);
        return customerPortalDynamicScript;
    }

    @BeforeEach
    public void initTest() {
        customerPortalDynamicScript = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerPortalDynamicScript() throws Exception {
        int databaseSizeBeforeCreate = customerPortalDynamicScriptRepository.findAll().size();
        // Create the CustomerPortalDynamicScript
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = customerPortalDynamicScriptMapper.toDto(customerPortalDynamicScript);
        restCustomerPortalDynamicScriptMockMvc.perform(post("/api/customer-portal-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalDynamicScriptDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerPortalDynamicScript in the database
        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerPortalDynamicScript testCustomerPortalDynamicScript = customerPortalDynamicScriptList.get(customerPortalDynamicScriptList.size() - 1);
        assertThat(testCustomerPortalDynamicScript.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testCustomerPortalDynamicScript.getDynamicScript()).isEqualTo(DEFAULT_DYNAMIC_SCRIPT);
    }

    @Test
    @Transactional
    public void createCustomerPortalDynamicScriptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerPortalDynamicScriptRepository.findAll().size();

        // Create the CustomerPortalDynamicScript with an existing ID
        customerPortalDynamicScript.setId(1L);
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = customerPortalDynamicScriptMapper.toDto(customerPortalDynamicScript);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerPortalDynamicScriptMockMvc.perform(post("/api/customer-portal-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerPortalDynamicScript in the database
        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalDynamicScriptRepository.findAll().size();
        // set the field null
        customerPortalDynamicScript.setShop(null);

        // Create the CustomerPortalDynamicScript, which fails.
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = customerPortalDynamicScriptMapper.toDto(customerPortalDynamicScript);


        restCustomerPortalDynamicScriptMockMvc.perform(post("/api/customer-portal-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerPortalDynamicScripts() throws Exception {
        // Initialize the database
        customerPortalDynamicScriptRepository.saveAndFlush(customerPortalDynamicScript);

        // Get all the customerPortalDynamicScriptList
        restCustomerPortalDynamicScriptMockMvc.perform(get("/api/customer-portal-dynamic-scripts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerPortalDynamicScript.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].dynamicScript").value(hasItem(DEFAULT_DYNAMIC_SCRIPT.toString())));
    }
    
    @Test
    @Transactional
    public void getCustomerPortalDynamicScript() throws Exception {
        // Initialize the database
        customerPortalDynamicScriptRepository.saveAndFlush(customerPortalDynamicScript);

        // Get the customerPortalDynamicScript
        restCustomerPortalDynamicScriptMockMvc.perform(get("/api/customer-portal-dynamic-scripts/{id}", customerPortalDynamicScript.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerPortalDynamicScript.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.dynamicScript").value(DEFAULT_DYNAMIC_SCRIPT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCustomerPortalDynamicScript() throws Exception {
        // Get the customerPortalDynamicScript
        restCustomerPortalDynamicScriptMockMvc.perform(get("/api/customer-portal-dynamic-scripts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerPortalDynamicScript() throws Exception {
        // Initialize the database
        customerPortalDynamicScriptRepository.saveAndFlush(customerPortalDynamicScript);

        int databaseSizeBeforeUpdate = customerPortalDynamicScriptRepository.findAll().size();

        // Update the customerPortalDynamicScript
        CustomerPortalDynamicScript updatedCustomerPortalDynamicScript = customerPortalDynamicScriptRepository.findById(customerPortalDynamicScript.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerPortalDynamicScript are not directly saved in db
        em.detach(updatedCustomerPortalDynamicScript);
        updatedCustomerPortalDynamicScript
            .shop(UPDATED_SHOP)
            .dynamicScript(UPDATED_DYNAMIC_SCRIPT);
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = customerPortalDynamicScriptMapper.toDto(updatedCustomerPortalDynamicScript);

        restCustomerPortalDynamicScriptMockMvc.perform(put("/api/customer-portal-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalDynamicScriptDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerPortalDynamicScript in the database
        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeUpdate);
        CustomerPortalDynamicScript testCustomerPortalDynamicScript = customerPortalDynamicScriptList.get(customerPortalDynamicScriptList.size() - 1);
        assertThat(testCustomerPortalDynamicScript.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testCustomerPortalDynamicScript.getDynamicScript()).isEqualTo(UPDATED_DYNAMIC_SCRIPT);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerPortalDynamicScript() throws Exception {
        int databaseSizeBeforeUpdate = customerPortalDynamicScriptRepository.findAll().size();

        // Create the CustomerPortalDynamicScript
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = customerPortalDynamicScriptMapper.toDto(customerPortalDynamicScript);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerPortalDynamicScriptMockMvc.perform(put("/api/customer-portal-dynamic-scripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalDynamicScriptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerPortalDynamicScript in the database
        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerPortalDynamicScript() throws Exception {
        // Initialize the database
        customerPortalDynamicScriptRepository.saveAndFlush(customerPortalDynamicScript);

        int databaseSizeBeforeDelete = customerPortalDynamicScriptRepository.findAll().size();

        // Delete the customerPortalDynamicScript
        restCustomerPortalDynamicScriptMockMvc.perform(delete("/api/customer-portal-dynamic-scripts/{id}", customerPortalDynamicScript.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerPortalDynamicScript> customerPortalDynamicScriptList = customerPortalDynamicScriptRepository.findAll();
        assertThat(customerPortalDynamicScriptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
