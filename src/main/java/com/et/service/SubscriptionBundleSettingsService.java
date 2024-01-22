package com.et.service;

import com.et.service.dto.SubscriptionBundleSettingsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionBundleSettings}.
 */
public interface SubscriptionBundleSettingsService {

    /**
     * Save a subscriptionBundleSettings.
     *
     * @param subscriptionBundleSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionBundleSettingsDTO save(SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO);

    /**
     * Get all the subscriptionBundleSettings.
     *
     * @return the list of entities.
     */
    List<SubscriptionBundleSettingsDTO> findAll();


    /**
     * Get the "id" subscriptionBundleSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionBundleSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionBundleSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SubscriptionBundleSettingsDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
