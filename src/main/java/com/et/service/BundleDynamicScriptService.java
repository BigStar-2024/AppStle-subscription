package com.et.service;

import com.et.service.dto.BundleDynamicScriptDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.BundleDynamicScript}.
 */
public interface BundleDynamicScriptService {

    /**
     * Save a bundleDynamicScript.
     *
     * @param bundleDynamicScriptDTO the entity to save.
     * @return the persisted entity.
     */
    BundleDynamicScriptDTO save(BundleDynamicScriptDTO bundleDynamicScriptDTO);

    /**
     * Get all the bundleDynamicScripts.
     *
     * @return the list of entities.
     */
    List<BundleDynamicScriptDTO> findAll();


    /**
     * Get the "id" bundleDynamicScript.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BundleDynamicScriptDTO> findOne(Long id);

    /**
     * Delete the "id" bundleDynamicScript.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<BundleDynamicScriptDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
