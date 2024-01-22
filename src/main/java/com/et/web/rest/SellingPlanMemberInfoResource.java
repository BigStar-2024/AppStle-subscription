package com.et.web.rest;

import com.et.domain.SubscriptionGroupPlan;
import com.et.security.SecurityUtils;
import com.et.service.SellingPlanMemberInfoService;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.dto.FrequencyInfoDTO;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.utils.CommonUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.SellingPlanMemberInfoDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.SellingPlanMemberInfo}.
 */
@RestController
@RequestMapping("/api")
public class SellingPlanMemberInfoResource {

    private final Logger log = LoggerFactory.getLogger(SellingPlanMemberInfoResource.class);

    private static final String ENTITY_NAME = "sellingPlanMemberInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    private final SellingPlanMemberInfoService sellingPlanMemberInfoService;

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    public SellingPlanMemberInfoResource(SellingPlanMemberInfoService sellingPlanMemberInfoService) {
        this.sellingPlanMemberInfoService = sellingPlanMemberInfoService;
    }

    /**
     * {@code POST  /selling-plan-member-infos} : Create a new sellingPlanMemberInfo.
     *
     * @param sellingPlanMemberInfoDTO the sellingPlanMemberInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sellingPlanMemberInfoDTO, or with status {@code 400 (Bad Request)} if the sellingPlanMemberInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/selling-plan-member-infos")
    public ResponseEntity<SellingPlanMemberInfoDTO> createSellingPlanMemberInfo(@RequestBody SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SellingPlanMemberInfo : {}", sellingPlanMemberInfoDTO);
        if (sellingPlanMemberInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new sellingPlanMemberInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String shop = commonUtils.getShop();
        sellingPlanMemberInfoDTO.setShop(shop);
        SellingPlanMemberInfoDTO result = sellingPlanMemberInfoService.save(sellingPlanMemberInfoDTO);
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return ResponseEntity.created(new URI("/api/selling-plan-member-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /selling-plan-member-infos} : Updates an existing sellingPlanMemberInfo.
     *
     * @param sellingPlanMemberInfoDTO the sellingPlanMemberInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sellingPlanMemberInfoDTO,
     * or with status {@code 400 (Bad Request)} if the sellingPlanMemberInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sellingPlanMemberInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/selling-plan-member-infos")
    public ResponseEntity<SellingPlanMemberInfoDTO> updateSellingPlanMemberInfo(@Valid @RequestBody SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SellingPlanMemberInfo : {}", sellingPlanMemberInfoDTO);
        if (sellingPlanMemberInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = commonUtils.getShop();
        sellingPlanMemberInfoDTO.setShop(shop);
        SellingPlanMemberInfoDTO result = sellingPlanMemberInfoService.save(sellingPlanMemberInfoDTO);
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sellingPlanMemberInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /selling-plan-member-infos} : get all the sellingPlanMemberInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sellingPlanMemberInfos in body.
     */
    @GetMapping("/selling-plan-member-infos")
    public List<SubscriptionGroupPlanDTO> getAllSellingPlanMember() {
        log.debug("REST request to get all SellingPlanMemberInfos");
        String shop = commonUtils.getShop();
        return subscriptionGroupPlanService.findByShop(shop);
    }
    @GetMapping("/selling-plan-member-infos/get-info")
    public List<SellingPlanMemberInfoDTO> getAllSellingPlanMemberInfos() {
        log.debug("REST request to get all SellingPlanMemberInfos");
        String shop = commonUtils.getShop();
        return sellingPlanMemberInfoService.findByShop(shop);
    }

    /**
     * {@code GET  /selling-plan-member-infos/:id} : get the "id" sellingPlanMemberInfo.
     *
     * @param id the id of the sellingPlanMemberInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sellingPlanMemberInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/selling-plan-member-infos/{id}")
    public ResponseEntity<SellingPlanMemberInfoDTO> getSellingPlanMemberInfo(@PathVariable Long id) {
        log.debug("REST request to get SellingPlanMemberInfo : {}", id);
        String shop = commonUtils.getShop();
        Optional<SellingPlanMemberInfoDTO> sellingPlanMemberInfoDTO = sellingPlanMemberInfoService.findOneBySellingPlanId(shop, id);
        return ResponseUtil.wrapOrNotFound(sellingPlanMemberInfoDTO);
    }

    /**
     * {@code DELETE  /selling-plan-member-infos/:id} : delete the "id" sellingPlanMemberInfo.
     *
     * @param id the id of the sellingPlanMemberInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/selling-plan-member-infos/{id}")
    public ResponseEntity<Void> deleteSellingPlanMemberInfo(@PathVariable Long id) {
        log.debug("REST request to delete SellingPlanMemberInfo : {}", id);
        String shop = commonUtils.getShop();
        Optional<SellingPlanMemberInfoDTO> sellingPlanMemberInfoDTO = sellingPlanMemberInfoService.findOneBySellingPlanId(shop, id);

        if (sellingPlanMemberInfoDTO.isPresent()) {
            if (!sellingPlanMemberInfoDTO.get().getShop().equals(shop)) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
        }

        sellingPlanMemberInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
