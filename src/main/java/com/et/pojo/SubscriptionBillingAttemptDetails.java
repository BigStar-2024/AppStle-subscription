package com.et.pojo;

import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.SubscriptionContractDetails;

public class SubscriptionBillingAttemptDetails {

    private SubscriptionBillingAttempt subscriptionBillingAttempt;
    private SubscriptionContractDetails subscriptionContractDetails;

    public SubscriptionBillingAttemptDetails(SubscriptionBillingAttempt subscriptionBillingAttempt, SubscriptionContractDetails subscriptionContractDetails) {
        this.subscriptionBillingAttempt = subscriptionBillingAttempt;
        this.subscriptionContractDetails = subscriptionContractDetails;
    }

    public SubscriptionBillingAttemptDetails() {

    }

    public SubscriptionBillingAttempt getSubscriptionBillingAttempt() {
        return subscriptionBillingAttempt;
    }

    public void setSubscriptionBillingAttempt(SubscriptionBillingAttempt subscriptionBillingAttempt) {
        this.subscriptionBillingAttempt = subscriptionBillingAttempt;
    }

    public SubscriptionContractDetails getSubscriptionContractDetails() {
        return subscriptionContractDetails;
    }

    public void setSubscriptionContractDetails(SubscriptionContractDetails subscriptionContractDetails) {
        this.subscriptionContractDetails = subscriptionContractDetails;
    }
}
