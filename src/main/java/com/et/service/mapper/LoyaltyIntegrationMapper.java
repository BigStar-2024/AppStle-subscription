package com.et.service.mapper;

import com.et.api.appstle.model.CustomerLoyalty;
import com.et.api.appstle.model.PointEarnRule;
import com.et.api.appstle.model.PointRedeemRule;
import com.et.api.yotpo.model.Campaign;
import com.et.api.yotpo.model.CustomerDetails;
import com.et.api.yotpo.model.RedemptionOption;
import com.et.pojo.LoyaltyIntegration.LoyaltyCustomerDataResponse;
import com.et.pojo.LoyaltyIntegration.LoyaltyPointEarnCampaign;
import com.et.pojo.LoyaltyIntegration.LoyaltyPointRedemptionRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class LoyaltyIntegrationMapper {
    public LoyaltyCustomerDataResponse toCustomerData(CustomerDetails customerDetails){
        if (Objects.isNull(customerDetails)){
            return null;
        }
        LoyaltyCustomerDataResponse loyaltyCustomerDataResponse = new LoyaltyCustomerDataResponse();
        loyaltyCustomerDataResponse.setAvailablePointsBalance(customerDetails.getPoints_balance());
        return loyaltyCustomerDataResponse;
    }

    public LoyaltyCustomerDataResponse toCustomerData(CustomerLoyalty customerDetails){
        if (Objects.isNull(customerDetails)){
            return null;
        }
        LoyaltyCustomerDataResponse loyaltyCustomerDataResponse = new LoyaltyCustomerDataResponse();
        loyaltyCustomerDataResponse.setAvailablePointsBalance(customerDetails.getAvailablePoints());
        return loyaltyCustomerDataResponse;
    }

    public List<LoyaltyPointRedemptionRule> yotpoToLoyaltyRedeemOptions(List<RedemptionOption> redemptionOptions){
        ArrayList<LoyaltyPointRedemptionRule> loyaltyPointRedemptionRuleArrayList = new ArrayList<>();

        for(RedemptionOption redemptionOption : redemptionOptions){
            LoyaltyPointRedemptionRule loyaltyPointRedemptionRule = new LoyaltyPointRedemptionRule();
            loyaltyPointRedemptionRule.setId(redemptionOption.getId());
            loyaltyPointRedemptionRule.setName(redemptionOption.getName());
            loyaltyPointRedemptionRule.setDescription(redemptionOption.getDescription());
            loyaltyPointRedemptionRule.setIcon(redemptionOption.getIcon());
            loyaltyPointRedemptionRule.setPointsCostText(redemptionOption.getCost_text());
            loyaltyPointRedemptionRule.setType(redemptionOption.getType());
            loyaltyPointRedemptionRuleArrayList.add(loyaltyPointRedemptionRule);
        }
        return loyaltyPointRedemptionRuleArrayList;
    }

    public List<LoyaltyPointRedemptionRule> appstleToLoyaltyRedeemOptions(List<PointRedeemRule> redemptionOptions){
        ArrayList<LoyaltyPointRedemptionRule> loyaltyPointRedemptionRuleArrayList = new ArrayList<>();

        for(PointRedeemRule redemptionOption : redemptionOptions){
            LoyaltyPointRedemptionRule loyaltyPointRedemptionRule = new LoyaltyPointRedemptionRule();
            loyaltyPointRedemptionRule.setId(redemptionOption.getId());
            loyaltyPointRedemptionRule.setName(redemptionOption.getName());
            loyaltyPointRedemptionRule.setDescription(redemptionOption.getCustomerFacingLabel());
            loyaltyPointRedemptionRule.setType(redemptionOption.getType());
            loyaltyPointRedemptionRule.setPointsCostText(Objects.nonNull(redemptionOption.getRedeemPoints()) ? redemptionOption.getRedeemPoints().toString() : "Cost not defined");
            loyaltyPointRedemptionRuleArrayList.add(loyaltyPointRedemptionRule);
        }
        return loyaltyPointRedemptionRuleArrayList;
    }

    public List<LoyaltyPointEarnCampaign> yotpoToLoyaltyPointsEarn(List<Campaign> campaigns){
        ArrayList<LoyaltyPointEarnCampaign> loyaltyPointEarnCampaignArrayList = new ArrayList<>();

        for(Campaign campaign: campaigns) {
            LoyaltyPointEarnCampaign loyaltyPointEarnCampaign = new LoyaltyPointEarnCampaign();
            loyaltyPointEarnCampaign.setId(campaign.getId());
            loyaltyPointEarnCampaign.setName(campaign.getUsername());
            loyaltyPointEarnCampaign.setCreateAt(campaign.getCreated_at());
            loyaltyPointEarnCampaign.setType(campaign.getType());
            loyaltyPointEarnCampaign.setUpdateAt(campaign.getUpdated_at());
            loyaltyPointEarnCampaign.setReward_text(campaign.getReward_text());
            loyaltyPointEarnCampaign.setDetails(campaign.getDetails());
            loyaltyPointEarnCampaignArrayList.add(loyaltyPointEarnCampaign);
        }
        return loyaltyPointEarnCampaignArrayList;
    }

    public List<LoyaltyPointEarnCampaign> appstleToLoyaltyPointsEarn(List<PointEarnRule> pointEarnRules){
        ArrayList<LoyaltyPointEarnCampaign> loyaltyPointEarnCampaignArrayList = new ArrayList<>();

        for(PointEarnRule pointEarnRule: pointEarnRules) {
            LoyaltyPointEarnCampaign loyaltyPointEarnCampaign = new LoyaltyPointEarnCampaign();
            loyaltyPointEarnCampaign.setId(pointEarnRule.getId());
            loyaltyPointEarnCampaign.setName(pointEarnRule.getName());
            loyaltyPointEarnCampaign.setCreateAt(pointEarnRule.getCreateAt());
            loyaltyPointEarnCampaign.setType(pointEarnRule.getType());
            loyaltyPointEarnCampaign.setUpdateAt(pointEarnRule.getUpdateAt());
            loyaltyPointEarnCampaign.setBasePoints(pointEarnRule.getBasePoints());
            loyaltyPointEarnCampaign.setProductData(pointEarnRule.getProductData());
            loyaltyPointEarnCampaignArrayList.add(loyaltyPointEarnCampaign);
        }
        return loyaltyPointEarnCampaignArrayList;
    }
}
