package com.et.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mailgun")
public class MailgunProperties {
    private String apiKey;
    private String baseUrl;
    private String domain;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
