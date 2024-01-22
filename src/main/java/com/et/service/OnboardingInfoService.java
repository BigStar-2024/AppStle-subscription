package com.et.service;

import com.et.service.dto.CancellationManagementDTO;
import com.et.service.dto.OnboardingInfoDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.OnboardingInfo}.
 */
public interface OnboardingInfoService {

    /**
     * Save a onboardingInfo.
     *
     * @param onboardingInfoDTO the entity to save.
     * @return the persisted entity.
     */
    OnboardingInfoDTO save(OnboardingInfoDTO onboardingInfoDTO);

    /**
     * Get all the onboardingInfos.
     *
     * @return the list of entities.
     */
    List<OnboardingInfoDTO> findAll();


    /**
     * Get the "id" onboardingInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OnboardingInfoDTO> findOne(Long id);

    /**
     * Delete the "id" onboardingInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    Optional<OnboardingInfoDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
