package com.et.service;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.DeliveryMethodType;
import com.et.domain.enumeration.EmailSettingType;
import com.et.pojo.ExportInputRequest;
import com.et.pojo.SubscriptionProductInfo;
import com.et.service.dto.*;
import com.et.web.rest.vm.AttributeInfo;
import com.et.web.rest.vm.ChangeShippingAddressVM;
import com.et.web.rest.vm.CustomerInfo;
import com.shopify.java.graphql.client.queries.SubscriptionContractQuery;
import com.shopify.java.graphql.client.type.AttributeInput;
import com.shopify.java.graphql.client.type.SellingPlanAnchorInput;
import com.shopify.java.graphql.client.type.SellingPlanInterval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionContractDetails}.
 */
public interface SubscriptionContractDetailsService {

    /**
     * Save a subscriptionContractDetails.
     *
     * @param subscriptionContractDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionContractDetailsDTO save(SubscriptionContractDetailsDTO subscriptionContractDetailsDTO);

    /**
     * Get all the subscriptionContractDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubscriptionContractDetailsDTO> findAll(Pageable pageable);

    List<SubscriptionContractDetailsDTO> findAll();


    /**
     * Get the "id" subscriptionContractDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionContractDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionContractDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop);

    Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop,
                                                    ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                    ZonedDateTime fromUpdatedDate, ZonedDateTime toUpdatedDate,
                                                    ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                    String subscriptionContractId, String customerName,
                                                    String orderName, String status,
                                                    Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                    String planType, String recordType,
                                                    Long productId, Long variantId, Long sellingPlanId, Double minOrderAmount, Double maxOrderAmount);

    Page<SubscriptionContractDetailsDTO> findByShop(Pageable pageable, String shop,
                                                    ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                    ZonedDateTime fromUpdatedDate, ZonedDateTime toUpdatedDate,
                                                    ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                    String subscriptionContractId, String customerName,
                                                    String orderName, String status,
                                                    Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                    String planType, String recordType,
                                                    Long productId, Long variantId, List<String> sellingPlanIds,
                                                    Double minOrderAmount, Double maxOrderAmount);

    List<SubscriptionContractDetailsDTO> findByShop(String shop);

    Set<Long> findByShopGroupByCustomerId(String shop);

    Page<CustomerInfo> findByShopGroupByCustomerIdPaginated(Pageable pageable, String shop, String name, String email, Boolean activeMoreThanOneSubscription);

    List<SubscriptionContractDetailsDTO> findByShopAndCustomerIds(String shop, Set<Long> customerIds);

    Optional<SubscriptionContractDetailsDTO> findByContractId(Long contractId);

    List<Long> findSubscriptionContractIdsByShop(String shop);

    Page<Long> findSubscriptionContractIdsByShop(String shop, Pageable pageable);

    List<Long> findSubscriptionContractIdsByShopAndIsImported(String shop);

    List<String> findCustomerEmailByShop(String shop);

    List<Long> findCustomerIdsByShop(String shop);

    int exportSubscriptionContracts(ExportInputRequest exportInputRequest);

    List<SubscriptionContractDetailsDTO> findByShop(String shop,
                                                    ZonedDateTime fromCreatedDate, ZonedDateTime toCreatedDate,
                                                    ZonedDateTime fromNextDate, ZonedDateTime toNextDate,
                                                    String subscriptionContractId, String customerName,
                                                    String orderName, String status,
                                                    Integer billingPolicyIntervalCount, String billingPolicyInterval,
                                                    String planType, String recordType,
                                                    Long productId,
                                                    Long variantId,
                                                    Long sellingPlanId);

    List<SubscriptionContractDetailsDTO> findByShopAndCustomerId(String shop, long customerId);

    Optional<SubscriptionContractDetailsDTO> findByShopAndImportedId(String shop, String id);

    List<SubscriptionContractDetailsDTO> findByProductOrVariantId(String shop, String gqlProductOrVariantId);

    Page<SubscriptionContractDetailsDTO> findByProductOrVariantId(String shop, String gqlProductOrVariantId, Pageable pageable);

    List<ProductDeliveryAnalytics> getProductDeliveryAnalytics(String shop);

    Optional<SubscriptionContractDetailsDTO> getSubscriptionByContractId(Long contractId);

    void deleteByContractId(Long contractId);

    List<SubscriptionContractDetailsDTO> findByContractIdIn(Set<Long> subscriptionContractIds);

    void exportProductDeliveryAnalytics(String email, String shop);

    List<ProductRevenueAnalytics> getProductRevenueAnalytics(String shop, String filterBy, Long days, ZonedDateTime fromDate, ZonedDateTime toDay);

    List<SubscriptionContractDetailsDTO> findNextOrderDateDiscrepancy();

    void deleteBySubscriptionContractIdIn(Set<Long> contractIds);

    List<SubscriptionContractDetailsDTO> findSubscriptionWithNullOrderAmount();

    void updateLinePrice(String shop, Long contractId, Long variantId, Double price, ActivityLogEventSource activityLogEventSource) throws Exception;

    void subscriptionContractUpdateLineItem(Long contractId, String shop, Integer quantity, String productVariantId, String lineId, String sellingPlanName, Double price, boolean isPricePerUnit, String requestURL, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateLineItemQuantity(Long contractId, String shop, Integer quantity, String lineId, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateBillingInterval(Long contractId, String shop, int billingIntervalCount, SellingPlanInterval billingInterval, ActivityLogEventSource eventSource) throws Exception;

    List<SellingPlanAnchorInput> getOldAnchors(List<SubscriptionContractQuery.Anchor> anchorDays);

    void subscriptionContractUpdateLineItemPrice(Long contractId, String shop, Double basePrice, String lineId, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateLineItemPrice(SubscriptionContractQuery.SubscriptionContract subscriptionContract, String shop, Double basePrice, String lineId, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateLineItemPricingPolicy(String shop, Long contractId, String lineId, Double basePrice, List<AppstleCycle> cycles, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateLineItemPricingPolicy(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, String lineId, Double basePrice, List<AppstleCycle> cycles, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateLineItemSellingPlan(Long contractId, String shop, Long sellingPlanId, String sellingPlanName, String lineId, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractDetailsDTO updateDetails(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract);

    SubscriptionContractDetailsDTO updateDetails(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, Boolean updateCancelled);

    List<SubscriptionProductInfo> getProductData(SubscriptionContractQuery.SubscriptionContract subscriptionContract);

    void updateDeliveryPriceBySubscriptionContractId(String shop, Long contractId, Double deliveryPrice, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractUpdateResult updateDeliveryMethod(String shop, Long contractId, String deliveryMethodName, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateStatus(Long subscriptionContractId, String status, String shop, Integer pauseDurationCycle, ActivityLogEventSource eventSource) throws Exception;

    void hideSubscription(String shop, Long contractId, ActivityLogEventSource eventSource) throws Exception;

    void updateNextBillingDateTime(String shop, Long contractId, Integer hour, Integer minute, Integer zonedOffSetHours) throws Exception;

    void updateNextBillingDateTime(String shop, Integer hour, Integer minute, Integer zonedOffSetHours,
                                   SubscriptionContractDetailsDTO subscriptionContractDetailsDTO,
                                   ShopifyGraphqlClient shopifyGraphqlClient) throws Exception;

    Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractUpdateNextBillingDate(Long contractId, String shop, ZonedDateTime nextBillingDate, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractUpdateShippingAddress(Long contractId, String shop, ChangeShippingAddressVM changeShippingAddressVM, ActivityLogEventSource eventSource) throws Exception;

    void subscriptionContractUpdateMissingAddress(Long contractId, String shop, ChangeShippingAddressVM changeShippingAddressVM, ActivityLogEventSource eventSource) throws Exception;

    void updateDeliveryMethodType(Long contractId, String shop, DeliveryMethodType fromDeliveryType, DeliveryMethodType toDeliveryType) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Boolean isOneTimeProduct, String RequestURL, List<AttributeInfo> attributeInfoList, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItem(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, List<AttributeInput> attributeInputList, ActivityLogEventSource eventSource) throws Exception;

    void resyncBuildABoxDiscount(String shop, Long contractId) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractAddLineItemWithNoDiscount(Long contractId, String shop, Integer quantity, String productVariantId, Double price, Boolean isOneTimeProduct, String requestURL, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItemWithRetry(Long contractId, String shop, String lineId, String requestURL, Boolean removeDiscount, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItem(Long contractId, String shop, String lineId, String requestURL, Boolean removeDiscount, ActivityLogEventSource eventSource) throws Exception;

    void applyDiscount(
        Long contractId,
        Integer percentage,
        String discountTitle,
        Integer recurringCycleLimit,
        Boolean appliesOnEachItem,
        Double amount,
        String discountType,
        ShopifyGraphqlClient shopifyGraphqlClient,
        String draftId,
        List<String> lineItems,
        ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract getSubscriptionContractRawInternal(@PathVariable Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, Boolean updateCancelled) throws Exception;

    void sendSubscriptionProductAlterationMail(Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient, EmailSettingType emailSettingType, Map<String, Object> additionalAttributes) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveDiscount(Long contractId, String shop, String discountId, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract removeContractDiscountByCode(Long contractId, String shop, String discountCode, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract getSubscriptionContractRawInternal(@PathVariable Long contractId, String shop, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceRemovedVariants(String shop, Long contractId, String removedVariantTitle, Long newVariantId, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceVariants(String shop, Long contractId, List<Long> oldVariantIds, List<Long> newVariantIds) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceVariantsV2WithRetry(String shop, Long contractId, List<Long> oldVariantIds, List<Long> newVariantIds, Integer quantity, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource, int retryCycle) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceVariantsV2(String shop, Long contractId, List<Long> oldVariantIdList, List<Long> newVariantIdList, Integer quantity, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceVariantsV3WithRetry(String shop, Long contractId, List<Long> oldVariantIds, Map<Long, Integer> newVariants, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource, int retryCycle) throws Exception;

    SubscriptionContractQuery.SubscriptionContract replaceVariantsV3(String shop, Long contractId, List<Long> oldVariants, Map<Long, Integer> newVariants, String oldLineId, Boolean carryForwardDiscount, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveLineItem(String shop, Long contractId, Long variantId) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsRemoveDeletedLineItem(String shop, Long contractId) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractUpdateMinCycles(Long contractId, String shop, Integer minCycles, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract updateContractWith(
        Long contractId,
        String shop,
        int billingIntervalCount,
        SellingPlanInterval billingInterval,
        ZonedDateTime nextBillingDate,
        ShopifyGraphqlClient shopifyGraphqlClient,
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO,
        Integer maxCycles,
        Integer minCycles,
        Integer deliveryIntervalCount,
        SellingPlanInterval deliveryInterval,
        Boolean overwriteAnchorDay,
        List<SellingPlanAnchorInput> anchorList) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateMaxCycles(Long contractId, String shop, Integer maxCycles, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract splitExistingContract(String shop, Long contractId, List<String> lineIds, boolean isSplitContract, ActivityLogEventSource eventSource) throws Exception;

    SubscriptionContractQuery.SubscriptionContract createMatchingContract(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, List<SubscriptionContractLineDTO> lines, boolean isSplitContract, ActivityLogEventSource eventSource) throws Exception;

    @Transactional
    void updateNewContractDetails(String shop, Long newContractId, Long oldContractId, boolean isSplitContract, boolean attemptBilling, ActivityLogEventSource eventSource) throws Exception;

    void refreshLineInfo(String shop, Long contractId, Long variantId) throws Exception;

    void maybeRemoveDuplicateContracts(String shop);

    Long countAllByShopAndSubscriptionTypeIdentifier(String shop, String uniqueIdentifire);

    void rescheduleOrderFulfillment(String shop, String fulfillmentId, ZonedDateTime deliveryDate) throws Exception;

    Boolean isOverwriteAnchorDay(Long contractId, String shop, ZonedDateTime nextBillingDate, Integer billingIntervalCount, SellingPlanInterval billingInterval) throws Exception;
    SubscriptionContractQuery.SubscriptionContract updateContractSellingPlanAndPricingPolicy(String shop, SubscriptionContractQuery.SubscriptionContract subscriptionContract, FrequencyInfoDTO sellingPlan) throws Exception;

    void updatePaymentInfo(Long contractId, String shop) throws Exception;

    SubscriptionContractQuery.SubscriptionContract subscriptionContractsUpdateDeliveryInterval(Long contractId, String shop, int deliveryIntervalCount, SellingPlanInterval deliveryInterval, ActivityLogEventSource eventSource) throws Exception;

    List<SubscriptionContractDetailsDTO> getTotalSubscriptionsDataCreatedInDateRange(String shop, ZonedDateTime toDate, ZonedDateTime fromDate);

    List<SubscriptionContractDetailsDTO> getTotalCancelledSubscriptionsButCreatedBeforeDateRange(String shop, ZonedDateTime toDate, ZonedDateTime fromDate);

    List<SubscriptionContractDetailsDTO> getNewSubscriptions(String shop, ZonedDateTime fromDate, ZonedDateTime toDate);

    List<SubscriptionContractDetailsDTO> getTotalSubscriptionsDataCreatedBefore(String shop, ZonedDateTime fromDate);
}
