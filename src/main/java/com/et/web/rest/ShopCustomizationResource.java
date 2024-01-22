package com.et.web.rest;

import com.et.domain.enumeration.CustomizationCategory;
import com.et.pojo.ShopCustomizationInfo;
import com.et.security.SecurityUtils;
import com.et.service.ShopCustomizationService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.ShopCustomizationDTO;

import tech.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

/**
 * REST controller for managing {@link com.et.domain.ShopCustomization}.
 */
@RestController
@RequestMapping
public class ShopCustomizationResource {

    private final Logger log = LoggerFactory.getLogger(ShopCustomizationResource.class);

    private static final String ENTITY_NAME = "shopCustomization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopCustomizationService shopCustomizationService;

    public ShopCustomizationResource(ShopCustomizationService shopCustomizationService) {
        this.shopCustomizationService = shopCustomizationService;
    }

    /**
     * {@code POST  /shop-customizations} : Create a new shopCustomization.
     *
     * @param shopCustomizationDTO the shopCustomizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopCustomizationDTO, or with status {@code 400 (Bad Request)} if the shopCustomization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/shop-customizations")
    public ResponseEntity<ShopCustomizationDTO> createShopCustomization(@RequestBody ShopCustomizationDTO shopCustomizationDTO) throws URISyntaxException {
        log.debug("REST request to save ShopCustomization : {}", shopCustomizationDTO);
        if (shopCustomizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopCustomization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopCustomizationDTO result = shopCustomizationService.save(shopCustomizationDTO);
        return ResponseEntity.created(new URI("/api/shop-customizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-customizations} : Updates an existing shopCustomization.
     *
     * @param shopCustomizationDTO the shopCustomizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopCustomizationDTO,
     * or with status {@code 400 (Bad Request)} if the shopCustomizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopCustomizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/shop-customizations")
    public ResponseEntity<ShopCustomizationDTO> updateShopCustomization(@RequestBody ShopCustomizationDTO shopCustomizationDTO) throws URISyntaxException {
        log.debug("REST request to update ShopCustomization : {}", shopCustomizationDTO);
        if (shopCustomizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopCustomizationDTO result = shopCustomizationService.save(shopCustomizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopCustomizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-customizations} : get all the shopCustomizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopCustomizations in body.
     */
    @GetMapping("/api/shop-customizations")
    public List<ShopCustomizationDTO> getAllShopCustomizations() {
        log.debug("REST request to get all ShopCustomizations");
        return shopCustomizationService.findAll();
    }

    /**
     * {@code GET  /shop-customizations/:id} : get the "id" shopCustomization.
     *
     * @param id the id of the shopCustomizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopCustomizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/shop-customizations/{id}")
    public List<ShopCustomizationDTO> getShopCustomization(@PathVariable Long id) {
        log.debug("REST request to get ShopCustomization : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<ShopCustomizationDTO> shopCustomizationDTO = shopCustomizationService.findByShop(shop);
        return shopCustomizationDTO;
    }

    /**
     * {@code DELETE  /shop-customizations/:id} : delete the "id" shopCustomization.
     *
     * @param id the id of the shopCustomizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/shop-customizations/{id}")
    public ResponseEntity<Void> deleteShopCustomization(@PathVariable Long id) {
        log.debug("REST request to delete ShopCustomization : {}", id);
        shopCustomizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(value = {"/api/shop-customizations/css/{category}", "/subscriptions/cp/api/shop-customizations/css/{category}", "/subscriptions/bb/api/shop-customizations/css/{category}"})
    public List<String> getShopCustomizationCss(@PathVariable CustomizationCategory category) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return shopCustomizationService.getShopCustomizationCss(shop, category);
    }

    @GetMapping("/api/shop-customizations/category/{category}")
    public List<ShopCustomizationInfo> getShopCustomizationByCategory(@PathVariable CustomizationCategory category) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return shopCustomizationService.getShopCustomizationInfo(shop, category);
    }
}
