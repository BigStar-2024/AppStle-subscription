package com.et.service;

import com.et.domain.CartWidgetSettings;
import com.et.repository.CartWidgetSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CartWidgetSettings}.
 */
@Service
@Transactional
public class CartWidgetSettingsService {

    private final Logger log = LoggerFactory.getLogger(CartWidgetSettingsService.class);

    private final CartWidgetSettingsRepository cartWidgetSettingsRepository;

    public CartWidgetSettingsService(CartWidgetSettingsRepository cartWidgetSettingsRepository) {
        this.cartWidgetSettingsRepository = cartWidgetSettingsRepository;
    }

    /**
     * Save a cartWidgetSettings.
     *
     * @param cartWidgetSettings the entity to save.
     * @return the persisted entity.
     */
    public CartWidgetSettings save(CartWidgetSettings cartWidgetSettings) {
        log.debug("Request to save CartWidgetSettings : {}", cartWidgetSettings);
        return cartWidgetSettingsRepository.save(cartWidgetSettings);
    }

    /**
     * Get all the cartWidgetSettings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CartWidgetSettings> findAll() {
        log.debug("Request to get all CartWidgetSettings");
        return cartWidgetSettingsRepository.findAll();
    }


    /**
     * Get one cartWidgetSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CartWidgetSettings> findOne(Long id) {
        log.debug("Request to get CartWidgetSettings : {}", id);
        return cartWidgetSettingsRepository.findById(id);
    }

    /**
     * Delete the cartWidgetSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CartWidgetSettings : {}", id);
        cartWidgetSettingsRepository.deleteById(id);
    }

    public void deleteByShop(String shop) {
        log.debug("Request to delete CartWidgetSettings : {}", shop);
        cartWidgetSettingsRepository.deleteByShop(shop);
    }

    public Optional<CartWidgetSettings> findByShop(String shop) {
        log.debug("Request to get CartWidgetSettings : {}", shop);
        return cartWidgetSettingsRepository.findByShop(shop);
    }
}
