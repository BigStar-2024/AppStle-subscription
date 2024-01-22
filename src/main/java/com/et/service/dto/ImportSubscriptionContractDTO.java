package com.et.service.dto;

import javax.validation.constraints.NotNull;

public class ImportSubscriptionContractDTO {
    @NotNull
    private String id;

    @NotNull
    private String status;

    @NotNull
    private String deliveryFirstName;

    @NotNull
    private String deliveryLastName;

    @NotNull
    private String deliveryAddress1;

    @NotNull
    private String deliveryAddress2;

    @NotNull
    private String deliveryProvinceCode;

    private String deliveryCity;

    @NotNull
    private String deliveryZip;

    @NotNull
    private String deliveryCountryCode;

    @NotNull
    private String deliveryPhone;

    private String deliveryPriceAmount;

    @NotNull
    private String nextOrderDate;

    @NotNull
    private String billingIntervalType;

    @NotNull
    private String billingIntervalCount;

    @NotNull
    private String deliveryIntervalType;

    @NotNull
    private String deliveryIntervalCount;

    @NotNull
    private String lineZeroQuantity;

    @NotNull
    private String lineZeroPriceAmount;

    @NotNull
    private String lineZeroVariantID;

    @NotNull
    private String customerEmail;

    private String customerID;

    private String cardID;

    private String customerProfileID;

    private String lastFourDigits;

    @NotNull
    private String importType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryFirstName() {
        return deliveryFirstName;
    }

    public void setDeliveryFirstName(String deliveryFirstName) {
        this.deliveryFirstName = deliveryFirstName;
    }

    public String getDeliveryLastName() {
        return deliveryLastName;
    }

    public void setDeliveryLastName(String deliveryLastName) {
        this.deliveryLastName = deliveryLastName;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public void setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public void setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
    }

    public String getDeliveryProvinceCode() {
        return deliveryProvinceCode;
    }

    public void setDeliveryProvinceCode(String deliveryProvinceCode) {
        this.deliveryProvinceCode = deliveryProvinceCode;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public void setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
    }

    public String getDeliveryCountryCode() {
        return deliveryCountryCode;
    }

    public void setDeliveryCountryCode(String deliveryCountryCode) {
        this.deliveryCountryCode = deliveryCountryCode;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryPriceAmount() {
        return deliveryPriceAmount;
    }

    public void setDeliveryPriceAmount(String deliveryPriceAmount) {
        this.deliveryPriceAmount = deliveryPriceAmount;
    }

    public String getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(String nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public String getBillingIntervalType() {
        return billingIntervalType;
    }

    public void setBillingIntervalType(String billingIntervalType) {
        this.billingIntervalType = billingIntervalType;
    }

    public String getBillingIntervalCount() {
        return billingIntervalCount;
    }

    public void setBillingIntervalCount(String billingIntervalCount) {
        this.billingIntervalCount = billingIntervalCount;
    }

    public String getDeliveryIntervalType() {
        return deliveryIntervalType;
    }

    public void setDeliveryIntervalType(String deliveryIntervalType) {
        this.deliveryIntervalType = deliveryIntervalType;
    }

    public String getDeliveryIntervalCount() {
        return deliveryIntervalCount;
    }

    public void setDeliveryIntervalCount(String deliveryIntervalCount) {
        this.deliveryIntervalCount = deliveryIntervalCount;
    }

    public String getLineZeroQuantity() {
        return lineZeroQuantity;
    }

    public void setLineZeroQuantity(String lineZeroQuantity) {
        this.lineZeroQuantity = lineZeroQuantity;
    }

    public String getLineZeroPriceAmount() {
        return lineZeroPriceAmount;
    }

    public void setLineZeroPriceAmount(String lineZeroPriceAmount) {
        this.lineZeroPriceAmount = lineZeroPriceAmount;
    }

    public String getLineZeroVariantID() {
        return lineZeroVariantID;
    }

    public void setLineZeroVariantID(String lineZeroVariantID) {
        this.lineZeroVariantID = lineZeroVariantID;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getCustomerProfileID() {
        return customerProfileID;
    }

    public void setCustomerProfileID(String customerProfileID) {
        this.customerProfileID = customerProfileID;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }
}
