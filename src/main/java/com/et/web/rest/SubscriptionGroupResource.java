package com.et.web.rest;

import com.amazonaws.util.CollectionUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.data.ProductData;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.domain.enumeration.DiscountTypeUnit;
import com.et.domain.enumeration.FrequencyIntervalUnit;
import com.et.pojo.SubscriptionGroupProductDTO;
import com.et.security.SecurityUtils;
import com.et.service.SubscriptionGroupPlanService;
import com.et.service.SubscriptionGroupService;
import com.et.service.dto.*;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.AddPlansToProduct;
import com.et.web.rest.vm.RemovePlansToProduct;
import com.et.web.rest.vm.SubscribedProductVariantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.SellingPlanRecurringDeliveryPolicyPreAnchorBehavior;
import org.springframework.beans.factory.annotation.Qualifier;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.PRODUCT_ID_PREFIX;
import static com.et.api.constants.ShopifyIdPrefix.SELLING_PLAN_GROUP_ID_PREFIX;

/**
 * REST controller for managing {@link com.et.api.graphql.pojo.SellingPlanGroup}.
 */
@RestController
public class SubscriptionGroupResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionGroupResource.class);

    private static final String ENTITY_NAME = "subscriptionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionGroupService subscriptionGroupService;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor asyncExecutor;

    private final CommonUtils commonUtils;

    public SubscriptionGroupResource(SubscriptionGroupService subscriptionGroupService, CommonUtils commonUtils) {
        this.subscriptionGroupService = subscriptionGroupService;
        this.commonUtils = commonUtils;
    }

    @PostMapping("/api/v2/subscription-groups")
    public ResponseEntity<SubscriptionGroupV2DTO> createSubscriptionGroupV2(@Valid @RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO) throws Exception {
        log.debug("REST request to save SubscriptionGroup : {}", subscriptionGroupDTO);

        return createSubscriptionGroup(subscriptionGroupDTO);
    }
    @CrossOrigin
    @ApiOperation("Create selling plan group")
    @PostMapping("/api/external/v2/subscription-groups")
    public ResponseEntity<SubscriptionGroupV2DTO> createSubscriptionGroupV2External(@Valid @ApiParam("Subscription Group Plan Data Model") @RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO,
                                                                                    @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                                    HttpServletRequest request) throws Exception {

        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 to save SubscriptionGroup RequestURL: {} /external/v2/subscription-groups api_key: {}", RequestURL, apiKey);

        return createSubscriptionGroup(subscriptionGroupDTO);

    }

    private ResponseEntity<SubscriptionGroupV2DTO> createSubscriptionGroup(SubscriptionGroupV2DTO subscriptionGroupDTO) throws Exception {
        String shop = commonUtils.getShop();

        if (subscriptionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }

        SubscriptionGroupV2DTO subscriptionGroupDTONew = new SubscriptionGroupV2DTO();
        subscriptionGroupDTONew.setGroupName(subscriptionGroupDTO.getGroupName());

        ArrayList<FrequencyInfoDTO> subscriptionPlans = new ArrayList<>();
        int index = 0;
        for (FrequencyInfoDTO subscriptionPlan : subscriptionGroupDTO.getSubscriptionPlans()) {
            FrequencyInfoDTO frequencyInfoDTO = new FrequencyInfoDTO();
            frequencyInfoDTO.setFrequencyName(subscriptionPlan.getFrequencyName());
            frequencyInfoDTO.setFrequencyDescription(subscriptionPlan.getFrequencyDescription());
            frequencyInfoDTO.setFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            frequencyInfoDTO.setFrequencyCount(subscriptionPlan.getFrequencyCount());
            frequencyInfoDTO.setMemberOnly(subscriptionPlan.getMemberOnly());
            frequencyInfoDTO.setNonMemberOnly(subscriptionPlan.getNonMemberOnly());
            frequencyInfoDTO.setMemberInclusiveTags(subscriptionPlan.getMemberInclusiveTags());
            frequencyInfoDTO.setMemberExclusiveTags(subscriptionPlan.getMemberExclusiveTags());
            frequencyInfoDTO.setFormFieldJson(subscriptionPlan.getFormFieldJson());

            if (shop.equalsIgnoreCase("masarap-box.myshopify.com") || shop.equalsIgnoreCase("test-subscription-v152.myshopify.com")) {
                frequencyInfoDTO.setDeliveryPolicyPreAnchorBehavior(SellingPlanRecurringDeliveryPolicyPreAnchorBehavior.NEXT);
            }

            if (subscriptionPlan.getBillingFrequencyCount() == null || subscriptionPlan.getPlanType().equals(PlanType.PAY_AS_YOU_GO)) {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            } else {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getBillingFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getBillingFrequencyInterval());
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled())) {
                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setDiscountType(Optional.ofNullable(subscriptionPlan.getDiscountType()).orElse(DiscountTypeUnit.PERCENTAGE));
                frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                frequencyInfoDTO.setAfterCycle1(Optional.ofNullable(subscriptionPlan.getAfterCycle1()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled(false);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled2())) {
                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setDiscountType2(Optional.ofNullable(subscriptionPlan.getDiscountType2()).orElse(DiscountTypeUnit.PERCENTAGE));
                frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                frequencyInfoDTO.setAfterCycle2(Optional.ofNullable(subscriptionPlan.getAfterCycle2()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled2(false);
            }

            if(BooleanUtils.isTrue(subscriptionPlan.isFreeTrialEnabled())) {
                frequencyInfoDTO.setFreeTrialEnabled(true);
                frequencyInfoDTO.setFreeTrialCount(subscriptionPlan.getFreeTrialCount());
                frequencyInfoDTO.setFreeTrialInterval(Optional.ofNullable(subscriptionPlan.getFreeTrialInterval()).orElse(FrequencyIntervalUnit.DAY));

                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setAfterCycle1(0);
                if(subscriptionPlan.getDiscountOffer() != null) {
                    frequencyInfoDTO.setDiscountType(Optional.ofNullable(subscriptionPlan.getDiscountType()).orElse(DiscountTypeUnit.PERCENTAGE));
                    frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                } else {
                    frequencyInfoDTO.setDiscountType(DiscountTypeUnit.PERCENTAGE);
                    frequencyInfoDTO.setDiscountOffer(100d);
                }

                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setAfterCycle2(1);
                if(subscriptionPlan.getDiscountOffer2() != null) {
                    frequencyInfoDTO.setDiscountType2(Optional.ofNullable(subscriptionPlan.getDiscountType2()).orElse(DiscountTypeUnit.PERCENTAGE));
                    frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                } else {
                    frequencyInfoDTO.setDiscountType2(DiscountTypeUnit.PERCENTAGE);
                    frequencyInfoDTO.setDiscountOffer2(0d);
                }
            } else {
                frequencyInfoDTO.setFreeTrialEnabled(false);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayValue(subscriptionPlan.getSpecificDayValue());
                frequencyInfoDTO.setSpecificMonthValue(subscriptionPlan.getSpecificMonthValue());
                frequencyInfoDTO.setCutOff(subscriptionPlan.getCutOff());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_PURCHASE_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else {
                frequencyInfoDTO.setSpecificDayEnabled(false);
            }

            frequencyInfoDTO.setMaxCycles(subscriptionPlan.getMaxCycles());
            frequencyInfoDTO.setMinCycles(subscriptionPlan.getMinCycles());

            if (subscriptionPlan.getUpcomingOrderEmailBuffer() != null) {
                frequencyInfoDTO.setUpcomingOrderEmailBuffer(subscriptionPlan.getUpcomingOrderEmailBuffer());
            }

            frequencyInfoDTO.setFrequencySequence(index);

            subscriptionPlans.add(frequencyInfoDTO);
            index++;
        }

        subscriptionGroupDTONew.setSubscriptionPlans(subscriptionPlans);

        List<String> productIds = getProductIds(subscriptionGroupDTO);

        List<String> variantIds = getVariantIds(subscriptionGroupDTO);

        SubscriptionGroupV2DTO result = subscriptionGroupService.saveSubscriptionGroupV2(subscriptionGroupDTONew, shop, productIds, variantIds);

        return ResponseEntity.created(new URI("/api/subscription-groups/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "New Subscription Plan Created.", ""))
            .body(result);
    }

    @PostMapping("/api/shopify/v2/subscription-groups")
    @CrossOrigin
    public ResponseEntity<SubscriptionGroupV2DTO> shopifyCreateSubscriptionGroupV2(@RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO, HttpServletRequest request) throws Exception {
        log.debug("REST request to save SubscriptionGroup : {}", subscriptionGroupDTO);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);

        SubscriptionGroupV2DTO subscriptionGroupDTONew = new SubscriptionGroupV2DTO();
        subscriptionGroupDTONew.setGroupName(subscriptionGroupDTO.getGroupName());

        ArrayList<FrequencyInfoDTO> subscriptionPlans = new ArrayList<>();

        int index = 0;
        for (FrequencyInfoDTO subscriptionPlan : subscriptionGroupDTO.getSubscriptionPlans()) {
            FrequencyInfoDTO frequencyInfoDTO = new FrequencyInfoDTO();
            frequencyInfoDTO.setFrequencyName(subscriptionPlan.getFrequencyName());
            frequencyInfoDTO.setFrequencyDescription(subscriptionPlan.getFrequencyDescription());
            frequencyInfoDTO.setFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            frequencyInfoDTO.setFrequencyCount(subscriptionPlan.getFrequencyCount());
            frequencyInfoDTO.setMemberOnly(subscriptionPlan.getMemberOnly());
            frequencyInfoDTO.setNonMemberOnly(subscriptionPlan.getNonMemberOnly());
            frequencyInfoDTO.setMemberInclusiveTags(subscriptionPlan.getMemberInclusiveTags());
            frequencyInfoDTO.setMemberExclusiveTags(subscriptionPlan.getMemberExclusiveTags());
            frequencyInfoDTO.setFormFieldJson(subscriptionPlan.getFormFieldJson());

            if (shop.equalsIgnoreCase("masarap-box.myshopify.com") || shop.equalsIgnoreCase("test-subscription-v152.myshopify.com")) {
                frequencyInfoDTO.setDeliveryPolicyPreAnchorBehavior(SellingPlanRecurringDeliveryPolicyPreAnchorBehavior.NEXT);
            }

            if (subscriptionPlan.getBillingFrequencyCount() == null || subscriptionPlan.getPlanType().equals(PlanType.PAY_AS_YOU_GO)) {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            } else {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getBillingFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getBillingFrequencyInterval());
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled())) {
                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setDiscountType(subscriptionPlan.getDiscountType());
                frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                frequencyInfoDTO.setAfterCycle1(Optional.ofNullable(subscriptionPlan.getAfterCycle1()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled(false);
            }


            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled2())) {
                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setDiscountType2(subscriptionPlan.getDiscountType2());
                frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                frequencyInfoDTO.setAfterCycle2(subscriptionPlan.getAfterCycle2());
            } else {
                frequencyInfoDTO.setDiscountEnabled2(false);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayValue(subscriptionPlan.getSpecificDayValue());
                frequencyInfoDTO.setSpecificMonthValue(subscriptionPlan.getSpecificMonthValue());
                frequencyInfoDTO.setCutOff(subscriptionPlan.getCutOff());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_PURCHASE_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else {
                frequencyInfoDTO.setSpecificDayEnabled(false);
            }

            frequencyInfoDTO.setMaxCycles(subscriptionPlan.getMaxCycles());
            frequencyInfoDTO.setMinCycles(subscriptionPlan.getMinCycles());

            if (subscriptionPlan.getUpcomingOrderEmailBuffer() != null) {
                frequencyInfoDTO.setUpcomingOrderEmailBuffer(subscriptionPlan.getUpcomingOrderEmailBuffer());
            }
            frequencyInfoDTO.setFrequencySequence(index);
            subscriptionPlans.add(frequencyInfoDTO);
            index++;
        }

        subscriptionGroupDTONew.setSubscriptionPlans(subscriptionPlans);

        List<String> productIds = new ArrayList<>();
        productIds.add(subscriptionGroupDTO.getProductId());

        List<String> variantIds = new ArrayList<>();

        SubscriptionGroupV2DTO result = subscriptionGroupService.saveSubscriptionGroupV2(subscriptionGroupDTONew, shop, productIds, variantIds);

        return ResponseEntity.created(new URI("/api/subscription-groups/" + result.getId())).body(result);
    }

    @PutMapping("/api/v2/subscription-groups")
    public ResponseEntity<SubscriptionGroupV2DTO> updateSubscriptionGroupV2(@Valid @RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO) throws Exception {
        log.debug("REST request to update SubscriptionGroup : {}", subscriptionGroupDTO);
        if (subscriptionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();

        SubscriptionGroupV2DTO subscriptionGroupDTONew = new SubscriptionGroupV2DTO();
        subscriptionGroupDTONew.setGroupName(subscriptionGroupDTO.getGroupName());

        long sellingPlanGroupId = subscriptionGroupDTO.getId();

        subscriptionGroupDTONew.setId(sellingPlanGroupId);

        ArrayList<FrequencyInfoDTO> subscriptionPlans = buildFrequencyInfoList(subscriptionGroupDTO, shop);

        subscriptionGroupDTONew.setSubscriptionPlans(subscriptionPlans);

        List<String> productIds = getProductIds(subscriptionGroupDTO);

        List<String> variantIds = getVariantIds(subscriptionGroupDTO);

        SubscriptionGroupV2DTO result = subscriptionGroupService.updateSubscriptionGroupV2(subscriptionGroupDTONew, shop, productIds, variantIds);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Updated.", ""))
            .body(result);
    }

    @PutMapping("/api/v2/subscription-groups/details")
    public ResponseEntity<SubscriptionGroupV2DTO> updateSubscriptionGroupDetails(@Valid @RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO) throws Exception {
        log.debug("REST request to update SubscriptionGroup : {}", subscriptionGroupDTO);
        if (subscriptionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = commonUtils.getShop();

        SubscriptionGroupV2DTO subscriptionGroupDTONew = new SubscriptionGroupV2DTO();
        subscriptionGroupDTONew.setGroupName(subscriptionGroupDTO.getGroupName());

        long sellingPlanGroupId = subscriptionGroupDTO.getId();

        subscriptionGroupDTONew.setId(sellingPlanGroupId);

        ArrayList<FrequencyInfoDTO> subscriptionPlans = buildFrequencyInfoList(subscriptionGroupDTO, shop);

        subscriptionGroupDTONew.setSubscriptionPlans(subscriptionPlans);

        SubscriptionGroupV2DTO result = subscriptionGroupService.updateSubscriptionGroupDetails(subscriptionGroupDTONew, shop);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Updated.", ""))
            .body(result);
    }

    private ArrayList<FrequencyInfoDTO> buildFrequencyInfoList(SubscriptionGroupV2DTO subscriptionGroupDTO, String shop) {
        ArrayList<FrequencyInfoDTO> subscriptionPlans = new ArrayList<>();
        int index = 0;
        for (FrequencyInfoDTO subscriptionPlan : subscriptionGroupDTO.getSubscriptionPlans()) {
            FrequencyInfoDTO frequencyInfoDTO = new FrequencyInfoDTO();
            frequencyInfoDTO.setFrequencyName(subscriptionPlan.getFrequencyName());
            frequencyInfoDTO.setFrequencyDescription(subscriptionPlan.getFrequencyDescription());
            frequencyInfoDTO.setFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            frequencyInfoDTO.setFrequencyCount(subscriptionPlan.getFrequencyCount());
            frequencyInfoDTO.setId(subscriptionPlan.getIdNew());
            frequencyInfoDTO.setMemberOnly(subscriptionPlan.getMemberOnly());
            frequencyInfoDTO.setNonMemberOnly(subscriptionPlan.getNonMemberOnly());
            frequencyInfoDTO.setMemberInclusiveTags(subscriptionPlan.getMemberInclusiveTags());
            frequencyInfoDTO.setMemberExclusiveTags(subscriptionPlan.getMemberExclusiveTags());
            frequencyInfoDTO.setFormFieldJson(subscriptionPlan.getFormFieldJson());

            if (shop.equalsIgnoreCase("masarap-box.myshopify.com") || shop.equalsIgnoreCase("test-subscription-v152.myshopify.com")) {
                frequencyInfoDTO.setDeliveryPolicyPreAnchorBehavior(SellingPlanRecurringDeliveryPolicyPreAnchorBehavior.NEXT);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled())) {
                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setDiscountType(Optional.ofNullable(subscriptionPlan.getDiscountType()).orElse(DiscountTypeUnit.PERCENTAGE));
                frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                frequencyInfoDTO.setAfterCycle1(Optional.ofNullable(subscriptionPlan.getAfterCycle1()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled(false);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled2())) {
                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setDiscountType2(Optional.ofNullable(subscriptionPlan.getDiscountType2()).orElse(DiscountTypeUnit.PERCENTAGE));
                frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                frequencyInfoDTO.setAfterCycle2(Optional.ofNullable(subscriptionPlan.getAfterCycle2()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled2(false);
            }

            if(BooleanUtils.isTrue(subscriptionPlan.isFreeTrialEnabled())) {
                frequencyInfoDTO.setFreeTrialEnabled(true);
                frequencyInfoDTO.setFreeTrialCount(subscriptionPlan.getFreeTrialCount());
                frequencyInfoDTO.setFreeTrialInterval(Optional.ofNullable(subscriptionPlan.getFreeTrialInterval()).orElse(FrequencyIntervalUnit.DAY));

                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setAfterCycle1(0);
                if(subscriptionPlan.getDiscountOffer() != null) {
                    frequencyInfoDTO.setDiscountType(Optional.ofNullable(subscriptionPlan.getDiscountType()).orElse(DiscountTypeUnit.PERCENTAGE));
                    frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                } else {
                    frequencyInfoDTO.setDiscountType(DiscountTypeUnit.PERCENTAGE);
                    frequencyInfoDTO.setDiscountOffer(100d);
                }

                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setAfterCycle2(1);
                if(subscriptionPlan.getDiscountOffer2() != null) {
                    frequencyInfoDTO.setDiscountType2(Optional.ofNullable(subscriptionPlan.getDiscountType2()).orElse(DiscountTypeUnit.PERCENTAGE));
                    frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                } else {
                    frequencyInfoDTO.setDiscountType2(DiscountTypeUnit.PERCENTAGE);
                    frequencyInfoDTO.setDiscountOffer2(0d);
                }
            } else {
                frequencyInfoDTO.setFreeTrialEnabled(false);
            }

            List<AppstleCycle> appstleCycles = subscriptionPlan.getAppstleCycles();

            if(!CollectionUtils.isNullOrEmpty(appstleCycles) && frequencyInfoDTO.getDiscountEnabled() && !frequencyInfoDTO.getDiscountEnabled2()) {
                Optional<AppstleCycle> firstCycleOptional = appstleCycles.stream().sorted(Comparator.comparing(AppstleCycle::getAfterCycle)).findFirst();

                if(firstCycleOptional.isPresent()) {
                    AppstleCycle firstCycle = firstCycleOptional.get();
                    if (DiscountType.PERCENTAGE.equals(firstCycle.getDiscountType())) {
                        frequencyInfoDTO.setDiscountEnabled2(true);
                        frequencyInfoDTO.setDiscountType2(DiscountTypeUnit.PERCENTAGE);
                        frequencyInfoDTO.setDiscountOffer2(firstCycle.getValue());
                        frequencyInfoDTO.setAfterCycle2(firstCycle.getAfterCycle());

                        appstleCycles.remove(firstCycle);
                    } else if (DiscountType.FIXED.equals(firstCycle.getDiscountType())) {
                        frequencyInfoDTO.setDiscountEnabled2(true);
                        frequencyInfoDTO.setDiscountType2(DiscountTypeUnit.FIXED);
                        frequencyInfoDTO.setDiscountOffer2(firstCycle.getValue());
                        frequencyInfoDTO.setAfterCycle2(firstCycle.getAfterCycle());

                        appstleCycles.remove(firstCycle);
                    } else if (DiscountType.PRICE.equals(firstCycle.getDiscountType())) {
                        frequencyInfoDTO.setDiscountEnabled2(true);
                        frequencyInfoDTO.setDiscountType2(DiscountTypeUnit.PRICE);
                        frequencyInfoDTO.setDiscountOffer2(firstCycle.getValue());
                        frequencyInfoDTO.setAfterCycle2(firstCycle.getAfterCycle());

                        appstleCycles.remove(firstCycle);
                    }
                }
            }

            frequencyInfoDTO.setAppstleCycles(appstleCycles);

            if (subscriptionPlan.getBillingFrequencyCount() == null || subscriptionPlan.getPlanType().equals(PlanType.PAY_AS_YOU_GO)) {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            } else {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getBillingFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getBillingFrequencyInterval());
            }

            if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayValue(subscriptionPlan.getSpecificDayValue());
                frequencyInfoDTO.setSpecificMonthValue(subscriptionPlan.getSpecificMonthValue());
                frequencyInfoDTO.setCutOff(subscriptionPlan.getCutOff());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_PURCHASE_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else {
                frequencyInfoDTO.setSpecificDayEnabled(false);
            }

            if (subscriptionPlan.getUpcomingOrderEmailBuffer() != null) {
                frequencyInfoDTO.setUpcomingOrderEmailBuffer(subscriptionPlan.getUpcomingOrderEmailBuffer());
            }

            frequencyInfoDTO.setMaxCycles(subscriptionPlan.getMaxCycles());
            frequencyInfoDTO.setMinCycles(subscriptionPlan.getMinCycles());
            frequencyInfoDTO.setFrequencySequence(index);
            subscriptionPlans.add(frequencyInfoDTO);
            index++;
        }
        return subscriptionPlans;
    }

    @PutMapping("/api/external/v2/subscription-groups/{id}/add-products")
    @CrossOrigin
    @ApiOperation("Add products or variants to existing subscription group (selling plan group) ")
    public ResponseEntity<SubscriptionGroupV2DTO> addProductsOrVariants(@ApiParam("Subscription Group Id") @PathVariable Long id,
                                                                        @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey,
                                                                        @ApiParam("Product Ids (comma separated list)") @RequestParam(value = "productIds", required = false) List<Long> productIds,
                                                                        @ApiParam("Variant Ids (comma separated list)") @RequestParam(value = "variantIds", required = false) List<Long> variantIds) throws Exception {
        log.debug("REST request to add products or variants to SubscriptionGroup : {}", id);

        String shop = commonUtils.getShop();

        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTOOptional = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, id);

        if (subscriptionGroupDTOOptional.isEmpty()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if(CollectionUtils.isNullOrEmpty(productIds) && CollectionUtils.isNullOrEmpty(variantIds)) {
            throw new BadRequestAlertException("At least one of the Product Id(s) or Variant Id(s) is required", ENTITY_NAME, "");
        }

        SubscriptionGroupV2DTO subscriptionGroupDTO = subscriptionGroupDTOOptional.get();

        List<String> existingProductIds = getProductIds(subscriptionGroupDTO);

        List<String> existingVariantIds = getVariantIds(subscriptionGroupDTO);

        if(!CollectionUtils.isNullOrEmpty(productIds)) {
            productIds.forEach(productId -> {
                existingProductIds.add(ShopifyIdPrefix.PRODUCT_ID_PREFIX + productId);
            });
        }

        if(!CollectionUtils.isNullOrEmpty(variantIds)) {
            variantIds.forEach(variantId -> {
                existingVariantIds.add(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId);
            });
        }

        SubscriptionGroupV2DTO result = subscriptionGroupService.updateSubscriptionGroupV2(subscriptionGroupDTO, shop, existingProductIds, existingVariantIds);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Updated.", ""))
            .body(result);
    }

    @PutMapping("/api/shopify/v2/subscription-groups")
    @CrossOrigin
    public ResponseEntity<SubscriptionGroupV2DTO> shopifyUpdateSubscriptionGroupV2(@RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO, HttpServletRequest request) throws Exception {
        log.debug("REST request to save SubscriptionGroup : {}", subscriptionGroupDTO);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);

        SubscriptionGroupV2DTO subscriptionGroupDTONew = new SubscriptionGroupV2DTO();
        subscriptionGroupDTONew.setGroupName(subscriptionGroupDTO.getGroupName());

        long sellingPlanGroupId = subscriptionGroupDTO.getId();

        subscriptionGroupDTONew.setId(sellingPlanGroupId);

        ArrayList<FrequencyInfoDTO> subscriptionPlans = new ArrayList<>();
        int index = 0;
        for (FrequencyInfoDTO subscriptionPlan : subscriptionGroupDTO.getSubscriptionPlans()) {
            FrequencyInfoDTO frequencyInfoDTO = new FrequencyInfoDTO();
            frequencyInfoDTO.setFrequencyName(subscriptionPlan.getFrequencyName());
            frequencyInfoDTO.setFrequencyDescription(subscriptionPlan.getFrequencyDescription());
            frequencyInfoDTO.setFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            frequencyInfoDTO.setFrequencyCount(subscriptionPlan.getFrequencyCount());
            frequencyInfoDTO.setId(subscriptionPlan.getIdNew());
            frequencyInfoDTO.setMemberOnly(subscriptionPlan.getMemberOnly());
            frequencyInfoDTO.setNonMemberOnly(subscriptionPlan.getNonMemberOnly());
            frequencyInfoDTO.setMemberInclusiveTags(subscriptionPlan.getMemberInclusiveTags());
            frequencyInfoDTO.setMemberExclusiveTags(subscriptionPlan.getMemberExclusiveTags());
            frequencyInfoDTO.setFormFieldJson(subscriptionPlan.getFormFieldJson());

            if (shop.equalsIgnoreCase("masarap-box.myshopify.com") || shop.equalsIgnoreCase("test-subscription-v152.myshopify.com")) {
                frequencyInfoDTO.setDeliveryPolicyPreAnchorBehavior(SellingPlanRecurringDeliveryPolicyPreAnchorBehavior.NEXT);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled())) {
                frequencyInfoDTO.setDiscountEnabled(true);
                frequencyInfoDTO.setDiscountType(subscriptionPlan.getDiscountType());
                frequencyInfoDTO.setDiscountOffer(subscriptionPlan.getDiscountOffer());
                frequencyInfoDTO.setAfterCycle1(Optional.ofNullable(subscriptionPlan.getAfterCycle1()).orElse(0));
            } else {
                frequencyInfoDTO.setDiscountEnabled(false);
            }

            if (BooleanUtils.isTrue(subscriptionPlan.getDiscountEnabled2())) {
                frequencyInfoDTO.setDiscountEnabled2(true);
                frequencyInfoDTO.setDiscountType2(subscriptionPlan.getDiscountType2());
                frequencyInfoDTO.setDiscountOffer2(subscriptionPlan.getDiscountOffer2());
                frequencyInfoDTO.setAfterCycle2(subscriptionPlan.getAfterCycle2());
            } else {
                frequencyInfoDTO.setDiscountEnabled2(false);
            }

            if (subscriptionPlan.getBillingFrequencyCount() == null || subscriptionPlan.getPlanType().equals(PlanType.PAY_AS_YOU_GO)) {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getFrequencyInterval());
            } else {
                frequencyInfoDTO.setBillingFrequencyCount(subscriptionPlan.getBillingFrequencyCount());
                frequencyInfoDTO.setBillingFrequencyInterval(subscriptionPlan.getBillingFrequencyInterval());
            }

            if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_SPECIFIC_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayValue(subscriptionPlan.getSpecificDayValue());
                frequencyInfoDTO.setSpecificMonthValue(subscriptionPlan.getSpecificMonthValue());
                frequencyInfoDTO.setCutOff(subscriptionPlan.getCutOff());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else if (BooleanUtils.isTrue(subscriptionPlan.isSpecificDayEnabled()) && subscriptionPlan.getFrequencyType() == FrequencyType.ON_PURCHASE_DAY) {
                frequencyInfoDTO.setFrequencyType(subscriptionPlan.getFrequencyType());
                frequencyInfoDTO.setSpecificDayEnabled(true);
            } else {
                frequencyInfoDTO.setSpecificDayEnabled(false);
            }

            if (subscriptionPlan.getUpcomingOrderEmailBuffer() != null) {
                frequencyInfoDTO.setUpcomingOrderEmailBuffer(subscriptionPlan.getUpcomingOrderEmailBuffer());
            }
            frequencyInfoDTO.setFrequencySequence(index);
            frequencyInfoDTO.setMaxCycles(subscriptionPlan.getMaxCycles());
            frequencyInfoDTO.setMinCycles(subscriptionPlan.getMinCycles());

            subscriptionPlans.add(frequencyInfoDTO);
            index++;
        }

        subscriptionGroupDTONew.setSubscriptionPlans(subscriptionPlans);

        List<String> productIds = getProductIds(subscriptionGroupDTO);

        List<String> variantIds = getVariantIds(subscriptionGroupDTO);

        SubscriptionGroupV2DTO result = subscriptionGroupService.updateSubscriptionGroupV2(subscriptionGroupDTONew, shop, productIds, variantIds);

        return ResponseEntity.created(new URI("/api/subscription-groups/" + result.getId())).body(result);
    }

    @GetMapping("/api/subscription-groups/get-all")
    public String getAllInsert() throws Exception {
        log.debug("REST request to get all SubscriptionGroups=== Testing");
        String shop = "test-subscription-v1.myshopify.com";
        //commonUtils.getShop()
        subscriptionGroupService.insertAllShopSubscriptionGroupsV2();
        return "success";
    }

    @GetMapping("/api/v2/subscription-groups")
    public List<SubscriptionGroupV2DTO> getAllSubscriptionGroupsV2() throws Exception {
        log.debug("REST request to get all SubscriptionGroups");

        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(commonUtils.getShop());

        Set<Long> planIds = shopSubscriptionGroups.stream().map(s -> s.getId()).collect(Collectors.toSet());

        List<SubscriptionGroupPlanDTO> dbPlans = subscriptionGroupPlanService.findByShop(commonUtils.getShop());

        for (SubscriptionGroupPlanDTO dbPlan : dbPlans) {
            if (!planIds.contains(dbPlan.getSubscriptionId())) {
                subscriptionGroupPlanService.delete(dbPlan.getId());
            }
        }

        return shopSubscriptionGroups;
    }

    @GetMapping("/api/external/v2/subscription-groups")
    @CrossOrigin
    @ApiOperation("Get list of all subscription groups (selling plan groups)")
    public List<SubscriptionGroupV2DTO> getAllSubscriptionGroupsV2External(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey) throws Exception {
        log.debug("REST request to get all SubscriptionGroups");

        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(commonUtils.getShop());

        return shopSubscriptionGroups;
    }

    @GetMapping("/api/v2/subscription-groups/{id}")
    public ResponseEntity<SubscriptionGroupV2DTO> getSubscriptionGroupV2(@PathVariable Long id) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTO = subscriptionGroupService.findSingleSubscriptionGroupV3(commonUtils.getShop(), id);
        return ResponseUtil.wrapOrNotFound(subscriptionGroupDTO);
    }

    @GetMapping("/api/v2/subscription-groups/{id}/sync")
    public void syncSubscriptionGroupV2(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        String shop = commonUtils.getShop();
        asyncExecutor.execute(() -> {
            try {
                subscriptionGroupService.syncSubscriptionGroupPlan(shop, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @GetMapping("/api/v2/subscription-groups/detail/{id}")
    public ResponseEntity<SubscriptionGroupV2DTO> getSubscriptionGroupPlanDetail(@PathVariable Long id) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTO = subscriptionGroupService.getSubscriptionGroupPlanDetail(commonUtils.getShop(), id);
        return ResponseUtil.wrapOrNotFound(subscriptionGroupDTO);
    }

    @GetMapping("/api/v2/subscription-groups/products/{id}")
    public ResponseEntity<ProductData> getSubscriptionGroupPlanProductsDetail(@PathVariable Long id,
                                                                              @RequestParam(value = "next", defaultValue = "false") Boolean next,
                                                                              @RequestParam(value = "cursor") String cursor) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<ProductData> productData = subscriptionGroupService.getSellingGroupProductsData(commonUtils.getShop(), id, next, cursor);
        return ResponseUtil.wrapOrNotFound(productData);
    }

    @GetMapping("/api/v2/subscription-groups/variants/{id}")
    public ResponseEntity<ProductData> getSubscriptionGroupPlanVariantsDetail(@PathVariable Long id,
                                                                              @RequestParam(value = "next", defaultValue = "false") Boolean next,
                                                                              @RequestParam(value = "cursor") String cursor) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<ProductData> productData = subscriptionGroupService.getSellingGroupVariantsData(commonUtils.getShop(), id, next, cursor);
        return ResponseUtil.wrapOrNotFound(productData);
    }
    @PutMapping("/api/v2/subscription-groups/products-sort/{id}")
    public void sortProductsOrVariants(@PathVariable Long id, @RequestBody SubscriptionGroupProductDTO subscriptionGroupProductDTO) {
        String shop = commonUtils.getShop();

        List<Long> productList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupProductDTO.getProductIds())) {
            productList = Arrays.stream(subscriptionGroupProductDTO.getProductIds().split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        List<Long> variantList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupProductDTO.getVariantIds())) {
            variantList = Arrays.stream(subscriptionGroupProductDTO.getVariantIds().split(",")).filter(StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }
        subscriptionGroupService.sortSellingPlanGroupProductOrVariant(shop, id, productList, variantList);
    }

    @PutMapping("/api/v2/subscription-groups/products-update/{id}")
    public ResponseEntity<SubscriptionGroupV2DTO> updateProductsOrVariants(@PathVariable Long id,
                                                                           @RequestBody SubscriptionGroupProductDTO subscriptionGroupProductDTO) throws Exception {
        String shop = commonUtils.getShop();
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTOOptional = subscriptionGroupService.getSubscriptionGroupPlanDetail(shop, id);

        if (subscriptionGroupDTOOptional.isEmpty()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if(StringUtils.isEmpty(subscriptionGroupProductDTO.getProductIds()) && StringUtils.isEmpty(subscriptionGroupProductDTO.getVariantIds())) {
            throw new BadRequestAlertException("At least one of the Product Id(s) or Variant Id(s) is required", ENTITY_NAME, "");
        }

        List<SubscribedProductVariantInfo> productInfoList = new ArrayList<>();
        List<String> newProductIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupProductDTO.getProductIds())) {
            productInfoList = getSubscribeInfoFromJSON(subscriptionGroupProductDTO.getProductIds());
            newProductIds = productInfoList.stream().map(a -> ShopifyGraphQLUtils.getGraphQLProductId(a.getId())).collect(Collectors.toList());
        }

        List<SubscribedProductVariantInfo> variantInfoList = new ArrayList<>();
        List<String> newVariantIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupProductDTO.getVariantIds())) {
            variantInfoList = getSubscribeInfoFromJSON(subscriptionGroupProductDTO.getVariantIds());
            newVariantIds = variantInfoList.stream().map(a -> ShopifyGraphQLUtils.getGraphQLVariantId(a.getId())).collect(Collectors.toList());
        }

        List<SubscribedProductVariantInfo> oldProductInfoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupDTOOptional.get().getProductIds())) {
            oldProductInfoList = getSubscribeInfoFromJSON(subscriptionGroupDTOOptional.get().getProductIds());
        }

        List<SubscribedProductVariantInfo> oldVariantInfoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupDTOOptional.get().getVariantIds())) {
            oldVariantInfoList = getSubscribeInfoFromJSON(subscriptionGroupDTOOptional.get().getVariantIds());
        }

        List<String> finalNewProductIds = newProductIds;
        List<SubscribedProductVariantInfo> removeProduct = oldProductInfoList.stream().filter(subscribedProductVariantInfo -> finalNewProductIds.contains(ShopifyGraphQLUtils.getGraphQLProductId(subscribedProductVariantInfo.getId()))).collect(Collectors.toList());
        if (removeProduct.size() > 0){
            oldProductInfoList.removeAll(removeProduct);
        }

        List<String> finalNewVariantIds = newVariantIds;
        List<SubscribedProductVariantInfo> removeVariant = oldVariantInfoList.stream().filter(subscribedProductVariantInfo -> finalNewVariantIds.contains(ShopifyGraphQLUtils.getGraphQLVariantId(subscribedProductVariantInfo.getId()))).collect(Collectors.toList());
        if (removeVariant.size() > 0){
            oldVariantInfoList.removeAll(removeVariant);
        }

        oldVariantInfoList.addAll(variantInfoList);
        oldProductInfoList.addAll(productInfoList);

        SubscriptionGroupV2DTO result = subscriptionGroupService.updateSubscriptionGroupProducts(subscriptionGroupDTOOptional.get(), shop, newProductIds, newVariantIds, oldProductInfoList, oldVariantInfoList);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Products Updated.", "")).body(result);
    }

    @PutMapping("/api/v2/subscription-groups/products-delete/{id}")
    public ResponseEntity<SubscriptionGroupV2DTO> deleteProductsOrVariants(@PathVariable Long id,
                                                                           @RequestBody SubscriptionGroupProductDTO subscriptionGroupProductDTO) throws Exception {
        String shop = commonUtils.getShop();
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTOOptional = subscriptionGroupService.getSubscriptionGroupPlanDetail(shop, id);

        if (subscriptionGroupDTOOptional.isEmpty()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if(CollectionUtils.isNullOrEmpty(subscriptionGroupProductDTO.getDeleteProductIds()) && CollectionUtils.isNullOrEmpty(subscriptionGroupProductDTO.getDeleteVariantIds())) {
            throw new BadRequestAlertException("At least one of the Product Id(s) or Variant Id(s) is required", ENTITY_NAME, "");
        }


        List<String> deleteProductIds = new ArrayList<>();
        if (!CollectionUtils.isNullOrEmpty(subscriptionGroupProductDTO.getDeleteProductIds())) {
            deleteProductIds = subscriptionGroupProductDTO.getDeleteProductIds();
        }

        List<String> deleteVariantIds = new ArrayList<>();
        if (!CollectionUtils.isNullOrEmpty(subscriptionGroupProductDTO.getDeleteVariantIds())) {
            deleteVariantIds = subscriptionGroupProductDTO.getDeleteVariantIds();
        }

        List<SubscribedProductVariantInfo> oldProductInfoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupDTOOptional.get().getProductIds())) {
            oldProductInfoList = getSubscribeInfoFromJSON(subscriptionGroupDTOOptional.get().getProductIds());
        }

        List<SubscribedProductVariantInfo> oldVariantInfoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(subscriptionGroupDTOOptional.get().getVariantIds())) {
            oldVariantInfoList = getSubscribeInfoFromJSON(subscriptionGroupDTOOptional.get().getVariantIds());
        }


        List<String> finalDeleteProductIds = deleteProductIds;
        List<SubscribedProductVariantInfo> removeProduct = oldProductInfoList.stream().filter(subscribedProductVariantInfo -> finalDeleteProductIds.contains(subscribedProductVariantInfo.getId().toString())).collect(Collectors.toList());
        if (removeProduct.size() > 0){
            oldProductInfoList.removeAll(removeProduct);
        }

        List<String> finalDeleteVariantIds = deleteVariantIds;
        List<SubscribedProductVariantInfo> removeVariant = oldVariantInfoList.stream().filter(subscribedProductVariantInfo -> finalDeleteVariantIds.contains(subscribedProductVariantInfo.getId().toString())).collect(Collectors.toList());
        if (removeVariant.size() > 0){
            oldVariantInfoList.removeAll(removeVariant);
        }

        SubscriptionGroupV2DTO result = subscriptionGroupService.deleteSubscriptionGroupProducts(subscriptionGroupDTOOptional.get(), shop, deleteProductIds, deleteVariantIds, oldProductInfoList, oldVariantInfoList);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Products Deleted.", "")).body(result);
    }

    @GetMapping("/api/external/v2/subscription-groups/{id}")
    @CrossOrigin
    @ApiOperation("Get Subscription group (selling plan group) by ID")
    public ResponseEntity<SubscriptionGroupV2DTO> getSubscriptionGroupV2External(@ApiParam("Subscription Group Id") @PathVariable Long id,
                                                                                 @ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTO = subscriptionGroupService.findSingleSubscriptionGroupV2(commonUtils.getShop(), id);
        return ResponseUtil.wrapOrNotFound(subscriptionGroupDTO);
    }


    @GetMapping("/api/v2/subscription-groups/all-used-products/{id}")
    public List<String> getSubscriptionGroupUsedProducts(@PathVariable Long id) throws Exception {
        log.debug("REST request to get used product ids : {}", id);
        String shop = commonUtils.getShop();
        return subscriptionGroupPlanService.getAllOtherProductIdsBySubscription(shop, id);
    }

    @GetMapping("/api/v2/subscription-groups/all-used-products")
    public List<String> getSubscriptionGroupUsedProducts() throws Exception {
        log.debug("REST request to get used product ids : {}");
        String shop = commonUtils.getShop();
        return subscriptionGroupPlanService.getAllOtherProductIdsBySubscription(shop, 0l);
    }


    @GetMapping("/api/shopify/v2/subscription-groups")
    @CrossOrigin
    public ResponseEntity<SubscriptionGroupV2DTO> getShopifySubscriptionGroupV2(@RequestParam("sellingPlanGroupId") String sellingPlanGroupId, @Header("Appstle-Authorisation") String token, HttpServletRequest request) throws Exception {
        log.debug("REST request to get sellingPlanGroupId : {}", sellingPlanGroupId);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);
        long id = Long.parseLong(sellingPlanGroupId.replace(SELLING_PLAN_GROUP_ID_PREFIX, ""));
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroup = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, id);
        return ResponseUtil.wrapOrNotFound(singleSubscriptionGroup);
    }


    @GetMapping("/api/shopify/v2/subscription-groups/find")
    @CrossOrigin
    public Boolean findProblemId(@RequestParam("productId") String productIdString, @Header("Appstle-Authorisation") String token, HttpServletRequest request) throws Exception {
        log.debug("REST request to get productIdString : {}", productIdString);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);
        long productId = Long.parseLong(productIdString.replace(PRODUCT_ID_PREFIX, ""));
        List<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOList = subscriptionGroupPlanService.findByShop(shop);

        Set<Long> existingProductIds = subscriptionGroupPlanDTOList.stream()
            .map(s -> StringUtils.split(s.getProductIds(), ","))
            .flatMap(Arrays::stream)
            .map(Long::parseLong)
            .collect(Collectors.toSet());

        return existingProductIds.contains(productId);
    }


    @GetMapping(value = {"/api/shopify/subscription-groups-list", "/api/shopify/v2/subscription-groups-list"})
    @CrossOrigin
    public List<SubscriptionGroupV2DTO> getShopifySubscriptionGroup(HttpServletRequest request) throws Exception {
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);

        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(shop);
        return shopSubscriptionGroups;
    }

    @PutMapping(value = {"/api/shopify/add-plans", "/api/shopify/v2/add-plans"})
    @CrossOrigin
    public void addPlansToProduct(@RequestBody AddPlansToProduct addPlansToProduct, HttpServletRequest request) throws Exception {
        log.debug("REST request to get addPlansToProduct : {}", addPlansToProduct);
        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);

        ShopifyGraphqlClient api = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long groupId : addPlansToProduct.getPlans()) {

            if(StringUtils.isNotBlank(addPlansToProduct.getVariantId())) {
                // Add variant
                List<String> variantIds = Lists.newArrayList();
                variantIds.add(addPlansToProduct.getVariantId());
                SellingPlanGroupAddProductVariantsMutation sellingPlanGroupAddProductVariantsMutation = new SellingPlanGroupAddProductVariantsMutation(SELLING_PLAN_GROUP_ID_PREFIX + groupId, variantIds);
                api.getOptionalMutationResponse(sellingPlanGroupAddProductVariantsMutation);
            } else {
                // Add product
                List<String> productIds = Lists.newArrayList();
                productIds.add(addPlansToProduct.getProductId());
                SellingPlanGroupAddProductsMutation sellingPlanGroupAddProductsMutation = new SellingPlanGroupAddProductsMutation(SELLING_PLAN_GROUP_ID_PREFIX + groupId, productIds);
                api.getOptionalMutationResponse(sellingPlanGroupAddProductsMutation);
            }

            Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, groupId);
            subscriptionGroupPlanService.createOrUpdateRecord(shop, groupId, singleSubscriptionGroupV2.get());
        }
    }

    @GetMapping("/api/subscription-groups/raw/{id}")
    public ResponseEntity<SellingPlanGroupQuery.SellingPlanGroup> getSubscriptionGroupRaw(@PathVariable String id) throws Exception {
        log.debug("REST request to get SubscriptionGroup : {}", id);
        Optional<SellingPlanGroupQuery.SellingPlanGroup> singleSubscriptionGroupRaw = subscriptionGroupService.getSingleSubscriptionGroupRaw(commonUtils.getShop(), id);
        return ResponseUtil.wrapOrNotFound(singleSubscriptionGroupRaw);
    }

    @DeleteMapping(value = {"/api/shopify/remove-plans", "/api/shopify/v2/remove-plans"})
    @CrossOrigin
    public void removePlansToProduct(@RequestBody RemovePlansToProduct removePlansToProduct, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String shop = getShop(request);

        log.debug("REST request to get RemovePlansToProduct : {}, shop: {}", removePlansToProduct, shop);


        ShopifyGraphqlClient api = commonUtils.prepareShopifyGraphqlClient(shop);
        List<String> productIds = Lists.newArrayList();
        productIds.add(removePlansToProduct.getProductId());

        List<String> variantIds = Lists.newArrayList();

        String sellingPlanGroupId = removePlansToProduct.getSellingPlanGroupId();

        if(StringUtils.isNotBlank(removePlansToProduct.getVariantId())) {
            // Remove Variant
            variantIds.add(removePlansToProduct.getVariantId());
            SellingPlanGroupRemoveProductVariantsMutation sellingPlanGroupRemoveProductVariantsMutation = new SellingPlanGroupRemoveProductVariantsMutation(sellingPlanGroupId, variantIds);
            api.getOptionalMutationResponse(sellingPlanGroupRemoveProductVariantsMutation);
        } else {
            // Remove Product and all of its variants
            variantIds.addAll(removePlansToProduct.getVariantIds());
            SellingPlanGroupRemoveProductVariantsMutation sellingPlanGroupRemoveProductVariantsMutation = new SellingPlanGroupRemoveProductVariantsMutation(sellingPlanGroupId, variantIds);
            api.getOptionalMutationResponse(sellingPlanGroupRemoveProductVariantsMutation);

            SellingPlanGroupRemoveProductsMutation sellingPlanGroupRemoveProductsMutation = new SellingPlanGroupRemoveProductsMutation(sellingPlanGroupId, productIds);
            api.getOptionalMutationResponse(sellingPlanGroupRemoveProductsMutation);
        }

        long groupId = Long.parseLong(sellingPlanGroupId.replace(SELLING_PLAN_GROUP_ID_PREFIX, ""));
        Optional<SubscriptionGroupV2DTO> singleSubscriptionGroupV2 = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, groupId);
        subscriptionGroupPlanService.createOrUpdateRecord(shop, groupId, singleSubscriptionGroupV2.get());
    }

    @DeleteMapping(value = {"/api/subscription-groups/{id}", "/api/v2/subscription-groups/{id}"})
    public ResponseEntity<Void> deleteSubscriptionGroup(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete SubscriptionGroup : {}", id);
        subscriptionGroupService.delete(commonUtils.getShop(), id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Subscription Plan Deleted", "")).build();
    }

    @CrossOrigin
    @GetMapping(value = {"/api/subscription-groups/all-selling-plans", "/subscriptions/cp/api/subscription-groups/all-selling-plans"})
    public List<FrequencyInfoDTO> getAllSellingPlans() throws Exception {
        log.debug("REST request to get all SellingPlans");

        return subscriptionGroupService.getAllSellingPlans(commonUtils.getShop());
    }

    @GetMapping("/api/subscription-groups/selling-plans-for-product-variant")
    public List<FrequencyInfoDTO> getSellingPlansForProductVariant(@RequestParam Long productId, @RequestParam Long variantId,
                                                                   @RequestParam(required = false) Integer billingFrequencyCount,
                                                                   @RequestParam(required = false) FrequencyIntervalUnit billingFrequencyInterval) throws Exception {
        log.debug("REST request to get Selling Plans for Product Variant");
        String shop = commonUtils.getShop();

        List<FrequencyInfoDTO> sellingPlans = new ArrayList<>();

        if(billingFrequencyCount != null && billingFrequencyInterval != null) {
            sellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(shop, productId, variantId, billingFrequencyCount, billingFrequencyInterval);
        } else {
            sellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(shop, productId, variantId);
        }

        return sellingPlans;
    }

    private List<SubscribedProductVariantInfo> getSubscribeInfoFromJSON(String subscribedProductInfoJson) {

        List<SubscribedProductVariantInfo> toReturn = CommonUtils.fromJSON(
            new TypeReference<>() {
            },
            subscribedProductInfoJson
        );

        return Optional.ofNullable(toReturn).orElse(new ArrayList<>());
    }

    private String getShop(HttpServletRequest request) {
        String header = request.getHeader("Appstle-Authorisation");

        Algorithm algorithm = Algorithm.HMAC256("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");
        JWTVerifier verifier = JWT.require(algorithm)
            .build();
        DecodedJWT jwt = verifier.verify(header);
        Claim iss = jwt.getClaim("iss");
        String shop = iss.asString();
        shop = shop.replace("https://", "");
        shop = shop.replace("/admin", "");
        return shop;
    }

    private List<String> getProductIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO) {
        List<SubscribedProductVariantInfo> subscribeInfoList = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getProductIds());
        return subscribeInfoList.stream().map(a -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + a.getId().toString()).collect(Collectors.toList());
    }

    private List<String> getVariantIds(SubscriptionGroupV2DTO subscriptionGroupV2DTO) {
        List<SubscribedProductVariantInfo> subscribeInfoList = getSubscribeInfoFromJSON(subscriptionGroupV2DTO.getVariantIds());
        return subscribeInfoList.stream().map(a -> ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + a.getId().toString()).collect(Collectors.toList());
    }

    @GetMapping("/api/subscription-groups/export-subscription-group-plans")
    public ResponseEntity<Void> exportSubscriptionGroupsProducts(@RequestParam("email") String email) {

        try {
            String shop = SecurityUtils.getCurrentUserLogin().get();
            log.info("REST request to export subscription group plans for shop: {}", shop);
            subscriptionGroupService.exportSubscriptionGroups(shop, email);
            return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Subscription groups exported successfully.", ENTITY_NAME)).build();
        } catch (Exception e){
            log.error("An error occurred while exporting subscriptions groups products, error: {}", e.getMessage());
            return ResponseEntity.noContent().headers(HeaderUtil.createFailureAlert(applicationName, false, ENTITY_NAME,  "", "An error occurred while exporting subscriptions groups products, error: "+e.getMessage())).build();
        }
    }

}
