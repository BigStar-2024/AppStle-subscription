package com.et.pojo;

import com.et.domain.enumeration.FeatureWeOffer;

public class ExportMerchantsRequest {

    private String apiKey;

    private String email;

    private FeatureWeOffer featureWeOffer;

    private int pageSize;

    private int pageNumber;

    public ExportMerchantsRequest() {
    }

    public ExportMerchantsRequest(String apiKey, String email, FeatureWeOffer featureWeOffer, int pageSize, int pageNumber) {
        this.apiKey = apiKey;
        this.email = email;
        this.featureWeOffer = featureWeOffer;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public FeatureWeOffer getFeatureWeOffer() {
        return featureWeOffer;
    }

    public void setFeatureWeOffer(FeatureWeOffer featureWeOffer) {
        this.featureWeOffer = featureWeOffer;
    }
}
