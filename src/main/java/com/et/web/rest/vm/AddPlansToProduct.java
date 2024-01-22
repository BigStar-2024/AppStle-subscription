
package com.et.web.rest.vm;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productId",
    "plans"
})
public class AddPlansToProduct implements Serializable
{

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("variantId")
    private String variantId;
    @JsonProperty("plans")
    private List<Long> plans = new ArrayList<Long>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7147906833275803445L;

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("variantId")
    public String getVariantId() {
        return variantId;
    }

    @JsonProperty("variantId")
    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    @JsonProperty("plans")
    public List<Long> getPlans() {
        return plans;
    }

    @JsonProperty("plans")
    public void setPlans(List<Long> plans) {
        this.plans = plans;
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
        return "AddPlansToProduct{" +
            "productId='" + productId + '\'' +
            ", variantId='" + variantId + '\'' +
            ", plans=" + plans +
            ", additionalProperties=" + additionalProperties +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddPlansToProduct)) return false;

        AddPlansToProduct that = (AddPlansToProduct) o;

        if (!Objects.equals(productId, that.productId)) return false;
        if (!Objects.equals(variantId, that.variantId)) return false;
        if (!Objects.equals(plans, that.plans)) return false;
        return Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (plans != null ? plans.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
