package com.et.utils;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.constant.Constants;
import com.et.service.SubscriptionBillingAttemptService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.service.dto.SubscriptionContractDetailsDTO;
import com.et.web.rest.errors.BadRequestAlertException;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import com.shopify.java.graphql.client.queries.SubscriptionContractUpdateMutation;
import com.shopify.java.graphql.client.queries.SubscriptionDraftCommitMutation;
import com.shopify.java.graphql.client.queries.SubscriptionDraftDiscountRemoveMutation;
import com.shopify.java.graphql.client.type.SubscriptionDraftInput;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX;

@Component
public class SubscriptionBillingAttemptUtils {

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    @Autowired
    private ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    @Autowired
    private SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    @Autowired
    private CommonUtils commonUtils;

    private final Logger log = LoggerFactory.getLogger(SubscriptionBillingAttemptUtils.class);

    public void updateSubscriptionContractNextBillingDate(Long contractId) throws Exception {
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId).get();
        ZonedDateTime nextBillingDateRetrieved = subscriptionBillingAttemptService.findNextBillingDateForContractId(contractId).get();
        String shop = subscriptionContractDetailsDTO.getShop();

        if (!subscriptionContractDetailsDTO.getNextBillingDate().equals(nextBillingDateRetrieved)) {

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT).withZone(ZoneId.of("UTC"));
            String formattedDate = dateTimeFormatter.format(nextBillingDateRetrieved);
            subscriptionDraftInputBuilder.nextBillingDate(formattedDate);

            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

            if (!subscriptionContractUpdateResult.isSuccess()) {
                throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
            }

            subscriptionContractDetailsDTO.setNextBillingDate(nextBillingDateRetrieved);
            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
        }
    }
}
