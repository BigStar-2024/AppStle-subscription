package com.et.service.impl;

import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.recurringcharge.ActivateRecurringChargeRequest;
import com.et.api.shopify.recurringcharge.GetRecurringChargesResponse;
import com.et.api.shopify.recurringcharge.RecurringApplicationChargeResponse;
import com.et.api.shopify.shop.Shop;
import com.et.domain.PlanInfo;
import com.et.domain.SocialConnection;
import com.et.domain.enumeration.BasedOn;
import com.et.domain.enumeration.BillingType;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.PaymentPlanService;
import com.et.domain.PaymentPlan;
import com.et.repository.PaymentPlanRepository;
import com.et.service.PlanInfoService;
import com.et.service.SocialConnectionService;
import com.et.service.dto.AdditionalDetailsDTO;
import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanLimitInformation;
import com.et.utils.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service Implementation for managing {@link PaymentPlan}.
 */
@Service
@Transactional
public class PaymentPlanServiceImpl implements PaymentPlanService {

    private final Logger log = LoggerFactory.getLogger(PaymentPlanServiceImpl.class);

    private final PaymentPlanRepository paymentPlanRepository;

    public PaymentPlanServiceImpl(PaymentPlanRepository paymentPlanRepository) {
        this.paymentPlanRepository = paymentPlanRepository;
    }

