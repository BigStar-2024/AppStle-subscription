package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.AppstleMenuLabelsService;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.AppstleMenuLabelsDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.AppstleMenuLabels}.
 */
@RestController
@RequestMapping("/api")
public class AppstleMenuLabelsResource {

    private final Logger log = LoggerFactory.getLogger(AppstleMenuLabelsResource.class);

    private static final String ENTITY_NAME = "appstleMenuLabels";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppstleMenuLabelsService appstleMenuLabelsService;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    public AppstleMenuLabelsResource(AppstleMenuLabelsService appstleMenuLabelsService) {
        this.appstleMenuLabelsService = appstleMenuLabelsService;
    }

    /**
     * {@code POST  /appstle-menu-labels} : Create a new appstleMenuLabels.
     *
     * @param appstleMenuLabelsDTO the appstleMenuLabelsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appstleMenuLabelsDTO, or with status {@code 400 (Bad Request)} if the appstleMenuLabels has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appstle-menu-labels")
    public ResponseEntity<AppstleMenuLabelsDTO> createAppstleMenuLabels(@Valid @RequestBody AppstleMenuLabelsDTO appstleMenuLabelsDTO) throws URISyntaxException {
        log.debug("REST request to save AppstleMenuLabels : {}", appstleMenuLabelsDTO);
        if (appstleMenuLabelsDTO.getId() != null) {
            throw new BadRequestAlertException("A new appstleMenuLabels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String shop = SecurityUtils.getCurrentUserLogin().get();
        AppstleMenuLabelsDTO result = appstleMenuLabelsService.save(appstleMenuLabelsDTO);
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return ResponseEntity.created(new URI("/api/appstle-menu-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appstle-menu-labels} : Updates an existing appstleMenuLabels.
     *
     * @param appstleMenuLabelsDTO the appstleMenuLabelsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appstleMenuLabelsDTO,
     * or with status {@code 400 (Bad Request)} if the appstleMenuLabelsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appstleMenuLabelsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appstle-menu-labels")
    public ResponseEntity<AppstleMenuLabelsDTO> updateAppstleMenuLabels(@Valid @RequestBody AppstleMenuLabelsDTO appstleMenuLabelsDTO) throws URISyntaxException {
        log.debug("REST request to update AppstleMenuLabels : {}", appstleMenuLabelsDTO);
        if (appstleMenuLabelsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = SecurityUtils.getCurrentUserLogin().get();
        AppstleMenuLabelsDTO result = appstleMenuLabelsService.save(appstleMenuLabelsDTO);
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appstleMenuLabelsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /appstle-menu-labels} : get all the appstleMenuLabels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appstleMenuLabels in body.
     */
    @GetMapping("/appstle-menu-labels")
    public ResponseEntity<List<AppstleMenuLabelsDTO>> getAllAppstleMenuLabels(Pageable pageable) {
        log.debug("REST request to get a page of AppstleMenuLabels");
        Page<AppstleMenuLabelsDTO> page = appstleMenuLabelsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appstle-menu-labels/:id} : get the "id" appstleMenuLabels.
     *
     * @param id the id of the appstleMenuLabelsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appstleMenuLabelsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appstle-menu-labels/{id}")
    public ResponseEntity<AppstleMenuLabelsDTO> getAppstleMenuLabels(@PathVariable Long id) {
        log.debug("REST request to get AppstleMenuLabels : {}", id);
        Optional<AppstleMenuLabelsDTO> appstleMenuLabelsDTO = appstleMenuLabelsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appstleMenuLabelsDTO);
    }


    @GetMapping("/appstle-menu-labels/shop")
    public ResponseEntity<AppstleMenuLabelsDTO> getAppstleMenuLabelsByShop() {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<AppstleMenuLabelsDTO> appstleMenuLabelsDTO = appstleMenuLabelsService.findOneByShop(shop);
        return ResponseUtil.wrapOrNotFound(appstleMenuLabelsDTO);
    }

    /**
     * {@code DELETE  /appstle-menu-labels/:id} : delete the "id" appstleMenuLabels.
     *
     * @param id the id of the appstleMenuLabelsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appstle-menu-labels/{id}")
    public ResponseEntity<Void> deleteAppstleMenuLabels(@PathVariable Long id) {
        log.debug("REST request to delete AppstleMenuLabels : {}", id);
        appstleMenuLabelsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
