package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.ActivityUpdatesSettings;
import com.et.domain.enumeration.SummaryReportFrequencyUnit;
import com.et.domain.enumeration.SummaryReportTimePeriodUnit;
import com.et.repository.ActivityUpdatesSettingsRepository;
import com.et.service.ActivityUpdatesSettingsService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.et.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ActivityUpdatesSettingsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ActivityUpdatesSettingsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUMMARY_REPORT_ENABLED = false;
    private static final Boolean UPDATED_SUMMARY_REPORT_ENABLED = true;

    private static final String DEFAULT_SUMMARY_REPORT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY_REPORT_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY_REPORT_DELIVER_TO_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY_REPORT_DELIVER_TO_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY_REPORT_TIME_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY_REPORT_TIME_PERIOD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SUMMARY_REPORT_LAST_SENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUMMARY_REPORT_LAST_SENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_SUMMARY_REPORT_PROCESSING = false;
    private static final Boolean UPDATED_SUMMARY_REPORT_PROCESSING = true;

    @Autowired
    private ActivityUpdatesSettingsRepository activityUpdatesSettingsRepository;

    @Autowired
    private ActivityUpdatesSettingsService activityUpdatesSettingsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityUpdatesSettingsMockMvc;

    private ActivityUpdatesSettings activityUpdatesSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityUpdatesSettings createEntity(EntityManager em) {
        ActivityUpdatesSettings activityUpdatesSettings = new ActivityUpdatesSettings()
            .shop(DEFAULT_SHOP)
            .summaryReportEnabled(DEFAULT_SUMMARY_REPORT_ENABLED)
            .summaryReportFrequency(SummaryReportFrequencyUnit.DAILY)
            .summaryReportDeliverToEmail(DEFAULT_SUMMARY_REPORT_DELIVER_TO_EMAIL)
            .summaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_7_DAYS)
            .summaryReportLastSent(DEFAULT_SUMMARY_REPORT_LAST_SENT)
            .summaryReportProcessing(DEFAULT_SUMMARY_REPORT_PROCESSING);
        return activityUpdatesSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityUpdatesSettings createUpdatedEntity(EntityManager em) {
        ActivityUpdatesSettings activityUpdatesSettings = new ActivityUpdatesSettings()
            .shop(UPDATED_SHOP)
            .summaryReportEnabled(UPDATED_SUMMARY_REPORT_ENABLED)
            .summaryReportFrequency(SummaryReportFrequencyUnit.DAILY)
            .summaryReportDeliverToEmail(UPDATED_SUMMARY_REPORT_DELIVER_TO_EMAIL)
            .summaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_7_DAYS)
            .summaryReportLastSent(UPDATED_SUMMARY_REPORT_LAST_SENT)
            .summaryReportProcessing(UPDATED_SUMMARY_REPORT_PROCESSING);
        return activityUpdatesSettings;
    }

    @BeforeEach
    public void initTest() {
        activityUpdatesSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityUpdatesSettings() throws Exception {
        int databaseSizeBeforeCreate = activityUpdatesSettingsRepository.findAll().size();
        // Create the ActivityUpdatesSettings
        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isCreated());

        // Validate the ActivityUpdatesSettings in the database
        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityUpdatesSettings testActivityUpdatesSettings = activityUpdatesSettingsList.get(activityUpdatesSettingsList.size() - 1);
        assertThat(testActivityUpdatesSettings.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testActivityUpdatesSettings.isSummaryReportEnabled()).isEqualTo(DEFAULT_SUMMARY_REPORT_ENABLED);
        assertThat(testActivityUpdatesSettings.getSummaryReportFrequency()).isEqualTo(DEFAULT_SUMMARY_REPORT_FREQUENCY);
        assertThat(testActivityUpdatesSettings.getSummaryReportDeliverToEmail()).isEqualTo(DEFAULT_SUMMARY_REPORT_DELIVER_TO_EMAIL);
        assertThat(testActivityUpdatesSettings.getSummaryReportTimePeriod()).isEqualTo(DEFAULT_SUMMARY_REPORT_TIME_PERIOD);
        assertThat(testActivityUpdatesSettings.getSummaryReportLastSent()).isEqualTo(DEFAULT_SUMMARY_REPORT_LAST_SENT);
        assertThat(testActivityUpdatesSettings.isSummaryReportProcessing()).isEqualTo(DEFAULT_SUMMARY_REPORT_PROCESSING);
    }

    @Test
    @Transactional
    public void createActivityUpdatesSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityUpdatesSettingsRepository.findAll().size();

        // Create the ActivityUpdatesSettings with an existing ID
        activityUpdatesSettings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityUpdatesSettings in the database
        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityUpdatesSettingsRepository.findAll().size();
        // set the field null
        activityUpdatesSettings.setShop(null);

        // Create the ActivityUpdatesSettings, which fails.


        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSummaryReportEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityUpdatesSettingsRepository.findAll().size();
        // set the field null
        activityUpdatesSettings.setSummaryReportEnabled(null);

        // Create the ActivityUpdatesSettings, which fails.


        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSummaryReportFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityUpdatesSettingsRepository.findAll().size();
        // set the field null
        activityUpdatesSettings.setSummaryReportFrequency(null);

        // Create the ActivityUpdatesSettings, which fails.


        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSummaryReportTimePeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityUpdatesSettingsRepository.findAll().size();
        // set the field null
        activityUpdatesSettings.setSummaryReportTimePeriod(null);

        // Create the ActivityUpdatesSettings, which fails.


        restActivityUpdatesSettingsMockMvc.perform(post("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivityUpdatesSettings() throws Exception {
        // Initialize the database
        activityUpdatesSettingsRepository.saveAndFlush(activityUpdatesSettings);

        // Get all the activityUpdatesSettingsList
        restActivityUpdatesSettingsMockMvc.perform(get("/api/activity-updates-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityUpdatesSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].summaryReportEnabled").value(hasItem(DEFAULT_SUMMARY_REPORT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].summaryReportFrequency").value(hasItem(DEFAULT_SUMMARY_REPORT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].summaryReportDeliverToEmail").value(hasItem(DEFAULT_SUMMARY_REPORT_DELIVER_TO_EMAIL)))
            .andExpect(jsonPath("$.[*].summaryReportTimePeriod").value(hasItem(DEFAULT_SUMMARY_REPORT_TIME_PERIOD)))
            .andExpect(jsonPath("$.[*].summaryReportLastSent").value(hasItem(sameInstant(DEFAULT_SUMMARY_REPORT_LAST_SENT))))
            .andExpect(jsonPath("$.[*].summaryReportProcessing").value(hasItem(DEFAULT_SUMMARY_REPORT_PROCESSING.booleanValue())));
    }

    @Test
    @Transactional
    public void getActivityUpdatesSettings() throws Exception {
        // Initialize the database
        activityUpdatesSettingsRepository.saveAndFlush(activityUpdatesSettings);

        // Get the activityUpdatesSettings
        restActivityUpdatesSettingsMockMvc.perform(get("/api/activity-updates-settings/{id}", activityUpdatesSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activityUpdatesSettings.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.summaryReportEnabled").value(DEFAULT_SUMMARY_REPORT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.summaryReportFrequency").value(DEFAULT_SUMMARY_REPORT_FREQUENCY))
            .andExpect(jsonPath("$.summaryReportDeliverToEmail").value(DEFAULT_SUMMARY_REPORT_DELIVER_TO_EMAIL))
            .andExpect(jsonPath("$.summaryReportTimePeriod").value(DEFAULT_SUMMARY_REPORT_TIME_PERIOD))
            .andExpect(jsonPath("$.summaryReportLastSent").value(sameInstant(DEFAULT_SUMMARY_REPORT_LAST_SENT)))
            .andExpect(jsonPath("$.summaryReportProcessing").value(DEFAULT_SUMMARY_REPORT_PROCESSING.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingActivityUpdatesSettings() throws Exception {
        // Get the activityUpdatesSettings
        restActivityUpdatesSettingsMockMvc.perform(get("/api/activity-updates-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityUpdatesSettings() throws Exception {
        // Initialize the database
        activityUpdatesSettingsService.save(activityUpdatesSettings);

        int databaseSizeBeforeUpdate = activityUpdatesSettingsRepository.findAll().size();

        // Update the activityUpdatesSettings
        ActivityUpdatesSettings updatedActivityUpdatesSettings = activityUpdatesSettingsRepository.findById(activityUpdatesSettings.getId()).get();
        // Disconnect from session so that the updates on updatedActivityUpdatesSettings are not directly saved in db
        em.detach(updatedActivityUpdatesSettings);
        updatedActivityUpdatesSettings
            .shop(UPDATED_SHOP)
            .summaryReportEnabled(UPDATED_SUMMARY_REPORT_ENABLED)
            .summaryReportFrequency(SummaryReportFrequencyUnit.DAILY)
            .summaryReportDeliverToEmail(UPDATED_SUMMARY_REPORT_DELIVER_TO_EMAIL)
            .summaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_7_DAYS)
            .summaryReportLastSent(UPDATED_SUMMARY_REPORT_LAST_SENT)
            .summaryReportProcessing(UPDATED_SUMMARY_REPORT_PROCESSING);

        restActivityUpdatesSettingsMockMvc.perform(put("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedActivityUpdatesSettings)))
            .andExpect(status().isOk());

        // Validate the ActivityUpdatesSettings in the database
        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeUpdate);
        ActivityUpdatesSettings testActivityUpdatesSettings = activityUpdatesSettingsList.get(activityUpdatesSettingsList.size() - 1);
        assertThat(testActivityUpdatesSettings.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testActivityUpdatesSettings.isSummaryReportEnabled()).isEqualTo(UPDATED_SUMMARY_REPORT_ENABLED);
        assertThat(testActivityUpdatesSettings.getSummaryReportFrequency()).isEqualTo(UPDATED_SUMMARY_REPORT_FREQUENCY);
        assertThat(testActivityUpdatesSettings.getSummaryReportDeliverToEmail()).isEqualTo(UPDATED_SUMMARY_REPORT_DELIVER_TO_EMAIL);
        assertThat(testActivityUpdatesSettings.getSummaryReportTimePeriod()).isEqualTo(UPDATED_SUMMARY_REPORT_TIME_PERIOD);
        assertThat(testActivityUpdatesSettings.getSummaryReportLastSent()).isEqualTo(UPDATED_SUMMARY_REPORT_LAST_SENT);
        assertThat(testActivityUpdatesSettings.isSummaryReportProcessing()).isEqualTo(UPDATED_SUMMARY_REPORT_PROCESSING);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityUpdatesSettings() throws Exception {
        int databaseSizeBeforeUpdate = activityUpdatesSettingsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityUpdatesSettingsMockMvc.perform(put("/api/activity-updates-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityUpdatesSettings)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityUpdatesSettings in the database
        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityUpdatesSettings() throws Exception {
        // Initialize the database
        activityUpdatesSettingsService.save(activityUpdatesSettings);

        int databaseSizeBeforeDelete = activityUpdatesSettingsRepository.findAll().size();

        // Delete the activityUpdatesSettings
        restActivityUpdatesSettingsMockMvc.perform(delete("/api/activity-updates-settings/{id}", activityUpdatesSettings.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivityUpdatesSettings> activityUpdatesSettingsList = activityUpdatesSettingsRepository.findAll();
        assertThat(activityUpdatesSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
