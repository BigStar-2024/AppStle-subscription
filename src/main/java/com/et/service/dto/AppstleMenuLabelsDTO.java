package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.AppstleMenuLabels} entity.
 */
public class AppstleMenuLabelsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String customCss;

    @Lob
    private String labels;

    private String seeMore;

    private String noDataFound;

    private String productDetails;

    private String editQuantity;

    private String addToCart;

    private String productAddedSuccessfully;

    private String wentWrong;

    private String results;

    private String adding;

    private String subscribe;

    private String notAvailable;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getSeeMore() {
        return seeMore;
    }

    public void setSeeMore(String seeMore) {
        this.seeMore = seeMore;
    }

    public String getNoDataFound() {
        return noDataFound;
    }

    public void setNoDataFound(String noDataFound) {
        this.noDataFound = noDataFound;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getEditQuantity() {
        return editQuantity;
    }

    public void setEditQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
    }

    public String getAddToCart() {
        return addToCart;
    }

    public void setAddToCart(String addToCart) {
        this.addToCart = addToCart;
    }

    public String getProductAddedSuccessfully() {
        return productAddedSuccessfully;
    }

    public void setProductAddedSuccessfully(String productAddedSuccessfully) {
        this.productAddedSuccessfully = productAddedSuccessfully;
    }

    public String getWentWrong() {
        return wentWrong;
    }

    public void setWentWrong(String wentWrong) {
        this.wentWrong = wentWrong;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getAdding() {
        return adding;
    }

    public void setAdding(String adding) {
        this.adding = adding;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(String notAvailable) {
        this.notAvailable = notAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppstleMenuLabelsDTO)) {
            return false;
        }

        return id != null && id.equals(((AppstleMenuLabelsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppstleMenuLabelsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", customCss='" + getCustomCss() + "'" +
            ", labels='" + getLabels() + "'" +
            ", seeMore='" + getSeeMore() + "'" +
            ", noDataFound='" + getNoDataFound() + "'" +
            ", productDetails='" + getProductDetails() + "'" +
            ", editQuantity='" + getEditQuantity() + "'" +
            ", addToCart='" + getAddToCart() + "'" +
            ", productAddedSuccessfully='" + getProductAddedSuccessfully() + "'" +
            ", wentWrong='" + getWentWrong() + "'" +
            ", results='" + getResults() + "'" +
            ", adding='" + getAdding() + "'" +
            ", subscribe='" + getSubscribe() + "'" +
            ", notAvailable='" + getNotAvailable() + "'" +
            "}";
    }
}
