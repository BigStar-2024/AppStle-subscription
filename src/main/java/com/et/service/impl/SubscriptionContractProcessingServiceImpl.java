package com.et.service.impl;

import com.et.service.SubscriptionContractProcessingService;
import com.et.domain.SubscriptionContractProcessing;
import com.et.repository.SubscriptionContractProcessingRepository;
import com.et.service.dto.SubscriptionContractProcessingDTO;
import com.et.service.mapper.SubscriptionContractProcessingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SubscriptionContractProcessing}.
 */
@Service
@Transactional
public class SubscriptionContractProcessingServiceImpl implements SubscriptionContractProcessingService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractProcessingServiceImpl.class);

    private final SubscriptionContractProcessingRepository subscriptionContractProcessingRepository;

    private final SubscriptionContractProcessingMapper subscriptionContractProcessingMapper;

    public SubscriptionContractProcessingServiceImpl(SubscriptionContractProcessingRepository subscriptionContractProcessingRepository, SubscriptionContractProcessingMapper subscriptionContractProcessingMapper) {
        this.subscriptionContractProcessingRepository = subscriptionContractProcessingRepository;
        this.subscriptionContractProcessingMapper = subscriptionContractProcessingMapper;
    }

    @Override
    public SubscriptionContractProcessingDTO save(SubscriptionContractProcessingDTO subscriptionContractProcessingDTO) {
        log.debug("Request to save SubscriptionContractProcessing : {}", subscriptionContractProcessingDTO);
        SubscriptionContractProcessing subscriptionContractProcessing = subscriptionContractProcessingMapper.toEntity(subscriptionContractProcessingDTO);
        subscriptionContractProcessing = subscriptionContractProcessingRepository.save(subscriptionContractProcessing);
        return subscriptionContractProcessingMapper.toDto(subscriptionContractProcessing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionContractProcessingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionContractProcessings");
        return subscriptionContractProcessingRepository.findAll(pageable)
            .map(subscriptionContractProcessingMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionContractProcessingDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionContractProcessing : {}", id);
        return subscriptionContractProcessingRepository.findById(id)
            .map(subscriptionContractProcessingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionContractProcessing : {}", id);
        subscriptionContractProcessingRepository.deleteById(id);
    }

    @Override
    public Optional<SubscriptionContractProcessingDTO> findByContractId(Long contractId) {
        return subscriptionContractProcessingRepository.findByContractId(contractId).map(subscriptionContractProcessingMapper::toDto);
    }
}
