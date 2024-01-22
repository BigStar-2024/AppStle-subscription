package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.PlanInfoDiscount;
import com.et.repository.PlanInfoDiscountRepository;
import com.et.service.PlanInfoDiscountService;
import com.et.service.dto.PlanInfoDiscountDTO;
import com.et.service.mapper.PlanInfoDiscountMapper;
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
import java.math.BigDecimal;
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

import com.et.domain.enumeration.PlanInfoDiscountType;
/**
 * Integration tests for the {@link PlanInfoDiscountResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class PlanInfoDiscountResourceIT {

    private static final String DEFAULT_DISCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final PlanInfoDiscountType DEFAULT_DISCOUNT_TYPE = PlanInfoDiscountType.PERCENTAGE;
    private static final PlanInfoDiscountType UPDATED_DISCOUNT_TYPE = PlanInfoDiscountType.FIXED_AMOUNT;

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MAX_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_DISCOUNT_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_TRIAL_DAYS = 1;
    private static final Integer UPDATED_TRIAL_DAYS = 2;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    @Autowired
    private PlanInfoDiscountRepository planInfoDiscountRepository;

    @Autowired
    private PlanInfoDiscountMapper planInfoDiscountMapper;

    @Autowired
    private PlanInfoDiscountService planInfoDiscountService;

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

    private MockMvc restPlanInfoDiscountMockMvc;

    private PlanInfoDiscount planInfoDiscount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanInfoDiscountResource planInfoDiscountResource = new PlanInfoDiscountResource(planInfoDiscountService);
        this.restPlanInfoDiscountMockMvc = MockMvcBuilders.standaloneSetup(planInfoDiscountResource)
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
    public static PlanInfoDiscount createEntity(EntityManager em) {
        PlanInfoDiscount planInfoDiscount = new PlanInfoDiscount()
            .discountCode(DEFAULT_DISCOUNT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discount(DEFAULT_DISCOUNT)
            .maxDiscountAmount(DEFAULT_MAX_DISCOUNT_AMOUNT)
            .trialDays(DEFAULT_TRIAL_DAYS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .archived(DEFAULT_ARCHIVED);
        return planInfoDiscount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanInfoDiscount createUpdatedEntity(EntityManager em) {
        PlanInfoDiscount planInfoDiscount = new PlanInfoDiscount()
            .discountCode(UPDATED_DISCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .maxDiscountAmount(UPDATED_MAX_DISCOUNT_AMOUNT)
            .trialDays(UPDATED_TRIAL_DAYS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .archived(UPDATED_ARCHIVED);
        return planInfoDiscount;
    }

    @BeforeEach
    public void initTest() {
        planInfoDiscount = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanInfoDiscount() throws Exception {
        int databaseSizeBeforeCreate = planInfoDiscountRepository.findAll().size();

        // Create the PlanInfoDiscount
        PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountMapper.toDto(planInfoDiscount);
        restPlanInfoDiscountMockMvc.perform(post("/api/plan-info-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planInfoDiscountDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanInfoDiscount in the database
        List<PlanInfoDiscount> planInfoDiscountList = planInfoDiscountRepository.findAll();
        assertThat(planInfoDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        PlanInfoDiscount testPlanInfoDiscount = planInfoDiscountList.get(planInfoDiscountList.size() - 1);
        assertThat(testPlanInfoDiscount.getDiscountCode()).isEqualTo(DEFAULT_DISCOUNT_CODE);
        assertThat(testPlanInfoDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlanInfoDiscount.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testPlanInfoDiscount.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPlanInfoDiscount.getMaxDiscountAmount()).isEqualTo(DEFAULT_MAX_DISCOUNT_AMOUNT);
        assertThat(testPlanInfoDiscount.getTrialDays()).isEqualTo(DEFAULT_TRIAL_DAYS);
        assertThat(testPlanInfoDiscount.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPlanInfoDiscount.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPlanInfoDiscount.isArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    public void createPlanInfoDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planInfoDiscountRepository.findAll().size();

        // Create the PlanInfoDiscount with an existing ID
        planInfoDiscount.setId(1L);
        PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountMapper.toDto(planInfoDiscount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanInfoDiscountMockMvc.perform(post("/api/plan-info-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planInfoDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanInfoDiscount in the database
        List<PlanInfoDiscount> planInfoDiscountList = planInfoDiscountRepository.findAll();
        assertThat(planInfoDiscountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlanInfoDiscounts() throws Exception {
        // Initialize the database
        planInfoDiscountRepository.saveAndFlush(planInfoDiscount);

        // Get all the planInfoDiscountList
        restPlanInfoDiscountMockMvc.perform(get("/api/plan-info-discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planInfoDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].discountCode").value(hasItem(DEFAULT_DISCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].maxDiscountAmount").value(hasItem(DEFAULT_MAX_DISCOUNT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].trialDays").value(hasItem(DEFAULT_TRIAL_DAYS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlanInfoDiscount() throws Exception {
        // Initialize the database
        planInfoDiscountRepository.saveAndFlush(planInfoDiscount);

        // Get the planInfoDiscount
        restPlanInfoDiscountMockMvc.perform(get("/api/plan-info-discounts/{id}", planInfoDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planInfoDiscount.getId().intValue()))
            .andExpect(jsonPath("$.discountCode").value(DEFAULT_DISCOUNT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.maxDiscountAmount").value(DEFAULT_MAX_DISCOUNT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.trialDays").value(DEFAULT_TRIAL_DAYS))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanInfoDiscount() throws Exception {
        // Get the planInfoDiscount
        restPlanInfoDiscountMockMvc.perform(get("/api/plan-info-discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanInfoDiscount() throws Exception {
        // Initialize the database
        planInfoDiscountRepository.saveAndFlush(planInfoDiscount);

        int databaseSizeBeforeUpdate = planInfoDiscountRepository.findAll().size();

        // Update the planInfoDiscount
        PlanInfoDiscount updatedPlanInfoDiscount = planInfoDiscountRepository.findById(planInfoDiscount.getId()).get();
        // Disconnect from session so that the updates on updatedPlanInfoDiscount are not directly saved in db
        em.detach(updatedPlanInfoDiscount);
        updatedPlanInfoDiscount
            .discountCode(UPDATED_DISCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .maxDiscountAmount(UPDATED_MAX_DISCOUNT_AMOUNT)
            .trialDays(UPDATED_TRIAL_DAYS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .archived(UPDATED_ARCHIVED);
        PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountMapper.toDto(updatedPlanInfoDiscount);

        restPlanInfoDiscountMockMvc.perform(put("/api/plan-info-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planInfoDiscountDTO)))
            .andExpect(status().isOk());

        // Validate the PlanInfoDiscount in the database
        List<PlanInfoDiscount> planInfoDiscountList = planInfoDiscountRepository.findAll();
        assertThat(planInfoDiscountList).hasSize(databaseSizeBeforeUpdate);
        PlanInfoDiscount testPlanInfoDiscount = planInfoDiscountList.get(planInfoDiscountList.size() - 1);
        assertThat(testPlanInfoDiscount.getDiscountCode()).isEqualTo(UPDATED_DISCOUNT_CODE);
        assertThat(testPlanInfoDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlanInfoDiscount.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPlanInfoDiscount.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPlanInfoDiscount.getMaxDiscountAmount()).isEqualTo(UPDATED_MAX_DISCOUNT_AMOUNT);
        assertThat(testPlanInfoDiscount.getTrialDays()).isEqualTo(UPDATED_TRIAL_DAYS);
        assertThat(testPlanInfoDiscount.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPlanInfoDiscount.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPlanInfoDiscount.isArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanInfoDiscount() throws Exception {
        int databaseSizeBeforeUpdate = planInfoDiscountRepository.findAll().size();

        // Create the PlanInfoDiscount
        PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountMapper.toDto(planInfoDiscount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanInfoDiscountMockMvc.perform(put("/api/plan-info-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planInfoDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanInfoDiscount in the database
        List<PlanInfoDiscount> planInfoDiscountList = planInfoDiscountRepository.findAll();
        assertThat(planInfoDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanInfoDiscount() throws Exception {
        // Initialize the database
        planInfoDiscountRepository.saveAndFlush(planInfoDiscount);

        int databaseSizeBeforeDelete = planInfoDiscountRepository.findAll().size();

        // Delete the planInfoDiscount
        restPlanInfoDiscountMockMvc.perform(delete("/api/plan-info-discounts/{id}", planInfoDiscount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanInfoDiscount> planInfoDiscountList = planInfoDiscountRepository.findAll();
        assertThat(planInfoDiscountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
