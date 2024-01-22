package com.et.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import com.et.domain.enumeration.subscriptionCreatedEmailSentStatus;
import com.et.domain.enumeration.SubscriptionCreatedSmsSentStatus;
import com.et.domain.enumeration.SubscriptionOriginType;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.et.domain.SubscriptionContractDetails} entity. This class is used
 * in {@link com.et.web.rest.SubscriptionContractDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscription-contract-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubscriptionContractDetailsCriteria implements Serializable, Criteria {
    /**
     * Class for filtering subscriptionCreatedEmailSentStatus
     */
    public static class subscriptionCreatedEmailSentStatusFilter extends Filter<subscriptionCreatedEmailSentStatus> {

        public subscriptionCreatedEmailSentStatusFilter() {
        }

        public subscriptionCreatedEmailSentStatusFilter(subscriptionCreatedEmailSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public subscriptionCreatedEmailSentStatusFilter copy() {
            return new subscriptionCreatedEmailSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering SubscriptionCreatedSmsSentStatus
     */
    public static class SubscriptionCreatedSmsSentStatusFilter extends Filter<SubscriptionCreatedSmsSentStatus> {

        public SubscriptionCreatedSmsSentStatusFilter() {
        }

        public SubscriptionCreatedSmsSentStatusFilter(SubscriptionCreatedSmsSentStatusFilter filter) {
            super(filter);
        }

        @Override
        public SubscriptionCreatedSmsSentStatusFilter copy() {
            return new SubscriptionCreatedSmsSentStatusFilter(this);
        }

    }
    /**
     * Class for filtering SubscriptionOriginType
     */
    public static class SubscriptionOriginTypeFilter extends Filter<SubscriptionOriginType> {

        public SubscriptionOriginTypeFilter() {
        }

        public SubscriptionOriginTypeFilter(SubscriptionOriginTypeFilter filter) {
            super(filter);
        }

        @Override
        public SubscriptionOriginTypeFilter copy() {
            return new SubscriptionOriginTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shop;

    private StringFilter graphSubscriptionContractId;

    private LongFilter subscriptionContractId;

    private StringFilter billingPolicyInterval;

    private IntegerFilter billingPolicyIntervalCount;

    private StringFilter currencyCode;

    private LongFilter customerId;

    private StringFilter graphCustomerId;

    private StringFilter deliveryPolicyInterval;

    private IntegerFilter deliveryPolicyIntervalCount;

    private StringFilter status;

    private StringFilter graphOrderId;

    private LongFilter orderId;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private ZonedDateTimeFilter nextBillingDate;

    private DoubleFilter orderAmount;

    private StringFilter orderName;

    private StringFilter customerName;

    private StringFilter customerEmail;

    private BooleanFilter subscriptionCreatedEmailSent;

    private ZonedDateTimeFilter endsAt;

    private ZonedDateTimeFilter startsAt;

    private subscriptionCreatedEmailSentStatusFilter subscriptionCreatedEmailSentStatus;

    private IntegerFilter minCycles;

    private IntegerFilter maxCycles;

    private StringFilter customerFirstName;

    private StringFilter customerLastName;

    private BooleanFilter autoCharge;

    private StringFilter importedId;

    private BooleanFilter stopUpComingOrderEmail;

    private BooleanFilter pausedFromActive;

    private SubscriptionCreatedSmsSentStatusFilter subscriptionCreatedSmsSentStatus;

    private StringFilter phone;

    private ZonedDateTimeFilter activatedOn;

    private ZonedDateTimeFilter pausedOn;

    private ZonedDateTimeFilter cancelledOn;

    private BooleanFilter allowDeliveryPriceOverride;

    private BooleanFilter disableFixEmptyQueue;

    private DoubleFilter orderAmountUSD;

    private SubscriptionOriginTypeFilter originType;

    private LongFilter originalContractId;

    private StringFilter cancellationNote;

    private DoubleFilter contractAmount;

    private DoubleFilter contractAmountUSD;

    public SubscriptionContractDetailsCriteria() {
    }

    public SubscriptionContractDetailsCriteria(SubscriptionContractDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.shop = other.shop == null ? null : other.shop.copy();
        this.graphSubscriptionContractId = other.graphSubscriptionContractId == null ? null : other.graphSubscriptionContractId.copy();
        this.subscriptionContractId = other.subscriptionContractId == null ? null : other.subscriptionContractId.copy();
        this.billingPolicyInterval = other.billingPolicyInterval == null ? null : other.billingPolicyInterval.copy();
        this.billingPolicyIntervalCount = other.billingPolicyIntervalCount == null ? null : other.billingPolicyIntervalCount.copy();
        this.currencyCode = other.currencyCode == null ? null : other.currencyCode.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.graphCustomerId = other.graphCustomerId == null ? null : other.graphCustomerId.copy();
        this.deliveryPolicyInterval = other.deliveryPolicyInterval == null ? null : other.deliveryPolicyInterval.copy();
        this.deliveryPolicyIntervalCount = other.deliveryPolicyIntervalCount == null ? null : other.deliveryPolicyIntervalCount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.graphOrderId = other.graphOrderId == null ? null : other.graphOrderId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.nextBillingDate = other.nextBillingDate == null ? null : other.nextBillingDate.copy();
        this.orderAmount = other.orderAmount == null ? null : other.orderAmount.copy();
        this.orderName = other.orderName == null ? null : other.orderName.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.customerEmail = other.customerEmail == null ? null : other.customerEmail.copy();
        this.subscriptionCreatedEmailSent = other.subscriptionCreatedEmailSent == null ? null : other.subscriptionCreatedEmailSent.copy();
        this.endsAt = other.endsAt == null ? null : other.endsAt.copy();
        this.startsAt = other.startsAt == null ? null : other.startsAt.copy();
        this.subscriptionCreatedEmailSentStatus = other.subscriptionCreatedEmailSentStatus == null ? null : other.subscriptionCreatedEmailSentStatus.copy();
        this.minCycles = other.minCycles == null ? null : other.minCycles.copy();
        this.maxCycles = other.maxCycles == null ? null : other.maxCycles.copy();
        this.customerFirstName = other.customerFirstName == null ? null : other.customerFirstName.copy();
        this.customerLastName = other.customerLastName == null ? null : other.customerLastName.copy();
        this.autoCharge = other.autoCharge == null ? null : other.autoCharge.copy();
        this.importedId = other.importedId == null ? null : other.importedId.copy();
        this.stopUpComingOrderEmail = other.stopUpComingOrderEmail == null ? null : other.stopUpComingOrderEmail.copy();
        this.pausedFromActive = other.pausedFromActive == null ? null : other.pausedFromActive.copy();
        this.subscriptionCreatedSmsSentStatus = other.subscriptionCreatedSmsSentStatus == null ? null : other.subscriptionCreatedSmsSentStatus.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.activatedOn = other.activatedOn == null ? null : other.activatedOn.copy();
        this.pausedOn = other.pausedOn == null ? null : other.pausedOn.copy();
        this.cancelledOn = other.cancelledOn == null ? null : other.cancelledOn.copy();
        this.allowDeliveryPriceOverride = other.allowDeliveryPriceOverride == null ? null : other.allowDeliveryPriceOverride.copy();
        this.disableFixEmptyQueue = other.disableFixEmptyQueue == null ? null : other.disableFixEmptyQueue.copy();
        this.orderAmountUSD = other.orderAmountUSD == null ? null : other.orderAmountUSD.copy();
        this.originType = other.originType == null ? null : other.originType.copy();
        this.originalContractId = other.originalContractId == null ? null : other.originalContractId.copy();
        this.cancellationNote = other.cancellationNote == null ? null : other.cancellationNote.copy();
        this.contractAmount = other.contractAmount == null ? null : other.contractAmount.copy();
        this.contractAmountUSD = other.contractAmountUSD == null ? null : other.contractAmountUSD.copy();
    }

    @Override
    public SubscriptionContractDetailsCriteria copy() {
        return new SubscriptionContractDetailsCriteria(this);
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

    public StringFilter getGraphSubscriptionContractId() {
        return graphSubscriptionContractId;
    }

    public void setGraphSubscriptionContractId(StringFilter graphSubscriptionContractId) {
        this.graphSubscriptionContractId = graphSubscriptionContractId;
    }

    public LongFilter getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(LongFilter subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public StringFilter getBillingPolicyInterval() {
        return billingPolicyInterval;
    }

    public void setBillingPolicyInterval(StringFilter billingPolicyInterval) {
        this.billingPolicyInterval = billingPolicyInterval;
    }

    public IntegerFilter getBillingPolicyIntervalCount() {
        return billingPolicyIntervalCount;
    }

    public void setBillingPolicyIntervalCount(IntegerFilter billingPolicyIntervalCount) {
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
    }

    public StringFilter getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(StringFilter currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getGraphCustomerId() {
        return graphCustomerId;
    }

    public void setGraphCustomerId(StringFilter graphCustomerId) {
        this.graphCustomerId = graphCustomerId;
    }

    public StringFilter getDeliveryPolicyInterval() {
        return deliveryPolicyInterval;
    }

    public void setDeliveryPolicyInterval(StringFilter deliveryPolicyInterval) {
        this.deliveryPolicyInterval = deliveryPolicyInterval;
    }

    public IntegerFilter getDeliveryPolicyIntervalCount() {
        return deliveryPolicyIntervalCount;
    }

    public void setDeliveryPolicyIntervalCount(IntegerFilter deliveryPolicyIntervalCount) {
        this.deliveryPolicyIntervalCount = deliveryPolicyIntervalCount;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
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

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTimeFilter getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(ZonedDateTimeFilter nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
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

    public StringFilter getCustomerName() {
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public StringFilter getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(StringFilter customerEmail) {
        this.customerEmail = customerEmail;
    }

    public BooleanFilter getSubscriptionCreatedEmailSent() {
        return subscriptionCreatedEmailSent;
    }

    public void setSubscriptionCreatedEmailSent(BooleanFilter subscriptionCreatedEmailSent) {
        this.subscriptionCreatedEmailSent = subscriptionCreatedEmailSent;
    }

    public ZonedDateTimeFilter getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(ZonedDateTimeFilter endsAt) {
        this.endsAt = endsAt;
    }

    public ZonedDateTimeFilter getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(ZonedDateTimeFilter startsAt) {
        this.startsAt = startsAt;
    }

    public subscriptionCreatedEmailSentStatusFilter getSubscriptionCreatedEmailSentStatus() {
        return subscriptionCreatedEmailSentStatus;
    }

    public void setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatusFilter subscriptionCreatedEmailSentStatus) {
        this.subscriptionCreatedEmailSentStatus = subscriptionCreatedEmailSentStatus;
    }

    public IntegerFilter getMinCycles() {
        return minCycles;
    }

    public void setMinCycles(IntegerFilter minCycles) {
        this.minCycles = minCycles;
    }

    public IntegerFilter getMaxCycles() {
        return maxCycles;
    }

    public void setMaxCycles(IntegerFilter maxCycles) {
        this.maxCycles = maxCycles;
    }

    public StringFilter getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(StringFilter customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public StringFilter getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(StringFilter customerLastName) {
        this.customerLastName = customerLastName;
    }

    public BooleanFilter getAutoCharge() {
        return autoCharge;
    }

    public void setAutoCharge(BooleanFilter autoCharge) {
        this.autoCharge = autoCharge;
    }

    public StringFilter getImportedId() {
        return importedId;
    }

    public void setImportedId(StringFilter importedId) {
        this.importedId = importedId;
    }

    public BooleanFilter getStopUpComingOrderEmail() {
        return stopUpComingOrderEmail;
    }

    public void setStopUpComingOrderEmail(BooleanFilter stopUpComingOrderEmail) {
        this.stopUpComingOrderEmail = stopUpComingOrderEmail;
    }

    public BooleanFilter getPausedFromActive() {
        return pausedFromActive;
    }

    public void setPausedFromActive(BooleanFilter pausedFromActive) {
        this.pausedFromActive = pausedFromActive;
    }

    public SubscriptionCreatedSmsSentStatusFilter getSubscriptionCreatedSmsSentStatus() {
        return subscriptionCreatedSmsSentStatus;
    }

    public void setSubscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatusFilter subscriptionCreatedSmsSentStatus) {
        this.subscriptionCreatedSmsSentStatus = subscriptionCreatedSmsSentStatus;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public ZonedDateTimeFilter getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(ZonedDateTimeFilter activatedOn) {
        this.activatedOn = activatedOn;
    }

    public ZonedDateTimeFilter getPausedOn() {
        return pausedOn;
    }

    public void setPausedOn(ZonedDateTimeFilter pausedOn) {
        this.pausedOn = pausedOn;
    }

    public ZonedDateTimeFilter getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(ZonedDateTimeFilter cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public BooleanFilter getAllowDeliveryPriceOverride() {
        return allowDeliveryPriceOverride;
    }

    public void setAllowDeliveryPriceOverride(BooleanFilter allowDeliveryPriceOverride) {
        this.allowDeliveryPriceOverride = allowDeliveryPriceOverride;
    }

    public BooleanFilter getDisableFixEmptyQueue() {
        return disableFixEmptyQueue;
    }

    public void setDisableFixEmptyQueue(BooleanFilter disableFixEmptyQueue) {
        this.disableFixEmptyQueue = disableFixEmptyQueue;
    }

    public DoubleFilter getOrderAmountUSD() {
        return orderAmountUSD;
    }

    public void setOrderAmountUSD(DoubleFilter orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
    }

    public SubscriptionOriginTypeFilter getOriginType() {
        return originType;
    }

    public void setOriginType(SubscriptionOriginTypeFilter originType) {
        this.originType = originType;
    }

    public LongFilter getOriginalContractId() {
        return originalContractId;
    }

    public void setOriginalContractId(LongFilter originalContractId) {
        this.originalContractId = originalContractId;
    }

    public StringFilter getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(StringFilter cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public DoubleFilter getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(DoubleFilter contractAmount) {
        this.contractAmount = contractAmount;
    }

    public DoubleFilter getContractAmountUSD() {
        return contractAmountUSD;
    }

    public void setContractAmountUSD(DoubleFilter contractAmountUSD) {
        this.contractAmountUSD = contractAmountUSD;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubscriptionContractDetailsCriteria that = (SubscriptionContractDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(shop, that.shop) &&
            Objects.equals(graphSubscriptionContractId, that.graphSubscriptionContractId) &&
            Objects.equals(subscriptionContractId, that.subscriptionContractId) &&
            Objects.equals(billingPolicyInterval, that.billingPolicyInterval) &&
            Objects.equals(billingPolicyIntervalCount, that.billingPolicyIntervalCount) &&
            Objects.equals(currencyCode, that.currencyCode) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(graphCustomerId, that.graphCustomerId) &&
            Objects.equals(deliveryPolicyInterval, that.deliveryPolicyInterval) &&
            Objects.equals(deliveryPolicyIntervalCount, that.deliveryPolicyIntervalCount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(graphOrderId, that.graphOrderId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(nextBillingDate, that.nextBillingDate) &&
            Objects.equals(orderAmount, that.orderAmount) &&
            Objects.equals(orderName, that.orderName) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(customerEmail, that.customerEmail) &&
            Objects.equals(subscriptionCreatedEmailSent, that.subscriptionCreatedEmailSent) &&
            Objects.equals(endsAt, that.endsAt) &&
            Objects.equals(startsAt, that.startsAt) &&
            Objects.equals(subscriptionCreatedEmailSentStatus, that.subscriptionCreatedEmailSentStatus) &&
            Objects.equals(minCycles, that.minCycles) &&
            Objects.equals(maxCycles, that.maxCycles) &&
            Objects.equals(customerFirstName, that.customerFirstName) &&
            Objects.equals(customerLastName, that.customerLastName) &&
            Objects.equals(autoCharge, that.autoCharge) &&
            Objects.equals(importedId, that.importedId) &&
            Objects.equals(stopUpComingOrderEmail, that.stopUpComingOrderEmail) &&
            Objects.equals(pausedFromActive, that.pausedFromActive) &&
            Objects.equals(subscriptionCreatedSmsSentStatus, that.subscriptionCreatedSmsSentStatus) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(activatedOn, that.activatedOn) &&
            Objects.equals(pausedOn, that.pausedOn) &&
            Objects.equals(cancelledOn, that.cancelledOn) &&
            Objects.equals(allowDeliveryPriceOverride, that.allowDeliveryPriceOverride) &&
            Objects.equals(disableFixEmptyQueue, that.disableFixEmptyQueue) &&
            Objects.equals(orderAmountUSD, that.orderAmountUSD) &&
            Objects.equals(originType, that.originType) &&
            Objects.equals(originalContractId, that.originalContractId) &&
            Objects.equals(cancellationNote, that.cancellationNote) &&
            Objects.equals(contractAmount, that.contractAmount) &&
            Objects.equals(contractAmountUSD, that.contractAmountUSD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        shop,
        graphSubscriptionContractId,
        subscriptionContractId,
        billingPolicyInterval,
        billingPolicyIntervalCount,
        currencyCode,
        customerId,
        graphCustomerId,
        deliveryPolicyInterval,
        deliveryPolicyIntervalCount,
        status,
        graphOrderId,
        orderId,
        createdAt,
        updatedAt,
        nextBillingDate,
        orderAmount,
        orderName,
        customerName,
        customerEmail,
        subscriptionCreatedEmailSent,
        endsAt,
        startsAt,
        subscriptionCreatedEmailSentStatus,
        minCycles,
        maxCycles,
        customerFirstName,
        customerLastName,
        autoCharge,
        importedId,
        stopUpComingOrderEmail,
        pausedFromActive,
        subscriptionCreatedSmsSentStatus,
        phone,
        activatedOn,
        pausedOn,
        cancelledOn,
        allowDeliveryPriceOverride,
        disableFixEmptyQueue,
        orderAmountUSD,
        originType,
        originalContractId,
        cancellationNote,
        contractAmount,
        contractAmountUSD
        );
    }

    @Override
    public String toString() {
        return "SubscriptionContractDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (shop != null ? "shop=" + shop + ", " : "") +
                (graphSubscriptionContractId != null ? "graphSubscriptionContractId=" + graphSubscriptionContractId + ", " : "") +
                (subscriptionContractId != null ? "subscriptionContractId=" + subscriptionContractId + ", " : "") +
                (billingPolicyInterval != null ? "billingPolicyInterval=" + billingPolicyInterval + ", " : "") +
                (billingPolicyIntervalCount != null ? "billingPolicyIntervalCount=" + billingPolicyIntervalCount + ", " : "") +
                (currencyCode != null ? "currencyCode=" + currencyCode + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (graphCustomerId != null ? "graphCustomerId=" + graphCustomerId + ", " : "") +
                (deliveryPolicyInterval != null ? "deliveryPolicyInterval=" + deliveryPolicyInterval + ", " : "") +
                (deliveryPolicyIntervalCount != null ? "deliveryPolicyIntervalCount=" + deliveryPolicyIntervalCount + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (graphOrderId != null ? "graphOrderId=" + graphOrderId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (nextBillingDate != null ? "nextBillingDate=" + nextBillingDate + ", " : "") +
                (orderAmount != null ? "orderAmount=" + orderAmount + ", " : "") +
                (orderName != null ? "orderName=" + orderName + ", " : "") +
                (customerName != null ? "customerName=" + customerName + ", " : "") +
                (customerEmail != null ? "customerEmail=" + customerEmail + ", " : "") +
                (subscriptionCreatedEmailSent != null ? "subscriptionCreatedEmailSent=" + subscriptionCreatedEmailSent + ", " : "") +
                (endsAt != null ? "endsAt=" + endsAt + ", " : "") +
                (startsAt != null ? "startsAt=" + startsAt + ", " : "") +
                (subscriptionCreatedEmailSentStatus != null ? "subscriptionCreatedEmailSentStatus=" + subscriptionCreatedEmailSentStatus + ", " : "") +
                (minCycles != null ? "minCycles=" + minCycles + ", " : "") +
                (maxCycles != null ? "maxCycles=" + maxCycles + ", " : "") +
                (customerFirstName != null ? "customerFirstName=" + customerFirstName + ", " : "") +
                (customerLastName != null ? "customerLastName=" + customerLastName + ", " : "") +
                (autoCharge != null ? "autoCharge=" + autoCharge + ", " : "") +
                (importedId != null ? "importedId=" + importedId + ", " : "") +
                (stopUpComingOrderEmail != null ? "stopUpComingOrderEmail=" + stopUpComingOrderEmail + ", " : "") +
                (pausedFromActive != null ? "pausedFromActive=" + pausedFromActive + ", " : "") +
                (subscriptionCreatedSmsSentStatus != null ? "subscriptionCreatedSmsSentStatus=" + subscriptionCreatedSmsSentStatus + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (activatedOn != null ? "activatedOn=" + activatedOn + ", " : "") +
                (pausedOn != null ? "pausedOn=" + pausedOn + ", " : "") +
                (cancelledOn != null ? "cancelledOn=" + cancelledOn + ", " : "") +
                (allowDeliveryPriceOverride != null ? "allowDeliveryPriceOverride=" + allowDeliveryPriceOverride + ", " : "") +
                (disableFixEmptyQueue != null ? "disableFixEmptyQueue=" + disableFixEmptyQueue + ", " : "") +
                (orderAmountUSD != null ? "orderAmountUSD=" + orderAmountUSD + ", " : "") +
                (originType != null ? "originType=" + originType + ", " : "") +
                (originalContractId != null ? "originalContractId=" + originalContractId + ", " : "") +
                (cancellationNote != null ? "cancellationNote=" + cancellationNote + ", " : "") +
                (contractAmount != null ? "contractAmount=" + contractAmount + ", " : "") +
                (contractAmountUSD != null ? "contractAmountUSD=" + contractAmountUSD + ", " : "") +
            "}";
    }

}
