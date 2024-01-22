package com.et.service.impl;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSellingPlanGroupService;
import com.et.api.graphql.data.Product;
import com.et.api.graphql.data.ProductData;
import com.et.api.graphql.data.SellingPlanGroupData;
import com.et.api.graphql.ordercontext.PageInfo;
import com.et.api.graphql.pojo.DiscountType;
import com.et.api.graphql.pojo.*;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.SellingPlanMemberInfo;
import com.et.domain.ShopInfo;
import com.et.domain.SubscriptionBundling;
import com.et.domain.SubscriptionGroupPlan;
import com.et.domain.enumeration.DiscountTypeUnit;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.et.pojo.DiscountDetails;
import com.et.repository.SellingPlanMemberInfoRepository;
import com.et.repository.ShopInfoRepository;
import com.et.repository.SubscriptionBundlingRepository;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.MailgunService;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.SubscriptionGroupService;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.utils.ShopInfoUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.DeliveryProfileResource;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.AssociateFreeShippingDTO;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.*;

@Service
@Lazy
public class SubscriptionGroupServiceImpl implements SubscriptionGroupService {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionGroupServiceImpl.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ShopifyGraphqlSellingPlanGroupService shopifyGraphqlSellingPlanGroupService;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    @Autowired
    private SellingPlanMemberInfoRepository sellingPlanMemberInfoRepository;

    @Lazy
    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private SubscriptionBundlingRepository subscriptionBundlingRepository;

    @Autowired
    private ShopInfoUtils shopInfoUtils;

    @Autowired
    private MailgunService mailgunService;

    public static final Gson gson = new Gson();

