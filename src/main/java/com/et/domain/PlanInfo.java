package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.et.domain.enumeration.PlanType;

import com.et.domain.enumeration.BillingType;

import com.et.domain.enumeration.BasedOn;

import com.et.domain.enumeration.BasePlan;

/**
 * A PlanInfo.
 */
@Entity
@Table(name = "plan_info")
public class PlanInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "archived")
    private Boolean archived;

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
    @Column(name = "additional_details")
    private String additionalDetails;

    @Lob
    @Column(name = "features")
    private String features;

    @Column(name = "trial_days")
    private Integer trialDays;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "base_plan", nullable = false)
    private BasePlan basePlan;

    @OneToMany(mappedBy = "planInfo")
    private Set<PaymentPlan> paymentPlans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PlanInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public PlanInfo price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isArchived() {
        return archived;
    }

    public PlanInfo archived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public PlanInfo planType(PlanType planType) {
        this.planType = planType;
        return this;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public PlanInfo billingType(BillingType billingType) {
        this.billingType = billingType;
        return this;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public BasedOn getBasedOn() {
        return basedOn;
    }

    public PlanInfo basedOn(BasedOn basedOn) {
        this.basedOn = basedOn;
        return this;
    }

    public void setBasedOn(BasedOn basedOn) {
        this.basedOn = basedOn;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public PlanInfo additionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
        return this;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getFeatures() {
        return features;
    }

    public PlanInfo features(String features) {
        this.features = features;
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public PlanInfo trialDays(Integer trialDays) {
        this.trialDays = trialDays;
        return this;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public BasePlan getBasePlan() {
        return basePlan;
    }

    public PlanInfo basePlan(BasePlan basePlan) {
        this.basePlan = basePlan;
        return this;
    }

    public void setBasePlan(BasePlan basePlan) {
        this.basePlan = basePlan;
    }

    public Set<PaymentPlan> getPaymentPlans() {
        return paymentPlans;
    }

    public PlanInfo paymentPlans(Set<PaymentPlan> paymentPlans) {
        this.paymentPlans = paymentPlans;
        return this;
    }

    public PlanInfo addPaymentPlan(PaymentPlan paymentPlan) {
        this.paymentPlans.add(paymentPlan);
        paymentPlan.setPlanInfo(this);
        return this;
    }

    public PlanInfo removePaymentPlan(PaymentPlan paymentPlan) {
        this.paymentPlans.remove(paymentPlan);
        paymentPlan.setPlanInfo(null);
        return this;
    }

    public void setPaymentPlans(Set<PaymentPlan> paymentPlans) {
        this.paymentPlans = paymentPlans;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanInfo)) {
            return false;
        }
        return id != null && id.equals(((PlanInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlanInfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", archived='" + isArchived() + "'" +
            ", planType='" + getPlanType() + "'" +
            ", billingType='" + getBillingType() + "'" +
            ", basedOn='" + getBasedOn() + "'" +
            ", additionalDetails='" + getAdditionalDetails() + "'" +
            ", features='" + getFeatures() + "'" +
            ", trialDays=" + getTrialDays() +
            ", basePlan='" + getBasePlan() + "'" +
            "}";
    }
}
