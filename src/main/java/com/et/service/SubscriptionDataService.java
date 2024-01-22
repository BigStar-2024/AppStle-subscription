package com.et.service;

import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.data.ProductData;
import com.et.pojo.ProductFilterDTO;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.web.rest.vm.SubscribedProductVariantInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SubscriptionDataService {

    /**
     * Get all products with shop.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next to load next values
     * @param cursor from which cursor to load
     * @return the list of products.
     */
    ProductData findShopProducts(String shop, String searchText, boolean next, String cursor) throws Exception;

    /**
     * Get all products with shop and advance filter.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next to load next values
     * @param cursor from which cursor to load
     * @return the list of products.
     */
    ProductData findShopProductsWithAdvanceFilter(String shop, String searchText, boolean next, String cursor, String sorting, ProductFilterDTO productFilterDTO, Integer size) throws Exception;

    ProductData findShopProducts(String shop, String searchText, boolean next, String cursor, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception;

    /**
     * Get all product variants with shop.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next to load next values
     * @param cursor from which cursor to load
     * @param variantNext
     * @param variantCursor
     * @return the list of products.
     */
    ProductData findShopProductVariants(String shop, String searchText, boolean next, String cursor, boolean variantNext, String variantCursor, String vendor, String tags, String productType, String collection, String sellingPlanIds) throws Exception;

    /**
     * Get all product collections with shop.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next to load next values
     * @param cursor from which cursor to load
     * @return the list of products.
     */
    ProductData findShopProductCollections(String shop, String searchText, boolean next, String cursor) throws Exception;

    List<SubscribedProductVariantInfo> getSubscriptionProductsData(List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS, Set<String> sellingPlanIds);

    Map<Long, String> getProductHandles(String shop, List<String> productIDs) throws Exception;
}
