package com.et.service;

import com.et.service.dto.ShopSettingsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ShopSettings}.
 */
public interface ShopSettingsService {

    /**
     * Save a shopSettings.
     *
     * @param shopSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    ShopSettingsDTO save(ShopSettingsDTO shopSettingsDTO);

    /**
     * Get all the shopSettings.
     *
     * @return the list of entities.
     */
    List<ShopSettingsDTO> findAll();


    /**
     * Get the "id" shopSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShopSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" shopSettings.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the "shop" shopSettings.
     *
     * @param shop the shop of the entity.
     * @return the entity.
     */
    Optional<ShopSettingsDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
