package com.et.service.impl;

import com.et.domain.SocialConnection;
import com.et.pojo.KeyLabelInfo;
import com.et.pojo.LabelValueInfo;
import com.et.service.ShopLabelService;
import com.et.service.SocialConnectionService;
import com.et.service.SubscriptionWidgetSettingsService;
import com.et.domain.SubscriptionWidgetSettings;
import com.et.repository.SubscriptionWidgetSettingsRepository;
import com.et.service.dto.SubscriptionWidgetSettingsDTO;
import com.et.service.mapper.SubscriptionWidgetSettingsMapper;
import com.et.web.rest.vm.SyncInfoItem;
import com.et.web.rest.vm.SyncLabelsInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionWidgetSettings}.
 */
@Service
@Transactional
public class SubscriptionWidgetSettingsServiceImpl implements SubscriptionWidgetSettingsService {

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private ShopLabelService shopLabelService;

    private final Logger log = LoggerFactory.getLogger(SubscriptionWidgetSettingsServiceImpl.class);

    private final SubscriptionWidgetSettingsRepository subscriptionWidgetSettingsRepository;

    private final SubscriptionWidgetSettingsMapper subscriptionWidgetSettingsMapper;

    public SubscriptionWidgetSettingsServiceImpl(SubscriptionWidgetSettingsRepository subscriptionWidgetSettingsRepository, SubscriptionWidgetSettingsMapper subscriptionWidgetSettingsMapper) {
        this.subscriptionWidgetSettingsRepository = subscriptionWidgetSettingsRepository;
        this.subscriptionWidgetSettingsMapper = subscriptionWidgetSettingsMapper;
    }

    @Override
    public SubscriptionWidgetSettingsDTO save(SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO) {
        log.debug("Request to save SubscriptionWidgetSettings : {}", subscriptionWidgetSettingsDTO);
        SubscriptionWidgetSettings subscriptionWidgetSettings = subscriptionWidgetSettingsMapper.toEntity(subscriptionWidgetSettingsDTO);
        subscriptionWidgetSettings = subscriptionWidgetSettingsRepository.save(subscriptionWidgetSettings);
        return subscriptionWidgetSettingsMapper.toDto(subscriptionWidgetSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionWidgetSettingsDTO> findAll() {
        log.debug("Request to get all SubscriptionWidgetSettings");
        return subscriptionWidgetSettingsRepository.findAll().stream()
            .map(subscriptionWidgetSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionWidgetSettingsDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionWidgetSettings : {}", id);
        return subscriptionWidgetSettingsRepository.findById(id)
            .map(subscriptionWidgetSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionWidgetSettings : {}", id);
        subscriptionWidgetSettingsRepository.deleteById(id);
    }

    @Override
    public Optional<SubscriptionWidgetSettingsDTO> findByShop(String shop) {
        log.debug("Request to get SubscriptionWidgetSettings : {}", shop);
        Optional<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsRepository.findByShop(shop)
            .map(subscriptionWidgetSettingsMapper::toDto);

        if(subscriptionWidgetSettingsDTO.isEmpty()){
            return subscriptionWidgetSettingsDTO;
        }

        Map<String, LabelValueInfo> shopLabels = shopLabelService.getShopLabels(shop);
        Map<String, String> widgetLabels = shopLabels.entrySet().stream().filter(e -> e.getValue().getGroups().contains("WIDGET")).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));

        for (Map.Entry<String, String> entry : widgetLabels.entrySet()) {
            String[] split = StringUtils.split(entry.getKey(), ".");
            subscriptionWidgetSettingsDTO.get().setAdditionalProperty(split[split.length - 1], entry.getValue());
        }

        return subscriptionWidgetSettingsDTO;
    }

    @Override
    public void deleteByShop(String shop) {
        subscriptionWidgetSettingsRepository.deleteByShop(shop);
    }

    @Override
    public List<SubscriptionWidgetSettingsDTO> findAllByShop(String shop) {
        return subscriptionWidgetSettingsRepository.findAllByShop(shop).stream()
            .map(subscriptionWidgetSettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
