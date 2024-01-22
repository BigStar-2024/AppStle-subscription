package com.et.web.rest.vm;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ShopFulfillmentLocation {

    private String id;

    private String name;

    private boolean pickupEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPickupEnabled() {
        return pickupEnabled;
    }

    public void setPickupEnabled(boolean pickupEnabled) {
        this.pickupEnabled = pickupEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShopFulfillmentLocation that = (ShopFulfillmentLocation) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
