package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ProductInfo;
import com.et.repository.ProductInfoRepository;
import com.et.service.ProductInfoService;
import com.et.service.dto.ProductInfoDTO;
import com.et.service.mapper.ProductInfoMapper;

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
 * Integration tests for the {@link ProductInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductInfoResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_HANDLE = "BBBBBBBBBB";

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductInfoMockMvc;

    private ProductInfo productInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInfo createEntity(EntityManager em) {
        ProductInfo productInfo = new ProductInfo()
            .shop(DEFAULT_SHOP)
            .productId(DEFAULT_PRODUCT_ID)
            .productTitle(DEFAULT_PRODUCT_TITLE)
            .productHandle(DEFAULT_PRODUCT_HANDLE);
        return productInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInfo createUpdatedEntity(EntityManager em) {
        ProductInfo productInfo = new ProductInfo()
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE)
            .productHandle(UPDATED_PRODUCT_HANDLE);
        return productInfo;
    }

    @BeforeEach
    public void initTest() {
        productInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductInfo() throws Exception {
        int databaseSizeBeforeCreate = productInfoRepository.findAll().size();
        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);
        restProductInfoMockMvc.perform(post("/api/product-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testProductInfo.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductInfo.getProductTitle()).isEqualTo(DEFAULT_PRODUCT_TITLE);
        assertThat(testProductInfo.getProductHandle()).isEqualTo(DEFAULT_PRODUCT_HANDLE);
    }

    @Test
    @Transactional
    public void createProductInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productInfoRepository.findAll().size();

        // Create the ProductInfo with an existing ID
        productInfo.setId(1L);
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInfoMockMvc.perform(post("/api/product-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = productInfoRepository.findAll().size();
        // set the field null
        productInfo.setShop(null);

        // Create the ProductInfo, which fails.
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);


        restProductInfoMockMvc.perform(post("/api/product-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductInfos() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get all the productInfoList
        restProductInfoMockMvc.perform(get("/api/product-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productTitle").value(hasItem(DEFAULT_PRODUCT_TITLE)))
            .andExpect(jsonPath("$.[*].productHandle").value(hasItem(DEFAULT_PRODUCT_HANDLE)));
    }
    
    @Test
    @Transactional
    public void getProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        // Get the productInfo
        restProductInfoMockMvc.perform(get("/api/product-infos/{id}", productInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productInfo.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productTitle").value(DEFAULT_PRODUCT_TITLE))
            .andExpect(jsonPath("$.productHandle").value(DEFAULT_PRODUCT_HANDLE));
    }
    @Test
    @Transactional
    public void getNonExistingProductInfo() throws Exception {
        // Get the productInfo
        restProductInfoMockMvc.perform(get("/api/product-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();

        // Update the productInfo
        ProductInfo updatedProductInfo = productInfoRepository.findById(productInfo.getId()).get();
        // Disconnect from session so that the updates on updatedProductInfo are not directly saved in db
        em.detach(updatedProductInfo);
        updatedProductInfo
            .shop(UPDATED_SHOP)
            .productId(UPDATED_PRODUCT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE)
            .productHandle(UPDATED_PRODUCT_HANDLE);
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(updatedProductInfo);

        restProductInfoMockMvc.perform(put("/api/product-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isOk());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
        ProductInfo testProductInfo = productInfoList.get(productInfoList.size() - 1);
        assertThat(testProductInfo.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testProductInfo.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductInfo.getProductTitle()).isEqualTo(UPDATED_PRODUCT_TITLE);
        assertThat(testProductInfo.getProductHandle()).isEqualTo(UPDATED_PRODUCT_HANDLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProductInfo() throws Exception {
        int databaseSizeBeforeUpdate = productInfoRepository.findAll().size();

        // Create the ProductInfo
        ProductInfoDTO productInfoDTO = productInfoMapper.toDto(productInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInfoMockMvc.perform(put("/api/product-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductInfo in the database
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductInfo() throws Exception {
        // Initialize the database
        productInfoRepository.saveAndFlush(productInfo);

        int databaseSizeBeforeDelete = productInfoRepository.findAll().size();

        // Delete the productInfo
        restProductInfoMockMvc.perform(delete("/api/product-infos/{id}", productInfo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductInfo> productInfoList = productInfoRepository.findAll();
        assertThat(productInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
