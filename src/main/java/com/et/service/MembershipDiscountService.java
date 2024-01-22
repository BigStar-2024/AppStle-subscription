package com.et.service;

import com.et.service.dto.MembershipDiscountDTO;
import com.et.web.rest.vm.MembershipDiscountRequest;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.MembershipDiscount}.
 */
public interface MembershipDiscountService {

    /**
     * Save a membershipDiscount.
     *
     * @param membershipDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipDiscountDTO save(MembershipDiscountDTO membershipDiscountDTO);

    /**
     * Get all the membershipDiscounts.
     *
     * @return the list of entities.
     */
    List<MembershipDiscountDTO> findAll();


    /**
     * Get the "id" membershipDiscount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipDiscountDTO> findOne(Long id);

    /**
     * Delete the "id" membershipDiscount.
     *
     * @param id the id of the entity.
     */
    void delete(String shop ,Long id);

    List<MembershipDiscountDTO> findByShop(String shop);

    MembershipDiscountDTO createOrUpdate(String shop, MembershipDiscountRequest membershipDiscountRequest);
}
