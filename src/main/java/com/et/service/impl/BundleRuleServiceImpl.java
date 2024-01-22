package com.et.service.impl;

import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.constant.Constants;
import com.et.domain.BundleRule;
import com.et.domain.enumeration.BundleLevel;
import com.et.domain.enumeration.BundleStatus;
import com.et.repository.BundleRuleRepository;
import com.et.service.BundleRuleService;
import com.et.service.dto.BundleRuleDTO;
import com.et.service.dto.BundleRuleProductDTO;
import com.et.service.mapper.BundleRuleMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.vm.bundling.DiscountCodeRequest;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import com.et.web.rest.vm.bundling.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.DiscountCodeBasicCreateMutation;
import com.shopify.java.graphql.client.type.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BundleRule}.
 */
@Service
@Transactional
public class BundleRuleServiceImpl implements BundleRuleService {

    private final Logger log = LoggerFactory.getLogger(BundleRuleServiceImpl.class);

    private final BundleRuleRepository bundleRuleRepository;

    private final BundleRuleMapper bundleRuleMapper;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository, BundleRuleMapper bundleRuleMapper) {
        this.bundleRuleRepository = bundleRuleRepository;
        this.bundleRuleMapper = bundleRuleMapper;
    }

    @Override
    public BundleRuleDTO save(BundleRuleDTO bundleRuleDTO) {
        log.debug("Request to save BundleRule : {}", bundleRuleDTO);
        BundleRule bundleRule = bundleRuleMapper.toEntity(bundleRuleDTO);
        if (bundleRule.getId() == null) {
            bundleRule.setSequenceNo(bundleRuleRepository.getMaxIndex() + 1);
        }
        bundleRule = bundleRuleRepository.save(bundleRule);
        return bundleRuleMapper.toDto(bundleRule);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleRuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BundleRules");
        return bundleRuleRepository.findAll(pageable)
            .map(bundleRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleRuleDTO> findAllByShop(String shop, Pageable pageable) {
        log.debug("Request to get all BundleRules");
        return bundleRuleRepository.findAllByShopOrderBySequenceNo(shop, pageable)
            .map(bundleRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BundleRuleDTO> findAllByShop(String shop) {
        log.debug("Request to get all BundleRules");
        return bundleRuleRepository.findAllByShop(shop)
            .stream().map(bundleRuleMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BundleRule> findAllByShopAndStatus(String shop, BundleStatus status) {
        log.debug("Request to get all BundleRules");
        return bundleRuleRepository.findAllByShopAndStatus(shop, status);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BundleRuleDTO> findOne(Long id) {
        log.debug("Request to get BundleRule : {}", id);
        return bundleRuleRepository.findById(id)
            .map(bundleRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BundleRule : {}", id);
        bundleRuleRepository.deleteById(id);
    }


    @Override
    public Boolean updateIndex(Long id, Integer sourceIndex, Integer destinationIndex) {
        if (sourceIndex.intValue() < destinationIndex.intValue()) {
            bundleRuleRepository.updateNextIndexesToMoveDown(destinationIndex);
            bundleRuleRepository.updateIndexToMoveDown(id, destinationIndex);
        } else {
            bundleRuleRepository.updateNextIndexesToMoveUp(destinationIndex);
            bundleRuleRepository.updateIndexToMoveUp(id, destinationIndex);
        }
        return true;
    }

    @Override
    public Boolean updateStatus(Long id, Boolean status) {
        bundleRuleRepository.updateStatus(id, status ? BundleStatus.ACTIVE : BundleStatus.PAUSED);
        return true;
    }

}
