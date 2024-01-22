package com.et.service;

import com.et.domain.User;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.BillingAttemptStatus;
import com.et.pojo.SubscriptionBillingAttemptDetails;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionBillingAttempt}.
 */
public interface SubscriptionBillingAttemptService {

    /**
     * Save a subscriptionBillingAttempt.
     *
     * @param subscriptionBillingAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionBillingAttemptDTO save(SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO);

    /**
     * Get all the subscriptionBillingAttempts.
     *
     * @return the list of entities.
     */
    List<SubscriptionBillingAttemptDTO> findAll();


    /**
     * Get the "id" subscriptionBillingAttempt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionBillingAttemptDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionBillingAttempt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Finds the next billing date on subscriptionContractId
     * @param subscriptionContractId
     * @return ZonedDateTime
     */
    Optional<ZonedDateTime> findNextBillingDateForContractId(Long subscriptionContractId);

    Optional<Long> findNextBillingIdForContractId(Long subscriptionContractId);

    List<SubscriptionBillingAttemptDTO> getTopOrdersByShop(String shop);

    List<SubscriptionBillingAttemptDTO> getTopOrdersByContractId(Long contractId, String shop);

    List<SubscriptionBillingAttemptDTO> getTopOrdersByCustomerId(Long customerId, String shop);

    Page<SubscriptionBillingAttemptDTO> getPastOrdersByShop(Pageable pageable, String shop);

    Page<SubscriptionBillingAttemptDTO> getPastOrdersByContractId(Pageable pageable, Long contractId, String shop);

    Page<SubscriptionBillingAttemptDTO> getPastOrdersByCustomerId(Pageable pageable, Long customerId, String shop);

    Optional<SubscriptionBillingAttemptDTO> skipOrder(String shop, Long id);

    void deleteByStatusAndContractId(BillingAttemptStatus status, Long contractId);

    void deleteByContractIdAndStatusAndBillingDateAfter(Long contractId, BillingAttemptStatus status, ZonedDateTime date);

    @Async
    @Transactional
    void asyncAttemptBilling(String shop, Long contractId, ZonedDateTime nextBillingDate, Integer billingIntervalCount, String billingInterval, Integer maxCycles, ActivityLogEventSource eventSource) throws Exception;

    Optional<SubscriptionBillingAttemptDTO> attemptBilling(String shop, Long id, ActivityLogEventSource eventSource) throws Exception;

    Optional<SubscriptionBillingAttemptDTO>  findById(Long id);

    void exportUpcomingSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    Page<SubscriptionBillingAttemptDTO> getPastOrdersByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status);

    Page<SubscriptionBillingAttemptDetails> getPastOrdersDetailsByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status);

    Page<SubscriptionBillingAttemptDTO> getUpcomingOrdersByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status);

    Page<SubscriptionBillingAttemptDetails> getUpcomingOrdersDetailsByShop(Pageable pageable, String shop, Long contractId, BillingAttemptStatus status);

    void exportSuccessSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    void exportFailedSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    List<SubscriptionBillingAttemptDTO> findByContractIdAndStatusAndShop(Long contractId, BillingAttemptStatus billingAttemptStatus, String shop);

    List<SubscriptionBillingAttemptDTO> findByContractIdAndStatus(Long contractId, BillingAttemptStatus billingAttemptStatus);

    List<SubscriptionBillingAttemptDTO> findRecentAttemptsWithStatus(String shop, BillingAttemptStatus success, ZonedDateTime zonedDateTime);

    @Transactional(readOnly = true)
    List<SubscriptionBillingAttemptDTO> findByContractIdAndStatusAndShop(String shop, Long contractId, BillingAttemptStatus billingAttemptStatus);

    void exportSkippedSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    void exportSkippedByDunningMgmtSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    void exportSkippedByInventoryMgmtSubscriptionBillingAttempt(String shop, User user, String emailId, Long contractId);

    void deleteByContractId(Long specificSubscriptionId);

    void deleteByContractIdIn(Set<Long> contractIds);

    List<Long> findDuplicateBillingAttemptContractIds();

    List<SubscriptionBillingAttemptDTO> findBillingAttemptsInProgressState(ZonedDateTime attemptTime);

    void updateUSDOrderAmount(String orderId, Double amount);
}
