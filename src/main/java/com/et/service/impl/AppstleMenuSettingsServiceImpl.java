package com.et.service.impl;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.product.GetProductsPaginatedResponse;
import com.et.api.shopify.product.Product;
import com.et.domain.AppstleMenuSettings;
import com.et.domain.enumeration.ProductViewSettings;
import com.et.repository.AppstleMenuSettingsRepository;
import com.et.service.AppstleMenuSettingsService;
import com.et.service.SubscriptionGroupService;
import com.et.service.dto.AppstleMenuSettingsDTO;
import com.et.service.dto.AppstleMenuTypeDTO;
import com.et.service.dto.ProductDTO;
import com.et.service.dto.SubscriptionGroupV2DTO;
import com.et.service.mapper.AppstleMenuSettingsMapper;
import com.et.utils.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.ProductSearchQuery;
import com.shopify.java.graphql.client.queries.ProductUpdateMutation;
import com.shopify.java.graphql.client.queries.TagsAddMutation;
import com.shopify.java.graphql.client.queries.TagsRemoveMutation;
import com.shopify.java.graphql.client.type.ProductInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link AppstleMenuSettings}.
 */
@Service
@Transactional
public class AppstleMenuSettingsServiceImpl implements AppstleMenuSettingsService {

    private final Logger log = LoggerFactory.getLogger(AppstleMenuSettingsServiceImpl.class);

    private final AppstleMenuSettingsRepository appstleMenuSettingsRepository;

    private final AppstleMenuSettingsMapper appstleMenuSettingsMapper;

    private final CommonUtils commonUtils;

    private final SubscriptionGroupService subscriptionGroupService;

    public AppstleMenuSettingsServiceImpl(AppstleMenuSettingsRepository appstleMenuSettingsRepository, AppstleMenuSettingsMapper appstleMenuSettingsMapper, CommonUtils commonUtils, SubscriptionGroupService subscriptionGroupService) {
        this.appstleMenuSettingsRepository = appstleMenuSettingsRepository;
        this.appstleMenuSettingsMapper = appstleMenuSettingsMapper;
        this.commonUtils = commonUtils;
        this.subscriptionGroupService = subscriptionGroupService;
    }

