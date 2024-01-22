package com.et.service;

import com.et.pojo.OrderAmountByDatePojo;
import com.et.pojo.OrderSummeryByDate;
import com.et.service.dto.AnalyticsDTO;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import com.et.service.dto.SubscriptionsTotalAmountByWeek;
import com.et.web.rest.vm.FilterProperties;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.Analytics}.
 */
public interface AnalyticsService {

    /**
     * Save a analytics.
     *
     * @param analyticsDTO the entity to save.
     * @return the persisted entity.
     */
    AnalyticsDTO save(AnalyticsDTO analyticsDTO);

    /**
     * Get all the analytics.
     *
     * @return the list of entities.
     */
    List<AnalyticsDTO> findAll();


    @Transactional(readOnly = true)
    List<AnalyticsDTO> findByShop(String shop);

    /**
     * Get the "id" analytics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnalyticsDTO> findOne(Long id);

    /**
     * Delete the "id" analytics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SubscriptionBillingAttemptDTO> getFailedPaymentsList(
        String shop,
        FilterProperties filterProperties);

    Double getTotalSoldAmountByDateRange(String shop, FilterProperties filterProperties, String currency);

    Double getTotalSubscribedAmountByDateRange(String shop,FilterProperties filterProperties, String currency);

    Long getTotalOrderCountByDateRange(String shop, FilterProperties filterProperties);

    Double getTotalSoldAmountByDateRange(String shop, FilterProperties filterProperties);

    List<OrderAmountByDatePojo> orderAmountByWeek(String shop, FilterProperties filterProperties, String currency);

    List<OrderSummeryByDate> orderSummeryByWeek(String shop, FilterProperties filterProperties, String currency);

    List<OrderSummeryByDate> orderCountByWeek(String shop, FilterProperties filterProperties, String currency);

    List<SubscriptionsTotalByDate> subscriptionsTotalByWeek(String shop, FilterProperties filterProperties);

    List<SubscriptionsTotalByDate> subscriptionsCountByWeekAndStatus(String shop, FilterProperties filterProperties, String status);

    List<SubscriptionsTotalAmountByWeek> subscriptionsTotalAmountByWeek(String shop, FilterProperties filterProperties);

    Long getTotalCanceledSubscriptionCount(String shop, FilterProperties filterProperties);

    Double getAverageOrderValue(String shop, FilterProperties filterProperties, String currency);

    Double futureUpcomingOrderRevenueDayWiseTotal(String shop, Long days, String futureOrPast, String currency);

    Double getPastDaysOrderRevenueTotal(String shop, Long days, String futureOrPast, String currency);

    Long getNewSubscription(String shop, FilterProperties filterProperties);

    Long getTotalSubscriptionByBillingIntervalDays(String shop, FilterProperties filterProperties, String billingPolicyInterval);

    Double getChurnRate(String shop, FilterProperties filterProperties);

    Long getTotalPausedSubscriptionCount(String shop, FilterProperties filterProperties);

    Long getTotalCustomerCount(String shop, FilterProperties filterProperties);

    Double getSubscriptionGrowthMonthOverMonth(String shop, FilterProperties filterProperties);

    Double getRevenueGrowthMonthOverMonth(String shop, FilterProperties filterProperties, String currency);

    Long getTotalSubscriptionCount(String shop, FilterProperties filterProperties);

    Long getTotalSubscriptionCountByStatus(String shop, FilterProperties filterProperties, String status);

    Double getTotalSubscriptionAmount(String shop, FilterProperties filterProperties);

    Long getTotalActiveSubscriptionCount(String shop, FilterProperties filterProperties);

    Long getTotalFailedPaymentsCount(String shop, FilterProperties filterProperties);

    Double getAverageSubscriptionValue(String shop, FilterProperties filterProperties);

    Long getTotalNewSubscriptionCount(String shop, Long days, String futureOrPast);

    Long getTotalCanceledSubscriptionCount(String shop, Long days, String futureOrPast);

    Long getTotalRecurringOrdersSoldCounterByDateRange(
        String shop,
        FilterProperties filterProperties
    );

    Long getTotalSkippedCounterByDateRange(
        String shop,
        FilterProperties filterProperties
    );

    Double getApprovalRate(String shop,
                         FilterProperties filterProperties);

    Double getCancellationRate(String shop,
                         FilterProperties filterProperties);

    Double getAverageSuccessfulOrderBeforeCancellation(String shop, FilterProperties filterProperties);
}
