package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ProductSwap;
import com.et.repository.ProductSwapRepository;
import com.et.service.ProductSwapService;
import com.et.service.dto.ProductSwapDTO;
import com.et.service.mapper.ProductSwapMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.et.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductSwapResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class ProductSwapResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_VARIANTS = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_VARIANTS = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_VARIANTS = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_VARIANTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_UPDATED_FIRST_ORDER = false;
    private static final Boolean UPDATED_UPDATED_FIRST_ORDER = true;

    private static final Boolean DEFAULT_CHECK_FOR_EVERY_RECURRING_ORDER = false;
    private static final Boolean UPDATED_CHECK_FOR_EVERY_RECURRING_ORDER = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHANGE_NEXT_ORDER_DATE_BY = 1;
    private static final Integer UPDATED_CHANGE_NEXT_ORDER_DATE_BY = 2;

    private static final Integer DEFAULT_FOR_BILLING_CYCLE = 1;
    private static final Integer UPDATED_FOR_BILLING_CYCLE = 2;

    @Autowired
    private ProductSwapRepository productSwapRepository;

    @Autowired
    private ProductSwapMapper productSwapMapper;

    @Autowired
    private ProductSwapService productSwapService;

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

    private MockMvc restProductSwapMockMvc;

    private ProductSwap productSwap;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductSwapResource productSwapResource = new ProductSwapResource(productSwapService);
        this.restProductSwapMockMvc = MockMvcBuilders.standaloneSetup(productSwapResource)
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
    public static ProductSwap createEntity(EntityManager em) {
        ProductSwap productSwap = new ProductSwap()
            .shop(DEFAULT_SHOP)
            .sourceVariants(DEFAULT_SOURCE_VARIANTS)
            .destinationVariants(DEFAULT_DESTINATION_VARIANTS)
            .updatedFirstOrder(DEFAULT_UPDATED_FIRST_ORDER)
            .checkForEveryRecurringOrder(DEFAULT_CHECK_FOR_EVERY_RECURRING_ORDER)
            .name(DEFAULT_NAME)
            .changeNextOrderDateBy(DEFAULT_CHANGE_NEXT_ORDER_DATE_BY)
            .forBillingCycle(DEFAULT_FOR_BILLING_CYCLE);
        return productSwap;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductSwap createUpdatedEntity(EntityManager em) {
        ProductSwap productSwap = new ProductSwap()
            .shop(UPDATED_SHOP)
            .sourceVariants(UPDATED_SOURCE_VARIANTS)
            .destinationVariants(UPDATED_DESTINATION_VARIANTS)
            .updatedFirstOrder(UPDATED_UPDATED_FIRST_ORDER)
            .checkForEveryRecurringOrder(UPDATED_CHECK_FOR_EVERY_RECURRING_ORDER)
            .name(UPDATED_NAME)
            .changeNextOrderDateBy(UPDATED_CHANGE_NEXT_ORDER_DATE_BY)
            .forBillingCycle(UPDATED_FOR_BILLING_CYCLE);
        return productSwap;
    }

    @BeforeEach
    public void initTest() {
        productSwap = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductSwap() throws Exception {
        int databaseSizeBeforeCreate = productSwapRepository.findAll().size();

        // Create the ProductSwap
        ProductSwapDTO productSwapDTO = productSwapMapper.toDto(productSwap);
        restProductSwapMockMvc.perform(post("/api/product-swaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSwapDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductSwap in the database
        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeCreate + 1);
        ProductSwap testProductSwap = productSwapList.get(productSwapList.size() - 1);
        assertThat(testProductSwap.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testProductSwap.getSourceVariants()).isEqualTo(DEFAULT_SOURCE_VARIANTS);
        assertThat(testProductSwap.getDestinationVariants()).isEqualTo(DEFAULT_DESTINATION_VARIANTS);
        assertThat(testProductSwap.isUpdatedFirstOrder()).isEqualTo(DEFAULT_UPDATED_FIRST_ORDER);
        assertThat(testProductSwap.isCheckForEveryRecurringOrder()).isEqualTo(DEFAULT_CHECK_FOR_EVERY_RECURRING_ORDER);
        assertThat(testProductSwap.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductSwap.getChangeNextOrderDateBy()).isEqualTo(DEFAULT_CHANGE_NEXT_ORDER_DATE_BY);
        assertThat(testProductSwap.getForBillingCycle()).isEqualTo(DEFAULT_FOR_BILLING_CYCLE);
    }

    @Test
    @Transactional
    public void createProductSwapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productSwapRepository.findAll().size();

        // Create the ProductSwap with an existing ID
        productSwap.setId(1L);
        ProductSwapDTO productSwapDTO = productSwapMapper.toDto(productSwap);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductSwapMockMvc.perform(post("/api/product-swaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSwapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductSwap in the database
        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = productSwapRepository.findAll().size();
        // set the field null
        productSwap.setShop(null);

        // Create the ProductSwap, which fails.
        ProductSwapDTO productSwapDTO = productSwapMapper.toDto(productSwap);

        restProductSwapMockMvc.perform(post("/api/product-swaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSwapDTO)))
            .andExpect(status().isBadRequest());

        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductSwaps() throws Exception {
        // Initialize the database
        productSwapRepository.saveAndFlush(productSwap);

        // Get all the productSwapList
        restProductSwapMockMvc.perform(get("/api/product-swaps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productSwap.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].sourceVariants").value(hasItem(DEFAULT_SOURCE_VARIANTS.toString())))
            .andExpect(jsonPath("$.[*].destinationVariants").value(hasItem(DEFAULT_DESTINATION_VARIANTS.toString())))
            .andExpect(jsonPath("$.[*].updatedFirstOrder").value(hasItem(DEFAULT_UPDATED_FIRST_ORDER.booleanValue())))
            .andExpect(jsonPath("$.[*].checkForEveryRecurringOrder").value(hasItem(DEFAULT_CHECK_FOR_EVERY_RECURRING_ORDER.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].changeNextOrderDateBy").value(hasItem(DEFAULT_CHANGE_NEXT_ORDER_DATE_BY)))
            .andExpect(jsonPath("$.[*].forBillingCycle").value(hasItem(DEFAULT_FOR_BILLING_CYCLE)));
    }
    
    @Test
    @Transactional
    public void getProductSwap() throws Exception {
        // Initialize the database
        productSwapRepository.saveAndFlush(productSwap);

        // Get the productSwap
        restProductSwapMockMvc.perform(get("/api/product-swaps/{id}", productSwap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productSwap.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.sourceVariants").value(DEFAULT_SOURCE_VARIANTS.toString()))
            .andExpect(jsonPath("$.destinationVariants").value(DEFAULT_DESTINATION_VARIANTS.toString()))
            .andExpect(jsonPath("$.updatedFirstOrder").value(DEFAULT_UPDATED_FIRST_ORDER.booleanValue()))
            .andExpect(jsonPath("$.checkForEveryRecurringOrder").value(DEFAULT_CHECK_FOR_EVERY_RECURRING_ORDER.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.changeNextOrderDateBy").value(DEFAULT_CHANGE_NEXT_ORDER_DATE_BY))
            .andExpect(jsonPath("$.forBillingCycle").value(DEFAULT_FOR_BILLING_CYCLE));
    }

    @Test
    @Transactional
    public void getNonExistingProductSwap() throws Exception {
        // Get the productSwap
        restProductSwapMockMvc.perform(get("/api/product-swaps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductSwap() throws Exception {
        // Initialize the database
        productSwapRepository.saveAndFlush(productSwap);

        int databaseSizeBeforeUpdate = productSwapRepository.findAll().size();

        // Update the productSwap
        ProductSwap updatedProductSwap = productSwapRepository.findById(productSwap.getId()).get();
        // Disconnect from session so that the updates on updatedProductSwap are not directly saved in db
        em.detach(updatedProductSwap);
        updatedProductSwap
            .shop(UPDATED_SHOP)
            .sourceVariants(UPDATED_SOURCE_VARIANTS)
            .destinationVariants(UPDATED_DESTINATION_VARIANTS)
            .updatedFirstOrder(UPDATED_UPDATED_FIRST_ORDER)
            .checkForEveryRecurringOrder(UPDATED_CHECK_FOR_EVERY_RECURRING_ORDER)
            .name(UPDATED_NAME)
            .changeNextOrderDateBy(UPDATED_CHANGE_NEXT_ORDER_DATE_BY)
            .forBillingCycle(UPDATED_FOR_BILLING_CYCLE);
        ProductSwapDTO productSwapDTO = productSwapMapper.toDto(updatedProductSwap);

        restProductSwapMockMvc.perform(put("/api/product-swaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSwapDTO)))
            .andExpect(status().isOk());

        // Validate the ProductSwap in the database
        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeUpdate);
        ProductSwap testProductSwap = productSwapList.get(productSwapList.size() - 1);
        assertThat(testProductSwap.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testProductSwap.getSourceVariants()).isEqualTo(UPDATED_SOURCE_VARIANTS);
        assertThat(testProductSwap.getDestinationVariants()).isEqualTo(UPDATED_DESTINATION_VARIANTS);
        assertThat(testProductSwap.isUpdatedFirstOrder()).isEqualTo(UPDATED_UPDATED_FIRST_ORDER);
        assertThat(testProductSwap.isCheckForEveryRecurringOrder()).isEqualTo(UPDATED_CHECK_FOR_EVERY_RECURRING_ORDER);
        assertThat(testProductSwap.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductSwap.getChangeNextOrderDateBy()).isEqualTo(UPDATED_CHANGE_NEXT_ORDER_DATE_BY);
        assertThat(testProductSwap.getForBillingCycle()).isEqualTo(UPDATED_FOR_BILLING_CYCLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProductSwap() throws Exception {
        int databaseSizeBeforeUpdate = productSwapRepository.findAll().size();

        // Create the ProductSwap
        ProductSwapDTO productSwapDTO = productSwapMapper.toDto(productSwap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductSwapMockMvc.perform(put("/api/product-swaps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSwapDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductSwap in the database
        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductSwap() throws Exception {
        // Initialize the database
        productSwapRepository.saveAndFlush(productSwap);

        int databaseSizeBeforeDelete = productSwapRepository.findAll().size();

        // Delete the productSwap
        restProductSwapMockMvc.perform(delete("/api/product-swaps/{id}", productSwap.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductSwap> productSwapList = productSwapRepository.findAll();
        assertThat(productSwapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
