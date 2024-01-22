package com.et.service;

import com.et.service.dto.ProductFieldDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ProductField}.
 */
public interface ProductFieldService {

    /**
     * Save a productField.
     *
     * @param productFieldDTOs the entity to save.
     * @param shop
     * @return the persisted entity.
     */
    ProductFieldDTO save(List<ProductFieldDTO> productFieldDTOs, String shop);

    /**
     * Get all the productFields.
     *
     * @return the list of entities.
     * @param shop
     */
    List<ProductFieldDTO> findByShopUnCached(String shop);


    /**
     * Get the "id" productField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductFieldDTO> findOne(Long id);

    /**
     * Delete the "id" productField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductFieldDTO> findByShop(String shop);
}
