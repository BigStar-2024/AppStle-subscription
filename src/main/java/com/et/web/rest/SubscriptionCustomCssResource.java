package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.SubscriptionCustomCssService;
import com.et.utils.CommonUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionCustomCssDTO;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionCustomCss}.
 */
@RestController
@Api(tags = "Subscription Custom CSS Resource")
public class SubscriptionCustomCssResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionCustomCssResource.class);

    private static final String ENTITY_NAME = "subscriptionCustomCss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionCustomCssService subscriptionCustomCssService;

    @Autowired
    private CommonUtils commonUtils;

    public SubscriptionCustomCssResource(SubscriptionCustomCssService subscriptionCustomCssService) {
        this.subscriptionCustomCssService = subscriptionCustomCssService;
    }

    /**
     * {@code POST  /api/subscription-custom-csses} : Create a new subscriptionCustomCss.
     *
     * @param subscriptionCustomCssDTO the subscriptionCustomCssDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionCustomCssDTO, or with status {@code 400 (Bad Request)} if the subscriptionCustomCss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-custom-csses")
    public ResponseEntity<SubscriptionCustomCssDTO> createSubscriptionCustomCss(@Valid @RequestBody SubscriptionCustomCssDTO subscriptionCustomCssDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionCustomCss : {}", subscriptionCustomCssDTO);
        if (subscriptionCustomCssDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionCustomCss cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionCustomCssDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionCustomCssDTO result = subscriptionCustomCssService.save(subscriptionCustomCssDTO);
        return ResponseEntity.created(new URI("/api/subscription-custom-csses/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Custom CSS Created.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /api/subscription-custom-csses} : Updates an existing subscriptionCustomCss.
     *
     * @param subscriptionCustomCssDTO the subscriptionCustomCssDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionCustomCssDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionCustomCssDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionCustomCssDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @PutMapping("/api/subscription-custom-csses")
    public ResponseEntity<SubscriptionCustomCssDTO> updateSubscriptionCustomCss(@Valid @RequestBody SubscriptionCustomCssDTO subscriptionCustomCssDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionCustomCss : {}", subscriptionCustomCssDTO);
        if (subscriptionCustomCssDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriptionCustomCssDTO result = subscriptionCustomCssService.save(subscriptionCustomCssDTO);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionCustomCssDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Custom CSS updated.", subscriptionCustomCssDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /api/subscription-custom-csses} : get all the subscriptionCustomCsses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionCustomCsses in body.
     */
    /*@GetMapping("/api/subscription-custom-csses")
    public List<SubscriptionCustomCssDTO> getAllSubscriptionCustomCsses() {
        log.debug("REST request to get all SubscriptionCustomCsses");
        return subscriptionCustomCssService.findAll();
    }*/

    /**
     * {@code GET  /api/subscription-custom-csses/:id} : get the "id" subscriptionCustomCss.
     *
     * @param id the id of the subscriptionCustomCssDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionCustomCssDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = {"/api/subscription-custom-csses/{id}", "/subscriptions/cp/api/subscription-custom-csses/{id}", "/subscriptions/bb/api/subscription-custom-csses/{id}"})
    @CrossOrigin
    public ResponseEntity<SubscriptionCustomCssDTO> getSubscriptionCustomCss(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get SubscriptionCustomCss : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Optional<SubscriptionCustomCssDTO> subscriptionCustomCssDTO = subscriptionCustomCssService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(subscriptionCustomCssDTO);
    }

    @GetMapping("/api/external/v2/subscription-custom-csses/{id}")
    @CrossOrigin
    @ApiOperation("Get Custom CSS for Customer Portal")
    public ResponseEntity<SubscriptionCustomCssDTO> getSubscriptionCustomCssV2(@ApiParam("Id") @PathVariable Long id, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) {
        log.debug("REST request to get SubscriptionCustomCss : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-custom-csses/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionCustomCssDTO> subscriptionCustomCssDTO = subscriptionCustomCssService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(subscriptionCustomCssDTO);
    }

    /**
     * {@code DELETE  /api/subscription-custom-csses/:id} : delete the "id" subscriptionCustomCss.
     *
     * @param id the id of the subscriptionCustomCssDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-custom-csses/{id}")
    public ResponseEntity<Void> deleteSubscriptionCustomCss(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionCustomCss : {}", id);
        subscriptionCustomCssService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Custom CSS Deleted.", "")).build();
    }
}
