package com.et.web.rest.vm;

import java.time.ZonedDateTime;

public class CustomerInfo {

    private Long customerId;
    private String name;
    private String email;
    private Long activeSubscriptions;
    private ZonedDateTime nextOrderDate;
    private long inActiveSubscriptions;

    public CustomerInfo() {
    }

    public CustomerInfo(Long customerId, String name, String email, Long activeSubscriptions, ZonedDateTime nextOrderDate, long inActiveSubscriptions) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.activeSubscriptions = activeSubscriptions;
        this.nextOrderDate = nextOrderDate;
        this.inActiveSubscriptions = inActiveSubscriptions;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(Long activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public ZonedDateTime getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(ZonedDateTime nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public void setInActiveSubscriptions(long inActiveSubscriptions) {
        this.inActiveSubscriptions = inActiveSubscriptions;
    }

    public long getInActiveSubscriptions() {
        return inActiveSubscriptions;
    }
}
