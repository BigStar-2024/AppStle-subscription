package com.et.service;

import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.service.dto.SubscriptionContractOneOffDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionContractOneOff}.
 */
public interface SubscriptionContractOneOffService {

    /**
     * Save a subscriptionContractOneOff.
     *
     * @param subscriptionContractOneOffDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionContractOneOffDTO save(SubscriptionContractOneOffDTO subscriptionContractOneOffDTO);

    /**
     * Get all the subscriptionContractOneOffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriptionContractOneOffDTO> findAll(Pageable pageable);


    /**
     * Get the "id" subscriptionContractOneOff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionContractOneOffDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionContractOneOff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SubscriptionContractOneOffDTO> findByShopAndSubscriptionContractId(String shop, Long subscriptionContractId);

    @Transactional(readOnly = true)
    List<SubscriptionContractOneOffDTO> findByShopAndSubscriptionContractIdAndBillingAttemptId(String shop, Long subscriptionContractId, Long billingAttemptId);

    void deleteByShopAndContractIdAndBillingAttemptIdAndVariantId(String shop, Long contractId, Long billingAttemptId, Long variantId);

    SubscriptionContractOneOffDTO subscriptionContractUpdateOneOffQuantity(String shop, Long contractId, Long billingAttemptId, Long variantId, Integer quantity, ActivityLogEventSource eventSource) throws JsonProcessingException;

    List<SubscriptionContractOneOffDTO> getOneOffForContractNextOrder(String shop, Long contractId);
}
