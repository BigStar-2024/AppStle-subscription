package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.subscriptionCreatedEmailSentStatus;

import com.et.domain.enumeration.SubscriptionCreatedSmsSentStatus;

import com.et.domain.enumeration.SubscriptionOriginType;

import com.et.domain.enumeration.SubscriptionType;

/**
 * A SubscriptionContractDetails.
 */
@Entity
@Table(name = "subscription_contract_details")
public class SubscriptionContractDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "graph_subscription_contract_id")
    private String graphSubscriptionContractId;

    @Column(name = "subscription_contract_id")
    private Long subscriptionContractId;

    @Column(name = "billing_policy_interval")
    private String billingPolicyInterval;

    @Column(name = "billing_policy_interval_count")
    private Integer billingPolicyIntervalCount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "graph_customer_id")
    private String graphCustomerId;

    @Column(name = "delivery_policy_interval")
    private String deliveryPolicyInterval;

    @Column(name = "delivery_policy_interval_count")
    private Integer deliveryPolicyIntervalCount;

    @Column(name = "status")
    private String status;

    @Column(name = "graph_order_id")
    private String graphOrderId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "next_billing_date")
    private ZonedDateTime nextBillingDate;

    @Column(name = "order_amount")
    private Double orderAmount;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "subscription_created_email_sent")
    private Boolean subscriptionCreatedEmailSent;

    @Column(name = "ends_at")
    private ZonedDateTime endsAt;

    @Column(name = "starts_at")
    private ZonedDateTime startsAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_created_email_sent_status")
    private subscriptionCreatedEmailSentStatus subscriptionCreatedEmailSentStatus;

    @Column(name = "min_cycles")
    private Integer minCycles;

    @Column(name = "max_cycles")
    private Integer maxCycles;

    @Column(name = "customer_first_name")
    private String customerFirstName;

    @Column(name = "customer_last_name")
    private String customerLastName;

    @Column(name = "auto_charge")
    private Boolean autoCharge;

    @Column(name = "imported_id")
    private String importedId;

    @Column(name = "stop_up_coming_order_email")
    private Boolean stopUpComingOrderEmail;

    @Column(name = "paused_from_active")
    private Boolean pausedFromActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_created_sms_sent_status")
    private SubscriptionCreatedSmsSentStatus subscriptionCreatedSmsSentStatus;

    @Column(name = "phone")
    private String phone;

    @Column(name = "activated_on")
    private ZonedDateTime activatedOn;

    @Column(name = "paused_on")
    private ZonedDateTime pausedOn;

    @Column(name = "cancelled_on")
    private ZonedDateTime cancelledOn;

    @Lob
    @Column(name = "contract_details_json")
    private String contractDetailsJSON;

    @Lob
    @Column(name = "cancellation_feedback")
    private String cancellationFeedback;

    @Lob
    @Column(name = "order_note")
    private String orderNote;

    @Lob
    @Column(name = "order_note_attributes")
    private String orderNoteAttributes;

    @Column(name = "allow_delivery_price_override")
    private Boolean allowDeliveryPriceOverride;

    @Column(name = "disable_fix_empty_queue")
    private Boolean disableFixEmptyQueue;

    @Column(name = "order_amount_usd")
    private Double orderAmountUSD;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_type")
    private SubscriptionOriginType originType;

    @Column(name = "original_contract_id")
    private Long originalContractId;

    @Column(name = "cancellation_note")
    private String cancellationNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(name = "subscription_type_identifier")
    private String subscriptionTypeIdentifier;

    @Column(name = "upcoming_email_buffer_days")
    private Integer upcomingEmailBufferDays;

    @Column(name = "upcoming_email_task_url")
    private String upcomingEmailTaskUrl;

    @Column(name = "contract_amount")
    private Double contractAmount;

    @Column(name = "contract_amount_usd")
    private Double contractAmountUSD;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public SubscriptionContractDetails shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getGraphSubscriptionContractId() {
        return graphSubscriptionContractId;
    }

    public SubscriptionContractDetails graphSubscriptionContractId(String graphSubscriptionContractId) {
        this.graphSubscriptionContractId = graphSubscriptionContractId;
        return this;
    }

    public void setGraphSubscriptionContractId(String graphSubscriptionContractId) {
        this.graphSubscriptionContractId = graphSubscriptionContractId;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public SubscriptionContractDetails subscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
        return this;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public String getBillingPolicyInterval() {
        return billingPolicyInterval;
    }

    public SubscriptionContractDetails billingPolicyInterval(String billingPolicyInterval) {
        this.billingPolicyInterval = billingPolicyInterval;
        return this;
    }

    public void setBillingPolicyInterval(String billingPolicyInterval) {
        this.billingPolicyInterval = billingPolicyInterval;
    }

    public Integer getBillingPolicyIntervalCount() {
        return billingPolicyIntervalCount;
    }

    public SubscriptionContractDetails billingPolicyIntervalCount(Integer billingPolicyIntervalCount) {
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
        return this;
    }

    public void setBillingPolicyIntervalCount(Integer billingPolicyIntervalCount) {
        this.billingPolicyIntervalCount = billingPolicyIntervalCount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public SubscriptionContractDetails currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public SubscriptionContractDetails customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getGraphCustomerId() {
        return graphCustomerId;
    }

    public SubscriptionContractDetails graphCustomerId(String graphCustomerId) {
        this.graphCustomerId = graphCustomerId;
        return this;
    }

    public void setGraphCustomerId(String graphCustomerId) {
        this.graphCustomerId = graphCustomerId;
    }

    public String getDeliveryPolicyInterval() {
        return deliveryPolicyInterval;
    }

    public SubscriptionContractDetails deliveryPolicyInterval(String deliveryPolicyInterval) {
        this.deliveryPolicyInterval = deliveryPolicyInterval;
        return this;
    }

    public void setDeliveryPolicyInterval(String deliveryPolicyInterval) {
        this.deliveryPolicyInterval = deliveryPolicyInterval;
    }

    public Integer getDeliveryPolicyIntervalCount() {
        return deliveryPolicyIntervalCount;
    }

    public SubscriptionContractDetails deliveryPolicyIntervalCount(Integer deliveryPolicyIntervalCount) {
        this.deliveryPolicyIntervalCount = deliveryPolicyIntervalCount;
        return this;
    }

    public void setDeliveryPolicyIntervalCount(Integer deliveryPolicyIntervalCount) {
        this.deliveryPolicyIntervalCount = deliveryPolicyIntervalCount;
    }

    public String getStatus() {
        return status;
    }

    public SubscriptionContractDetails status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGraphOrderId() {
        return graphOrderId;
    }

    public SubscriptionContractDetails graphOrderId(String graphOrderId) {
        this.graphOrderId = graphOrderId;
        return this;
    }

    public void setGraphOrderId(String graphOrderId) {
        this.graphOrderId = graphOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public SubscriptionContractDetails orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public SubscriptionContractDetails createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SubscriptionContractDetails updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getNextBillingDate() {
        return nextBillingDate;
    }

    public SubscriptionContractDetails nextBillingDate(ZonedDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
        return this;
    }

    public void setNextBillingDate(ZonedDateTime nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public SubscriptionContractDetails orderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderName() {
        return orderName;
    }

    public SubscriptionContractDetails orderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public SubscriptionContractDetails customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public SubscriptionContractDetails customerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Boolean isSubscriptionCreatedEmailSent() {
        return subscriptionCreatedEmailSent;
    }

    public SubscriptionContractDetails subscriptionCreatedEmailSent(Boolean subscriptionCreatedEmailSent) {
        this.subscriptionCreatedEmailSent = subscriptionCreatedEmailSent;
        return this;
    }

    public void setSubscriptionCreatedEmailSent(Boolean subscriptionCreatedEmailSent) {
        this.subscriptionCreatedEmailSent = subscriptionCreatedEmailSent;
    }

    public ZonedDateTime getEndsAt() {
        return endsAt;
    }

    public SubscriptionContractDetails endsAt(ZonedDateTime endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public void setEndsAt(ZonedDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public ZonedDateTime getStartsAt() {
        return startsAt;
    }

    public SubscriptionContractDetails startsAt(ZonedDateTime startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public void setStartsAt(ZonedDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public subscriptionCreatedEmailSentStatus getSubscriptionCreatedEmailSentStatus() {
        return subscriptionCreatedEmailSentStatus;
    }

    public SubscriptionContractDetails subscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus subscriptionCreatedEmailSentStatus) {
        this.subscriptionCreatedEmailSentStatus = subscriptionCreatedEmailSentStatus;
        return this;
    }

    public void setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus subscriptionCreatedEmailSentStatus) {
        this.subscriptionCreatedEmailSentStatus = subscriptionCreatedEmailSentStatus;
    }

    public Integer getMinCycles() {
        return minCycles;
    }

    public SubscriptionContractDetails minCycles(Integer minCycles) {
        this.minCycles = minCycles;
        return this;
    }

    public void setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
    }

    public Integer getMaxCycles() {
        return maxCycles;
    }

    public SubscriptionContractDetails maxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
        return this;
    }

    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public SubscriptionContractDetails customerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
        return this;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public SubscriptionContractDetails customerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
        return this;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Boolean isAutoCharge() {
        return autoCharge;
    }

    public SubscriptionContractDetails autoCharge(Boolean autoCharge) {
        this.autoCharge = autoCharge;
        return this;
    }

    public void setAutoCharge(Boolean autoCharge) {
        this.autoCharge = autoCharge;
    }

    public String getImportedId() {
        return importedId;
    }

    public SubscriptionContractDetails importedId(String importedId) {
        this.importedId = importedId;
        return this;
    }

    public void setImportedId(String importedId) {
        this.importedId = importedId;
    }

    public Boolean isStopUpComingOrderEmail() {
        return stopUpComingOrderEmail;
    }

    public SubscriptionContractDetails stopUpComingOrderEmail(Boolean stopUpComingOrderEmail) {
        this.stopUpComingOrderEmail = stopUpComingOrderEmail;
        return this;
    }

    public void setStopUpComingOrderEmail(Boolean stopUpComingOrderEmail) {
        this.stopUpComingOrderEmail = stopUpComingOrderEmail;
    }

    public Boolean isPausedFromActive() {
        return pausedFromActive;
    }

    public SubscriptionContractDetails pausedFromActive(Boolean pausedFromActive) {
        this.pausedFromActive = pausedFromActive;
        return this;
    }

    public void setPausedFromActive(Boolean pausedFromActive) {
        this.pausedFromActive = pausedFromActive;
    }

    public SubscriptionCreatedSmsSentStatus getSubscriptionCreatedSmsSentStatus() {
        return subscriptionCreatedSmsSentStatus;
    }

    public SubscriptionContractDetails subscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatus subscriptionCreatedSmsSentStatus) {
        this.subscriptionCreatedSmsSentStatus = subscriptionCreatedSmsSentStatus;
        return this;
    }

    public void setSubscriptionCreatedSmsSentStatus(SubscriptionCreatedSmsSentStatus subscriptionCreatedSmsSentStatus) {
        this.subscriptionCreatedSmsSentStatus = subscriptionCreatedSmsSentStatus;
    }

    public String getPhone() {
        return phone;
    }

    public SubscriptionContractDetails phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ZonedDateTime getActivatedOn() {
        return activatedOn;
    }

    public SubscriptionContractDetails activatedOn(ZonedDateTime activatedOn) {
        this.activatedOn = activatedOn;
        return this;
    }

    public void setActivatedOn(ZonedDateTime activatedOn) {
        this.activatedOn = activatedOn;
    }

    public ZonedDateTime getPausedOn() {
        return pausedOn;
    }

    public SubscriptionContractDetails pausedOn(ZonedDateTime pausedOn) {
        this.pausedOn = pausedOn;
        return this;
    }

    public void setPausedOn(ZonedDateTime pausedOn) {
        this.pausedOn = pausedOn;
    }

    public ZonedDateTime getCancelledOn() {
        return cancelledOn;
    }

    public SubscriptionContractDetails cancelledOn(ZonedDateTime cancelledOn) {
        this.cancelledOn = cancelledOn;
        return this;
    }

    public void setCancelledOn(ZonedDateTime cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public String getContractDetailsJSON() {
        return contractDetailsJSON;
    }

    public SubscriptionContractDetails contractDetailsJSON(String contractDetailsJSON) {
        this.contractDetailsJSON = contractDetailsJSON;
        return this;
    }

    public void setContractDetailsJSON(String contractDetailsJSON) {
        this.contractDetailsJSON = contractDetailsJSON;
    }

    public String getCancellationFeedback() {
        return cancellationFeedback;
    }

    public SubscriptionContractDetails cancellationFeedback(String cancellationFeedback) {
        this.cancellationFeedback = cancellationFeedback;
        return this;
    }

    public void setCancellationFeedback(String cancellationFeedback) {
        this.cancellationFeedback = cancellationFeedback;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public SubscriptionContractDetails orderNote(String orderNote) {
        this.orderNote = orderNote;
        return this;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getOrderNoteAttributes() {
        return orderNoteAttributes;
    }

    public SubscriptionContractDetails orderNoteAttributes(String orderNoteAttributes) {
        this.orderNoteAttributes = orderNoteAttributes;
        return this;
    }

    public void setOrderNoteAttributes(String orderNoteAttributes) {
        this.orderNoteAttributes = orderNoteAttributes;
    }

    public Boolean isAllowDeliveryPriceOverride() {
        return allowDeliveryPriceOverride;
    }

    public SubscriptionContractDetails allowDeliveryPriceOverride(Boolean allowDeliveryPriceOverride) {
        this.allowDeliveryPriceOverride = allowDeliveryPriceOverride;
        return this;
    }

    public void setAllowDeliveryPriceOverride(Boolean allowDeliveryPriceOverride) {
        this.allowDeliveryPriceOverride = allowDeliveryPriceOverride;
    }

    public Boolean isDisableFixEmptyQueue() {
        return disableFixEmptyQueue;
    }

    public SubscriptionContractDetails disableFixEmptyQueue(Boolean disableFixEmptyQueue) {
        this.disableFixEmptyQueue = disableFixEmptyQueue;
        return this;
    }

    public void setDisableFixEmptyQueue(Boolean disableFixEmptyQueue) {
        this.disableFixEmptyQueue = disableFixEmptyQueue;
    }

    public Double getOrderAmountUSD() {
        return orderAmountUSD;
    }

    public SubscriptionContractDetails orderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
        return this;
    }

    public void setOrderAmountUSD(Double orderAmountUSD) {
        this.orderAmountUSD = orderAmountUSD;
    }

    public SubscriptionOriginType getOriginType() {
        return originType;
    }

    public SubscriptionContractDetails originType(SubscriptionOriginType originType) {
        this.originType = originType;
        return this;
    }

    public void setOriginType(SubscriptionOriginType originType) {
        this.originType = originType;
    }

    public Long getOriginalContractId() {
        return originalContractId;
    }

    public SubscriptionContractDetails originalContractId(Long originalContractId) {
        this.originalContractId = originalContractId;
        return this;
    }

    public void setOriginalContractId(Long originalContractId) {
        this.originalContractId = originalContractId;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public SubscriptionContractDetails cancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
        return this;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public SubscriptionContractDetails subscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
        return this;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionTypeIdentifier() {
        return subscriptionTypeIdentifier;
    }

    public SubscriptionContractDetails subscriptionTypeIdentifier(String subscriptionTypeIdentifier) {
        this.subscriptionTypeIdentifier = subscriptionTypeIdentifier;
        return this;
    }

    public void setSubscriptionTypeIdentifier(String subscriptionTypeIdentifier) {
        this.subscriptionTypeIdentifier = subscriptionTypeIdentifier;
    }

    public Integer getUpcomingEmailBufferDays() {
        return upcomingEmailBufferDays;
    }

    public SubscriptionContractDetails upcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
        return this;
    }

    public void setUpcomingEmailBufferDays(Integer upcomingEmailBufferDays) {
        this.upcomingEmailBufferDays = upcomingEmailBufferDays;
    }

    public String getUpcomingEmailTaskUrl() {
        return upcomingEmailTaskUrl;
    }

    public SubscriptionContractDetails upcomingEmailTaskUrl(String upcomingEmailTaskUrl) {
        this.upcomingEmailTaskUrl = upcomingEmailTaskUrl;
        return this;
    }

    public void setUpcomingEmailTaskUrl(String upcomingEmailTaskUrl) {
        this.upcomingEmailTaskUrl = upcomingEmailTaskUrl;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public SubscriptionContractDetails contractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
        return this;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Double getContractAmountUSD() {
        return contractAmountUSD;
    }

    public SubscriptionContractDetails contractAmountUSD(Double contractAmountUSD) {
        this.contractAmountUSD = contractAmountUSD;
        return this;
    }

    public void setContractAmountUSD(Double contractAmountUSD) {
        this.contractAmountUSD = contractAmountUSD;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractDetails)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionContractDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractDetails{" +
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
