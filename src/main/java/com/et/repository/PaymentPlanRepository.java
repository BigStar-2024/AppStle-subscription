package com.et.repository;

import com.et.domain.PaymentPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PaymentPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

    Optional<PaymentPlan> findByShop(String shop);

    @Modifying
    @Query("delete from PaymentPlan pp where pp.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
