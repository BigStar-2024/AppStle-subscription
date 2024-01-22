package com.et.service;

import com.et.service.dto.CustomerPortalDynamicScriptDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.CustomerPortalDynamicScript}.
 */
public interface CustomerPortalDynamicScriptService {

    /**
     * Save a customerPortalDynamicScript.
     *
     * @param customerPortalDynamicScriptDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerPortalDynamicScriptDTO save(CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO);

    /**
     * Get all the customerPortalDynamicScripts.
     *
     * @return the list of entities.
     */
    List<CustomerPortalDynamicScriptDTO> findAll();


    /**
     * Get the "id" customerPortalDynamicScript.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerPortalDynamicScriptDTO> findOne(Long id);

    /**
     * Delete the "id" customerPortalDynamicScript.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<CustomerPortalDynamicScriptDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
