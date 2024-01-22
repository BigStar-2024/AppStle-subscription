package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.MembershipDiscountProducts;
import com.et.repository.MembershipDiscountProductsRepository;
import com.et.service.MembershipDiscountProductsService;
import com.et.service.dto.MembershipDiscountProductsDTO;
import com.et.service.mapper.MembershipDiscountProductsMapper;

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
 * Integration tests for the {@link MembershipDiscountProductsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MembershipDiscountProductsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_MEMBERSHIP_DISCOUNT_ID = 1L;
    private static final Long UPDATED_MEMBERSHIP_DISCOUNT_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TITLE = "BBBBBBBBBB";

    @Autowired
    private MembershipDiscountProductsRepository membershipDiscountProductsRepository;

    @Autowired
    private MembershipDiscountProductsMapper membershipDiscountProductsMapper;

    @Autowired
    private MembershipDiscountProductsService membershipDiscountProductsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipDiscountProductsMockMvc;

    private MembershipDiscountProducts membershipDiscountProducts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscountProducts createEntity(EntityManager em) {
        MembershipDiscountProducts membershipDiscountProducts = new MembershipDiscountProducts()
            .shop(DEFAULT_SHOP)
            .membershipDiscountId(DEFAULT_MEMBERSHIP_DISCOUNT_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .productTitle(DEFAULT_PRODUCT_TITLE);
        return membershipDiscountProducts;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscountProducts createUpdatedEntity(EntityManager em) {
        MembershipDiscountProducts membershipDiscountProducts = new MembershipDiscountProducts()
            .shop(UPDATED_SHOP)
            .membershipDiscountId(UPDATED_MEMBERSHIP_DISCOUNT_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE);
        return membershipDiscountProducts;
    }

    @BeforeEach
    public void initTest() {
        membershipDiscountProducts = createEntity(em);
    }

    @Test
    @Transactional
    public void createMembershipDiscountProducts() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountProductsRepository.findAll().size();
        // Create the MembershipDiscountProducts
        MembershipDiscountProductsDTO membershipDiscountProductsDTO = membershipDiscountProductsMapper.toDto(membershipDiscountProducts);
        restMembershipDiscountProductsMockMvc.perform(post("/api/membership-discount-products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountProductsDTO)))
            .andExpect(status().isCreated());

        // Validate the MembershipDiscountProducts in the database
        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipDiscountProducts testMembershipDiscountProducts = membershipDiscountProductsList.get(membershipDiscountProductsList.size() - 1);
        assertThat(testMembershipDiscountProducts.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testMembershipDiscountProducts.getMembershipDiscountId()).isEqualTo(DEFAULT_MEMBERSHIP_DISCOUNT_ID);
        assertThat(testMembershipDiscountProducts.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testMembershipDiscountProducts.getProductTitle()).isEqualTo(DEFAULT_PRODUCT_TITLE);
    }

    @Test
    @Transactional
    public void createMembershipDiscountProductsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountProductsRepository.findAll().size();

        // Create the MembershipDiscountProducts with an existing ID
        membershipDiscountProducts.setId(1L);
        MembershipDiscountProductsDTO membershipDiscountProductsDTO = membershipDiscountProductsMapper.toDto(membershipDiscountProducts);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipDiscountProductsMockMvc.perform(post("/api/membership-discount-products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountProductsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscountProducts in the database
        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipDiscountProductsRepository.findAll().size();
        // set the field null
        membershipDiscountProducts.setShop(null);

        // Create the MembershipDiscountProducts, which fails.
        MembershipDiscountProductsDTO membershipDiscountProductsDTO = membershipDiscountProductsMapper.toDto(membershipDiscountProducts);


        restMembershipDiscountProductsMockMvc.perform(post("/api/membership-discount-products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountProductsDTO)))
            .andExpect(status().isBadRequest());

        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembershipDiscountProducts() throws Exception {
        // Initialize the database
        membershipDiscountProductsRepository.saveAndFlush(membershipDiscountProducts);

        // Get all the membershipDiscountProductsList
        restMembershipDiscountProductsMockMvc.perform(get("/api/membership-discount-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipDiscountProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].membershipDiscountId").value(hasItem(DEFAULT_MEMBERSHIP_DISCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productTitle").value(hasItem(DEFAULT_PRODUCT_TITLE)));
    }
    
    @Test
    @Transactional
    public void getMembershipDiscountProducts() throws Exception {
        // Initialize the database
        membershipDiscountProductsRepository.saveAndFlush(membershipDiscountProducts);

        // Get the membershipDiscountProducts
        restMembershipDiscountProductsMockMvc.perform(get("/api/membership-discount-products/{id}", membershipDiscountProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipDiscountProducts.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.membershipDiscountId").value(DEFAULT_MEMBERSHIP_DISCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productTitle").value(DEFAULT_PRODUCT_TITLE));
    }
    @Test
    @Transactional
    public void getNonExistingMembershipDiscountProducts() throws Exception {
        // Get the membershipDiscountProducts
        restMembershipDiscountProductsMockMvc.perform(get("/api/membership-discount-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembershipDiscountProducts() throws Exception {
        // Initialize the database
        membershipDiscountProductsRepository.saveAndFlush(membershipDiscountProducts);

        int databaseSizeBeforeUpdate = membershipDiscountProductsRepository.findAll().size();

        // Update the membershipDiscountProducts
        MembershipDiscountProducts updatedMembershipDiscountProducts = membershipDiscountProductsRepository.findById(membershipDiscountProducts.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipDiscountProducts are not directly saved in db
        em.detach(updatedMembershipDiscountProducts);
        updatedMembershipDiscountProducts
            .shop(UPDATED_SHOP)
            .membershipDiscountId(UPDATED_MEMBERSHIP_DISCOUNT_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productTitle(UPDATED_PRODUCT_TITLE);
        MembershipDiscountProductsDTO membershipDiscountProductsDTO = membershipDiscountProductsMapper.toDto(updatedMembershipDiscountProducts);

        restMembershipDiscountProductsMockMvc.perform(put("/api/membership-discount-products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountProductsDTO)))
            .andExpect(status().isOk());

        // Validate the MembershipDiscountProducts in the database
        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeUpdate);
        MembershipDiscountProducts testMembershipDiscountProducts = membershipDiscountProductsList.get(membershipDiscountProductsList.size() - 1);
        assertThat(testMembershipDiscountProducts.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testMembershipDiscountProducts.getMembershipDiscountId()).isEqualTo(UPDATED_MEMBERSHIP_DISCOUNT_ID);
        assertThat(testMembershipDiscountProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testMembershipDiscountProducts.getProductTitle()).isEqualTo(UPDATED_PRODUCT_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingMembershipDiscountProducts() throws Exception {
        int databaseSizeBeforeUpdate = membershipDiscountProductsRepository.findAll().size();

        // Create the MembershipDiscountProducts
        MembershipDiscountProductsDTO membershipDiscountProductsDTO = membershipDiscountProductsMapper.toDto(membershipDiscountProducts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipDiscountProductsMockMvc.perform(put("/api/membership-discount-products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountProductsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscountProducts in the database
        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMembershipDiscountProducts() throws Exception {
        // Initialize the database
        membershipDiscountProductsRepository.saveAndFlush(membershipDiscountProducts);

        int databaseSizeBeforeDelete = membershipDiscountProductsRepository.findAll().size();

        // Delete the membershipDiscountProducts
        restMembershipDiscountProductsMockMvc.perform(delete("/api/membership-discount-products/{id}", membershipDiscountProducts.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipDiscountProducts> membershipDiscountProductsList = membershipDiscountProductsRepository.findAll();
        assertThat(membershipDiscountProductsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
