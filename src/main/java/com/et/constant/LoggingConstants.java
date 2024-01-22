package com.et.constant;

public class LoggingConstants {
    public enum Statuses {
        SUCCESS("success"),
        FAILURE( "failure");
        private String status;
        Statuses(String status)
        {
            this.status = status;
        }
        public String getStatus(){
            return status;
        }
    }

    public enum EventSource {
        CUSTOMER_PORTAL,
        MERCHANT_PORTAL,
        BUNDLE_PORTAL,
        VIA_SHOPIFY_PORTAL,
        OTHER
    }

    public enum EntityType {
        SUBSCRIPTION_BILLING_ATTEMPT, SUBSCRIPTION_UPCOMING_ORDER
    }
    public enum EventLog {
        CREATE_UPCOMING_ORDER,
        UPDATE_UPCOMING_ORDER_SHIPMENT_DATE, UPCOMING_ORDER_SKIP_SHIPMENT,
        CREATE_BILLING_ATTEMPT, DELETE_UPCOMING_ORDER
    }
}
