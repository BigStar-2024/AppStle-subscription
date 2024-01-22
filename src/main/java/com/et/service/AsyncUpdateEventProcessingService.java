package com.et.service;

import com.et.service.dto.AsyncUpdateEventProcessingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.AsyncUpdateEventProcessing}.
 */
public interface AsyncUpdateEventProcessingService {

    /**
     * Save a asyncUpdateEventProcessing.
     *
     * @param asyncUpdateEventProcessingDTO the entity to save.
     * @return the persisted entity.
     */
    AsyncUpdateEventProcessingDTO save(AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO);

    /**
     * Get all the asyncUpdateEventProcessings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AsyncUpdateEventProcessingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" asyncUpdateEventProcessing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AsyncUpdateEventProcessingDTO> findOne(Long id);

    /**
     * Delete the "id" asyncUpdateEventProcessing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
