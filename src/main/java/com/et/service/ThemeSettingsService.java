package com.et.service;

import com.et.domain.ThemeSettings;
import com.et.repository.ThemeSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ThemeSettings}.
 */
@Service
@Transactional
public class ThemeSettingsService {

    private final Logger log = LoggerFactory.getLogger(ThemeSettingsService.class);

    private final ThemeSettingsRepository themeSettingsRepository;

    public ThemeSettingsService(ThemeSettingsRepository themeSettingsRepository) {
        this.themeSettingsRepository = themeSettingsRepository;
    }

    /**
     * Save a themeSettings.
     *
     * @param themeSettings the entity to save.
     * @return the persisted entity.
     */
    public ThemeSettings save(ThemeSettings themeSettings) {
        log.debug("Request to save ThemeSettings : {}", themeSettings);
        return themeSettingsRepository.save(themeSettings);
    }

    /**
     * Get all the themeSettings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ThemeSettings> findAll() {
        log.debug("Request to get all ThemeSettings");
        return themeSettingsRepository.findAll();
    }


    /**
     * Get one themeSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ThemeSettings> findOne(Long id) {
        log.debug("Request to get ThemeSettings : {}", id);
        return themeSettingsRepository.findById(id);
    }

    /**
     * Delete the themeSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ThemeSettings : {}", id);
        themeSettingsRepository.deleteById(id);
    }

    public Optional<ThemeSettings> findByShop(String shop) {
        ThemeSettings themeSettings = themeSettingsRepository.findByShop(shop);
        return Optional.ofNullable(themeSettings);
    }
}
