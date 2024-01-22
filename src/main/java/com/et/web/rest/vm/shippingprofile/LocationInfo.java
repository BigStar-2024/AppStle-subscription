package com.et.web.rest.vm.shippingprofile;

import java.util.ArrayList;
import java.util.List;

public class LocationInfo {

    private String locationId;

    private List<CountryInfo> countryInfos = new ArrayList<CountryInfo>();

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<CountryInfo> getCountryInfos() {
        return countryInfos;
    }

    public void setCountryInfos(List<CountryInfo> countryInfos) {
        this.countryInfos = countryInfos;
    }
}
