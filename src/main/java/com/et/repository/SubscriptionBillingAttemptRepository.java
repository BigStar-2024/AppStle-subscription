package com.et.repository;

import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.enumeration.BillingAttemptStatus;
import com.et.pojo.OrderSummeryByDate;
import com.et.pojo.SubscriptionBillingAttemptDetails;
import com.et.service.dto.ContractAnalytics;
import com.et.service.dto.OrderAmountByDate;
import com.et.service.dto.OrderCountByDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the SubscriptionBillingAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionBillingAttemptRepository extends JpaRepository<SubscriptionBillingAttempt, Long>, JpaSpecificationExecutor<SubscriptionBillingAttempt> {

    @Query("SELECT SUM (sba.orderAmountUSD) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Double> getTotalSoldAmountByDateRange(@Param("shop") String shop,
                                                   @Param("fromDate") ZonedDateTime fromDate,
                                                   @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (sba.orderId) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Long> getTotalSoldAmountByDateRangeAVG(@Param("shop") String shop,
                                                    @Param("fromDate") ZonedDateTime fromDate,
                                                    @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (sba.orderId) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Long> getTotalOrderCountByDateRange(@Param("shop") String shop,
                                                 @Param("fromDate") ZonedDateTime fromDate,
                                                 @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT sum (sba.orderAmount) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Double> getTotalOrderAmountByDateRange(@Param("shop") String shop,
                                                 @Param("fromDate") ZonedDateTime fromDate,
                                                 @Param("toDate") ZonedDateTime toDate);


    @Query("SELECT SUM (sba.orderAmount) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Double> getTotalSoldRevenueByDateRange(@Param("shop") String shop,
                                                    @Param("fromDate") ZonedDateTime fromDate,
                                                    @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (sba.id) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Long> countNewRecurringOrders(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT sba " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SKIPPED " +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    List<SubscriptionBillingAttempt> getSkippedSubscriptionByDateRange(@Param("shop") String shop,
                                                                       @Param("fromDate") ZonedDateTime fromDate,
                                                                       @Param("toDate") ZonedDateTime toDate);


    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W') AS orderCreatedAt, sum(subscription_billing_attempt.order_amount_usd) AS sum\n" +
            "FROM subscription_billing_attempt\n" +
            "WHERE subscription_billing_attempt.shop = ?1\n" +
            "AND subscription_billing_attempt.attempt_time >= ?2\n" +
            "AND subscription_billing_attempt.attempt_time <= ?3\n" +
            "AND subscription_billing_attempt.status = 'SUCCESS'\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<OrderAmountByDate> orderAmountByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W') AS orderCreatedAt, count(subscription_billing_attempt.id) AS total\n" +
            "FROM subscription_billing_attempt\n" +
            "WHERE subscription_billing_attempt.shop = ?1\n" +
            "AND subscription_billing_attempt.attempt_time >= ?2\n" +
            "AND subscription_billing_attempt.attempt_time <= ?3\n" +
            "AND subscription_billing_attempt.status = 'SUCCESS'\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_billing_attempt.attempt_time), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<OrderCountByDate> orderCountByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    Optional<SubscriptionBillingAttempt> findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(Long contractId, BillingAttemptStatus status);

    @Query(value = "select * from subscription_billing_attempt as sba where billing_date >= \n" +
        "(SELECT IFNULL((select next_billing_date from subscription_contract_details where shop = ?1 and status = 'active' order by next_billing_date asc limit 1) , null) as next_billing_date) \n" +
        "and (sba.status = 'QUEUED' or sba.status='SKIPPED') and shop = ?1 order by sba.billing_date asc limit 5", nativeQuery = true)
    List<SubscriptionBillingAttempt> findTopOrdersByShop(String shop);

    @Query(value = "SELECT sba.* FROM subscription_billing_attempt as sba where sba.contract_Id = ?1 and shop = ?2 and (sba.status = 'QUEUED' or sba.status = 'SKIPPED') and sba.billing_date >= \n" +
        "(SELECT IFNULL((select scd.next_billing_date from subscription_contract_details as scd where scd.shop = ?2 and scd.subscription_contract_id = ?1 and scd.status = 'active' order by scd.next_billing_date asc limit 1),null) as next_billing_date) \n" +
        "ORDER BY sba.billing_date asc LIMIT 5", nativeQuery = true)
    List<SubscriptionBillingAttempt> findTopOrderByContractId(Long contractId, String shop);

    @Query(value = "select * from subscription_billing_attempt as sba where billing_date >= \n" +
        "(SELECT IFNULL((select next_billing_date from subscription_contract_details where shop = ?2 and status = 'active' and customer_id = ?1 order by next_billing_date asc limit 1), null ) as next_billing_date) \n" +
        "and (sba.status = 'QUEUED' or sba.status='SKIPPED') and shop = ?2 order by sba.billing_date asc limit 5", nativeQuery = true)
    List<SubscriptionBillingAttempt> findTopOrdersByCustomerId(Long customerId, String shop);

    @Query(value = "select sba from SubscriptionBillingAttempt sba where sba.status != 'QUEUED' and sba.shop = :shop ORDER BY sba.billingDate desc ")
    Page<SubscriptionBillingAttempt> findPastOrdersByShop(Pageable pageable, @Param("shop") String shop);

    @Query(value = "select sbt.orderId from SubscriptionBillingAttempt sbt where sbt.orderId is not null and sbt.shop = :shop and sbt.orderAmountUSD is null ")
    Set<Long> findSuccessfulOrderIdsFor(@Param("shop") String shop);

    @Query(value = "select sba from SubscriptionBillingAttempt sba where sba.status != 'QUEUED' and sba.shop = :shop and sba.contractId = :contractId ORDER BY sba.billingDate desc ")
    Page<SubscriptionBillingAttempt> findPastOrderByContractId(Pageable pageable, @Param("contractId") Long contractId, @Param("shop") String shop);

    @Query(value = "select sba from SubscriptionBillingAttempt sba join SubscriptionContractDetails scd on sba.contractId = scd.subscriptionContractId where sba.shop = :shop and scd.customerId = :customerId and sba.status != 'QUEUED' ORDER BY sba.billingDate desc ")
    Page<SubscriptionBillingAttempt> findPastOrdersByCustomerId(Pageable pageable, @Param("customerId") Long customerId, @Param("shop") String shop);

    @Query("UPDATE SubscriptionBillingAttempt sba set sba.status =:billingAttemptStatus where sba.shop =:shop and sba.id =:id")
    @Modifying
    int skipOrder(@Param("shop") String shop, @Param("id") Long id, @Param("billingAttemptStatus") BillingAttemptStatus billingAttemptStatus);

    @Modifying
    @Query(value = "delete from subscription_billing_attempt where contract_id = :contractId and status = :status", nativeQuery = true)
    @Transactional
    int deleteByStatusAndContractId(@Param("status") String status, @Param("contractId") Long contractId);

    @Modifying
    @Transactional
    int deleteByContractIdAndStatusAndBillingDateAfter(Long contractId, BillingAttemptStatus status, ZonedDateTime date);

    List<SubscriptionBillingAttempt> findByShopAndContractIdAndStatus(String shop, Long contractId, BillingAttemptStatus status);

    List<SubscriptionBillingAttempt> findByShopAndContractIdAndStatusIn(String shop, Long contractId, List<BillingAttemptStatus> status);

    List<SubscriptionBillingAttempt> findByShopAndStatus(String shop, BillingAttemptStatus status);

    @Query("SELECT SUM (scd.orderAmountUSD) " +
        "FROM SubscriptionBillingAttempt sba " +
        "JOIN SubscriptionContractDetails scd ON sba.contractId = scd.subscriptionContractId " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.QUEUED " +
        "AND scd.status = 'active' " +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    Optional<Double> getTotalNextSevenOrThirtyOrNinetyDayOrderAmountByDateRange(@Param("shop") String shop,
                                                                                @Param("fromDate") ZonedDateTime fromDate,
                                                                                @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT SUM (scd.orderAmountUSD) " +
        "FROM SubscriptionBillingAttempt sba " +
        "JOIN SubscriptionContractDetails scd ON sba.contractId = scd.subscriptionContractId " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    Optional<Double> getPastDaysOrderRevenueTotal(@Param("shop") String shop,
                                                  @Param("fromDate") ZonedDateTime fromDate,
                                                  @Param("toDate") ZonedDateTime toDate);

    @Query("select new com.et.service.dto.ContractAnalytics(count(sba.id), sum(sba.orderAmount)) from SubscriptionBillingAttempt sba where sba.contractId = :contractId and sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS and sba.orderAmount is not null")
    Optional<ContractAnalytics> findTotalOrderAndTotalRevenueByContractId(@Param("contractId") Long contractId);

    List<SubscriptionBillingAttempt> findByShop(String shop);

    Optional<SubscriptionBillingAttempt> findByShopAndContractIdAndBillingAttemptId(String shop, Long contractId, String billingAttemptId);

    Optional<SubscriptionBillingAttempt> findById(Long id);

    @Query("SELECT sba " +
        "FROM SubscriptionBillingAttempt sba " +
        "JOIN SubscriptionContractDetails scd ON sba.contractId = scd.subscriptionContractId " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.FAILURE " +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    List<SubscriptionBillingAttempt> getFailedAttemptsDateRange(@Param("shop") String shop,
                                                                @Param("fromDate") ZonedDateTime fromDate,
                                                                @Param("toDate") ZonedDateTime toDate);

    @Query(value = "select a from SubscriptionBillingAttempt a " +
        " where a.shop = :shop " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) "
    )
    List<SubscriptionBillingAttempt> getPastOrders(@Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);

    @Query(value = "select new com.et.pojo.SubscriptionBillingAttemptDetails(a, scd) from SubscriptionBillingAttempt a JOIN SubscriptionContractDetails scd ON a.contractId = scd.subscriptionContractId " +
        " where a.shop = :shop " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) "
    )
    List<SubscriptionBillingAttemptDetails> getPastOrdersDetails(@Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);

    @Query(value = "select a from SubscriptionBillingAttempt a " +
        " where a.shop = :shop " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) " +
        " ORDER BY a.billingDate desc "
    )
    Page<SubscriptionBillingAttempt> getPastOrdersByShop(Pageable pageable, @Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);

    @Query(value = "select new com.et.pojo.SubscriptionBillingAttemptDetails(a, scd) from SubscriptionBillingAttempt a JOIN SubscriptionContractDetails scd ON a.contractId = scd.subscriptionContractId " +
        " where a.shop = :shop " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) " +
        " ORDER BY a.billingDate desc "
    )
    Page<SubscriptionBillingAttemptDetails> getPastOrdersDetailsByShop(Pageable pageable, @Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);

    @Query(value = "select new com.et.pojo.SubscriptionBillingAttemptDetails(a, scd) from SubscriptionBillingAttempt a JOIN SubscriptionContractDetails scd ON a.contractId = scd.subscriptionContractId " +
        " where a.shop = :shop " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) " +
        " ORDER BY a.attemptTime desc "
    )
    Page<SubscriptionBillingAttemptDetails> getSuccessfulPastOrdersDetailsByShop(Pageable pageable, @Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);


    @Query(value = "select a from SubscriptionBillingAttempt a " +
        " JOIN SubscriptionContractDetails scd ON a.contractId = scd.subscriptionContractId " +
        " where a.shop = :shop " +
        " AND scd.status = 'active' " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) " +
        " ORDER BY a.billingDate asc "
    )
    Page<SubscriptionBillingAttempt> getUpcomingOrdersByShop(Pageable pageable, @Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);

    @Query(value = "select new com.et.pojo.SubscriptionBillingAttemptDetails(a, scd) from SubscriptionBillingAttempt a JOIN SubscriptionContractDetails scd ON a.contractId = scd.subscriptionContractId " +
        " where a.shop = :shop " +
        " AND scd.status = 'active' " +
        " AND a.status = :status " +
        " AND (:contractId is null OR a.contractId = :contractId) " +
        " ORDER BY a.billingDate asc "
    )
    Page<SubscriptionBillingAttemptDetails> getUpcomingOrdersDetails(Pageable pageable, @Param("shop") String shop, @Param("contractId") Long contractId, @Param("status") BillingAttemptStatus status);


    @Query("SELECT count (sba.orderId) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.CONTRACT_PAUSED " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Long> getTotalPausedSubscriptionCountByDateRange(@Param("shop") String shop,
                                                              @Param("fromDate") ZonedDateTime fromDate,
                                                              @Param("toDate") ZonedDateTime toDate);

    List<SubscriptionBillingAttempt> findByContractIdAndStatus(Long contractId, BillingAttemptStatus billingAttemptStatus);

    List<SubscriptionBillingAttempt> findByContractIdAndStatusAndShop(Long contractId, BillingAttemptStatus billingAttemptStatus, String shop);

    @Query("SELECT sum (sba.orderAmountUSD) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.billingDate is not null " +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    Optional<Double> getTotalMonthoverMonthRevenueCountByDateRange(@Param("shop") String shop,
                                                                 @Param("fromDate") ZonedDateTime fromDate,
                                                                 @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count(sba.id) " +
        "FROM SubscriptionBillingAttempt sba " +
        "JOIN SubscriptionContractDetails scd ON sba.contractId = scd.subscriptionContractId " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.FAILURE " +
        "AND sba.attemptTime >=:fromDate " +
        "AND sba.attemptTime <=:toDate ")
    Optional<Long> getTotalFailedPaymentsCount(@Param("shop") String shop,
                                               @Param("fromDate") ZonedDateTime fromDate,
                                               @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT sba " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = :billingAttemptStatus " +
        "AND sba.billingDate >=:zonedDateTime ")
    List<SubscriptionBillingAttempt> findRecentAttemptsWithStatus(@Param("shop") String shop, @Param("billingAttemptStatus") BillingAttemptStatus billingAttemptStatus, @Param("zonedDateTime") ZonedDateTime zonedDateTime);

    @Query(value = "SELECT * " +
        "   FROM subscription_billing_attempt sba " +
        "       JOIN subscription_contract_details scd ON sba.contract_id = scd.subscription_contract_id " +
        "   WHERE sba.contract_id = ?1 and sba.status <> 'QUEUED' " +
        "       ORDER BY sba.id DESC LIMIT 1", nativeQuery = true)
    Optional<SubscriptionBillingAttempt> findLatestBillingAttemptByContractId(@Param("contractId") Long contractId);

    @Modifying
    @Query("delete from SubscriptionBillingAttempt sba where sba.contractId = :contractId")
    void deleteByContractId(@Param("contractId") Long contractId);

    @Query(value = "select distinct contract_id from subscription_billing_attempt where status = 'QUEUED' and billing_date is not null group by contract_id, DATE(billing_date) having count(*) > 1", nativeQuery = true)
    List<Long> findDuplicateBillingAttempts();

    @Modifying
    @Query("delete from SubscriptionBillingAttempt sba where sba.contractId in :contractIds")
    void deleteByContractIdIn(@Param("contractIds") Set<Long> contractIds);

    @Query("select scd from SubscriptionBillingAttempt scd where scd.status = com.et.domain.enumeration.BillingAttemptStatus.PROGRESS and scd.attemptTime is not null and scd.attemptTime < :attemptTime")
    List<SubscriptionBillingAttempt> findBillingAttemptsInProgressState(@Param("attemptTime") ZonedDateTime attemptTime);

    @Query(value = "SELECT Sum(`subscription_billing_attempt`.`order_amount_usd`) AS `sum`\n" +
        "FROM   `subscription_billing_attempt`\n" +
        "WHERE  ( `subscription_billing_attempt`.`shop` = :shop\n" +
        "         AND `subscription_billing_attempt`.`status` = 'SUCCESS'\n" +
        "         AND `subscription_billing_attempt`.`order_amount_usd` is not null\n" +
        "         AND `subscription_billing_attempt`.`attempt_time` >=\n" +
        "                 Str_to_date(Concat(Date_format(Date_add(Now(6),\n" +
        "                                                INTERVAL -1 month),\n" +
        "                                    '%Y-%m')\n" +
        "                             ,\n" +
        "                             '-01'), '%Y-%m-%d')\n" +
        "         AND `subscription_billing_attempt`.`attempt_time` <\n" +
        "             Str_to_date(Concat(\n" +
        "             Date_format(Now(6), '%Y-%m')\n" +
        "                         , '-01'), '%Y-%m-%d') ) ", nativeQuery = true)
    Double findSuccessOrderAmountUSD(@Param("shop") String shop);


    @Query("UPDATE SubscriptionBillingAttempt sba set sba.orderAmountUSD = :orderAmountUSD where sba.graphOrderId = :orderId")
    @Modifying
    int updateUSDOrderAmount(@Param("orderId") String orderId, @Param("orderAmountUSD") Double orderAmountUSD);

    @Query(value = "select sum(sba.orderAmountUSD) from SubscriptionBillingAttempt sba where sba.shop = :shop and upper(sba.status) = 'SUCCESS' and sba.orderAmountUSD is not null " +
        "AND sba.attemptTime >=:fromDate and sba.attemptTime <=:toDate")
    Double getTotalUsedOrderAmountUSDForDateRange(@Param("shop") String shop, @Param("fromDate") ZonedDateTime fromDate, @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (sba.id) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND (sba.status = com.et.domain.enumeration.BillingAttemptStatus.SKIPPED " +
        "OR sba.status = com.et.domain.enumeration.BillingAttemptStatus.SKIPPED_DUNNING_MGMT " +
        "OR sba.status = com.et.domain.enumeration.BillingAttemptStatus.SKIPPED_INVENTORY_MGMT )" +
        "AND sba.billingDate >=:fromDate " +
        "AND sba.billingDate <=:toDate ")
    Optional<Long> getTotalSkippedOrdersByDateRange(@Param("shop") String shop,
                                                    @Param("fromDate") ZonedDateTime fromDate,
                                                    @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (sba.id) " +
        "FROM SubscriptionBillingAttempt sba " +
        "WHERE sba.shop =:shop " +
        "AND sba.status = com.et.domain.enumeration.BillingAttemptStatus.SUCCESS " +
        "AND sba.billingDate >=:fromDate " +
        "AND (:contractId is null OR sba.contractId = :contractId) "
    )
    Optional<Long> countSuccessfulOrdersAfterSpecifiedDate(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("contractId") Long contractId
        );

}
