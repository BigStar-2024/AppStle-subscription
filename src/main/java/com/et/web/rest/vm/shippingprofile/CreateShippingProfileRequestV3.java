package com.et.web.rest.vm.shippingprofile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "sellingPlanGroups",
    "locations",
    "zones"
})
@Generated("jsonschema2pojo")
public class CreateShippingProfileRequestV3 implements Serializable {

    @JsonProperty("name")
    private String name;

    private String id;

    private Long profileId;

    @JsonProperty("sellingPlanGroups")
    private List<SellingPlanGroupInfo> sellingPlanGroups = new ArrayList<>();

    @JsonProperty("locationInfos")
    private List<LocationInfoV3> locations = new ArrayList<>();

    @JsonProperty("zones")
    private List<ZoneInfo> zones = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     */
    public CreateShippingProfileRequestV3() {
    }

    /**
     * @param name
     * @param id
     * @param sellingPlanGroups
     * @param locations
     * @param zones
     */

    public CreateShippingProfileRequestV3(String name, String id, List<SellingPlanGroupInfo> sellingPlanGroups, List<LocationInfoV3> locations, List<ZoneInfo> zones) {
        super();
        this.name = name;
        this.id = id;
        this.sellingPlanGroups = sellingPlanGroups;
        this.locations = locations;
        this.zones = zones;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("sellingPlanGroups")
    public List<SellingPlanGroupInfo> getSellingPlanGroups() {
        return sellingPlanGroups;
    }

    @JsonProperty("sellingPlanGroups")
    public void setSellingPlanGroups(List<SellingPlanGroupInfo> sellingPlanGroups) {
        this.sellingPlanGroups = sellingPlanGroups;
    }

    @JsonProperty("locations")
    public List<LocationInfoV3> getLocations() {
        return locations;
    }

    @JsonProperty("locations")
    public void setLocations(List<LocationInfoV3> locations) {
        this.locations = locations;
    }

    @JsonProperty("zones")
    public List<ZoneInfo> getZones() {
        return zones;
    }

    @JsonProperty("zones")
    public void setZones(List<ZoneInfo> zones) {
        this.zones = zones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
