package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.BundleSettingService;
import com.et.service.ShopCustomizationService;
import com.et.service.dto.BundleSettingDTO;
import com.et.utils.CommonUtils;
import com.et.utils.ShopLabelUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.ObjectUtils;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.BundleSetting}.
 */
@RestController
@RequestMapping("/api")
public class BundleSettingResource {

    private final Logger log = LoggerFactory.getLogger(BundleSettingResource.class);

    private static final String ENTITY_NAME = "bundleSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CommonUtils commonUtils;

    private final BundleSettingService bundleSettingService;

    @Autowired
    ShopCustomizationService shopCustomizationService;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private ShopLabelUtils shopLabelUtils;

    public BundleSettingResource(BundleSettingService bundleSettingService) {
        this.bundleSettingService = bundleSettingService;
    }

    /**
     * {@code POST  /bundle-settings} : Create a new bundleSetting.
     *
     * @param bundleSettingDTO the bundleSettingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bundleSettingDTO, or with status {@code 400 (Bad Request)} if the bundleSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bundle-settings")
    public ResponseEntity<BundleSettingDTO> createBundleSetting(@RequestBody BundleSettingDTO bundleSettingDTO) throws URISyntaxException {
        log.debug("REST request to save BundleSetting : {}", bundleSettingDTO);
        if (bundleSettingDTO.getId() != null) {
            throw new BadRequestAlertException("A new bundleSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        bundleSettingDTO.setShop(shop);

        BundleSettingDTO result = bundleSettingService.save(bundleSettingDTO);

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.bn.", "BUNDLE", bundleSettingDTO.getAdditionalProperties());

        return ResponseEntity.created(new URI("/api/bundle-settings/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Bundle settings created successfully.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /bundle-settings} : Updates an existing bundleSetting.
     *
     * @param bundleSettingDTO the bundleSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bundleSettingDTO,
     * or with status {@code 400 (Bad Request)} if the bundleSettingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bundleSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bundle-settings")
    public ResponseEntity<BundleSettingDTO> updateBundleSetting(@RequestBody BundleSettingDTO bundleSettingDTO) throws URISyntaxException {
        log.debug("REST request to update BundleSetting : {}", bundleSettingDTO);
        if (bundleSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();
        bundleSettingDTO.setShop(shop);

        BundleSettingDTO result = bundleSettingService.save(bundleSettingDTO);

        if (ObjectUtils.isNotEmpty(bundleSettingDTO.getShopCustomizationData())) {
            shopCustomizationService.updateShopCustomizationData(bundleSettingDTO.getShopCustomizationData(), shop);
        }

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        shopLabelUtils.addNewLabels(shop, "appstle.subscription.bn.", "BUNDLE", bundleSettingDTO.getAdditionalProperties());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Bundle settings updated successfully.", ""))
            .body(result);
    }

    /**
     * {@code GET  /bundle-settings} : get all the bundleSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bundleSettings in body.
     */
    @GetMapping("/bundle-settings")
    public ResponseEntity<List<BundleSettingDTO>> getAllBundleSettings(Pageable pageable) {
        log.debug("REST request to get a page of BundleSettings");

        String shop = commonUtils.getShop();

        Page<BundleSettingDTO> page = bundleSettingService.findAllByShop(shop, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bundle-settings/:id} : get the "id" bundleSetting.
     *
     * @param id the id of the bundleSettingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bundleSettingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bundle-settings/{id}")
    public ResponseEntity<BundleSettingDTO> getBundleSetting(@PathVariable Long id) {
        log.debug("REST request to get BundleSetting : {}", id);
        String shop = commonUtils.getShop();
        Optional<BundleSettingDTO> bundleSettingDTO = bundleSettingService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(bundleSettingDTO);
    }


    @GetMapping("/bundle-settings/by-shop")
    public ResponseEntity<BundleSettingDTO> getBundleSettingByShop() {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<BundleSettingDTO> bundleSettingDTO = bundleSettingService.findByShop(shop);
        if (bundleSettingDTO.isEmpty()) {
            BundleSettingDTO bundleSettingNew = new BundleSettingDTO();
            bundleSettingNew.setShop(shop);
            BundleSettingDTO settingDTO = bundleSettingService.save(bundleSettingNew);
            return ResponseUtil.wrapOrNotFound(Optional.of(settingDTO));
        }
        return ResponseUtil.wrapOrNotFound(bundleSettingDTO);
    }

    /**
     * {@code DELETE  /bundle-settings/:id} : delete the "id" bundleSetting.
     *
     * @param id the id of the bundleSettingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
//    @DeleteMapping("/bundle-settings/{id}")
//    public ResponseEntity<Void> deleteBundleSetting(@PathVariable Long id) {
//        log.debug("REST request to delete BundleSetting : {}", id);
//        bundleSettingService.delete(id);
//
//        String shop =commonUtils.getShop();
//        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);
//
//        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
//    }
}
