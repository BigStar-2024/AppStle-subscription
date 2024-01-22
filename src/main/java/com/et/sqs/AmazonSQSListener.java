package com.et.sqs;

import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.BulkAutomationType;
import com.et.pojo.bulkAutomation.*;
import com.et.service.BulkAutomationService;
import com.et.service.SubscriptionContractDetailsService;
import com.et.utils.AwsUtils;
import com.et.web.rest.SubscriptionContractDetailsResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import tech.jhipster.config.JHipsterConstants;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;*/
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.et.constant.Constants.SPRING_PROFILE_JOBS;

@Component
@Profile(SPRING_PROFILE_JOBS)
public class AmazonSQSListener {

    private final Logger log = LoggerFactory.getLogger(AmazonSQSListener.class);

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BulkAutomationService bulkAutomationService;

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    public AmazonSQSListener() {
        super();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_INTERVAL_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateBillingIntervalMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_BILLING_INTERVAL, message);
            UpdateBillingIntervalRequest updateBillingIntervalRequest = objectMapper.readValue(message, UpdateBillingIntervalRequest.class);
            shop = updateBillingIntervalRequest.getShop();
            contractId = updateBillingIntervalRequest.getContractId();
            isLast = updateBillingIntervalRequest.isLast();

            subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shop, updateBillingIntervalRequest.getBillingIntervalCount(), updateBillingIntervalRequest.getBillingInterval(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_BILLING_INTERVAL, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_BILLING_INTERVAL, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_DELIVERY_PRICE_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateDeliveryPriceMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_DELIVERY_PRICE, message);
            UpdateDeliveryPriceRequest updateDeliveryPriceRequest = objectMapper.readValue(message, UpdateDeliveryPriceRequest.class);
            shop = updateDeliveryPriceRequest.getShop();
            contractId = updateDeliveryPriceRequest.getContractId();
            isLast = updateDeliveryPriceRequest.isLast();

