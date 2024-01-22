package com.et.web.rest;

import com.et.api.shopify.recurringcharge.CreateRecurringChargeRequest;
import com.et.api.shopify.shop.Shop;
import com.et.security.SecurityUtils;
import com.et.service.PlanInfoDiscountService;
import com.et.service.PlanInfoService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanInfoDiscountDTO;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.PlanInfo}.
 */
@RestController
@RequestMapping("/api")
public class PlanInfoResource {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(PlanInfoResource.class);
    public static final HashSet<String> TEST_STORE_PLANS = Sets.newHashSet("affiliate", "partner_test", "staff");

    private static final String ENTITY_NAME = "planInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanInfoService planInfoService;

    private final AccountResource accountResource;

    @Value("${oauth.base-uri}")
    private String oauthBaseUri;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private PlanInfoDiscountService planInfoDiscountService;

    public PlanInfoResource(PlanInfoService planInfoService, AccountResource accountResource) {
        this.planInfoService = planInfoService;
        this.accountResource = accountResource;
    }

    /**
     * {@code POST  /plan-infos} : Create a new planInfo.
     *
     * @param planInfoDTO the planInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planInfoDTO, or with status {@code 400 (Bad Request)} if the planInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plan-infos")
    public ResponseEntity<PlanInfoDTO> createPlanInfo(@RequestBody PlanInfoDTO planInfoDTO) throws URISyntaxException {
        log.debug("REST request to save PlanInfo : {}", planInfoDTO);
        if (planInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new planInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String planName = planInfoDTO.getName();
        // Check plan name exists
        if(planInfoService.findByName(planName).isPresent()) {
            throw new BadRequestAlertException("A planInfo with same name already exists", ENTITY_NAME, "nameexists");
        }

        PlanInfoDTO result = planInfoService.save(planInfoDTO);
        return ResponseEntity.created(new URI("/api/plan-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-infos} : Updates an existing planInfo.
     *
     * @param planInfoDTO the planInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planInfoDTO,
     * or with status {@code 400 (Bad Request)} if the planInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plan-infos")
    public ResponseEntity<PlanInfoDTO> updatePlanInfo(@RequestBody PlanInfoDTO planInfoDTO) throws URISyntaxException {
        log.debug("REST request to update PlanInfo : {}", planInfoDTO);
        if (planInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String planName = planInfoDTO.getName();
        // Check plan name exists
        Optional<PlanInfoDTO> existingPlanInfo = planInfoService.findByName(planName);
        if(existingPlanInfo.isPresent() && !existingPlanInfo.get().getId().equals(planInfoDTO.getId())) {
            throw new BadRequestAlertException("A planInfo with same name already exists", ENTITY_NAME, "nameexists");
        }

        PlanInfoDTO result = planInfoService.save(planInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plan-infos} : get all the planInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planInfos in body.
     */


    @GetMapping("/plan-infos")
    public List<PlanInfoDTO> getAllPlanInfos() {
        log.debug("REST request to get all PlanInfos");
        return planInfoService.findAll();
    }

    @GetMapping("/shop-plan-infos")
    public List<PlanInfoDTO> getAllValidPlanInfos(@RequestParam (name = "discountCode", required = false) String discountCode) throws IOException {
        log.debug("REST request to get all PlanInfos");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<PlanInfoDTO> planInfoDTOList = planInfoService.findAll();

        planInfoDTOList = planInfoDTOList.stream().filter(p -> !p.isArchived()).collect(Collectors.toList());

        if (planInfoDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        if(planInfoDiscountService.isValidDiscountCode(discountCode)) {
            PlanInfoDiscountDTO planInfoDiscountDTO = planInfoDiscountService.findByDiscountCode(discountCode.trim()).get();

            planInfoDTOList.forEach(planInfoDTO ->  {
                planInfoDTO.setDiscountPrice(planInfoDiscountService.calculatePlanDiscountPrice(planInfoDTO, planInfoDiscountDTO));
            });
        }

        planInfoDTOList.sort(new OrderByAmount());

        return planInfoDTOList;
    }

    /*
     * Comparator implementation to Sort Order object based on Amount
     */
    public static class OrderByAmount implements Comparator<PlanInfoDTO> {

        @Override
        public int compare(PlanInfoDTO o1, PlanInfoDTO o2) {
            return o1.getPrice().compareTo(o2.getPrice());
        }
    }

    public CreateRecurringChargeRequest buildRecurringCharge(String shop, PlanInfoDTO planInfoDTO, Shop shopInfo) {

        CreateRecurringChargeRequest createRecurringChargeRequest = new CreateRecurringChargeRequest();

        CreateRecurringChargeRequest.RecurringApplicationChargeRequest recurringApplicationCharge = new CreateRecurringChargeRequest.RecurringApplicationChargeRequest();

        if (TEST_STORE_PLANS.contains(shopInfo.getPlanName().toLowerCase())) {
            recurringApplicationCharge.setTest(true);
        } else {
            recurringApplicationCharge.setTest(false);
        }

        recurringApplicationCharge.setName(planInfoDTO.getName());
        recurringApplicationCharge.setPrice(planInfoDTO.getPrice());


        recurringApplicationCharge.setTrial_days(Optional.ofNullable(planInfoDTO.getTrialDays()).orElse(14));


        recurringApplicationCharge.setReturn_url(oauthBaseUri + "/api/activatecharge?shop=" + shop + "&planId=" + planInfoDTO.getId());

        createRecurringChargeRequest.setRecurring_application_charge(recurringApplicationCharge);

        return createRecurringChargeRequest;
    }

    /**
     * {@code GET  /plan-infos/:id} : get the "id" planInfo.
     *
     * @param id the id of the planInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plan-infos/{id}")
    public ResponseEntity<PlanInfoDTO> getPlanInfo(@PathVariable Long id) {
        log.debug("REST request to get PlanInfo : {}", id);
        Optional<PlanInfoDTO> planInfoDTO = planInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planInfoDTO);
    }

    /**
     * {@code DELETE  /plan-infos/:id} : delete the "id" planInfo.
     *
     * @param id the id of the planInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/plan-infos/{id}")
    public ResponseEntity<Void> deletePlanInfo(@PathVariable Long id) {
        log.debug("REST request to delete PlanInfo : {}", id);
        planInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
