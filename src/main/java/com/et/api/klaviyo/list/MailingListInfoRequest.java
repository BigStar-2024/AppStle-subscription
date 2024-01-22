package com.et.api.klaviyo.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "api_key"
})
public class MailingListInfoRequest {

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("api_key")
    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("api_key")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
