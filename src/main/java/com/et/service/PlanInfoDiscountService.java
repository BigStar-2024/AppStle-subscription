package com.et.service;

import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanInfoDiscountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.PlanInfoDiscount}.
 */
public interface PlanInfoDiscountService {

    /**
     * Save a planInfoDiscount.
     *
     * @param planInfoDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    PlanInfoDiscountDTO save(PlanInfoDiscountDTO planInfoDiscountDTO);

    /**
     * Get all the planInfoDiscounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanInfoDiscountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" planInfoDiscount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanInfoDiscountDTO> findOne(Long id);

    /**
     * Delete the "id" planInfoDiscount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    Optional<PlanInfoDiscountDTO> findByDiscountCode(String discountCode);

    boolean isValidDiscountCode(String discountCode);

    Double calculatePlanDiscountPrice(PlanInfoDTO planInfoDTO, PlanInfoDiscountDTO planInfoDiscountDTO);
}
