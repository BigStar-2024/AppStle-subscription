package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.SubscriptionContractSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SubscriptionContractSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionContractSettingsRepository extends JpaRepository<SubscriptionContractSettings, Long> {
    List<SubscriptionContractSettings> findByShop(String shop);
}
