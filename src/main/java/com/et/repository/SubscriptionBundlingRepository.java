package com.et.repository;

import com.et.domain.SubscriptionBundling;
import com.et.domain.enumeration.BuildABoxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionBundling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionBundlingRepository extends JpaRepository<SubscriptionBundling, Long> {

    List<SubscriptionBundling> findByShopAndSubscriptionId(String shop, Long subscriptionId);

    Optional<SubscriptionBundling> findByShopAndUniqueRef(String shop, String uniqueRef);

    List<SubscriptionBundling> findByShop(String shop);

    List<SubscriptionBundling> findByShopAndBuildABoxType(String shop, BuildABoxType buildABoxType);

}
