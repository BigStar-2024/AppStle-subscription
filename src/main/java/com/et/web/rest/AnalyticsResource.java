package com.et.web.rest;

import com.et.api.utils.CurrencyFormattingUtils;
import com.et.constant.Constants;
import com.et.domain.ShopInfo;
import com.et.pojo.OrderAmountByDatePojo;
import com.et.pojo.OrderSummeryByDate;
import com.et.repository.ShopInfoRepository;
import com.et.security.SecurityUtils;
import com.et.service.AnalyticsService;
import com.et.service.SubscriptionsTotalByDate;
import com.et.service.dto.*;
import com.et.utils.SeedsUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.FilterProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.Analytics}.
 */
@RestController
@RequestMapping("/api")
public class AnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    private static final String ENTITY_NAME = "analytics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalyticsService analyticsService;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Autowired
    private SeedsUtils seedsUtils;

    public AnalyticsResource(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * {@code POST  /analytics} : Create a new analytics.
     *
     * @param analyticsDTO the analyticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analyticsDTO, or with status {@code 400 (Bad Request)} if the analytics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analytics")
    public ResponseEntity<AnalyticsDTO> createAnalytics(@Valid @RequestBody AnalyticsDTO analyticsDTO) throws URISyntaxException {
        log.debug("REST request to save Analytics : {}", analyticsDTO);
        if (analyticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new analytics cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(analyticsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        AnalyticsDTO result = analyticsService.save(analyticsDTO);
        return ResponseEntity.created(new URI("/api/analytics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analytics} : Updates an existing analytics.
     *
     * @param analyticsDTO the analyticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analyticsDTO,
     * or with status {@code 400 (Bad Request)} if the analyticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analyticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analytics")
    public ResponseEntity<AnalyticsDTO> updateAnalytics(@Valid @RequestBody AnalyticsDTO analyticsDTO) throws URISyntaxException {
        log.debug("REST request to update Analytics : {}", analyticsDTO);
        if (analyticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(analyticsDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        AnalyticsDTO result = analyticsService.save(analyticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, analyticsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /analytics} : get all the analytics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analytics in body.
     */
    @GetMapping("/analytics")
    public List<AnalyticsDTO> getAllAnalytics() {
        log.debug("REST request to get all Analytics");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return analyticsService.findByShop(shop);
    }

    @GetMapping("/conversion-analytics/total-order-amount")
    public ResponseEntity<HashMap<String, Object>> getAllConversionAnalyticsForShop(@RequestParam String filterBy,
                                                                                    @RequestParam Long days,
                                                                                    @RequestParam ZonedDateTime fromDay,
                                                                                    @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(shopInfo.getIanaTimeZone());
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);

        Long totalCanceledSubscriptionCount = analyticsService.getTotalCanceledSubscriptionCount(shop, filterProperties);
        Long totalPausedSubscriptionCount = analyticsService.getTotalPausedSubscriptionCount(shop, filterProperties);
        Long totalActiveSubscriptionCount = analyticsService.getTotalActiveSubscriptionCount(shop, filterProperties);
        Long totalFailedPaymentsCount = analyticsService.getTotalFailedPaymentsCount(shop, filterProperties);

        Long totalRecurringOrderCount = analyticsService.getTotalRecurringOrdersSoldCounterByDateRange(shop, filterProperties);
        Double churnRate = analyticsService.getChurnRate(shop, filterProperties);
        List<SubscriptionsTotalByDate> subscriptionsTotalByWeek = analyticsService.subscriptionsTotalByWeek(shop, filterProperties);
        Long totalSkippedOrders = analyticsService.getTotalSkippedCounterByDateRange(shop, filterProperties);

//        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());
//        Long dayIntervalSubscription = analyticsService.getTotalSubscriptionByBillingIntervalDays(shop, filterProperties, FrequencyIntervalUnit.DAY.toString()); // Not used
//        Long weekIntervalSubscription = analyticsService.getTotalSubscriptionByBillingIntervalDays(shop, filterProperties, FrequencyIntervalUnit.WEEK.toString()); // Not used
//        Long monthIntervalSubscription = analyticsService.getTotalSubscriptionByBillingIntervalDays(shop, filterProperties, FrequencyIntervalUnit.MONTH.toString()); // Not used
//        Long yearIntervalSubscription = analyticsService.getTotalSubscriptionByBillingIntervalDays(shop, filterProperties, FrequencyIntervalUnit.YEAR.toString()); // Not used
//        List<SubscriptionBillingAttemptDTO> failedPaymentsList = analyticsService.getFailedPaymentsList(shop, filterProperties); // not used
//        Double averageOrdersBeforeCancellation = analyticsService.getAverageSuccessfulOrderBeforeCancellation(shop, filterProperties); // Not used

        Double approvalRate = analyticsService.getApprovalRate(shop, filterProperties);
        Double cancellationRate = analyticsService.getCancellationRate(shop, filterProperties);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));

        HashMap<String, Object> result = new HashMap<>();
        result.put("canceledSubscriptionCount", totalCanceledSubscriptionCount);
        result.put("pausedSubscriptionCount", totalPausedSubscriptionCount);
        result.put("currency", "USD");
        result.put("subscriptionsTotalByWeek", subscriptionsTotalByWeek);
        result.put("churnRate", churnRate + "%");
        result.put("totalActiveSubscriptionCount", totalActiveSubscriptionCount);
        result.put("totalFailedPaymentsCount", totalFailedPaymentsCount);
        result.put("fromDate", dateTimeFormatter.format(filterProperties.getFromDate()));
        result.put("toDate", dateTimeFormatter.format(filterProperties.getToDate()));
        result.put("totalRecurringOrderCount", totalRecurringOrderCount);
        result.put("totalSkippedOrders", totalSkippedOrders);
        result.put("approvalRate", approvalRate + "%");
        result.put("cancellationRate", cancellationRate + "%");

//        result.put("dayIntervalSubscription", dayIntervalSubscription); // Not used
//        result.put("weekIntervalSubscription", weekIntervalSubscription); // Not used
//        result.put("monthIntervalSubscription", monthIntervalSubscription); // Not used
//        result.put("yearIntervalSubscription", yearIntervalSubscription); // Not used
//        result.put("failedPaymentsList", failedPaymentsList); // Not used
//        result.put("averageOrdersBeforeCancellation", averageOrdersBeforeCancellation); // Not Used

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/overview/total-order-amount")
    public ResponseEntity<HashMap<String, Object>> getOverviewTotalOrderAmountAnalyticsForShop(@RequestParam String filterBy,
                                                                                    @RequestParam Long days,
                                                                                    @RequestParam ZonedDateTime fromDay,
                                                                                    @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(shopInfo.getIanaTimeZone());
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }
        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);

        Long totalSoldOrderCount = analyticsService.getTotalOrderCountByDateRange(shop, filterProperties);
        Long totalSubscriptionCount = analyticsService.getTotalSubscriptionCount(shop, filterProperties);
        Long totalCustomerCount = analyticsService.getTotalCustomerCount(shop, filterProperties);
        Long newSubscription = analyticsService.getNewSubscription(shop, filterProperties);

        HashMap<String, Object> result = new HashMap<>();
        result.put("orderCount", totalSoldOrderCount);
        result.put("totalSubscriptionCount", totalSubscriptionCount);
        result.put("customerCount", totalCustomerCount);
        result.put("newSubscription", newSubscription);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/subscriptions/total-order-amount")
    public ResponseEntity<HashMap<String, Object>> getSubscriptionsTotalOrderAmountAnalyticsForShop(@RequestParam String filterBy,
                                                                                               @RequestParam Long days,
                                                                                               @RequestParam ZonedDateTime fromDay,
                                                                                               @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(shopInfo.getIanaTimeZone());
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);
        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

        Double totalSubscribedAmount = analyticsService.getTotalSubscribedAmountByDateRange(shop, filterProperties, shopInfo.getCurrency());
        String formattedTotalSubscribedAmount = currencyFormattingUtils.formatPrice(totalSubscribedAmount);

        Double totalSoldAmount = analyticsService.getTotalSoldAmountByDateRange(shop, filterProperties, shopInfo.getCurrency());
        String formattedTotalSoldAmount = currencyFormattingUtils.formatPrice(totalSoldAmount);

        Double averageSubscriptionValue = analyticsService.getAverageSubscriptionValue(shop, filterProperties);

        Double subscriptionGrowthMonthOverMonth = analyticsService.getSubscriptionGrowthMonthOverMonth(shop, filterProperties);

        HashMap<String, Object> result = new HashMap<>();
        result.put("totalSubscribed", formattedTotalSubscribedAmount);
        result.put("totalSold", formattedTotalSoldAmount);
        result.put("averageSubscriptionValue", averageSubscriptionValue);
        result.put("subscriptionGrowthMonthOverMonth", subscriptionGrowthMonthOverMonth != null ? (subscriptionGrowthMonthOverMonth + "%") : "Not enough data");

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/revenue/total-order-amount")
    public ResponseEntity<HashMap<String, Object>> getRevenueTotalOrderAmountAnalyticsForShop(@RequestParam String filterBy,
                                                                                                    @RequestParam Long days,
                                                                                                    @RequestParam ZonedDateTime fromDay,
                                                                                                    @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");
        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(shopInfo.getIanaTimeZone());
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);
        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

        Double revenueGrowthMonthOverMonth = analyticsService.getRevenueGrowthMonthOverMonth(shop, filterProperties, shopInfo.getCurrency());

        Double averageOrderValue = analyticsService.getAverageOrderValue(shop, filterProperties, shopInfo.getCurrency());
        String formattedAverageOrderValue = currencyFormattingUtils.formatPrice(averageOrderValue);

        List<OrderAmountByDatePojo> conversionAnalyticsByWeek = analyticsService.orderAmountByWeek(shop, filterProperties, shopInfo.getCurrency());

        HashMap<String, Object> result = new HashMap<>();
        result.put("revenueGrowthMonthOverMonth", revenueGrowthMonthOverMonth != null ? (revenueGrowthMonthOverMonth + "%") : "Not enough data");
        result.put("averageOrderValue", formattedAverageOrderValue);
        result.put("ordersByWeek", conversionAnalyticsByWeek);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/estimated-total-order-amount")
    public ResponseEntity<HashMap<String, Object>> getFutureRevenuePlanningAnalyticsForShop() throws JsonProcessingException {
        log.debug("REST request to get a total order amount of queue ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        Double nextSevenDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 7L, "future", shopInfo.getCurrency());
        Double nextThirtyDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 30L, "future", shopInfo.getCurrency());
        Double nextNinetyDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 90L, "future", shopInfo.getCurrency());

        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());
        String formattedNextSevenDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextSevenDayEstimatedRevenueTotal);
        String formattedNextThirtyDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextThirtyDayEstimatedRevenueTotal);
        String formattedNextNinetyDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextNinetyDayEstimatedRevenueTotal);

        HashMap<String, Object> result = new HashMap<>();
        result.put("nextSevenDayEstimatedRevenueTotal", formattedNextSevenDayEstimatedRevenueTotal);
        result.put("nextThirtyDayEstimatedRevenueTotal", formattedNextThirtyDayEstimatedRevenueTotal);
        result.put("nextNinetyDayEstimatedRevenueTotal", formattedNextNinetyDayEstimatedRevenueTotal);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/estimated-historical-order-amount-total")
    public ResponseEntity<HashMap<String, Object>> getFutureVsPastRevenueAnalyticsForShop() throws JsonProcessingException {
        log.debug("REST request to get a analytics for total order amount of future queue and past success order status ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        Double nextSevenDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 7L, "future", shopInfo.getCurrency());
        Double nextThirtyDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 30L, "future", shopInfo.getCurrency());
        Double nextNinetyDayEstimatedRevenueTotal = analyticsService.futureUpcomingOrderRevenueDayWiseTotal(shop, 90L, "future", shopInfo.getCurrency());

        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());
        String formattedNextSevenDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextSevenDayEstimatedRevenueTotal);
        String formattedNextThirtyDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextThirtyDayEstimatedRevenueTotal);
        String formattedNextNinetyDayEstimatedRevenueTotal = currencyFormattingUtils.formatPrice(nextNinetyDayEstimatedRevenueTotal);

        String currencyCode = currencyFormattingUtils.getCurrencyCode();

        Double historicalSevenDayRevenueTotal = analyticsService.getPastDaysOrderRevenueTotal(shop, 7L, "past", shopInfo.getCurrency());
        Double historicalThirtyDayRevenueTotal = analyticsService.getPastDaysOrderRevenueTotal(shop, 30L, "past", shopInfo.getCurrency());
        Double historicalNinetyDayRevenueTotal = analyticsService.getPastDaysOrderRevenueTotal(shop, 90L, "past", shopInfo.getCurrency());

        String formattedHistoricalSevenDayRevenueTotal = currencyFormattingUtils.formatPrice(historicalSevenDayRevenueTotal);
        String formattedHistoricalThirtyDayRevenueTotal = currencyFormattingUtils.formatPrice(historicalThirtyDayRevenueTotal);
        String formattedHistoricalNinetyDayRevenueTotal = currencyFormattingUtils.formatPrice(historicalNinetyDayRevenueTotal);

        List<EstimatedVsHistoricalRevenue> estimatedVsHistoricalRevenues = new ArrayList<>();

        EstimatedVsHistoricalRevenue estimatedVsHistoricalRevenue1 = new EstimatedVsHistoricalRevenue();

        estimatedVsHistoricalRevenue1.setEstimatedRevenueTotal(formattedNextSevenDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue1.setHistoricalRevenueTotal(formattedHistoricalSevenDayRevenueTotal);

        estimatedVsHistoricalRevenue1.setEstimatedRevenueTotalNumerical(nextSevenDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue1.setHistoricalRevenueTotalNumerical(historicalSevenDayRevenueTotal);

        estimatedVsHistoricalRevenue1.setName("7 days estimatedVsHistorical data");
        estimatedVsHistoricalRevenues.add(estimatedVsHistoricalRevenue1);

        EstimatedVsHistoricalRevenue estimatedVsHistoricalRevenue2 = new EstimatedVsHistoricalRevenue();

        estimatedVsHistoricalRevenue2.setEstimatedRevenueTotal(formattedNextThirtyDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue2.setHistoricalRevenueTotal(formattedHistoricalThirtyDayRevenueTotal);

        estimatedVsHistoricalRevenue2.setEstimatedRevenueTotalNumerical(nextThirtyDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue2.setHistoricalRevenueTotalNumerical(historicalThirtyDayRevenueTotal);

        estimatedVsHistoricalRevenue2.setName("30 days estimatedVsHistorical data");
        estimatedVsHistoricalRevenues.add(estimatedVsHistoricalRevenue2);

        EstimatedVsHistoricalRevenue estimatedVsHistoricalRevenue3 = new EstimatedVsHistoricalRevenue();

        estimatedVsHistoricalRevenue3.setEstimatedRevenueTotal(formattedNextNinetyDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue3.setHistoricalRevenueTotal(formattedHistoricalNinetyDayRevenueTotal);

        estimatedVsHistoricalRevenue3.setEstimatedRevenueTotalNumerical(nextNinetyDayEstimatedRevenueTotal);
        estimatedVsHistoricalRevenue3.setHistoricalRevenueTotalNumerical(historicalNinetyDayRevenueTotal);

        estimatedVsHistoricalRevenue3.setName("90 days estimatedVsHistorical data");
        estimatedVsHistoricalRevenues.add(estimatedVsHistoricalRevenue3);

        HashMap<String, Object> result = new HashMap<>();
        result.put("estimatedVsHistoricalRevenue", estimatedVsHistoricalRevenues);
        result.put("currencyCode", currencyCode);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/subscription-unsubscription")
    public ResponseEntity<HashMap<String, Object>> getSubscriptionVsUnsubscriptionGraphForShop() throws JsonProcessingException {
        log.debug("REST request to get a analytics for subscription vs unsubscription graph ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        Long totalSevenDayNewSubscriptionCount = analyticsService.getTotalNewSubscriptionCount(shop, 7L, "past");
        Long totalThirtyDayNewSubscriptionCount = analyticsService.getTotalNewSubscriptionCount(shop, 30L, "past");
        Long totalNinetyDayNewSubscriptionCount = analyticsService.getTotalNewSubscriptionCount(shop, 90L, "past");

        Long totalSevenDayCanceledSubscriptionCount = analyticsService.getTotalCanceledSubscriptionCount(shop, 7L, "past");
        Long totalThirtyDayCanceledSubscriptionCount = analyticsService.getTotalCanceledSubscriptionCount(shop, 30L, "past");
        Long totalNinetyDayCanceledSubscriptionCount = analyticsService.getTotalCanceledSubscriptionCount(shop, 90L, "past");

        List<SubscriptionVsUnsubscriptionDTO> subscriptionVsUnsubscriptionDTOList = new ArrayList<>();

        SubscriptionVsUnsubscriptionDTO subscriptionVsUnsubscriptionDTO1 = new SubscriptionVsUnsubscriptionDTO();
        subscriptionVsUnsubscriptionDTO1.setSubscriptionCount(totalSevenDayNewSubscriptionCount);
        subscriptionVsUnsubscriptionDTO1.setUnsubscriptionCount(totalSevenDayCanceledSubscriptionCount);
        subscriptionVsUnsubscriptionDTO1.setName("7 days subscriptionVsUnsubscription data");
        subscriptionVsUnsubscriptionDTOList.add(subscriptionVsUnsubscriptionDTO1);

        SubscriptionVsUnsubscriptionDTO subscriptionVsUnsubscriptionDTO2 = new SubscriptionVsUnsubscriptionDTO();
        subscriptionVsUnsubscriptionDTO2.setSubscriptionCount(totalThirtyDayNewSubscriptionCount);
        subscriptionVsUnsubscriptionDTO2.setUnsubscriptionCount(totalThirtyDayCanceledSubscriptionCount);
        subscriptionVsUnsubscriptionDTO2.setName("30 days subscriptionVsUnsubscription data");
        subscriptionVsUnsubscriptionDTOList.add(subscriptionVsUnsubscriptionDTO2);

        SubscriptionVsUnsubscriptionDTO subscriptionVsUnsubscriptionDTO3 = new SubscriptionVsUnsubscriptionDTO();
        subscriptionVsUnsubscriptionDTO3.setSubscriptionCount(totalNinetyDayNewSubscriptionCount);
        subscriptionVsUnsubscriptionDTO3.setUnsubscriptionCount(totalNinetyDayCanceledSubscriptionCount);
        subscriptionVsUnsubscriptionDTO3.setName("90 days subscriptionVsUnsubscription data");
        subscriptionVsUnsubscriptionDTOList.add(subscriptionVsUnsubscriptionDTO3);

        HashMap<String, Object> result = new HashMap<>();
        result.put("subscriptionVsUnsubscription", subscriptionVsUnsubscriptionDTOList);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /analytics/:id} : get the "id" analytics.
     *
     * @param id the id of the analyticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analyticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analytics/{id}")
    public ResponseEntity<AnalyticsDTO> getAnalytics(@PathVariable Long id) {
        log.debug("REST request to get Analytics : {}", id);
        Optional<AnalyticsDTO> analyticsDTO = analyticsService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (analyticsDTO.isPresent() && !shop.equals(analyticsDTO.get().getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseUtil.wrapOrNotFound(analyticsDTO);
    }

    /**
     * {@code DELETE  /analytics/:id} : delete the "id" analytics.
     *
     * @param id the id of the analyticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analytics/{id}")
    public ResponseEntity<Void> deleteAnalytics(@PathVariable Long id) {
        log.debug("REST request to delete Analytics : {}", id);

        Optional<AnalyticsDTO> analyticsDTO = analyticsService.findOne(id);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        if (analyticsDTO.isPresent() && !shop.equals(analyticsDTO.get().getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        analyticsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/conversion-analytics/subscriptions-analytics")
    public ResponseEntity<HashMap<String, Object>> getSubscriptionAmountAnalytics(@RequestParam String filterBy,
                                                                            @RequestParam Long days,
                                                                            @RequestParam ZonedDateTime fromDay,
                                                                            @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        String[] shopTimeZone = shopInfo.getShopTimeZone().split(" ");

        ZoneId zoneId = null;

        try {
            zoneId = ZoneId.of(shopTimeZone[1]);
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);

        Double totalSubscriptionAmount = analyticsService.getTotalSubscriptionAmount(shop, filterProperties);
        List<SubscriptionsTotalAmountByWeek> totalSubscriptionAmountSummary = analyticsService.subscriptionsTotalAmountByWeek(shop, filterProperties);

        Long totalSubscriptionCount = analyticsService.getTotalSubscriptionCountByStatus(shop, filterProperties,"active");
        List<SubscriptionsTotalByDate> totalSubscriptionCountSummary = analyticsService.subscriptionsCountByWeekAndStatus(shop, filterProperties,"active");

        CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

        HashMap<String, Object> result = new HashMap<>();
        result.put("totalSubscriptionAmount", totalSubscriptionAmount);
        result.put("totalSubscriptionAmountSummary", totalSubscriptionAmountSummary);

        result.put("totalSubscriptionCount", totalSubscriptionCount);
        result.put("totalSubscriptionCountSummary", totalSubscriptionCountSummary);
        String formattedTotalSoldAmount = currencyFormattingUtils.formatPrice(0);
        result.put("currency", formattedTotalSoldAmount);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conversion-analytics/subscriptions-canceled-analytics")
    public ResponseEntity<HashMap<String, Object>> getSubscriptionCanceledCountAnalytics(@RequestParam String filterBy,
                                                                            @RequestParam Long days,
                                                                            @RequestParam ZonedDateTime fromDay,
                                                                            @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        String[] shopTimeZone = shopInfo.getShopTimeZone().split(" ");

        ZoneId zoneId = null;

        try {
            zoneId = ZoneId.of(shopTimeZone[1]);
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);

        Long totalSubscriptionCanceledCount = analyticsService.getTotalSubscriptionCountByStatus(shop, filterProperties, "cancelled");
        List<SubscriptionsTotalByDate> totalSubscriptionCanceledCountSummary = analyticsService.subscriptionsCountByWeekAndStatus(shop, filterProperties, "cancelled");
        HashMap<String, Object> result = new HashMap<>();
        result.put("totalCanceledSubscriptionCount", totalSubscriptionCanceledCount);
        result.put("totalCanceledSubscriptionCountSummary", totalSubscriptionCanceledCountSummary);
        return ResponseEntity.ok().body(result);
    }



    @GetMapping("/conversion-analytics/subscriptions-order-analytics")
    public ResponseEntity<HashMap<String, Object>> getSubscriptionOrderAnalytics(@RequestParam String filterBy,
                                                                            @RequestParam Long days,
                                                                            @RequestParam ZonedDateTime fromDay,
                                                                            @RequestParam ZonedDateTime toDay) throws JsonProcessingException {
        log.debug("REST request to get a total order amount of ConversionAnalytics");

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

        String[] shopTimeZone = shopInfo.getShopTimeZone().split(" ");

        ZoneId zoneId = null;

        try {
            zoneId = ZoneId.of(shopTimeZone[1]);
        } catch (Exception ex) {
            zoneId = ZoneId.of("UTC");
        }

        FilterProperties filterProperties = seedsUtils.getFilterProperties(filterBy, days, fromDay, toDay, zoneId);

        Double totalOrderAmount = analyticsService.getTotalSoldAmountByDateRange(shop, filterProperties);
        List<OrderSummeryByDate> totalOrderAmountSummary = analyticsService.orderSummeryByWeek(shop, filterProperties, shopInfo.getCurrency());

        Long totalOrderCount = analyticsService.getTotalOrderCountByDateRange(shop, filterProperties);
        List<OrderSummeryByDate> totalOrderCountSummary = analyticsService.orderCountByWeek(shop, filterProperties, shopInfo.getCurrency());//TODO: need improvements on calculation
        HashMap<String, Object> result = new HashMap<>();

        result.put("totalOrderAmount", totalOrderAmount);
        result.put("totalOrderAmountSummary", totalOrderAmountSummary);

        result.put("totalOrderCount", totalOrderCount);
        result.put("totalOrderCountSummary", totalOrderCountSummary);
        return ResponseEntity.ok().body(result);
    }



}
