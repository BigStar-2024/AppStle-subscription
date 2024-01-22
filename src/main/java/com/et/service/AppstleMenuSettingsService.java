package com.et.service;

import com.et.service.dto.AppstleMenuSettingsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.AppstleMenuSettings}.
 */
public interface AppstleMenuSettingsService {

    /**
     * Save a appstleMenuSettings.
     *
     * @param appstleMenuSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    AppstleMenuSettingsDTO save(AppstleMenuSettingsDTO appstleMenuSettingsDTO);

    /**
     * Get all the appstleMenuSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppstleMenuSettingsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" appstleMenuSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppstleMenuSettingsDTO> findOne(Long id);

    Optional<AppstleMenuSettingsDTO> findByShop(String shop);

    /**
     * Delete the "id" appstleMenuSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void applyTagsToProductsProcess(String shop, AppstleMenuSettingsDTO appstleMenuSettingsDTO);
}
