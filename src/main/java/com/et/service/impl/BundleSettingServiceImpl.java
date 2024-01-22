package com.et.service.impl;

import com.et.domain.BundleSetting;
import com.et.pojo.LabelValueInfo;
import com.et.repository.BundleSettingRepository;
import com.et.service.BundleSettingService;
import com.et.service.ShopLabelService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.BundleSettingDTO;
import com.et.service.mapper.BundleSettingMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BundleSetting}.
 */
@Service
@Transactional
public class BundleSettingServiceImpl implements BundleSettingService {

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private ShopLabelService shopLabelService;

    private final Logger log = LoggerFactory.getLogger(BundleSettingServiceImpl.class);

    private final BundleSettingRepository bundleSettingRepository;

    private final BundleSettingMapper bundleSettingMapper;

    public BundleSettingServiceImpl(BundleSettingRepository bundleSettingRepository, BundleSettingMapper bundleSettingMapper) {
        this.bundleSettingRepository = bundleSettingRepository;
        this.bundleSettingMapper = bundleSettingMapper;
    }

    @Override
    public BundleSettingDTO save(BundleSettingDTO bundleSettingDTO) {
        log.debug("Request to save BundleSetting : {}", bundleSettingDTO);
        BundleSetting bundleSetting = bundleSettingMapper.toEntity(bundleSettingDTO);
        bundleSetting = bundleSettingRepository.save(bundleSetting);
        return bundleSettingMapper.toDto(bundleSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleSettingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BundleSettings");
        return bundleSettingRepository.findAll(pageable)
            .map(bundleSettingMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BundleSettingDTO> findOne(Long id) {
        log.debug("Request to get BundleSetting : {}", id);
        return bundleSettingRepository.findById(id)
            .map(bundleSettingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BundleSettingDTO> findByShop(String shop) {
        Optional<BundleSettingDTO> bundleSettingDTO = bundleSettingRepository.findByShop(shop)
            .map(bundleSettingMapper::toDto);

        if(bundleSettingDTO.isEmpty()){
            return bundleSettingDTO;
        }

        Map<String, LabelValueInfo> shopLabels = shopLabelService.getShopLabels(shop);
        Map<String, String> bundleLabels = shopLabels.entrySet().stream().filter(e -> e.getValue().getGroups().contains("BUNDLE")).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));

        for (Map.Entry<String, String> entry : bundleLabels.entrySet()) {
            String[] split = StringUtils.split(entry.getKey(), ".");
            bundleSettingDTO.get().setAdditionalProperty(split[split.length - 1], entry.getValue());
        }

        return bundleSettingDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BundleSettingDTO> findAllByShop(String shop, Pageable pageable) {
        return bundleSettingRepository.findAllByShop(shop, pageable)
            .map(bundleSettingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BundleSetting : {}", id);
        bundleSettingRepository.deleteById(id);
    }

    @Override
    public void deleteByShop(String shop) {
        log.debug("Request to delete BundleSetting : {}", shop);
        bundleSettingRepository.deleteByShop(shop);
    }
}
