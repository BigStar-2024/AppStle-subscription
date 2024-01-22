package com.et.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.BillingAttemptStatus;
import com.et.domain.enumeration.transactionFailedEmailSentStatus;
import com.et.domain.enumeration.upcomingOrderEmailSentStatus;
import com.et.domain.enumeration.UsageChargeStatus;
import com.et.domain.enumeration.TransactionFailedSmsSentStatus;
import com.et.domain.enumeration.UpcomingOrderSmsSentStatus;
import com.et.domain.enumeration.SecurityChallengeSentStatus;
import com.et.web.rest.vm.VariantQuantity;
import com.shopify.java.graphql.client.type.OrderCancelReason;
import com.shopify.java.graphql.client.type.OrderDisplayFinancialStatus;
import com.shopify.java.graphql.client.type.OrderDisplayFulfillmentStatus;

/**
 * A DTO for the {@link com.et.domain.SubscriptionBillingAttempt} entity.
 */
public class SubscriptionBillingAttemptDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private String billingAttemptId;

    private BillingAttemptStatus status;

    private ZonedDateTime billingDate;

    private Long contractId;

    private Integer attemptCount;

    private ZonedDateTime attemptTime;

    private String graphOrderId;

    private Long orderId;

    private Double orderAmount;

    private String orderName;

    @NotNull
    private Boolean retryingNeeded;

    private transactionFailedEmailSentStatus transactionFailedEmailSentStatus;

    private upcomingOrderEmailSentStatus upcomingOrderEmailSentStatus;

    private Boolean applyUsageCharge;

    private Long recurringChargeId;

    private Double transactionRate;

    private UsageChargeStatus usageChargeStatus;

    private TransactionFailedSmsSentStatus transactionFailedSmsSentStatus;

    private UpcomingOrderSmsSentStatus upcomingOrderSmsSentStatus;

    @Lob
    private String billingAttemptResponseMessage;

    private Integer progressAttemptCount;

    @Lob
    private String orderNote;

    private List<VariantQuantity> variantList = new ArrayList<>();

    private SecurityChallengeSentStatus securityChallengeSentStatus;

    private Double orderAmountUSD;

    private OrderCancelReason orderCancelReason;

    private ZonedDateTime orderCancelledAt;

    private Boolean orderClosed;

    private ZonedDateTime orderClosedAt;

    private Boolean orderConfirmed;

    private OrderDisplayFinancialStatus orderDisplayFinancialStatus;

    private OrderDisplayFulfillmentStatus orderDisplayFulfillmentStatus;

    private ZonedDateTime orderProcessedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getBillingAttemptId() {
        return billingAttemptId;
    }

    public void setBillingAttemptId(String billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
    }

    public BillingAttemptStatus getStatus() {
        return status;
    }

    public void setStatus(BillingAttemptStatus status) {
        this.status = status;
    }

    public ZonedDateTime getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(ZonedDateTime billingDate) {
        this.billingDate = billingDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTime getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(ZonedDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getGraphOrderId() {
        return graphOrderId;
    }

    public void setGraphOrderId(String graphOrderId) {
        this.graphOrderId = graphOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Boolean isRetryingNeeded() {
        return retryingNeeded;
    }

    public void setRetryingNeeded(Boolean retryingNeeded) {
        this.retryingNeeded = retryingNeeded;
    }

    public transactionFailedEmailSentStatus getTransactionFailedEmailSentStatus() {
        return transactionFailedEmailSentStatus;
    }

    public void setTransactionFailedEmailSentStatus(transactionFailedEmailSentStatus transactionFailedEmailSentStatus) {
        this.transactionFailedEmailSentStatus = transactionFailedEmailSentStatus;
    }

    public upcomingOrderEmailSentStatus getUpcomingOrderEmailSentStatus() {
        return upcomingOrderEmailSentStatus;
    }

    public void setUpcomingOrderEmailSentStatus(upcomingOrderEmailSentStatus upcomingOrderEmailSentStatus) {
        this.upcomingOrderEmailSentStatus = upcomingOrderEmailSentStatus;
    }

    public Boolean isApplyUsageCharge() {
        return applyUsageCharge;
    }

    public void setApplyUsageCharge(Boolean applyUsageCharge) {
        this.applyUsageCharge = applyUsageCharge;
    }

    public Long getRecurringChargeId() {
        return recurringChargeId;
    }

    public void setRecurringChargeId(Long recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
    }

    public Double getTransactionRate() {
        return transactionRate;
    }

    public void setTransactionRate(Double transactionRate) {
        this.transactionRate = transactionRate;
    }

    public UsageChargeStatus getUsageChargeStatus() {
        return usageChargeStatus;
    }

    public void setUsageChargeStatus(UsageChargeStatus usageChargeStatus) {
        this.usageChargeStatus = usageChargeStatus;
    }

    public TransactionFailedSmsSentStatus getTransactionFailedSmsSentStatus() {
        return transactionFailedSmsSentStatus;
    }

    public void setTransactionFailedSmsSentStatus(TransactionFailedSmsSentStatus transactionFailedSmsSentStatus) {
        this.transactionFailedSmsSentStatus = transactionFailedSmsSentStatus;
    }

    public UpcomingOrderSmsSentStatus getUpcomingOrderSmsSentStatus() {
        return upcomingOrderSmsSentStatus;
    }

    public void setUpcomingOrderSmsSentStatus(UpcomingOrderSmsSentStatus upcomingOrderSmsSentStatus) {
        this.upcomingOrderSmsSentStatus = upcomingOrderSmsSentStatus;
    }

    public String getBillingAttemptResponseMessage() {
        return billingAttemptResponseMessage;
    }

    public void setBillingAttemptResponseMessage(String billingAttemptResponseMessage) {
        this.billingAttemptResponseMessage = billingAttemptResponseMessage;
    }

    public Integer getProgressAttemptCount() {
        return progressAttemptCount;
    }

    public void setProgressAttemptCount(Integer progressAttemptCount) {
        this.progressAttemptCount = progressAttemptCount;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public void setVariantList(List<VariantQuantity> variantList) {
        this.variantList = variantList;
    }

    public List<VariantQuantity> getVariantList() {
        return variantList;
    }

    public SecurityChallengeSentStatus getSecurityChallengeSentStatus() {
        return securityChallengeSentStatus;
    }

    public void setSecurityChallengeSentStatus(SecurityChallengeSentStatus securityChallengeSentStatus) {
        this.securityChallengeSentStatus = securityChallengeSentStatus;
    }

    public Double getOrderAmountUSD() {
        return orderAmountUSD;
    }

    public void setOrderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
    }

    public OrderCancelReason getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(OrderCancelReason orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public ZonedDateTime getOrderCancelledAt() {
        return orderCancelledAt;
    }

    public void setOrderCancelledAt(ZonedDateTime orderCancelledAt) {
        this.orderCancelledAt = orderCancelledAt;
    }

    public Boolean getOrderClosed() {
        return orderClosed;
    }

    public void setOrderClosed(Boolean orderClosed) {
        this.orderClosed = orderClosed;
    }

    public ZonedDateTime getOrderClosedAt() {
        return orderClosedAt;
    }

    public void setOrderClosedAt(ZonedDateTime orderClosedAt) {
        this.orderClosedAt = orderClosedAt;
    }

    public Boolean getOrderConfirmed() {
        return orderConfirmed;
    }

    public void setOrderConfirmed(Boolean orderConfirmed) {
        this.orderConfirmed = orderConfirmed;
    }

    public OrderDisplayFinancialStatus getOrderDisplayFinancialStatus() {
        return orderDisplayFinancialStatus;
    }

    public void setOrderDisplayFinancialStatus(OrderDisplayFinancialStatus orderDisplayFinancialStatus) {
        this.orderDisplayFinancialStatus = orderDisplayFinancialStatus;
    }

    public OrderDisplayFulfillmentStatus getOrderDisplayFulfillmentStatus() {
        return orderDisplayFulfillmentStatus;
    }

    public void setOrderDisplayFulfillmentStatus(OrderDisplayFulfillmentStatus orderDisplayFulfillmentStatus) {
        this.orderDisplayFulfillmentStatus = orderDisplayFulfillmentStatus;
    }

    public ZonedDateTime getOrderProcessedAt() {
        return orderProcessedAt;
    }

    public void setOrderProcessedAt(ZonedDateTime orderProcessedAt) {
        this.orderProcessedAt = orderProcessedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO = (SubscriptionBillingAttemptDTO) o;
        if (subscriptionBillingAttemptDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subscriptionBillingAttemptDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubscriptionBillingAttemptDTO{" +
            "id=" + id +
            ", shop='" + shop + '\'' +
            ", billingAttemptId='" + billingAttemptId + '\'' +
            ", status=" + status +
            ", billingDate=" + billingDate +
            ", contractId=" + contractId +
            ", attemptCount=" + attemptCount +
            ", attemptTime=" + attemptTime +
            ", graphOrderId='" + graphOrderId + '\'' +
            ", orderId=" + orderId +
            ", orderAmount=" + orderAmount +
            ", orderName='" + orderName + '\'' +
            ", retryingNeeded=" + retryingNeeded +
            ", transactionFailedEmailSentStatus=" + transactionFailedEmailSentStatus +
            ", upcomingOrderEmailSentStatus=" + upcomingOrderEmailSentStatus +
            ", applyUsageCharge=" + applyUsageCharge +
            ", recurringChargeId=" + recurringChargeId +
            ", transactionRate=" + transactionRate +
            ", usageChargeStatus=" + usageChargeStatus +
            ", transactionFailedSmsSentStatus=" + transactionFailedSmsSentStatus +
            ", upcomingOrderSmsSentStatus=" + upcomingOrderSmsSentStatus +
            ", billingAttemptResponseMessage='" + billingAttemptResponseMessage + '\'' +
            ", progressAttemptCount=" + progressAttemptCount +
            ", orderNote='" + orderNote + '\'' +
            ", variantList=" + variantList +
            ", securityChallengeSentStatus=" + securityChallengeSentStatus +
            ", orderAmountUSD=" + orderAmountUSD +
            ", orderCancelReason=" + orderCancelReason +
            ", orderCancelledAt=" + orderCancelledAt +
            ", orderClosed=" + orderClosed +
            ", orderClosedAt=" + orderClosedAt +
            ", orderConfirmed=" + orderConfirmed +
            ", orderDisplayFinancialStatus=" + orderDisplayFinancialStatus +
            ", orderDisplayFulfillmentStatus=" + orderDisplayFulfillmentStatus +
            ", orderProcessedAt=" + orderProcessedAt +
            '}';
    }
}
