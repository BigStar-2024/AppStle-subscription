package com.et.web.rest.vm;

import java.time.ZonedDateTime;

public class TokenDetails {

    private String manageSubscriptionLink;
    private ZonedDateTime tokenExpirationTime;

    public String getManageSubscriptionLink() {
        return manageSubscriptionLink;
    }

    public void setManageSubscriptionLink(String manageSubscriptionLink) {
        this.manageSubscriptionLink = manageSubscriptionLink;
    }

    public ZonedDateTime getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(ZonedDateTime tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
