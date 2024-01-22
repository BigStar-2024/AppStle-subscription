package com.et.liquid;

public class SummaryReportEmailModel {

    private String summaryDate;
    private String shopName;
    private Long newSubscriptionCount;
    private Long newRecurringOrderCount;
    private String totalSubscriptionAmount;
    private String recurringOrderAmount;
    private Long totalCanceledSubscriptionCount;
    private Long totalPausedSubscriptionCount;
    private Long totalCustomerCount;
    private String averageOrderValue;
    private Double subscriptionGrowthMonthOverMonth;
    private Double revenueGrowthMonthOverMonth;
    private Double churnRate;
    private Long totalSubscriptionCount;
    private Long totalActiveSubscriptionCount;
    private Long totalFailedPaymentsCount;
    private Double averageSubscriptionValue;

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getNewSubscriptionCount() {
        return newSubscriptionCount;
    }

    public void setNewSubscriptionCount(Long newSubscriptionCount) {
        this.newSubscriptionCount = newSubscriptionCount;
    }

    public Long getNewRecurringOrderCount() {
        return newRecurringOrderCount;
    }

    public void setNewRecurringOrderCount(Long newRecurringOrderCount) {
        this.newRecurringOrderCount = newRecurringOrderCount;
    }

    public String getTotalSubscriptionAmount() {
        return totalSubscriptionAmount;
    }

    public void setTotalSubscriptionAmount(String totalSubscriptionAmount) {
        this.totalSubscriptionAmount = totalSubscriptionAmount;
    }

    public String getRecurringOrderAmount() {
        return recurringOrderAmount;
    }

    public void setRecurringOrderAmount(String recurringOrderAmount) {
        this.recurringOrderAmount = recurringOrderAmount;
    }

    public Long getTotalCanceledSubscriptionCount() {
        return totalCanceledSubscriptionCount;
    }

    public void setTotalCanceledSubscriptionCount(Long totalCanceledSubscriptionCount) {
        this.totalCanceledSubscriptionCount = totalCanceledSubscriptionCount;
    }

    public Long getTotalPausedSubscriptionCount() {
        return totalPausedSubscriptionCount;
    }

    public void setTotalPausedSubscriptionCount(Long totalPausedSubscriptionCount) {
        this.totalPausedSubscriptionCount = totalPausedSubscriptionCount;
    }

    public Long getTotalCustomerCount() {
        return totalCustomerCount;
    }

    public void setTotalCustomerCount(Long totalCustomerCount) {
        this.totalCustomerCount = totalCustomerCount;
    }

    public String getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(String averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public Double getSubscriptionGrowthMonthOverMonth() {
        return subscriptionGrowthMonthOverMonth;
    }

    public void setSubscriptionGrowthMonthOverMonth(Double subscriptionGrowthMonthOverMonth) {
        this.subscriptionGrowthMonthOverMonth = subscriptionGrowthMonthOverMonth;
    }

    public Double getRevenueGrowthMonthOverMonth() {
        return revenueGrowthMonthOverMonth;
    }

    public void setRevenueGrowthMonthOverMonth(Double revenueGrowthMonthOverMonth) {
        this.revenueGrowthMonthOverMonth = revenueGrowthMonthOverMonth;
    }

    public Double getChurnRate() {
        return churnRate;
    }

    public void setChurnRate(Double churnRate) {
        this.churnRate = churnRate;
    }

    public Long getTotalSubscriptionCount() {
        return totalSubscriptionCount;
    }

    public void setTotalSubscriptionCount(Long totalSubscriptionCount) {
        this.totalSubscriptionCount = totalSubscriptionCount;
    }

    public Long getTotalActiveSubscriptionCount() {
        return totalActiveSubscriptionCount;
    }

    public void setTotalActiveSubscriptionCount(Long totalActiveSubscriptionCount) {
        this.totalActiveSubscriptionCount = totalActiveSubscriptionCount;
    }

    public Long getTotalFailedPaymentsCount() {
        return totalFailedPaymentsCount;
    }

    public void setTotalFailedPaymentsCount(Long totalFailedPaymentsCount) {
        this.totalFailedPaymentsCount = totalFailedPaymentsCount;
    }

    public Double getAverageSubscriptionValue() {
        return averageSubscriptionValue;
    }

    public void setAverageSubscriptionValue(Double averageSubscriptionValue) {
        this.averageSubscriptionValue = averageSubscriptionValue;
    }
}
