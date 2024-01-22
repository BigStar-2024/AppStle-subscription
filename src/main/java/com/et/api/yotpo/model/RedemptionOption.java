package com.et.api.yotpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedemptionOption {

    private Long id;
    private String icon;
    private String cost_text;
    private String amount;
    private String applies_to_product_type;
    private String duration;
    private String type;
    private String discount_amount_cents;
    private String discount_rate_cents;
    private String discount_percentage;
    private String discount_type;
    private String discount_value_cents;
    private String name;
    private String description;
    private String unrendered_name;
    private String unrendered_description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCost_text() {
        return cost_text;
    }

    public void setCost_text(String cost_text) {
        this.cost_text = cost_text;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplies_to_product_type() {
        return applies_to_product_type;
    }

    public void setApplies_to_product_type(String applies_to_product_type) {
        this.applies_to_product_type = applies_to_product_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount_amount_cents() {
        return discount_amount_cents;
    }

    public void setDiscount_amount_cents(String discount_amount_cents) {
        this.discount_amount_cents = discount_amount_cents;
    }

    public String getDiscount_rate_cents() {
        return discount_rate_cents;
    }

    public void setDiscount_rate_cents(String discount_rate_cents) {
        this.discount_rate_cents = discount_rate_cents;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_value_cents() {
        return discount_value_cents;
    }

    public void setDiscount_value_cents(String discount_value_cents) {
        this.discount_value_cents = discount_value_cents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnrendered_name() {
        return unrendered_name;
    }

    public void setUnrendered_name(String unrendered_name) {
        this.unrendered_name = unrendered_name;
    }

    public String getUnrendered_description() {
        return unrendered_description;
    }

    public void setUnrendered_description(String unrendered_description) {
        this.unrendered_description = unrendered_description;
    }
}
