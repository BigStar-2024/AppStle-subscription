package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionContractOneOff;
import com.et.repository.SubscriptionContractOneOffRepository;
import com.et.service.SubscriptionContractOneOffService;
import com.et.service.dto.SubscriptionContractOneOffDTO;
import com.et.service.mapper.SubscriptionContractOneOffMapper;
import com.et.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.et.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubscriptionContractOneOffResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class SubscriptionContractOneOffResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_BILLING_ATTEMPT_ID = 1L;
    private static final Long UPDATED_BILLING_ATTEMPT_ID = 2L;

    private static final Long DEFAULT_SUBSCRIPTION_CONTRACT_ID = 1L;
    private static final Long UPDATED_SUBSCRIPTION_CONTRACT_ID = 2L;

    private static final Long DEFAULT_VARIANT_ID = 1L;
    private static final Long UPDATED_VARIANT_ID = 2L;

    private static final String DEFAULT_VARIANT_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_HANDLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Autowired
    private SubscriptionContractOneOffRepository subscriptionContractOneOffRepository;

    @Autowired
    private SubscriptionContractOneOffMapper subscriptionContractOneOffMapper;

    @Autowired
    private SubscriptionContractOneOffService subscriptionContractOneOffService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSubscriptionContractOneOffMockMvc;

    private SubscriptionContractOneOff subscriptionContractOneOff;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriptionContractOneOffResource subscriptionContractOneOffResource = new SubscriptionContractOneOffResource(subscriptionContractOneOffService);
        this.restSubscriptionContractOneOffMockMvc = MockMvcBuilders.standaloneSetup(subscriptionContractOneOffResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractOneOff createEntity(EntityManager em) {
        SubscriptionContractOneOff subscriptionContractOneOff = new SubscriptionContractOneOff()
            .shop(DEFAULT_SHOP)
            .billingAttemptId(DEFAULT_BILLING_ATTEMPT_ID)
            .subscriptionContractId(DEFAULT_SUBSCRIPTION_CONTRACT_ID)
            .variantId(DEFAULT_VARIANT_ID)
            .variantHandle(DEFAULT_VARIANT_HANDLE)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE);
        return subscriptionContractOneOff;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionContractOneOff createUpdatedEntity(EntityManager em) {
        SubscriptionContractOneOff subscriptionContractOneOff = new SubscriptionContractOneOff()
            .shop(UPDATED_SHOP)
            .billingAttemptId(UPDATED_BILLING_ATTEMPT_ID)
            .subscriptionContractId(UPDATED_SUBSCRIPTION_CONTRACT_ID)
            .variantId(UPDATED_VARIANT_ID)
            .variantHandle(UPDATED_VARIANT_HANDLE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);
        return subscriptionContractOneOff;
    }

    @BeforeEach
    public void initTest() {
        subscriptionContractOneOff = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionContractOneOff() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractOneOffRepository.findAll().size();

        // Create the SubscriptionContractOneOff
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);
        restSubscriptionContractOneOffMockMvc.perform(post("/api/subscription-contract-one-offs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractOneOffDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionContractOneOff in the database
        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionContractOneOff testSubscriptionContractOneOff = subscriptionContractOneOffList.get(subscriptionContractOneOffList.size() - 1);
        assertThat(testSubscriptionContractOneOff.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSubscriptionContractOneOff.getBillingAttemptId()).isEqualTo(DEFAULT_BILLING_ATTEMPT_ID);
        assertThat(testSubscriptionContractOneOff.getSubscriptionContractId()).isEqualTo(DEFAULT_SUBSCRIPTION_CONTRACT_ID);
        assertThat(testSubscriptionContractOneOff.getVariantId()).isEqualTo(DEFAULT_VARIANT_ID);
        assertThat(testSubscriptionContractOneOff.getVariantHandle()).isEqualTo(DEFAULT_VARIANT_HANDLE);
        assertThat(testSubscriptionContractOneOff.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSubscriptionContractOneOff.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createSubscriptionContractOneOffWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionContractOneOffRepository.findAll().size();

        // Create the SubscriptionContractOneOff with an existing ID
        subscriptionContractOneOff.setId(1L);
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionContractOneOffMockMvc.perform(post("/api/subscription-contract-one-offs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractOneOffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractOneOff in the database
        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionContractOneOffRepository.findAll().size();
        // set the field null
        subscriptionContractOneOff.setShop(null);

        // Create the SubscriptionContractOneOff, which fails.
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);

        restSubscriptionContractOneOffMockMvc.perform(post("/api/subscription-contract-one-offs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractOneOffDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionContractOneOffs() throws Exception {
        // Initialize the database
        subscriptionContractOneOffRepository.saveAndFlush(subscriptionContractOneOff);

        // Get all the subscriptionContractOneOffList
        restSubscriptionContractOneOffMockMvc.perform(get("/api/subscription-contract-one-offs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionContractOneOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].billingAttemptId").value(hasItem(DEFAULT_BILLING_ATTEMPT_ID.intValue())))
            .andExpect(jsonPath("$.[*].subscriptionContractId").value(hasItem(DEFAULT_SUBSCRIPTION_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].variantId").value(hasItem(DEFAULT_VARIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].variantHandle").value(hasItem(DEFAULT_VARIANT_HANDLE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getSubscriptionContractOneOff() throws Exception {
        // Initialize the database
        subscriptionContractOneOffRepository.saveAndFlush(subscriptionContractOneOff);

        // Get the subscriptionContractOneOff
        restSubscriptionContractOneOffMockMvc.perform(get("/api/subscription-contract-one-offs/{id}", subscriptionContractOneOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionContractOneOff.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.billingAttemptId").value(DEFAULT_BILLING_ATTEMPT_ID.intValue()))
            .andExpect(jsonPath("$.subscriptionContractId").value(DEFAULT_SUBSCRIPTION_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.variantId").value(DEFAULT_VARIANT_ID.intValue()))
            .andExpect(jsonPath("$.variantHandle").value(DEFAULT_VARIANT_HANDLE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSubscriptionContractOneOff() throws Exception {
        // Get the subscriptionContractOneOff
        restSubscriptionContractOneOffMockMvc.perform(get("/api/subscription-contract-one-offs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionContractOneOff() throws Exception {
        // Initialize the database
        subscriptionContractOneOffRepository.saveAndFlush(subscriptionContractOneOff);

        int databaseSizeBeforeUpdate = subscriptionContractOneOffRepository.findAll().size();

        // Update the subscriptionContractOneOff
        SubscriptionContractOneOff updatedSubscriptionContractOneOff = subscriptionContractOneOffRepository.findById(subscriptionContractOneOff.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionContractOneOff are not directly saved in db
        em.detach(updatedSubscriptionContractOneOff);
        updatedSubscriptionContractOneOff
            .shop(UPDATED_SHOP)
            .billingAttemptId(UPDATED_BILLING_ATTEMPT_ID)
            .subscriptionContractId(UPDATED_SUBSCRIPTION_CONTRACT_ID)
            .variantId(UPDATED_VARIANT_ID)
            .variantHandle(UPDATED_VARIANT_HANDLE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE);
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffMapper.toDto(updatedSubscriptionContractOneOff);

        restSubscriptionContractOneOffMockMvc.perform(put("/api/subscription-contract-one-offs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractOneOffDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionContractOneOff in the database
        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionContractOneOff testSubscriptionContractOneOff = subscriptionContractOneOffList.get(subscriptionContractOneOffList.size() - 1);
        assertThat(testSubscriptionContractOneOff.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSubscriptionContractOneOff.getBillingAttemptId()).isEqualTo(UPDATED_BILLING_ATTEMPT_ID);
        assertThat(testSubscriptionContractOneOff.getSubscriptionContractId()).isEqualTo(UPDATED_SUBSCRIPTION_CONTRACT_ID);
        assertThat(testSubscriptionContractOneOff.getVariantId()).isEqualTo(UPDATED_VARIANT_ID);
        assertThat(testSubscriptionContractOneOff.getVariantHandle()).isEqualTo(UPDATED_VARIANT_HANDLE);
        assertThat(testSubscriptionContractOneOff.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSubscriptionContractOneOff.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionContractOneOff() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionContractOneOffRepository.findAll().size();

        // Create the SubscriptionContractOneOff
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionContractOneOffMockMvc.perform(put("/api/subscription-contract-one-offs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionContractOneOffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionContractOneOff in the database
        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionContractOneOff() throws Exception {
        // Initialize the database
        subscriptionContractOneOffRepository.saveAndFlush(subscriptionContractOneOff);

        int databaseSizeBeforeDelete = subscriptionContractOneOffRepository.findAll().size();

        // Delete the subscriptionContractOneOff
        restSubscriptionContractOneOffMockMvc.perform(delete("/api/subscription-contract-one-offs/{id}", subscriptionContractOneOff.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionContractOneOff> subscriptionContractOneOffList = subscriptionContractOneOffRepository.findAll();
        assertThat(subscriptionContractOneOffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
