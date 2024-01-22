package com.et.web.rest;

public class MigrationInputRequest {
    private String importType;
    private String headerMappingJsonString;
    private boolean isValidation;
    private String subscriptionDataS3Key;
    private String customerDataS3Key;
    private String shop;

    public MigrationInputRequest(String importType, String headerMappingJsonString, boolean isValidation, String shop) {
        this.importType = importType;
        this.headerMappingJsonString = headerMappingJsonString;
        this.isValidation = isValidation;
        this.shop = shop;
    }

    public MigrationInputRequest(String importType, String headerMappingJsonString, boolean isValidation, String subscriptionDataS3Key, String customerDataS3Key, String shop) {
        this.importType = importType;
        this.headerMappingJsonString = headerMappingJsonString;
        this.isValidation = isValidation;
        this.subscriptionDataS3Key = subscriptionDataS3Key;
        this.customerDataS3Key = customerDataS3Key;
        this.shop = shop;
    }

    public MigrationInputRequest() {

    }

    public String getImportType() {
        return importType;
    }

    public String getHeaderMappingJsonString() {
        return headerMappingJsonString;
    }

    public boolean isValidation() {
        return isValidation;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public void setHeaderMappingJsonString(String headerMappingJsonString) {
        this.headerMappingJsonString = headerMappingJsonString;
    }

    public void setValidation(boolean validation) {
        isValidation = validation;
    }

    public String getSubscriptionDataS3Key() {
        return subscriptionDataS3Key;
    }

    public void setSubscriptionDataS3Key(String subscriptionDataS3Key) {
        this.subscriptionDataS3Key = subscriptionDataS3Key;
    }

    public String getCustomerDataS3Key() {
        return customerDataS3Key;
    }

    public void setCustomerDataS3Key(String customerDataS3Key) {
        this.customerDataS3Key = customerDataS3Key;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}
