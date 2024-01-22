package com.et.service.impl;

import com.et.domain.enumeration.BulkAutomationType;
import com.et.service.BulkAutomationService;
import com.et.domain.BulkAutomation;
import com.et.repository.BulkAutomationRepository;
import com.et.service.dto.BulkAutomationDTO;
import com.et.service.mapper.BulkAutomationMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BulkAutomation}.
 */
@Service
@Transactional
public class BulkAutomationServiceImpl implements BulkAutomationService {

    private final Logger log = LoggerFactory.getLogger(BulkAutomationServiceImpl.class);

    private final BulkAutomationRepository bulkAutomationRepository;

    private final BulkAutomationMapper bulkAutomationMapper;

    public BulkAutomationServiceImpl(BulkAutomationRepository bulkAutomationRepository, BulkAutomationMapper bulkAutomationMapper) {
        this.bulkAutomationRepository = bulkAutomationRepository;
        this.bulkAutomationMapper = bulkAutomationMapper;
    }

    @Override
    public BulkAutomationDTO save(BulkAutomationDTO bulkAutomationDTO) {
        log.debug("Request to save BulkAutomation : {}", bulkAutomationDTO);
        BulkAutomation bulkAutomation = bulkAutomationMapper.toEntity(bulkAutomationDTO);
        bulkAutomation = bulkAutomationRepository.save(bulkAutomation);
        return bulkAutomationMapper.toDto(bulkAutomation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BulkAutomationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BulkAutomations");
        return bulkAutomationRepository.findAll(pageable)
            .map(bulkAutomationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BulkAutomationDTO> findOne(Long id) {
        log.debug("Request to get BulkAutomation : {}", id);
        return bulkAutomationRepository.findById(id)
            .map(bulkAutomationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BulkAutomation : {}", id);
        bulkAutomationRepository.deleteById(id);
    }

    @Override
    public Optional<BulkAutomationDTO> findByShopAndAutomationType(String shop, BulkAutomationType automationType) {
        log.debug("Request to get running BulkAutomation by shop: {} and automation  type: {}", shop, automationType);
        return bulkAutomationRepository.findByShopAndAutomationType(shop, automationType)
            .map(bulkAutomationMapper::toDto);
    }

    @Override
    public Page<BulkAutomationDTO> findAllByShop(String shop, Pageable pageable) {
        log.debug("Request to get all BulkAutomation by shop: {}", shop);
        return bulkAutomationRepository.findAllByShop(shop, pageable)
            .map(bulkAutomationMapper::toDto);
    }

    @Override
    public BulkAutomationDTO startBulkAutomationProcess(BulkAutomationDTO bulkAutomationDTO, String shop, BulkAutomationType automationType, String requestJson){
        log.info("Start Bulk Automation Process of Type: {}, with Request: {}", automationType, requestJson);
        bulkAutomationDTO.setShop(shop);
        bulkAutomationDTO.setAutomationType(automationType);
        bulkAutomationDTO.setRunning(true);
        bulkAutomationDTO.setStartTime(ZonedDateTime.now());
        bulkAutomationDTO.setEndTime(null);
        bulkAutomationDTO.setRequestInfo(requestJson);
        bulkAutomationDTO.setErrorInfo(null);
        return save(bulkAutomationDTO);
    }

    @Override
    public void updateBulkAutomationErrorMsg(String shop, BulkAutomationType automationType, String errorMsg){
        log.info("Updating Bulk automation process of Type: {}, for shop: {}, with error: {}", automationType, shop, errorMsg);
        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = findByShopAndAutomationType(shop, automationType);
        if (bulkAutomationDTOOpt.isPresent()) {
            BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.get();
            String errorInfo = bulkAutomationDTO.getErrorInfo();
            if(StringUtils.isBlank(errorInfo)){
                errorInfo = errorMsg;
            }else{
                errorInfo += "," + errorMsg;
            }
            bulkAutomationDTO.setErrorInfo(errorInfo);
            save(bulkAutomationDTO);
        }
    }

    @Override
    public void stopBulkAutomationProcess(String shop, BulkAutomationType automationType, String errorMsg){
        Optional<BulkAutomationDTO> bulkAutomationDTOOpt = findByShopAndAutomationType(shop, automationType);
        if (bulkAutomationDTOOpt.isPresent()) {
            BulkAutomationDTO bulkAutomationDTO = bulkAutomationDTOOpt.get();
            bulkAutomationDTO.setRunning(false);
            bulkAutomationDTO.setEndTime(ZonedDateTime.now());
            if(StringUtils.isNotBlank(errorMsg)){
                String errorInfo = bulkAutomationDTO.getErrorInfo();
                if(StringUtils.isBlank(errorInfo)){
                    errorInfo = errorMsg;
                }else{
                    errorInfo += "," + errorMsg;
                }
                bulkAutomationDTO.setErrorInfo(errorInfo);
            }
            bulkAutomationDTO = save(bulkAutomationDTO);

            log.info("Stopped Bulk automation process of Type: {}, for shop: {}, with errors: {}", automationType, shop, bulkAutomationDTO.getErrorInfo());
        }
    }
}
