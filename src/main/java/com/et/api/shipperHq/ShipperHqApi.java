package com.et.api.shipperHq;

import com.et.api.shipperHq.model.RatingInfo;
import com.et.web.rest.errors.BadRequestAlertException;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipperHqApi {

    private final Logger logger = LoggerFactory.getLogger(ShipperHqApi.class);

    public static  final String CREATE_SECRET_TOKEN = "https://rms.shipperhq.com";
    public static final String CHECK_ADDRESS_VALIDITY = "https://api.shipperhq.com/v2/graphql";

    public String createSecretToken(String apiKey, String authCode) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post(CREATE_SECRET_TOKEN)
            .header("content-type", "application/json")
            .body("{\"query\":\"mutation {\\n  " +
                "createSecretToken(\\n      " +
                "api_key: \\\"" + apiKey + "\\\",\\n        " +
                "auth_code:\\\"" + authCode + "\\\"\\n        " +
                ")" +
                "{\\n    token\\n  }" +
                "\\n}\"}")
            .asJson();
        if(response.getBody().getObject().isNull("errors")) {
            return response.getBody().getObject().getJSONObject("data").getJSONObject("createSecretToken").getString("token");
        } else {
            throw new BadRequestAlertException("Error occured while creating Token", "", "");
        }
    }

    public HttpResponse<JsonNode> checkIsValidForRates(String token, RatingInfo ratingInfo) throws Exception {

        String query = "query RateQuery($ratingInfo: RatingInfoInput!){\n" +
            "  retrieveShippingQuote(ratingInfo: $ratingInfo){\n" +
            "    errors {\n" +
            "        errorCode\n" +
            "        priority\n" +
            "        externalErrorMessage\n" +
            "        internalErrorMessage\n" +
            "    }\n" +
            "    carriers {\n" +
            "        carrierCode\n" +
            "        carrierTitle\n" +
            "        carrierType\n" +
            "        error {\n" +
            "            errorCode\n" +
            "            externalErrorMessage\n" +
            "            internalErrorMessage\n" +
            "        }\n" +
            "        shippingRates {\n" +
            "            code\n" +
            "            currency\n" +
            "            totalCharges\n" +
            "            title\n" +
            "        }\n" +
            "    }\n" +
            "    transactionId\n" +
            "  }}";

        JsonObject body = new JsonObject();
        body.addProperty("query", query);
        body.add("variables", new Gson().toJsonTree(ImmutableMap.of("ratingInfo", ratingInfo)));

        HttpResponse<JsonNode> response = Unirest.post(CHECK_ADDRESS_VALIDITY)
            .header("Content-Type", "application/json")
            .header("X-ShipperHQ-Secret-Token", token)
            .body(body.toString())
            .asJson();

        return response;

    }

}
