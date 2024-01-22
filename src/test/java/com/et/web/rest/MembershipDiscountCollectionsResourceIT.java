package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.MembershipDiscountCollections;
import com.et.repository.MembershipDiscountCollectionsRepository;
import com.et.service.MembershipDiscountCollectionsService;
import com.et.service.dto.MembershipDiscountCollectionsDTO;
import com.et.service.mapper.MembershipDiscountCollectionsMapper;

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

import com.et.domain.enumeration.CollectionType;
/**
 * Integration tests for the {@link MembershipDiscountCollectionsResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MembershipDiscountCollectionsResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final Long DEFAULT_MEMBERSHIP_DISCOUNT_ID = 1L;
    private static final Long UPDATED_MEMBERSHIP_DISCOUNT_ID = 2L;

    private static final Long DEFAULT_COLLECTION_ID = 1L;
    private static final Long UPDATED_COLLECTION_ID = 2L;

    private static final String DEFAULT_COLLECTION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COLLECTION_TITLE = "BBBBBBBBBB";

    private static final CollectionType DEFAULT_COLLECTION_TYPE = CollectionType.SMART_COLLECTION;
    private static final CollectionType UPDATED_COLLECTION_TYPE = CollectionType.CUSTOM_COLLECTION;

    @Autowired
    private MembershipDiscountCollectionsRepository membershipDiscountCollectionsRepository;

    @Autowired
    private MembershipDiscountCollectionsMapper membershipDiscountCollectionsMapper;

    @Autowired
    private MembershipDiscountCollectionsService membershipDiscountCollectionsService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipDiscountCollectionsMockMvc;

    private MembershipDiscountCollections membershipDiscountCollections;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscountCollections createEntity(EntityManager em) {
        MembershipDiscountCollections membershipDiscountCollections = new MembershipDiscountCollections()
            .shop(DEFAULT_SHOP)
            .membershipDiscountId(DEFAULT_MEMBERSHIP_DISCOUNT_ID)
            .collectionId(DEFAULT_COLLECTION_ID)
            .collectionTitle(DEFAULT_COLLECTION_TITLE)
            .collectionType(DEFAULT_COLLECTION_TYPE);
        return membershipDiscountCollections;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipDiscountCollections createUpdatedEntity(EntityManager em) {
        MembershipDiscountCollections membershipDiscountCollections = new MembershipDiscountCollections()
            .shop(UPDATED_SHOP)
            .membershipDiscountId(UPDATED_MEMBERSHIP_DISCOUNT_ID)
            .collectionId(UPDATED_COLLECTION_ID)
            .collectionTitle(UPDATED_COLLECTION_TITLE)
            .collectionType(UPDATED_COLLECTION_TYPE);
        return membershipDiscountCollections;
    }

    @BeforeEach
    public void initTest() {
        membershipDiscountCollections = createEntity(em);
    }

    @Test
    @Transactional
    public void createMembershipDiscountCollections() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountCollectionsRepository.findAll().size();
        // Create the MembershipDiscountCollections
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO = membershipDiscountCollectionsMapper.toDto(membershipDiscountCollections);
        restMembershipDiscountCollectionsMockMvc.perform(post("/api/membership-discount-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountCollectionsDTO)))
            .andExpect(status().isCreated());

        // Validate the MembershipDiscountCollections in the database
        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipDiscountCollections testMembershipDiscountCollections = membershipDiscountCollectionsList.get(membershipDiscountCollectionsList.size() - 1);
        assertThat(testMembershipDiscountCollections.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testMembershipDiscountCollections.getMembershipDiscountId()).isEqualTo(DEFAULT_MEMBERSHIP_DISCOUNT_ID);
        assertThat(testMembershipDiscountCollections.getCollectionId()).isEqualTo(DEFAULT_COLLECTION_ID);
        assertThat(testMembershipDiscountCollections.getCollectionTitle()).isEqualTo(DEFAULT_COLLECTION_TITLE);
        assertThat(testMembershipDiscountCollections.getCollectionType()).isEqualTo(DEFAULT_COLLECTION_TYPE);
    }

    @Test
    @Transactional
    public void createMembershipDiscountCollectionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = membershipDiscountCollectionsRepository.findAll().size();

        // Create the MembershipDiscountCollections with an existing ID
        membershipDiscountCollections.setId(1L);
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO = membershipDiscountCollectionsMapper.toDto(membershipDiscountCollections);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipDiscountCollectionsMockMvc.perform(post("/api/membership-discount-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountCollectionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscountCollections in the database
        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkShopIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipDiscountCollectionsRepository.findAll().size();
        // set the field null
        membershipDiscountCollections.setShop(null);

        // Create the MembershipDiscountCollections, which fails.
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO = membershipDiscountCollectionsMapper.toDto(membershipDiscountCollections);


        restMembershipDiscountCollectionsMockMvc.perform(post("/api/membership-discount-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountCollectionsDTO)))
            .andExpect(status().isBadRequest());

        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembershipDiscountCollections() throws Exception {
        // Initialize the database
        membershipDiscountCollectionsRepository.saveAndFlush(membershipDiscountCollections);

        // Get all the membershipDiscountCollectionsList
        restMembershipDiscountCollectionsMockMvc.perform(get("/api/membership-discount-collections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipDiscountCollections.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].membershipDiscountId").value(hasItem(DEFAULT_MEMBERSHIP_DISCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].collectionId").value(hasItem(DEFAULT_COLLECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].collectionTitle").value(hasItem(DEFAULT_COLLECTION_TITLE)))
            .andExpect(jsonPath("$.[*].collectionType").value(hasItem(DEFAULT_COLLECTION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getMembershipDiscountCollections() throws Exception {
        // Initialize the database
        membershipDiscountCollectionsRepository.saveAndFlush(membershipDiscountCollections);

        // Get the membershipDiscountCollections
        restMembershipDiscountCollectionsMockMvc.perform(get("/api/membership-discount-collections/{id}", membershipDiscountCollections.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipDiscountCollections.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.membershipDiscountId").value(DEFAULT_MEMBERSHIP_DISCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.collectionId").value(DEFAULT_COLLECTION_ID.intValue()))
            .andExpect(jsonPath("$.collectionTitle").value(DEFAULT_COLLECTION_TITLE))
            .andExpect(jsonPath("$.collectionType").value(DEFAULT_COLLECTION_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMembershipDiscountCollections() throws Exception {
        // Get the membershipDiscountCollections
        restMembershipDiscountCollectionsMockMvc.perform(get("/api/membership-discount-collections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembershipDiscountCollections() throws Exception {
        // Initialize the database
        membershipDiscountCollectionsRepository.saveAndFlush(membershipDiscountCollections);

        int databaseSizeBeforeUpdate = membershipDiscountCollectionsRepository.findAll().size();

        // Update the membershipDiscountCollections
        MembershipDiscountCollections updatedMembershipDiscountCollections = membershipDiscountCollectionsRepository.findById(membershipDiscountCollections.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipDiscountCollections are not directly saved in db
        em.detach(updatedMembershipDiscountCollections);
        updatedMembershipDiscountCollections
            .shop(UPDATED_SHOP)
            .membershipDiscountId(UPDATED_MEMBERSHIP_DISCOUNT_ID)
            .collectionId(UPDATED_COLLECTION_ID)
            .collectionTitle(UPDATED_COLLECTION_TITLE)
            .collectionType(UPDATED_COLLECTION_TYPE);
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO = membershipDiscountCollectionsMapper.toDto(updatedMembershipDiscountCollections);

        restMembershipDiscountCollectionsMockMvc.perform(put("/api/membership-discount-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountCollectionsDTO)))
            .andExpect(status().isOk());

        // Validate the MembershipDiscountCollections in the database
        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeUpdate);
        MembershipDiscountCollections testMembershipDiscountCollections = membershipDiscountCollectionsList.get(membershipDiscountCollectionsList.size() - 1);
        assertThat(testMembershipDiscountCollections.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testMembershipDiscountCollections.getMembershipDiscountId()).isEqualTo(UPDATED_MEMBERSHIP_DISCOUNT_ID);
        assertThat(testMembershipDiscountCollections.getCollectionId()).isEqualTo(UPDATED_COLLECTION_ID);
        assertThat(testMembershipDiscountCollections.getCollectionTitle()).isEqualTo(UPDATED_COLLECTION_TITLE);
        assertThat(testMembershipDiscountCollections.getCollectionType()).isEqualTo(UPDATED_COLLECTION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMembershipDiscountCollections() throws Exception {
        int databaseSizeBeforeUpdate = membershipDiscountCollectionsRepository.findAll().size();

        // Create the MembershipDiscountCollections
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO = membershipDiscountCollectionsMapper.toDto(membershipDiscountCollections);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipDiscountCollectionsMockMvc.perform(put("/api/membership-discount-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(membershipDiscountCollectionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipDiscountCollections in the database
        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMembershipDiscountCollections() throws Exception {
        // Initialize the database
        membershipDiscountCollectionsRepository.saveAndFlush(membershipDiscountCollections);

        int databaseSizeBeforeDelete = membershipDiscountCollectionsRepository.findAll().size();

        // Delete the membershipDiscountCollections
        restMembershipDiscountCollectionsMockMvc.perform(delete("/api/membership-discount-collections/{id}", membershipDiscountCollections.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipDiscountCollections> membershipDiscountCollectionsList = membershipDiscountCollectionsRepository.findAll();
        assertThat(membershipDiscountCollectionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
