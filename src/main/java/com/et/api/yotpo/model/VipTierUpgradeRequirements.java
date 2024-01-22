package com.et.api.yotpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VipTierUpgradeRequirements {

    private Long points_needed;
    private Long amount_cents_needed;
    private Long amount_cents_needed_in_customer_currency;
    private Integer purchases_needed;
    private Integer referrals_needed;
    private List<Long> campaigns_needed;

    public Long getPoints_needed() {
        return points_needed;
    }

    public void setPoints_needed(Long points_needed) {
        this.points_needed = points_needed;
    }

    public Long getAmount_cents_needed() {
        return amount_cents_needed;
    }

    public void setAmount_cents_needed(Long amount_cents_needed) {
        this.amount_cents_needed = amount_cents_needed;
    }

    public Long getAmount_cents_needed_in_customer_currency() {
        return amount_cents_needed_in_customer_currency;
    }

    public void setAmount_cents_needed_in_customer_currency(Long amount_cents_needed_in_customer_currency) {
        this.amount_cents_needed_in_customer_currency = amount_cents_needed_in_customer_currency;
    }

    public Integer getPurchases_needed() {
        return purchases_needed;
    }

    public void setPurchases_needed(Integer purchases_needed) {
        this.purchases_needed = purchases_needed;
    }

    public Integer getReferrals_needed() {
        return referrals_needed;
    }

    public void setReferrals_needed(Integer referrals_needed) {
        this.referrals_needed = referrals_needed;
    }

    public List<Long> getCampaigns_needed() {
        return campaigns_needed;
    }

    public void setCampaigns_needed(List<Long> campaigns_needed) {
        this.campaigns_needed = campaigns_needed;
    }
}
