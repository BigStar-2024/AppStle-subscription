package com.et.service.impl;

import com.et.service.ProductCycleService;
import com.et.domain.ProductCycle;
import com.et.repository.ProductCycleRepository;
import com.et.service.dto.ProductCycleDTO;
import com.et.service.mapper.ProductCycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProductCycle}.
 */
@Service
@Transactional
public class ProductCycleServiceImpl implements ProductCycleService {

    private final Logger log = LoggerFactory.getLogger(ProductCycleServiceImpl.class);

    private final ProductCycleRepository productCycleRepository;

    private final ProductCycleMapper productCycleMapper;

    public ProductCycleServiceImpl(ProductCycleRepository productCycleRepository, ProductCycleMapper productCycleMapper) {
        this.productCycleRepository = productCycleRepository;
        this.productCycleMapper = productCycleMapper;
    }

    @Override
    public ProductCycleDTO save(ProductCycleDTO productCycleDTO) {
        log.debug("Request to save ProductCycle : {}", productCycleDTO);
        ProductCycle productCycle = productCycleMapper.toEntity(productCycleDTO);
        productCycle = productCycleRepository.save(productCycle);
        return productCycleMapper.toDto(productCycle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCycleDTO> findAll() {
        log.debug("Request to get all ProductCycles");
        return productCycleRepository.findAll().stream()
            .map(productCycleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCycleDTO> findByShop(String shop) {
        log.debug("Request to get all ProductCycles");
        return productCycleRepository.findByShop(shop).stream()
            .map(productCycleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCycleDTO> findOne(Long id) {
        log.debug("Request to get ProductCycle : {}", id);
        return productCycleRepository.findById(id)
            .map(productCycleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductCycle : {}", id);
        productCycleRepository.deleteById(id);
    }
}
