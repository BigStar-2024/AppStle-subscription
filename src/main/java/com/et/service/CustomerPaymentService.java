package com.et.service;

import com.et.domain.CustomerPayment;
import com.et.service.dto.CustomerPaymentDTO;
import com.et.web.rest.vm.CustomerTokenInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link com.et.domain.CustomerPayment}.
 */
public interface CustomerPaymentService {


    /**
     * Save a customerPayment.
     *
     * @param customerPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerPaymentDTO save(CustomerPayment customerPayment);

    /**
     * Save a customerPayment.
     *
     * @param customerPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerPaymentDTO save(CustomerPaymentDTO customerPaymentDTO);

    /**
     * Get all the customerPayments.
     *
     * @return the list of entities.
     */
    List<CustomerPaymentDTO> findAll();


    /**
     * Get the "id" customerPayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerPaymentDTO> findOne(Long id);

    /**
     * Delete the "id" customerPayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Long> getCustomerIdFromCustomerUid(String customerUid);

    Optional<CustomerPaymentDTO> findByCustomerIdAndShop(Long customerId, String shop);

    Set<Long> findDuplicateRecords();

    List<CustomerPaymentDTO> findByCustomerId(Long customerId);

    List<CustomerPaymentDTO> findByShop(String shop);

    CustomerTokenInfo getCustomerTokenInfo(Long customerId, String shop);
}
