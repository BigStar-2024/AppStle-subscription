package com.et.repository;

import com.et.domain.PaymentPlanActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentPlanActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentPlanActivityRepository extends JpaRepository<PaymentPlanActivity, Long> {

}
