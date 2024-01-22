package com.et.service.impl;

import com.et.service.ProductSwapService;
import com.et.domain.ProductSwap;
import com.et.repository.ProductSwapRepository;
import com.et.service.dto.ProductSwapDTO;
import com.et.service.mapper.ProductSwapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProductSwap}.
 */
@Service
@Transactional
public class ProductSwapServiceImpl implements ProductSwapService {

    private final Logger log = LoggerFactory.getLogger(ProductSwapServiceImpl.class);

    private final ProductSwapRepository productSwapRepository;

    private final ProductSwapMapper productSwapMapper;

    public ProductSwapServiceImpl(ProductSwapRepository productSwapRepository, ProductSwapMapper productSwapMapper) {
        this.productSwapRepository = productSwapRepository;
        this.productSwapMapper = productSwapMapper;
    }

    @Override
    public ProductSwapDTO save(ProductSwapDTO productSwapDTO) {
        log.debug("Request to save ProductSwap : {}", productSwapDTO);
        ProductSwap productSwap = productSwapMapper.toEntity(productSwapDTO);
        productSwap = productSwapRepository.save(productSwap);
        return productSwapMapper.toDto(productSwap);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSwapDTO> findAll() {
        log.debug("Request to get all ProductSwaps");
        return productSwapRepository.findAll().stream()
            .map(productSwapMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProductSwapDTO> findOne(Long id) {
        log.debug("Request to get ProductSwap : {}", id);
        return productSwapRepository.findById(id)
            .map(productSwapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductSwap : {}", id);
        productSwapRepository.deleteById(id);
    }

    @Override
    public List<ProductSwapDTO> findByShop(String shop) {
        return productSwapRepository.findByShop(shop).stream()
            .map(productSwapMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductSwapDTO> findByShopAndEnabledForRecurring(String shop, boolean enabledForRecurringOrder) {
        return productSwapRepository.findByShopAndCheckForEveryRecurringOrder(shop, enabledForRecurringOrder).stream()
            .map(productSwapMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
