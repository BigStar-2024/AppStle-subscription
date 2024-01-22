package com.et.web.rest;

import com.et.service.CurrencyConversionInfoService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.CurrencyConversionInfoDTO;
import com.et.service.dto.CurrencyConversionInfoCriteria;
import com.et.service.CurrencyConversionInfoQueryService;

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
 * REST controller for managing {@link com.et.domain.CurrencyConversionInfo}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyConversionInfoResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyConversionInfoResource.class);

    private static final String ENTITY_NAME = "currencyConversionInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyConversionInfoService currencyConversionInfoService;

    private final CurrencyConversionInfoQueryService currencyConversionInfoQueryService;

    public CurrencyConversionInfoResource(CurrencyConversionInfoService currencyConversionInfoService, CurrencyConversionInfoQueryService currencyConversionInfoQueryService) {
        this.currencyConversionInfoService = currencyConversionInfoService;
        this.currencyConversionInfoQueryService = currencyConversionInfoQueryService;
    }

    /**
     * {@code POST  /currency-conversion-infos} : Create a new currencyConversionInfo.
     *
     * @param currencyConversionInfoDTO the currencyConversionInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyConversionInfoDTO, or with status {@code 400 (Bad Request)} if the currencyConversionInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-conversion-infos")
    public ResponseEntity<CurrencyConversionInfoDTO> createCurrencyConversionInfo(@Valid @RequestBody CurrencyConversionInfoDTO currencyConversionInfoDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyConversionInfo : {}", currencyConversionInfoDTO);
        if (currencyConversionInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyConversionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyConversionInfoDTO result = currencyConversionInfoService.save(currencyConversionInfoDTO);
        return ResponseEntity.created(new URI("/api/currency-conversion-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-conversion-infos} : Updates an existing currencyConversionInfo.
     *
     * @param currencyConversionInfoDTO the currencyConversionInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyConversionInfoDTO,
     * or with status {@code 400 (Bad Request)} if the currencyConversionInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyConversionInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-conversion-infos")
    public ResponseEntity<CurrencyConversionInfoDTO> updateCurrencyConversionInfo(@Valid @RequestBody CurrencyConversionInfoDTO currencyConversionInfoDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyConversionInfo : {}", currencyConversionInfoDTO);
        if (currencyConversionInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyConversionInfoDTO result = currencyConversionInfoService.save(currencyConversionInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyConversionInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /currency-conversion-infos} : get all the currencyConversionInfos.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyConversionInfos in body.
     */
    @GetMapping("/currency-conversion-infos")
    public ResponseEntity<List<CurrencyConversionInfoDTO>> getAllCurrencyConversionInfos(CurrencyConversionInfoCriteria criteria) {
        log.debug("REST request to get CurrencyConversionInfos by criteria: {}", criteria);
        List<CurrencyConversionInfoDTO> entityList = currencyConversionInfoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /currency-conversion-infos/count} : count all the currencyConversionInfos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/currency-conversion-infos/count")
    public ResponseEntity<Long> countCurrencyConversionInfos(CurrencyConversionInfoCriteria criteria) {
        log.debug("REST request to count CurrencyConversionInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyConversionInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currency-conversion-infos/:id} : get the "id" currencyConversionInfo.
     *
     * @param id the id of the currencyConversionInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyConversionInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-conversion-infos/{id}")
    public ResponseEntity<CurrencyConversionInfoDTO> getCurrencyConversionInfo(@PathVariable Long id) {
        log.debug("REST request to get CurrencyConversionInfo : {}", id);
        Optional<CurrencyConversionInfoDTO> currencyConversionInfoDTO = currencyConversionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyConversionInfoDTO);
    }

    /**
     * {@code DELETE  /currency-conversion-infos/:id} : delete the "id" currencyConversionInfo.
     *
     * @param id the id of the currencyConversionInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-conversion-infos/{id}")
    public ResponseEntity<Void> deleteCurrencyConversionInfo(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyConversionInfo : {}", id);
        currencyConversionInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
