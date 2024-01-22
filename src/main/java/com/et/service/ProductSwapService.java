package com.et.service;

import com.et.service.dto.ProductSwapDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ProductSwap}.
 */
public interface ProductSwapService {

    /**
     * Save a productSwap.
     *
     * @param productSwapDTO the entity to save.
     * @return the persisted entity.
     */
    ProductSwapDTO save(ProductSwapDTO productSwapDTO);

    /**
     * Get all the productSwaps.
     *
     * @return the list of entities.
     */
    List<ProductSwapDTO> findAll();


    /**
     * Get the "id" productSwap.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductSwapDTO> findOne(Long id);

    /**
     * Delete the "id" productSwap.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProductSwapDTO> findByShop(String shop);

    List<ProductSwapDTO> findByShopAndEnabledForRecurring(String shop, boolean enabledForRecurringOrder);
}
