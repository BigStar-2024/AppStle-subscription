package com.et.service.impl;

import com.et.service.VariantInfoService;
import com.et.domain.VariantInfo;
import com.et.repository.VariantInfoRepository;
import com.et.service.dto.VariantInfoDTO;
import com.et.service.mapper.VariantInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link VariantInfo}.
 */
@Service
@Transactional
public class VariantInfoServiceImpl implements VariantInfoService {

    private final Logger log = LoggerFactory.getLogger(VariantInfoServiceImpl.class);

    private final VariantInfoRepository variantInfoRepository;

    private final VariantInfoMapper variantInfoMapper;

    public VariantInfoServiceImpl(VariantInfoRepository variantInfoRepository, VariantInfoMapper variantInfoMapper) {
        this.variantInfoRepository = variantInfoRepository;
        this.variantInfoMapper = variantInfoMapper;
    }

    /**
     * Save a variantInfo.
     *
     * @param variantInfoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VariantInfoDTO save(VariantInfoDTO variantInfoDTO) {
        log.debug("Request to save VariantInfo : {}", variantInfoDTO);
        VariantInfo variantInfo = variantInfoMapper.toEntity(variantInfoDTO);
        variantInfo = variantInfoRepository.save(variantInfo);
        return variantInfoMapper.toDto(variantInfo);
    }

    /**
     * Get all the variantInfos.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VariantInfoDTO> findAll() {
        log.debug("Request to get all VariantInfos");
        return variantInfoRepository.findAll().stream()
            .map(variantInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one variantInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VariantInfoDTO> findOne(Long id) {
        log.debug("Request to get VariantInfo : {}", id);
        return variantInfoRepository.findById(id)
            .map(variantInfoMapper::toDto);
    }

    /**
     * Delete the variantInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VariantInfo : {}", id);
        variantInfoRepository.deleteById(id);
    }
}
