package com.et.repository;

import com.et.domain.SubscriptionContractOneOff;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import java.util.List;

/**
 * Spring Data  repository for the SubscriptionContractOneOff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionContractOneOffRepository extends JpaRepository<SubscriptionContractOneOff, Long> {

    List<SubscriptionContractOneOff> findByShopAndSubscriptionContractId(String shop, Long subscriptionContractId);

    List<SubscriptionContractOneOff> findByShopAndSubscriptionContractIdAndBillingAttemptId(String shop, Long subscriptionContractId, Long billingAttemptId);

    int deleteByShopAndSubscriptionContractIdAndBillingAttemptIdAndVariantId(String shop, Long subscriptionContractId, Long billingAttemptId, Long variantId);

}
