package com.et.web.rest;

import com.et.service.CustomizationService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.CustomizationDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.Customization}.
 */
@RestController
@RequestMapping("/api")
public class CustomizationResource {

    private final Logger log = LoggerFactory.getLogger(CustomizationResource.class);

    private static final String ENTITY_NAME = "customization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomizationService customizationService;

    public CustomizationResource(CustomizationService customizationService) {
        this.customizationService = customizationService;
    }

    /**
     * {@code POST  /customizations} : Create a new customization.
     *
     * @param customizationDTO the customizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customizationDTO, or with status {@code 400 (Bad Request)} if the customization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customizations")
    public ResponseEntity<CustomizationDTO> createCustomization(@RequestBody CustomizationDTO customizationDTO) throws URISyntaxException {
        log.debug("REST request to save Customization : {}", customizationDTO);
        if (customizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new customization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomizationDTO result = customizationService.save(customizationDTO);
        return ResponseEntity.created(new URI("/api/customizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customizations} : Updates an existing customization.
     *
     * @param customizationDTO the customizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customizationDTO,
     * or with status {@code 400 (Bad Request)} if the customizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customizations")
    public ResponseEntity<CustomizationDTO> updateCustomization(@RequestBody CustomizationDTO customizationDTO) throws URISyntaxException {
        log.debug("REST request to update Customization : {}", customizationDTO);
        if (customizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomizationDTO result = customizationService.save(customizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customizations} : get all the customizations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customizations in body.
     */
    @GetMapping("/customizations")
    public List<CustomizationDTO> getAllCustomizations() {
        log.debug("REST request to get all Customizations");
        return customizationService.findAll();
    }

    /**
     * {@code GET  /customizations/:id} : get the "id" customization.
     *
     * @param id the id of the customizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customizations/{id}")
    public ResponseEntity<CustomizationDTO> getCustomization(@PathVariable Long id) {
        log.debug("REST request to get Customization : {}", id);
        Optional<CustomizationDTO> customizationDTO = customizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customizationDTO);
    }

    /**
     * {@code DELETE  /customizations/:id} : delete the "id" customization.
     *
     * @param id the id of the customizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customizations/{id}")
    public ResponseEntity<Void> deleteCustomization(@PathVariable Long id) {
        log.debug("REST request to delete Customization : {}", id);
        customizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
