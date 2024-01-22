package com.et.pojo;

import java.util.List;

public class SubscriptionGroupProductDTO {
    private String productIds;
    private String variantIds;

    private List<String> deleteProductIds;
    private List<String> deleteVariantIds;

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getVariantIds() {
        return variantIds;
    }

    public void setVariantIds(String variantIds) {
        this.variantIds = variantIds;
    }

    public List<String> getDeleteProductIds() {
        return deleteProductIds;
    }

    public void setDeleteProductIds(List<String> deleteProductIds) {
        this.deleteProductIds = deleteProductIds;
    }

    public List<String> getDeleteVariantIds() {
        return deleteVariantIds;
    }

    public void setDeleteVariantIds(List<String> deleteVariantIds) {
        this.deleteVariantIds = deleteVariantIds;
    }

    public SubscriptionGroupProductDTO(String productIds, String variantIds, List<String> deleteProductIds, List<String> deleteVariantIds) {
        this.productIds = productIds;
        this.variantIds = variantIds;
        this.deleteProductIds = deleteProductIds;
        this.deleteVariantIds = deleteVariantIds;
    }

    public SubscriptionGroupProductDTO() {
    }

}
