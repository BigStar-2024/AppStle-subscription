package com.et.service.impl;

import com.et.service.DunningManagementService;
import com.et.domain.DunningManagement;
import com.et.repository.DunningManagementRepository;
import com.et.service.dto.DunningManagementDTO;
import com.et.service.mapper.DunningManagementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DunningManagement}.
 */
@Service
@Transactional
public class DunningManagementServiceImpl implements DunningManagementService {

    private final Logger log = LoggerFactory.getLogger(DunningManagementServiceImpl.class);

    private final DunningManagementRepository dunningManagementRepository;

    private final DunningManagementMapper dunningManagementMapper;

    public DunningManagementServiceImpl(DunningManagementRepository dunningManagementRepository, DunningManagementMapper dunningManagementMapper) {
        this.dunningManagementRepository = dunningManagementRepository;
        this.dunningManagementMapper = dunningManagementMapper;
    }

    @Override
    public DunningManagementDTO save(DunningManagementDTO dunningManagementDTO) {
        log.debug("Request to save DunningManagement : {}", dunningManagementDTO);
        DunningManagement dunningManagement = dunningManagementMapper.toEntity(dunningManagementDTO);
        dunningManagement = dunningManagementRepository.save(dunningManagement);
        return dunningManagementMapper.toDto(dunningManagement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DunningManagementDTO> findAll() {
        log.debug("Request to get all DunningManagements");
        return dunningManagementRepository.findAll().stream()
            .map(dunningManagementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DunningManagementDTO> findAllByShop(String shop) {
        log.debug("Request to get all DunningManagements by shop");
        return dunningManagementRepository.findAllByShop(shop).stream()
            .map(dunningManagementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<DunningManagementDTO> findOne(Long id) {
        log.debug("Request to get DunningManagement : {}", id);
        return dunningManagementRepository.findById(id)
            .map(dunningManagementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DunningManagementDTO> findOneByShop(String shop) {
        log.debug("Request to get DunningManagement by shop: {}", shop);
        return dunningManagementRepository.findByShop(shop)
            .map(dunningManagementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DunningManagement : {}", id);
        dunningManagementRepository.deleteById(id);
    }

    @Override
    public void deleteByShop(String shop) {
        dunningManagementRepository.deleteByShop(shop);
    }
}
