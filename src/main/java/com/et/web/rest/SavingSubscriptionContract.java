package com.et.web.rest;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.shopify.java.graphql.client.type.SubscriptionContractCreateInput;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface SavingSubscriptionContract {
    boolean trySavingSubscriptionContract(String shop, ShopifyGraphqlClient shopifyGraphqlClient, Set<String> importedIds, List<String> errorList, List<String> processedId, Map<String, String> subscriptionHeaderMap, CSVRecord subscriptionDataRecord, String id, String status, SubscriptionContractCreateInput subscriptionContractCreateInput, Set<String> currentIterationImportedIds);
}
