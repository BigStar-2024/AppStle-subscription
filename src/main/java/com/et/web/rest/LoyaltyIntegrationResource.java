package com.et.web.rest;

import com.apollographql.apollo.api.Response;
import com.et.api.appstle.model.CustomerLoyalty;
import com.et.api.appstle.model.PointEarnRule;
import com.et.api.appstle.model.PointRedeemRule;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.api.yotpo.model.Campaign;
import com.et.api.yotpo.model.CustomerDetails;
import com.et.api.yotpo.model.RedemptionOption;
import com.et.pojo.LoyaltyIntegration.LoyaltyCustomerDataResponse;
import com.et.pojo.LoyaltyIntegration.LoyaltyPointEarnCampaign;
import com.et.pojo.LoyaltyIntegration.LoyaltyPointRedemptionRule;
import com.et.service.AppstleLoyaltyService;
import com.et.service.ShopInfoService;
import com.et.service.YotpoService;
import com.et.service.dto.ShopInfoDTO;
import com.et.service.mapper.LoyaltyIntegrationMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopify.java.graphql.client.queries.CustomerBriefQuery;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Loyalty Integration Resource")
public class LoyaltyIntegrationResource {

    private final Logger log = LoggerFactory.getLogger(LoyaltyIntegrationResource.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private YotpoService yotpoService;

    @Autowired
    private AppstleLoyaltyService appstleLoyaltyService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private LoyaltyIntegrationMapper loyaltyIntegrationMapper;

    @CrossOrigin
    @GetMapping(value = {"/api/loyalty-integration/customer", "/subscriptions/cp/api/loyalty-integration/customer"})
    public ResponseEntity<LoyaltyCustomerDataResponse> fetchCustomerDetail(@RequestParam String customerId) throws JsonProcessingException {

        String shop = commonUtils.getShop();

        String customerEmail = commonUtils.getCustomerEmailById(customerId, shop);

        log.info("REST request to fetch loyalty customer details, shop={}, customer_email={} ", shop, customerEmail);

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);

        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop settings not found for shop: "+ shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();
        LoyaltyCustomerDataResponse result = null;

        if(StringUtils.isNotBlank(shopInfoDTO.getAppstleLoyaltyApiKey())){
            CustomerLoyalty customerLoyalty = appstleLoyaltyService.getCustomerLoyalty(shop, customerEmail, shopInfoDTO.getAppstleLoyaltyApiKey(), ShopifyGraphQLUtils.getCustomerId(customerId));
            result = loyaltyIntegrationMapper.toCustomerData(customerLoyalty);
        }else if(StringUtils.isNotBlank(shopInfoDTO.getYotpoApiKey()) && StringUtils.isNotBlank(shopInfoDTO.getYotpoGuid())) {
            CustomerDetails customerDetails = yotpoService.fetchCustomerDetails(shop, customerEmail, shopInfoDTO.getYotpoApiKey(), shopInfoDTO.getYotpoGuid());
            result = loyaltyIntegrationMapper.toCustomerData(customerDetails);
        }

        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping(value = {"/api/loyalty-integration/earn-options", "/subscriptions/cp/api/loyalty-integration/earn-options"})
    public ResponseEntity<List<LoyaltyPointEarnCampaign>> getEarnOptions(@RequestParam(required = false) String customerId) throws JsonProcessingException {

        String shop = commonUtils.getShop();
        String customerEmail = commonUtils.getCustomerEmailById(customerId, shop);

        log.info("REST request to get loyalty redeem options for shop={} ", shop);

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);

        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop settings not found for shop: "+ shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();
        List<LoyaltyPointEarnCampaign> result = new ArrayList<>();

        if(StringUtils.isNotBlank(shopInfoDTO.getAppstleLoyaltyApiKey())){
            List<PointEarnRule> pointEarnRules = appstleLoyaltyService.getAllPointEarnRules(shop, shopInfoDTO.getAppstleLoyaltyApiKey());
            result = loyaltyIntegrationMapper.appstleToLoyaltyPointsEarn(pointEarnRules);
        }else if(StringUtils.isNotBlank(shopInfoDTO.getYotpoApiKey()) && StringUtils.isNotBlank(shopInfoDTO.getYotpoGuid())) {
            List<Campaign> campaigns = yotpoService.getAllCampaigns(shop, customerEmail, shopInfoDTO.getYotpoApiKey(), shopInfoDTO.getYotpoGuid());
            result = loyaltyIntegrationMapper.yotpoToLoyaltyPointsEarn(campaigns);
        }

        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @GetMapping(value = {"/api/loyalty-integration/redeem-options", "/subscriptions/cp/api/loyalty-integration/redeem-options"})
    public ResponseEntity<List<LoyaltyPointRedemptionRule>> getRedeemOptions(@RequestParam(required = false) String customerId) throws JsonProcessingException {

        String shop = commonUtils.getShop();
        String customerEmail = commonUtils.getCustomerEmailById(customerId, shop);

        log.info("REST request to get loyalty redeem options for shop={} ", shop);

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);

        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop settings not found for shop: "+ shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();
        List<LoyaltyPointRedemptionRule> result = new ArrayList<>();

        if(StringUtils.isNotBlank(shopInfoDTO.getAppstleLoyaltyApiKey())){
            List<PointRedeemRule> pointRedeemRules = appstleLoyaltyService.getAllPointRedeemRules(shop, shopInfoDTO.getAppstleLoyaltyApiKey());
            result = loyaltyIntegrationMapper.appstleToLoyaltyRedeemOptions(pointRedeemRules);
        }else if(StringUtils.isNotBlank(shopInfoDTO.getYotpoApiKey()) && StringUtils.isNotBlank(shopInfoDTO.getYotpoGuid())) {
            List<RedemptionOption> redemptionOptionList = yotpoService.getActiveRedemptionOptions(shop, customerEmail, shopInfoDTO.getYotpoApiKey(), shopInfoDTO.getYotpoGuid());
            result = loyaltyIntegrationMapper.yotpoToLoyaltyRedeemOptions(redemptionOptionList);
        }

        return ResponseEntity.ok(result);
    }

    @CrossOrigin
    @PostMapping(value = {"/api/loyalty-integration/redeem", "/subscriptions/cp/api/loyalty-integration/redeem"})
    public ResponseEntity<String> redeemPoints(@RequestParam String customerId, @RequestParam Long redeemOptionId) throws JsonProcessingException {

        String shop = commonUtils.getShop();
        String customerEmail = commonUtils.getCustomerEmailById(customerId, shop);

        log.info("REST request to redeem loyalty points for shop={}, customer={}", shop, customerEmail);

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);

        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop settings not found for shop: "+ shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();
        String result = null;

        if(StringUtils.isNotBlank(shopInfoDTO.getAppstleLoyaltyApiKey())){
            result = appstleLoyaltyService.redeemPoints(shop, customerEmail, redeemOptionId, shopInfoDTO.getAppstleLoyaltyApiKey());
        }else if(StringUtils.isNotBlank(shopInfoDTO.getYotpoApiKey()) && StringUtils.isNotBlank(shopInfoDTO.getYotpoGuid())) {
            result = yotpoService.redeemPoint(shop, customerEmail, redeemOptionId, shopInfoDTO.getYotpoApiKey(), shopInfoDTO.getYotpoGuid());
        }

        return ResponseEntity.ok(result);
    }
}
