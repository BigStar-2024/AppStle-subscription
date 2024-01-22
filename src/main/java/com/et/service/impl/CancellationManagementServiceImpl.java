package com.et.service.impl;

import com.et.service.CancellationManagementService;
import com.et.domain.CancellationManagement;
import com.et.repository.CancellationManagementRepository;
import com.et.service.dto.CancellationManagementDTO;
import com.et.service.mapper.CancellationManagementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CancellationManagement}.
 */
@Service
@Transactional
public class CancellationManagementServiceImpl implements CancellationManagementService {

    private final Logger log = LoggerFactory.getLogger(CancellationManagementServiceImpl.class);

    private final CancellationManagementRepository cancellationManagementRepository;

    private final CancellationManagementMapper cancellationManagementMapper;

    public CancellationManagementServiceImpl(CancellationManagementRepository cancellationManagementRepository, CancellationManagementMapper cancellationManagementMapper) {
        this.cancellationManagementRepository = cancellationManagementRepository;
        this.cancellationManagementMapper = cancellationManagementMapper;
    }

    @Override
    public CancellationManagementDTO save(CancellationManagementDTO cancellationManagementDTO) {
        log.debug("Request to save CancellationManagement : {}", cancellationManagementDTO);
        CancellationManagement cancellationManagement = cancellationManagementMapper.toEntity(cancellationManagementDTO);
        cancellationManagement = cancellationManagementRepository.save(cancellationManagement);
        return cancellationManagementMapper.toDto(cancellationManagement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationManagementDTO> findAll() {
        log.debug("Request to get all CancellationManagements");
        return cancellationManagementRepository.findAll().stream()
            .map(cancellationManagementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CancellationManagementDTO> findOne(Long id) {
        log.debug("Request to get CancellationManagement : {}", id);
        return cancellationManagementRepository.findById(id)
            .map(cancellationManagementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CancellationManagement : {}", id);
        cancellationManagementRepository.deleteById(id);
    }

    @Override
    public Optional<CancellationManagementDTO> findByShop(String shop) {
        return cancellationManagementRepository.findByShop(shop)
            .map(cancellationManagementMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        cancellationManagementRepository.deleteByShop(shop);
    }
}
