package com.et.web.rest;

import com.et.domain.CartWidgetSettings;
import com.et.security.SecurityUtils;
import com.et.service.CartWidgetSettingsService;
import com.et.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
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
 * REST controller for managing {@link com.et.domain.CartWidgetSettings}.
 */
@RestController
@RequestMapping("/api")
public class CartWidgetSettingsResource {

    private final Logger log = LoggerFactory.getLogger(CartWidgetSettingsResource.class);

    private static final String ENTITY_NAME = "cartWidgetSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartWidgetSettingsService cartWidgetSettingsService;

    public CartWidgetSettingsResource(CartWidgetSettingsService cartWidgetSettingsService) {
        this.cartWidgetSettingsService = cartWidgetSettingsService;
    }

    /**
     * {@code POST  /cart-widget-settings} : Create a new cartWidgetSettings.
     *
     * @param cartWidgetSettings the cartWidgetSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartWidgetSettings, or with status {@code 400 (Bad Request)} if the cartWidgetSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-widget-settings")
    public ResponseEntity<CartWidgetSettings> createCartWidgetSettings(@Valid @RequestBody CartWidgetSettings cartWidgetSettings) throws URISyntaxException {
        log.debug("REST request to save CartWidgetSettings : {}", cartWidgetSettings);
        if (cartWidgetSettings.getId() != null) {
            throw new BadRequestAlertException("A new cartWidgetSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(cartWidgetSettings.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CartWidgetSettings result = cartWidgetSettingsService.save(cartWidgetSettings);
        return ResponseEntity.created(new URI("/api/cart-widget-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-widget-settings} : Updates an existing cartWidgetSettings.
     *
     * @param cartWidgetSettings the cartWidgetSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartWidgetSettings,
     * or with status {@code 400 (Bad Request)} if the cartWidgetSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartWidgetSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-widget-settings")
    public ResponseEntity<CartWidgetSettings> updateCartWidgetSettings(@Valid @RequestBody CartWidgetSettings cartWidgetSettings) throws URISyntaxException {
        log.debug("REST request to update CartWidgetSettings : {}", cartWidgetSettings);
        if (cartWidgetSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(cartWidgetSettings.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CartWidgetSettings result = cartWidgetSettingsService.save(cartWidgetSettings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartWidgetSettings.getShop()))
            .body(result);
    }


    /**
     * {@code GET  /cart-widget-settings/:id} : get the "id" cartWidgetSettings.
     *
     * @param id the id of the cartWidgetSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartWidgetSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-widget-settings/{id}")
    public ResponseEntity<CartWidgetSettings> getCartWidgetSettings(@PathVariable Long id) {

            String shopName = SecurityUtils.getCurrentUserLogin().get();


        Optional<CartWidgetSettings> cartWidgetSettings = cartWidgetSettingsService.findByShop(shopName);
        return ResponseUtil.wrapOrNotFound(cartWidgetSettings);
    }

    /**
     * {@code DELETE  /cart-widget-settings/:id} : delete the "id" cartWidgetSettings.
     *
     * @param id the id of the cartWidgetSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-widget-settings/{id}")
    public ResponseEntity<Void> deleteCartWidgetSettings(@PathVariable Long id) {
        log.debug("REST request to delete CartWidgetSettings : {}", id);
        cartWidgetSettingsService.deleteByShop(SecurityUtils.getCurrentUserLogin().get());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
