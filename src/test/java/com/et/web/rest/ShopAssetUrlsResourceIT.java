package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ShopAssetUrls;
import com.et.repository.ShopAssetUrlsRepository;
import com.et.service.ShopAssetUrlsService;
import com.et.service.dto.ShopAssetUrlsDTO;
import com.et.service.mapper.ShopAssetUrlsMapper;
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
 * Integration tests for the {@link ShopAssetUrlsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class ShopAssetUrlsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_JAVASCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_JAVASCRIPT = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_CSS = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_CSS = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_JAVASCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_JAVASCRIPT = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CSS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CSS = "BBBBBBBBBB";

    private static final String DEFAULT_BUNDLE_JAVASCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_BUNDLE_JAVASCRIPT = "BBBBBBBBBB";

    private static final String DEFAULT_BUNDLE_CSS = "AAAAAAAAAA";
    private static final String UPDATED_BUNDLE_CSS = "BBBBBBBBBB";

    @Autowired
    private ShopAssetUrlsRepository shopAssetUrlsRepository;

    @Autowired
    private ShopAssetUrlsMapper shopAssetUrlsMapper;

    @Autowired
    private ShopAssetUrlsService shopAssetUrlsService;

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

    private MockMvc restShopAssetUrlsMockMvc;

    private ShopAssetUrls shopAssetUrls;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopAssetUrlsResource shopAssetUrlsResource = new ShopAssetUrlsResource(shopAssetUrlsService);
        this.restShopAssetUrlsMockMvc = MockMvcBuilders.standaloneSetup(shopAssetUrlsResource)
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
    public static ShopAssetUrls createEntity(EntityManager em) {
        ShopAssetUrls shopAssetUrls = new ShopAssetUrls()
            .shop(DEFAULT_SHOP)
            .vendorJavascript(DEFAULT_VENDOR_JAVASCRIPT)
            .vendorCss(DEFAULT_VENDOR_CSS)
            .customerJavascript(DEFAULT_CUSTOMER_JAVASCRIPT)
            .customerCss(DEFAULT_CUSTOMER_CSS)
            .bundleJavascript(DEFAULT_BUNDLE_JAVASCRIPT)
            .bundleCss(DEFAULT_BUNDLE_CSS);
        return shopAssetUrls;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopAssetUrls createUpdatedEntity(EntityManager em) {
        ShopAssetUrls shopAssetUrls = new ShopAssetUrls()
            .shop(UPDATED_SHOP)
            .vendorJavascript(UPDATED_VENDOR_JAVASCRIPT)
            .vendorCss(UPDATED_VENDOR_CSS)
            .customerJavascript(UPDATED_CUSTOMER_JAVASCRIPT)
            .customerCss(UPDATED_CUSTOMER_CSS)
            .bundleJavascript(UPDATED_BUNDLE_JAVASCRIPT)
            .bundleCss(UPDATED_BUNDLE_CSS);
        return shopAssetUrls;
    }

    @BeforeEach
    public void initTest() {
        shopAssetUrls = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopAssetUrls() throws Exception {
        int databaseSizeBeforeCreate = shopAssetUrlsRepository.findAll().size();

        // Create the ShopAssetUrls
        ShopAssetUrlsDTO shopAssetUrlsDTO = shopAssetUrlsMapper.toDto(shopAssetUrls);
        restShopAssetUrlsMockMvc.perform(post("/api/shop-asset-urls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopAssetUrlsDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopAssetUrls in the database
        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeCreate + 1);
        ShopAssetUrls testShopAssetUrls = shopAssetUrlsList.get(shopAssetUrlsList.size() - 1);
        assertThat(testShopAssetUrls.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testShopAssetUrls.getVendorJavascript()).isEqualTo(DEFAULT_VENDOR_JAVASCRIPT);
        assertThat(testShopAssetUrls.getVendorCss()).isEqualTo(DEFAULT_VENDOR_CSS);
        assertThat(testShopAssetUrls.getCustomerJavascript()).isEqualTo(DEFAULT_CUSTOMER_JAVASCRIPT);
        assertThat(testShopAssetUrls.getCustomerCss()).isEqualTo(DEFAULT_CUSTOMER_CSS);
        assertThat(testShopAssetUrls.getBundleJavascript()).isEqualTo(DEFAULT_BUNDLE_JAVASCRIPT);
        assertThat(testShopAssetUrls.getBundleCss()).isEqualTo(DEFAULT_BUNDLE_CSS);
    }

    @Test
    @Transactional
    public void createShopAssetUrlsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopAssetUrlsRepository.findAll().size();

        // Create the ShopAssetUrls with an existing ID
        shopAssetUrls.setId(1L);
        ShopAssetUrlsDTO shopAssetUrlsDTO = shopAssetUrlsMapper.toDto(shopAssetUrls);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopAssetUrlsMockMvc.perform(post("/api/shop-asset-urls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopAssetUrlsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopAssetUrls in the database
        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopAssetUrlsRepository.findAll().size();
        // set the field null
        shopAssetUrls.setShop(null);

        // Create the ShopAssetUrls, which fails.
        ShopAssetUrlsDTO shopAssetUrlsDTO = shopAssetUrlsMapper.toDto(shopAssetUrls);

        restShopAssetUrlsMockMvc.perform(post("/api/shop-asset-urls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopAssetUrlsDTO)))
            .andExpect(status().isBadRequest());

        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShopAssetUrls() throws Exception {
        // Initialize the database
        shopAssetUrlsRepository.saveAndFlush(shopAssetUrls);

        // Get all the shopAssetUrlsList
        restShopAssetUrlsMockMvc.perform(get("/api/shop-asset-urls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopAssetUrls.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].vendorJavascript").value(hasItem(DEFAULT_VENDOR_JAVASCRIPT)))
            .andExpect(jsonPath("$.[*].vendorCss").value(hasItem(DEFAULT_VENDOR_CSS)))
            .andExpect(jsonPath("$.[*].customerJavascript").value(hasItem(DEFAULT_CUSTOMER_JAVASCRIPT)))
            .andExpect(jsonPath("$.[*].customerCss").value(hasItem(DEFAULT_CUSTOMER_CSS)))
            .andExpect(jsonPath("$.[*].bundleJavascript").value(hasItem(DEFAULT_BUNDLE_JAVASCRIPT)))
            .andExpect(jsonPath("$.[*].bundleCss").value(hasItem(DEFAULT_BUNDLE_CSS)));
    }
    
    @Test
    @Transactional
    public void getShopAssetUrls() throws Exception {
        // Initialize the database
        shopAssetUrlsRepository.saveAndFlush(shopAssetUrls);

        // Get the shopAssetUrls
        restShopAssetUrlsMockMvc.perform(get("/api/shop-asset-urls/{id}", shopAssetUrls.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopAssetUrls.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.vendorJavascript").value(DEFAULT_VENDOR_JAVASCRIPT))
            .andExpect(jsonPath("$.vendorCss").value(DEFAULT_VENDOR_CSS))
            .andExpect(jsonPath("$.customerJavascript").value(DEFAULT_CUSTOMER_JAVASCRIPT))
            .andExpect(jsonPath("$.customerCss").value(DEFAULT_CUSTOMER_CSS))
            .andExpect(jsonPath("$.bundleJavascript").value(DEFAULT_BUNDLE_JAVASCRIPT))
            .andExpect(jsonPath("$.bundleCss").value(DEFAULT_BUNDLE_CSS));
    }

    @Test
    @Transactional
    public void getNonExistingShopAssetUrls() throws Exception {
        // Get the shopAssetUrls
        restShopAssetUrlsMockMvc.perform(get("/api/shop-asset-urls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopAssetUrls() throws Exception {
        // Initialize the database
        shopAssetUrlsRepository.saveAndFlush(shopAssetUrls);

        int databaseSizeBeforeUpdate = shopAssetUrlsRepository.findAll().size();

        // Update the shopAssetUrls
        ShopAssetUrls updatedShopAssetUrls = shopAssetUrlsRepository.findById(shopAssetUrls.getId()).get();
        // Disconnect from session so that the updates on updatedShopAssetUrls are not directly saved in db
        em.detach(updatedShopAssetUrls);
        updatedShopAssetUrls
            .shop(UPDATED_SHOP)
            .vendorJavascript(UPDATED_VENDOR_JAVASCRIPT)
            .vendorCss(UPDATED_VENDOR_CSS)
            .customerJavascript(UPDATED_CUSTOMER_JAVASCRIPT)
            .customerCss(UPDATED_CUSTOMER_CSS)
            .bundleJavascript(UPDATED_BUNDLE_JAVASCRIPT)
            .bundleCss(UPDATED_BUNDLE_CSS);
        ShopAssetUrlsDTO shopAssetUrlsDTO = shopAssetUrlsMapper.toDto(updatedShopAssetUrls);

        restShopAssetUrlsMockMvc.perform(put("/api/shop-asset-urls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopAssetUrlsDTO)))
            .andExpect(status().isOk());

        // Validate the ShopAssetUrls in the database
        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeUpdate);
        ShopAssetUrls testShopAssetUrls = shopAssetUrlsList.get(shopAssetUrlsList.size() - 1);
        assertThat(testShopAssetUrls.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testShopAssetUrls.getVendorJavascript()).isEqualTo(UPDATED_VENDOR_JAVASCRIPT);
        assertThat(testShopAssetUrls.getVendorCss()).isEqualTo(UPDATED_VENDOR_CSS);
        assertThat(testShopAssetUrls.getCustomerJavascript()).isEqualTo(UPDATED_CUSTOMER_JAVASCRIPT);
        assertThat(testShopAssetUrls.getCustomerCss()).isEqualTo(UPDATED_CUSTOMER_CSS);
        assertThat(testShopAssetUrls.getBundleJavascript()).isEqualTo(UPDATED_BUNDLE_JAVASCRIPT);
        assertThat(testShopAssetUrls.getBundleCss()).isEqualTo(UPDATED_BUNDLE_CSS);
    }

    @Test
    @Transactional
    public void updateNonExistingShopAssetUrls() throws Exception {
        int databaseSizeBeforeUpdate = shopAssetUrlsRepository.findAll().size();

        // Create the ShopAssetUrls
        ShopAssetUrlsDTO shopAssetUrlsDTO = shopAssetUrlsMapper.toDto(shopAssetUrls);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopAssetUrlsMockMvc.perform(put("/api/shop-asset-urls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopAssetUrlsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopAssetUrls in the database
        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShopAssetUrls() throws Exception {
        // Initialize the database
        shopAssetUrlsRepository.saveAndFlush(shopAssetUrls);

        int databaseSizeBeforeDelete = shopAssetUrlsRepository.findAll().size();

        // Delete the shopAssetUrls
        restShopAssetUrlsMockMvc.perform(delete("/api/shop-asset-urls/{id}", shopAssetUrls.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShopAssetUrls> shopAssetUrlsList = shopAssetUrlsRepository.findAll();
        assertThat(shopAssetUrlsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
