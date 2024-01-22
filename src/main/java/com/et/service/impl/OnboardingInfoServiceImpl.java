package com.et.service.impl;

import com.et.service.OnboardingInfoService;
import com.et.domain.OnboardingInfo;
import com.et.repository.OnboardingInfoRepository;
import com.et.service.dto.OnboardingInfoDTO;
import com.et.service.mapper.OnboardingInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link OnboardingInfo}.
 */
@Service
@Transactional
public class OnboardingInfoServiceImpl implements OnboardingInfoService {

    private final Logger log = LoggerFactory.getLogger(OnboardingInfoServiceImpl.class);

    private final OnboardingInfoRepository onboardingInfoRepository;

    private final OnboardingInfoMapper onboardingInfoMapper;

    public OnboardingInfoServiceImpl(OnboardingInfoRepository onboardingInfoRepository, OnboardingInfoMapper onboardingInfoMapper) {
        this.onboardingInfoRepository = onboardingInfoRepository;
        this.onboardingInfoMapper = onboardingInfoMapper;
    }

    @Override
    public OnboardingInfoDTO save(OnboardingInfoDTO onboardingInfoDTO) {
        log.debug("Request to save OnboardingInfo : {}", onboardingInfoDTO);
        OnboardingInfo onboardingInfo = onboardingInfoMapper.toEntity(onboardingInfoDTO);
        onboardingInfo = onboardingInfoRepository.save(onboardingInfo);
        return onboardingInfoMapper.toDto(onboardingInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnboardingInfoDTO> findAll() {
        log.debug("Request to get all OnboardingInfos");
        return onboardingInfoRepository.findAll().stream()
            .map(onboardingInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<OnboardingInfoDTO> findOne(Long id) {
        log.debug("Request to get OnboardingInfo : {}", id);
        return onboardingInfoRepository.findById(id)
            .map(onboardingInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OnboardingInfo : {}", id);
        onboardingInfoRepository.deleteById(id);
    }

    @Override
    public Optional<OnboardingInfoDTO> findByShop(String shop) {
        return onboardingInfoRepository.findByShop(shop)
            .map(onboardingInfoMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        onboardingInfoRepository.deleteByShop(shop);
    }
}
