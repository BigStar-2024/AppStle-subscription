package com.et.service.impl;

import com.et.pojo.LabelValueInfo;
import com.et.service.ShopLabelService;
import com.et.service.SubscriptionBundleSettingsService;
import com.et.domain.SubscriptionBundleSettings;
import com.et.repository.SubscriptionBundleSettingsRepository;
import com.et.service.dto.SubscriptionBundleSettingsDTO;
import com.et.service.mapper.SubscriptionBundleSettingsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionBundleSettings}.
 */
@Service
@Transactional
public class SubscriptionBundleSettingsServiceImpl implements SubscriptionBundleSettingsService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleSettingsServiceImpl.class);

    private final SubscriptionBundleSettingsRepository subscriptionBundleSettingsRepository;

    private final SubscriptionBundleSettingsMapper subscriptionBundleSettingsMapper;

    @Autowired
    private ShopLabelService shopLabelService;

    public SubscriptionBundleSettingsServiceImpl(SubscriptionBundleSettingsRepository subscriptionBundleSettingsRepository, SubscriptionBundleSettingsMapper subscriptionBundleSettingsMapper) {
        this.subscriptionBundleSettingsRepository = subscriptionBundleSettingsRepository;
        this.subscriptionBundleSettingsMapper = subscriptionBundleSettingsMapper;
    }

    @Override
    public SubscriptionBundleSettingsDTO save(SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO) {
        log.debug("Request to save SubscriptionBundleSettings : {}", subscriptionBundleSettingsDTO);
        SubscriptionBundleSettings subscriptionBundleSettings = subscriptionBundleSettingsMapper.toEntity(subscriptionBundleSettingsDTO);
        subscriptionBundleSettings = subscriptionBundleSettingsRepository.save(subscriptionBundleSettings);
        return subscriptionBundleSettingsMapper.toDto(subscriptionBundleSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionBundleSettingsDTO> findAll() {
        log.debug("Request to get all SubscriptionBundleSettings");
        return subscriptionBundleSettingsRepository.findAll().stream()
            .map(subscriptionBundleSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBundleSettingsDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionBundleSettings : {}", id);
        return subscriptionBundleSettingsRepository.findById(id)
            .map(subscriptionBundleSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionBundleSettings : {}", id);
        subscriptionBundleSettingsRepository.deleteById(id);
    }

    @Override
    public Optional<SubscriptionBundleSettingsDTO> findByShop(String shop) {
        Optional<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTO = subscriptionBundleSettingsRepository.findByShop(shop)
            .map(subscriptionBundleSettingsMapper::toDto);

        if(subscriptionBundleSettingsDTO.isEmpty()){
            return subscriptionBundleSettingsDTO;
        }

        Map<String, LabelValueInfo> shopLabels = shopLabelService.getShopLabels(shop);
        Map<String, String> bundleLabels = shopLabels.entrySet().stream().filter(e -> e.getValue().getGroups().contains("BUNDLE")).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));

        for (Map.Entry<String, String> entry : bundleLabels.entrySet()) {
            String[] split = StringUtils.split(entry.getKey(), ".");
            subscriptionBundleSettingsDTO.get().setAdditionalProperty(split[split.length - 1], entry.getValue());
        }
        return subscriptionBundleSettingsDTO;
    }

    @Override
    public void deleteByShop(String shop) {
        subscriptionBundleSettingsRepository.deleteByShop(shop);
    }
}
