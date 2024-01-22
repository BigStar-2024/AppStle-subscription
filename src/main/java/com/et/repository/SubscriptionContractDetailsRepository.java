package com.et.repository;

import com.et.domain.SubscriptionContractDetails;
import com.et.service.SubscriptionsTotalByDate;
import com.et.service.dto.*;
import com.et.web.rest.vm.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the SubscriptionContractDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionContractDetailsRepository extends JpaRepository<SubscriptionContractDetails, Long>, JpaSpecificationExecutor<SubscriptionContractDetails> {

    @Query("SELECT SUM (scd.orderAmountUSD) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Double> getTotalSoldAmountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Long> getTotalSoldAmountByDateRangeAVG(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.orderId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Long> getTotalOrderCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT sum (scd.orderAmount) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Double> getTotalOrderAmountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'cancelled'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt < :fromDate " +
        "AND scd.cancelledOn is not null " +
        "AND scd.cancelledOn >=:fromDate " +
        "AND scd.cancelledOn <=:toDate "
    )
    Optional<Long> getTotalCancelledButCreatedBeforeDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT scd " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'cancelled'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt < :fromDate " +
        "AND scd.cancelledOn is not null " +
        "AND scd.cancelledOn >=:fromDate " +
        "AND scd.cancelledOn <=:toDate "
    )
    List<SubscriptionContractDetails> getTotalCancelledSubscriptionsButCreatedBeforeDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT scd " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    List<SubscriptionContractDetails> getNewSubscriptions(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt < :fromDate "
    )
    Optional<Long> getTotalSubscriptionsCreatedBefore(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate);

    @Query("SELECT scd " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt < :fromDate "
    )
    List<SubscriptionContractDetails> getTotalSubscriptionsDataCreatedBefore(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate);

    @Query("SELECT scd " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate")
    List<SubscriptionContractDetails> getTotalSubscriptionsDataCreatedInDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);


    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Long> countActiveSubscription(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.billingPolicyInterval =:billingPolicyInterval " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Long> getTotalSubscriptionByBillingIntervalDays(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate,
        @Param("billingPolicyInterval") String billingPolicyInterval);

    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') AS orderCreatedAt, sum(subscription_contract_details.order_amount_usd) AS sum\n" +
            "FROM subscription_contract_details\n" +
            "WHERE subscription_contract_details.shop = ?1\n" +
            "AND subscription_contract_details.created_at >= ?2\n" +
            "AND subscription_contract_details.created_at <= ?3\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<OrderAmountByDate> orderAmountByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') AS orderCreatedAt, count(subscription_contract_details.id) AS total\n" +
            "FROM subscription_contract_details\n" +
            "WHERE subscription_contract_details.shop = ?1\n" +
            "AND subscription_contract_details.created_at >= ?2\n" +
            "AND subscription_contract_details.created_at <= ?3\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<OrderCountByDate> orderCountByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    @Query("SELECT COUNT (DISTINCT scd.id) from SubscriptionContractDetails scd where scd.shop=:shop")
    Integer findSubscriptionsCount(@Param("shop") String shop);

    @Query(value = "select * from subscription_contract_details where shop = :shop " +
        " AND (:fromCreatedDate is null OR created_at >= :fromCreatedDate) " +
        " AND (:toCreatedDate is null OR created_at <= :toCreatedDate) " +
        " AND (:fromUpdatedDate is null OR updated_at >= :fromUpdatedDate) " +
        " AND (:toUpdatedDate is null OR updated_at <= :toUpdatedDate) " +
        " AND (:fromNextDate is null OR :toNextDate is null OR (next_billing_date >= :fromNextDate AND next_billing_date <= :toNextDate ) ) " +
        " AND (:subscriptionContractId is null OR CAST(subscription_contract_id as CHAR) LIKE LOWER(CONCAT('%',:subscriptionContractId,'%')) ) " +
        " AND (:customerName is null OR LOWER(customer_name) LIKE LOWER(CONCAT('%',:customerName,'%'))  OR LOWER(customer_email) LIKE LOWER(CONCAT('%',:customerName,'%')) ) " +
        " AND (:orderName is null OR LOWER(order_name) LIKE LOWER(CONCAT('%',:orderName,'%')) ) " +
        " AND (:status is null OR LOWER(status) LIKE LOWER(CONCAT('%',:status,'%')) ) " +
        " AND (:billingPolicyInterval is null OR LOWER(delivery_policy_interval) = LOWER(:billingPolicyInterval)) " +
        " AND (:billingPolicyIntervalCount is null OR delivery_policy_interval_count = :billingPolicyIntervalCount) " +
        " AND (:planType is null OR ( :planType = 'prepaid' AND billing_policy_interval_count != delivery_policy_interval_count) OR ( :planType = 'non-prepaid' AND billing_policy_interval_count = delivery_policy_interval_count) ) " +
        " AND (:recordType is null OR ( :recordType = 'imported' AND imported_id is not null) OR ( :recordType = 'nonImported' AND imported_id is null) ) " +
        " AND (:gqlProductId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlProductId) ) " +
        " AND (:gqlVariantId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlVariantId) ) " +
        " AND (:gqlSellingPlanId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlSellingPlanId) ) " +
        " AND (:minOrderAmount is null OR :minOrderAmount <= (select sum(json_detail.quantity*json_detail.current_price) as total_price from " +
        " JSON_TABLE (contract_details_json, '$[*]' COLUMNS ( quantity int PATH '$.quantity', current_price double PATH '$.currentPrice'))json_detail)) " +
        " AND (:maxOrderAmount is null OR :maxOrderAmount >= (select sum(json_detail.quantity*json_detail.current_price) as total_price from " +
        " JSON_TABLE (contract_details_json, '$[*]' COLUMNS ( quantity int PATH '$.quantity', current_price double PATH '$.currentPrice'))json_detail)) ",
        nativeQuery = true
    )
    Page<SubscriptionContractDetails> findByShop(
        Pageable pageable,
        @Param("shop") String shop,
        @Param("fromCreatedDate") ZonedDateTime fromCreatedDate,
        @Param("toCreatedDate") ZonedDateTime toCreatedDate,
        @Param("fromUpdatedDate") ZonedDateTime fromUpdatedDate,
        @Param("toUpdatedDate") ZonedDateTime toUpdatedDate,
        @Param("fromNextDate") ZonedDateTime fromNextDate,
        @Param("toNextDate") ZonedDateTime toNextDate,
        @Param("subscriptionContractId") String subscriptionContractId,
        @Param("customerName") String customerName,
        @Param("orderName") String orderName,
        @Param("status") String status,
        @Param("billingPolicyIntervalCount") Integer billingPolicyIntervalCount,
        @Param("billingPolicyInterval") String billingPolicyInterval,
        @Param("planType") String planType,
        @Param("recordType") String recordType,
        @Param("gqlProductId") String gqlProductId,
        @Param("gqlVariantId") String gqlVariantId,
        @Param("gqlSellingPlanId") String gqlSellingPlanId,
        @Param("minOrderAmount") Double minOrderAmount,
        @Param("maxOrderAmount") Double maxOrderAmount);

    @Query(value = "select * from subscription_contract_details where shop = :shop " +
        " AND (:fromCreatedDate is null OR created_at >= :fromCreatedDate) " +
        " AND (:toCreatedDate is null OR created_at <= :toCreatedDate) " +
        " AND (:fromUpdatedDate is null OR updated_at >= :fromUpdatedDate) " +
        " AND (:toUpdatedDate is null OR updated_at <= :toUpdatedDate) " +
        " AND (:fromNextDate is null OR :toNextDate is null OR (next_billing_date >= :fromNextDate AND next_billing_date <= :toNextDate ) ) " +
        " AND (:subscriptionContractId is null OR CAST(subscription_contract_id as CHAR) LIKE LOWER(CONCAT('%',:subscriptionContractId,'%')) ) " +
        " AND (:customerName is null OR LOWER(customer_name) LIKE LOWER(CONCAT('%',:customerName,'%'))  OR LOWER(customer_email) LIKE LOWER(CONCAT('%',:customerName,'%')) ) " +
        " AND (:orderName is null OR (LOWER(order_name) LIKE LOWER(CONCAT('%',:orderName,'%'))) OR (select count(*) from subscription_billing_attempt sba where sba.shop = :shop AND sba.contract_id = subscription_contract_id AND LOWER(sba.order_name) LIKE LOWER(CONCAT('%',:orderName,'%')) > 0) ) " +
        " AND (:status is null OR LOWER(status) LIKE LOWER(CONCAT('%',:status,'%')) ) " +
        " AND (:billingPolicyInterval is null OR LOWER(delivery_policy_interval) = LOWER(:billingPolicyInterval)) " +
        " AND (:billingPolicyIntervalCount is null OR delivery_policy_interval_count = :billingPolicyIntervalCount) " +
        " AND (:planType is null OR ( :planType = 'prepaid' AND billing_policy_interval_count != delivery_policy_interval_count) OR ( :planType = 'non-prepaid' AND billing_policy_interval_count = delivery_policy_interval_count) ) " +
        " AND (:recordType is null OR ( :recordType = 'imported' AND imported_id is not null) OR ( :recordType = 'nonImported' AND imported_id is null) ) " +
        " AND (:gqlProductId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlProductId) ) " +
        " AND (:gqlVariantId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlVariantId) ) " +
        " AND (COALESCE(:gqlSellingPlanIds) is null  OR 0 < (select count(*) from " +
        " JSON_TABLE (contract_details_json, '$[*]' COLUMNS ( selling_plan_id VARCHAR(255) PATH '$.sellingPlanId'))selling_plans where selling_plans.selling_plan_id in (:gqlSellingPlanIds)) ) " +
        " AND (:minOrderAmount is null OR :minOrderAmount <= (select sum(json_detail.quantity*json_detail.current_price) as total_price from " +
        " JSON_TABLE (contract_details_json, '$[*]' COLUMNS ( quantity int PATH '$.quantity', current_price double PATH '$.currentPrice'))json_detail)) " +
        " AND (:maxOrderAmount is null OR :maxOrderAmount >= (select sum(json_detail.quantity*json_detail.current_price) as total_price from " +
        " JSON_TABLE (contract_details_json, '$[*]' COLUMNS ( quantity int PATH '$.quantity', current_price double PATH '$.currentPrice'))json_detail)) ",
        nativeQuery = true
    )
    Page<SubscriptionContractDetails> findByShop(
        Pageable pageable,
        @Param("shop") String shop,
        @Param("fromCreatedDate") ZonedDateTime fromCreatedDate,
        @Param("toCreatedDate") ZonedDateTime toCreatedDate,
        @Param("fromUpdatedDate") ZonedDateTime fromUpdatedDate,
        @Param("toUpdatedDate") ZonedDateTime toUpdatedDate,
        @Param("fromNextDate") ZonedDateTime fromNextDate,
        @Param("toNextDate") ZonedDateTime toNextDate,
        @Param("subscriptionContractId") String subscriptionContractId,
        @Param("customerName") String customerName,
        @Param("orderName") String orderName,
        @Param("status") String status,
        @Param("billingPolicyIntervalCount") Integer billingPolicyIntervalCount,
        @Param("billingPolicyInterval") String billingPolicyInterval,
        @Param("planType") String planType,
        @Param("recordType") String recordType,
        @Param("gqlProductId") String gqlProductId,
        @Param("gqlVariantId") String gqlVariantId,
        @Param("gqlSellingPlanIds") List<String> gqlSellingPlanIds,
        @Param("minOrderAmount") Double minOrderAmount,
        @Param("maxOrderAmount") Double maxOrderAmount);


    Page<SubscriptionContractDetails> findByShop(Pageable pageable, String shop);

    List<SubscriptionContractDetails> findByShop(String shop);

    List<SubscriptionContractDetails> findByShopAndStatus(String shop, String status);

    List<SubscriptionContractDetails> findByShopAndCustomerEmail(String shop, String customerEmail);

    @Query(value = "select scd.contract_details_json as contractDetailsJson, sba.billing_date as billingDate from subscription.subscription_contract_details scd inner join subscription.subscription_billing_attempt sba\n" +
        "on scd.subscription_contract_id = sba.contract_id where scd.status = 'active' and scd.shop = :shop\n" +
        "AND scd.created_at >=:fromDate \n " +
        "AND scd.created_at <=:toDate \n" +
        "and sba.status = 'QUEUED'", nativeQuery = true)
    List<ProductAnalytics> findActiveQueuedAttempts(@Param("shop") String shop,
                                                    @Param("fromDate") ZonedDateTime fromDate,
                                                    @Param("toDate") ZonedDateTime toDate);

    @Query(value = "select scd.contract_details_json as contractDetailsJson, sba.contract_id as contractID, COUNT(sba.contract_id) as contractBillingAttempt, sba.billing_date as billingDate, sba.attempt_count as billingAttempt from subscription.subscription_contract_details scd inner join subscription.subscription_billing_attempt sba\n" +
        "on scd.subscription_contract_id = sba.contract_id and scd.shop = :shop \n" +
        "AND sba.attempt_time >=:fromDate \n " +
        "AND sba.attempt_time <=:toDate \n" +
        "AND sba.status = 'SUCCESS'  GROUP BY contractID", nativeQuery = true)
    List<ProductRevenue> findSuccessContractAttempts(@Param("shop") String shop,
                                                     @Param("fromDate") ZonedDateTime fromDate,
                                                     @Param("toDate") ZonedDateTime toDate);

    @Query(value = "select scd.contract_details_json as contractDetailsJson, sba.contract_id as contractID, COUNT(sba.contract_id) as contractBillingAttempt, sba.billing_date as billingDate, sba.attempt_count as billingAttempt from subscription.subscription_contract_details scd inner join subscription.subscription_billing_attempt sba\n" +
        "on scd.subscription_contract_id = sba.contract_id and scd.shop = :shop \n" +
        "AND sba.billing_date >=:fromDate \n " +
        "AND sba.billing_date <=:toDate \n" +
        "AND sba.status = 'FAILURE'  GROUP BY contractID", nativeQuery = true)
    List<ProductRevenue> findFailedContractAttempts(@Param("shop") String shop,
                                                     @Param("fromDate") ZonedDateTime fromDate,
                                                     @Param("toDate") ZonedDateTime toDate);

    @Query(value = "select scd.customerId from SubscriptionContractDetails scd where scd.shop = :shop ")
    Set<Long> findByShopGroupByCustomerId(@Param("shop") String shop);

    String commonCustomerGroupByQuery = "\t  FROM SubscriptionContractDetails scd \n" +
        "WHERE scd.shop = :shop \n" +
        "AND  (:name is null OR LOWER(scd.customerName) LIKE LOWER(CONCAT('%',:name,'%')) ) \n" +
        "AND  (:email is null OR LOWER(scd.customerEmail) LIKE LOWER(CONCAT('%',:email,'%')) ) \n" +
        "GROUP BY scd.customerId\n";

    String commonCustomerGroupByAndHavingMoreThanOneActiveSubscriptionQuery = "\t  FROM SubscriptionContractDetails scd \n" +
        "WHERE scd.shop = :shop \n" +
        "AND  (:name is null OR LOWER(scd.customerName) LIKE LOWER(CONCAT('%',:name,'%')) ) \n" +
        "AND  (:email is null OR LOWER(scd.customerEmail) LIKE LOWER(CONCAT('%',:email,'%')) ) \n" +
        "GROUP BY scd.customerId\n" +
        "HAVING SUM(CASE WHEN scd.status = 'active' THEN 1 ELSE 0 END) >1\n";

    @Query(value = "SELECT \n" +
        "\tnew com.et.web.rest.vm.CustomerInfo(\n" +
        "\tscd.customerId as customerId ,\n" +
        "\tscd.customerName AS name,\n" +
        "\tscd.customerEmail AS email,\n" +
        "\tSUM(CASE WHEN scd.status = 'active' THEN 1 ELSE 0 END) AS activeSubscriptions, \n" +
        "\t(\n" +
        "\t\tSELECT MIN(scd1.nextBillingDate)  \n" +
        "\t\tFROM SubscriptionContractDetails scd1\n" +
        "\t\tWHERE scd1.shop = :shop \n" +
        "\t\tAND scd1.customerId = scd.customerId\n" +
        "\t\tAND DATE(scd1.nextBillingDate) >= current_date\n" +
        "\t) AS nextOrderDate,\n" +
        "\tSUM(CASE WHEN scd.status in ('cancelled', 'paused') THEN 1 ELSE 0 END) AS inActiveSubscriptions\n" +
        "\t )  \n" + commonCustomerGroupByQuery,
        countQuery = "SELECT COUNT(scd) " + commonCustomerGroupByQuery)
    Page<CustomerInfo> findByShopGroupByCustomerIdPaginated(Pageable pageable, @Param("shop") String shop, @Param("name") String name, @Param("email") String email);


    @Query(value = "SELECT \n" +
        "\tnew com.et.web.rest.vm.CustomerInfo(\n" +
        "\tscd.customerId as customerId ,\n" +
        "\tscd.customerName AS name,\n" +
        "\tscd.customerEmail AS email,\n" +
        "\tSUM(CASE WHEN scd.status = 'active' THEN 1 ELSE 0 END) AS activeSubscriptions, \n" +
        "\t(\n" +
        "\t\tSELECT MIN(scd1.nextBillingDate)  \n" +
        "\t\tFROM SubscriptionContractDetails scd1\n" +
        "\t\tWHERE scd1.shop = :shop \n" +
        "\t\tAND scd1.customerId = scd.customerId\n" +
        "\t\tAND DATE(scd1.nextBillingDate) >= current_date\n" +
        "\t) AS nextOrderDate,\n" +
        "\tSUM(CASE WHEN scd.status in ('cancelled', 'paused') THEN 1 ELSE 0 END) AS inActiveSubscriptions\n" +
        "\t )  \n" + commonCustomerGroupByAndHavingMoreThanOneActiveSubscriptionQuery,
        countQuery = "SELECT COUNT(scd) " + commonCustomerGroupByAndHavingMoreThanOneActiveSubscriptionQuery)
    Page<CustomerInfo> findByShopGroupByCustomerIdAndHavingMoreThanOneSubscriptionPaginated(Pageable pageable, @Param("shop") String shop, @Param("name") String name, @Param("email") String email);

    List<SubscriptionContractDetails> findByShopAndCustomerIdIn(String shop, Set<Long> customerIds);

    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') AS orderCreatedAt, count(subscription_contract_details.id) AS total\n" +
            "FROM subscription_contract_details\n" +
            "WHERE subscription_contract_details.shop = ?1\n" +
            "AND subscription_contract_details.created_at >= ?2\n" +
            "AND subscription_contract_details.created_at <= ?3\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<SubscriptionsTotalByDate> subscriptionsTotalByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);


    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') AS orderCreatedAt, count(subscription_contract_details.id) AS total\n" +
            "FROM subscription_contract_details\n" +
            "WHERE subscription_contract_details.shop = ?1\n" +
            "AND subscription_contract_details.created_at >= ?2\n" +
            "AND subscription_contract_details.created_at <= ?3\n" +
            "AND subscription_contract_details.status = ?4\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<SubscriptionsTotalByDate> subscriptionsCountByWeekAndStatus(String shop, ZonedDateTime fromDate, ZonedDateTime toDate, String status);


    @Query(
        value = "SELECT str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') AS orderCreatedAt, sum(subscription_contract_details.order_amount) AS total\n" +
            "FROM subscription_contract_details\n" +
            "WHERE subscription_contract_details.shop = ?1\n" +
            "AND subscription_contract_details.created_at >= ?2\n" +
            "AND subscription_contract_details.created_at <= ?3\n" +
            "GROUP BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W')\n" +
            "ORDER BY str_to_date(concat(yearweek(subscription_contract_details.created_at), ' Sunday'), '%X%V %W') ASC",
        nativeQuery = true
    )
    List<SubscriptionsTotalAmountByWeek> subscriptionsTotalAmountByWeek(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    @Transactional
    @Modifying
    @Query("update SubscriptionContractDetails SET status = :status, cancellationFeedback = :cancellationFeedback, cancellationNote = :cancellationNote  where subscriptionContractId = :subscriptionContractId")
    void updateStatus(
        @Param("subscriptionContractId") Long subscriptionContractId,
        @Param("status") String status,
        @Param("cancellationFeedback") String cancellationFeedback,
        @Param("cancellationNote") String cancellationNote);

    Optional<SubscriptionContractDetails> findBySubscriptionContractId(Long subscriptionContractId);

    List<SubscriptionContractDetails> findAllBySubscriptionContractId(Long subscriptionContractId);

    List<SubscriptionContractDetails> findBySubscriptionContractIdIn(Set<Long> subscriptionContractIds);

    List<SubscriptionContractDetails> findByShopAndSubscriptionContractIdIn(String shop, Set<Long> subscriptionContractIds);

    Optional<SubscriptionContractDetails> findByShopAndGraphOrderId(String shop, String graphOrderId);

    List<SubscriptionContractDetails> findByShopAndCustomerId(String shop, long customerId);

    Optional<SubscriptionContractDetails> findByShopAndImportedId(String shop, String importedId);

    List<SubscriptionContractDetails> findByShopAndImportedIdIn(String shop, List<String> importedId);

    @Transactional
    @Modifying
    @Query("update SubscriptionContractDetails SET status = :status where subscriptionContractId = :subscriptionContractId")
    void updateStatus(@Param("subscriptionContractId") Long subscriptionContractId, @Param("status") String status);

    @Query(value = "select * from subscription_contract_details where shop = :shop " +
        " AND (:fromCreatedDate is null OR created_at >= :fromCreatedDate) " +
        " AND (:toCreatedDate is null OR created_at <= :toCreatedDate) " +
        " AND (:fromNextDate is null OR :toNextDate is null OR (next_billing_date >= :fromNextDate AND next_billing_date <= :toNextDate ) ) " +
        " AND (:subscriptionContractId is null OR CAST(subscription_contract_id as CHAR) LIKE LOWER(CONCAT('%',:subscriptionContractId,'%')) ) " +
        " AND (:customerName is null OR LOWER(customer_name) LIKE LOWER(CONCAT('%',:customerName,'%'))  OR LOWER(customer_email) LIKE LOWER(CONCAT('%',:customerName,'%')) ) " +
        " AND (:orderName is null OR LOWER(order_name) LIKE LOWER(CONCAT('%',:orderName,'%')) ) " +
        " AND (:status is null OR LOWER(status) LIKE LOWER(CONCAT('%',:status,'%')) ) " +
        " AND (:billingPolicyInterval is null OR LOWER(delivery_policy_interval) = LOWER(:billingPolicyInterval)) " +
        " AND (:billingPolicyIntervalCount is null OR delivery_policy_interval_count = :billingPolicyIntervalCount) " +
        " AND (:planType is null OR ( :planType = 'prepaid' AND billing_policy_interval_count != delivery_policy_interval_count) OR ( :planType = 'non-prepaid' AND billing_policy_interval_count = delivery_policy_interval_count) ) " +
        " AND (:recordType is null OR ( :recordType = 'imported' AND imported_id is not null) OR ( :recordType = 'nonImported' AND imported_id is null) ) " +
        " AND (:gqlProductId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlProductId) ) " +
        " AND (:gqlVariantId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlVariantId) ) " +
        " AND (:gqlSellingPlanId is null OR JSON_SEARCH(contract_details_json, 'one', :gqlSellingPlanId) ) ",
        nativeQuery = true
    )
    List<SubscriptionContractDetails> findByShop(@Param("shop") String shop,
                                                 @Param("fromCreatedDate") ZonedDateTime fromCreatedDate,
                                                 @Param("toCreatedDate") ZonedDateTime toCreatedDate,
                                                 @Param("fromNextDate") ZonedDateTime fromNextDate,
                                                 @Param("toNextDate") ZonedDateTime toNextDate,
                                                 @Param("subscriptionContractId") String subscriptionContractId,
                                                 @Param("customerName") String customerName,
                                                 @Param("orderName") String orderName,
                                                 @Param("status") String status,
                                                 @Param("billingPolicyIntervalCount") Integer billingPolicyIntervalCount,
                                                 @Param("billingPolicyInterval") String billingPolicyInterval,
                                                 @Param("planType") String planType,
                                                 @Param("recordType") String recordType,
                                                 @Param("gqlProductId") String gqlProductId,
                                                 @Param("gqlVariantId") String gqlVariantId,
                                                 @Param("gqlSellingPlanId") String gqlSellingPlanId);

    @Modifying
    @Query("delete from SubscriptionContractDetails sds where sds.subscriptionContractId = :contractId")
    void deleteBySubscriptionContractId(@Param("contractId") Long contractId);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'paused'" +
        "AND scd.pausedOn is not null " +
        "AND scd.pausedOn >=:fromDate " +
        "AND scd.pausedOn <=:toDate ")
    Optional<Long> getTotalPausedSubscriptionCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.orderId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Long> getTotalMonthoverMonthSubscriptionCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT SUM (scd.orderAmount) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate " +
        "AND scd.createdAt <=:toDate ")
    Optional<Double> getTotalSoldRevenueByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (distinct scd.customerId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate")
    Optional<Long> getTotalCustomerCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (id) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate")
    Optional<Long> getTotalSubscriptionCount(@Param("shop") String shop,
                                             @Param("fromDate") ZonedDateTime fromDate,
                                             @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (id) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate AND scd.status =:status")
    Optional<Long> getTotalSubscriptionCountByStatus(@Param("shop") String shop,
                                                     @Param("fromDate") ZonedDateTime fromDate,
                                                     @Param("toDate") ZonedDateTime toDate,
                                                     @Param("status") String status);

    @Query("SELECT sum(scd.orderAmount) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate")
    Optional<Double> getTotalSubscriptionAmount(@Param("shop") String shop,
                                                @Param("fromDate") ZonedDateTime fromDate,
                                                @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (id) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.activatedOn is not null " +
        "AND scd.activatedOn >=:fromDate AND scd.activatedOn <=:toDate")
    Optional<Long> getTotalActiveSubscriptionCount(@Param("shop") String shop,
                                                   @Param("fromDate") ZonedDateTime fromDate,
                                                   @Param("toDate") ZonedDateTime toDate);

    @Query(value = "SELECT avg(datediff(scd.cancelled_on, scd.created_at)) as avg_days  " +
        "FROM subscription_contract_details scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.created_at is not null " +
        "AND scd.cancelled_on is not null " +
        "AND scd.created_at >=:fromDate " +
        "AND scd.created_at <=:toDate ",
        nativeQuery = true)
    Optional<Double> getAverageSubscriptionValue(@Param("shop") String shop,
                                                 @Param("fromDate") ZonedDateTime fromDate,
                                                 @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (id) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'active'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt >=:fromDate AND scd.createdAt <=:toDate")
    Optional<Long> getTotalNewSubscriptionCount(@Param("shop") String shop,
                                                @Param("fromDate") ZonedDateTime fromDate,
                                                @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.status = 'cancelled'" +
        "AND scd.cancelledOn is not null " +
        "AND scd.cancelledOn >=:fromDate " +
        "AND scd.cancelledOn <=:toDate ")
    Optional<Long> getTotalCanceledSubscriptionCountByDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query(value = "select productId, variantId, max(title) as title, max(variantTitle) as variantTitle,  " +
        "       sum(case when billing_date <= day_after_7_days THEN quantity ELSE 0 END) as deliveryInNext7Days,  " +
        "       sum(case when billing_date <= day_after_30_days THEN quantity ELSE 0 END) as deliveryInNext30Days,  " +
        "       sum(quantity) as deliveryInNext90Days  " +
        "   from (  select sba.billing_date, subscription_product_details.*, DATE_ADD(CURDATE(), INTERVAL 7 DAY) as day_after_7_days,  " +
        "               DATE_ADD(CURDATE(), INTERVAL 30 DAY) as day_after_30_days  " +
        "           from subscription.subscription_billing_attempt sba inner join ( select scd.subscription_contract_id, product_details.*  " +
        "               from subscription.subscription_contract_details scd,  " +
        "                   JSON_TABLE( scd.contract_details_json, '$[*]' COLUMNS (  " +
        "                       id FOR ORDINALITY, productId VARCHAR(300) PATH '$.productId', title VARCHAR(300) PATH '$.title', " +
        "                       variantId VARCHAR(300) PATH '$.variantId', variantTitle VARCHAR(300) PATH '$.variantTitle',  " +
        "                       quantity BIGINT PATH '$.quantity') ) AS product_details  " +
        "           where scd.status = 'active' and scd.shop = :shop ) subscription_product_details  " +
        "   where sba.shop = :shop  " +
        "       and subscription_product_details.subscription_contract_id = sba.contract_id  " +
        "       and sba.billing_date <= DATE_ADD(CURDATE(), INTERVAL 90 DAY) ) a  " +
        "   GROUP by productId, variantId " +
        "   ORDER by deliveryInNext90Days desc ", nativeQuery = true)
    List<ProductDeliveryAnalyticsDTO> findProductDeliveryAnalytics(@Param("shop") String shop);

    List<SubscriptionContractDetails> findByCustomerId(Long customerId);

    @Query(value = "select scd.*\n" +
        "from subscription_contract_details scd\n" +
        "         inner join subscription_billing_attempt sba on scd.subscription_contract_id = sba.contract_id\n" +
        "where sba.id in (\n" +
        "    select min(sba.id)\n" +
        "    from subscription_billing_attempt sba\n" +
        "    where sba.status = 'QUEUED'\n" +
        "      and sba.billing_date is not null\n" +
        "    group by sba.contract_id\n" +
        ")\n" +
        "  and scd.next_billing_date != sba.billing_date\n" +
        "  and scd.status = 'active';", nativeQuery = true)
    List<SubscriptionContractDetails> findNextOrderDateDiscrepancy();

    @Query(value = "select scd.subscription_contract_id\n" +
        "from subscription_contract_details scd\n" +
        "         inner join subscription_billing_attempt sba on scd.subscription_contract_id = sba.contract_id\n" +
        "where sba.id in (\n" +
        "    select min(sba.id)\n" +
        "    from subscription_billing_attempt sba\n" +
        "    where sba.status = 'QUEUED'\n" +
        "      and sba.shop = :shop\n" +
        "      and sba.billing_date is not null\n" +
        "    group by sba.contract_id\n" +
        ")\n" +
        "  and ABS(TIMESTAMPDIFF(MINUTE , sba.billing_date, scd.next_billing_date)) > 1\n" +
        "  and scd.status = 'active';", nativeQuery = true)
    List<SubscriptionContractDetails> findNextOrderDateDiscrepancyForShop(@Param("shop") String shop);


    @Query(value = "select subscription_contract_id, shop, status\n" +
        "from subscription_contract_details\n" +
        "where (status = 'active' or status = 'paused')\n" +
        "  and shop <> 'demo-appstle-subscription.myshopify.com'\n" +
        "  and max_cycles is null and (disable_fix_empty_queue is null or disable_fix_empty_queue = 0) and next_billing_date >= NOW() \n" +
        "  and subscription_contract_id not in (select distinct contract_id from subscription_billing_attempt where subscription_billing_attempt.status = 'QUEUED')\n" +
        "  and shop in (select user_id from social_connection);", nativeQuery = true)
    List<Long> findEmptyQueueIssue();

    @Modifying
    @Query("delete from SubscriptionContractDetails sds where sds.subscriptionContractId in :contractIds")
    void deleteBySubscriptionContractIdIn(@Param("contractIds") Set<Long> contractIds);

    @Query(value = "select scd.* from subscription.subscription_contract_details scd left join subscription.subscription_contract_processing scp on  scd.subscription_contract_id = scp.contract_id where (scd.order_amount is null or scd.order_amount = 0) and (scp.contract_id is null or scp.attempt_count < 3)", nativeQuery = true)
    List<SubscriptionContractDetails> findSubscriptionWithNullOrderAmount();

    @Query(value = "select subscription_contract_id\n" +
        "from subscription_contract_details\n" +
        "where shop = :shop\n" +
        "  and status = 'active'\n" +
        "  and subscription_contract_id\n" +
        "    not in (select distinct contract_id\n" +
        "            from subscription_billing_attempt\n" +
        "            where subscription_billing_attempt.shop = :shop);", nativeQuery = true)
    List<Long> findActiveSubscriptionsWithNoOrderQueue(@Param("shop") String shop);


    @Query(value = "select subscription_contract_id, shop\n" +
        "from subscription_contract_details\n" +
        "where status = 'active'\n" +
        "  and shop in (select user_id from social_connection)\n" +
        "  and shop <> 'demo-appstle-subscription.myshopify.com'\n" +
        "  and shop not like '%test%'\n" +
        "  and max_cycles is null\n" +
        "  and subscription_contract_id\n" +
        "    not in (select distinct contract_id\n" +
        "            from subscription_billing_attempt\n" +
        "            where subscription_billing_attempt.shop = subscription_contract_details.shop);", nativeQuery = true)
    List<SubscriptionWithNoOrderQueue> findActiveSubscriptionWithNoOrderQueue();


    @Transactional
    @Modifying
    @Query("update SubscriptionContractDetails SET customerEmail = :customerEmail, customerName = :customerName,\n" +
        " customerFirstName = :customerFirstName, customerLastName = :customerLastName\n" +
        " where customerId = :customerId")
    void updateCustomerInfo(
        @Param("customerId") Long customerId,
        @Param("customerEmail") String customerEmail,
        @Param("customerName") String customerName,
        @Param("customerFirstName") String customerFirstName,
        @Param("customerLastName") String customerLastName);

    @Transactional
    @Modifying
    @Query("update SubscriptionContractDetails SET customerEmail = :customerEmail, customerName = :customerName,\n" +
        " customerFirstName = :customerFirstName, customerLastName = :customerLastName, phone = :phone\n" +
        " where customerId = :customerId")
    void updateCustomerInfo(
        @Param("customerId") Long customerId,
        @Param("customerEmail") String customerEmail,
        @Param("customerName") String customerName,
        @Param("customerFirstName") String customerFirstName,
        @Param("customerLastName") String customerLastName,
        @Param("phone") String phone);


    @Query(value = "select * from subscription_contract_details where shop = :shop " +
        " AND JSON_SEARCH(contract_details_json, 'one', :gqlProductOrVariantId)",
        nativeQuery = true
    )
    List<SubscriptionContractDetails> findByProductOrVariantId(@Param("shop") String shop, @Param("gqlProductOrVariantId") String gqlProductOrVariantId);

    @Query(value = "select * from subscription_contract_details where shop = :shop " +
        " AND JSON_SEARCH(contract_details_json, 'one', :gqlProductOrVariantId)",
        nativeQuery = true
    )
    Page<SubscriptionContractDetails> findByProductOrVariantId(@Param("shop") String shop, @Param("gqlProductOrVariantId") String gqlProductOrVariantId, Pageable page);

    @Query("SELECT scd.subscriptionContractId " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop and scd.subscriptionContractId is not null")
    List<Long> findSubscriptionContractIdsByShop(@Param("shop") String shop);

    @Query("SELECT scd.subscriptionContractId " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop and scd.subscriptionContractId is not null")
    Page<Long> findSubscriptionContractIdsByShop(Pageable pageable, @Param("shop") String shop);

    @Query(value = "select scd from SubscriptionContractDetails scd where scd.orderAmount is not null and scd.shop = :shop and scd.orderAmountUSD is null ")
    Set<SubscriptionContractDetails> findOrderIdsForShop(@Param("shop") String shop);

    @Transactional
    @Query("UPDATE SubscriptionContractDetails scd set scd.orderAmountUSD = :orderAmountUSD where scd.graphOrderId = :orderId")
    @Modifying
    int updateUSDOrderAmount(@Param("orderId") String orderId, @Param("orderAmountUSD") Double orderAmountUSD);

    @Query("select scd.importedId from SubscriptionContractDetails  scd where scd.shop = :shop group by scd.importedId having count(scd.importedId) > 1")
    Set<String> findSubscriptionsWithSameImportedId(@Param("shop") String shop);

    @Query(value = "select subscription_contract_id from subscription_contract_details where shop = :shop and customer_email in ('drakey416@yahoo.com','wnirote@gmail.com','alannadesavino@gmail.com','aleblanc_98@yahoo.com','kyrajewell103@gmail.com','krystalcabla@yahoo.com','twins09mom@gmail.com','cassandrahelen00@gmail.com','eh888258@gmail.com','noelani317@gmail.com','monicahcooper@gmail.com','jesslieblich@gmail.com','anthonyemery06@gmail.com','ginaleeannebailey@gmail.com','nichole.halloran@gmail.com','misschrissy1985@yahoo.com','kayney03@hotmail.com','mrsmith2288@hotmail.com','mdefossesrn2010@aol.com','nikaltep@gmail.com','wortwick101@yahoo.com','averydtaylor1@comcast.net','gerilynnmetzger@yahoo.com','kateasulli@gmail.com','sarahtrice@gmail.com','kristin.lewis901@gmail.com','hardy.chas@att.net','jenmyers@njmyers.com','cassiemorn@yahoo.com','leannelangston1207@gmail.com','estellagirl83@aol.com','wilcoxson.laura@yahoo.com','bannergrl87@gmail.com','straughapril@gmail.com','shortyld21@yahoo.com','nikkkic811@gmail.com','elizabethreneeflores@gmail.com','talyn.macdonald@gmail.com','carolking1972@msn.com','allirosenichols@gmail.com','ladyec@hotmail.com','judycueva2011@gmail.com','ddhier@gmail.com','jerryfaubion@gmail.com','keldaarlenebrooks@hotmail.com','dnatko828@yahoo.com','chakios@hotmail.com','alysroy@hotmail.com','j.flanders@hotmail.com','kime21@ymail.com','stephanie5772@hotmail.com','erinproffit@yahoo.com','brandee.savell@yahoo.com','julianalupacchino@gmail.com','maibzcamp98@yahoo.com','deanna.spencer26@gmail.com','juneconstantino@hotmail.com','lmullen30188@yahoo.com','amst2u@hotmail.com','rittelma002@gannon.edu','d.fortier728@gmail.com','kpoyer@me.com','ericadtbuchanan@gmail.com','buddykat3@gmail.com','chase.libby@gmail.com','lhsobh@umich.edu','tscboo2@aol.com','tess_rutledge@yahoo.com','karen.kniffin@yahoo.com','maddegibba@gmail.com','twinful@aol.com','melissatgriffin@gmail.com','minayaem@gmail.com','stultzj13@ymail.com','melaniemagallanes@gmail.com','cemanna117@gmail.com','gen.bushie@aol.com','usglass1@aol.com','amandaemmag@hotmail.com','joellelhicks@gmail.com','shawnligatlin@gmail.com','mynafish27@outlook.com','elatrenta11@gmail.com','angela.whitt52@yahoo.com','lojac208@msn.com','karalynne.wilson@gmail.com','daisyduke_03904@yahoo.com','abbieee.078@gmail.com','sheilamelancon@yahoo.com','hannahbldw@gmail.com','kathleenhausman@yahoo.com','jaclynscarinci@gmail.com','trickyrichard44047@yahoo.com','mrsheatherirenetaylor@gmail.com','keheim@yahoo.com','caylasjostrand@yahoo.com','mirandamorgan766@gmail.com','msstrickland@bellsouth.net','heatherbutkow@yahoo.com','zazasgranny.sg@gmail.com','leslie.zevnik@stepsconsulting.org','goldenhairsister22@gmail.com','sbopro123@gmail.com','amymmelch@gmail.com','mmerritt610@gmail.com','orange85@outlook.com','edel2006@msn.com','katy.reif@gmail.com','nataline8@mac.com','feenem01@gettysburg.edu','christy.1312.cw@gmail.com','caitlin@ibuckeyes.com','djexpnjv8@yahoo.com','frenchkath3@gmail.com','sydneye30@gmail.com','bethannv@gmail.com','bunie68315@yahoo.com','yajairaroman13@yahoo.com','elisa.c.morales@gmail.com','brittanyknowles93@yahoo.com','essalisbury@frontier.com','belcmari3@gmail.com','stacyjsimon17@yahoo.com','adddawn@gmail.com','ginnydruart@gmail.com','beccytriolett@gmail.com','brandimcelhaney@verizon.net','dianenorris988@yahoo.com','ksdelost@gmail.com','ldenja@gmail.com','johnsonfamily56175@gmail.com','lacygreco3@yahoo.com','apfrank@optonline.net','hecox.katie@yahoo.com','kimhof4@gmail.com','habdc8@yahoo.com','abolen30@gmail.com','j_holt@comcast.net','info@davinaandthevagabonds.com','debbowman@me.com','ekaufman@westneph.com','vmfcurran@gmail.com','hjkimble@gmail.com','slharper77@yahoo.com','aravegang@aol.com','leighburns@mindspring.com','jbkoop@gmail.com','joycruik@aol.com','chelsing1@yahoo.com','svanwinter@gmail.com','npfohl@hotmail.com','clavin3@gmail.com','angiemaefuller2020@gmail.com','lorna13m@gmail.com','las21980@aol.com','pgames19@gmail.com','charlotte.yarbrough@childrens.harvard.edu','s_kreie@yahoo.com','jaredtollen@yahoo.com','momo.meehan@gmail.com','jessie78@comcast.net','easmart@ymail.com','dlpixley@roadrunner.com','arnoldlori22@outlook.com','noel.lasalle@yahoo.com','joanofsnark712@outlook.com','allieandressmua@gmail.com','starb216@gmail.com','sarahhopper@familyresourcesrc.org','hammillalicia@yahoo.com','carew04@gmail.com','sarahlsmo@icloud.com','courtney@naturalskincare.com','jesteurer@gmail.com','hildaybad@gmail.com','mariannedoe@yahoo.com','akaslinger43@yahoo.com','xaligillisx@gmail.com','sjones0105@hotmail.com','lindsmaurer@gmail.com','carlystaples@icloud.com','lexijona@gmail.com','swansonmo@yahoo.com','jilltatehiggins@gmail.com','terriw78@gmail.com','bowdenaudrey5@gmail.com','paw121212@outlook.com','mekaley@yahoo.com','amy.cloud81@yahoo.com','combs1992@gmail.com','hiersteresa@gmail.com','oinesskara@gmail.com','vlb7662@gmail.com','greinergang@yahoo.com','lbenney83@gmail.com','lessly.lugo@yahoo.com','lindabernabeu@gmail.com','daveshearn@ymail.com','bfulcher92@gmail.com','chrisatl70@gmail.com','tlynn1977@aol.com','ama2814@gmail.com','stephmorrissey1@gmail.com','chelseaowens89@yahoo.com','lisadegroffjams@gmail.com','ayoung461@voicecharterschool.org','marytheresa007@aol.com','lauren.blackmore@gmail.com','scleland1269@yahoo.com','colleen.celiberti@gmail.com','buffalodancer@sbcglobal.net','jlw8128@yahoo.com','ashtonfadel@gmail.com','ginachesne76@gmail.com','smhart242@hotmail.com','anoel.hicks@gmail.com','judyfamily5@aol.com','jamiejensen7790@gmail.com','khoffman14@icloud.com','jenrobbins@hotmail.com','rachelpell13@gmail.com','baan.alsinawi@verizon.net','autumnly28@gmail.com','sonjadelgado2020@gmail.com','rgonzale65@gmail.com','dawnmb83@gmail.com','silk@durrett.net','chill1@sienaheights.edu','jessholloway@gmail.com','annamejza@gmail.com','nicolentc248@hotmail.com','cristina51@hotmail.com','jendiehl7@gmail.com','deklan1008@gmail.com','arisolo2763@gmail.com','lindsaykay2242@yahoo.com','katiefaye1005@gmail.com','jeff.stokes65@gmail.com','aiko.mclaughlin@yahoo.com','janenicolay61@gmail.com','lucretiaziobro@yahoo.com','nicoletbrennan@gmail.com','lesliefetzer1@gmail.com','tamireynolds785@gmail.com','kaytie.carter@gmail.com','craebryant@gmail.com','amanda.nicodemus@gmail.com','jsholland01@gmail.com','jessicagoodwin847@gmail.com','sarah03221983@hotmail.com','emily.paige.todd@hotmail.com','emily@coremaryland.com','amlesniakbetley@gmail.com','tvmeeburke@aol.com','rogersmd669@yahoo.com','taylorl12344@aol.com','rairaimonkey27@gmail.com','mmrn112000@gmail.com','tiffanyjordan86@aol.com','walsdorf.family@yahoo.com','raoravec@yahoo.com','davealloway2001@gmail.com','eklepar@gmail.com','haustin98@yahoo.com','stacyarms67@gmail.com','lilgmg84@gmail.com','pampamela@aol.com','cupcake647192@gmail.com','jerhodes@bellsouth.net','kaylaves@icloud.com','jkanaventi@gmail.com','madtatter@reagan.com','misfeldt65@att.net','amanda.golz@sgws.com','flclifford@gmail.com','hopelrtc@gmail.com','ladypolcat@yahoo.com','salewis1968@gmail.com','tuyyo_anna@yahoo.com','m.kaplan@roadrunner.com','aubriegarza@icloud.com','ikustovskaya@aol.com','plmbery9@aol.com','crews002@gmail.com','molly.tobin@temple.edu','sean_wallace@yahoo.com','alyssa@schiff.ws','els0712@gmail.com','funkydo4u@gmail.com','erikahernandez@mac.com','eric.dahmer@me.com','mike.stark13@gmail.com','abigailknoble@gmail.com','dtshreve@yahoo.com','jlivengo@gmail.com','ashleylafrance72@gmail.com','hurstge@jacksonprep.net','kenzia.wilson@gmail.com','ekedrowski@gmail.com','alaneconn@hotmail.com','hockeyfightsrule@aol.com','lkbenz300@aol.com','nancegold@gmail.com','racheljenniferturnbull@hotmail.com','hnlazo@comcast.net','russop@umich.edu','p.k.kelleher@comcast.net','jgadzinski87@gmail.com','guzibean@yahoo.com','eoga2012@gmail.com','rincon2804@gmail.com','rnrwhite92@hotmail.com','tcandsons@comcast.net','hoodie.salinas@icloud.com','maryleawagner@gmail.com','kandb2012@gmail.com','asfaulkner1@yahoo.com','tleanmohouse@aol.com','maddmomm@att.net','nmastin47@gmail.com','brookebeckwith12@gmail.com','kjameson81@gmail.com','izzy.stoltz12@gmail.com','samisharon77@gmail.com','wwengren@aol.com','cfieldsend7143@gmail.com','morganlaucius@yahoo.com','katiealeblanc@yahoo.com','mandzak5@gmail.com','lexibakes10@yahoo.com','kandayce.koehler@outlook.com','jessgladstone@hotmail.com','christine.rinker@gmail.com','js.rosenberg@comcast.net','christicummings@hotmail.com','deagoldsmith@gmail.com','superflysarah@comcast.net','victoria.j.bollinger@gmail.com','cupcake5681@yahoo.com','acosta0797@gmail.com','dianeritativ@roadrunner.com','samleecro@gmail.com','aadamski369@icloud.com','raidensmommy92@gmail.com','reenadhanda1@gmail.com','jackiechristo04@gmail.com','ehmsusan@gmail.com','ian@corefivedesign.com','mbychok@aol.com','aandk@lowcountry.com','kginther17@gmaul.com','samantharoloff@gmail.com','kuhnny1274@gmail.com','kfortner912@gmail.com','jessidills@gmail.com','poluch19@gmail.com','lrozzi74@gmail.com','brittany.mathias22@gmail.com','mereburrow@yahoo.com','naz420@hotmail.com','bostonm1529@gmail.com','aliciastrahm2768@gmail.com','chewbstrelle7@gmail.com','cquock@gmail.com','sarahbeisner0@gmail.com','anfidum@gmail.com','kearosherick@gmail.com','kelly.racicot@outlook.com','kandcsmith@gmail.com','lraposo@tapmedicine.com','kberkovitch@rgp.com','cmcgill101302@gmail.com','kaselfamily1@verizon.net','jessicajackson@cox.net','carly.chester@g.austincc.edu','ajballain@gmail.com','madshot789@gmail.com','riddlecindy2@gmail.com','blu3eyedbabie@yahoo.com','alayxalinn@gmail.com','madisonnbrickman@gmail.com','lj091981@yahoo.com','computerdownstairs@yahoo.com','birchbox@fastimpex.com','ashtentdickey@gmail.com','cgilani2018@gmail.com','christan.siracusa@gmail.com','auntiejojo@cableone.net','debi_cook77@yahoo.com','kikuboyance@cox.net','berger.katelyn@gmail.com','njp1983@hotmail.com','jliefschultz@gmail.com','rdkirby86@gmail.com','lexiginfl@icloud.com','daniellevaleriano@hotmail.com','faithq@hotmail.com','haileypic@icloud.com','amber13rutledge@hotmail.com','jwhite01@comcast.net','kandcry@aol.com','sburton0610@gmail.com','smzoukis@gmail.com','brittanystephenson@ccs.k12.nc.us','stephaniesullivan1114@yahoo.com','victoriakarabut@gmail.com','camagee26@gmail.com','belsmom09@gmail.com','cookiejar701@yahoo.com','sljohnson1963@aol.com','sgotch@rmhccni.org','kopry42@gmail.com','ekirsch1995@gmail.com','akeith1313@gmail.com','iluvgoats81@gmail.com','katelynnmcalister@yahoo.com','ashleighksutton@gmail.com','jacqueline_breen@outlook.com','dipietrobucks@gmail.com','denagravestock@comcast.net','zeb72@hotmail.com','licia.boothe@verizon.net','olson0154@msn.com','abtrd66@gmail.com','dezertroze@cox.net','robin.brown1@frontier.com','karodukla@gmail.com','erin8432@gmail.com','domi_ricana@yahoo.com','amiller1191@icloud.com','ratkins354@gmail.com','charliemwhitaker@gmail.com','kim@soonercalligraphy.com','doglover11735@yahoo.com','looking.forward@live.com','turtlecc2526@aol.com','lyndseyj2000@yahoo.com','alicleaver@gmail.com','lwrobey@yahoo.com','mindee_lou@hotmail.com','katie_harper99@aol.com','olivia.bryan1243@gmail.com','hendersonba3@gmail.com','peace4l@hotmail.com','almahon1.louisiana@yahoo.com','lisamerwin33@gmail.com','teecna2002@yahoo.com','tannaburnett@live.com','hairbymacaela@yahoo.com','nekanearrietaresnick@gmail.com','janieb2202@yahoo.com','cathyforrest@gmail.com','carnevaleski@gmail.com','t12207@aol.com','jennyrosa718@yahoo.com','gina.pepe@deltaapparel.com','ashleena@rocketmail.com','lancamrn@gmail.com','rhonda.pinkney@yahoo.com','pantezano@gmail.com','kerikat4@gmail.com','chellelou1979@gmail.com','cjpr1977@gmail.com','dasa.etheridge@efsww.com','mspartyfan@hotmail.com','mrossidesign@icloud.com','9thirtyone@gmail.com','maggie.sweeney@comcast.net','vaxgal@yahoo.com','andyvonrohr@yahoo.com','cindy_alewine@yahoo.com','cripegail@yahoo.com','rbnewman226@yahoo.com','ntwoods@hotmail.com','loughranhayden@gmail.com','norahangel@icloud.com','leannasilva422@gmail.com','franquidejesus@gmail.com','mannheim11763@yahoo.com','arfy80@yahoo.com','laforge1978@icloud.com','aebf1986@gmail.com','suetruhngriffin@gmail.com','blaborde0725@hotmail.com','michelle00.walker@gmail.com','ginadellacqua@gmail.com','kerri.lafever@t-mobile.com','    daniellenbenjamin@yahoo.com','madisonroberts96@gmail.com','ktisis1981@gmail.com','junie_monster@yahoo.com','jenna010@gmail.com','epanella333@gmail.com','vanceazi@cox.net','ashes026@msn.com','windfiredva@rocketmail.com','harriscarney@outlook.com','cindybwillard@gmail.com','sljmyers880@gmail.com','elenaleon19@yahoo.com','mlogan41901@gmail.com','donna.sanford@gmail.com','pdglynn68@gmail.com','angcldrn@gmail.com','hgreco814@gmail.com','prettygirl37421@gmail.com','mistycorbin@gmail.com','mroney8@comcast.net','pattieboop@gmail.com','ks.fransaise@yahoo.com','upnorthfloors@gmail.com','genevahunt@icloud.com','mckee1294@verizon.net','vczavataro@gmail.com','mfiggins927@gmail.com','sadaed.williams@gmail.com','benkyker31@gmail.com','hefrajones@gmail.com','helendever@yahoo.com','arzolan@gmail.com','jennifer.c.orellana89@gmail.com','kim081890@gmail.com','nnyce@dentalassociates.us','oxloozerxo@yahoo.com','chlybo35@gmail.com','kgilbertson35@gmail.com','savannah.hansford@gmail.com','avajon2@gmail.com','howeusmc05@yahoo.com','poddubnyak_yuliya_ap2004@yahoo.com','deb.newman57@gmail.com','teresadevane@hotmail.com','tlireland2@gmail.com','labdelnour@verizon.net','skf1277@yahoo.com','andrewdquirk@lewisu.edu','kcdonnan@gmail.com','ashmeiners13@gmail.com','deniseashton4@gmail.com','candice_gray@hotmail.com','debisweetland@gmail.com','leaberumen@sbcglobal.net','taylarfaye@yahoo.com','elliecreighton@gmail.com','mljones75@hotmail.com','emilypatton071792@outlook.com','sassyashu@yahoo.com','karinagriffith@hotmail.com','ashleypersino@gmail.com','bfeldhousen@yahoo.com','mark.frates@gmail.com','hbkreisher@aol.com','tracielynnmoore@gmail.com','kklohse@gmail.com','harmonyagapetus@yahoo.com','mennis8572@gmail.com','kimberlynorris7@gmail.com','dpuccetti@charter.net','alexadavid@mymail.mines.edu','jshilkrot@gmail.com','eatlikhani@utexas.edu','ashchrisman87@gmail.com','cheryl.omarley@gmail.com','mbmalli77@yahoo.com','ariel_wallin@hotmail.com','ashleighalbers@gmail.com','lettybaskin@gmail.com','crandall@randallvethospital.com','kristinagrossman92@gmail.com','amandalorianne88@gmail.com','carollane1988@gmail.com','terrelltynefield@yahoo.com','naturespocket2@gmail.com','trfboston@gmail.com','jennkenyon30@gmail.com','bethaney.surles@gmail.com','mitty78@yahoo.com','amangiulli@gmail.com','taylorarroyo7@gmail.com','kayleigh.marsh@yahoo.com','tfr1959@aol.com','ckchap1@yahoo.com','jennol1@hotmail.com','sylasteeter01@gmail.com','stealyr22@yahoo.com','danabrady423@gmail.com','kdirst@d70schools.org','kobrien466@gmail.com','anunez0008@gmail.com','juliethurston@hotmail.com','jillian.fonseca@gmail.com','gjconcepcion@gmail.com','lizzyb58@aol.com','larakin39@gmail.com','aidan.linden@gmail.com','ndady515@gmail.com','kristenbaker115@gmail.com','thivkandy@aol.com','stebeltk@yahoo.com','brianaholder10@yahoo.com','lcnihill@gmail.com','stateopalm@aol.com','vfrueda@gmail.com','kathy.redwine@gmail.com','acanderson89@gmail.com','bozenahubble@yahoo.com','jonna@fantas-eyes.com','    field1@socket.net','meininger330@gmail.com','hope.woodworth@gmail.com','bexlu77@yahoo.com','ssmith73102@yahoo.com','joeymdennis99@gmail.com','lindseybushong@gmail.com','mooseduck@gmail.com','ehhall1229@yahoo.com','sandybubnowski@gmail.com','brittanyburton5183@gmail.com','deborahvillarruel28@gmail.com','bryank55@roadrunner.com','miralee492@gmail.com','spizzzak@gmail.com','alimezger@yahoo.com','megankeconomides@gmail.com','laurajohnson.ga@gmail.com','qtranportation@yahoo.com','lucky13jazzy@yahoo.com','lindaestrada011@gmail.com','jennahazlewood@yahoo.com','sjhartman89@gmail.com','camstarrhull@gmail.com','ryneec@hotmail.com','moroflo73@hotmail.com','mcdonelcg@zoomtown.com','alineloudie@gmail.com','maryjohn1204@gmail.com','bbalfrey@gmail.com','s.a.brewer89@gmail.com','yairapartyup.03@gmail.com','mariemilne26@gmail.com','tessi@lamarelectric.coop','cholmes2515@gmail.com','allitalics@yahoo.com','rachel.white85@gmail.com','remyburris@gmail.com','needforspeedrecovery@gmail.com','ps6jackiematthews@yahoo.com','courtney.beckenholdt@yahoo.com','jpaster123@verizon.net','desiree.mooney@thechristhospital.com','amy.76j@gmail.com','rachelmstamps@gmail.com','abigail.martinez215@yahoo.com','ashley.magnant@outlook.com','kbakersbaker@aol.com','mengl67561@aol.com','drobinson75093@gmail.com','ruggereamy@gmail.com','sandy@ssrb.us','williams_samantha51@yahoo.com','lib_71@yahoo.com','mtmancini@outlook.com','jackigacki020@gmail.com','mschuler@farmersagent.com','kdavid512@gmail.com','jandavidson119@gmail.com','msgshar@gmail.com','ch64879@gmail.com','anyans2@gmail.com','emma.coppes@gmail.com','patte256@gmail.com','nicfie6@yahoo.com','emkd12000@yahoo.com','margarita.christine@gmail.com','susan.callaghan1@gmail.com','chelsc46@yahoo.com','artistmichellenicole@gmail.com','big-burb@sbcglobal.net','realtreble@comcast.net','michelle_cieslak@yahoo.com','sanjiviiyer10@gmail.com','mamieraines@yahoo.com','eyeball04@aol.com','chthomas1010@gmail.com','katielynnmcneill94@gmail.com','rildachick@msn.com','wendybuter22@gmail.com','jacquelyngarland@gmail.com','lbsyrek@netzero.net','flynnheather84@yahoo.com','hjwest97@gmail.com','cubbie2007@gmail.com','ali.k356@outlook.com','mpf2021@gmail.com','tonicilli@yahoo.com','priscilla8806@gmail.com','adepedro@villanova.edu','nriedel.geo@gmail.com','indpsych@yahoo.com','ekushdesigns@optonline.net','juliesowers@gmail.com','scstanhope@hotmail.com','adiaz51390@gmail.com','mcilrathleah8@gmail.com','tabormiller@aol.com','hattonfamily7@gmail.com','stefan@theholyblack.com','nursedebb@gmail.com','laurenpula@gmail.com','swartzdeborah@yahoo.com','tylrsmom2004@gmail.com','wandabukett@yahoo.com','lilysmommy0623@gmail.com','eastellen@aol.com','cakelly13104@gmail.com','tracie.caudill03@gmail.com','lindseycicogni@gmail.com','kaisamikaleproductions@gmail.com','betsears@att.net','cutie0057@hotmail.com','gina.ohlman@rymondjames.com','monkeylover.breeze@gmail.com','tati0214@gmail.com','kaylilowery@gmail.com','moonmomma14@gmail.com','margaretb_19@yahoo.com','sssurroundings@aol.com','cynthiaalancaster@gmail.com','margobjorkman@yahoo.com','melindaquinn85@gmail.com','sunshinydaniels@hotmail.com','tarapelczar@gmail.com','cuttybrick86@gmail.com','alice_mcmillan@yahoo.com','marylineberry3@gmail.com','kels2111@aim.com','danajoypeacock@yahoo.com','megan.steck@gmail.com','mstricia28@yahoo.com','harterkay@aol.com','daniellemleto@gmail.com','smsnich@gmail.com','lisadieter@hotmail.com','katelynjane93@gmail.com','jrand73@optonline.net','halleg98@gmail.com','nateh898@gmail.com','linafeez@gmail.com','brian@paine-associates.com','    sampleslyndsey@gmail.com','bntaylor1998@yahoo.com','lily.rogers@gmail.com','timncarm@comcast.net','betsy.elges@gmail.com','jhawkemiller@sbcglobal.net','dbconner42206@hotmail.com','caseydearing@gmail.com','leah_joseph2001@yahoo.com','djones1@wkhs.com','hszalda@hotmail.com','amespy.espy@gmail.com','keim1962@gmail.com','aislingdeirdre.murphy@gmail.com','islandtimes86@gmail.com','jessicamckenzie2090@gmail.com','itzalives@gmail.com','mady.sheets1@gmail.com','stephanieday26@yahoo.com','claygirl001@yahoo.com','brendastein3@aol.com','m.l.cherie@gmail.com','englerac@yahoo.com','mimi.quatrini@gmail.com','purplegang911@yahoo.com','stormyskies89@gmail.com','nbugger03@gmail.com','amandahgreenwood@gmail.com','denisemariechevalier@hotmail.com','dsingerbuerger@gmail.com','marycallahan2011@gmail.com','cassroloff@yahoo.com','khatkar_r@hotmail.com','bryanbrown740@gmail.com','klreed76@yahoo.com','tessyjh@hotmail.com','boricua23949@gmail.com','alworthi9459@gmail.com','kcolemancarolina@gmaim.com','br257403@gmail.com','kerryewang@gmail.com','maryellen.miles@gmail.com','jenndemke@icloud.com','maracrossett@gmail.com','shanmelvin@gmail.com','karie.bedwell@hotmail.com','stepatwork@yahoo.com','lshaw21@verizon.net','cjbeall@ucdavis.edu','tmmmhome@yahoo.com','lenmen200@gmail.com','erinmaguire.em@gmail.com','grant_5528@hotmail.com','kg0872@icloud.com','gedes3@yahoi.com','daiglekb@gmail.com','lisa.houk83@yahoo.com','gbnykole9802@yahoo.com','chloe.gaillot@gmail.com','nzamet@att.net','cbick1224@aol.com','karatherese@gmail.com','trishdaniel60@yahoo.com','janicehogan59@gmail.com','bloomy100@gmail.com','laura.gee@yahoo.com','vondrak4702@gmail.com','theroman816@gmail.com','ncmomommy@yahoo.com','jackielp002@gmail.com','tessadonn@gmail.com','pearlinescott0419@gmail.com','lauradrake2010@hotmail.com','gibsmi@yahoo.com','eks2131@columbia.edu','gkenyon106@icloud.com','sarahejagoda@gmail.com','itzel1610@hotmail.com','irenekmontano@gmail.com','bulmanjody@gmail.com','katie5579@gmail.com','catherinebroxon@me.com','garland0802@gmail.com','jocelyncallot@yahoo.com','smyrriah@yahoo.com','gilleyj274@gmail.com','allicoleman@gmail.com','fgarcia@op97.org','ciaobella9172@yahoo.com','karolinenelson517@gmail.com','cyndimcmaster@yahoo.com','aoreilly26@gmail.com','ms.love11@yahoo.com','darden.do@icloud.com','loulou1001@icloud.com','ashleyyoung2188@gmail.com','johannaksnyder@gmail.com','tiffy1026@gmail.com','caldeslisa@gmail.com','marcialsculinary@gmail.com','kristin.l.bryan@gmail.com','sjciraci@gmail.com','djackley37@gmail.com','gmellisor@yahoo.com','baileyedeburn@gmail.com','christinaveselik@hotmail.com','elizabethriegert@gmail.com','edgcon@aol.com','dinaduck04@gmail.com','myshepherd@me.com','marnie.kazarian1@gmail.com','susanparisi@hotmail.com','ishchedrov@gmail.com','srmoore818@gmail.com','lizcap10@yahoo.com','mlangdon04@yahoo.com','cherylcmc1@frontier.com','rocky.holt@hotmail.com','jesseat22@gmail.com','aubvolssx@aol.com','julcacurak@gmail.com','kmatch06@hotmail.com','tracyrph@cs.com','shelbyisert@gmail.com','logankatie27@gmail.com','megsmariga@gmail.com','greg.neuberger7@gmail.com','jesigrisham@gmail.com','rosiedschatkowski@gmail.com','kirstensteves@gmail.com','kmtaff91@gmail.com','andreakozak@comcast.net','melanie0880@gmail.com','tlwalker503@gmail.com','tclark26@gmail.com','reese.shannon@yahoo.com','m.king7594@gmail.com','eeaton07@gmail.com','kmcdaniels15@gmail.com','elyselambing@gmail.com','apollopepper1997@gmail.com','rhschulz@bellsouth.net','dulce_griego@hotmail.com','cassidyrosas333@gmail.com','muilenburg_215@yahoo.com','eapepler64@yahoo.com','beccawatson823@gmail.com','nisper101@aol.com','ctighe@minlib.net','mandirsacks@gmail.com','cmm2365@gmail.com','kelleewoodburn@yahoo.com','griemanb@gmail.com','dmdancy@hotmail.com','niki.burger@verizon.net','erinmichelle64@yahoo.com','ashley.mooney7@gmail.com','shawna08st@gmail.com','amberdsny1124@gmail.com','stephanieparker@comcast.net','sonya.hairlinx@bellsouth.net','michelle.farrell27@yahoo.com','srkeith14@gmail.com','frateralexandria@gmail.com','mmohr@hcps.us','ashley.d.richards123@gmail.com','ls2cats@gmail.com','mizcrystel@hotmail.com','e.a.wright03@gmail.com','rosswschofield@gmail.com','babyjaiden21104@aol.com','graybillca@yahoo.com','seriousmarine@aol.com','megan.lysek@gmail.com','sherzig@rocketmail.com','tahneerayehanberg@gmail.com','anaalves509@gmail.com','cat0487@hotmail.com','jdickerson1031@gmail.com','cclarevoyant@gmail.com','lonnamae.ladke@gmail.com','jullieray@gmail.com','ahuvalieder@gmail.com','aah617@hotmail.com','kari_944@yahoo.com','lela.marie.27@hotmail.com','cyd.grady@gmail.com','nursey312@gmail.com','sbonifer@live.com','jordanleahhopkins@yahoo.com','rasmith727@gmail.com','merymoon@aol.com','kimcastroowens@gmail.com','ilovemygirls2820@gmail.com','kellyowens2871@hotmail.com','laurenhcuddy@gmail.com','hturk8658@yahoo.com','mjweaver2@aol.com','clements99@gmail.com','kristin.tait@yahoo.com','colleen.omalley@comcast.net','kimberlyaveryn@hotmail.com','jgcladams2011@gmail.com','melnzoe@yahoo.com','kell750@gmail.com','jess.willingham434@gmail.com','scispi7@gmail.com','nickyann720@hotmail.com','kvbassett@gmail.com','kcollins2585@yahoo.com','foxboyzmom@yahoo.com','pgoos16@gmail.com','velozrachel1117@gmail.com','chelsrobin89@gmail.com','snowco17@gmail.com','careydougan@gmail.com','plant.hannah@yahoo.com','kab0424@yahoo.com','angeeaa@gmail.com','loridotson1982@yahoo.com','jdgoedhart@gmail.com','jangeles402@gmail.com','paul_r_berryman@hotmail.com','snowy412@gmail.com','nicolebgamache@yahoo.com','tdolan7@hotmail.com','kaylor555@yahoo.com','mcolbert1130@gmail.com','cbfallon@hotmail.com','sarahmwilderman@gmail.com','sdymon1@hotmail.com','davidheath8@gmail.com','tacomagirl01@yahoo.com','amber.murfield@gmail.com','murphymary@gmail.com','kelseyspencer@gmail.com','nclfox5@gmail.com','jhstover@yahoo.com','ndrew3@gmail.com','alaratx@yahoo.com','bubbs788@gmail.com','jennifereskey@yahoo.com','karimoeller.5@gmail.com','lyndsey@lmkinc.org','dyalknowlesginny@gmail.com','rahima@gwu.edu','qulirod@gmail.com','cullen.38@buckeyemail.osu.edu','mthunt00@gmail.com','kathrynsearcy@gmail.com','remoketb66@gmail.com','karajeanne13@gmail.com','lpenatzer1@aol.com','jrjames22@yahoo.com','kay_hddrs@hotmail.com','mrsmdjr18@yahoo.com','amysdowntown1@icloud.com','mmdelugas@gmail.com','rrwiedower@yahoo.com','pateppoe18@gmail.com','rebekah.l.wolfe@gmail.com','amcowley93@gmail.com','feebeelyn@gmail.com','sarah.bates82@yahoo.com','grnninkd@gmail.com','loriadams80@gmail.com','tretcheski@gmail.com','vback1016@yahoo.com','swebster@lhs.org','shaelee.thomas@gmail.com','melmoore623@gmail.com','ashepherd1245@outlook.com','mommax378@gmail.com','terriquintero45@gmail.com','kcrollin@yahoo.com','renee122595@gmail.com','mklangdoc@gmail.com','steph_contact@yahoo.com','laneshakearney@outlook.com','courtney_carlson@me.com','cjones31425@gmail.com','miav230@aol.com','jgaskell86@gmail.com','ktwanninger@gmail.com','tbentley958@gmail.com','abira.knight@gmail.com','kelsieeowen@gmail.com','mgip1945@gmail.com','kacimovic@gmail.com','cackybastian@gmail.com','chelseyricedavis@gmail.com','ashlanhendricks@gmail.com','jojake5@aol.com','nmconner1990@gmail.com','jsbrendle87@gmail.com','candaceunique@gmail.com','buffy@yaffe.com','jsmytka@gmail.com','janicelg@outlook.com','soundra35@gmail.com','krissy.rn36@gmail.com','seanparr@comcast.net','cmarienyc@yahoo.com','lmw120189@gmail.com','rosedennycarroll55@hotmail.com','anniyahsnel@gmail.com','mmercer1975@outlook.com','hollyanthony@comcast.net','loe.trash@gmail.com','cathy.doula@gmail.com','storemanager4life@yahoo.com','ginstew@gmail.com','sudan58.j@gmail.com','mamafreud@gmail.com','giuliana.pirozzi22@gmail.com','t.perez1403@gmail.com','stormyeyb82675@yahoo.com','helayne.kushner@gmail.com','pammyga.pd@gmail.com','sugey45@hotmail.com','keegan.etheredge@gmail.com','dgiddings5354@gmail.com','akatdesign@gmail.com','hlhammitt@gmail.com','zosgirl17@aol.com','lalk_79@hotmail.com','jenmoscola@yahoo.com','sasssie52@aol.com','jlynnetravis@yahoo.com','shanelle.lcmb@gmail.com','kyliejanewatts@yahoo.com','cherylgrocock@hotmail.com','graciegraygirl@gmail.com','codynicolewhite@gmail.com','erynd@optimum.net','jrichmond32@hotmsil.com','bekahgwallace@gmail.com','jah396@miami.edu','margaretylee@yahoo.com','nrec@comcast.net','starsnmoon61@yahoo.com','handjcatherineh@gmail.com','anelken23@yahoo.com','aawhitetx@live.com','heathermshoop@yahoo.com','carriem906@gmail.com','esosa32@yahoo.com','karin_mullen@yahoo.com','jenniferphipps@hotmail.com','liliyashafieva8@gmail.com','nmleswari@gmail.com','zoe.boysen@live.com','jamywag@yahoo.com','orlenamorris@gmail.com','rquinta3@yahoo.com','masharon.greer@gmail.com','carriecar18@gmail.com','smithdw58@yahoo.net','tnstrib@cox.net','moneys5@verizon.net','brookeleneewickham98@gmail.com','kmshields@gmail.com','lisaday6@aol.com','queensley022@gmail.com','kevinhealy1@netzero.com','dnnamares@aol.com','amowrns@frontiernet.net','loranwhiting@gmail.com','jetspence63@gmail.com','brandymichellew2009@gmail.com','frankie.velez@gmail.com','fitwithsheryl@yahoo.com','esh65@georgetown.edu','alyson.stearns0@gmail.com','roxanna7794@gmail.com','bsanders659@gmail.com','ekblosser2014@gmail.com','ketthomass@gmail.com','ginger10142@gmail.com','bkny248@gmail.com','annie.brady9@gmail.com','drthom6@yahoo.com','ajacobszoon@yahoo.com','sandybochnia@hotmail.com','greatestgrandma844@gmail.com','lv_jenson@yahoo.com','yeimy.c.esp@gmail.com','tammy.wzorek@gmail.com','abrand12000@yahoo.com','ebelonga@gmail.com','lak2014@yahoo.com','annie.griffin@yahoo.com','cassandra1216@gmail.com','dmquesada@yahoo.com','crystalfletcher26@yahoo.com','ekawescott@gmail.com','chrissiesmith3223@gmail.com','peytonglee@comcast.net','kadytalley@gmail.com','paulamharvey2016@gmail.com','nnovotny12@gmail.com','dawn.washburn75@gmail.com','alyssa.swingle@gmail.com','dinkwod@hotmail.com','jsolazzi@gmail.com','anniemac6@aol.com','lmcmilla13@gmail.com','shetman83@gmail.com','heart0406@gmail.com','miamiangel58@yahoo.com','k_davis1989@aol.com','catdee2000@verizon.net','bethrumpl@gmail.com','alicesizzy@gmail.com','psanchez2269@charter.net','laurabhill72@gmail.com','kim.dumitrescu@gmail.com','kristenbobst@gmail.com','hckred@gmail.com','soapwoldt23@gmail.com','loren_pizano@hotmail.com','karenwaskewicz@gmail.com','sarampaburch@yahoo.com','kellyglowicz@gmail.com','lilyroloson@yahoo.com','angieviarreola@hotmail.com','jflowosubux@gmail.com','cvmoney@comcast.net','ddellacort@gmail.com','wrongemailahandy@gmail.com','dalgarra@gmail.com','spencer.briana@gmail.com','seahorse1994@gmail.com','michelle.chabino@gmail.com','ceakin70x7@msn.com','galstyanhermine@yahoo.com','winklemanlea@yahoo.com','laurasansone@outlook.com','jhmcmillian@gmail.com','laurajferreira@aol.com','dancerliz01@yahoo.com','gliddle492@gmail.com','dmy418@hotmail.com','brinkley.glenda@yahoo.com','mlle.archie@gmail.com','kmenzel33@yahoo.com','triley900@gmail.com','aridiacl1@gmail.com','kellyemcclurkin@yahoo.com','alexpsimon05@gmail.com','kpoole@graceba.net','jeff@jeffvance.com','kristina-paige@hotmail.com','maggtrev@gmail.com','jaedenalexandra@icloud.com','bikebabe23@gmail.com','zdowd1977@yahoo.com','jwootan5347@yahoo.com','llayton@me.com','victoriamoser18@gmail.com','courtney_c8911@yahoo.com','marciamurphy615@gmail.com','tudie0011@gmail.com','saurich12@gmail.com','rcole8990@gmail.com','mmojab0@gmail.com','monshar7@gmail.com','reagand203@live.com','chericorey@gmail.com','aebosak@gmail.com','jacknsally86@yahoo.com','bguanc@gmail.com','jbkiii@aol.com','sbabs99@gmail.com','tcartledge911@gmail.com','lindseybeadles@gmail.com','ajeannette82@yahoo.com','shelbyezernack0776@icloud.com','judithkennedy@mail.com','thelittlestu57@gmail.com','beccarob99@gmail.com','tiffanykayser74@gmail.com','lbrandstatt@aol.com','jbchicken34@aol.com','lauren02128@yahoo.com','flydl1959@aol.com','kerryannandrews@gmail.com','jmj489113@gmail.com','a_conway514@yahoo.com','lissab042@gmail.com','valeriekilmartin@yahoo.com','kcasey83@gmail.com','asialunsford08@gmail.com','christinar123@gmail.com','kloverde2@gmail.com','tehutto@comcast.net','nicole213@outlook.com','charlottev654@gmail.com','cwhaney61@yahoo.com','avi.scheinbaum@gmail.com','maurashaffer64@gmail.com','jsweetjenn@aol.com','briana.hogg@gmail.com','cvaldeon@fetteamerica.com','anastasiarees@gmail.com','roseaponte15@gmail.com','michellecook1583@yahoo.com','jmoak@cinci.rr.com','ecarny12@gmail.com','andreascollon@yahoo.com','eidens0014@gmail.com','laurenmb1020@yahoo.com','katielynmooneyham@yahoo.com','korismommy@gmail.com','melissaperez@icloud.com','momofboyz28@gmail.com','lauriepatrick3@gmail.com','oloegarza@yahoo.com','kjw000@hotmail.com','banana.anna973@gmail.com','wbrueck@hotmail.com','candicecaytonmorin@gmail.com','cello9876@yahoo.com','ewindley@gmail.com','hcropp@samaritanbethany.com','kat_cairns@hotmail.com','hariett.wike@gmail.com','kjtrip@hotmail.com','kcallais@msn.com','margaret_north@att.net','kconti@kcontilaw.com','jonsinfinity@gmail.com','bdmurphy94@gmail.com','rubenandleanngonzalez@live.com','athenafocas@gmail.com','aesoto0320@hotmail.com');", nativeQuery = true)
    Set<Long> findSubscriptionsToRemove(@Param("shop") String shop);

    @Query("select scd from SubscriptionContractDetails scd where scd.shop = :shop and scd.importedId = :importedId")
    List<SubscriptionContractDetails> findSubscriptionListByShopAndImportedId(@Param("shop") String shop, @Param("importedId") String importedId);

    @Query(value = "select sum(scd.orderAmountUSD) from SubscriptionContractDetails scd where scd.shop = :shop and scd.orderAmountUSD is not null " +
        "AND scd.importedId is null AND scd.createdAt >=:fromDate and scd.createdAt <=:toDate")
    Double getTotalUsedOrderAmountUSDForDateRange(@Param("shop") String shop, @Param("fromDate") ZonedDateTime fromDate, @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT scd.subscriptionContractId FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop and scd.importedId is not null and scd.pausedFromActive = true and scd.subscriptionContractId is not null")
    List<Long> findSubscriptionContractIdsByShopAndIsImported(@Param("shop") String shop);


    @Query("Select distinct(scd.customerEmail) from SubscriptionContractDetails scd where scd.shop = :shop")
    List<String> findCustomerEmailsByShop(@Param("shop") String shop);

    @Query("Select distinct(scd.customerId) from SubscriptionContractDetails scd where scd.shop = :shop")
    List<Long> findCustomerIdsByShop(@Param("shop") String shop);

    @Query(value = "select subscription_contract_id from subscription_contract_details where shop = :shop group by subscription_contract_id having count(*) > 1", nativeQuery = true)
    List<Long> findDuplicateContractIds(@Param("shop") String shop);

    Long countAllByShopAndSubscriptionTypeIdentifier(String shop, String subscriptionTypeIdentifier);

    SubscriptionContractDetails findFirstByShop(String shop);
    SubscriptionContractDetails findFirstByShopAndStatus(String shop, String status);

    @Query("SELECT count (scd.subscriptionContractId) " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'cancelled'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt >= :fromDate " +
        "AND scd.createdAt <= :toDate " +
        "AND scd.cancelledOn is not null " +
        "AND scd.cancelledOn >=:fromDate " +
        "AND scd.cancelledOn <=:toDate "
    )
    Optional<Long> getCountTotalSubscriptionCancelledInDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);

    @Query("SELECT scd " +
        "FROM SubscriptionContractDetails scd " +
        "WHERE scd.shop =:shop " +
        "AND scd.shop is not null " +
        "AND scd.status = 'cancelled'" +
        "AND scd.createdAt is not null " +
        "AND scd.createdAt >= :fromDate " +
        "AND scd.createdAt <= :toDate " +
        "AND scd.cancelledOn is not null " +
        "AND scd.cancelledOn >=:fromDate " +
        "AND scd.cancelledOn <=:toDate "
    )
    List<SubscriptionContractDetails> getTotalSubscriptionCancelledInDateRange(
        @Param("shop") String shop,
        @Param("fromDate") ZonedDateTime fromDate,
        @Param("toDate") ZonedDateTime toDate);
}
