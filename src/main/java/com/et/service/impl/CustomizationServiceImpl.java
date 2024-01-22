package com.et.service.impl;

import com.et.service.CustomizationService;
import com.et.domain.Customization;
import com.et.repository.CustomizationRepository;
import com.et.service.dto.CustomizationDTO;
import com.et.service.mapper.CustomizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Customization}.
 */
@Service
@Transactional
public class CustomizationServiceImpl implements CustomizationService {

    private final Logger log = LoggerFactory.getLogger(CustomizationServiceImpl.class);

    private final CustomizationRepository customizationRepository;

    private final CustomizationMapper customizationMapper;

    public CustomizationServiceImpl(CustomizationRepository customizationRepository, CustomizationMapper customizationMapper) {
        this.customizationRepository = customizationRepository;
        this.customizationMapper = customizationMapper;
    }

    /**
     * Save a customization.
     *
     * @param customizationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CustomizationDTO save(CustomizationDTO customizationDTO) {
        log.debug("Request to save Customization : {}", customizationDTO);
        Customization customization = customizationMapper.toEntity(customizationDTO);
        customization = customizationRepository.save(customization);
        return customizationMapper.toDto(customization);
    }

    /**
     * Get all the customizations.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomizationDTO> findAll() {
        log.debug("Request to get all Customizations");
        return customizationRepository.findAll().stream()
            .map(customizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one customization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomizationDTO> findOne(Long id) {
        log.debug("Request to get Customization : {}", id);
        return customizationRepository.findById(id)
            .map(customizationMapper::toDto);
    }

    /**
     * Delete the customization by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customization : {}", id);
        customizationRepository.deleteById(id);
    }

    @Override
    public List<CustomizationDTO> findAllById(List<Long> labelIds) {

        if (labelIds.isEmpty()) {
            return new ArrayList<>();
        }

        return customizationRepository.findAllById(labelIds).stream()
            .map(customizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
