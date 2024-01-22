package com.et.web.rest;

import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.enumeration.*;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.security.SecurityUtils;
import com.et.service.SubscriptionContractOneOffService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionContractOneOffDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionContractOneOff}.
 */
@RestController
@Api(tags = "Subscription Contract One Off Resource")
public class SubscriptionContractOneOffResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractOneOffResource.class);

    private static final String ENTITY_NAME = "subscriptionContractOneOff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionContractOneOffService subscriptionContractOneOffService;

    @Autowired
    private CommonUtils commonUtils;

    public SubscriptionContractOneOffResource(SubscriptionContractOneOffService subscriptionContractOneOffService) {
        this.subscriptionContractOneOffService = subscriptionContractOneOffService;
    }

    /**
     * {@code POST  /api/subscription-contract-one-offs} : Create a new subscriptionContractOneOff.
     *
     * @param subscriptionContractOneOffDTO the subscriptionContractOneOffDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionContractOneOffDTO, or with status {@code 400 (Bad Request)} if the subscriptionContractOneOff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-contract-one-offs")
    public ResponseEntity<SubscriptionContractOneOffDTO> createSubscriptionContractOneOff(@Valid @RequestBody SubscriptionContractOneOffDTO subscriptionContractOneOffDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionContractOneOff : {}", subscriptionContractOneOffDTO);
        if (subscriptionContractOneOffDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionContractOneOff cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionContractOneOffDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractOneOffDTO result = subscriptionContractOneOffService.save(subscriptionContractOneOffDTO);
        return ResponseEntity.created(new URI("/api/subscription-contract-one-offs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api/subscription-contract-one-offs} : Updates an existing subscriptionContractOneOff.
     *
     * @param subscriptionContractOneOffDTO the subscriptionContractOneOffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionContractOneOffDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionContractOneOffDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionContractOneOffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/subscription-contract-one-offs")
    public ResponseEntity<SubscriptionContractOneOffDTO> updateSubscriptionContractOneOff(@Valid @RequestBody SubscriptionContractOneOffDTO subscriptionContractOneOffDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionContractOneOff : {}", subscriptionContractOneOffDTO);
        if (subscriptionContractOneOffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionContractOneOffDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionContractOneOffDTO result = subscriptionContractOneOffService.save(subscriptionContractOneOffDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptionContractOneOffDTO.getId().toString()))
            .body(result);
    }

    @GetMapping(value = {"/api/subscription-contract-one-offs-by-contractId", "/subscriptions/cp/api/subscription-contract-one-offs-by-contractId"})
    @CrossOrigin
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> getOneOffsForSubscriptionContract(@RequestParam("contractId") Long contractId, HttpServletRequest request) {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shopName, contractId);
        return ResponseEntity.ok().body(subscriptionContractsList);
    }

    @GetMapping("/api/external/v2/subscription-contract-one-offs-by-contractId")
    @CrossOrigin
    @ApiOperation("Get One-time Products for Subscription Contract")
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> getOneOffsForSubscriptionContractV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, @ApiParam("Contract ID") @RequestParam("contractId") Long contractId, HttpServletRequest request) {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contract-one-offs-by-contractId api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);
        return ResponseEntity.ok().body(subscriptionContractsList);
    }

    @GetMapping(value = {"/api/upcoming-subscription-contract-one-offs-by-contractId", "/subscriptions/cp/api/upcoming-subscription-contract-one-offs-by-contractId"})
    @CrossOrigin
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> getOneOffForContractNextOrder(@RequestParam("contractId") Long contractId, HttpServletRequest request) {
        String shop = commonUtils.getShop();
        List<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOList = subscriptionContractOneOffService.getOneOffForContractNextOrder(shop, contractId);
        return ResponseEntity.ok().body(subscriptionContractOneOffDTOList);
    }

    @GetMapping("/api/external/v2/upcoming-subscription-contract-one-offs-by-contractId")
    @CrossOrigin
    @ApiOperation("Get One-time Products for Subscription Contract")
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> getOneOffForContractNextOrderExternal(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, @ApiParam("Contract ID") @RequestParam("contractId") Long contractId, HttpServletRequest request) {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/upcoming-subscription-contract-one-offs-by-contractId api_key: {}", RequestURL, apiKey);

        String shop = commonUtils.getShop();

        List<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOList = subscriptionContractOneOffService.getOneOffForContractNextOrder(shop, contractId);
        return ResponseEntity.ok().body(subscriptionContractOneOffDTOList);
    }


    @PutMapping(value = {"/api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id", "/subscriptions/cp/api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id"})
    @CrossOrigin
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> saveOneOffBy(
        @RequestParam("contractId") Long contractId,
        @RequestParam("billingAttemptId") Long billingAttemptId,
        @RequestParam("variantId") Long variantId,
        @RequestParam("variantHandle") String variantHandle,
        @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
        HttpServletRequest request) throws JsonProcessingException {

        boolean isExternal = commonUtils.isExternal();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
        }
        return subscriptionContractSaveOneOff(shopName, contractId, billingAttemptId, variantId, variantHandle, quantity, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @PutMapping("/api/external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id")
    @CrossOrigin
    @ApiOperation("Add One-time Product from Customer Portal")
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> saveOneOffByV2(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Billing Attempt ID") @RequestParam("billingAttemptId") Long billingAttemptId,
        @ApiParam("Variant ID") @RequestParam("variantId") Long variantId,
        @ApiParam("Variant Handle") @RequestParam("variantHandle") String variantHandle,
        @ApiParam("Variant Quantity") @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
        HttpServletRequest request) throws JsonProcessingException {
        log.debug("REST request to get a page of SubscriptionContractOneOffs");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shop);
        return subscriptionContractSaveOneOff(shop, contractId, billingAttemptId, variantId, variantHandle, quantity, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
    }

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @NotNull
    private ResponseEntity<List<SubscriptionContractOneOffDTO>> subscriptionContractSaveOneOff(String shop, Long contractId, Long billingAttemptId, Long variantId, String variantHandle, Integer quantity, ActivityLogEventSource eventSource) throws JsonProcessingException {
        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);

        Optional<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOOptional = subscriptionContractsList.stream().filter(sc -> sc.getBillingAttemptId().equals(billingAttemptId) && sc.getVariantId().equals(variantId)).findFirst();

        if (subscriptionContractOneOffDTOOptional.isPresent()) {
            return ResponseEntity.ok().body(subscriptionContractsList);
        }

        List<SubscriptionBillingAttempt> queuedBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, contractId, BillingAttemptStatus.QUEUED);
        Optional<SubscriptionBillingAttempt> firstBillingAttemptOptional = queuedBillingAttempts.stream().sorted(Comparator.comparing(SubscriptionBillingAttempt::getBillingDate)).findFirst();

        if (firstBillingAttemptOptional.isEmpty()) {
            throw new BadRequestAlertException("There is no upcoming order scheduled.", "data", "idnull");
        }

        Long finalBillingAttemptId = firstBillingAttemptOptional.get().getId();

        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = new SubscriptionContractOneOffDTO();
        subscriptionContractOneOffDTO.setShop(shop);
        subscriptionContractOneOffDTO.setBillingAttemptId(finalBillingAttemptId);
        subscriptionContractOneOffDTO.setSubscriptionContractId(contractId);
        subscriptionContractOneOffDTO.setVariantId(variantId);
        subscriptionContractOneOffDTO.setVariantHandle(variantHandle);
        subscriptionContractOneOffDTO.setQuantity(quantity);

        Map<String, Object> activityLogMap = new HashMap<>();
        activityLogMap.put("variantId", variantId);
        activityLogMap.put("variantHandle", variantHandle);
        activityLogMap.put("quantity", quantity);

        subscriptionContractOneOffService.save(subscriptionContractOneOffDTO);

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_ADDED, ActivityLogStatus.SUCCESS, activityLogMap);

        subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);
        return ResponseEntity.ok().body(subscriptionContractsList);
    }

    @PutMapping(value = {"/api/subscription-contract-one-offs-update-quantity", "/subscriptions/cp/api/subscription-contract-one-offs-update-quantity"})
    @CrossOrigin
    public ResponseEntity<SubscriptionContractOneOffDTO> updateOneOffQuantity(
        @RequestParam("contractId") Long contractId,
        @RequestParam("billingAttemptId") Long billingAttemptId,
        @RequestParam("variantId") Long variantId,
        @RequestParam("quantity") Integer quantity,
        HttpServletRequest request) throws JsonProcessingException {

        boolean isExternal = commonUtils.isExternal();

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        if (isExternal) {
            commonUtils.checkIsFreezeOrderTillMinCycleCondition(contractId, shopName);
        }
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffService.subscriptionContractUpdateOneOffQuantity(shopName, contractId, billingAttemptId, variantId, quantity, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        return ResponseEntity.ok(subscriptionContractOneOffDTO);
    }

    /**
     * {@code GET  /api/subscription-contract-one-offs/:id} : get the "id" subscriptionContractOneOff.
     *
     * @param id the id of the subscriptionContractOneOffDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionContractOneOffDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/subscription-contract-one-offs/{id}")
    public ResponseEntity<SubscriptionContractOneOffDTO> getSubscriptionContractOneOff(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionContractOneOff : {}", id);
        Optional<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTO = subscriptionContractOneOffService.findOne(id);

        if (subscriptionContractOneOffDTO.isPresent()) {
            if (!subscriptionContractOneOffDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        return ResponseUtil.wrapOrNotFound(subscriptionContractOneOffDTO);
    }

    /**
     * {@code DELETE  /api/subscription-contract-one-offs/:id} : delete the "id" subscriptionContractOneOff.
     *
     * @param id the id of the subscriptionContractOneOffDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-contract-one-offs/{id}")
    public ResponseEntity<Void> deleteSubscriptionContractOneOff(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionContractOneOff : {}", id);

        Optional<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTO = subscriptionContractOneOffService.findOne(id);

        if (subscriptionContractOneOffDTO.isPresent()) {
            if (!subscriptionContractOneOffDTO.get().getShop().equals(SecurityUtils.getCurrentUserLogin().get())) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        subscriptionContractOneOffService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping(value = {"/api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id", "/subscriptions/cp/api/subscription-contract-one-offs-by-contractId-and-billing-attempt-id"})
    @CrossOrigin
    @ApiOperation("Delete One-time Product from Customer Portal")
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> deleteOneOff(
        @RequestParam("contractId") Long contractId,
        @RequestParam("billingAttemptId") Long billingAttemptId,
        @RequestParam("variantId") Long variantId,
        HttpServletRequest request) throws JsonProcessingException {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        deleteOneOffCommon(shopName, contractId, billingAttemptId, variantId, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shopName, contractId);
        return ResponseEntity.ok().body(subscriptionContractsList);
    }

    private void deleteOneOffCommon(String shopName, Long contractId, Long billingAttemptId, Long variantId, ActivityLogEventSource eventSource) throws JsonProcessingException {
        subscriptionContractOneOffService.deleteByShopAndContractIdAndBillingAttemptIdAndVariantId(shopName, contractId, billingAttemptId, variantId);

        Map<String, Object> activityLogMap = new HashMap<>();
        activityLogMap.put("billingAttemptId", billingAttemptId);
        activityLogMap.put("variantId", variantId);

        commonUtils.writeActivityLog(shopName, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_REMOVED, ActivityLogStatus.SUCCESS, activityLogMap);
    }

    @DeleteMapping("/api/external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id")
    @CrossOrigin
    @ApiOperation("Delete One-time Product from Customer Portal")
    public ResponseEntity<List<SubscriptionContractOneOffDTO>> deleteOneOffByV2(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Contract ID") @RequestParam("contractId") Long contractId,
        @ApiParam("Billing Attempt ID") @RequestParam("billingAttemptId") Long billingAttemptId,
        @ApiParam("Variant ID") @RequestParam("variantId") Long variantId,
        HttpServletRequest request) throws JsonProcessingException {
        log.debug("REST request to get a page of SubscriptionContractOneOffs");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        deleteOneOffCommon(shop, contractId, billingAttemptId, variantId, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);
        return ResponseEntity.ok().body(subscriptionContractsList);
    }
}
