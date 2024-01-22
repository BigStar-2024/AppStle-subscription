package com.et.web.rest;

import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.enumeration.BulkAutomationType;
import com.et.domain.enumeration.DeliveryMethodType;
import com.et.pojo.bulkAutomation.*;
import com.et.security.SecurityUtils;
import com.et.service.BulkAutomationService;
import com.et.service.ShopInfoService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.service.dto.BulkAutomationDTO;
import com.et.service.dto.ShopInfoDTO;
import com.et.service.dto.SubscriptionContractDetailsDTO;
import com.et.utils.AwsUtils;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.type.SellingPlanInterval;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.et.domain.BulkAutomation}.
 */
@RestController
@RequestMapping("/api")
public class BulkAutomationResource {

    private final Logger log = LoggerFactory.getLogger(BulkAutomationResource.class);

    private static final String ENTITY_NAME = "bulkAutomation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BulkAutomationService bulkAutomationService;

    private final SubscriptionContractDetailsService subscriptionContractDetailsService;

    private final AwsUtils awsUtils;

    private final CommonUtils commonUtils;

    private final ObjectMapper objectMapper;

    private final int PAGE_SIZE = 200;

    public BulkAutomationResource(BulkAutomationService bulkAutomationService,
                                  SubscriptionContractDetailsResource subscriptionContractDetailsResource,
                                  SubscriptionContractDetailsService subscriptionContractDetailsService,
                                  MiscellaneousResource miscellaneousResource,
                                  AwsUtils awsUtils, ObjectMapper objectMapper, CommonUtils commonUtils) {
        this.bulkAutomationService = bulkAutomationService;
        this.subscriptionContractDetailsService = subscriptionContractDetailsService;
        this.awsUtils = awsUtils;
        this.objectMapper = objectMapper;
        this.commonUtils = commonUtils;
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * {@code POST  /bulk-automations} : Create a new bulkAutomation.
     *
     * @param bulkAutomationDTO the bulkAutomationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bulkAutomationDTO, or with status {@code 400 (Bad Request)} if the bulkAutomation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bulk-automations")
    public ResponseEntity<BulkAutomationDTO> createBulkAutomation(@Valid @RequestBody BulkAutomationDTO bulkAutomationDTO) throws URISyntaxException {
        log.debug("REST request to save BulkAutomation : {}", bulkAutomationDTO);
        if (bulkAutomationDTO.getId() != null) {
            throw new BadRequestAlertException("A new bulkAutomation cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        bulkAutomationDTO.setShop(shop);

        BulkAutomationDTO result = bulkAutomationService.save(bulkAutomationDTO);
        return ResponseEntity.created(new URI("/api/bulk-automations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bulk-automations} : Updates an existing bulkAutomation.
     *
     * @param bulkAutomationDTO the bulkAutomationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bulkAutomationDTO,
     * or with status {@code 400 (Bad Request)} if the bulkAutomationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bulkAutomationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bulk-automations")
    public ResponseEntity<BulkAutomationDTO> updateBulkAutomation(@Valid @RequestBody BulkAutomationDTO bulkAutomationDTO) throws URISyntaxException {
        log.debug("REST request to update BulkAutomation : {}", bulkAutomationDTO);
        if (bulkAutomationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();
        bulkAutomationDTO.setShop(shop);

        BulkAutomationDTO result = bulkAutomationService.save(bulkAutomationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bulkAutomationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bulk-automations} : get all the bulkAutomations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bulkAutomations in body.
     */
    @GetMapping("/bulk-automations")
    public ResponseEntity<List<BulkAutomationDTO>> getAllBulkAutomations(Pageable pageable) {
        log.debug("REST request to get a page of BulkAutomations");

        String shop = commonUtils.getShop();

        Page<BulkAutomationDTO> page = bulkAutomationService.findAllByShop(shop, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bulk-automations/:id} : get the "id" bulkAutomation.
     *
     * @param id the id of the bulkAutomationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bulkAutomationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bulk-automations/{id}")
    public ResponseEntity<BulkAutomationDTO> getBulkAutomation(@PathVariable Long id) {
        log.debug("REST request to get BulkAutomation : {}", id);
        Optional<BulkAutomationDTO> bulkAutomationDTO = bulkAutomationService.findOne(id);

        String shop = commonUtils.getShop();
        if(bulkAutomationDTO.isPresent() && !bulkAutomationDTO.get().getShop().equals(shop)){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(bulkAutomationDTO);
    }

    /**
     * {@code DELETE  /bulk-automations/:id} : delete the "id" bulkAutomation.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
//    @DeleteMapping("/bulk-automations/{id}")
//    public ResponseEntity<Void> deleteBulkAutomation(@PathVariable Long id) {
//        log.debug("REST request to delete BulkAutomation : {}", id);
//        bulkAutomationService.delete(id);
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
//    }

    @GetMapping("/bulk-automations/download-import-data-file")
    public void downloadImportDataFile(@RequestParam String key, HttpServletResponse response) {
        log.debug("Download import data file: key={}", key);
        byte[] dataBytes = awsUtils.downloadFile(AwsUtils.SUBSCRIPTION_MIGRATION_BUCKET, key);

        if (dataBytes != null) {
            try (InputStream is = new ByteArrayInputStream(dataBytes); OutputStream os = response.getOutputStream()) {
                IOUtils.copy(is, os);
                response.setContentType("text/csv");
                response.setContentLength(dataBytes.length);
                response.setStatus(HttpStatus.OK.value());
            } catch (IOException e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                log.error("Error while writing file to response.", e);
            }
        }
    }

    @PostMapping("/bulk-automations/import-csv")
    public ResponseEntity<Void> importCsvS3(@RequestParam("headerMappingList") String headerMappingJsonString,
                                            @RequestParam("subscriptionDataS3Key") String subscriptionDataS3Key,
                                            @RequestParam(value = "customerDataS3Key", required = false) String customerDataS3Key,
                                            @RequestParam(value = "importType", required = false) String importType) throws IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Start migration for shop: {}", shop);

        MigrationInputRequest migrationInputRequest = new MigrationInputRequest(importType, headerMappingJsonString, false, subscriptionDataS3Key, customerDataS3Key, shop);
        awsUtils.startMigrationExecution(migrationInputRequest);

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Migration started", "")).build();
    }

    @PostMapping("/bulk-automations/import-csv-validation")
    public ResponseEntity<Void> importCsvS3Validation(@RequestParam("headerMappingList") String headerMappingJsonString,
                                                      @RequestParam("subscriptionDataS3Key") String subscriptionDataS3Key,
                                                      @RequestParam(value = "customerDataS3Key", required = false) String customerDataS3Key,
                                                      @RequestParam(value = "importType", required = false) String importType) throws IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        log.info("Start migration validation for shop: {}", shop);

        MigrationInputRequest migrationInputRequest = new MigrationInputRequest(importType, headerMappingJsonString, true, subscriptionDataS3Key, customerDataS3Key, shop);
        awsUtils.startMigrationExecution(migrationInputRequest);

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Migration validation started", "")).build();
    }

    private List<Long> getBulkContractIds(String contractIds, Boolean allSubscriptions) {
        List<Long> contractIdList = new ArrayList<>();
        if (BooleanUtils.isNotTrue(allSubscriptions)) {
            if(StringUtils.isBlank(contractIds)) {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }else {
                contractIdList = Arrays.stream(contractIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
                contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());
                if(contractIdList.size() > PAGE_SIZE){
                    throw new BadRequestAlertException("Cannot process more than " + PAGE_SIZE + " contracts at once", ENTITY_NAME, "idnull");
                }
            }
        }
        return contractIdList;
    }

    private List<Long> getBulkContractIds(String shop, String contractIds, Boolean allSubscriptions) {
        List<Long> contractIdList;
        if (StringUtils.isBlank(contractIds)) {
            if (BooleanUtils.isTrue(allSubscriptions)) {
                contractIdList = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop);
            } else {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }
        } else {
            contractIdList = Arrays.stream(contractIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        // Remove duplicate
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

        return contractIdList;
    }

    private List<Long> getBulkContractIdsByVariantId(String shop, String contractIds, Boolean allSubscriptions, Long variantId) {
        List<Long> contractIdList;
        if (StringUtils.isBlank(contractIds)) {
            if (BooleanUtils.isTrue(allSubscriptions)) {
                List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId));
                contractIdList = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            } else {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }
        } else {
            contractIdList = Arrays.stream(contractIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        // Remove duplicate
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

        return contractIdList;
    }

    @PutMapping("/bulk-automation/stop-automation")
    public ResponseEntity<Void> stopBulkAutomation(@RequestParam("automationType") BulkAutomationType automationType) throws Exception{
        String shop = commonUtils.getShop();

        log.info("Rest Request to Stop bulk automation: {} for shop: {}", automationType, shop);

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, automationType);

        if(bulkAutomationDTOOpt.isEmpty()){
            throw new BadRequestAlertException("No automation found for provided type", ENTITY_NAME, "");
        } else {
            if(BooleanUtils.isTrue(bulkAutomationDTOOpt.get().isRunning())){
                awsUtils.stopStepExecution(bulkAutomationDTOOpt.get().getCurrentExecution());
                bulkAutomationService.stopBulkAutomationProcess(shop, automationType, null);
            }
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk automation stopped", "")).build();
    }

    @PutMapping("/bulk-automations/billing-interval")
    public ResponseEntity<Void> bulkUpdateBillingInterval(@RequestParam("intervalCount") int billingIntervalCount,
                                                          @RequestParam("interval") SellingPlanInterval billingInterval,
                                                          @RequestParam(value = "contractIds", required = false) String contractIds,
                                                          @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("intervalCount", billingIntervalCount);
        processAttributes.put("interval", billingInterval);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_BILLING_INTERVAL,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        Map<String,Object> request = new HashMap<>();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("intervalCount", billingIntervalCount);
        request.put("interval", billingInterval);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, objectMapper.writeValueAsString(request));

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_BILLING_INTERVAL);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @Autowired
    private ShopInfoService shopInfoService;

    @PutMapping("/bulk-automations/delivery-price")
    public ResponseEntity<Void> updateDeliveryPrice(@RequestParam("deliveryPrice") Double deliveryPrice,
                                                    @RequestParam(value = "contractIds", required = false) String contractIds,
                                                    @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        if (!BooleanUtils.isTrue(shopInfoDTO.isDisableShippingPricingAutoCalculation())) {
            throw new BadRequestAlertException("Disable shipping price auto calculation isn't on.", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("deliveryPrice", deliveryPrice);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_DELIVERY_PRICE,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        Map<String,Object> request = new HashMap<>();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("deliveryPrice", deliveryPrice);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_PRICE);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/status")
    public ResponseEntity<Void> updateSubscriptionStatus(@RequestParam("status") String status,
                                                         @RequestParam("importedOnly") boolean importedOnly,
                                                         @RequestParam(value = "contractIds", required = false) String contractIds,
                                                         @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_STATUS);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList;
        if (BooleanUtils.isTrue(importedOnly)) {
            contractIdList = subscriptionContractDetailsService.findSubscriptionContractIdsByShopAndIsImported(shop);
        } else {
            contractIdList = getBulkContractIds(contractIds, allSubscriptions);
        }

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("status", status);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_STATUS,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("importedOnly", importedOnly);
        request.put("status", status);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_STATUS, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_STATUS);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_STATUS, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/delivery-method")
    public ResponseEntity<Void> updateDeliveryMethod(@RequestParam("deliveryMethodName") String deliveryMethodName,
                                                     @RequestParam(value = "contractIds", required = false) String contractIds,
                                                     @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("deliveryMethodName", deliveryMethodName);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_DELIVERY_METHOD,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("deliveryMethodName", deliveryMethodName);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_METHOD);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/hide-subscriptions")
    public ResponseEntity<Void> hideSubscriptions(@RequestParam(value = "contractIds", required = false) String contractIds,
                                                  @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.HIDE_SUBSCRIPTIONS,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.HIDE_SUBSCRIPTIONS);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PostMapping("/external/v2/bulk-automations/hide-subscriptions")
    @CrossOrigin
    @ApiOperation("Subscription Hide Bulk Automation")
    public ResponseEntity<Void> hideSubscriptionsV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                    @ApiParam("Comma Separated Contract Ids") @RequestBody(required = false) String contractIds,
                                                                    @ApiParam("allSubscriptions") @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions,
                                                                    HttpServletRequest httRequest) throws Exception {
        log.debug("REST request Hide Subscriptions");

        commonUtils.restrictV1APIRequestFromCustomerPortal(httRequest);
        String RequestURL = Optional.ofNullable(httRequest).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/bulk-automations/hide-subscriptions api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = new ArrayList<>();
        if (BooleanUtils.isNotTrue(allSubscriptions)) {
            if(StringUtils.isBlank(contractIds)) {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }else {
                contractIdList = Arrays.stream(contractIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
                contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());
            }
        }

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.HIDE_SUBSCRIPTIONS,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.HIDE_SUBSCRIPTIONS);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();

    }

    @PutMapping("/bulk-automations/update-next-billing-date-time")
    public ResponseEntity<Void> updateNextBillingDateTime(@RequestParam("hour") Integer hour,
                                                          @RequestParam("minute") Integer minute,
                                                          @RequestParam("zonedOffsetHours") Integer zonedOffsetHours,
                                                          @RequestParam(value = "contractIds", required = false) String contractIds,
                                                          @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("hour", hour);
        processAttributes.put("minute", minute);
        processAttributes.put("zonedOffsetHours", zonedOffsetHours);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("hour", hour);
        request.put("minute", minute);
        request.put("zonedOffsetHours", zonedOffsetHours);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/update-next-billing-date")
    public ResponseEntity<Void> updateNextBillingDate(@RequestParam("nextBillingDate") ZonedDateTime nextBillingDate,
                                                      @RequestParam(value = "contractIds", required = false) String contractIds,
                                                      @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("nextBillingDate", nextBillingDate.toString());

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_NEXT_BILLING_DATE,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("nextBillingDate", nextBillingDate);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_NEXT_BILLING_DATE);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/update-line-price")
    public ResponseEntity<Void> updateLinePrice(@RequestParam("variantId") Long variantId,
                                                @RequestParam("price") Double price,
                                                @RequestParam(value = "contractIds", required = false) String contractIds,
                                                @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_LINE_PRICE);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("variantId", variantId);
        processAttributes.put("price", price);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_LINE_PRICE,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("variantId", variantId);
        request.put("price", price);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_LINE_PRICE, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_LINE_PRICE);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_LINE_PRICE, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/replace-removed-variants")
    public ResponseEntity<Void> replaceRemovedVariants(@RequestParam("title") String title,
                                                       @RequestParam("newVariantId") Long newVariantId,
                                                       @RequestParam(value = "contractIds", required = false) String contractIds,
                                                       @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("title", title);
        processAttributes.put("newVariantId", newVariantId);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.REPLACE_REMOVED_VARIANT,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("title", title);
        request.put("newVariantId", newVariantId);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.REPLACE_REMOVED_VARIANT);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/replace-product")
    public ResponseEntity<Void> replaceProduct(@RequestParam("newVariantIds") List<Long> newVariantIds,
                                               @RequestParam("oldVariantIds") List<Long> oldVariantIds,
                                               @RequestParam(value = "contractIds", required = false) String contractIds,
                                               @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.REPLACE_PRODUCT);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        if (com.amazonaws.util.CollectionUtils.isNullOrEmpty(oldVariantIds)
            || com.amazonaws.util.CollectionUtils.isNullOrEmpty(newVariantIds)
            || org.apache.commons.collections.CollectionUtils.intersection(oldVariantIds, newVariantIds).size() > 0) {
            throw new BadRequestAlertException("Common Variant Ids found", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("newVariantIds", newVariantIds);
        processAttributes.put("oldVariantIds", oldVariantIds);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.REPLACE_PRODUCT,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("newVariantIds", newVariantIds);
        request.put("oldVariantIds", oldVariantIds);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.REPLACE_PRODUCT, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.REPLACE_PRODUCT);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PostMapping("/external/v2/bulk-automations/replace-product")
    @CrossOrigin
    @ApiOperation("Replace product Bulk Automation")
    public ResponseEntity<Void> replaceProductV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                 @ApiParam("Comma Separated Contract Ids") @RequestBody(required = false) String contractIds,
                                                 @ApiParam("allSubscriptions") @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions,
                                                 @ApiParam("New Variant Ids") @RequestParam("newVariantIds") List<Long> newVariantIds,
                                                 @ApiParam("Old Variant Ids") @RequestParam("oldVariantIds") List<Long> oldVariantIds,
                                                 HttpServletRequest httRequest) throws Exception {
        log.debug("REST request Replace products");

        commonUtils.restrictV1APIRequestFromCustomerPortal(httRequest);
        String RequestURL = Optional.ofNullable(httRequest).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/bulk-automations/replace-product api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.REPLACE_PRODUCT);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = new ArrayList<>();
        if (BooleanUtils.isNotTrue(allSubscriptions)) {
            if(StringUtils.isBlank(contractIds)) {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }else {
                contractIdList = Arrays.stream(contractIds.split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
                contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());
            }
        }

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("newVariantIds", newVariantIds);
        processAttributes.put("oldVariantIds", oldVariantIds);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.REPLACE_PRODUCT,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("newVariantIds", newVariantIds);
        request.put("oldVariantIds", oldVariantIds);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.REPLACE_PRODUCT, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.REPLACE_PRODUCT);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();

    }

    @PutMapping("/bulk-automations/delete-product")
    public ResponseEntity<Void> deleteRemovedProduct(@RequestParam(value = "variantId", required = false) Long variantId,
                                                     @RequestParam(value = "deleteRemovedProductsFromShopify", required = false) Boolean deleteRemovedProductsFromShopify,
                                                     @RequestParam(value = "contractIds", required = false) String contractIds,
                                                     @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("variantId", variantId);
        processAttributes.put("deleteRemovedProductsFromShopify", deleteRemovedProductsFromShopify);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.DELETE_REMOVED_PRODUCT,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("variantId", variantId);
        request.put("deleteRemovedProductsFromShopify", deleteRemovedProductsFromShopify);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.DELETE_REMOVED_PRODUCT);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/update-min-max-cycles")
    public ResponseEntity<Void> updateMinMaxCycles(@RequestParam(value = "minCycles", required = false) Integer minCycles,
                                                   @RequestParam(value = "maxCycles", required = false) Integer maxCycles,
                                                   @RequestParam(value = "updateMinCycles") Boolean updateMinCycles,
                                                   @RequestParam(value = "updateMaxCycles") Boolean updateMaxCycles,
                                                   @RequestParam(value = "contractIds", required = false) String contractIds,
                                                   @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(shop, contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("minCycles", minCycles);
        processAttributes.put("maxCycles", maxCycles);
        processAttributes.put("updateMinCycles", updateMinCycles);
        processAttributes.put("updateMaxCycles", updateMaxCycles);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_MIN_MAX_CYCLES,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("minCycles", minCycles);
        request.put("maxCycles", maxCycles);
        request.put("updateMinCycles", updateMinCycles);
        request.put("updateMaxCycles", updateMaxCycles);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_MIN_MAX_CYCLES);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/add-remove-discount-code")
    public ResponseEntity<Void> addRemoveDiscountCode(@RequestParam("discountCode") String discountCode,
                                                      @RequestParam("addDiscount") Boolean addDiscount,
                                                      @RequestParam(value = "contractIds", required = false) String contractIds,
                                                      @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        if(StringUtils.isBlank(discountCode)){
            throw new BadRequestAlertException("Discount code cannot be empty", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("discountCode", discountCode);
        processAttributes.put("addDiscount", addDiscount);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("discountCode", discountCode);
        request.put("addDiscount", addDiscount);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        } catch (Exception e) {
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.ADD_REMOVE_DISCOUNT_CODE, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/update-delivery-method-type")
    public ResponseEntity<Void> updateDeliveryMethodType(@RequestParam("fromDeliveryType") DeliveryMethodType fromDeliveryType,
                                                         @RequestParam("toDeliveryType") DeliveryMethodType toDeliveryType,
                                                         @RequestParam(value = "contractIds", required = false) String contractIds,
                                                         @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        if(fromDeliveryType.equals(toDeliveryType)){
            throw new BadRequestAlertException("From delivery type cannot be same as To delivery type", ENTITY_NAME, "");
        }

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("fromDeliveryType", fromDeliveryType);
        processAttributes.put("toDeliveryType", toDeliveryType);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        request.put("fromDeliveryType", fromDeliveryType);
        request.put("toDeliveryType", toDeliveryType);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        }catch (Exception e){
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD_TYPE, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

    @PutMapping("/bulk-automations/add-product")
    public ResponseEntity<Void> addProduct(@RequestParam(required = false) Double price,
                                           @RequestParam("variantId") Long variantId,
                                           @RequestParam(value = "contractIds", required = false) String contractIds,
                                           @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions,
                                           @RequestParam("productType") String productType) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        List<Long> contractIdList = getBulkContractIds(contractIds, allSubscriptions);

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.ADD_PRODUCT);
        if (bulkAutomationDTOOpt.isPresent() && bulkAutomationDTOOpt.get().isRunning()) {
            throw new BadRequestAlertException("Bulk request for current shop is already in progress", ENTITY_NAME, "");
        }

        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());

        Map<String, Object> processAttributes = new HashMap<>();
        processAttributes.put("variantId", variantId);
        processAttributes.put("price", price);
        processAttributes.put("productType", productType);

        BulkAutomationActivityRequest bulkAutomationActivityRequest = new BulkAutomationActivityRequest(
            shop,
            BulkAutomationType.ADD_PRODUCT,
            contractIdList,
            allSubscriptions,
            PAGE_SIZE,
            0,
            processAttributes
        );

        JSONObject request = new JSONObject();
        request.put("contractIds", contractIdList);
        request.put("allSubscriptions", allSubscriptions);
        processAttributes.put("variantId", variantId);
        processAttributes.put("price", price);
        processAttributes.put("productType", productType);

        bulkAutomationDTO = bulkAutomationService.startBulkAutomationProcess(bulkAutomationDTO, shop, BulkAutomationType.ADD_PRODUCT, request.toString());

        try {
            StartExecutionResult startExecutionResult = awsUtils.startStepExecution(shop, bulkAutomationActivityRequest, AwsUtils.BULK_AUTOMATION_SM_ARN, BulkAutomationType.ADD_PRODUCT);
            bulkAutomationDTO.setCurrentExecution(startExecutionResult.getExecutionArn());
            bulkAutomationService.save(bulkAutomationDTO);
        } catch (Exception e) {
            bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.ADD_PRODUCT, e.getMessage());
            log.error("An error occurred while starting Step execution, error: {}", e.getMessage());
            throw new RuntimeException("An error occurred while starting step execution");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bulk request added to queue", "")).build();
    }

}
