package com.et.service.dto;

import com.et.domain.PlanInfo;

import java.time.ZonedDateTime;

public class PlanLimitInformation {

    private Integer activeSubscriptionCount;
    private Integer planLimit;
    private PlanInfo planInfo;
    private ZonedDateTime trialEndsOn;

    private Double usedOrderAmount;
    private Double orderAmountLimit;

    public Integer getActiveSubscriptionCount() {
        return activeSubscriptionCount;
    }

    public void setActiveSubscriptionCount(Integer activeSubscriptionCount) {
        this.activeSubscriptionCount = activeSubscriptionCount;
    }

    public Integer getPlanLimit() {
        return planLimit;
    }

    public void setPlanLimit(Integer planLimit) {
        this.planLimit = planLimit;
    }

    public PlanInfo getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(PlanInfo planInfo) {
        this.planInfo = planInfo;
    }

    public void setTrialEndsOn(ZonedDateTime trialEndsOn) {
        this.trialEndsOn = trialEndsOn;
    }

    public ZonedDateTime getTrialEndsOn() {
        return trialEndsOn;
    }

    public Double getUsedOrderAmount() {
        return usedOrderAmount;
    }

    public void setUsedOrderAmount(Double usedOrderAmount) {
        this.usedOrderAmount = usedOrderAmount;
    }

    public Double getOrderAmountLimit() {
        return orderAmountLimit;
    }

    public void setOrderAmountLimit(Double orderAmountLimit) {
        this.orderAmountLimit = orderAmountLimit;
    }
}
