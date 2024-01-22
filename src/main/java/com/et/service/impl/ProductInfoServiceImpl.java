package com.et.service.impl;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.customcollection.GetCustomCollectionsPaginatedResponse;
import com.et.api.shopify.product.GetProductsResponse;
import com.et.api.shopify.product.Product;
import com.et.api.shopify.smartcollection.GetSmartCollectionsPaginatedResponse;
import com.et.domain.ProductInfo;
import com.et.domain.SubscriptionGroupPlan;
import com.et.pojo.ProductFilterDTO;
import com.et.repository.ProductInfoRepository;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.ProductInfoService;
import com.et.service.dto.CollectionVM;
import com.et.service.dto.ProductInfoDTO;
import com.et.service.mapper.ProductInfoMapper;
import com.et.utils.CommonUtils;
import com.google.common.collect.Lists;
import com.shopify.java.graphql.client.queries.CollectionsQuery;
import com.shopify.java.graphql.client.queries.ShopProductFiltersQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProductInfo}.
 */
@Service
@Transactional
@Lazy
public class ProductInfoServiceImpl implements ProductInfoService {

    private final Logger log = LoggerFactory.getLogger(ProductInfoServiceImpl.class);

    private final ProductInfoRepository productInfoRepository;

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    private final ProductInfoMapper productInfoMapper;

    @Autowired
    private CommonUtils commonUtils;

    public ProductInfoServiceImpl(ProductInfoRepository productInfoRepository, ProductInfoMapper productInfoMapper) {
        this.productInfoRepository = productInfoRepository;
        this.productInfoMapper = productInfoMapper;
    }

