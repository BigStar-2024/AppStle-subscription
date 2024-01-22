package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.ShopCustomizationService;
import com.et.service.SubscriptionWidgetSettingsService;
import com.et.utils.ShopLabelUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SubscriptionWidgetSettingsDTO;

import org.apache.commons.lang3.ObjectUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SubscriptionWidgetSettings}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionWidgetSettingsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionWidgetSettingsResource.class);

    private static final String ENTITY_NAME = "subscriptionWidgetSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ShopLabelUtils shopLabelUtils;

    private final SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    public SubscriptionWidgetSettingsResource(SubscriptionWidgetSettingsService subscriptionWidgetSettingsService) {
        this.subscriptionWidgetSettingsService = subscriptionWidgetSettingsService;
    }

    /**
     * {@code POST  /subscription-widget-settings} : Create a new subscriptionWidgetSettings.
     *
     * @param subscriptionWidgetSettingsDTO the subscriptionWidgetSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionWidgetSettingsDTO, or with status {@code 400 (Bad Request)} if the subscriptionWidgetSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-widget-settings")
    public ResponseEntity<SubscriptionWidgetSettingsDTO> createSubscriptionWidgetSettings(@Valid @RequestBody SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save SubscriptionWidgetSettings : {}", subscriptionWidgetSettingsDTO);
        if (subscriptionWidgetSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionWidgetSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionWidgetSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        SubscriptionWidgetSettingsDTO result = subscriptionWidgetSettingsService.save(subscriptionWidgetSettingsDTO);
        result.getAdditionalProperties().putAll(subscriptionWidgetSettingsDTO.getAdditionalProperties());

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.wg.", "WIDGET", subscriptionWidgetSettingsDTO.getAdditionalProperties());

        return ResponseEntity.created(new URI("/api/subscription-widget-settings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Widget Settings Created.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-widget-settings} : Updates an existing subscriptionWidgetSettings.
     *
     * @param subscriptionWidgetSettingsDTO the subscriptionWidgetSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionWidgetSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionWidgetSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionWidgetSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private ShopCustomizationService shopCustomizationService;

    @PutMapping("/subscription-widget-settings")
    public ResponseEntity<SubscriptionWidgetSettingsDTO> updateSubscriptionWidgetSettings(@Valid @RequestBody SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update SubscriptionWidgetSettings : {}", subscriptionWidgetSettingsDTO);
        if (subscriptionWidgetSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(subscriptionWidgetSettingsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (ObjectUtils.isNotEmpty(subscriptionWidgetSettingsDTO.getShopCustomizationData())) {
            shopCustomizationService.updateShopCustomizationData(subscriptionWidgetSettingsDTO.getShopCustomizationData(), shop);
        }

        SubscriptionWidgetSettingsDTO result = subscriptionWidgetSettingsService.save(subscriptionWidgetSettingsDTO);
        result.getAdditionalProperties().putAll(subscriptionWidgetSettingsDTO.getAdditionalProperties());

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.wg.", "WIDGET", subscriptionWidgetSettingsDTO.getAdditionalProperties());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Widget Settings Updated.", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscription-widget-settings} : get all the subscriptionWidgetSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionWidgetSettings in body.
     */
    @GetMapping("/subscription-widget-settings")
    public List<SubscriptionWidgetSettingsDTO> getAllSubscriptionWidgetSettings() {
        log.debug("REST request to get all SubscriptionWidgetSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTOOptional = subscriptionWidgetSettingsService.findByShop(shop);
        ArrayList<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTOS = new ArrayList<>();
        subscriptionWidgetSettingsDTOOptional.ifPresent(subscriptionWidgetSettingsDTOS::add);
        return subscriptionWidgetSettingsDTOS;
    }

    /**
     * {@code GET  /subscription-widget-settings/:id} : get the "id" subscriptionWidgetSettings.
     *
     * @param id the id of the subscriptionWidgetSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionWidgetSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-widget-settings/{id}")
    public ResponseEntity<SubscriptionWidgetSettingsDTO> getSubscriptionWidgetSettings(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionWidgetSettings : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(subscriptionWidgetSettingsDTO);
    }

    /**
     * {@code DELETE  /subscription-widget-settings/:id} : delete the "id" subscriptionWidgetSettings.
     *
     * @param id the id of the subscriptionWidgetSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-widget-settings/{id}")
    public ResponseEntity<Void> deleteSubscriptionWidgetSettings(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionWidgetSettings : {}", id);
        subscriptionWidgetSettingsService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Widget Settings Deleted.",""))
            .build();
    }
}
