package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.FrequencyIntervalUnit;

/**
 * A SubscriptionContractSettings.
 */
@Entity
@Table(name = "subscription_contract_settings")
public class SubscriptionContractSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "product_id")
    private String productId;

    @NotNull
    @Column(name = "ends_on_count", nullable = false)
    private Integer endsOnCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ends_on_interval")
    private FrequencyIntervalUnit endsOnInterval;

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

    public SubscriptionContractSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getProductId() {
        return productId;
    }

    public SubscriptionContractSettings productId(String productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getEndsOnCount() {
        return endsOnCount;
    }

    public SubscriptionContractSettings endsOnCount(Integer endsOnCount) {
        this.endsOnCount = endsOnCount;
        return this;
    }

    public void setEndsOnCount(Integer endsOnCount) {
        this.endsOnCount = endsOnCount;
    }

    public FrequencyIntervalUnit getEndsOnInterval() {
        return endsOnInterval;
    }

    public SubscriptionContractSettings endsOnInterval(FrequencyIntervalUnit endsOnInterval) {
        this.endsOnInterval = endsOnInterval;
        return this;
    }

    public void setEndsOnInterval(FrequencyIntervalUnit endsOnInterval) {
        this.endsOnInterval = endsOnInterval;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractSettings)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionContractSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", productId='" + getProductId() + "'" +
            ", endsOnCount=" + getEndsOnCount() +
            ", endsOnInterval='" + getEndsOnInterval() + "'" +
            "}";
    }
}
