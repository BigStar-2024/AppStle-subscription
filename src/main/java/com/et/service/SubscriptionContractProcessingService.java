package com.et.service;

import com.et.service.dto.SubscriptionContractProcessingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionContractProcessing}.
 */
public interface SubscriptionContractProcessingService {

    /**
     * Save a subscriptionContractProcessing.
     *
     * @param subscriptionContractProcessingDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionContractProcessingDTO save(SubscriptionContractProcessingDTO subscriptionContractProcessingDTO);

    /**
     * Get all the subscriptionContractProcessings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriptionContractProcessingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" subscriptionContractProcessing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionContractProcessingDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionContractProcessing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SubscriptionContractProcessingDTO> findByContractId(Long contractId);
}
