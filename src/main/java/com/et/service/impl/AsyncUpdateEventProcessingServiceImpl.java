package com.et.service.impl;

import com.et.service.AsyncUpdateEventProcessingService;
import com.et.domain.AsyncUpdateEventProcessing;
import com.et.repository.AsyncUpdateEventProcessingRepository;
import com.et.service.dto.AsyncUpdateEventProcessingDTO;
import com.et.service.mapper.AsyncUpdateEventProcessingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AsyncUpdateEventProcessing}.
 */
@Service
@Transactional
public class AsyncUpdateEventProcessingServiceImpl implements AsyncUpdateEventProcessingService {

    private final Logger log = LoggerFactory.getLogger(AsyncUpdateEventProcessingServiceImpl.class);

    private final AsyncUpdateEventProcessingRepository asyncUpdateEventProcessingRepository;

    private final AsyncUpdateEventProcessingMapper asyncUpdateEventProcessingMapper;

    public AsyncUpdateEventProcessingServiceImpl(AsyncUpdateEventProcessingRepository asyncUpdateEventProcessingRepository, AsyncUpdateEventProcessingMapper asyncUpdateEventProcessingMapper) {
        this.asyncUpdateEventProcessingRepository = asyncUpdateEventProcessingRepository;
        this.asyncUpdateEventProcessingMapper = asyncUpdateEventProcessingMapper;
    }

    @Override
    public AsyncUpdateEventProcessingDTO save(AsyncUpdateEventProcessingDTO asyncUpdateEventProcessingDTO) {
        log.debug("Request to save AsyncUpdateEventProcessing : {}", asyncUpdateEventProcessingDTO);
        AsyncUpdateEventProcessing asyncUpdateEventProcessing = asyncUpdateEventProcessingMapper.toEntity(asyncUpdateEventProcessingDTO);
        asyncUpdateEventProcessing = asyncUpdateEventProcessingRepository.save(asyncUpdateEventProcessing);
        return asyncUpdateEventProcessingMapper.toDto(asyncUpdateEventProcessing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AsyncUpdateEventProcessingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AsyncUpdateEventProcessings");
        return asyncUpdateEventProcessingRepository.findAll(pageable)
            .map(asyncUpdateEventProcessingMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AsyncUpdateEventProcessingDTO> findOne(Long id) {
        log.debug("Request to get AsyncUpdateEventProcessing : {}", id);
        return asyncUpdateEventProcessingRepository.findById(id)
            .map(asyncUpdateEventProcessingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AsyncUpdateEventProcessing : {}", id);
        asyncUpdateEventProcessingRepository.deleteById(id);
    }
}
