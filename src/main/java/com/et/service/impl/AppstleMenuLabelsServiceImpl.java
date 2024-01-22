package com.et.service.impl;

import com.et.domain.AppstleMenuLabels;
import com.et.repository.AppstleMenuLabelsRepository;
import com.et.service.AppstleMenuLabelsService;
import com.et.service.dto.AppstleMenuLabelsDTO;
import com.et.service.mapper.AppstleMenuLabelsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AppstleMenuLabels}.
 */
@Service
@Transactional
public class AppstleMenuLabelsServiceImpl implements AppstleMenuLabelsService {

    private final Logger log = LoggerFactory.getLogger(AppstleMenuLabelsServiceImpl.class);

    private final AppstleMenuLabelsRepository appstleMenuLabelsRepository;

    private final AppstleMenuLabelsMapper appstleMenuLabelsMapper;

    public AppstleMenuLabelsServiceImpl(AppstleMenuLabelsRepository appstleMenuLabelsRepository, AppstleMenuLabelsMapper appstleMenuLabelsMapper) {
        this.appstleMenuLabelsRepository = appstleMenuLabelsRepository;
        this.appstleMenuLabelsMapper = appstleMenuLabelsMapper;
    }

    @Override
    public AppstleMenuLabelsDTO save(AppstleMenuLabelsDTO appstleMenuLabelsDTO) {
        log.debug("Request to save AppstleMenuLabels : {}", appstleMenuLabelsDTO);
        AppstleMenuLabels appstleMenuLabels = appstleMenuLabelsMapper.toEntity(appstleMenuLabelsDTO);
        appstleMenuLabels = appstleMenuLabelsRepository.save(appstleMenuLabels);
        return appstleMenuLabelsMapper.toDto(appstleMenuLabels);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppstleMenuLabelsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppstleMenuLabels");
        return appstleMenuLabelsRepository.findAll(pageable)
            .map(appstleMenuLabelsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AppstleMenuLabelsDTO> findOne(Long id) {
        log.debug("Request to get AppstleMenuLabels : {}", id);
        return appstleMenuLabelsRepository.findById(id)
            .map(appstleMenuLabelsMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<AppstleMenuLabelsDTO> findOneByShop(String shop) {
        Optional<AppstleMenuLabels> byShop = appstleMenuLabelsRepository.findByShop(shop);
        if (!byShop.isPresent()) {
            AppstleMenuLabels appstleMenuLabels = new AppstleMenuLabels();
            appstleMenuLabels.setShop(shop);
            appstleMenuLabels.setSeeMore("See more");
            appstleMenuLabels.setNoDataFound("No data found");
            appstleMenuLabels.setProductDetails("Product Details");
            appstleMenuLabels.setEditQuantity("Edit Quantity");
            appstleMenuLabels.setAddToCart("Add to cart");
            appstleMenuLabels.setProductAddedSuccessfully("Product added successfully");
            appstleMenuLabels.setWentWrong("Something went wrong");
            appstleMenuLabels.setResults("Results 0 found");
            appstleMenuLabels.setAdding("Adding");
            appstleMenuLabels.setSubscribe("Subscribe");
            appstleMenuLabels.setNotAvailable("Not available");
            AppstleMenuLabels result = appstleMenuLabelsRepository.save(appstleMenuLabels);
            return Optional.of(result).map(appstleMenuLabelsMapper::toDto);
        }
        return byShop.map(appstleMenuLabelsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppstleMenuLabels : {}", id);
        appstleMenuLabelsRepository.deleteById(id);
    }
}
