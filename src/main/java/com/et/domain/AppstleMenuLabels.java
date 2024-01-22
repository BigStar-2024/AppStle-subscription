package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A AppstleMenuLabels.
 */
@Entity
@Table(name = "appstle_menu_labels")
public class AppstleMenuLabels implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Lob
    @Column(name = "custom_css")
    private String customCss;

    @Lob
    @Column(name = "labels")
    private String labels;

    @Column(name = "see_more")
    private String seeMore;

    @Column(name = "no_data_found")
    private String noDataFound;

    @Column(name = "product_details")
    private String productDetails;

    @Column(name = "edit_quantity")
    private String editQuantity;

    @Column(name = "add_to_cart")
    private String addToCart;

    @Column(name = "product_added_successfully")
    private String productAddedSuccessfully;

    @Column(name = "went_wrong")
    private String wentWrong;

    @Column(name = "results")
    private String results;

    @Column(name = "adding")
    private String adding;

    @Column(name = "subscribe")
    private String subscribe;

    @Column(name = "not_available")
    private String notAvailable;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public AppstleMenuLabels shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCustomCss() {
        return customCss;
    }

    public AppstleMenuLabels customCss(String customCss) {
        this.customCss = customCss;
        return this;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getLabels() {
        return labels;
    }

    public AppstleMenuLabels labels(String labels) {
        this.labels = labels;
        return this;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getSeeMore() {
        return seeMore;
    }

    public AppstleMenuLabels seeMore(String seeMore) {
        this.seeMore = seeMore;
        return this;
    }

    public void setSeeMore(String seeMore) {
        this.seeMore = seeMore;
    }

    public String getNoDataFound() {
        return noDataFound;
    }

    public AppstleMenuLabels noDataFound(String noDataFound) {
        this.noDataFound = noDataFound;
        return this;
    }

    public void setNoDataFound(String noDataFound) {
        this.noDataFound = noDataFound;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public AppstleMenuLabels productDetails(String productDetails) {
        this.productDetails = productDetails;
        return this;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getEditQuantity() {
        return editQuantity;
    }

    public AppstleMenuLabels editQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
        return this;
    }

    public void setEditQuantity(String editQuantity) {
        this.editQuantity = editQuantity;
    }

    public String getAddToCart() {
        return addToCart;
    }

    public AppstleMenuLabels addToCart(String addToCart) {
        this.addToCart = addToCart;
        return this;
    }

    public void setAddToCart(String addToCart) {
        this.addToCart = addToCart;
    }

    public String getProductAddedSuccessfully() {
        return productAddedSuccessfully;
    }

    public AppstleMenuLabels productAddedSuccessfully(String productAddedSuccessfully) {
        this.productAddedSuccessfully = productAddedSuccessfully;
        return this;
    }

    public void setProductAddedSuccessfully(String productAddedSuccessfully) {
        this.productAddedSuccessfully = productAddedSuccessfully;
    }

    public String getWentWrong() {
        return wentWrong;
    }

    public AppstleMenuLabels wentWrong(String wentWrong) {
        this.wentWrong = wentWrong;
        return this;
    }

    public void setWentWrong(String wentWrong) {
        this.wentWrong = wentWrong;
    }

    public String getResults() {
        return results;
    }

    public AppstleMenuLabels results(String results) {
        this.results = results;
        return this;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getAdding() {
        return adding;
    }

    public AppstleMenuLabels adding(String adding) {
        this.adding = adding;
        return this;
    }

    public void setAdding(String adding) {
        this.adding = adding;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public AppstleMenuLabels subscribe(String subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getNotAvailable() {
        return notAvailable;
    }

    public AppstleMenuLabels notAvailable(String notAvailable) {
        this.notAvailable = notAvailable;
        return this;
    }

    public void setNotAvailable(String notAvailable) {
        this.notAvailable = notAvailable;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppstleMenuLabels)) {
            return false;
        }
        return id != null && id.equals(((AppstleMenuLabels) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppstleMenuLabels{" +
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
