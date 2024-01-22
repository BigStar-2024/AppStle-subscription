package com.et.service;

import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.web.rest.vm.SyncLabelsInfo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.CustomerPortalSettings}.
 */
public interface CustomerPortalSettingsService {

    /**
     * Save a customerPortalSettings.
     *
     * @param customerPortalSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerPortalSettingsDTO save(CustomerPortalSettingsDTO customerPortalSettingsDTO);

    /**
     * Get all the customerPortalSettings.
     *
     * @return the list of entities.
     */
    List<CustomerPortalSettingsDTO> findAll();


    /**
     * Get the "id" customerPortalSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerPortalSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" customerPortalSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<CustomerPortalSettingsDTO> findByShop(String shop);

    void deleteByShop(String shop);

    List<CustomerPortalSettingsDTO> findAllByShop(String shop);
}
