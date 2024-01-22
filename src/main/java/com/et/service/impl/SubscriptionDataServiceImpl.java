package com.et.service.impl;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlDataService;
import com.et.api.graphql.data.Product;
import com.et.api.graphql.data.ProductData;
import com.et.api.graphql.data.ProductVariant;
import com.et.api.graphql.data.VariantOptions;
import com.et.api.graphql.ordercontext.PageInfo;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.pojo.ProductFilterDTO;
import com.et.service.SubscriptionDataService;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.dto.FrequencyInfoDTO;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.service.dto.SubscriptionGroupV2DTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.ProductSortKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class SubscriptionDataServiceImpl implements SubscriptionDataService {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionDataServiceImpl.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopifyGraphqlDataService shopifyGraphqlDataService;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    public static final Gson gson = new Gson();

    /**
     * Get all products with shop.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next       to load next values
     * @param cursor     from which cursor to load
     * @return the list of products.
     */
    @Override
    public ProductData findShopProducts(String shop, String searchText, boolean next, String cursor) throws Exception {
        logger.info("{} Calling shopify graphql for load products", shop);
        return shopifyGraphqlDataService.getShopProducts(commonUtils.prepareShopifyGraphqlClient(shop), searchText, next, cursor);
    }

    @Override
    public ProductData findShopProductsWithAdvanceFilter(String shop, String searchText, boolean next, String cursor, String sorting, ProductFilterDTO productFilterDTO, Integer size) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        int index = 0;
        for (String vendorText : productFilterDTO.getVendor()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("vendor:").append(vendorText).append(index == productFilterDTO.getVendor().size() ? ") AND " : " OR ");
        }

        index = 0;
        for (String tagsText : productFilterDTO.getTags()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("tag:").append(tagsText).append(index == productFilterDTO.getTags().size() ? ") AND " : " OR ");
        }

        index = 0;
        for (String productTypeText : productFilterDTO.getProductType()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("product_type:").append(productTypeText).append(index == productFilterDTO.getProductType().size() ? ") AND " : " OR ");
        }

        if (StringUtils.isNoneEmpty(searchText)) {
            queryBuilder.append("status:active AND (title:*").append(searchText).append("* OR ").append(searchText).append(")");
        } else {
            queryBuilder.append("status:active");
        }
        ProductSearchWithSortQuery productSearchQuery = new ProductSearchWithSortQuery(Input.optional(queryBuilder.toString()), Input.optional(ProductSortKeys.TITLE), Input.optional(StringUtils.isNotEmpty(sorting) && sorting.equals("2")), Input.optional(size), next ? Input.optional(cursor) : Input.optional(null));
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        Response<Optional<ProductSearchWithSortQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(productSearchQuery);
        ProductData productData = new ProductData();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);
        List<Product> products = response.getData().get().getProducts().getEdges().stream().map(ProductSearchWithSortQuery.Edge::getNode).map(node -> {
            Product product = new Product();
            product.setTitle(node.getTitle());
            String idString = node.getId().replace("gid://shopify/Product/", "");
            product.setCurrencyCode(node.getPriceRangeV2().getMaxVariantPrice().getCurrencyCode());
            product.setTotalInventory(node.getTotalInventory());
            product.setTracksInventory(node.isTracksInventory());
            product.setStatus(node.getStatus());
            product.setPrice(Double.parseDouble(node.getPriceRangeV2().getMaxVariantPrice().getAmount().toString()));
            product.setId(Long.parseLong(idString));
            product.setHandle(node.getHandle());
            product.setRequiresSellingPlan(node.isRequiresSellingPlan());
            product.setOnlineStoreUrl(node.getOnlineStoreUrl().orElse("").toString());
            if (node.getFeaturedImage().isPresent()) {
                String imageId = node.getFeaturedImage().get().getId().get().replace("gid://shopify/ProductImage/", "");
                product.setImageId(Long.parseLong(imageId));
                product.setImageSrc(node.getFeaturedImage().get().getTransformedSrc().toString());
            }
            return product;
        }).collect(Collectors.toList());

        if (response.getData().isPresent() && !products.isEmpty()) {
            boolean hasNextPage = ((com.shopify.java.graphql.client.queries.ProductSearchWithSortQuery.Data) ((Optional) response.getData()).get()).getProducts().getPageInfo().isHasNextPage();
            pageInfo.setHasNextPage(hasNextPage);
        }

        if (pageInfo.isHasNextPage()) {
            Optional<ProductSearchWithSortQuery.Edge> last = response.getData().get().getProducts().getEdges().stream().reduce((first, second) -> second);
            last.ifPresent((edge) -> {
                pageInfo.setCursor(edge.getCursor());
            });
        }
        productData.setProducts(products);
        productData.setPageInfo(pageInfo);
        return productData;
    }

    @Override
    public ProductData findShopProducts(String shop, String searchText, boolean next, String cursor, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {
        logger.info("{} Calling shopify graphql for load products", shop);
        return shopifyGraphqlDataService.getShopProducts(shopifyGraphqlClient, searchText, next, cursor);
    }

    /**
     * Get all product variants with shop.
     *
     * @param shop          the shop of the entity.
     * @param searchText    the title search text
     * @param next          to load next values
     * @param cursor        from which cursor to load
     * @param variantNext
     * @param variantCursor
     * @return the list of products.
     */
    @Override
    public ProductData findShopProductVariants(String shop, String searchText, boolean next, String cursor, boolean variantNext, String variantCursor, String vendor, String tags, String productType, String collection, String sellingPlanIds) throws Exception {
        logger.info("{} Calling shopify graphql for load product variants", shop);

        List<String> searchTextList = new ArrayList<>();
        if (StringUtils.isNotBlank(searchText)) {
            searchText = searchText.replaceAll("^[^a-zA-Z0-9][\\s]+|[\\s]+[^a-zA-Z0-9][\\s]+|[\\s]+[^a-zA-Z0-9]$", " ");
            searchTextList = Arrays.stream(searchText.split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        ProductData productData = new ProductData();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);
        List<Product> products = new ArrayList<>();

        if (StringUtils.isNotEmpty(collection)) {

            Optional<CollectionProductVariantQuery.Data> responseData = searchShopCollectionProducts(shop, next, cursor, variantNext, variantCursor,  "gid://shopify/Collection/" + collection);
            getCollectionProductVariantData(responseData, pageInfo, products);
        } else {
            Optional<ProductVariantSearchQuery.Data> responseData = searchShopProductsByTitleMatching(shop, searchTextList, next, cursor, variantNext, variantCursor, vendor, tags, productType, sellingPlanIds);
            getProductVariantSearchData(responseData, pageInfo, products);
        }

        productData.setProducts(products);
        productData.setPageInfo(pageInfo);
        return productData;
    }

    private void getCollectionProductVariantData(Optional<CollectionProductVariantQuery.Data> responseData, PageInfo pageInfo, List<Product> products) {
        Optional<List<CollectionProductVariantQuery.Edge>> productVariantsEdges = requireNonNull(responseData).map(CollectionProductVariantQuery.Data::getCollection).map(Optional::get).map(CollectionProductVariantQuery.Collection::getProducts).map(CollectionProductVariantQuery.Products::getEdges);
        productVariantsEdges.get().stream().map(CollectionProductVariantQuery.Edge::getNode).forEach(node -> {
            Product product = new Product();
            product.setTitle(node.getTitle());
            product.setCurrencyCode(node.getPriceRangeV2().getMaxVariantPrice().getCurrencyCode());
            String idString = node.getId().replace("gid://shopify/Product/", "");
            product.setId(Long.parseLong(idString));
            product.setOnlineStoreUrl(node.getOnlineStoreUrl().orElse("").toString());
            product.setStatus(node.getStatus());
            if (!ListUtils.isEmpty(node.getTags())) {
                product.setTags(gson.toJson(node.getTags()));
            }
            product.setProductType(node.getProductType());
            product.setVendor(node.getVendor());

            product.setRequiresSellingPlan(node.isRequiresSellingPlan());
            if (node.getFeaturedImage().isPresent()) {
                String imageId = node.getFeaturedImage().get().getId().get().replace("gid://shopify/ProductImage/", "");
                product.setImageId(Long.parseLong(imageId));
                product.setImageSrc(node.getFeaturedImage().get().getTransformedSrc().toString());
            }

            List<ProductVariant> variants = new ArrayList<>();

            node.getVariants().getEdges().stream().map(CollectionProductVariantQuery.Edge1::getNode).forEach(variantNode -> {
                ProductVariant productVariant = new ProductVariant();
                String variantId = variantNode.getId().replace("gid://shopify/ProductVariant/", "");
                productVariant.setId(Long.parseLong(variantId));
                productVariant.setTitle(variantNode.getTitle());
                productVariant.setPrice((String) variantNode.getPrice());
                productVariant.setDisplayName(variantNode.getDisplayName());
                List<VariantOptions> selectedOptions = new ArrayList<>();
                variantNode.getSelectedOptions().forEach(options -> {
                    VariantOptions variantOptions = new VariantOptions();
                    variantOptions.setValue(options.getValue());
                    variantOptions.setName(options.getName());
                    selectedOptions.add(variantOptions);
                });
                if (variantNode.getImage().isPresent()){
                    productVariant.setImageSrc(variantNode.getImage().get().getUrl().toString());
                }
                productVariant.setSelectedOptions(selectedOptions);
                productVariant.setProductId(Long.parseLong(node.getId().replace("gid://shopify/Product/", "")));
                productVariant.setProductHandle(node.getHandle());
                variants.add(productVariant);
            });
            product.setVariants(variants);
            PageInfo variantPageInfo = new PageInfo();
            variantPageInfo.setHasNextPage(node.getVariants().getPageInfo().isHasNextPage());
            if (variantPageInfo.isHasNextPage()) {
                node.getVariants().getEdges().stream().reduce((f, s) -> s).ifPresent((v) -> variantPageInfo.setCursor(v.getCursor()));
            }
            product.setPageInfo(variantPageInfo);
            products.add(product);
        });
        if (!CollectionUtils.isEmpty(products)) {
            if (responseData.get().getCollection().isPresent()) {
                responseData.ifPresent(data -> pageInfo.setHasNextPage(data.getCollection().get().getProducts().getPageInfo().isHasNextPage()));
            }
        }

        if (pageInfo.isHasNextPage()) {
            Optional<CollectionProductVariantQuery.Edge> last = productVariantsEdges.get().stream().reduce((first, second) -> second);
            last.ifPresent((edge) -> pageInfo.setCursor(edge.getCursor()));
        }
    }

    private void getProductVariantSearchData(Optional<ProductVariantSearchQuery.Data> responseData, PageInfo pageInfo, List<Product> products) {
        List<ProductVariantSearchQuery.Edge> productVariantsEdges = requireNonNull(responseData).map(ProductVariantSearchQuery.Data::getProducts).map(ProductVariantSearchQuery.Products::getEdges).orElse(new ArrayList<>());
        productVariantsEdges.stream().map(ProductVariantSearchQuery.Edge::getNode).forEach(node -> {
                Product product = new Product();
                product.setTitle(node.getTitle());
                product.setCurrencyCode(node.getPriceRangeV2().getMaxVariantPrice().getCurrencyCode());
                String idString = node.getId().replace("gid://shopify/Product/", "");
                product.setId(Long.parseLong(idString));
                product.setOnlineStoreUrl(node.getOnlineStoreUrl().orElse("").toString());
                product.setStatus(node.getStatus());
                if (!ListUtils.isEmpty(node.getTags())) {
                    product.setTags(gson.toJson(node.getTags()));
                }
                product.setProductType(node.getProductType());
                product.setVendor(node.getVendor());

                product.setRequiresSellingPlan(node.isRequiresSellingPlan());
                if (node.getFeaturedImage().isPresent()) {
                    String imageId = node.getFeaturedImage().get().getId().get().replace("gid://shopify/ProductImage/", "");
                    product.setImageId(Long.parseLong(imageId));
                    product.setImageSrc(node.getFeaturedImage().get().getTransformedSrc().toString());
                }

                List<ProductVariant> variants = new ArrayList<>();
                node.getVariants().getEdges().stream().map(ProductVariantSearchQuery.Edge1::getNode).forEach(variantNode -> {
                    ProductVariant productVariant = new ProductVariant();
                    String variantId = variantNode.getId().replace("gid://shopify/ProductVariant/", "");
                    productVariant.setId(Long.parseLong(variantId));
                    productVariant.setTitle(variantNode.getTitle());
                    productVariant.setPrice((String) variantNode.getPrice());
                    productVariant.setDisplayName(variantNode.getDisplayName());
                    List<VariantOptions> selectedOptions = new ArrayList<>();
                    variantNode.getSelectedOptions().forEach(options -> {
                        VariantOptions variantOptions = new VariantOptions();
                        variantOptions.setValue(options.getValue());
                        variantOptions.setName(options.getName());
                        selectedOptions.add(variantOptions);
                    });
                    if (variantNode.getImage().isPresent()){
                        productVariant.setImageSrc(variantNode.getImage().get().getUrl().toString());
                    }
                    productVariant.setSelectedOptions(selectedOptions);
                    productVariant.setProductId(Long.parseLong(node.getId().replace("gid://shopify/Product/", "")));
                    productVariant.setProductHandle(node.getHandle());
                    variants.add(productVariant);
                });
                product.setVariants(variants);
                PageInfo variantPageInfo = new PageInfo();
                variantPageInfo.setHasNextPage(node.getVariants().getPageInfo().isHasNextPage());
                if (variantPageInfo.isHasNextPage()) {
                    node.getVariants().getEdges().stream().reduce((f, s) -> s).ifPresent((v) -> variantPageInfo.setCursor(v.getCursor()));
                }
                product.setPageInfo(variantPageInfo);
                products.add(product);
            });
        if (!CollectionUtils.isEmpty(products)) {
            responseData.ifPresent(data -> pageInfo.setHasNextPage(data.getProducts().getPageInfo().isHasNextPage()));
        }

        if (pageInfo.isHasNextPage()) {
            Optional<ProductVariantSearchQuery.Edge> last = productVariantsEdges.stream().reduce((first, second) -> second);
            last.ifPresent((edge) -> pageInfo.setCursor(edge.getCursor()));
        }
    }

    private Set<String> convertStringToArray(String arrayString) {
        return Arrays
            .stream(StringUtils.split(Optional.of(arrayString).orElse(StringUtils.EMPTY), ","))
            .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    private Optional<ProductVariantSearchQuery.Data> searchShopProductsByTitleMatching(String shop, List<String> searchTextList, boolean next, String cursor, boolean variantNext, String variantCursor, String vendor, String tags, String productType, String sellingPlanIds) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Set<String> productIdList = new HashSet<>();
        if (StringUtils.isNotBlank(sellingPlanIds)) {
            Set<String> sellingPlanIdsArray = convertStringToArray(sellingPlanIds);
            List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            subscriptionGroupPlanDTOS = subscriptionGroupPlanDTOS.stream()
                .filter(subscriptionGroupPlanDTO -> {
                    try {
                        SubscriptionGroupV2DTO subscriptionGroupV2DTO = objectMapper.readValue(subscriptionGroupPlanDTO.getInfoJson(), SubscriptionGroupV2DTO.class);
                        Set<String> existingSellingPlanIds = subscriptionGroupV2DTO.getSubscriptionPlans().stream()
                            .map(FrequencyInfoDTO::getId)
                            .collect(Collectors.toSet());

                        return !Collections.disjoint(sellingPlanIdsArray, existingSellingPlanIds);
                    } catch (Exception ignored) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

            productIdList = subscriptionGroupPlanDTOS.stream()
                .flatMap(subscriptionGroupPlanDTO -> {
                    Set<String> allProducts = new HashSet<>();
                    Set<String> productIds = convertStringToArray(subscriptionGroupPlanDTO.getProductIds());
                    Set<String> variantProductIds = convertStringToArray(subscriptionGroupPlanDTO.getVariantProductIds());

                    allProducts.addAll(productIds);
                    allProducts.addAll(variantProductIds);

                    return allProducts.stream();
                })
                .collect(Collectors.toSet());
        }


        String query = null;
        ProductFilterDTO productFilterDTO = new ProductFilterDTO(vendor, tags, productType);
        StringBuilder queryBuilder = new StringBuilder();

        int index = 0;
        for (String productId : productIdList) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("id:").append(productId).append(index == productIdList.size() ? ") AND " : " OR ");
        }

        index = 0;
        for (String vendorText : productFilterDTO.getVendor()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("vendor:").append(vendorText).append(index == productFilterDTO.getVendor().size() ? ") AND " : " OR ");
        }

        index = 0;
        for (String tagsText : productFilterDTO.getTags()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("tag:").append(tagsText).append(index == productFilterDTO.getTags().size() ? ") AND " : " OR ");
        }

        index = 0;
        for (String productTypeText : productFilterDTO.getProductType()) {
            index++;
            queryBuilder.append(index == 1 ? "(" : "").append("product_type:").append(productTypeText).append(index == productFilterDTO.getProductType().size() ? ") AND " : " OR ");
        }

        if (!CollectionUtils.isEmpty(searchTextList)) {
            for (String searchText : searchTextList) {
                queryBuilder.append("title:").append(searchText).append("* AND ");
            }
        }

        if (StringUtils.isNotBlank(queryBuilder)) {
            query = queryBuilder.substring(0, queryBuilder.length() - 5);
        }
        ProductVariantSearchQuery productSearchQuery = new ProductVariantSearchQuery(
            Input.optional(query),
            next ? Input.optional(cursor) : Input.optional(null),
            variantNext ? Input.optional(variantCursor) : Input.optional(null));

        Response<Optional<ProductVariantSearchQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(productSearchQuery);
        return response.getData();
    }

    private Optional<CollectionProductVariantQuery.Data> searchShopCollectionProducts(String shop, boolean next, String cursor, boolean variantNext, String variantCursor, String collectionId) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        CollectionProductVariantQuery collectionProductVariantQuery = new CollectionProductVariantQuery(collectionId, next ? Input.optional(cursor) : Input.optional(null), variantNext ? Input.optional(variantCursor) : Input.optional(null));
        Response<Optional<CollectionProductVariantQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(collectionProductVariantQuery);
        return response.getData();
    }

    /**
     * Get all product collections with shop.
     *
     * @param shop       the shop of the entity.
     * @param searchText the title search text
     * @param next       to load next values
     * @param cursor     from which cursor to load
     * @return the list of products.
     */
    @Override
    public ProductData findShopProductCollections(String shop, String searchText, boolean next, String cursor) throws Exception {
        logger.info("{} Calling shopify graphql for load product collections", shop);
        return shopifyGraphqlDataService.getShopProductCollections(commonUtils.prepareShopifyGraphqlClient(shop), searchText, next, cursor);
    }

    @Override
    public List<SubscribedProductVariantInfo> getSubscriptionProductsData(List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS, Set<String> sellingPlanIds){

        Set<Long> productIds = new HashSet<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        List<SubscribedProductVariantInfo> productList = new ArrayList<>();
        for(SubscriptionGroupPlanDTO subscriptionGroupPlanDTO : subscriptionGroupPlanDTOS){
            try {
                SubscriptionGroupV2DTO subscriptionGroupV2DTO = objectMapper.readValue(subscriptionGroupPlanDTO.getInfoJson(), SubscriptionGroupV2DTO.class);

                Set<String> existingSellingPlanIds = subscriptionGroupV2DTO.getSubscriptionPlans().stream().map(FrequencyInfoDTO::getId).collect(Collectors.toSet());

                if (CollectionUtils.isEmpty(sellingPlanIds) || Sets.intersection(sellingPlanIds, existingSellingPlanIds).size() > 0) {

                    if (StringUtils.isNotBlank(subscriptionGroupV2DTO.getProductIds())) {
                        List<SubscribedProductVariantInfo> subscribedProductVariantInfoList = objectMapper.readValue(subscriptionGroupV2DTO.getProductIds(), new TypeReference<List<SubscribedProductVariantInfo>>() {
                        });

                        for (SubscribedProductVariantInfo subscribedProductVariantInfo : subscribedProductVariantInfoList) {
                            if (!productIds.contains(subscribedProductVariantInfo.getId())) {
                                productIds.add(subscribedProductVariantInfo.getId());
                                productList.add(subscribedProductVariantInfo);
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(subscriptionGroupV2DTO.getVariantIds())) {
                        List<SubscribedProductVariantInfo> subscribedProductVariantInfoList = objectMapper.readValue(subscriptionGroupV2DTO.getVariantIds(), new TypeReference<List<SubscribedProductVariantInfo>>() {
                        });

                        for (SubscribedProductVariantInfo subscribedProductVariantInfo : subscribedProductVariantInfoList) {
                            if (!productIds.contains(subscribedProductVariantInfo.getId())) {
                                productIds.add(subscribedProductVariantInfo.getId());
                                productList.add(subscribedProductVariantInfo);
                            }
                        }
                    }
                }
            }catch (Exception e){
                logger.error("An error occurred while getting products for Subscription Group Plan: {}", subscriptionGroupPlanDTO.getSubscriptionId());
            }
        }
        return productList;
    }

    @Override
    public Map<Long, String> getProductHandles(String shop, List<String> productIDs) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<String> graphQlProductIds = productIDs.stream().map(ShopifyGraphQLUtils::getGraphQLProductId).distinct().collect(Collectors.toList());

        ProductNodesQuery productNodesQuery = new ProductNodesQuery(graphQlProductIds);
        Response<Optional<ProductNodesQuery.Data>> optionalResponse = shopifyGraphqlClient.getOptionalQueryResponse(productNodesQuery);

        if (!optionalResponse.hasErrors() && optionalResponse.getData().isPresent()) {
            List<ProductNodesQuery.AsProduct> productList = optionalResponse.getData().get().getNodes().stream()
                .map(node -> ((ProductNodesQuery.AsProduct) node)).collect(Collectors.toList());
            Map<Long, String> productHandleMap = new HashMap<>();
            for (ProductNodesQuery.AsProduct product : productList) {
                productHandleMap.put(ShopifyGraphQLUtils.getProductId(product.getId()), product.getHandle());
            }
            return productHandleMap;
        }
        return null;
    }
}
