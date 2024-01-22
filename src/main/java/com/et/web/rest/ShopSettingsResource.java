package com.et.web.rest;

import com.et.domain.enumeration.DelayedTaggingUnit;
import com.et.security.SecurityUtils;
import com.et.service.ShopSettingsService;
import com.et.service.dto.ShopSettingsDTO;
import com.et.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ShopSettings}.
 */
@RestController
@RequestMapping("/api")
public class ShopSettingsResource {

    private final Logger log = LoggerFactory.getLogger(ShopSettingsResource.class);

    private static final String ENTITY_NAME = "shopSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopSettingsService shopSettingsService;

    public ShopSettingsResource(ShopSettingsService shopSettingsService) {
        this.shopSettingsService = shopSettingsService;
    }

    /**
     * {@code POST  /shop-settings} : Create a new shopSettings.
     *
     * @param shopSettingsDTO the shopSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopSettingsDTO, or with status {@code 400 (Bad Request)} if the shopSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-settings")
    public ResponseEntity<ShopSettingsDTO> createShopSettings(@Valid @RequestBody ShopSettingsDTO shopSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save ShopSettings : {}", shopSettingsDTO);
        if (shopSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopSettingsDTO result = shopSettingsService.save(shopSettingsDTO);
        return ResponseEntity.created(new URI("/api/shop-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-settings} : Updates an existing shopSettings.
     *
     * @param shopSettingsDTO the shopSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the shopSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-settings")
    public ResponseEntity<ShopSettingsDTO> updateShopSettings(@Valid @RequestBody ShopSettingsDTO shopSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update ShopSettings : {}", shopSettingsDTO);
        if (shopSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (shopSettingsDTO.isDelayTagging()) {
            if (shopSettingsDTO.getDelayedTaggingValue() == null || shopSettingsDTO.getDelayedTaggingValue() <= 0) {
                throw new BadRequestAlertException("Please provide value for Delayed Tagging", ENTITY_NAME, "idnull");
            }

            if(shopSettingsDTO.getDelayedTaggingUnit() == null) {
                shopSettingsDTO.setDelayedTaggingUnit(DelayedTaggingUnit.SECONDS);
            }
        } else {
            shopSettingsDTO.setDelayedTaggingUnit(null);
            shopSettingsDTO.setDelayedTaggingValue(null);
        }

        ShopSettingsDTO result = shopSettingsService.save(shopSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Shop Settings updated.", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-settings} : get all the shopSettings.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopSettings in body.
     */
    @GetMapping("/shop-settings")
    public List<ShopSettingsDTO> getAllShopSettings() {
        log.debug("REST request to get all ShopSettings");
        return shopSettingsService.findAll();
    }

    /**
     * {@code GET  /shop-settings/:id} : get the "id" shopSettings.
     *
     * @param id the id of the shopSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-settings/{id}")
    public ResponseEntity<ShopSettingsDTO> getShopSettings(@PathVariable Long id) {
        log.debug("REST request to get ShopSettings : {}", id);
        Optional<ShopSettingsDTO> shopSettingsDTO = shopSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopSettingsDTO);
    }

    @GetMapping("/shop-settings/shop")
    public ResponseEntity<ShopSettingsDTO> getShopSettingsByShop() {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopSettingsDTO> shopSettingsDTO = shopSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(shopSettingsDTO);
    }

    /**
     * {@code DELETE  /shop-settings/:id} : delete the "id" shopSettings.
     *
     * @param id the id of the shopSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-settings/{id}")
    public ResponseEntity<Void> deleteShopSettings(@PathVariable Long id) {
        log.debug("REST request to delete ShopSettings : {}", id);
        shopSettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
