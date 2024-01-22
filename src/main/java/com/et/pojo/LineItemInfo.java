package com.et.pojo;

import com.et.web.rest.vm.AttributeInfo;

import java.util.List;

public class LineItemInfo {

    private Long contractId;
    private Integer quantity;
    private String variantId;
    private List<AttributeInfo> customAttributes;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public List<AttributeInfo> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<AttributeInfo> customAttributes) {
        this.customAttributes = customAttributes;
    }
}
