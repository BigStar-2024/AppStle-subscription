package com.et.web.rest.vm.shippingprofile;

import com.et.web.rest.vm.DeliveryMethodInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZoneInfo implements Serializable {

    private List<CountryInfoV3> countries = new ArrayList<>();
    private Boolean restOfWorld;
    private List<DeliveryMethodInfoV3> deliveryMethods = new ArrayList<>();

    public ZoneInfo() {
    }

    public ZoneInfo(List<CountryInfoV3> countries, Boolean restOfWorld, List<DeliveryMethodInfoV3> deliveryMethods) {
        this.countries = countries;
        this.restOfWorld = restOfWorld;
        this.deliveryMethods = deliveryMethods;
    }

    public List<CountryInfoV3> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryInfoV3> countries) {
        this.countries = countries;
    }

    public Boolean getRestOfWorld() {
        return restOfWorld;
    }

    public void setRestOfWorld(Boolean restOfWorld) {
        this.restOfWorld = restOfWorld;
    }

    public List<DeliveryMethodInfoV3> getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(List<DeliveryMethodInfoV3> deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZoneInfo zoneInfo = (ZoneInfo) o;
        return Objects.equals(countries, zoneInfo.countries) && Objects.equals(restOfWorld, zoneInfo.restOfWorld) && Objects.equals(deliveryMethods, zoneInfo.deliveryMethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countries, restOfWorld, deliveryMethods);
    }
}
