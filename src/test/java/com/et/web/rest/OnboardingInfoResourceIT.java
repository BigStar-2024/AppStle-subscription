package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.OnboardingInfo;
import com.et.domain.enumeration.OnboardingChecklistStep;
import com.et.repository.OnboardingInfoRepository;
import com.et.service.OnboardingInfoService;
import com.et.service.dto.OnboardingInfoDTO;
import com.et.service.mapper.OnboardingInfoMapper;

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
 * Integration tests for the {@link OnboardingInfoResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OnboardingInfoResourceIT {

    private static final String DEFAULT_SHOP = "AAAAAAAAAA";
    private static final String UPDATED_SHOP = "BBBBBBBBBB";

    private static final List<OnboardingChecklistStep> DEFAULT_UNCOMPLETED_CHECKLIST_STEPS = List.of(OnboardingChecklistStep.values());
    private static final List<OnboardingChecklistStep> UPDATED_UNCOMPLETED_CHECKLIST_STEPS = List.of(OnboardingChecklistStep.values());

    private static final List<OnboardingChecklistStep> DEFAULT_COMPLETED_CHECKLIST_STEPS = List.of();
    private static final List<OnboardingChecklistStep> UPDATED_COMPLETED_CHECKLIST_STEPS = List.of();

    private static final Boolean DEFAULT_CHECKLIST_COMPLETED = false;
    private static final Boolean UPDATED_CHECKLIST_COMPLETED = true;

    @Autowired
    private OnboardingInfoRepository onboardingInfoRepository;

    @Autowired
    private OnboardingInfoMapper onboardingInfoMapper;

    @Autowired
    private OnboardingInfoService onboardingInfoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOnboardingInfoMockMvc;

    private OnboardingInfo onboardingInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OnboardingInfo createEntity(EntityManager em) {
        OnboardingInfo onboardingInfo = new OnboardingInfo()
            .shop(DEFAULT_SHOP)
            .uncompletedChecklistSteps(DEFAULT_UNCOMPLETED_CHECKLIST_STEPS)
            .completedChecklistSteps(DEFAULT_COMPLETED_CHECKLIST_STEPS)
            .checklistCompleted(DEFAULT_CHECKLIST_COMPLETED);
        return onboardingInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OnboardingInfo createUpdatedEntity(EntityManager em) {
        OnboardingInfo onboardingInfo = new OnboardingInfo()
            .shop(UPDATED_SHOP)
            .uncompletedChecklistSteps(UPDATED_UNCOMPLETED_CHECKLIST_STEPS)
            .completedChecklistSteps(UPDATED_COMPLETED_CHECKLIST_STEPS)
            .checklistCompleted(UPDATED_CHECKLIST_COMPLETED);
        return onboardingInfo;
    }

    @BeforeEach
    public void initTest() {
        onboardingInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createOnboardingInfo() throws Exception {
        int databaseSizeBeforeCreate = onboardingInfoRepository.findAll().size();
        // Create the OnboardingInfo
        OnboardingInfoDTO onboardingInfoDTO = onboardingInfoMapper.toDto(onboardingInfo);
        restOnboardingInfoMockMvc.perform(post("/api/onboarding-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(onboardingInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the OnboardingInfo in the database
        List<OnboardingInfo> onboardingInfoList = onboardingInfoRepository.findAll();
        assertThat(onboardingInfoList).hasSize(databaseSizeBeforeCreate + 1);
        OnboardingInfo testOnboardingInfo = onboardingInfoList.get(onboardingInfoList.size() - 1);
        assertThat(testOnboardingInfo.getShop()).isEqualTo(DEFAULT_SHOP);
        assertThat(testOnboardingInfo.getUncompletedChecklistSteps()).isEqualTo(DEFAULT_UNCOMPLETED_CHECKLIST_STEPS);
        assertThat(testOnboardingInfo.getCompletedChecklistSteps()).isEqualTo(DEFAULT_COMPLETED_CHECKLIST_STEPS);
        assertThat(testOnboardingInfo.isChecklistCompleted()).isEqualTo(DEFAULT_CHECKLIST_COMPLETED);
    }

    @Test
    @Transactional
    public void createOnboardingInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = onboardingInfoRepository.findAll().size();

        // Create the OnboardingInfo with an existing ID
        onboardingInfo.setId(1L);
        OnboardingInfoDTO onboardingInfoDTO = onboardingInfoMapper.toDto(onboardingInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOnboardingInfoMockMvc.perform(post("/api/onboarding-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(onboardingInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OnboardingInfo in the database
        List<OnboardingInfo> onboardingInfoList = onboardingInfoRepository.findAll();
        assertThat(onboardingInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOnboardingInfos() throws Exception {
        // Initialize the database
        onboardingInfoRepository.saveAndFlush(onboardingInfo);

        // Get all the onboardingInfoList
        restOnboardingInfoMockMvc.perform(get("/api/onboarding-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(onboardingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].shop").value(hasItem(DEFAULT_SHOP)))
            .andExpect(jsonPath("$.[*].uncompletedChecklistSteps").value(hasItem(DEFAULT_UNCOMPLETED_CHECKLIST_STEPS)))
            .andExpect(jsonPath("$.[*].completedChecklistSteps").value(hasItem(DEFAULT_COMPLETED_CHECKLIST_STEPS)))
            .andExpect(jsonPath("$.[*].checklistCompleted").value(hasItem(DEFAULT_CHECKLIST_COMPLETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getOnboardingInfo() throws Exception {
        // Initialize the database
        onboardingInfoRepository.saveAndFlush(onboardingInfo);

        // Get the onboardingInfo
        restOnboardingInfoMockMvc.perform(get("/api/onboarding-infos/{id}", onboardingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(onboardingInfo.getId().intValue()))
            .andExpect(jsonPath("$.shop").value(DEFAULT_SHOP))
            .andExpect(jsonPath("$.uncompletedChecklistSteps").value(DEFAULT_UNCOMPLETED_CHECKLIST_STEPS))
            .andExpect(jsonPath("$.completedChecklistSteps").value(DEFAULT_COMPLETED_CHECKLIST_STEPS))
            .andExpect(jsonPath("$.checklistCompleted").value(DEFAULT_CHECKLIST_COMPLETED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingOnboardingInfo() throws Exception {
        // Get the onboardingInfo
        restOnboardingInfoMockMvc.perform(get("/api/onboarding-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOnboardingInfo() throws Exception {
        // Initialize the database
        onboardingInfoRepository.saveAndFlush(onboardingInfo);

        int databaseSizeBeforeUpdate = onboardingInfoRepository.findAll().size();

        // Update the onboardingInfo
        OnboardingInfo updatedOnboardingInfo = onboardingInfoRepository.findById(onboardingInfo.getId()).get();
        // Disconnect from session so that the updates on updatedOnboardingInfo are not directly saved in db
        em.detach(updatedOnboardingInfo);
        updatedOnboardingInfo
            .shop(UPDATED_SHOP)
            .uncompletedChecklistSteps(UPDATED_UNCOMPLETED_CHECKLIST_STEPS)
            .completedChecklistSteps(UPDATED_COMPLETED_CHECKLIST_STEPS)
            .checklistCompleted(UPDATED_CHECKLIST_COMPLETED);
        OnboardingInfoDTO onboardingInfoDTO = onboardingInfoMapper.toDto(updatedOnboardingInfo);

        restOnboardingInfoMockMvc.perform(put("/api/onboarding-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(onboardingInfoDTO)))
            .andExpect(status().isOk());

        // Validate the OnboardingInfo in the database
        List<OnboardingInfo> onboardingInfoList = onboardingInfoRepository.findAll();
        assertThat(onboardingInfoList).hasSize(databaseSizeBeforeUpdate);
        OnboardingInfo testOnboardingInfo = onboardingInfoList.get(onboardingInfoList.size() - 1);
        assertThat(testOnboardingInfo.getShop()).isEqualTo(UPDATED_SHOP);
        assertThat(testOnboardingInfo.getUncompletedChecklistSteps()).isEqualTo(UPDATED_UNCOMPLETED_CHECKLIST_STEPS);
        assertThat(testOnboardingInfo.getCompletedChecklistSteps()).isEqualTo(UPDATED_COMPLETED_CHECKLIST_STEPS);
        assertThat(testOnboardingInfo.isChecklistCompleted()).isEqualTo(UPDATED_CHECKLIST_COMPLETED);
    }

    @Test
    @Transactional
    public void updateNonExistingOnboardingInfo() throws Exception {
        int databaseSizeBeforeUpdate = onboardingInfoRepository.findAll().size();

        // Create the OnboardingInfo
        OnboardingInfoDTO onboardingInfoDTO = onboardingInfoMapper.toDto(onboardingInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOnboardingInfoMockMvc.perform(put("/api/onboarding-infos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(onboardingInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OnboardingInfo in the database
        List<OnboardingInfo> onboardingInfoList = onboardingInfoRepository.findAll();
        assertThat(onboardingInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOnboardingInfo() throws Exception {
        // Initialize the database
        onboardingInfoRepository.saveAndFlush(onboardingInfo);

        int databaseSizeBeforeDelete = onboardingInfoRepository.findAll().size();

        // Delete the onboardingInfo
        restOnboardingInfoMockMvc.perform(delete("/api/onboarding-infos/{id}", onboardingInfo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OnboardingInfo> onboardingInfoList = onboardingInfoRepository.findAll();
        assertThat(onboardingInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
