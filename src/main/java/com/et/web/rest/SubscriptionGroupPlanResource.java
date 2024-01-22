package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.SubscriptionGroupPlanService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionGroupPlanDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionGroupPlan}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionGroupPlanResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionGroupPlanResource.class);

    private static final String ENTITY_NAME = "subscriptionGroupPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionGroupPlanService subscriptionGroupPlanService;

    public SubscriptionGroupPlanResource(SubscriptionGroupPlanService subscriptionGroupPlanService) {
        this.subscriptionGroupPlanService = subscriptionGroupPlanService;
    }

    /**
     * {@code POST  /subscription-group-plans} : Create a new subscriptionGroupPlan.
     *
     * @param subscriptionGroupPlanDTO the subscriptionGroupPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionGroupPlanDTO, or with status {@code 400 (Bad Request)} if the subscriptionGroupPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-group-plans")
    public ResponseEntity<SubscriptionGroupPlanDTO> createSubscriptionGroupPlan(@Valid @RequestBody SubscriptionGroupPlanDTO subscriptionGroupPlanDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionGroupPlan : {}", subscriptionGroupPlanDTO);
        if (subscriptionGroupPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionGroupPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionGroupPlanDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionGroupPlanDTO result = subscriptionGroupPlanService.save(subscriptionGroupPlanDTO);
        return ResponseEntity.created(new URI("/api/subscription-group-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-group-plans} : Updates an existing subscriptionGroupPlan.
     *
     * @param subscriptionGroupPlanDTO the subscriptionGroupPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionGroupPlanDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionGroupPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionGroupPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-group-plans")
    public ResponseEntity<SubscriptionGroupPlanDTO> updateSubscriptionGroupPlan(@Valid @RequestBody SubscriptionGroupPlanDTO subscriptionGroupPlanDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionGroupPlan : {}", subscriptionGroupPlanDTO);
        if (subscriptionGroupPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionGroupPlanDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionGroupPlanDTO result = subscriptionGroupPlanService.save(subscriptionGroupPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionGroupPlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-group-plans} : get all the subscriptionGroupPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionGroupPlans in body.
     */
    /*@GetMapping("/subscription-group-plans")
    public List<SubscriptionGroupPlanDTO> getAllSubscriptionGroupPlans() {
        log.debug("REST request to get all SubscriptionGroupPlans");
        return subscriptionGroupPlanService.findAll();
    }*/

    /**
     * {@code GET  /subscription-group-plans/:id} : get the "id" subscriptionGroupPlan.
     *
     * @param id the id of the subscriptionGroupPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionGroupPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-group-plans/{id}")
    public ResponseEntity<SubscriptionGroupPlanDTO> getSubscriptionGroupPlan(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionGroupPlan : {}", id);
        Optional<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTO = subscriptionGroupPlanService.findOne(id);

        if (subscriptionGroupPlanDTO.isPresent()) {
            if (!subscriptionGroupPlanDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionGroupPlanDTO);
    }

    /**
     * {@code DELETE  /subscription-group-plans/:id} : delete the "id" subscriptionGroupPlan.
     *
     * @param id the id of the subscriptionGroupPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-group-plans/{id}")
    public ResponseEntity<Void> deleteSubscriptionGroupPlan(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionGroupPlan : {}", id);
        Optional<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTO = subscriptionGroupPlanService.findOne(id);

        if (subscriptionGroupPlanDTO.isPresent()) {
            if (!subscriptionGroupPlanDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }
        subscriptionGroupPlanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
