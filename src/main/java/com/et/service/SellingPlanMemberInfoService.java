package com.et.service;

import com.et.service.dto.SellingPlanMemberInfoDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SellingPlanMemberInfo}.
 */
public interface SellingPlanMemberInfoService {

    /**
     * Save a sellingPlanMemberInfo.
     *
     * @param sellingPlanMemberInfoDTO the entity to save.
     * @return the persisted entity.
     */
    SellingPlanMemberInfoDTO save(SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO);

    /**
     * Get all the sellingPlanMemberInfos.
     *
     * @return the list of entities.
     */
    List<SellingPlanMemberInfoDTO> findAll();


    /**
     * Get the "id" sellingPlanMemberInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SellingPlanMemberInfoDTO> findOne(Long id);

    /**
     * Delete the "id" sellingPlanMemberInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SellingPlanMemberInfoDTO> findOneBySellingPlanId(String shop, Long sellingPlanId);

    List<SellingPlanMemberInfoDTO> findByShop(String shop);
}
