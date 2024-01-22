package com.et.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.subscriptionCreatedEmailSentStatus;
import com.et.domain.enumeration.SubscriptionCreatedSmsSentStatus;
import com.et.domain.enumeration.SubscriptionOriginType;
import com.et.domain.enumeration.SubscriptionType;

/**
 * A DTO for the {@link com.et.domain.SubscriptionContractDetails} entity.
 */
public class SubscriptionContractDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private String graphSubscriptionContractId;

    private Long subscriptionContractId;

    private String billingPolicyInterval;

    private Integer billingPolicyIntervalCount;

    private String currencyCode;

    private Long customerId;

    private String graphCustomerId;

    private String deliveryPolicyInterval;

    private Integer deliveryPolicyIntervalCount;

    private String status;

    private String graphOrderId;

    private Long orderId;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime nextBillingDate;

    private Double orderAmount;

    private String orderName;

    private String customerName;

    private String customerEmail;

    private Boolean subscriptionCreatedEmailSent;

    private ZonedDateTime endsAt;

    private ZonedDateTime startsAt;

    private subscriptionCreatedEmailSentStatus subscriptionCreatedEmailSentStatus;

    private Integer minCycles;

    private Integer maxCycles;

    private String customerFirstName;

    private String customerLastName;

    private Boolean autoCharge;

    private String importedId;

    private Boolean stopUpComingOrderEmail;

    private Boolean pausedFromActive;

    private SubscriptionCreatedSmsSentStatus subscriptionCreatedSmsSentStatus;

    private String phone;

    private ZonedDateTime activatedOn;

    private ZonedDateTime pausedOn;

    private ZonedDateTime cancelledOn;

    @Lob
    private String contractDetailsJSON;

    @Lob
    private String cancellationFeedback;

    @Lob
    private String orderNote;

    @Lob
    private String orderNoteAttributes;

    private Boolean allowDeliveryPriceOverride;

    private Boolean disableFixEmptyQueue;

    private Double orderAmountUSD;

    private SubscriptionOriginType originType;

    private Long originalContractId;

    private String cancellationNote;

    private SubscriptionType subscriptionType;

    private String subscriptionTypeIdentifier;

    private Integer upcomingEmailBufferDays;

    private String upcomingEmailTaskUrl;

    private Double contractAmount;

    private Double contractAmountUSD;


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

    public String getGraphSubscriptionContractId() {
        return graphSubscriptionContractId;
    }

    public void setGraphSubscriptionContractId(String graphSubscriptionContractId) {
        this.graphSubscriptionContractId = graphSubscriptionContractId;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public String getBillingPolicyInterval() {
        return billingPolicyInterval;
    }

    public void setBillingPolicyInterval(String billingPolicyInterval) {
        this.billingPolicyInterval = billingPolicyInterval;
    }

    public Integer getBillingPolicyIntervalCount() {
        return billingPolicyIntervalCount;
    }

    public void setBillingPolicyIntervalCount(Integer billingPolicyIntervalCount) {
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getGraphCustomerId() {
        return graphCustomerId;
    }

    public void setGraphCustomerId(String graphCustomerId) {
        this.graphCustomerId = graphCustomerId;
    }

    public String getDeliveryPolicyInterval() {
        return deliveryPolicyInterval;
    }

    public void setDeliveryPolicyInterval(String deliveryPolicyInterval) {
        this.deliveryPolicyInterval = deliveryPolicyInterval;
    }

    public Integer getDeliveryPolicyIntervalCount() {
        return deliveryPolicyIntervalCount;
    }

    public void setDeliveryPolicyIntervalCount(Integer deliveryPolicyIntervalCount) {
        this.deliveryPolicyIntervalCount = deliveryPolicyIntervalCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(ZonedDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Boolean isSubscriptionCreatedEmailSent() {
        return subscriptionCreatedEmailSent;
    }

    public void setSubscriptionCreatedEmailSent(Boolean subscriptionCreatedEmailSent) {
        this.subscriptionCreatedEmailSent = subscriptionCreatedEmailSent;
    }

    public ZonedDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(ZonedDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public ZonedDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(ZonedDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public subscriptionCreatedEmailSentStatus getSubscriptionCreatedEmailSentStatus() {
        return subscriptionCreatedEmailSentStatus;
    }

    public void setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus subscriptionCreatedEmailSentStatus) {
        this.subscriptionCreatedEmailSentStatus = subscriptionCreatedEmailSentStatus;
    }

    public Integer getMinCycles() {
        return minCycles;
    }

    public void setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
    }

    public Integer getMaxCycles() {
        return maxCycles;
    }

    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Boolean isAutoCharge() {
        return autoCharge;
    }

    public void setAutoCharge(Boolean autoCharge) {
        this.autoCharge = autoCharge;
    }

    public String getImportedId() {
        return importedId;
    }

    public void setImportedId(String importedId) {
        this.importedId = importedId;
    }

    public Boolean isStopUpComingOrderEmail() {
        return stopUpComingOrderEmail;
    }

    public void setStopUpComingOrderEmail(Boolean stopUpComingOrderEmail) {
        this.stopUpComingOrderEmail = stopUpComingOrderEmail;
    }

    public Boolean isPausedFromActive() {
        return pausedFromActive;
    }

    public void setPausedFromActive(Boolean pausedFromActive) {
        this.pausedFromActive = pausedFromActive;
    }

    public SubscriptionCreatedSmsSentStatus getSubscriptionCreatedSmsSentStatus() {
        return subscriptionCreatedSmsSentStatus;
    }

    public void setSubscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatus subscriptionCreatedSmsSentStatus) {
        this.subscriptionCreatedSmsSentStatus = subscriptionCreatedSmsSentStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ZonedDateTime getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(ZonedDateTime activatedOn) {
        this.activatedOn = activatedOn;
    }

    public ZonedDateTime getPausedOn() {
        return pausedOn;
    }

    public void setPausedOn(ZonedDateTime pausedOn) {
        this.pausedOn = pausedOn;
    }

    public ZonedDateTime getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(ZonedDateTime cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public String getContractDetailsJSON() {
        return contractDetailsJSON;
    }

    public void setContractDetailsJSON(String contractDetailsJSON) {
        this.contractDetailsJSON = contractDetailsJSON;
    }

    public String getCancellationFeedback() {
        return cancellationFeedback;
    }

    public void setCancellationFeedback(String cancellationFeedback) {
        this.cancellationFeedback = cancellationFeedback;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getOrderNoteAttributes() {
        return orderNoteAttributes;
    }

    public void setOrderNoteAttributes(String orderNoteAttributes) {
        this.orderNoteAttributes = orderNoteAttributes;
    }

    public Boolean isAllowDeliveryPriceOverride() {
        return allowDeliveryPriceOverride;
    }

    public void setAllowDeliveryPriceOverride(Boolean allowDeliveryPriceOverride) {
        this.allowDeliveryPriceOverride = allowDeliveryPriceOverride;
    }

    public Boolean isDisableFixEmptyQueue() {
        return disableFixEmptyQueue;
    }

    public void setDisableFixEmptyQueue(Boolean disableFixEmptyQueue) {
        this.disableFixEmptyQueue = disableFixEmptyQueue;
    }

    public Double getOrderAmountUSD() {
        return orderAmountUSD;
    }

    public void setOrderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
    }

    public SubscriptionOriginType getOriginType() {
        return originType;
    }

    public void setOriginType(SubscriptionOriginType originType) {
        this.originType = originType;
    }

    public Long getOriginalContractId() {
        return originalContractId;
    }

    public void setOriginalContractId(Long originalContractId) {
        this.originalContractId = originalContractId;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionTypeIdentifier() {
        return subscriptionTypeIdentifier;
    }

    public void setSubscriptionTypeIdentifier(String subscriptionTypeIdentifier) {
        this.subscriptionTypeIdentifier = subscriptionTypeIdentifier;
    }

    public Integer getUpcomingEmailBufferDays() {
        return upcomingEmailBufferDays;
    }

    public void setUpcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
    }

    public String getUpcomingEmailTaskUrl() {
        return upcomingEmailTaskUrl;
    }

    public void setUpcomingEmailTaskUrl(String upcomingEmailTaskUrl) {
        this.upcomingEmailTaskUrl = upcomingEmailTaskUrl;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Double getContractAmountUSD() {
        return contractAmountUSD;
    }

    public void setContractAmountUSD(Double contractAmountUSD) {
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

        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = (SubscriptionContractDetailsDTO) o;
        if (subscriptionContractDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subscriptionContractDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractDetailsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", graphSubscriptionContractId='" + getGraphSubscriptionContractId() + "'" +
            ", subscriptionContractId=" + getSubscriptionContractId() +
            ", billingPolicyInterval='" + getBillingPolicyInterval() + "'" +
            ", billingPolicyIntervalCount=" + getBillingPolicyIntervalCount() +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", customerId=" + getCustomerId() +
            ", graphCustomerId='" + getGraphCustomerId() + "'" +
            ", deliveryPolicyInterval='" + getDeliveryPolicyInterval() + "'" +
            ", deliveryPolicyIntervalCount=" + getDeliveryPolicyIntervalCount() +
            ", status='" + getStatus() + "'" +
            ", graphOrderId='" + getGraphOrderId() + "'" +
            ", orderId=" + getOrderId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", nextBillingDate='" + getNextBillingDate() + "'" +
            ", orderAmount=" + getOrderAmount() +
            ", orderName='" + getOrderName() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", customerEmail='" + getCustomerEmail() + "'" +
            ", subscriptionCreatedEmailSent='" + isSubscriptionCreatedEmailSent() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", startsAt='" + getStartsAt() + "'" +
            ", subscriptionCreatedEmailSentStatus='" + getSubscriptionCreatedEmailSentStatus() + "'" +
            ", minCycles=" + getMinCycles() +
            ", maxCycles=" + getMaxCycles() +
            ", customerFirstName='" + getCustomerFirstName() + "'" +
            ", customerLastName='" + getCustomerLastName() + "'" +
            ", autoCharge='" + isAutoCharge() + "'" +
            ", importedId='" + getImportedId() + "'" +
            ", stopUpComingOrderEmail='" + isStopUpComingOrderEmail() + "'" +
            ", pausedFromActive='" + isPausedFromActive() + "'" +
            ", subscriptionCreatedSmsSentStatus='" + getSubscriptionCreatedSmsSentStatus() + "'" +
            ", phone='" + getPhone() + "'" +
            ", activatedOn='" + getActivatedOn() + "'" +
            ", pausedOn='" + getPausedOn() + "'" +
            ", cancelledOn='" + getCancelledOn() + "'" +
            ", contractDetailsJSON='" + getContractDetailsJSON() + "'" +
            ", cancellationFeedback='" + getCancellationFeedback() + "'" +
            ", orderNote='" + getOrderNote() + "'" +
            ", orderNoteAttributes='" + getOrderNoteAttributes() + "'" +
            ", allowDeliveryPriceOverride='" + isAllowDeliveryPriceOverride() + "'" +
            ", disableFixEmptyQueue='" + isDisableFixEmptyQueue() + "'" +
            ", orderAmountUSD=" + getOrderAmountUSD() +
            ", originType='" + getOriginType() + "'" +
            ", originalContractId=" + getOriginalContractId() +
            ", cancellationNote='" + getCancellationNote() + "'" +
            ", subscriptionType='" + getSubscriptionType() + "'" +
            ", subscriptionTypeIdentifier='" + getSubscriptionTypeIdentifier() + "'" +
            ", upcomingEmailBufferDays=" + getUpcomingEmailBufferDays() +
            ", upcomingEmailTaskUrl='" + getUpcomingEmailTaskUrl() + "'" +
            ", contractAmount=" + getContractAmount() +
            ", contractAmountUSD=" + getContractAmountUSD() +
            "}";
    }
}
