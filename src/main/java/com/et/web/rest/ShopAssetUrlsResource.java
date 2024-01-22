package com.et.web.rest;

import com.et.service.ShopAssetUrlsService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.ShopAssetUrlsDTO;

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
 * REST controller for managing {@link com.et.domain.ShopAssetUrls}.
 */
@RestController
@RequestMapping("/api")
public class ShopAssetUrlsResource {

    private final Logger log = LoggerFactory.getLogger(ShopAssetUrlsResource.class);

    private static final String ENTITY_NAME = "shopAssetUrls";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopAssetUrlsService shopAssetUrlsService;

    public ShopAssetUrlsResource(ShopAssetUrlsService shopAssetUrlsService) {
        this.shopAssetUrlsService = shopAssetUrlsService;
    }

    /**
     * {@code POST  /shop-asset-urls} : Create a new shopAssetUrls.
     *
     * @param shopAssetUrlsDTO the shopAssetUrlsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopAssetUrlsDTO, or with status {@code 400 (Bad Request)} if the shopAssetUrls has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-asset-urls")
    public ResponseEntity<ShopAssetUrlsDTO> createShopAssetUrls(@Valid @RequestBody ShopAssetUrlsDTO shopAssetUrlsDTO) throws URISyntaxException {
        log.debug("REST request to save ShopAssetUrls : {}", shopAssetUrlsDTO);
        if (shopAssetUrlsDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopAssetUrls cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopAssetUrlsDTO result = shopAssetUrlsService.save(shopAssetUrlsDTO);
        return ResponseEntity.created(new URI("/api/shop-asset-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-asset-urls} : Updates an existing shopAssetUrls.
     *
     * @param shopAssetUrlsDTO the shopAssetUrlsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopAssetUrlsDTO,
     * or with status {@code 400 (Bad Request)} if the shopAssetUrlsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopAssetUrlsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-asset-urls")
    public ResponseEntity<ShopAssetUrlsDTO> updateShopAssetUrls(@Valid @RequestBody ShopAssetUrlsDTO shopAssetUrlsDTO) throws URISyntaxException {
        log.debug("REST request to update ShopAssetUrls : {}", shopAssetUrlsDTO);
        if (shopAssetUrlsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShopAssetUrlsDTO result = shopAssetUrlsService.save(shopAssetUrlsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopAssetUrlsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shop-asset-urls} : get all the shopAssetUrls.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopAssetUrls in body.
     */
    @GetMapping("/shop-asset-urls")
    public List<ShopAssetUrlsDTO> getAllShopAssetUrls() {
        log.debug("REST request to get all ShopAssetUrls");
        return shopAssetUrlsService.findAll();
    }

    /**
     * {@code GET  /shop-asset-urls/:id} : get the "id" shopAssetUrls.
     *
     * @param id the id of the shopAssetUrlsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopAssetUrlsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-asset-urls/{id}")
    public ResponseEntity<ShopAssetUrlsDTO> getShopAssetUrls(@PathVariable Long id) {
        log.debug("REST request to get ShopAssetUrls : {}", id);
        Optional<ShopAssetUrlsDTO> shopAssetUrlsDTO = shopAssetUrlsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopAssetUrlsDTO);
    }

    /**
     * {@code DELETE  /shop-asset-urls/:id} : delete the "id" shopAssetUrls.
     *
     * @param id the id of the shopAssetUrlsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-asset-urls/{id}")
    public ResponseEntity<Void> deleteShopAssetUrls(@PathVariable Long id) {
        log.debug("REST request to delete ShopAssetUrls : {}", id);
        shopAssetUrlsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
