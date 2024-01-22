package com.et.web.rest.vm.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarrierServiceDataVM implements Serializable {

    private List<String> carrierServiceOptions = new ArrayList<>();

    public List<String> getCarrierServiceOptions() {
        return carrierServiceOptions;
    }

    public void setCarrierServiceOptions(List<String> carrierServiceOptions) {
        this.carrierServiceOptions = carrierServiceOptions;
    }

    @Override
    public String toString() {
        return "CarrierServiceDataVM{" +
            "carrierServiceOptions=" + carrierServiceOptions +
            '}';
    }
}
