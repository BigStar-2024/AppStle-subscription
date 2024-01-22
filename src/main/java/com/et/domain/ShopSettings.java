package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.DelayedTaggingUnit;

import com.et.domain.enumeration.ShopSettingsOrderStatus;

import com.et.domain.enumeration.PaymentStatus;

import com.et.domain.enumeration.FulfillmentStatus;

/**
 * A ShopSettings.
 */
@Entity
@Table(name = "shop_settings")
public class ShopSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "tagging_enabled")
    private Boolean taggingEnabled;

    @Column(name = "delay_tagging")
    private Boolean delayTagging;

    @Column(name = "delayed_tagging_value")
    private Long delayedTaggingValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "delayed_tagging_unit")
    private DelayedTaggingUnit delayedTaggingUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private ShopSettingsOrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "fulfillment_status")
    private FulfillmentStatus fulfillmentStatus;

    @Column(name = "price_sync_enabled")
    private Boolean priceSyncEnabled;

    @Column(name = "sku_sync_enabled")
    private Boolean skuSyncEnabled;

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

    public ShopSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isTaggingEnabled() {
        return taggingEnabled;
    }

    public ShopSettings taggingEnabled(Boolean taggingEnabled) {
        this.taggingEnabled = taggingEnabled;
        return this;
    }

    public void setTaggingEnabled(Boolean taggingEnabled) {
        this.taggingEnabled = taggingEnabled;
    }

    public Boolean isDelayTagging() {
        return delayTagging;
    }

    public ShopSettings delayTagging(Boolean delayTagging) {
        this.delayTagging = delayTagging;
        return this;
    }

    public void setDelayTagging(Boolean delayTagging) {
        this.delayTagging = delayTagging;
    }

    public Long getDelayedTaggingValue() {
        return delayedTaggingValue;
    }

    public ShopSettings delayedTaggingValue(Long delayedTaggingValue) {
        this.delayedTaggingValue = delayedTaggingValue;
        return this;
    }

    public void setDelayedTaggingValue(Long delayedTaggingValue) {
        this.delayedTaggingValue = delayedTaggingValue;
    }

    public DelayedTaggingUnit getDelayedTaggingUnit() {
        return delayedTaggingUnit;
    }

    public ShopSettings delayedTaggingUnit(DelayedTaggingUnit delayedTaggingUnit) {
        this.delayedTaggingUnit = delayedTaggingUnit;
        return this;
    }

    public void setDelayedTaggingUnit(DelayedTaggingUnit delayedTaggingUnit) {
        this.delayedTaggingUnit = delayedTaggingUnit;
    }

    public ShopSettingsOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public ShopSettings orderStatus(ShopSettingsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(ShopSettingsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public ShopSettings paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public FulfillmentStatus getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public ShopSettings fulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
        return this;
    }

    public void setFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public Boolean isPriceSyncEnabled() {
        return priceSyncEnabled;
    }

    public ShopSettings priceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
        return this;
    }

    public void setPriceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
    }

    public Boolean isSkuSyncEnabled() {
        return skuSyncEnabled;
    }

    public ShopSettings skuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
        return this;
    }

    public void setSkuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopSettings)) {
            return false;
        }
        return id != null && id.equals(((ShopSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShopSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", taggingEnabled='" + isTaggingEnabled() + "'" +
            ", delayTagging='" + isDelayTagging() + "'" +
            ", delayedTaggingValue=" + getDelayedTaggingValue() +
            ", delayedTaggingUnit='" + getDelayedTaggingUnit() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", fulfillmentStatus='" + getFulfillmentStatus() + "'" +
            ", priceSyncEnabled='" + isPriceSyncEnabled() + "'" +
            ", skuSyncEnabled='" + isSkuSyncEnabled() + "'" +
            "}";
    }
}
