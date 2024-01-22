package com.et.service.impl;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.data.SubscriptionCustomerData;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.service.SubscriptionCustomerService;
import com.et.utils.CommonUtils;
import com.shopify.java.graphql.client.queries.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.et.api.constants.ShopifyIdPrefix.CUSTOMER_ID_PREFIX;

@Service
public class SubscriptionCustomerServiceImpl implements SubscriptionCustomerService {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionCustomerServiceImpl.class);

    @Autowired
    private ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    @Autowired
    private CommonUtils commonUtils;


    /**
     * Get all shop customerData.
     *
     * @param shop   the shop of the entity.
     * @param next   to load next values
     * @param cursor from which cursor to load
     * @return the customerData.
     */
    @Override
    public SubscriptionCustomerData findAllSubscriptionContractCustomers(String shop, boolean next, String cursor) throws Exception {
        logger.info("{} Calling shopify graphql for get subscription contract customers", shop);
        return shopifyGraphqlSubscriptionContractService.getShopSubscriptionContractCustomers(commonUtils.prepareShopifyGraphqlClient(shop), shop, next, cursor);
    }

    /**
     * Get single Customer.
     *
     * @param shop                   the shop of the entity.
     * @param subscriptionCustomerId the id of the entity
     * @return the single Customer.
     */
    @Override
    public SubscriptionContactCustomerWithCursorQuery.Customer findSingleSubscriptionCustomer(String shop, Long subscriptionCustomerId, String cursor) throws Exception {
        logger.info("{} Calling shopify graphql for get single subscription contract customer", shop);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContactCustomerWithCursorQuery subscriptionContactCustomerQuery = new SubscriptionContactCustomerWithCursorQuery(CUSTOMER_ID_PREFIX + subscriptionCustomerId, Input.optional(cursor));
        Response<Optional<SubscriptionContactCustomerWithCursorQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContactCustomerQuery);
        SubscriptionContactCustomerWithCursorQuery.Customer customer = response.getData().map(d -> d.getCustomer().orElse(null)).orElse(null);

        return customer;
    }

    @Override
    public SubscriptionContactCustomerWithCursorQuery.Customer findSingleSubscriptionCustomerForMP(String shop, Long subscriptionCustomerId) throws Exception {
        logger.info("{} Calling shopify graphql for get single subscription contract customer", shop);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SubscriptionContactCustomerWithCursorQuery subscriptionContactWithCursorCustomerQuery = new SubscriptionContactCustomerWithCursorQuery(CUSTOMER_ID_PREFIX + subscriptionCustomerId, Input.optional(null));
        Response<Optional<SubscriptionContactCustomerWithCursorQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContactWithCursorCustomerQuery);
        SubscriptionContactCustomerWithCursorQuery.Customer customer = response.getData().map(d -> d.getCustomer().orElse(null)).orElse(null);

        return customer;
    }

    @Override
    public CustomerSearchQuery.Customers searchShopifyCustomers(String shop, String searchText, String cursor) throws Exception {
        logger.info("Search shopify customers. shop={}, searchText={}, cursor={}", shop, searchText, cursor);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        CustomerSearchQuery customerSearchQuery = new CustomerSearchQuery(Input.fromNullable(searchText), Input.fromNullable(cursor));
        Response<Optional<CustomerSearchQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(customerSearchQuery);

        if(!optionalQueryResponse.hasErrors()) {
            return Objects.requireNonNull(optionalQueryResponse.getData()).map(CustomerSearchQuery.Data::getCustomers).orElse(null);
        } else {
            logger.error("Error while searching customers.", optionalQueryResponse.getErrors().get(0).getMessage());
        }

        return null;
    }

    @Override
    public CustomerQuery.Customer getShopifyCustomerDetailsById(String shop, Long customerId) throws Exception{
        logger.info("Get shopify customer details by Id ={}", customerId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        CustomerQuery customerQuery = new CustomerQuery(ShopifyGraphQLUtils.getGraphQLCustomerId(customerId));
        Response<Optional<CustomerQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(customerQuery);

        if(!optionalQueryResponse.hasErrors()) {
            return Objects.requireNonNull(optionalQueryResponse.getData()).map(CustomerQuery.Data::getCustomer).orElse(Optional.ofNullable(null)).orElse(null);
        } else {
            logger.error("Error while getting customer details from shopify.", optionalQueryResponse.getErrors().get(0).getMessage());
        }

        return null;
    }

    @Override
    public CustomerPaymentMethodsQuery.PaymentMethods getShopifyCustomerPaymentDetailsById(String shop, Long customerId) throws Exception {

        logger.info("Get shopify customer payment methods details by Id ={}", customerId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(ShopifyGraphQLUtils.getGraphQLCustomerId(customerId), Input.optional(false));

        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

        if(!response.hasErrors()) {
            return Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods()).orElse(null)).orElse(null);
        } else {
            logger.error("Error while getting customer payment method details from shopify.", response.getErrors().get(0).getMessage());
        }

        return null;
    }
}
