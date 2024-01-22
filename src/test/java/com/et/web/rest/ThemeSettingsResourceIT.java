package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ThemeSettings;
import com.et.repository.ThemeSettingsRepository;
import com.et.service.ThemeSettingsService;
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

import com.et.domain.enumeration.ShopifyThemeInstallationVersion;
import com.et.domain.enumeration.PlacementPosition;
import com.et.domain.enumeration.PlacementPosition;
import com.et.domain.enumeration.PlacementPosition;
import com.et.domain.enumeration.WidgetTemplateType;
/**
 * Integration tests for the {@link ThemeSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class ThemeSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SKIP_SETTING_THEME = false;
    private static final Boolean UPDATED_SKIP_SETTING_THEME = true;

    private static final Boolean DEFAULT_THEME_V_2_SAVED = false;
    private static final Boolean UPDATED_THEME_V_2_SAVED = true;

    private static final String DEFAULT_THEME_NAME = "AAAAAAAAAA";
    private static final String UPDATED_THEME_NAME = "BBBBBBBBBB";

    private static final ShopifyThemeInstallationVersion DEFAULT_SHOPIFY_THEME_INSTALLATION_VERSION = ShopifyThemeInstallationVersion.V1;
    private static final ShopifyThemeInstallationVersion UPDATED_SHOPIFY_THEME_INSTALLATION_VERSION = ShopifyThemeInstallationVersion.V2;

    private static final String DEFAULT_SELECTED_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_LINK_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_LINK_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_JAVASCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_JAVASCRIPT = "BBBBBBBBBB";

    private static final PlacementPosition DEFAULT_PLACEMENT = PlacementPosition.BEFORE;
    private static final PlacementPosition UPDATED_PLACEMENT = PlacementPosition.AFTER;

    private static final PlacementPosition DEFAULT_SUBSCRIPTION_LINK_PLACEMENT = PlacementPosition.BEFORE;
    private static final PlacementPosition UPDATED_SUBSCRIPTION_LINK_PLACEMENT = PlacementPosition.AFTER;

    private static final String DEFAULT_PRICE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_SELECTOR = "BBBBBBBBBB";

    private static final PlacementPosition DEFAULT_PRICE_PLACEMENT = PlacementPosition.BEFORE;
    private static final PlacementPosition UPDATED_PRICE_PLACEMENT = PlacementPosition.AFTER;

    private static final String DEFAULT_BADGE_TOP = "AAAAAAAAAA";
    private static final String UPDATED_BADGE_TOP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISABLE_LOADING_JQUERY = false;
    private static final Boolean UPDATED_DISABLE_LOADING_JQUERY = true;

    private static final String DEFAULT_QUICK_VIEW_CLICK_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_QUICK_VIEW_CLICK_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_LANDING_PAGE_PRICE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_LANDING_PAGE_PRICE_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_ROW_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_ROW_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_LINE_ITEM_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_SUB_TOTAL_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_SUB_TOTAL_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CART_LINE_ITEM_PRICE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_CART_LINE_ITEM_PRICE_SELECTOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_CART_WIDGET_FEATURE = false;
    private static final Boolean UPDATED_ENABLE_CART_WIDGET_FEATURE = true;

    private static final Boolean DEFAULT_ENABLE_SLOW_SCRIPT_LOAD = false;
    private static final Boolean UPDATED_ENABLE_SLOW_SCRIPT_LOAD = true;

    private static final Integer DEFAULT_SCRIPT_LOAD_DELAY = 1;
    private static final Integer UPDATED_SCRIPT_LOAD_DELAY = 2;

    private static final Boolean DEFAULT_FORMAT_MONEY_OVERRIDE = false;
    private static final Boolean UPDATED_FORMAT_MONEY_OVERRIDE = true;

    private static final WidgetTemplateType DEFAULT_WIDGET_TEMPLATE_TYPE = WidgetTemplateType.WIDGET_TYPE_1;
    private static final WidgetTemplateType UPDATED_WIDGET_TEMPLATE_TYPE = WidgetTemplateType.WIDGET_TYPE_2;

    private static final String DEFAULT_WIDGET_TEMPLATE_HTML = "AAAAAAAAAA";
    private static final String UPDATED_WIDGET_TEMPLATE_HTML = "BBBBBBBBBB";

    @Autowired
    private ThemeSettingsRepository themeSettingsRepository;

    @Autowired
    private ThemeSettingsService themeSettingsService;

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

    private MockMvc restThemeSettingsMockMvc;

    private ThemeSettings themeSettings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ThemeSettingsResource themeSettingsResource = new ThemeSettingsResource(themeSettingsService);
        this.restThemeSettingsMockMvc = MockMvcBuilders.standaloneSetup(themeSettingsResource)
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
    public static ThemeSettings createEntity(EntityManager em) {
        ThemeSettings themeSettings = new ThemeSettings()
            .shop(DEFAULT_SHOP)
            .skip_setting_theme(DEFAULT_SKIP_SETTING_THEME)
            .themeV2Saved(DEFAULT_THEME_V_2_SAVED)
            .themeName(DEFAULT_THEME_NAME)
            .shopifyThemeInstallationVersion(DEFAULT_SHOPIFY_THEME_INSTALLATION_VERSION)
            .selectedSelector(DEFAULT_SELECTED_SELECTOR)
            .subscriptionLinkSelector(DEFAULT_SUBSCRIPTION_LINK_SELECTOR)
            .customJavascript(DEFAULT_CUSTOM_JAVASCRIPT)
            .placement(DEFAULT_PLACEMENT)
            .subscriptionLinkPlacement(DEFAULT_SUBSCRIPTION_LINK_PLACEMENT)
            .priceSelector(DEFAULT_PRICE_SELECTOR)
            .pricePlacement(DEFAULT_PRICE_PLACEMENT)
            .badgeTop(DEFAULT_BADGE_TOP)
            .disableLoadingJquery(DEFAULT_DISABLE_LOADING_JQUERY)
            .quickViewClickSelector(DEFAULT_QUICK_VIEW_CLICK_SELECTOR)
            .landingPagePriceSelector(DEFAULT_LANDING_PAGE_PRICE_SELECTOR)
            .cartRowSelector(DEFAULT_CART_ROW_SELECTOR)
            .cartLineItemSelector(DEFAULT_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPerQuantityPriceSelector(DEFAULT_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR)
            .cartLineItemTotalPriceSelector(DEFAULT_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR)
            .cartLineItemSellingPlanNameSelector(DEFAULT_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR)
            .cartSubTotalSelector(DEFAULT_CART_SUB_TOTAL_SELECTOR)
            .cartLineItemPriceSelector(DEFAULT_CART_LINE_ITEM_PRICE_SELECTOR)
            .enableCartWidgetFeature(DEFAULT_ENABLE_CART_WIDGET_FEATURE)
            .enableSlowScriptLoad(DEFAULT_ENABLE_SLOW_SCRIPT_LOAD)
            .scriptLoadDelay(DEFAULT_SCRIPT_LOAD_DELAY)
            .formatMoneyOverride(DEFAULT_FORMAT_MONEY_OVERRIDE)
            .widgetTemplateType(DEFAULT_WIDGET_TEMPLATE_TYPE)
            .widgetTemplateHtml(DEFAULT_WIDGET_TEMPLATE_HTML);
        return themeSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeSettings createUpdatedEntity(EntityManager em) {
        ThemeSettings themeSettings = new ThemeSettings()
            .shop(UPDATED_SHOP)
            .skip_setting_theme(UPDATED_SKIP_SETTING_THEME)
            .themeV2Saved(UPDATED_THEME_V_2_SAVED)
            .themeName(UPDATED_THEME_NAME)
            .shopifyThemeInstallationVersion(UPDATED_SHOPIFY_THEME_INSTALLATION_VERSION)
            .selectedSelector(UPDATED_SELECTED_SELECTOR)
            .subscriptionLinkSelector(UPDATED_SUBSCRIPTION_LINK_SELECTOR)
            .customJavascript(UPDATED_CUSTOM_JAVASCRIPT)
            .placement(UPDATED_PLACEMENT)
            .subscriptionLinkPlacement(UPDATED_SUBSCRIPTION_LINK_PLACEMENT)
            .priceSelector(UPDATED_PRICE_SELECTOR)
            .pricePlacement(UPDATED_PRICE_PLACEMENT)
            .badgeTop(UPDATED_BADGE_TOP)
            .disableLoadingJquery(UPDATED_DISABLE_LOADING_JQUERY)
            .quickViewClickSelector(UPDATED_QUICK_VIEW_CLICK_SELECTOR)
            .landingPagePriceSelector(UPDATED_LANDING_PAGE_PRICE_SELECTOR)
            .cartRowSelector(UPDATED_CART_ROW_SELECTOR)
            .cartLineItemSelector(UPDATED_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPerQuantityPriceSelector(UPDATED_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR)
            .cartLineItemTotalPriceSelector(UPDATED_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR)
            .cartLineItemSellingPlanNameSelector(UPDATED_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR)
            .cartSubTotalSelector(UPDATED_CART_SUB_TOTAL_SELECTOR)
            .cartLineItemPriceSelector(UPDATED_CART_LINE_ITEM_PRICE_SELECTOR)
            .enableCartWidgetFeature(UPDATED_ENABLE_CART_WIDGET_FEATURE)
            .enableSlowScriptLoad(UPDATED_ENABLE_SLOW_SCRIPT_LOAD)
            .scriptLoadDelay(UPDATED_SCRIPT_LOAD_DELAY)
            .formatMoneyOverride(UPDATED_FORMAT_MONEY_OVERRIDE)
            .widgetTemplateType(UPDATED_WIDGET_TEMPLATE_TYPE)
            .widgetTemplateHtml(UPDATED_WIDGET_TEMPLATE_HTML);
        return themeSettings;
    }

    @BeforeEach
    public void initTest() {
        themeSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createThemeSettings() throws Exception {
        int databaseSizeBeforeCreate = themeSettingsRepository.findAll().size();

        // Create the ThemeSettings
        restThemeSettingsMockMvc.perform(post("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeSettings)))
            .andExpect(status().isCreated());

        // Validate the ThemeSettings in the database
        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        ThemeSettings testThemeSettings = themeSettingsList.get(themeSettingsList.size() - 1);
        assertThat(testThemeSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testThemeSettings.isSkip_setting_theme()).isEqualTo(DEFAULT_SKIP_SETTING_THEME);
        assertThat(testThemeSettings.isThemeV2Saved()).isEqualTo(DEFAULT_THEME_V_2_SAVED);
        assertThat(testThemeSettings.getThemeName()).isEqualTo(DEFAULT_THEME_NAME);
        assertThat(testThemeSettings.getShopifyThemeInstallationVersion()).isEqualTo(DEFAULT_SHOPIFY_THEME_INSTALLATION_VERSION);
        assertThat(testThemeSettings.getSelectedSelector()).isEqualTo(DEFAULT_SELECTED_SELECTOR);
        assertThat(testThemeSettings.getSubscriptionLinkSelector()).isEqualTo(DEFAULT_SUBSCRIPTION_LINK_SELECTOR);
        assertThat(testThemeSettings.getCustomJavascript()).isEqualTo(DEFAULT_CUSTOM_JAVASCRIPT);
        assertThat(testThemeSettings.getPlacement()).isEqualTo(DEFAULT_PLACEMENT);
        assertThat(testThemeSettings.getSubscriptionLinkPlacement()).isEqualTo(DEFAULT_SUBSCRIPTION_LINK_PLACEMENT);
        assertThat(testThemeSettings.getPriceSelector()).isEqualTo(DEFAULT_PRICE_SELECTOR);
        assertThat(testThemeSettings.getPricePlacement()).isEqualTo(DEFAULT_PRICE_PLACEMENT);
        assertThat(testThemeSettings.getBadgeTop()).isEqualTo(DEFAULT_BADGE_TOP);
        assertThat(testThemeSettings.isDisableLoadingJquery()).isEqualTo(DEFAULT_DISABLE_LOADING_JQUERY);
        assertThat(testThemeSettings.getQuickViewClickSelector()).isEqualTo(DEFAULT_QUICK_VIEW_CLICK_SELECTOR);
        assertThat(testThemeSettings.getLandingPagePriceSelector()).isEqualTo(DEFAULT_LANDING_PAGE_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartRowSelector()).isEqualTo(DEFAULT_CART_ROW_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemPerQuantityPriceSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemTotalPriceSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemSellingPlanNameSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR);
        assertThat(testThemeSettings.getCartSubTotalSelector()).isEqualTo(DEFAULT_CART_SUB_TOTAL_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemPriceSelector()).isEqualTo(DEFAULT_CART_LINE_ITEM_PRICE_SELECTOR);
        assertThat(testThemeSettings.isEnableCartWidgetFeature()).isEqualTo(DEFAULT_ENABLE_CART_WIDGET_FEATURE);
        assertThat(testThemeSettings.isEnableSlowScriptLoad()).isEqualTo(DEFAULT_ENABLE_SLOW_SCRIPT_LOAD);
        assertThat(testThemeSettings.getScriptLoadDelay()).isEqualTo(DEFAULT_SCRIPT_LOAD_DELAY);
        assertThat(testThemeSettings.isFormatMoneyOverride()).isEqualTo(DEFAULT_FORMAT_MONEY_OVERRIDE);
        assertThat(testThemeSettings.getWidgetTemplateType()).isEqualTo(DEFAULT_WIDGET_TEMPLATE_TYPE);
        assertThat(testThemeSettings.getWidgetTemplateHtml()).isEqualTo(DEFAULT_WIDGET_TEMPLATE_HTML);
    }

    @Test
    @Transactional
    public void createThemeSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = themeSettingsRepository.findAll().size();

        // Create the ThemeSettings with an existing ID
        themeSettings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeSettingsMockMvc.perform(post("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeSettings)))
            .andExpect(status().isBadRequest());

        // Validate the ThemeSettings in the database
        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeSettingsRepository.findAll().size();
        // set the field null
        themeSettings.setShop(null);

        // Create the ThemeSettings, which fails.

        restThemeSettingsMockMvc.perform(post("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeSettings)))
            .andExpect(status().isBadRequest());

        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSkip_setting_themeIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeSettingsRepository.findAll().size();
        // set the field null
        themeSettings.setSkip_setting_theme(null);

        // Create the ThemeSettings, which fails.

        restThemeSettingsMockMvc.perform(post("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeSettings)))
            .andExpect(status().isBadRequest());

        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllThemeSettings() throws Exception {
        // Initialize the database
        themeSettingsRepository.saveAndFlush(themeSettings);

        // Get all the themeSettingsList
        restThemeSettingsMockMvc.perform(get("/api/theme-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].skip_setting_theme").value(hasItem(DEFAULT_SKIP_SETTING_THEME.booleanValue())))
            .andExpect(jsonPath("$.[*].themeV2Saved").value(hasItem(DEFAULT_THEME_V_2_SAVED.booleanValue())))
            .andExpect(jsonPath("$.[*].themeName").value(hasItem(DEFAULT_THEME_NAME)))
            .andExpect(jsonPath("$.[*].shopifyThemeInstallationVersion").value(hasItem(DEFAULT_SHOPIFY_THEME_INSTALLATION_VERSION.toString())))
            .andExpect(jsonPath("$.[*].selectedSelector").value(hasItem(DEFAULT_SELECTED_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].subscriptionLinkSelector").value(hasItem(DEFAULT_SUBSCRIPTION_LINK_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].customJavascript").value(hasItem(DEFAULT_CUSTOM_JAVASCRIPT.toString())))
            .andExpect(jsonPath("$.[*].placement").value(hasItem(DEFAULT_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].subscriptionLinkPlacement").value(hasItem(DEFAULT_SUBSCRIPTION_LINK_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].priceSelector").value(hasItem(DEFAULT_PRICE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].pricePlacement").value(hasItem(DEFAULT_PRICE_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].badgeTop").value(hasItem(DEFAULT_BADGE_TOP.toString())))
            .andExpect(jsonPath("$.[*].disableLoadingJquery").value(hasItem(DEFAULT_DISABLE_LOADING_JQUERY.booleanValue())))
            .andExpect(jsonPath("$.[*].quickViewClickSelector").value(hasItem(DEFAULT_QUICK_VIEW_CLICK_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].landingPagePriceSelector").value(hasItem(DEFAULT_LANDING_PAGE_PRICE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartRowSelector").value(hasItem(DEFAULT_CART_ROW_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemPerQuantityPriceSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemTotalPriceSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemSellingPlanNameSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartSubTotalSelector").value(hasItem(DEFAULT_CART_SUB_TOTAL_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].cartLineItemPriceSelector").value(hasItem(DEFAULT_CART_LINE_ITEM_PRICE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].enableCartWidgetFeature").value(hasItem(DEFAULT_ENABLE_CART_WIDGET_FEATURE.booleanValue())))
            .andExpect(jsonPath("$.[*].enableSlowScriptLoad").value(hasItem(DEFAULT_ENABLE_SLOW_SCRIPT_LOAD.booleanValue())))
            .andExpect(jsonPath("$.[*].scriptLoadDelay").value(hasItem(DEFAULT_SCRIPT_LOAD_DELAY)))
            .andExpect(jsonPath("$.[*].formatMoneyOverride").value(hasItem(DEFAULT_FORMAT_MONEY_OVERRIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].widgetTemplateType").value(hasItem(DEFAULT_WIDGET_TEMPLATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].widgetTemplateHtml").value(hasItem(DEFAULT_WIDGET_TEMPLATE_HTML.toString())));
    }
    
    @Test
    @Transactional
    public void getThemeSettings() throws Exception {
        // Initialize the database
        themeSettingsRepository.saveAndFlush(themeSettings);

        // Get the themeSettings
        restThemeSettingsMockMvc.perform(get("/api/theme-settings/{id}", themeSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(themeSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.skip_setting_theme").value(DEFAULT_SKIP_SETTING_THEME.booleanValue()))
            .andExpect(jsonPath("$.themeV2Saved").value(DEFAULT_THEME_V_2_SAVED.booleanValue()))
            .andExpect(jsonPath("$.themeName").value(DEFAULT_THEME_NAME))
            .andExpect(jsonPath("$.shopifyThemeInstallationVersion").value(DEFAULT_SHOPIFY_THEME_INSTALLATION_VERSION.toString()))
            .andExpect(jsonPath("$.selectedSelector").value(DEFAULT_SELECTED_SELECTOR.toString()))
            .andExpect(jsonPath("$.subscriptionLinkSelector").value(DEFAULT_SUBSCRIPTION_LINK_SELECTOR.toString()))
            .andExpect(jsonPath("$.customJavascript").value(DEFAULT_CUSTOM_JAVASCRIPT.toString()))
            .andExpect(jsonPath("$.placement").value(DEFAULT_PLACEMENT.toString()))
            .andExpect(jsonPath("$.subscriptionLinkPlacement").value(DEFAULT_SUBSCRIPTION_LINK_PLACEMENT.toString()))
            .andExpect(jsonPath("$.priceSelector").value(DEFAULT_PRICE_SELECTOR.toString()))
            .andExpect(jsonPath("$.pricePlacement").value(DEFAULT_PRICE_PLACEMENT.toString()))
            .andExpect(jsonPath("$.badgeTop").value(DEFAULT_BADGE_TOP.toString()))
            .andExpect(jsonPath("$.disableLoadingJquery").value(DEFAULT_DISABLE_LOADING_JQUERY.booleanValue()))
            .andExpect(jsonPath("$.quickViewClickSelector").value(DEFAULT_QUICK_VIEW_CLICK_SELECTOR.toString()))
            .andExpect(jsonPath("$.landingPagePriceSelector").value(DEFAULT_LANDING_PAGE_PRICE_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartRowSelector").value(DEFAULT_CART_ROW_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemSelector").value(DEFAULT_CART_LINE_ITEM_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemPerQuantityPriceSelector").value(DEFAULT_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemTotalPriceSelector").value(DEFAULT_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemSellingPlanNameSelector").value(DEFAULT_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartSubTotalSelector").value(DEFAULT_CART_SUB_TOTAL_SELECTOR.toString()))
            .andExpect(jsonPath("$.cartLineItemPriceSelector").value(DEFAULT_CART_LINE_ITEM_PRICE_SELECTOR.toString()))
            .andExpect(jsonPath("$.enableCartWidgetFeature").value(DEFAULT_ENABLE_CART_WIDGET_FEATURE.booleanValue()))
            .andExpect(jsonPath("$.enableSlowScriptLoad").value(DEFAULT_ENABLE_SLOW_SCRIPT_LOAD.booleanValue()))
            .andExpect(jsonPath("$.scriptLoadDelay").value(DEFAULT_SCRIPT_LOAD_DELAY))
            .andExpect(jsonPath("$.formatMoneyOverride").value(DEFAULT_FORMAT_MONEY_OVERRIDE.booleanValue()))
            .andExpect(jsonPath("$.widgetTemplateType").value(DEFAULT_WIDGET_TEMPLATE_TYPE.toString()))
            .andExpect(jsonPath("$.widgetTemplateHtml").value(DEFAULT_WIDGET_TEMPLATE_HTML.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingThemeSettings() throws Exception {
        // Get the themeSettings
        restThemeSettingsMockMvc.perform(get("/api/theme-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThemeSettings() throws Exception {
        // Initialize the database
        themeSettingsService.save(themeSettings);

        int databaseSizeBeforeUpdate = themeSettingsRepository.findAll().size();

        // Update the themeSettings
        ThemeSettings updatedThemeSettings = themeSettingsRepository.findById(themeSettings.getId()).get();
        // Disconnect from session so that the updates on updatedThemeSettings are not directly saved in db
        em.detach(updatedThemeSettings);
        updatedThemeSettings
            .shop(UPDATED_SHOP)
            .skip_setting_theme(UPDATED_SKIP_SETTING_THEME)
            .themeV2Saved(UPDATED_THEME_V_2_SAVED)
            .themeName(UPDATED_THEME_NAME)
            .shopifyThemeInstallationVersion(UPDATED_SHOPIFY_THEME_INSTALLATION_VERSION)
            .selectedSelector(UPDATED_SELECTED_SELECTOR)
            .subscriptionLinkSelector(UPDATED_SUBSCRIPTION_LINK_SELECTOR)
            .customJavascript(UPDATED_CUSTOM_JAVASCRIPT)
            .placement(UPDATED_PLACEMENT)
            .subscriptionLinkPlacement(UPDATED_SUBSCRIPTION_LINK_PLACEMENT)
            .priceSelector(UPDATED_PRICE_SELECTOR)
            .pricePlacement(UPDATED_PRICE_PLACEMENT)
            .badgeTop(UPDATED_BADGE_TOP)
            .disableLoadingJquery(UPDATED_DISABLE_LOADING_JQUERY)
            .quickViewClickSelector(UPDATED_QUICK_VIEW_CLICK_SELECTOR)
            .landingPagePriceSelector(UPDATED_LANDING_PAGE_PRICE_SELECTOR)
            .cartRowSelector(UPDATED_CART_ROW_SELECTOR)
            .cartLineItemSelector(UPDATED_CART_LINE_ITEM_SELECTOR)
            .cartLineItemPerQuantityPriceSelector(UPDATED_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR)
            .cartLineItemTotalPriceSelector(UPDATED_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR)
            .cartLineItemSellingPlanNameSelector(UPDATED_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR)
            .cartSubTotalSelector(UPDATED_CART_SUB_TOTAL_SELECTOR)
            .cartLineItemPriceSelector(UPDATED_CART_LINE_ITEM_PRICE_SELECTOR)
            .enableCartWidgetFeature(UPDATED_ENABLE_CART_WIDGET_FEATURE)
            .enableSlowScriptLoad(UPDATED_ENABLE_SLOW_SCRIPT_LOAD)
            .scriptLoadDelay(UPDATED_SCRIPT_LOAD_DELAY)
            .formatMoneyOverride(UPDATED_FORMAT_MONEY_OVERRIDE)
            .widgetTemplateType(UPDATED_WIDGET_TEMPLATE_TYPE)
            .widgetTemplateHtml(UPDATED_WIDGET_TEMPLATE_HTML);

        restThemeSettingsMockMvc.perform(put("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedThemeSettings)))
            .andExpect(status().isOk());

        // Validate the ThemeSettings in the database
        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeUpdate);
        ThemeSettings testThemeSettings = themeSettingsList.get(themeSettingsList.size() - 1);
        assertThat(testThemeSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testThemeSettings.isSkip_setting_theme()).isEqualTo(UPDATED_SKIP_SETTING_THEME);
        assertThat(testThemeSettings.isThemeV2Saved()).isEqualTo(UPDATED_THEME_V_2_SAVED);
        assertThat(testThemeSettings.getThemeName()).isEqualTo(UPDATED_THEME_NAME);
        assertThat(testThemeSettings.getShopifyThemeInstallationVersion()).isEqualTo(UPDATED_SHOPIFY_THEME_INSTALLATION_VERSION);
        assertThat(testThemeSettings.getSelectedSelector()).isEqualTo(UPDATED_SELECTED_SELECTOR);
        assertThat(testThemeSettings.getSubscriptionLinkSelector()).isEqualTo(UPDATED_SUBSCRIPTION_LINK_SELECTOR);
        assertThat(testThemeSettings.getCustomJavascript()).isEqualTo(UPDATED_CUSTOM_JAVASCRIPT);
        assertThat(testThemeSettings.getPlacement()).isEqualTo(UPDATED_PLACEMENT);
        assertThat(testThemeSettings.getSubscriptionLinkPlacement()).isEqualTo(UPDATED_SUBSCRIPTION_LINK_PLACEMENT);
        assertThat(testThemeSettings.getPriceSelector()).isEqualTo(UPDATED_PRICE_SELECTOR);
        assertThat(testThemeSettings.getPricePlacement()).isEqualTo(UPDATED_PRICE_PLACEMENT);
        assertThat(testThemeSettings.getBadgeTop()).isEqualTo(UPDATED_BADGE_TOP);
        assertThat(testThemeSettings.isDisableLoadingJquery()).isEqualTo(UPDATED_DISABLE_LOADING_JQUERY);
        assertThat(testThemeSettings.getQuickViewClickSelector()).isEqualTo(UPDATED_QUICK_VIEW_CLICK_SELECTOR);
        assertThat(testThemeSettings.getLandingPagePriceSelector()).isEqualTo(UPDATED_LANDING_PAGE_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartRowSelector()).isEqualTo(UPDATED_CART_ROW_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemPerQuantityPriceSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_PER_QUANTITY_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemTotalPriceSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_TOTAL_PRICE_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemSellingPlanNameSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_SELLING_PLAN_NAME_SELECTOR);
        assertThat(testThemeSettings.getCartSubTotalSelector()).isEqualTo(UPDATED_CART_SUB_TOTAL_SELECTOR);
        assertThat(testThemeSettings.getCartLineItemPriceSelector()).isEqualTo(UPDATED_CART_LINE_ITEM_PRICE_SELECTOR);
        assertThat(testThemeSettings.isEnableCartWidgetFeature()).isEqualTo(UPDATED_ENABLE_CART_WIDGET_FEATURE);
        assertThat(testThemeSettings.isEnableSlowScriptLoad()).isEqualTo(UPDATED_ENABLE_SLOW_SCRIPT_LOAD);
        assertThat(testThemeSettings.getScriptLoadDelay()).isEqualTo(UPDATED_SCRIPT_LOAD_DELAY);
        assertThat(testThemeSettings.isFormatMoneyOverride()).isEqualTo(UPDATED_FORMAT_MONEY_OVERRIDE);
        assertThat(testThemeSettings.getWidgetTemplateType()).isEqualTo(UPDATED_WIDGET_TEMPLATE_TYPE);
        assertThat(testThemeSettings.getWidgetTemplateHtml()).isEqualTo(UPDATED_WIDGET_TEMPLATE_HTML);
    }

    @Test
    @Transactional
    public void updateNonExistingThemeSettings() throws Exception {
        int databaseSizeBeforeUpdate = themeSettingsRepository.findAll().size();

        // Create the ThemeSettings

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeSettingsMockMvc.perform(put("/api/theme-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(themeSettings)))
            .andExpect(status().isBadRequest());

        // Validate the ThemeSettings in the database
        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteThemeSettings() throws Exception {
        // Initialize the database
        themeSettingsService.save(themeSettings);

        int databaseSizeBeforeDelete = themeSettingsRepository.findAll().size();

        // Delete the themeSettings
        restThemeSettingsMockMvc.perform(delete("/api/theme-settings/{id}", themeSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ThemeSettings> themeSettingsList = themeSettingsRepository.findAll();
        assertThat(themeSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
