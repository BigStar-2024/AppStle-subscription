
package com.et.web.rest.vm;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "admin_graphql_api_id",
    "idempotency_key",
    "order_id",
    "admin_graphql_api_order_id",
    "subscription_contract_id",
    "admin_graphql_api_subscription_contract_id",
    "ready",
    "error_message",
    "error_code"
})
public class SubscriptionBillingAttemptInfo implements Serializable
{

    @JsonProperty("id")
    private Long id;
    @JsonProperty("admin_graphql_api_id")
    private String adminGraphqlApiId;
    @JsonProperty("idempotency_key")
    private String idempotencyKey;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("admin_graphql_api_order_id")
    private String adminGraphqlApiOrderId;
    @JsonProperty("subscription_contract_id")
    private Long subscriptionContractId;
    @JsonProperty("admin_graphql_api_subscription_contract_id")
    private String adminGraphqlApiSubscriptionContractId;
    @JsonProperty("ready")
    private Boolean ready;
    @JsonProperty("error_message")
    private Object errorMessage;
    @JsonProperty("error_code")
    private Object errorCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6074692196585660637L;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlApiId() {
        return adminGraphqlApiId;
    }

    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
    }

    @JsonProperty("idempotency_key")
    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    @JsonProperty("idempotency_key")
    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    @JsonProperty("order_id")
    public Long getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("admin_graphql_api_order_id")
    public String getAdminGraphqlApiOrderId() {
        return adminGraphqlApiOrderId;
    }

    @JsonProperty("admin_graphql_api_order_id")
    public void setAdminGraphqlApiOrderId(String adminGraphqlApiOrderId) {
        this.adminGraphqlApiOrderId = adminGraphqlApiOrderId;
    }

    @JsonProperty("subscription_contract_id")
    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    @JsonProperty("subscription_contract_id")
    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    @JsonProperty("admin_graphql_api_subscription_contract_id")
    public String getAdminGraphqlApiSubscriptionContractId() {
        return adminGraphqlApiSubscriptionContractId;
    }

    @JsonProperty("admin_graphql_api_subscription_contract_id")
    public void setAdminGraphqlApiSubscriptionContractId(String adminGraphqlApiSubscriptionContractId) {
        this.adminGraphqlApiSubscriptionContractId = adminGraphqlApiSubscriptionContractId;
    }

    @JsonProperty("ready")
    public Boolean getReady() {
        return ready;
    }

    @JsonProperty("ready")
    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    @JsonProperty("error_message")
    public Object getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("error_message")
    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("error_code")
    public Object getErrorCode() {
        return errorCode;
    }

    @JsonProperty("error_code")
    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
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
        return new ToStringBuilder(this).append("id", id).append("adminGraphqlApiId", adminGraphqlApiId).append("idempotencyKey", idempotencyKey).append("orderId", orderId).append("adminGraphqlApiOrderId", adminGraphqlApiOrderId).append("subscriptionContractId", subscriptionContractId).append("adminGraphqlApiSubscriptionContractId", adminGraphqlApiSubscriptionContractId).append("ready", ready).append("errorMessage", errorMessage).append("errorCode", errorCode).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(adminGraphqlApiOrderId).append(subscriptionContractId).append(adminGraphqlApiSubscriptionContractId).append(orderId).append(idempotencyKey).append(ready).append(errorMessage).append(errorCode).append(id).append(additionalProperties).append(adminGraphqlApiId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SubscriptionBillingAttemptInfo) == false) {
            return false;
        }
        SubscriptionBillingAttemptInfo rhs = ((SubscriptionBillingAttemptInfo) other);
        return new EqualsBuilder().append(adminGraphqlApiOrderId, rhs.adminGraphqlApiOrderId).append(subscriptionContractId, rhs.subscriptionContractId).append(adminGraphqlApiSubscriptionContractId, rhs.adminGraphqlApiSubscriptionContractId).append(orderId, rhs.orderId).append(idempotencyKey, rhs.idempotencyKey).append(ready, rhs.ready).append(errorMessage, rhs.errorMessage).append(errorCode, rhs.errorCode).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(adminGraphqlApiId, rhs.adminGraphqlApiId).isEquals();
    }

}
