package com.et.web.rest;

import com.et.service.MembershipDiscountProductsService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.MembershipDiscountProductsDTO;

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
 * REST controller for managing {@link com.et.domain.MembershipDiscountProducts}.
 */
@RestController
@RequestMapping("/api")
public class MembershipDiscountProductsResource {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountProductsResource.class);

    private static final String ENTITY_NAME = "membershipDiscountProducts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipDiscountProductsService membershipDiscountProductsService;

    public MembershipDiscountProductsResource(MembershipDiscountProductsService membershipDiscountProductsService) {
        this.membershipDiscountProductsService = membershipDiscountProductsService;
    }

    /**
     * {@code POST  /membership-discount-products} : Create a new membershipDiscountProducts.
     *
     * @param membershipDiscountProductsDTO the membershipDiscountProductsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipDiscountProductsDTO, or with status {@code 400 (Bad Request)} if the membershipDiscountProducts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-discount-products")
    public ResponseEntity<MembershipDiscountProductsDTO> createMembershipDiscountProducts(@Valid @RequestBody MembershipDiscountProductsDTO membershipDiscountProductsDTO) throws URISyntaxException {
        log.debug("REST request to save MembershipDiscountProducts : {}", membershipDiscountProductsDTO);
        if (membershipDiscountProductsDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipDiscountProducts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipDiscountProductsDTO result = membershipDiscountProductsService.save(membershipDiscountProductsDTO);
        return ResponseEntity.created(new URI("/api/membership-discount-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-discount-products} : Updates an existing membershipDiscountProducts.
     *
     * @param membershipDiscountProductsDTO the membershipDiscountProductsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipDiscountProductsDTO,
     * or with status {@code 400 (Bad Request)} if the membershipDiscountProductsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipDiscountProductsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-discount-products")
    public ResponseEntity<MembershipDiscountProductsDTO> updateMembershipDiscountProducts(@Valid @RequestBody MembershipDiscountProductsDTO membershipDiscountProductsDTO) throws URISyntaxException {
        log.debug("REST request to update MembershipDiscountProducts : {}", membershipDiscountProductsDTO);
        if (membershipDiscountProductsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipDiscountProductsDTO result = membershipDiscountProductsService.save(membershipDiscountProductsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipDiscountProductsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /membership-discount-products} : get all the membershipDiscountProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipDiscountProducts in body.
     */
    @GetMapping("/membership-discount-products")
    public List<MembershipDiscountProductsDTO> getAllMembershipDiscountProducts() {
        log.debug("REST request to get all MembershipDiscountProducts");
        return membershipDiscountProductsService.findAll();
    }

    /**
     * {@code GET  /membership-discount-products/:id} : get the "id" membershipDiscountProducts.
     *
     * @param id the id of the membershipDiscountProductsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipDiscountProductsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-discount-products/{id}")
    public ResponseEntity<MembershipDiscountProductsDTO> getMembershipDiscountProducts(@PathVariable Long id) {
        log.debug("REST request to get MembershipDiscountProducts : {}", id);
        Optional<MembershipDiscountProductsDTO> membershipDiscountProductsDTO = membershipDiscountProductsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipDiscountProductsDTO);
    }

    /**
     * {@code DELETE  /membership-discount-products/:id} : delete the "id" membershipDiscountProducts.
     *
     * @param id the id of the membershipDiscountProductsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-discount-products/{id}")
    public ResponseEntity<Void> deleteMembershipDiscountProducts(@PathVariable Long id) {
        log.debug("REST request to delete MembershipDiscountProducts : {}", id);
        membershipDiscountProductsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
