package com.et.web.rest;

import com.et.domain.PaymentPlanActivity;
import com.et.service.PaymentPlanActivityService;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.PaymentPlanActivity}.
 */
@RestController
@RequestMapping("/api")
public class PaymentPlanActivityResource {

    private final Logger log = LoggerFactory.getLogger(PaymentPlanActivityResource.class);

    private static final String ENTITY_NAME = "paymentPlanActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentPlanActivityService paymentPlanActivityService;

    public PaymentPlanActivityResource(PaymentPlanActivityService paymentPlanActivityService) {
        this.paymentPlanActivityService = paymentPlanActivityService;
    }

    /**
     * {@code POST  /payment-plan-activities} : Create a new paymentPlanActivity.
     *
     * @param paymentPlanActivity the paymentPlanActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentPlanActivity, or with status {@code 400 (Bad Request)} if the paymentPlanActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-plan-activities")
    public ResponseEntity<PaymentPlanActivity> createPaymentPlanActivity(@Valid @RequestBody PaymentPlanActivity paymentPlanActivity) throws URISyntaxException {
        log.debug("REST request to save PaymentPlanActivity : {}", paymentPlanActivity);
        if (paymentPlanActivity.getId() != null) {
            throw new BadRequestAlertException("A new paymentPlanActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentPlanActivity result = paymentPlanActivityService.save(paymentPlanActivity);
        return ResponseEntity.created(new URI("/api/payment-plan-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-plan-activities} : Updates an existing paymentPlanActivity.
     *
     * @param paymentPlanActivity the paymentPlanActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentPlanActivity,
     * or with status {@code 400 (Bad Request)} if the paymentPlanActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentPlanActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-plan-activities")
    public ResponseEntity<PaymentPlanActivity> updatePaymentPlanActivity(@Valid @RequestBody PaymentPlanActivity paymentPlanActivity) throws URISyntaxException {
        log.debug("REST request to update PaymentPlanActivity : {}", paymentPlanActivity);
        if (paymentPlanActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentPlanActivity result = paymentPlanActivityService.save(paymentPlanActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentPlanActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-plan-activities} : get all the paymentPlanActivities.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentPlanActivities in body.
     */
    @GetMapping("/payment-plan-activities")
    public ResponseEntity<List<PaymentPlanActivity>> getAllPaymentPlanActivities(Pageable pageable) {
        log.debug("REST request to get a page of PaymentPlanActivities");
        Page<PaymentPlanActivity> page = paymentPlanActivityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-plan-activities/:id} : get the "id" paymentPlanActivity.
     *
     * @param id the id of the paymentPlanActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentPlanActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-plan-activities/{id}")
    public ResponseEntity<PaymentPlanActivity> getPaymentPlanActivity(@PathVariable Long id) {
        log.debug("REST request to get PaymentPlanActivity : {}", id);
        Optional<PaymentPlanActivity> paymentPlanActivity = paymentPlanActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentPlanActivity);
    }

    /**
     * {@code DELETE  /payment-plan-activities/:id} : delete the "id" paymentPlanActivity.
     *
     * @param id the id of the paymentPlanActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-plan-activities/{id}")
    public ResponseEntity<Void> deletePaymentPlanActivity(@PathVariable Long id) {
        log.debug("REST request to delete PaymentPlanActivity : {}", id);
        paymentPlanActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
