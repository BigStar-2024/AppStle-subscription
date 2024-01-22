package com.et.domain.enumeration;

/**
 * The OrderStatus enumeration.
 */

// TODO check later
// https://shopify.dev/docs/admin-api/graphql/reference/orders/orderdisplayfulfillmentstatus
public enum OrderStatus {
    FULFILLED, PARTIAL, RESTOCKED, NEW, REFUND_ISSUED, FULFILLMENT_UPDATED
}
