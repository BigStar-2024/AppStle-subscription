package com.et.repository;

import com.et.domain.PlanInfoDiscount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PlanInfoDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanInfoDiscountRepository extends JpaRepository<PlanInfoDiscount, Long> {
    Optional<PlanInfoDiscount> findByDiscountCodeIgnoreCase(String discountCode);
}
