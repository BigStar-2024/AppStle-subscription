package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.SellingPlanMemberInfo;
import com.et.repository.SellingPlanMemberInfoRepository;
import com.et.service.SellingPlanMemberInfoService;
import com.et.service.dto.SellingPlanMemberInfoDTO;
import com.et.service.mapper.SellingPlanMemberInfoMapper;

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
 * Integration tests for the {@link SellingPlanMemberInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SellingPlanMemberInfoResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_SUBSCRIPTION_ID = 1L;
    private static final Long UPDATED_SUBSCRIPTION_ID = 2L;

    private static final Long DEFAULT_SELLING_PLAN_ID = 1L;
    private static final Long UPDATED_SELLING_PLAN_ID = 2L;

    private static final Boolean DEFAULT_ENABLE_MEMBER_INCLUSIVE_TAG = false;
    private static final Boolean UPDATED_ENABLE_MEMBER_INCLUSIVE_TAG = true;

    private static final String DEFAULT_MEMBER_INCLUSIVE_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_INCLUSIVE_TAGS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_MEMBER_EXCLUSIVE_TAG = false;
    private static final Boolean UPDATED_ENABLE_MEMBER_EXCLUSIVE_TAG = true;

    private static final String DEFAULT_MEMBER_EXCLUSIVE_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_EXCLUSIVE_TAGS = "BBBBBBBBBB";

    @Autowired
    private SellingPlanMemberInfoRepository sellingPlanMemberInfoRepository;

    @Autowired
    private SellingPlanMemberInfoMapper sellingPlanMemberInfoMapper;

    @Autowired
    private SellingPlanMemberInfoService sellingPlanMemberInfoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellingPlanMemberInfoMockMvc;

    private SellingPlanMemberInfo sellingPlanMemberInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellingPlanMemberInfo createEntity(EntityManager em) {
        SellingPlanMemberInfo sellingPlanMemberInfo = new SellingPlanMemberInfo()
            .shop(DEFAULT_SHOP)
            .subscriptionId(DEFAULT_SUBSCRIPTION_ID)
            .sellingPlanId(DEFAULT_SELLING_PLAN_ID)
            .enableMemberInclusiveTag(DEFAULT_ENABLE_MEMBER_INCLUSIVE_TAG)
            .memberInclusiveTags(DEFAULT_MEMBER_INCLUSIVE_TAGS)
            .enableMemberExclusiveTag(DEFAULT_ENABLE_MEMBER_EXCLUSIVE_TAG)
            .memberExclusiveTags(DEFAULT_MEMBER_EXCLUSIVE_TAGS);
        return sellingPlanMemberInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellingPlanMemberInfo createUpdatedEntity(EntityManager em) {
        SellingPlanMemberInfo sellingPlanMemberInfo = new SellingPlanMemberInfo()
            .shop(UPDATED_SHOP)
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .sellingPlanId(UPDATED_SELLING_PLAN_ID)
            .enableMemberInclusiveTag(UPDATED_ENABLE_MEMBER_INCLUSIVE_TAG)
            .memberInclusiveTags(UPDATED_MEMBER_INCLUSIVE_TAGS)
            .enableMemberExclusiveTag(UPDATED_ENABLE_MEMBER_EXCLUSIVE_TAG)
            .memberExclusiveTags(UPDATED_MEMBER_EXCLUSIVE_TAGS);
        return sellingPlanMemberInfo;
    }

    @BeforeEach
    public void initTest() {
        sellingPlanMemberInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSellingPlanMemberInfo() throws Exception {
        int databaseSizeBeforeCreate = sellingPlanMemberInfoRepository.findAll().size();
        // Create the SellingPlanMemberInfo
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = sellingPlanMemberInfoMapper.toDto(sellingPlanMemberInfo);
        restSellingPlanMemberInfoMockMvc.perform(post("/api/selling-plan-member-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sellingPlanMemberInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the SellingPlanMemberInfo in the database
        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SellingPlanMemberInfo testSellingPlanMemberInfo = sellingPlanMemberInfoList.get(sellingPlanMemberInfoList.size() - 1);
        assertThat(testSellingPlanMemberInfo.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testSellingPlanMemberInfo.getSubscriptionId()).isEqualTo(DEFAULT_SUBSCRIPTION_ID);
        assertThat(testSellingPlanMemberInfo.getSellingPlanId()).isEqualTo(DEFAULT_SELLING_PLAN_ID);
        assertThat(testSellingPlanMemberInfo.isEnableMemberInclusiveTag()).isEqualTo(DEFAULT_ENABLE_MEMBER_INCLUSIVE_TAG);
        assertThat(testSellingPlanMemberInfo.getMemberInclusiveTags()).isEqualTo(DEFAULT_MEMBER_INCLUSIVE_TAGS);
        assertThat(testSellingPlanMemberInfo.isEnableMemberExclusiveTag()).isEqualTo(DEFAULT_ENABLE_MEMBER_EXCLUSIVE_TAG);
        assertThat(testSellingPlanMemberInfo.getMemberExclusiveTags()).isEqualTo(DEFAULT_MEMBER_EXCLUSIVE_TAGS);
    }

    @Test
    @Transactional
    public void createSellingPlanMemberInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sellingPlanMemberInfoRepository.findAll().size();

        // Create the SellingPlanMemberInfo with an existing ID
        sellingPlanMemberInfo.setId(1L);
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = sellingPlanMemberInfoMapper.toDto(sellingPlanMemberInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellingPlanMemberInfoMockMvc.perform(post("/api/selling-plan-member-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sellingPlanMemberInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SellingPlanMemberInfo in the database
        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellingPlanMemberInfoRepository.findAll().size();
        // set the field null
        sellingPlanMemberInfo.setShop(null);

        // Create the SellingPlanMemberInfo, which fails.
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = sellingPlanMemberInfoMapper.toDto(sellingPlanMemberInfo);


        restSellingPlanMemberInfoMockMvc.perform(post("/api/selling-plan-member-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sellingPlanMemberInfoDTO)))
            .andExpect(status().isBadRequest());

        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSellingPlanMemberInfos() throws Exception {
        // Initialize the database
        sellingPlanMemberInfoRepository.saveAndFlush(sellingPlanMemberInfo);

        // Get all the sellingPlanMemberInfoList
        restSellingPlanMemberInfoMockMvc.perform(get("/api/selling-plan-member-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellingPlanMemberInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].subscriptionId").value(hasItem(DEFAULT_SUBSCRIPTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].sellingPlanId").value(hasItem(DEFAULT_SELLING_PLAN_ID.intValue())))
            .andExpect(jsonPath("$.[*].enableMemberInclusiveTag").value(hasItem(DEFAULT_ENABLE_MEMBER_INCLUSIVE_TAG.booleanValue())))
            .andExpect(jsonPath("$.[*].memberInclusiveTags").value(hasItem(DEFAULT_MEMBER_INCLUSIVE_TAGS)))
            .andExpect(jsonPath("$.[*].enableMemberExclusiveTag").value(hasItem(DEFAULT_ENABLE_MEMBER_EXCLUSIVE_TAG.booleanValue())))
            .andExpect(jsonPath("$.[*].memberExclusiveTags").value(hasItem(DEFAULT_MEMBER_EXCLUSIVE_TAGS)));
    }
    
    @Test
    @Transactional
    public void getSellingPlanMemberInfo() throws Exception {
        // Initialize the database
        sellingPlanMemberInfoRepository.saveAndFlush(sellingPlanMemberInfo);

        // Get the sellingPlanMemberInfo
        restSellingPlanMemberInfoMockMvc.perform(get("/api/selling-plan-member-infos/{id}", sellingPlanMemberInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sellingPlanMemberInfo.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.subscriptionId").value(DEFAULT_SUBSCRIPTION_ID.intValue()))
            .andExpect(jsonPath("$.sellingPlanId").value(DEFAULT_SELLING_PLAN_ID.intValue()))
            .andExpect(jsonPath("$.enableMemberInclusiveTag").value(DEFAULT_ENABLE_MEMBER_INCLUSIVE_TAG.booleanValue()))
            .andExpect(jsonPath("$.memberInclusiveTags").value(DEFAULT_MEMBER_INCLUSIVE_TAGS))
            .andExpect(jsonPath("$.enableMemberExclusiveTag").value(DEFAULT_ENABLE_MEMBER_EXCLUSIVE_TAG.booleanValue()))
            .andExpect(jsonPath("$.memberExclusiveTags").value(DEFAULT_MEMBER_EXCLUSIVE_TAGS));
    }
    @Test
    @Transactional
    public void getNonExistingSellingPlanMemberInfo() throws Exception {
        // Get the sellingPlanMemberInfo
        restSellingPlanMemberInfoMockMvc.perform(get("/api/selling-plan-member-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSellingPlanMemberInfo() throws Exception {
        // Initialize the database
        sellingPlanMemberInfoRepository.saveAndFlush(sellingPlanMemberInfo);

        int databaseSizeBeforeUpdate = sellingPlanMemberInfoRepository.findAll().size();

        // Update the sellingPlanMemberInfo
        SellingPlanMemberInfo updatedSellingPlanMemberInfo = sellingPlanMemberInfoRepository.findById(sellingPlanMemberInfo.getId()).get();
        // Disconnect from session so that the updates on updatedSellingPlanMemberInfo are not directly saved in db
        em.detach(updatedSellingPlanMemberInfo);
        updatedSellingPlanMemberInfo
            .shop(UPDATED_SHOP)
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .sellingPlanId(UPDATED_SELLING_PLAN_ID)
            .enableMemberInclusiveTag(UPDATED_ENABLE_MEMBER_INCLUSIVE_TAG)
            .memberInclusiveTags(UPDATED_MEMBER_INCLUSIVE_TAGS)
            .enableMemberExclusiveTag(UPDATED_ENABLE_MEMBER_EXCLUSIVE_TAG)
            .memberExclusiveTags(UPDATED_MEMBER_EXCLUSIVE_TAGS);
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = sellingPlanMemberInfoMapper.toDto(updatedSellingPlanMemberInfo);

        restSellingPlanMemberInfoMockMvc.perform(put("/api/selling-plan-member-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sellingPlanMemberInfoDTO)))
            .andExpect(status().isOk());

        // Validate the SellingPlanMemberInfo in the database
        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeUpdate);
        SellingPlanMemberInfo testSellingPlanMemberInfo = sellingPlanMemberInfoList.get(sellingPlanMemberInfoList.size() - 1);
        assertThat(testSellingPlanMemberInfo.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testSellingPlanMemberInfo.getSubscriptionId()).isEqualTo(UPDATED_SUBSCRIPTION_ID);
        assertThat(testSellingPlanMemberInfo.getSellingPlanId()).isEqualTo(UPDATED_SELLING_PLAN_ID);
        assertThat(testSellingPlanMemberInfo.isEnableMemberInclusiveTag()).isEqualTo(UPDATED_ENABLE_MEMBER_INCLUSIVE_TAG);
        assertThat(testSellingPlanMemberInfo.getMemberInclusiveTags()).isEqualTo(UPDATED_MEMBER_INCLUSIVE_TAGS);
        assertThat(testSellingPlanMemberInfo.isEnableMemberExclusiveTag()).isEqualTo(UPDATED_ENABLE_MEMBER_EXCLUSIVE_TAG);
        assertThat(testSellingPlanMemberInfo.getMemberExclusiveTags()).isEqualTo(UPDATED_MEMBER_EXCLUSIVE_TAGS);
    }

    @Test
    @Transactional
    public void updateNonExistingSellingPlanMemberInfo() throws Exception {
        int databaseSizeBeforeUpdate = sellingPlanMemberInfoRepository.findAll().size();

        // Create the SellingPlanMemberInfo
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO = sellingPlanMemberInfoMapper.toDto(sellingPlanMemberInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellingPlanMemberInfoMockMvc.perform(put("/api/selling-plan-member-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sellingPlanMemberInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SellingPlanMemberInfo in the database
        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSellingPlanMemberInfo() throws Exception {
        // Initialize the database
        sellingPlanMemberInfoRepository.saveAndFlush(sellingPlanMemberInfo);

        int databaseSizeBeforeDelete = sellingPlanMemberInfoRepository.findAll().size();

        // Delete the sellingPlanMemberInfo
        restSellingPlanMemberInfoMockMvc.perform(delete("/api/selling-plan-member-infos/{id}", sellingPlanMemberInfo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SellingPlanMemberInfo> sellingPlanMemberInfoList = sellingPlanMemberInfoRepository.findAll();
        assertThat(sellingPlanMemberInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
