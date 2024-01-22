package com.et.service;

import com.et.domain.SubscriptionGroupPlan;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.service.dto.SubscriptionGroupV2DTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionGroupPlan}.
 */
public interface SubscriptionGroupPlanService {


    /**
     * Save a subscriptionGroupPlan.
     *
     * @param subscriptionGroupPlanDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionGroupPlanDTO save(SubscriptionGroupPlanDTO subscriptionGroupPlanDTO);

    /**
     * Get all the subscriptionGroupPlans.
     *
     * @return the list of entities.
     */
    List<SubscriptionGroupPlanDTO> findAll();


    /**
     * Get the "id" subscriptionGroupPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionGroupPlanDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionGroupPlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    SubscriptionGroupPlan createOrUpdateRecord(String shop, Long subscriptionId, SubscriptionGroupV2DTO subscriptionGroupDTO);

    Optional<SubscriptionGroupV2DTO> getSingleSubscriptionGroupPlan(String shop,Long subscriptionId);

    List<String> getAllOtherProductIdsBySubscription(String shop,Long subscriptionId);

    List<SubscriptionGroupV2DTO> getAllSubscriptionGroupPlan(String shop);

    List<SubscriptionGroupPlanDTO> findByShop(String shop);

    Optional<SubscriptionGroupPlanDTO> findBySubscriptionId(Long subscriptionId);
}
