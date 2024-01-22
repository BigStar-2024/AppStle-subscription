package com.et.service;

import com.et.domain.ActivityUpdatesSettings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link ActivityUpdatesSettings}.
 */
public interface ActivityUpdatesSettingsService {

    /**
     * Save a activityUpdatesSettings.
     *
     * @param activityUpdatesSettings the entity to save.
     * @return the persisted entity.
     */
    ActivityUpdatesSettings save(ActivityUpdatesSettings activityUpdatesSettings);

    /**
     * Get all the activityUpdatesSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityUpdatesSettings> findAll(Pageable pageable);


    /**
     * Get the "id" activityUpdatesSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityUpdatesSettings> findOne(Long id);

    /**
     * Delete the "id" activityUpdatesSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ActivityUpdatesSettings> findByShop(String shop);
}
