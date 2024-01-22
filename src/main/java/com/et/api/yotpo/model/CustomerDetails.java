package com.et.api.yotpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDetails {
    private Long total_spend_cents;
    private Integer total_purchases;
    private Integer perks_redeemed;
    private ZonedDateTime last_purchase_at;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private Long points_balance;
    private Long points_earned;
    private ZonedDateTime last_seen_at;
    private String thirty_party_id;
    private String third_party_id;
    private String pos_account_id;
    private Boolean has_store_account;
    private String credit_balance;
    private String credit_balance_in_customer_currency;
    private Boolean opt_in;
    private String opted_in_at;
    private String points_expire_at;
    private VipTierActionsCompleted vip_tier_actions_completed;
    private VipTierUpgradeRequirements vip_tier_upgrade_requirements;

    public Long getTotal_spend_cents() {
        return total_spend_cents;
    }

    public void setTotal_spend_cents(Long total_spend_cents) {
        this.total_spend_cents = total_spend_cents;
    }

    public Integer getTotal_purchases() {
        return total_purchases;
    }

    public void setTotal_purchases(Integer total_purchases) {
        this.total_purchases = total_purchases;
    }

    public Integer getPerks_redeemed() {
        return perks_redeemed;
    }

    public void setPerks_redeemed(Integer perks_redeemed) {
        this.perks_redeemed = perks_redeemed;
    }

    public ZonedDateTime getLast_purchase_at() {
        return last_purchase_at;
    }

    public void setLast_purchase_at(ZonedDateTime last_purchase_at) {
        this.last_purchase_at = last_purchase_at;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPoints_balance() {
        return points_balance;
    }

    public void setPoints_balance(Long points_balance) {
        this.points_balance = points_balance;
    }

    public Long getPoints_earned() {
        return points_earned;
    }

    public void setPoints_earned(Long points_earned) {
        this.points_earned = points_earned;
    }

    public ZonedDateTime getLast_seen_at() {
        return last_seen_at;
    }

    public void setLast_seen_at(ZonedDateTime last_seen_at) {
        this.last_seen_at = last_seen_at;
    }

    public String getThirty_party_id() {
        return thirty_party_id;
    }

    public void setThirty_party_id(String thirty_party_id) {
        this.thirty_party_id = thirty_party_id;
    }

    public String getThird_party_id() {
        return third_party_id;
    }

    public void setThird_party_id(String third_party_id) {
        this.third_party_id = third_party_id;
    }

    public String getPos_account_id() {
        return pos_account_id;
    }

    public void setPos_account_id(String pos_account_id) {
        this.pos_account_id = pos_account_id;
    }

    public Boolean getHas_store_account() {
        return has_store_account;
    }

    public void setHas_store_account(Boolean has_store_account) {
        this.has_store_account = has_store_account;
    }

    public String getCredit_balance() {
        return credit_balance;
    }

    public void setCredit_balance(String credit_balance) {
        this.credit_balance = credit_balance;
    }

    public String getCredit_balance_in_customer_currency() {
        return credit_balance_in_customer_currency;
    }

    public void setCredit_balance_in_customer_currency(String credit_balance_in_customer_currency) {
        this.credit_balance_in_customer_currency = credit_balance_in_customer_currency;
    }

    public Boolean getOpt_in() {
        return opt_in;
    }

    public void setOpt_in(Boolean opt_in) {
        this.opt_in = opt_in;
    }

    public String getOpted_in_at() {
        return opted_in_at;
    }

    public void setOpted_in_at(String opted_in_at) {
        this.opted_in_at = opted_in_at;
    }

    public String getPoints_expire_at() {
        return points_expire_at;
    }

    public void setPoints_expire_at(String points_expire_at) {
        this.points_expire_at = points_expire_at;
    }

    public VipTierActionsCompleted getVip_tier_actions_completed() {
        return vip_tier_actions_completed;
    }

    public void setVip_tier_actions_completed(VipTierActionsCompleted vip_tier_actions_completed) {
        this.vip_tier_actions_completed = vip_tier_actions_completed;
    }

    public VipTierUpgradeRequirements getVip_tier_upgrade_requirements() {
        return vip_tier_upgrade_requirements;
    }

    public void setVip_tier_upgrade_requirements(VipTierUpgradeRequirements vip_tier_upgrade_requirements) {
        this.vip_tier_upgrade_requirements = vip_tier_upgrade_requirements;
    }
}
