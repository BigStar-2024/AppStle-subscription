package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.BundleRuleService;
import com.et.service.dto.BundleRuleDTO;
import com.et.utils.CommonUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.BundleRule}.
 */
@RestController
@RequestMapping("/api")
public class BundleRuleResource {

    private final Logger log = LoggerFactory.getLogger(BundleRuleResource.class);

    private static final String ENTITY_NAME = "bundleRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BundleRuleService bundleRuleService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    public BundleRuleResource(BundleRuleService bundleRuleService) {
        this.bundleRuleService = bundleRuleService;
    }

    /**
     * {@code POST  /bundle-rules} : Create a new bundleRule.
     *
     * @param bundleRuleDTO the bundleRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bundleRuleDTO, or with status {@code 400 (Bad Request)} if the bundleRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bundle-rules")
    public ResponseEntity<BundleRuleDTO> createBundleRule(@Valid @RequestBody BundleRuleDTO bundleRuleDTO) throws URISyntaxException {
        log.debug("REST request to save BundleRule : {}", bundleRuleDTO);
        if (bundleRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new bundleRule cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        bundleRuleDTO.setShop(shop);

        BundleRuleDTO result = bundleRuleService.save(bundleRuleDTO);

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return ResponseEntity.created(new URI("/api/bundle-rules/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "Bundle rule created successfully.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /bundle-rules} : Updates an existing bundleRule.
     *
     * @param bundleRuleDTO the bundleRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bundleRuleDTO,
     * or with status {@code 400 (Bad Request)} if the bundleRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bundleRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bundle-rules")
    public ResponseEntity<BundleRuleDTO> updateBundleRule(@Valid @RequestBody BundleRuleDTO bundleRuleDTO) throws URISyntaxException {
        log.debug("REST request to update BundleRule : {}", bundleRuleDTO);
        if (bundleRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();
        bundleRuleDTO.setShop(shop);

        BundleRuleDTO result = bundleRuleService.save(bundleRuleDTO);

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Bundle rule updated successfully", ""))
            .body(result);
    }

    /**
     * {@code GET  /bundle-rules} : get all the bundleRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bundleRules in body.
     */
    @GetMapping("/bundle-rules")
    public ResponseEntity<List<BundleRuleDTO>> getAllBundleRules(Pageable pageable) {
        log.debug("REST request to get a page of BundleRules");
        String shop = commonUtils.getShop();
        Page<BundleRuleDTO> page = bundleRuleService.findAllByShop(shop, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/bundle-rules/by-shop")
    public ResponseEntity<List<BundleRuleDTO>> getAllBundleRulesByShop(Pageable pageable) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Page<BundleRuleDTO> page = bundleRuleService.findAllByShop(shop, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bundle-rules/:id} : get the "id" bundleRule.
     *
     * @param id the id of the bundleRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bundleRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bundle-rules/{id}")
    public ResponseEntity<BundleRuleDTO> getBundleRule(@PathVariable Long id) {
        log.debug("REST request to get BundleRule : {}", id);
        Optional<BundleRuleDTO> bundleRuleDTO = bundleRuleService.findOne(id);

        String shop = commonUtils.getShop();
        if(bundleRuleDTO.isPresent() && !shop.equals(bundleRuleDTO.get().getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        return ResponseUtil.wrapOrNotFound(bundleRuleDTO);
    }

    /**
     * {@code DELETE  /bundle-rules/:id} : delete the "id" bundleRule.
     *
     * @param id the id of the bundleRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bundle-rules/{id}")
    public ResponseEntity<Void> deleteBundleRule(@PathVariable Long id) {
        log.debug("REST request to delete BundleRule : {}", id);

        Optional<BundleRuleDTO> bundleRuleDTO = bundleRuleService.findOne(id);

        String shop = commonUtils.getShop();
        if(bundleRuleDTO.isPresent() && !shop.equals(bundleRuleDTO.get().getShop())){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        bundleRuleService.delete(id);

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Bundle rule delete successfully.", "")).build();
    }

    @PutMapping("/bundle-rules/update-index/{id}/{sourceIndex}/{destinationIndex}")
    public ResponseEntity<Boolean> updateIndex(@PathVariable Long id,
                                               @PathVariable Integer sourceIndex,
                                               @PathVariable Integer destinationIndex) throws URISyntaxException {
        Boolean result = bundleRuleService.updateIndex(id, sourceIndex, destinationIndex);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }


    @PutMapping("/bundle-rules/update-status/{id}/{status}")
    public ResponseEntity<Boolean> updateStatus(@PathVariable Long id,
                                                @PathVariable Boolean status) throws URISyntaxException {
        Boolean result = bundleRuleService.updateStatus(id, status);

        String shop = commonUtils.getShop();
        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Bundle status updated successfully.", id.toString()))
            .body(result);
    }


}
