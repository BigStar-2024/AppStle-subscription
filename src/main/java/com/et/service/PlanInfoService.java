package com.et.service;

import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanInfoDiscountDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.PlanInfo}.
 */
public interface PlanInfoService {

    /**
     * Save a planInfo.
     *
     * @param planInfoDTO the entity to save.
     * @return the persisted entity.
     */
    PlanInfoDTO save(PlanInfoDTO planInfoDTO);

    /**
     * Get all the planInfos.
     *
     * @return the list of entities.
     */
    List<PlanInfoDTO> findAll();


    /**
     * Get the "id" planInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanInfoDTO> findOne(Long id);

    /**
     * Delete the "id" planInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<PlanInfoDTO> findByName(String name);

    PlanInfoDTO createPlanInfoForDiscount(PlanInfoDTO planInfoDTO, PlanInfoDiscountDTO planInfoDiscountDTO);
}
