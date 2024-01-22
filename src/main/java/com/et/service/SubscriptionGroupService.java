package com.et.service;


import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.data.ProductData;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.et.service.dto.FrequencyInfoDTO;
import com.et.service.dto.SubscriptionGroupDTO;
import com.et.service.dto.SubscriptionGroupV2DTO;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.shopify.java.graphql.client.queries.SellingPlanGroupQuery;
import com.shopify.java.graphql.client.type.SellingPlanInterval;

import java.util.List;
import java.util.Optional;

public interface SubscriptionGroupService {

    List<SubscriptionGroupDTO> findShopSubscriptionGroups(String shop) throws Exception;

    ProductData getSellingGroupProducts(ShopifyGraphqlClient shopifyGraphqlClient, Long sellingPlanGroupId, boolean next, String cursor) throws Exception;

    ProductData getSellingGroupProductVariants(ShopifyGraphqlClient shopifyGraphqlClient, Long sellingPlanGroupId, boolean next, String cursor) throws Exception;

    FrequencyIntervalUnit buildFrequencyInterval(SellingPlanInterval interval);

    Optional<SellingPlanGroupQuery.SellingPlanGroup> getSingleSubscriptionGroupRaw(String shop, String sellingPlanGroupId) throws Exception;

    /**
     * Get all subscriptionGroup products.
     *
     * @param shop           the shop of the entity.
     * @param sellingGroupId the id of the entity
     * @param next           to load next values
     * @param cursor         from which cursor to load
     * @return the list of products.
     */
    ProductData findSubscriptionGroupProducts(String shop, Long sellingGroupId, boolean next, String cursor);

    /**
     * Delete the "id" subscriptionGroup.
     *
     * @param shop           the id of the entity.
     * @param sellingGroupId the id of the entity.
     */
    void delete(String shop, Long sellingGroupId) throws Exception;

    SubscriptionGroupV2DTO saveSubscriptionGroupV2(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> productIds, List<String> variantIds) throws Exception;

    List<SubscriptionGroupV2DTO> findShopSubscriptionGroupsV2(String shop) throws Exception;

    Optional<SubscriptionGroupV2DTO> findSingleSubscriptionGroupV2(String shop, Long subscriptionGroupId) throws Exception;

    void sortSellingPlanGroupProductOrVariant(String shop, Long sellingPlanGroupId, List<Long> productIdList, List<Long> variantIdList);

    Optional<SubscriptionGroupV2DTO> findSingleSubscriptionGroupV3(String shop, Long subscriptionGroupId) throws Exception;

    Optional<SubscriptionGroupV2DTO> syncSubscriptionGroupPlan(String shop, Long subscriptionGroupId) throws Exception;

    Optional<SubscriptionGroupV2DTO> getSubscriptionGroupPlanDetail(String shop, Long subscriptionGroupId) throws Exception;

    Optional<ProductData> getSellingGroupVariantsData(String shop, Long sellingPlanGroupId, boolean hasNextPage, String cursor) throws Exception;

    Optional<ProductData> getSellingGroupProductsData(String shop, Long sellingPlanGroupId, boolean hasNextPage, String cursor) throws Exception;

    SubscriptionGroupV2DTO updateSubscriptionGroupV2(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> newProductIds, List<String> variantIds) throws Exception;

    SubscriptionGroupV2DTO updateSubscriptionGroupDetails(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop) throws Exception;

    SubscriptionGroupV2DTO updateSubscriptionGroupProducts(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> updateProductIds, List<String> updateVariantIds, List<SubscribedProductVariantInfo> oldProductInfoList, List<SubscribedProductVariantInfo> oldVariantInfoList) throws Exception;

    SubscriptionGroupV2DTO deleteSubscriptionGroupProducts(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> updateProductIds, List<String> updateVariantIds, List<SubscribedProductVariantInfo> oldProductInfoList, List<SubscribedProductVariantInfo> oldVariantInfoList) throws Exception;

    void insertAllShopSubscriptionGroupsV2() throws Exception;

    List<FrequencyInfoDTO> getAllSellingPlans(String shopName);

    FrequencyInfoDTO getSellingPlanById(String shopName, Long sellingPlanId);

    List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId, Integer billingFrequencyCount, SellingPlanInterval billingSellingPlanInterval);

    List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId, Integer billingFrequencyCount, FrequencyIntervalUnit billingFrequencyInterval);

    List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId);

    void exportSubscriptionGroups(String shop, String email);
}
