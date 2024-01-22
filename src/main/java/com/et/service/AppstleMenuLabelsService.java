package com.et.service;

import com.et.service.dto.AppstleMenuLabelsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.AppstleMenuLabels}.
 */
public interface AppstleMenuLabelsService {

    /**
     * Save a appstleMenuLabels.
     *
     * @param appstleMenuLabelsDTO the entity to save.
     * @return the persisted entity.
     */
    AppstleMenuLabelsDTO save(AppstleMenuLabelsDTO appstleMenuLabelsDTO);

    /**
     * Get all the appstleMenuLabels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppstleMenuLabelsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" appstleMenuLabels.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppstleMenuLabelsDTO> findOne(Long id);

    Optional<AppstleMenuLabelsDTO> findOneByShop(String shop);

    /**
     * Delete the "id" appstleMenuLabels.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
