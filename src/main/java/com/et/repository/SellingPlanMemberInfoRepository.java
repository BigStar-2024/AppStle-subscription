package com.et.repository;

import com.et.domain.SellingPlanMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SellingPlanMemberInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellingPlanMemberInfoRepository extends JpaRepository<SellingPlanMemberInfo, Long> {

    Optional<SellingPlanMemberInfo> findByShopAndSellingPlanId(String shop, Long sellingPlanId);
    List<SellingPlanMemberInfo> findAllByShopAndSubscriptionId(String shop, Long subscriptionId);

    List<SellingPlanMemberInfo> findByShop(String shop);
}
