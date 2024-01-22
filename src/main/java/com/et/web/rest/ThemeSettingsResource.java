package com.et.web.rest;

import com.et.domain.ThemeSettings;
import com.et.domain.enumeration.ShopifyThemeInstallationVersion;
import com.et.security.SecurityUtils;
import com.et.service.ThemeSettingsService;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.ThemeSettings}.
 */
@RestController
public class ThemeSettingsResource {

    private final Logger log = LoggerFactory.getLogger(ThemeSettingsResource.class);

    private static final String ENTITY_NAME = "themeSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    private final ThemeSettingsService themeSettingsService;

    public ThemeSettingsResource(ThemeSettingsService themeSettingsService) {
        this.themeSettingsService = themeSettingsService;
    }

    /**
     * {@code POST  /theme-settings} : Create a new themeSettings.
     *
     * @param themeSettings the themeSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeSettings, or with status {@code 400 (Bad Request)} if the themeSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api/theme-settings")
    public ResponseEntity<ThemeSettings> createThemeSettings(@Valid @RequestBody ThemeSettings themeSettings) throws URISyntaxException {
        log.debug("REST request to save ThemeSettings : {}", themeSettings);
        if (themeSettings.getId() != null) {
            throw new BadRequestAlertException("A new themeSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(themeSettings.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ThemeSettings result = themeSettingsService.save(themeSettings);
        return ResponseEntity.created(new URI("/api/theme-settings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "New Theme Created.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /theme-settings} : Updates an existing themeSettings.
     *
     * @param themeSettings the themeSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeSettings,
     * or with status {@code 400 (Bad Request)} if the themeSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api/theme-settings")
    public ResponseEntity<ThemeSettings> updateThemeSettings(@Valid @RequestBody ThemeSettings themeSettings) throws URISyntaxException {
        log.debug("REST request to update ThemeSettings : {}", themeSettings);
        if (themeSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(themeSettings.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ThemeSettings result = themeSettingsService.save(themeSettings);

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop, false, null);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Theme Updated.", ""))
            .body(result);
    }

    /**
     * {@code GET  /theme-settings} : get all the themeSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeSettings in body.
     */
    @GetMapping("/api/theme-settings")
    public List<ThemeSettings> getAllThemeSettings() {
        log.debug("REST request to get all ThemeSettings");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ThemeSettings> themeSettingsOptional = themeSettingsService.findByShop(shop);
        List<ThemeSettings> themeSettings = new ArrayList<>();
        themeSettingsOptional.ifPresent(themeSettings::add);
        return themeSettings;
    }

    /**
     * {@code GET  /theme-settings/:id} : get the "id" themeSettings.
     *
     * @param id the id of the themeSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api/theme-settings/{id}")
    public ResponseEntity<ThemeSettings> getThemeSettings(@PathVariable Long id) {
        log.debug("REST request to get ThemeSettings : {}", id);
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ThemeSettings> themeSettings = themeSettingsService.findByShop(shop);
        if (!themeSettings.isPresent()) {
            ThemeSettings themeSettings1 = new ThemeSettings();
            themeSettings1.setShop(shop);
            themeSettings1.setSkip_setting_theme(false);
            ThemeSettings result = themeSettingsService.save(themeSettings1);
            return ResponseUtil.wrapOrNotFound(Optional.of(result));
        }

        return ResponseUtil.wrapOrNotFound(themeSettings);
    }

    /**
     * {@code DELETE  /theme-settings/:id} : delete the "id" themeSettings.
     *
     * @param id the id of the themeSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api/theme-settings/{id}")
    public ResponseEntity<Void> deleteThemeSettings(@PathVariable Long id) {
        log.debug("REST request to delete ThemeSettings : {}", id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ThemeSettings> themeSettings = themeSettingsService.findByShop(shop);

        if (themeSettings.isPresent()) {
            if (!themeSettings.get().getId().equals(id)) {
                throw new BadRequestAlertException("", "", "");
            }
        }

        themeSettingsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createAlert(applicationName, "Theme Deleted.", "")).build();
    }

    @PutMapping("/api/theme-settings/theme/{themeName}")
    public ResponseEntity<ThemeSettings> updateThemeName(@PathVariable String themeName) throws URISyntaxException, IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        ThemeSettings themeSettings = themeSettingsService.findByShop(shop).get();

        themeSettings.setThemeName(themeName);
        if (themeSettings.getShopifyThemeInstallationVersion() == ShopifyThemeInstallationVersion.V2) {
            themeSettings.setThemeV2Saved(true);
        }

        themeSettingsService.save(themeSettings);

        updateJavascript(shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Theme name updated.", ""))
            .body(themeSettings);
    }

    @PutMapping("/api/theme-settings/update-to-v2")
    public ResponseEntity<ThemeSettings> updateToV2() throws IOException {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        ThemeSettings themeSettings = themeSettingsService.findByShop(shop).get();

        if(!ShopifyThemeInstallationVersion.V2.equals(themeSettings.getShopifyThemeInstallationVersion())) {
            themeSettings.setShopifyThemeInstallationVersion(ShopifyThemeInstallationVersion.V2);
            themeSettings.setThemeV2Saved(true);

            themeSettingsService.save(themeSettings);

            updateJavascript(shop);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Script version updated.", ""))
            .body(themeSettings);
    }

    private void updateJavascript(String shop) throws IOException {
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
    }

    @CrossOrigin
    @GetMapping("/api/external/v2/theme-settings/regenerate-scripts-for-shop")
    public boolean regenerateScriptForShop(@RequestParam(value = "api_key") String apiKey) {
        log.info("REST request external v2 /api/external/v2/theme-settings/regenerate-scripts-for-shop api_key: {}", apiKey);
        String shop = SecurityUtils.getCurrentUserLogin().get();

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return true;
    }

    @PutMapping("/api/theme-settings/setting/{skipThemeSetting}")
    public ResponseEntity<ThemeSettings> updateThemeSkipSetting(@PathVariable Boolean skipThemeSetting) throws URISyntaxException, IOException {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ThemeSettings themeSettings = themeSettingsService.findByShop(shop).get();

        themeSettings.setSkip_setting_theme(skipThemeSetting);

        themeSettingsService.save(themeSettings);


        updateJavascript(shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Theme settings updated.", ""))
            .body(themeSettings);
    }

    @GetMapping("/api/theme-settings/generate-scripts-for-theme")
    public boolean regenerateScriptForShop(@RequestParam(value = "themeId") Long themeId) {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop, themeId);

        return true;
    }
}
