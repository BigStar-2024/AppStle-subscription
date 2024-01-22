package com.et.service.impl;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.data.SubscriptionContractData;
import com.et.api.graphql.data.SubscriptionContractInfo;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.SubscriptionContractDetails;
import com.et.domain.enumeration.*;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.CustomerPortalSettingsService;
import com.et.service.SubscriptionContractService;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import com.shopify.java.graphql.client.type.SubscriptionContractSubscriptionStatus;
import com.shopify.java.graphql.client.type.SubscriptionDraftInput;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionContractServiceImpl implements SubscriptionContractService {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionContractServiceImpl.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    /**
     * Get all shop subscriptionContracts.
     *
     * @param shop   the shop of the entity.
     * @param next   to load next values
     * @param cursor from which cursor to load
     * @return the list of products.
     */
    @Override
    public SubscriptionContractData findShopSubscriptionContracts(String shop, boolean next, String cursor) throws Exception {
        logger.info("{} Calling shopify graphql for get selling groups", shop);
        return shopifyGraphqlSubscriptionContractService.getShopSubscriptionContracts(commonUtils.prepareShopifyGraphqlClient(shop), shop, next, cursor);
    }

    /**
     * Delete the "id" subscriptionContract.
     *
     * @param shop                   the id of the entity.
     * @param subscriptionContractId the id of the entity.
     */

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Override
    public void delete(String shop, Long subscriptionContractId, boolean isInternal, String cancellationFeedback, String cancellationNote, ShopifyGraphqlClient shopifyGraphqlClient, ActivityLogEventSource eventSource) throws Exception {
        logger.info("{} Calling shopify graphql for cancel subscription contract", shop);

        List<SubscriptionBillingAttempt> successAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractId, BillingAttemptStatus.SUCCESS);

        Optional<SubscriptionContractDetails> subscriptionContractionDetailsOptional = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId);

        if (subscriptionContractionDetailsOptional.isEmpty()) {
            return;
        }

        SubscriptionContractDetails subscriptionContractDetails = subscriptionContractionDetailsOptional.get();

        if (!subscriptionContractDetails.getShop().equals(shop)) {
            throw new BadRequestAlertException("Invalid id", "", "idnull");
        }

        if (!isInternal && (successAttempts.size() + 1) < Optional.ofNullable(subscriptionContractDetails.getMinCycles()).orElse(1)) {
            String cancelSubscriptionMinimumBillingIterationsMessage = getErrorMessage(shop);
            throw new BadRequestAlertException(cancelSubscriptionMinimumBillingIterationsMessage.replace("{{minCycles}}", Optional.ofNullable(subscriptionContractDetails.getMinCycles()).orElse(1).toString()), "", "");
        }

        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
        subscriptionDraftInputBuilder.status(SubscriptionContractSubscriptionStatus.CANCELLED);

        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, subscriptionContractId, subscriptionDraftInput);

        if (subscriptionContractUpdateResult.isSuccess()) {
            subscriptionContractDetailsRepository.updateStatus(subscriptionContractId, "cancelled", cancellationFeedback, cancellationNote);
            ActivityLogEventType activityLogEventType = ActivityLogEventType.CONTRACT_CANCELLED;
            commonUtils.writeActivityLog(shop, subscriptionContractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, activityLogEventType, ActivityLogStatus.SUCCESS);
        } else {
            logger.error("An error occurred while activating/pausing subscription contractId=" + subscriptionContractId + " status=cancelled" + " errorMessage=" + subscriptionContractUpdateResult.getErrorMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
        }
    }

    private String getErrorMessage(String shop) {
        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();
        String cancelSubscriptionMinimumBillingIterationsMessage = customerPortalSettingsDTO.getCancelSubscriptionMinimumBillingIterationsMessage();
        if (StringUtils.isBlank(cancelSubscriptionMinimumBillingIterationsMessage)) {
            cancelSubscriptionMinimumBillingIterationsMessage = "Subscription requires minimum billing iterations of {{minCycles}} before it can be cancelled.";
        }
        return cancelSubscriptionMinimumBillingIterationsMessage;
    }

    @Override
    public Optional<SubscriptionContractInfo> findShopSubscriptionContractById(Long contractId, String shop) throws Exception {
        return shopifyGraphqlSubscriptionContractService.getShopSubscriptionContract(commonUtils.prepareShopifyGraphqlClient(shop), contractId);
    }

    @Override
    public Optional<SubscriptionContractQuery.SubscriptionContract> findSubscriptionContractByContractId(Long contractId, String shop) throws Exception {
        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractRaw = shopifyGraphqlSubscriptionContractService.getSubscriptionContractRaw(commonUtils.prepareShopifyGraphqlClient(shop), contractId);
        return subscriptionContractRaw;
    }
}
