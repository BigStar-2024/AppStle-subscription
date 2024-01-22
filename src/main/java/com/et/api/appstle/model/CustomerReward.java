package com.et.api.appstle.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerReward {
    private Long id;

    private String shop;

    private Long customerId;

    private String description;

    private Long pointTransactionId;

    private Long pointRedeemRuleId;

    private String discountCode;

    private ZonedDateTime usedAt;

    private Long orderId;

    private String orderName;

    private String status;

    private ZonedDateTime createAt;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPointTransactionId() {
        return pointTransactionId;
    }

    public void setPointTransactionId(Long pointTransactionId) {
        this.pointTransactionId = pointTransactionId;
    }

    public Long getPointRedeemRuleId() {
        return pointRedeemRuleId;
    }

    public void setPointRedeemRuleId(Long pointRedeemRuleId) {
        this.pointRedeemRuleId = pointRedeemRuleId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public ZonedDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(ZonedDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }
}
