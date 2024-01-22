package com.et.api.klaviyo.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddToListRequest {

    @JsonProperty("profiles")
    private List<Profile> profiles = new ArrayList<>();

    public List<Profile> getProfiles() {
        return profiles;
    }

    public AddToListRequest setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }

    @Override
    public String toString() {
        return "AddToListRequest{" +
            "profiles=" + profiles +
            '}';
    }
}
