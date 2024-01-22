package com.et.service;

import com.et.service.dto.MembershipDiscountProductsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.MembershipDiscountProducts}.
 */
public interface MembershipDiscountProductsService {

    /**
     * Save a membershipDiscountProducts.
     *
     * @param membershipDiscountProductsDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipDiscountProductsDTO save(MembershipDiscountProductsDTO membershipDiscountProductsDTO);

    /**
     * Get all the membershipDiscountProducts.
     *
     * @return the list of entities.
     */
    List<MembershipDiscountProductsDTO> findAll();


    /**
     * Get the "id" membershipDiscountProducts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipDiscountProductsDTO> findOne(Long id);

    /**
     * Delete the "id" membershipDiscountProducts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
