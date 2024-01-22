package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.CurrencyConversionInfo;
import com.et.repository.CurrencyConversionInfoRepository;
import com.et.service.CurrencyConversionInfoService;
import com.et.service.dto.CurrencyConversionInfoDTO;
import com.et.service.mapper.CurrencyConversionInfoMapper;
import com.et.web.rest.errors.ExceptionTranslator;
import com.et.service.dto.CurrencyConversionInfoCriteria;
import com.et.service.CurrencyConversionInfoQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.et.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CurrencyConversionInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class CurrencyConversionInfoResourceIT {

    private static final String DEFAULT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TO = "AAAAAAAAAA";
    private static final String UPDATED_TO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STORED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STORED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STORED_ON = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_CURRENCY_RATE = 1D;
    private static final Double UPDATED_CURRENCY_RATE = 2D;
    private static final Double SMALLER_CURRENCY_RATE = 1D - 1D;

    @Autowired
    private CurrencyConversionInfoRepository currencyConversionInfoRepository;

    @Autowired
    private CurrencyConversionInfoMapper currencyConversionInfoMapper;

    @Autowired
    private CurrencyConversionInfoService currencyConversionInfoService;

    @Autowired
    private CurrencyConversionInfoQueryService currencyConversionInfoQueryService;

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

    private MockMvc restCurrencyConversionInfoMockMvc;

    private CurrencyConversionInfo currencyConversionInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyConversionInfoResource currencyConversionInfoResource = new CurrencyConversionInfoResource(currencyConversionInfoService, currencyConversionInfoQueryService);
        this.restCurrencyConversionInfoMockMvc = MockMvcBuilders.standaloneSetup(currencyConversionInfoResource)
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
    public static CurrencyConversionInfo createEntity(EntityManager em) {
        CurrencyConversionInfo currencyConversionInfo = new CurrencyConversionInfo()
            .from(DEFAULT_FROM)
            .to(DEFAULT_TO)
            .storedOn(DEFAULT_STORED_ON)
            .currencyRate(DEFAULT_CURRENCY_RATE);
        return currencyConversionInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyConversionInfo createUpdatedEntity(EntityManager em) {
        CurrencyConversionInfo currencyConversionInfo = new CurrencyConversionInfo()
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .storedOn(UPDATED_STORED_ON)
            .currencyRate(UPDATED_CURRENCY_RATE);
        return currencyConversionInfo;
    }

    @BeforeEach
    public void initTest() {
        currencyConversionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyConversionInfo() throws Exception {
        int databaseSizeBeforeCreate = currencyConversionInfoRepository.findAll().size();

        // Create the CurrencyConversionInfo
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(currencyConversionInfo);
        restCurrencyConversionInfoMockMvc.perform(post("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyConversionInfo in the database
        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyConversionInfo testCurrencyConversionInfo = currencyConversionInfoList.get(currencyConversionInfoList.size() - 1);
        assertThat(testCurrencyConversionInfo.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testCurrencyConversionInfo.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testCurrencyConversionInfo.getStoredOn()).isEqualTo(DEFAULT_STORED_ON);
        assertThat(testCurrencyConversionInfo.getCurrencyRate()).isEqualTo(DEFAULT_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void createCurrencyConversionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyConversionInfoRepository.findAll().size();

        // Create the CurrencyConversionInfo with an existing ID
        currencyConversionInfo.setId(1L);
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(currencyConversionInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyConversionInfoMockMvc.perform(post("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyConversionInfo in the database
        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyConversionInfoRepository.findAll().size();
        // set the field null
        currencyConversionInfo.setFrom(null);

        // Create the CurrencyConversionInfo, which fails.
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(currencyConversionInfo);

        restCurrencyConversionInfoMockMvc.perform(post("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isBadRequest());

        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyConversionInfoRepository.findAll().size();
        // set the field null
        currencyConversionInfo.setTo(null);

        // Create the CurrencyConversionInfo, which fails.
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(currencyConversionInfo);

        restCurrencyConversionInfoMockMvc.perform(post("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isBadRequest());

        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfos() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyConversionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].storedOn").value(hasItem(DEFAULT_STORED_ON.toString())))
            .andExpect(jsonPath("$.[*].currencyRate").value(hasItem(DEFAULT_CURRENCY_RATE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCurrencyConversionInfo() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get the currencyConversionInfo
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos/{id}", currencyConversionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyConversionInfo.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO))
            .andExpect(jsonPath("$.storedOn").value(DEFAULT_STORED_ON.toString()))
            .andExpect(jsonPath("$.currencyRate").value(DEFAULT_CURRENCY_RATE.doubleValue()));
    }


    @Test
    @Transactional
    public void getCurrencyConversionInfosByIdFiltering() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        Long id = currencyConversionInfo.getId();

        defaultCurrencyConversionInfoShouldBeFound("id.equals=" + id);
        defaultCurrencyConversionInfoShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyConversionInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyConversionInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyConversionInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyConversionInfoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from equals to DEFAULT_FROM
        defaultCurrencyConversionInfoShouldBeFound("from.equals=" + DEFAULT_FROM);

        // Get all the currencyConversionInfoList where from equals to UPDATED_FROM
        defaultCurrencyConversionInfoShouldNotBeFound("from.equals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from not equals to DEFAULT_FROM
        defaultCurrencyConversionInfoShouldNotBeFound("from.notEquals=" + DEFAULT_FROM);

        // Get all the currencyConversionInfoList where from not equals to UPDATED_FROM
        defaultCurrencyConversionInfoShouldBeFound("from.notEquals=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromIsInShouldWork() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from in DEFAULT_FROM or UPDATED_FROM
        defaultCurrencyConversionInfoShouldBeFound("from.in=" + DEFAULT_FROM + "," + UPDATED_FROM);

        // Get all the currencyConversionInfoList where from equals to UPDATED_FROM
        defaultCurrencyConversionInfoShouldNotBeFound("from.in=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from is not null
        defaultCurrencyConversionInfoShouldBeFound("from.specified=true");

        // Get all the currencyConversionInfoList where from is null
        defaultCurrencyConversionInfoShouldNotBeFound("from.specified=false");
    }
                @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromContainsSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from contains DEFAULT_FROM
        defaultCurrencyConversionInfoShouldBeFound("from.contains=" + DEFAULT_FROM);

        // Get all the currencyConversionInfoList where from contains UPDATED_FROM
        defaultCurrencyConversionInfoShouldNotBeFound("from.contains=" + UPDATED_FROM);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByFromNotContainsSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where from does not contain DEFAULT_FROM
        defaultCurrencyConversionInfoShouldNotBeFound("from.doesNotContain=" + DEFAULT_FROM);

        // Get all the currencyConversionInfoList where from does not contain UPDATED_FROM
        defaultCurrencyConversionInfoShouldBeFound("from.doesNotContain=" + UPDATED_FROM);
    }


    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to equals to DEFAULT_TO
        defaultCurrencyConversionInfoShouldBeFound("to.equals=" + DEFAULT_TO);

        // Get all the currencyConversionInfoList where to equals to UPDATED_TO
        defaultCurrencyConversionInfoShouldNotBeFound("to.equals=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to not equals to DEFAULT_TO
        defaultCurrencyConversionInfoShouldNotBeFound("to.notEquals=" + DEFAULT_TO);

        // Get all the currencyConversionInfoList where to not equals to UPDATED_TO
        defaultCurrencyConversionInfoShouldBeFound("to.notEquals=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToIsInShouldWork() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to in DEFAULT_TO or UPDATED_TO
        defaultCurrencyConversionInfoShouldBeFound("to.in=" + DEFAULT_TO + "," + UPDATED_TO);

        // Get all the currencyConversionInfoList where to equals to UPDATED_TO
        defaultCurrencyConversionInfoShouldNotBeFound("to.in=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to is not null
        defaultCurrencyConversionInfoShouldBeFound("to.specified=true");

        // Get all the currencyConversionInfoList where to is null
        defaultCurrencyConversionInfoShouldNotBeFound("to.specified=false");
    }
                @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToContainsSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to contains DEFAULT_TO
        defaultCurrencyConversionInfoShouldBeFound("to.contains=" + DEFAULT_TO);

        // Get all the currencyConversionInfoList where to contains UPDATED_TO
        defaultCurrencyConversionInfoShouldNotBeFound("to.contains=" + UPDATED_TO);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByToNotContainsSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where to does not contain DEFAULT_TO
        defaultCurrencyConversionInfoShouldNotBeFound("to.doesNotContain=" + DEFAULT_TO);

        // Get all the currencyConversionInfoList where to does not contain UPDATED_TO
        defaultCurrencyConversionInfoShouldBeFound("to.doesNotContain=" + UPDATED_TO);
    }


    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn equals to DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.equals=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn equals to UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.equals=" + UPDATED_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn not equals to DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.notEquals=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn not equals to UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.notEquals=" + UPDATED_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsInShouldWork() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn in DEFAULT_STORED_ON or UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.in=" + DEFAULT_STORED_ON + "," + UPDATED_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn equals to UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.in=" + UPDATED_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn is not null
        defaultCurrencyConversionInfoShouldBeFound("storedOn.specified=true");

        // Get all the currencyConversionInfoList where storedOn is null
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn is greater than or equal to DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.greaterThanOrEqual=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn is greater than or equal to UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.greaterThanOrEqual=" + UPDATED_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn is less than or equal to DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.lessThanOrEqual=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn is less than or equal to SMALLER_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.lessThanOrEqual=" + SMALLER_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn is less than DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.lessThan=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn is less than UPDATED_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.lessThan=" + UPDATED_STORED_ON);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByStoredOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where storedOn is greater than DEFAULT_STORED_ON
        defaultCurrencyConversionInfoShouldNotBeFound("storedOn.greaterThan=" + DEFAULT_STORED_ON);

        // Get all the currencyConversionInfoList where storedOn is greater than SMALLER_STORED_ON
        defaultCurrencyConversionInfoShouldBeFound("storedOn.greaterThan=" + SMALLER_STORED_ON);
    }


    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate equals to DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.equals=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate equals to UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.equals=" + UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate not equals to DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.notEquals=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate not equals to UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.notEquals=" + UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsInShouldWork() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate in DEFAULT_CURRENCY_RATE or UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.in=" + DEFAULT_CURRENCY_RATE + "," + UPDATED_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate equals to UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.in=" + UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate is not null
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.specified=true");

        // Get all the currencyConversionInfoList where currencyRate is null
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate is greater than or equal to DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.greaterThanOrEqual=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate is greater than or equal to UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.greaterThanOrEqual=" + UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate is less than or equal to DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.lessThanOrEqual=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate is less than or equal to SMALLER_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.lessThanOrEqual=" + SMALLER_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate is less than DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.lessThan=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate is less than UPDATED_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.lessThan=" + UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void getAllCurrencyConversionInfosByCurrencyRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        // Get all the currencyConversionInfoList where currencyRate is greater than DEFAULT_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldNotBeFound("currencyRate.greaterThan=" + DEFAULT_CURRENCY_RATE);

        // Get all the currencyConversionInfoList where currencyRate is greater than SMALLER_CURRENCY_RATE
        defaultCurrencyConversionInfoShouldBeFound("currencyRate.greaterThan=" + SMALLER_CURRENCY_RATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyConversionInfoShouldBeFound(String filter) throws Exception {
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyConversionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM)))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO)))
            .andExpect(jsonPath("$.[*].storedOn").value(hasItem(DEFAULT_STORED_ON.toString())))
            .andExpect(jsonPath("$.[*].currencyRate").value(hasItem(DEFAULT_CURRENCY_RATE.doubleValue())));

        // Check, that the count call also returns 1
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyConversionInfoShouldNotBeFound(String filter) throws Exception {
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCurrencyConversionInfo() throws Exception {
        // Get the currencyConversionInfo
        restCurrencyConversionInfoMockMvc.perform(get("/api/currency-conversion-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyConversionInfo() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        int databaseSizeBeforeUpdate = currencyConversionInfoRepository.findAll().size();

        // Update the currencyConversionInfo
        CurrencyConversionInfo updatedCurrencyConversionInfo = currencyConversionInfoRepository.findById(currencyConversionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedCurrencyConversionInfo are not directly saved in db
        em.detach(updatedCurrencyConversionInfo);
        updatedCurrencyConversionInfo
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .storedOn(UPDATED_STORED_ON)
            .currencyRate(UPDATED_CURRENCY_RATE);
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(updatedCurrencyConversionInfo);

        restCurrencyConversionInfoMockMvc.perform(put("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isOk());

        // Validate the CurrencyConversionInfo in the database
        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeUpdate);
        CurrencyConversionInfo testCurrencyConversionInfo = currencyConversionInfoList.get(currencyConversionInfoList.size() - 1);
        assertThat(testCurrencyConversionInfo.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testCurrencyConversionInfo.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testCurrencyConversionInfo.getStoredOn()).isEqualTo(UPDATED_STORED_ON);
        assertThat(testCurrencyConversionInfo.getCurrencyRate()).isEqualTo(UPDATED_CURRENCY_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyConversionInfo() throws Exception {
        int databaseSizeBeforeUpdate = currencyConversionInfoRepository.findAll().size();

        // Create the CurrencyConversionInfo
        CurrencyConversionInfoDTO currencyConversionInfoDTO = currencyConversionInfoMapper.toDto(currencyConversionInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyConversionInfoMockMvc.perform(put("/api/currency-conversion-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyConversionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyConversionInfo in the database
        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCurrencyConversionInfo() throws Exception {
        // Initialize the database
        currencyConversionInfoRepository.saveAndFlush(currencyConversionInfo);

        int databaseSizeBeforeDelete = currencyConversionInfoRepository.findAll().size();

        // Delete the currencyConversionInfo
        restCurrencyConversionInfoMockMvc.perform(delete("/api/currency-conversion-infos/{id}", currencyConversionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CurrencyConversionInfo> currencyConversionInfoList = currencyConversionInfoRepository.findAll();
        assertThat(currencyConversionInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
