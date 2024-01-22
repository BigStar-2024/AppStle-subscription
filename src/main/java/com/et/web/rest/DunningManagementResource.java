package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.DunningManagementService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.DunningManagementDTO;

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
 * REST controller for managing {@link com.et.domain.DunningManagement}.
 */
@RestController
@RequestMapping("/api")
public class DunningManagementResource {

    private final Logger log = LoggerFactory.getLogger(DunningManagementResource.class);

    private static final String ENTITY_NAME = "dunningManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DunningManagementService dunningManagementService;

    public DunningManagementResource(DunningManagementService dunningManagementService) {
        this.dunningManagementService = dunningManagementService;
    }

    /**
     * {@code POST  /dunning-managements} : Create a new dunningManagement.
     *
     * @param dunningManagementDTO the dunningManagementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dunningManagementDTO, or with status {@code 400 (Bad Request)} if the dunningManagement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dunning-managements")
    public ResponseEntity<DunningManagementDTO> createDunningManagement(@Valid @RequestBody DunningManagementDTO dunningManagementDTO) throws URISyntaxException {
        log.debug("REST request to save DunningManagement : {}", dunningManagementDTO);
        if (dunningManagementDTO.getId() != null) {
            throw new BadRequestAlertException("A new dunningManagement cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(dunningManagementDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        DunningManagementDTO result = dunningManagementService.save(dunningManagementDTO);
        return ResponseEntity.created(new URI("/api/dunning-managements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dunning-managements} : Updates an existing dunningManagement.
     *
     * @param dunningManagementDTO the dunningManagementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dunningManagementDTO,
     * or with status {@code 400 (Bad Request)} if the dunningManagementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dunningManagementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dunning-managements")
    public ResponseEntity<DunningManagementDTO> updateDunningManagement(@Valid @RequestBody DunningManagementDTO dunningManagementDTO) throws URISyntaxException {
        log.debug("REST request to update DunningManagement : {}", dunningManagementDTO);
        if (dunningManagementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(dunningManagementDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        DunningManagementDTO result = dunningManagementService.save(dunningManagementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Settings updated.", dunningManagementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dunning-managements} : get all the dunningManagements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dunningManagements in body.
     */
    @GetMapping("/dunning-managements")
    public List<DunningManagementDTO> getAllDunningManagements() {
        log.debug("REST request to get all DunningManagements");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return dunningManagementService.findAllByShop(shop);
    }

    /**
     * {@code GET  /dunning-managements/:id} : get the "id" dunningManagement.
     *
     * @param id the id of the dunningManagementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dunningManagementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dunning-managements/{id}")
    public ResponseEntity<DunningManagementDTO> getDunningManagement(@PathVariable Long id) {
        log.debug("REST request to get DunningManagement : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<DunningManagementDTO> dunningManagementDTO = dunningManagementService.findOneByShop(shop);
        return ResponseUtil.wrapOrNotFound(dunningManagementDTO);
    }

    /**
     * {@code DELETE  /dunning-managements/:id} : delete the "id" dunningManagement.
     *
     * @param id the id of the dunningManagementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dunning-managements/{id}")
    public ResponseEntity<Void> deleteDunningManagement(@PathVariable Long id) {
        log.debug("REST request to delete DunningManagement : {}", id);
        dunningManagementService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
