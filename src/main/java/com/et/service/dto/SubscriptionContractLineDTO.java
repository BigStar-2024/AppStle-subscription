package com.et.service.dto;

import com.shopify.java.graphql.client.type.AttributeInput;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SubscriptionContractLineDTO {

    @NotNull
    private int quantity;

    @NotNull
    private String variantId;

    private String productId;

    private String sellingPlanId;

    private Double currentPrice;

    private Double unitPrice;

    private List<AttributeInput> customAttributes;

    public int getQuantity() {
        return quantity;
    }

    public SubscriptionContractLineDTO setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getVariantId() {
        return variantId;
    }

    public SubscriptionContractLineDTO setVariantId(String variantId) {
        this.variantId = variantId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public SubscriptionContractLineDTO setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getSellingPlanId() {
        return sellingPlanId;
    }

    public SubscriptionContractLineDTO setSellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
        return this;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public SubscriptionContractLineDTO setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public SubscriptionContractLineDTO setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public List<AttributeInput> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<AttributeInput> customAttributes) {
        this.customAttributes = customAttributes;
    }
}
