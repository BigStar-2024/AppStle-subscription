package com.et.web.rest;

import com.et.service.AsyncUpdateEventProcessingService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.AsyncUpdateEventProcessingDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.AsyncUpdateEventProcessing}.
 */
@RestController
@RequestMapping("/api")
public class AsyncUpdateEventProcessingResource {

    private final Logger log = LoggerFactory.getLogger(AsyncUpdateEventProcessingResource.class);

    private static final String ENTITY_NAME = "asyncUpdateEventProcessing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsyncUpdateEventProcessingService asyncUpdateEventProcessingService;

    public AsyncUpdateEventProcessingResource(AsyncUpdateEventProcessingService asyncUpdateEventProcessingService) {
        this.asyncUpdateEventProcessingService = asyncUpdateEventProcessingService;
    }

    /**
     * {@code POST  /async-update-event-processings} : Create a new asyncUpdateEventProcessing.
     *
     * @param asyncUpdateEventProcessingDTO the asyncUpdateEventProcessingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asyncUpdateEventProcessingDTO, or with status {@code 400 (Bad Request)} if the asyncUpdateEventProcessing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/async-update-event-processings")
    public ResponseEntity<AsyncUpdateEventProcessingDTO> createAsyncUpdateEventProcessing(@Valid @RequestBody AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO) throws URISyntaxException {
        log.debug("REST request to save AsyncUpdateEventProcessing : {}", asyncUpdateEventProcessingDTO);
        if (asyncUpdateEventProcessingDTO.getId() != null) {
            throw new BadRequestAlertException("A new asyncUpdateEventProcessing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AsyncUpdateEventProcessingDTO result = asyncUpdateEventProcessingService.save(asyncUpdateEventProcessingDTO);
        return ResponseEntity.created(new URI("/api/async-update-event-processings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /async-update-event-processings} : Updates an existing asyncUpdateEventProcessing.
     *
     * @param asyncUpdateEventProcessingDTO the asyncUpdateEventProcessingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asyncUpdateEventProcessingDTO,
     * or with status {@code 400 (Bad Request)} if the asyncUpdateEventProcessingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asyncUpdateEventProcessingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/async-update-event-processings")
    public ResponseEntity<AsyncUpdateEventProcessingDTO> updateAsyncUpdateEventProcessing(@Valid @RequestBody AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO) throws URISyntaxException {
        log.debug("REST request to update AsyncUpdateEventProcessing : {}", asyncUpdateEventProcessingDTO);
        if (asyncUpdateEventProcessingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AsyncUpdateEventProcessingDTO result = asyncUpdateEventProcessingService.save(asyncUpdateEventProcessingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, asyncUpdateEventProcessingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /async-update-event-processings} : get all the asyncUpdateEventProcessings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asyncUpdateEventProcessings in body.
     */
    @GetMapping("/async-update-event-processings")
    public ResponseEntity<List<AsyncUpdateEventProcessingDTO>> getAllAsyncUpdateEventProcessings(Pageable pageable) {
        log.debug("REST request to get a page of AsyncUpdateEventProcessings");
        Page<AsyncUpdateEventProcessingDTO> page = asyncUpdateEventProcessingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /async-update-event-processings/:id} : get the "id" asyncUpdateEventProcessing.
     *
     * @param id the id of the asyncUpdateEventProcessingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asyncUpdateEventProcessingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/async-update-event-processings/{id}")
    public ResponseEntity<AsyncUpdateEventProcessingDTO> getAsyncUpdateEventProcessing(@PathVariable Long id) {
        log.debug("REST request to get AsyncUpdateEventProcessing : {}", id);
        Optional<AsyncUpdateEventProcessingDTO> asyncUpdateEventProcessingDTO = asyncUpdateEventProcessingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asyncUpdateEventProcessingDTO);
    }

    /**
     * {@code DELETE  /async-update-event-processings/:id} : delete the "id" asyncUpdateEventProcessing.
     *
     * @param id the id of the asyncUpdateEventProcessingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/async-update-event-processings/{id}")
    public ResponseEntity<Void> deleteAsyncUpdateEventProcessing(@PathVariable Long id) {
        log.debug("REST request to delete AsyncUpdateEventProcessing : {}", id);
        asyncUpdateEventProcessingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
