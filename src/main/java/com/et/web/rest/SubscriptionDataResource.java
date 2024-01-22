package com.et.web.rest;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.data.ProductData;
import com.et.api.graphql.ordercontext.PageInfo;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.country.Country;
import com.et.api.shopify.currency.Currency;
import com.et.api.shopify.product.GetProductsResponse;
import com.et.api.shopify.product.Product;
import com.et.api.shopify.product.Variant;
import com.et.api.shopify.shop.Shop;
import com.et.api.utils.CurrencyUtils;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.enumeration.ProductSelectionOption;
import com.et.pojo.ProductDetails;
import com.et.pojo.ProductFilterDTO;
import com.et.security.SecurityUtils;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.ContextualPricingContext;
import com.shopify.java.graphql.client.type.CountryCode;
import com.shopify.java.graphql.client.type.ProductStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Subscription Data Resource")
public class SubscriptionDataResource {

    public static final String NULL_STRING = "null";
    private final Logger log = LoggerFactory.getLogger(SubscriptionDataResource.class);

    private static final String ENTITY_NAME = "subscriptionData";

    @Autowired
    private SubscriptionDataService subscriptionDataService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private ProductInfoService productInfoService;

    public static final Gson gson = new Gson();

    /**
     * {@code GET  /api/data/products} : get product data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the ProductData data in body.
     */
    @GetMapping(value = {"/api/data/products", "/subscriptions/cp/api/data/products"})
    @CrossOrigin
    public ProductData getProductsData(@RequestParam(value = "search", required = false) String search,
                                       @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                       @RequestParam(value = "cursor", required = false) String cursor,
                                       @RequestParam(value = "contractId", required = false) Long contractId,
                                       @RequestParam(value = "sellingPlanIds", required = false) String sellingPlanIds,
                                       @RequestParam(value = "sendAllData", required = false) Boolean sendAllData,
                                       @RequestParam(value = "purchaseOption", required = false) String purchaseOption,
                                       HttpServletRequest request)
        throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();


