package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ActivityLog;
import com.et.repository.ActivityLogRepository;
import com.et.repository.UserRepository;
import com.et.service.ActivityLogService;
import com.et.service.BulkAutomationService;
import com.et.service.dto.ActivityLogDTO;
import com.et.service.mapper.ActivityLogMapper;
import com.et.utils.AwsUtils;
import com.et.web.rest.errors.ExceptionTranslator;
import com.et.service.dto.ActivityLogCriteria;
import com.et.service.ActivityLogQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.et.web.rest.TestUtil.sameInstant;
import static com.et.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.et.domain.enumeration.ActivityLogEntityType;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.ActivityLogEventType;
import com.et.domain.enumeration.ActivityLogStatus;
/**
 * Integration tests for the {@link ActivityLogResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class ActivityLogResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;
    private static final Long SMALLER_ENTITY_ID = 1L - 1L;

    private static final ActivityLogEntityType DEFAULT_ENTITY_TYPE = ActivityLogEntityType.SUBSCRIPTION_BILLING_ATTEMPT;
    private static final ActivityLogEntityType UPDATED_ENTITY_TYPE = ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS;

    private static final ActivityLogEventSource DEFAULT_EVENT_SOURCE = ActivityLogEventSource.CUSTOMER_PORTAL;
    private static final ActivityLogEventSource UPDATED_EVENT_SOURCE = ActivityLogEventSource.MERCHANT_PORTAL;

    private static final ActivityLogEventType DEFAULT_EVENT_TYPE = ActivityLogEventType.NEXT_BILLING_DATE_CHANGE;
    private static final ActivityLogEventType UPDATED_EVENT_TYPE = ActivityLogEventType.BILLING_INTERVAL_CHANGE;

    private static final ActivityLogStatus DEFAULT_STATUS = ActivityLogStatus.SUCCESS;
    private static final ActivityLogStatus UPDATED_STATUS = ActivityLogStatus.FAILURE;

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ActivityLogMapper activityLogMapper;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private ActivityLogQueryService activityLogQueryService;

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

    @Autowired
    private AwsUtils awsUtils;

    @Autowired
    private BulkAutomationService bulkAutomationService;

    @Autowired
    private UserRepository userRepository;

    private MockMvc restActivityLogMockMvc;

    private ActivityLog activityLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityLogResource activityLogResource = new ActivityLogResource(activityLogService, activityLogQueryService, awsUtils, bulkAutomationService, userRepository);
        this.restActivityLogMockMvc = MockMvcBuilders.standaloneSetup(activityLogResource)
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
    public static ActivityLog createEntity(EntityManager em) {
        ActivityLog activityLog = new ActivityLog()
            .shop(DEFAULT_SHOP)
            .entityId(DEFAULT_ENTITY_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .eventSource(DEFAULT_EVENT_SOURCE)
            .eventType(DEFAULT_EVENT_TYPE)
            .status(DEFAULT_STATUS)
            .createAt(DEFAULT_CREATE_AT)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO);
        return activityLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityLog createUpdatedEntity(EntityManager em) {
        ActivityLog activityLog = new ActivityLog()
            .shop(UPDATED_SHOP)
            .entityId(UPDATED_ENTITY_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .eventSource(UPDATED_EVENT_SOURCE)
            .eventType(UPDATED_EVENT_TYPE)
            .status(UPDATED_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        return activityLog;
    }

    @BeforeEach
    public void initTest() {
        activityLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityLog() throws Exception {
        int databaseSizeBeforeCreate = activityLogRepository.findAll().size();

        // Create the ActivityLog
        ActivityLogDTO activityLogDTO = activityLogMapper.toDto(activityLog);
        restActivityLogMockMvc.perform(post("/api/activity-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityLogDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityLog in the database
        List<ActivityLog> activityLogList = activityLogRepository.findAll();
        assertThat(activityLogList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityLog testActivityLog = activityLogList.get(activityLogList.size() - 1);
        assertThat(testActivityLog.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testActivityLog.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testActivityLog.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testActivityLog.getEventSource()).isEqualTo(DEFAULT_EVENT_SOURCE);
        assertThat(testActivityLog.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testActivityLog.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testActivityLog.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testActivityLog.getAdditionalInfo()).isEqualTo(DEFAULT_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    public void createActivityLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityLogRepository.findAll().size();

        // Create the ActivityLog with an existing ID
        activityLog.setId(1L);
        ActivityLogDTO activityLogDTO = activityLogMapper.toDto(activityLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityLogMockMvc.perform(post("/api/activity-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        List<ActivityLog> activityLogList = activityLogRepository.findAll();
        assertThat(activityLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActivityLogs() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList
        restActivityLogMockMvc.perform(get("/api/activity-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventSource").value(hasItem(DEFAULT_EVENT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO.toString())));
    }

    @Test
    @Transactional
    public void getActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get the activityLog
        restActivityLogMockMvc.perform(get("/api/activity-logs/{id}", activityLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityLog.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.eventSource").value(DEFAULT_EVENT_SOURCE.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createAt").value(sameInstant(DEFAULT_CREATE_AT)))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO.toString()));
    }


    @Test
    @Transactional
    public void getActivityLogsByIdFiltering() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        Long id = activityLog.getId();

        defaultActivityLogShouldBeFound("id.equals=" + id);
        defaultActivityLogShouldNotBeFound("id.notEquals=" + id);

        defaultActivityLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActivityLogShouldNotBeFound("id.greaterThan=" + id);

        defaultActivityLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActivityLogShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllActivityLogsByShopIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop equals to DEFAULT_SHOP
        defaultActivityLogShouldBeFound("shop.equals=" + DEFAULT_SHOP);

        // Get all the activityLogList where shop equals to UPDATED_SHOP
        defaultActivityLogShouldNotBeFound("shop.equals=" + UPDATED_SHOP);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByShopIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop not equals to DEFAULT_SHOP
        defaultActivityLogShouldNotBeFound("shop.notEquals=" + DEFAULT_SHOP);

        // Get all the activityLogList where shop not equals to UPDATED_SHOP
        defaultActivityLogShouldBeFound("shop.notEquals=" + UPDATED_SHOP);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByShopIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop in DEFAULT_SHOP or UPDATED_SHOP
        defaultActivityLogShouldBeFound("shop.in=" + DEFAULT_SHOP + "," + UPDATED_SHOP);

        // Get all the activityLogList where shop equals to UPDATED_SHOP
        defaultActivityLogShouldNotBeFound("shop.in=" + UPDATED_SHOP);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByShopIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop is not null
        defaultActivityLogShouldBeFound("shop.specified=true");

        // Get all the activityLogList where shop is null
        defaultActivityLogShouldNotBeFound("shop.specified=false");
    }
                @Test
    @Transactional
    public void getAllActivityLogsByShopContainsSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop contains DEFAULT_SHOP
        defaultActivityLogShouldBeFound("shop.contains=" + DEFAULT_SHOP);

        // Get all the activityLogList where shop contains UPDATED_SHOP
        defaultActivityLogShouldNotBeFound("shop.contains=" + UPDATED_SHOP);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByShopNotContainsSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where shop does not contain DEFAULT_SHOP
        defaultActivityLogShouldNotBeFound("shop.doesNotContain=" + DEFAULT_SHOP);

        // Get all the activityLogList where shop does not contain UPDATED_SHOP
        defaultActivityLogShouldBeFound("shop.doesNotContain=" + UPDATED_SHOP);
    }


    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId equals to DEFAULT_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.equals=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId equals to UPDATED_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId not equals to DEFAULT_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.notEquals=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId not equals to UPDATED_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.notEquals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId in DEFAULT_ENTITY_ID or UPDATED_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID);

        // Get all the activityLogList where entityId equals to UPDATED_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId is not null
        defaultActivityLogShouldBeFound("entityId.specified=true");

        // Get all the activityLogList where entityId is null
        defaultActivityLogShouldNotBeFound("entityId.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId is greater than or equal to DEFAULT_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.greaterThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId is greater than or equal to UPDATED_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.greaterThanOrEqual=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId is less than or equal to DEFAULT_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.lessThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId is less than or equal to SMALLER_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.lessThanOrEqual=" + SMALLER_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId is less than DEFAULT_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.lessThan=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId is less than UPDATED_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.lessThan=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityId is greater than DEFAULT_ENTITY_ID
        defaultActivityLogShouldNotBeFound("entityId.greaterThan=" + DEFAULT_ENTITY_ID);

        // Get all the activityLogList where entityId is greater than SMALLER_ENTITY_ID
        defaultActivityLogShouldBeFound("entityId.greaterThan=" + SMALLER_ENTITY_ID);
    }


    @Test
    @Transactional
    public void getAllActivityLogsByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityType equals to DEFAULT_ENTITY_TYPE
        defaultActivityLogShouldBeFound("entityType.equals=" + DEFAULT_ENTITY_TYPE);

        // Get all the activityLogList where entityType equals to UPDATED_ENTITY_TYPE
        defaultActivityLogShouldNotBeFound("entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityType not equals to DEFAULT_ENTITY_TYPE
        defaultActivityLogShouldNotBeFound("entityType.notEquals=" + DEFAULT_ENTITY_TYPE);

        // Get all the activityLogList where entityType not equals to UPDATED_ENTITY_TYPE
        defaultActivityLogShouldBeFound("entityType.notEquals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityType in DEFAULT_ENTITY_TYPE or UPDATED_ENTITY_TYPE
        defaultActivityLogShouldBeFound("entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE);

        // Get all the activityLogList where entityType equals to UPDATED_ENTITY_TYPE
        defaultActivityLogShouldNotBeFound("entityType.in=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where entityType is not null
        defaultActivityLogShouldBeFound("entityType.specified=true");

        // Get all the activityLogList where entityType is null
        defaultActivityLogShouldNotBeFound("entityType.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventSource equals to DEFAULT_EVENT_SOURCE
        defaultActivityLogShouldBeFound("eventSource.equals=" + DEFAULT_EVENT_SOURCE);

        // Get all the activityLogList where eventSource equals to UPDATED_EVENT_SOURCE
        defaultActivityLogShouldNotBeFound("eventSource.equals=" + UPDATED_EVENT_SOURCE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventSourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventSource not equals to DEFAULT_EVENT_SOURCE
        defaultActivityLogShouldNotBeFound("eventSource.notEquals=" + DEFAULT_EVENT_SOURCE);

        // Get all the activityLogList where eventSource not equals to UPDATED_EVENT_SOURCE
        defaultActivityLogShouldBeFound("eventSource.notEquals=" + UPDATED_EVENT_SOURCE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventSourceIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventSource in DEFAULT_EVENT_SOURCE or UPDATED_EVENT_SOURCE
        defaultActivityLogShouldBeFound("eventSource.in=" + DEFAULT_EVENT_SOURCE + "," + UPDATED_EVENT_SOURCE);

        // Get all the activityLogList where eventSource equals to UPDATED_EVENT_SOURCE
        defaultActivityLogShouldNotBeFound("eventSource.in=" + UPDATED_EVENT_SOURCE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventSource is not null
        defaultActivityLogShouldBeFound("eventSource.specified=true");

        // Get all the activityLogList where eventSource is null
        defaultActivityLogShouldNotBeFound("eventSource.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventType equals to DEFAULT_EVENT_TYPE
        defaultActivityLogShouldBeFound("eventType.equals=" + DEFAULT_EVENT_TYPE);

        // Get all the activityLogList where eventType equals to UPDATED_EVENT_TYPE
        defaultActivityLogShouldNotBeFound("eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventType not equals to DEFAULT_EVENT_TYPE
        defaultActivityLogShouldNotBeFound("eventType.notEquals=" + DEFAULT_EVENT_TYPE);

        // Get all the activityLogList where eventType not equals to UPDATED_EVENT_TYPE
        defaultActivityLogShouldBeFound("eventType.notEquals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventType in DEFAULT_EVENT_TYPE or UPDATED_EVENT_TYPE
        defaultActivityLogShouldBeFound("eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE);

        // Get all the activityLogList where eventType equals to UPDATED_EVENT_TYPE
        defaultActivityLogShouldNotBeFound("eventType.in=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where eventType is not null
        defaultActivityLogShouldBeFound("eventType.specified=true");

        // Get all the activityLogList where eventType is null
        defaultActivityLogShouldNotBeFound("eventType.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where status equals to DEFAULT_STATUS
        defaultActivityLogShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the activityLogList where status equals to UPDATED_STATUS
        defaultActivityLogShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where status not equals to DEFAULT_STATUS
        defaultActivityLogShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the activityLogList where status not equals to UPDATED_STATUS
        defaultActivityLogShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultActivityLogShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the activityLogList where status equals to UPDATED_STATUS
        defaultActivityLogShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where status is not null
        defaultActivityLogShouldBeFound("status.specified=true");

        // Get all the activityLogList where status is null
        defaultActivityLogShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt equals to DEFAULT_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt equals to UPDATED_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt not equals to DEFAULT_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.notEquals=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt not equals to UPDATED_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.notEquals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the activityLogList where createAt equals to UPDATED_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt is not null
        defaultActivityLogShouldBeFound("createAt.specified=true");

        // Get all the activityLogList where createAt is null
        defaultActivityLogShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt is less than DEFAULT_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt is less than UPDATED_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllActivityLogsByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        // Get all the activityLogList where createAt is greater than DEFAULT_CREATE_AT
        defaultActivityLogShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the activityLogList where createAt is greater than SMALLER_CREATE_AT
        defaultActivityLogShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActivityLogShouldBeFound(String filter) throws Exception {
        restActivityLogMockMvc.perform(get("/api/activity-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eventSource").value(hasItem(DEFAULT_EVENT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO.toString())));

        // Check, that the count call also returns 1
        restActivityLogMockMvc.perform(get("/api/activity-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActivityLogShouldNotBeFound(String filter) throws Exception {
        restActivityLogMockMvc.perform(get("/api/activity-logs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActivityLogMockMvc.perform(get("/api/activity-logs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingActivityLog() throws Exception {
        // Get the activityLog
        restActivityLogMockMvc.perform(get("/api/activity-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        int databaseSizeBeforeUpdate = activityLogRepository.findAll().size();

        // Update the activityLog
        ActivityLog updatedActivityLog = activityLogRepository.findById(activityLog.getId()).get();
        // Disconnect from session so that the updates on updatedActivityLog are not directly saved in db
        em.detach(updatedActivityLog);
        updatedActivityLog
            .shop(UPDATED_SHOP)
            .entityId(UPDATED_ENTITY_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .eventSource(UPDATED_EVENT_SOURCE)
            .eventType(UPDATED_EVENT_TYPE)
            .status(UPDATED_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        ActivityLogDTO activityLogDTO = activityLogMapper.toDto(updatedActivityLog);

        restActivityLogMockMvc.perform(put("/api/activity-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityLogDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityLog in the database
        List<ActivityLog> activityLogList = activityLogRepository.findAll();
        assertThat(activityLogList).hasSize(databaseSizeBeforeUpdate);
        ActivityLog testActivityLog = activityLogList.get(activityLogList.size() - 1);
        assertThat(testActivityLog.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testActivityLog.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testActivityLog.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testActivityLog.getEventSource()).isEqualTo(UPDATED_EVENT_SOURCE);
        assertThat(testActivityLog.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testActivityLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testActivityLog.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testActivityLog.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityLog() throws Exception {
        int databaseSizeBeforeUpdate = activityLogRepository.findAll().size();

        // Create the ActivityLog
        ActivityLogDTO activityLogDTO = activityLogMapper.toDto(activityLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityLogMockMvc.perform(put("/api/activity-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityLog in the database
        List<ActivityLog> activityLogList = activityLogRepository.findAll();
        assertThat(activityLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityLog() throws Exception {
        // Initialize the database
        activityLogRepository.saveAndFlush(activityLog);

        int databaseSizeBeforeDelete = activityLogRepository.findAll().size();

        // Delete the activityLog
        restActivityLogMockMvc.perform(delete("/api/activity-logs/{id}", activityLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivityLog> activityLogList = activityLogRepository.findAll();
        assertThat(activityLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
