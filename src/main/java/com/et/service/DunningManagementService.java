package com.et.service;

import com.et.service.dto.DunningManagementDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.DunningManagement}.
 */
public interface DunningManagementService {

    /**
     * Save a dunningManagement.
     *
     * @param dunningManagementDTO the entity to save.
     * @return the persisted entity.
     */
    DunningManagementDTO save(DunningManagementDTO dunningManagementDTO);

    /**
     * Get all the dunningManagements.
     *
     * @return the list of entities.
     */
    List<DunningManagementDTO> findAll();

    List<DunningManagementDTO> findAllByShop(String shop);


    /**
     * Get the "id" dunningManagement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DunningManagementDTO> findOne(Long id);

    Optional<DunningManagementDTO> findOneByShop(String shop);

    /**
     * Delete the "id" dunningManagement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByShop(String shop);
}