        return productData(search, next, cursor, shop, contractId, sellingPlanIds, sendAllData, purchaseOption);
    }


    @GetMapping(value = {"/api/data/products1", "/subscriptions/cp/api/data/products1"})
    public ProductData getProductsData(@RequestParam(value = "search", required = false) String search,
                                       @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                       @RequestParam(value = "cursor", required = false) String cursor,
                                       @RequestParam(value = "vendor", required = false) String vendor,
                                       @RequestParam(value = "tags", required = false) String tags,
                                       @RequestParam(value = "sorting", required = false) String sorting,
                                       @RequestParam(value = "productType", required = false) String productType,
                                       @RequestParam(value = "sellingPlanIds", required = false) String sellingPlanIds,
                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                       @RequestParam(value = "productSelectionOption") ProductSelectionOption productSelectionOption,
                                       @RequestParam(value = "purchaseOption", required = false) String purchaseOption) throws Exception {

        //if one_time then always send all products if subscribe
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ProductData productData = new ProductData();

        CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();

        if (Objects.equals(purchaseOption, "ONE_TIME") && ObjectUtils.isNotEmpty(customerPortalSettingsDTO.getAllowedProductIdsForOneTimeProductAdd())) {
            List<String> productIDs = Arrays.asList(customerPortalSettingsDTO.getAllowedProductIdsForOneTimeProductAdd().split("\\s*,\\s*"));
            PageInfo pageInfo = new PageInfo();
            pageInfo.setHasNextPage(false);
            pageInfo.setCursor(null);
            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            List<String> graphQlProductIds = productIDs.stream().map(ShopifyGraphQLUtils::getGraphQLProductId).distinct().collect(Collectors.toList());

            ProductNodesQuery productNodesQuery = new ProductNodesQuery(graphQlProductIds);
            Response<Optional<ProductNodesQuery.Data>> optionalResponse = shopifyGraphqlClient.getOptionalQueryResponse(productNodesQuery);

            if (!optionalResponse.hasErrors() && optionalResponse.getData().isPresent()) {
                List<ProductNodesQuery.AsProduct> productList = optionalResponse.getData().get().getNodes().stream()
                    .filter(ObjectUtils::isNotEmpty)
                    .map(node -> ((ProductNodesQuery.AsProduct) node)).collect(Collectors.toList());
                Map<Long, String> productHandleMap = new HashMap<>();
                AtomicInteger i = new AtomicInteger();
                productList.forEach(p -> {
                    productHandleMap.put((long) i.get(), p.getHandle());
                    i.getAndIncrement();
                });
                productData.setProductHandleData(productHandleMap);
                productData.setPageInfo(pageInfo);
            }
        } else if (ProductSelectionOption.ALL_PRODUCTS.equals(productSelectionOption)) {

            ProductFilterDTO productFilterDTO = new ProductFilterDTO(vendor, tags, productType);
            ProductData products = subscriptionDataService.findShopProductsWithAdvanceFilter(shop, search, next, cursor, sorting, productFilterDTO, size);
            productData.setPageInfo(products.getPageInfo());
            Map<Long, String> productHandleData = new LinkedHashMap<>();
            AtomicInteger i = new AtomicInteger();
            products.getProducts().forEach(p -> {
                if (!productHandleData.containsValue(p.getHandle())) {
                    productHandleData.put((long) i.get(), p.getHandle());
                    i.getAndIncrement();
                }
            });
            productData.setProductHandleData(productHandleData);
        } else {
            Set<String> sellingPlanIdsArray = new HashSet<>();

            if (!ProductSelectionOption.PRODUCTS_FROM_ALL_PLANS.equals(productSelectionOption)) {
                sellingPlanIdsArray = Arrays
                    .stream(StringUtils.split(Optional.ofNullable(sellingPlanIds).orElse(StringUtils.EMPTY), ","))
                    .map(String::trim).map(s -> s.equals("") ? "null" : s).map(ShopifyGraphQLUtils::getGraphQLSellingPlanId)
                    .collect(Collectors.toSet());
            }

            List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);

            List<SubscribedProductVariantInfo> productList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(subscriptionGroupPlanDTOS)) {
                productList = subscriptionDataService.getSubscriptionProductsData(subscriptionGroupPlanDTOS, sellingPlanIdsArray);
            }

            // Filter based on search
            if (StringUtils.isNotBlank(vendor) || StringUtils.isNotBlank(tags) || StringUtils.isNotBlank(productType) || StringUtils.isNotBlank(search)) {
                ProductFilterDTO productFilterDTO = new ProductFilterDTO(vendor, tags, productType);
                productList = productList.stream().filter(p -> (
                    (!productFilterDTO.getVendor().isEmpty() && p.getAdditionalProperties().get("vendor") != null && productFilterDTO.getVendor().contains(p.getAdditionalProperties().get("vendor").toString())) ||
                        (checkIsTagsAvailable(productFilterDTO.getTags(), p.getAdditionalProperties().get("tags"))) ||
                        (!productFilterDTO.getProductType().isEmpty() && p.getAdditionalProperties().get("productType") != null && productFilterDTO.getProductType().contains(p.getAdditionalProperties().get("productType").toString())) ||
                        (StringUtils.isNotBlank(search) && p.getTitle() != null && p.getTitle().toLowerCase().contains(search.trim().toLowerCase())))).collect(Collectors.toList());
            }

            if (productList.size() > 0) {
                List<SubscribedProductVariantInfo> newproductList = new ArrayList<>();
                productList.forEach(productVariantInfo -> {
                    if (productVariantInfo.getHandle() != null && newproductList.stream().noneMatch(subscribedProductVariantInfo -> subscribedProductVariantInfo.getHandle().equals(productVariantInfo.getHandle()))) {
                        newproductList.add(productVariantInfo);
                    }
                });
                productList = newproductList;
            }

            if (StringUtils.isNotEmpty(sorting)) {
                if (sorting.equalsIgnoreCase("1")) {
                    productList = productList.stream().sorted((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle())).collect(Collectors.toList());
                } else if (sorting.equalsIgnoreCase("2")) {
                    productList = productList.stream().sorted((o1, o2) -> o2.getTitle().compareToIgnoreCase(o1.getTitle())).collect(Collectors.toList());
                } else if (sorting.equalsIgnoreCase("3")) {
                    productList = productList.stream().sorted(Comparator.comparing(subscribedProductVariantInfo -> parseDoubleValue(subscribedProductVariantInfo.getAdditionalProperties().get("price")), Comparator.nullsLast(Double::compareTo))).collect(Collectors.toList());
                } else if (sorting.equalsIgnoreCase("4")) {
                    productList = productList.stream().sorted(Comparator.comparing(subscribedProductVariantInfo -> parseDoubleValue(subscribedProductVariantInfo.getAdditionalProperties().get("price")), Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                }
            }

            int page = 0;

            if (next && StringUtils.isNotBlank(cursor) && cursor.contains("-")) {
                String[] cursorTokens = cursor.split("-");
                page = Integer.parseInt(cursorTokens[0]);
            }

            int fromIndex = page * size;
            int toIndex = (page * size) + size;
            boolean hasNextPage = false;

            if (productList.size() > toIndex) {
                hasNextPage = true;
            } else if (productList.size() < toIndex) {
                toIndex = productList.size();
            }

            productList = productList.subList(fromIndex, toIndex);

            Map<Long, String> productHandleData = new HashMap<>();
            AtomicInteger i = new AtomicInteger();
            productList.forEach(p -> {
                if (!productHandleData.containsValue(p.getHandle())) {
                    productHandleData.put((long) i.get(), p.getHandle());
                    i.getAndIncrement();
                }
            });

            PageInfo pageInfo = new PageInfo();
            pageInfo.setHasNextPage(hasNextPage);

            if (hasNextPage) {
                String customCursor = (page + 1) + "-" + size;
                pageInfo.setCursor(customCursor);
            }

            productData.setProductHandleData(productHandleData);
            productData.setPageInfo(pageInfo);
        }


        return productData;
    }

    private Double parseDoubleValue(Object value) {
        return ObjectUtils.isNotEmpty(value) ? Double.parseDouble(value.toString()) : null;
    }

    private Boolean checkIsTagsAvailable(Set<String> searchTags, Object tags) {
        Set stringSet = null;
        if (ObjectUtils.isNotEmpty(tags)) {
            stringSet = gson.fromJson(tags.toString(), Set.class);
        }
        return stringSet != null && !stringSet.isEmpty() && !searchTags.isEmpty() && stringSet.stream().anyMatch(element -> searchTags.contains(element.toString()));
    }

    @GetMapping("/api/data/external/v2/products")
    @CrossOrigin
    @ApiOperation("Get Product Data")
    public ProductData getProductsDataV2(@ApiParam("Search") @RequestParam(value = "search", required = false) String search,
                                         @ApiParam("Next") @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                         @ApiParam("Cursor") @RequestParam(value = "cursor", required = false) String cursor,
                                         @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                         @ApiParam("Contract ID") @RequestParam(value = "contractId", required = false) Long contractId,
                                         @ApiParam("Selling Plan IDs") @RequestParam(value = "sellingPlanIds", required = false) String sellingPlanIds,
                                         @ApiParam("Send all data?") @RequestParam(value = "sendAllData", required = false) Boolean sendAllData,
                                         HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get all products");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/products api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return productData(search, next, cursor, shop, contractId, sellingPlanIds, sendAllData, "");
    }

    @Nullable
    private ProductData productData(String search, boolean next, String cursor, String shop, Long contractId, String sellingPlanIds, Boolean sendAllData, String purchaseOption) throws Exception {
        ProductData productData = subscriptionDataService.findShopProducts(shop, search, next, cursor);
        Set<String> sellingPlanIdsArray = Arrays
            .stream(StringUtils.split(Optional.ofNullable(sellingPlanIds).orElse(StringUtils.EMPTY), ","))
            .map(String::trim).map(s -> s.equals("") ? "null" : s)
            .collect(Collectors.toSet());

        try {
            if (contractId != null) {
                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).get();

                if ((sellingPlanIdsArray.contains(NULL_STRING) || sellingPlanIdsArray.isEmpty()) && customerPortalSettingsDTO.getProductSelectionOption() == ProductSelectionOption.PRODUCTS_FROM_CURRENT_PLAN) {
                    customerPortalSettingsDTO.setProductSelectionOption(ProductSelectionOption.ALL_PRODUCTS);
                }

                if (BooleanUtils.isTrue(sendAllData)) {
                    customerPortalSettingsDTO.setProductSelectionOption(ProductSelectionOption.ALL_PRODUCTS);
                }

                if (customerPortalSettingsDTO.getProductSelectionOption() != null && !customerPortalSettingsDTO.getProductSelectionOption().equals(ProductSelectionOption.ALL_PRODUCTS)) {
                    //if (customerPortalSettingsDTO.getProductSelectionOption() != null && !customerPortalSettingsDTO.getProductSelectionOption().equals(ProductSelectionOption.ALL_PRODUCTS) || Objects.equals(purchaseOption, "SUBSCRIBE")) {

//                    if (customerPortalSettingsDTO.getProductSelectionOption().equals(ProductSelectionOption.ALL_PRODUCTS) && Objects.equals(purchaseOption, "SUBSCRIBE")) {
//                        customerPortalSettingsDTO.setProductSelectionOption(ProductSelectionOption.PRODUCTS_FROM_ALL_PLANS);
//                    }

                    List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);
                    List<String> productIDs = new ArrayList<>();
                    if (customerPortalSettingsDTO.getProductSelectionOption().equals(ProductSelectionOption.PRODUCTS_FROM_ALL_PLANS)) {
                        for (SubscriptionGroupPlanDTO subscriptionGroupPlanDTO : subscriptionGroupPlanDTOS) {
                            List<String> tempProductIDs = Arrays.asList(subscriptionGroupPlanDTO.getProductIds().split("\\s*,\\s*"));
                            List<String> tempTempProductIds = tempProductIDs.stream().filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.toList());
                            Arrays.stream(
                                    Optional.ofNullable(subscriptionGroupPlanDTO.getVariantProductIds())
                                        .orElse(StringUtils.EMPTY).split("\\s*,\\s*"))
                                .filter(StringUtils::isNotBlank)
                                .filter(s -> !tempTempProductIds.contains(s))
                                .forEach(tempTempProductIds::add);
                            productIDs.addAll(tempTempProductIds);
                        }
                    } else if (!CollectionUtils.isEmpty(sellingPlanIdsArray) && customerPortalSettingsDTO.getProductSelectionOption().equals(ProductSelectionOption.PRODUCTS_FROM_CURRENT_PLAN)) {
                        List<SubscriptionGroupV2DTO> subscriptionGroupV2DTOList = new ArrayList<>();
                        for (SubscriptionGroupPlanDTO subscriptionGroupPlanDTO : subscriptionGroupPlanDTOS) {
                            SubscriptionGroupV2DTO subscriptionGroupV2DTO = getSubscribeInfoFromJSON(subscriptionGroupPlanDTO.getInfoJson());

                            Set<String> existingSellingPlanIds = subscriptionGroupV2DTO.getSubscriptionPlans().stream().map(FrequencyInfoDTO::getId).collect(Collectors.toSet());

                            if (Sets.intersection(sellingPlanIdsArray, existingSellingPlanIds).size() > 0) {
                                List<String> tempProductIds = Arrays.asList(subscriptionGroupPlanDTO.getProductIds().split("\\s*,\\s*"));

                                Arrays.stream(
                                        Optional.ofNullable(subscriptionGroupPlanDTO.getVariantProductIds())
                                            .orElse(StringUtils.EMPTY).split("\\s*,\\s*"))
                                    .filter(StringUtils::isNotBlank)
                                    .filter(s -> !tempProductIds.contains(s))
                                    .forEach(tempProductIds::add);

                                productIDs.addAll(tempProductIds);
                            }
                            subscriptionGroupV2DTOList.add(subscriptionGroupV2DTO);
                        }
                    }
                    if (!productIDs.isEmpty()) {
                        productData.setProductHandleData(subscriptionDataService.getProductHandles(shop, productIDs));
                    }
                    productData = getFilterProductData(productData, productIDs);
                }

                if (!customerPortalSettingsDTO.isIncludeOutOfStockProduct()) {
                    productData.setProducts(productData.getProducts().stream().filter(f -> (f.getTotalInventory() > 0 && f.getTracksInventory()) || !f.getTracksInventory()).collect(Collectors.toList()));
                }

                productData.setProducts(productData.getProducts().stream().filter(p -> p.getStatus().equals(ProductStatus.ACTIVE)).collect(Collectors.toList()));

            }
        } catch (Exception e) {
            log.error("Something went wrong while getting products for shop : {}", shop);
        }
        return productData;
    }

    @NotNull
    private ProductData getFilterProductData(ProductData productData, List<String> productIDs) {
        List<com.et.api.graphql.data.Product> filteredProductList = new ArrayList<>();

        for (String productID : productIDs) {
            filteredProductList.addAll(productData.getProducts().stream().filter(product -> product.getId().toString().equals(productID)).collect(Collectors.toList()));
            productData.getProducts().removeAll(filteredProductList);
        }

        ProductData filteredProdctData = new ProductData();
        filteredProdctData.setPageInfo(productData.getPageInfo());
        filteredProdctData.setProducts(filteredProductList);
        filteredProdctData.setProductHandleData(productData.getProductHandleData());
        return filteredProdctData;
    }

    private SubscriptionGroupV2DTO getSubscribeInfoFromJSON(String subscribedInfoJson) {
        SubscriptionGroupV2DTO toReturn = CommonUtils.fromJSONIgnoreUnknownProperty(
            new TypeReference<>() {
            },
            subscribedInfoJson
        );
        return Optional.ofNullable(toReturn).orElse(new SubscriptionGroupV2DTO());
    }

    private List<ProductDetails> getProductDetailFromJSON(String subscribedInfoJson) {
        List<ProductDetails> toReturn = CommonUtils.fromJSONIgnoreUnknownProperty(
            new TypeReference<>() {
            },
            subscribedInfoJson
        );
        return Optional.ofNullable(toReturn).orElse(new ArrayList<>());
    }

    @GetMapping(value = {"/api/data/subscription-products", "/subscriptions/cp/api/data/subscription-products"})
    @CrossOrigin
    public List<SubscribedProductVariantInfo> getSubscriptionProductsData() throws Exception {

        String shop = commonUtils.getShop();

        List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);

        List<SubscribedProductVariantInfo> productList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionGroupPlanDTOS)) {
            productList = subscriptionDataService.getSubscriptionProductsData(subscriptionGroupPlanDTOS, null);
        }

        return productList;
    }

    @GetMapping(value = {"/api/data/selling-plan-products", "/subscriptions/cp/api/data/selling-plan-products"})
    @CrossOrigin
    public List<SubscribedProductVariantInfo> getSubscriptionProductsData(@RequestParam(value = "sellingPlanIds", required = false) String sellingPlanIds) {

        String shop = commonUtils.getShop();

        Set<String> sellingPlanIdsArray = Arrays
            .stream(StringUtils.split(Optional.ofNullable(sellingPlanIds).orElse(StringUtils.EMPTY), ","))
            .map(String::trim).map(s -> s.equals("") ? "null" : s).map(ShopifyGraphQLUtils::getGraphQLSellingPlanId)
            .collect(Collectors.toSet());

        List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOS = subscriptionGroupPlanService.findByShop(shop);

        List<SubscribedProductVariantInfo> productList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subscriptionGroupPlanDTOS)) {
            productList = subscriptionDataService.getSubscriptionProductsData(subscriptionGroupPlanDTOS, sellingPlanIdsArray);
        }

        return productList;
    }

    @GetMapping(value = {"/api/data/product-handles", "/subscriptions/cp/api/data/product-handles"})
    @CrossOrigin
    public Map<Long, String> getProductHandles(@RequestParam String productIds) throws Exception {

        String shop = commonUtils.getShop();

        if(StringUtils.isBlank(productIds)){
            throw new BadRequestAlertException("Product Ids cannot be null or empty", "", "");
        }

        List<String> productIdList = Arrays.stream(productIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());

        return subscriptionDataService.getProductHandles(shop, productIdList);
    }

    @GetMapping("/api/data/locations")
    public ResponseEntity<LocationSearchQuery.Data> getLocations()
        throws Exception {
        log.debug("REST request to get all products");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        LocationSearchQuery locationSearchQuery = new LocationSearchQuery(Input.optional(null));
        Response<Optional<LocationSearchQuery.Data>> optionalResponse = shopifyGraphqlClient.getOptionalQueryResponse(locationSearchQuery);

        Optional<LocationSearchQuery.Data> data = optionalResponse.getData();

        return ResponseUtil.wrapOrNotFound(data);
    }

    @GetMapping("/api/data/available-carriers")
    public ResponseEntity<AvailableCarrierServicesQuery.Data> availableCarriers()
        throws Exception {
        log.debug("REST request to get all products");
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        AvailableCarrierServicesQuery availableCarrierServicesQuery = new AvailableCarrierServicesQuery();
        Response<Optional<AvailableCarrierServicesQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(availableCarrierServicesQuery);

        Optional<AvailableCarrierServicesQuery.Data> data = optionalQueryResponse.getData();

        return ResponseUtil.wrapOrNotFound(data);
    }

    @GetMapping(value = {"/api/data/product", "/subscriptions/cp/api/data/product"})
    @CrossOrigin
    public Product getProductData(@RequestParam("productId") Long productId,
                                  @RequestParam(value = "shop", required = false) String shopName,
                                  HttpServletRequest request)
        throws Exception {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");


        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /product RequestURL: {}, shop: {}", RequestURL, shopName);
            throw new BadRequestAlertException("", "", "");
        }

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shopName);

        Product product = shopifyAPI.getProduct(productId).getProduct();

        return product;
    }

    @GetMapping("/api/data/external/v2/product")
    @CrossOrigin
    @ApiOperation("Get Product Data")
    public Product getProductDataV2(@ApiParam("Product ID") @RequestParam("productId") Long productId,
                                    @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                    HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get all products");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/product api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
        Product product = shopifyAPI.getProduct(productId).getProduct();
        return product;
    }

    @GetMapping(value = {"/api/data/products/{handle}.js", "/api/data/products/{handle}.json"})
    @CrossOrigin
    public Product getProductDataByHandle(@PathVariable("handle") String handle,
                                          @RequestParam(value = "shop", required = false) String shopName,
                                          HttpServletRequest request) {
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");


        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals("anonymousUser")) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        } else {
            log.info("REST request v1 /product RequestURL: {}, shop: {}", RequestURL, shopName);
            throw new BadRequestAlertException("", "", "");
        }


        HttpResponse<String> response = Unirest.get(String.format("https://%s/admin/api/2022-01/products.json?handle=%s", shopName, handle))
            .header("X-Shopify-Access-Token", commonUtils.getShopAccessToken())
            .asString();

        if (response.isSuccess()) {
            GetProductsResponse productsResponse = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
            }, response.getBody());
            if (!CollectionUtils.isEmpty(productsResponse.getProducts())) {
                return productsResponse.getProducts().get(0);
            }
        }

        return null;
    }

    @GetMapping(value = {"/api/data/products-selling-plans", "/subscriptions/cp/api/data/products-selling-plans"})
    @CrossOrigin
    @ApiOperation("Get Products Selling Plans")
    public List<ProductSellingPlanNodesQuery.Node> getProductSellingPlanData(@ApiParam("Product IDs") @RequestParam("productIds") Set<Long> productIds,
                                                                             HttpServletRequest request)
        throws Exception {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shopName);
        List<String> graphQlProductIds = productIds.stream().map(s -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + s).collect(Collectors.toList());
        ProductSellingPlanNodesQuery productSellingPlanNodesQuery = new ProductSellingPlanNodesQuery(graphQlProductIds);
        Response<Optional<ProductSellingPlanNodesQuery.Data>> productSellingPlanNodesQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productSellingPlanNodesQuery);
        List<ProductSellingPlanNodesQuery.Node> nodes = productSellingPlanNodesQueryResponse.getData().map(d -> d.getNodes()).orElse(new ArrayList<>());
        return nodes;
    }

    @GetMapping("/api/data/external/v2/products-selling-plans")
    @CrossOrigin
    @ApiOperation("Get Products Selling Plans")
    public List<ProductSellingPlanNodesQuery.Node> getProductsDataV2(@ApiParam("Product IDs") @RequestParam("productIds") Set<Long> productIds,
                                                                     @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                     HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get all products");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/product api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        List<String> graphQlProductIds = productIds.stream().map(s -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + s).collect(Collectors.toList());
        ProductSellingPlanNodesQuery productSellingPlanNodesQuery = new ProductSellingPlanNodesQuery(graphQlProductIds);
        Response<Optional<ProductSellingPlanNodesQuery.Data>> productSellingPlanNodesQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productSellingPlanNodesQuery);
        List<ProductSellingPlanNodesQuery.Node> nodes = productSellingPlanNodesQueryResponse.getData().map(d -> d.getNodes()).orElse(new ArrayList<>());
        return nodes;
    }

    @GetMapping(value = {"/api/data/variant-contextual-pricing", "/subscriptions/cp/api/data/variant-contextual-pricing"})
    @CrossOrigin
    public ResponseEntity<ProductVariantContextualPricingQuery.ProductVariant> getVariantContextualPricing(@RequestParam("variantId") Long variantId, @RequestParam("currencyCode") String currencyCode, HttpServletRequest request)
        throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return getVariantContextualPricingInternal(variantId, currencyCode, shop);
    }

    @GetMapping("/api/data/external/v2/variant-contextual-pricing")
    @CrossOrigin
    @ApiOperation("Get Variant Contextual Price")
    public ResponseEntity<ProductVariantContextualPricingQuery.ProductVariant> getVariantContextualPricingV2(@ApiParam("Variant ID") @RequestParam("variantId") Long variantId, @ApiParam("Currency Code") @RequestParam("currencyCode") String currencyCode, @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request)
        throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/product api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        return getVariantContextualPricingInternal(variantId, currencyCode, shop);
    }

    private ResponseEntity<ProductVariantContextualPricingQuery.ProductVariant> getVariantContextualPricingInternal(Long variantId, String currencyCode, String shop) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        if (!currencyCode.equals(shopInfoDTO.getCurrency())) {
            CountryCode countryCode = CurrencyUtils.getCountryCode(currencyCode);
            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(countryCode).build();
            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId, contextualPricingContext);
            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            return ResponseUtil.wrapOrNotFound(Objects.requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant));
        } else {

            Shop shop1 = shopifyAPI.getShopInfo().getShop();

            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(CountryCode.safeValueOf(shop1.getCountryCode())).build();
            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId, contextualPricingContext);
            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            return ResponseUtil.wrapOrNotFound(Objects.requireNonNull(productVariantResponse.getData()).flatMap(ProductVariantContextualPricingQuery.Data::getProductVariant));
        }

    }


    @GetMapping("/api/data/variant")
    @CrossOrigin
    public Variant getVariantData(@RequestParam("variantId") Long variantId,
                                  HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get all products");


        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

        Variant variant = shopifyAPI.getVariant(variantId).getVariant();

        return variant;
    }

    @GetMapping("/api/data/external/v2/variant")
    @CrossOrigin
    @ApiOperation("Get Variant Data")
    public Variant getVariantDataV2(@ApiParam("Variant ID") @RequestParam("variantId") Long variantId,
                                    @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                    HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get all products");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/variant api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
        Variant variant = shopifyAPI.getVariant(variantId).getVariant();
        return variant;
    }

    /**
     * {@code GET  /product-variants} : get product-variants data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the ProductData in body.
     */
    @GetMapping("/api/data/product-variants")
    @CrossOrigin
    public ProductData getProductVariantsData(@RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                              @RequestParam(value = "cursor", required = false) String cursor,
                                              @RequestParam(value = "variantNext", required = false, defaultValue = "false") boolean variantNext,
                                              @RequestParam(value = "variantCursor", required = false) String variantCursor,
                                              @RequestParam(value = "productType", required = false) String productType,
                                              @RequestParam(value = "vendor", required = false) String vendor,
                                              @RequestParam(value = "tags", required = false) String tags,
                                              @RequestParam(value = "collection", required = false) String collection,
                                              @RequestParam(value = "sellingPlanIds", required = false) String sellingPlanIds,
                                              HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get product variants");
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        return subscriptionDataService.findShopProductVariants(commonUtils.getShop(), search, next, cursor, variantNext, variantCursor, vendor, tags, productType, collection, sellingPlanIds);
    }

    @GetMapping("/api/data/get-variant-detail-by-ids")
    public List<Variant> getPresentmentPrices(@RequestParam("variantIds") String variantIds) {
        String shop = commonUtils.getShop();
        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
        Set<Long> variantIdsList = Arrays.stream(StringUtils.split(Optional.of(variantIds).orElse(StringUtils.EMPTY), ","))
            .map(String::trim).filter(StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());
        return variantIdsList.stream().map(aLong -> shopifyAPI.getVariant(aLong).getVariant()).collect(Collectors.toList());
    }


    @GetMapping("/api/data/v2/product-variants")
    @CrossOrigin
    public Optional<ProductVariantSearchQuery.Data> getProductVariantsDataV2(@RequestParam(value = "search", required = false) String search,
                                                                             @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                                                             @RequestParam(value = "cursor", required = false) String cursor,
                                                                             @RequestParam(value = "variantNext", required = false, defaultValue = "false") boolean variantNext,
                                                                             @RequestParam(value = "variantCursor", required = false) String variantCursor,
                                                                             HttpServletRequest request)
        throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ProductVariantSearchQuery productSearchQuery = new ProductVariantSearchQuery(
            StringUtils.isNoneEmpty(search) ? Input.optional("title:*" + search + "*") : Input.optional(null),
            next ? Input.optional(cursor) : Input.optional(null), variantNext ? Input.optional(variantCursor) : Input.optional(null));

        Response<Optional<ProductVariantSearchQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(productSearchQuery);
        Optional<ProductVariantSearchQuery.Data> data = response.getData();
        return data;
    }

    @GetMapping("/api/data/external/v2/product-variants")
    @CrossOrigin
    @ApiOperation("Get Product Variant Data")
    public Optional<ProductVariantSearchQuery.Data> getProductVariantsDataV2External(@ApiParam("Search") @RequestParam(value = "search", required = false) String search,
                                                                                     @ApiParam("Next") @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                                                                     @ApiParam("Cursor") @RequestParam(value = "cursor", required = false) String cursor,
                                                                                     @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                                     @ApiParam("Next Variant") @RequestParam(value = "variantNext", required = false, defaultValue = "false") boolean variantNext,
                                                                                     @ApiParam("Variant Cursor") @RequestParam(value = "variantCursor", required = false) String variantCursor,
                                                                                     HttpServletRequest request)
        throws Exception {
        log.debug("REST request to get product variants");

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/product-variants api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ProductVariantSearchQuery productSearchQuery = new ProductVariantSearchQuery(
            StringUtils.isNoneEmpty(search) ? Input.optional("title:*" + search + "*") : Input.optional(null),
            next ? Input.optional(cursor) : Input.optional(null), variantNext ? Input.optional(variantCursor) : Input.optional(null));

        Response<Optional<ProductVariantSearchQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(productSearchQuery);
        Optional<ProductVariantSearchQuery.Data> data = response.getData();
        return data;
    }

    /**
     * {@code GET  /api/data/product-collections} : get product-collections data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the  ProductData in body.
     */
    @GetMapping("/api/data/product-collections")
    public ProductData getProductCollectionsData(@RequestParam(value = "search", required = false) String search,
                                                 @RequestParam(value = "next", required = false, defaultValue = "false") boolean next,
                                                 @RequestParam(value = "cursor", required = false) String cursor)
        throws Exception {
        log.debug("REST request to get all product collection");
        return subscriptionDataService.findShopProductCollections(commonUtils.getShop(), search, next, cursor);
    }

    @GetMapping(value = {"/api/data/countries", "/subscriptions/cp/api/data/countries"})
    @CrossOrigin
    public List<Country> getCountriesData() throws Exception {
        log.debug("REST request to get all countries");

        String shop = commonUtils.getShop();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

        return shopifyAPI.getCountries().getCountries();
    }

    @GetMapping(value = {"/api/data/currencies", "/subscriptions/cp/api/data/currencies"})
    @CrossOrigin
    public List<Currency> getEnabledCurrenciesData() throws Exception {
        log.debug("REST request to get shop's enabled currencies ");

        String shop = commonUtils.getShop();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

        return shopifyAPI.getCurrencies().getCurrencies();
    }
}
