package com.et.service;

import com.et.api.appstle.AppstleLoyaltyApi;
import com.et.api.appstle.model.CustomerLoyalty;
import com.et.api.appstle.model.PointEarnRule;
import com.et.api.appstle.model.PointRedeemRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AppstleLoyaltyService {

    private final Logger log = LoggerFactory.getLogger(AppstleLoyaltyService.class);

    public CustomerLoyalty getCustomerLoyalty(String shop, String customerEmail, String apiKey, Long customerId) throws JsonProcessingException {
        log.info("Fetching Appstle Loyalty customer loyalty for shop={}, customer={}", shop, customerEmail);

        AppstleLoyaltyApi appstleLoyaltyApi = new AppstleLoyaltyApi();
        return appstleLoyaltyApi.getCustomerLoyalty(apiKey, customerEmail, customerId);
    }

    public List<PointEarnRule> getAllPointEarnRules(String shop, String apiKey) throws JsonProcessingException {
        log.info("Fetching Appstle Loyalty point earn rules for shop={}", shop);

        AppstleLoyaltyApi appstleLoyaltyApi = new AppstleLoyaltyApi();
        return appstleLoyaltyApi.getAllPointEarnRules(apiKey);
    }

    public List<PointRedeemRule> getAllPointRedeemRules(String shop, String apiKey) throws JsonProcessingException {
        log.info("Fetching Appstle Loyalty point redeem rules for shop={}", shop);

        AppstleLoyaltyApi appstleLoyaltyApi = new AppstleLoyaltyApi();
        return appstleLoyaltyApi.getAllPointRedeemRules(apiKey);
    }

    public String redeemPoints(String shop, String customerEmail, Long redemptionOptionId, String apiKey) throws JsonProcessingException {
        log.info("Redeeming Appstle Loyalty point for shop={}, customer={}", shop, customerEmail);

        AppstleLoyaltyApi appstleLoyaltyApi = new AppstleLoyaltyApi();
        CustomerLoyalty customerLoyalty = appstleLoyaltyApi.redeemPoints(apiKey, customerEmail, redemptionOptionId);
        if(CollectionUtils.isEmpty(customerLoyalty.getRewards())){
            return null;
        }
        return customerLoyalty.getRewards().get(0).getDiscountCode();
    }
}
