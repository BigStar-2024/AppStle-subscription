package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SubscriptionWidgetSettings;
import com.et.repository.SubscriptionWidgetSettingsRepository;
import com.et.service.SubscriptionWidgetSettingsService;
import com.et.service.dto.SubscriptionWidgetSettingsDTO;
import com.et.service.mapper.SubscriptionWidgetSettingsMapper;
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
 * Integration tests for the {@link SubscriptionWidgetSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class SubscriptionWidgetSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_ONE_TIME_PURCHASE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ONE_TIME_PURCHASE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PURCHASE_OPTIONS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASE_OPTIONS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_OPTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_OPTION_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SELLING_PLAN_SELECT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SELLING_PLAN_SELECT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_DESCTIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_DESCTIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_TOP = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_WIDGET_MARGIN_TOP = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_WIDTH = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_WRAPPER_BORDER_WIDTH = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_WRAPPER_BORDER_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CIRCLE_BORDER_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_CIRCLE_BORDER_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_DOT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_DOT_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_PADDING_TOP = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_PADDING_TOP = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_PADDING_BOTTOM = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_PADDING_BOTTOM = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_PADDING_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_PADDING_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_PADDING_RIGHT = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_PADDING_RIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_BORDER_WIDTH = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_BORDER_WIDTH = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_BORDER_STYLE = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_BORDER_STYLE = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_BORDER_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_BORDER_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SELECT_BORDER_RADIUS = "AAAAAAAAAA";
    private static final String UPDATED_SELECT_BORDER_RADIUS = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_SUBSCRIPTION_SVG_FILL = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_SUBSCRIPTION_SVG_FILL = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_FINAL_PRICE_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_FINAL_PRICE_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_WIDGET_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_WIDGET_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_TOOLTIP_ON_CLICK = false;
    private static final Boolean UPDATED_SHOW_TOOLTIP_ON_CLICK = true;

    private static final Boolean DEFAULT_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT = false;
    private static final Boolean UPDATED_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT = true;

    private static final Boolean DEFAULT_WIDGET_ENABLED = false;
    private static final Boolean UPDATED_WIDGET_ENABLED = true;

    private static final Boolean DEFAULT_SHOW_TOOLTIP = false;
    private static final Boolean UPDATED_SHOW_TOOLTIP = true;

    private static final String DEFAULT_SELLING_PLAN_TITLE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SELLING_PLAN_TITLE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ONE_TIME_PRICE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ONE_TIME_PRICE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_DISCOUNT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_DISCOUNT_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BTN_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BTN_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_TOOLTIP_DESCRIPTION_CUSTOMIZATION = "AAAAAAAAAA";
    private static final String UPDATED_TOOLTIP_DESCRIPTION_CUSTOMIZATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_STATIC_TOOLTIP = false;
    private static final Boolean UPDATED_SHOW_STATIC_TOOLTIP = true;

    private static final Boolean DEFAULT_SHOW_APPSTLE_LINK = false;
    private static final Boolean UPDATED_SHOW_APPSTLE_LINK = true;

    private static final String DEFAULT_SUBSCRIPTION_PRICE_DISPLAY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_PRICE_DISPLAY_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SORT_BY_DEFAULT_SEQUENCE = false;
    private static final Boolean UPDATED_SORT_BY_DEFAULT_SEQUENCE = true;

    private static final Boolean DEFAULT_SHOW_SUB_OPTION_BEFORE_ONE_TIME = false;
    private static final Boolean UPDATED_SHOW_SUB_OPTION_BEFORE_ONE_TIME = true;

    private static final Boolean DEFAULT_SHOW_CHECKOUT_SUBSCRIPTION_BTN = false;
    private static final Boolean UPDATED_SHOW_CHECKOUT_SUBSCRIPTION_BTN = true;

    private static final String DEFAULT_TOTAL_PRICE_PER_DELIVERY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_PRICE_PER_DELIVERY_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WIDGET_ENABLED_ON_SOLD_VARIANT = false;
    private static final Boolean UPDATED_WIDGET_ENABLED_ON_SOLD_VARIANT = true;

    private static final Boolean DEFAULT_ENABLE_CART_WIDGET_FEATURE = false;
    private static final Boolean UPDATED_ENABLE_CART_WIDGET_FEATURE = true;

    private static final Boolean DEFAULT_SWITCH_RADIO_BUTTON_WIDGET = false;
    private static final Boolean UPDATED_SWITCH_RADIO_BUTTON_WIDGET = true;

    private static final String DEFAULT_FORM_MAPPING_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_MAPPING_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_MAPPING_ATTRIBUTE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_FORM_MAPPING_ATTRIBUTE_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_QUICK_VIEW_MODAL_POLLING_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_QUICK_VIEW_MODAL_POLLING_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_PRICE_ON_QUANTITY_CHANGE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_PRICE_ON_QUANTITY_CHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_WIDGET_PARENT_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_WIDGET_PARENT_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_LOYALTY_DETAILS_LABEL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY_DETAILS_LABEL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_LOYALTY_PERK_DESCRIPTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LOYALTY_PERK_DESCRIPTION_TEXT = "BBBBBBBBBB";

    @Autowired
    private SubscriptionWidgetSettingsRepository subscriptionWidgetSettingsRepository;

    @Autowired
    private SubscriptionWidgetSettingsMapper subscriptionWidgetSettingsMapper;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

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

    private MockMvc restSubscriptionWidgetSettingsMockMvc;

    private SubscriptionWidgetSettings subscriptionWidgetSettings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriptionWidgetSettingsResource subscriptionWidgetSettingsResource = new SubscriptionWidgetSettingsResource(subscriptionWidgetSettingsService);
        this.restSubscriptionWidgetSettingsMockMvc = MockMvcBuilders.standaloneSetup(subscriptionWidgetSettingsResource)
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
    public static SubscriptionWidgetSettings createEntity(EntityManager em) {
        SubscriptionWidgetSettings subscriptionWidgetSettings = new SubscriptionWidgetSettings()
            .shop(DEFAULT_SHOP)
            .oneTimePurchaseText(DEFAULT_ONE_TIME_PURCHASE_TEXT)
            .deliveryText(DEFAULT_DELIVERY_TEXT)
            .purchaseOptionsText(DEFAULT_PURCHASE_OPTIONS_TEXT)
            .subscriptionOptionText(DEFAULT_SUBSCRIPTION_OPTION_TEXT)
            .sellingPlanSelectTitle(DEFAULT_SELLING_PLAN_SELECT_TITLE)
            .tooltipTitle(DEFAULT_TOOLTIP_TITLE)
            .tooltipDesctiption(DEFAULT_TOOLTIP_DESCTIPTION)
            .subscriptionWidgetMarginTop(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_TOP)
            .subscriptionWidgetMarginBottom(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM)
            .subscriptionWrapperBorderWidth(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_WIDTH)
            .subscriptionWrapperBorderColor(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_COLOR)
            .circleBorderColor(DEFAULT_CIRCLE_BORDER_COLOR)
            .dotBackgroundColor(DEFAULT_DOT_BACKGROUND_COLOR)
            .selectPaddingTop(DEFAULT_SELECT_PADDING_TOP)
            .selectPaddingBottom(DEFAULT_SELECT_PADDING_BOTTOM)
            .selectPaddingLeft(DEFAULT_SELECT_PADDING_LEFT)
            .selectPaddingRight(DEFAULT_SELECT_PADDING_RIGHT)
            .selectBorderWidth(DEFAULT_SELECT_BORDER_WIDTH)
            .selectBorderStyle(DEFAULT_SELECT_BORDER_STYLE)
            .selectBorderColor(DEFAULT_SELECT_BORDER_COLOR)
            .selectBorderRadius(DEFAULT_SELECT_BORDER_RADIUS)
            .tooltipSubscriptionSvgFill(DEFAULT_TOOLTIP_SUBSCRIPTION_SVG_FILL)
            .tooltipColor(DEFAULT_TOOLTIP_COLOR)
            .tooltipBackgroundColor(DEFAULT_TOOLTIP_BACKGROUND_COLOR)
            .tooltipBorderTopColorBorderTopColor(DEFAULT_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR)
            .subscriptionFinalPriceColor(DEFAULT_SUBSCRIPTION_FINAL_PRICE_COLOR)
            .subscriptionWidgetTextColor(DEFAULT_SUBSCRIPTION_WIDGET_TEXT_COLOR)
            .orderStatusManageSubscriptionTitle(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE)
            .orderStatusManageSubscriptionDescription(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION)
            .orderStatusManageSubscriptionButtonText(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .showTooltipOnClick(DEFAULT_SHOW_TOOLTIP_ON_CLICK)
            .subscriptionOptionSelectedByDefault(DEFAULT_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT)
            .widgetEnabled(DEFAULT_WIDGET_ENABLED)
            .showTooltip(DEFAULT_SHOW_TOOLTIP)
            .sellingPlanTitleText(DEFAULT_SELLING_PLAN_TITLE_TEXT)
            .oneTimePriceText(DEFAULT_ONE_TIME_PRICE_TEXT)
            .selectedPayAsYouGoSellingPlanPriceText(DEFAULT_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT)
            .selectedPrepaidSellingPlanPriceText(DEFAULT_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT)
            .selectedDiscountFormat(DEFAULT_SELECTED_DISCOUNT_FORMAT)
            .manageSubscriptionBtnFormat(DEFAULT_MANAGE_SUBSCRIPTION_BTN_FORMAT)
            .tooltipDescriptionOnPrepaidPlan(DEFAULT_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN)
            .tooltipDescriptionOnMultipleDiscount(DEFAULT_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT)
            .tooltipDescriptionCustomization(DEFAULT_TOOLTIP_DESCRIPTION_CUSTOMIZATION)
            .showStaticTooltip(DEFAULT_SHOW_STATIC_TOOLTIP)
            .showAppstleLink(DEFAULT_SHOW_APPSTLE_LINK)
            .subscriptionPriceDisplayText(DEFAULT_SUBSCRIPTION_PRICE_DISPLAY_TEXT)
            .sortByDefaultSequence(DEFAULT_SORT_BY_DEFAULT_SEQUENCE)
            .showSubOptionBeforeOneTime(DEFAULT_SHOW_SUB_OPTION_BEFORE_ONE_TIME)
            .showCheckoutSubscriptionBtn(DEFAULT_SHOW_CHECKOUT_SUBSCRIPTION_BTN)
            .totalPricePerDeliveryText(DEFAULT_TOTAL_PRICE_PER_DELIVERY_TEXT)
            .widgetEnabledOnSoldVariant(DEFAULT_WIDGET_ENABLED_ON_SOLD_VARIANT)
            .enableCartWidgetFeature(DEFAULT_ENABLE_CART_WIDGET_FEATURE)
            .switchRadioButtonWidget(DEFAULT_SWITCH_RADIO_BUTTON_WIDGET)
            .formMappingAttributeName(DEFAULT_FORM_MAPPING_ATTRIBUTE_NAME)
            .formMappingAttributeSelector(DEFAULT_FORM_MAPPING_ATTRIBUTE_SELECTOR)
            .quickViewModalPollingSelector(DEFAULT_QUICK_VIEW_MODAL_POLLING_SELECTOR)
            .updatePriceOnQuantityChange(DEFAULT_UPDATE_PRICE_ON_QUANTITY_CHANGE)
            .widgetParentSelector(DEFAULT_WIDGET_PARENT_SELECTOR)
            .quantitySelector(DEFAULT_QUANTITY_SELECTOR)
            .loyaltyDetailsLabelText(DEFAULT_LOYALTY_DETAILS_LABEL_TEXT)
            .loyaltyPerkDescriptionText(DEFAULT_LOYALTY_PERK_DESCRIPTION_TEXT);
        return subscriptionWidgetSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionWidgetSettings createUpdatedEntity(EntityManager em) {
        SubscriptionWidgetSettings subscriptionWidgetSettings = new SubscriptionWidgetSettings()
            .shop(UPDATED_SHOP)
            .oneTimePurchaseText(UPDATED_ONE_TIME_PURCHASE_TEXT)
            .deliveryText(UPDATED_DELIVERY_TEXT)
            .purchaseOptionsText(UPDATED_PURCHASE_OPTIONS_TEXT)
            .subscriptionOptionText(UPDATED_SUBSCRIPTION_OPTION_TEXT)
            .sellingPlanSelectTitle(UPDATED_SELLING_PLAN_SELECT_TITLE)
            .tooltipTitle(UPDATED_TOOLTIP_TITLE)
            .tooltipDesctiption(UPDATED_TOOLTIP_DESCTIPTION)
            .subscriptionWidgetMarginTop(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_TOP)
            .subscriptionWidgetMarginBottom(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM)
            .subscriptionWrapperBorderWidth(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_WIDTH)
            .subscriptionWrapperBorderColor(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_COLOR)
            .circleBorderColor(UPDATED_CIRCLE_BORDER_COLOR)
            .dotBackgroundColor(UPDATED_DOT_BACKGROUND_COLOR)
            .selectPaddingTop(UPDATED_SELECT_PADDING_TOP)
            .selectPaddingBottom(UPDATED_SELECT_PADDING_BOTTOM)
            .selectPaddingLeft(UPDATED_SELECT_PADDING_LEFT)
            .selectPaddingRight(UPDATED_SELECT_PADDING_RIGHT)
            .selectBorderWidth(UPDATED_SELECT_BORDER_WIDTH)
            .selectBorderStyle(UPDATED_SELECT_BORDER_STYLE)
            .selectBorderColor(UPDATED_SELECT_BORDER_COLOR)
            .selectBorderRadius(UPDATED_SELECT_BORDER_RADIUS)
            .tooltipSubscriptionSvgFill(UPDATED_TOOLTIP_SUBSCRIPTION_SVG_FILL)
            .tooltipColor(UPDATED_TOOLTIP_COLOR)
            .tooltipBackgroundColor(UPDATED_TOOLTIP_BACKGROUND_COLOR)
            .tooltipBorderTopColorBorderTopColor(UPDATED_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR)
            .subscriptionFinalPriceColor(UPDATED_SUBSCRIPTION_FINAL_PRICE_COLOR)
            .subscriptionWidgetTextColor(UPDATED_SUBSCRIPTION_WIDGET_TEXT_COLOR)
            .orderStatusManageSubscriptionTitle(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE)
            .orderStatusManageSubscriptionDescription(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION)
            .orderStatusManageSubscriptionButtonText(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .showTooltipOnClick(UPDATED_SHOW_TOOLTIP_ON_CLICK)
            .subscriptionOptionSelectedByDefault(UPDATED_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT)
            .widgetEnabled(UPDATED_WIDGET_ENABLED)
            .showTooltip(UPDATED_SHOW_TOOLTIP)
            .sellingPlanTitleText(UPDATED_SELLING_PLAN_TITLE_TEXT)
            .oneTimePriceText(UPDATED_ONE_TIME_PRICE_TEXT)
            .selectedPayAsYouGoSellingPlanPriceText(UPDATED_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT)
            .selectedPrepaidSellingPlanPriceText(UPDATED_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT)
            .selectedDiscountFormat(UPDATED_SELECTED_DISCOUNT_FORMAT)
            .manageSubscriptionBtnFormat(UPDATED_MANAGE_SUBSCRIPTION_BTN_FORMAT)
            .tooltipDescriptionOnPrepaidPlan(UPDATED_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN)
            .tooltipDescriptionOnMultipleDiscount(UPDATED_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT)
            .tooltipDescriptionCustomization(UPDATED_TOOLTIP_DESCRIPTION_CUSTOMIZATION)
            .showStaticTooltip(UPDATED_SHOW_STATIC_TOOLTIP)
            .showAppstleLink(UPDATED_SHOW_APPSTLE_LINK)
            .subscriptionPriceDisplayText(UPDATED_SUBSCRIPTION_PRICE_DISPLAY_TEXT)
            .sortByDefaultSequence(UPDATED_SORT_BY_DEFAULT_SEQUENCE)
            .showSubOptionBeforeOneTime(UPDATED_SHOW_SUB_OPTION_BEFORE_ONE_TIME)
            .showCheckoutSubscriptionBtn(UPDATED_SHOW_CHECKOUT_SUBSCRIPTION_BTN)
            .totalPricePerDeliveryText(UPDATED_TOTAL_PRICE_PER_DELIVERY_TEXT)
            .widgetEnabledOnSoldVariant(UPDATED_WIDGET_ENABLED_ON_SOLD_VARIANT)
            .enableCartWidgetFeature(UPDATED_ENABLE_CART_WIDGET_FEATURE)
            .switchRadioButtonWidget(UPDATED_SWITCH_RADIO_BUTTON_WIDGET)
            .formMappingAttributeName(UPDATED_FORM_MAPPING_ATTRIBUTE_NAME)
            .formMappingAttributeSelector(UPDATED_FORM_MAPPING_ATTRIBUTE_SELECTOR)
            .quickViewModalPollingSelector(UPDATED_QUICK_VIEW_MODAL_POLLING_SELECTOR)
            .updatePriceOnQuantityChange(UPDATED_UPDATE_PRICE_ON_QUANTITY_CHANGE)
            .widgetParentSelector(UPDATED_WIDGET_PARENT_SELECTOR)
            .quantitySelector(UPDATED_QUANTITY_SELECTOR)
            .loyaltyDetailsLabelText(UPDATED_LOYALTY_DETAILS_LABEL_TEXT)
            .loyaltyPerkDescriptionText(UPDATED_LOYALTY_PERK_DESCRIPTION_TEXT);
        return subscriptionWidgetSettings;
    }

    @BeforeEach
    public void initTest() {
        subscriptionWidgetSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriptionWidgetSettings() throws Exception {
        int databaseSizeBeforeCreate = subscriptionWidgetSettingsRepository.findAll().size();

        // Create the SubscriptionWidgetSettings
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsMapper.toDto(subscriptionWidgetSettings);
        restSubscriptionWidgetSettingsMockMvc.perform(post("/api/subscription-widget-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionWidgetSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the SubscriptionWidgetSettings in the database
        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionWidgetSettings testSubscriptionWidgetSettings = subscriptionWidgetSettingsList.get(subscriptionWidgetSettingsList.size() - 1);
        assertThat(testSubscriptionWidgetSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSubscriptionWidgetSettings.getOneTimePurchaseText()).isEqualTo(DEFAULT_ONE_TIME_PURCHASE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getDeliveryText()).isEqualTo(DEFAULT_DELIVERY_TEXT);
        assertThat(testSubscriptionWidgetSettings.getPurchaseOptionsText()).isEqualTo(DEFAULT_PURCHASE_OPTIONS_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionOptionText()).isEqualTo(DEFAULT_SUBSCRIPTION_OPTION_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSellingPlanSelectTitle()).isEqualTo(DEFAULT_SELLING_PLAN_SELECT_TITLE);
        assertThat(testSubscriptionWidgetSettings.getTooltipTitle()).isEqualTo(DEFAULT_TOOLTIP_TITLE);
        assertThat(testSubscriptionWidgetSettings.getTooltipDesctiption()).isEqualTo(DEFAULT_TOOLTIP_DESCTIPTION);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetMarginTop()).isEqualTo(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_TOP);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetMarginBottom()).isEqualTo(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWrapperBorderWidth()).isEqualTo(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_WIDTH);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWrapperBorderColor()).isEqualTo(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getCircleBorderColor()).isEqualTo(DEFAULT_CIRCLE_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getDotBackgroundColor()).isEqualTo(DEFAULT_DOT_BACKGROUND_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingTop()).isEqualTo(DEFAULT_SELECT_PADDING_TOP);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingBottom()).isEqualTo(DEFAULT_SELECT_PADDING_BOTTOM);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingLeft()).isEqualTo(DEFAULT_SELECT_PADDING_LEFT);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingRight()).isEqualTo(DEFAULT_SELECT_PADDING_RIGHT);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderWidth()).isEqualTo(DEFAULT_SELECT_BORDER_WIDTH);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderStyle()).isEqualTo(DEFAULT_SELECT_BORDER_STYLE);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderColor()).isEqualTo(DEFAULT_SELECT_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderRadius()).isEqualTo(DEFAULT_SELECT_BORDER_RADIUS);
        assertThat(testSubscriptionWidgetSettings.getTooltipSubscriptionSvgFill()).isEqualTo(DEFAULT_TOOLTIP_SUBSCRIPTION_SVG_FILL);
        assertThat(testSubscriptionWidgetSettings.getTooltipColor()).isEqualTo(DEFAULT_TOOLTIP_COLOR);
        assertThat(testSubscriptionWidgetSettings.getTooltipBackgroundColor()).isEqualTo(DEFAULT_TOOLTIP_BACKGROUND_COLOR);
        assertThat(testSubscriptionWidgetSettings.getTooltipBorderTopColorBorderTopColor()).isEqualTo(DEFAULT_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionFinalPriceColor()).isEqualTo(DEFAULT_SUBSCRIPTION_FINAL_PRICE_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetTextColor()).isEqualTo(DEFAULT_SUBSCRIPTION_WIDGET_TEXT_COLOR);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionTitle()).isEqualTo(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionDescription()).isEqualTo(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionButtonText()).isEqualTo(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testSubscriptionWidgetSettings.isShowTooltipOnClick()).isEqualTo(DEFAULT_SHOW_TOOLTIP_ON_CLICK);
        assertThat(testSubscriptionWidgetSettings.isSubscriptionOptionSelectedByDefault()).isEqualTo(DEFAULT_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT);
        assertThat(testSubscriptionWidgetSettings.isWidgetEnabled()).isEqualTo(DEFAULT_WIDGET_ENABLED);
        assertThat(testSubscriptionWidgetSettings.isShowTooltip()).isEqualTo(DEFAULT_SHOW_TOOLTIP);
        assertThat(testSubscriptionWidgetSettings.getSellingPlanTitleText()).isEqualTo(DEFAULT_SELLING_PLAN_TITLE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getOneTimePriceText()).isEqualTo(DEFAULT_ONE_TIME_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedPayAsYouGoSellingPlanPriceText()).isEqualTo(DEFAULT_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedPrepaidSellingPlanPriceText()).isEqualTo(DEFAULT_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedDiscountFormat()).isEqualTo(DEFAULT_SELECTED_DISCOUNT_FORMAT);
        assertThat(testSubscriptionWidgetSettings.getManageSubscriptionBtnFormat()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BTN_FORMAT);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionOnPrepaidPlan()).isEqualTo(DEFAULT_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionOnMultipleDiscount()).isEqualTo(DEFAULT_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionCustomization()).isEqualTo(DEFAULT_TOOLTIP_DESCRIPTION_CUSTOMIZATION);
        assertThat(testSubscriptionWidgetSettings.isShowStaticTooltip()).isEqualTo(DEFAULT_SHOW_STATIC_TOOLTIP);
        assertThat(testSubscriptionWidgetSettings.isShowAppstleLink()).isEqualTo(DEFAULT_SHOW_APPSTLE_LINK);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionPriceDisplayText()).isEqualTo(DEFAULT_SUBSCRIPTION_PRICE_DISPLAY_TEXT);
        assertThat(testSubscriptionWidgetSettings.isSortByDefaultSequence()).isEqualTo(DEFAULT_SORT_BY_DEFAULT_SEQUENCE);
        assertThat(testSubscriptionWidgetSettings.isShowSubOptionBeforeOneTime()).isEqualTo(DEFAULT_SHOW_SUB_OPTION_BEFORE_ONE_TIME);
        assertThat(testSubscriptionWidgetSettings.isShowCheckoutSubscriptionBtn()).isEqualTo(DEFAULT_SHOW_CHECKOUT_SUBSCRIPTION_BTN);
        assertThat(testSubscriptionWidgetSettings.getTotalPricePerDeliveryText()).isEqualTo(DEFAULT_TOTAL_PRICE_PER_DELIVERY_TEXT);
        assertThat(testSubscriptionWidgetSettings.isWidgetEnabledOnSoldVariant()).isEqualTo(DEFAULT_WIDGET_ENABLED_ON_SOLD_VARIANT);
        assertThat(testSubscriptionWidgetSettings.isEnableCartWidgetFeature()).isEqualTo(DEFAULT_ENABLE_CART_WIDGET_FEATURE);
        assertThat(testSubscriptionWidgetSettings.isSwitchRadioButtonWidget()).isEqualTo(DEFAULT_SWITCH_RADIO_BUTTON_WIDGET);
        assertThat(testSubscriptionWidgetSettings.getFormMappingAttributeName()).isEqualTo(DEFAULT_FORM_MAPPING_ATTRIBUTE_NAME);
        assertThat(testSubscriptionWidgetSettings.getFormMappingAttributeSelector()).isEqualTo(DEFAULT_FORM_MAPPING_ATTRIBUTE_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getQuickViewModalPollingSelector()).isEqualTo(DEFAULT_QUICK_VIEW_MODAL_POLLING_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getUpdatePriceOnQuantityChange()).isEqualTo(DEFAULT_UPDATE_PRICE_ON_QUANTITY_CHANGE);
        assertThat(testSubscriptionWidgetSettings.getWidgetParentSelector()).isEqualTo(DEFAULT_WIDGET_PARENT_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getQuantitySelector()).isEqualTo(DEFAULT_QUANTITY_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getLoyaltyDetailsLabelText()).isEqualTo(DEFAULT_LOYALTY_DETAILS_LABEL_TEXT);
        assertThat(testSubscriptionWidgetSettings.getLoyaltyPerkDescriptionText()).isEqualTo(DEFAULT_LOYALTY_PERK_DESCRIPTION_TEXT);
    }

    @Test
    @Transactional
    public void createSubscriptionWidgetSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriptionWidgetSettingsRepository.findAll().size();

        // Create the SubscriptionWidgetSettings with an existing ID
        subscriptionWidgetSettings.setId(1L);
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsMapper.toDto(subscriptionWidgetSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionWidgetSettingsMockMvc.perform(post("/api/subscription-widget-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionWidgetSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionWidgetSettings in the database
        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionWidgetSettingsRepository.findAll().size();
        // set the field null
        subscriptionWidgetSettings.setShop(null);

        // Create the SubscriptionWidgetSettings, which fails.
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsMapper.toDto(subscriptionWidgetSettings);

        restSubscriptionWidgetSettingsMockMvc.perform(post("/api/subscription-widget-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionWidgetSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscriptionWidgetSettings() throws Exception {
        // Initialize the database
        subscriptionWidgetSettingsRepository.saveAndFlush(subscriptionWidgetSettings);

        // Get all the subscriptionWidgetSettingsList
        restSubscriptionWidgetSettingsMockMvc.perform(get("/api/subscription-widget-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionWidgetSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].oneTimePurchaseText").value(hasItem(DEFAULT_ONE_TIME_PURCHASE_TEXT)))
            .andExpect(jsonPath("$.[*].deliveryText").value(hasItem(DEFAULT_DELIVERY_TEXT)))
            .andExpect(jsonPath("$.[*].purchaseOptionsText").value(hasItem(DEFAULT_PURCHASE_OPTIONS_TEXT)))
            .andExpect(jsonPath("$.[*].subscriptionOptionText").value(hasItem(DEFAULT_SUBSCRIPTION_OPTION_TEXT)))
            .andExpect(jsonPath("$.[*].sellingPlanSelectTitle").value(hasItem(DEFAULT_SELLING_PLAN_SELECT_TITLE)))
            .andExpect(jsonPath("$.[*].tooltipTitle").value(hasItem(DEFAULT_TOOLTIP_TITLE)))
            .andExpect(jsonPath("$.[*].tooltipDesctiption").value(hasItem(DEFAULT_TOOLTIP_DESCTIPTION.toString())))
            .andExpect(jsonPath("$.[*].subscriptionWidgetMarginTop").value(hasItem(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_TOP)))
            .andExpect(jsonPath("$.[*].subscriptionWidgetMarginBottom").value(hasItem(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM)))
            .andExpect(jsonPath("$.[*].subscriptionWrapperBorderWidth").value(hasItem(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_WIDTH)))
            .andExpect(jsonPath("$.[*].subscriptionWrapperBorderColor").value(hasItem(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_COLOR)))
            .andExpect(jsonPath("$.[*].circleBorderColor").value(hasItem(DEFAULT_CIRCLE_BORDER_COLOR)))
            .andExpect(jsonPath("$.[*].dotBackgroundColor").value(hasItem(DEFAULT_DOT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].selectPaddingTop").value(hasItem(DEFAULT_SELECT_PADDING_TOP)))
            .andExpect(jsonPath("$.[*].selectPaddingBottom").value(hasItem(DEFAULT_SELECT_PADDING_BOTTOM)))
            .andExpect(jsonPath("$.[*].selectPaddingLeft").value(hasItem(DEFAULT_SELECT_PADDING_LEFT)))
            .andExpect(jsonPath("$.[*].selectPaddingRight").value(hasItem(DEFAULT_SELECT_PADDING_RIGHT)))
            .andExpect(jsonPath("$.[*].selectBorderWidth").value(hasItem(DEFAULT_SELECT_BORDER_WIDTH)))
            .andExpect(jsonPath("$.[*].selectBorderStyle").value(hasItem(DEFAULT_SELECT_BORDER_STYLE)))
            .andExpect(jsonPath("$.[*].selectBorderColor").value(hasItem(DEFAULT_SELECT_BORDER_COLOR)))
            .andExpect(jsonPath("$.[*].selectBorderRadius").value(hasItem(DEFAULT_SELECT_BORDER_RADIUS)))
            .andExpect(jsonPath("$.[*].tooltipSubscriptionSvgFill").value(hasItem(DEFAULT_TOOLTIP_SUBSCRIPTION_SVG_FILL)))
            .andExpect(jsonPath("$.[*].tooltipColor").value(hasItem(DEFAULT_TOOLTIP_COLOR)))
            .andExpect(jsonPath("$.[*].tooltipBackgroundColor").value(hasItem(DEFAULT_TOOLTIP_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].tooltipBorderTopColorBorderTopColor").value(hasItem(DEFAULT_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR)))
            .andExpect(jsonPath("$.[*].subscriptionFinalPriceColor").value(hasItem(DEFAULT_SUBSCRIPTION_FINAL_PRICE_COLOR)))
            .andExpect(jsonPath("$.[*].subscriptionWidgetTextColor").value(hasItem(DEFAULT_SUBSCRIPTION_WIDGET_TEXT_COLOR)))
            .andExpect(jsonPath("$.[*].orderStatusManageSubscriptionTitle").value(hasItem(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE)))
            .andExpect(jsonPath("$.[*].orderStatusManageSubscriptionDescription").value(hasItem(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].orderStatusManageSubscriptionButtonText").value(hasItem(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].showTooltipOnClick").value(hasItem(DEFAULT_SHOW_TOOLTIP_ON_CLICK.booleanValue())))
            .andExpect(jsonPath("$.[*].subscriptionOptionSelectedByDefault").value(hasItem(DEFAULT_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].widgetEnabled").value(hasItem(DEFAULT_WIDGET_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].showTooltip").value(hasItem(DEFAULT_SHOW_TOOLTIP.booleanValue())))
            .andExpect(jsonPath("$.[*].sellingPlanTitleText").value(hasItem(DEFAULT_SELLING_PLAN_TITLE_TEXT)))
            .andExpect(jsonPath("$.[*].oneTimePriceText").value(hasItem(DEFAULT_ONE_TIME_PRICE_TEXT)))
            .andExpect(jsonPath("$.[*].selectedPayAsYouGoSellingPlanPriceText").value(hasItem(DEFAULT_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT)))
            .andExpect(jsonPath("$.[*].selectedPrepaidSellingPlanPriceText").value(hasItem(DEFAULT_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT)))
            .andExpect(jsonPath("$.[*].selectedDiscountFormat").value(hasItem(DEFAULT_SELECTED_DISCOUNT_FORMAT)))
            .andExpect(jsonPath("$.[*].manageSubscriptionBtnFormat").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BTN_FORMAT)))
            .andExpect(jsonPath("$.[*].tooltipDescriptionOnPrepaidPlan").value(hasItem(DEFAULT_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN.toString())))
            .andExpect(jsonPath("$.[*].tooltipDescriptionOnMultipleDiscount").value(hasItem(DEFAULT_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT.toString())))
            .andExpect(jsonPath("$.[*].tooltipDescriptionCustomization").value(hasItem(DEFAULT_TOOLTIP_DESCRIPTION_CUSTOMIZATION.toString())))
            .andExpect(jsonPath("$.[*].showStaticTooltip").value(hasItem(DEFAULT_SHOW_STATIC_TOOLTIP.booleanValue())))
            .andExpect(jsonPath("$.[*].showAppstleLink").value(hasItem(DEFAULT_SHOW_APPSTLE_LINK.booleanValue())))
            .andExpect(jsonPath("$.[*].subscriptionPriceDisplayText").value(hasItem(DEFAULT_SUBSCRIPTION_PRICE_DISPLAY_TEXT.toString())))
            .andExpect(jsonPath("$.[*].sortByDefaultSequence").value(hasItem(DEFAULT_SORT_BY_DEFAULT_SEQUENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].showSubOptionBeforeOneTime").value(hasItem(DEFAULT_SHOW_SUB_OPTION_BEFORE_ONE_TIME.booleanValue())))
            .andExpect(jsonPath("$.[*].showCheckoutSubscriptionBtn").value(hasItem(DEFAULT_SHOW_CHECKOUT_SUBSCRIPTION_BTN.booleanValue())))
            .andExpect(jsonPath("$.[*].totalPricePerDeliveryText").value(hasItem(DEFAULT_TOTAL_PRICE_PER_DELIVERY_TEXT)))
            .andExpect(jsonPath("$.[*].widgetEnabledOnSoldVariant").value(hasItem(DEFAULT_WIDGET_ENABLED_ON_SOLD_VARIANT.booleanValue())))
            .andExpect(jsonPath("$.[*].enableCartWidgetFeature").value(hasItem(DEFAULT_ENABLE_CART_WIDGET_FEATURE.booleanValue())))
            .andExpect(jsonPath("$.[*].switchRadioButtonWidget").value(hasItem(DEFAULT_SWITCH_RADIO_BUTTON_WIDGET.booleanValue())))
            .andExpect(jsonPath("$.[*].formMappingAttributeName").value(hasItem(DEFAULT_FORM_MAPPING_ATTRIBUTE_NAME)))
            .andExpect(jsonPath("$.[*].formMappingAttributeSelector").value(hasItem(DEFAULT_FORM_MAPPING_ATTRIBUTE_SELECTOR)))
            .andExpect(jsonPath("$.[*].quickViewModalPollingSelector").value(hasItem(DEFAULT_QUICK_VIEW_MODAL_POLLING_SELECTOR)))
            .andExpect(jsonPath("$.[*].updatePriceOnQuantityChange").value(hasItem(DEFAULT_UPDATE_PRICE_ON_QUANTITY_CHANGE)))
            .andExpect(jsonPath("$.[*].widgetParentSelector").value(hasItem(DEFAULT_WIDGET_PARENT_SELECTOR)))
            .andExpect(jsonPath("$.[*].quantitySelector").value(hasItem(DEFAULT_QUANTITY_SELECTOR)))
            .andExpect(jsonPath("$.[*].loyaltyDetailsLabelText").value(hasItem(DEFAULT_LOYALTY_DETAILS_LABEL_TEXT)))
            .andExpect(jsonPath("$.[*].loyaltyPerkDescriptionText").value(hasItem(DEFAULT_LOYALTY_PERK_DESCRIPTION_TEXT.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriptionWidgetSettings() throws Exception {
        // Initialize the database
        subscriptionWidgetSettingsRepository.saveAndFlush(subscriptionWidgetSettings);

        // Get the subscriptionWidgetSettings
        restSubscriptionWidgetSettingsMockMvc.perform(get("/api/subscription-widget-settings/{id}", subscriptionWidgetSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionWidgetSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.oneTimePurchaseText").value(DEFAULT_ONE_TIME_PURCHASE_TEXT))
            .andExpect(jsonPath("$.deliveryText").value(DEFAULT_DELIVERY_TEXT))
            .andExpect(jsonPath("$.purchaseOptionsText").value(DEFAULT_PURCHASE_OPTIONS_TEXT))
            .andExpect(jsonPath("$.subscriptionOptionText").value(DEFAULT_SUBSCRIPTION_OPTION_TEXT))
            .andExpect(jsonPath("$.sellingPlanSelectTitle").value(DEFAULT_SELLING_PLAN_SELECT_TITLE))
            .andExpect(jsonPath("$.tooltipTitle").value(DEFAULT_TOOLTIP_TITLE))
            .andExpect(jsonPath("$.tooltipDesctiption").value(DEFAULT_TOOLTIP_DESCTIPTION.toString()))
            .andExpect(jsonPath("$.subscriptionWidgetMarginTop").value(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_TOP))
            .andExpect(jsonPath("$.subscriptionWidgetMarginBottom").value(DEFAULT_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM))
            .andExpect(jsonPath("$.subscriptionWrapperBorderWidth").value(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_WIDTH))
            .andExpect(jsonPath("$.subscriptionWrapperBorderColor").value(DEFAULT_SUBSCRIPTION_WRAPPER_BORDER_COLOR))
            .andExpect(jsonPath("$.circleBorderColor").value(DEFAULT_CIRCLE_BORDER_COLOR))
            .andExpect(jsonPath("$.dotBackgroundColor").value(DEFAULT_DOT_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.selectPaddingTop").value(DEFAULT_SELECT_PADDING_TOP))
            .andExpect(jsonPath("$.selectPaddingBottom").value(DEFAULT_SELECT_PADDING_BOTTOM))
            .andExpect(jsonPath("$.selectPaddingLeft").value(DEFAULT_SELECT_PADDING_LEFT))
            .andExpect(jsonPath("$.selectPaddingRight").value(DEFAULT_SELECT_PADDING_RIGHT))
            .andExpect(jsonPath("$.selectBorderWidth").value(DEFAULT_SELECT_BORDER_WIDTH))
            .andExpect(jsonPath("$.selectBorderStyle").value(DEFAULT_SELECT_BORDER_STYLE))
            .andExpect(jsonPath("$.selectBorderColor").value(DEFAULT_SELECT_BORDER_COLOR))
            .andExpect(jsonPath("$.selectBorderRadius").value(DEFAULT_SELECT_BORDER_RADIUS))
            .andExpect(jsonPath("$.tooltipSubscriptionSvgFill").value(DEFAULT_TOOLTIP_SUBSCRIPTION_SVG_FILL))
            .andExpect(jsonPath("$.tooltipColor").value(DEFAULT_TOOLTIP_COLOR))
            .andExpect(jsonPath("$.tooltipBackgroundColor").value(DEFAULT_TOOLTIP_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.tooltipBorderTopColorBorderTopColor").value(DEFAULT_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR))
            .andExpect(jsonPath("$.subscriptionFinalPriceColor").value(DEFAULT_SUBSCRIPTION_FINAL_PRICE_COLOR))
            .andExpect(jsonPath("$.subscriptionWidgetTextColor").value(DEFAULT_SUBSCRIPTION_WIDGET_TEXT_COLOR))
            .andExpect(jsonPath("$.orderStatusManageSubscriptionTitle").value(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE))
            .andExpect(jsonPath("$.orderStatusManageSubscriptionDescription").value(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION))
            .andExpect(jsonPath("$.orderStatusManageSubscriptionButtonText").value(DEFAULT_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT))
            .andExpect(jsonPath("$.showTooltipOnClick").value(DEFAULT_SHOW_TOOLTIP_ON_CLICK.booleanValue()))
            .andExpect(jsonPath("$.subscriptionOptionSelectedByDefault").value(DEFAULT_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.widgetEnabled").value(DEFAULT_WIDGET_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.showTooltip").value(DEFAULT_SHOW_TOOLTIP.booleanValue()))
            .andExpect(jsonPath("$.sellingPlanTitleText").value(DEFAULT_SELLING_PLAN_TITLE_TEXT))
            .andExpect(jsonPath("$.oneTimePriceText").value(DEFAULT_ONE_TIME_PRICE_TEXT))
            .andExpect(jsonPath("$.selectedPayAsYouGoSellingPlanPriceText").value(DEFAULT_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT))
            .andExpect(jsonPath("$.selectedPrepaidSellingPlanPriceText").value(DEFAULT_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT))
            .andExpect(jsonPath("$.selectedDiscountFormat").value(DEFAULT_SELECTED_DISCOUNT_FORMAT))
            .andExpect(jsonPath("$.manageSubscriptionBtnFormat").value(DEFAULT_MANAGE_SUBSCRIPTION_BTN_FORMAT))
            .andExpect(jsonPath("$.tooltipDescriptionOnPrepaidPlan").value(DEFAULT_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN.toString()))
            .andExpect(jsonPath("$.tooltipDescriptionOnMultipleDiscount").value(DEFAULT_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT.toString()))
            .andExpect(jsonPath("$.tooltipDescriptionCustomization").value(DEFAULT_TOOLTIP_DESCRIPTION_CUSTOMIZATION.toString()))
            .andExpect(jsonPath("$.showStaticTooltip").value(DEFAULT_SHOW_STATIC_TOOLTIP.booleanValue()))
            .andExpect(jsonPath("$.showAppstleLink").value(DEFAULT_SHOW_APPSTLE_LINK.booleanValue()))
            .andExpect(jsonPath("$.subscriptionPriceDisplayText").value(DEFAULT_SUBSCRIPTION_PRICE_DISPLAY_TEXT.toString()))
            .andExpect(jsonPath("$.sortByDefaultSequence").value(DEFAULT_SORT_BY_DEFAULT_SEQUENCE.booleanValue()))
            .andExpect(jsonPath("$.showSubOptionBeforeOneTime").value(DEFAULT_SHOW_SUB_OPTION_BEFORE_ONE_TIME.booleanValue()))
            .andExpect(jsonPath("$.showCheckoutSubscriptionBtn").value(DEFAULT_SHOW_CHECKOUT_SUBSCRIPTION_BTN.booleanValue()))
            .andExpect(jsonPath("$.totalPricePerDeliveryText").value(DEFAULT_TOTAL_PRICE_PER_DELIVERY_TEXT))
            .andExpect(jsonPath("$.widgetEnabledOnSoldVariant").value(DEFAULT_WIDGET_ENABLED_ON_SOLD_VARIANT.booleanValue()))
            .andExpect(jsonPath("$.enableCartWidgetFeature").value(DEFAULT_ENABLE_CART_WIDGET_FEATURE.booleanValue()))
            .andExpect(jsonPath("$.switchRadioButtonWidget").value(DEFAULT_SWITCH_RADIO_BUTTON_WIDGET.booleanValue()))
            .andExpect(jsonPath("$.formMappingAttributeName").value(DEFAULT_FORM_MAPPING_ATTRIBUTE_NAME))
            .andExpect(jsonPath("$.formMappingAttributeSelector").value(DEFAULT_FORM_MAPPING_ATTRIBUTE_SELECTOR))
            .andExpect(jsonPath("$.quickViewModalPollingSelector").value(DEFAULT_QUICK_VIEW_MODAL_POLLING_SELECTOR))
            .andExpect(jsonPath("$.updatePriceOnQuantityChange").value(DEFAULT_UPDATE_PRICE_ON_QUANTITY_CHANGE))
            .andExpect(jsonPath("$.widgetParentSelector").value(DEFAULT_WIDGET_PARENT_SELECTOR))
            .andExpect(jsonPath("$.quantitySelector").value(DEFAULT_QUANTITY_SELECTOR))
            .andExpect(jsonPath("$.loyaltyDetailsLabelText").value(DEFAULT_LOYALTY_DETAILS_LABEL_TEXT))
            .andExpect(jsonPath("$.loyaltyPerkDescriptionText").value(DEFAULT_LOYALTY_PERK_DESCRIPTION_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubscriptionWidgetSettings() throws Exception {
        // Get the subscriptionWidgetSettings
        restSubscriptionWidgetSettingsMockMvc.perform(get("/api/subscription-widget-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriptionWidgetSettings() throws Exception {
        // Initialize the database
        subscriptionWidgetSettingsRepository.saveAndFlush(subscriptionWidgetSettings);

        int databaseSizeBeforeUpdate = subscriptionWidgetSettingsRepository.findAll().size();

        // Update the subscriptionWidgetSettings
        SubscriptionWidgetSettings updatedSubscriptionWidgetSettings = subscriptionWidgetSettingsRepository.findById(subscriptionWidgetSettings.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionWidgetSettings are not directly saved in db
        em.detach(updatedSubscriptionWidgetSettings);
        updatedSubscriptionWidgetSettings
            .shop(UPDATED_SHOP)
            .oneTimePurchaseText(UPDATED_ONE_TIME_PURCHASE_TEXT)
            .deliveryText(UPDATED_DELIVERY_TEXT)
            .purchaseOptionsText(UPDATED_PURCHASE_OPTIONS_TEXT)
            .subscriptionOptionText(UPDATED_SUBSCRIPTION_OPTION_TEXT)
            .sellingPlanSelectTitle(UPDATED_SELLING_PLAN_SELECT_TITLE)
            .tooltipTitle(UPDATED_TOOLTIP_TITLE)
            .tooltipDesctiption(UPDATED_TOOLTIP_DESCTIPTION)
            .subscriptionWidgetMarginTop(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_TOP)
            .subscriptionWidgetMarginBottom(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM)
            .subscriptionWrapperBorderWidth(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_WIDTH)
            .subscriptionWrapperBorderColor(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_COLOR)
            .circleBorderColor(UPDATED_CIRCLE_BORDER_COLOR)
            .dotBackgroundColor(UPDATED_DOT_BACKGROUND_COLOR)
            .selectPaddingTop(UPDATED_SELECT_PADDING_TOP)
            .selectPaddingBottom(UPDATED_SELECT_PADDING_BOTTOM)
            .selectPaddingLeft(UPDATED_SELECT_PADDING_LEFT)
            .selectPaddingRight(UPDATED_SELECT_PADDING_RIGHT)
            .selectBorderWidth(UPDATED_SELECT_BORDER_WIDTH)
            .selectBorderStyle(UPDATED_SELECT_BORDER_STYLE)
            .selectBorderColor(UPDATED_SELECT_BORDER_COLOR)
            .selectBorderRadius(UPDATED_SELECT_BORDER_RADIUS)
            .tooltipSubscriptionSvgFill(UPDATED_TOOLTIP_SUBSCRIPTION_SVG_FILL)
            .tooltipColor(UPDATED_TOOLTIP_COLOR)
            .tooltipBackgroundColor(UPDATED_TOOLTIP_BACKGROUND_COLOR)
            .tooltipBorderTopColorBorderTopColor(UPDATED_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR)
            .subscriptionFinalPriceColor(UPDATED_SUBSCRIPTION_FINAL_PRICE_COLOR)
            .subscriptionWidgetTextColor(UPDATED_SUBSCRIPTION_WIDGET_TEXT_COLOR)
            .orderStatusManageSubscriptionTitle(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE)
            .orderStatusManageSubscriptionDescription(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION)
            .orderStatusManageSubscriptionButtonText(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .showTooltipOnClick(UPDATED_SHOW_TOOLTIP_ON_CLICK)
            .subscriptionOptionSelectedByDefault(UPDATED_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT)
            .widgetEnabled(UPDATED_WIDGET_ENABLED)
            .showTooltip(UPDATED_SHOW_TOOLTIP)
            .sellingPlanTitleText(UPDATED_SELLING_PLAN_TITLE_TEXT)
            .oneTimePriceText(UPDATED_ONE_TIME_PRICE_TEXT)
            .selectedPayAsYouGoSellingPlanPriceText(UPDATED_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT)
            .selectedPrepaidSellingPlanPriceText(UPDATED_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT)
            .selectedDiscountFormat(UPDATED_SELECTED_DISCOUNT_FORMAT)
            .manageSubscriptionBtnFormat(UPDATED_MANAGE_SUBSCRIPTION_BTN_FORMAT)
            .tooltipDescriptionOnPrepaidPlan(UPDATED_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN)
            .tooltipDescriptionOnMultipleDiscount(UPDATED_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT)
            .tooltipDescriptionCustomization(UPDATED_TOOLTIP_DESCRIPTION_CUSTOMIZATION)
            .showStaticTooltip(UPDATED_SHOW_STATIC_TOOLTIP)
            .showAppstleLink(UPDATED_SHOW_APPSTLE_LINK)
            .subscriptionPriceDisplayText(UPDATED_SUBSCRIPTION_PRICE_DISPLAY_TEXT)
            .sortByDefaultSequence(UPDATED_SORT_BY_DEFAULT_SEQUENCE)
            .showSubOptionBeforeOneTime(UPDATED_SHOW_SUB_OPTION_BEFORE_ONE_TIME)
            .showCheckoutSubscriptionBtn(UPDATED_SHOW_CHECKOUT_SUBSCRIPTION_BTN)
            .totalPricePerDeliveryText(UPDATED_TOTAL_PRICE_PER_DELIVERY_TEXT)
            .widgetEnabledOnSoldVariant(UPDATED_WIDGET_ENABLED_ON_SOLD_VARIANT)
            .enableCartWidgetFeature(UPDATED_ENABLE_CART_WIDGET_FEATURE)
            .switchRadioButtonWidget(UPDATED_SWITCH_RADIO_BUTTON_WIDGET)
            .formMappingAttributeName(UPDATED_FORM_MAPPING_ATTRIBUTE_NAME)
            .formMappingAttributeSelector(UPDATED_FORM_MAPPING_ATTRIBUTE_SELECTOR)
            .quickViewModalPollingSelector(UPDATED_QUICK_VIEW_MODAL_POLLING_SELECTOR)
            .updatePriceOnQuantityChange(UPDATED_UPDATE_PRICE_ON_QUANTITY_CHANGE)
            .widgetParentSelector(UPDATED_WIDGET_PARENT_SELECTOR)
            .quantitySelector(UPDATED_QUANTITY_SELECTOR)
            .loyaltyDetailsLabelText(UPDATED_LOYALTY_DETAILS_LABEL_TEXT)
            .loyaltyPerkDescriptionText(UPDATED_LOYALTY_PERK_DESCRIPTION_TEXT);
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsMapper.toDto(updatedSubscriptionWidgetSettings);

        restSubscriptionWidgetSettingsMockMvc.perform(put("/api/subscription-widget-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionWidgetSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the SubscriptionWidgetSettings in the database
        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionWidgetSettings testSubscriptionWidgetSettings = subscriptionWidgetSettingsList.get(subscriptionWidgetSettingsList.size() - 1);
        assertThat(testSubscriptionWidgetSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSubscriptionWidgetSettings.getOneTimePurchaseText()).isEqualTo(UPDATED_ONE_TIME_PURCHASE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getDeliveryText()).isEqualTo(UPDATED_DELIVERY_TEXT);
        assertThat(testSubscriptionWidgetSettings.getPurchaseOptionsText()).isEqualTo(UPDATED_PURCHASE_OPTIONS_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionOptionText()).isEqualTo(UPDATED_SUBSCRIPTION_OPTION_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSellingPlanSelectTitle()).isEqualTo(UPDATED_SELLING_PLAN_SELECT_TITLE);
        assertThat(testSubscriptionWidgetSettings.getTooltipTitle()).isEqualTo(UPDATED_TOOLTIP_TITLE);
        assertThat(testSubscriptionWidgetSettings.getTooltipDesctiption()).isEqualTo(UPDATED_TOOLTIP_DESCTIPTION);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetMarginTop()).isEqualTo(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_TOP);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetMarginBottom()).isEqualTo(UPDATED_SUBSCRIPTION_WIDGET_MARGIN_BOTTOM);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWrapperBorderWidth()).isEqualTo(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_WIDTH);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWrapperBorderColor()).isEqualTo(UPDATED_SUBSCRIPTION_WRAPPER_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getCircleBorderColor()).isEqualTo(UPDATED_CIRCLE_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getDotBackgroundColor()).isEqualTo(UPDATED_DOT_BACKGROUND_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingTop()).isEqualTo(UPDATED_SELECT_PADDING_TOP);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingBottom()).isEqualTo(UPDATED_SELECT_PADDING_BOTTOM);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingLeft()).isEqualTo(UPDATED_SELECT_PADDING_LEFT);
        assertThat(testSubscriptionWidgetSettings.getSelectPaddingRight()).isEqualTo(UPDATED_SELECT_PADDING_RIGHT);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderWidth()).isEqualTo(UPDATED_SELECT_BORDER_WIDTH);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderStyle()).isEqualTo(UPDATED_SELECT_BORDER_STYLE);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderColor()).isEqualTo(UPDATED_SELECT_BORDER_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSelectBorderRadius()).isEqualTo(UPDATED_SELECT_BORDER_RADIUS);
        assertThat(testSubscriptionWidgetSettings.getTooltipSubscriptionSvgFill()).isEqualTo(UPDATED_TOOLTIP_SUBSCRIPTION_SVG_FILL);
        assertThat(testSubscriptionWidgetSettings.getTooltipColor()).isEqualTo(UPDATED_TOOLTIP_COLOR);
        assertThat(testSubscriptionWidgetSettings.getTooltipBackgroundColor()).isEqualTo(UPDATED_TOOLTIP_BACKGROUND_COLOR);
        assertThat(testSubscriptionWidgetSettings.getTooltipBorderTopColorBorderTopColor()).isEqualTo(UPDATED_TOOLTIP_BORDER_TOP_COLOR_BORDER_TOP_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionFinalPriceColor()).isEqualTo(UPDATED_SUBSCRIPTION_FINAL_PRICE_COLOR);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionWidgetTextColor()).isEqualTo(UPDATED_SUBSCRIPTION_WIDGET_TEXT_COLOR);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionTitle()).isEqualTo(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_TITLE);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionDescription()).isEqualTo(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_DESCRIPTION);
        assertThat(testSubscriptionWidgetSettings.getOrderStatusManageSubscriptionButtonText()).isEqualTo(UPDATED_ORDER_STATUS_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testSubscriptionWidgetSettings.isShowTooltipOnClick()).isEqualTo(UPDATED_SHOW_TOOLTIP_ON_CLICK);
        assertThat(testSubscriptionWidgetSettings.isSubscriptionOptionSelectedByDefault()).isEqualTo(UPDATED_SUBSCRIPTION_OPTION_SELECTED_BY_DEFAULT);
        assertThat(testSubscriptionWidgetSettings.isWidgetEnabled()).isEqualTo(UPDATED_WIDGET_ENABLED);
        assertThat(testSubscriptionWidgetSettings.isShowTooltip()).isEqualTo(UPDATED_SHOW_TOOLTIP);
        assertThat(testSubscriptionWidgetSettings.getSellingPlanTitleText()).isEqualTo(UPDATED_SELLING_PLAN_TITLE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getOneTimePriceText()).isEqualTo(UPDATED_ONE_TIME_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedPayAsYouGoSellingPlanPriceText()).isEqualTo(UPDATED_SELECTED_PAY_AS_YOU_GO_SELLING_PLAN_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedPrepaidSellingPlanPriceText()).isEqualTo(UPDATED_SELECTED_PREPAID_SELLING_PLAN_PRICE_TEXT);
        assertThat(testSubscriptionWidgetSettings.getSelectedDiscountFormat()).isEqualTo(UPDATED_SELECTED_DISCOUNT_FORMAT);
        assertThat(testSubscriptionWidgetSettings.getManageSubscriptionBtnFormat()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BTN_FORMAT);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionOnPrepaidPlan()).isEqualTo(UPDATED_TOOLTIP_DESCRIPTION_ON_PREPAID_PLAN);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionOnMultipleDiscount()).isEqualTo(UPDATED_TOOLTIP_DESCRIPTION_ON_MULTIPLE_DISCOUNT);
        assertThat(testSubscriptionWidgetSettings.getTooltipDescriptionCustomization()).isEqualTo(UPDATED_TOOLTIP_DESCRIPTION_CUSTOMIZATION);
        assertThat(testSubscriptionWidgetSettings.isShowStaticTooltip()).isEqualTo(UPDATED_SHOW_STATIC_TOOLTIP);
        assertThat(testSubscriptionWidgetSettings.isShowAppstleLink()).isEqualTo(UPDATED_SHOW_APPSTLE_LINK);
        assertThat(testSubscriptionWidgetSettings.getSubscriptionPriceDisplayText()).isEqualTo(UPDATED_SUBSCRIPTION_PRICE_DISPLAY_TEXT);
        assertThat(testSubscriptionWidgetSettings.isSortByDefaultSequence()).isEqualTo(UPDATED_SORT_BY_DEFAULT_SEQUENCE);
        assertThat(testSubscriptionWidgetSettings.isShowSubOptionBeforeOneTime()).isEqualTo(UPDATED_SHOW_SUB_OPTION_BEFORE_ONE_TIME);
        assertThat(testSubscriptionWidgetSettings.isShowCheckoutSubscriptionBtn()).isEqualTo(UPDATED_SHOW_CHECKOUT_SUBSCRIPTION_BTN);
        assertThat(testSubscriptionWidgetSettings.getTotalPricePerDeliveryText()).isEqualTo(UPDATED_TOTAL_PRICE_PER_DELIVERY_TEXT);
        assertThat(testSubscriptionWidgetSettings.isWidgetEnabledOnSoldVariant()).isEqualTo(UPDATED_WIDGET_ENABLED_ON_SOLD_VARIANT);
        assertThat(testSubscriptionWidgetSettings.isEnableCartWidgetFeature()).isEqualTo(UPDATED_ENABLE_CART_WIDGET_FEATURE);
        assertThat(testSubscriptionWidgetSettings.isSwitchRadioButtonWidget()).isEqualTo(UPDATED_SWITCH_RADIO_BUTTON_WIDGET);
        assertThat(testSubscriptionWidgetSettings.getFormMappingAttributeName()).isEqualTo(UPDATED_FORM_MAPPING_ATTRIBUTE_NAME);
        assertThat(testSubscriptionWidgetSettings.getFormMappingAttributeSelector()).isEqualTo(UPDATED_FORM_MAPPING_ATTRIBUTE_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getQuickViewModalPollingSelector()).isEqualTo(UPDATED_QUICK_VIEW_MODAL_POLLING_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getUpdatePriceOnQuantityChange()).isEqualTo(UPDATED_UPDATE_PRICE_ON_QUANTITY_CHANGE);
        assertThat(testSubscriptionWidgetSettings.getWidgetParentSelector()).isEqualTo(UPDATED_WIDGET_PARENT_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getQuantitySelector()).isEqualTo(UPDATED_QUANTITY_SELECTOR);
        assertThat(testSubscriptionWidgetSettings.getLoyaltyDetailsLabelText()).isEqualTo(UPDATED_LOYALTY_DETAILS_LABEL_TEXT);
        assertThat(testSubscriptionWidgetSettings.getLoyaltyPerkDescriptionText()).isEqualTo(UPDATED_LOYALTY_PERK_DESCRIPTION_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriptionWidgetSettings() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionWidgetSettingsRepository.findAll().size();

        // Create the SubscriptionWidgetSettings
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsMapper.toDto(subscriptionWidgetSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionWidgetSettingsMockMvc.perform(put("/api/subscription-widget-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriptionWidgetSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionWidgetSettings in the database
        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubscriptionWidgetSettings() throws Exception {
        // Initialize the database
        subscriptionWidgetSettingsRepository.saveAndFlush(subscriptionWidgetSettings);

        int databaseSizeBeforeDelete = subscriptionWidgetSettingsRepository.findAll().size();

        // Delete the subscriptionWidgetSettings
        restSubscriptionWidgetSettingsMockMvc.perform(delete("/api/subscription-widget-settings/{id}", subscriptionWidgetSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionWidgetSettings> subscriptionWidgetSettingsList = subscriptionWidgetSettingsRepository.findAll();
        assertThat(subscriptionWidgetSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
