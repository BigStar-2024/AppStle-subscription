package com.et.web.rest;

import com.et.pojo.ProductFilterDTO;
import com.et.security.SecurityUtils;
import com.et.service.ProductInfoService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.ProductInfoDTO;

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
 * REST controller for managing {@link com.et.domain.ProductInfo}.
 */
@RestController
@RequestMapping("/api")
public class ProductInfoResource {

    private final Logger log = LoggerFactory.getLogger(ProductInfoResource.class);

    private static final String ENTITY_NAME = "productInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductInfoService productInfoService;

    public ProductInfoResource(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    /**
     * {@code POST  /product-infos} : Create a new productInfo.
     *
     * @param productInfoDTO the productInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productInfoDTO, or with status {@code 400 (Bad Request)} if the productInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-infos")
    public ResponseEntity<ProductInfoDTO> createProductInfo(@Valid @RequestBody ProductInfoDTO productInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ProductInfo : {}", productInfoDTO);
        if (productInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new productInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productInfoDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductInfoDTO result = productInfoService.save(productInfoDTO);
        return ResponseEntity.created(new URI("/api/product-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-infos} : Updates an existing productInfo.
     *
     * @param productInfoDTO the productInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productInfoDTO,
     * or with status {@code 400 (Bad Request)} if the productInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-infos")
    public ResponseEntity<ProductInfoDTO> updateProductInfo(@Valid @RequestBody ProductInfoDTO productInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ProductInfo : {}", productInfoDTO);
        if (productInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(productInfoDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ProductInfoDTO result = productInfoService.save(productInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-infos} : get all the productInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productInfos in body.
     */


    /**
     * {@code GET  /product-infos/:id} : get the "id" productInfo.
     *
     * @param id the id of the productInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-infos/{id}")
    public ResponseEntity<ProductInfoDTO> getProductInfo(@PathVariable Long id) {
        log.debug("REST request to get ProductInfo : {}", id);
        Optional<ProductInfoDTO> productInfoDTO = productInfoService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (productInfoDTO.isPresent()) {
            if (!productInfoDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        return ResponseUtil.wrapOrNotFound(productInfoDTO);
    }

    /**
     * {@code DELETE  /product-infos/:id} : delete the "id" productInfo.
     *
     * @param id the id of the productInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-infos/{id}")
    public ResponseEntity<Void> deleteProductInfo(@PathVariable Long id) {
        log.debug("REST request to delete ProductInfo : {}", id);

        Optional<ProductInfoDTO> productInfoDTO = productInfoService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (productInfoDTO.isPresent()) {
            if (!productInfoDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        productInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/product-infos/product-filter-data")
    public ProductFilterDTO getProductFilterData() throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return  productInfoService.getAllProductFilterData(shop);
    }
}
