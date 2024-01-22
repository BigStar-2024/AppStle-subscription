package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SubscriptionCustomCss.
 */
@Entity
@Table(name = "subscription_custom_css")
public class SubscriptionCustomCss implements Serializable {

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
    @Column(name = "customer_poratl_css")
    private String customerPoratlCSS;

    @Lob
    @Column(name = "bundling_css")
    private String bundlingCSS;

    @Lob
    @Column(name = "bundling_iframe_css")
    private String bundlingIframeCSS;

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

    public SubscriptionCustomCss shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getCustomCss() {
        return customCss;
    }

    public SubscriptionCustomCss customCss(String customCss) {
        this.customCss = customCss;
        return this;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public String getCustomerPoratlCSS() {
        return customerPoratlCSS;
    }

    public SubscriptionCustomCss customerPoratlCSS(String customerPoratlCSS) {
        this.customerPoratlCSS = customerPoratlCSS;
        return this;
    }

    public void setCustomerPoratlCSS(String customerPoratlCSS) {
        this.customerPoratlCSS = customerPoratlCSS;
    }

    public String getBundlingCSS() {
        return bundlingCSS;
    }

    public SubscriptionCustomCss bundlingCSS(String bundlingCSS) {
        this.bundlingCSS = bundlingCSS;
        return this;
    }

    public void setBundlingCSS(String bundlingCSS) {
        this.bundlingCSS = bundlingCSS;
    }

    public String getBundlingIframeCSS() {
        return bundlingIframeCSS;
    }

    public SubscriptionCustomCss bundlingIframeCSS(String bundlingIframeCSS) {
        this.bundlingIframeCSS = bundlingIframeCSS;
        return this;
    }

    public void setBundlingIframeCSS(String bundlingIframeCSS) {
        this.bundlingIframeCSS = bundlingIframeCSS;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionCustomCss)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionCustomCss) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionCustomCss{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", customCss='" + getCustomCss() + "'" +
            ", customerPoratlCSS='" + getCustomerPoratlCSS() + "'" +
            ", bundlingCSS='" + getBundlingCSS() + "'" +
            ", bundlingIframeCSS='" + getBundlingIframeCSS() + "'" +
            "}";
    }
}
