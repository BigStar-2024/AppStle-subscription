package com.et.service.impl;

import com.et.api.constants.ShopifyIdPrefix;
import com.et.service.CustomerPaymentService;
import com.et.domain.CustomerPayment;
import com.et.repository.CustomerPaymentRepository;
import com.et.service.dto.CustomerPaymentDTO;
import com.et.service.mapper.CustomerPaymentMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.vm.CustomerTokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CustomerPayment}.
 */
@Service
@Transactional
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    private final Logger log = LoggerFactory.getLogger(CustomerPaymentServiceImpl.class);

    private final CustomerPaymentRepository customerPaymentRepository;

    private final CustomerPaymentMapper customerPaymentMapper;

    public CustomerPaymentServiceImpl(CustomerPaymentRepository customerPaymentRepository, CustomerPaymentMapper customerPaymentMapper) {
        this.customerPaymentRepository = customerPaymentRepository;
        this.customerPaymentMapper = customerPaymentMapper;
    }

    @Override
    public CustomerPaymentDTO save(CustomerPayment customerPayment) {
        log.debug("Request to save CustomerPayment : {}", customerPayment);
        customerPayment = customerPaymentRepository.save(customerPayment);
        return customerPaymentMapper.toDto(customerPayment);
    }

    @Override
    public CustomerPaymentDTO save(CustomerPaymentDTO customerPaymentDTO) {
        log.debug("Request to save CustomerPayment : {}", customerPaymentDTO);
        CustomerPayment customerPayment = customerPaymentMapper.toEntity(customerPaymentDTO);
        customerPayment = customerPaymentRepository.save(customerPayment);
        return customerPaymentMapper.toDto(customerPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerPaymentDTO> findAll() {
        log.debug("Request to get all CustomerPayments");
        return customerPaymentRepository.findAll().stream()
            .map(customerPaymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerPaymentDTO> findOne(Long id) {
        log.debug("Request to get CustomerPayment : {}", id);
        return customerPaymentRepository.findById(id)
            .map(customerPaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerPayment : {}", id);
        customerPaymentRepository.deleteById(id);
    }

    @Override
    public Optional<Long> getCustomerIdFromCustomerUid(String customerUid) {
        return customerPaymentRepository.findCustomerIdByCustomerUid(customerUid);
    }

    @Override
    public Optional<CustomerPaymentDTO> findByCustomerIdAndShop(Long customerId, String shop) {
        return customerPaymentRepository.findByCustomerIdAndShop(customerId, shop)
            .map(customerPaymentMapper::toDto);
    }

    @Override
    public Set<Long> findDuplicateRecords() {
        return customerPaymentRepository.findDuplicateRecords();
    }

    @Override
    public List<CustomerPaymentDTO> findByCustomerId(Long customerId) {
        return customerPaymentRepository.findByCustomerId(customerId).stream().map(c -> customerPaymentMapper.toDto(c)).collect(Collectors.toList());
    }

    @Override
    public List<CustomerPaymentDTO> findByShop(String shop) {
        return customerPaymentRepository.findByShop(shop).stream().map(c -> customerPaymentMapper.toDto(c)).collect(Collectors.toList());
    }

    @Override
    public CustomerTokenInfo getCustomerTokenInfo(Long customerId, String shop) {
        Optional<CustomerPaymentDTO> customerPaymentDTOOptional = findByCustomerIdAndShop(customerId, shop);

        if (customerPaymentDTOOptional.isPresent()) {
            CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOOptional.get();
            String token = customerPaymentDTO.getCustomerUid();

            if (StringUtils.isBlank(token)) {
                customerPaymentDTO.setCustomerUid(CommonUtils.generateRandomUid());
                customerPaymentDTO.setTokenCreatedTime(ZonedDateTime.now());
                save(customerPaymentDTO);
            }

            CustomerTokenInfo customerTokenInfo = new CustomerTokenInfo();
            customerTokenInfo.setCustomerId(customerPaymentDTO.getCustomerId());
            customerTokenInfo.setToken(customerPaymentDTO.getCustomerUid());
            return customerTokenInfo;
        } else {
            CustomerPaymentDTO customerPaymentDTO = new CustomerPaymentDTO();
            customerPaymentDTO.setCustomerUid(CommonUtils.generateRandomUid());
            customerPaymentDTO.setTokenCreatedTime(ZonedDateTime.now());
            customerPaymentDTO.setCardExpiryNotificationCounter(0L);
            customerPaymentDTO.setCustomerId(customerId);
            customerPaymentDTO.setShop(shop);
            customerPaymentDTO.setAdminGraphqlApiId(null);
            customerPaymentDTO.setToken(null);
            customerPaymentDTO.setAdminGraphqlApiCustomerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId);
            customerPaymentDTO.setInstrumentType(null);
            customerPaymentDTO.setPaymentInstrumentLastDigits(null);
            save(customerPaymentDTO);

            CustomerTokenInfo customerTokenInfo = new CustomerTokenInfo();
            customerTokenInfo.setCustomerId(customerPaymentDTO.getCustomerId());
            customerTokenInfo.setToken(customerPaymentDTO.getCustomerUid());
            return customerTokenInfo;
        }
    }
}
