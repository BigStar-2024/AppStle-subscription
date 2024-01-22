package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.et.domain.enumeration.FrequencyIntervalUnit;

/**
 * A DTO for the {@link com.et.domain.SubscriptionContractSettings} entity.
 */
public class SubscriptionContractSettingsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private String productId;

    @NotNull
    private Integer endsOnCount;

    private FrequencyIntervalUnit endsOnInterval;

    
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getEndsOnCount() {
        return endsOnCount;
    }

    public void setEndsOnCount(Integer endsOnCount) {
        this.endsOnCount = endsOnCount;
    }

    public FrequencyIntervalUnit getEndsOnInterval() {
        return endsOnInterval;
    }

    public void setEndsOnInterval(FrequencyIntervalUnit endsOnInterval) {
        this.endsOnInterval = endsOnInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractSettingsDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionContractSettingsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractSettingsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", productId='" + getProductId() + "'" +
            ", endsOnCount=" + getEndsOnCount() +
            ", endsOnInterval='" + getEndsOnInterval() + "'" +
            "}";
    }
}
