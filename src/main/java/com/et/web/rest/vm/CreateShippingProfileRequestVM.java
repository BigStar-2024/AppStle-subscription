package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class CreateShippingProfileRequestVM {
    private String name;
    private Long id;
    private List<DeliveryMethodInfo> deliveryMethodInfo = new ArrayList<>();
    private List<DeliveryCountryInfo> countryInfos = new ArrayList<>();
    private List<LocationInfo> locationInfos = new ArrayList<>();
    private boolean restOfWorld;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeliveryMethodInfo> getDeliveryMethodInfo() {
        return deliveryMethodInfo;
    }

    public void setDeliveryMethodInfo(List<DeliveryMethodInfo> deliveryMethodInfo) {
        this.deliveryMethodInfo = deliveryMethodInfo;
    }

    public boolean isRestOfWorld() {
        return restOfWorld;
    }

    public void setRestOfWorld(boolean restOfWorld) {
        this.restOfWorld = restOfWorld;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LocationInfo> getLocationInfos() {
        return locationInfos;
    }

    public void setLocationInfos(List<LocationInfo> locationInfos) {
        this.locationInfos = locationInfos;
    }

    public List<DeliveryCountryInfo> getCountryInfos() {
        return countryInfos;
    }

    public void setCountryInfos(List<DeliveryCountryInfo> countryInfos) {
        this.countryInfos = countryInfos;
    }
}
