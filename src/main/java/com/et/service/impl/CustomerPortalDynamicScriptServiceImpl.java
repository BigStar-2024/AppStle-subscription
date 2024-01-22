package com.et.service.impl;

import com.et.service.CustomerPortalDynamicScriptService;
import com.et.domain.CustomerPortalDynamicScript;
import com.et.repository.CustomerPortalDynamicScriptRepository;
import com.et.service.dto.CustomerPortalDynamicScriptDTO;
import com.et.service.mapper.CustomerPortalDynamicScriptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CustomerPortalDynamicScript}.
 */
@Service
@Transactional
public class CustomerPortalDynamicScriptServiceImpl implements CustomerPortalDynamicScriptService {

    private final Logger log = LoggerFactory.getLogger(CustomerPortalDynamicScriptServiceImpl.class);

    private final CustomerPortalDynamicScriptRepository customerPortalDynamicScriptRepository;

    private final CustomerPortalDynamicScriptMapper customerPortalDynamicScriptMapper;

    public CustomerPortalDynamicScriptServiceImpl(CustomerPortalDynamicScriptRepository customerPortalDynamicScriptRepository, CustomerPortalDynamicScriptMapper customerPortalDynamicScriptMapper) {
        this.customerPortalDynamicScriptRepository = customerPortalDynamicScriptRepository;
        this.customerPortalDynamicScriptMapper = customerPortalDynamicScriptMapper;
    }

    @Override
    public CustomerPortalDynamicScriptDTO save(CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO) {
        log.debug("Request to save CustomerPortalDynamicScript : {}", customerPortalDynamicScriptDTO);
        CustomerPortalDynamicScript customerPortalDynamicScript = customerPortalDynamicScriptMapper.toEntity(customerPortalDynamicScriptDTO);
        customerPortalDynamicScript = customerPortalDynamicScriptRepository.save(customerPortalDynamicScript);
        return customerPortalDynamicScriptMapper.toDto(customerPortalDynamicScript);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerPortalDynamicScriptDTO> findAll() {
        log.debug("Request to get all CustomerPortalDynamicScripts");
        return customerPortalDynamicScriptRepository.findAll().stream()
            .map(customerPortalDynamicScriptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerPortalDynamicScriptDTO> findOne(Long id) {
        log.debug("Request to get CustomerPortalDynamicScript : {}", id);
        return customerPortalDynamicScriptRepository.findById(id)
            .map(customerPortalDynamicScriptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerPortalDynamicScript : {}", id);
        customerPortalDynamicScriptRepository.deleteById(id);
    }

    @Override
    public Optional<CustomerPortalDynamicScriptDTO> findByShop(String shop) {
        return customerPortalDynamicScriptRepository.findByShop(shop)
            .map(customerPortalDynamicScriptMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        customerPortalDynamicScriptRepository.deleteByShop(shop);
    }
}