    @Override
    public AppstleMenuSettingsDTO save(AppstleMenuSettingsDTO appstleMenuSettingsDTO) {
        log.debug("Request to save AppstleMenuSettings : {}", appstleMenuSettingsDTO);
        AppstleMenuSettings appstleMenuSettings = appstleMenuSettingsMapper.toEntity(appstleMenuSettingsDTO);
        appstleMenuSettings = appstleMenuSettingsRepository.save(appstleMenuSettings);
        return appstleMenuSettingsMapper.toDto(appstleMenuSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppstleMenuSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppstleMenuSettings");
        return appstleMenuSettingsRepository.findAll(pageable).map(appstleMenuSettingsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AppstleMenuSettingsDTO> findOne(Long id) {
        log.debug("Request to get AppstleMenuSettings : {}", id);
        return appstleMenuSettingsRepository.findById(id).map(appstleMenuSettingsMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<AppstleMenuSettingsDTO> findByShop(String shop) {
        log.debug("Request to get AppstleMenuSettings for shop : {}", shop);
        Optional<AppstleMenuSettings> byShop = appstleMenuSettingsRepository.findByShop(shop);
        if (!byShop.isPresent()) {
            AppstleMenuSettings appstleMenuSettings = new AppstleMenuSettings();
            appstleMenuSettings.setShop(shop);
            appstleMenuSettings.setProductViewStyle(ProductViewSettings.QUICK_ADD);
            AppstleMenuSettings result = appstleMenuSettingsRepository.save(appstleMenuSettings);
            return Optional.of(result).map(appstleMenuSettingsMapper::toDto);
        }
        return byShop.map(appstleMenuSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppstleMenuSettings : {}", id);
        appstleMenuSettingsRepository.deleteById(id);
    }

    @Async
    public void applyTagsToProductsProcess(String shop, AppstleMenuSettingsDTO appstleMenuSettingsDTO) {

        ObjectMapper mapper = new ObjectMapper();
        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        try {
            List<AppstleMenuTypeDTO> appstleMenuTypeDTOS = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>(){}, appstleMenuSettingsDTO.getFilterMenu());
            appstleMenuTypeDTOS.forEach(appstleMenuTypeDTO -> {
                if (appstleMenuTypeDTO.getMenuType() != null && appstleMenuTypeDTO.getMenuType().equals("ONE_TIME") && appstleMenuTypeDTO.getSourceCollection() != null) {
                    String applyingTagFor = "APPSTLE_ONE_TIME" + appstleMenuTypeDTO.getSourceCollection();

                    try {
                        //Removing tags from all existing onetime products
                        getAndDeleteAllTag(shopifyGraphqlClient, applyingTagFor);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    GetProductsPaginatedResponse productsForCollection = api.getProductsForCollection(appstleMenuTypeDTO.getSourceCollection(), 250);

                    List<Product> productList = productsForCollection.getOriginalResponse().getProducts();
                    List<String> products = new ArrayList<>();
                    if (productList.size() > 0) {
                        productList.forEach(product -> products.add(product.getAdminGraphqlApiId()));
                        applyTags(shopifyGraphqlClient, products, List.of(applyingTagFor));
                    }
                } else if (appstleMenuTypeDTO.getMenuType() != null && appstleMenuTypeDTO.getMenuType().equals("SUBSCRIBE") && appstleMenuTypeDTO.getSubscriptionGroup() != null) {
                    try {
                        Optional<SubscriptionGroupV2DTO> subscriptionGroupV2 = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, appstleMenuTypeDTO.getSubscriptionGroup());
                        if (subscriptionGroupV2.isPresent() && subscriptionGroupV2.get().getProductIds() != null) {
                            String applyingTagFor = "APPSTLE_SUBSCRIBE" + appstleMenuTypeDTO.getSubscriptionGroup();
                            try {
                                //Removing tags from all existing subscription getProductsResponse
                                getAndDeleteAllTag(shopifyGraphqlClient, applyingTagFor);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            List<ProductDTO> productDTOS = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, subscriptionGroupV2.get().getProductIds());

                            Set<Long> productsIds = productDTOS.stream().map(ProductDTO::getId).collect(Collectors.toSet());

                            List<String> products = new ArrayList<>();
                            if (productsIds.size() > 0) {
                                productsIds.forEach(productId -> products.add(ShopifyIdPrefix.PRODUCT_ID_PREFIX + productId));
                                applyTags(shopifyGraphqlClient, products, List.of(applyingTagFor));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAndDeleteAllTag(ShopifyGraphqlClient shopifyGraphqlClient, String tag) throws Exception {
        String query = null;
        String cursor = null;
        boolean hasNextPage = true;

        List<String> products = new ArrayList<>();
        if (tag != null) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("tag:").append(tag);
            while (hasNextPage) {
                ProductSearchQuery productSearchQuery = new ProductSearchQuery(Input.optional(query), hasNextPage && cursor != null ? Input.optional(cursor) : Input.optional(null));
                Response<Optional<ProductSearchQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(productSearchQuery);
                if (response.getData().isPresent()) {
                    List<String> productIds = response.getData().get().getProducts().getEdges().stream().map(edge -> edge.getNode().getId()).collect(Collectors.toList());
                    products.addAll(productIds);
                    hasNextPage = response.getData().get().getProducts().getPageInfo().isHasNextPage();
                }
                if (hasNextPage) {
                    Optional<ProductSearchQuery.Edge> lastEdge = response.getData().map(ProductSearchQuery.Data::getProducts).map(ProductSearchQuery.Products::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);
                    if (lastEdge.isPresent()) {
                        cursor = lastEdge.get().getCursor();
                    }
                }
            }
            deleteTags(shopifyGraphqlClient, products, List.of(tag));
        }
    }

    public void deleteTags(ShopifyGraphqlClient shopifyGraphqlClient, List<String> productIds, List<String> tags) {
        productIds.forEach(productId -> {
            TagsRemoveMutation tagsRemoveMutation = TagsRemoveMutation.builder().id(productId).tags(tags).build();
            try {
                Response<Optional<TagsRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(tagsRemoveMutation);
                if (optionalMutationResponse.hasErrors()) {
                    log.error("Error while adding line item." + optionalMutationResponse.getErrors().get(0).getMessage());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void applyTags(ShopifyGraphqlClient shopifyGraphqlClient, List<String> productIds, List<String> tags) {
        productIds.forEach(productId -> {
            TagsAddMutation tagsAddMutation = TagsAddMutation.builder().id(productId).tags(tags).build();
            try {
                Response<Optional<TagsAddMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(tagsAddMutation);
                if (optionalMutationResponse.hasErrors()) {
                    log.error("Error while adding line item." + optionalMutationResponse.getErrors().get(0).getMessage());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateProductTags(String shop, List<Product> products, String checkTag) {
        products.forEach(product -> {
            List<String> tags = Stream.of(product.getTags().split(",")).map(String::trim).collect(Collectors.toList());
            if (tags.stream().noneMatch(tag -> tag.equals(checkTag))) {
                tags.add(checkTag);
                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                ProductInput productInput = ProductInput.builder().id(ShopifyIdPrefix.PRODUCT_ID_PREFIX + product.getId()).tags(tags).build();
                ProductUpdateMutation productUpdateMutation = new ProductUpdateMutation(productInput);
                try {
                    shopifyGraphqlClient.getOptionalMutationResponse(productUpdateMutation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



