package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.OrderInfo;
import com.et.repository.OrderInfoRepository;
import com.et.service.OrderInfoService;
import com.et.service.dto.OrderInfoDTO;
import com.et.service.mapper.OrderInfoMapper;
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
 * Integration tests for the {@link OrderInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class OrderInfoResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final String DEFAULT_LINES_JSON = "AAAAAAAAAA";
    private static final String UPDATED_LINES_JSON = "BBBBBBBBBB";

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderInfoService orderInfoService;

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

    private MockMvc restOrderInfoMockMvc;

    private OrderInfo orderInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderInfoResource orderInfoResource = new OrderInfoResource(orderInfoService);
        this.restOrderInfoMockMvc = MockMvcBuilders.standaloneSetup(orderInfoResource)
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
    public static OrderInfo createEntity(EntityManager em) {
        OrderInfo orderInfo = new OrderInfo()
            .shop(DEFAULT_SHOP)
            .orderId(DEFAULT_ORDER_ID)
            .linesJson(DEFAULT_LINES_JSON);
        return orderInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderInfo createUpdatedEntity(EntityManager em) {
        OrderInfo orderInfo = new OrderInfo()
            .shop(UPDATED_SHOP)
            .orderId(UPDATED_ORDER_ID)
            .linesJson(UPDATED_LINES_JSON);
        return orderInfo;
    }

    @BeforeEach
    public void initTest() {
        orderInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderInfo() throws Exception {
        int databaseSizeBeforeCreate = orderInfoRepository.findAll().size();

        // Create the OrderInfo
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(orderInfo);
        restOrderInfoMockMvc.perform(post("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeCreate + 1);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testOrderInfo.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrderInfo.getLinesJson()).isEqualTo(DEFAULT_LINES_JSON);
    }

    @Test
    @Transactional
    public void createOrderInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderInfoRepository.findAll().size();

        // Create the OrderInfo with an existing ID
        orderInfo.setId(1L);
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(orderInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderInfoMockMvc.perform(post("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().size();
        // set the field null
        orderInfo.setShop(null);

        // Create the OrderInfo, which fails.
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(orderInfo);

        restOrderInfoMockMvc.perform(post("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isBadRequest());

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().size();
        // set the field null
        orderInfo.setOrderId(null);

        // Create the OrderInfo, which fails.
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(orderInfo);

        restOrderInfoMockMvc.perform(post("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isBadRequest());

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderInfos() throws Exception {
        // Initialize the database
        orderInfoRepository.saveAndFlush(orderInfo);

        // Get all the orderInfoList
        restOrderInfoMockMvc.perform(get("/api/order-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].linesJson").value(hasItem(DEFAULT_LINES_JSON.toString())));
    }
    
    @Test
    @Transactional
    public void getOrderInfo() throws Exception {
        // Initialize the database
        orderInfoRepository.saveAndFlush(orderInfo);

        // Get the orderInfo
        restOrderInfoMockMvc.perform(get("/api/order-infos/{id}", orderInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderInfo.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.linesJson").value(DEFAULT_LINES_JSON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderInfo() throws Exception {
        // Get the orderInfo
        restOrderInfoMockMvc.perform(get("/api/order-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderInfo() throws Exception {
        // Initialize the database
        orderInfoRepository.saveAndFlush(orderInfo);

        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().size();

        // Update the orderInfo
        OrderInfo updatedOrderInfo = orderInfoRepository.findById(orderInfo.getId()).get();
        // Disconnect from session so that the updates on updatedOrderInfo are not directly saved in db
        em.detach(updatedOrderInfo);
        updatedOrderInfo
            .shop(UPDATED_SHOP)
            .orderId(UPDATED_ORDER_ID)
            .linesJson(UPDATED_LINES_JSON);
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(updatedOrderInfo);

        restOrderInfoMockMvc.perform(put("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isOk());

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testOrderInfo.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrderInfo.getLinesJson()).isEqualTo(UPDATED_LINES_JSON);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().size();

        // Create the OrderInfo
        OrderInfoDTO orderInfoDTO = orderInfoMapper.toDto(orderInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderInfoMockMvc.perform(put("/api/order-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderInfo() throws Exception {
        // Initialize the database
        orderInfoRepository.saveAndFlush(orderInfo);

        int databaseSizeBeforeDelete = orderInfoRepository.findAll().size();

        // Delete the orderInfo
        restOrderInfoMockMvc.perform(delete("/api/order-infos/{id}", orderInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