    @Override
    public SubscriptionGroupV2DTO saveSubscriptionGroupV2(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> productIds, List<String> variantIds) throws Exception {

        SellingPlanGroupResourceInput.Builder resourceBuilder = SellingPlanGroupResourceInput.builder();

        SellingPlanGroupResourceInput resources = resourceBuilder.build();

        SellingPlanGroupInput sellingPlanGroupInput = buildSellingPlanGroupInput(subscriptionGroupDTO);
        SellingPlanGroupCreateMutation sellingPlanGroupCreateMutation = new SellingPlanGroupCreateMutation(sellingPlanGroupInput, resources);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SellingPlanGroupCreateMutation.Data>> createdSellingPlanGroups = shopifyGraphqlClient
            .getOptionalMutationResponse(sellingPlanGroupCreateMutation);

        checkForErrors(createdSellingPlanGroups);


        createdSellingPlanGroups.getData()
            .flatMap(SellingPlanGroupCreateMutation.Data::getSellingPlanGroupCreate)
            .flatMap(SellingPlanGroupCreateMutation.SellingPlanGroupCreate::getSellingPlanGroup).ifPresent(sg -> {
                //System.out.println(sg.getId());
                subscriptionGroupDTO.setId(Long.parseLong(sg.getId().replace(SELLING_PLAN_GROUP_ID_PREFIX, "")));
            });

        SubscriptionGroupV2DTO subscriptionGroupV2DTO = new SubscriptionGroupV2DTO();

        Objects.requireNonNull(createdSellingPlanGroups.getData())
            .flatMap(d -> d.getSellingPlanGroupCreate().flatMap(SellingPlanGroupCreateMutation.SellingPlanGroupCreate::getSellingPlanGroup))
            .ifPresent(sg -> {updateProductIds(sg.getId(), subscriptionGroupV2DTO, shopifyGraphqlClient, productIds, variantIds);});

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = findSingleSubscriptionGroupV2(shop, subscriptionGroupDTO.getId());
        sortSubscriptionGroupDataByProductIds(singleSubscriptionGroupV2.get(), productIds);
        sortSubscriptionGroupDataByVariantIds(singleSubscriptionGroupV2.get(), variantIds);
        updateAppstleDataForSellingPlanGroup(singleSubscriptionGroupV2.get(), subscriptionGroupDTO);

        subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), singleSubscriptionGroupV2.get());
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
        return subscriptionGroupDTO;
    }

    private void checkForErrors(Response<Optional<SellingPlanGroupCreateMutation.Data>> createdSellingPlanGroups) {
        if (!CollectionUtils.isEmpty(createdSellingPlanGroups.getErrors())) {
            logger.error("Error in creating subscription plan." + createdSellingPlanGroups.getErrors().get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(createdSellingPlanGroups.getErrors().get(0).getMessage()), "", "");
        }

        List<SellingPlanGroupCreateMutation.UserError> userErrors = Objects.requireNonNull(createdSellingPlanGroups.getData())
            .map(d -> d.getSellingPlanGroupCreate()
                .map(SellingPlanGroupCreateMutation.SellingPlanGroupCreate::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            logger.error("Error in creating subscription plan." + userErrors.get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }
    }

    @Override
    public List<SubscriptionGroupDTO> findShopSubscriptionGroups(String shop) throws Exception {
        logger.info("{} Calling shopify graphql for get selling groups", shop);
        SellingPlanGroupData subscriptionGroups = shopifyGraphqlSellingPlanGroupService.getShopSubscriptionGroups(commonUtils.prepareShopifyGraphqlClient(shop), shop, null, false, null);

        List<SubscriptionGroupDTO> subscriptionGroupDTOList = new ArrayList<>();
        for (SellingPlanGroup sellingPlanGroup : subscriptionGroups.getSellingPlanGroups()) {
            SubscriptionGroupDTO subscriptionGroupDTO = buildSubscriptionGroupDTO(sellingPlanGroup);
            subscriptionGroupDTOList.add(subscriptionGroupDTO);
        }

        return subscriptionGroupDTOList;
    }

    @Async
    public void insertAllShopSubscriptionGroupsV2() {
        String shop;
//        String shop = "test-subscription-v1.myshopify.com";
        List<ShopInfo> shopInfos = shopInfoRepository.findAll();
        for (int i = shopInfos.size() - 1; i >= 0; i--) {
            ShopInfo shopInfo = shopInfos.get(i);
            System.out.println(shopInfo.getId() + "=start ==>" + shopInfo.getShop());
            shop = shopInfo.getShop();
            try {
                List<SubscriptionGroupV2DTO> subscriptionGroupV2DTOS = findShopSubscriptionGroupsV2(shop);
                for (SubscriptionGroupV2DTO subscriptionGroupDTO : subscriptionGroupV2DTOS) {
                    SubscriptionGroupV2DTO sellingPlanGroupOpt = findSingleSubscriptionGroupV2(shop, subscriptionGroupDTO.getId()).get();
                    subscriptionGroupDTO.setSubscriptionPlans(sellingPlanGroupOpt.getSubscriptionPlans());
                    subscriptionGroupDTO.setProductIds(sellingPlanGroupOpt.getProductIds());
                    subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), subscriptionGroupDTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(shopInfo.getId() + "=end ==>" + shopInfo.getShop());
        }

    }

    @Override
    public List<SubscriptionGroupV2DTO> findShopSubscriptionGroupsV2(String shop) throws Exception {
        SellingPlanGroupData sellingPlanGroupData = new SellingPlanGroupData();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);

        List<SellingPlanGroup> sellingPlanGroups = new ArrayList<>();

        String searchText = null;
        String cursor = null;
        boolean next = false;

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SellingPlanGroupsQuery sellingPlanGroupsQuery = new SellingPlanGroupsQuery(
            StringUtils.isNoneEmpty(searchText) ? Input.optional("title:*" + searchText + "*") : Input.optional(null),
            next ? Input.optional(cursor) : Input.optional(null));

        Response<Optional<SellingPlanGroupsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupsQuery);
        //logger.debug("{} Response received from graphql for subscription groups {} ", shop, response);
        response.getData()
            .map(SellingPlanGroupsQuery.Data::getSellingPlanGroups)
            .map(SellingPlanGroupsQuery.SellingPlanGroups::getEdges)
            .orElse(new ArrayList<>()).stream()
            .map(SellingPlanGroupsQuery.Edge::getNode)
            .map(s -> {
                SellingPlanGroup sellingPlanGroup = new SellingPlanGroup();
                sellingPlanGroup.setGroupName(s.getName());
                sellingPlanGroup.setId(Long.parseLong(s.getId().replace(SELLING_PLAN_GROUP_ID_PREFIX, "")));
                sellingPlanGroup.setProductCount(s.getProductCount());
                sellingPlanGroup.setProductVariantCount(s.getProductVariantCount());
                sellingPlanGroup.setEnabled(true);
                return sellingPlanGroup;
            }).forEach(sellingPlanGroups::add);

        if (response.getData().isPresent()) {
            boolean hasNextPage = response.getData().get().getSellingPlanGroups().getPageInfo().isHasNextPage();
            pageInfo.setHasNextPage(hasNextPage);
        }

        if (pageInfo.isHasNextPage()) {
            Optional<SellingPlanGroupsQuery.Edge> last = response.getData()
                .map(SellingPlanGroupsQuery.Data::getSellingPlanGroups)
                .map(SellingPlanGroupsQuery.SellingPlanGroups::getEdges)
                .orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

            last.ifPresent(edge -> pageInfo.setCursor(edge.getCursor()));
        }

        sellingPlanGroupData.setSellingPlanGroups(sellingPlanGroups);
        sellingPlanGroupData.setPageInfo(pageInfo);


        List<SubscriptionGroupV2DTO> subscriptionGroupDTOList = new ArrayList<>();
        for (SellingPlanGroup sellingPlanGroup : sellingPlanGroupData.getSellingPlanGroups()) {
            SubscriptionGroupV2DTO subscriptionGroupDTO = buildSubscriptionGroupV2DTO(sellingPlanGroup, shop);
            subscriptionGroupDTOList.add(subscriptionGroupDTO);
        }

        return subscriptionGroupDTOList;

    }

    private SubscriptionGroupV2DTO buildSubscriptionGroupV2DTO(SellingPlanGroup sellingPlanGroup, String shop) throws JsonProcessingException {
        SubscriptionGroupV2DTO subscriptionGroupDTO = new SubscriptionGroupV2DTO();

        subscriptionGroupDTO.setId(sellingPlanGroup.getId());
        subscriptionGroupDTO.setGroupName(sellingPlanGroup.getGroupName());
        subscriptionGroupDTO.setProductCount((long) sellingPlanGroup.getProductCount());
        subscriptionGroupDTO.setProductVariantCount((long) sellingPlanGroup.getProductVariantCount());

        Optional<SubscriptionGroupV2DTO> subscriptionGroupV2DTO = subscriptionGroupPlanService.getSingleSubscriptionGroupPlan(shop, sellingPlanGroup.getId());

        subscriptionGroupV2DTO.ifPresent(groupV2DTO -> subscriptionGroupDTO.setSubscriptionPlans(groupV2DTO.getSubscriptionPlans()));


        return subscriptionGroupDTO;
    }


    private SellingPlanGroupInput buildSellingPlanGroupInput(SubscriptionGroupV2DTO subscriptionGroupDTO) throws JsonProcessingException {
        SellingPlanGroupInput.Builder sellingPlanGroupInputBuilder = SellingPlanGroupInput.builder();
        sellingPlanGroupInputBuilder.name(subscriptionGroupDTO.getGroupName());
        sellingPlanGroupInputBuilder.merchantCode(subscriptionGroupDTO.getGroupName());
        List<String> options = Lists.newArrayList("Delivery every");
        sellingPlanGroupInputBuilder.options(options);
        sellingPlanGroupInputBuilder.appId("appstle");
        sellingPlanGroupInputBuilder.position(1);

        //frequency
        List<SellingPlanInput> sellingPlans = buildSellingPlans(subscriptionGroupDTO, new HashMap<>());

        sellingPlanGroupInputBuilder.sellingPlansToCreate(sellingPlans);

        SellingPlanGroupInput sellingPlanGroupInput = sellingPlanGroupInputBuilder.build();
        return sellingPlanGroupInput;
    }

    private List<SellingPlanInput> buildSellingPlans(
        SubscriptionGroupV2DTO subscriptionGroupDTO,
        Map<String, Boolean> existingDiscountEnabledBySellingPlanId) throws JsonProcessingException {

        List<SellingPlanInput> sellingPlans = Lists.newArrayList();


        for (FrequencyInfoDTO frequency : subscriptionGroupDTO.getSubscriptionPlans()) {

            SellingPlanInput.Builder sellingPlanBuilder = SellingPlanInput.builder();
            sellingPlanBuilder.name(frequency.getFrequencyName());
            sellingPlanBuilder.description(frequency.getFrequencyDescription());
            sellingPlanBuilder.category(SellingPlanCategory.SUBSCRIPTION);

            String options = frequency.getFrequencyCount()
                + frequency.getFrequencyInterval().toString()
                + frequency.getBillingFrequencyCount()
                + frequency.getBillingFrequencyInterval().toString()
                + (frequency.getMinCycles() == null ? "MIN_CYCLES=NULL" : "MIN_CYCLES=" + frequency.getMinCycles().toString())
                + (frequency.getMaxCycles() == null ? "MAX_CYCLES=NULL" : "MAX_CYCLES=" + frequency.getMaxCycles().toString())
                + (BooleanUtils.isTrue(frequency.getDiscountEnabled()) ? frequency.getDiscountEnabled() + "-" + frequency.getDiscountOffer() + "-" + frequency.getDiscountType() : frequency.getDiscountEnabled())
                + (BooleanUtils.isTrue(frequency.getDiscountEnabled2()) ? frequency.getDiscountEnabled2() + "-" + frequency.getDiscountOffer2() + "-" + frequency.getDiscountType2() : frequency.getDiscountEnabled2())
                + frequency.getFrequencyName();

            //String options = OBJECT_MAPPER.writeValueAsString(frequency);

            List<String> options1 = Lists.newArrayList(options);
            sellingPlanBuilder.options(options1);
            sellingPlanBuilder.position(1);

            SellingPlanBillingPolicyInput billingPolicy = buildBillingPolicy(frequency);
            sellingPlanBuilder.billingPolicy(billingPolicy);

            if (frequency.getId() != null) {
                sellingPlanBuilder.id(frequency.getId());
            }

            SellingPlanDeliveryPolicyInput deliveryPolicyInput = buildDeliveryPolicy(frequency);
            sellingPlanBuilder.deliveryPolicy(deliveryPolicyInput);

            List<SellingPlanPricingPolicyInput> pricingPolicies = Lists.newArrayList();

            if (BooleanUtils.isTrue(frequency.getDiscountEnabled())) {
                pricingPolicies.add(buildFixedPricingPolicy(frequency.getDiscountOffer(), frequency.getDiscountType()));
            }

            if (BooleanUtils.isTrue(frequency.getDiscountEnabled2())) {
                pricingPolicies.add(buildPricingPolicy(frequency.getAfterCycle2(), frequency.getDiscountOffer2(), frequency.getDiscountType2()));
            }

            sellingPlanBuilder.pricingPolicies(pricingPolicies);

            SellingPlanInput sellingPlan = sellingPlanBuilder.build();
            sellingPlans.add(sellingPlan);
        }
        return sellingPlans;
    }

    private List<SellingPlanPricingPolicyInput> buildPricingPolicies(FrequencyInfoDTO frequency) {

        List<SellingPlanPricingPolicyInput> pricingPolicies = Lists.newArrayList();
        SellingPlanPricingPolicyInput pricingPolicy = buildPricingPolicy(
            frequency.getAfterCycle1(),
            frequency.getDiscountOffer(),
            frequency.getDiscountType());
        pricingPolicies.add(pricingPolicy);

        if (frequency.getAfterCycle2() != null
            && frequency.getDiscountOffer2() != null
            && frequency.getDiscountType2() != null) {
            SellingPlanPricingPolicyInput pricingPolicy2 = buildPricingPolicy(
                frequency.getAfterCycle2(),
                frequency.getDiscountOffer2(),
                frequency.getDiscountType2());
            pricingPolicies.add(0, pricingPolicy2);
        }

        return pricingPolicies;
    }

    private SellingPlanPricingPolicyInput buildPricingPolicy(Integer afterCycle, Double discountOffer, DiscountTypeUnit discountType) {
        SellingPlanPricingPolicyInput.Builder pricingPolicyInputBuilder = SellingPlanPricingPolicyInput.builder();

        SellingPlanRecurringPricingPolicyInput.Builder recurringPricingPolicyBuilder = SellingPlanRecurringPricingPolicyInput.builder();

        if (discountType == DiscountTypeUnit.FIXED) {
            recurringPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT);
        } else if (discountType == DiscountTypeUnit.PRICE) {
            recurringPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE);
        } else {
            recurringPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE);
        }

        SellingPlanPricingPolicyValueInput.Builder pricingPolicyValueBuilder = SellingPlanPricingPolicyValueInput.builder();
        if (discountType == DiscountTypeUnit.FIXED) {
            pricingPolicyValueBuilder.fixedValue(discountOffer);
        } else if (discountType == DiscountTypeUnit.PRICE) {
            pricingPolicyValueBuilder.fixedValue(discountOffer);
        } else {
            pricingPolicyValueBuilder.percentage(discountOffer);
        }

        SellingPlanPricingPolicyValueInput adjustmentValue = pricingPolicyValueBuilder.build();

        recurringPricingPolicyBuilder.adjustmentValue(adjustmentValue);

        if (afterCycle != null) {
            recurringPricingPolicyBuilder.afterCycle(afterCycle);
        }

        SellingPlanRecurringPricingPolicyInput recurring = recurringPricingPolicyBuilder.build();

        pricingPolicyInputBuilder.recurring(recurring);

        SellingPlanPricingPolicyInput pricingPolicy = pricingPolicyInputBuilder.build();
        return pricingPolicy;
    }

    private SellingPlanPricingPolicyInput buildFixedPricingPolicy(Double discountOffer, DiscountTypeUnit discountType) {
        SellingPlanPricingPolicyInput.Builder pricingPolicyInputBuilder = SellingPlanPricingPolicyInput.builder();

        SellingPlanFixedPricingPolicyInput.Builder fixedPricingPolicyBuilder = SellingPlanFixedPricingPolicyInput.builder();

        if (discountType == DiscountTypeUnit.FIXED) {
            fixedPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT);
        } else if (discountType == DiscountTypeUnit.PRICE) {
            fixedPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE);
        } else {
            fixedPricingPolicyBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE);
        }

        SellingPlanPricingPolicyValueInput.Builder pricingPolicyValueBuilder = SellingPlanPricingPolicyValueInput.builder();
        if (discountType == DiscountTypeUnit.FIXED) {
            pricingPolicyValueBuilder.fixedValue(discountOffer);
        } else if (discountType == DiscountTypeUnit.PRICE) {
            pricingPolicyValueBuilder.fixedValue(discountOffer);
        } else {
            pricingPolicyValueBuilder.percentage(discountOffer);
        }

        SellingPlanPricingPolicyValueInput adjustmentValue = pricingPolicyValueBuilder.build();

        fixedPricingPolicyBuilder.adjustmentValue(adjustmentValue);

        SellingPlanFixedPricingPolicyInput recurring = fixedPricingPolicyBuilder.build();

        pricingPolicyInputBuilder.fixed(recurring);

        SellingPlanPricingPolicyInput pricingPolicy = pricingPolicyInputBuilder.build();
        return pricingPolicy;
    }

    private SellingPlanDeliveryPolicyInput buildDeliveryPolicy(FrequencyInfoDTO frequency) {
        SellingPlanDeliveryPolicyInput.Builder deliveryPolicyBuilder = SellingPlanDeliveryPolicyInput.builder();
        SellingPlanRecurringDeliveryPolicyInput.Builder recurringDeliveryPolicyBuilder = SellingPlanRecurringDeliveryPolicyInput.builder();

        recurringDeliveryPolicyBuilder.intervalCount(frequency.getFrequencyCount());
        recurringDeliveryPolicyBuilder.interval(SellingPlanInterval.safeValueOf(frequency.getFrequencyInterval().toString()));

        if (frequency.isSpecificDayEnabled() && frequency.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
            recurringDeliveryPolicyBuilder.intent(SellingPlanRecurringDeliveryPolicyIntent.FULFILLMENT_BEGIN);
            recurringDeliveryPolicyBuilder.preAnchorBehavior(frequency.getDeliveryPolicyPreAnchorBehavior());
            List<SellingPlanAnchorInput> anchors = buildAnchors(frequency);
            recurringDeliveryPolicyBuilder.anchors(anchors);
            recurringDeliveryPolicyBuilder.cutoff(frequency.getCutOff());
        } else {
            recurringDeliveryPolicyBuilder.anchors(new ArrayList<>());
            recurringDeliveryPolicyBuilder.cutoff(0);
        }


        SellingPlanRecurringDeliveryPolicyInput recurringDeliveryPolicy = recurringDeliveryPolicyBuilder.build();
        deliveryPolicyBuilder.recurring(recurringDeliveryPolicy);
        return deliveryPolicyBuilder.build();
    }

    private List<SellingPlanAnchorInput> buildAnchors(FrequencyInfoDTO frequency) {
        List<SellingPlanAnchorInput> anchors = new ArrayList<>();
        SellingPlanAnchorInput.Builder anchorInputBuilder = SellingPlanAnchorInput.builder();
        anchorInputBuilder.day(frequency.getSpecificDayValue());

        if (frequency.getFrequencyInterval() == FrequencyIntervalUnit.MONTH) {
            anchorInputBuilder.type(SellingPlanAnchorType.MONTHDAY);
        } else if (frequency.getFrequencyInterval() == FrequencyIntervalUnit.WEEK) {
            anchorInputBuilder.type(SellingPlanAnchorType.WEEKDAY);
        } else if (frequency.getFrequencyInterval() == FrequencyIntervalUnit.YEAR) {
            anchorInputBuilder.type(SellingPlanAnchorType.YEARDAY);
            anchorInputBuilder.month(frequency.getSpecificMonthValue());
        }
        SellingPlanAnchorInput sellingPlanAnchorInput = anchorInputBuilder.build();
        anchors.add(sellingPlanAnchorInput);
        return anchors;
    }

    private SellingPlanBillingPolicyInput buildBillingPolicy(FrequencyInfoDTO frequency) {
        SellingPlanBillingPolicyInput.Builder billingPolicyBuilder = SellingPlanBillingPolicyInput.builder();
        SellingPlanRecurringBillingPolicyInput.Builder recurringBillingPolicyBuilder = SellingPlanRecurringBillingPolicyInput.builder();

        recurringBillingPolicyBuilder.intervalCount(frequency.getBillingFrequencyCount());
        recurringBillingPolicyBuilder.interval(SellingPlanInterval.safeValueOf(frequency.getBillingFrequencyInterval().toString()));

        if (frequency.isSpecificDayEnabled() && frequency.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
            List<SellingPlanAnchorInput> anchors = buildAnchors(frequency);
            recurringBillingPolicyBuilder.anchors(anchors);
        } else {
            recurringBillingPolicyBuilder.anchors(new ArrayList<>());
        }

        recurringBillingPolicyBuilder.maxCycles(frequency.getMaxCycles());
        recurringBillingPolicyBuilder.minCycles(frequency.getMinCycles());

        SellingPlanRecurringBillingPolicyInput recurringBillingPolicy = recurringBillingPolicyBuilder.build();
        billingPolicyBuilder.recurring(recurringBillingPolicy);
        return billingPolicyBuilder.build();
    }

    private List<SubscribedProductVariantInfo> getSubscribeInfoFromJSON(String subscribedProductInfoJson) {

        List<SubscribedProductVariantInfo> toReturn = CommonUtils.fromJSON(
            new TypeReference<>() {
            },
            subscribedProductInfoJson
        );

        return Optional.ofNullable(toReturn).orElse(new ArrayList<>());
    }

    private Settings buildSettings(SubscriptionGroupDTO subscriptionGroupDTO) {
        Settings settings = new Settings();
        settings.setEnabled(BooleanUtils.isTrue(subscriptionGroupDTO.isDiscountEnabled()));
        settings.setDiscountOffer(subscriptionGroupDTO.getDiscountOffer());
        settings.setDiscountType(buildDiscountType(subscriptionGroupDTO.getDiscountType()));
        return settings;
    }

    private DiscountType buildDiscountType(DiscountTypeUnit discountType) {
        if (discountType == DiscountTypeUnit.FIXED) {
            return DiscountType.FIXED_AMOUNT;
        } else if (discountType == DiscountTypeUnit.PRICE) {
            return DiscountType.PRICE;
        } else {
            return DiscountType.PERCENTAGE;
        }
    }

    private Frequency buildFrequency(SubscriptionGroupDTO subscriptionGroupDTO) {
        Frequency frequency = new Frequency();
        frequency.setFrequencyInterval(buildFrequencyInterval(subscriptionGroupDTO.getFrequencyInterval()));
        frequency.setFrequency(subscriptionGroupDTO.getFrequencyCount());
        frequency.setFrequencyName(subscriptionGroupDTO.getFrequencyName());
        return frequency;
    }

    private FrequencyInterval buildFrequencyInterval(FrequencyIntervalUnit frequencyInterval) {
        if (frequencyInterval == FrequencyIntervalUnit.DAY) {
            return FrequencyInterval.DAY;
        } else if (frequencyInterval == FrequencyIntervalUnit.WEEK) {
            return FrequencyInterval.WEEK;
        } else if (frequencyInterval == FrequencyIntervalUnit.MONTH) {
            return FrequencyInterval.MONTH;
        } else {
            return FrequencyInterval.YEAR;
        }
    }

    @Autowired
    private DeliveryProfileResource deliveryProfileResource;


    @Override
    public SubscriptionGroupV2DTO updateSubscriptionGroupV2(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> newProductIds, List<String> newVariantIds) throws Exception {

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroup = findSingleSubscriptionGroupV3(shop, subscriptionGroupDTO.getId());

        Map<Long, Set<String>> sellingGroupIdsByDeliveryProfileId = new HashMap<>();
        List<DeliveryProfileDTO> allDeliveryProfiles = new ArrayList<>();

        try {
            allDeliveryProfiles = deliveryProfileResource.getAllDeliveryProfiles();
            for (DeliveryProfileDTO deliveryProfile : allDeliveryProfiles) {
                DeliveryProfileDTO deliveryProfileDTO = deliveryProfileResource.getDeliveryProfile(deliveryProfile.getId()).getBody();
                sellingGroupIdsByDeliveryProfileId.put(deliveryProfileDTO.getId(), deliveryProfileDTO.getSellerGroupIds());
            }
        } catch (Exception ex) {

        }

        List<String> existingSellingPlanIds = singleSubscriptionGroup
            .map(s -> s.getSubscriptionPlans().stream()
                .map(FrequencyInfoDTO::getId)
                .collect(Collectors.toList()))
            .orElse(new ArrayList<>());

        Map<String, Boolean> existingDiscountEnabledBySellingPlanId = singleSubscriptionGroup
            .map(SubscriptionGroupV2DTO::getSubscriptionPlans)
            .orElse(new ArrayList<>())
            .stream()
            .collect(Collectors.toMap(FrequencyInfoDTO::getIdNew, FrequencyInfoDTO::getDiscountEnabled));

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        //deleteExistingPlan(shopifyGraphqlClient, subscriptionGroupDTO, existingSellingPlanIds);

        SellingPlanGroupInput sellingPlanGroupInput = buildSellingPlanGroupInput(subscriptionGroupDTO, existingSellingPlanIds, existingDiscountEnabledBySellingPlanId);

        SellingPlanGroupUpdateMutation sellingPlanGroupUpdateMutation = new SellingPlanGroupUpdateMutation(ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + subscriptionGroupDTO.getId(), sellingPlanGroupInput);
        Response<Optional<SellingPlanGroupUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient
            .getOptionalMutationResponse(sellingPlanGroupUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalResponse.getErrors())) {
            logger.error("Error in updating subscription plan." + optionalResponse.getErrors().get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalResponse.getErrors().get(0).getMessage()), "", "");
        }

        List<SellingPlanGroupUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalResponse.getData())
            .map(d -> d.getSellingPlanGroupUpdate()
                .map(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            logger.error("Error in updating subscription plan." + userErrors.get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }
        Objects.requireNonNull(optionalResponse.getData())
            .flatMap(d -> d.getSellingPlanGroupUpdate().flatMap(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getSellingPlanGroup))
            .ifPresent(sg -> {updateProductIds(sg.getId(), singleSubscriptionGroup.get(), shopifyGraphqlClient, newProductIds, newVariantIds);});

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = findSingleSubscriptionGroupV3(shop, subscriptionGroupDTO.getId());
        sortSubscriptionGroupDataByProductIds(singleSubscriptionGroupV2.get(), newProductIds);
        sortSubscriptionGroupDataByVariantIds(singleSubscriptionGroupV2.get(), newVariantIds);
        updateAppstleDataForSellingPlanGroup(singleSubscriptionGroupV2.get(), subscriptionGroupDTO);

        subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), singleSubscriptionGroupV2.get());

        try {
            for (DeliveryProfileDTO deliveryProfile : allDeliveryProfiles) {
                DeliveryProfileDTO deliveryProfileDTO = deliveryProfileResource.getDeliveryProfile(deliveryProfile.getId()).getBody();
                Set<String> newSellingGroupIds = deliveryProfileDTO.getSellerGroupIds();
                Set<String> oldSellingGroupIds = sellingGroupIdsByDeliveryProfileId.get(deliveryProfile.getId());

                if (!newSellingGroupIds.equals(oldSellingGroupIds)) {
                    AssociateFreeShippingDTO associateFreeShippingDTO = new AssociateFreeShippingDTO();
                    associateFreeShippingDTO.setId(deliveryProfileDTO.getId());
                    associateFreeShippingDTO.setSellerGroupIds(Optional.ofNullable(oldSellingGroupIds).orElse(new HashSet<>())
                        .stream()
                        .map(s -> s.replace(SELLING_PLAN_GROUP_ID_PREFIX, ""))
                        .collect(Collectors.toSet()));
                    deliveryProfileResource.updateFreeShippingSellerGroupIds(associateFreeShippingDTO);
                }
            }
        } catch (Exception ex) {

        }

        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);

        return subscriptionGroupDTO;
    }

    // This Function Only Update Subscription Group Details Like Frequency Details and Group Name
    @Override
    public SubscriptionGroupV2DTO updateSubscriptionGroupDetails(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroup = getSubscriptionPlanDetail(shop, subscriptionGroupDTO.getId());
        Map<Long, Set<String>> sellingGroupIdsByDeliveryProfileId = new HashMap<>();
        List<DeliveryProfileDTO> allDeliveryProfiles = new ArrayList<>();
        try {
            allDeliveryProfiles = deliveryProfileResource.getAllDeliveryProfiles();
            for (DeliveryProfileDTO deliveryProfile : allDeliveryProfiles) {
                DeliveryProfileDTO deliveryProfileDTO = deliveryProfileResource.getDeliveryProfile(deliveryProfile.getId()).getBody();
                sellingGroupIdsByDeliveryProfileId.put(deliveryProfileDTO.getId(), deliveryProfileDTO.getSellerGroupIds());
            }
        } catch (Exception ignored) {}
        List<String> existingSellingPlanIds = singleSubscriptionGroup.map(s -> s.getSubscriptionPlans().stream().map(FrequencyInfoDTO::getId).collect(Collectors.toList())).orElse(new ArrayList<>());
        Map<String, Boolean> existingDiscountEnabledBySellingPlanId = singleSubscriptionGroup
            .map(SubscriptionGroupV2DTO::getSubscriptionPlans)
            .orElse(new ArrayList<>())
            .stream()
            .collect(Collectors.toMap(FrequencyInfoDTO::getIdNew, FrequencyInfoDTO::getDiscountEnabled));

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SellingPlanGroupInput sellingPlanGroupInput = buildSellingPlanGroupInput(subscriptionGroupDTO, existingSellingPlanIds, existingDiscountEnabledBySellingPlanId);

        SellingPlanGroupUpdateMutation sellingPlanGroupUpdateMutation = new SellingPlanGroupUpdateMutation(ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + subscriptionGroupDTO.getId(), sellingPlanGroupInput);
        Response<Optional<SellingPlanGroupUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalResponse.getErrors())) {
            logger.error("Error in updating subscription plan." + optionalResponse.getErrors().get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalResponse.getErrors().get(0).getMessage()), "", "");
        }

        List<SellingPlanGroupUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalResponse.getData()).map(d -> d.getSellingPlanGroupUpdate()
            .map(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            logger.error("Error in updating subscription plan." + userErrors.get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = getSubscriptionPlanDetail(shop, subscriptionGroupDTO.getId());
        updateAppstleDataForSellingPlanGroup(singleSubscriptionGroupV2.get(), subscriptionGroupDTO);

        subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), singleSubscriptionGroupV2.get());

        try {
            for (DeliveryProfileDTO deliveryProfile : allDeliveryProfiles) {
                DeliveryProfileDTO deliveryProfileDTO = deliveryProfileResource.getDeliveryProfile(deliveryProfile.getId()).getBody();
                Set<String> newSellingGroupIds = deliveryProfileDTO.getSellerGroupIds();
                Set<String> oldSellingGroupIds = sellingGroupIdsByDeliveryProfileId.get(deliveryProfile.getId());

                if (!newSellingGroupIds.equals(oldSellingGroupIds)) {
                    AssociateFreeShippingDTO associateFreeShippingDTO = new AssociateFreeShippingDTO();
                    associateFreeShippingDTO.setId(deliveryProfileDTO.getId());
                    associateFreeShippingDTO.setSellerGroupIds(Optional.ofNullable(oldSellingGroupIds).orElse(new HashSet<>())
                        .stream()
                        .map(s -> s.replace(SELLING_PLAN_GROUP_ID_PREFIX, ""))
                        .collect(Collectors.toSet()));
                    deliveryProfileResource.updateFreeShippingSellerGroupIds(associateFreeShippingDTO);
                }
            }
        } catch (Exception ex) {}
        subscribeItScriptUtils.createOrUpdateFileInCloudAsync(shop);
        return subscriptionGroupDTO;
    }

    @Override
    public SubscriptionGroupV2DTO updateSubscriptionGroupProducts(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> updateProductIds, List<String> updateVariantIds, List<SubscribedProductVariantInfo> oldProductInfoList, List<SubscribedProductVariantInfo> oldVariantInfoList) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroup = getSubscriptionPlanDetail(shop, subscriptionGroupDTO.getId());
        List<String> existingSellingPlanIds = singleSubscriptionGroup
            .map(s -> s.getSubscriptionPlans().stream()
                .map(FrequencyInfoDTO::getId)
                .collect(Collectors.toList()))
            .orElse(new ArrayList<>());

        Map<String, Boolean> existingDiscountEnabledBySellingPlanId = singleSubscriptionGroup
            .map(SubscriptionGroupV2DTO::getSubscriptionPlans)
            .orElse(new ArrayList<>())
            .stream()
            .collect(Collectors.toMap(FrequencyInfoDTO::getIdNew, FrequencyInfoDTO::getDiscountEnabled));

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SellingPlanGroupInput sellingPlanGroupInput = buildSellingPlanGroupInput(subscriptionGroupDTO, existingSellingPlanIds, existingDiscountEnabledBySellingPlanId);

        SellingPlanGroupUpdateMutation sellingPlanGroupUpdateMutation = new SellingPlanGroupUpdateMutation(ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + subscriptionGroupDTO.getId(), sellingPlanGroupInput);
        Response<Optional<SellingPlanGroupUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient
            .getOptionalMutationResponse(sellingPlanGroupUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalResponse.getErrors())) {
            logger.error("Error in updating subscription plan." + optionalResponse.getErrors().get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalResponse.getErrors().get(0).getMessage()), "", "");
        }

        List<SellingPlanGroupUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalResponse.getData())
            .map(d -> d.getSellingPlanGroupUpdate()
                .map(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            logger.error("Error in updating subscription plan." + userErrors.get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }
        Objects.requireNonNull(optionalResponse.getData())
            .flatMap(d -> d.getSellingPlanGroupUpdate().flatMap(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getSellingPlanGroup))
            .ifPresent(sg -> updateNewProductIds(sg.getId(), singleSubscriptionGroup.get(), shopifyGraphqlClient, updateProductIds, updateVariantIds));

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = getSubscriptionPlanDetail(shop, subscriptionGroupDTO.getId());
        singleSubscriptionGroupV2.get().setProductIds(OBJECT_MAPPER.writeValueAsString(oldProductInfoList));
        singleSubscriptionGroupV2.get().setVariantIds(OBJECT_MAPPER.writeValueAsString(oldVariantInfoList));

//        sortSubscriptionGroupDataByProductIds(singleSubscriptionGroupV2.get(), newProductIds);
//        sortSubscriptionGroupDataByVariantIds(singleSubscriptionGroupV2.get(), newVariantIds);
        updateAppstleDataForSellingPlanGroup(singleSubscriptionGroupV2.get(), subscriptionGroupDTO);

        subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), singleSubscriptionGroupV2.get());

        return subscriptionGroupDTO;
    }

    @Override
    public SubscriptionGroupV2DTO deleteSubscriptionGroupProducts(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop, List<String> updateProductIds, List<String> updateVariantIds, List<SubscribedProductVariantInfo> oldProductInfoList, List<SubscribedProductVariantInfo> oldVariantInfoList) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroup = getSubscriptionPlanDetail(shop, subscriptionGroupDTO.getId());
        List<String> existingSellingPlanIds = singleSubscriptionGroup
            .map(s -> s.getSubscriptionPlans().stream()
                .map(FrequencyInfoDTO::getId)
                .collect(Collectors.toList()))
            .orElse(new ArrayList<>());

        Map<String, Boolean> existingDiscountEnabledBySellingPlanId = singleSubscriptionGroup
            .map(SubscriptionGroupV2DTO::getSubscriptionPlans)
            .orElse(new ArrayList<>())
            .stream()
            .collect(Collectors.toMap(FrequencyInfoDTO::getIdNew, FrequencyInfoDTO::getDiscountEnabled));

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SellingPlanGroupInput sellingPlanGroupInput = buildSellingPlanGroupInput(subscriptionGroupDTO, existingSellingPlanIds, existingDiscountEnabledBySellingPlanId);

        SellingPlanGroupUpdateMutation sellingPlanGroupUpdateMutation = new SellingPlanGroupUpdateMutation(ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + subscriptionGroupDTO.getId(), sellingPlanGroupInput);
        Response<Optional<SellingPlanGroupUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupUpdateMutation);

        if (!CollectionUtils.isEmpty(optionalResponse.getErrors())) {
            logger.error("Error in updating subscription plan." + optionalResponse.getErrors().get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalResponse.getErrors().get(0).getMessage()), "", "");
        }

        List<SellingPlanGroupUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalResponse.getData())
            .map(d -> d.getSellingPlanGroupUpdate()
                .map(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            logger.error("Error in updating subscription plan." + userErrors.get(0).getMessage());
            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(userErrors.get(0).getMessage()), "", "");
        }
        Objects.requireNonNull(optionalResponse.getData())
            .flatMap(d -> d.getSellingPlanGroupUpdate().flatMap(SellingPlanGroupUpdateMutation.SellingPlanGroupUpdate::getSellingPlanGroup))
            .ifPresent(sg -> deleteProductIds(sg.getId(), singleSubscriptionGroup.get(), shopifyGraphqlClient, updateProductIds, updateVariantIds));

        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = findSingleSubscriptionGroupV3(shop, subscriptionGroupDTO.getId());
        singleSubscriptionGroupV2.get().setProductIds(OBJECT_MAPPER.writeValueAsString(oldProductInfoList));
        singleSubscriptionGroupV2.get().setVariantIds(OBJECT_MAPPER.writeValueAsString(oldVariantInfoList));
        updateAppstleDataForSellingPlanGroup(singleSubscriptionGroupV2.get(), subscriptionGroupDTO);

        subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupDTO.getId(), singleSubscriptionGroupV2.get());

        return subscriptionGroupDTO;
    }

    private void deleteProductIds(String id, SubscriptionGroupV2DTO existingSubscriptionGroupV2DTO, ShopifyGraphqlClient shopifyGraphqlClient, List<String> newProductIds, List<String> newVariantIds) {

        newProductIds = newProductIds.stream().map(ShopifyGraphQLUtils::getGraphQLProductId).collect(Collectors.toList());

        newVariantIds = newVariantIds.stream().map(ShopifyGraphQLUtils::getGraphQLVariantId).collect(Collectors.toList());

        try {
            for (List<String> productsToRemove : Lists.partition(newProductIds, 250)) {
                SellingPlanGroupRemoveProductsMutation sellingPlanGroupRemoveProductsMutation = new SellingPlanGroupRemoveProductsMutation(id, productsToRemove);
                logger.info("removing products for productIdsToRemove: {}, shop: {}", productsToRemove, shopifyGraphqlClient.getShop());
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupRemoveProductsMutation);
            }

            for (List<String> variantsToRemove : Lists.partition(newVariantIds, 250)) {
                SellingPlanGroupRemoveProductVariantsMutation sellingPlanGroupRemoveProductVariantMutation = new SellingPlanGroupRemoveProductVariantsMutation(id, variantsToRemove);
                logger.info("removing variants for variantsToRemove: {}, shop: {}", variantsToRemove, shopifyGraphqlClient.getShop());
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupRemoveProductVariantMutation);
            }

        } catch (Exception e) {
        }
    }

    private SubscriptionGroupV2DTO sortSubscriptionGroupDataByProductIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO,  List<String> newProductIds) {
        try {
            List<Product> products = OBJECT_MAPPER.readValue(subscriptionGroupV2DTO.getProductIds(), new TypeReference<List<Product>>() {});
            List<SubscribedProductVariantInfo> collect = products.stream().map(product -> {
                SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                subscribedProductVariantInfo.setId(product.getId());
                subscribedProductVariantInfo.setTitle(product.getTitle());
                subscribedProductVariantInfo.setStatus(product.getStatus());
                subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                return subscribedProductVariantInfo;
            }).sorted(Comparator.comparing(product -> newProductIds.indexOf(ShopifyIdPrefix.PRODUCT_ID_PREFIX + product.getId()))).collect(Collectors.toList());
            subscriptionGroupV2DTO.setProductIds(OBJECT_MAPPER.writeValueAsString(collect));
        } catch (Exception e){
            System.out.println(e);
        }
        return subscriptionGroupV2DTO;
    }

    private SubscriptionGroupV2DTO sortSubscriptionGroupDataByVariantIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO,  List<String> variantIds) {
        try {
            List<Product> products = OBJECT_MAPPER.readValue(subscriptionGroupV2DTO.getVariantIds(), new TypeReference<List<Product>>() {});
            List<SubscribedProductVariantInfo> collect = products.stream().map(product -> {
                SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                subscribedProductVariantInfo.setId(product.getId());
                subscribedProductVariantInfo.setTitle(product.getTitle());
                subscribedProductVariantInfo.setStatus(product.getStatus());
                subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                return subscribedProductVariantInfo;
            }).sorted(Comparator.comparing(product -> variantIds.indexOf(PRODUCT_VARIANT_ID_PREFIX + product.getId()))).collect(Collectors.toList());
            subscriptionGroupV2DTO.setVariantIds(OBJECT_MAPPER.writeValueAsString(collect));
        } catch (Exception e){
            System.out.println(e);
        }
        return subscriptionGroupV2DTO;
    }

    private void updateNewProductIds(String id, SubscriptionGroupV2DTO existingSubscriptionGroupV2DTO, ShopifyGraphqlClient shopifyGraphqlClient, List<String> newProductIds, List<String> newVariantIds) {
        List<String> productIds = getProductIds(existingSubscriptionGroupV2DTO);
        List<String> variantIds = getVariantIds(existingSubscriptionGroupV2DTO);
        List<String> newVariantIdsToAdd = getIdsToAdd(newVariantIds, variantIds);
        List<String> newProductIdsToAdd = getIdsToAdd(newProductIds, productIds);
        try {

            for (List<String> productsToAdd : Lists.partition(newProductIdsToAdd, 250)) {
                SellingPlanGroupAddProductsMutation sellingPlanGroupAddProductsMutation = new SellingPlanGroupAddProductsMutation(id, productsToAdd);
                Response<Optional<SellingPlanGroupAddProductsMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupAddProductsMutation);
                String a = "b";
            }

            for (List<String> variantsToAdd : Lists.partition(newVariantIdsToAdd, 250)) {
                SellingPlanGroupAddProductVariantsMutation sellingPlanGroupAddProductVariantsMutation = new SellingPlanGroupAddProductVariantsMutation(id, variantsToAdd);
                Response<Optional<SellingPlanGroupAddProductVariantsMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupAddProductVariantsMutation);
                String a = "b";
            }
        } catch (Exception e) {
            logger.error("Error while attaching product/variant to selling plans.", e);
        }
    }

    private void updateProductIds(
        String id,
        SubscriptionGroupV2DTO existingSubscriptionGroupV2DTO,
        ShopifyGraphqlClient shopifyGraphqlClient,
        List<String> newProductIds,
        List<String> newVariantIds) {

        List<String> productIds = getProductIds(existingSubscriptionGroupV2DTO);
        List<String> variantIds = getVariantIds(existingSubscriptionGroupV2DTO);

        List<String> newVariantIdsToAdd = getIdsToAdd(newVariantIds, variantIds);
        List<String> variantIdsToRemove = getIdsToRemove(newVariantIds, variantIds);

        List<String> newProductIdsToAdd = getIdsToAdd(newProductIds, productIds);
        List<String> productIdsToRemove = getIdsToRemove(newProductIds, productIds);

        try {
            for (List<String> productsToRemove : Lists.partition(productIdsToRemove, 250)) {
                SellingPlanGroupRemoveProductsMutation sellingPlanGroupRemoveProductsMutation = new SellingPlanGroupRemoveProductsMutation(id, productsToRemove);
                logger.info("removing products for productIdsToRemove: {}, shop: {}", productsToRemove, shopifyGraphqlClient.getShop());
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupRemoveProductsMutation);
            }

            for (List<String> productsToAdd : Lists.partition(newProductIdsToAdd, 250)) {
                SellingPlanGroupAddProductsMutation sellingPlanGroupAddProductsMutation = new SellingPlanGroupAddProductsMutation(id, productsToAdd);
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupAddProductsMutation);
            }

            for (List<String> variantsToRemove : Lists.partition(variantIdsToRemove, 250)) {
                SellingPlanGroupRemoveProductVariantsMutation sellingPlanGroupRemoveProductVariantMutation = new SellingPlanGroupRemoveProductVariantsMutation(id, variantsToRemove);
                logger.info("removing variants for variantsToRemove: {}, shop: {}", variantsToRemove, shopifyGraphqlClient.getShop());
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupRemoveProductVariantMutation);
            }

            for (List<String> variantsToAdd : Lists.partition(newVariantIdsToAdd, 250)) {
                SellingPlanGroupAddProductVariantsMutation sellingPlanGroupAddProductVariantsMutation = new SellingPlanGroupAddProductVariantsMutation(id, variantsToAdd);
                shopifyGraphqlClient.getOptionalMutationResponse(sellingPlanGroupAddProductVariantsMutation);
            }
        } catch (Exception e) {
        }
    }

    private List<String> getProductIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO) {
        List<SubscribedProductVariantInfo> subscribeInfoList = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getProductIds());
        return subscribeInfoList.stream().map(a -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + a.getId().toString()).collect(Collectors.toList());
    }

    private List<String> getVariantIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO) {
        List<SubscribedProductVariantInfo> subscribeInfoList = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getVariantIds());
        return subscribeInfoList.stream().map(a -> PRODUCT_VARIANT_ID_PREFIX + a.getId().toString()).collect(Collectors.toList());
    }

    private List<String> getIdsToRemove(List<String> newIds, List<String> oldIds) {
        return oldIds.stream()
            .filter(id -> !newIds.contains(id))
            .collect(Collectors.toList());
    }

    private List<String> getIdsToAdd(List<String> newIds, List<String> oldIds) {
        return newIds.stream()
            .filter(id -> !oldIds.contains(id))
            .collect(Collectors.toList());
    }

    private SellingPlanGroupInput buildSellingPlanGroupInput(
        SubscriptionGroupV2DTO subscriptionGroupDTO,
        List<String> existingSellingPlanIds,
        Map<String, Boolean> existingDiscountEnabledBySellingPlanId) throws JsonProcessingException {

        SellingPlanGroupInput.Builder sellingPlanGroupInputBuilder = SellingPlanGroupInput.builder();
        sellingPlanGroupInputBuilder.name(subscriptionGroupDTO.getGroupName());
        sellingPlanGroupInputBuilder.merchantCode(subscriptionGroupDTO.getGroupName());
        List<String> options = Lists.newArrayList("Delivery every");
        sellingPlanGroupInputBuilder.options(options);
        sellingPlanGroupInputBuilder.position(1);
        sellingPlanGroupInputBuilder.appId("appstle");

        //frequency
        List<SellingPlanInput> sellingPlans = buildSellingPlans(subscriptionGroupDTO, existingDiscountEnabledBySellingPlanId);


        List<SellingPlanInput> sellingPlansToCreate = sellingPlans.stream().filter(s -> s.id() == null).collect(Collectors.toList());
        if (!sellingPlansToCreate.isEmpty()) {
            sellingPlanGroupInputBuilder.sellingPlansToCreate(sellingPlansToCreate);
        }

        List<SellingPlanInput> sellingPlansToUpdate = sellingPlans.stream().filter(s -> s.id() != null).collect(Collectors.toList());

        if (!sellingPlansToUpdate.isEmpty()) {
            sellingPlanGroupInputBuilder.sellingPlansToUpdate(sellingPlansToUpdate);
        }

        Set<String> sellingPlansToUpdateIds = sellingPlansToUpdate.stream().map(SellingPlanInput::id).collect(Collectors.toSet());
        List<String> planIdsToDelete = existingSellingPlanIds.stream().filter(id -> !sellingPlansToUpdateIds.contains(id)).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(planIdsToDelete)) {
            sellingPlanGroupInputBuilder.sellingPlansToDelete(planIdsToDelete);
        }

        SellingPlanGroupInput sellingPlanGroupInput = sellingPlanGroupInputBuilder.build();
        return sellingPlanGroupInput;
    }

    private void deleteExistingPlan(ShopifyGraphqlClient shopifyGraphqlClient, SubscriptionGroupV2DTO subscriptionGroupDTO, List<String> existingSellingPlanIds) throws Exception {
        SellingPlanGroupInput sellingPlanGroupInputToDelete = buildSellingPlanGroupInputForDelete(subscriptionGroupDTO, existingSellingPlanIds);
        SellingPlanGroupUpdateMutation sellingPlanGroupUpdateMutation1 = new SellingPlanGroupUpdateMutation(ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX + subscriptionGroupDTO.getId(), sellingPlanGroupInputToDelete);
        Response<Optional<SellingPlanGroupUpdateMutation.Data>> optionalResponse1 = shopifyGraphqlClient
            .getOptionalMutationResponse(sellingPlanGroupUpdateMutation1);
    }

    private SellingPlanGroupInput buildSellingPlanGroupInputForDelete(SubscriptionGroupV2DTO subscriptionGroupDTO, List<String> existingSellingPlanIds) {
        SellingPlanGroupInput.Builder sellingPlanGroupInputBuilder = SellingPlanGroupInput.builder();
        sellingPlanGroupInputBuilder.name(subscriptionGroupDTO.getGroupName());
        sellingPlanGroupInputBuilder.merchantCode(subscriptionGroupDTO.getGroupName());
        List<String> options = Lists.newArrayList("Delivery every");
        sellingPlanGroupInputBuilder.options(options);
        sellingPlanGroupInputBuilder.position(1);
        sellingPlanGroupInputBuilder.appId("appstle");

        if (!CollectionUtils.isEmpty(existingSellingPlanIds)) {
            sellingPlanGroupInputBuilder.sellingPlansToDelete(existingSellingPlanIds);
        }

        SellingPlanGroupInput sellingPlanGroupInput = sellingPlanGroupInputBuilder.build();
        return sellingPlanGroupInput;
    }

    private void populateFrequencyInfo(SellingPlanGroup sellingPlanGroup, SubscriptionGroupDTO subscriptionGroupDTO) {
        List<Frequency> frequencies = sellingPlanGroup.getFrequencies();

        if (frequencies == null) {
            return;
        }

        if (frequencies.isEmpty()) {
            return;
        }
        Frequency frequency = frequencies.get(0);
        subscriptionGroupDTO.setFrequencyCount(frequency.getFrequency());
        subscriptionGroupDTO.setFrequencyInterval(buildFrequencyInterval(frequency.getFrequencyInterval()));
        subscriptionGroupDTO.setFrequencyName(frequency.getFrequencyName());
    }

    private FrequencyIntervalUnit buildFrequencyInterval(FrequencyInterval frequencyInterval) {
        if (frequencyInterval == FrequencyInterval.DAY) {
            return FrequencyIntervalUnit.DAY;
        } else if (frequencyInterval == FrequencyInterval.WEEK) {
            return FrequencyIntervalUnit.WEEK;
        } else if (frequencyInterval == FrequencyInterval.MONTH) {
            return FrequencyIntervalUnit.MONTH;
        } else {
            return FrequencyIntervalUnit.YEAR;
        }
    }

    private void populateDiscountDetails(SellingPlanGroup sellingPlanGroup, SubscriptionGroupDTO subscriptionGroupDTO) {
        Settings settings = sellingPlanGroup.getSettings();
        if (settings == null) {
            return;
        }
        subscriptionGroupDTO.setDiscountEnabled(settings.isEnabled());
        subscriptionGroupDTO.setDiscountOffer(settings.getDiscountOffer());
        subscriptionGroupDTO.setDiscountType(buildDiscountType(settings.getDiscountType()));
    }

    private DiscountTypeUnit buildDiscountType(DiscountType discountType) {
        if (discountType == DiscountType.FIXED_AMOUNT) {
            return DiscountTypeUnit.FIXED;
        } else if (discountType == DiscountType.PRICE) {
            return DiscountTypeUnit.PRICE;
        } else {
            return DiscountTypeUnit.PERCENTAGE;
        }
    }

    @Override
    public Optional<SubscriptionGroupV2DTO> findSingleSubscriptionGroupV3(String shop, Long subscriptionGroupId) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = findSingleSubscriptionGroupV2(shop, subscriptionGroupId);
        Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(subscriptionGroupId);
        SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
        List<String> productIds = new ArrayList<>();
        List<String> variantIds = new ArrayList<>();

        if (dbGroupPlan.isPresent() && StringUtils.isNotBlank(dbGroupPlan.get().getInfoJson())) {
            dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, dbGroupPlan.get().getInfoJson());
        }

        if (dbSubscriptionGroupDTO != null) {
            productIds = getProductIds(dbSubscriptionGroupDTO);
            variantIds = getVariantIds(dbSubscriptionGroupDTO);
        }

        sortSubscriptionGroupDataByProductIds(singleSubscriptionGroupV2.get(), productIds);
        sortSubscriptionGroupDataByVariantIds(singleSubscriptionGroupV2.get(), variantIds);
        singleSubscriptionGroupV2.get().getSubscriptionPlans().sort(Comparator.comparing(FrequencyInfoDTO::getFrequencySequence, Comparator.nullsLast(Integer::compareTo)));
        return singleSubscriptionGroupV2;
    }

    @Override
    public Optional<SubscriptionGroupV2DTO> syncSubscriptionGroupPlan(String shop, Long subscriptionGroupId) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = findSingleSubscriptionGroupV3(shop, subscriptionGroupId);
        singleSubscriptionGroupV2.ifPresent(subscriptionGroupV2DTO -> subscriptionGroupPlanService.createOrUpdateRecord(shop, subscriptionGroupId, subscriptionGroupV2DTO));
        return singleSubscriptionGroupV2;
    }

    @Override
    public Optional<SubscriptionGroupV2DTO> getSubscriptionGroupPlanDetail(String shop, Long subscriptionGroupId) throws Exception {
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = getSubscriptionPlanDetail(shop, subscriptionGroupId);
        singleSubscriptionGroupV2.get().getSubscriptionPlans().sort(Comparator.comparing(FrequencyInfoDTO::getFrequencySequence, Comparator.nullsLast(Integer::compareTo)));
        return singleSubscriptionGroupV2;
    }

    @Override
    public Optional<SubscriptionGroupV2DTO> findSingleSubscriptionGroupV2(String shop, Long sellingPlanGroupId) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);


        SellingPlanGroupQuery sellingPlanGroupQuery = new SellingPlanGroupQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId);

        Response<Optional<SellingPlanGroupQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);

        while (!CollectionUtils.isEmpty(response.getErrors()) && response.getErrors().get(0).getMessage().equals("Throttled")) {
            Thread.sleep(1000l);
            response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);
        }

        Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);

        //logger.debug("{} Response received from graphql for subscription groups {} ", sellingPlanGroupId, response);
        Optional<SubscriptionGroupV2DTO> subscriptionGroupV2DTO = Objects.requireNonNull(response.getData())
            .flatMap(SellingPlanGroupQuery.Data::getSellingPlanGroup)
            .map(s -> {
                SubscriptionGroupV2DTO subscriptionGroup = new SubscriptionGroupV2DTO();
                subscriptionGroup.setGroupName(s.getName());
                subscriptionGroup.setId(Long.parseLong(s.getId().replace(SELLING_PLAN_GROUP_ID_PREFIX, "")));

                List<FrequencyInfoDTO> frequencies = s.getSellingPlans().getEdges()
                    .stream()
                    .map(SellingPlanGroupQuery.Edge::getNode)
                    .map(node -> {
                        String sellingPlanId = node.getId();
                        SellingPlanGroupQuery.BillingPolicy billingPolicy = node.getBillingPolicy();

                        FrequencyInfoDTO frequency = new FrequencyInfoDTO();
                        frequency.setId(sellingPlanId);
                        frequency.setIdNew(sellingPlanId);

                        if (billingPolicy instanceof SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy) {
                            SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy billingPolicy1 = (SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy) billingPolicy;
                            frequency.setBillingFrequencyCount(billingPolicy1.getIntervalCount());
                            frequency.setBillingFrequencyInterval(buildFrequencyInterval(billingPolicy1.getInterval()));
                            frequency.setMaxCycles(billingPolicy1.getMaxCycles().orElse(null));
                            frequency.setMinCycles(billingPolicy1.getMinCycles().orElse(null));
                        }


                        SellingPlanGroupQuery.DeliveryPolicy deliveryPolicy = node.getDeliveryPolicy();

                        if (deliveryPolicy instanceof SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy) {
                            SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy deliveryPolicy1 = (SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy) deliveryPolicy;

                            frequency.setCutOff(deliveryPolicy1.getCutoff().orElse(null));
                            frequency.setFrequencyCount(deliveryPolicy1.getIntervalCount());
                            frequency.setFrequencyInterval(buildFrequencyInterval(deliveryPolicy1.getInterval()));

                            List<SellingPlanGroupQuery.Anchor1> anchors = deliveryPolicy1.getAnchors();
                            if (anchors.isEmpty()) {
                                frequency.setFrequencyType(FrequencyType.ON_PURCHASE_DAY);
                                frequency.setSpecificDayEnabled(false);
                            } else {
                                frequency.setFrequencyType(FrequencyType.ON_SPECIFIC_DAY);
                                frequency.setSpecificDayEnabled(true);
                                SellingPlanGroupQuery.Anchor1 anchor1 = anchors.get(0);
                                frequency.setSpecificDayValue(anchor1.getDay());
                                frequency.setSpecificMonthValue(anchors.get(0).getMonth().orElse(null));
                            }
                        }

                        if (frequency.getFrequencyCount().equals(frequency.getBillingFrequencyCount())) {
                            frequency.setPrepaidFlag("false");
                        } else {
                            frequency.setPrepaidFlag("true");
                        }

                        frequency.setFrequencyName(node.getName());
                        frequency.setFrequencyDescription(node.getDescription().orElse(null));

                        List<SellingPlanGroupQuery.PricingPolicy> pricingPolicies = node.getPricingPolicies();

                        if (CollectionUtils.isEmpty(pricingPolicies)) {
                            frequency.setDiscountEnabled(false);
                            frequency.setDiscountEnabled2(false);
                        } else {
                            frequency.setDiscountEnabled(true);
                            SellingPlanGroupQuery.PricingPolicy pricingPolicy1 = pricingPolicies.get(0);
                            DiscountDetails discountDetails1 = buildDiscountDetails(pricingPolicy1);

                            frequency.setDiscountOffer(discountDetails1.getDiscountOffer());
                            frequency.setDiscountType(discountDetails1.getDiscountType());
                            frequency.setAfterCycle1(discountDetails1.getAfterCycle());

                            if (pricingPolicies.size() == 2) {
                                frequency.setDiscountEnabled2(true);
                                SellingPlanGroupQuery.PricingPolicy pricingPolicy2 = pricingPolicies.get(1);
                                DiscountDetails discountDetails2 = buildDiscountDetails(pricingPolicy2);

                                frequency.setDiscountOffer2(discountDetails2.getDiscountOffer());
                                frequency.setDiscountType2(discountDetails2.getDiscountType());
                                frequency.setAfterCycle2(discountDetails2.getAfterCycle());
                            }
                        }

                        if (!frequency.getDiscountEnabled()) {

                            frequency.setDiscountEnabled(false);

                            frequency.setDiscountType(null);
                            frequency.setDiscountOffer(null);
                            frequency.setAfterCycle1(0);
                        }

                        if (BooleanUtils.isFalse(frequency.getDiscountEnabled2())) {

                            frequency.setDiscountEnabled2(false);
                            frequency.setDiscountType2(null);
                            frequency.setDiscountOffer2(null);
                            frequency.setAfterCycle2(0);
                        }

                        if (frequency.getBillingFrequencyCount().equals(frequency.getFrequencyCount())) {
                            frequency.setPlanType(PlanType.PAY_AS_YOU_GO);
                        } else if (frequency.getBillingFrequencyCount() > frequency.getFrequencyCount()) {
                            if (frequency.getMaxCycles() != null && frequency.getMaxCycles() == 1 && frequency.getMinCycles() == null) {
                                frequency.setPlanType(PlanType.PREPAID);
                            } else {
                                frequency.setPlanType(PlanType.ADVANCED_PREPAID);
                            }
                        }

                        if (dbGroupPlan.isPresent() && StringUtils.isNotBlank(dbGroupPlan.get().getInfoJson())) {
                            SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
                            dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                            }, dbGroupPlan.get().getInfoJson());
                            Optional<FrequencyInfoDTO> sellingPlanWithAppstleData = dbSubscriptionGroupDTO.getSubscriptionPlans()
                                .stream()
                                .filter(asp -> frequency.getId().equalsIgnoreCase(asp.getId()) || frequency.getFrequencyName().equalsIgnoreCase(asp.getFrequencyName()))
                                .findFirst();

                            if (sellingPlanWithAppstleData.isPresent()) {
                                updateAppstleDataForSellingPlan(frequency, sellingPlanWithAppstleData.get());
                            }
                        }


                        return frequency;
                    }).collect(Collectors.toList());


                subscriptionGroup.setSubscriptionPlans(frequencies);


                try {
                    ProductData productData = getSellingGroupProducts(shopifyGraphqlClient, sellingPlanGroupId, true, null);

                    List<SubscribedProductVariantInfo> collect = productData.getProducts().stream().map(product -> {
                        SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                        subscribedProductVariantInfo.setId(product.getId());
                        subscribedProductVariantInfo.setTitle(product.getTitle());
                        subscribedProductVariantInfo.setStatus(product.getStatus());
                        subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                        subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                        subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                        subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                        return subscribedProductVariantInfo;
                    }).collect(Collectors.toList());
                    subscriptionGroup.setProductIds(OBJECT_MAPPER.writeValueAsString(collect));
                } catch (Exception e) {

                }

                try {
                    ProductData productVariantsData = getSellingGroupProductVariants(shopifyGraphqlClient, sellingPlanGroupId, true, null);

                    List<SubscribedProductVariantInfo> collect = productVariantsData.getProducts().stream().map(product -> {
                        SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                        subscribedProductVariantInfo.setId(product.getId());
                        subscribedProductVariantInfo.setTitle(product.getTitle());
                        subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                        subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                        subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                        subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                        return subscribedProductVariantInfo;
                    }).collect(Collectors.toList());
                    subscriptionGroup.setVariantIds(OBJECT_MAPPER.writeValueAsString(collect));
                } catch (Exception e) {

                }

                return subscriptionGroup;
            });

        return subscriptionGroupV2DTO;
    }

    @Override
    public void sortSellingPlanGroupProductOrVariant(String shop, Long sellingPlanGroupId, List<Long> productIdList, List<Long> variantIdList) {
        try {
            // Get the subscription group information
            Optional<SubscriptionGroupV2DTO> subscriptionGroupV2DTOOptional = getSubscriptionGroupPlanDetail(shop, sellingPlanGroupId);

            if (subscriptionGroupV2DTOOptional.isPresent()) {
                SubscriptionGroupV2DTO subscriptionGroupV2DTO = subscriptionGroupV2DTOOptional.get();

                // Sort and update product information
                if (!productIdList.isEmpty()) {
                    List<SubscribedProductVariantInfo> productInfos = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getProductIds());
                    if (!productInfos.isEmpty()) {
                        productInfos.sort(Comparator.comparingLong(product -> productIdList.indexOf(product.getId())));
                        subscriptionGroupV2DTO.setProductIds(OBJECT_MAPPER.writeValueAsString(productInfos));
                    }
                }

                // Sort and update variant information
                if (!variantIdList.isEmpty()) {
                    List<SubscribedProductVariantInfo> variantInfos = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getVariantIds());
                    if (!variantInfos.isEmpty()) {
                        variantInfos.sort(Comparator.comparingLong(variant -> variantIdList.indexOf(variant.getId())));
                        subscriptionGroupV2DTO.setVariantIds(OBJECT_MAPPER.writeValueAsString(variantInfos));
                    }
                }

                // Update the subscription group
                subscriptionGroupPlanService.createOrUpdateRecord(shop, sellingPlanGroupId, subscriptionGroupV2DTO);
            }
        } catch (Exception ignore) {
            logger.error("{} {} Calling sort product subscription groups in {}", shop ,sellingPlanGroupId, ignore);
        }
    }


    private Optional<SubscriptionGroupV2DTO> getSubscriptionPlanDetail(String shop, Long sellingPlanGroupId) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SellingPlanGroupQuery sellingPlanGroupQuery = new SellingPlanGroupQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId);
        Response<Optional<SellingPlanGroupQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);

        while (!CollectionUtils.isEmpty(response.getErrors()) && response.getErrors().get(0).getMessage().equals("Throttled")) {
            Thread.sleep(1000L);
            response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);
        }
        Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);

        if (dbGroupPlan.isEmpty()) {
            syncSubscriptionGroupPlan(shop, sellingPlanGroupId);
            dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);
        }

        Optional<SubscriptionGroupPlan> finalDbGroupPlan = dbGroupPlan;
        return Objects.requireNonNull(response.getData())
            .flatMap(SellingPlanGroupQuery.Data::getSellingPlanGroup)
            .map(s -> {
                SubscriptionGroupV2DTO subscriptionGroup = new SubscriptionGroupV2DTO();
                subscriptionGroup.setGroupName(s.getName());
                subscriptionGroup.setId(Long.parseLong(s.getId().replace(SELLING_PLAN_GROUP_ID_PREFIX, "")));

                List<FrequencyInfoDTO> frequencies = s.getSellingPlans().getEdges()
                    .stream()
                    .map(SellingPlanGroupQuery.Edge::getNode)
                    .map(node -> {
                        String sellingPlanId = node.getId();
                        SellingPlanGroupQuery.BillingPolicy billingPolicy = node.getBillingPolicy();

                        FrequencyInfoDTO frequency = new FrequencyInfoDTO();
                        frequency.setId(sellingPlanId);
                        frequency.setIdNew(sellingPlanId);

                        if (billingPolicy instanceof SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy) {
                            SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy billingPolicy1 = (SellingPlanGroupQuery.AsSellingPlanRecurringBillingPolicy) billingPolicy;
                            frequency.setBillingFrequencyCount(billingPolicy1.getIntervalCount());
                            frequency.setBillingFrequencyInterval(buildFrequencyInterval(billingPolicy1.getInterval()));
                            frequency.setMaxCycles(billingPolicy1.getMaxCycles().orElse(null));
                            frequency.setMinCycles(billingPolicy1.getMinCycles().orElse(null));
                        }
                        SellingPlanGroupQuery.DeliveryPolicy deliveryPolicy = node.getDeliveryPolicy();
                        if (deliveryPolicy instanceof SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy) {
                            SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy deliveryPolicy1 = (SellingPlanGroupQuery.AsSellingPlanRecurringDeliveryPolicy) deliveryPolicy;

                            frequency.setCutOff(deliveryPolicy1.getCutoff().orElse(null));
                            frequency.setFrequencyCount(deliveryPolicy1.getIntervalCount());
                            frequency.setFrequencyInterval(buildFrequencyInterval(deliveryPolicy1.getInterval()));

                            List<SellingPlanGroupQuery.Anchor1> anchors = deliveryPolicy1.getAnchors();
                            if (anchors.isEmpty()) {
                                frequency.setFrequencyType(FrequencyType.ON_PURCHASE_DAY);
                                frequency.setSpecificDayEnabled(false);
                            } else {
                                frequency.setFrequencyType(FrequencyType.ON_SPECIFIC_DAY);
                                frequency.setSpecificDayEnabled(true);
                                SellingPlanGroupQuery.Anchor1 anchor1 = anchors.get(0);
                                frequency.setSpecificDayValue(anchor1.getDay());
                                frequency.setSpecificMonthValue(anchors.get(0).getMonth().orElse(null));
                            }
                        }

                        if (frequency.getFrequencyCount().equals(frequency.getBillingFrequencyCount())) {
                            frequency.setPrepaidFlag("false");
                        } else {
                            frequency.setPrepaidFlag("true");
                        }

                        frequency.setFrequencyName(node.getName());
                        frequency.setFrequencyDescription(node.getDescription().orElse(null));

                        List<SellingPlanGroupQuery.PricingPolicy> pricingPolicies = node.getPricingPolicies();

                        if (CollectionUtils.isEmpty(pricingPolicies)) {
                            frequency.setDiscountEnabled(false);
                            frequency.setDiscountEnabled2(false);
                        } else {
                            frequency.setDiscountEnabled(true);
                            SellingPlanGroupQuery.PricingPolicy pricingPolicy1 = pricingPolicies.get(0);
                            DiscountDetails discountDetails1 = buildDiscountDetails(pricingPolicy1);

                            frequency.setDiscountOffer(discountDetails1.getDiscountOffer());
                            frequency.setDiscountType(discountDetails1.getDiscountType());
                            frequency.setAfterCycle1(discountDetails1.getAfterCycle());

                            if (pricingPolicies.size() == 2) {
                                frequency.setDiscountEnabled2(true);
                                SellingPlanGroupQuery.PricingPolicy pricingPolicy2 = pricingPolicies.get(1);
                                DiscountDetails discountDetails2 = buildDiscountDetails(pricingPolicy2);

                                frequency.setDiscountOffer2(discountDetails2.getDiscountOffer());
                                frequency.setDiscountType2(discountDetails2.getDiscountType());
                                frequency.setAfterCycle2(discountDetails2.getAfterCycle());
                            }
                        }

                        if (!frequency.getDiscountEnabled()) {
                            frequency.setDiscountEnabled(false);
                            frequency.setDiscountType(null);
                            frequency.setDiscountOffer(null);
                            frequency.setAfterCycle1(0);
                        }

                        if (BooleanUtils.isFalse(frequency.getDiscountEnabled2())) {

                            frequency.setDiscountEnabled2(false);
                            frequency.setDiscountType2(null);
                            frequency.setDiscountOffer2(null);
                            frequency.setAfterCycle2(0);
                        }

                        if (frequency.getBillingFrequencyCount().equals(frequency.getFrequencyCount())) {
                            frequency.setPlanType(PlanType.PAY_AS_YOU_GO);
                        } else if (frequency.getBillingFrequencyCount() > frequency.getFrequencyCount()) {
                            if (frequency.getMaxCycles() != null && frequency.getMaxCycles() == 1 && frequency.getMinCycles() == null) {
                                frequency.setPlanType(PlanType.PREPAID);
                            } else {
                                frequency.setPlanType(PlanType.ADVANCED_PREPAID);
                            }
                        }
                        if (finalDbGroupPlan.isPresent() && StringUtils.isNotBlank(finalDbGroupPlan.get().getInfoJson())) {
                            SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
                            dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                            }, finalDbGroupPlan.get().getInfoJson());
                                Optional<FrequencyInfoDTO> sellingPlanWithAppstleData = dbSubscriptionGroupDTO.getSubscriptionPlans()
                                .stream()
                                .filter(asp -> frequency.getId().equalsIgnoreCase(asp.getId()) || frequency.getFrequencyName().equalsIgnoreCase(asp.getFrequencyName()))
                                .findFirst();
                            sellingPlanWithAppstleData.ifPresent(frequencyInfoDTO -> updateAppstleDataForSellingPlan(frequency, frequencyInfoDTO));
                        }
                        return frequency;
                    }).collect(Collectors.toList());
                SubscriptionGroupV2DTO jsonInfo = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, finalDbGroupPlan.get().getInfoJson());
                subscriptionGroup.setProductIds(jsonInfo.getProductIds());
                subscriptionGroup.setVariantIds(jsonInfo.getVariantIds());
                subscriptionGroup.setSubscriptionPlans(frequencies);
                return subscriptionGroup;
            });
    }

    private Optional<SubscriptionGroupV2DTO> getSubscriptionGroupPlanProductDetails(String shop, Long sellingPlanGroupId) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);
        SubscriptionGroupV2DTO subscriptionGroup = new SubscriptionGroupV2DTO();
        SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
        if (dbGroupPlan.isPresent() && StringUtils.isNotBlank(dbGroupPlan.get().getInfoJson())) {
            dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, dbGroupPlan.get().getInfoJson());
        }
        List<String> productIds = new ArrayList<>();
        List<String> variantIds = new ArrayList<>();

        if (dbSubscriptionGroupDTO != null) {
            productIds = getProductIds(dbSubscriptionGroupDTO);
            variantIds = getVariantIds(dbSubscriptionGroupDTO);
        }

        try {
            ProductData productData = getSellingGroupProducts(shopifyGraphqlClient, sellingPlanGroupId, true, null);

            List<String> finalProductIds = productIds;
            List<SubscribedProductVariantInfo> collect = productData.getProducts().stream().map(product -> {
                SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                subscribedProductVariantInfo.setId(product.getId());
                subscribedProductVariantInfo.setTitle(product.getTitle());
                subscribedProductVariantInfo.setStatus(product.getStatus());
                subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                return subscribedProductVariantInfo;
            }).sorted(Comparator.comparing(product -> finalProductIds.indexOf(PRODUCT_VARIANT_ID_PREFIX + product.getId()))).collect(Collectors.toList());
            subscriptionGroup.setProductIds(OBJECT_MAPPER.writeValueAsString(collect));
        } catch (Exception ignored) {

        }

        try {
            ProductData productVariantsData = getSellingGroupProductVariants(shopifyGraphqlClient, sellingPlanGroupId, true, null);
            List<String> finalVariantIds = variantIds;
            List<SubscribedProductVariantInfo> collect = productVariantsData.getProducts().stream().map(product -> {
                SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                subscribedProductVariantInfo.setId(product.getId());
                subscribedProductVariantInfo.setTitle(product.getTitle());
                subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                subscribedProductVariantInfo.setAdditionalProperty("vendor", product.getVendor());
                subscribedProductVariantInfo.setAdditionalProperty("tags", product.getTags());
                subscribedProductVariantInfo.setAdditionalProperty("productType", product.getProductType());
                subscribedProductVariantInfo.setAdditionalProperty("price", product.getPrice());
                return subscribedProductVariantInfo;
            }).sorted(Comparator.comparing(product -> finalVariantIds.indexOf(PRODUCT_VARIANT_ID_PREFIX + product.getId()))).collect(Collectors.toList());
            subscriptionGroup.setVariantIds(OBJECT_MAPPER.writeValueAsString(collect));
        } catch (Exception ignored) {

        }
        return Optional.of(subscriptionGroup);
    }

    @Override
    public Optional<ProductData> getSellingGroupProductsData(String shop, Long sellingPlanGroupId, boolean next, String cursor) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ProductData productData = new ProductData();
        SellingPlanGroupProductsQuery sellingPlanGroupProductsQuery = new SellingPlanGroupProductsQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId, next ? Input.optional(cursor) : Input.optional(null));
        Response<Optional<SellingPlanGroupProductsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupProductsQuery);
        List<Product> products = convertProducts(response);

        if (!CollectionUtils.isEmpty(products)) {
            List<String> productIds = new ArrayList<>();
            SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
            Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);
            if (dbGroupPlan.isPresent() && StringUtils.isNotBlank(dbGroupPlan.get().getInfoJson())) {
                dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, dbGroupPlan.get().getInfoJson());
            }
            if (dbSubscriptionGroupDTO != null) {
                productIds = getProductIds(dbSubscriptionGroupDTO);
            }
            List<String> finalProductIds = productIds;
            products = products.stream().sorted(Comparator.comparing(product -> finalProductIds.indexOf(ShopifyIdPrefix.PRODUCT_ID_PREFIX + product.getId()))).collect(Collectors.toList());
        }

        PageInfo pageInfo = new PageInfo();
        if (response.getData() != null && response.getData().isPresent()) {
            next = response.getData().get().getSellingPlanGroup().get().getProducts().getPageInfo().isHasNextPage();
            pageInfo.setHasNextPage(next);
        } else {
            pageInfo.setHasNextPage(false);
        }
        cursor = null;
        if (next) {
            Optional<SellingPlanGroupProductsQuery.Edge> last = response.getData()
                .flatMap(SellingPlanGroupProductsQuery.Data::getSellingPlanGroup)
                .map(SellingPlanGroupProductsQuery.SellingPlanGroup::getProducts)
                .map(SellingPlanGroupProductsQuery.Products::getEdges)
                .orElse(new ArrayList<>()).stream().reduce((first, second) -> second);
            if (last.isPresent()) {
                cursor = last.get().getCursor();
            }
        }
        pageInfo.setCursor(cursor);
        productData.setProducts(products);
        productData.setPageInfo(pageInfo);

        return Optional.of(productData);
    }

    @Override
    public Optional<ProductData> getSellingGroupVariantsData(String shop, Long sellingPlanGroupId, boolean hasNextPage, String cursor) throws Exception {
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        ProductData productData = new ProductData();
        SellingPlanGroupProductVariantsQuery sellingPlanGroupProductsQuery = new SellingPlanGroupProductVariantsQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId,
            hasNextPage ? Input.optional(cursor) : Input.optional(null));

        Response<Optional<SellingPlanGroupProductVariantsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupProductsQuery);
        List<Product> variants = convertProductVariants(response);

        if (!CollectionUtils.isEmpty(variants)) {
            List<String> variantIds = new ArrayList<>();
            SubscriptionGroupV2DTO dbSubscriptionGroupDTO = null;
            Optional<SubscriptionGroupPlan> dbGroupPlan = subscriptionGroupPlanRepository.findBySubscriptionId(sellingPlanGroupId);
            if (dbGroupPlan.isPresent() && StringUtils.isNotBlank(dbGroupPlan.get().getInfoJson())) {
                dbSubscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, dbGroupPlan.get().getInfoJson());
            }
            if (dbSubscriptionGroupDTO != null) {
                variantIds = getVariantIds(dbSubscriptionGroupDTO);
            }
            List<String> finalProductIds = variantIds;
            variants = variants.stream().sorted(Comparator.comparing(variant -> finalProductIds.indexOf(PRODUCT_VARIANT_ID_PREFIX + variant.getId()))).collect(Collectors.toList());
        }

        PageInfo pageInfo = new PageInfo();
        if (response.getData() != null && response.getData().isPresent()) {
            hasNextPage = response.getData().get().getSellingPlanGroup().get().getProductVariants().getPageInfo().isHasNextPage();
            pageInfo.setHasNextPage(hasNextPage);
        } else {
            pageInfo.setHasNextPage(false);
        }
        cursor = null;
        if (hasNextPage) {
            Optional<SellingPlanGroupProductVariantsQuery.Edge> last = response.getData()
                .flatMap(SellingPlanGroupProductVariantsQuery.Data::getSellingPlanGroup)
                .map(SellingPlanGroupProductVariantsQuery.SellingPlanGroup::getProductVariants)
                .map(SellingPlanGroupProductVariantsQuery.ProductVariants::getEdges)
                .orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

            if (last.isPresent()) {
                cursor = last.get().getCursor();
            }
        }
        pageInfo.setCursor(cursor);
        productData.setProducts(variants);
        productData.setPageInfo(pageInfo);
        return Optional.of(productData);
    }

    private void updateAppstleDataForSellingPlanGroup(SubscriptionGroupV2DTO sellingPlanGroupToUpdate, SubscriptionGroupV2DTO sellingPlanGroupWithAppstleData) {

        sellingPlanGroupToUpdate.getSubscriptionPlans().forEach(sellingPlanToUpdate -> {

            Optional<FrequencyInfoDTO> sellingPlanWithAppstleData = sellingPlanGroupWithAppstleData.getSubscriptionPlans()
                .stream()
                .filter(asp -> sellingPlanToUpdate.getId().equalsIgnoreCase(asp.getId()) || sellingPlanToUpdate.getFrequencyName().equalsIgnoreCase(asp.getFrequencyName()))
                .findFirst();

            if (sellingPlanWithAppstleData.isPresent()) {
                updateAppstleDataForSellingPlan(sellingPlanToUpdate, sellingPlanWithAppstleData.get());
            }
        });
    }

    private void updateAppstleDataForSellingPlan(FrequencyInfoDTO sellingPlanToUpdate, FrequencyInfoDTO sellingPlanWithAppstleData) {

        sellingPlanToUpdate.setFreeTrialEnabled(sellingPlanWithAppstleData.isFreeTrialEnabled());
        sellingPlanToUpdate.setFreeTrialCount(sellingPlanWithAppstleData.getFreeTrialCount());
        sellingPlanToUpdate.setFreeTrialInterval(sellingPlanWithAppstleData.getFreeTrialInterval());
        sellingPlanToUpdate.setAppstleCycles(sellingPlanWithAppstleData.getAppstleCycles());
        sellingPlanToUpdate.setMemberOnly(sellingPlanWithAppstleData.getMemberOnly());
        sellingPlanToUpdate.setNonMemberOnly(sellingPlanWithAppstleData.getNonMemberOnly());
        sellingPlanToUpdate.setMemberInclusiveTags(sellingPlanWithAppstleData.getMemberInclusiveTags());
        sellingPlanToUpdate.setMemberExclusiveTags(sellingPlanWithAppstleData.getMemberExclusiveTags());
        sellingPlanToUpdate.setFormFieldJson(sellingPlanWithAppstleData.getFormFieldJson());
        sellingPlanToUpdate.setUpcomingOrderEmailBuffer(sellingPlanWithAppstleData.getUpcomingOrderEmailBuffer());
        sellingPlanToUpdate.setFrequencySequence(sellingPlanWithAppstleData.getFrequencySequence());
    }

    private DiscountDetails buildDiscountDetails(SellingPlanGroupQuery.PricingPolicy pricingPolicy) {
        SellingPlanPricingPolicyAdjustmentType adjustmentType = null;
        DiscountDetails discountDetails = new DiscountDetails();

        if (pricingPolicy instanceof SellingPlanGroupQuery.AsSellingPlanFixedPricingPolicy) {
            SellingPlanGroupQuery.AsSellingPlanFixedPricingPolicy pricingPolicyBase = (SellingPlanGroupQuery.AsSellingPlanFixedPricingPolicy) pricingPolicy;
            SellingPlanGroupQuery.AdjustmentValue adjustmentValue = pricingPolicyBase.getAdjustmentValue();
            adjustmentType = pricingPolicyBase.getAdjustmentType();

            if (adjustmentType == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE) {
                SellingPlanGroupQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue1 = (SellingPlanGroupQuery.AsSellingPlanPricingPolicyPercentageValue) adjustmentValue;
                double percentage = adjustmentValue1.getPercentage();
                discountDetails.setDiscountType(DiscountTypeUnit.PERCENTAGE);
                discountDetails.setDiscountOffer(percentage);
            } else if (adjustmentType == SellingPlanPricingPolicyAdjustmentType.PRICE) {
                SellingPlanGroupQuery.AsMoneyV2 adjustmentValue1 = (SellingPlanGroupQuery.AsMoneyV2) adjustmentValue;
                discountDetails.setDiscountType(DiscountTypeUnit.PRICE);
                discountDetails.setDiscountOffer(Double.parseDouble(adjustmentValue1.getAmount().toString()));
            } else {
                SellingPlanGroupQuery.AsMoneyV2 adjustmentValue1 = (SellingPlanGroupQuery.AsMoneyV2) adjustmentValue;
                discountDetails.setDiscountType(DiscountTypeUnit.FIXED);
                discountDetails.setDiscountOffer(Double.parseDouble(adjustmentValue1.getAmount().toString()));
            }

            discountDetails.setAfterCycle(0);
        } else {
            SellingPlanGroupQuery.AsSellingPlanRecurringPricingPolicy pricingPolicyBase = (SellingPlanGroupQuery.AsSellingPlanRecurringPricingPolicy) pricingPolicy;
            SellingPlanGroupQuery.AdjustmentValue1 adjustmentValue = pricingPolicyBase.getAdjustmentValue();
            adjustmentType = pricingPolicyBase.getAdjustmentType();

            if (adjustmentType == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE) {
                SellingPlanGroupQuery.AsSellingPlanPricingPolicyPercentageValue1 adjustmentValue1 = (SellingPlanGroupQuery.AsSellingPlanPricingPolicyPercentageValue1) adjustmentValue;
                double percentage = adjustmentValue1.getPercentage();
                discountDetails.setDiscountType(DiscountTypeUnit.PERCENTAGE);
                discountDetails.setDiscountOffer(percentage);
            } else if (adjustmentType == SellingPlanPricingPolicyAdjustmentType.PRICE) {
                SellingPlanGroupQuery.AsMoneyV21 adjustmentValue1 = (SellingPlanGroupQuery.AsMoneyV21) adjustmentValue;
                discountDetails.setDiscountType(DiscountTypeUnit.PRICE);
                discountDetails.setDiscountOffer(Double.parseDouble(adjustmentValue1.getAmount().toString()));
            } else {
                SellingPlanGroupQuery.AsMoneyV21 adjustmentValue1 = (SellingPlanGroupQuery.AsMoneyV21) adjustmentValue;
                discountDetails.setDiscountType(DiscountTypeUnit.FIXED);
                discountDetails.setDiscountOffer(Double.parseDouble(adjustmentValue1.getAmount().toString()));
            }

            discountDetails.setAfterCycle(pricingPolicyBase.getAfterCycle().orElse(0));
        }

        return discountDetails;
    }

    @Override
    public ProductData getSellingGroupProducts(ShopifyGraphqlClient shopifyGraphqlClient, Long sellingPlanGroupId, boolean next, String cursor) throws Exception {

        ProductData productData = new ProductData();

        List<Product> products = new ArrayList<>();

        boolean hasNextPage = true;

        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);
        pageInfo.setCursor(cursor);

        while(hasNextPage) {

            SellingPlanGroupProductsQuery sellingPlanGroupProductsQuery = new SellingPlanGroupProductsQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId,
                next ? Input.optional(cursor) : Input.optional(null));
            Response<Optional<SellingPlanGroupProductsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupProductsQuery);
            List<Product> collect = convertProducts(response);
            products.addAll(collect);
            if (response.getData().isPresent()) {
                hasNextPage = response.getData().get().getSellingPlanGroup().get().getProducts().getPageInfo().isHasNextPage();
            }
            if(hasNextPage) {
                Optional<SellingPlanGroupProductsQuery.Edge> last = response.getData()
                    .flatMap(SellingPlanGroupProductsQuery.Data::getSellingPlanGroup)
                    .map(SellingPlanGroupProductsQuery.SellingPlanGroup::getProducts)
                    .map(SellingPlanGroupProductsQuery.Products::getEdges)
                    .orElse(new ArrayList<>()).stream().reduce((first, second) -> second);
                if(last.isPresent()) {
                    cursor = last.get().getCursor();
                }
            }
        }

        productData.setProducts(products);
        productData.setPageInfo(pageInfo);

        return productData;
    }

    @Override
    public ProductData getSellingGroupProductVariants(ShopifyGraphqlClient shopifyGraphqlClient, Long sellingPlanGroupId, boolean next, String cursor) throws Exception {

        ProductData productData = new ProductData();
        List<Product> products = new ArrayList<>();

        boolean hasNextPage = true;

        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);
        pageInfo.setCursor(cursor);

        while (hasNextPage) {
            SellingPlanGroupProductVariantsQuery sellingPlanGroupProductsQuery = new SellingPlanGroupProductVariantsQuery(SELLING_PLAN_GROUP_ID_PREFIX + sellingPlanGroupId,
                next ? Input.optional(cursor) : Input.optional(null));

            Response<Optional<SellingPlanGroupProductVariantsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupProductsQuery);

            List<Product> collect = convertProductVariants(response);
            products.addAll(collect);
            if (response.getData().isPresent()) {
                hasNextPage = response.getData().get().getSellingPlanGroup().get().getProductVariants().getPageInfo().isHasNextPage();
            }

            if (hasNextPage) {
                Optional<SellingPlanGroupProductVariantsQuery.Edge> last = response.getData()
                    .flatMap(SellingPlanGroupProductVariantsQuery.Data::getSellingPlanGroup)
                    .map(SellingPlanGroupProductVariantsQuery.SellingPlanGroup::getProductVariants)
                    .map(SellingPlanGroupProductVariantsQuery.ProductVariants::getEdges)
                    .orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                if(last.isPresent()) {
                    cursor = last.get().getCursor();
                }
            }
        }
        productData.setProducts(products);
        productData.setPageInfo(pageInfo);
        return productData;
    }

    private List<Product> convertProducts(Response<Optional<SellingPlanGroupProductsQuery.Data>> response) {
        return response.getData()
            .flatMap(SellingPlanGroupProductsQuery.Data::getSellingPlanGroup)
            .map(SellingPlanGroupProductsQuery.SellingPlanGroup::getProducts)
            .map(SellingPlanGroupProductsQuery.Products::getEdges)
            .orElse(new ArrayList<>()).stream()
            .map(SellingPlanGroupProductsQuery.Edge::getNode)
            .map(node -> {
                Product product = new Product();
                product.setTitle(node.getTitle());
                if (!ListUtils.isEmpty(node.getTags())) {
                    product.setTags(gson.toJson(node.getTags()));
                }
                product.setVendor(node.getVendor());
                product.setProductType(node.getProductType());
                product.setPrice(Double.parseDouble(node.getPriceRangeV2().getMinVariantPrice().getAmount().toString()));
                product.setId(Long.parseLong(node.getId().replace(PRODUCT_ID_PREFIX, "")));
                product.setHandle(node.getHandle());
                product.setStatus(node.getStatus());
                if (node.getFeaturedImage().isPresent()) {
                    String imageId = node.getFeaturedImage().get().getId().get()
                        .replace(PRODUCT_IMAGE_ID_PREFIX, "");
                    product.setImageId(Long.parseLong(imageId));
                    product.setImageSrc(node.getFeaturedImage().get().getTransformedSrc().toString());
                }
                return product;
            }).collect(Collectors.toList());
    }
    private List<Product> convertProductVariants(Response<Optional<SellingPlanGroupProductVariantsQuery.Data>> response) {
        return response.getData()
            .flatMap(SellingPlanGroupProductVariantsQuery.Data::getSellingPlanGroup)
            .map(SellingPlanGroupProductVariantsQuery.SellingPlanGroup::getProductVariants)
            .map(SellingPlanGroupProductVariantsQuery.ProductVariants::getEdges)
            .orElse(new ArrayList<>()).stream()
            .map(SellingPlanGroupProductVariantsQuery.Edge::getNode)
            .map(node -> {
                Product product = new Product();
                product.setTitle(node.getDisplayName());
                product.setHandle(node.getProduct().getHandle());
                product.setId(Long.parseLong(node.getId().replace(PRODUCT_VARIANT_ID_PREFIX, "")));
                return product;
            }).collect(Collectors.toList());
    }
    @Override
    public FrequencyIntervalUnit buildFrequencyInterval(SellingPlanInterval interval) {
        if (interval == SellingPlanInterval.DAY) {
            return FrequencyIntervalUnit.DAY;
        } else if (interval == SellingPlanInterval.WEEK) {
            return FrequencyIntervalUnit.WEEK;
        } else if (interval == SellingPlanInterval.MONTH) {
            return FrequencyIntervalUnit.MONTH;
        } else {
            return FrequencyIntervalUnit.YEAR;
        }
    }


    @Override
    public Optional<SellingPlanGroupQuery.SellingPlanGroup> getSingleSubscriptionGroupRaw(String shop, String sellingPlanGroupId) throws Exception {

        logger.info("{} Calling shopify graphql for load subscription groups", sellingPlanGroupId);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        SellingPlanGroupQuery sellingPlanGroupQuery = new SellingPlanGroupQuery(sellingPlanGroupId);

        Response<Optional<SellingPlanGroupQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);
        Optional<SellingPlanGroupQuery.SellingPlanGroup> sellingPlanGroup = response.getData().flatMap(SellingPlanGroupQuery.Data::getSellingPlanGroup);
        return sellingPlanGroup;
    }


    private SubscriptionGroupDTO buildSubscriptionGroupDTO(SellingPlanGroup sellingPlanGroup) throws JsonProcessingException {
        SubscriptionGroupDTO subscriptionGroupDTO = new SubscriptionGroupDTO();
        populateDiscountDetails(sellingPlanGroup, subscriptionGroupDTO);

        subscriptionGroupDTO.setId(sellingPlanGroup.getId());
        populateFrequencyInfo(sellingPlanGroup, subscriptionGroupDTO);
        subscriptionGroupDTO.setGroupName(sellingPlanGroup.getGroupName());
        subscriptionGroupDTO.setProductCount((long) sellingPlanGroup.getProductCount());

        ProductData productData = sellingPlanGroup.getProductData();

        if (productData != null) {
            List<SubscribedProductVariantInfo> collect = productData.getProducts().stream().map(p -> {
                SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                subscribedProductVariantInfo.setId(p.getId());
                subscribedProductVariantInfo.setTitle(p.getTitle());
                return subscribedProductVariantInfo;
            }).collect(Collectors.toList());
            subscriptionGroupDTO.setProductIds(OBJECT_MAPPER.writeValueAsString(collect));
        }

        return subscriptionGroupDTO;
    }

    /**
     * Get all subscriptionGroup products.
     *
     * @param shop           the shop of the entity.
     * @param sellingGroupId the title search text
     * @param next           to load next values
     * @param cursor         from which cursor to load
     * @return the list of products.
     */
    @Override
    public ProductData findSubscriptionGroupProducts(String shop, Long sellingGroupId, boolean next, String cursor) {
        logger.info("{} Calling shopify graphql for get selling group products", shop);
        return null;//shopifyGraphqlSellingPlanGroupService.getSellingGroupProducts(prepareShopifyGraphqlClient(shop), sellingGroupId, next, cursor);
    }

    /**
     * Delete the "id" subscriptionGroup.
     *
     * @param shop           the id of the entity.
     * @param sellingGroupId the id of the entity.
     */
    @Override
    public void delete(String shop, Long sellingGroupId) throws Exception {
        logger.info("{} Calling shopify graphql for delete selling group", shop);

        shopifyGraphqlSellingPlanGroupService.deleteSellingPlanGroup(commonUtils.prepareShopifyGraphqlClient(shop), shop, sellingGroupId);
        SubscriptionGroupPlan subscriptionGroupPlan = subscriptionGroupPlanRepository.findByShopAndSubscriptionId(shop, sellingGroupId);
        if (subscriptionGroupPlan != null) {
            subscriptionGroupPlanRepository.delete(subscriptionGroupPlan);
            List<SellingPlanMemberInfo> sellingPlanMemberInfos = sellingPlanMemberInfoRepository.findAllByShopAndSubscriptionId(shop, sellingGroupId);
            if (sellingPlanMemberInfos.size() > 0) {
                sellingPlanMemberInfoRepository.deleteAll(sellingPlanMemberInfos);
                subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
            }
            List<SubscriptionBundling> subscriptionBundlings = subscriptionBundlingRepository.findByShopAndSubscriptionId(shop, sellingGroupId);
            if (!CollectionUtils.isEmpty(subscriptionBundlings)) {
                subscriptionBundlingRepository.deleteAll(subscriptionBundlings);
            }
        }
    }

    @Override
    public List<FrequencyInfoDTO> getAllSellingPlans(String shopName) {
        List<FrequencyInfoDTO> allSellingPlans = new ArrayList<>();
        try {
            List<SubscriptionGroupPlanDTO> subscriptionGroups = subscriptionGroupPlanService.findByShop(shopName);

            if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionGroups)) {
                for (SubscriptionGroupPlanDTO subscriptionGroupPlan : subscriptionGroups) {
                    SubscriptionGroupV2DTO subscriptionGroupV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionGroupPlan.getInfoJson());
                    if (subscriptionGroupV2DTO != null && !com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionGroupV2DTO.getSubscriptionPlans())) {
                        subscriptionGroupV2DTO.getSubscriptionPlans().sort(Comparator.comparing(FrequencyInfoDTO::getFrequencySequence, Comparator.nullsLast(Integer::compareTo)));
                        subscriptionGroupV2DTO.setSubscriptionPlans(subscriptionGroupV2DTO.getSubscriptionPlans().stream().peek(frequencyInfoDTO -> frequencyInfoDTO.setGroupName(subscriptionGroupV2DTO.getGroupName())).collect(Collectors.toList()));
                        allSellingPlans.addAll(subscriptionGroupV2DTO.getSubscriptionPlans());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while getting all selling plans, Message: {}", e.getMessage());
        }
        return allSellingPlans;
    }

    @Override
    public FrequencyInfoDTO getSellingPlanById(String shopName, Long sellingPlanId) {
        try {
            List<SubscriptionGroupPlanDTO> subscriptionGroups = subscriptionGroupPlanService.findByShop(shopName);

            if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionGroups)) {
                for (SubscriptionGroupPlanDTO subscriptionGroupPlan : subscriptionGroups) {
                    SubscriptionGroupV2DTO subscriptionGroupV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionGroupPlan.getInfoJson());
                    if (subscriptionGroupV2DTO != null && !com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionGroupV2DTO.getSubscriptionPlans())) {
                        Optional<FrequencyInfoDTO> frequencyInfoDTO = subscriptionGroupV2DTO.getSubscriptionPlans().stream().filter(plan -> plan.getId().equals(ShopifyGraphQLUtils.getGraphQLSellingPlanId(sellingPlanId))).findFirst();
                        if(frequencyInfoDTO.isPresent()){
                            return frequencyInfoDTO.get();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while getting all selling plans, Message: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId, Integer billingFrequencyCount, SellingPlanInterval billingSellingPlanInterval) {
        FrequencyIntervalUnit billingFrequencyInterval = buildFrequencyInterval(billingSellingPlanInterval);

        return findSellingPlansForProductVariant(shop, productId, variantId, billingFrequencyCount, billingFrequencyInterval);
    }

    @Override
    public List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId, Integer billingFrequencyCount, FrequencyIntervalUnit billingFrequencyInterval) {
        List<FrequencyInfoDTO> allSellingPlans = new ArrayList<>();
        try {
            allSellingPlans = findSellingPlansForProductVariant(shop, productId, variantId);

            allSellingPlans = allSellingPlans.stream()
                .filter(sp -> sp.getBillingFrequencyInterval().equals(billingFrequencyInterval) && Objects.equals(sp.getBillingFrequencyCount(), billingFrequencyCount))
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error while getting selling plans productId={}, variantId={}, Message: {}", productId, variantId, e.getMessage());
        }
        return allSellingPlans;
    }

    @Override
    public List<FrequencyInfoDTO> findSellingPlansForProductVariant(String shop, Long productId, Long variantId) {
        List<FrequencyInfoDTO> allSellingPlans = new ArrayList<>();
        try {
            List<SubscriptionGroupPlan> subscriptionGroups = subscriptionGroupPlanRepository.findByVariantIdOrProductId(shop, variantId, productId);

            if (!CollectionUtils.isEmpty(subscriptionGroups)) {
                for (SubscriptionGroupPlan subscriptionGroupPlan : subscriptionGroups) {
                    SubscriptionGroupV2DTO subscriptionGroupV2DTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionGroupPlan.getInfoJson());
                    if (subscriptionGroupV2DTO != null && !CollectionUtils.isEmpty(subscriptionGroupV2DTO.getSubscriptionPlans())) {
                        subscriptionGroupV2DTO.setSubscriptionPlans(subscriptionGroupV2DTO.getSubscriptionPlans().stream().peek(frequencyInfoDTO -> frequencyInfoDTO.setGroupName(subscriptionGroupV2DTO.getGroupName())).collect(Collectors.toList()));
                        allSellingPlans.addAll(subscriptionGroupV2DTO.getSubscriptionPlans());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while getting selling plans productId={}, variantId={}, Message: {}", productId, variantId, e.getMessage());
        }
        return allSellingPlans;
    }

    @Async
    @Override
    public void exportSubscriptionGroups(String shop, String email) {

        logger.info("Start exporting subscription group plans for shop: {}", shop);
        File tempFile = null;
        try {
            String[] headers = {
                "Selling Plan Group ID",
                "Selling Plan Group Name",
                "Product ID",
                "Product Name",
                "Variant ID",
                "Variant Name"
            };
            tempFile = File.createTempFile("Subscription-Groups-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                List<SubscriptionGroupV2DTO> subscriptionGroupV2DTOList = subscriptionGroupPlanService.getAllSubscriptionGroupPlan(shop);

                if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionGroupV2DTOList)) {
                    logger.info("Total group plans:" + subscriptionGroupV2DTOList.size());
                    ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                    for (SubscriptionGroupV2DTO subscriptionGroupV2DTO : subscriptionGroupV2DTOList) {
                        try {
                            List<ProductVariantNodesQuery.AsProductVariant> productVariantList = new ArrayList<>();

                            List<String> variantIds = getVariantIds(subscriptionGroupV2DTO);

                            ProductVariantNodesQuery productVariantNodesQuery = new ProductVariantNodesQuery(variantIds);

                            Response<Optional<ProductVariantNodesQuery.Data>> optionalResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantNodesQuery);

                            if (!optionalResponse.hasErrors() && optionalResponse.getData().isPresent()) {
                                productVariantList = optionalResponse.getData().get().getNodes().stream()
                                    .map(node -> ((ProductVariantNodesQuery.AsProductVariant) node)).collect(Collectors.toList());
                            }
                            List<Map<String, String>> products = OBJECT_MAPPER.readValue(subscriptionGroupV2DTO.getProductIds(), new TypeReference<List<Map<String, String>>>() {
                            });
                            List<Map<String, String>> variants = OBJECT_MAPPER.readValue(subscriptionGroupV2DTO.getVariantIds(), new TypeReference<List<Map<String, String>>>() {
                            });

                            for (Map<String, String> product : products) {
                                try {
                                    csvPrinter.printRecord(
                                        subscriptionGroupV2DTO.getId(),
                                        subscriptionGroupV2DTO.getGroupName(),
                                        product.get("id"),
                                        product.get("title"),
                                        "",
                                        ""
                                    );
                                } catch (Exception e) {
                                    logger.error("ex=" + ExceptionUtils.getStackTrace(e));
                                }
                            }

                            for (Map<String, String> variant : variants) {
                                try {
                                    ProductVariantNodesQuery.AsProductVariant productVariant = productVariantList.stream().filter(v -> v.getId().equals(ShopifyGraphQLUtils.getGraphQLVariantId(variant.get("id")))).findFirst().orElse(null);
                                    csvPrinter.printRecord(
                                        subscriptionGroupV2DTO.getId(),
                                        subscriptionGroupV2DTO.getGroupName(),
                                        productVariant != null ? ShopifyGraphQLUtils.getProductId(productVariant.getProduct().getId()) : "",
                                        productVariant != null ? productVariant.getProduct().getTitle() : "",
                                        variant.get("id"),
                                        productVariant != null ? productVariant.getTitle() : variant.get("title")
                                    );
                                } catch (Exception e) {
                                    logger.error("ex=" + ExceptionUtils.getStackTrace(e));
                                }
                            }
                        } catch (Exception e) {
                            logger.error("ex=" + ExceptionUtils.getStackTrace(e));
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Subscription Group plans Exported ", "Check attached csv file for your all subscription group plans products.", "subscription-support@appstle.com", shop, email);

        logger.info("Subscription group plan export for shop: {}, Completed", shop);
    }
}