            subscriptionContractDetailsService.updateDeliveryPriceBySubscriptionContractId(shop, contractId, updateDeliveryPriceRequest.getDeliveryPrice(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_DELIVERY_PRICE, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_PRICE, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_STATUS_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateStatusMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_STATUS, message);
            UpdateSubscriptionStatusRequest updateSubscriptionStatusRequest = objectMapper.readValue(message, UpdateSubscriptionStatusRequest.class);
            shop = updateSubscriptionStatusRequest.getShop();
            contractId = updateSubscriptionStatusRequest.getContractId();
            isLast = updateSubscriptionStatusRequest.isLast();

            subscriptionContractDetailsService.subscriptionContractUpdateStatus(contractId, updateSubscriptionStatusRequest.getStatus(), shop, null, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_STATUS, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_STATUS, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_STATUS, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_STATUS, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_DELIVERY_METHOD_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateDeliveryMethodMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_DELIVERY_METHOD, message);
            UpdateDeliveryMethodRequest updateDeliveryMethodRequest = objectMapper.readValue(message, UpdateDeliveryMethodRequest.class);
            shop = updateDeliveryMethodRequest.getShop();
            contractId = updateDeliveryMethodRequest.getContractId();
            isLast = updateDeliveryMethodRequest.isLast();

            subscriptionContractDetailsService.updateDeliveryMethod(shop, contractId, updateDeliveryMethodRequest.getDeliveryMethodName(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_DELIVERY_METHOD, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_DELIVERY_METHOD, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.HIDE_SUBSCRIPTION_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveHideSubscriptionMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.HIDE_SUBSCRIPTIONS, message);
            HideSubscriptionsRequest hideSubscriptionsRequest = objectMapper.readValue(message, HideSubscriptionsRequest.class);
            shop = hideSubscriptionsRequest.getShop();
            contractId = hideSubscriptionsRequest.getContractId();
            isLast = hideSubscriptionsRequest.isLast();

            subscriptionContractDetailsService.hideSubscription(shop, contractId, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.HIDE_SUBSCRIPTIONS, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.HIDE_SUBSCRIPTIONS, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_NEXT_BILLING_DATE_TIME_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateNextBillingDateTimeMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, message);
            UpdateNextBillingDateTimeRequest updateNextBillingDateTimeRequest = objectMapper.readValue(message, UpdateNextBillingDateTimeRequest.class);
            contractId = updateNextBillingDateTimeRequest.getContractId();
            isLast = updateNextBillingDateTimeRequest.isLast();

            subscriptionContractDetailsService.updateNextBillingDateTime(shop, contractId, updateNextBillingDateTimeRequest.getHour(), updateNextBillingDateTimeRequest.getMinute(), updateNextBillingDateTimeRequest.getZonedOffHours());

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE_TIME, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.BULK_UPDATE_NEXT_BILLING_DATE_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateNextBillingDateMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE, message);

            ObjectMapper localObjectMapper = new ObjectMapper();
            localObjectMapper.registerModule(new JavaTimeModule());
            localObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            UpdateNextBillingDateRequest updateNextBillingDateRequest = localObjectMapper.readValue(message, UpdateNextBillingDateRequest.class);
            contractId = updateNextBillingDateRequest.getContractId();
            isLast = updateNextBillingDateRequest.isLast();

            subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shop, updateNextBillingDateRequest.getNextBillingDate(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_NEXT_BILLING_DATE, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_NEXT_BILLING_DATE, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.REPLACE_REMOVED_VARIANT_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveReplaceRemovedVariantMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.REPLACE_REMOVED_VARIANT, message);
            ReplaceRemovedVariantRequest replaceRemovedVariantRequest = objectMapper.readValue(message, ReplaceRemovedVariantRequest.class);
            contractId = replaceRemovedVariantRequest.getContractId();
            String title = replaceRemovedVariantRequest.getTitle();
            Long newVariantId = replaceRemovedVariantRequest.getNewVariantId();

            isLast = replaceRemovedVariantRequest.isLast();

            subscriptionContractDetailsService.replaceRemovedVariants(shop, contractId, title, newVariantId, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.REPLACE_REMOVED_VARIANT, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_REMOVED_VARIANT, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.UPDATE_LINE_PRICE_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateLinePriceMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;
        Double price;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_LINE_PRICE, message);
            UpdateLinePriceRequest updateLinePriceRequest = objectMapper.readValue(message, UpdateLinePriceRequest.class);
            contractId = updateLinePriceRequest.getContractId();
            isLast = updateLinePriceRequest.isLast();
            price = updateLinePriceRequest.getPrice();

            subscriptionContractDetailsService.updateLinePrice(shop, contractId, updateLinePriceRequest.getVariantId(), price, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_LINE_PRICE, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_LINE_PRICE, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_LINE_PRICE, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_LINE_PRICE, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.REPLACE_PRODUCT_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveReplaceProductMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.REPLACE_PRODUCT, message);
            ReplaceProductRequest replaceProductRequest = objectMapper.readValue(message, ReplaceProductRequest.class);
            contractId = replaceProductRequest.getContractId();
            isLast = replaceProductRequest.isLast();

            Map<Long, Integer> newVariants = new HashMap<>();
            for(Long variantId : replaceProductRequest.getNewVariantIds()){
                newVariants.put(variantId, null);
            }

            subscriptionContractDetailsService.replaceVariantsV3(shop, contractId, replaceProductRequest.getOldVariantIds(), newVariants, null, true, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.REPLACE_PRODUCT, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.REPLACE_PRODUCT, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.REPLACE_PRODUCT, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.DELETE_REMOVED_PRODUCT_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveDeleteRemovedProductMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.DELETE_REMOVED_PRODUCT, message);
            DeleteProductRequest deleteProductRequest = objectMapper.readValue(message, DeleteProductRequest.class);
            contractId = deleteProductRequest.getContractId();
            isLast = deleteProductRequest.isLast();

            if (deleteProductRequest.getVariantId() != null) {
                subscriptionContractDetailsService.subscriptionContractsRemoveLineItem(shop, contractId, deleteProductRequest.getVariantId());
            }

            if (BooleanUtils.isTrue(deleteProductRequest.isDeleteRemovedProductsFromShopify())) {
                subscriptionContractDetailsService.subscriptionContractsRemoveDeletedLineItem(shop, contractId);
            }

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.DELETE_REMOVED_PRODUCT, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.DELETE_REMOVED_PRODUCT, errorObject.toString());
                }
            }
        }
    }

    @SqsListener(value = AwsUtils.UPDATE_MIN_MAX_CYCLES_SQS, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveUpdateMinMaxCyclesMessage(String message, @Header("Shop") String shop, @Header("SenderId") String senderId) throws Exception {
        Long contractId = null;
        boolean isLast = false;

        try {
            log.info("SQS {} message received: {}", BulkAutomationType.UPDATE_MIN_MAX_CYCLES, message);
            UpdateMinMaxCyclesRequest updateMinMaxCyclesRequest = objectMapper.readValue(message, UpdateMinMaxCyclesRequest.class);
            contractId = updateMinMaxCyclesRequest.getContractId();
            isLast = updateMinMaxCyclesRequest.isLast();

            if(BooleanUtils.isTrue(updateMinMaxCyclesRequest.getUpdateMinCycles())) {
                subscriptionContractDetailsService.subscriptionContractUpdateMinCycles(contractId, shop, updateMinMaxCyclesRequest.getMinCycles(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }
            if(BooleanUtils.isTrue(updateMinMaxCyclesRequest.getUpdateMaxCycles())) {
                subscriptionContractDetailsService.subscriptionContractsUpdateMaxCycles(contractId, shop, updateMinMaxCyclesRequest.getMaxCycles(), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
            }

            if (isLast) {
                bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, null);
            }
        } catch (Exception e) {
            log.error("Some error occurred while processing SQS {} message: {}", BulkAutomationType.UPDATE_MIN_MAX_CYCLES, e.getMessage());
            if (StringUtils.isNotBlank(shop)) {
                JSONObject errorObject = new JSONObject();
                errorObject.put("contractId", contractId);
                errorObject.put("message", e.getMessage());
                if (BooleanUtils.isTrue(isLast)) {
                    bulkAutomationService.stopBulkAutomationProcess(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, errorObject.toString());
                } else {
                    bulkAutomationService.updateBulkAutomationErrorMsg(shop, BulkAutomationType.UPDATE_MIN_MAX_CYCLES, errorObject.toString());
                }
            }
        }
    }
}
