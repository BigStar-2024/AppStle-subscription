package com.et.api.yotpo;

import com.et.api.yotpo.model.Campaign;
import com.et.api.yotpo.model.RedemptionOption;
import com.et.api.yotpo.model.CustomerDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YotpoApi {

    public static final String API_BASE_URl = "https://loyalty.yotpo.com/api/v2";

    public static final String FETCH_CUSTOMER_DETAILS = "/customers";

    public static final String GET_CAMPAIGNS = "/campaigns";

    public static final String GET_REDEMPTION_OPTIONS = "/redemption_options";

    public static final String REDEEM_POINTS = "/redemptions";

    private final Logger logger = LoggerFactory.getLogger(YotpoApi.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public YotpoApi() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public CustomerDetails fetchCustomerDetails(String apiKey, String guid, String customerEmail) throws JsonProcessingException {

        String url = API_BASE_URl+FETCH_CUSTOMER_DETAILS;

        logger.info("Api call to {} for customer: {}", url, customerEmail);

        String result = Unirest.get(url)
            .queryString("customer_email", customerEmail)
            .header("accept", "application/json")
            .header("x-api-key", apiKey)
            .header("x-guid", guid)
            .asString()
            .getBody();

        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        return OBJECT_MAPPER.readValue(result, CustomerDetails.class);
    }

    public List<Campaign> getAllCampaigns(String apiKey, String guid, String customerEmail) throws JsonProcessingException {

        String url = API_BASE_URl+GET_CAMPAIGNS;

        logger.info("Api call to {} for customer: {}", url, customerEmail);

        String result = Unirest.get(url)
            .queryString("customer_email", customerEmail)
            .header("accept", "application/json")
            .header("x-api-key", apiKey)
            .header("x-guid", guid)
            .asString()
            .getBody();

        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        return OBJECT_MAPPER.readValue(result, new TypeReference<List<Campaign>>() {});
    }

    public List<RedemptionOption> getActiveRedemptionOptions(String apiKey, String guid, String customerEmail) throws JsonProcessingException {

        String url = API_BASE_URl+GET_REDEMPTION_OPTIONS;

        logger.info("Api call to {} for customer: {}", url, customerEmail);

        String result = Unirest.get(url)
            .queryString("customer_email", customerEmail)
            .header("accept", "application/json")
            .header("x-api-key", apiKey)
            .header("x-guid", guid)
            .asString()
            .getBody();

        return OBJECT_MAPPER.readValue(result, new TypeReference<List<RedemptionOption>>() {});
    }

    public String redeemPoints(String apiKey, String guid, String customerEmail, Long redemptionOptionId) throws JsonProcessingException {

        String url = API_BASE_URl+REDEEM_POINTS;

        logger.info("Api call to {} for customer: {} and option: {}", url, customerEmail, redemptionOptionId);

        Map<String, Object> body = new HashMap<>();
        body.put("customer_email", customerEmail);
        body.put("redemption_option_id", redemptionOptionId);

        String jsonBody = OBJECT_MAPPER.writeValueAsString(body);

        JsonNode response = Unirest.post(url)
            .header("accept", "application/json")
            .header("content-type", "application/json")
            .header("x-api-key", apiKey)
            .header("x-guid", guid)
            .body(jsonBody)
            .asJson().getBody();

        JSONObject jsonResponse = response.getObject();

        return jsonResponse.getString("code");
    }
}
