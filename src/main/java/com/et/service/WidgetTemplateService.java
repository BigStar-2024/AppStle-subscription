package com.et.service;

import com.et.service.dto.WidgetTemplateDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.WidgetTemplate}.
 */
public interface WidgetTemplateService {

    /**
     * Save a widgetTemplate.
     *
     * @param widgetTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    WidgetTemplateDTO save(WidgetTemplateDTO widgetTemplateDTO);

    /**
     * Get all the widgetTemplates.
     *
     * @return the list of entities.
     */
    List<WidgetTemplateDTO> findAll();


    /**
     * Get the "id" widgetTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WidgetTemplateDTO> findOne(Long id);

    /**
     * Delete the "id" widgetTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
