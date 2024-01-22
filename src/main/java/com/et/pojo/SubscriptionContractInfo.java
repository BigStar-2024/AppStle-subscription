
package com.et.pojo;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "admin_graphql_api_id",
    "id",
    "billing_policy",
    "currency_code",
    "customer_id",
    "admin_graphql_api_customer_id",
    "delivery_policy",
    "status",
    "admin_graphql_api_origin_order_id",
    "origin_order_id"
})
public class SubscriptionContractInfo implements Serializable
{

    @JsonProperty("admin_graphql_api_id")
    private String adminGraphqlApiId;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("billing_policy")
    private BillingPolicy billingPolicy;
    @JsonProperty("currency_code")
    private String currencyCode;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("admin_graphql_api_customer_id")
    private String adminGraphqlApiCustomerId;
    @JsonProperty("delivery_policy")
    private DeliveryPolicy deliveryPolicy;
    @JsonProperty("status")
    private String status;
    @JsonProperty("admin_graphql_api_origin_order_id")
    private String adminGraphqlApiOriginOrderId;
    @JsonProperty("origin_order_id")
    private Long originOrderId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2825619999583465181L;

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlApiId() {
        return adminGraphqlApiId;
    }

    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("billing_policy")
    public BillingPolicy getBillingPolicy() {
        return billingPolicy;
    }

    @JsonProperty("billing_policy")
    public void setBillingPolicy(BillingPolicy billingPolicy) {
        this.billingPolicy = billingPolicy;
    }

    @JsonProperty("currency_code")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currency_code")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("customer_id")
    public Long getCustomerId() {
        return customerId;
    }

    @JsonProperty("customer_id")
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("admin_graphql_api_customer_id")
    public String getAdminGraphqlApiCustomerId() {
        return adminGraphqlApiCustomerId;
    }

    @JsonProperty("admin_graphql_api_customer_id")
    public void setAdminGraphqlApiCustomerId(String adminGraphqlApiCustomerId) {
        this.adminGraphqlApiCustomerId = adminGraphqlApiCustomerId;
    }

    @JsonProperty("delivery_policy")
    public DeliveryPolicy getDeliveryPolicy() {
        return deliveryPolicy;
    }

    @JsonProperty("delivery_policy")
    public void setDeliveryPolicy(DeliveryPolicy deliveryPolicy) {
        this.deliveryPolicy = deliveryPolicy;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("admin_graphql_api_origin_order_id")
    public String getAdminGraphqlApiOriginOrderId() {
        return adminGraphqlApiOriginOrderId;
    }

    @JsonProperty("admin_graphql_api_origin_order_id")
    public void setAdminGraphqlApiOriginOrderId(String adminGraphqlApiOriginOrderId) {
        this.adminGraphqlApiOriginOrderId = adminGraphqlApiOriginOrderId;
    }

    @JsonProperty("origin_order_id")
    public Long getOriginOrderId() {
        return originOrderId;
    }

    @JsonProperty("origin_order_id")
    public void setOriginOrderId(Long originOrderId) {
        this.originOrderId = originOrderId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminGraphqlApiId", adminGraphqlApiId).append("id", id).append("billingPolicy", billingPolicy).append("currencyCode", currencyCode).append("customerId", customerId).append("adminGraphqlApiCustomerId", adminGraphqlApiCustomerId).append("deliveryPolicy", deliveryPolicy).append("status", status).append("adminGraphqlApiOriginOrderId", adminGraphqlApiOriginOrderId).append("originOrderId", originOrderId).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryPolicy).append(adminGraphqlApiOriginOrderId).append(adminGraphqlApiCustomerId).append(customerId).append(id).append(additionalProperties).append(adminGraphqlApiId).append(currencyCode).append(billingPolicy).append(originOrderId).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SubscriptionContractInfo) == false) {
            return false;
        }
        SubscriptionContractInfo rhs = ((SubscriptionContractInfo) other);
        return new EqualsBuilder().append(deliveryPolicy, rhs.deliveryPolicy).append(adminGraphqlApiOriginOrderId, rhs.adminGraphqlApiOriginOrderId).append(adminGraphqlApiCustomerId, rhs.adminGraphqlApiCustomerId).append(customerId, rhs.customerId).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(adminGraphqlApiId, rhs.adminGraphqlApiId).append(currencyCode, rhs.currencyCode).append(billingPolicy, rhs.billingPolicy).append(originOrderId, rhs.originOrderId).append(status, rhs.status).isEquals();
    }

}
