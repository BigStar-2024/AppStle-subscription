package com.et.api.klaviyo.track;

import com.et.api.klaviyo.model.CustomerProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackRequest {

    private String token;
    private String event;
    private CustomerProperties customer_properties;
    private Object properties;
    private Long time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public CustomerProperties getCustomer_properties() {
        return customer_properties;
    }

    public void setCustomer_properties(CustomerProperties customer_properties) {
        this.customer_properties = customer_properties;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