    @Override
    public ProductInfoDTO save(ProductInfoDTO productInfoDTO) {
        log.debug("Request to save ProductInfo : {}", productInfoDTO);
        ProductInfo productInfo = productInfoMapper.toEntity(productInfoDTO);
        productInfo = productInfoRepository.save(productInfo);
        return productInfoMapper.toDto(productInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> findAll() {
        log.debug("Request to get all ProductInfos");
        return productInfoRepository.findAll().stream()
            .map(productInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProductInfoDTO> findOne(Long id) {
        log.debug("Request to get ProductInfo : {}", id);
        return productInfoRepository.findById(id)
            .map(productInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductInfo : {}", id);
        productInfoRepository.deleteById(id);
    }


    @Async
    @Override
    public void createOrUpdateProductByIds(String shop, Set<Long> productIds) {
        List<Long> productIdList = new ArrayList<>(productIds);
        try {
            ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
            for (List<Long> productIdsPartition : Lists.partition(productIdList, 50)) {
                GetProductsResponse getProductsResponse = shopifyAPI.getProducts(new HashSet<Long>(productIdsPartition));
                for (Product product : getProductsResponse.getProducts()) {
                    productInfoRepository.deleteByShopAndProductId(shop, product.getId());
                    Optional<ProductInfo> productInfo = productInfoRepository.findOneByShopAndProductId(shop, product.getId());
                    ProductInfo productInfo1;
                    if (productInfo.isPresent()) {
                        productInfo1 = productInfo.get();
                    } else {
                        productInfo1 = new ProductInfo();
                        productInfo1.setShop(shop);
                        productInfo1.setProductId(product.getId());
                    }
                    productInfo1.setProductTitle(product.getTitle());
                    productInfo1.setProductHandle(product.getHandle());
                    productInfoRepository.save(productInfo1);
                }
            }
        }catch (Exception e){

        }

    }

    @Async
    @Override
    public void syncAllProductInfo() {
        List<SubscriptionGroupPlan> subscriptionGroupPlans = subscriptionGroupPlanRepository.findAll();
        for (SubscriptionGroupPlan subscriptionGroupPlanDTO:subscriptionGroupPlans){
            String[] productIdsString = StringUtils.split(subscriptionGroupPlanDTO.getProductIds(),",");
            Set<Long> productIdsSet = new HashSet<>();
            for(String productId:productIdsString){
                productIdsSet.add(Long.parseLong(productId));
            }
            createOrUpdateProductByIds(subscriptionGroupPlanDTO.getShop(),productIdsSet);
        }
    }

    @Override
    public List<ProductInfo> findByShopAndProductIds(String shop, Set<Long> productIds) {
        List<ProductInfo> productInfos = productInfoRepository.findByShopAndProductIdIn(shop, new ArrayList<>(productIds));
        return productInfos;
    }

    @Override
    public ProductFilterDTO getAllProductFilterData(String shop) throws Exception {
        ProductFilterDTO productFilterDTO =  new ProductFilterDTO();
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ShopProductFiltersQuery shopProductFiltersQuery = new ShopProductFiltersQuery();
        Response<Optional<ShopProductFiltersQuery.Data>> clientOptionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(shopProductFiltersQuery);
        if (clientOptionalQueryResponse.getData() != null && clientOptionalQueryResponse.getData().isPresent()) {
            productFilterDTO.setProductType(clientOptionalQueryResponse.getData().get().getShop().getProductTypes().getEdges().stream().map(ShopProductFiltersQuery.Edge1::getNode).collect(Collectors.toSet()));
            productFilterDTO.setTags(clientOptionalQueryResponse.getData().get().getShop().getProductTags().getEdges().stream().map(ShopProductFiltersQuery.Edge::getNode).collect(Collectors.toSet()));
            productFilterDTO.setVendor(clientOptionalQueryResponse.getData().get().getShop().getProductVendors().getEdges().stream().map(ShopProductFiltersQuery.Edge2::getNode).collect(Collectors.toSet()));
        }
        productFilterDTO.setCollection(Set.copyOf(getAllCollectionList(shop, null)));
        return productFilterDTO;
    }

    @Override
    public List<CollectionVM> getAllCollectionList(String shop, String searchText) {
        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
        List<CollectionVM> collectionVMS = new ArrayList<>();

        if (StringUtils.isNotEmpty(searchText)) {
            try {
                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                CollectionsQuery collectionsQuery = new CollectionsQuery(Input.optional("title:*" + searchText + "*"), Input.optional(null));
                Response<Optional<CollectionsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(collectionsQuery);

                if (CollectionUtils.isEmpty(response.getErrors()) && response.getData().isPresent()) {
                    collectionVMS = response.getData().get().getCollections().getNodes().stream().map(node -> {
                        CollectionVM collectionItem = new CollectionVM();
                        collectionItem.setHandle(node.getHandle());
                        collectionItem.setTitle(node.getTitle());
                        collectionItem.setId(Long.parseLong(node.getId().replace("gid://shopify/Collection/", "")));
                        return collectionItem;
                    }).collect(Collectors.toList());
                }
            } catch (Exception ignore) {}
        } else {
            GetCustomCollectionsPaginatedResponse customCollections = api.getCustomCollections(250);
            List<CollectionVM> finalCollectionVMS = collectionVMS;
            customCollections.getOriginalResponse().getCustomCollections().forEach(collectionVM -> getCollectionVM(finalCollectionVMS, collectionVM.getId(), collectionVM.getHandle(), collectionVM.getTitle()));
            collectionVMS = finalCollectionVMS;

            GetSmartCollectionsPaginatedResponse smartCollections = api.getSmartCollections(250);
            List<CollectionVM> finalCollectionVMS1 = collectionVMS;
            smartCollections.getOriginalResponse().getSmartCollections().forEach(collectionVM -> getCollectionVM(finalCollectionVMS1, collectionVM.getId(), collectionVM.getHandle(), collectionVM.getTitle()));
            collectionVMS = finalCollectionVMS1;
        }
        collectionVMS.sort(Comparator.comparing(CollectionVM::getTitle));
        return collectionVMS;
    }

    private void getCollectionVM(List<CollectionVM> collectionVMS, Long id, String handle, String title) {
        if (collectionVMS.stream().noneMatch(collectionVM1 -> collectionVM1.getId().equals(id))) {
            CollectionVM collectionItem = new CollectionVM();
            collectionItem.setHandle(handle);
            collectionItem.setTitle(title);
            collectionItem.setId(id);
            collectionVMS.add(collectionItem);
        }
    }
}
