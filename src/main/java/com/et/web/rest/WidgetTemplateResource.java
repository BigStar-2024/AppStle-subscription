package com.et.web.rest;

import com.et.service.WidgetTemplateService;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.WidgetTemplateDTO;

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
 * REST controller for managing {@link com.et.domain.WidgetTemplate}.
 */
@RestController
@RequestMapping("/api")
public class WidgetTemplateResource {

    private final Logger log = LoggerFactory.getLogger(WidgetTemplateResource.class);

    private static final String ENTITY_NAME = "widgetTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WidgetTemplateService widgetTemplateService;

    public WidgetTemplateResource(WidgetTemplateService widgetTemplateService) {
        this.widgetTemplateService = widgetTemplateService;
    }

    /**
     * {@code POST  /widget-templates} : Create a new widgetTemplate.
     *
     * @param widgetTemplateDTO the widgetTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new widgetTemplateDTO, or with status {@code 400 (Bad Request)} if the widgetTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/widget-templates")
    public ResponseEntity<WidgetTemplateDTO> createWidgetTemplate(@Valid @RequestBody WidgetTemplateDTO widgetTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save WidgetTemplate : {}", widgetTemplateDTO);
        if (widgetTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new widgetTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WidgetTemplateDTO result = widgetTemplateService.save(widgetTemplateDTO);
        return ResponseEntity.created(new URI("/api/widget-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /widget-templates} : Updates an existing widgetTemplate.
     *
     * @param widgetTemplateDTO the widgetTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated widgetTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the widgetTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the widgetTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/widget-templates")
    public ResponseEntity<WidgetTemplateDTO> updateWidgetTemplate(@Valid @RequestBody WidgetTemplateDTO widgetTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update WidgetTemplate : {}", widgetTemplateDTO);
        if (widgetTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WidgetTemplateDTO result = widgetTemplateService.save(widgetTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, widgetTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /widget-templates} : get all the widgetTemplates.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of widgetTemplates in body.
     */
    @GetMapping("/widget-templates")
    public List<WidgetTemplateDTO> getAllWidgetTemplates() {
        log.debug("REST request to get all WidgetTemplates");
        return widgetTemplateService.findAll();
    }

    /**
     * {@code GET  /widget-templates/:id} : get the "id" widgetTemplate.
     *
     * @param id the id of the widgetTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the widgetTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/widget-templates/{id}")
    public ResponseEntity<WidgetTemplateDTO> getWidgetTemplate(@PathVariable Long id) {
        log.debug("REST request to get WidgetTemplate : {}", id);
        Optional<WidgetTemplateDTO> widgetTemplateDTO = widgetTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(widgetTemplateDTO);
    }

    /**
     * {@code DELETE  /widget-templates/:id} : delete the "id" widgetTemplate.
     *
     * @param id the id of the widgetTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/widget-templates/{id}")
    public ResponseEntity<Void> deleteWidgetTemplate(@PathVariable Long id) {
        log.debug("REST request to delete WidgetTemplate : {}", id);
        widgetTemplateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
