
package com.et.web.rest.vm.bundling;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "discountCode",
    "errorMessage",
    "discountNeeded"
})
@Generated("jsonschema2pojo")
public class DiscountCodeResponse implements Serializable {

    @JsonProperty("discountCode")
    private String discountCode;
    @JsonProperty("discountAmount")
    private Double discountAmount;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("discountNeeded")
    private Boolean discountNeeded;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 4809924178762814028L;

    /**
     * No args constructor for use in serialization
     */
    public DiscountCodeResponse() {
    }

    /**
     * @param discountCode
     * @param errorMessage
     * @param discountNeeded
     */
    public DiscountCodeResponse(String discountCode, String errorMessage, Boolean discountNeeded,Double discountAmount) {
        super();
        this.discountCode = discountCode;
        this.errorMessage = errorMessage;
        this.discountNeeded = discountNeeded;
        this.discountAmount = discountAmount;
    }

    @JsonProperty("discountCode")
    public String getDiscountCode() {
        return discountCode;
    }

    @JsonProperty("discountCode")
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("errorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("discountNeeded")
    public Boolean getDiscountNeeded() {
        return discountNeeded;
    }

    @JsonProperty("discountNeeded")
    public void setDiscountNeeded(Boolean discountNeeded) {
        this.discountNeeded = discountNeeded;
    }

    @JsonProperty("discountAmount")
    public Double getDiscountAmount() {
        return discountAmount;
    }

    @JsonProperty("discountAmount")
    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
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
        StringBuilder sb = new StringBuilder();
        sb.append(DiscountCodeResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("discountCode");
        sb.append('=');
        sb.append(((this.discountCode == null) ? "<null>" : this.discountCode));
        sb.append(',');
        sb.append("errorMessage");
        sb.append('=');
        sb.append(((this.errorMessage == null) ? "<null>" : this.errorMessage));
        sb.append(',');
        sb.append("discountNeeded");
        sb.append('=');
        sb.append(((this.discountNeeded == null) ? "<null>" : this.discountNeeded));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode()));
        result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
        result = ((result * 31) + ((this.discountCode == null) ? 0 : this.discountCode.hashCode()));
        result = ((result * 31) + ((this.discountNeeded == null) ? 0 : this.discountNeeded.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DiscountCodeResponse) == false) {
            return false;
        }
        DiscountCodeResponse rhs = ((DiscountCodeResponse) other);
        return (((((this.errorMessage == rhs.errorMessage) || ((this.errorMessage != null) && this.errorMessage.equals(rhs.errorMessage))) && ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null) && this.additionalProperties.equals(rhs.additionalProperties)))) && ((this.discountCode == rhs.discountCode) || ((this.discountCode != null) && this.discountCode.equals(rhs.discountCode)))) && ((this.discountNeeded == rhs.discountNeeded) || ((this.discountNeeded != null) && this.discountNeeded.equals(rhs.discountNeeded))));
    }

}
