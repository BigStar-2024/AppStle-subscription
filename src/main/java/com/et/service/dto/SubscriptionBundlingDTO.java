package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.BuildABoxRedirect;
import com.et.domain.enumeration.ProductViewSettings;
import com.et.domain.enumeration.BuildABoxType;

/**
 * A DTO for the {@link com.et.domain.SubscriptionBundling} entity.
 */
public class SubscriptionBundlingDTO implements Serializable {

    private Long id;

    public SubscriptionBundlingDTO() {
    }

    public SubscriptionBundlingDTO(Long id, @NotNull String shop, Boolean subscriptionBundlingEnabled, Long subscriptionId, Integer minProductCount, Integer maxProductCount, Double discount, String groupName, BuildABoxRedirect bundleRedirect, String customRedirectURL) {
        this.id = id;
        this.shop = shop;
        this.subscriptionBundlingEnabled = subscriptionBundlingEnabled;
        this.subscriptionId = subscriptionId;
        this.minProductCount = minProductCount;
        this.maxProductCount = maxProductCount;
        this.discount = discount;
        this.groupName = groupName;
        this.bundleRedirect = bundleRedirect == null ? BuildABoxRedirect.CHECKOUT : bundleRedirect;
        this.customRedirectURL = customRedirectURL;
    }

    @NotNull
    private String shop;

    private Boolean subscriptionBundlingEnabled;

    private Long subscriptionId;

    private Integer minProductCount;

    private Integer maxProductCount;

    private Double discount;

    private String uniqueRef;

    private String groupName;

    private String subscriptionBundleLink;

    private BuildABoxRedirect bundleRedirect;

    private String customRedirectURL;

    private Double minOrderAmount;

    @Lob
    private String tieredDiscount;

    private ProductViewSettings productViewStyle;

    private BuildABoxType buildABoxType;

    @Lob
    private String singleProductSettings;

    @Lob
    private String subscriptionGroup;

    @Lob
    private String bundleTopHtml;

    @Lob
    private String bundleBottomHtml;

    private String proceedToCheckoutButtonText;

    private String chooseProductsText;

    private String name;


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

    public Boolean isSubscriptionBundlingEnabled() {
        return subscriptionBundlingEnabled;
    }

    public void setSubscriptionBundlingEnabled(Boolean subscriptionBundlingEnabled) {
        this.subscriptionBundlingEnabled = subscriptionBundlingEnabled;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getMinProductCount() {
        return minProductCount;
    }

    public void setMinProductCount(Integer minProductCount) {
        this.minProductCount = minProductCount;
    }

    public Integer getMaxProductCount() {
        return maxProductCount;
    }

    public void setMaxProductCount(Integer maxProductCount) {
        this.maxProductCount = maxProductCount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getUniqueRef() {
        return uniqueRef;
    }

    public void setUniqueRef(String uniqueRef) {
        this.uniqueRef = uniqueRef;
    }

    public String getSubscriptionBundleLink() {
        return subscriptionBundleLink;
    }

    public void setSubscriptionBundleLink(String subscriptionBundleLink) {
        this.subscriptionBundleLink = subscriptionBundleLink;
    }

    public BuildABoxRedirect getBundleRedirect() {
        return bundleRedirect;
    }

    public void setBundleRedirect(BuildABoxRedirect bundleRedirect) {
        this.bundleRedirect = bundleRedirect;
    }

    public String getCustomRedirectURL() {
        return customRedirectURL;
    }

    public void setCustomRedirectURL(String customRedirectURL) {
        this.customRedirectURL = customRedirectURL;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getTieredDiscount() {
        return tieredDiscount;
    }

    public void setTieredDiscount(String tieredDiscount) {
        this.tieredDiscount = tieredDiscount;
    }

    public ProductViewSettings getProductViewStyle() {
        return productViewStyle;
    }

    public void setProductViewStyle(ProductViewSettings productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    public BuildABoxType getBuildABoxType() {
        return buildABoxType;
    }

    public void setBuildABoxType(BuildABoxType buildABoxType) {
        this.buildABoxType = buildABoxType;
    }

    public String getSingleProductSettings() {
        return singleProductSettings;
    }

    public void setSingleProductSettings(String singleProductSettings) {
        this.singleProductSettings = singleProductSettings;
    }

    public String getSubscriptionGroup() {
      return subscriptionGroup;
    }

    public void setSubscriptionGroup(String subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
    }

    public String getBundleTopHtml() {
        return bundleTopHtml;
    }

    public void setBundleTopHtml(String bundleTopHtml) {
        this.bundleTopHtml = bundleTopHtml;
    }

    public String getBundleBottomHtml() {
        return bundleBottomHtml;
    }

    public void setBundleBottomHtml(String bundleBottomHtml) {
        this.bundleBottomHtml = bundleBottomHtml;
    }

    public String getProceedToCheckoutButtonText() {
        return proceedToCheckoutButtonText;
    }

    public void setProceedToCheckoutButtonText(String proceedToCheckoutButtonText) {
        this.proceedToCheckoutButtonText = proceedToCheckoutButtonText;
    }

    public String getChooseProductsText() {
        return chooseProductsText;
    }

    public void setChooseProductsText(String chooseProductsText) {
        this.chooseProductsText = chooseProductsText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionBundlingDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionBundlingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionBundlingDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", subscriptionBundlingEnabled='" + isSubscriptionBundlingEnabled() + "'" +
            ", subscriptionId=" + getSubscriptionId() +
            ", minProductCount=" + getMinProductCount() +
            ", maxProductCount=" + getMaxProductCount() +
            ", discount=" + getDiscount() +
            ", uniqueRef='" + getUniqueRef() + "'" +
            ", bundleRedirect='" + getBundleRedirect() + "'" +
            ", customRedirectURL='" + getCustomRedirectURL() + "'" +
            ", minOrderAmount=" + getMinOrderAmount() +
            ", tieredDiscount='" + getTieredDiscount() + "'" +
            ", productViewStyle='" + getProductViewStyle() + "'" +
            ", buildABoxType='" + getBuildABoxType() + "'" +
            ", singleProductSettings='" + getSingleProductSettings() + "'" +
            ", subscriptionGroup='" + getSubscriptionGroup() + "'" +
            ", bundleTopHtml='" + getBundleTopHtml() + "'" +
            ", bundleBottomHtml='" + getBundleBottomHtml() + "'" +
            ", proceedToCheckoutButtonText='" + getProceedToCheckoutButtonText() + "'" +
            ", chooseProductsText='" + getChooseProductsText() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
