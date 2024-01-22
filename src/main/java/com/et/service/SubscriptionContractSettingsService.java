package com.et.service;

import com.et.service.dto.SubscriptionContractSettingsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionContractSettings}.
 */
public interface SubscriptionContractSettingsService {

    /**
     * Save a subscriptionContractSettings.
     *
     * @param subscriptionContractSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionContractSettingsDTO save(SubscriptionContractSettingsDTO subscriptionContractSettingsDTO);

    /**
     * Get all the subscriptionContractSettings.
     *
     * @return the list of entities.
     */
    List<SubscriptionContractSettingsDTO> findAll();


    /**
     * Get the "id" subscriptionContractSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionContractSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionContractSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SubscriptionContractSettingsDTO> findByShop(String shop);
}
