
package com.et.web.rest.vm;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productId",
    "sellingPlanGroupId",
    "subscriptionPlans",
    "groupName"
})
public class SubscriptionGroupModificationV2Request implements Serializable
{

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("sellingPlanGroupId")
    private String sellingPlanGroupId;
    @JsonProperty("subscriptionPlans")
    List<SubscriptionPlanV2Request> subscriptionPlans = new ArrayList<>();
    @JsonProperty("groupName")
    private String groupName;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -554999649085192810L;



    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("sellingPlanGroupId")
    public String getSellingPlanGroupId() {
        return sellingPlanGroupId;
    }

    @JsonProperty("sellingPlanGroupId")
    public void setSellingPlanGroupId(String sellingPlanGroupId) {
        this.sellingPlanGroupId = sellingPlanGroupId;
    }

    @JsonProperty("subscriptionPlans")
    public List<SubscriptionPlanV2Request> getSubscriptionPlans() {
        return subscriptionPlans;
    }

    @JsonProperty("subscriptionPlans")
    public void setSubscriptionPlans(List<SubscriptionPlanV2Request> subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }

    @JsonProperty("groupName")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("groupName")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
