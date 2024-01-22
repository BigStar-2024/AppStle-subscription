package com.et.service.impl;

import com.et.pojo.ExportMerchantsResponse;
import com.et.service.ShopInfoService;
import com.et.domain.ShopInfo;
import com.et.repository.ShopInfoRepository;
import com.et.service.dto.ShopInfoDTO;
import com.et.service.mapper.ShopInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShopInfo}.
 */
@Service
@Transactional
public class ShopInfoServiceImpl implements ShopInfoService {

    private final Logger log = LoggerFactory.getLogger(ShopInfoServiceImpl.class);

    private final ShopInfoRepository shopInfoRepository;

    private final ShopInfoMapper shopInfoMapper;

    public ShopInfoServiceImpl(ShopInfoRepository shopInfoRepository, ShopInfoMapper shopInfoMapper) {
        this.shopInfoRepository = shopInfoRepository;
        this.shopInfoMapper = shopInfoMapper;
    }

    @Override
    public ShopInfoDTO save(ShopInfoDTO shopInfoDTO) {
        log.debug("Request to save ShopInfo : {}", shopInfoDTO);
        ShopInfo shopInfo = shopInfoMapper.toEntity(shopInfoDTO);
        shopInfo = shopInfoRepository.save(shopInfo);
        return shopInfoMapper.toDto(shopInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopInfoDTO> findAll() {
        log.debug("Request to get all ShopInfos");
        return shopInfoRepository.findAll().stream()
            .map(shopInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ShopInfoDTO> findOne(Long id) {
        log.debug("Request to get ShopInfo : {}", id);
        return shopInfoRepository.findById(id)
            .map(shopInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopInfo : {}", id);
        shopInfoRepository.deleteById(id);
    }

    @Override
    public Optional<ShopInfoDTO> findByShop(String shop) {
        return shopInfoRepository.findByShop(shop)
            .map(shopInfoMapper::toDto);
    }

    @Override
    public Optional<ShopInfoDTO> findByApiKey(String apiKey) {
        return shopInfoRepository.findByApiKey(apiKey)
            .map(shopInfoMapper::toDto);
    }

    @Override
    public List<ShopInfoDTO> findAllByCountryCodeIsNullAndCountryNameIsNull() {
        return shopInfoRepository.findAllByCountryCodeIsNullAndCountryNameIsNull().stream()
            .map(shopInfoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteByShop(String shop) {
        shopInfoRepository.deleteByShop(shop);
    }

    @Override
    public List<ShopInfoDTO> findAllByShop(String shop) {
        return shopInfoRepository.findAllByShop(shop).stream()
            .map(shopInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ExportMerchantsResponse> findAllShopWhichUseBuildABoxFeature(Pageable pageable) {
        return shopInfoRepository.findAllShopWhichUseBuildABoxFeature(pageable);
    }

    @Override
    public List<ExportMerchantsResponse> findAllShopWhichUseAppstleMenu(Pageable pageable) {
        return shopInfoRepository.findAllShopWhichUseAppstleMenu(pageable);
    }
}
