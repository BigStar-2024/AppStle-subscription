package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.MemberOnly} entity.
 */
public class MemberOnlyDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private String sellingPlanId;

    private String tags;

    
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

    public String getSellingPlanId() {
        return sellingPlanId;
    }

    public void setSellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberOnlyDTO)) {
            return false;
        }

        return id != null && id.equals(((MemberOnlyDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberOnlyDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", sellingPlanId='" + getSellingPlanId() + "'" +
            ", tags='" + getTags() + "'" +
            "}";
    }
}
