package com.et.web.rest;

import com.et.service.MemberOnlyService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.MemberOnlyDTO;

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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.MemberOnly}.
 */
@RestController
@RequestMapping("/api")
public class MemberOnlyResource {

    private final Logger log = LoggerFactory.getLogger(MemberOnlyResource.class);

    private static final String ENTITY_NAME = "memberOnly";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CommonUtils commonUtils;

    private final MemberOnlyService memberOnlyService;

    public MemberOnlyResource(MemberOnlyService memberOnlyService) {
        this.memberOnlyService = memberOnlyService;
    }

    /**
     * {@code POST  /member-onlies} : Create a new memberOnly.
     *
     * @param memberOnlyDTO the memberOnlyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberOnlyDTO, or with status {@code 400 (Bad Request)} if the memberOnly has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-onlies")
    public ResponseEntity<MemberOnlyDTO> createMemberOnly(@Valid @RequestBody MemberOnlyDTO memberOnlyDTO) throws URISyntaxException {
        log.debug("REST request to save MemberOnly : {}", memberOnlyDTO);
        if (memberOnlyDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberOnly cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        if(!shop.equals(memberOnlyDTO.getShop())){
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "idnull");
        }
        MemberOnlyDTO result = memberOnlyService.save(memberOnlyDTO);
        return ResponseEntity.created(new URI("/api/member-onlies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-onlies} : Updates an existing memberOnly.
     *
     * @param memberOnlyDTO the memberOnlyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberOnlyDTO,
     * or with status {@code 400 (Bad Request)} if the memberOnlyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberOnlyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-onlies")
    public ResponseEntity<MemberOnlyDTO> updateMemberOnly(@Valid @RequestBody MemberOnlyDTO memberOnlyDTO) throws URISyntaxException {
        log.debug("REST request to update MemberOnly : {}", memberOnlyDTO);
        if (memberOnlyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = commonUtils.getShop();
        if(!shop.equals(memberOnlyDTO.getShop())){
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "idnull");
        }
        MemberOnlyDTO result = memberOnlyService.save(memberOnlyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, memberOnlyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-onlies} : get all the memberOnlies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberOnlies in body.
     */
    @GetMapping("/member-onlies")
    public List<MemberOnlyDTO> getAllMemberOnlies() {
        log.debug("REST request to get all MemberOnlies");
        String shop = commonUtils.getShop();
        return memberOnlyService.findAllByShop(shop);
    }

    /**
     * {@code GET  /member-onlies/:id} : get the "id" memberOnly.
     *
     * @param id the id of the memberOnlyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberOnlyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-onlies/{id}")
    public ResponseEntity<MemberOnlyDTO> getMemberOnly(@PathVariable Long id) {
        log.debug("REST request to get MemberOnly : {}", id);
        Optional<MemberOnlyDTO> memberOnlyDTO = memberOnlyService.findOne(id);
        String shop = commonUtils.getShop();
        if(memberOnlyDTO.isPresent() && !shop.equals(memberOnlyDTO.get().getShop())){
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(memberOnlyDTO);
    }

    /**
     * {@code DELETE  /member-onlies/:id} : delete the "id" memberOnly.
     *
     * @param id the id of the memberOnlyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-onlies/{id}")
    public ResponseEntity<Void> deleteMemberOnly(@PathVariable Long id) {
        log.debug("REST request to delete MemberOnly : {}", id);
        Optional<MemberOnlyDTO> memberOnlyDTO = memberOnlyService.findOne(id);
        String shop = commonUtils.getShop();
        if(memberOnlyDTO.isPresent() && !shop.equals(memberOnlyDTO.get().getShop())){
            throw new BadRequestAlertException("invalid id", ENTITY_NAME, "idnull");
        }
        memberOnlyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
