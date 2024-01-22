package com.et.web.rest;

import com.et.service.SubscriptionContractProcessingService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionContractProcessingDTO;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionContractProcessing}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionContractProcessingResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractProcessingResource.class);

    private static final String ENTITY_NAME = "subscriptionContractProcessing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionContractProcessingService subscriptionContractProcessingService;

    public SubscriptionContractProcessingResource(SubscriptionContractProcessingService subscriptionContractProcessingService) {
        this.subscriptionContractProcessingService = subscriptionContractProcessingService;
    }

    /**
     * {@code POST  /subscription-contract-processings} : Create a new subscriptionContractProcessing.
     *
     * @param subscriptionContractProcessingDTO the subscriptionContractProcessingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionContractProcessingDTO, or with status {@code 400 (Bad Request)} if the subscriptionContractProcessing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-contract-processings")
    public ResponseEntity<SubscriptionContractProcessingDTO> createSubscriptionContractProcessing(@RequestBody SubscriptionContractProcessingDTO subscriptionContractProcessingDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionContractProcessing : {}", subscriptionContractProcessingDTO);
        if (subscriptionContractProcessingDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionContractProcessing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionContractProcessingDTO result = subscriptionContractProcessingService.save(subscriptionContractProcessingDTO);
        return ResponseEntity.created(new URI("/api/subscription-contract-processings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-contract-processings} : Updates an existing subscriptionContractProcessing.
     *
     * @param subscriptionContractProcessingDTO the subscriptionContractProcessingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionContractProcessingDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionContractProcessingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionContractProcessingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-contract-processings")
    public ResponseEntity<SubscriptionContractProcessingDTO> updateSubscriptionContractProcessing(@RequestBody SubscriptionContractProcessingDTO subscriptionContractProcessingDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionContractProcessing : {}", subscriptionContractProcessingDTO);
        if (subscriptionContractProcessingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionContractProcessingDTO result = subscriptionContractProcessingService.save(subscriptionContractProcessingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionContractProcessingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-contract-processings} : get all the subscriptionContractProcessings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionContractProcessings in body.
     */
    @GetMapping("/subscription-contract-processings")
    public ResponseEntity<List<SubscriptionContractProcessingDTO>> getAllSubscriptionContractProcessings(Pageable pageable) {
        log.debug("REST request to get a page of SubscriptionContractProcessings");
        Page<SubscriptionContractProcessingDTO> page = subscriptionContractProcessingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-contract-processings/:id} : get the "id" subscriptionContractProcessing.
     *
     * @param id the id of the subscriptionContractProcessingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionContractProcessingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-contract-processings/{id}")
    public ResponseEntity<SubscriptionContractProcessingDTO> getSubscriptionContractProcessing(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionContractProcessing : {}", id);
        Optional<SubscriptionContractProcessingDTO> subscriptionContractProcessingDTO = subscriptionContractProcessingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionContractProcessingDTO);
    }

    /**
     * {@code DELETE  /subscription-contract-processings/:id} : delete the "id" subscriptionContractProcessing.
     *
     * @param id the id of the subscriptionContractProcessingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-contract-processings/{id}")
    public ResponseEntity<Void> deleteSubscriptionContractProcessing(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionContractProcessing : {}", id);
        subscriptionContractProcessingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
