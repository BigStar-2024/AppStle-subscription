package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.VariantInfo;
import com.et.repository.VariantInfoRepository;
import com.et.service.VariantInfoService;
import com.et.service.dto.VariantInfoDTO;
import com.et.service.mapper.VariantInfoMapper;
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
 * Integration tests for the {@link VariantInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class VariantInfoResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_VARIANT_ID = 1L;
    private static final Long UPDATED_VARIANT_ID = 2L;

    private static final String DEFAULT_PRODUCT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VARIANT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    @Autowired
    private VariantInfoRepository variantInfoRepository;

    @Autowired
    private VariantInfoMapper variantInfoMapper;

    @Autowired
    private VariantInfoService variantInfoService;

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

    private MockMvc restVariantInfoMockMvc;

    private VariantInfo variantInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VariantInfoResource variantInfoResource = new VariantInfoResource(variantInfoService);
        this.restVariantInfoMockMvc = MockMvcBuilders.standaloneSetup(variantInfoResource)
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
    public static VariantInfo createEntity(EntityManager em) {
        VariantInfo variantInfo = new VariantInfo()
            .shop(DEFAULT_SHOP)
            .productId(DEFAULT_PRODUCT_ID)
            .variantId(DEFAULT_VARIANT_ID)
            .productTitle(DEFAULT_PRODUCT_TITLE)
            .variantTitle(DEFAULT_VARIANT_TITLE)
            .sku(DEFAULT_SKU);
        return variantInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VariantInfo createUpdatedEntity(EntityManager em) {
        VariantInfo variantInfo = new VariantInfo()
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .variantId(UPDATED_VARIANT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE)
            .variantTitle(UPDATED_VARIANT_TITLE)
            .sku(UPDATED_SKU);
        return variantInfo;
    }

    @BeforeEach
    public void initTest() {
        variantInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createVariantInfo() throws Exception {
        int databaseSizeBeforeCreate = variantInfoRepository.findAll().size();

        // Create the VariantInfo
        VariantInfoDTO variantInfoDTO = variantInfoMapper.toDto(variantInfo);
        restVariantInfoMockMvc.perform(post("/api/variant-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variantInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the VariantInfo in the database
        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeCreate + 1);
        VariantInfo testVariantInfo = variantInfoList.get(variantInfoList.size() - 1);
        assertThat(testVariantInfo.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testVariantInfo.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testVariantInfo.getVariantId()).isEqualTo(DEFAULT_VARIANT_ID);
        assertThat(testVariantInfo.getProductTitle()).isEqualTo(DEFAULT_PRODUCT_TITLE);
        assertThat(testVariantInfo.getVariantTitle()).isEqualTo(DEFAULT_VARIANT_TITLE);
        assertThat(testVariantInfo.getSku()).isEqualTo(DEFAULT_SKU);
    }

    @Test
    @Transactional
    public void createVariantInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = variantInfoRepository.findAll().size();

        // Create the VariantInfo with an existing ID
        variantInfo.setId(1L);
        VariantInfoDTO variantInfoDTO = variantInfoMapper.toDto(variantInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVariantInfoMockMvc.perform(post("/api/variant-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variantInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VariantInfo in the database
        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = variantInfoRepository.findAll().size();
        // set the field null
        variantInfo.setShop(null);

        // Create the VariantInfo, which fails.
        VariantInfoDTO variantInfoDTO = variantInfoMapper.toDto(variantInfo);

        restVariantInfoMockMvc.perform(post("/api/variant-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variantInfoDTO)))
            .andExpect(status().isBadRequest());

        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVariantInfos() throws Exception {
        // Initialize the database
        variantInfoRepository.saveAndFlush(variantInfo);

        // Get all the variantInfoList
        restVariantInfoMockMvc.perform(get("/api/variant-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variantInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].variantId").value(hasItem(DEFAULT_VARIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productTitle").value(hasItem(DEFAULT_PRODUCT_TITLE)))
            .andExpect(jsonPath("$.[*].variantTitle").value(hasItem(DEFAULT_VARIANT_TITLE)))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)));
    }

    @Test
    @Transactional
    public void getVariantInfo() throws Exception {
        // Initialize the database
        variantInfoRepository.saveAndFlush(variantInfo);

        // Get the variantInfo
        restVariantInfoMockMvc.perform(get("/api/variant-infos/{id}", variantInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(variantInfo.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.variantId").value(DEFAULT_VARIANT_ID.intValue()))
            .andExpect(jsonPath("$.productTitle").value(DEFAULT_PRODUCT_TITLE))
            .andExpect(jsonPath("$.variantTitle").value(DEFAULT_VARIANT_TITLE))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU));
    }

    @Test
    @Transactional
    public void getNonExistingVariantInfo() throws Exception {
        // Get the variantInfo
        restVariantInfoMockMvc.perform(get("/api/variant-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVariantInfo() throws Exception {
        // Initialize the database
        variantInfoRepository.saveAndFlush(variantInfo);

        int databaseSizeBeforeUpdate = variantInfoRepository.findAll().size();

        // Update the variantInfo
        VariantInfo updatedVariantInfo = variantInfoRepository.findById(variantInfo.getId()).get();
        // Disconnect from session so that the updates on updatedVariantInfo are not directly saved in db
        em.detach(updatedVariantInfo);
        updatedVariantInfo
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .variantId(UPDATED_VARIANT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE)
            .variantTitle(UPDATED_VARIANT_TITLE)
            .sku(UPDATED_SKU);
        VariantInfoDTO variantInfoDTO = variantInfoMapper.toDto(updatedVariantInfo);

        restVariantInfoMockMvc.perform(put("/api/variant-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variantInfoDTO)))
            .andExpect(status().isOk());

        // Validate the VariantInfo in the database
        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeUpdate);
        VariantInfo testVariantInfo = variantInfoList.get(variantInfoList.size() - 1);
        assertThat(testVariantInfo.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testVariantInfo.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testVariantInfo.getVariantId()).isEqualTo(UPDATED_VARIANT_ID);
        assertThat(testVariantInfo.getProductTitle()).isEqualTo(UPDATED_PRODUCT_TITLE);
        assertThat(testVariantInfo.getVariantTitle()).isEqualTo(UPDATED_VARIANT_TITLE);
        assertThat(testVariantInfo.getSku()).isEqualTo(UPDATED_SKU);
    }

    @Test
    @Transactional
    public void updateNonExistingVariantInfo() throws Exception {
        int databaseSizeBeforeUpdate = variantInfoRepository.findAll().size();

        // Create the VariantInfo
        VariantInfoDTO variantInfoDTO = variantInfoMapper.toDto(variantInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariantInfoMockMvc.perform(put("/api/variant-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variantInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VariantInfo in the database
        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVariantInfo() throws Exception {
        // Initialize the database
        variantInfoRepository.saveAndFlush(variantInfo);

        int databaseSizeBeforeDelete = variantInfoRepository.findAll().size();

        // Delete the variantInfo
        restVariantInfoMockMvc.perform(delete("/api/variant-infos/{id}", variantInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VariantInfo> variantInfoList = variantInfoRepository.findAll();
        assertThat(variantInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
