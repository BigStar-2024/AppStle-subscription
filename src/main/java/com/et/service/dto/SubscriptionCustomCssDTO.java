package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.SubscriptionCustomCss} entity.
 */
public class SubscriptionCustomCssDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String customCss;

    @Lob
    private String customerPoratlCSS;

    @Lob
    private String bundlingCSS;

    @Lob
    private String bundlingIframeCSS;

    
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

    public String getCustomerPoratlCSS() {
        return customerPoratlCSS;
    }

    public void setCustomerPoratlCSS(String customerPoratlCSS) {
        this.customerPoratlCSS = customerPoratlCSS;
    }

    public String getBundlingCSS() {
        return bundlingCSS;
    }

    public void setBundlingCSS(String bundlingCSS) {
        this.bundlingCSS = bundlingCSS;
    }

    public String getBundlingIframeCSS() {
        return bundlingIframeCSS;
    }

    public void setBundlingIframeCSS(String bundlingIframeCSS) {
        this.bundlingIframeCSS = bundlingIframeCSS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionCustomCssDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionCustomCssDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionCustomCssDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", customCss='" + getCustomCss() + "'" +
            ", customerPoratlCSS='" + getCustomerPoratlCSS() + "'" +
            ", bundlingCSS='" + getBundlingCSS() + "'" +
            ", bundlingIframeCSS='" + getBundlingIframeCSS() + "'" +
            "}";
    }
}
