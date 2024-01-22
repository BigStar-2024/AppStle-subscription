package com.et.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import com.et.domain.enumeration.BillingAttemptStatus;
import com.et.domain.enumeration.transactionFailedEmailSentStatus;
import com.et.domain.enumeration.upcomingOrderEmailSentStatus;
import com.et.domain.enumeration.UsageChargeStatus;
import com.et.domain.enumeration.TransactionFailedSmsSentStatus;
import com.et.domain.enumeration.UpcomingOrderSmsSentStatus;
import com.et.domain.enumeration.SecurityChallengeSentStatus;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.et.domain.SubscriptionBillingAttempt} entity. This class is used
 * in {@link com.et.web.rest.SubscriptionBillingAttemptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscription-billing-attempts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubscriptionBillingAttemptCriteria implements Serializable, Criteria {
    /**
     * Class for filtering BillingAttemptStatus
     */
    public static class BillingAttemptStatusFilter extends Filter<BillingAttemptStatus> {

        public BillingAttemptStatusFilter() {
        }

        public BillingAttemptStatusFilter(BillingAttemptStatusFilter filter) {
            super(filter);
        }

        @Override
        public BillingAttemptStatusFilter copy() {
            return new BillingAttemptStatusFilter(this);
        }

    }
    /**
     * Class for filtering transactionFailedEmailSentStatus
     */
    public static class transactionFailedEmailSentStatusFilter extends Filter<transactionFailedEmailSentStatus> {

        public transactionFailedEmailSentStatusFilter() {
        }

        public transactionFailedEmailSentStatusFilter(transactionFailedEmailSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public transactionFailedEmailSentStatusFilter copy() {
            return new transactionFailedEmailSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering upcomingOrderEmailSentStatus
     */
    public static class upcomingOrderEmailSentStatusFilter extends Filter<upcomingOrderEmailSentStatus> {

        public upcomingOrderEmailSentStatusFilter() {
        }

        public upcomingOrderEmailSentStatusFilter(upcomingOrderEmailSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public upcomingOrderEmailSentStatusFilter copy() {
            return new upcomingOrderEmailSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering UsageChargeStatus
     */
    public static class UsageChargeStatusFilter extends Filter<UsageChargeStatus> {

        public UsageChargeStatusFilter() {
        }

        public UsageChargeStatusFilter(UsageChargeStatusFilter filter) {
            super(filter);
        }

        @Override
        public UsageChargeStatusFilter copy() {
            return new UsageChargeStatusFilter(this);
        }

    }
    /**
     * Class for filtering TransactionFailedSmsSentStatus
     */
    public static class TransactionFailedSmsSentStatusFilter extends Filter<TransactionFailedSmsSentStatus> {

        public TransactionFailedSmsSentStatusFilter() {
        }

        public TransactionFailedSmsSentStatusFilter(TransactionFailedSmsSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransactionFailedSmsSentStatusFilter copy() {
            return new TransactionFailedSmsSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering UpcomingOrderSmsSentStatus
     */
    public static class UpcomingOrderSmsSentStatusFilter extends Filter<UpcomingOrderSmsSentStatus> {

        public UpcomingOrderSmsSentStatusFilter() {
        }

        public UpcomingOrderSmsSentStatusFilter(UpcomingOrderSmsSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public UpcomingOrderSmsSentStatusFilter copy() {
            return new UpcomingOrderSmsSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering SecurityChallengeSentStatus
     */
    public static class SecurityChallengeSentStatusFilter extends Filter<SecurityChallengeSentStatus> {

        public SecurityChallengeSentStatusFilter() {
        }

        public SecurityChallengeSentStatusFilter(SecurityChallengeSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public SecurityChallengeSentStatusFilter copy() {
            return new SecurityChallengeSentStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shop;

    private StringFilter billingAttemptId;

    private BillingAttemptStatusFilter status;

    private ZonedDateTimeFilter billingDate;

    private LongFilter contractId;

    private IntegerFilter attemptCount;

    private ZonedDateTimeFilter attemptTime;

    private StringFilter graphOrderId;

    private LongFilter orderId;

    private DoubleFilter orderAmount;

    private StringFilter orderName;

    private BooleanFilter retryingNeeded;

    private transactionFailedEmailSentStatusFilter transactionFailedEmailSentStatus;

    private upcomingOrderEmailSentStatusFilter upcomingOrderEmailSentStatus;

    private BooleanFilter applyUsageCharge;

    private LongFilter recurringChargeId;

    private DoubleFilter transactionRate;

    private UsageChargeStatusFilter usageChargeStatus;

    private TransactionFailedSmsSentStatusFilter transactionFailedSmsSentStatus;

    private UpcomingOrderSmsSentStatusFilter upcomingOrderSmsSentStatus;

    private IntegerFilter progressAttemptCount;

    private SecurityChallengeSentStatusFilter securityChallengeSentStatus;

    public SubscriptionBillingAttemptCriteria(){
    }

    public SubscriptionBillingAttemptCriteria(SubscriptionBillingAttemptCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.shop = other.shop == null ? null : other.shop.copy();
        this.billingAttemptId = other.billingAttemptId == null ? null : other.billingAttemptId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.billingDate = other.billingDate == null ? null : other.billingDate.copy();
        this.contractId = other.contractId == null ? null : other.contractId.copy();
        this.attemptCount = other.attemptCount == null ? null : other.attemptCount.copy();
        this.attemptTime = other.attemptTime == null ? null : other.attemptTime.copy();
        this.graphOrderId = other.graphOrderId == null ? null : other.graphOrderId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.orderName = other.orderName == null ? null : other.orderName.copy();
        this.retryingNeeded = other.retryingNeeded == null ? null : other.retryingNeeded.copy();
        this.transactionFailedEmailSentStatus = other.transactionFailedEmailSentStatus == null ? null : other.transactionFailedEmailSentStatus.copy();
        this.upcomingOrderEmailSentStatus = other.upcomingOrderEmailSentStatus == null ? null : other.upcomingOrderEmailSentStatus.copy();
        this.applyUsageCharge = other.applyUsageCharge == null ? null : other.applyUsageCharge.copy();
        this.recurringChargeId = other.recurringChargeId == null ? null : other.recurringChargeId.copy();
        this.transactionRate = other.transactionRate == null ? null : other.transactionRate.copy();
        this.usageChargeStatus = other.usageChargeStatus == null ? null : other.usageChargeStatus.copy();
        this.transactionFailedSmsSentStatus = other.transactionFailedSmsSentStatus == null ? null : other.transactionFailedSmsSentStatus.copy();
        this.upcomingOrderSmsSentStatus = other.upcomingOrderSmsSentStatus == null ? null : other.upcomingOrderSmsSentStatus.copy();
        this.progressAttemptCount = other.progressAttemptCount == null ? null : other.progressAttemptCount.copy();
        this.securityChallengeSentStatus = other.securityChallengeSentStatus == null ? null : other.securityChallengeSentStatus.copy();
    }

    @Override
    public SubscriptionBillingAttemptCriteria copy() {
        return new SubscriptionBillingAttemptCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getShop() {
        return shop;
    }

    public void setShop(StringFilter shop) {
        this.shop = shop;
    }

    public StringFilter getBillingAttemptId() {
        return billingAttemptId;
    }

    public void setBillingAttemptId(StringFilter billingAttemptId) {
        this.billingAttemptId = billingAttemptId;
    }

    public BillingAttemptStatusFilter getStatus() {
        return status;
    }

    public void setStatus(BillingAttemptStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(ZonedDateTimeFilter billingDate) {
        this.billingDate = billingDate;
    }

    public LongFilter getContractId() {
        return contractId;
    }

    public void setContractId(LongFilter contractId) {
        this.contractId = contractId;
    }

    public IntegerFilter getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(IntegerFilter attemptCount) {
        this.attemptCount = attemptCount;
    }

    public ZonedDateTimeFilter getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(ZonedDateTimeFilter attemptTime) {
        this.attemptTime = attemptTime;
    }

    public StringFilter getGraphOrderId() {
        return graphOrderId;
    }

    public void setGraphOrderId(StringFilter graphOrderId) {
        this.graphOrderId = graphOrderId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public DoubleFilter getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(DoubleFilter orderAmount) {
        this.orderAmount = orderAmount;
    }

    public StringFilter getOrderName() {
        return orderName;
    }

    public void setOrderName(StringFilter orderName) {
        this.orderName = orderName;
    }

    public BooleanFilter getRetryingNeeded() {
        return retryingNeeded;
    }

    public void setRetryingNeeded(BooleanFilter retryingNeeded) {
        this.retryingNeeded = retryingNeeded;
    }

    public transactionFailedEmailSentStatusFilter getTransactionFailedEmailSentStatus() {
        return transactionFailedEmailSentStatus;
    }

    public void setTransactionFailedEmailSentStatus(transactionFailedEmailSentStatusFilter transactionFailedEmailSentStatus) {
        this.transactionFailedEmailSentStatus = transactionFailedEmailSentStatus;
    }

    public upcomingOrderEmailSentStatusFilter getUpcomingOrderEmailSentStatus() {
        return upcomingOrderEmailSentStatus;
    }

    public void setUpcomingOrderEmailSentStatus(upcomingOrderEmailSentStatusFilter upcomingOrderEmailSentStatus) {
        this.upcomingOrderEmailSentStatus = upcomingOrderEmailSentStatus;
    }

    public BooleanFilter getApplyUsageCharge() {
        return applyUsageCharge;
    }

    public void setApplyUsageCharge(BooleanFilter applyUsageCharge) {
        this.applyUsageCharge = applyUsageCharge;
    }

    public LongFilter getRecurringChargeId() {
        return recurringChargeId;
    }

    public void setRecurringChargeId(LongFilter recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
    }

    public DoubleFilter getTransactionRate() {
        return transactionRate;
    }

    public void setTransactionRate(DoubleFilter transactionRate) {
        this.transactionRate = transactionRate;
    }

    public UsageChargeStatusFilter getUsageChargeStatus() {
        return usageChargeStatus;
    }

    public void setUsageChargeStatus(UsageChargeStatusFilter usageChargeStatus) {
        this.usageChargeStatus = usageChargeStatus;
    }

    public TransactionFailedSmsSentStatusFilter getTransactionFailedSmsSentStatus() {
        return transactionFailedSmsSentStatus;
    }

    public void setTransactionFailedSmsSentStatus(TransactionFailedSmsSentStatusFilter transactionFailedSmsSentStatus) {
        this.transactionFailedSmsSentStatus = transactionFailedSmsSentStatus;
    }

    public UpcomingOrderSmsSentStatusFilter getUpcomingOrderSmsSentStatus() {
        return upcomingOrderSmsSentStatus;
    }

    public void setUpcomingOrderSmsSentStatus(UpcomingOrderSmsSentStatusFilter upcomingOrderSmsSentStatus) {
        this.upcomingOrderSmsSentStatus = upcomingOrderSmsSentStatus;
    }

    public IntegerFilter getProgressAttemptCount() {
        return progressAttemptCount;
    }

    public void setProgressAttemptCount(IntegerFilter progressAttemptCount) {
        this.progressAttemptCount = progressAttemptCount;
    }

    public SecurityChallengeSentStatusFilter getSecurityChallengeSentStatus() {
        return securityChallengeSentStatus;
    }

    public void setSecurityChallengeSentStatus(SecurityChallengeSentStatusFilter securityChallengeSentStatus) {
        this.securityChallengeSentStatus = securityChallengeSentStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubscriptionBillingAttemptCriteria that = (SubscriptionBillingAttemptCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(shop, that.shop) &&
            Objects.equals(billingAttemptId, that.billingAttemptId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(billingDate, that.billingDate) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(attemptCount, that.attemptCount) &&
            Objects.equals(attemptTime, that.attemptTime) &&
            Objects.equals(graphOrderId, that.graphOrderId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(orderName, that.orderName) &&
            Objects.equals(retryingNeeded, that.retryingNeeded) &&
            Objects.equals(transactionFailedEmailSentStatus, that.transactionFailedEmailSentStatus) &&
            Objects.equals(upcomingOrderEmailSentStatus, that.upcomingOrderEmailSentStatus) &&
            Objects.equals(applyUsageCharge, that.applyUsageCharge) &&
            Objects.equals(recurringChargeId, that.recurringChargeId) &&
            Objects.equals(transactionRate, that.transactionRate) &&
            Objects.equals(usageChargeStatus, that.usageChargeStatus) &&
            Objects.equals(transactionFailedSmsSentStatus, that.transactionFailedSmsSentStatus) &&
            Objects.equals(upcomingOrderSmsSentStatus, that.upcomingOrderSmsSentStatus) &&
            Objects.equals(progressAttemptCount, that.progressAttemptCount) &&
            Objects.equals(securityChallengeSentStatus, that.securityChallengeSentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        shop,
        billingAttemptId,
        status,
        billingDate,
        contractId,
        attemptCount,
        attemptTime,
        graphOrderId,
        orderId,
        orderAmount,
        orderName,
        retryingNeeded,
        transactionFailedEmailSentStatus,
        upcomingOrderEmailSentStatus,
        applyUsageCharge,
        recurringChargeId,
        transactionRate,
        usageChargeStatus,
        transactionFailedSmsSentStatus,
        upcomingOrderSmsSentStatus,
        progressAttemptCount,
        securityChallengeSentStatus
        );
    }

    @Override
    public String toString() {
        return "SubscriptionBillingAttemptCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (shop != null ? "shop=" + shop + ", " : "") +
                (billingAttemptId != null ? "billingAttemptId=" + billingAttemptId + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (billingDate != null ? "billingDate=" + billingDate + ", " : "") +
                (contractId != null ? "contractId=" + contractId + ", " : "") +
                (attemptCount != null ? "attemptCount=" + attemptCount + ", " : "") +
                (attemptTime != null ? "attemptTime=" + attemptTime + ", " : "") +
                (graphOrderId != null ? "graphOrderId=" + graphOrderId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
                (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
                (orderName != null ? "orderName=" + orderName + ", " : "") +
                (retryingNeeded != null ? "retryingNeeded=" + retryingNeeded + ", " : "") +
                (transactionFailedEmailSentStatus != null ? "transactionFailedEmailSentStatus=" + transactionFailedEmailSentStatus + ", " : "") +
                (upcomingOrderEmailSentStatus != null ? "upcomingOrderEmailSentStatus=" + upcomingOrderEmailSentStatus + ", " : "") +
                (applyUsageCharge != null ? "applyUsageCharge=" + applyUsageCharge + ", " : "") +
                (recurringChargeId != null ? "recurringChargeId=" + recurringChargeId + ", " : "") +
                (transactionRate != null ? "transactionRate=" + transactionRate + ", " : "") +
                (usageChargeStatus != null ? "usageChargeStatus=" + usageChargeStatus + ", " : "") +
                (transactionFailedSmsSentStatus != null ? "transactionFailedSmsSentStatus=" + transactionFailedSmsSentStatus + ", " : "") +
                (upcomingOrderSmsSentStatus != null ? "upcomingOrderSmsSentStatus=" + upcomingOrderSmsSentStatus + ", " : "") +
                (progressAttemptCount != null ? "progressAttemptCount=" + progressAttemptCount + ", " : "") +
                (securityChallengeSentStatus != null ? "securityChallengeSentStatus=" + securityChallengeSentStatus + ", " : "") +
            "}";
    }

}
