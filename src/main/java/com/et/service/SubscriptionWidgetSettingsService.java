package com.et.service;

import com.et.service.dto.SubscriptionWidgetSettingsDTO;
import com.et.web.rest.vm.SyncLabelsInfo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionWidgetSettings}.
 */
public interface SubscriptionWidgetSettingsService {

    /**
     * Save a subscriptionWidgetSettings.
     *
     * @param subscriptionWidgetSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionWidgetSettingsDTO save(SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO);

    /**
     * Get all the subscriptionWidgetSettings.
     *
     * @return the list of entities.
     */
    List<SubscriptionWidgetSettingsDTO> findAll();


    /**
     * Get the "id" subscriptionWidgetSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionWidgetSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionWidgetSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SubscriptionWidgetSettingsDTO> findByShop(String shop);

    void deleteByShop(String shop);

    List<SubscriptionWidgetSettingsDTO> findAllByShop(String shop);
}
