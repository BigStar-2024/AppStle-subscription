package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.et.domain.enumeration.DelayedTaggingUnit;
import com.et.domain.enumeration.ShopSettingsOrderStatus;
import com.et.domain.enumeration.PaymentStatus;
import com.et.domain.enumeration.FulfillmentStatus;

/**
 * A DTO for the {@link com.et.domain.ShopSettings} entity.
 */
public class ShopSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private Boolean taggingEnabled;

    private Boolean delayTagging;

    private Long delayedTaggingValue;

    private DelayedTaggingUnit delayedTaggingUnit;

    private ShopSettingsOrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private FulfillmentStatus fulfillmentStatus;

    private Boolean priceSyncEnabled;

    private Boolean skuSyncEnabled;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isTaggingEnabled() {
        return taggingEnabled;
    }

    public void setTaggingEnabled(Boolean taggingEnabled) {
        this.taggingEnabled = taggingEnabled;
    }

    public Boolean isDelayTagging() {
        return delayTagging;
    }

    public void setDelayTagging(Boolean delayTagging) {
        this.delayTagging = delayTagging;
    }

    public Long getDelayedTaggingValue() {
        return delayedTaggingValue;
    }

    public void setDelayedTaggingValue(Long delayedTaggingValue) {
        this.delayedTaggingValue = delayedTaggingValue;
    }

    public DelayedTaggingUnit getDelayedTaggingUnit() {
        return delayedTaggingUnit;
    }

    public void setDelayedTaggingUnit(DelayedTaggingUnit delayedTaggingUnit) {
        this.delayedTaggingUnit = delayedTaggingUnit;
    }

    public ShopSettingsOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ShopSettingsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public FulfillmentStatus getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    public Boolean isPriceSyncEnabled() {
        return priceSyncEnabled;
    }

    public void setPriceSyncEnabled(Boolean priceSyncEnabled) {
        this.priceSyncEnabled = priceSyncEnabled;
    }

    public Boolean isSkuSyncEnabled() {
        return skuSyncEnabled;
    }

    public void setSkuSyncEnabled(Boolean skuSyncEnabled) {
        this.skuSyncEnabled = skuSyncEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopSettingsDTO shopSettingsDTO = (ShopSettingsDTO) o;
        if (shopSettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopSettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopSettingsDTO{" +
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
