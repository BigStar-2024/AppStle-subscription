package com.et.web.rest;

import com.et.domain.ActivityUpdatesSettings;
import com.et.domain.enumeration.SummaryReportFrequencyUnit;
import com.et.domain.enumeration.SummaryReportTimePeriodUnit;
import com.et.security.SecurityUtils;
import com.et.service.ActivityUpdatesSettingsService;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ActivityUpdatesSettings}.
 */
@RestController
@RequestMapping("/api")
public class ActivityUpdatesSettingsResource {

    private final Logger log = LoggerFactory.getLogger(ActivityUpdatesSettingsResource.class);

    private static final String ENTITY_NAME = "activityUpdatesSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityUpdatesSettingsService activityUpdatesSettingsService;

    public ActivityUpdatesSettingsResource(ActivityUpdatesSettingsService activityUpdatesSettingsService) {
        this.activityUpdatesSettingsService = activityUpdatesSettingsService;
    }

    /**
     * {@code POST  /activity-updates-settings} : Create a new
     * activityUpdatesSettings.
     *
     * @param activityUpdatesSettings the activityUpdatesSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new activityUpdatesSettings, or with status
     *         {@code 400 (Bad Request)} if the activityUpdatesSettings has already
     *         an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/activity-updates-settings")
    public ResponseEntity<ActivityUpdatesSettings> createActivityUpdatesSettings(
        @Valid @RequestBody ActivityUpdatesSettings activityUpdatesSettings) throws URISyntaxException {
        log.debug("REST request to save ActivityUpdatesSettings : {}", activityUpdatesSettings);
        if (activityUpdatesSettings.getId() != null) {
            throw new BadRequestAlertException("A new activityUpdatesSettings cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        ActivityUpdatesSettings result = activityUpdatesSettingsService.save(activityUpdatesSettings);
        return ResponseEntity
            .created(new URI("/api/activity-updates-settings/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /activity-updates-settings} : Updates an existing
     * activityUpdatesSettings.
     *
     * @param activityUpdatesSettings the activityUpdatesSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated activityUpdatesSettings, or with status
     * {@code 400 (Bad Request)} if the activityUpdatesSettings is not
     * valid, or with status {@code 500 (Internal Server Error)} if the
     * activityUpdatesSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-updates-settings")
    public ResponseEntity<ActivityUpdatesSettings> updateActivityUpdatesSettings(
        @Valid @RequestBody ActivityUpdatesSettings activityUpdatesSettings) throws URISyntaxException {
        log.debug("REST request to update ActivityUpdatesSettings : {}", activityUpdatesSettings);
        if (activityUpdatesSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(activityUpdatesSettings.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ActivityUpdatesSettings result = activityUpdatesSettingsService.save(activityUpdatesSettings);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Activity Settings updated.", ENTITY_NAME)).body(result);
    }

    /**
     * {@code GET  /activity-updates-settings} : get all the
     * activityUpdatesSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of activityUpdatesSettings in body.
     */
    @GetMapping("/activity-updates-settings")
    public ResponseEntity<List<ActivityUpdatesSettings>> getAllActivityUpdatesSettings(Pageable pageable,
                                                                                       @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ActivityUpdatesSettings");

        ActivityUpdatesSettings activityUpdatesSettings =
            activityUpdatesSettingsService.findByShop(SecurityUtils.getCurrentUserLogin().get()).get();

        return ResponseEntity.ok().body(Arrays.asList(activityUpdatesSettings));
    }

    @GetMapping("/activity-updates-setting")
    public ResponseEntity<ActivityUpdatesSettings> getActivityUpdatesSettingsForShop() {
        log.debug("REST request to get a page of ActivityUpdatesSettings");
        Optional<ActivityUpdatesSettings> optionalActivityUpdatesSettings = activityUpdatesSettingsService
            .findByShop(SecurityUtils.getCurrentUserLogin().get());
        ActivityUpdatesSettings activityUpdatesSettings = new ActivityUpdatesSettings();
        if (optionalActivityUpdatesSettings.isPresent()) {
            activityUpdatesSettings = optionalActivityUpdatesSettings.get();
        } else {
            // Return default value if the entry in DB is not present.
            activityUpdatesSettings.setShop(SecurityUtils.getCurrentUserLogin().get());
            activityUpdatesSettings.setSummaryReportEnabled(false);
            if (activityUpdatesSettings.getSummaryReportFrequency() == null) {
                activityUpdatesSettings.setSummaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_7_DAYS);
                activityUpdatesSettings.setSummaryReportFrequency(SummaryReportFrequencyUnit.WEEKLY);
            }
            activityUpdatesSettingsService.save(activityUpdatesSettings);
        }
        return ResponseEntity.ok(activityUpdatesSettings);
    }

    /**
     * {@code GET  /activity-updates-settings/:id} : get the "id"
     * activityUpdatesSettings.
     *
     * @param id the id of the activityUpdatesSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the activityUpdatesSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-updates-settings/{id}")
    public ResponseEntity<ActivityUpdatesSettings> getActivityUpdatesSettings(@PathVariable Long id) {
        log.debug("REST request to get ActivityUpdatesSettings : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ActivityUpdatesSettings> activityUpdatesSettings = activityUpdatesSettingsService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(activityUpdatesSettings);
    }

    /**
     * {@code DELETE  /activity-updates-settings/:id} : delete the "id"
     * activityUpdatesSettings.
     *
     * @param id the id of the activityUpdatesSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/activity-updates-settings/{id}")
    public ResponseEntity<Void> deleteActivityUpdatesSettings(@PathVariable Long id) {
        log.debug("REST request to delete ActivityUpdatesSettings : {}", id);
        activityUpdatesSettingsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }*/
}
