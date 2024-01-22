package com.et.service.impl;

import com.et.service.WidgetTemplateService;
import com.et.domain.WidgetTemplate;
import com.et.repository.WidgetTemplateRepository;
import com.et.service.dto.WidgetTemplateDTO;
import com.et.service.mapper.WidgetTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WidgetTemplate}.
 */
@Service
@Transactional
public class WidgetTemplateServiceImpl implements WidgetTemplateService {

    private final Logger log = LoggerFactory.getLogger(WidgetTemplateServiceImpl.class);

    private final WidgetTemplateRepository widgetTemplateRepository;

    private final WidgetTemplateMapper widgetTemplateMapper;

    public WidgetTemplateServiceImpl(WidgetTemplateRepository widgetTemplateRepository, WidgetTemplateMapper widgetTemplateMapper) {
        this.widgetTemplateRepository = widgetTemplateRepository;
        this.widgetTemplateMapper = widgetTemplateMapper;
    }

    /**
     * Save a widgetTemplate.
     *
     * @param widgetTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WidgetTemplateDTO save(WidgetTemplateDTO widgetTemplateDTO) {
        log.debug("Request to save WidgetTemplate : {}", widgetTemplateDTO);
        WidgetTemplate widgetTemplate = widgetTemplateMapper.toEntity(widgetTemplateDTO);
        widgetTemplate = widgetTemplateRepository.save(widgetTemplate);
        return widgetTemplateMapper.toDto(widgetTemplate);
    }

    /**
     * Get all the widgetTemplates.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WidgetTemplateDTO> findAll() {
        log.debug("Request to get all WidgetTemplates");
        return widgetTemplateRepository.findAll().stream()
            .map(widgetTemplateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one widgetTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WidgetTemplateDTO> findOne(Long id) {
        log.debug("Request to get WidgetTemplate : {}", id);
        return widgetTemplateRepository.findById(id)
            .map(widgetTemplateMapper::toDto);
    }

    /**
     * Delete the widgetTemplate by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WidgetTemplate : {}", id);
        widgetTemplateRepository.deleteById(id);
    }
}
