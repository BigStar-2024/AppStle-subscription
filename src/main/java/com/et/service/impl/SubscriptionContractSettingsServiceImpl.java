package com.et.service.impl;

import com.et.service.SubscriptionContractSettingsService;
import com.et.domain.SubscriptionContractSettings;
import com.et.repository.SubscriptionContractSettingsRepository;
import com.et.service.dto.SubscriptionContractSettingsDTO;
import com.et.service.mapper.SubscriptionContractSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionContractSettings}.
 */
@Service
@Transactional
public class SubscriptionContractSettingsServiceImpl implements SubscriptionContractSettingsService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractSettingsServiceImpl.class);

    private final SubscriptionContractSettingsRepository subscriptionContractSettingsRepository;

    private final SubscriptionContractSettingsMapper subscriptionContractSettingsMapper;

    public SubscriptionContractSettingsServiceImpl(SubscriptionContractSettingsRepository subscriptionContractSettingsRepository, SubscriptionContractSettingsMapper subscriptionContractSettingsMapper) {
        this.subscriptionContractSettingsRepository = subscriptionContractSettingsRepository;
        this.subscriptionContractSettingsMapper = subscriptionContractSettingsMapper;
    }

    @Override
    public SubscriptionContractSettingsDTO save(SubscriptionContractSettingsDTO subscriptionContractSettingsDTO) {
        log.debug("Request to save SubscriptionContractSettings : {}", subscriptionContractSettingsDTO);
        SubscriptionContractSettings subscriptionContractSettings = subscriptionContractSettingsMapper.toEntity(subscriptionContractSettingsDTO);
        subscriptionContractSettings = subscriptionContractSettingsRepository.save(subscriptionContractSettings);
        return subscriptionContractSettingsMapper.toDto(subscriptionContractSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionContractSettingsDTO> findAll() {
        log.debug("Request to get all SubscriptionContractSettings");
        return subscriptionContractSettingsRepository.findAll().stream()
            .map(subscriptionContractSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionContractSettingsDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionContractSettings : {}", id);
        return subscriptionContractSettingsRepository.findById(id)
            .map(subscriptionContractSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionContractSettings : {}", id);
        subscriptionContractSettingsRepository.deleteById(id);
    }

    @Override
    public List<SubscriptionContractSettingsDTO> findByShop(String shop) {
        return subscriptionContractSettingsRepository.findByShop(shop).stream()
            .map(subscriptionContractSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
