package com.et.service.impl;

import com.et.service.BundleDynamicScriptService;
import com.et.domain.BundleDynamicScript;
import com.et.repository.BundleDynamicScriptRepository;
import com.et.service.dto.BundleDynamicScriptDTO;
import com.et.service.mapper.BundleDynamicScriptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BundleDynamicScript}.
 */
@Service
@Transactional
public class BundleDynamicScriptServiceImpl implements BundleDynamicScriptService {

    private final Logger log = LoggerFactory.getLogger(BundleDynamicScriptServiceImpl.class);

    private final BundleDynamicScriptRepository bundleDynamicScriptRepository;

    private final BundleDynamicScriptMapper bundleDynamicScriptMapper;

    public BundleDynamicScriptServiceImpl(BundleDynamicScriptRepository bundleDynamicScriptRepository, BundleDynamicScriptMapper bundleDynamicScriptMapper) {
        this.bundleDynamicScriptRepository = bundleDynamicScriptRepository;
        this.bundleDynamicScriptMapper = bundleDynamicScriptMapper;
    }

    @Override
    public BundleDynamicScriptDTO save(BundleDynamicScriptDTO bundleDynamicScriptDTO) {
        log.debug("Request to save BundleDynamicScript : {}", bundleDynamicScriptDTO);
        BundleDynamicScript bundleDynamicScript = bundleDynamicScriptMapper.toEntity(bundleDynamicScriptDTO);
        bundleDynamicScript = bundleDynamicScriptRepository.save(bundleDynamicScript);
        return bundleDynamicScriptMapper.toDto(bundleDynamicScript);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BundleDynamicScriptDTO> findAll() {
        log.debug("Request to get all BundleDynamicScripts");
        return bundleDynamicScriptRepository.findAll().stream()
            .map(bundleDynamicScriptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BundleDynamicScriptDTO> findOne(Long id) {
        log.debug("Request to get BundleDynamicScript : {}", id);
        return bundleDynamicScriptRepository.findById(id)
            .map(bundleDynamicScriptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BundleDynamicScript : {}", id);
        bundleDynamicScriptRepository.deleteById(id);
    }

    @Override
    public Optional<BundleDynamicScriptDTO> findByShop(String shop) {
        return bundleDynamicScriptRepository.findByShop(shop)
            .map(bundleDynamicScriptMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        bundleDynamicScriptRepository.deleteByShop(shop);
    }
}
