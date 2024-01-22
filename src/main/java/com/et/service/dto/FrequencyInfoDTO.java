package com.et.service.dto;

import com.et.domain.enumeration.DiscountTypeUnit;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.shopify.java.graphql.client.type.SellingPlanRecurringDeliveryPolicyPreAnchorBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FrequencyInfoDTO {

    private Integer frequencyCount;
    private FrequencyIntervalUnit frequencyInterval;

    private Integer billingFrequencyCount;
    private FrequencyIntervalUnit billingFrequencyInterval;

    private String frequencyName;

    private String frequencyDescription;
    private Double discountOffer;
    private Double discountOffer2;

    private Integer afterCycle1;
    private Integer afterCycle2;

    private DiscountTypeUnit discountType;
    private DiscountTypeUnit discountType2;

    private Boolean discountEnabled;
    private Boolean discountEnabled2;

    private Boolean discountEnabledMasked;
    private Boolean discountEnabled2Masked;

    private String id;

    private FrequencyType frequencyType = FrequencyType.ON_PURCHASE_DAY;
    private Integer specificDayValue;
    private Integer specificMonthValue;

    private boolean specificDayEnabled;
    private Integer maxCycles;
    private Integer minCycles;
    private Integer cutOff;

    private String prepaidFlag;
    private String idNew;
    private PlanType planType;
    private SellingPlanRecurringDeliveryPolicyPreAnchorBehavior deliveryPolicyPreAnchorBehavior = SellingPlanRecurringDeliveryPolicyPreAnchorBehavior.ASAP;

    private boolean freeTrialEnabled;
    private Integer freeTrialCount;
    private FrequencyIntervalUnit freeTrialInterval;

    private Boolean memberOnly;
    private Boolean nonMemberOnly;
    private String memberInclusiveTags;
    private String memberExclusiveTags;

    private String formFieldJson;

    private Integer upcomingOrderEmailBuffer;

    private Integer frequencySequence;

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getUpcomingOrderEmailBuffer() {
        return upcomingOrderEmailBuffer;
    }

    public void setUpcomingOrderEmailBuffer(Integer upcomingOrderEmailBuffer) {
        this.upcomingOrderEmailBuffer = upcomingOrderEmailBuffer;
    }

    public Integer getFrequencySequence() {
        return frequencySequence;
    }

    public void setFrequencySequence(Integer frequencySequence) {
        this.frequencySequence = frequencySequence;
    }

    private List<AppstleCycle> appstleCycles = new ArrayList<>();

    public String getPrepaidFlag() {
        return prepaidFlag;
    }

    public void setPrepaidFlag(String prepaidFlag) {
        this.prepaidFlag = prepaidFlag;
    }

    public Integer getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(Integer frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public FrequencyIntervalUnit getFrequencyInterval() {
        return frequencyInterval;
    }

    public void setFrequencyInterval(FrequencyIntervalUnit frequencyInterval) {
        this.frequencyInterval = frequencyInterval;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getFrequencyDescription() {
        return frequencyDescription;
    }

    public void setFrequencyDescription(String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
    }

    public Double getDiscountOffer() {
        return discountOffer;
    }

    public void setDiscountOffer(Double discountOffer) {
        this.discountOffer = discountOffer;
    }

    public DiscountTypeUnit getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeUnit discountType) {
        this.discountType = discountType;
    }

    public Boolean getDiscountEnabled() {
        return discountEnabled;
    }

    public void setDiscountEnabled(Boolean discountEnabled) {
        this.discountEnabled = discountEnabled;
    }

    public void setId(String sellingPlanId) {
        this.id = sellingPlanId;
    }

    public String getId() {
        return id;
    }

    public FrequencyType getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public Integer getSpecificDayValue() {
        return specificDayValue;
    }

    public void setSpecificDayValue(Integer specificDayValue) {
        this.specificDayValue = specificDayValue;
    }

    public Integer getSpecificMonthValue() {
        return specificMonthValue;
    }

    public void setSpecificMonthValue(Integer specificMonthValue) {
        this.specificMonthValue = specificMonthValue;
    }

    public boolean isSpecificDayEnabled() {
        return specificDayEnabled;
    }

    public void setSpecificDayEnabled(boolean specificDayEnabled) {
        this.specificDayEnabled = specificDayEnabled;
    }

    public Integer getMaxCycles() {
        return maxCycles;
    }

    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
    }

    public Integer getMinCycles() {
        return minCycles;
    }

    public void setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
    }

    public Integer getCutOff() {
        return cutOff;
    }

    public void setCutOff(Integer cutOff) {
        this.cutOff = cutOff;
    }

    public Integer getBillingFrequencyCount() {
        return billingFrequencyCount;
    }

    public void setBillingFrequencyCount(Integer billingFrequencyCount) {
        this.billingFrequencyCount = billingFrequencyCount;
    }

    public FrequencyIntervalUnit getBillingFrequencyInterval() {
        return billingFrequencyInterval;
    }

    public void setBillingFrequencyInterval(FrequencyIntervalUnit billingFrequencyInterval) {
        this.billingFrequencyInterval = billingFrequencyInterval;
    }

    public void setIdNew(String idNew) {
        this.idNew = idNew;
    }

    public String getIdNew() {
        return idNew;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public Double getDiscountOffer2() {
        return discountOffer2;
    }

    public void setDiscountOffer2(Double discountOffer2) {
        this.discountOffer2 = discountOffer2;
    }

    public Integer getAfterCycle1() {
        return afterCycle1;
    }

    public void setAfterCycle1(Integer afterCycle1) {
        this.afterCycle1 = afterCycle1;
    }

    public Integer getAfterCycle2() {
        return afterCycle2;
    }

    public void setAfterCycle2(Integer afterCycle2) {
        this.afterCycle2 = afterCycle2;
    }

    public DiscountTypeUnit getDiscountType2() {
        return discountType2;
    }

    public void setDiscountType2(DiscountTypeUnit discountType2) {
        this.discountType2 = discountType2;
    }

    public Boolean getDiscountEnabled2() {
        return discountEnabled2;
    }

    public void setDiscountEnabled2(Boolean discountEnabled2) {
        this.discountEnabled2 = discountEnabled2;
    }

    public Boolean getDiscountEnabledMasked() {
        return discountEnabled;
    }

    public void setDiscountEnabledMasked(Boolean discountEnabledMasked) {
        this.discountEnabledMasked = discountEnabledMasked;
    }

    public Boolean getDiscountEnabled2Masked() {
        return discountEnabled2;
    }

    public void setDiscountEnabled2Masked(Boolean discountEnabled2Masked) {
        this.discountEnabled2Masked = discountEnabled2Masked;
    }

    public SellingPlanRecurringDeliveryPolicyPreAnchorBehavior getDeliveryPolicyPreAnchorBehavior() {
        return deliveryPolicyPreAnchorBehavior;
    }

    public void setDeliveryPolicyPreAnchorBehavior(SellingPlanRecurringDeliveryPolicyPreAnchorBehavior deliveryPolicyPreAnchorBehavior) {
        this.deliveryPolicyPreAnchorBehavior = deliveryPolicyPreAnchorBehavior;
    }
    public List<AppstleCycle> getAppstleCycles() {
        return appstleCycles;
    }

    public void setAppstleCycles(List<AppstleCycle> appstleCycles) {
        this.appstleCycles = appstleCycles;
    }

    public boolean isFreeTrialEnabled() {
        return freeTrialEnabled;
    }

    public void setFreeTrialEnabled(boolean freeTrialEnabled) {
        this.freeTrialEnabled = freeTrialEnabled;
    }

    public Integer getFreeTrialCount() {
        return freeTrialCount;
    }

    public void setFreeTrialCount(Integer freeTrialCount) {
        this.freeTrialCount = freeTrialCount;
    }

    public FrequencyIntervalUnit getFreeTrialInterval() {
        return freeTrialInterval;
    }

    public void setFreeTrialInterval(FrequencyIntervalUnit freeTrialInterval) {
        this.freeTrialInterval = freeTrialInterval;
    }

    public Boolean getMemberOnly() {
        return memberOnly;
    }

    public void setMemberOnly(Boolean memberOnly) {
        this.memberOnly = memberOnly;
    }

    public Boolean getNonMemberOnly() {
        return nonMemberOnly;
    }

    public void setNonMemberOnly(Boolean nonMemberOnly) {
        this.nonMemberOnly = nonMemberOnly;
    }

    public String getMemberInclusiveTags() {
        return memberInclusiveTags;
    }

    public void setMemberInclusiveTags(String memberInclusiveTags) {
        this.memberInclusiveTags = memberInclusiveTags;
    }

    public String getMemberExclusiveTags() {
        return memberExclusiveTags;
    }

    public void setMemberExclusiveTags(String memberExclusiveTags) {
        this.memberExclusiveTags = memberExclusiveTags;
    }

    public String getFormFieldJson() {
        return formFieldJson;
    }

    public void setFormFieldJson(String formFieldJson) {
        this.formFieldJson = formFieldJson;
    }

    @Override
    public String toString() {
        return "FrequencyInfoDTO{" +
            "frequencyCount=" + frequencyCount +
            ", frequencyInterval=" + frequencyInterval +
            ", billingFrequencyCount=" + billingFrequencyCount +
            ", billingFrequencyInterval=" + billingFrequencyInterval +
            ", frequencyName='" + frequencyName + '\'' +
            ", discountOffer=" + discountOffer +
            ", discountOffer2=" + discountOffer2 +
            ", afterCycle1=" + afterCycle1 +
            ", afterCycle2=" + afterCycle2 +
            ", discountType=" + discountType +
            ", discountType2=" + discountType2 +
            ", discountEnabled=" + discountEnabled +
            ", discountEnabled2=" + discountEnabled2 +
            ", discountEnabledMasked=" + discountEnabledMasked +
            ", discountEnabled2Masked=" + discountEnabled2Masked +
            ", id='" + id + '\'' +
            ", frequencyType=" + frequencyType +
            ", specificDayValue=" + specificDayValue +
            ", specificDayEnabled=" + specificDayEnabled +
            ", maxCycles=" + maxCycles +
            ", minCycles=" + minCycles +
            ", cutOff=" + cutOff +
            ", prepaidFlag='" + prepaidFlag + '\'' +
            ", idNew='" + idNew + '\'' +
            ", planType=" + planType +
            ", appstleCycles=" + appstleCycles +
            ", freeTrialEnabled=" + freeTrialEnabled +
            ", freeTrialCount=" + freeTrialCount +
            ", freeTrialInterval=" + freeTrialInterval +
            ", formFieldJson=" + formFieldJson +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequencyInfoDTO that = (FrequencyInfoDTO) o;
        return specificDayEnabled == that.specificDayEnabled &&
            freeTrialEnabled == that.freeTrialEnabled &&
            Objects.equals(frequencyCount, that.frequencyCount) &&
            frequencyInterval == that.frequencyInterval &&
            Objects.equals(billingFrequencyCount, that.billingFrequencyCount) &&
            billingFrequencyInterval == that.billingFrequencyInterval &&
            Objects.equals(discountOffer, that.discountOffer) &&
            Objects.equals(discountOffer2, that.discountOffer2) &&
            Objects.equals(afterCycle1, that.afterCycle1) &&
            Objects.equals(afterCycle2, that.afterCycle2) &&
            discountType == that.discountType &&
            discountType2 == that.discountType2 &&
            Objects.equals(discountEnabled, that.discountEnabled) &&
            Objects.equals(discountEnabled2, that.discountEnabled2) &&
            Objects.equals(discountEnabledMasked, that.discountEnabledMasked) &&
            Objects.equals(discountEnabled2Masked, that.discountEnabled2Masked) &&
            frequencyType == that.frequencyType &&
            Objects.equals(specificDayValue, that.specificDayValue) &&
            Objects.equals(specificMonthValue, that.specificMonthValue) &&
            Objects.equals(maxCycles, that.maxCycles) &&
            Objects.equals(minCycles, that.minCycles) &&
            Objects.equals(cutOff, that.cutOff) &&
            Objects.equals(prepaidFlag, that.prepaidFlag) &&
            planType == that.planType &&
            deliveryPolicyPreAnchorBehavior == that.deliveryPolicyPreAnchorBehavior &&
            Objects.equals(freeTrialCount, that.freeTrialCount) &&
            freeTrialInterval == that.freeTrialInterval &&
            Objects.equals(memberInclusiveTags, that.memberInclusiveTags) &&
            Objects.equals(memberExclusiveTags, that.memberExclusiveTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frequencyCount, frequencyInterval, billingFrequencyCount, billingFrequencyInterval, discountOffer, discountOffer2, afterCycle1, afterCycle2, discountType, discountType2, discountEnabled, discountEnabled2, discountEnabledMasked, discountEnabled2Masked, frequencyType, specificDayValue, specificMonthValue, specificDayEnabled, maxCycles, minCycles, cutOff, prepaidFlag, planType, deliveryPolicyPreAnchorBehavior, freeTrialEnabled, freeTrialCount, freeTrialInterval, memberOnly, nonMemberOnly, memberInclusiveTags, memberExclusiveTags, formFieldJson, appstleCycles);
    }
}
