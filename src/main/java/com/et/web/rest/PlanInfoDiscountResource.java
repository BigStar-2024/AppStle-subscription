package com.et.web.rest;

import com.et.service.PlanInfoDiscountService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.PlanInfoDiscountDTO;

import com.et.web.rest.vm.DiscountCodeValidationVM;
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
 * REST controller for managing {@link com.et.domain.PlanInfoDiscount}.
 */
@RestController
@RequestMapping("/api")
public class PlanInfoDiscountResource {

    private final Logger log = LoggerFactory.getLogger(PlanInfoDiscountResource.class);

    private static final String ENTITY_NAME = "planInfoDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanInfoDiscountService planInfoDiscountService;

    public PlanInfoDiscountResource(PlanInfoDiscountService planInfoDiscountService) {
        this.planInfoDiscountService = planInfoDiscountService;
    }

    /**
     * {@code POST  /plan-info-discounts} : Create a new planInfoDiscount.
     *
     * @param planInfoDiscountDTO the planInfoDiscountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planInfoDiscountDTO, or with status {@code 400 (Bad Request)} if the planInfoDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plan-info-discounts")
    public ResponseEntity<PlanInfoDiscountDTO> createPlanInfoDiscount(@Valid @RequestBody PlanInfoDiscountDTO planInfoDiscountDTO) throws URISyntaxException {
        log.debug("REST request to save PlanInfoDiscount : {}", planInfoDiscountDTO);
        if (planInfoDiscountDTO.getId() != null) {
            throw new BadRequestAlertException("A new planInfoDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanInfoDiscountDTO result = planInfoDiscountService.save(planInfoDiscountDTO);
        return ResponseEntity.created(new URI("/api/plan-info-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-info-discounts} : Updates an existing planInfoDiscount.
     *
     * @param planInfoDiscountDTO the planInfoDiscountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planInfoDiscountDTO,
     * or with status {@code 400 (Bad Request)} if the planInfoDiscountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planInfoDiscountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plan-info-discounts")
    public ResponseEntity<PlanInfoDiscountDTO> updatePlanInfoDiscount(@Valid @RequestBody PlanInfoDiscountDTO planInfoDiscountDTO) throws URISyntaxException {
        log.debug("REST request to update PlanInfoDiscount : {}", planInfoDiscountDTO);
        if (planInfoDiscountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanInfoDiscountDTO result = planInfoDiscountService.save(planInfoDiscountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planInfoDiscountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plan-info-discounts} : get all the planInfoDiscounts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planInfoDiscounts in body.
     */
    @GetMapping("/plan-info-discounts")
    public ResponseEntity<List<PlanInfoDiscountDTO>> getAllPlanInfoDiscounts(Pageable pageable) {
        log.debug("REST request to get a page of PlanInfoDiscounts");
        Page<PlanInfoDiscountDTO> page = planInfoDiscountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-info-discounts/:id} : get the "id" planInfoDiscount.
     *
     * @param id the id of the planInfoDiscountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planInfoDiscountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plan-info-discounts/{id}")
    public ResponseEntity<PlanInfoDiscountDTO> getPlanInfoDiscount(@PathVariable Long id) {
        log.debug("REST request to get PlanInfoDiscount : {}", id);
        Optional<PlanInfoDiscountDTO> planInfoDiscountDTO = planInfoDiscountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planInfoDiscountDTO);
    }

    /**
     * {@code DELETE  /plan-info-discounts/:id} : delete the "id" planInfoDiscount.
     *
     * @param id the id of the planInfoDiscountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plan-info-discounts/{id}")
    public ResponseEntity<Void> deletePlanInfoDiscount(@PathVariable Long id) {
        log.debug("REST request to delete PlanInfoDiscount : {}", id);
        planInfoDiscountService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/plan-info-discounts/validate")
    public ResponseEntity<DiscountCodeValidationVM> validateDiscountCode(@RequestParam(name = "discountCode", required = true) String discountCode) {
        log.debug("REST request to validate validateDiscountCode : {}", discountCode);

        DiscountCodeValidationVM discountCodeValidationVM = new DiscountCodeValidationVM();
        discountCodeValidationVM.setDiscountCode(discountCode);
        discountCodeValidationVM.setValid(planInfoDiscountService.isValidDiscountCode(discountCode));

        return ResponseEntity.ok(discountCodeValidationVM);
    }
}
