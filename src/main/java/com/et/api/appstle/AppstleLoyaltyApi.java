package com.et.api.appstle;

import com.et.api.appstle.model.CustomerLoyalty;
import com.et.api.appstle.model.PointEarnRule;
import com.et.api.appstle.model.PointRedeemRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppstleLoyaltyApi {

    public static final String API_BASE_URl = "https://loyalty-admin.appstle.com/api/external";

    public static final String GET_CUSTOMER_LOYALTY = "/customer-loyalty";

    public static final String GET_POINT_EARN_RULES = "/point-earn-rules";

    public static final String GET_POINT_REDEEM_RULES = "/point-redeem-rules";

    public static final String REDEEM_POINTS = "/redeem-points";

    private final Logger logger = LoggerFactory.getLogger(AppstleLoyaltyApi.class);

    private static ObjectMapper OBJECT_MAPPER = initObjectMapper();

    public static ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    public CustomerLoyalty getCustomerLoyalty(String apiKey, String customerEmail, Long customerId) throws JsonProcessingException {

        String url = API_BASE_URl+GET_CUSTOMER_LOYALTY;

        logger.info("Api call to {} for customer: {}", url, customerEmail);

        String result = Unirest.get(url)
            .queryString("api_key", apiKey)
            .queryString("customerEmail", customerEmail)
            .queryString("customerId", customerId)
            .header("accept", "application/json")
            .asString()
            .getBody();

        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        return OBJECT_MAPPER.readValue(result, CustomerLoyalty.class);
    }

    public List<PointEarnRule> getAllPointEarnRules(String apiKey) throws JsonProcessingException {

        String url = API_BASE_URl+GET_POINT_EARN_RULES;

        logger.info("Api call to {}", url);

        String result = Unirest.get(url)
            .queryString("api_key", apiKey)
            .header("accept", "application/json")
            .asString()
            .getBody();

        return OBJECT_MAPPER.readValue(result, new TypeReference<List<PointEarnRule>>() {});
    }

    public List<PointRedeemRule> getAllPointRedeemRules(String apiKey) throws JsonProcessingException {

        String url = API_BASE_URl+GET_POINT_REDEEM_RULES;

        logger.info("Api call to {}", url);

        String result = Unirest.get(url)
            .queryString("api_key", apiKey)
            .header("accept", "application/json")
            .asString()
            .getBody();

        return OBJECT_MAPPER.readValue(result, new TypeReference<List<PointRedeemRule>>() {});
    }

    public CustomerLoyalty redeemPoints(String apiKey, String customerEmail, Long redemptionOptionId) throws JsonProcessingException {

        String url = API_BASE_URl+REDEEM_POINTS;

        logger.info("Api call to {} for customer: {} and option: {}", url, customerEmail, redemptionOptionId);

        Map<String, Object> body = new HashMap<>();
        body.put("customerEmail", customerEmail);
        body.put("redeemRuleId", redemptionOptionId);

        String jsonBody = OBJECT_MAPPER.writeValueAsString(body);

        String result = Unirest.post(url)
            .queryString("api_key", apiKey)
            .header("accept", "application/json")
            .header("content-type", "application/json")
            .body(jsonBody)
            .asString().getBody();

        return OBJECT_MAPPER.readValue(result, CustomerLoyalty.class);
    }
}
