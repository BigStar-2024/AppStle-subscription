package com.et.service.impl;

import com.et.service.CurrencyConversionInfoService;
import com.et.domain.CurrencyConversionInfo;
import com.et.repository.CurrencyConversionInfoRepository;
import com.et.service.dto.CurrencyConversionInfoDTO;
import com.et.service.mapper.CurrencyConversionInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CurrencyConversionInfo}.
 */
@Service
@Transactional
public class CurrencyConversionInfoServiceImpl implements CurrencyConversionInfoService {

    private final Logger log = LoggerFactory.getLogger(CurrencyConversionInfoServiceImpl.class);

    private final CurrencyConversionInfoRepository currencyConversionInfoRepository;

    private final CurrencyConversionInfoMapper currencyConversionInfoMapper;

    public CurrencyConversionInfoServiceImpl(CurrencyConversionInfoRepository currencyConversionInfoRepository, CurrencyConversionInfoMapper currencyConversionInfoMapper) {
        this.currencyConversionInfoRepository = currencyConversionInfoRepository;
        this.currencyConversionInfoMapper = currencyConversionInfoMapper;
    }

    /**
     * Save a currencyConversionInfo.
     *
     * @param currencyConversionInfoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CurrencyConversionInfoDTO save(CurrencyConversionInfoDTO currencyConversionInfoDTO) {
        log.debug("Request to save CurrencyConversionInfo : {}", currencyConversionInfoDTO);
        CurrencyConversionInfo currencyConversionInfo = currencyConversionInfoMapper.toEntity(currencyConversionInfoDTO);
        currencyConversionInfo = currencyConversionInfoRepository.save(currencyConversionInfo);
        return currencyConversionInfoMapper.toDto(currencyConversionInfo);
    }

    /**
     * Get all the currencyConversionInfos.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CurrencyConversionInfoDTO> findAll() {
        log.debug("Request to get all CurrencyConversionInfos");
        return currencyConversionInfoRepository.findAll().stream()
            .map(currencyConversionInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one currencyConversionInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyConversionInfoDTO> findOne(Long id) {
        log.debug("Request to get CurrencyConversionInfo : {}", id);
        return currencyConversionInfoRepository.findById(id)
            .map(currencyConversionInfoMapper::toDto);
    }

    /**
     * Delete the currencyConversionInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyConversionInfo : {}", id);
        currencyConversionInfoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyConversionInfoDTO> findCurrencyConversionInfoByFrom(String currency) {
        log.debug("Request to get CurrencyConversionInfo from Currency: {}", currency);
        return currencyConversionInfoRepository.findCurrencyConversionInfoByFrom(currency)
            .map(currencyConversionInfoMapper::toDto);
    }
}
