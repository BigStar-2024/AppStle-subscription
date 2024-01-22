package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class VariantQuantityInfo {
    private List<VariantQuantity> variantQuantityList = new ArrayList<>();

    public List<VariantQuantity> getVariantQuantityList() {
        return variantQuantityList;
    }

    public void setVariantQuantityList(List<VariantQuantity> variantQuantityList) {
        this.variantQuantityList = variantQuantityList;
    }
}
