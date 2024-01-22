package com.et.web.rest;

import com.et.domain.SocialConnection;
import com.et.service.SocialConnectionService;
import com.et.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link SocialConnection}.
 */
@RestController
@RequestMapping("/api")
public class SocialConnectionResource {

    private final Logger log = LoggerFactory.getLogger(SocialConnectionResource.class);

    private static final String ENTITY_NAME = "socialConnection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialConnectionService socialConnectionService;

    public SocialConnectionResource(SocialConnectionService socialConnectionService) {
        this.socialConnectionService = socialConnectionService;
    }

    /**
     * {@code POST  /social-connections} : Create a new socialConnection.
     *
     * @param socialConnection the socialConnection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialConnection, or with status {@code 400 (Bad Request)} if the socialConnection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
   /* @PostMapping("/social-connections")
    public ResponseEntity<SocialConnection> createSocialConnection(@Valid @RequestBody SocialConnection socialConnection) throws URISyntaxException {
        log.debug("REST request to save SocialConnection : {}", socialConnection);
        if (socialConnection.getId() != null) {
            throw new BadRequestAlertException("A new socialConnection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialConnection result = socialConnectionService.save(socialConnection);
        return ResponseEntity.created(new URI("/api/social-connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /social-connections} : Updates an existing socialConnection.
     *
     * @param socialConnection the socialConnection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialConnection,
     * or with status {@code 400 (Bad Request)} if the socialConnection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialConnection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PutMapping("/social-connections")
    public ResponseEntity<SocialConnection> updateSocialConnection(@Valid @RequestBody SocialConnection socialConnection) throws URISyntaxException {
        log.debug("REST request to update SocialConnection : {}", socialConnection);
        if (socialConnection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SocialConnection result = socialConnectionService.save(socialConnection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, socialConnection.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code GET  /social-connections} : get all the socialConnections.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialConnections in body.
     */
    /*@GetMapping("/social-connections")
    public ResponseEntity<List<SocialConnection>> getAllSocialConnections(Pageable pageable) {
        log.debug("REST request to get a page of SocialConnections");
        Page<SocialConnection> page = socialConnectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    /**
     * {@code GET  /social-connections/:id} : get the "id" socialConnection.
     *
     * @param id the id of the socialConnection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialConnection, or with status {@code 404 (Not Found)}.
     */
    /*@GetMapping("/social-connections/{id}")
    public ResponseEntity<SocialConnection> getSocialConnection(@PathVariable Long id) {
        log.debug("REST request to get SocialConnection : {}", id);
        Optional<SocialConnection> socialConnection = socialConnectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialConnection);
    }*/

    /**
     * {@code DELETE  /social-connections/:id} : delete the "id" socialConnection.
     *
     * @param id the id of the socialConnection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

}
