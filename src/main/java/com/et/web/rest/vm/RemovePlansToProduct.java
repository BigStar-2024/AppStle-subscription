
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
    "sellingPlanGroupId",
    "productId",
    "variantIds",
    "variantId"
})
public class RemovePlansToProduct implements Serializable
{

    @JsonProperty("sellingPlanGroupId")
    private String sellingPlanGroupId;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("variantIds")
    private List<String> variantIds = new ArrayList<String>();
    @JsonProperty("variantId")
    private String variantId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1895320900208579758L;

    @JsonProperty("sellingPlanGroupId")
    public String getSellingPlanGroupId() {
        return sellingPlanGroupId;
    }

    @JsonProperty("sellingPlanGroupId")
    public void setSellingPlanGroupId(String sellingPlanGroupId) {
        this.sellingPlanGroupId = sellingPlanGroupId;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("variantIds")
    public List<String> getVariantIds() {
        return variantIds;
    }

    @JsonProperty("variantIds")
    public void setVariantIds(List<String> variantIds) {
        this.variantIds = variantIds;
    }

    @JsonProperty("variantId")
    public String getVariantId() {
        return variantId;
    }

    @JsonProperty("variantId")
    public void setVariantId(String variantId) {
        this.variantId = variantId;
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
        return "RemovePlansToProduct{" +
            "sellingPlanGroupId='" + sellingPlanGroupId + '\'' +
            ", productId='" + productId + '\'' +
            ", variantIds=" + variantIds +
            ", variantId='" + variantId + '\'' +
            ", additionalProperties=" + additionalProperties +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemovePlansToProduct)) return false;

        RemovePlansToProduct that = (RemovePlansToProduct) o;

        if (!Objects.equals(sellingPlanGroupId, that.sellingPlanGroupId))
            return false;
        if (!Objects.equals(productId, that.productId)) return false;
        if (!Objects.equals(variantIds, that.variantIds)) return false;
        if (!Objects.equals(variantId, that.variantId)) return false;
        return Objects.equals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        int result = sellingPlanGroupId != null ? sellingPlanGroupId.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (variantIds != null ? variantIds.hashCode() : 0);
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
