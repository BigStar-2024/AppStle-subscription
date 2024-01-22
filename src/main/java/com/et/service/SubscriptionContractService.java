package com.et.service;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.data.SubscriptionContractData;
import com.et.api.graphql.data.SubscriptionContractInfo;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;

import java.util.Optional;

public interface SubscriptionContractService {

    /**
     * Get all shop subscriptionGroups.
     *
     * @param shop   the shop of the entity.
     * @param next   to load next values
     * @param cursor from which cursor to load
     * @return the list of subscriptionGroups.
     */
    SubscriptionContractData findShopSubscriptionContracts(String shop, boolean next, String cursor) throws Exception;

    /**
     * Delete the "id" subscriptionGroup.
     * @param shop           the id of the entity.
     * @param subscriptionContractId the id of the entity.
     * @param b
     * @param cancellationFeedback
     * @param shopifyGraphqlClient
     * @param merchantPortal
     */
    void delete(String shop, Long subscriptionContractId, boolean b, String cancellationFeedback, String cancellationNote, ShopifyGraphqlClient shopifyGraphqlClient, ActivityLogEventSource merchantPortal) throws Exception;

    Optional<SubscriptionContractInfo> findShopSubscriptionContractById(Long contractId, String shop) throws Exception;

    Optional<SubscriptionContractQuery.SubscriptionContract> findSubscriptionContractByContractId(Long contractId, String shop) throws Exception;
}
