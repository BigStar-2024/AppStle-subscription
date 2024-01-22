package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.ProductCycleService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.ProductCycleDTO;

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
 * REST controller for managing {@link com.et.domain.ProductCycle}.
 */
@RestController
@RequestMapping("/api")
public class ProductCycleResource {

    private final Logger log = LoggerFactory.getLogger(ProductCycleResource.class);

    private static final String ENTITY_NAME = "productCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCycleService productCycleService;

    public ProductCycleResource(ProductCycleService productCycleService) {
        this.productCycleService = productCycleService;
    }

    /**
     * {@code POST  /product-cycles} : Create a new productCycle.
     *
     * @param productCycleDTO the productCycleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productCycleDTO, or with status {@code 400 (Bad Request)} if the productCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-cycles")
    public ResponseEntity<ProductCycleDTO> createProductCycle(@Valid @RequestBody ProductCycleDTO productCycleDTO) throws URISyntaxException {
        log.debug("REST request to save ProductCycle : {}", productCycleDTO);
        if (productCycleDTO.getId() != null) {
            throw new BadRequestAlertException("A new productCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productCycleDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductCycleDTO result = productCycleService.save(productCycleDTO);
        return ResponseEntity.created(new URI("/api/product-cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-cycles} : Updates an existing productCycle.
     *
     * @param productCycleDTO the productCycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productCycleDTO,
     * or with status {@code 400 (Bad Request)} if the productCycleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productCycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-cycles")
    public ResponseEntity<ProductCycleDTO> updateProductCycle(@Valid @RequestBody ProductCycleDTO productCycleDTO) throws URISyntaxException {
        log.debug("REST request to update ProductCycle : {}", productCycleDTO);
        if (productCycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productCycleDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductCycleDTO result = productCycleService.save(productCycleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productCycleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-cycles} : get all the productCycles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCycles in body.
     */
    @GetMapping("/product-cycles")
    public List<ProductCycleDTO> getAllProductCycles() {
        log.debug("REST request to get all ProductCycles");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return productCycleService.findByShop(shop);
    }

    /**
     * {@code GET  /product-cycles/:id} : get the "id" productCycle.
     *
     * @param id the id of the productCycleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productCycleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-cycles/{id}")
    public ResponseEntity<ProductCycleDTO> getProductCycle(@PathVariable Long id) {
        log.debug("REST request to get ProductCycle : {}", id);
        Optional<ProductCycleDTO> productCycleDTO = productCycleService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (productCycleDTO.isPresent() && !shop.equals(productCycleDTO.get().getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        return ResponseUtil.wrapOrNotFound(productCycleDTO);
    }

    /**
     * {@code DELETE  /product-cycles/:id} : delete the "id" productCycle.
     *
     * @param id the id of the productCycleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-cycles/{id}")
    public ResponseEntity<Void> deleteProductCycle(@PathVariable Long id) {
        log.debug("REST request to delete ProductCycle : {}", id);
        Optional<ProductCycleDTO> one = productCycleService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (one.isPresent()) {
            if (!one.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        productCycleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
