package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.SubscriptionContractSettingsService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionContractSettingsDTO;

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
 * REST controller for managing {@link com.et.domain.SubscriptionContractSettings}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionContractSettingsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractSettingsResource.class);

    private static final String ENTITY_NAME = "subscriptionContractSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionContractSettingsService subscriptionContractSettingsService;

    public SubscriptionContractSettingsResource(SubscriptionContractSettingsService subscriptionContractSettingsService) {
        this.subscriptionContractSettingsService = subscriptionContractSettingsService;
    }

    /**
     * {@code POST  /subscription-contract-settings} : Create a new subscriptionContractSettings.
     *
     * @param subscriptionContractSettingsDTO the subscriptionContractSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionContractSettingsDTO, or with status {@code 400 (Bad Request)} if the subscriptionContractSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-contract-settings")
    public ResponseEntity<SubscriptionContractSettingsDTO> createSubscriptionContractSettings(@Valid @RequestBody SubscriptionContractSettingsDTO subscriptionContractSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionContractSettings : {}", subscriptionContractSettingsDTO);
        if (subscriptionContractSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionContractSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionContractSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractSettingsDTO result = subscriptionContractSettingsService.save(subscriptionContractSettingsDTO);
        return ResponseEntity.created(new URI("/api/subscription-contract-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-contract-settings} : Updates an existing subscriptionContractSettings.
     *
     * @param subscriptionContractSettingsDTO the subscriptionContractSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionContractSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionContractSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionContractSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-contract-settings")
    public ResponseEntity<SubscriptionContractSettingsDTO> updateSubscriptionContractSettings(@Valid @RequestBody SubscriptionContractSettingsDTO subscriptionContractSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionContractSettings : {}", subscriptionContractSettingsDTO);
        if (subscriptionContractSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionContractSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractSettingsDTO result = subscriptionContractSettingsService.save(subscriptionContractSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionContractSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-contract-settings} : get all the subscriptionContractSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionContractSettings in body.
     */
    @GetMapping("/subscription-contract-settings")
    public List<SubscriptionContractSettingsDTO> getAllSubscriptionContractSettings() {
        log.debug("REST request to get all SubscriptionContractSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return subscriptionContractSettingsService.findByShop(shop);
    }

    /**
     * {@code GET  /subscription-contract-settings/:id} : get the "id" subscriptionContractSettings.
     *
     * @param id the id of the subscriptionContractSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionContractSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-contract-settings/{id}")
    public ResponseEntity<SubscriptionContractSettingsDTO> getSubscriptionContractSettings(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionContractSettings : {}", id);
        Optional<SubscriptionContractSettingsDTO> subscriptionContractSettingsDTO = subscriptionContractSettingsService.findOne(id);

        if (subscriptionContractSettingsDTO.isPresent()) {
            if (!subscriptionContractSettingsDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionContractSettingsDTO);
    }

    /**
     * {@code DELETE  /subscription-contract-settings/:id} : delete the "id" subscriptionContractSettings.
     *
     * @param id the id of the subscriptionContractSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-contract-settings/{id}")
    public ResponseEntity<Void> deleteSubscriptionContractSettings(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionContractSettings : {}", id);

        Optional<SubscriptionContractSettingsDTO> subscriptionContractSettingsDTO = subscriptionContractSettingsService.findOne(id);

        if (subscriptionContractSettingsDTO.isPresent()) {
            if (!subscriptionContractSettingsDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        subscriptionContractSettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
