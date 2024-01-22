package com.et.service;

import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.et.api.utils.CurrencyFormattingUtils;
import com.et.domain.ActivityUpdatesSettings;
import com.et.domain.ShopInfo;
import com.et.domain.enumeration.SummaryReportFrequencyUnit;
import com.et.liquid.LiquidUtils;
import com.et.liquid.SummaryReportEmailModel;
import com.et.repository.ShopInfoRepository;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
@Transactional
public class SummaryReportService {

    private final Logger log = LoggerFactory.getLogger(SummaryReportService.class);

    @Value("classpath:summary-report-email-template.liquid")
    Resource summaryReportEmailTemplate;

    @Autowired
    private LiquidUtils liquidUtils;

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    public void sendActivityReports(List<ActivityUpdatesSettings> activityUpdatesSettings) {
        for (ActivityUpdatesSettings activityUpdatesSetting : activityUpdatesSettings) {
            String shop = activityUpdatesSetting.getShop();
            try {
                MDC.put("shop", shop);

                ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();

                CurrencyFormattingUtils currencyFormattingUtils = new CurrencyFormattingUtils(shopInfo.getMoneyFormat(), shopInfo.getCurrency());

                ZonedDateTime from = ZonedDateTime.now();
                if(activityUpdatesSetting.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.DAILY)) {
                    from = from.minusDays(1);
                } else if(activityUpdatesSetting.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.WEEKLY)) {
                    from = from.minusDays(7);
                } else if(activityUpdatesSetting.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.MONTHLY)) {
                    from = from.minusDays(30);
                } else if(activityUpdatesSetting.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.QUARTERLY)) {
                    from = from.minusDays(90);
                }

                ZonedDateTime now = ZonedDateTime.now();

                Long newSubscriptionCount = subscriptionContractDetailsRepository.countActiveSubscription(shop, from, now).orElse(0L);
                Double totalSoldAmountFirstOrder = subscriptionContractDetailsRepository.getTotalSoldAmountByDateRange(shop, from, now).orElse(0D);
                Double totalSoldAmountRecurringOrder = subscriptionBillingAttemptRepository.getTotalSoldAmountByDateRange(shop, from, now).orElse(0D);
                Long newRecurringCount = subscriptionBillingAttemptRepository.countNewRecurringOrders(shop, from, now).orElse(0L);
                Long totalCanceledSubscriptionCount = subscriptionContractDetailsRepository.getTotalCanceledSubscriptionCountByDateRange(shop, from, now).orElse(0L);
                Long totalPausedSubscriptionCount = subscriptionContractDetailsRepository.getTotalPausedSubscriptionCountByDateRange(shop, from, now).orElse(0L);
                Long totalCustomerCount = subscriptionContractDetailsRepository.getTotalCustomerCountByDateRange(shop, from, now).orElse(0L);
                Double averageOrderValue = getAverageOrderValue(shop, from, now);
                Double subscriptionGrowthMonthOverMonth = getSubscriptionGrowthMonthOverMonth(shop, from, now);
                Double revenueGrowthMonthOverMonth = getRevenueGrowthMonthOverMonth(shop, from, now);
                Double churnRate = getChurnRate(shop, from, now);
                Long totalSubscriptionCount = subscriptionContractDetailsRepository.getTotalSubscriptionCount(shop, from, now).orElse(0L);
                Long totalActiveSubscriptionCount = subscriptionContractDetailsRepository.getTotalActiveSubscriptionCount(shop, from, now).orElse(0L);
                Long totalFailedPaymentsCount = subscriptionBillingAttemptRepository.getTotalFailedPaymentsCount(shop, from, now).orElse(0L);
                Double averageSubscriptionValue = subscriptionContractDetailsRepository.getAverageSubscriptionValue(shop, from, now).orElse(0D);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                String formattedString = ZonedDateTime.now().format(formatter);

                SummaryReportEmailModel emailModel = new SummaryReportEmailModel();

                emailModel.setSummaryDate(formattedString);
                emailModel.setShopName(shop);
                emailModel.setNewSubscriptionCount(newSubscriptionCount);
                emailModel.setNewRecurringOrderCount(newRecurringCount);
                emailModel.setRecurringOrderAmount(currencyFormattingUtils.formatPrice(totalSoldAmountRecurringOrder));
                emailModel.setTotalSubscriptionAmount(currencyFormattingUtils.formatPrice(totalSoldAmountFirstOrder));
                emailModel.setAverageOrderValue(currencyFormattingUtils.formatPrice(averageOrderValue));
                emailModel.setChurnRate(churnRate);
                emailModel.setRevenueGrowthMonthOverMonth(revenueGrowthMonthOverMonth);
                emailModel.setSubscriptionGrowthMonthOverMonth(subscriptionGrowthMonthOverMonth);
                emailModel.setTotalCustomerCount(totalCustomerCount);
                emailModel.setTotalCanceledSubscriptionCount(totalCanceledSubscriptionCount);
                emailModel.setTotalPausedSubscriptionCount(totalPausedSubscriptionCount);
                emailModel.setTotalSubscriptionCount(totalSubscriptionCount);
                emailModel.setTotalActiveSubscriptionCount(totalActiveSubscriptionCount);
                emailModel.setTotalFailedPaymentsCount(totalFailedPaymentsCount);
                emailModel.setAverageSubscriptionValue(averageSubscriptionValue);

                String emailTemplate = IOUtils.toString(summaryReportEmailTemplate.getInputStream());

                String htmlBody = liquidUtils.getValue(emailModel, emailTemplate);

                String toEmail = activityUpdatesSetting.getSummaryReportDeliverToEmail();
                List<String> emailToBeSent = new ArrayList<>();
                if (!StringUtils.isNullOrEmpty(toEmail)) {
                    emailToBeSent = Arrays.asList(toEmail.split(","));
                }

                if (emailToBeSent.size() > 0) {
                    emailToBeSent.forEach(email -> {
                        try {
                            mailgunService.sendRawEmail(email, "Summary Report", htmlBody, shop, "subscription-support@appstle.com");
                        } catch (Exception e) {
                            log.error("Error sending summary report shop=" + shop  + " ex=" + ExceptionUtils.getStackTrace(e), e);
                        }
                    });
                }


            } catch (Exception ex) {
                log.error("An error occurred while sending summary report shop data. shop=" + shop + " ex="+ ExceptionUtils.getStackTrace(ex), ex);
            } finally {
                MDC.clear();
            }
        }
    }

    private Double getChurnRate(String shop, ZonedDateTime from, ZonedDateTime now) {
        Long cancelledSubscriptionsBetween = subscriptionContractDetailsRepository.getTotalCancelledButCreatedBeforeDateRange(shop, from, now).orElse(0L);
        Long totalActiveSubscriptionsCreatedBefore = subscriptionContractDetailsRepository.getTotalSubscriptionsCreatedBefore(shop, from).orElse(0L);

        if (totalActiveSubscriptionsCreatedBefore == 0) {
            return 0D;
        }

        return round(((double) cancelledSubscriptionsBetween / (double) totalActiveSubscriptionsCreatedBefore) * 100);
    }

    private Double getRevenueGrowthMonthOverMonth(String shop, ZonedDateTime from, ZonedDateTime now) {
        Optional<Double> currentMonthRevenueCount = subscriptionBillingAttemptRepository.getTotalMonthoverMonthRevenueCountByDateRange(shop, now.minusDays(30), now);
        Optional<Double> pastMonthRevenueCount = subscriptionBillingAttemptRepository.getTotalMonthoverMonthRevenueCountByDateRange(shop, now.minusDays(60), now.minusDays(30));
        Double revenueGrowthMonthOverMonth = currentMonthRevenueCount.orElse(0d) - pastMonthRevenueCount.orElse(0d);
        Double revenueGrowthMonthOverMonthPercentage = null;
        if(pastMonthRevenueCount.orElse(0d) != 0d){
            revenueGrowthMonthOverMonthPercentage = Double.valueOf(revenueGrowthMonthOverMonth * 100/ pastMonthRevenueCount.orElse(0d));
        }
        if(revenueGrowthMonthOverMonthPercentage == null) {
            return 0D;
        } else{
            return revenueGrowthMonthOverMonthPercentage;
        }
    }

    private Double getSubscriptionGrowthMonthOverMonth(String shop, ZonedDateTime from, ZonedDateTime now) {
        Optional<Long> currentMonthSubscriptionCount = subscriptionContractDetailsRepository.getTotalMonthoverMonthSubscriptionCountByDateRange(shop, now.minusDays(30), now);
        Optional<Long> pastMonthSubscriptionCount = subscriptionContractDetailsRepository.getTotalMonthoverMonthSubscriptionCountByDateRange(shop, now.minusDays(60), now.minusDays(30));
        Long subscriptionGrowthMonthOverMonth = currentMonthSubscriptionCount.orElse(0L) - pastMonthSubscriptionCount.orElse(0L);
        Double subscriptionGrowthMonthOverMonthPercentage = null;
        if(pastMonthSubscriptionCount.orElse(0L) != 0L){
            subscriptionGrowthMonthOverMonthPercentage =  Double.valueOf(subscriptionGrowthMonthOverMonth * 100/ pastMonthSubscriptionCount.orElse(0L));
        }
        if(subscriptionGrowthMonthOverMonthPercentage == null) {
            return 0D;
        } else{
            return subscriptionGrowthMonthOverMonthPercentage;
        }
    }

    private Double getAverageOrderValue(String shop, ZonedDateTime from, ZonedDateTime now) {
        Optional<Double> oneTimeOrderAmount = subscriptionContractDetailsRepository.getTotalSoldRevenueByDateRange(shop, from, now);
        Optional<Double> recurringOrderAmount = subscriptionBillingAttemptRepository.getTotalSoldRevenueByDateRange(shop, from, now);
        Optional<Long> oneTimeOrderCount = subscriptionContractDetailsRepository.getTotalSoldAmountByDateRangeAVG(shop, from, now);
        Optional<Long> recurringOrderCount = subscriptionBillingAttemptRepository.getTotalSoldAmountByDateRangeAVG(shop, from, now);
        Double totalAmount = oneTimeOrderAmount.orElse(0D) + recurringOrderAmount.orElse(0D);
        Long totalOrder = oneTimeOrderCount.orElse(0L) + recurringOrderCount.orElse(0L);
        double averageValue = (totalAmount / totalOrder);
        if (Double.isNaN(averageValue)) {
            averageValue = 0D;
        }
        return averageValue;
    }

    private double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }
}
