package com.et.web.rest.vm.shippingprofile;

import java.io.Serializable;
import java.util.Objects;

public class LocationInfoV3 implements Serializable {

    private String label;
    private String value;

    public LocationInfoV3() {
    }

    public LocationInfoV3(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationInfoV3 that = (LocationInfoV3) o;
        return Objects.equals(label, that.label) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, value);
    }
}
