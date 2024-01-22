package com.et.service.impl;

import com.et.service.ShopSettingsService;
import com.et.domain.ShopSettings;
import com.et.repository.ShopSettingsRepository;
import com.et.service.dto.ShopSettingsDTO;
import com.et.service.mapper.ShopSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShopSettings}.
 */
@Service
@Transactional
public class ShopSettingsServiceImpl implements ShopSettingsService {

    private final Logger log = LoggerFactory.getLogger(ShopSettingsServiceImpl.class);

    private final ShopSettingsRepository shopSettingsRepository;

    private final ShopSettingsMapper shopSettingsMapper;

    public ShopSettingsServiceImpl(ShopSettingsRepository shopSettingsRepository, ShopSettingsMapper shopSettingsMapper) {
        this.shopSettingsRepository = shopSettingsRepository;
        this.shopSettingsMapper = shopSettingsMapper;
    }

    /**
     * Save a shopSettings.
     *
     * @param shopSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ShopSettingsDTO save(ShopSettingsDTO shopSettingsDTO) {
        log.debug("Request to save ShopSettings : {}", shopSettingsDTO);
        ShopSettings shopSettings = shopSettingsMapper.toEntity(shopSettingsDTO);
        shopSettings = shopSettingsRepository.save(shopSettings);
        return shopSettingsMapper.toDto(shopSettings);
    }

    /**
     * Get all the shopSettings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShopSettingsDTO> findAll() {
        log.debug("Request to get all ShopSettings");
        return shopSettingsRepository.findAll().stream()
            .map(shopSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shopSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShopSettingsDTO> findOne(Long id) {
        log.debug("Request to get ShopSettings : {}", id);
        return shopSettingsRepository.findById(id)
            .map(shopSettingsMapper::toDto);
    }

    /**
     * Delete the shopSettings by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopSettings : {}", id);
        shopSettingsRepository.deleteById(id);
    }

    /**
     * Get the "shop" shopSettings.
     *
     * @param shop the shop of the entity.
     * @return the entity.
     */
    @Override
    public Optional<ShopSettingsDTO> findByShop(String shop) {
        return shopSettingsRepository.findByShop(shop)
            .map(shopSettingsMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        shopSettingsRepository.deleteByShop(shop);
    }
}
