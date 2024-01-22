package com.et.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.apollographql.apollo.api.Response;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.shopify.product.Product;
import com.et.domain.SellingPlanMemberInfo;
import com.et.domain.SubscriptionGroupPlan;
import com.et.repository.SellingPlanMemberInfoRepository;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.ProductInfoService;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.dto.FrequencyInfoDTO;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.service.dto.SubscriptionGroupV2DTO;
import com.et.service.mapper.SubscriptionGroupPlanMapper;
import com.et.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.java.graphql.client.queries.ProductVariantNodesQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.et.api.constants.ShopifyIdPrefix.PRODUCT_ID_PREFIX;
import static com.et.api.constants.ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX;

/**
 * Service Implementation for managing {@link SubscriptionGroupPlan}.
 */
@Service
@Transactional
@Lazy
public class SubscriptionGroupPlanServiceImpl implements SubscriptionGroupPlanService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionGroupPlanServiceImpl.class);

    private final SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    private final SubscriptionGroupPlanMapper subscriptionGroupPlanMapper;

    @Autowired
    private ProductInfoService productInfoService;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private SellingPlanMemberInfoRepository sellingPlanMemberInfoRepository;

    public SubscriptionGroupPlanServiceImpl(SubscriptionGroupPlanRepository subscriptionGroupPlanRepository, SubscriptionGroupPlanMapper subscriptionGroupPlanMapper) {
        this.subscriptionGroupPlanRepository = subscriptionGroupPlanRepository;
        this.subscriptionGroupPlanMapper = subscriptionGroupPlanMapper;
    }

    @Override
    public SubscriptionGroupPlanDTO save(SubscriptionGroupPlanDTO subscriptionGroupPlanDTO) {
        log.debug("Request to save SubscriptionGroupPlan : {}", subscriptionGroupPlanDTO);
        SubscriptionGroupPlan subscriptionGroupPlan = subscriptionGroupPlanMapper.toEntity(subscriptionGroupPlanDTO);
        subscriptionGroupPlan = subscriptionGroupPlanRepository.save(subscriptionGroupPlan);
        return subscriptionGroupPlanMapper.toDto(subscriptionGroupPlan);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionGroupPlanDTO> findAll() {
        log.debug("Request to get all SubscriptionGroupPlans");
        return subscriptionGroupPlanRepository.findAll().stream()
            .map(subscriptionGroupPlanMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionGroupPlanDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionGroupPlan : {}", id);
        return subscriptionGroupPlanRepository.findById(id)
            .map(subscriptionGroupPlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionGroupPlan : {}", id);
        subscriptionGroupPlanRepository.deleteById(id);
    }

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public SubscriptionGroupPlan createOrUpdateRecord(String shop, Long subscriptionId, SubscriptionGroupV2DTO subscriptionGroupDTO) {
        log.info("Saving Subscription Group. shop = {}, subscriptionId = {}", shop, subscriptionId);
        Long productCount = 0l;
        String productIdString = "";
        String variantIdString = "";
        String variantProductIdString = "";
        try {
            List<Product> products = OBJECT_MAPPER.readValue(subscriptionGroupDTO.getProductIds(), new TypeReference<List<Product>>() {
            });

            List<Product> variants = OBJECT_MAPPER.readValue(subscriptionGroupDTO.getVariantIds(), new TypeReference<List<Product>>() {
            });

            if (products == null) {
                products = new ArrayList<>();
            }

            if (variants == null) {
                variants = new ArrayList<>();
            }

            productCount = (long) products.size();
            Set<Long> productsSet = new HashSet<>();
            for (Product product : products) {
                productsSet.add(product.getId());
            }

            List<Long> variantsSet = new ArrayList<>();
            for (Product variant : variants) {
                variantsSet.add(variant.getId());
            }

            List<Long> variantProductIds = new ArrayList<>();

            if(!CollectionUtils.isNullOrEmpty(variantsSet)) {
                List<String> productVariantGQLIds = variantsSet.stream().map(variantId -> PRODUCT_VARIANT_ID_PREFIX + variantId.toString()).collect(Collectors.toList());

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                ProductVariantNodesQuery productVariantNodesQuery = new ProductVariantNodesQuery(productVariantGQLIds);

                Response<Optional<ProductVariantNodesQuery.Data>> optionalResponse =  shopifyGraphqlClient.getOptionalQueryResponse(productVariantNodesQuery);

                if(!optionalResponse.hasErrors() && optionalResponse.getData().isPresent()) {
                    variantProductIds = optionalResponse.getData().get().getNodes().stream().filter(ObjectUtils::isNotEmpty)
                        .map(node -> ((ProductVariantNodesQuery.AsProductVariant)node))
                        .map(ProductVariantNodesQuery.AsProductVariant::getProduct)
                        .map(ProductVariantNodesQuery.Product::getId)
                        .map(idStr -> idStr.replace(PRODUCT_ID_PREFIX, ""))
                        .map(Long::parseLong)
                        .distinct()
                        .collect(Collectors.toList());
                }
            }

            productsSet.addAll(variantProductIds);
            productInfoService.createOrUpdateProductByIds(shop, productsSet);
            subscriptionGroupDTO.setProductCount(productCount);
            productIdString = products.stream().map(product -> String.valueOf(product.getId())).collect(Collectors.joining(","));
            variantIdString = variants.stream().map(product -> String.valueOf(product.getId())).collect(Collectors.joining(","));
            variantProductIdString = variantProductIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while saving Subscription Group.", e);
        }

        String subscriptionGroupDTOString = null;
        try {
            subscriptionGroupDTOString = OBJECT_MAPPER.writeValueAsString(subscriptionGroupDTO);
        } catch (JsonProcessingException e) {
        }

        SubscriptionGroupPlan subscriptionGroupPlan = subscriptionGroupPlanRepository.findByShopAndSubscriptionId(shop, subscriptionGroupDTO.getId());
        if (subscriptionGroupPlan != null) {
            // update record
            removeUnusedFrequencyInfo(shop, subscriptionGroupDTO);
            subscriptionGroupPlan.setGroupName(subscriptionGroupDTO.getGroupName());
            subscriptionGroupPlan.setInfoJson(subscriptionGroupDTOString);
            subscriptionGroupPlan.setProductCount(Math.toIntExact(productCount));
            subscriptionGroupPlan.setProductVariantCount(Math.toIntExact(Optional.ofNullable(subscriptionGroupDTO.getProductVariantCount()).orElse(0L)));
            subscriptionGroupPlan.setProductIds(productIdString);
            subscriptionGroupPlan.setVariantIds(variantIdString);
            subscriptionGroupPlan.setVariantProductIds(variantProductIdString);
            return subscriptionGroupPlanRepository.saveAndFlush(subscriptionGroupPlan);
        } else {
            //Insert new record
            SubscriptionGroupPlan newEntry = new SubscriptionGroupPlan();
            newEntry.setSubscriptionId(subscriptionGroupDTO.getId());
            newEntry.setGroupName(subscriptionGroupDTO.getGroupName());
            newEntry.setShop(shop);
            newEntry.setInfoJson(subscriptionGroupDTOString);
            newEntry.setProductCount(Math.toIntExact(productCount));
            newEntry.setProductVariantCount(Math.toIntExact(Optional.ofNullable(subscriptionGroupDTO.getProductVariantCount()).orElse(0L)));
            newEntry.setProductIds(productIdString);
            newEntry.setVariantIds(variantIdString);
            newEntry.setVariantProductIds(variantProductIdString);
            return subscriptionGroupPlanRepository.saveAndFlush(newEntry);
        }
    }

    private void removeUnusedFrequencyInfo(String shop, SubscriptionGroupV2DTO subscriptionGroupDTO) {
        List<SellingPlanMemberInfo> sellingPlanMemberInfos = sellingPlanMemberInfoRepository.findAllByShopAndSubscriptionId(shop, subscriptionGroupDTO.getId());
        List<Long> newIdList = new ArrayList<>();
        for (FrequencyInfoDTO frequencyInfoDTO : subscriptionGroupDTO.getSubscriptionPlans()) {
            newIdList.add(Long.valueOf(frequencyInfoDTO.getId().replace("gid://shopify/SellingPlan/", "")));
        }
        if (sellingPlanMemberInfos.size() > 0) {
            for (SellingPlanMemberInfo sellingPlanMemberInfo : sellingPlanMemberInfos) {
                if (!newIdList.contains(sellingPlanMemberInfo.getSellingPlanId())) {
                    sellingPlanMemberInfoRepository.delete(sellingPlanMemberInfo);
                }
            }
        }
    }

    @Override
    public Optional<SubscriptionGroupV2DTO> getSingleSubscriptionGroupPlan(String shop, Long subscriptionGroupId) {
        SubscriptionGroupPlan subscriptionGroupPlan = subscriptionGroupPlanRepository.findByShopAndSubscriptionId(shop, subscriptionGroupId);
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTO = null;

        if (subscriptionGroupPlan == null) {
            return Optional.empty();
        }
        subscriptionGroupDTO = Optional.of(CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, subscriptionGroupPlan.getInfoJson()));
        return subscriptionGroupDTO;
    }

    @Override
    public List<String> getAllOtherProductIdsBySubscription(String shop, Long subscriptionId) {
        List<SubscriptionGroupPlan> subscriptionGroupPlans = subscriptionGroupPlanRepository.getGetProductIdsExceptSubscription(shop, subscriptionId);
        return getProductIdsForSubscriptions(subscriptionGroupPlans);
    }

    private List<String> getProductIdsForSubscriptions(List<SubscriptionGroupPlan> subscriptionGroupPlans) {
        List<String> productIds = new ArrayList<>();
        for (SubscriptionGroupPlan subscriptionGroupPlan : subscriptionGroupPlans) {
            List<String> ids = Arrays.asList(subscriptionGroupPlan.getProductIds().split(","));
            productIds = Stream.concat(productIds.stream(), ids.stream()).collect(Collectors.toList());
        }
        System.out.println("=ids====>" + productIds.toString());
        return productIds;

    }

    @Override
    public List<SubscriptionGroupV2DTO> getAllSubscriptionGroupPlan(String shop) {
        List<SubscriptionGroupPlan> subscriptionGroupPlans = subscriptionGroupPlanRepository.findAllByShop(shop);
        List<SubscriptionGroupV2DTO> subscriptionGroupV2DTOS = new ArrayList<>();
        System.out.println("Get All Called");
        for (SubscriptionGroupPlan subscriptionGroupPlan : subscriptionGroupPlans) {
            SubscriptionGroupV2DTO subscriptionGroupV2DTO = null;
            subscriptionGroupV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, subscriptionGroupPlan.getInfoJson());
            subscriptionGroupV2DTOS.add(subscriptionGroupV2DTO);
        }
        return subscriptionGroupV2DTOS;
    }

    @Override
    public List<SubscriptionGroupPlanDTO> findByShop(String shop) {
        return subscriptionGroupPlanRepository.findByShop(shop).stream()
            .map(subscriptionGroupPlanMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<SubscriptionGroupPlanDTO> findBySubscriptionId(Long subscriptionId) {
        return subscriptionGroupPlanRepository.findBySubscriptionId(subscriptionId)
            .map(subscriptionGroupPlanMapper::toDto);
    }
}
