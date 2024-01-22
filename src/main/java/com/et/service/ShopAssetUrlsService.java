package com.et.service;

import com.et.service.dto.ShopAssetUrlsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ShopAssetUrls}.
 */
public interface ShopAssetUrlsService {

    /**
     * Save a shopAssetUrls.
     *
     * @param shopAssetUrlsDTO the entity to save.
     * @return the persisted entity.
     */
    ShopAssetUrlsDTO save(ShopAssetUrlsDTO shopAssetUrlsDTO);

    /**
     * Get all the shopAssetUrls.
     *
     * @return the list of entities.
     */
    List<ShopAssetUrlsDTO> findAll();


    /**
     * Get the "id" shopAssetUrls.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShopAssetUrlsDTO> findOne(Long id);

    /**
     * Delete the "id" shopAssetUrls.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ShopAssetUrlsDTO> findByShop(String shop);

    void deleteByShop(String shop);
}
