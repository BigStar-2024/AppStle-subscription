package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.BillingAttemptStatus;

import com.et.domain.enumeration.transactionFailedEmailSentStatus;

import com.et.domain.enumeration.upcomingOrderEmailSentStatus;

import com.et.domain.enumeration.UsageChargeStatus;

import com.et.domain.enumeration.TransactionFailedSmsSentStatus;

import com.et.domain.enumeration.UpcomingOrderSmsSentStatus;

import com.et.domain.enumeration.SecurityChallengeSentStatus;

/**
 * A SubscriptionBillingAttempt.
 */
@Entity
@Table(name = "subscription_billing_attempt")
public class SubscriptionBillingAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "billing_attempt_id")
    private String billingAttemptId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillingAttemptStatus status;

    @Column(name = "billing_date")
    private ZonedDateTime billingDate;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "attempt_count")
    private Integer attemptCount;

    @Column(name = "attempt_time")
    private ZonedDateTime attemptTime;

    @Column(name = "graph_order_id")
    private String graphOrderId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_amount")
    private Double orderAmount;

    @Column(name = "order_name")
    private String orderName;

    @NotNull
    @Column(name = "retrying_needed", nullable = false)
    private Boolean retryingNeeded;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_failed_email_sent_status")
    private transactionFailedEmailSentStatus transactionFailedEmailSentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "upcoming_order_email_sent_status")
    private upcomingOrderEmailSentStatus upcomingOrderEmailSentStatus;

    @Column(name = "apply_usage_charge")
    private Boolean applyUsageCharge;

    @Column(name = "recurring_charge_id")
    private Long recurringChargeId;

    @Column(name = "transaction_rate")
    private Double transactionRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_charge_status")
    private UsageChargeStatus usageChargeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_failed_sms_sent_status")
    private TransactionFailedSmsSentStatus transactionFailedSmsSentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "upcoming_order_sms_sent_status")
    private UpcomingOrderSmsSentStatus upcomingOrderSmsSentStatus;

    @Lob
    @Column(name = "billing_attempt_response_message")
    private String billingAttemptResponseMessage;

    @Column(name = "progress_attempt_count")
    private Integer progressAttemptCount;

    @Lob
    @Column(name = "order_note")
    private String orderNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_challenge_sent_status")
    private SecurityChallengeSentStatus securityChallengeSentStatus;

    @Column(name = "order_amount_usd")
    private Double orderAmountUSD;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public SubscriptionBillingAttempt shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getBillingAttemptId() {
        return billingAttemptId;
    }

    public SubscriptionBillingAttempt billingAttemptId(String billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
        return this;
    }

    public void setBillingAttemptId(String billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
    }

    public BillingAttemptStatus getStatus() {
        return status;
    }

    public SubscriptionBillingAttempt status(BillingAttemptStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BillingAttemptStatus status) {
        this.status = status;
    }

    public ZonedDateTime getBillingDate() {
        return billingDate;
    }

    public SubscriptionBillingAttempt billingDate(ZonedDateTime billingDate) {
        this.billingDate = billingDate;
        return this;
    }

    public void setBillingDate(ZonedDateTime billingDate) {
        this.billingDate = billingDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public SubscriptionBillingAttempt contractId(Long contractId) {
        this.contractId = contractId;
        return this;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public SubscriptionBillingAttempt attemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
        return this;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTime getAttemptTime() {
        return attemptTime;
    }

    public SubscriptionBillingAttempt attemptTime(ZonedDateTime attemptTime) {
        this.attemptTime = attemptTime;
        return this;
    }

    public void setAttemptTime(ZonedDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getGraphOrderId() {
        return graphOrderId;
    }

    public SubscriptionBillingAttempt graphOrderId(String graphOrderId) {
        this.graphOrderId = graphOrderId;
        return this;
    }

    public void setGraphOrderId(String graphOrderId) {
        this.graphOrderId = graphOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public SubscriptionBillingAttempt orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public SubscriptionBillingAttempt orderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderName() {
        return orderName;
    }

    public SubscriptionBillingAttempt orderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Boolean isRetryingNeeded() {
        return retryingNeeded;
    }

    public SubscriptionBillingAttempt retryingNeeded(Boolean retryingNeeded) {
        this.retryingNeeded = retryingNeeded;
        return this;
    }

    public void setRetryingNeeded(Boolean retryingNeeded) {
        this.retryingNeeded = retryingNeeded;
    }

    public transactionFailedEmailSentStatus getTransactionFailedEmailSentStatus() {
        return transactionFailedEmailSentStatus;
    }

    public SubscriptionBillingAttempt transactionFailedEmailSentStatus(transactionFailedEmailSentStatus transactionFailedEmailSentStatus) {
        this.transactionFailedEmailSentStatus = transactionFailedEmailSentStatus;
        return this;
    }

    public void setTransactionFailedEmailSentStatus(transactionFailedEmailSentStatus transactionFailedEmailSentStatus) {
        this.transactionFailedEmailSentStatus = transactionFailedEmailSentStatus;
    }

    public upcomingOrderEmailSentStatus getUpcomingOrderEmailSentStatus() {
        return upcomingOrderEmailSentStatus;
    }

    public SubscriptionBillingAttempt upcomingOrderEmailSentStatus(upcomingOrderEmailSentStatus upcomingOrderEmailSentStatus) {
        this.upcomingOrderEmailSentStatus = upcomingOrderEmailSentStatus;
        return this;
    }

    public void setUpcomingOrderEmailSentStatus(upcomingOrderEmailSentStatus upcomingOrderEmailSentStatus) {
        this.upcomingOrderEmailSentStatus = upcomingOrderEmailSentStatus;
    }

    public Boolean isApplyUsageCharge() {
        return applyUsageCharge;
    }

    public SubscriptionBillingAttempt applyUsageCharge(Boolean applyUsageCharge) {
        this.applyUsageCharge = applyUsageCharge;
        return this;
    }

    public void setApplyUsageCharge(Boolean applyUsageCharge) {
        this.applyUsageCharge = applyUsageCharge;
    }

    public Long getRecurringChargeId() {
        return recurringChargeId;
    }

    public SubscriptionBillingAttempt recurringChargeId(Long recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
        return this;
    }

    public void setRecurringChargeId(Long recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
    }

    public Double getTransactionRate() {
        return transactionRate;
    }

    public SubscriptionBillingAttempt transactionRate(Double transactionRate) {
        this.transactionRate = transactionRate;
        return this;
    }

    public void setTransactionRate(Double transactionRate) {
        this.transactionRate = transactionRate;
    }

    public UsageChargeStatus getUsageChargeStatus() {
        return usageChargeStatus;
    }

    public SubscriptionBillingAttempt usageChargeStatus(UsageChargeStatus usageChargeStatus) {
        this.usageChargeStatus = usageChargeStatus;
        return this;
    }

    public void setUsageChargeStatus(UsageChargeStatus usageChargeStatus) {
        this.usageChargeStatus = usageChargeStatus;
    }

    public TransactionFailedSmsSentStatus getTransactionFailedSmsSentStatus() {
        return transactionFailedSmsSentStatus;
    }

    public SubscriptionBillingAttempt transactionFailedSmsSentStatus(TransactionFailedSmsSentStatus transactionFailedSmsSentStatus) {
        this.transactionFailedSmsSentStatus = transactionFailedSmsSentStatus;
        return this;
    }

    public void setTransactionFailedSmsSentStatus(TransactionFailedSmsSentStatus transactionFailedSmsSentStatus) {
        this.transactionFailedSmsSentStatus = transactionFailedSmsSentStatus;
    }

    public UpcomingOrderSmsSentStatus getUpcomingOrderSmsSentStatus() {
        return upcomingOrderSmsSentStatus;
    }

    public SubscriptionBillingAttempt upcomingOrderSmsSentStatus(UpcomingOrderSmsSentStatus upcomingOrderSmsSentStatus) {
        this.upcomingOrderSmsSentStatus = upcomingOrderSmsSentStatus;
        return this;
    }

    public void setUpcomingOrderSmsSentStatus(UpcomingOrderSmsSentStatus upcomingOrderSmsSentStatus) {
        this.upcomingOrderSmsSentStatus = upcomingOrderSmsSentStatus;
    }

    public String getBillingAttemptResponseMessage() {
        return billingAttemptResponseMessage;
    }

    public SubscriptionBillingAttempt billingAttemptResponseMessage(String billingAttemptResponseMessage) {
        this.billingAttemptResponseMessage = billingAttemptResponseMessage;
        return this;
    }

    public void setBillingAttemptResponseMessage(String billingAttemptResponseMessage) {
        this.billingAttemptResponseMessage = billingAttemptResponseMessage;
    }

    public Integer getProgressAttemptCount() {
        return progressAttemptCount;
    }

    public SubscriptionBillingAttempt progressAttemptCount(Integer progressAttemptCount) {
        this.progressAttemptCount = progressAttemptCount;
        return this;
    }

    public void setProgressAttemptCount(Integer progressAttemptCount) {
        this.progressAttemptCount = progressAttemptCount;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public SubscriptionBillingAttempt orderNote(String orderNote) {
        this.orderNote = orderNote;
        return this;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public SecurityChallengeSentStatus getSecurityChallengeSentStatus() {
        return securityChallengeSentStatus;
    }

    public SubscriptionBillingAttempt securityChallengeSentStatus(SecurityChallengeSentStatus securityChallengeSentStatus) {
        this.securityChallengeSentStatus = securityChallengeSentStatus;
        return this;
    }

    public void setSecurityChallengeSentStatus(SecurityChallengeSentStatus securityChallengeSentStatus) {
        this.securityChallengeSentStatus = securityChallengeSentStatus;
    }

    public Double getOrderAmountUSD() {
        return orderAmountUSD;
    }

    public SubscriptionBillingAttempt orderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
        return this;
    }

    public void setOrderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBillingAttempt)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionBillingAttempt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionBillingAttempt{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", billingAttemptId='" + getBillingAttemptId() + "'" +
            ", status='" + getStatus() + "'" +
            ", billingDate='" + getBillingDate() + "'" +
            ", contractId=" + getContractId() +
            ", attemptCount=" + getAttemptCount() +
            ", attemptTime='" + getAttemptTime() + "'" +
            ", graphOrderId='" + getGraphOrderId() + "'" +
            ", orderId=" + getOrderId() +
            ", orderAmount=" + getOrderAmount() +
            ", orderName='" + getOrderName() + "'" +
            ", retryingNeeded='" + isRetryingNeeded() + "'" +
            ", transactionFailedEmailSentStatus='" + getTransactionFailedEmailSentStatus() + "'" +
            ", upcomingOrderEmailSentStatus='" + getUpcomingOrderEmailSentStatus() + "'" +
            ", applyUsageCharge='" + isApplyUsageCharge() + "'" +
            ", recurringChargeId=" + getRecurringChargeId() +
            ", transactionRate=" + getTransactionRate() +
            ", usageChargeStatus='" + getUsageChargeStatus() + "'" +
            ", transactionFailedSmsSentStatus='" + getTransactionFailedSmsSentStatus() + "'" +
            ", upcomingOrderSmsSentStatus='" + getUpcomingOrderSmsSentStatus() + "'" +
            ", billingAttemptResponseMessage='" + getBillingAttemptResponseMessage() + "'" +
            ", progressAttemptCount=" + getProgressAttemptCount() +
            ", orderNote='" + getOrderNote() + "'" +
            ", securityChallengeSentStatus='" + getSecurityChallengeSentStatus() + "'" +
            ", orderAmountUSD=" + getOrderAmountUSD() +
            "}";
    }
}
