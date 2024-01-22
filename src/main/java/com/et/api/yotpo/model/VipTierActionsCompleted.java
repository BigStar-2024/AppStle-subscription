package com.et.api.yotpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VipTierActionsCompleted {

    private Long points_earned;
    private Long amount_spent_cents;
    private Long amount_spent_cents_in_customer_currency;
    private Integer purchases_made;
    private Integer referrals_completed;
    private List<Long> campaigns_completed;

    public Long getPoints_earned() {
        return points_earned;
    }

    public void setPoints_earned(Long points_earned) {
        this.points_earned = points_earned;
    }

    public Long getAmount_spent_cents() {
        return amount_spent_cents;
    }

    public void setAmount_spent_cents(Long amount_spent_cents) {
        this.amount_spent_cents = amount_spent_cents;
    }

    public Long getAmount_spent_cents_in_customer_currency() {
        return amount_spent_cents_in_customer_currency;
    }

    public void setAmount_spent_cents_in_customer_currency(Long amount_spent_cents_in_customer_currency) {
        this.amount_spent_cents_in_customer_currency = amount_spent_cents_in_customer_currency;
    }

    public Integer getPurchases_made() {
        return purchases_made;
    }

    public void setPurchases_made(Integer purchases_made) {
        this.purchases_made = purchases_made;
    }

    public Integer getReferrals_completed() {
        return referrals_completed;
    }

    public void setReferrals_completed(Integer referrals_completed) {
        this.referrals_completed = referrals_completed;
    }

    public List<Long> getCampaigns_completed() {
        return campaigns_completed;
    }

    public void setCampaigns_completed(List<Long> campaigns_completed) {
        this.campaigns_completed = campaigns_completed;
    }
}
