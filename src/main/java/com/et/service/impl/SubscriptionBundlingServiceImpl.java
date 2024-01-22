package com.et.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.et.domain.ProductInfo;
import com.et.domain.ShopInfo;
import com.et.domain.SubscriptionBundling;
import com.et.domain.SubscriptionGroupPlan;
import com.et.domain.enumeration.BuildABoxRedirect;
import com.et.domain.enumeration.BuildABoxType;
import com.et.domain.enumeration.BuildBoxVersion;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.et.repository.ProductInfoRepository;
import com.et.repository.ShopInfoRepository;
import com.et.repository.SubscriptionBundlingRepository;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.ShopInfoService;
import com.et.service.SubscriptionBundlingService;
import com.et.service.dto.*;
import com.et.service.mapper.SubscriptionBundlingMapper;
import com.et.utils.CommonUtils;
import com.et.utils.ShopInfoUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.et.utils.CommonUtils.convertCommaSeparatedStringToList;

/**
 * Service Implementation for managing {@link SubscriptionBundling}.
 */
@Service
@Transactional
public class SubscriptionBundlingServiceImpl implements SubscriptionBundlingService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundlingServiceImpl.class);

    private final SubscriptionBundlingRepository subscriptionBundlingRepository;

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    private final SubscriptionBundlingMapper subscriptionBundlingMapper;


    private final ShopInfoService shopInfoService;

    public SubscriptionBundlingServiceImpl(SubscriptionBundlingRepository subscriptionBundlingRepository, SubscriptionBundlingMapper subscriptionBundlingMapper, ShopInfoService shopInfoService) {
        this.subscriptionBundlingRepository = subscriptionBundlingRepository;
        this.subscriptionBundlingMapper = subscriptionBundlingMapper;
        this.shopInfoService = shopInfoService;
    }

    @Override
    public SubscriptionBundlingDTO save(SubscriptionBundlingDTO subscriptionBundlingDTO) {
        log.debug("Request to save SubscriptionBundling : {}", subscriptionBundlingDTO);
        if (subscriptionBundlingDTO.getUniqueRef() == null ||
            subscriptionBundlingDTO.getUniqueRef().isEmpty() ||
            subscriptionBundlingDTO.getUniqueRef().isBlank()) {
            String random = RandomStringUtils.randomAlphanumeric(8);
            subscriptionBundlingDTO.setUniqueRef(random);
        }
        SubscriptionBundling subscriptionBundling = subscriptionBundlingMapper.toEntity(subscriptionBundlingDTO);
        subscriptionBundling = subscriptionBundlingRepository.save(subscriptionBundling);
        return subscriptionBundlingMapper.toDto(subscriptionBundling);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionBundlingDTO> findAll() {
        log.debug("Request to get all SubscriptionBundlings");
        return subscriptionBundlingRepository.findAll().stream()
            .map(subscriptionBundlingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionBundlingDTO> findAllByShop(String shop) {
        List<SubscriptionBundlingDTO> subscriptionBundlingDTOList = subscriptionBundlingRepository.findByShop(shop).stream()
            .map(subscriptionBundlingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

        if (!CollectionUtils.isNullOrEmpty(subscriptionBundlingDTOList)) {
            List<SubscriptionGroupPlan> groupPlanDTOS = subscriptionGroupPlanRepository.findByShop(shop);

            ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
            subscriptionBundlingDTOList.forEach(subscriptionBundlingDTO -> {
                setSubscriptionBundleLink(subscriptionBundlingDTO, shopInfo);
                Optional<SubscriptionGroupPlan> groupPlan = groupPlanDTOS.stream().filter(subscriptionGroupPlan -> subscriptionGroupPlan.getSubscriptionId().equals(subscriptionBundlingDTO.getSubscriptionId())).findFirst();
                groupPlan.ifPresent(subscriptionGroupPlan -> subscriptionBundlingDTO.setGroupName(subscriptionGroupPlan.getGroupName()));
            });
        }

        return subscriptionBundlingDTOList;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBundlingDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionBundling : {}", id);
        Optional<SubscriptionBundlingDTO> subscriptionBundling = subscriptionBundlingRepository.findById(id)
            .map(subscriptionBundlingMapper::toDto);

        if (subscriptionBundling.isPresent()) {
            setSubscriptionBundleLink(subscriptionBundling.get());
        }

        return subscriptionBundling;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBundlingDTO> findOneByToken(String shop, String token) {
        Optional<SubscriptionBundlingDTO> subscriptionBundling = subscriptionBundlingRepository.findByShopAndUniqueRef(shop, token).map(subscriptionBundlingMapper::toDto);
        if (subscriptionBundling.isPresent()) {
            setSubscriptionBundleLink(subscriptionBundling.get());
        }
        return subscriptionBundling;
    }


    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionBundlingDTO> findByShopAndBuildABoxType(String shop, BuildABoxType buildABoxType) {
        return subscriptionBundlingRepository.findByShopAndBuildABoxType(shop, buildABoxType).stream().map(subscriptionBundlingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SubscriptionBundlingDTO> findByShopAndSubscriptionId(String shop, Long subscriptionId) {
        List<SubscriptionBundling> subscriptionBundlingList = subscriptionBundlingRepository.findByShopAndSubscriptionId(shop, subscriptionId);

        if (!CollectionUtils.isNullOrEmpty(subscriptionBundlingList)) {
            List<SubscriptionBundlingDTO> subscriptionBundlingDTOList = subscriptionBundlingList.stream().map(subscriptionBundlingMapper::toDto).collect(Collectors.toList());
            ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
            subscriptionBundlingDTOList.forEach(subscriptionBundlingDTO -> {
                setSubscriptionBundleLink(subscriptionBundlingDTO, shopInfo);
            });

            return subscriptionBundlingDTOList;
        } else {
            SubscriptionBundling newSubscriptionBundling = new SubscriptionBundling();
            newSubscriptionBundling.setShop(shop);
            newSubscriptionBundling.setSubscriptionId(subscriptionId);
            newSubscriptionBundling.setSubscriptionBundlingEnabled(false);
            newSubscriptionBundling.setBundleRedirect(BuildABoxRedirect.CART);
            String random = RandomStringUtils.randomAlphanumeric(8);
            newSubscriptionBundling.setUniqueRef(random);
            SubscriptionBundling result = subscriptionBundlingRepository.save(newSubscriptionBundling);
            SubscriptionBundlingDTO subscriptionBundlingDTO = subscriptionBundlingMapper.toDto(result);
            return List.of(subscriptionBundlingDTO);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionBundling : {}", id);
        subscriptionBundlingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changeBuildABoxStatus(Long buildABoxId, Boolean status) {
        Optional<SubscriptionBundling> subscriptionBundlingOptional = subscriptionBundlingRepository.findById(buildABoxId);
        if (subscriptionBundlingOptional.isPresent()) {
            SubscriptionBundling subscriptionBundling = subscriptionBundlingOptional.get();
            subscriptionBundling.setSubscriptionBundlingEnabled(status);
            subscriptionBundlingRepository.save(subscriptionBundling);
        }
    }

    @Override
    public SubscriptionBundlingResponse getBundleDetails(String shop, String handle) {
        SubscriptionBundlingResponse subscriptionBundlingResponse = new SubscriptionBundlingResponse();
        Optional<SubscriptionBundling> subscriptionBundling = subscriptionBundlingRepository.findByShopAndUniqueRef(shop, handle);
        if (subscriptionBundling.isPresent()) {
            if (subscriptionBundling.get().isSubscriptionBundlingEnabled()) {
                subscriptionBundlingResponse.setBundle(subscriptionBundling.get());
                List<SubscriptionGroupPlan> subscriptionGroupPlans = null;

                if(StringUtils.isNotBlank(subscriptionBundling.get().getSubscriptionGroup())) {
                    List<Long> multipleSubscriptionIds = new ArrayList<>();
                    JSONArray subscriptionGroupList = new JSONArray(subscriptionBundling.get().getSubscriptionGroup());
                    for (int i = 0; i < subscriptionGroupList.length(); i++) {
                        JSONObject subscription = subscriptionGroupList.getJSONObject(i);
                        if (subscription.has("value")) {
                            multipleSubscriptionIds.add(subscription.getLong("value"));
                        }
                    }
                    subscriptionGroupPlans  = subscriptionGroupPlanRepository.findByShopAndSubscriptionIdIn(shop, multipleSubscriptionIds);

                }

                if(CollectionUtils.isNullOrEmpty(subscriptionGroupPlans)) {
                    SubscriptionGroupPlan subscriptionGroupPlan = subscriptionGroupPlanRepository.findByShopAndSubscriptionId(shop, subscriptionBundling.get().getSubscriptionId());
                    subscriptionGroupPlans = List.of(subscriptionGroupPlan);
                }

                subscriptionBundlingResponse.setSubscriptions(subscriptionGroupPlans);

                List<Long> productIdsLong = new ArrayList<>();
                List<String> variantIdsString = new ArrayList<>();

                for(SubscriptionGroupPlan sgp: subscriptionGroupPlans) {
                    productIdsLong.addAll(getProductIds(sgp.getProductIds()));
                    if (StringUtils.isNotBlank(sgp.getVariantProductIds())) {
                        productIdsLong.addAll(getProductIds(sgp.getVariantProductIds()));
                    }
                    if (StringUtils.isNotBlank(sgp.getVariantIds())) {
                        variantIdsString.addAll(convertCommaSeparatedStringToList(sgp.getVariantIds()));
                    }
                }

                productIdsLong = productIdsLong.stream().distinct().collect(Collectors.toList());
                variantIdsString = variantIdsString.stream().distinct().collect(Collectors.toList());

                subscriptionBundlingResponse.setVariants(variantIdsString);

                List<ProductInfo> productInfos = productInfoRepository.findByShopAndProductIdIn(shop, productIdsLong);
                List<Long> finalProductIdsLong = productIdsLong;
                productInfos.sort(Comparator.comparing(product -> finalProductIdsLong.indexOf(product.getProductId())));
                subscriptionBundlingResponse.setProducts(productInfos);

            }
        }
        return subscriptionBundlingResponse;
    }

    @Override
    public SubscriptionBundlingResponseV3 getBundleDetailsV3(String shop, String handle) {
        SubscriptionBundlingResponseV3 subscriptionBundlingResponseV3 = new SubscriptionBundlingResponseV3();
        Optional<SubscriptionBundling> subscriptionBundling = subscriptionBundlingRepository.findByShopAndUniqueRef(shop, handle);
        if (subscriptionBundling.isPresent()) {
            if (subscriptionBundling.get().isSubscriptionBundlingEnabled()) {
                subscriptionBundlingResponseV3.setBundle(subscriptionBundling.get());
                List<Long> productIdsLong = new LinkedList<>();
                SubscriptionGroupPlan subscriptionGroupPlan = null;
                if(subscriptionBundling.get().getSubscriptionGroup() != null) {
                    List<Long> multipleSubscriptionIds = new LinkedList<>();
                    JSONArray subscriptionGroupList = new JSONArray(subscriptionBundling.get().getSubscriptionGroup());
                    for (int i = 0; i < subscriptionGroupList.length(); i++) {
                        JSONObject subscription = subscriptionGroupList.getJSONObject(i);
                        if (subscription.has("value")) {
                            multipleSubscriptionIds.add(subscription.getLong("value"));
                        }
                    }
                    List<SubscriptionGroupPlan> subscriptionGroupPlans  = subscriptionGroupPlanRepository.findByShopAndSubscriptionIdIn(shop, multipleSubscriptionIds);
                    if(!subscriptionGroupPlans.isEmpty()) {
                        boolean isMatch = checkAllSubscriptionPlansFrequencyMatch(subscriptionGroupPlans);
                        if(!isMatch) {
                            subscriptionGroupPlan = subscriptionGroupPlans.get(0);
                            return showOnlyMainSubscriptionPlanProducts(subscriptionGroupPlan, shop, subscriptionBundlingResponseV3);
                        } else {
                            Map<Long, List<ProductInfo>> subscriptionGroupPlanProductMap = new HashMap<>();
                            Map<Long, List<String>> subscriptionGroupPlanVariantMap = new HashMap<>();
                            subscriptionGroupPlan = subscriptionGroupPlans.get(0);
                            subscriptionBundlingResponseV3.setSubscription(subscriptionGroupPlan);
                            subscriptionGroupPlans.sort(Comparator.comparingLong(plan -> multipleSubscriptionIds.indexOf(plan.getSubscriptionId())));
                            for (SubscriptionGroupPlan plan: subscriptionGroupPlans) {
                                List<Long> productIds = getProductIds(plan.getProductIds());
                                productIdsLong.addAll(productIds);
                                if (StringUtils.isNotBlank(plan.getVariantProductIds())) {
                                    productIdsLong.addAll(getProductIds(plan.getVariantProductIds()));
                                }
                                List<ProductInfo> productInfos = productInfoRepository.findByShopAndProductIdIn(shop, productIds);
                                subscriptionGroupPlanProductMap.put(plan.getId(), productInfos);
                                if (StringUtils.isNotBlank(plan.getVariantIds())) {
                                    List<String> variants = convertCommaSeparatedStringToList(plan.getVariantIds());
                                    subscriptionGroupPlanVariantMap.put(plan.getId(), variants);
                                }
                            }
                            subscriptionBundlingResponseV3.setSubscriptionGroupPlans(subscriptionGroupPlans);
                            subscriptionBundlingResponseV3.setSubscriptionPlanProductInfoMap(subscriptionGroupPlanProductMap);
                            subscriptionBundlingResponseV3.setSubscriptionPlanVariantMap(subscriptionGroupPlanVariantMap);
                        }
                        List<ProductInfo> productInfos = productInfoRepository.findByShopAndProductIdIn(shop, productIdsLong);
                        List<Long> finalProductIdsLong = productIdsLong;
                        productInfos.sort(Comparator.comparing(product -> finalProductIdsLong.indexOf(product.getProductId())));
                        subscriptionBundlingResponseV3.setProducts(productInfos);
                    }
                } else {
                    subscriptionGroupPlan = subscriptionGroupPlanRepository.findByShopAndSubscriptionId(shop, subscriptionBundling.get().getSubscriptionId());
                    return showOnlyMainSubscriptionPlanProducts(subscriptionGroupPlan, shop, subscriptionBundlingResponseV3);
                }
            }

        }
        return subscriptionBundlingResponseV3;
    }

    private SubscriptionBundlingResponseV3 showOnlyMainSubscriptionPlanProducts(SubscriptionGroupPlan subscriptionGroupPlan, String shop, SubscriptionBundlingResponseV3 subscriptionBundlingResponseV3) {
        List<Long> productIdsLong = new ArrayList<>();
        subscriptionBundlingResponseV3.setSubscription(subscriptionGroupPlan);
        productIdsLong = getProductIds(subscriptionGroupPlan.getProductIds());
        if (StringUtils.isNotBlank(subscriptionGroupPlan.getVariantProductIds())) {
            productIdsLong.addAll(getProductIds(subscriptionGroupPlan.getVariantProductIds()));
        }
        List<ProductInfo> productInfos = productInfoRepository.findByShopAndProductIdIn(shop, productIdsLong);
        List<Long> finalProductIdsLong = productIdsLong;
        productInfos.sort(Comparator.comparing(product -> finalProductIdsLong.indexOf(product.getProductId())));
        subscriptionBundlingResponseV3.setProducts(productInfos);
        if (StringUtils.isNotBlank(subscriptionGroupPlan.getVariantIds())) {
            subscriptionBundlingResponseV3.setVariants(convertCommaSeparatedStringToList(subscriptionGroupPlan.getVariantIds()));
        }
        subscriptionBundlingResponseV3.setSubscriptionPlanProductInfoMap(null);
        subscriptionBundlingResponseV3.setSubscriptionPlanVariantMap(null);
        return subscriptionBundlingResponseV3;
    }

    // check if frequency of all subscription plans are same or not
    private boolean checkAllSubscriptionPlansFrequencyMatch(List<SubscriptionGroupPlan> subscriptionGroupPlans) {
        if (subscriptionGroupPlans.size() == 0) {
            return true;
        }
        SubscriptionGroupPlan firstSubscriptionGroupPlan = subscriptionGroupPlans.get(0);

        return subscriptionGroupPlans.stream()
            .skip(1)
            .map(el -> checkIfSubscriptionPlansAreEqual(firstSubscriptionGroupPlan, el))
            .reduce(true, (acc, el) -> acc && el );

    }

    private boolean checkIfSubscriptionPlansAreEqual(SubscriptionGroupPlan firstPlan, SubscriptionGroupPlan secondPlan) {
        List<FrequencyInfoDTO> firstSubscriptionPlanFrequencies = getSubscribeInfoFromJSON(firstPlan.getInfoJson()).getSubscriptionPlans();
        List<FrequencyInfoDTO> secondSubscriptionPlanFrequencies = getSubscribeInfoFromJSON(secondPlan.getInfoJson()).getSubscriptionPlans();
        if (firstSubscriptionPlanFrequencies.size() != secondSubscriptionPlanFrequencies.size()) {
            return false;
        }
        return firstSubscriptionPlanFrequencies.stream()
            .map(first -> secondSubscriptionPlanFrequencies.stream().anyMatch(second -> second.equals(first))).reduce(true, (acc, fl) -> acc && fl);
    }

    private SubscriptionGroupV2DTO getSubscribeInfoFromJSON(String subscribedInfoJson) {
        SubscriptionGroupV2DTO toReturn = CommonUtils.fromJSONIgnoreUnknownProperty(
            new TypeReference<>() {
            },
            subscribedInfoJson
        );
        return Optional.ofNullable(toReturn).orElse(new SubscriptionGroupV2DTO());
    }
    private List<Long> getProductIds(String productIdsString) {
        List<String> productIds = convertCommaSeparatedStringToList(productIdsString);
        List<Long> productIdsLong = new ArrayList<>();
        for (String productId : productIds) {
            productIdsLong.add(Long.parseLong(productId));
        }
        return productIdsLong;
    }

    @Override
    public Double prepareDiscountAmount(String shop, SubscriptionBundling bundle, TieredDiscountDTO applicableDiscount, double totalCartPrice, long cartQuantity) {
        if (applicableDiscount.getDiscount() != null) {
            return applicableDiscount.getDiscount();
        } else {
            Optional<ShopInfoDTO> shopOption = shopInfoService.findByShop(shop);
            if (shopOption.isPresent() && shopOption.get().getBuildBoxVersion() == BuildBoxVersion.V1) {
                if (bundle.getMinOrderAmount() != null && bundle.getMinProductCount() != null && bundle.getMinOrderAmount() <= (totalCartPrice / 100) && bundle.getMinProductCount() <= cartQuantity) {
                    return bundle.getDiscount();
                } else if (bundle.getMinProductCount() == null && bundle.getMinOrderAmount() <= (totalCartPrice / 100)) {
                    return bundle.getDiscount();
                } else if (bundle.getMinOrderAmount() == null && bundle.getMinProductCount() <= cartQuantity) {
                    return bundle.getDiscount();
                } else {
                    return (double) 0;
                }
            } else {
                return (double) 0;
            }
        }
    }

    private void setSubscriptionBundleLink(SubscriptionBundlingDTO subscriptionBundlingDTO) {
        if (subscriptionBundlingDTO != null && BooleanUtils.isTrue(subscriptionBundlingDTO.isSubscriptionBundlingEnabled())) {
            String shop = subscriptionBundlingDTO.getShop();
            ShopInfo shopInfo = shopInfoRepository.findByShop(shop).get();
            setSubscriptionBundleLink(subscriptionBundlingDTO, shopInfo);
        }
    }

    private void setSubscriptionBundleLink(SubscriptionBundlingDTO subscriptionBundlingDTO, ShopInfo shopInfo) {
        if (subscriptionBundlingDTO != null && BooleanUtils.isTrue(subscriptionBundlingDTO.isSubscriptionBundlingEnabled())) {
            String shop = subscriptionBundlingDTO.getShop();
            String SubscriptionBundleLink = "https://" + shopInfo.getPublicDomain() + "/" + shopInfoUtils.getManageSubscriptionUrl(shop) + "/bb/" + subscriptionBundlingDTO.getUniqueRef();
            subscriptionBundlingDTO.setSubscriptionBundleLink(SubscriptionBundleLink);
        }
    }

}
