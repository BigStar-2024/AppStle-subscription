package com.et.api.eber;

import com.et.api.eber.model.PointsIssue;
import com.et.api.eber.model.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class EberLoyaltyApi {
    public static final String API_BASE_URl = "https://api.eber.co/v3/public";

    public static final String GET_USER_INFO = "/integration/user/show";

    public static final String REDEEM_POINTS = "/integration/redeem_reward";
    public static final String GET_POINT_REDEEM_RULES = "/reward/available/for_user";

    public static final String ISSUE_POINTS = "/integration/issue_point";

    private final Logger logger = LoggerFactory.getLogger(EberLoyaltyApi.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public UserInfo getUserInfo(String apiKey, String customerEmail) throws JsonProcessingException {

        String authKey = apiKey + ":";

        String url = API_BASE_URl+GET_USER_INFO;

        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(authKey.getBytes()));

        logger.info("Eber Api call to {} for customer: {}", url, customerEmail);

        String result = Unirest.get(url)
            .queryString("email", customerEmail)
            .header("Authorization", basicAuth)
            .asString()
            .getBody();

        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return OBJECT_MAPPER.readValue(result, UserInfo.class);
    }

    public PointsIssue issuePoints(String apiKey, String customerEmail, Double amount, Long points, String note) throws JsonProcessingException {

        String authKey = apiKey + ":";

        String url = API_BASE_URl+ISSUE_POINTS;

        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(authKey.getBytes()));

        logger.info("Eber Api call to {} for customer: {}", url, customerEmail);

        String response = Unirest.post(url)
            .queryString("email", customerEmail)
            .queryString("amount", amount)
            .queryString("points", points)
            .queryString("note", note)
            .header("Authorization", basicAuth)
            .asString().getBody();

        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return OBJECT_MAPPER.readValue(response, PointsIssue.class);

    }

}