    /**
     * Save a paymentPlan.
     *
     * @param paymentPlan the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaymentPlan save(PaymentPlan paymentPlan) {
        log.debug("Request to save PaymentPlan : {}", paymentPlan);
        return paymentPlanRepository.save(paymentPlan);
    }

    /**
     * Get all the paymentPlans.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentPlan> findAll() {
        log.debug("Request to get all PaymentPlans");
        return paymentPlanRepository.findAll();
    }


    /**
     * Get one paymentPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentPlan> findOne(Long id) {
        log.debug("Request to get PaymentPlan : {}", id);
        return paymentPlanRepository.findById(id);
    }

    /**
     * Delete the paymentPlan by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentPlan : {}", id);
        paymentPlanRepository.deleteById(id);
    }

    @Override
    public Optional<PaymentPlan> findByShop(String shop) {
        return paymentPlanRepository.findByShop(shop);
    }

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    public static final HashSet<String> TEST_STORE_PLANS = Sets.newHashSet("affiliate", "partner_test", "staff");

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    @Override
    public PlanLimitInformation getPlanLimitInformation(String shop) throws IOException {
        PlanLimitInformation planLimitInformation = new PlanLimitInformation();

        Optional<PaymentPlan> paymentPlanOptional = paymentPlanRepository.findByShop(shop);

        if (paymentPlanOptional.isPresent()) {
            PaymentPlan paymentPlan = paymentPlanOptional.get();

            AdditionalDetailsDTO additionalDetailsDTO = CommonUtils.fromJSONIgnoreUnknownProperty(
                new TypeReference<>() {
                },
                paymentPlan.getAdditionalDetails()
            );

            Integer activeSubscriptionCount = null;
            Integer subscriptionLimit = null;

            Double usedRecOrderAmount = null;
            Double usedFirstOrderAmount = null;
            Double monthlyOrderAmountLimit = null;

            ZonedDateTime fromDate = ZonedDateTime.now(ZoneId.of("UTC")).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            ZonedDateTime toDate = ZonedDateTime.now(ZoneId.of("UTC"));

            if (BasedOn.SUBSCRIPTION_ORDER_AMOUNT.equals(paymentPlan.getBasedOn())) {
                usedRecOrderAmount = subscriptionBillingAttemptRepository.getTotalUsedOrderAmountUSDForDateRange(shop, fromDate, toDate);
                usedFirstOrderAmount = subscriptionContractDetailsRepository.getTotalUsedOrderAmountUSDForDateRange(shop, fromDate, toDate);
                monthlyOrderAmountLimit = additionalDetailsDTO.getSubscriptionOrderAmount();
                planLimitInformation.setUsedOrderAmount(Optional.ofNullable(usedRecOrderAmount).orElse(0d) + Optional.ofNullable(usedFirstOrderAmount).orElse(0d));
                planLimitInformation.setOrderAmountLimit(monthlyOrderAmountLimit);
            } else {
                activeSubscriptionCount = subscriptionContractDetailsRepository.findSubscriptionsCount(shop);

                if (additionalDetailsDTO.getSubscriptionCount() != null) {
                    subscriptionLimit = additionalDetailsDTO.getSubscriptionCount().intValue();
                }
                planLimitInformation.setActiveSubscriptionCount(activeSubscriptionCount);
                planLimitInformation.setPlanLimit(subscriptionLimit);
            }

            planLimitInformation.setPlanInfo(paymentPlan.getPlanInfo());
            planLimitInformation.setTrialEndsOn(paymentPlan.getTrialEndsOn());


        } else {
            ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
            List<RecurringApplicationChargeResponse> recurringApplicationCharges = shopifyAPI.getRecurringCharges().getRecurring_application_charges();

            Long planId = null;
            for (RecurringApplicationChargeResponse recurringApplicationCharge : recurringApplicationCharges) {
                if (recurringApplicationCharge.getStatus().equals("active")) {
                    planId = getPlanId(recurringApplicationCharge.getReturn_url());
                }
            }

            for (RecurringApplicationChargeResponse recurringApplicationCharge : recurringApplicationCharges) {
                if (recurringApplicationCharge.getStatus().equals("active")) {
                    activateCharge(shop, recurringApplicationCharge.getId(), planId);
                }
            }
        }
        return planLimitInformation;
    }

    private Long getPlanId(String url) {

        final String regex = "planId=(\\d+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(url);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));

            for (int i = 1; i <= matcher.groupCount(); i++) {
                return Long.parseLong(matcher.group(i));
            }
        }

        return null;
    }

    @Autowired
    private PlanInfoService planInfoService;

    public void activateCharge(String shop, Long chargeId, Long planId) throws IOException {

        log.info("shop=" + shop + " chargeId=" + Optional.ofNullable(chargeId).orElse(0L) + " planId=" + planId);

        Optional<PlanInfoDTO> planInfoOptional = planInfoService.findOne(planId);

        if (planInfoOptional.isPresent() && planInfoOptional.get().getBillingType().equals(BillingType.SHOPIFY_ANNUAL)) {
            updatePaymentEntity(shop, planId, chargeId);
        } else {
            ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

            GetRecurringChargesResponse recurringCharges = api.getRecurringCharges();

            for (RecurringApplicationChargeResponse recurringApplicationCharge : recurringCharges
                .getRecurring_application_charges()) {

                Long recurringApplicationChargeId = recurringApplicationCharge.getId();

                if (!recurringApplicationChargeId.equals(chargeId)
                    || !recurringApplicationCharge.getStatus().equalsIgnoreCase("active")) {
                    continue;
                }

                ActivateRecurringChargeRequest activateRecurringChargeRequest = new ActivateRecurringChargeRequest();
                activateRecurringChargeRequest.setRecurring_application_charge(recurringApplicationCharge);
                api.activateRecurringChargeRequest(activateRecurringChargeRequest);

                updatePaymentEntity(shop, planId, recurringApplicationChargeId);
            }
        }
    }


    private void updatePaymentEntity(String shop, Long planId, Long chargeId) throws IOException {
        PlanInfoDTO planInfoDTO = planInfoService.findOne(planId).get();

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        boolean isTestCharge = true;
        try {
            isTestCharge = BooleanUtils.isTrue(api.getRecurringCharge(chargeId).getRecurring_application_charge().isTest());
        } catch (Exception ex) {

        }

        Optional<PaymentPlan> paymentPlanOptional = findByShop(shop);

        PaymentPlan paymentPlan = paymentPlanOptional.orElseGet(PaymentPlan::new);

        paymentPlan.setShop(shop);
        paymentPlan.setRecurringChargeId(chargeId);
        paymentPlan.setChargeActivated(true);
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        paymentPlan.setActivationDate(utcNow);
        paymentPlan.setBilledDate(utcNow);
        paymentPlan.setAdditionalDetails(planInfoDTO.getAdditionalDetails());
        paymentPlan.setTrialDays(planInfoDTO.getTrialDays());
        paymentPlan.setPrice(planInfoDTO.getPrice());
        paymentPlan.setName(planInfoDTO.getName());
        paymentPlan.setPlanType(planInfoDTO.getPlanType());
        paymentPlan.setBillingType(planInfoDTO.getBillingType());
        paymentPlan.setBasedOn(planInfoDTO.getBasedOn());
        paymentPlan.setFeatures(planInfoDTO.getFeatures());
        paymentPlan.setTestCharge(isTestCharge);
        paymentPlan.setBasePlan(planInfoDTO.getBasePlan());

        PlanInfo planInfo = new PlanInfo();
        planInfo.setId(planId);
        paymentPlan.setPlanInfo(planInfo);

        save(paymentPlan);
    }


    @Override
    public Boolean getTestChargeInformation(String shop) {
        try {
            PaymentPlan paymentPlan = paymentPlanRepository.findByShop(shop).get();

            if (paymentPlan.getPrice() > 0) {
                ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(shop);

                boolean isTestCharge = true;
                try {
                    isTestCharge = BooleanUtils.isTrue(shopifyRestApi.getRecurringCharge(paymentPlan.getRecurringChargeId()).getRecurring_application_charge().isTest());
                } catch (Exception ex1) {

                }

                if (isTestCharge) {
                    Shop shopInfoDetails = shopifyRestApi.getShopInfo().getShop();
                    Boolean isTestStore = false;
                    if (TEST_STORE_PLANS.contains(shopInfoDetails.getPlanName().toLowerCase())) {
                        isTestStore = true;
                    }

                    if (!isTestStore) {
                        return false;
                    }
                }
            }
        } catch (Exception ex2) {

        }

        return true;
    }

    @Override
    public void deleteByShop(String shop) {
        paymentPlanRepository.deleteByShop(shop);
    }
}
