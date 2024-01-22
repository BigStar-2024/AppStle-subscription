package com.et.pojo;

import java.time.ZonedDateTime;
import java.util.List;

public class ExportInputRequest {
    private String shop;
    private String emailId;
    private ZonedDateTime fromCreatedDate;
    private ZonedDateTime toCreatedDate;
    private ZonedDateTime fromNextDate;
    private ZonedDateTime toNextDate;
    private String subscriptionContractId;
    private String customerName;
    private String orderName;
    private String status;
    private Integer billingPolicyIntervalCount;
    private String billingPolicyInterval;
    private String planType;
    private String recordType;

    private Long productId;

    private Long variantId;

    private List<String> sellingPlanIds;

    private Double minOrderAmount;

    private Double maxOrderAmount;

    private int pageSize;
    private int pageNumber;

    public ExportInputRequest(String shop, String emailId, ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate, ZonedDateTime fromNextDate, ZonedDateTime toNextDate, String subscriptionContractId, String customerName, String orderName, String status, Integer billingPolicyIntervalCount, String billingPolicyInterval, String planType, String recordType, Long productId, Long variantId, List<String> sellingPlanIds, Double minOrderAmount, Double maxOrderAmount, int pageSize, int pageNumber) {
        this.shop = shop;
        this.emailId = emailId;
        this.fromCreatedDate = fromCreatedDate;
        this.toCreatedDate = toCreatedDate;
        this.fromNextDate = fromNextDate;
        this.toNextDate = toNextDate;
        this.subscriptionContractId = subscriptionContractId;
        this.customerName = customerName;
        this.orderName = orderName;
        this.status = status;
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
        this.billingPolicyInterval = billingPolicyInterval;
        this.planType = planType;
        this.recordType = recordType;
        this.productId = productId;
        this.variantId = variantId;
        this.sellingPlanIds = sellingPlanIds;
        this.minOrderAmount = minOrderAmount;
        this.maxOrderAmount = maxOrderAmount;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public ExportInputRequest() {

    }

    public String getShop() {
        return shop;
    }

    public String getEmailId() {
        return emailId;
    }

    public ZonedDateTime getFromCreatedDate() {
        return fromCreatedDate;
    }

    public ZonedDateTime getToCreatedDate() {
        return toCreatedDate;
    }

    public ZonedDateTime getFromNextDate() {
        return fromNextDate;
    }

    public ZonedDateTime getToNextDate() {
        return toNextDate;
    }

    public String getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getStatus() {
        return status;
    }

    public Integer getBillingPolicyIntervalCount() {
        return billingPolicyIntervalCount;
    }

    public String getBillingPolicyInterval() {
        return billingPolicyInterval;
    }

    public String getPlanType() {
        return planType;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFromCreatedDate(ZonedDateTime fromCreatedDate) {
        this.fromCreatedDate = fromCreatedDate;
    }

    public void setToCreatedDate(ZonedDateTime toCreatedDate) {
        this.toCreatedDate = toCreatedDate;
    }

    public void setFromNextDate(ZonedDateTime fromNextDate) {
        this.fromNextDate = fromNextDate;
    }

    public void setToNextDate(ZonedDateTime toNextDate) {
        this.toNextDate = toNextDate;
    }

    public void setSubscriptionContractId(String subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBillingPolicyIntervalCount(Integer billingPolicyIntervalCount) {
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
    }

    public void setBillingPolicyInterval(String billingPolicyInterval) {
        this.billingPolicyInterval = billingPolicyInterval;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public List<String> getSellingPlanIds() {
        return sellingPlanIds;
    }

    public void setSellingPlanIds(List<String> sellingPlanIds) {
        this.sellingPlanIds = sellingPlanIds;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Double getMaxOrderAmount() {
        return maxOrderAmount;
    }

    public void setMaxOrderAmount(Double maxOrderAmount) {
        this.maxOrderAmount = maxOrderAmount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
