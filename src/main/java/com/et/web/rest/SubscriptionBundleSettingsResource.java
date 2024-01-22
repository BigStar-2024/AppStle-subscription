package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.ShopCustomizationService;
import com.et.service.SubscriptionBundleSettingsService;
import com.et.utils.CommonUtils;
import com.et.utils.ShopLabelUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionBundleSettingsDTO;

import org.apache.commons.lang3.ObjectUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
 * REST controller for managing {@link com.et.domain.SubscriptionBundleSettings}.
 */
@RestController
@Api(tags = "Subscription Bundle Settings Resource")
public class SubscriptionBundleSettingsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleSettingsResource.class);

    private static final String ENTITY_NAME = "subscriptionBundleSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBundleSettingsService subscriptionBundleSettingsService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopLabelUtils shopLabelUtils;

    @Autowired
    ShopCustomizationService shopCustomizationService;

    public SubscriptionBundleSettingsResource(SubscriptionBundleSettingsService subscriptionBundleSettingsService) {
        this.subscriptionBundleSettingsService = subscriptionBundleSettingsService;
    }

    /**
     * {@code POST  /api/subscription-bundle-settings} : Create a new subscriptionBundleSettings.
     *
     * @param subscriptionBundleSettingsDTO the subscriptionBundleSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBundleSettingsDTO, or with status {@code 400 (Bad Request)} if the subscriptionBundleSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/subscription-bundle-settings")
    public ResponseEntity<SubscriptionBundleSettingsDTO> createSubscriptionBundleSettings(@Valid @RequestBody SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionBundleSettings : {}", subscriptionBundleSettingsDTO);
        if (subscriptionBundleSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionBundleSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionBundleSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionBundleSettingsDTO result = subscriptionBundleSettingsService.save(subscriptionBundleSettingsDTO);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.bn.", "BUNDLE", subscriptionBundleSettingsDTO.getAdditionalProperties());

        return ResponseEntity.created(new URI("/api/subscription-bundle-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api/subscription-bundle-settings} : Updates an existing subscriptionBundleSettings.
     *
     * @param subscriptionBundleSettingsDTO the subscriptionBundleSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/subscription-bundle-settings")
    public ResponseEntity<SubscriptionBundleSettingsDTO> updateSubscriptionBundleSettings(@Valid @RequestBody SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionBundleSettings : {}", subscriptionBundleSettingsDTO);
        if (subscriptionBundleSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionBundleSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionBundleSettingsDTO result = subscriptionBundleSettingsService.save(subscriptionBundleSettingsDTO);

        if (ObjectUtils.isNotEmpty(subscriptionBundleSettingsDTO.getShopCustomizationData())) {
            shopCustomizationService.updateShopCustomizationData(subscriptionBundleSettingsDTO.getShopCustomizationData(), shop);
        }


        shopLabelUtils.addNewLabels(shop, "appstle.subscription.bn.", "BUNDLE", subscriptionBundleSettingsDTO.getAdditionalProperties());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Bundle Settings Updated.", ""))
            .body(result);
    }


    /**
     * {@code GET  /api/subscription-bundle-settings/:id} : get the "id" subscriptionBundleSettings.
     *
     * @param id the id of the subscriptionBundleSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBundleSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @CrossOrigin
    @GetMapping(value = {"/api/subscription-bundle-settings/{id}", "/subscriptions/bb/api/subscription-bundle-settings/{id}"})
    public ResponseEntity<SubscriptionBundleSettingsDTO> getSubscriptionBundleSettings(@PathVariable Long id, HttpServletRequest request) {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        Optional<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTO = subscriptionBundleSettingsService.findByShop(shopName);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleSettingsDTO);
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/subscription-bundle-settings/{id}")
    @ApiOperation("Get Subscription Bundle Settings")
    public ResponseEntity<SubscriptionBundleSettingsDTO> getSubscriptionBundleSettingsV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) {
//        log.debug("REST request to get SubscriptionBundleSettings : {}", id);

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/subscription-bundle-settings/{id} api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTO = subscriptionBundleSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleSettingsDTO);
    }

    /**
     * {@code DELETE  /api/subscription-bundle-settings/:id} : delete the "id" subscriptionBundleSettings.
     *
     * @param id the id of the subscriptionBundleSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/subscription-bundle-settings/{id}")
    public ResponseEntity<Void> deleteSubscriptionBundleSettings(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionBundleSettings : {}", id);
        subscriptionBundleSettingsService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
