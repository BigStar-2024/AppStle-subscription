package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.EmailTemplateSetting;
import com.et.repository.EmailTemplateSettingRepository;
import com.et.service.EmailTemplateSettingService;

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

import com.et.domain.enumeration.EmailSettingType;
/**
 * Integration tests for the {@link EmailTemplateSettingResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EmailTemplateSettingResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final EmailSettingType DEFAULT_EMAIL_SETTING_TYPE = EmailSettingType.SUBSCRIPTION_CREATED;
    private static final EmailSettingType UPDATED_EMAIL_SETTING_TYPE = EmailSettingType.TRANSACTION_FAILED;

    private static final Boolean DEFAULT_SEND_EMAIL_DISABLED = false;
    private static final Boolean UPDATED_SEND_EMAIL_DISABLED = true;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_FROM_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_HEADING = "AAAAAAAAAA";
    private static final String UPDATED_HEADING = "BBBBBBBBBB";

    private static final String DEFAULT_HEADING_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_HEADING_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_LINK_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_LINK_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER_LINK_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER_LINK_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_HTML = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BUTTON_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPING_ADDRESS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_ADDRESS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_ADDRESS_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_ADDRESS_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_ORDERDATE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_ORDERDATE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ENDING_IN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ENDING_IN_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BCC_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_BCC_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPCOMING_ORDER_EMAIL_BUFFER = 1;
    private static final Integer UPDATED_UPCOMING_ORDER_EMAIL_BUFFER = 2;

    private static final String DEFAULT_HEADING_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_HEADING_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_URL = "AAAAAAAAAA";
    private static final String UPDATED_MANAGE_SUBSCRIPTION_BUTTON_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_HEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_WIDTH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_WIDTH = "BBBBBBBBBB";

    private static final String DEFAULT_THANKS_IMAGE_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_THANKS_IMAGE_HEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_THANKS_IMAGE_WIDTH = "AAAAAAAAAA";
    private static final String UPDATED_THANKS_IMAGE_WIDTH = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_ALIGNMENT = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_ALIGNMENT = "BBBBBBBBBB";

    private static final String DEFAULT_THANKS_IMAGE_ALIGNMENT = "AAAAAAAAAA";
    private static final String UPDATED_THANKS_IMAGE_ALIGNMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_REPLY_TO = "AAAAAAAAAA";
    private static final String UPDATED_REPLY_TO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEND_BCC_EMAIL_FLAG = false;
    private static final Boolean UPDATED_SEND_BCC_EMAIL_FLAG = true;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailTemplateSettingMockMvc;

    private EmailTemplateSetting emailTemplateSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailTemplateSetting createEntity(EntityManager em) {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting()
            .shop(DEFAULT_SHOP)
            .emailSettingType(DEFAULT_EMAIL_SETTING_TYPE)
            .sendEmailDisabled(DEFAULT_SEND_EMAIL_DISABLED)
            .subject(DEFAULT_SUBJECT)
            .fromEmail(DEFAULT_FROM_EMAIL)
            .logo(DEFAULT_LOGO)
            .heading(DEFAULT_HEADING)
            .headingTextColor(DEFAULT_HEADING_TEXT_COLOR)
            .contentTextColor(DEFAULT_CONTENT_TEXT_COLOR)
            .contentLinkColor(DEFAULT_CONTENT_LINK_COLOR)
            .content(DEFAULT_CONTENT)
            .footerTextColor(DEFAULT_FOOTER_TEXT_COLOR)
            .footerLinkColor(DEFAULT_FOOTER_LINK_COLOR)
            .footerText(DEFAULT_FOOTER_TEXT)
            .templateBackgroundColor(DEFAULT_TEMPLATE_BACKGROUND_COLOR)
            .html(DEFAULT_HTML)
            .textImageUrl(DEFAULT_TEXT_IMAGE_URL)
            .manageSubscriptionButtonColor(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_COLOR)
            .manageSubscriptionButtonText(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .manageSubscriptionButtonTextColor(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR)
            .shippingAddressText(DEFAULT_SHIPPING_ADDRESS_TEXT)
            .billingAddressText(DEFAULT_BILLING_ADDRESS_TEXT)
            .nextOrderdateText(DEFAULT_NEXT_ORDERDATE_TEXT)
            .paymentMethodText(DEFAULT_PAYMENT_METHOD_TEXT)
            .endingInText(DEFAULT_ENDING_IN_TEXT)
            .bccEmail(DEFAULT_BCC_EMAIL)
            .upcomingOrderEmailBuffer(DEFAULT_UPCOMING_ORDER_EMAIL_BUFFER)
            .headingImageUrl(DEFAULT_HEADING_IMAGE_URL)
            .quantityText(DEFAULT_QUANTITY_TEXT)
            .manageSubscriptionButtonUrl(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_URL)
            .logoHeight(DEFAULT_LOGO_HEIGHT)
            .logoWidth(DEFAULT_LOGO_WIDTH)
            .thanksImageHeight(DEFAULT_THANKS_IMAGE_HEIGHT)
            .thanksImageWidth(DEFAULT_THANKS_IMAGE_WIDTH)
            .logoAlignment(DEFAULT_LOGO_ALIGNMENT)
            .thanksImageAlignment(DEFAULT_THANKS_IMAGE_ALIGNMENT)
            .shippingAddress(DEFAULT_SHIPPING_ADDRESS)
            .billingAddress(DEFAULT_BILLING_ADDRESS)
            .replyTo(DEFAULT_REPLY_TO)
            .sendBCCEmailFlag(DEFAULT_SEND_BCC_EMAIL_FLAG);
        return emailTemplateSetting;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailTemplateSetting createUpdatedEntity(EntityManager em) {
        EmailTemplateSetting emailTemplateSetting = new EmailTemplateSetting()
            .shop(UPDATED_SHOP)
            .emailSettingType(UPDATED_EMAIL_SETTING_TYPE)
            .sendEmailDisabled(UPDATED_SEND_EMAIL_DISABLED)
            .subject(UPDATED_SUBJECT)
            .fromEmail(UPDATED_FROM_EMAIL)
            .logo(UPDATED_LOGO)
            .heading(UPDATED_HEADING)
            .headingTextColor(UPDATED_HEADING_TEXT_COLOR)
            .contentTextColor(UPDATED_CONTENT_TEXT_COLOR)
            .contentLinkColor(UPDATED_CONTENT_LINK_COLOR)
            .content(UPDATED_CONTENT)
            .footerTextColor(UPDATED_FOOTER_TEXT_COLOR)
            .footerLinkColor(UPDATED_FOOTER_LINK_COLOR)
            .footerText(UPDATED_FOOTER_TEXT)
            .templateBackgroundColor(UPDATED_TEMPLATE_BACKGROUND_COLOR)
            .html(UPDATED_HTML)
            .textImageUrl(UPDATED_TEXT_IMAGE_URL)
            .manageSubscriptionButtonColor(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_COLOR)
            .manageSubscriptionButtonText(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .manageSubscriptionButtonTextColor(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR)
            .shippingAddressText(UPDATED_SHIPPING_ADDRESS_TEXT)
            .billingAddressText(UPDATED_BILLING_ADDRESS_TEXT)
            .nextOrderdateText(UPDATED_NEXT_ORDERDATE_TEXT)
            .paymentMethodText(UPDATED_PAYMENT_METHOD_TEXT)
            .endingInText(UPDATED_ENDING_IN_TEXT)
            .bccEmail(UPDATED_BCC_EMAIL)
            .upcomingOrderEmailBuffer(UPDATED_UPCOMING_ORDER_EMAIL_BUFFER)
            .headingImageUrl(UPDATED_HEADING_IMAGE_URL)
            .quantityText(UPDATED_QUANTITY_TEXT)
            .manageSubscriptionButtonUrl(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_URL)
            .logoHeight(UPDATED_LOGO_HEIGHT)
            .logoWidth(UPDATED_LOGO_WIDTH)
            .thanksImageHeight(UPDATED_THANKS_IMAGE_HEIGHT)
            .thanksImageWidth(UPDATED_THANKS_IMAGE_WIDTH)
            .logoAlignment(UPDATED_LOGO_ALIGNMENT)
            .thanksImageAlignment(UPDATED_THANKS_IMAGE_ALIGNMENT)
            .shippingAddress(UPDATED_SHIPPING_ADDRESS)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .replyTo(UPDATED_REPLY_TO)
            .sendBCCEmailFlag(UPDATED_SEND_BCC_EMAIL_FLAG);
        return emailTemplateSetting;
    }

    @BeforeEach
    public void initTest() {
        emailTemplateSetting = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailTemplateSetting() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateSettingRepository.findAll().size();
        // Create the EmailTemplateSetting
        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isCreated());

        // Validate the EmailTemplateSetting in the database
        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeCreate + 1);
        EmailTemplateSetting testEmailTemplateSetting = emailTemplateSettingList.get(emailTemplateSettingList.size() - 1);
        assertThat(testEmailTemplateSetting.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testEmailTemplateSetting.getEmailSettingType()).isEqualTo(DEFAULT_EMAIL_SETTING_TYPE);
        assertThat(testEmailTemplateSetting.isSendEmailDisabled()).isEqualTo(DEFAULT_SEND_EMAIL_DISABLED);
        assertThat(testEmailTemplateSetting.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testEmailTemplateSetting.getFromEmail()).isEqualTo(DEFAULT_FROM_EMAIL);
        assertThat(testEmailTemplateSetting.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testEmailTemplateSetting.getHeading()).isEqualTo(DEFAULT_HEADING);
        assertThat(testEmailTemplateSetting.getHeadingTextColor()).isEqualTo(DEFAULT_HEADING_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getContentTextColor()).isEqualTo(DEFAULT_CONTENT_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getContentLinkColor()).isEqualTo(DEFAULT_CONTENT_LINK_COLOR);
        assertThat(testEmailTemplateSetting.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEmailTemplateSetting.getFooterTextColor()).isEqualTo(DEFAULT_FOOTER_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getFooterLinkColor()).isEqualTo(DEFAULT_FOOTER_LINK_COLOR);
        assertThat(testEmailTemplateSetting.getFooterText()).isEqualTo(DEFAULT_FOOTER_TEXT);
        assertThat(testEmailTemplateSetting.getTemplateBackgroundColor()).isEqualTo(DEFAULT_TEMPLATE_BACKGROUND_COLOR);
        assertThat(testEmailTemplateSetting.getHtml()).isEqualTo(DEFAULT_HTML);
        assertThat(testEmailTemplateSetting.getTextImageUrl()).isEqualTo(DEFAULT_TEXT_IMAGE_URL);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonColor()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_COLOR);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonText()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonTextColor()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getShippingAddressText()).isEqualTo(DEFAULT_SHIPPING_ADDRESS_TEXT);
        assertThat(testEmailTemplateSetting.getBillingAddressText()).isEqualTo(DEFAULT_BILLING_ADDRESS_TEXT);
        assertThat(testEmailTemplateSetting.getNextOrderdateText()).isEqualTo(DEFAULT_NEXT_ORDERDATE_TEXT);
        assertThat(testEmailTemplateSetting.getPaymentMethodText()).isEqualTo(DEFAULT_PAYMENT_METHOD_TEXT);
        assertThat(testEmailTemplateSetting.getEndingInText()).isEqualTo(DEFAULT_ENDING_IN_TEXT);
        assertThat(testEmailTemplateSetting.getBccEmail()).isEqualTo(DEFAULT_BCC_EMAIL);
        assertThat(testEmailTemplateSetting.getUpcomingOrderEmailBuffer()).isEqualTo(DEFAULT_UPCOMING_ORDER_EMAIL_BUFFER);
        assertThat(testEmailTemplateSetting.getHeadingImageUrl()).isEqualTo(DEFAULT_HEADING_IMAGE_URL);
        assertThat(testEmailTemplateSetting.getQuantityText()).isEqualTo(DEFAULT_QUANTITY_TEXT);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonUrl()).isEqualTo(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_URL);
        assertThat(testEmailTemplateSetting.getLogoHeight()).isEqualTo(DEFAULT_LOGO_HEIGHT);
        assertThat(testEmailTemplateSetting.getLogoWidth()).isEqualTo(DEFAULT_LOGO_WIDTH);
        assertThat(testEmailTemplateSetting.getThanksImageHeight()).isEqualTo(DEFAULT_THANKS_IMAGE_HEIGHT);
        assertThat(testEmailTemplateSetting.getThanksImageWidth()).isEqualTo(DEFAULT_THANKS_IMAGE_WIDTH);
        assertThat(testEmailTemplateSetting.getLogoAlignment()).isEqualTo(DEFAULT_LOGO_ALIGNMENT);
        assertThat(testEmailTemplateSetting.getThanksImageAlignment()).isEqualTo(DEFAULT_THANKS_IMAGE_ALIGNMENT);
        assertThat(testEmailTemplateSetting.getShippingAddress()).isEqualTo(DEFAULT_SHIPPING_ADDRESS);
        assertThat(testEmailTemplateSetting.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
        assertThat(testEmailTemplateSetting.getReplyTo()).isEqualTo(DEFAULT_REPLY_TO);
        assertThat(testEmailTemplateSetting.isSendBCCEmailFlag()).isEqualTo(DEFAULT_SEND_BCC_EMAIL_FLAG);
    }

    @Test
    @Transactional
    public void createEmailTemplateSettingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailTemplateSettingRepository.findAll().size();

        // Create the EmailTemplateSetting with an existing ID
        emailTemplateSetting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        // Validate the EmailTemplateSetting in the database
        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setShop(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSendEmailDisabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setSendEmailDisabled(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setFromEmail(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLogoIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setLogo(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeadingTextColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setHeadingTextColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentTextColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setContentTextColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentLinkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setContentLinkColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFooterTextColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setFooterTextColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFooterLinkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setFooterLinkColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTemplateBackgroundColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailTemplateSettingRepository.findAll().size();
        // set the field null
        emailTemplateSetting.setTemplateBackgroundColor(null);

        // Create the EmailTemplateSetting, which fails.


        restEmailTemplateSettingMockMvc.perform(post("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmailTemplateSettings() throws Exception {
        // Initialize the database
        emailTemplateSettingRepository.saveAndFlush(emailTemplateSetting);

        // Get all the emailTemplateSettingList
        restEmailTemplateSettingMockMvc.perform(get("/api/email-template-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailTemplateSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].emailSettingType").value(hasItem(DEFAULT_EMAIL_SETTING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendEmailDisabled").value(hasItem(DEFAULT_SEND_EMAIL_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].fromEmail").value(hasItem(DEFAULT_FROM_EMAIL)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING.toString())))
            .andExpect(jsonPath("$.[*].headingTextColor").value(hasItem(DEFAULT_HEADING_TEXT_COLOR)))
            .andExpect(jsonPath("$.[*].contentTextColor").value(hasItem(DEFAULT_CONTENT_TEXT_COLOR)))
            .andExpect(jsonPath("$.[*].contentLinkColor").value(hasItem(DEFAULT_CONTENT_LINK_COLOR)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].footerTextColor").value(hasItem(DEFAULT_FOOTER_TEXT_COLOR)))
            .andExpect(jsonPath("$.[*].footerLinkColor").value(hasItem(DEFAULT_FOOTER_LINK_COLOR)))
            .andExpect(jsonPath("$.[*].footerText").value(hasItem(DEFAULT_FOOTER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].templateBackgroundColor").value(hasItem(DEFAULT_TEMPLATE_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML.toString())))
            .andExpect(jsonPath("$.[*].textImageUrl").value(hasItem(DEFAULT_TEXT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].manageSubscriptionButtonColor").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_COLOR)))
            .andExpect(jsonPath("$.[*].manageSubscriptionButtonText").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT)))
            .andExpect(jsonPath("$.[*].manageSubscriptionButtonTextColor").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR)))
            .andExpect(jsonPath("$.[*].shippingAddressText").value(hasItem(DEFAULT_SHIPPING_ADDRESS_TEXT)))
            .andExpect(jsonPath("$.[*].billingAddressText").value(hasItem(DEFAULT_BILLING_ADDRESS_TEXT)))
            .andExpect(jsonPath("$.[*].nextOrderdateText").value(hasItem(DEFAULT_NEXT_ORDERDATE_TEXT)))
            .andExpect(jsonPath("$.[*].paymentMethodText").value(hasItem(DEFAULT_PAYMENT_METHOD_TEXT)))
            .andExpect(jsonPath("$.[*].endingInText").value(hasItem(DEFAULT_ENDING_IN_TEXT)))
            .andExpect(jsonPath("$.[*].bccEmail").value(hasItem(DEFAULT_BCC_EMAIL)))
            .andExpect(jsonPath("$.[*].upcomingOrderEmailBuffer").value(hasItem(DEFAULT_UPCOMING_ORDER_EMAIL_BUFFER)))
            .andExpect(jsonPath("$.[*].headingImageUrl").value(hasItem(DEFAULT_HEADING_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].quantityText").value(hasItem(DEFAULT_QUANTITY_TEXT)))
            .andExpect(jsonPath("$.[*].manageSubscriptionButtonUrl").value(hasItem(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_URL)))
            .andExpect(jsonPath("$.[*].logoHeight").value(hasItem(DEFAULT_LOGO_HEIGHT)))
            .andExpect(jsonPath("$.[*].logoWidth").value(hasItem(DEFAULT_LOGO_WIDTH)))
            .andExpect(jsonPath("$.[*].thanksImageHeight").value(hasItem(DEFAULT_THANKS_IMAGE_HEIGHT)))
            .andExpect(jsonPath("$.[*].thanksImageWidth").value(hasItem(DEFAULT_THANKS_IMAGE_WIDTH)))
            .andExpect(jsonPath("$.[*].logoAlignment").value(hasItem(DEFAULT_LOGO_ALIGNMENT)))
            .andExpect(jsonPath("$.[*].thanksImageAlignment").value(hasItem(DEFAULT_THANKS_IMAGE_ALIGNMENT)))
            .andExpect(jsonPath("$.[*].shippingAddress").value(hasItem(DEFAULT_SHIPPING_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].replyTo").value(hasItem(DEFAULT_REPLY_TO)))
            .andExpect(jsonPath("$.[*].sendBCCEmailFlag").value(hasItem(DEFAULT_SEND_BCC_EMAIL_FLAG.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEmailTemplateSetting() throws Exception {
        // Initialize the database
        emailTemplateSettingRepository.saveAndFlush(emailTemplateSetting);

        // Get the emailTemplateSetting
        restEmailTemplateSettingMockMvc.perform(get("/api/email-template-settings/{id}", emailTemplateSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailTemplateSetting.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.emailSettingType").value(DEFAULT_EMAIL_SETTING_TYPE.toString()))
            .andExpect(jsonPath("$.sendEmailDisabled").value(DEFAULT_SEND_EMAIL_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.fromEmail").value(DEFAULT_FROM_EMAIL))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.heading").value(DEFAULT_HEADING.toString()))
            .andExpect(jsonPath("$.headingTextColor").value(DEFAULT_HEADING_TEXT_COLOR))
            .andExpect(jsonPath("$.contentTextColor").value(DEFAULT_CONTENT_TEXT_COLOR))
            .andExpect(jsonPath("$.contentLinkColor").value(DEFAULT_CONTENT_LINK_COLOR))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.footerTextColor").value(DEFAULT_FOOTER_TEXT_COLOR))
            .andExpect(jsonPath("$.footerLinkColor").value(DEFAULT_FOOTER_LINK_COLOR))
            .andExpect(jsonPath("$.footerText").value(DEFAULT_FOOTER_TEXT.toString()))
            .andExpect(jsonPath("$.templateBackgroundColor").value(DEFAULT_TEMPLATE_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.html").value(DEFAULT_HTML.toString()))
            .andExpect(jsonPath("$.textImageUrl").value(DEFAULT_TEXT_IMAGE_URL))
            .andExpect(jsonPath("$.manageSubscriptionButtonColor").value(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_COLOR))
            .andExpect(jsonPath("$.manageSubscriptionButtonText").value(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT))
            .andExpect(jsonPath("$.manageSubscriptionButtonTextColor").value(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR))
            .andExpect(jsonPath("$.shippingAddressText").value(DEFAULT_SHIPPING_ADDRESS_TEXT))
            .andExpect(jsonPath("$.billingAddressText").value(DEFAULT_BILLING_ADDRESS_TEXT))
            .andExpect(jsonPath("$.nextOrderdateText").value(DEFAULT_NEXT_ORDERDATE_TEXT))
            .andExpect(jsonPath("$.paymentMethodText").value(DEFAULT_PAYMENT_METHOD_TEXT))
            .andExpect(jsonPath("$.endingInText").value(DEFAULT_ENDING_IN_TEXT))
            .andExpect(jsonPath("$.bccEmail").value(DEFAULT_BCC_EMAIL))
            .andExpect(jsonPath("$.upcomingOrderEmailBuffer").value(DEFAULT_UPCOMING_ORDER_EMAIL_BUFFER))
            .andExpect(jsonPath("$.headingImageUrl").value(DEFAULT_HEADING_IMAGE_URL))
            .andExpect(jsonPath("$.quantityText").value(DEFAULT_QUANTITY_TEXT))
            .andExpect(jsonPath("$.manageSubscriptionButtonUrl").value(DEFAULT_MANAGE_SUBSCRIPTION_BUTTON_URL))
            .andExpect(jsonPath("$.logoHeight").value(DEFAULT_LOGO_HEIGHT))
            .andExpect(jsonPath("$.logoWidth").value(DEFAULT_LOGO_WIDTH))
            .andExpect(jsonPath("$.thanksImageHeight").value(DEFAULT_THANKS_IMAGE_HEIGHT))
            .andExpect(jsonPath("$.thanksImageWidth").value(DEFAULT_THANKS_IMAGE_WIDTH))
            .andExpect(jsonPath("$.logoAlignment").value(DEFAULT_LOGO_ALIGNMENT))
            .andExpect(jsonPath("$.thanksImageAlignment").value(DEFAULT_THANKS_IMAGE_ALIGNMENT))
            .andExpect(jsonPath("$.shippingAddress").value(DEFAULT_SHIPPING_ADDRESS.toString()))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS.toString()))
            .andExpect(jsonPath("$.replyTo").value(DEFAULT_REPLY_TO))
            .andExpect(jsonPath("$.sendBCCEmailFlag").value(DEFAULT_SEND_BCC_EMAIL_FLAG.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingEmailTemplateSetting() throws Exception {
        // Get the emailTemplateSetting
        restEmailTemplateSettingMockMvc.perform(get("/api/email-template-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailTemplateSetting() throws Exception {
        // Initialize the database
        emailTemplateSettingService.save(emailTemplateSetting);

        int databaseSizeBeforeUpdate = emailTemplateSettingRepository.findAll().size();

        // Update the emailTemplateSetting
        EmailTemplateSetting updatedEmailTemplateSetting = emailTemplateSettingRepository.findById(emailTemplateSetting.getId()).get();
        // Disconnect from session so that the updates on updatedEmailTemplateSetting are not directly saved in db
        em.detach(updatedEmailTemplateSetting);
        updatedEmailTemplateSetting
            .shop(UPDATED_SHOP)
            .emailSettingType(UPDATED_EMAIL_SETTING_TYPE)
            .sendEmailDisabled(UPDATED_SEND_EMAIL_DISABLED)
            .subject(UPDATED_SUBJECT)
            .fromEmail(UPDATED_FROM_EMAIL)
            .logo(UPDATED_LOGO)
            .heading(UPDATED_HEADING)
            .headingTextColor(UPDATED_HEADING_TEXT_COLOR)
            .contentTextColor(UPDATED_CONTENT_TEXT_COLOR)
            .contentLinkColor(UPDATED_CONTENT_LINK_COLOR)
            .content(UPDATED_CONTENT)
            .footerTextColor(UPDATED_FOOTER_TEXT_COLOR)
            .footerLinkColor(UPDATED_FOOTER_LINK_COLOR)
            .footerText(UPDATED_FOOTER_TEXT)
            .templateBackgroundColor(UPDATED_TEMPLATE_BACKGROUND_COLOR)
            .html(UPDATED_HTML)
            .textImageUrl(UPDATED_TEXT_IMAGE_URL)
            .manageSubscriptionButtonColor(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_COLOR)
            .manageSubscriptionButtonText(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT)
            .manageSubscriptionButtonTextColor(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR)
            .shippingAddressText(UPDATED_SHIPPING_ADDRESS_TEXT)
            .billingAddressText(UPDATED_BILLING_ADDRESS_TEXT)
            .nextOrderdateText(UPDATED_NEXT_ORDERDATE_TEXT)
            .paymentMethodText(UPDATED_PAYMENT_METHOD_TEXT)
            .endingInText(UPDATED_ENDING_IN_TEXT)
            .bccEmail(UPDATED_BCC_EMAIL)
            .upcomingOrderEmailBuffer(UPDATED_UPCOMING_ORDER_EMAIL_BUFFER)
            .headingImageUrl(UPDATED_HEADING_IMAGE_URL)
            .quantityText(UPDATED_QUANTITY_TEXT)
            .manageSubscriptionButtonUrl(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_URL)
            .logoHeight(UPDATED_LOGO_HEIGHT)
            .logoWidth(UPDATED_LOGO_WIDTH)
            .thanksImageHeight(UPDATED_THANKS_IMAGE_HEIGHT)
            .thanksImageWidth(UPDATED_THANKS_IMAGE_WIDTH)
            .logoAlignment(UPDATED_LOGO_ALIGNMENT)
            .thanksImageAlignment(UPDATED_THANKS_IMAGE_ALIGNMENT)
            .shippingAddress(UPDATED_SHIPPING_ADDRESS)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .replyTo(UPDATED_REPLY_TO)
            .sendBCCEmailFlag(UPDATED_SEND_BCC_EMAIL_FLAG);

        restEmailTemplateSettingMockMvc.perform(put("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmailTemplateSetting)))
            .andExpect(status().isOk());

        // Validate the EmailTemplateSetting in the database
        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeUpdate);
        EmailTemplateSetting testEmailTemplateSetting = emailTemplateSettingList.get(emailTemplateSettingList.size() - 1);
        assertThat(testEmailTemplateSetting.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testEmailTemplateSetting.getEmailSettingType()).isEqualTo(UPDATED_EMAIL_SETTING_TYPE);
        assertThat(testEmailTemplateSetting.isSendEmailDisabled()).isEqualTo(UPDATED_SEND_EMAIL_DISABLED);
        assertThat(testEmailTemplateSetting.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testEmailTemplateSetting.getFromEmail()).isEqualTo(UPDATED_FROM_EMAIL);
        assertThat(testEmailTemplateSetting.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testEmailTemplateSetting.getHeading()).isEqualTo(UPDATED_HEADING);
        assertThat(testEmailTemplateSetting.getHeadingTextColor()).isEqualTo(UPDATED_HEADING_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getContentTextColor()).isEqualTo(UPDATED_CONTENT_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getContentLinkColor()).isEqualTo(UPDATED_CONTENT_LINK_COLOR);
        assertThat(testEmailTemplateSetting.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEmailTemplateSetting.getFooterTextColor()).isEqualTo(UPDATED_FOOTER_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getFooterLinkColor()).isEqualTo(UPDATED_FOOTER_LINK_COLOR);
        assertThat(testEmailTemplateSetting.getFooterText()).isEqualTo(UPDATED_FOOTER_TEXT);
        assertThat(testEmailTemplateSetting.getTemplateBackgroundColor()).isEqualTo(UPDATED_TEMPLATE_BACKGROUND_COLOR);
        assertThat(testEmailTemplateSetting.getHtml()).isEqualTo(UPDATED_HTML);
        assertThat(testEmailTemplateSetting.getTextImageUrl()).isEqualTo(UPDATED_TEXT_IMAGE_URL);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonColor()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_COLOR);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonText()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonTextColor()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR);
        assertThat(testEmailTemplateSetting.getShippingAddressText()).isEqualTo(UPDATED_SHIPPING_ADDRESS_TEXT);
        assertThat(testEmailTemplateSetting.getBillingAddressText()).isEqualTo(UPDATED_BILLING_ADDRESS_TEXT);
        assertThat(testEmailTemplateSetting.getNextOrderdateText()).isEqualTo(UPDATED_NEXT_ORDERDATE_TEXT);
        assertThat(testEmailTemplateSetting.getPaymentMethodText()).isEqualTo(UPDATED_PAYMENT_METHOD_TEXT);
        assertThat(testEmailTemplateSetting.getEndingInText()).isEqualTo(UPDATED_ENDING_IN_TEXT);
        assertThat(testEmailTemplateSetting.getBccEmail()).isEqualTo(UPDATED_BCC_EMAIL);
        assertThat(testEmailTemplateSetting.getUpcomingOrderEmailBuffer()).isEqualTo(UPDATED_UPCOMING_ORDER_EMAIL_BUFFER);
        assertThat(testEmailTemplateSetting.getHeadingImageUrl()).isEqualTo(UPDATED_HEADING_IMAGE_URL);
        assertThat(testEmailTemplateSetting.getQuantityText()).isEqualTo(UPDATED_QUANTITY_TEXT);
        assertThat(testEmailTemplateSetting.getManageSubscriptionButtonUrl()).isEqualTo(UPDATED_MANAGE_SUBSCRIPTION_BUTTON_URL);
        assertThat(testEmailTemplateSetting.getLogoHeight()).isEqualTo(UPDATED_LOGO_HEIGHT);
        assertThat(testEmailTemplateSetting.getLogoWidth()).isEqualTo(UPDATED_LOGO_WIDTH);
        assertThat(testEmailTemplateSetting.getThanksImageHeight()).isEqualTo(UPDATED_THANKS_IMAGE_HEIGHT);
        assertThat(testEmailTemplateSetting.getThanksImageWidth()).isEqualTo(UPDATED_THANKS_IMAGE_WIDTH);
        assertThat(testEmailTemplateSetting.getLogoAlignment()).isEqualTo(UPDATED_LOGO_ALIGNMENT);
        assertThat(testEmailTemplateSetting.getThanksImageAlignment()).isEqualTo(UPDATED_THANKS_IMAGE_ALIGNMENT);
        assertThat(testEmailTemplateSetting.getShippingAddress()).isEqualTo(UPDATED_SHIPPING_ADDRESS);
        assertThat(testEmailTemplateSetting.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testEmailTemplateSetting.getReplyTo()).isEqualTo(UPDATED_REPLY_TO);
        assertThat(testEmailTemplateSetting.isSendBCCEmailFlag()).isEqualTo(UPDATED_SEND_BCC_EMAIL_FLAG);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailTemplateSetting() throws Exception {
        int databaseSizeBeforeUpdate = emailTemplateSettingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailTemplateSettingMockMvc.perform(put("/api/email-template-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emailTemplateSetting)))
            .andExpect(status().isBadRequest());

        // Validate the EmailTemplateSetting in the database
        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmailTemplateSetting() throws Exception {
        // Initialize the database
        emailTemplateSettingService.save(emailTemplateSetting);

        int databaseSizeBeforeDelete = emailTemplateSettingRepository.findAll().size();

        // Delete the emailTemplateSetting
        restEmailTemplateSettingMockMvc.perform(delete("/api/email-template-settings/{id}", emailTemplateSetting.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailTemplateSetting> emailTemplateSettingList = emailTemplateSettingRepository.findAll();
        assertThat(emailTemplateSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
