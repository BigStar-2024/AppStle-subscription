package com.et.utils;

import com.amazonaws.util.CollectionUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.shopify.java.graphql.client.queries.WebhookSubscriptionCreateMutation;
import com.shopify.java.graphql.client.queries.WebhookSubscriptionDeleteMutation;
import com.shopify.java.graphql.client.queries.WebhookSubscriptionsQuery;
import com.shopify.java.graphql.client.type.WebhookSubscriptionInput;
import com.shopify.java.graphql.client.type.WebhookSubscriptionTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WebhookUtils {

    @Autowired
    public CommonUtils commonUtils;

    public List<WebhookSubscriptionsQuery.Node> getWebhooks(String shop) {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        return getWebhooks(shopifyGraphqlClient);
    }
    public List<WebhookSubscriptionsQuery.Node> getWebhooks(ShopifyGraphqlClient shopifyGraphqlClient) {
        try {
            WebhookSubscriptionsQuery webhookSubscriptionsQuery = new WebhookSubscriptionsQuery();
            Response<Optional<WebhookSubscriptionsQuery.Data>> webhookSubscriptionsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(webhookSubscriptionsQuery);

            List<WebhookSubscriptionsQuery.Node> webhooks = webhookSubscriptionsQueryResponse.getData().map(WebhookSubscriptionsQuery.Data::getWebhookSubscriptions).map(WebhookSubscriptionsQuery.WebhookSubscriptions::getEdges).orElse(new ArrayList<>()).stream().map(WebhookSubscriptionsQuery.Edge::getNode).filter(s -> !s.getTopic().equals(WebhookSubscriptionTopic.APP_UNINSTALLED)).collect(Collectors.toList());

            return webhooks;

        } catch (Exception ex) {
        }

        return null;
    }

    public String getWebhookUrlForTopic(String shop, WebhookSubscriptionTopic topic) {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        return getWebhookUrlForTopic(shopifyGraphqlClient, topic);
    }

    public String getWebhookUrlForTopic(ShopifyGraphqlClient shopifyGraphqlClient, WebhookSubscriptionTopic topic) {

        List<WebhookSubscriptionsQuery.Node> existingWebhooks = getWebhooks(shopifyGraphqlClient);

        if(!CollectionUtils.isNullOrEmpty(existingWebhooks)) {
            Optional<WebhookSubscriptionsQuery.Node> response = existingWebhooks.stream().filter(wh -> wh.getTopic().equals(topic)).findFirst();

            String webhookUrl = response.map(wh -> (WebhookSubscriptionsQuery.AsWebhookHttpEndpoint)wh.getEndpoint())
                .map(WebhookSubscriptionsQuery.AsWebhookHttpEndpoint::getCallbackUrl)
                .map(Object::toString)
                .orElse(null);

            return webhookUrl;
        }

        return null;
    }

    public void createWebhook(ShopifyGraphqlClient shopifyGraphqlClient, WebhookSubscriptionTopic topic, String webhookUrl) {

        List<WebhookSubscriptionsQuery.Node> existingWebhooks = getWebhooks(shopifyGraphqlClient);

        if(!CollectionUtils.isNullOrEmpty(existingWebhooks)) {
            Optional<WebhookSubscriptionsQuery.Node> existingWebhook = existingWebhooks.stream()
                .filter(wh -> wh.getTopic().equals(topic) && ((WebhookSubscriptionsQuery.AsWebhookHttpEndpoint)wh.getEndpoint()).getCallbackUrl().toString().equals(webhookUrl))
                .findFirst();
            // If present, don't create it
            if(existingWebhook.isPresent()) {
                return;
            }
        }

        WebhookSubscriptionInput webhookSubscriptionInput = WebhookSubscriptionInput.builder().callbackUrl(webhookUrl).build();

        WebhookSubscriptionCreateMutation webhookSubscriptionCreateMutation = new WebhookSubscriptionCreateMutation(topic, webhookSubscriptionInput);

        try {
            Response<Optional<WebhookSubscriptionCreateMutation.Data>> response = shopifyGraphqlClient.getOptionalMutationResponse(webhookSubscriptionCreateMutation);
            String a = "b";
        } catch (Exception e) {
        }
    }

    public boolean deleteWebhookForTopic(String shop, WebhookSubscriptionTopic topic) {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        return deleteWebhookForTopic(shopifyGraphqlClient, topic);
    }

    public boolean deleteWebhookForTopic(ShopifyGraphqlClient shopifyGraphqlClient, WebhookSubscriptionTopic topic) {

        boolean isDeleted = true;

        try {
            List<WebhookSubscriptionsQuery.Node> existingWebhooks = getWebhooks(shopifyGraphqlClient);

            List<WebhookSubscriptionsQuery.Node> topicWebhooks = existingWebhooks.stream().filter(wh -> wh.getTopic().equals(topic)).collect(Collectors.toList());

            if(!CollectionUtils.isNullOrEmpty(topicWebhooks)) {
                for (WebhookSubscriptionsQuery.Node webhook: topicWebhooks) {
                    boolean deleteResponse = deleteWebhook(shopifyGraphqlClient, webhook.getId());
                    if(!deleteResponse) {
                        isDeleted = false;
                    }
                }
            }

        } catch (Exception ex) {
        }

        return isDeleted;
    }

    public boolean deleteWebhook(String shop, String graphqlId) {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        return deleteWebhook(shopifyGraphqlClient, graphqlId);
    }


    public boolean deleteWebhook(ShopifyGraphqlClient shopifyGraphqlClient, String graphqlId) {

        try {
            WebhookSubscriptionDeleteMutation webhookSubscriptionDeleteMutation = new WebhookSubscriptionDeleteMutation(graphqlId);

            Response<Optional<WebhookSubscriptionDeleteMutation.Data>> response = shopifyGraphqlClient.getOptionalMutationResponse(webhookSubscriptionDeleteMutation);

            return !response.hasErrors();

        } catch (Exception ex) {
        }

        return true;
    }
}
