package com.et.service;

import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.domain.SubscriptionContractDetails;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.DeliveryProfileUpdateMutation;
import com.shopify.java.graphql.client.queries.FlowTriggerReceiveMutation;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class ShopifyFlowService {

    private final Logger log = LoggerFactory.getLogger(ShopifyFlowService.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Async
    public void sendTrigger(String shop, Long contractId) throws Exception {

        log.info("Sending flow trigger");

        try {
            Optional<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(contractId);

            if(subscriptionContractDetails.isPresent()) {

                Long customerId = subscriptionContractDetails.get().getCustomerId();

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                Map<String, Object> body = new HashMap<>();
                List<Map<String, Object>> resources = new ArrayList<>();
                Map<String, Object> properties = new HashMap<>();

                body.put("trigger_id", "9efc9f49-e9a4-4380-859c-5d2baa7e71a7");

                Map<String, Object> resource = new HashMap<>();
                resource.put("name", "Customer Unsubscribed");
                resource.put("url", "http://www.example.com/weather/toronto");
                resources.add(resource);
                body.put("resources", resources);

                properties.put("customer_id", customerId);
                body.put("properties", properties);

                String jsonBody = objectMapper.writeValueAsString(body);

                FlowTriggerReceiveMutation flowTriggerReceiveMutation = new FlowTriggerReceiveMutation(jsonBody);

                Response<Optional<FlowTriggerReceiveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(flowTriggerReceiveMutation);

                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalMutationResponse.getErrors().get(0).getMessage()), "", "idexists");
                }

                List<FlowTriggerReceiveMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                    .map(d -> d.getFlowTriggerReceive()
                        .map(FlowTriggerReceiveMutation.FlowTriggerReceive::getUserErrors).orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());


                if (!userErrors.isEmpty()) {
                    throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "idexists");
                }
            }
        }catch (Exception e){
            log.error("trigger not sent, error={}", e.getMessage());
        }
    }
}
