package com.et.service.impl;

import com.et.domain.Analytics;
import com.et.domain.SubscriptionContractDetails;
import com.et.pojo.OrderAmountByDatePojo;
import com.et.pojo.OrderSummeryByDate;
import com.et.repository.AnalyticsRepository;
import com.et.repository.CustomerPaymentRepository;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.AnalyticsService;
import com.et.service.CurrencyConversionInfoService;
import com.et.service.SubscriptionsTotalByDate;
import com.et.service.dto.*;
import com.et.service.mapper.AnalyticsMapper;
import com.et.service.mapper.SubscriptionBillingAttemptMapper;
import com.et.utils.SeedsUtils;
import com.et.web.rest.vm.FilterProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link Analytics}.
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private final AnalyticsRepository analyticsRepository;

    private final AnalyticsMapper analyticsMapper;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository, AnalyticsMapper analyticsMapper) {
        this.analyticsRepository = analyticsRepository;
        this.analyticsMapper = analyticsMapper;
    }

    @Override
    public AnalyticsDTO save(AnalyticsDTO analyticsDTO) {
        log.debug("Request to save Analytics : {}", analyticsDTO);
        Analytics analytics = analyticsMapper.toEntity(analyticsDTO);
        analytics = analyticsRepository.save(analytics);
        return analyticsMapper.toDto(analytics);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsDTO> findAll() {
        log.debug("Request to get all Analytics");
        return analyticsRepository.findAll().stream()
            .map(analyticsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalyticsDTO> findByShop(String shop) {
        log.debug("Request to get all Analytics");
        return analyticsRepository.findByShop(shop).stream()
            .map(analyticsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnalyticsDTO> findOne(Long id) {
        log.debug("Request to get Analytics : {}", id);
        return analyticsRepository.findById(id)
            .map(analyticsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Analytics : {}", id);
        analyticsRepository.deleteById(id);
    }

    @Autowired
    private SeedsUtils seedsUtils;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper;

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    @Autowired
    private CurrencyConversionInfoService currencyConversionInfoService;

    @Override
    public List<SubscriptionBillingAttemptDTO> getFailedPaymentsList(
        String shop,
        FilterProperties filterProperties) {
        List<SubscriptionBillingAttemptDTO> failedBillingAttempts = subscriptionBillingAttemptRepository.getFailedAttemptsDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate()).stream()
            .map(subscriptionBillingAttemptMapper::toDto).collect(Collectors.toList());
        return failedBillingAttempts;
    }

    @Override
    public Double getTotalSoldAmountByDateRange(
        String shop,
        FilterProperties filterProperties,
        String currency) {
        Optional<Double> recurringOrderAmountOpt = subscriptionBillingAttemptRepository.getTotalSoldAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Double recurringOrderAmount = recurringOrderAmountOpt.orElse(0D);
        recurringOrderAmount = convertUsdToCurrency(recurringOrderAmount, currency);
        return recurringOrderAmount;
    }

    @Override
    public Double getTotalSubscribedAmountByDateRange(
        String shop,
        FilterProperties filterProperties,
        String currency) {
        Optional<Double> oneTimeOrderAmount = subscriptionContractDetailsRepository.getTotalSoldAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Double totalSubscribedAmount = oneTimeOrderAmount.orElse(0D);
        totalSubscribedAmount = convertUsdToCurrency(totalSubscribedAmount, currency);
        return totalSubscribedAmount;
    }

    @Override
    public Long getTotalOrderCountByDateRange(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Long> oneTimeOrderCount = subscriptionContractDetailsRepository.getTotalOrderCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Long> recurringOrderCount = subscriptionBillingAttemptRepository.getTotalOrderCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return oneTimeOrderCount.orElse(0L) + recurringOrderCount.orElse(0L);
    }

    @Override
    public Double getTotalSoldAmountByDateRange(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Double> oneTimeOrderAmount = subscriptionContractDetailsRepository.getTotalOrderAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Double> recurringOrderAmount = subscriptionBillingAttemptRepository.getTotalOrderAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return oneTimeOrderAmount.orElse(0.00) + recurringOrderAmount.orElse(0.00);
    }

    @Override
    public Long getTotalCanceledSubscriptionCount(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Long> oneTimeCanceledOrderCount = subscriptionContractDetailsRepository.getTotalCanceledSubscriptionCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return oneTimeCanceledOrderCount.orElse(0L);
    }

    @Override
    public Double getAverageOrderValue(
        String shop,
        FilterProperties filterProperties,
        String currency
    ) {
        Optional<Double> oneTimeOrderAmount = subscriptionContractDetailsRepository.getTotalSoldAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Double> recurringOrderAmount = subscriptionBillingAttemptRepository.getTotalSoldAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Long> oneTimeOrderCount = subscriptionContractDetailsRepository.getTotalSoldAmountByDateRangeAVG(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Long> recurringOrderCount = subscriptionBillingAttemptRepository.getTotalSoldAmountByDateRangeAVG(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Double totalAmount = oneTimeOrderAmount.orElse(0D) + recurringOrderAmount.orElse(0D);
        Long totalOrder = oneTimeOrderCount.orElse(0L) + recurringOrderCount.orElse(0L);
        totalAmount = convertUsdToCurrency(totalAmount, currency);
        double averageValue = (totalAmount / totalOrder);
        if (Double.isNaN(averageValue)) {
            averageValue = 0D;
        }
        return averageValue;
    }

    @Override
    public List<OrderAmountByDatePojo> orderAmountByWeek(
        @Nonnull final String shopName,
        FilterProperties filterProperties,
        String currency) {
        List<OrderAmountByDate> orderAmountByDateOneTimeOrder = subscriptionContractDetailsRepository.orderAmountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());
        List<OrderAmountByDate> orderAmountByDateRecurringOrder = subscriptionBillingAttemptRepository.orderAmountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());

        Map<String, List<OrderAmountByDate>> sumByDates = Stream.concat(orderAmountByDateOneTimeOrder.stream(), orderAmountByDateRecurringOrder.stream()).collect(Collectors.groupingBy(OrderAmountByDate::getOrderCreatedAt));

        List<OrderAmountByDatePojo> list = new ArrayList<>();

        Double currencyRate = getCurrencyConversionRate(currency);

        for (Map.Entry<String, List<OrderAmountByDate>> entry : sumByDates.entrySet()) {
            Double totalSum = entry.getValue()
                .stream()
                .map(OrderAmountByDate::getSum)
                .filter(sumByDate -> sumByDate != null)
                .map(Double::parseDouble)
                .reduce(Double::sum)
                .orElse(0D);

            totalSum = totalSum / currencyRate;
            OrderAmountByDatePojo orderAmountByDatePojo = new OrderAmountByDatePojo();
            orderAmountByDatePojo.setOrderCreatedAt(entry.getKey());
            orderAmountByDatePojo.setSum(totalSum.toString());
            list.add(orderAmountByDatePojo);
        }

        list.sort(Comparator.comparing(OrderAmountByDatePojo::getOrderCreatedAt));
        return list;
    }

    @Override
    public List<OrderSummeryByDate> orderSummeryByWeek(
        @Nonnull final String shopName,
        FilterProperties filterProperties,
        String currency) {
        List<OrderAmountByDate> orderAmountByDateOneTimeOrder = subscriptionContractDetailsRepository.orderAmountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());
        List<OrderAmountByDate> orderAmountByDateRecurringOrder = subscriptionBillingAttemptRepository.orderAmountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());

        Map<String, List<OrderAmountByDate>> sumByDates = Stream.concat(orderAmountByDateOneTimeOrder.stream(), orderAmountByDateRecurringOrder.stream()).collect(Collectors.groupingBy(OrderAmountByDate::getOrderCreatedAt));

        List<OrderSummeryByDate> list = new ArrayList<>();

        Double currencyRate = getCurrencyConversionRate(currency);

        for (Map.Entry<String, List<OrderAmountByDate>> entry : sumByDates.entrySet()) {
            Double totalSum = entry.getValue()
                .stream()
                .map(OrderAmountByDate::getSum)
                .filter(sumByDate -> sumByDate != null)
                .map(Double::parseDouble)
                .reduce(Double::sum)
                .orElse(0D);

            totalSum = totalSum / currencyRate;
            OrderSummeryByDate orderAmountByDatePojo = new OrderSummeryByDate();
            orderAmountByDatePojo.setOrderCreatedAt(entry.getKey());
            orderAmountByDatePojo.setTotal(totalSum.toString());
            list.add(orderAmountByDatePojo);
        }

        list.sort(Comparator.comparing(OrderSummeryByDate::getOrderCreatedAt));
        return list;
    }

    @Override
    public List<OrderSummeryByDate> orderCountByWeek(
        @Nonnull final String shopName,
        FilterProperties filterProperties,
        String currency) {
        List<OrderCountByDate> orderAmountByDateOneTimeOrder = subscriptionContractDetailsRepository.orderCountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());
        List<OrderCountByDate> orderAmountByDateRecurringOrder = subscriptionBillingAttemptRepository.orderCountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());

        Map<String, List<OrderCountByDate>> sumByDates = Stream.concat(orderAmountByDateOneTimeOrder.stream(), orderAmountByDateRecurringOrder.stream()).collect(Collectors.groupingBy(OrderCountByDate::getOrderCreatedAt));

        List<OrderSummeryByDate> list = new ArrayList<>();

        Double currencyRate = getCurrencyConversionRate(currency);

        for (Map.Entry<String, List<OrderCountByDate>> entry : sumByDates.entrySet()) {
            Double totalSum = entry.getValue()
                .stream()
                .map(OrderCountByDate::getTotal)
                .filter(Objects::nonNull)
                .map(Double::parseDouble)
                .reduce(Double::sum)
                .orElse(0D);

            totalSum = totalSum / currencyRate;
            OrderSummeryByDate orderAmountByDatePojo = new OrderSummeryByDate();
            orderAmountByDatePojo.setOrderCreatedAt(entry.getKey());
            orderAmountByDatePojo.setTotal(totalSum.toString());
            list.add(orderAmountByDatePojo);
        }

        list.sort(Comparator.comparing(OrderSummeryByDate::getOrderCreatedAt));
        return list;
    }

    @Override
    public List<SubscriptionsTotalByDate> subscriptionsTotalByWeek(
        @Nonnull final String shopName,
        FilterProperties filterProperties) {
        return subscriptionContractDetailsRepository.subscriptionsTotalByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());
    }

    public List<SubscriptionsTotalByDate> subscriptionsCountByWeekAndStatus(
        @Nonnull final String shopName,
        FilterProperties filterProperties,
        @Nonnull final String status
    ) {
        return subscriptionContractDetailsRepository.subscriptionsCountByWeekAndStatus(shopName, filterProperties.getFromDate(), filterProperties.getToDate(), status);
    }

    @Override
    public List<SubscriptionsTotalAmountByWeek> subscriptionsTotalAmountByWeek(
        @Nonnull final String shopName,
        FilterProperties filterProperties) {
        return subscriptionContractDetailsRepository.subscriptionsTotalAmountByWeek(shopName, filterProperties.getFromDate(), filterProperties.getToDate());
    }

    @Override
    public Double getPastDaysOrderRevenueTotal(String shop, Long days, String futureOrPast, String currency) {
        FilterProperties filterProperties = seedsUtils.getFutureOrPastFilterProperties(days, futureOrPast);
        Optional<Double> pastDaysOrderRevenueTotalOpt = subscriptionBillingAttemptRepository.getPastDaysOrderRevenueTotal(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Double pastDaysOrderRevenueTotal = pastDaysOrderRevenueTotalOpt.orElse(0D);
        pastDaysOrderRevenueTotal = convertUsdToCurrency(pastDaysOrderRevenueTotal, currency);
        return pastDaysOrderRevenueTotal;
    }

    @Override
    public Double futureUpcomingOrderRevenueDayWiseTotal(String shop, Long days, String futureOrPast, String currency) {
        FilterProperties filterProperties = seedsUtils.getFutureOrPastFilterProperties(days, futureOrPast);
        Optional<Double> futureUpcomingOrderRevenueDayWiseTotalOpt = subscriptionBillingAttemptRepository.getTotalNextSevenOrThirtyOrNinetyDayOrderAmountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());

        Double futureUpcomingOrderRevenueDayWiseTotal = futureUpcomingOrderRevenueDayWiseTotalOpt.orElse(0D);
        futureUpcomingOrderRevenueDayWiseTotal = convertUsdToCurrency(futureUpcomingOrderRevenueDayWiseTotal, currency);
        return futureUpcomingOrderRevenueDayWiseTotal;
    }

    @Override
    public Long getNewSubscription(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Long> newSubscription = subscriptionContractDetailsRepository.countActiveSubscription(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return newSubscription.orElse(0L);
    }

    @Override
    public Long getTotalSubscriptionByBillingIntervalDays(
        String shop,
        FilterProperties filterProperties,
        String billingPolicyInterval
    ) {
        Optional<Long> totalSubscriptionByBillingIntervalDays = subscriptionContractDetailsRepository.getTotalSubscriptionByBillingIntervalDays(shop, filterProperties.getFromDate(), filterProperties.getToDate(), billingPolicyInterval);
        return totalSubscriptionByBillingIntervalDays.orElse(0L);
    }

    @Override
    public Double getChurnRate(@Nonnull final String shop,
                               FilterProperties filterProperties) {
        Long cancelledSubscriptionsBetween = subscriptionContractDetailsRepository.getTotalCancelledButCreatedBeforeDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate()).orElse(0L);
        Long totalActiveSubscriptionsCreatedBefore = subscriptionContractDetailsRepository.getTotalSubscriptionsCreatedBefore(shop, filterProperties.getFromDate()).orElse(0L);

        if (totalActiveSubscriptionsCreatedBefore == 0) {
            return 0D;
        }

        return round(((double) cancelledSubscriptionsBetween / (double) totalActiveSubscriptionsCreatedBefore) * 100);
    }

    @Override
    public Long getTotalPausedSubscriptionCount(String shop,
                                                FilterProperties filterProperties) {
        Optional<Long> oneTimePausedOrderCount = subscriptionContractDetailsRepository.getTotalPausedSubscriptionCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return oneTimePausedOrderCount.orElse(0L);
    }

    @Override
    public Long getTotalCustomerCount(String shop,
                                      FilterProperties filterProperties) {
        Optional<Long> totalCustomerCount = subscriptionContractDetailsRepository.getTotalCustomerCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalCustomerCount.orElse(0L);
    }

    @Override
    public Double getSubscriptionGrowthMonthOverMonth(String shop,
                                                      FilterProperties filterProperties) {
        Optional<Long> currentMonthSubscriptionCount = subscriptionContractDetailsRepository.getTotalMonthoverMonthSubscriptionCountByDateRange(shop, filterProperties.getToDate().minusDays(30), filterProperties.getToDate());
        Optional<Long> pastMonthSubscriptionCount = subscriptionContractDetailsRepository.getTotalMonthoverMonthSubscriptionCountByDateRange(shop, filterProperties.getToDate().minusDays(60), filterProperties.getToDate().minusDays(30));
        Long subscriptionGrowthMonthOverMonth = currentMonthSubscriptionCount.orElse(0L) - pastMonthSubscriptionCount.orElse(0L);
        Double subscriptionGrowthMonthOverMonthPercentage = null;
        if (pastMonthSubscriptionCount.orElse(0L) != 0L) {
            subscriptionGrowthMonthOverMonthPercentage = Double.valueOf(subscriptionGrowthMonthOverMonth * 100 / pastMonthSubscriptionCount.orElse(0L));
        }
        return subscriptionGrowthMonthOverMonthPercentage;
    }

    @Override
    public Double getRevenueGrowthMonthOverMonth(String shop,
                                                 FilterProperties filterProperties,
                                                 String currency) {
        Optional<Double> currentMonthRevenueCount = subscriptionBillingAttemptRepository.getTotalMonthoverMonthRevenueCountByDateRange(shop, filterProperties.getToDate().minusDays(30), filterProperties.getToDate());
        Optional<Double> pastMonthRevenueCount = subscriptionBillingAttemptRepository.getTotalMonthoverMonthRevenueCountByDateRange(shop, filterProperties.getToDate().minusDays(60), filterProperties.getToDate().minusDays(30));
        Double revenueGrowthMonthOverMonth = currentMonthRevenueCount.orElse(0D) - pastMonthRevenueCount.orElse(0D);
        revenueGrowthMonthOverMonth = convertUsdToCurrency(revenueGrowthMonthOverMonth, currency);
        Double revenueGrowthMonthOverMonthPercentage = null;
        if (pastMonthRevenueCount.orElse(0D) != 0D) {
            revenueGrowthMonthOverMonthPercentage = (revenueGrowthMonthOverMonth * 100) / pastMonthRevenueCount.orElse(0D);
            DecimalFormat df = new DecimalFormat("0.0");
            return Double.parseDouble(df.format(revenueGrowthMonthOverMonthPercentage));
        } else {
            return revenueGrowthMonthOverMonthPercentage;
        }
    }

    @Override
    public Long getTotalSubscriptionCount(String shop,
                                          FilterProperties filterProperties) {
        Optional<Long> totalSubscriptionCount = subscriptionContractDetailsRepository.getTotalSubscriptionCount(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalSubscriptionCount.orElse(0L);
    }

    @Override
    public Long getTotalSubscriptionCountByStatus(String shop,
                                                  FilterProperties filterProperties,
                                                  String status

    ) {
        Optional<Long> totalSubscriptionCount = subscriptionContractDetailsRepository.getTotalSubscriptionCountByStatus(shop, filterProperties.getFromDate(), filterProperties.getToDate(), status);
        return totalSubscriptionCount.orElse(0L);
    }

    @Override
    public Double getTotalSubscriptionAmount(String shop,
                                             FilterProperties filterProperties) {
        Optional<Double> totalSubscriptionCount = subscriptionContractDetailsRepository.getTotalSubscriptionAmount(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalSubscriptionCount.orElse(0.00);
    }

    @Override
    public Long getTotalActiveSubscriptionCount(String shop,
                                                FilterProperties filterProperties) {
        Optional<Long> totalActiveSubscriptionCount = subscriptionContractDetailsRepository.getTotalActiveSubscriptionCount(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalActiveSubscriptionCount.orElse(0L);
    }

    @Override
    public Long getTotalFailedPaymentsCount(String shop,
                                            FilterProperties filterProperties) {
        Optional<Long> totalFailedPaymentsCount = subscriptionBillingAttemptRepository.getTotalFailedPaymentsCount(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalFailedPaymentsCount.orElse(0L);
    }

    @Override
    public Double getAverageSubscriptionValue(String shop,
                                              FilterProperties filterProperties) {
        Optional<Double> averageValue = subscriptionContractDetailsRepository.getAverageSubscriptionValue(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return averageValue.orElse(null);
    }

    @Override
    public Long getTotalNewSubscriptionCount(String shop, Long days, String futureOrPast) {
        FilterProperties filterProperties = seedsUtils.getFutureOrPastFilterProperties(days, futureOrPast);
        Optional<Long> totalActiveSubscriptionCount = subscriptionContractDetailsRepository.getTotalNewSubscriptionCount(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalActiveSubscriptionCount.orElse(0L);
    }

    @Override
    public Long getTotalCanceledSubscriptionCount(String shop, Long days, String futureOrPast) {
        FilterProperties filterProperties = seedsUtils.getFutureOrPastFilterProperties(days, futureOrPast);
        Optional<Long> oneTimeCanceledOrderCount = subscriptionContractDetailsRepository.getTotalCancelledButCreatedBeforeDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return oneTimeCanceledOrderCount.orElse(0L);
    }

    private double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }

    private Double convertUsdToCurrency(Double amount, String currency) {
        if (StringUtils.isNotBlank(currency) && !currency.equals("USD")) {
            Optional<CurrencyConversionInfoDTO> currencyConversionInfoDTO = currencyConversionInfoService.findCurrencyConversionInfoByFrom(currency);
            if (currencyConversionInfoDTO.isPresent()) {
                amount = amount / currencyConversionInfoDTO.get().getCurrencyRate();
            }
        }
        return amount;
    }

    private Double getCurrencyConversionRate(String currency) {
        if (StringUtils.isNotBlank(currency) && !currency.equals("USD")) {
            Optional<CurrencyConversionInfoDTO> currencyConversionInfoDTO = currencyConversionInfoService.findCurrencyConversionInfoByFrom(currency);
            if (currencyConversionInfoDTO.isPresent()) {
                return currencyConversionInfoDTO.get().getCurrencyRate();
            }
        }
        return 1D;
    }

    @Override
    public Long getTotalRecurringOrdersSoldCounterByDateRange(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Long> recurringOrderCount = subscriptionBillingAttemptRepository.getTotalOrderCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return recurringOrderCount.orElse(0L);
    }

    @Override
    public Long getTotalSkippedCounterByDateRange(
        String shop,
        FilterProperties filterProperties
    ) {
        Optional<Long> totalSkippedCounterByDateRange = subscriptionBillingAttemptRepository.getTotalSkippedOrdersByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        return totalSkippedCounterByDateRange.orElse(0L);
    }

    @Override
    public Double getApprovalRate(String shop,
                                FilterProperties filterProperties) {
        Optional<Long> totalSuccessfullOrders = subscriptionBillingAttemptRepository.getTotalOrderCountByDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Long totalFailedOrdersCount = getTotalFailedPaymentsCount(shop, filterProperties);
        Long totalOrders = totalSuccessfullOrders.orElse(0L) + totalFailedOrdersCount;

        if (totalOrders.equals(0L)) {
            return 0D;
        }

        return  round((double) ( (double) totalSuccessfullOrders.orElse(0L) /  (double) totalOrders) * 100);
    }

    @Override
    public Double getCancellationRate(String shop,
                                FilterProperties filterProperties) {
        Optional<Long> activeSubscription = subscriptionContractDetailsRepository.countActiveSubscription(shop, filterProperties.getFromDate(), filterProperties.getToDate());
        Optional<Long> totalSubscriptionCount = subscriptionContractDetailsRepository.getTotalSubscriptionCount(shop, filterProperties.getFromDate(), filterProperties.getToDate());

        if (totalSubscriptionCount.orElse(0L) == 0) {
            return 0D;
        }

        return round((((double) totalSubscriptionCount.orElse(0L) - (double) activeSubscription.orElse(0L)) / (double) totalSubscriptionCount.orElse(0L)) * 100);
    }

    @Override
    public Double getAverageSuccessfulOrderBeforeCancellation(String shop, FilterProperties filterProperties) {
        List<SubscriptionContractDetails> subscriptionContractDetailsDTOList = subscriptionContractDetailsRepository.getTotalSubscriptionCancelledInDateRange(shop, filterProperties.getFromDate(), filterProperties.getToDate());

        long contractsCount = subscriptionContractDetailsDTOList.size();

        if(contractsCount == 0) {
            return 0D;
        }

        Long successfulOrders = 0L;

        for(SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsDTOList) {
            Optional<ContractAnalytics> contractAnalytics= subscriptionBillingAttemptRepository.findTotalOrderAndTotalRevenueByContractId(subscriptionContractDetails.getSubscriptionContractId());
            if(contractAnalytics.isPresent()) {
                successfulOrders += contractAnalytics.get().getTotalOrderCount();
            }
        }

        return round((double) successfulOrders / contractsCount);

    }

}
