package com.et.web.rest;

import com.et.domain.User;
import com.et.domain.enumeration.BulkAutomationType;
import com.et.pojo.ExportActivityLogsRequest;
import com.et.repository.UserRepository;
import com.et.security.SecurityUtils;
import com.et.service.ActivityLogQueryService;
import com.et.service.ActivityLogService;
import com.et.service.BulkAutomationService;
import com.et.service.dto.ActivityLogCriteria;
import com.et.service.dto.ActivityLogDTO;
import com.et.service.dto.BulkAutomationDTO;
import com.et.utils.AwsUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.PageRequest;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ActivityLog}.
 */
@RestController
@RequestMapping("/api")
public class ActivityLogResource {

    private final Logger log = LoggerFactory.getLogger(ActivityLogResource.class);

    private static final String ENTITY_NAME = "activityLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityLogService activityLogService;

    private final ActivityLogQueryService activityLogQueryService;

    private final AwsUtils awsUtils;

    private final BulkAutomationService bulkAutomationService;

    private final UserRepository userRepository;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ActivityLogResource(ActivityLogService activityLogService, ActivityLogQueryService activityLogQueryService, AwsUtils awsUtils, BulkAutomationService bulkAutomationService, UserRepository userRepository) {
        this.activityLogService = activityLogService;
        this.activityLogQueryService = activityLogQueryService;
        this.awsUtils = awsUtils;
        this.bulkAutomationService = bulkAutomationService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /activity-logs} : Create a new activityLog.
     *
     * @param activityLogDTO the activityLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityLogDTO, or with status {@code 400 (Bad Request)} if the activityLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/activity-logs")
    public ResponseEntity<ActivityLogDTO> createActivityLog(@RequestBody ActivityLogDTO activityLogDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityLog : {}", activityLogDTO);
        if (activityLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityLogDTO result = activityLogService.save(activityLogDTO);
        return ResponseEntity.created(new URI("/api/activity-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /activity-logs} : Updates an existing activityLog.
     *
     * @param activityLogDTO the activityLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityLogDTO,
     * or with status {@code 400 (Bad Request)} if the activityLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PutMapping("/activity-logs")
    public ResponseEntity<ActivityLogDTO> updateActivityLog(@RequestBody ActivityLogDTO activityLogDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityLog : {}", activityLogDTO);
        if (activityLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityLogDTO result = activityLogService.save(activityLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activityLogDTO.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code GET  /activity-logs} : get all the activityLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityLogs in body.
     */
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLogDTO>> getAllActivityLogs(ActivityLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ActivityLogs by criteria: {}", criteria);

        Page<ActivityLogDTO> page = activityLogQueryService.filterActivityLogs(SecurityUtils.getCurrentUserLogin().get(), criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @CrossOrigin
    @ApiOperation("Get/Search Activity Logs")
    @GetMapping("/external/v2/activity-logs")
    public ResponseEntity<List<ActivityLogDTO>> getAllActivityLogs(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        @ApiParam("Activity Log Search Criteria") ActivityLogCriteria criteria,
        @ApiParam("Page Info") Pageable pageable) {
        log.debug("REST request to get External ActivityLogs by criteria: {}", criteria);

        Page<ActivityLogDTO> page = activityLogQueryService.filterActivityLogs(SecurityUtils.getCurrentUserLogin().get(), criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /activity-logs/export/all} : export all the activityLogs.
     * activityLogsRequest payload
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityLogs in email.
     */
//    @GetMapping("/activity-logs/export/all")
//    public ResponseEntity<Void> exportActivityLogsToCSV(
//        @RequestParam(value = "email") String email,
//        @RequestParam(value = "pageSize") int pageSize,
//        @RequestParam(value = "pageNumber") int pageNumber
//    ) throws IOException {
//        String shop = SecurityUtils.getCurrentUserLogin().get();
//        ExportActivityLogsRequest activityLogsRequest = new ExportActivityLogsRequest(shop, email, pageSize, pageNumber);
//        activityLogsRequest.setShop(shop);
//
//        log.debug("REST request to export ActivityLogs by criteria: {}", activityLogsRequest);
//
//        Optional<User> user = userRepository.findOneByLogin(shop);
//        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.EXPORT_ACTIVITY_LOGS);
//        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());
//        if (BooleanUtils.isNotTrue(bulkAutomationDTO.isRunning())) {
//            OBJECT_MAPPER.registerModule(new JavaTimeModule());
//            String exportActivityLogsRequestJSON = OBJECT_MAPPER.writeValueAsString(activityLogsRequest);
//
//            awsUtils.startStepExecution(shop, null, AwsUtils.EXPORT_ACTIVITY_LOGS_SM_ARN, exportActivityLogsRequestJSON);
//
//            bulkAutomationDTO.setShop(shop);
//            bulkAutomationDTO.setAutomationType(BulkAutomationType.EXPORT_ACTIVITY_LOGS);
//            bulkAutomationDTO.setRunning(true);
//            bulkAutomationDTO.setStartTime(ZonedDateTime.now());
//            bulkAutomationDTO.setEndTime(null);
//            bulkAutomationDTO.setRequestInfo(OBJECT_MAPPER.writeValueAsString(activityLogsRequest));
//            bulkAutomationDTO.setErrorInfo(null);
//            bulkAutomationService.save(bulkAutomationDTO);
//
//            return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + user.get().getEmail(), "")).build();
//        } else {
//            throw new BadRequestAlertException("An export is already in process for current shop.", applicationName, "");
//        }
//    }

    @GetMapping("/activity-logs/export/all")
    public ResponseEntity<List<ActivityLogDTO>> exportActivityLogsToCSV1(@RequestParam(value = "email") String email,
                                                                         ActivityLogCriteria criteria) throws JsonProcessingException {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = bulkAutomationService.findByShopAndAutomationType(shop, BulkAutomationType.EXPORT_ACTIVITY_LOGS);
        BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.orElse(new BulkAutomationDTO());
        if (BooleanUtils.isNotTrue(bulkAutomationDTO.isRunning())) {

            StringFilter shopFilter = new StringFilter();
            shopFilter.setEquals(SecurityUtils.getCurrentUserLogin().get());
            criteria.setShop(shopFilter);

            int pageSize = 1000;
            int pageNumber = 0;

            ExportActivityLogsRequest activityLogsRequest = new ExportActivityLogsRequest(shop, email, criteria, pageSize, pageNumber);
            activityLogsRequest.setShop(shop);

            log.debug("REST request to export ActivityLogs by criteria: {}", activityLogsRequest);

            Pageable pageable = PageRequest.of(activityLogsRequest.getPageNumber(), activityLogsRequest.getPageSize());

            Page<ActivityLogDTO> page = activityLogQueryService.findByCriteria(criteria, pageable);

            if(page.getTotalElements() > 10000L) {
                throw new BadRequestAlertException("Cannot export records more than 10000","","");
            }

            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            String exportActivityLogsRequestJSON = OBJECT_MAPPER.writeValueAsString(activityLogsRequest);

            awsUtils.startStepExecution(shop, null, AwsUtils.EXPORT_ACTIVITY_LOGS_SM_ARN, exportActivityLogsRequestJSON);

            bulkAutomationDTO.setShop(shop);
            bulkAutomationDTO.setAutomationType(BulkAutomationType.EXPORT_ACTIVITY_LOGS);
            bulkAutomationDTO.setRunning(true);
            bulkAutomationDTO.setStartTime(ZonedDateTime.now());
            bulkAutomationDTO.setEndTime(null);
            bulkAutomationDTO.setRequestInfo(exportActivityLogsRequestJSON);
            bulkAutomationDTO.setErrorInfo(null);
            bulkAutomationService.save(bulkAutomationDTO);

            return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + email, "")).build();
        } else {
            throw new BadRequestAlertException("An export is already in process for current shop.", applicationName, "");
        }

    }

    /**
     * {@code GET  /activity-logs/count} : count all the activityLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/activity-logs/count")
    public ResponseEntity<Long> countActivityLogs(ActivityLogCriteria criteria) {
        log.debug("REST request to count ActivityLogs by criteria: {}", criteria);
        StringFilter shopFilter = new StringFilter();
        shopFilter.setEquals(SecurityUtils.getCurrentUserLogin().get());
        criteria.setShop(shopFilter);
        return ResponseEntity.ok().body(activityLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /activity-logs/:id} : get the "id" activityLog.
     *
     * @param id the id of the activityLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-logs/{id}")
    public ResponseEntity<ActivityLogDTO> getActivityLog(@PathVariable Long id) {
        log.debug("REST request to get ActivityLog : {}", id);
        Optional<ActivityLogDTO> activityLogDTO = activityLogService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if(activityLogDTO.isPresent() && !shop.equals(activityLogDTO.get().getShop())){
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(activityLogDTO);
    }

    /**
     * {@code DELETE  /activity-logs/:id} : delete the "id" activityLog.
     *
     * @param id the id of the activityLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/activity-logs/{id}")
    public ResponseEntity<Void> deleteActivityLog(@PathVariable Long id) {
        log.debug("REST request to delete ActivityLog : {}", id);
        activityLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
