package com.et.service;

import com.et.service.dto.MembershipDiscountCollectionsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.MembershipDiscountCollections}.
 */
public interface MembershipDiscountCollectionsService {

    /**
     * Save a membershipDiscountCollections.
     *
     * @param membershipDiscountCollectionsDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipDiscountCollectionsDTO save(MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO);

    /**
     * Get all the membershipDiscountCollections.
     *
     * @return the list of entities.
     */
    List<MembershipDiscountCollectionsDTO> findAll();


    /**
     * Get the "id" membershipDiscountCollections.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipDiscountCollectionsDTO> findOne(Long id);

    /**
     * Delete the "id" membershipDiscountCollections.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
