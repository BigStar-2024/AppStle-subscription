package com.et.service;

import com.et.service.dto.CancellationManagementDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.CancellationManagement}.
 */
public interface CancellationManagementService {

    /**
     * Save a cancellationManagement.
     *
     * @param cancellationManagementDTO the entity to save.
     * @return the persisted entity.
     */
    CancellationManagementDTO save(CancellationManagementDTO cancellationManagementDTO);

    /**
     * Get all the cancellationManagements.
     *
     * @return the list of entities.
     */
    List<CancellationManagementDTO> findAll();


    /**
     * Get the "id" cancellationManagement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CancellationManagementDTO> findOne(Long id);

    /**
     * Delete the "id" cancellationManagement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<CancellationManagementDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
