package com.et.repository;

import com.et.domain.CustomerPayment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the CustomerPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    @Query("select cp.customerId from CustomerPayment cp where cp.customerUid =:customerUid")
    Optional<Long> findCustomerIdByCustomerUid(@Param("customerUid") String customerUid);

    Optional<CustomerPayment> findTop1ByCustomerId(Long customerId);

    Optional<CustomerPayment> findTop1ByCustomerIdAndShop(Long customerId, String shop);

    Optional<CustomerPayment> findByCustomerIdAndShop(Long customerId, String shop);

    List<CustomerPayment> findByCustomerId(Long customerId);

    List<CustomerPayment> findByShop(String shop);

    @Query("SELECT count (cp.customerId) " +
        "FROM CustomerPayment cp " +
        "WHERE cp.shop =:shop " +
        "AND cp.tokenCreatedTime is not null " +
        "AND cp.tokenCreatedTime >=:fromDate " +
        "AND cp.tokenCreatedTime <=:toDate ")
    Optional<Long> getTotalCustomerCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT distinct c.customerId from CustomerPayment c group by c.customerId having count (c.customerId) > 1")
    Set<Long> findDuplicateRecords();

    @Query("select cp from CustomerPayment cp where tokenCreatedTime IS NULL OR DATE(tokenCreatedTime) = DATE(:tokenCreatedTime)")
    List<CustomerPayment> findCustomerByTokenCreatedTime(@Param("tokenCreatedTime") ZonedDateTime tokenCreatedTime);
}
