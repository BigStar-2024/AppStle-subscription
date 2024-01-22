package com.et.service.impl;

import com.et.service.SubscriptionCustomCssService;
import com.et.domain.SubscriptionCustomCss;
import com.et.repository.SubscriptionCustomCssRepository;
import com.et.service.dto.SubscriptionCustomCssDTO;
import com.et.service.mapper.SubscriptionCustomCssMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionCustomCss}.
 */
@Service
@Transactional
public class SubscriptionCustomCssServiceImpl implements SubscriptionCustomCssService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionCustomCssServiceImpl.class);

    private final SubscriptionCustomCssRepository subscriptionCustomCssRepository;

    private final SubscriptionCustomCssMapper subscriptionCustomCssMapper;

    public SubscriptionCustomCssServiceImpl(SubscriptionCustomCssRepository subscriptionCustomCssRepository, SubscriptionCustomCssMapper subscriptionCustomCssMapper) {
        this.subscriptionCustomCssRepository = subscriptionCustomCssRepository;
        this.subscriptionCustomCssMapper = subscriptionCustomCssMapper;
    }

    @Override
    public SubscriptionCustomCssDTO save(SubscriptionCustomCssDTO subscriptionCustomCssDTO) {
        log.debug("Request to save SubscriptionCustomCss : {}", subscriptionCustomCssDTO);
        SubscriptionCustomCss subscriptionCustomCss = subscriptionCustomCssMapper.toEntity(subscriptionCustomCssDTO);
        subscriptionCustomCss = subscriptionCustomCssRepository.save(subscriptionCustomCss);
        return subscriptionCustomCssMapper.toDto(subscriptionCustomCss);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionCustomCssDTO> findAll() {
        log.debug("Request to get all SubscriptionCustomCsses");
        return subscriptionCustomCssRepository.findAll().stream()
            .map(subscriptionCustomCssMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionCustomCssDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionCustomCss : {}", id);
        return subscriptionCustomCssRepository.findById(id)
            .map(subscriptionCustomCssMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionCustomCss : {}", id);
        subscriptionCustomCssRepository.deleteById(id);
    }

    @Override
    public Optional<SubscriptionCustomCssDTO> findByShop(String shop) {
        return subscriptionCustomCssRepository.findByShop(shop)
            .map(subscriptionCustomCssMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        subscriptionCustomCssRepository.deleteByShop(shop);
    }

    @Override
    public List<SubscriptionCustomCssDTO> findAllByShop(String shop) {
        return subscriptionCustomCssRepository.findAllByShop(shop).stream()
            .map(subscriptionCustomCssMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
