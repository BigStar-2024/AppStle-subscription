package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.CustomerPortalSettings;
import com.et.repository.CustomerPortalSettingsRepository;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.service.mapper.CustomerPortalSettingsMapper;

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

import com.et.domain.enumeration.ProductSelectionOption;
/**
 * Integration tests for the {@link CustomerPortalSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CustomerPortalSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_FREQUENCY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_FREQUENCY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_PRODUCTS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_PRODUCTS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_ORDER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_ORDER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_SUBSCRIPTION_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_SUBSCRIPTION_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_NO_SUBSCRIPTION_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_NO_SUBSCRIPTION_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_NO_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_NO_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_PAYMENT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_PAYMENT_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_LAST_FOUR_DIGIT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CARD_LAST_FOUR_DIGIT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_EXPIRY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CARD_EXPIRY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_HOLDER_NAME_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CARD_HOLDER_NAME_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_TYPE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD_TYPE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD_TYPE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_ACCORDION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_ACCORDION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_DETAIL_ACCORDION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_DETAIL_ACCORDION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_UPCOMING_ORDER_ACCORDION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_UPCOMING_ORDER_ACCORDION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_INFO_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_INFO_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_PAYMENT_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_PAYMENT_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_ORDER_DATE_LBL = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_ORDER_DATE_LBL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_LBL = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_LBL = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY_LBL = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY_LBL = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT_LBL = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT_LBL = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_NO_LBL = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NO_LBL = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_FREQUENCY_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_FREQUENCY_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_FREQ_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_FREQ_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_FREQ_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_FREQ_BTN_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAUSE_RESUME_SUB = false;
    private static final Boolean UPDATED_PAUSE_RESUME_SUB = true;

    private static final Boolean DEFAULT_CHANGE_NEXT_ORDER_DATE = false;
    private static final Boolean UPDATED_CHANGE_NEXT_ORDER_DATE = true;

    private static final Boolean DEFAULT_CANCEL_SUB = false;
    private static final Boolean UPDATED_CANCEL_SUB = true;

    private static final Boolean DEFAULT_CHANGE_ORDER_FREQUENCY = false;
    private static final Boolean UPDATED_CHANGE_ORDER_FREQUENCY = true;

    private static final Boolean DEFAULT_CREATE_ADDITIONAL_ORDER = false;
    private static final Boolean UPDATED_CREATE_ADDITIONAL_ORDER = true;

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_CHANGE_ORDER_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_CHANGE_ORDER_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_CHANGE_ORDER_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_CHANGE_ORDER_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_CHANGE_ORDER_BTN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_CHANGE_ORDER_BTN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_PRODUCT_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_PRODUCT_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DELETE_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_DELETE_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ADD_PRODUCT_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ADD_PRODUCT_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ADD_PRODUCT_LABEL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ADD_PRODUCT_LABEL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVE_BADGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVE_BADGE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSE_BADGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CLOSE_BADGE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SKIP_ORDER_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SKIP_ORDER_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_LABEL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_LABEL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SEE_MORE_DETAILS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SEE_MORE_DETAILS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_HIDE_DETAILS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_HIDE_DETAILS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_IN_SUBSCRIPTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_IN_SUBSCRIPTION_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_EDIT_QUANTITY_LABEL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EDIT_QUANTITY_LABEL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TOTAL_LABEL_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TOTAL_LABEL_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_NOTIFICATION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NOTIFICATION_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EDIT_PRODUCT_FLAG = false;
    private static final Boolean UPDATED_EDIT_PRODUCT_FLAG = true;

    private static final Boolean DEFAULT_DELETE_PRODUCT_FLAG = false;
    private static final Boolean UPDATED_DELETE_PRODUCT_FLAG = true;

    private static final Boolean DEFAULT_SHOW_SHIPMENT = false;
    private static final Boolean UPDATED_SHOW_SHIPMENT = true;

    private static final Boolean DEFAULT_ADD_ADDITIONAL_PRODUCT = false;
    private static final Boolean UPDATED_ADD_ADDITIONAL_PRODUCT = true;

    private static final String DEFAULT_SUCCESS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SUCCESS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_UPCOMING_FULFILLMENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_UPCOMING_FULFILLMENT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_CARD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_CARD_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ENDING_WITH_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ENDING_WITH_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_WEEK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_WEEK_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DAY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_DAY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MONTH_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SKIP_BADGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SKIP_BADGE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_QUEUE_BADGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUEUE_BADGE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_PORTAL_SETTING_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_PORTAL_SETTING_JSON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ORDER_NOTE_FLAG = false;
    private static final Boolean UPDATED_ORDER_NOTE_FLAG = true;

    private static final String DEFAULT_ORDER_NOTE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NOTE_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USE_URL_WITH_CUSTOMER_ID = false;
    private static final Boolean UPDATED_USE_URL_WITH_CUSTOMER_ID = true;

    private static final ProductSelectionOption DEFAULT_PRODUCT_SELECTION_OPTION = ProductSelectionOption.ALL_PRODUCTS;
    private static final ProductSelectionOption UPDATED_PRODUCT_SELECTION_OPTION = ProductSelectionOption.PRODUCTS_FROM_ALL_PLANS;

    private static final Boolean DEFAULT_INCLUDE_OUT_OF_STOCK_PRODUCT = false;
    private static final Boolean UPDATED_INCLUDE_OUT_OF_STOCK_PRODUCT = true;

    private static final String DEFAULT_OPEN_BADGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_BADGE_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_UPDATE_SHIPMENT_BILLING_DATE = false;
    private static final Boolean UPDATED_UPDATE_SHIPMENT_BILLING_DATE = true;

    private static final Boolean DEFAULT_DISCOUNT_CODE = false;
    private static final Boolean UPDATED_DISCOUNT_CODE = true;

    private static final Boolean DEFAULT_FREEZE_ORDER_TILL_MIN_CYCLE = false;
    private static final Boolean UPDATED_FREEZE_ORDER_TILL_MIN_CYCLE = true;

    private static final Boolean DEFAULT_ADD_ONE_TIME_PRODUCT = false;
    private static final Boolean UPDATED_ADD_ONE_TIME_PRODUCT = true;

    private static final Boolean DEFAULT_ALLOW_ORDER_NOW = false;
    private static final Boolean UPDATED_ALLOW_ORDER_NOW = true;

    private static final Integer DEFAULT_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT = 1;
    private static final Integer UPDATED_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT = 2;

    private static final Boolean DEFAULT_ALLOW_SPLIT_CONTRACT = false;
    private static final Boolean UPDATED_ALLOW_SPLIT_CONTRACT = true;

    @Autowired
    private CustomerPortalSettingsRepository customerPortalSettingsRepository;

    @Autowired
    private CustomerPortalSettingsMapper customerPortalSettingsMapper;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerPortalSettingsMockMvc;

    private CustomerPortalSettings customerPortalSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerPortalSettings createEntity(EntityManager em) {
        CustomerPortalSettings customerPortalSettings = new CustomerPortalSettings()
            .shop(DEFAULT_SHOP)
            .orderFrequencyText(DEFAULT_ORDER_FREQUENCY_TEXT)
            .totalProductsText(DEFAULT_TOTAL_PRODUCTS_TEXT)
            .nextOrderText(DEFAULT_NEXT_ORDER_TEXT)
            .statusText(DEFAULT_STATUS_TEXT)
            .cancelSubscriptionBtnText(DEFAULT_CANCEL_SUBSCRIPTION_BTN_TEXT)
            .noSubscriptionMessage(DEFAULT_NO_SUBSCRIPTION_MESSAGE)
            .subscriptionNoText(DEFAULT_SUBSCRIPTION_NO_TEXT)
            .updatePaymentMessage(DEFAULT_UPDATE_PAYMENT_MESSAGE)
            .cardLastFourDigitText(DEFAULT_CARD_LAST_FOUR_DIGIT_TEXT)
            .cardExpiryText(DEFAULT_CARD_EXPIRY_TEXT)
            .cardHolderNameText(DEFAULT_CARD_HOLDER_NAME_TEXT)
            .cardTypeText(DEFAULT_CARD_TYPE_TEXT)
            .paymentMethodTypeText(DEFAULT_PAYMENT_METHOD_TYPE_TEXT)
            .cancelAccordionTitle(DEFAULT_CANCEL_ACCORDION_TITLE)
            .paymentDetailAccordionTitle(DEFAULT_PAYMENT_DETAIL_ACCORDION_TITLE)
            .upcomingOrderAccordionTitle(DEFAULT_UPCOMING_ORDER_ACCORDION_TITLE)
            .paymentInfoText(DEFAULT_PAYMENT_INFO_TEXT)
            .updatePaymentBtnText(DEFAULT_UPDATE_PAYMENT_BTN_TEXT)
            .nextOrderDateLbl(DEFAULT_NEXT_ORDER_DATE_LBL)
            .statusLbl(DEFAULT_STATUS_LBL)
            .quantityLbl(DEFAULT_QUANTITY_LBL)
            .amountLbl(DEFAULT_AMOUNT_LBL)
            .orderNoLbl(DEFAULT_ORDER_NO_LBL)
            .editFrequencyBtnText(DEFAULT_EDIT_FREQUENCY_BTN_TEXT)
            .cancelFreqBtnText(DEFAULT_CANCEL_FREQ_BTN_TEXT)
            .updateFreqBtnText(DEFAULT_UPDATE_FREQ_BTN_TEXT)
            .pauseResumeSub(DEFAULT_PAUSE_RESUME_SUB)
            .changeNextOrderDate(DEFAULT_CHANGE_NEXT_ORDER_DATE)
            .cancelSub(DEFAULT_CANCEL_SUB)
            .changeOrderFrequency(DEFAULT_CHANGE_ORDER_FREQUENCY)
            .createAdditionalOrder(DEFAULT_CREATE_ADDITIONAL_ORDER)
            .manageSubscriptionButtonText(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .editChangeOrderBtnText(DEFAULT_EDIT_CHANGE_ORDER_BTN_TEXT)
            .cancelChangeOrderBtnText(DEFAULT_CANCEL_CHANGE_ORDER_BTN_TEXT)
            .updateChangeOrderBtnText(DEFAULT_UPDATE_CHANGE_ORDER_BTN_TEXT)
            .editProductButtonText(DEFAULT_EDIT_PRODUCT_BUTTON_TEXT)
            .deleteButtonText(DEFAULT_DELETE_BUTTON_TEXT)
            .updateButtonText(DEFAULT_UPDATE_BUTTON_TEXT)
            .cancelButtonText(DEFAULT_CANCEL_BUTTON_TEXT)
            .addProductButtonText(DEFAULT_ADD_PRODUCT_BUTTON_TEXT)
            .addProductLabelText(DEFAULT_ADD_PRODUCT_LABEL_TEXT)
            .activeBadgeText(DEFAULT_ACTIVE_BADGE_TEXT)
            .closeBadgeText(DEFAULT_CLOSE_BADGE_TEXT)
            .skipOrderButtonText(DEFAULT_SKIP_ORDER_BUTTON_TEXT)
            .productLabelText(DEFAULT_PRODUCT_LABEL_TEXT)
            .seeMoreDetailsText(DEFAULT_SEE_MORE_DETAILS_TEXT)
            .hideDetailsText(DEFAULT_HIDE_DETAILS_TEXT)
            .productInSubscriptionText(DEFAULT_PRODUCT_IN_SUBSCRIPTION_TEXT)
            .EditQuantityLabelText(DEFAULT_EDIT_QUANTITY_LABEL_TEXT)
            .subTotalLabelText(DEFAULT_SUB_TOTAL_LABEL_TEXT)
            .paymentNotificationText(DEFAULT_PAYMENT_NOTIFICATION_TEXT)
            .editProductFlag(DEFAULT_EDIT_PRODUCT_FLAG)
            .deleteProductFlag(DEFAULT_DELETE_PRODUCT_FLAG)
            .showShipment(DEFAULT_SHOW_SHIPMENT)
            .addAdditionalProduct(DEFAULT_ADD_ADDITIONAL_PRODUCT)
            .successText(DEFAULT_SUCCESS_TEXT)
            .cancelSubscriptionConfirmPrepaidText(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT)
            .cancelSubscriptionConfirmPayAsYouGoText(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT)
            .cancelSubscriptionPrepaidButtonText(DEFAULT_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT)
            .cancelSubscriptionPayAsYouGoButtonText(DEFAULT_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT)
            .upcomingFulfillmentText(DEFAULT_UPCOMING_FULFILLMENT_TEXT)
            .creditCardText(DEFAULT_CREDIT_CARD_TEXT)
            .endingWithText(DEFAULT_ENDING_WITH_TEXT)
            .weekText(DEFAULT_WEEK_TEXT)
            .dayText(DEFAULT_DAY_TEXT)
            .monthText(DEFAULT_MONTH_TEXT)
            .yearText(DEFAULT_YEAR_TEXT)
            .skipBadgeText(DEFAULT_SKIP_BADGE_TEXT)
            .queueBadgeText(DEFAULT_QUEUE_BADGE_TEXT)
            .customerPortalSettingJson(DEFAULT_CUSTOMER_PORTAL_SETTING_JSON)
            .orderNoteFlag(DEFAULT_ORDER_NOTE_FLAG)
            .orderNoteText(DEFAULT_ORDER_NOTE_TEXT)
            .useUrlWithCustomerId(DEFAULT_USE_URL_WITH_CUSTOMER_ID)
            .productSelectionOption(DEFAULT_PRODUCT_SELECTION_OPTION)
            .includeOutOfStockProduct(DEFAULT_INCLUDE_OUT_OF_STOCK_PRODUCT)
            .openBadgeText(DEFAULT_OPEN_BADGE_TEXT)
            .updateShipmentBillingDate(DEFAULT_UPDATE_SHIPMENT_BILLING_DATE)
            .discountCode(DEFAULT_DISCOUNT_CODE)
            .freezeOrderTillMinCycle(DEFAULT_FREEZE_ORDER_TILL_MIN_CYCLE)
            .addOneTimeProduct(DEFAULT_ADD_ONE_TIME_PRODUCT)
            .allowOrderNow(DEFAULT_ALLOW_ORDER_NOW)
            .minQtyToAllowDuringAddProduct(DEFAULT_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT)
            .allowSplitContract(DEFAULT_ALLOW_SPLIT_CONTRACT);
        return customerPortalSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerPortalSettings createUpdatedEntity(EntityManager em) {
        CustomerPortalSettings customerPortalSettings = new CustomerPortalSettings()
            .shop(UPDATED_SHOP)
            .orderFrequencyText(UPDATED_ORDER_FREQUENCY_TEXT)
            .totalProductsText(UPDATED_TOTAL_PRODUCTS_TEXT)
            .nextOrderText(UPDATED_NEXT_ORDER_TEXT)
            .statusText(UPDATED_STATUS_TEXT)
            .cancelSubscriptionBtnText(UPDATED_CANCEL_SUBSCRIPTION_BTN_TEXT)
            .noSubscriptionMessage(UPDATED_NO_SUBSCRIPTION_MESSAGE)
            .subscriptionNoText(UPDATED_SUBSCRIPTION_NO_TEXT)
            .updatePaymentMessage(UPDATED_UPDATE_PAYMENT_MESSAGE)
            .cardLastFourDigitText(UPDATED_CARD_LAST_FOUR_DIGIT_TEXT)
            .cardExpiryText(UPDATED_CARD_EXPIRY_TEXT)
            .cardHolderNameText(UPDATED_CARD_HOLDER_NAME_TEXT)
            .cardTypeText(UPDATED_CARD_TYPE_TEXT)
            .paymentMethodTypeText(UPDATED_PAYMENT_METHOD_TYPE_TEXT)
            .cancelAccordionTitle(UPDATED_CANCEL_ACCORDION_TITLE)
            .paymentDetailAccordionTitle(UPDATED_PAYMENT_DETAIL_ACCORDION_TITLE)
            .upcomingOrderAccordionTitle(UPDATED_UPCOMING_ORDER_ACCORDION_TITLE)
            .paymentInfoText(UPDATED_PAYMENT_INFO_TEXT)
            .updatePaymentBtnText(UPDATED_UPDATE_PAYMENT_BTN_TEXT)
            .nextOrderDateLbl(UPDATED_NEXT_ORDER_DATE_LBL)
            .statusLbl(UPDATED_STATUS_LBL)
            .quantityLbl(UPDATED_QUANTITY_LBL)
            .amountLbl(UPDATED_AMOUNT_LBL)
            .orderNoLbl(UPDATED_ORDER_NO_LBL)
            .editFrequencyBtnText(UPDATED_EDIT_FREQUENCY_BTN_TEXT)
            .cancelFreqBtnText(UPDATED_CANCEL_FREQ_BTN_TEXT)
            .updateFreqBtnText(UPDATED_UPDATE_FREQ_BTN_TEXT)
            .pauseResumeSub(UPDATED_PAUSE_RESUME_SUB)
            .changeNextOrderDate(UPDATED_CHANGE_NEXT_ORDER_DATE)
            .cancelSub(UPDATED_CANCEL_SUB)
            .changeOrderFrequency(UPDATED_CHANGE_ORDER_FREQUENCY)
            .createAdditionalOrder(UPDATED_CREATE_ADDITIONAL_ORDER)
            .manageSubscriptionButtonText(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .editChangeOrderBtnText(UPDATED_EDIT_CHANGE_ORDER_BTN_TEXT)
            .cancelChangeOrderBtnText(UPDATED_CANCEL_CHANGE_ORDER_BTN_TEXT)
            .updateChangeOrderBtnText(UPDATED_UPDATE_CHANGE_ORDER_BTN_TEXT)
            .editProductButtonText(UPDATED_EDIT_PRODUCT_BUTTON_TEXT)
            .deleteButtonText(UPDATED_DELETE_BUTTON_TEXT)
            .updateButtonText(UPDATED_UPDATE_BUTTON_TEXT)
            .cancelButtonText(UPDATED_CANCEL_BUTTON_TEXT)
            .addProductButtonText(UPDATED_ADD_PRODUCT_BUTTON_TEXT)
            .addProductLabelText(UPDATED_ADD_PRODUCT_LABEL_TEXT)
            .activeBadgeText(UPDATED_ACTIVE_BADGE_TEXT)
            .closeBadgeText(UPDATED_CLOSE_BADGE_TEXT)
            .skipOrderButtonText(UPDATED_SKIP_ORDER_BUTTON_TEXT)
            .productLabelText(UPDATED_PRODUCT_LABEL_TEXT)
            .seeMoreDetailsText(UPDATED_SEE_MORE_DETAILS_TEXT)
            .hideDetailsText(UPDATED_HIDE_DETAILS_TEXT)
            .productInSubscriptionText(UPDATED_PRODUCT_IN_SUBSCRIPTION_TEXT)
            .EditQuantityLabelText(UPDATED_EDIT_QUANTITY_LABEL_TEXT)
            .subTotalLabelText(UPDATED_SUB_TOTAL_LABEL_TEXT)
            .paymentNotificationText(UPDATED_PAYMENT_NOTIFICATION_TEXT)
            .editProductFlag(UPDATED_EDIT_PRODUCT_FLAG)
            .deleteProductFlag(UPDATED_DELETE_PRODUCT_FLAG)
            .showShipment(UPDATED_SHOW_SHIPMENT)
            .addAdditionalProduct(UPDATED_ADD_ADDITIONAL_PRODUCT)
            .successText(UPDATED_SUCCESS_TEXT)
            .cancelSubscriptionConfirmPrepaidText(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT)
            .cancelSubscriptionConfirmPayAsYouGoText(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT)
            .cancelSubscriptionPrepaidButtonText(UPDATED_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT)
            .cancelSubscriptionPayAsYouGoButtonText(UPDATED_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT)
            .upcomingFulfillmentText(UPDATED_UPCOMING_FULFILLMENT_TEXT)
            .creditCardText(UPDATED_CREDIT_CARD_TEXT)
            .endingWithText(UPDATED_ENDING_WITH_TEXT)
            .weekText(UPDATED_WEEK_TEXT)
            .dayText(UPDATED_DAY_TEXT)
            .monthText(UPDATED_MONTH_TEXT)
            .yearText(UPDATED_YEAR_TEXT)
            .skipBadgeText(UPDATED_SKIP_BADGE_TEXT)
            .queueBadgeText(UPDATED_QUEUE_BADGE_TEXT)
            .customerPortalSettingJson(UPDATED_CUSTOMER_PORTAL_SETTING_JSON)
            .orderNoteFlag(UPDATED_ORDER_NOTE_FLAG)
            .orderNoteText(UPDATED_ORDER_NOTE_TEXT)
            .useUrlWithCustomerId(UPDATED_USE_URL_WITH_CUSTOMER_ID)
            .productSelectionOption(UPDATED_PRODUCT_SELECTION_OPTION)
            .includeOutOfStockProduct(UPDATED_INCLUDE_OUT_OF_STOCK_PRODUCT)
            .openBadgeText(UPDATED_OPEN_BADGE_TEXT)
            .updateShipmentBillingDate(UPDATED_UPDATE_SHIPMENT_BILLING_DATE)
            .discountCode(UPDATED_DISCOUNT_CODE)
            .freezeOrderTillMinCycle(UPDATED_FREEZE_ORDER_TILL_MIN_CYCLE)
            .addOneTimeProduct(UPDATED_ADD_ONE_TIME_PRODUCT)
            .allowOrderNow(UPDATED_ALLOW_ORDER_NOW)
            .minQtyToAllowDuringAddProduct(UPDATED_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT)
            .allowSplitContract(UPDATED_ALLOW_SPLIT_CONTRACT);
        return customerPortalSettings;
    }

    @BeforeEach
    public void initTest() {
        customerPortalSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerPortalSettings() throws Exception {
        int databaseSizeBeforeCreate = customerPortalSettingsRepository.findAll().size();
        // Create the CustomerPortalSettings
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);
        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerPortalSettings in the database
        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerPortalSettings testCustomerPortalSettings = customerPortalSettingsList.get(customerPortalSettingsList.size() - 1);
        assertThat(testCustomerPortalSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testCustomerPortalSettings.getOrderFrequencyText()).isEqualTo(DEFAULT_ORDER_FREQUENCY_TEXT);
        assertThat(testCustomerPortalSettings.getTotalProductsText()).isEqualTo(DEFAULT_TOTAL_PRODUCTS_TEXT);
        assertThat(testCustomerPortalSettings.getNextOrderText()).isEqualTo(DEFAULT_NEXT_ORDER_TEXT);
        assertThat(testCustomerPortalSettings.getStatusText()).isEqualTo(DEFAULT_STATUS_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionBtnText()).isEqualTo(DEFAULT_CANCEL_SUBSCRIPTION_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getNoSubscriptionMessage()).isEqualTo(DEFAULT_NO_SUBSCRIPTION_MESSAGE);
        assertThat(testCustomerPortalSettings.getSubscriptionNoText()).isEqualTo(DEFAULT_SUBSCRIPTION_NO_TEXT);
        assertThat(testCustomerPortalSettings.getUpdatePaymentMessage()).isEqualTo(DEFAULT_UPDATE_PAYMENT_MESSAGE);
        assertThat(testCustomerPortalSettings.getCardLastFourDigitText()).isEqualTo(DEFAULT_CARD_LAST_FOUR_DIGIT_TEXT);
        assertThat(testCustomerPortalSettings.getCardExpiryText()).isEqualTo(DEFAULT_CARD_EXPIRY_TEXT);
        assertThat(testCustomerPortalSettings.getCardHolderNameText()).isEqualTo(DEFAULT_CARD_HOLDER_NAME_TEXT);
        assertThat(testCustomerPortalSettings.getCardTypeText()).isEqualTo(DEFAULT_CARD_TYPE_TEXT);
        assertThat(testCustomerPortalSettings.getPaymentMethodTypeText()).isEqualTo(DEFAULT_PAYMENT_METHOD_TYPE_TEXT);
        assertThat(testCustomerPortalSettings.getCancelAccordionTitle()).isEqualTo(DEFAULT_CANCEL_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getPaymentDetailAccordionTitle()).isEqualTo(DEFAULT_PAYMENT_DETAIL_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getUpcomingOrderAccordionTitle()).isEqualTo(DEFAULT_UPCOMING_ORDER_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getPaymentInfoText()).isEqualTo(DEFAULT_PAYMENT_INFO_TEXT);
        assertThat(testCustomerPortalSettings.getUpdatePaymentBtnText()).isEqualTo(DEFAULT_UPDATE_PAYMENT_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getNextOrderDateLbl()).isEqualTo(DEFAULT_NEXT_ORDER_DATE_LBL);
        assertThat(testCustomerPortalSettings.getStatusLbl()).isEqualTo(DEFAULT_STATUS_LBL);
        assertThat(testCustomerPortalSettings.getQuantityLbl()).isEqualTo(DEFAULT_QUANTITY_LBL);
        assertThat(testCustomerPortalSettings.getAmountLbl()).isEqualTo(DEFAULT_AMOUNT_LBL);
        assertThat(testCustomerPortalSettings.getOrderNoLbl()).isEqualTo(DEFAULT_ORDER_NO_LBL);
        assertThat(testCustomerPortalSettings.getEditFrequencyBtnText()).isEqualTo(DEFAULT_EDIT_FREQUENCY_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getCancelFreqBtnText()).isEqualTo(DEFAULT_CANCEL_FREQ_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateFreqBtnText()).isEqualTo(DEFAULT_UPDATE_FREQ_BTN_TEXT);
        assertThat(testCustomerPortalSettings.isPauseResumeSub()).isEqualTo(DEFAULT_PAUSE_RESUME_SUB);
        assertThat(testCustomerPortalSettings.isChangeNextOrderDate()).isEqualTo(DEFAULT_CHANGE_NEXT_ORDER_DATE);
        assertThat(testCustomerPortalSettings.isCancelSub()).isEqualTo(DEFAULT_CANCEL_SUB);
        assertThat(testCustomerPortalSettings.isChangeOrderFrequency()).isEqualTo(DEFAULT_CHANGE_ORDER_FREQUENCY);
        assertThat(testCustomerPortalSettings.isCreateAdditionalOrder()).isEqualTo(DEFAULT_CREATE_ADDITIONAL_ORDER);
        assertThat(testCustomerPortalSettings.getManageSubscriptionButtonText()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getEditChangeOrderBtnText()).isEqualTo(DEFAULT_EDIT_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getCancelChangeOrderBtnText()).isEqualTo(DEFAULT_CANCEL_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateChangeOrderBtnText()).isEqualTo(DEFAULT_UPDATE_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getEditProductButtonText()).isEqualTo(DEFAULT_EDIT_PRODUCT_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getDeleteButtonText()).isEqualTo(DEFAULT_DELETE_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateButtonText()).isEqualTo(DEFAULT_UPDATE_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getCancelButtonText()).isEqualTo(DEFAULT_CANCEL_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getAddProductButtonText()).isEqualTo(DEFAULT_ADD_PRODUCT_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getAddProductLabelText()).isEqualTo(DEFAULT_ADD_PRODUCT_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getActiveBadgeText()).isEqualTo(DEFAULT_ACTIVE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getCloseBadgeText()).isEqualTo(DEFAULT_CLOSE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getSkipOrderButtonText()).isEqualTo(DEFAULT_SKIP_ORDER_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getProductLabelText()).isEqualTo(DEFAULT_PRODUCT_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getSeeMoreDetailsText()).isEqualTo(DEFAULT_SEE_MORE_DETAILS_TEXT);
        assertThat(testCustomerPortalSettings.getHideDetailsText()).isEqualTo(DEFAULT_HIDE_DETAILS_TEXT);
        assertThat(testCustomerPortalSettings.getProductInSubscriptionText()).isEqualTo(DEFAULT_PRODUCT_IN_SUBSCRIPTION_TEXT);
        assertThat(testCustomerPortalSettings.getEditQuantityLabelText()).isEqualTo(DEFAULT_EDIT_QUANTITY_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getSubTotalLabelText()).isEqualTo(DEFAULT_SUB_TOTAL_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getPaymentNotificationText()).isEqualTo(DEFAULT_PAYMENT_NOTIFICATION_TEXT);
        assertThat(testCustomerPortalSettings.isEditProductFlag()).isEqualTo(DEFAULT_EDIT_PRODUCT_FLAG);
        assertThat(testCustomerPortalSettings.isDeleteProductFlag()).isEqualTo(DEFAULT_DELETE_PRODUCT_FLAG);
        assertThat(testCustomerPortalSettings.isShowShipment()).isEqualTo(DEFAULT_SHOW_SHIPMENT);
        assertThat(testCustomerPortalSettings.isAddAdditionalProduct()).isEqualTo(DEFAULT_ADD_ADDITIONAL_PRODUCT);
        assertThat(testCustomerPortalSettings.getSuccessText()).isEqualTo(DEFAULT_SUCCESS_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionConfirmPrepaidText()).isEqualTo(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionConfirmPayAsYouGoText()).isEqualTo(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionPrepaidButtonText()).isEqualTo(DEFAULT_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionPayAsYouGoButtonText()).isEqualTo(DEFAULT_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getUpcomingFulfillmentText()).isEqualTo(DEFAULT_UPCOMING_FULFILLMENT_TEXT);
        assertThat(testCustomerPortalSettings.getCreditCardText()).isEqualTo(DEFAULT_CREDIT_CARD_TEXT);
        assertThat(testCustomerPortalSettings.getEndingWithText()).isEqualTo(DEFAULT_ENDING_WITH_TEXT);
        assertThat(testCustomerPortalSettings.getWeekText()).isEqualTo(DEFAULT_WEEK_TEXT);
        assertThat(testCustomerPortalSettings.getDayText()).isEqualTo(DEFAULT_DAY_TEXT);
        assertThat(testCustomerPortalSettings.getMonthText()).isEqualTo(DEFAULT_MONTH_TEXT);
        assertThat(testCustomerPortalSettings.getYearText()).isEqualTo(DEFAULT_YEAR_TEXT);
        assertThat(testCustomerPortalSettings.getSkipBadgeText()).isEqualTo(DEFAULT_SKIP_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getQueueBadgeText()).isEqualTo(DEFAULT_QUEUE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getCustomerPortalSettingJson()).isEqualTo(DEFAULT_CUSTOMER_PORTAL_SETTING_JSON);
        assertThat(testCustomerPortalSettings.isOrderNoteFlag()).isEqualTo(DEFAULT_ORDER_NOTE_FLAG);
        assertThat(testCustomerPortalSettings.getOrderNoteText()).isEqualTo(DEFAULT_ORDER_NOTE_TEXT);
        assertThat(testCustomerPortalSettings.isUseUrlWithCustomerId()).isEqualTo(DEFAULT_USE_URL_WITH_CUSTOMER_ID);
        assertThat(testCustomerPortalSettings.getProductSelectionOption()).isEqualTo(DEFAULT_PRODUCT_SELECTION_OPTION);
        assertThat(testCustomerPortalSettings.isIncludeOutOfStockProduct()).isEqualTo(DEFAULT_INCLUDE_OUT_OF_STOCK_PRODUCT);
        assertThat(testCustomerPortalSettings.getOpenBadgeText()).isEqualTo(DEFAULT_OPEN_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.isUpdateShipmentBillingDate()).isEqualTo(DEFAULT_UPDATE_SHIPMENT_BILLING_DATE);
        assertThat(testCustomerPortalSettings.isDiscountCode()).isEqualTo(DEFAULT_DISCOUNT_CODE);
        assertThat(testCustomerPortalSettings.isFreezeOrderTillMinCycle()).isEqualTo(DEFAULT_FREEZE_ORDER_TILL_MIN_CYCLE);
        assertThat(testCustomerPortalSettings.isAddOneTimeProduct()).isEqualTo(DEFAULT_ADD_ONE_TIME_PRODUCT);
        assertThat(testCustomerPortalSettings.isAllowOrderNow()).isEqualTo(DEFAULT_ALLOW_ORDER_NOW);
        assertThat(testCustomerPortalSettings.getMinQtyToAllowDuringAddProduct()).isEqualTo(DEFAULT_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT);
        assertThat(testCustomerPortalSettings.isAllowSplitContract()).isEqualTo(DEFAULT_ALLOW_SPLIT_CONTRACT);
    }

    @Test
    @Transactional
    public void createCustomerPortalSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerPortalSettingsRepository.findAll().size();

        // Create the CustomerPortalSettings with an existing ID
        customerPortalSettings.setId(1L);
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerPortalSettings in the database
        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setShop(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderFrequencyTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setOrderFrequencyText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalProductsTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setTotalProductsText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNextOrderTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setNextOrderText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setStatusText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCancelSubscriptionBtnTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCancelSubscriptionBtnText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNoSubscriptionMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setNoSubscriptionMessage(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubscriptionNoTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setSubscriptionNoText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatePaymentMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setUpdatePaymentMessage(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardLastFourDigitTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCardLastFourDigitText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardExpiryTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCardExpiryText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardHolderNameTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCardHolderNameText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardTypeTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCardTypeText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentMethodTypeTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setPaymentMethodTypeText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCancelAccordionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCancelAccordionTitle(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentDetailAccordionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setPaymentDetailAccordionTitle(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpcomingOrderAccordionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setUpcomingOrderAccordionTitle(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentInfoTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setPaymentInfoText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatePaymentBtnTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setUpdatePaymentBtnText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNextOrderDateLblIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setNextOrderDateLbl(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusLblIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setStatusLbl(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityLblIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setQuantityLbl(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountLblIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setAmountLbl(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderNoLblIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setOrderNoLbl(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEditFrequencyBtnTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setEditFrequencyBtnText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCancelFreqBtnTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setCancelFreqBtnText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdateFreqBtnTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerPortalSettingsRepository.findAll().size();
        // set the field null
        customerPortalSettings.setUpdateFreqBtnText(null);

        // Create the CustomerPortalSettings, which fails.
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);


        restCustomerPortalSettingsMockMvc.perform(post("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerPortalSettings() throws Exception {
        // Initialize the database
        customerPortalSettingsRepository.saveAndFlush(customerPortalSettings);

        // Get all the customerPortalSettingsList
        restCustomerPortalSettingsMockMvc.perform(get("/api/customer-portal-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerPortalSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].orderFrequencyText").value(hasItem(DEFAULT_ORDER_FREQUENCY_TEXT)))
            .andExpect(jsonPath("$.[*].totalProductsText").value(hasItem(DEFAULT_TOTAL_PRODUCTS_TEXT)))
            .andExpect(jsonPath("$.[*].nextOrderText").value(hasItem(DEFAULT_NEXT_ORDER_TEXT)))
            .andExpect(jsonPath("$.[*].statusText").value(hasItem(DEFAULT_STATUS_TEXT)))
            .andExpect(jsonPath("$.[*].cancelSubscriptionBtnText").value(hasItem(DEFAULT_CANCEL_SUBSCRIPTION_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].noSubscriptionMessage").value(hasItem(DEFAULT_NO_SUBSCRIPTION_MESSAGE)))
            .andExpect(jsonPath("$.[*].subscriptionNoText").value(hasItem(DEFAULT_SUBSCRIPTION_NO_TEXT)))
            .andExpect(jsonPath("$.[*].updatePaymentMessage").value(hasItem(DEFAULT_UPDATE_PAYMENT_MESSAGE)))
            .andExpect(jsonPath("$.[*].cardLastFourDigitText").value(hasItem(DEFAULT_CARD_LAST_FOUR_DIGIT_TEXT)))
            .andExpect(jsonPath("$.[*].cardExpiryText").value(hasItem(DEFAULT_CARD_EXPIRY_TEXT)))
            .andExpect(jsonPath("$.[*].cardHolderNameText").value(hasItem(DEFAULT_CARD_HOLDER_NAME_TEXT)))
            .andExpect(jsonPath("$.[*].cardTypeText").value(hasItem(DEFAULT_CARD_TYPE_TEXT)))
            .andExpect(jsonPath("$.[*].paymentMethodTypeText").value(hasItem(DEFAULT_PAYMENT_METHOD_TYPE_TEXT)))
            .andExpect(jsonPath("$.[*].cancelAccordionTitle").value(hasItem(DEFAULT_CANCEL_ACCORDION_TITLE)))
            .andExpect(jsonPath("$.[*].paymentDetailAccordionTitle").value(hasItem(DEFAULT_PAYMENT_DETAIL_ACCORDION_TITLE)))
            .andExpect(jsonPath("$.[*].upcomingOrderAccordionTitle").value(hasItem(DEFAULT_UPCOMING_ORDER_ACCORDION_TITLE)))
            .andExpect(jsonPath("$.[*].paymentInfoText").value(hasItem(DEFAULT_PAYMENT_INFO_TEXT)))
            .andExpect(jsonPath("$.[*].updatePaymentBtnText").value(hasItem(DEFAULT_UPDATE_PAYMENT_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].nextOrderDateLbl").value(hasItem(DEFAULT_NEXT_ORDER_DATE_LBL)))
            .andExpect(jsonPath("$.[*].statusLbl").value(hasItem(DEFAULT_STATUS_LBL)))
            .andExpect(jsonPath("$.[*].quantityLbl").value(hasItem(DEFAULT_QUANTITY_LBL)))
            .andExpect(jsonPath("$.[*].amountLbl").value(hasItem(DEFAULT_AMOUNT_LBL)))
            .andExpect(jsonPath("$.[*].orderNoLbl").value(hasItem(DEFAULT_ORDER_NO_LBL)))
            .andExpect(jsonPath("$.[*].editFrequencyBtnText").value(hasItem(DEFAULT_EDIT_FREQUENCY_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].cancelFreqBtnText").value(hasItem(DEFAULT_CANCEL_FREQ_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].updateFreqBtnText").value(hasItem(DEFAULT_UPDATE_FREQ_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].pauseResumeSub").value(hasItem(DEFAULT_PAUSE_RESUME_SUB.booleanValue())))
            .andExpect(jsonPath("$.[*].changeNextOrderDate").value(hasItem(DEFAULT_CHANGE_NEXT_ORDER_DATE.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelSub").value(hasItem(DEFAULT_CANCEL_SUB.booleanValue())))
            .andExpect(jsonPath("$.[*].changeOrderFrequency").value(hasItem(DEFAULT_CHANGE_ORDER_FREQUENCY.booleanValue())))
            .andExpect(jsonPath("$.[*].createAdditionalOrder").value(hasItem(DEFAULT_CREATE_ADDITIONAL_ORDER.booleanValue())))
            .andExpect(jsonPath("$.[*].manageSubscriptionButtonText").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].editChangeOrderBtnText").value(hasItem(DEFAULT_EDIT_CHANGE_ORDER_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].cancelChangeOrderBtnText").value(hasItem(DEFAULT_CANCEL_CHANGE_ORDER_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].updateChangeOrderBtnText").value(hasItem(DEFAULT_UPDATE_CHANGE_ORDER_BTN_TEXT)))
            .andExpect(jsonPath("$.[*].editProductButtonText").value(hasItem(DEFAULT_EDIT_PRODUCT_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].deleteButtonText").value(hasItem(DEFAULT_DELETE_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].updateButtonText").value(hasItem(DEFAULT_UPDATE_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].cancelButtonText").value(hasItem(DEFAULT_CANCEL_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].addProductButtonText").value(hasItem(DEFAULT_ADD_PRODUCT_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].addProductLabelText").value(hasItem(DEFAULT_ADD_PRODUCT_LABEL_TEXT)))
            .andExpect(jsonPath("$.[*].activeBadgeText").value(hasItem(DEFAULT_ACTIVE_BADGE_TEXT)))
            .andExpect(jsonPath("$.[*].closeBadgeText").value(hasItem(DEFAULT_CLOSE_BADGE_TEXT)))
            .andExpect(jsonPath("$.[*].skipOrderButtonText").value(hasItem(DEFAULT_SKIP_ORDER_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].productLabelText").value(hasItem(DEFAULT_PRODUCT_LABEL_TEXT)))
            .andExpect(jsonPath("$.[*].seeMoreDetailsText").value(hasItem(DEFAULT_SEE_MORE_DETAILS_TEXT)))
            .andExpect(jsonPath("$.[*].hideDetailsText").value(hasItem(DEFAULT_HIDE_DETAILS_TEXT)))
            .andExpect(jsonPath("$.[*].productInSubscriptionText").value(hasItem(DEFAULT_PRODUCT_IN_SUBSCRIPTION_TEXT)))
            .andExpect(jsonPath("$.[*].EditQuantityLabelText").value(hasItem(DEFAULT_EDIT_QUANTITY_LABEL_TEXT)))
            .andExpect(jsonPath("$.[*].subTotalLabelText").value(hasItem(DEFAULT_SUB_TOTAL_LABEL_TEXT)))
            .andExpect(jsonPath("$.[*].paymentNotificationText").value(hasItem(DEFAULT_PAYMENT_NOTIFICATION_TEXT)))
            .andExpect(jsonPath("$.[*].editProductFlag").value(hasItem(DEFAULT_EDIT_PRODUCT_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].deleteProductFlag").value(hasItem(DEFAULT_DELETE_PRODUCT_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].showShipment").value(hasItem(DEFAULT_SHOW_SHIPMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].addAdditionalProduct").value(hasItem(DEFAULT_ADD_ADDITIONAL_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].successText").value(hasItem(DEFAULT_SUCCESS_TEXT)))
            .andExpect(jsonPath("$.[*].cancelSubscriptionConfirmPrepaidText").value(hasItem(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT)))
            .andExpect(jsonPath("$.[*].cancelSubscriptionConfirmPayAsYouGoText").value(hasItem(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT)))
            .andExpect(jsonPath("$.[*].cancelSubscriptionPrepaidButtonText").value(hasItem(DEFAULT_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].cancelSubscriptionPayAsYouGoButtonText").value(hasItem(DEFAULT_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].upcomingFulfillmentText").value(hasItem(DEFAULT_UPCOMING_FULFILLMENT_TEXT)))
            .andExpect(jsonPath("$.[*].creditCardText").value(hasItem(DEFAULT_CREDIT_CARD_TEXT)))
            .andExpect(jsonPath("$.[*].endingWithText").value(hasItem(DEFAULT_ENDING_WITH_TEXT)))
            .andExpect(jsonPath("$.[*].weekText").value(hasItem(DEFAULT_WEEK_TEXT)))
            .andExpect(jsonPath("$.[*].dayText").value(hasItem(DEFAULT_DAY_TEXT)))
            .andExpect(jsonPath("$.[*].monthText").value(hasItem(DEFAULT_MONTH_TEXT)))
            .andExpect(jsonPath("$.[*].yearText").value(hasItem(DEFAULT_YEAR_TEXT)))
            .andExpect(jsonPath("$.[*].skipBadgeText").value(hasItem(DEFAULT_SKIP_BADGE_TEXT)))
            .andExpect(jsonPath("$.[*].queueBadgeText").value(hasItem(DEFAULT_QUEUE_BADGE_TEXT)))
            .andExpect(jsonPath("$.[*].customerPortalSettingJson").value(hasItem(DEFAULT_CUSTOMER_PORTAL_SETTING_JSON.toString())))
            .andExpect(jsonPath("$.[*].orderNoteFlag").value(hasItem(DEFAULT_ORDER_NOTE_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNoteText").value(hasItem(DEFAULT_ORDER_NOTE_TEXT)))
            .andExpect(jsonPath("$.[*].useUrlWithCustomerId").value(hasItem(DEFAULT_USE_URL_WITH_CUSTOMER_ID.booleanValue())))
            .andExpect(jsonPath("$.[*].productSelectionOption").value(hasItem(DEFAULT_PRODUCT_SELECTION_OPTION.toString())))
            .andExpect(jsonPath("$.[*].includeOutOfStockProduct").value(hasItem(DEFAULT_INCLUDE_OUT_OF_STOCK_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].openBadgeText").value(hasItem(DEFAULT_OPEN_BADGE_TEXT)))
            .andExpect(jsonPath("$.[*].updateShipmentBillingDate").value(hasItem(DEFAULT_UPDATE_SHIPMENT_BILLING_DATE.booleanValue())))
            .andExpect(jsonPath("$.[*].discountCode").value(hasItem(DEFAULT_DISCOUNT_CODE.booleanValue())))
            .andExpect(jsonPath("$.[*].freezeOrderTillMinCycle").value(hasItem(DEFAULT_FREEZE_ORDER_TILL_MIN_CYCLE.booleanValue())))
            .andExpect(jsonPath("$.[*].addOneTimeProduct").value(hasItem(DEFAULT_ADD_ONE_TIME_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].allowOrderNow").value(hasItem(DEFAULT_ALLOW_ORDER_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].minQtyToAllowDuringAddProduct").value(hasItem(DEFAULT_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT)))
            .andExpect(jsonPath("$.[*].allowSplitContract").value(hasItem(DEFAULT_ALLOW_SPLIT_CONTRACT.booleanValue())));
    }

    @Test
    @Transactional
    public void getCustomerPortalSettings() throws Exception {
        // Initialize the database
        customerPortalSettingsRepository.saveAndFlush(customerPortalSettings);

        // Get the customerPortalSettings
        restCustomerPortalSettingsMockMvc.perform(get("/api/customer-portal-settings/{id}", customerPortalSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerPortalSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.orderFrequencyText").value(DEFAULT_ORDER_FREQUENCY_TEXT))
            .andExpect(jsonPath("$.totalProductsText").value(DEFAULT_TOTAL_PRODUCTS_TEXT))
            .andExpect(jsonPath("$.nextOrderText").value(DEFAULT_NEXT_ORDER_TEXT))
            .andExpect(jsonPath("$.statusText").value(DEFAULT_STATUS_TEXT))
            .andExpect(jsonPath("$.cancelSubscriptionBtnText").value(DEFAULT_CANCEL_SUBSCRIPTION_BTN_TEXT))
            .andExpect(jsonPath("$.noSubscriptionMessage").value(DEFAULT_NO_SUBSCRIPTION_MESSAGE))
            .andExpect(jsonPath("$.subscriptionNoText").value(DEFAULT_SUBSCRIPTION_NO_TEXT))
            .andExpect(jsonPath("$.updatePaymentMessage").value(DEFAULT_UPDATE_PAYMENT_MESSAGE))
            .andExpect(jsonPath("$.cardLastFourDigitText").value(DEFAULT_CARD_LAST_FOUR_DIGIT_TEXT))
            .andExpect(jsonPath("$.cardExpiryText").value(DEFAULT_CARD_EXPIRY_TEXT))
            .andExpect(jsonPath("$.cardHolderNameText").value(DEFAULT_CARD_HOLDER_NAME_TEXT))
            .andExpect(jsonPath("$.cardTypeText").value(DEFAULT_CARD_TYPE_TEXT))
            .andExpect(jsonPath("$.paymentMethodTypeText").value(DEFAULT_PAYMENT_METHOD_TYPE_TEXT))
            .andExpect(jsonPath("$.cancelAccordionTitle").value(DEFAULT_CANCEL_ACCORDION_TITLE))
            .andExpect(jsonPath("$.paymentDetailAccordionTitle").value(DEFAULT_PAYMENT_DETAIL_ACCORDION_TITLE))
            .andExpect(jsonPath("$.upcomingOrderAccordionTitle").value(DEFAULT_UPCOMING_ORDER_ACCORDION_TITLE))
            .andExpect(jsonPath("$.paymentInfoText").value(DEFAULT_PAYMENT_INFO_TEXT))
            .andExpect(jsonPath("$.updatePaymentBtnText").value(DEFAULT_UPDATE_PAYMENT_BTN_TEXT))
            .andExpect(jsonPath("$.nextOrderDateLbl").value(DEFAULT_NEXT_ORDER_DATE_LBL))
            .andExpect(jsonPath("$.statusLbl").value(DEFAULT_STATUS_LBL))
            .andExpect(jsonPath("$.quantityLbl").value(DEFAULT_QUANTITY_LBL))
            .andExpect(jsonPath("$.amountLbl").value(DEFAULT_AMOUNT_LBL))
            .andExpect(jsonPath("$.orderNoLbl").value(DEFAULT_ORDER_NO_LBL))
            .andExpect(jsonPath("$.editFrequencyBtnText").value(DEFAULT_EDIT_FREQUENCY_BTN_TEXT))
            .andExpect(jsonPath("$.cancelFreqBtnText").value(DEFAULT_CANCEL_FREQ_BTN_TEXT))
            .andExpect(jsonPath("$.updateFreqBtnText").value(DEFAULT_UPDATE_FREQ_BTN_TEXT))
            .andExpect(jsonPath("$.pauseResumeSub").value(DEFAULT_PAUSE_RESUME_SUB.booleanValue()))
            .andExpect(jsonPath("$.changeNextOrderDate").value(DEFAULT_CHANGE_NEXT_ORDER_DATE.booleanValue()))
            .andExpect(jsonPath("$.cancelSub").value(DEFAULT_CANCEL_SUB.booleanValue()))
            .andExpect(jsonPath("$.changeOrderFrequency").value(DEFAULT_CHANGE_ORDER_FREQUENCY.booleanValue()))
            .andExpect(jsonPath("$.createAdditionalOrder").value(DEFAULT_CREATE_ADDITIONAL_ORDER.booleanValue()))
            .andExpect(jsonPath("$.manageSubscriptionButtonText").value(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT))
            .andExpect(jsonPath("$.editChangeOrderBtnText").value(DEFAULT_EDIT_CHANGE_ORDER_BTN_TEXT))
            .andExpect(jsonPath("$.cancelChangeOrderBtnText").value(DEFAULT_CANCEL_CHANGE_ORDER_BTN_TEXT))
            .andExpect(jsonPath("$.updateChangeOrderBtnText").value(DEFAULT_UPDATE_CHANGE_ORDER_BTN_TEXT))
            .andExpect(jsonPath("$.editProductButtonText").value(DEFAULT_EDIT_PRODUCT_BUTTON_TEXT))
            .andExpect(jsonPath("$.deleteButtonText").value(DEFAULT_DELETE_BUTTON_TEXT))
            .andExpect(jsonPath("$.updateButtonText").value(DEFAULT_UPDATE_BUTTON_TEXT))
            .andExpect(jsonPath("$.cancelButtonText").value(DEFAULT_CANCEL_BUTTON_TEXT))
            .andExpect(jsonPath("$.addProductButtonText").value(DEFAULT_ADD_PRODUCT_BUTTON_TEXT))
            .andExpect(jsonPath("$.addProductLabelText").value(DEFAULT_ADD_PRODUCT_LABEL_TEXT))
            .andExpect(jsonPath("$.activeBadgeText").value(DEFAULT_ACTIVE_BADGE_TEXT))
            .andExpect(jsonPath("$.closeBadgeText").value(DEFAULT_CLOSE_BADGE_TEXT))
            .andExpect(jsonPath("$.skipOrderButtonText").value(DEFAULT_SKIP_ORDER_BUTTON_TEXT))
            .andExpect(jsonPath("$.productLabelText").value(DEFAULT_PRODUCT_LABEL_TEXT))
            .andExpect(jsonPath("$.seeMoreDetailsText").value(DEFAULT_SEE_MORE_DETAILS_TEXT))
            .andExpect(jsonPath("$.hideDetailsText").value(DEFAULT_HIDE_DETAILS_TEXT))
            .andExpect(jsonPath("$.productInSubscriptionText").value(DEFAULT_PRODUCT_IN_SUBSCRIPTION_TEXT))
            .andExpect(jsonPath("$.EditQuantityLabelText").value(DEFAULT_EDIT_QUANTITY_LABEL_TEXT))
            .andExpect(jsonPath("$.subTotalLabelText").value(DEFAULT_SUB_TOTAL_LABEL_TEXT))
            .andExpect(jsonPath("$.paymentNotificationText").value(DEFAULT_PAYMENT_NOTIFICATION_TEXT))
            .andExpect(jsonPath("$.editProductFlag").value(DEFAULT_EDIT_PRODUCT_FLAG.booleanValue()))
            .andExpect(jsonPath("$.deleteProductFlag").value(DEFAULT_DELETE_PRODUCT_FLAG.booleanValue()))
            .andExpect(jsonPath("$.showShipment").value(DEFAULT_SHOW_SHIPMENT.booleanValue()))
            .andExpect(jsonPath("$.addAdditionalProduct").value(DEFAULT_ADD_ADDITIONAL_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.successText").value(DEFAULT_SUCCESS_TEXT))
            .andExpect(jsonPath("$.cancelSubscriptionConfirmPrepaidText").value(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT))
            .andExpect(jsonPath("$.cancelSubscriptionConfirmPayAsYouGoText").value(DEFAULT_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT))
            .andExpect(jsonPath("$.cancelSubscriptionPrepaidButtonText").value(DEFAULT_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT))
            .andExpect(jsonPath("$.cancelSubscriptionPayAsYouGoButtonText").value(DEFAULT_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT))
            .andExpect(jsonPath("$.upcomingFulfillmentText").value(DEFAULT_UPCOMING_FULFILLMENT_TEXT))
            .andExpect(jsonPath("$.creditCardText").value(DEFAULT_CREDIT_CARD_TEXT))
            .andExpect(jsonPath("$.endingWithText").value(DEFAULT_ENDING_WITH_TEXT))
            .andExpect(jsonPath("$.weekText").value(DEFAULT_WEEK_TEXT))
            .andExpect(jsonPath("$.dayText").value(DEFAULT_DAY_TEXT))
            .andExpect(jsonPath("$.monthText").value(DEFAULT_MONTH_TEXT))
            .andExpect(jsonPath("$.yearText").value(DEFAULT_YEAR_TEXT))
            .andExpect(jsonPath("$.skipBadgeText").value(DEFAULT_SKIP_BADGE_TEXT))
            .andExpect(jsonPath("$.queueBadgeText").value(DEFAULT_QUEUE_BADGE_TEXT))
            .andExpect(jsonPath("$.customerPortalSettingJson").value(DEFAULT_CUSTOMER_PORTAL_SETTING_JSON.toString()))
            .andExpect(jsonPath("$.orderNoteFlag").value(DEFAULT_ORDER_NOTE_FLAG.booleanValue()))
            .andExpect(jsonPath("$.orderNoteText").value(DEFAULT_ORDER_NOTE_TEXT))
            .andExpect(jsonPath("$.useUrlWithCustomerId").value(DEFAULT_USE_URL_WITH_CUSTOMER_ID.booleanValue()))
            .andExpect(jsonPath("$.productSelectionOption").value(DEFAULT_PRODUCT_SELECTION_OPTION.toString()))
            .andExpect(jsonPath("$.includeOutOfStockProduct").value(DEFAULT_INCLUDE_OUT_OF_STOCK_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.openBadgeText").value(DEFAULT_OPEN_BADGE_TEXT))
            .andExpect(jsonPath("$.updateShipmentBillingDate").value(DEFAULT_UPDATE_SHIPMENT_BILLING_DATE.booleanValue()))
            .andExpect(jsonPath("$.discountCode").value(DEFAULT_DISCOUNT_CODE.booleanValue()))
            .andExpect(jsonPath("$.freezeOrderTillMinCycle").value(DEFAULT_FREEZE_ORDER_TILL_MIN_CYCLE.booleanValue()))
            .andExpect(jsonPath("$.addOneTimeProduct").value(DEFAULT_ADD_ONE_TIME_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.allowOrderNow").value(DEFAULT_ALLOW_ORDER_NOW.booleanValue()))
            .andExpect(jsonPath("$.minQtyToAllowDuringAddProduct").value(DEFAULT_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT))
            .andExpect(jsonPath("$.allowSplitContract").value(DEFAULT_ALLOW_SPLIT_CONTRACT.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCustomerPortalSettings() throws Exception {
        // Get the customerPortalSettings
        restCustomerPortalSettingsMockMvc.perform(get("/api/customer-portal-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerPortalSettings() throws Exception {
        // Initialize the database
        customerPortalSettingsRepository.saveAndFlush(customerPortalSettings);

        int databaseSizeBeforeUpdate = customerPortalSettingsRepository.findAll().size();

        // Update the customerPortalSettings
        CustomerPortalSettings updatedCustomerPortalSettings = customerPortalSettingsRepository.findById(customerPortalSettings.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerPortalSettings are not directly saved in db
        em.detach(updatedCustomerPortalSettings);
        updatedCustomerPortalSettings
            .shop(UPDATED_SHOP)
            .orderFrequencyText(UPDATED_ORDER_FREQUENCY_TEXT)
            .totalProductsText(UPDATED_TOTAL_PRODUCTS_TEXT)
            .nextOrderText(UPDATED_NEXT_ORDER_TEXT)
            .statusText(UPDATED_STATUS_TEXT)
            .cancelSubscriptionBtnText(UPDATED_CANCEL_SUBSCRIPTION_BTN_TEXT)
            .noSubscriptionMessage(UPDATED_NO_SUBSCRIPTION_MESSAGE)
            .subscriptionNoText(UPDATED_SUBSCRIPTION_NO_TEXT)
            .updatePaymentMessage(UPDATED_UPDATE_PAYMENT_MESSAGE)
            .cardLastFourDigitText(UPDATED_CARD_LAST_FOUR_DIGIT_TEXT)
            .cardExpiryText(UPDATED_CARD_EXPIRY_TEXT)
            .cardHolderNameText(UPDATED_CARD_HOLDER_NAME_TEXT)
            .cardTypeText(UPDATED_CARD_TYPE_TEXT)
            .paymentMethodTypeText(UPDATED_PAYMENT_METHOD_TYPE_TEXT)
            .cancelAccordionTitle(UPDATED_CANCEL_ACCORDION_TITLE)
            .paymentDetailAccordionTitle(UPDATED_PAYMENT_DETAIL_ACCORDION_TITLE)
            .upcomingOrderAccordionTitle(UPDATED_UPCOMING_ORDER_ACCORDION_TITLE)
            .paymentInfoText(UPDATED_PAYMENT_INFO_TEXT)
            .updatePaymentBtnText(UPDATED_UPDATE_PAYMENT_BTN_TEXT)
            .nextOrderDateLbl(UPDATED_NEXT_ORDER_DATE_LBL)
            .statusLbl(UPDATED_STATUS_LBL)
            .quantityLbl(UPDATED_QUANTITY_LBL)
            .amountLbl(UPDATED_AMOUNT_LBL)
            .orderNoLbl(UPDATED_ORDER_NO_LBL)
            .editFrequencyBtnText(UPDATED_EDIT_FREQUENCY_BTN_TEXT)
            .cancelFreqBtnText(UPDATED_CANCEL_FREQ_BTN_TEXT)
            .updateFreqBtnText(UPDATED_UPDATE_FREQ_BTN_TEXT)
            .pauseResumeSub(UPDATED_PAUSE_RESUME_SUB)
            .changeNextOrderDate(UPDATED_CHANGE_NEXT_ORDER_DATE)
            .cancelSub(UPDATED_CANCEL_SUB)
            .changeOrderFrequency(UPDATED_CHANGE_ORDER_FREQUENCY)
            .createAdditionalOrder(UPDATED_CREATE_ADDITIONAL_ORDER)
            .manageSubscriptionButtonText(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .editChangeOrderBtnText(UPDATED_EDIT_CHANGE_ORDER_BTN_TEXT)
            .cancelChangeOrderBtnText(UPDATED_CANCEL_CHANGE_ORDER_BTN_TEXT)
            .updateChangeOrderBtnText(UPDATED_UPDATE_CHANGE_ORDER_BTN_TEXT)
            .editProductButtonText(UPDATED_EDIT_PRODUCT_BUTTON_TEXT)
            .deleteButtonText(UPDATED_DELETE_BUTTON_TEXT)
            .updateButtonText(UPDATED_UPDATE_BUTTON_TEXT)
            .cancelButtonText(UPDATED_CANCEL_BUTTON_TEXT)
            .addProductButtonText(UPDATED_ADD_PRODUCT_BUTTON_TEXT)
            .addProductLabelText(UPDATED_ADD_PRODUCT_LABEL_TEXT)
            .activeBadgeText(UPDATED_ACTIVE_BADGE_TEXT)
            .closeBadgeText(UPDATED_CLOSE_BADGE_TEXT)
            .skipOrderButtonText(UPDATED_SKIP_ORDER_BUTTON_TEXT)
            .productLabelText(UPDATED_PRODUCT_LABEL_TEXT)
            .seeMoreDetailsText(UPDATED_SEE_MORE_DETAILS_TEXT)
            .hideDetailsText(UPDATED_HIDE_DETAILS_TEXT)
            .productInSubscriptionText(UPDATED_PRODUCT_IN_SUBSCRIPTION_TEXT)
            .EditQuantityLabelText(UPDATED_EDIT_QUANTITY_LABEL_TEXT)
            .subTotalLabelText(UPDATED_SUB_TOTAL_LABEL_TEXT)
            .paymentNotificationText(UPDATED_PAYMENT_NOTIFICATION_TEXT)
            .editProductFlag(UPDATED_EDIT_PRODUCT_FLAG)
            .deleteProductFlag(UPDATED_DELETE_PRODUCT_FLAG)
            .showShipment(UPDATED_SHOW_SHIPMENT)
            .addAdditionalProduct(UPDATED_ADD_ADDITIONAL_PRODUCT)
            .successText(UPDATED_SUCCESS_TEXT)
            .cancelSubscriptionConfirmPrepaidText(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT)
            .cancelSubscriptionConfirmPayAsYouGoText(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT)
            .cancelSubscriptionPrepaidButtonText(UPDATED_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT)
            .cancelSubscriptionPayAsYouGoButtonText(UPDATED_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT)
            .upcomingFulfillmentText(UPDATED_UPCOMING_FULFILLMENT_TEXT)
            .creditCardText(UPDATED_CREDIT_CARD_TEXT)
            .endingWithText(UPDATED_ENDING_WITH_TEXT)
            .weekText(UPDATED_WEEK_TEXT)
            .dayText(UPDATED_DAY_TEXT)
            .monthText(UPDATED_MONTH_TEXT)
            .yearText(UPDATED_YEAR_TEXT)
            .skipBadgeText(UPDATED_SKIP_BADGE_TEXT)
            .queueBadgeText(UPDATED_QUEUE_BADGE_TEXT)
            .customerPortalSettingJson(UPDATED_CUSTOMER_PORTAL_SETTING_JSON)
            .orderNoteFlag(UPDATED_ORDER_NOTE_FLAG)
            .orderNoteText(UPDATED_ORDER_NOTE_TEXT)
            .useUrlWithCustomerId(UPDATED_USE_URL_WITH_CUSTOMER_ID)
            .productSelectionOption(UPDATED_PRODUCT_SELECTION_OPTION)
            .includeOutOfStockProduct(UPDATED_INCLUDE_OUT_OF_STOCK_PRODUCT)
            .openBadgeText(UPDATED_OPEN_BADGE_TEXT)
            .updateShipmentBillingDate(UPDATED_UPDATE_SHIPMENT_BILLING_DATE)
            .discountCode(UPDATED_DISCOUNT_CODE)
            .freezeOrderTillMinCycle(UPDATED_FREEZE_ORDER_TILL_MIN_CYCLE)
            .addOneTimeProduct(UPDATED_ADD_ONE_TIME_PRODUCT)
            .allowOrderNow(UPDATED_ALLOW_ORDER_NOW)
            .minQtyToAllowDuringAddProduct(UPDATED_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT)
            .allowSplitContract(UPDATED_ALLOW_SPLIT_CONTRACT);
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(updatedCustomerPortalSettings);

        restCustomerPortalSettingsMockMvc.perform(put("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerPortalSettings in the database
        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeUpdate);
        CustomerPortalSettings testCustomerPortalSettings = customerPortalSettingsList.get(customerPortalSettingsList.size() - 1);
        assertThat(testCustomerPortalSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testCustomerPortalSettings.getOrderFrequencyText()).isEqualTo(UPDATED_ORDER_FREQUENCY_TEXT);
        assertThat(testCustomerPortalSettings.getTotalProductsText()).isEqualTo(UPDATED_TOTAL_PRODUCTS_TEXT);
        assertThat(testCustomerPortalSettings.getNextOrderText()).isEqualTo(UPDATED_NEXT_ORDER_TEXT);
        assertThat(testCustomerPortalSettings.getStatusText()).isEqualTo(UPDATED_STATUS_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionBtnText()).isEqualTo(UPDATED_CANCEL_SUBSCRIPTION_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getNoSubscriptionMessage()).isEqualTo(UPDATED_NO_SUBSCRIPTION_MESSAGE);
        assertThat(testCustomerPortalSettings.getSubscriptionNoText()).isEqualTo(UPDATED_SUBSCRIPTION_NO_TEXT);
        assertThat(testCustomerPortalSettings.getUpdatePaymentMessage()).isEqualTo(UPDATED_UPDATE_PAYMENT_MESSAGE);
        assertThat(testCustomerPortalSettings.getCardLastFourDigitText()).isEqualTo(UPDATED_CARD_LAST_FOUR_DIGIT_TEXT);
        assertThat(testCustomerPortalSettings.getCardExpiryText()).isEqualTo(UPDATED_CARD_EXPIRY_TEXT);
        assertThat(testCustomerPortalSettings.getCardHolderNameText()).isEqualTo(UPDATED_CARD_HOLDER_NAME_TEXT);
        assertThat(testCustomerPortalSettings.getCardTypeText()).isEqualTo(UPDATED_CARD_TYPE_TEXT);
        assertThat(testCustomerPortalSettings.getPaymentMethodTypeText()).isEqualTo(UPDATED_PAYMENT_METHOD_TYPE_TEXT);
        assertThat(testCustomerPortalSettings.getCancelAccordionTitle()).isEqualTo(UPDATED_CANCEL_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getPaymentDetailAccordionTitle()).isEqualTo(UPDATED_PAYMENT_DETAIL_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getUpcomingOrderAccordionTitle()).isEqualTo(UPDATED_UPCOMING_ORDER_ACCORDION_TITLE);
        assertThat(testCustomerPortalSettings.getPaymentInfoText()).isEqualTo(UPDATED_PAYMENT_INFO_TEXT);
        assertThat(testCustomerPortalSettings.getUpdatePaymentBtnText()).isEqualTo(UPDATED_UPDATE_PAYMENT_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getNextOrderDateLbl()).isEqualTo(UPDATED_NEXT_ORDER_DATE_LBL);
        assertThat(testCustomerPortalSettings.getStatusLbl()).isEqualTo(UPDATED_STATUS_LBL);
        assertThat(testCustomerPortalSettings.getQuantityLbl()).isEqualTo(UPDATED_QUANTITY_LBL);
        assertThat(testCustomerPortalSettings.getAmountLbl()).isEqualTo(UPDATED_AMOUNT_LBL);
        assertThat(testCustomerPortalSettings.getOrderNoLbl()).isEqualTo(UPDATED_ORDER_NO_LBL);
        assertThat(testCustomerPortalSettings.getEditFrequencyBtnText()).isEqualTo(UPDATED_EDIT_FREQUENCY_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getCancelFreqBtnText()).isEqualTo(UPDATED_CANCEL_FREQ_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateFreqBtnText()).isEqualTo(UPDATED_UPDATE_FREQ_BTN_TEXT);
        assertThat(testCustomerPortalSettings.isPauseResumeSub()).isEqualTo(UPDATED_PAUSE_RESUME_SUB);
        assertThat(testCustomerPortalSettings.isChangeNextOrderDate()).isEqualTo(UPDATED_CHANGE_NEXT_ORDER_DATE);
        assertThat(testCustomerPortalSettings.isCancelSub()).isEqualTo(UPDATED_CANCEL_SUB);
        assertThat(testCustomerPortalSettings.isChangeOrderFrequency()).isEqualTo(UPDATED_CHANGE_ORDER_FREQUENCY);
        assertThat(testCustomerPortalSettings.isCreateAdditionalOrder()).isEqualTo(UPDATED_CREATE_ADDITIONAL_ORDER);
        assertThat(testCustomerPortalSettings.getManageSubscriptionButtonText()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getEditChangeOrderBtnText()).isEqualTo(UPDATED_EDIT_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getCancelChangeOrderBtnText()).isEqualTo(UPDATED_CANCEL_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateChangeOrderBtnText()).isEqualTo(UPDATED_UPDATE_CHANGE_ORDER_BTN_TEXT);
        assertThat(testCustomerPortalSettings.getEditProductButtonText()).isEqualTo(UPDATED_EDIT_PRODUCT_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getDeleteButtonText()).isEqualTo(UPDATED_DELETE_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getUpdateButtonText()).isEqualTo(UPDATED_UPDATE_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getCancelButtonText()).isEqualTo(UPDATED_CANCEL_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getAddProductButtonText()).isEqualTo(UPDATED_ADD_PRODUCT_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getAddProductLabelText()).isEqualTo(UPDATED_ADD_PRODUCT_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getActiveBadgeText()).isEqualTo(UPDATED_ACTIVE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getCloseBadgeText()).isEqualTo(UPDATED_CLOSE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getSkipOrderButtonText()).isEqualTo(UPDATED_SKIP_ORDER_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getProductLabelText()).isEqualTo(UPDATED_PRODUCT_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getSeeMoreDetailsText()).isEqualTo(UPDATED_SEE_MORE_DETAILS_TEXT);
        assertThat(testCustomerPortalSettings.getHideDetailsText()).isEqualTo(UPDATED_HIDE_DETAILS_TEXT);
        assertThat(testCustomerPortalSettings.getProductInSubscriptionText()).isEqualTo(UPDATED_PRODUCT_IN_SUBSCRIPTION_TEXT);
        assertThat(testCustomerPortalSettings.getEditQuantityLabelText()).isEqualTo(UPDATED_EDIT_QUANTITY_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getSubTotalLabelText()).isEqualTo(UPDATED_SUB_TOTAL_LABEL_TEXT);
        assertThat(testCustomerPortalSettings.getPaymentNotificationText()).isEqualTo(UPDATED_PAYMENT_NOTIFICATION_TEXT);
        assertThat(testCustomerPortalSettings.isEditProductFlag()).isEqualTo(UPDATED_EDIT_PRODUCT_FLAG);
        assertThat(testCustomerPortalSettings.isDeleteProductFlag()).isEqualTo(UPDATED_DELETE_PRODUCT_FLAG);
        assertThat(testCustomerPortalSettings.isShowShipment()).isEqualTo(UPDATED_SHOW_SHIPMENT);
        assertThat(testCustomerPortalSettings.isAddAdditionalProduct()).isEqualTo(UPDATED_ADD_ADDITIONAL_PRODUCT);
        assertThat(testCustomerPortalSettings.getSuccessText()).isEqualTo(UPDATED_SUCCESS_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionConfirmPrepaidText()).isEqualTo(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PREPAID_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionConfirmPayAsYouGoText()).isEqualTo(UPDATED_CANCEL_SUBSCRIPTION_CONFIRM_PAY_AS_YOU_GO_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionPrepaidButtonText()).isEqualTo(UPDATED_CANCEL_SUBSCRIPTION_PREPAID_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getCancelSubscriptionPayAsYouGoButtonText()).isEqualTo(UPDATED_CANCEL_SUBSCRIPTION_PAY_AS_YOU_GO_BUTTON_TEXT);
        assertThat(testCustomerPortalSettings.getUpcomingFulfillmentText()).isEqualTo(UPDATED_UPCOMING_FULFILLMENT_TEXT);
        assertThat(testCustomerPortalSettings.getCreditCardText()).isEqualTo(UPDATED_CREDIT_CARD_TEXT);
        assertThat(testCustomerPortalSettings.getEndingWithText()).isEqualTo(UPDATED_ENDING_WITH_TEXT);
        assertThat(testCustomerPortalSettings.getWeekText()).isEqualTo(UPDATED_WEEK_TEXT);
        assertThat(testCustomerPortalSettings.getDayText()).isEqualTo(UPDATED_DAY_TEXT);
        assertThat(testCustomerPortalSettings.getMonthText()).isEqualTo(UPDATED_MONTH_TEXT);
        assertThat(testCustomerPortalSettings.getYearText()).isEqualTo(UPDATED_YEAR_TEXT);
        assertThat(testCustomerPortalSettings.getSkipBadgeText()).isEqualTo(UPDATED_SKIP_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getQueueBadgeText()).isEqualTo(UPDATED_QUEUE_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.getCustomerPortalSettingJson()).isEqualTo(UPDATED_CUSTOMER_PORTAL_SETTING_JSON);
        assertThat(testCustomerPortalSettings.isOrderNoteFlag()).isEqualTo(UPDATED_ORDER_NOTE_FLAG);
        assertThat(testCustomerPortalSettings.getOrderNoteText()).isEqualTo(UPDATED_ORDER_NOTE_TEXT);
        assertThat(testCustomerPortalSettings.isUseUrlWithCustomerId()).isEqualTo(UPDATED_USE_URL_WITH_CUSTOMER_ID);
        assertThat(testCustomerPortalSettings.getProductSelectionOption()).isEqualTo(UPDATED_PRODUCT_SELECTION_OPTION);
        assertThat(testCustomerPortalSettings.isIncludeOutOfStockProduct()).isEqualTo(UPDATED_INCLUDE_OUT_OF_STOCK_PRODUCT);
        assertThat(testCustomerPortalSettings.getOpenBadgeText()).isEqualTo(UPDATED_OPEN_BADGE_TEXT);
        assertThat(testCustomerPortalSettings.isUpdateShipmentBillingDate()).isEqualTo(UPDATED_UPDATE_SHIPMENT_BILLING_DATE);
        assertThat(testCustomerPortalSettings.isDiscountCode()).isEqualTo(UPDATED_DISCOUNT_CODE);
        assertThat(testCustomerPortalSettings.isFreezeOrderTillMinCycle()).isEqualTo(UPDATED_FREEZE_ORDER_TILL_MIN_CYCLE);
        assertThat(testCustomerPortalSettings.isAddOneTimeProduct()).isEqualTo(UPDATED_ADD_ONE_TIME_PRODUCT);
        assertThat(testCustomerPortalSettings.isAllowOrderNow()).isEqualTo(UPDATED_ALLOW_ORDER_NOW);
        assertThat(testCustomerPortalSettings.getMinQtyToAllowDuringAddProduct()).isEqualTo(UPDATED_MIN_QTY_TO_ALLOW_DURING_ADD_PRODUCT);
        assertThat(testCustomerPortalSettings.isAllowSplitContract()).isEqualTo(UPDATED_ALLOW_SPLIT_CONTRACT);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerPortalSettings() throws Exception {
        int databaseSizeBeforeUpdate = customerPortalSettingsRepository.findAll().size();

        // Create the CustomerPortalSettings
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsMapper.toDto(customerPortalSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerPortalSettingsMockMvc.perform(put("/api/customer-portal-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerPortalSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerPortalSettings in the database
        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerPortalSettings() throws Exception {
        // Initialize the database
        customerPortalSettingsRepository.saveAndFlush(customerPortalSettings);

        int databaseSizeBeforeDelete = customerPortalSettingsRepository.findAll().size();

        // Delete the customerPortalSettings
        restCustomerPortalSettingsMockMvc.perform(delete("/api/customer-portal-settings/{id}", customerPortalSettings.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerPortalSettings> customerPortalSettingsList = customerPortalSettingsRepository.findAll();
        assertThat(customerPortalSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
