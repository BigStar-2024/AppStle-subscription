package com.et.service.dto;

public class SubscriptionVsUnsubscriptionDTO {

    private Long subscriptionCount;

    private Long unsubscriptionCount;

    private String name;

    public Long getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(Long subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public Long getUnsubscriptionCount() {
        return unsubscriptionCount;
    }

    public void setUnsubscriptionCount(Long unsubscriptionCount) {
        this.unsubscriptionCount = unsubscriptionCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
