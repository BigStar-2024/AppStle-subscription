package com.et.web.rest;

import com.et.service.OnboardingInfoService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.OnboardingInfoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.OnboardingInfo}.
 */
@RestController
@RequestMapping("/api")
public class OnboardingInfoResource {

    private final Logger log = LoggerFactory.getLogger(OnboardingInfoResource.class);

    private static final String ENTITY_NAME = "onboardingInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OnboardingInfoService onboardingInfoService;

    @Autowired
    private CommonUtils commonUtils;

    public OnboardingInfoResource(OnboardingInfoService onboardingInfoService) {
        this.onboardingInfoService = onboardingInfoService;
    }

    /**
     * {@code POST  /onboarding-infos} : Create a new onboardingInfo.
     *
     * @param onboardingInfoDTO the onboardingInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new onboardingInfoDTO, or with status {@code 400 (Bad Request)} if the onboardingInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/onboarding-infos")
    public ResponseEntity<OnboardingInfoDTO> createOnboardingInfo(@RequestBody OnboardingInfoDTO onboardingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save OnboardingInfo : {}", onboardingInfoDTO);
        if (onboardingInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new onboardingInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OnboardingInfoDTO result = onboardingInfoService.save(onboardingInfoDTO);

        // Don't allow creating
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        /*
        return ResponseEntity.created(new URI("/api/onboarding-infos/" + result.getId()))
            .body(result);
         */
    }

    /**
     * {@code PUT  /onboarding-infos} : Updates an existing onboardingInfo.
     *
     * @param onboardingInfoDTO the onboardingInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated onboardingInfoDTO,
     * or with status {@code 400 (Bad Request)} if the onboardingInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the onboardingInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/onboarding-infos")
    public ResponseEntity<OnboardingInfoDTO> updateOnboardingInfo(@RequestBody OnboardingInfoDTO onboardingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update OnboardingInfo : {}", onboardingInfoDTO);
        if (onboardingInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shopName = commonUtils.getShop();

        if (!shopName.equals(onboardingInfoDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        OnboardingInfoDTO result = onboardingInfoService.save(onboardingInfoDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * {@code GET  /onboarding-infos} : get all the onboardingInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of onboardingInfos in body.
     */
    @GetMapping("/onboarding-infos")
    public List<OnboardingInfoDTO> getAllOnboardingInfos() {
        log.debug("REST request to get all OnboardingInfos");
        List<OnboardingInfoDTO> list = new ArrayList<>();
        Optional<OnboardingInfoDTO> onboardingInfoDTOOptional = onboardingInfoService.findByShop(commonUtils.getShop());
        onboardingInfoDTOOptional.ifPresent(list::add);

        return list;
    }

    /**
     * {@code GET  /onboarding-infos/:id} : get the "id" onboardingInfo.
     *
     * @param id the id of the onboardingInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the onboardingInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/onboarding-infos/{id}")
    public ResponseEntity<OnboardingInfoDTO> getOnboardingInfo(@PathVariable Long id) {
        String shopName = commonUtils.getShop();
        log.debug("REST request to get OnboardingInfo : {}", shopName);

        Optional<OnboardingInfoDTO> onboardingInfoDTO = onboardingInfoService.findByShop(shopName);

        return ResponseUtil.wrapOrNotFound(onboardingInfoDTO);
    }

    /**
     * {@code DELETE  /onboarding-infos/:id} : delete the "id" onboardingInfo.
     *
     * @param id the id of the onboardingInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/onboarding-infos/{id}")
    public ResponseEntity<Void> deleteOnboardingInfo(@PathVariable Long id) {
        log.debug("REST request to delete OnboardingInfo : {}", id);
        onboardingInfoService.deleteByShop(commonUtils.getShop());
        return ResponseEntity.noContent().build();
    }
}
