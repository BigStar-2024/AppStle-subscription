
package com.et.web.rest.vm.shippingprofile;

import com.et.web.rest.vm.DeliveryMethodInfo;
import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "shouldIncludeAllProvince",
    "code",
    "deliveryMethodInfo",
    "restOfWorld",
    "provinceCode"
})
@Generated("jsonschema2pojo")
public class CountryInfo implements Serializable
{

    @JsonProperty("shouldIncludeAllProvince")
    private Boolean shouldIncludeAllProvince;
    @JsonProperty("code")
    private String code;
    @JsonProperty("deliveryMethodInfo")
    private List<DeliveryMethodInfo> deliveryMethodInfo = new ArrayList<DeliveryMethodInfo>();
    @JsonProperty("restOfWorld")
    private Boolean restOfWorld;
    @JsonProperty("provinceCode")
    private String provinceCode;


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -749046001766133812L;

    /**
     * No args constructor for use in serialization
     *
     */
    public CountryInfo() {
    }

    /**
     *
     * @param shouldIncludeAllProvince
     * @param code
     * @param restOfWorld
     * @param deliveryMethodInfo
     */
    public CountryInfo(Boolean shouldIncludeAllProvince, String code, List<DeliveryMethodInfo> deliveryMethodInfo, Boolean restOfWorld) {
        super();
        this.shouldIncludeAllProvince = shouldIncludeAllProvince;
        this.code = code;
        this.deliveryMethodInfo = deliveryMethodInfo;
        this.restOfWorld = restOfWorld;
    }

    @JsonProperty("shouldIncludeAllProvince")
    public Boolean getShouldIncludeAllProvince() {
        return shouldIncludeAllProvince;
    }

    @JsonProperty("shouldIncludeAllProvince")
    public void setShouldIncludeAllProvince(Boolean shouldIncludeAllProvince) {
        this.shouldIncludeAllProvince = shouldIncludeAllProvince;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("deliveryMethodInfo")
    public List<DeliveryMethodInfo> getDeliveryMethodInfo() {
        return deliveryMethodInfo;
    }

    @JsonProperty("deliveryMethodInfo")
    public void setDeliveryMethodInfo(List<DeliveryMethodInfo> deliveryMethodInfo) {
        this.deliveryMethodInfo = deliveryMethodInfo;
    }

    @JsonProperty("restOfWorld")
    public Boolean getRestOfWorld() {
        return restOfWorld;
    }

    @JsonProperty("restOfWorld")
    public void setRestOfWorld(Boolean restOfWorld) {
        this.restOfWorld = restOfWorld;
    }

    @JsonProperty("provinceCode")
    public String getProvinceCode() {
        return provinceCode;
    }

    @JsonProperty("provinceCode")
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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
        sb.append(CountryInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("shouldIncludeAllProvince");
        sb.append('=');
        sb.append(((this.shouldIncludeAllProvince == null)?"<null>":this.shouldIncludeAllProvince));
        sb.append(',');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null)?"<null>":this.code));
        sb.append(',');
        sb.append("deliveryMethodInfo");
        sb.append('=');
        sb.append(((this.deliveryMethodInfo == null)?"<null>":this.deliveryMethodInfo));
        sb.append(',');
        sb.append("restOfWorld");
        sb.append('=');
        sb.append(((this.restOfWorld == null)?"<null>":this.restOfWorld));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.shouldIncludeAllProvince == null)? 0 :this.shouldIncludeAllProvince.hashCode()));
        result = ((result* 31)+((this.code == null)? 0 :this.code.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.deliveryMethodInfo == null)? 0 :this.deliveryMethodInfo.hashCode()));
        result = ((result* 31)+((this.restOfWorld == null)? 0 :this.restOfWorld.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CountryInfo) == false) {
            return false;
        }
        CountryInfo rhs = ((CountryInfo) other);
        return ((((((this.shouldIncludeAllProvince == rhs.shouldIncludeAllProvince)||((this.shouldIncludeAllProvince!= null)&&this.shouldIncludeAllProvince.equals(rhs.shouldIncludeAllProvince)))&&((this.code == rhs.code)||((this.code!= null)&&this.code.equals(rhs.code))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.deliveryMethodInfo == rhs.deliveryMethodInfo)||((this.deliveryMethodInfo!= null)&&this.deliveryMethodInfo.equals(rhs.deliveryMethodInfo))))&&((this.restOfWorld == rhs.restOfWorld)||((this.restOfWorld!= null)&&this.restOfWorld.equals(rhs.restOfWorld))));
    }

}
