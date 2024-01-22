package com.et.web.rest;

import com.et.service.BundleDynamicScriptService;
import com.et.service.dto.CustomerPortalDynamicScriptDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.BundleDynamicScriptDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.BundleDynamicScript}.
 */
@RestController
@RequestMapping("/api")
public class BundleDynamicScriptResource {

    private final Logger log = LoggerFactory.getLogger(BundleDynamicScriptResource.class);

    private static final String ENTITY_NAME = "bundleDynamicScript";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BundleDynamicScriptService bundleDynamicScriptService;

    public BundleDynamicScriptResource(BundleDynamicScriptService bundleDynamicScriptService) {
        this.bundleDynamicScriptService = bundleDynamicScriptService;
    }

    /**
     * {@code POST  /bundle-dynamic-scripts} : Create a new bundleDynamicScript.
     *
     * @param bundleDynamicScriptDTO the bundleDynamicScriptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bundleDynamicScriptDTO, or with status {@code 400 (Bad Request)} if the bundleDynamicScript has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bundle-dynamic-scripts")
    public ResponseEntity<BundleDynamicScriptDTO> createBundleDynamicScript(@Valid @RequestBody BundleDynamicScriptDTO bundleDynamicScriptDTO) throws URISyntaxException {
        log.debug("REST request to save BundleDynamicScript : {}", bundleDynamicScriptDTO);
        if (bundleDynamicScriptDTO.getId() != null) {
            throw new BadRequestAlertException("A new bundleDynamicScript cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<BundleDynamicScriptDTO> bundleDynamicScriptDTO1 = bundleDynamicScriptService.findByShop(commonUtils.getShop());

        if (bundleDynamicScriptDTO1.isPresent()) {
            throw new BadRequestAlertException("A new bundleDynamicScript cannot already have an ID", ENTITY_NAME, "idexists");
        }

        BundleDynamicScriptDTO result = bundleDynamicScriptService.save(bundleDynamicScriptDTO);
        return ResponseEntity.created(new URI("/api/bundle-dynamic-scripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bundle-dynamic-scripts} : Updates an existing bundleDynamicScript.
     *
     * @param bundleDynamicScriptDTO the bundleDynamicScriptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bundleDynamicScriptDTO,
     * or with status {@code 400 (Bad Request)} if the bundleDynamicScriptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bundleDynamicScriptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bundle-dynamic-scripts")
    public ResponseEntity<BundleDynamicScriptDTO> updateBundleDynamicScript(@Valid @RequestBody BundleDynamicScriptDTO bundleDynamicScriptDTO) throws URISyntaxException {
        log.debug("REST request to update BundleDynamicScript : {}", bundleDynamicScriptDTO);
        if (bundleDynamicScriptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!commonUtils.getShop().equals(bundleDynamicScriptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        BundleDynamicScriptDTO result = bundleDynamicScriptService.save(bundleDynamicScriptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bundleDynamicScriptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bundle-dynamic-scripts} : get all the bundleDynamicScripts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bundleDynamicScripts in body.
     */
    @GetMapping("/bundle-dynamic-scripts")
    public List<BundleDynamicScriptDTO> getAllBundleDynamicScripts() {
        log.debug("REST request to get all BundleDynamicScripts");
        List<BundleDynamicScriptDTO> bundleDynamicScriptDTOList = new ArrayList<>();
        String shop = commonUtils.getShop();
        Optional<BundleDynamicScriptDTO> optionalBundleDynamicScript = bundleDynamicScriptService.findByShop(shop);
        optionalBundleDynamicScript.ifPresent(bundleDynamicScriptDTOList::add);
        return bundleDynamicScriptDTOList;
    }

    @Autowired
    private CommonUtils commonUtils;

    /**
     * {@code GET  /bundle-dynamic-scripts/:id} : get the "id" bundleDynamicScript.
     *
     * @param id the id of the bundleDynamicScriptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bundleDynamicScriptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bundle-dynamic-scripts/{id}")
    public ResponseEntity<BundleDynamicScriptDTO> getBundleDynamicScript(@PathVariable Long id) {
        log.debug("REST request to get BundleDynamicScript : {}", id);
        String shop = commonUtils.getShop();
        /*Optional<BundleDynamicScriptDTO> bundleDynamicScriptDTO = bundleDynamicScriptService.findByShop(shop);
        return ResponseUtil.wrapOrNotFound(bundleDynamicScriptDTO);*/

        Optional<BundleDynamicScriptDTO> optionalBundleDynamicScriptDTO = bundleDynamicScriptService.findByShop(shop);
        BundleDynamicScriptDTO other = new BundleDynamicScriptDTO();
        other.setShop(shop);

        BundleDynamicScriptDTO bundleDynamicScriptDTO = optionalBundleDynamicScriptDTO.orElse(other);

        Optional<BundleDynamicScriptDTO> bundleDynamicScriptDTO1 = Optional.of(bundleDynamicScriptDTO);
        return ResponseUtil.wrapOrNotFound(bundleDynamicScriptDTO1);
    }

    /**
     * {@code DELETE  /bundle-dynamic-scripts/:id} : delete the "id" bundleDynamicScript.
     *
     * @param id the id of the bundleDynamicScriptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bundle-dynamic-scripts/{id}")
    public ResponseEntity<Void> deleteBundleDynamicScript(@PathVariable Long id) {
        log.debug("REST request to delete BundleDynamicScript : {}", id);
        bundleDynamicScriptService.deleteByShop(commonUtils.getShop());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
