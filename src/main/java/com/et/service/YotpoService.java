package com.et.service;

import com.et.api.yotpo.YotpoApi;
import com.et.api.yotpo.model.Campaign;
import com.et.api.yotpo.model.CustomerDetails;
import com.et.api.yotpo.model.RedemptionOption;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YotpoService {

    private final Logger log = LoggerFactory.getLogger(YotpoService.class);

    public CustomerDetails fetchCustomerDetails(String shop, String customerEmail, String apiKey, String guid) throws JsonProcessingException {
        log.info("Fetching yotpo customer detail for customer={}, shop={}", customerEmail, shop);

        YotpoApi yotpoApi = new YotpoApi();
        return yotpoApi.fetchCustomerDetails(apiKey, guid, customerEmail);
    }

    public List<Campaign> getAllCampaigns(String shop, String customerEmail, String apiKey, String guid) throws JsonProcessingException {
        log.info("Fetching Yotpo campaigns for shop={}", shop);

        YotpoApi yotpoApi = new YotpoApi();
        return yotpoApi.getAllCampaigns(apiKey, guid, customerEmail);
    }

    public List<RedemptionOption> getActiveRedemptionOptions(String shop, String customerEmail, String apiKey, String guid) throws JsonProcessingException {
        log.info("Fetching Yotpo active redemption options for shop={}", shop);

        YotpoApi yotpoApi = new YotpoApi();
        return yotpoApi.getActiveRedemptionOptions(apiKey, guid, customerEmail);
    }

    public String redeemPoint(String shop, String customerEmail, Long redemptionOptionId, String apiKey, String guid) throws JsonProcessingException {
        log.info("Redeeming Yotpo point for customer={}, shop={}", customerEmail, shop);

        YotpoApi yotpoApi = new YotpoApi();
        return yotpoApi.redeemPoints(apiKey, guid, customerEmail, redemptionOptionId);
    }
}
