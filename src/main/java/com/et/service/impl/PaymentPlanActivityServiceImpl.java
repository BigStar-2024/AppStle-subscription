package com.et.service.impl;

import com.et.service.PaymentPlanActivityService;
import com.et.domain.PaymentPlanActivity;
import com.et.repository.PaymentPlanActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentPlanActivity}.
 */
@Service
@Transactional
public class PaymentPlanActivityServiceImpl implements PaymentPlanActivityService {

    private final Logger log = LoggerFactory.getLogger(PaymentPlanActivityServiceImpl.class);

    private final PaymentPlanActivityRepository paymentPlanActivityRepository;

    public PaymentPlanActivityServiceImpl(PaymentPlanActivityRepository paymentPlanActivityRepository) {
        this.paymentPlanActivityRepository = paymentPlanActivityRepository;
    }

    /**
     * Save a paymentPlanActivity.
     *
     * @param paymentPlanActivity the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaymentPlanActivity save(PaymentPlanActivity paymentPlanActivity) {
        log.debug("Request to save PaymentPlanActivity : {}", paymentPlanActivity);
        return paymentPlanActivityRepository.save(paymentPlanActivity);
    }

    /**
     * Get all the paymentPlanActivities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentPlanActivity> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentPlanActivities");
        return paymentPlanActivityRepository.findAll(pageable);
    }


    /**
     * Get one paymentPlanActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentPlanActivity> findOne(Long id) {
        log.debug("Request to get PaymentPlanActivity : {}", id);
        return paymentPlanActivityRepository.findById(id);
    }

    /**
     * Delete the paymentPlanActivity by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentPlanActivity : {}", id);
        paymentPlanActivityRepository.deleteById(id);
    }
}
