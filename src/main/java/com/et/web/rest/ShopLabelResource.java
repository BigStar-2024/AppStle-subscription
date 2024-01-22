package com.et.web.rest;

import com.et.pojo.LabelValueInfo;
import com.et.service.ShopLabelService;
import com.et.service.dto.ShopLabelDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ShopLabel}.
 */
@RestController
@RequestMapping("/api")
public class ShopLabelResource {

    private final Logger log = LoggerFactory.getLogger(ShopLabelResource.class);

    private static final String ENTITY_NAME = "shopLabel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopLabelService shopLabelService;

    private final CommonUtils commonUtils;

    public ShopLabelResource(ShopLabelService shopLabelService, CommonUtils commonUtils) {
        this.shopLabelService = shopLabelService;
        this.commonUtils = commonUtils;
    }

    /**
     * {@code POST  /shop-labels} : Create a new shopLabel.
     *
     * @param shopLabelDTO the shopLabelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopLabelDTO, or with status {@code 400 (Bad Request)} if the shopLabel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shop-labels")
    public ResponseEntity<ShopLabelDTO> createShopLabel(@RequestBody ShopLabelDTO shopLabelDTO) throws URISyntaxException {
        log.debug("REST request to save ShopLabel : {}", shopLabelDTO);
        if (shopLabelDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        shopLabelDTO.setShop(shop);
        ShopLabelDTO result = shopLabelService.save(shopLabelDTO);
        return ResponseEntity.created(new URI("/api/shop-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shop-labels} : Updates an existing shopLabel.
     *
     * @param shopLabelDTOList the List of shopLabelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopLabelDTO,
     * or with status {@code 400 (Bad Request)} if the shopLabelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopLabelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shop-labels")
    public ResponseEntity<ShopLabelDTO> updateShopLabel(@RequestBody List<ShopLabelDTO> shopLabelDTOList) throws Exception {
        log.debug("REST request to update ShopLabel");
        String shop = commonUtils.getShop();
        try {
            shopLabelService.updateShopLabels(shop, shopLabelDTOList);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, "Labels Updated Successfully", "")).build();
        } catch (Exception e) {
            log.error("An error occurred while updating shop labels: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * {@code GET  /shop-labels} : get all the shopLabels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopLabels in body.
     */
    @GetMapping("/shop-labels")
    public ResponseEntity<List<ShopLabelDTO>> getAllShopLabels(Pageable pageable) {
        log.debug("REST request to get a page of ShopLabels");
        String shop = commonUtils.getShop();
        Page<ShopLabelDTO> page = shopLabelService.findAllByShop(shop, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shop-labels/:id} : get the "id" shopLabel.
     *
     * @param id the id of the shopLabelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopLabelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shop-labels/{id}")
    public ResponseEntity<ShopLabelDTO> getShopLabel(@PathVariable Long id) {
        log.debug("REST request to get ShopLabel : {}", id);
        String shop = commonUtils.getShop();
        Optional<ShopLabelDTO> shopLabelDTO = shopLabelService.findByIdAndShop(id, shop);
        return ResponseUtil.wrapOrNotFound(shopLabelDTO);
    }

    /**
     * {@code DELETE  /shop-labels/:id} : delete the "id" shopLabel.
     *
     * @param id the id of the shopLabelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shop-labels/{id}")
    public ResponseEntity<Void> deleteShopLabel(@PathVariable Long id) {
        log.debug("REST request to delete ShopLabel : {}", id);
        String shop = commonUtils.getShop();
        shopLabelService.deleteByIdAndShop(id, shop);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/shop-labels/by-shop")
    public ResponseEntity<Map<String, LabelValueInfo>> getShopLabels() {

        log.debug("REST request to get ShopLabels values");
        String shop = commonUtils.getShop();

        Map<String, LabelValueInfo> labels = shopLabelService.getShopLabels(shop);

        return ResponseEntity.ok().body(labels);
    }

    @PutMapping("/shop-labels/by-shop")
    public ResponseEntity<Map<String, LabelValueInfo>> updateAllShopLabels(@RequestBody Map<String, LabelValueInfo> labels) {

        log.debug("REST request to get ShopLabels values");
        String shop = commonUtils.getShop();

        try {
            labels = shopLabelService.updateShopLabels(shop, labels);
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException("Incorrect Labels data", ENTITY_NAME, "");
        }

        return ResponseEntity.ok().body(labels);
    }

    @PostMapping("/shop-labels/add-key")
    public ResponseEntity<Void> addKey(@RequestParam("key") String key) {
        log.debug("Rest request to shop-label addKey for key: {}", key);
        try {
            shopLabelService.addKey(key);
            return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Key added successfully", "")).build();
        } catch (Exception e) {
            log.error("Some error occurred while adding new label, error message:{}", e.getMessage());
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(applicationName, false, ENTITY_NAME, "", e.getMessage())).build();
        }
    }

    @PostMapping("/shop-labels/remove-key")
    public ResponseEntity<Void> removeKey(@RequestParam("key") String key) {
        log.debug("Rest request to shop-label removeKey for key: {}", key);
        try {
            shopLabelService.removeKey(key);
            return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Key removed successfully", "")).build();
        } catch (Exception e) {
            log.error("Some error occurred while removing key, error message:{}", e.getMessage());
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(applicationName, false, ENTITY_NAME, "", e.getMessage())).build();
        }
    }
}
