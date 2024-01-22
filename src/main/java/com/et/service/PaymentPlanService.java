package com.et.service;

import com.et.domain.PaymentPlan;
import com.et.service.dto.PlanLimitInformation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PaymentPlan}.
 */
public interface PaymentPlanService {

    /**
     * Save a paymentPlan.
     *
     * @param paymentPlan the entity to save.
     * @return the persisted entity.
     */
    PaymentPlan save(PaymentPlan paymentPlan);

    /**
     * Get all the paymentPlans.
     *
     * @return the list of entities.
     */
    List<PaymentPlan> findAll();


    /**
     * Get the "id" paymentPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentPlan> findOne(Long id);

    /**
     * Delete the "id" paymentPlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<PaymentPlan> findByShop(String shop);

    PlanLimitInformation getPlanLimitInformation(String shop) throws IOException;

    void deleteByShop(String shop);

    Boolean getTestChargeInformation(String shop);
}
