package com.et.repository;

import com.et.domain.SubscriptionContractProcessing;

import com.et.service.dto.SubscriptionContractProcessingDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionContractProcessing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionContractProcessingRepository extends JpaRepository<SubscriptionContractProcessing, Long> {

    Optional<SubscriptionContractProcessing> findByContractId(Long contractId);
}
