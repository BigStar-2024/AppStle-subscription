package com.et.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.BasePlan;

import com.et.domain.enumeration.PlanType;

import com.et.domain.enumeration.BillingType;

import com.et.domain.enumeration.BasedOn;

import com.et.domain.enumeration.PaymentPlanEvent;

/**
 * A PaymentPlan.
 */
@Entity
@Table(name = "payment_plan")
public class PaymentPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "charge_activated", nullable = false)
    private Boolean chargeActivated;

    @NotNull
    @Column(name = "activation_date", nullable = false)
    private ZonedDateTime activationDate;

    @Column(name = "recurring_charge_id")
    private Long recurringChargeId;

    @Column(name = "last_email_sent_to_merchant")
    private ZonedDateTime lastEmailSentToMerchant;

    @Column(name = "number_of_email_sent_to_merchant")
    private Integer numberOfEmailSentToMerchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_plan")
    private BasePlan basePlan;

    @Lob
    @Column(name = "additional_details")
    private String additionalDetails;

    @Column(name = "trial_days")
    private Integer trialDays;

    @Column(name = "price")
    private Double price;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type")
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_type")
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "based_on")
    private BasedOn basedOn;

    @Lob
    @Column(name = "features")
    private String features;

    @Column(name = "test_charge")
    private Boolean testCharge;

    @Column(name = "billed_date")
    private ZonedDateTime billedDate;

    @Column(name = "valid_charge")
    private Boolean validCharge;

    @Column(name = "trial_ends_on")
    private ZonedDateTime trialEndsOn;

    @Column(name = "shop_frozen")
    private Boolean shopFrozen;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_plan_event")
    private PaymentPlanEvent paymentPlanEvent;

    @Column(name = "payment_plan_event_time")
    private ZonedDateTime paymentPlanEventTime;

    @Column(name = "old_price")
    private Double oldPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_base_plan")
    private BasePlan oldBasePlan;

    @Column(name = "old_activation_date")
    private ZonedDateTime oldActivationDate;

    @Column(name = "shopify_plan_display_name")
    private String shopifyPlanDisplayName;

    @Column(name = "shopify_plan_name")
    private String shopifyPlanName;

    @ManyToOne
    @JsonIgnoreProperties("paymentPlans")
    private PlanInfo planInfo;

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

    public PaymentPlan shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isChargeActivated() {
        return chargeActivated;
    }

    public PaymentPlan chargeActivated(Boolean chargeActivated) {
        this.chargeActivated = chargeActivated;
        return this;
    }

    public void setChargeActivated(Boolean chargeActivated) {
        this.chargeActivated = chargeActivated;
    }

    public ZonedDateTime getActivationDate() {
        return activationDate;
    }

    public PaymentPlan activationDate(ZonedDateTime activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(ZonedDateTime activationDate) {
        this.activationDate = activationDate;
    }

    public Long getRecurringChargeId() {
        return recurringChargeId;
    }

    public PaymentPlan recurringChargeId(Long recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
        return this;
    }

    public void setRecurringChargeId(Long recurringChargeId) {
        this.recurringChargeId = recurringChargeId;
    }

    public ZonedDateTime getLastEmailSentToMerchant() {
        return lastEmailSentToMerchant;
    }

    public PaymentPlan lastEmailSentToMerchant(ZonedDateTime lastEmailSentToMerchant) {
        this.lastEmailSentToMerchant = lastEmailSentToMerchant;
        return this;
    }

    public void setLastEmailSentToMerchant(ZonedDateTime lastEmailSentToMerchant) {
        this.lastEmailSentToMerchant = lastEmailSentToMerchant;
    }

    public Integer getNumberOfEmailSentToMerchant() {
        return numberOfEmailSentToMerchant;
    }

    public PaymentPlan numberOfEmailSentToMerchant(Integer numberOfEmailSentToMerchant) {
        this.numberOfEmailSentToMerchant = numberOfEmailSentToMerchant;
        return this;
    }

    public void setNumberOfEmailSentToMerchant(Integer numberOfEmailSentToMerchant) {
        this.numberOfEmailSentToMerchant = numberOfEmailSentToMerchant;
    }

    public BasePlan getBasePlan() {
        return basePlan;
    }

    public PaymentPlan basePlan(BasePlan basePlan) {
        this.basePlan = basePlan;
        return this;
    }

    public void setBasePlan(BasePlan basePlan) {
        this.basePlan = basePlan;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public PaymentPlan additionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
        return this;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public PaymentPlan trialDays(Integer trialDays) {
        this.trialDays = trialDays;
        return this;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public Double getPrice() {
        return price;
    }

    public PaymentPlan price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public PaymentPlan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public PaymentPlan planType(PlanType planType) {
        this.planType = planType;
        return this;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public PaymentPlan billingType(BillingType billingType) {
        this.billingType = billingType;
        return this;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public BasedOn getBasedOn() {
        return basedOn;
    }

    public PaymentPlan basedOn(BasedOn basedOn) {
        this.basedOn = basedOn;
        return this;
    }

    public void setBasedOn(BasedOn basedOn) {
        this.basedOn = basedOn;
    }

    public String getFeatures() {
        return features;
    }

    public PaymentPlan features(String features) {
        this.features = features;
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Boolean isTestCharge() {
        return testCharge;
    }

    public PaymentPlan testCharge(Boolean testCharge) {
        this.testCharge = testCharge;
        return this;
    }

    public void setTestCharge(Boolean testCharge) {
        this.testCharge = testCharge;
    }

    public ZonedDateTime getBilledDate() {
        return billedDate;
    }

    public PaymentPlan billedDate(ZonedDateTime billedDate) {
        this.billedDate = billedDate;
        return this;
    }

    public void setBilledDate(ZonedDateTime billedDate) {
        this.billedDate = billedDate;
    }

    public Boolean isValidCharge() {
        return validCharge;
    }

    public PaymentPlan validCharge(Boolean validCharge) {
        this.validCharge = validCharge;
        return this;
    }

    public void setValidCharge(Boolean validCharge) {
        this.validCharge = validCharge;
    }

    public ZonedDateTime getTrialEndsOn() {
        return trialEndsOn;
    }

    public PaymentPlan trialEndsOn(ZonedDateTime trialEndsOn) {
        this.trialEndsOn = trialEndsOn;
        return this;
    }

    public void setTrialEndsOn(ZonedDateTime trialEndsOn) {
        this.trialEndsOn = trialEndsOn;
    }

    public Boolean isShopFrozen() {
        return shopFrozen;
    }

    public PaymentPlan shopFrozen(Boolean shopFrozen) {
        this.shopFrozen = shopFrozen;
        return this;
    }

    public void setShopFrozen(Boolean shopFrozen) {
        this.shopFrozen = shopFrozen;
    }

    public PaymentPlanEvent getPaymentPlanEvent() {
        return paymentPlanEvent;
    }

    public PaymentPlan paymentPlanEvent(PaymentPlanEvent paymentPlanEvent) {
        this.paymentPlanEvent = paymentPlanEvent;
        return this;
    }

    public void setPaymentPlanEvent(PaymentPlanEvent paymentPlanEvent) {
        this.paymentPlanEvent = paymentPlanEvent;
    }

    public ZonedDateTime getPaymentPlanEventTime() {
        return paymentPlanEventTime;
    }

    public PaymentPlan paymentPlanEventTime(ZonedDateTime paymentPlanEventTime) {
        this.paymentPlanEventTime = paymentPlanEventTime;
        return this;
    }

    public void setPaymentPlanEventTime(ZonedDateTime paymentPlanEventTime) {
        this.paymentPlanEventTime = paymentPlanEventTime;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public PaymentPlan oldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
        return this;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BasePlan getOldBasePlan() {
        return oldBasePlan;
    }

    public PaymentPlan oldBasePlan(BasePlan oldBasePlan) {
        this.oldBasePlan = oldBasePlan;
        return this;
    }

    public void setOldBasePlan(BasePlan oldBasePlan) {
        this.oldBasePlan = oldBasePlan;
    }

    public ZonedDateTime getOldActivationDate() {
        return oldActivationDate;
    }

    public PaymentPlan oldActivationDate(ZonedDateTime oldActivationDate) {
        this.oldActivationDate = oldActivationDate;
        return this;
    }

    public void setOldActivationDate(ZonedDateTime oldActivationDate) {
        this.oldActivationDate = oldActivationDate;
    }

    public String getShopifyPlanDisplayName() {
        return shopifyPlanDisplayName;
    }

    public PaymentPlan shopifyPlanDisplayName(String shopifyPlanDisplayName) {
        this.shopifyPlanDisplayName = shopifyPlanDisplayName;
        return this;
    }

    public void setShopifyPlanDisplayName(String shopifyPlanDisplayName) {
        this.shopifyPlanDisplayName = shopifyPlanDisplayName;
    }

    public String getShopifyPlanName() {
        return shopifyPlanName;
    }

    public PaymentPlan shopifyPlanName(String shopifyPlanName) {
        this.shopifyPlanName = shopifyPlanName;
        return this;
    }

    public void setShopifyPlanName(String shopifyPlanName) {
        this.shopifyPlanName = shopifyPlanName;
    }

    public PlanInfo getPlanInfo() {
        return planInfo;
    }

    public PaymentPlan planInfo(PlanInfo planInfo) {
        this.planInfo = planInfo;
        return this;
    }

    public void setPlanInfo(PlanInfo planInfo) {
        this.planInfo = planInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentPlan)) {
            return false;
        }
        return id != null && id.equals(((PaymentPlan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PaymentPlan{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", chargeActivated='" + isChargeActivated() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", recurringChargeId=" + getRecurringChargeId() +
            ", lastEmailSentToMerchant='" + getLastEmailSentToMerchant() + "'" +
            ", numberOfEmailSentToMerchant=" + getNumberOfEmailSentToMerchant() +
            ", basePlan='" + getBasePlan() + "'" +
            ", additionalDetails='" + getAdditionalDetails() + "'" +
            ", trialDays=" + getTrialDays() +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", planType='" + getPlanType() + "'" +
            ", billingType='" + getBillingType() + "'" +
            ", basedOn='" + getBasedOn() + "'" +
            ", features='" + getFeatures() + "'" +
            ", testCharge='" + isTestCharge() + "'" +
            ", billedDate='" + getBilledDate() + "'" +
            ", validCharge='" + isValidCharge() + "'" +
            ", trialEndsOn='" + getTrialEndsOn() + "'" +
            ", shopFrozen='" + isShopFrozen() + "'" +
            ", paymentPlanEvent='" + getPaymentPlanEvent() + "'" +
            ", paymentPlanEventTime='" + getPaymentPlanEventTime() + "'" +
            ", oldPrice=" + getOldPrice() +
            ", oldBasePlan='" + getOldBasePlan() + "'" +
            ", oldActivationDate='" + getOldActivationDate() + "'" +
            ", shopifyPlanDisplayName='" + getShopifyPlanDisplayName() + "'" +
            ", shopifyPlanName='" + getShopifyPlanName() + "'" +
            "}";
    }
}
