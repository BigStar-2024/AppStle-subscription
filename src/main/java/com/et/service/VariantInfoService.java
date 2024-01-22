package com.et.service;

import com.et.service.dto.VariantInfoDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.VariantInfo}.
 */
public interface VariantInfoService {

    /**
     * Save a variantInfo.
     *
     * @param variantInfoDTO the entity to save.
     * @return the persisted entity.
     */
    VariantInfoDTO save(VariantInfoDTO variantInfoDTO);

    /**
     * Get all the variantInfos.
     *
     * @return the list of entities.
     */
    List<VariantInfoDTO> findAll();


    /**
     * Get the "id" variantInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VariantInfoDTO> findOne(Long id);

    /**
     * Delete the "id" variantInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
