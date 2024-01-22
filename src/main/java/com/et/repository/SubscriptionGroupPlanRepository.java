package com.et.repository;

import com.et.domain.SubscriptionGroupPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionGroupPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionGroupPlanRepository extends JpaRepository<SubscriptionGroupPlan, Long> {
    SubscriptionGroupPlan findByShopAndSubscriptionId(String shop, Long subscriptionId);

    List<SubscriptionGroupPlan> findByShopAndSubscriptionIdIn(String shop, List<Long> subscriptionId);

    List<SubscriptionGroupPlan> findAllByShop(String shop);

    Optional<SubscriptionGroupPlan> findBySubscriptionId(Long subscriptionId);

    @Query("select a from SubscriptionGroupPlan a " +
        "where a.subscriptionId <> :subscriptionId" +
        " and a.shop = :shop")
    List<SubscriptionGroupPlan> getGetProductIdsExceptSubscription(@Param("shop") String shop, @Param("subscriptionId") Long subscriptionId);

    List<SubscriptionGroupPlan> findByShop(String shop);

    @Query(value = "select sgp from SubscriptionGroupPlan sgp where sgp.shop = :shop and sgp.productIds like %:productId%")
    List<SubscriptionGroupPlan> findByProductId(@Param("shop") String shop, @Param("productId") Long productId);

    @Query(value = "select sgp from SubscriptionGroupPlan sgp where sgp.shop = :shop and sgp.variantIds like %:variantId%")
    List<SubscriptionGroupPlan> findByVariantId(@Param("shop") String shop, @Param("variantId") Long variantId);

    @Query(value = "select sgp from SubscriptionGroupPlan sgp where sgp.shop = :shop and (sgp.variantIds like %:variantId% or sgp.productIds like %:productId%)")
    List<SubscriptionGroupPlan> findByVariantIdOrProductId(@Param("shop") String shop, @Param("variantId") Long variantId, @Param("productId") Long productId);
}
