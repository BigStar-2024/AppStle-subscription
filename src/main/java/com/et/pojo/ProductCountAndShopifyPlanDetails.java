
package com.et.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "maxCount",
    "shopifyPlanType"
})
public class ProductCountAndShopifyPlanDetails {

    @JsonProperty("maxCount")
    private Long maxCount;
    @JsonProperty("shopifyPlanType")
    private List<String> shopifyPlanType = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("maxCount")
    public Long getMaxCount() {
        return maxCount;
    }

    @JsonProperty("maxCount")
    public void setMaxCount(Long maxCount) {
        this.maxCount = maxCount;
    }

    @JsonProperty("shopifyPlanType")
    public List<String> getShopifyPlanType() {
        return shopifyPlanType;
    }

    @JsonProperty("shopifyPlanType")
    public void setShopifyPlanType(List<String> shopifyPlanType) {
        this.shopifyPlanType = shopifyPlanType;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
