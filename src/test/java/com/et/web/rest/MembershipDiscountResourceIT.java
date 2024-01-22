package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.MembershipDiscount;
import com.et.repository.MembershipDiscountRepository;
import com.et.service.MembershipDiscountService;
import com.et.service.dto.MembershipDiscountDTO;
import com.et.service.mapper.MembershipDiscountMapper;

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
 * Integration tests for the {@link MembershipDiscountResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MembershipDiscountResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final String DEFAULT_CUSTOMER_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_TAGS = "BBBBBBBBBB";

    @Autowired
    private MembershipDiscountRepository membershipDiscountRepository;

    @Autowired
    private MembershipDiscountMapper membershipDiscountMapper;

    @Autowired
    private MembershipDiscountService membershipDiscountService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipDiscountMockMvc;

    private MembershipDiscount membershipDiscount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscount createEntity(EntityManager em) {
        MembershipDiscount membershipDiscount = new MembershipDiscount()
            .shop(DEFAULT_SHOP)
            .title(DEFAULT_TITLE)
            .discount(DEFAULT_DISCOUNT)
            .customerTags(DEFAULT_CUSTOMER_TAGS);
        return membershipDiscount;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscount createUpdatedEntity(EntityManager em) {
        MembershipDiscount membershipDiscount = new MembershipDiscount()
            .shop(UPDATED_SHOP)
            .title(UPDATED_TITLE)
            .discount(UPDATED_DISCOUNT)
            .customerTags(UPDATED_CUSTOMER_TAGS);
        return membershipDiscount;
    }

    @BeforeEach
    public void initTest() {
        membershipDiscount = createEntity(em);
    }

    @Test
    @Transactional
    public void createMembershipDiscount() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountRepository.findAll().size();
        // Create the MembershipDiscount
        MembershipDiscountDTO membershipDiscountDTO = membershipDiscountMapper.toDto(membershipDiscount);
        restMembershipDiscountMockMvc.perform(post("/api/membership-discounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountDTO)))
            .andExpect(status().isCreated());

        // Validate the MembershipDiscount in the database
        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipDiscount testMembershipDiscount = membershipDiscountList.get(membershipDiscountList.size() - 1);
        assertThat(testMembershipDiscount.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testMembershipDiscount.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMembershipDiscount.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testMembershipDiscount.getCustomerTags()).isEqualTo(DEFAULT_CUSTOMER_TAGS);
    }

    @Test
    @Transactional
    public void createMembershipDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountRepository.findAll().size();

        // Create the MembershipDiscount with an existing ID
        membershipDiscount.setId(1L);
        MembershipDiscountDTO membershipDiscountDTO = membershipDiscountMapper.toDto(membershipDiscount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipDiscountMockMvc.perform(post("/api/membership-discounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscount in the database
        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipDiscountRepository.findAll().size();
        // set the field null
        membershipDiscount.setShop(null);

        // Create the MembershipDiscount, which fails.
        MembershipDiscountDTO membershipDiscountDTO = membershipDiscountMapper.toDto(membershipDiscount);


        restMembershipDiscountMockMvc.perform(post("/api/membership-discounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountDTO)))
            .andExpect(status().isBadRequest());

        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembershipDiscounts() throws Exception {
        // Initialize the database
        membershipDiscountRepository.saveAndFlush(membershipDiscount);

        // Get all the membershipDiscountList
        restMembershipDiscountMockMvc.perform(get("/api/membership-discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].customerTags").value(hasItem(DEFAULT_CUSTOMER_TAGS)));
    }
    
    @Test
    @Transactional
    public void getMembershipDiscount() throws Exception {
        // Initialize the database
        membershipDiscountRepository.saveAndFlush(membershipDiscount);

        // Get the membershipDiscount
        restMembershipDiscountMockMvc.perform(get("/api/membership-discounts/{id}", membershipDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipDiscount.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.customerTags").value(DEFAULT_CUSTOMER_TAGS));
    }
    @Test
    @Transactional
    public void getNonExistingMembershipDiscount() throws Exception {
        // Get the membershipDiscount
        restMembershipDiscountMockMvc.perform(get("/api/membership-discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembershipDiscount() throws Exception {
        // Initialize the database
        membershipDiscountRepository.saveAndFlush(membershipDiscount);

        int databaseSizeBeforeUpdate = membershipDiscountRepository.findAll().size();

        // Update the membershipDiscount
        MembershipDiscount updatedMembershipDiscount = membershipDiscountRepository.findById(membershipDiscount.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipDiscount are not directly saved in db
        em.detach(updatedMembershipDiscount);
        updatedMembershipDiscount
            .shop(UPDATED_SHOP)
            .title(UPDATED_TITLE)
            .discount(UPDATED_DISCOUNT)
            .customerTags(UPDATED_CUSTOMER_TAGS);
        MembershipDiscountDTO membershipDiscountDTO = membershipDiscountMapper.toDto(updatedMembershipDiscount);

        restMembershipDiscountMockMvc.perform(put("/api/membership-discounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountDTO)))
            .andExpect(status().isOk());

        // Validate the MembershipDiscount in the database
        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeUpdate);
        MembershipDiscount testMembershipDiscount = membershipDiscountList.get(membershipDiscountList.size() - 1);
        assertThat(testMembershipDiscount.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testMembershipDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMembershipDiscount.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testMembershipDiscount.getCustomerTags()).isEqualTo(UPDATED_CUSTOMER_TAGS);
    }

    @Test
    @Transactional
    public void updateNonExistingMembershipDiscount() throws Exception {
        int databaseSizeBeforeUpdate = membershipDiscountRepository.findAll().size();

        // Create the MembershipDiscount
        MembershipDiscountDTO membershipDiscountDTO = membershipDiscountMapper.toDto(membershipDiscount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipDiscountMockMvc.perform(put("/api/membership-discounts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscount in the database
        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMembershipDiscount() throws Exception {
        // Initialize the database
        membershipDiscountRepository.saveAndFlush(membershipDiscount);

        int databaseSizeBeforeDelete = membershipDiscountRepository.findAll().size();

        // Delete the membershipDiscount
        restMembershipDiscountMockMvc.perform(delete("/api/membership-discounts/{id}", membershipDiscount.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipDiscount> membershipDiscountList = membershipDiscountRepository.findAll();
        assertThat(membershipDiscountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
