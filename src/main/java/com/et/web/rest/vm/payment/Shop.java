package com.et.web.rest.vm.payment;



import com.shopify.java.graphql.client.type.PaypalExpressSubscriptionsGatewayStatus;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private PaymentSettings paymentSettings;
    private boolean legacySubscriptionGatewayEnabled;
    private boolean eligibleForSubscriptionMigration;
    private PaypalExpressSubscriptionsGatewayStatus paypalExpressSubscriptionGatewayStatus;

    public PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }

    public void setPaymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
    }

    public void setLegacySubscriptionGatewayEnabled(boolean legacySubscriptionGatewayEnabled) {
        this.legacySubscriptionGatewayEnabled = legacySubscriptionGatewayEnabled;
    }

    public boolean getLegacySubscriptionGatewayEnabled() {
        return legacySubscriptionGatewayEnabled;
    }

    public void setEligibleForSubscriptionMigration(boolean eligibleForSubscriptionMigration) {
        this.eligibleForSubscriptionMigration = eligibleForSubscriptionMigration;
    }

    public boolean getEligibleForSubscriptionMigration() {
        return eligibleForSubscriptionMigration;
    }

    public void setPaypalExpressSubscriptionGatewayStatus(PaypalExpressSubscriptionsGatewayStatus paypalExpressSubscriptionGatewayStatus) {
        this.paypalExpressSubscriptionGatewayStatus = paypalExpressSubscriptionGatewayStatus;
    }

    public PaypalExpressSubscriptionsGatewayStatus getPaypalExpressSubscriptionGatewayStatus() {
        return paypalExpressSubscriptionGatewayStatus;
    }
}
