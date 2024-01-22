package com.et.web.rest;

import com.et.service.MembershipDiscountCollectionsService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.MembershipDiscountCollectionsDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing {@link com.et.domain.MembershipDiscountCollections}.
 */
@RestController
@RequestMapping("/api")
public class MembershipDiscountCollectionsResource {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountCollectionsResource.class);

    private static final String ENTITY_NAME = "membershipDiscountCollections";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipDiscountCollectionsService membershipDiscountCollectionsService;

    public MembershipDiscountCollectionsResource(MembershipDiscountCollectionsService membershipDiscountCollectionsService) {
        this.membershipDiscountCollectionsService = membershipDiscountCollectionsService;
    }

    /**
     * {@code POST  /membership-discount-collections} : Create a new membershipDiscountCollections.
     *
     * @param membershipDiscountCollectionsDTO the membershipDiscountCollectionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipDiscountCollectionsDTO, or with status {@code 400 (Bad Request)} if the membershipDiscountCollections has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-discount-collections")
    public ResponseEntity<MembershipDiscountCollectionsDTO> createMembershipDiscountCollections(@Valid @RequestBody MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO) throws URISyntaxException {
        log.debug("REST request to save MembershipDiscountCollections : {}", membershipDiscountCollectionsDTO);
        if (membershipDiscountCollectionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipDiscountCollections cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipDiscountCollectionsDTO result = membershipDiscountCollectionsService.save(membershipDiscountCollectionsDTO);
        return ResponseEntity.created(new URI("/api/membership-discount-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-discount-collections} : Updates an existing membershipDiscountCollections.
     *
     * @param membershipDiscountCollectionsDTO the membershipDiscountCollectionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipDiscountCollectionsDTO,
     * or with status {@code 400 (Bad Request)} if the membershipDiscountCollectionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipDiscountCollectionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-discount-collections")
    public ResponseEntity<MembershipDiscountCollectionsDTO> updateMembershipDiscountCollections(@Valid @RequestBody MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO) throws URISyntaxException {
        log.debug("REST request to update MembershipDiscountCollections : {}", membershipDiscountCollectionsDTO);
        if (membershipDiscountCollectionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipDiscountCollectionsDTO result = membershipDiscountCollectionsService.save(membershipDiscountCollectionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipDiscountCollectionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /membership-discount-collections} : get all the membershipDiscountCollections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipDiscountCollections in body.
     */
    @GetMapping("/membership-discount-collections")
    public List<MembershipDiscountCollectionsDTO> getAllMembershipDiscountCollections() {
        log.debug("REST request to get all MembershipDiscountCollections");
        return membershipDiscountCollectionsService.findAll();
    }

    /**
     * {@code GET  /membership-discount-collections/:id} : get the "id" membershipDiscountCollections.
     *
     * @param id the id of the membershipDiscountCollectionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipDiscountCollectionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-discount-collections/{id}")
    public ResponseEntity<MembershipDiscountCollectionsDTO> getMembershipDiscountCollections(@PathVariable Long id) {
        log.debug("REST request to get MembershipDiscountCollections : {}", id);
        Optional<MembershipDiscountCollectionsDTO> membershipDiscountCollectionsDTO = membershipDiscountCollectionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipDiscountCollectionsDTO);
    }

    /**
     * {@code DELETE  /membership-discount-collections/:id} : delete the "id" membershipDiscountCollections.
     *
     * @param id the id of the membershipDiscountCollectionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-discount-collections/{id}")
    public ResponseEntity<Void> deleteMembershipDiscountCollections(@PathVariable Long id) {
        log.debug("REST request to delete MembershipDiscountCollections : {}", id);
        membershipDiscountCollectionsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
