package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.Analytics;
import com.et.repository.AnalyticsRepository;
import com.et.service.AnalyticsService;
import com.et.service.dto.AnalyticsDTO;
import com.et.service.mapper.AnalyticsMapper;

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
 * Integration tests for the {@link AnalyticsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnalyticsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_SUBSCRIPTIONS = 1L;
    private static final Long UPDATED_TOTAL_SUBSCRIPTIONS = 2L;

    private static final Long DEFAULT_TOTAL_ORDERS = 1L;
    private static final Long UPDATED_TOTAL_ORDERS = 2L;

    private static final Double DEFAULT_TOTAL_ORDER_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_ORDER_AMOUNT = 2D;

    private static final Long DEFAULT_FIRST_TIME_ORDERS = 1L;
    private static final Long UPDATED_FIRST_TIME_ORDERS = 2L;

    private static final Long DEFAULT_RECURRING_ORDERS = 1L;
    private static final Long UPDATED_RECURRING_ORDERS = 2L;

    private static final Long DEFAULT_TOTAL_CUSTOMERS = 1L;
    private static final Long UPDATED_TOTAL_CUSTOMERS = 2L;

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private AnalyticsMapper analyticsMapper;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalyticsMockMvc;

    private Analytics analytics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analytics createEntity(EntityManager em) {
        Analytics analytics = new Analytics()
            .shop(DEFAULT_SHOP)
            .totalSubscriptions(DEFAULT_TOTAL_SUBSCRIPTIONS)
            .totalOrders(DEFAULT_TOTAL_ORDERS)
            .totalOrderAmount(DEFAULT_TOTAL_ORDER_AMOUNT)
            .firstTimeOrders(DEFAULT_FIRST_TIME_ORDERS)
            .recurringOrders(DEFAULT_RECURRING_ORDERS)
            .totalCustomers(DEFAULT_TOTAL_CUSTOMERS);
        return analytics;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analytics createUpdatedEntity(EntityManager em) {
        Analytics analytics = new Analytics()
            .shop(UPDATED_SHOP)
            .totalSubscriptions(UPDATED_TOTAL_SUBSCRIPTIONS)
            .totalOrders(UPDATED_TOTAL_ORDERS)
            .totalOrderAmount(UPDATED_TOTAL_ORDER_AMOUNT)
            .firstTimeOrders(UPDATED_FIRST_TIME_ORDERS)
            .recurringOrders(UPDATED_RECURRING_ORDERS)
            .totalCustomers(UPDATED_TOTAL_CUSTOMERS);
        return analytics;
    }

    @BeforeEach
    public void initTest() {
        analytics = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalytics() throws Exception {
        int databaseSizeBeforeCreate = analyticsRepository.findAll().size();
        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);
        restAnalyticsMockMvc.perform(post("/api/analytics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analyticsDTO)))
            .andExpect(status().isCreated());

        // Validate the Analytics in the database
        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeCreate + 1);
        Analytics testAnalytics = analyticsList.get(analyticsList.size() - 1);
        assertThat(testAnalytics.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testAnalytics.getTotalSubscriptions()).isEqualTo(DEFAULT_TOTAL_SUBSCRIPTIONS);
        assertThat(testAnalytics.getTotalOrders()).isEqualTo(DEFAULT_TOTAL_ORDERS);
        assertThat(testAnalytics.getTotalOrderAmount()).isEqualTo(DEFAULT_TOTAL_ORDER_AMOUNT);
        assertThat(testAnalytics.getFirstTimeOrders()).isEqualTo(DEFAULT_FIRST_TIME_ORDERS);
        assertThat(testAnalytics.getRecurringOrders()).isEqualTo(DEFAULT_RECURRING_ORDERS);
        assertThat(testAnalytics.getTotalCustomers()).isEqualTo(DEFAULT_TOTAL_CUSTOMERS);
    }

    @Test
    @Transactional
    public void createAnalyticsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analyticsRepository.findAll().size();

        // Create the Analytics with an existing ID
        analytics.setId(1L);
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyticsMockMvc.perform(post("/api/analytics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analyticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Analytics in the database
        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = analyticsRepository.findAll().size();
        // set the field null
        analytics.setShop(null);

        // Create the Analytics, which fails.
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);


        restAnalyticsMockMvc.perform(post("/api/analytics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analyticsDTO)))
            .andExpect(status().isBadRequest());

        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnalytics() throws Exception {
        // Initialize the database
        analyticsRepository.saveAndFlush(analytics);

        // Get all the analyticsList
        restAnalyticsMockMvc.perform(get("/api/analytics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analytics.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].totalSubscriptions").value(hasItem(DEFAULT_TOTAL_SUBSCRIPTIONS.intValue())))
            .andExpect(jsonPath("$.[*].totalOrders").value(hasItem(DEFAULT_TOTAL_ORDERS.intValue())))
            .andExpect(jsonPath("$.[*].totalOrderAmount").value(hasItem(DEFAULT_TOTAL_ORDER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].firstTimeOrders").value(hasItem(DEFAULT_FIRST_TIME_ORDERS.intValue())))
            .andExpect(jsonPath("$.[*].recurringOrders").value(hasItem(DEFAULT_RECURRING_ORDERS.intValue())))
            .andExpect(jsonPath("$.[*].totalCustomers").value(hasItem(DEFAULT_TOTAL_CUSTOMERS.intValue())));
    }
    
    @Test
    @Transactional
    public void getAnalytics() throws Exception {
        // Initialize the database
        analyticsRepository.saveAndFlush(analytics);

        // Get the analytics
        restAnalyticsMockMvc.perform(get("/api/analytics/{id}", analytics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analytics.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.totalSubscriptions").value(DEFAULT_TOTAL_SUBSCRIPTIONS.intValue()))
            .andExpect(jsonPath("$.totalOrders").value(DEFAULT_TOTAL_ORDERS.intValue()))
            .andExpect(jsonPath("$.totalOrderAmount").value(DEFAULT_TOTAL_ORDER_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.firstTimeOrders").value(DEFAULT_FIRST_TIME_ORDERS.intValue()))
            .andExpect(jsonPath("$.recurringOrders").value(DEFAULT_RECURRING_ORDERS.intValue()))
            .andExpect(jsonPath("$.totalCustomers").value(DEFAULT_TOTAL_CUSTOMERS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAnalytics() throws Exception {
        // Get the analytics
        restAnalyticsMockMvc.perform(get("/api/analytics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalytics() throws Exception {
        // Initialize the database
        analyticsRepository.saveAndFlush(analytics);

        int databaseSizeBeforeUpdate = analyticsRepository.findAll().size();

        // Update the analytics
        Analytics updatedAnalytics = analyticsRepository.findById(analytics.getId()).get();
        // Disconnect from session so that the updates on updatedAnalytics are not directly saved in db
        em.detach(updatedAnalytics);
        updatedAnalytics
            .shop(UPDATED_SHOP)
            .totalSubscriptions(UPDATED_TOTAL_SUBSCRIPTIONS)
            .totalOrders(UPDATED_TOTAL_ORDERS)
            .totalOrderAmount(UPDATED_TOTAL_ORDER_AMOUNT)
            .firstTimeOrders(UPDATED_FIRST_TIME_ORDERS)
            .recurringOrders(UPDATED_RECURRING_ORDERS)
            .totalCustomers(UPDATED_TOTAL_CUSTOMERS);
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(updatedAnalytics);

        restAnalyticsMockMvc.perform(put("/api/analytics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analyticsDTO)))
            .andExpect(status().isOk());

        // Validate the Analytics in the database
        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeUpdate);
        Analytics testAnalytics = analyticsList.get(analyticsList.size() - 1);
        assertThat(testAnalytics.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testAnalytics.getTotalSubscriptions()).isEqualTo(UPDATED_TOTAL_SUBSCRIPTIONS);
        assertThat(testAnalytics.getTotalOrders()).isEqualTo(UPDATED_TOTAL_ORDERS);
        assertThat(testAnalytics.getTotalOrderAmount()).isEqualTo(UPDATED_TOTAL_ORDER_AMOUNT);
        assertThat(testAnalytics.getFirstTimeOrders()).isEqualTo(UPDATED_FIRST_TIME_ORDERS);
        assertThat(testAnalytics.getRecurringOrders()).isEqualTo(UPDATED_RECURRING_ORDERS);
        assertThat(testAnalytics.getTotalCustomers()).isEqualTo(UPDATED_TOTAL_CUSTOMERS);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalytics() throws Exception {
        int databaseSizeBeforeUpdate = analyticsRepository.findAll().size();

        // Create the Analytics
        AnalyticsDTO analyticsDTO = analyticsMapper.toDto(analytics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalyticsMockMvc.perform(put("/api/analytics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analyticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Analytics in the database
        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnalytics() throws Exception {
        // Initialize the database
        analyticsRepository.saveAndFlush(analytics);

        int databaseSizeBeforeDelete = analyticsRepository.findAll().size();

        // Delete the analytics
        restAnalyticsMockMvc.perform(delete("/api/analytics/{id}", analytics.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Analytics> analyticsList = analyticsRepository.findAll();
        assertThat(analyticsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
