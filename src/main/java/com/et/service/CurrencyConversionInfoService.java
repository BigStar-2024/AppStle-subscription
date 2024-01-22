package com.et.service;

import com.et.service.dto.CurrencyConversionInfoDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.CurrencyConversionInfo}.
 */
public interface CurrencyConversionInfoService {

    /**
     * Save a currencyConversionInfo.
     *
     * @param currencyConversionInfoDTO the entity to save.
     * @return the persisted entity.
     */
    CurrencyConversionInfoDTO save(CurrencyConversionInfoDTO currencyConversionInfoDTO);

    /**
     * Get all the currencyConversionInfos.
     *
     * @return the list of entities.
     */
    List<CurrencyConversionInfoDTO> findAll();


    /**
     * Get the "id" currencyConversionInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CurrencyConversionInfoDTO> findOne(Long id);

    /**
     * Delete the "id" currencyConversionInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    @Transactional(readOnly = true)
    Optional<CurrencyConversionInfoDTO> findCurrencyConversionInfoByFrom(String currency);
}
