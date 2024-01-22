package com.et.web.rest;

import com.et.service.AppstleMenuSettingsService;
import com.et.service.dto.AppstleMenuSettingsDTO;
import com.et.utils.CommonUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.AppstleMenuSettings}.
 */
@RestController
@RequestMapping("/api")
public class AppstleMenuSettingsResource {

    private final Logger log = LoggerFactory.getLogger(AppstleMenuSettingsResource.class);

    private static final String ENTITY_NAME = "appstleMenuSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    private final AppstleMenuSettingsService appstleMenuSettingsService;

    public AppstleMenuSettingsResource(AppstleMenuSettingsService appstleMenuSettingsService) {
        this.appstleMenuSettingsService = appstleMenuSettingsService;
    }

    /**
     * {@code POST  /appstle-menu-settings} : Create a new appstleMenuSettings.
     *
     * @param appstleMenuSettingsDTO the appstleMenuSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appstleMenuSettingsDTO, or with status {@code 400 (Bad Request)} if the appstleMenuSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appstle-menu-settings")
    public ResponseEntity<AppstleMenuSettingsDTO> createAppstleMenuSettings(@RequestBody AppstleMenuSettingsDTO appstleMenuSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save AppstleMenuSettings : {}", appstleMenuSettingsDTO);
        if (appstleMenuSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new appstleMenuSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();

        appstleMenuSettingsDTO.setShop(shop);
        AppstleMenuSettingsDTO result = appstleMenuSettingsService.save(appstleMenuSettingsDTO);
        appstleMenuSettingsService.applyTagsToProductsProcess(shop, appstleMenuSettingsDTO);
        subscribeItScriptUtils.createOrUpdateAppstleMenuPageAsync(shop);
        return ResponseEntity.created(new URI("/api/appstle-menu-settings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Appstle menu setting created successfully.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /appstle-menu-settings} : Updates an existing appstleMenuSettings.
     *
     * @param appstleMenuSettingsDTO the appstleMenuSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appstleMenuSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the appstleMenuSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appstleMenuSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appstle-menu-settings")
    public ResponseEntity<AppstleMenuSettingsDTO> updateAppstleMenuSettings(@RequestBody AppstleMenuSettingsDTO appstleMenuSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update AppstleMenuSettings : {}", appstleMenuSettingsDTO);
        if (appstleMenuSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();
        appstleMenuSettingsDTO.setShop(shop);
        AppstleMenuSettingsDTO result = appstleMenuSettingsService.save(appstleMenuSettingsDTO);
        appstleMenuSettingsService.applyTagsToProductsProcess(shop, appstleMenuSettingsDTO);
        subscribeItScriptUtils.createOrUpdateAppstleMenuPageAsync(shop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Appstle menu settings updated successfully.", ""))
            .body(result);
    }

    /**
     * {@code GET  /appstle-menu-settings} : get all the appstleMenuSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appstleMenuSettings in body.
     */
    @GetMapping("/appstle-menu-settings")
    public ResponseEntity<List<AppstleMenuSettingsDTO>> getAllAppstleMenuSettings(Pageable pageable) {
        log.debug("REST request to get a page of AppstleMenuSettings");
        String shop = commonUtils.getShop();

        List<AppstleMenuSettingsDTO> result = new ArrayList<>();

        Optional<AppstleMenuSettingsDTO> appstleMenuSettingsDTO = appstleMenuSettingsService.findByShop(shop);
        appstleMenuSettingsDTO.ifPresent(result::add);

        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /appstle-menu-settings/:id} : get the "id" appstleMenuSettings.
     *
     * @param id the id of the appstleMenuSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appstleMenuSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appstle-menu-settings/{id}")
    public ResponseEntity<AppstleMenuSettingsDTO> getAppstleMenuSettings(@PathVariable Long id) {
        log.debug("REST request to get AppstleMenuSettings : {}", id);
        String shop = commonUtils.getShop();
        Optional<AppstleMenuSettingsDTO> appstleMenuSettingsDTO = appstleMenuSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(appstleMenuSettingsDTO);
    }

    @GetMapping("/appstle-menu-settings/by-shop")
    public ResponseEntity<AppstleMenuSettingsDTO> getAppstleMenuSettingsByShop() {
        String shop = commonUtils.getShop();
        Optional<AppstleMenuSettingsDTO> appstleMenuSettingsDTO = appstleMenuSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(appstleMenuSettingsDTO);
    }

    /**
     * {@code DELETE  /appstle-menu-settings/:id} : delete the "id" appstleMenuSettings.
     *
     * @param id the id of the appstleMenuSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appstle-menu-settings/{id}")
    public ResponseEntity<Void> deleteAppstleMenuSettings(@PathVariable Long id) {
        log.debug("REST request to delete AppstleMenuSettings : {}", id);
        appstleMenuSettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
