package com.et.web.rest;

import com.et.domain.ThemeCode;
import com.et.repository.ThemeCodeRepository;
import com.et.service.dto.ThemeCodeDTO;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ThemeCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ThemeCodeResource {

    private final Logger log = LoggerFactory.getLogger(ThemeCodeResource.class);

    private static final String ENTITY_NAME = "themeCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeCodeRepository themeCodeRepository;

    public ThemeCodeResource(ThemeCodeRepository themeCodeRepository) {
        this.themeCodeRepository = themeCodeRepository;
    }

    /**
     * {@code POST  /theme-codes} : Create a new themeCode.
     *
     * @param themeCode the themeCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeCode, or with status {@code 400 (Bad Request)} if the themeCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/theme-codes")
    public ResponseEntity<ThemeCode> createThemeCode(@Valid @RequestBody ThemeCode themeCode) throws URISyntaxException {
        log.debug("REST request to save ThemeCode : {}", themeCode);
        if (themeCode.getId() != null) {
            throw new BadRequestAlertException("A new themeCode cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ThemeCode result = themeCodeRepository.save(themeCode);
        return ResponseEntity.created(new URI("/api/theme-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /theme-codes} : Updates an existing themeCode.
     *
     * @param themeCode the themeCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeCode,
     * or with status {@code 400 (Bad Request)} if the themeCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PutMapping("/theme-codes")
    public ResponseEntity<ThemeCode> updateThemeCode(@Valid @RequestBody ThemeCode themeCode) throws URISyntaxException {
        log.debug("REST request to update ThemeCode : {}", themeCode);
        if (themeCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ThemeCode result = themeCodeRepository.save(themeCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, themeCode.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code GET  /theme-codes} : get all the themeCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeCodes in body.
     */
    @GetMapping("/theme-codes")
    public List<ThemeCode> getAllThemeCodes() {
        log.debug("REST request to get all ThemeCodes");
        return themeCodeRepository.findAll();
    }

    @GetMapping("/theme-codes-lite")
    public List<ThemeCodeDTO> getAllThemeCodesLite() {
        log.debug("REST request to get all ThemeCodes Lite");
        List<ThemeCode> themeCodes = themeCodeRepository.findAll();
        List<ThemeCodeDTO> result = new ArrayList<>();
        for (ThemeCode themeCode : themeCodes) {
            ThemeCodeDTO themeCodeDTO = new ThemeCodeDTO();
            themeCodeDTO.setThemeName(themeCode.getThemeName());
            themeCodeDTO.setThemeNameFriendly(themeCode.getThemeNameFriendly());
            result.add(themeCodeDTO);
        }
        result.sort(Comparator.comparing(ThemeCodeDTO::getThemeNameFriendly));
        return result;
    }

    /**
     * {@code GET  /theme-codes/:id} : get the "id" themeCode.
     *
     * @param id the id of the themeCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theme-codes/{id}")
    public ResponseEntity<ThemeCode> getThemeCode(@PathVariable Long id) {
        log.debug("REST request to get ThemeCode : {}", id);
        Optional<ThemeCode> themeCode = themeCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(themeCode);
    }

    /**
     * {@code DELETE  /theme-codes/:id} : delete the "id" themeCode.
     *
     * @param id the id of the themeCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/theme-codes/{id}")
    public ResponseEntity<Void> deleteThemeCode(@PathVariable Long id) {
        log.debug("REST request to delete ThemeCode : {}", id);
        themeCodeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
