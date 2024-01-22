package com.et.web.rest;

import com.et.service.VariantInfoService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.VariantInfoDTO;

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
 * REST controller for managing {@link com.et.domain.VariantInfo}.
 */
@RestController
@RequestMapping("/api")
public class VariantInfoResource {

    private final Logger log = LoggerFactory.getLogger(VariantInfoResource.class);

    private static final String ENTITY_NAME = "variantInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VariantInfoService variantInfoService;

    public VariantInfoResource(VariantInfoService variantInfoService) {
        this.variantInfoService = variantInfoService;
    }

    /**
     * {@code POST  /variant-infos} : Create a new variantInfo.
     *
     * @param variantInfoDTO the variantInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variantInfoDTO, or with status {@code 400 (Bad Request)} if the variantInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/variant-infos")
    public ResponseEntity<VariantInfoDTO> createVariantInfo(@Valid @RequestBody VariantInfoDTO variantInfoDTO) throws URISyntaxException {
        log.debug("REST request to save VariantInfo : {}", variantInfoDTO);
        if (variantInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new variantInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VariantInfoDTO result = variantInfoService.save(variantInfoDTO);
        return ResponseEntity.created(new URI("/api/variant-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /variant-infos} : Updates an existing variantInfo.
     *
     * @param variantInfoDTO the variantInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variantInfoDTO,
     * or with status {@code 400 (Bad Request)} if the variantInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variantInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/variant-infos")
    public ResponseEntity<VariantInfoDTO> updateVariantInfo(@Valid @RequestBody VariantInfoDTO variantInfoDTO) throws URISyntaxException {
        log.debug("REST request to update VariantInfo : {}", variantInfoDTO);
        if (variantInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VariantInfoDTO result = variantInfoService.save(variantInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variantInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /variant-infos} : get all the variantInfos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variantInfos in body.
     */
    @GetMapping("/variant-infos")
    public List<VariantInfoDTO> getAllVariantInfos() {
        log.debug("REST request to get all VariantInfos");
        return variantInfoService.findAll();
    }

    /**
     * {@code GET  /variant-infos/:id} : get the "id" variantInfo.
     *
     * @param id the id of the variantInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variantInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/variant-infos/{id}")
    public ResponseEntity<VariantInfoDTO> getVariantInfo(@PathVariable Long id) {
        log.debug("REST request to get VariantInfo : {}", id);
        Optional<VariantInfoDTO> variantInfoDTO = variantInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(variantInfoDTO);
    }

    /**
     * {@code DELETE  /variant-infos/:id} : delete the "id" variantInfo.
     *
     * @param id the id of the variantInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/variant-infos/{id}")
    public ResponseEntity<Void> deleteVariantInfo(@PathVariable Long id) {
        log.debug("REST request to delete VariantInfo : {}", id);
        variantInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
