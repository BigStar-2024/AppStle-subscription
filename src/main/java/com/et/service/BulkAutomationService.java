package com.et.service;

import com.et.domain.enumeration.BulkAutomationType;
import com.et.service.dto.BulkAutomationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.BulkAutomation}.
 */
public interface BulkAutomationService {

    /**
     * Save a bulkAutomation.
     *
     * @param bulkAutomationDTO the entity to save.
     * @return the persisted entity.
     */
    BulkAutomationDTO save(BulkAutomationDTO bulkAutomationDTO);

    /**
     * Get all the bulkAutomations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BulkAutomationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bulkAutomation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BulkAutomationDTO> findOne(Long id);

    /**
     * Delete the "id" bulkAutomation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<BulkAutomationDTO> findByShopAndAutomationType(String shop, BulkAutomationType automationType);

    Page<BulkAutomationDTO> findAllByShop(String shop, Pageable pageable);

    BulkAutomationDTO startBulkAutomationProcess(BulkAutomationDTO bulkAutomationDTO, String shop, BulkAutomationType automationType, String requestJson);

    void updateBulkAutomationErrorMsg(String shop, BulkAutomationType automationType, String errorMsg);

    void stopBulkAutomationProcess(String shop, BulkAutomationType automationType, String errorMsg);
}
