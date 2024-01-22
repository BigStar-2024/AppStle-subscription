package com.et.service;

import com.et.service.dto.CustomizationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.Customization}.
 */
public interface CustomizationService {

    /**
     * Save a customization.
     *
     * @param customizationDTO the entity to save.
     * @return the persisted entity.
     */
    CustomizationDTO save(CustomizationDTO customizationDTO);

    /**
     * Get all the customizations.
     *
     * @return the list of entities.
     */
    List<CustomizationDTO> findAll();


    /**
     * Get the "id" customization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomizationDTO> findOne(Long id);

    /**
     * Delete the "id" customization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CustomizationDTO> findAllById(List<Long> labelIds);
}
