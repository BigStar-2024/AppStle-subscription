
package com.et.web.rest.vm.shippingprofile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "name",
    "locationInfos"
})
@Generated("jsonschema2pojo")
public class CreateShippingProfileRequestV2 implements Serializable {

    @JsonProperty("name")
    private String name;
    private String id;
    @JsonProperty("locationInfos")
    private List<LocationInfo> locationInfos = new ArrayList<LocationInfo>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6300511859876796795L;

    /**
     * No args constructor for use in serialization
     */
    public CreateShippingProfileRequestV2() {
    }

    /**
     * @param locationInfos
     * @param restOfWorld
     * @param name
     * @param countryInfos
     */
    public CreateShippingProfileRequestV2(List<CountryInfo> countryInfos, String name, List<LocationInfo> locationInfos) {
        super();
        this.name = name;
        this.locationInfos = locationInfos;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("locationInfos")
    public List<LocationInfo> getLocationInfos() {
        return locationInfos;
    }

    @JsonProperty("locationInfos")
    public void setLocationInfos(List<LocationInfo> locationInfos) {
        this.locationInfos = locationInfos;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
