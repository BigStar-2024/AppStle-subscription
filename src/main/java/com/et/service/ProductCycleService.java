package com.et.service;

import com.et.service.dto.ProductCycleDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ProductCycle}.
 */
public interface ProductCycleService {

    /**
     * Save a productCycle.
     *
     * @param productCycleDTO the entity to save.
     * @return the persisted entity.
     */
    ProductCycleDTO save(ProductCycleDTO productCycleDTO);

    /**
     * Get all the productCycles.
     *
     * @return the list of entities.
     */
    List<ProductCycleDTO> findAll();


    @Transactional(readOnly = true)
    List<ProductCycleDTO> findByShop(String shop);

    /**
     * Get the "id" productCycle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductCycleDTO> findOne(Long id);

    /**
     * Delete the "id" productCycle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
