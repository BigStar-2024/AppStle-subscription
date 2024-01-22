package com.et.service;

import com.et.domain.PaymentPlanActivity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PaymentPlanActivity}.
 */
public interface PaymentPlanActivityService {

    /**
     * Save a paymentPlanActivity.
     *
     * @param paymentPlanActivity the entity to save.
     * @return the persisted entity.
     */
    PaymentPlanActivity save(PaymentPlanActivity paymentPlanActivity);

    /**
     * Get all the paymentPlanActivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentPlanActivity> findAll(Pageable pageable);


    /**
     * Get the "id" paymentPlanActivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentPlanActivity> findOne(Long id);

    /**
     * Delete the "id" paymentPlanActivity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
