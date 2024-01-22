package com.et.service;

import com.et.api.graphql.data.SubscriptionCustomer;
import com.et.api.graphql.data.SubscriptionCustomerData;
import com.shopify.java.graphql.client.queries.*;

import java.util.List;
import java.util.Optional;

public interface SubscriptionCustomerService {

    /**
     * Get all shop SubscriptionCustomerData.
     *
     * @param shop   the shop of the entity.
     * @param next   to load next values
     * @param cursor from which cursor to load
     * @return the customerData.
     */
    SubscriptionCustomerData findAllSubscriptionContractCustomers(String shop, boolean next, String cursor) throws Exception;

    /**
     * Get single SubscriptionCustomer.
     *
     * @param shop                   the shop of the entity.
     * @param subscriptionCustomerId the id of the entity
     * @return the single Customer.
     */
    SubscriptionContactCustomerWithCursorQuery.Customer findSingleSubscriptionCustomer(String shop, Long subscriptionCustomerId, String cursor) throws Exception;

    SubscriptionContactCustomerWithCursorQuery.Customer findSingleSubscriptionCustomerForMP(String shop, Long subscriptionCustomerId) throws Exception;

    CustomerSearchQuery.Customers searchShopifyCustomers(String shop, String searchText, String cursor) throws Exception;

    CustomerQuery.Customer getShopifyCustomerDetailsById(String shop, Long customerId) throws Exception;

    CustomerPaymentMethodsQuery.PaymentMethods getShopifyCustomerPaymentDetailsById(String shop, Long customerId) throws Exception;
}
