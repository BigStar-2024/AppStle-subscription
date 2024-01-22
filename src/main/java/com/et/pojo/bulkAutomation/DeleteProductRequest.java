package com.et.pojo.bulkAutomation;

public class DeleteProductRequest {

    private String shop;
    private Long contractId;
    private Long variantId;
    private Boolean deleteRemovedProductsFromShopify;
    private boolean isLast;

    public DeleteProductRequest() {
    }

    public DeleteProductRequest(String shop, Long contractId, Long variantId, Boolean deleteRemovedProductsFromShopify) {
        this.shop = shop;
        this.contractId = contractId;
        this.variantId = variantId;
        this.deleteRemovedProductsFromShopify = deleteRemovedProductsFromShopify;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Boolean isDeleteRemovedProductsFromShopify() {
        return deleteRemovedProductsFromShopify;
    }

    public void setDeleteRemovedProductsFromShopify(Boolean deleteRemovedProductsFromShopify) {
        this.deleteRemovedProductsFromShopify = deleteRemovedProductsFromShopify;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
