package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.CartWidgetSettings;
import com.et.repository.CartWidgetSettingsRepository;
import com.et.service.CartWidgetSettingsService;

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

import com.et.domain.enumeration.CartWidgetSettingApproach;
import com.et.domain.enumeration.PlacementPosition;

/**
 * Integration tests for the {@link CartWidgetSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CartWidgetSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_CART_WIDGET_SETTINGS = false;
    private static final Boolean UPDATED_ENABLE_CART_WIDGET_SETTINGS = true;

    private static final CartWidgetSettingApproach DEFAULT_CART_WIDGET_SETTING_APPROACH = CartWidgetSettingApproach.V1;
    private static final CartWidgetSettingApproach UPDATED_CART_WIDGET_SETTING_APPROACH = CartWidgetSettingApproach.V2;

    private static final String DEFAULT_CART_ROW_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_ROW_SELECTOR = "BBBBBBBBBB";

    private static final PlacementPosition DEFAULT_CART_ROW_PLACEMENT = PlacementPosition.BEFORE;
    private static final PlacementPosition UPDATED_CART_ROW_PLACEMENT = PlacementPosition.AFTER;

    private static final String DEFAULT_CART_LINE_ITEM_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_SELECTOR = "BBBBBBBBBB";

    private static final PlacementPosition DEFAULT_CART_LINE_ITEM_PLACEMENT = PlacementPosition.BEFORE;
    private static final PlacementPosition UPDATED_CART_LINE_ITEM_PLACEMENT = PlacementPosition.AFTER;

    private static final String DEFAULT_CART_FORM_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_FORM_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_APPSTEL_CUSTOME_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_APPSTEL_CUSTOME_SELECTOR = "BBBBBBBBBB";

    @Autowired
    private CartWidgetSettingsRepository cartWidgetSettingsRepository;

    @Autowired
    private CartWidgetSettingsService cartWidgetSettingsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartWidgetSettingsMockMvc;

    private CartWidgetSettings cartWidgetSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartWidgetSettings createEntity(EntityManager em) {
        CartWidgetSettings cartWidgetSettings = new CartWidgetSettings()
            .shop(DEFAULT_SHOP)
            .enable_cart_widget_settings(DEFAULT_ENABLE_CART_WIDGET_SETTINGS)
            .cartWidgetSettingApproach(DEFAULT_CART_WIDGET_SETTING_APPROACH)
            .cartRowSelector(DEFAULT_CART_ROW_SELECTOR)
            .cartRowPlacement(DEFAULT_CART_ROW_PLACEMENT)
            .cartLineItemSelector(DEFAULT_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPlacement(DEFAULT_CART_LINE_ITEM_PLACEMENT)
            .cartFormSelector(DEFAULT_CART_FORM_SELECTOR)
            .appstelCustomeSelector(DEFAULT_APPSTEL_CUSTOME_SELECTOR);
        return cartWidgetSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartWidgetSettings createUpdatedEntity(EntityManager em) {
        CartWidgetSettings cartWidgetSettings = new CartWidgetSettings()
            .shop(UPDATED_SHOP)
            .enable_cart_widget_settings(UPDATED_ENABLE_CART_WIDGET_SETTINGS)
            .cartWidgetSettingApproach(UPDATED_CART_WIDGET_SETTING_APPROACH)
            .cartRowSelector(UPDATED_CART_ROW_SELECTOR)
            .cartRowPlacement(UPDATED_CART_ROW_PLACEMENT)
            .cartLineItemSelector(UPDATED_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPlacement(UPDATED_CART_LINE_ITEM_PLACEMENT)
            .cartFormSelector(UPDATED_CART_FORM_SELECTOR)
            .appstelCustomeSelector(UPDATED_APPSTEL_CUSTOME_SELECTOR);
        return cartWidgetSettings;
    }

    @BeforeEach
    public void initTest() {
        cartWidgetSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartWidgetSettings() throws Exception {
        int databaseSizeBeforeCreate = cartWidgetSettingsRepository.findAll().size();
        // Create the CartWidgetSettings
        restCartWidgetSettingsMockMvc.perform(post("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cartWidgetSettings)))
            .andExpect(status().isCreated());

        // Validate the CartWidgetSettings in the database
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        CartWidgetSettings testCartWidgetSettings = cartWidgetSettingsList.get(cartWidgetSettingsList.size() - 1);
        assertThat(testCartWidgetSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testCartWidgetSettings.isEnable_cart_widget_settings()).isEqualTo(DEFAULT_ENABLE_CART_WIDGET_SETTINGS);
        assertThat(testCartWidgetSettings.getCartWidgetSettingApproach()).isEqualTo(DEFAULT_CART_WIDGET_SETTING_APPROACH);
        assertThat(testCartWidgetSettings.getCartRowSelector()).isEqualTo(DEFAULT_CART_ROW_SELECTOR);
        assertThat(testCartWidgetSettings.getCartRowPlacement()).isEqualTo(DEFAULT_CART_ROW_PLACEMENT);
        assertThat(testCartWidgetSettings.getCartLineItemSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_SELECTOR);
        assertThat(testCartWidgetSettings.getCartLineItemPlacement()).isEqualTo(DEFAULT_CART_LINE_ITEM_PLACEMENT);
        assertThat(testCartWidgetSettings.getCartFormSelector()).isEqualTo(DEFAULT_CART_FORM_SELECTOR);
        assertThat(testCartWidgetSettings.getAppstelCustomeSelector()).isEqualTo(DEFAULT_APPSTEL_CUSTOME_SELECTOR);
    }

    @Test
    @Transactional
    public void createCartWidgetSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartWidgetSettingsRepository.findAll().size();

        // Create the CartWidgetSettings with an existing ID
        cartWidgetSettings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartWidgetSettingsMockMvc.perform(post("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cartWidgetSettings)))
            .andExpect(status().isBadRequest());

        // Validate the CartWidgetSettings in the database
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartWidgetSettingsRepository.findAll().size();
        // set the field null
        cartWidgetSettings.setShop(null);

        // Create the CartWidgetSettings, which fails.


        restCartWidgetSettingsMockMvc.perform(post("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cartWidgetSettings)))
            .andExpect(status().isBadRequest());

        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnable_cart_widget_settingsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartWidgetSettingsRepository.findAll().size();
        // set the field null
        cartWidgetSettings.setEnable_cart_widget_settings(null);

        // Create the CartWidgetSettings, which fails.


        restCartWidgetSettingsMockMvc.perform(post("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cartWidgetSettings)))
            .andExpect(status().isBadRequest());

        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCartWidgetSettings() throws Exception {
        // Initialize the database
        cartWidgetSettingsRepository.saveAndFlush(cartWidgetSettings);

        // Get all the cartWidgetSettingsList
        restCartWidgetSettingsMockMvc.perform(get("/api/cart-widget-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartWidgetSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].enable_cart_widget_settings").value(hasItem(DEFAULT_ENABLE_CART_WIDGET_SETTINGS.booleanValue())))
            .andExpect(jsonPath("$.[*].cartWidgetSettingApproach").value(hasItem(DEFAULT_CART_WIDGET_SETTING_APPROACH.toString())))
            .andExpect(jsonPath("$.[*].cartRowSelector").value(hasItem(DEFAULT_CART_ROW_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartRowPlacement").value(hasItem(DEFAULT_CART_ROW_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemPlacement").value(hasItem(DEFAULT_CART_LINE_ITEM_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].cartFormSelector").value(hasItem(DEFAULT_CART_FORM_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].appstelCustomeSelector").value(hasItem(DEFAULT_APPSTEL_CUSTOME_SELECTOR.toString())));
    }

    @Test
    @Transactional
    public void getCartWidgetSettings() throws Exception {
        // Initialize the database
        cartWidgetSettingsRepository.saveAndFlush(cartWidgetSettings);

        // Get the cartWidgetSettings
        restCartWidgetSettingsMockMvc.perform(get("/api/cart-widget-settings/{id}", cartWidgetSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartWidgetSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.enable_cart_widget_settings").value(DEFAULT_ENABLE_CART_WIDGET_SETTINGS.booleanValue()))
            .andExpect(jsonPath("$.cartWidgetSettingApproach").value(DEFAULT_CART_WIDGET_SETTING_APPROACH.toString()))
            .andExpect(jsonPath("$.cartRowSelector").value(DEFAULT_CART_ROW_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartRowPlacement").value(DEFAULT_CART_ROW_PLACEMENT.toString()))
            .andExpect(jsonPath("$.cartLineItemSelector").value(DEFAULT_CART_LINE_ITEM_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemPlacement").value(DEFAULT_CART_LINE_ITEM_PLACEMENT.toString()))
            .andExpect(jsonPath("$.cartFormSelector").value(DEFAULT_CART_FORM_SELECTOR.toString()))
            .andExpect(jsonPath("$.appstelCustomeSelector").value(DEFAULT_APPSTEL_CUSTOME_SELECTOR.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCartWidgetSettings() throws Exception {
        // Get the cartWidgetSettings
        restCartWidgetSettingsMockMvc.perform(get("/api/cart-widget-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartWidgetSettings() throws Exception {
        // Initialize the database
        cartWidgetSettingsService.save(cartWidgetSettings);

        int databaseSizeBeforeUpdate = cartWidgetSettingsRepository.findAll().size();

        // Update the cartWidgetSettings
        CartWidgetSettings updatedCartWidgetSettings = cartWidgetSettingsRepository.findById(cartWidgetSettings.getId()).get();
        // Disconnect from session so that the updates on updatedCartWidgetSettings are not directly saved in db
        em.detach(updatedCartWidgetSettings);
        updatedCartWidgetSettings
            .shop(UPDATED_SHOP)
            .enable_cart_widget_settings(UPDATED_ENABLE_CART_WIDGET_SETTINGS)
            .cartWidgetSettingApproach(UPDATED_CART_WIDGET_SETTING_APPROACH)
            .cartRowSelector(UPDATED_CART_ROW_SELECTOR)
            .cartRowPlacement(UPDATED_CART_ROW_PLACEMENT)
            .cartLineItemSelector(UPDATED_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPlacement(UPDATED_CART_LINE_ITEM_PLACEMENT)
            .cartFormSelector(UPDATED_CART_FORM_SELECTOR)
            .appstelCustomeSelector(UPDATED_APPSTEL_CUSTOME_SELECTOR);

        restCartWidgetSettingsMockMvc.perform(put("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartWidgetSettings)))
            .andExpect(status().isOk());

        // Validate the CartWidgetSettings in the database
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeUpdate);
        CartWidgetSettings testCartWidgetSettings = cartWidgetSettingsList.get(cartWidgetSettingsList.size() - 1);
        assertThat(testCartWidgetSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testCartWidgetSettings.isEnable_cart_widget_settings()).isEqualTo(UPDATED_ENABLE_CART_WIDGET_SETTINGS);
        assertThat(testCartWidgetSettings.getCartWidgetSettingApproach()).isEqualTo(UPDATED_CART_WIDGET_SETTING_APPROACH);
        assertThat(testCartWidgetSettings.getCartRowSelector()).isEqualTo(UPDATED_CART_ROW_SELECTOR);
        assertThat(testCartWidgetSettings.getCartRowPlacement()).isEqualTo(UPDATED_CART_ROW_PLACEMENT);
        assertThat(testCartWidgetSettings.getCartLineItemSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_SELECTOR);
        assertThat(testCartWidgetSettings.getCartLineItemPlacement()).isEqualTo(UPDATED_CART_LINE_ITEM_PLACEMENT);
        assertThat(testCartWidgetSettings.getCartFormSelector()).isEqualTo(UPDATED_CART_FORM_SELECTOR);
        assertThat(testCartWidgetSettings.getAppstelCustomeSelector()).isEqualTo(UPDATED_APPSTEL_CUSTOME_SELECTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCartWidgetSettings() throws Exception {
        int databaseSizeBeforeUpdate = cartWidgetSettingsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartWidgetSettingsMockMvc.perform(put("/api/cart-widget-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cartWidgetSettings)))
            .andExpect(status().isBadRequest());

        // Validate the CartWidgetSettings in the database
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCartWidgetSettings() throws Exception {
        // Initialize the database
        cartWidgetSettingsService.save(cartWidgetSettings);

        int databaseSizeBeforeDelete = cartWidgetSettingsRepository.findAll().size();

        // Delete the cartWidgetSettings
        restCartWidgetSettingsMockMvc.perform(delete("/api/cart-widget-settings/{id}", cartWidgetSettings.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsRepository.findAll();
        assertThat(cartWidgetSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
