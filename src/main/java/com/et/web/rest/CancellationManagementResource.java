package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.CancellationManagementService;
import com.et.service.dto.CancellationManagementDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.CancellationManagement}.
 */
@RestController
@Api(tags = {"Cancellation Management Resource"})
public class CancellationManagementResource {

    private final Logger log = LoggerFactory.getLogger(CancellationManagementResource.class);

    private static final String ENTITY_NAME = "cancellationManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CancellationManagementService cancellationManagementService;

    @Autowired
    private CommonUtils commonUtils;

    public CancellationManagementResource(CancellationManagementService cancellationManagementService) {
        this.cancellationManagementService = cancellationManagementService;
    }

    /**
     * {@code POST  /api/cancellation-managements} : Create a new cancellationManagement.
     *
     * @param cancellationManagementDTO the cancellationManagementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cancellationManagementDTO, or with status {@code 400 (Bad Request)} if the cancellationManagement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/api/cancellation-managements")
    public ResponseEntity<CancellationManagementDTO> createCancellationManagement(@Valid @RequestBody CancellationManagementDTO cancellationManagementDTO) throws URISyntaxException {
        log.debug("REST request to save CancellationManagement : {}", cancellationManagementDTO);
        if (cancellationManagementDTO.getId() != null) {
            throw new BadRequestAlertException("A new cancellationManagement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CancellationManagementDTO result = cancellationManagementService.save(cancellationManagementDTO);
        return ResponseEntity.created(new URI("/api/cancellation-managements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /api/cancellation-managements} : Updates an existing cancellationManagement.
     *
     * @param cancellationManagementDTO the cancellationManagementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cancellationManagementDTO,
     * or with status {@code 400 (Bad Request)} if the cancellationManagementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cancellationManagementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/cancellation-managements")
    public ResponseEntity<CancellationManagementDTO> updateCancellationManagement(@Valid @RequestBody CancellationManagementDTO cancellationManagementDTO) throws URISyntaxException {
        log.debug("REST request to update CancellationManagement : {}", cancellationManagementDTO);
        if (cancellationManagementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(cancellationManagementDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CancellationManagementDTO result = cancellationManagementService.save(cancellationManagementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Cancellation Management Updated.", ""))
            .body(result);
    }


    /**
     * {@code GET /api/cancellation-managements/:id} : get the "id" cancellationManagement.
     *
     * @param id the id of the cancellationManagementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cancellationManagementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = {"/api/cancellation-managements/{id}", "/subscriptions/cp/api/cancellation-managements/{id}"})
    @CrossOrigin
    public ResponseEntity<CancellationManagementDTO> getCancellationManagement( @PathVariable Long id, HttpServletRequest request) {

            String shopName = SecurityUtils.getCurrentUserLogin().get();

        Optional<CancellationManagementDTO> cancellationManagementDTO = cancellationManagementService.findByShop(shopName);
        return ResponseUtil.wrapOrNotFound(cancellationManagementDTO);
    }

    @GetMapping("/api/external/v2/cancellation-managements/{id}")
    @CrossOrigin
    @ApiOperation("Returns Cancellation Management configuration")
    public ResponseEntity<CancellationManagementDTO> getCancellationManagementV2(
        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
        HttpServletRequest request
    ) {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/cancellation-managements/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<CancellationManagementDTO> cancellationManagementDTO = cancellationManagementService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(cancellationManagementDTO);
    }

    /**
     * {@code DELETE  /api/cancellation-managements/:id} : delete the "id" cancellationManagement.
     *
     * @param id the id of the cancellationManagementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/cancellation-managements/{id}")
    public ResponseEntity<Void> deleteCancellationManagement(@PathVariable Long id) {
        log.debug("REST request to delete CancellationManagement : {}", id);
        cancellationManagementService.deleteByShop(commonUtils.getShop());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
