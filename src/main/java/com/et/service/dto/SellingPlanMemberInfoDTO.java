package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.SellingPlanMemberInfo} entity.
 */
public class SellingPlanMemberInfoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private Long subscriptionId;

    private Long sellingPlanId;

    private Boolean enableMemberInclusiveTag;

    private String memberInclusiveTags;

    private Boolean enableMemberExclusiveTag;

    private String memberExclusiveTags;

    
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

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getSellingPlanId() {
        return sellingPlanId;
    }

    public void setSellingPlanId(Long sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    public Boolean isEnableMemberInclusiveTag() {
        return enableMemberInclusiveTag;
    }

    public void setEnableMemberInclusiveTag(Boolean enableMemberInclusiveTag) {
        this.enableMemberInclusiveTag = enableMemberInclusiveTag;
    }

    public String getMemberInclusiveTags() {
        return memberInclusiveTags;
    }

    public void setMemberInclusiveTags(String memberInclusiveTags) {
        this.memberInclusiveTags = memberInclusiveTags;
    }

    public Boolean isEnableMemberExclusiveTag() {
        return enableMemberExclusiveTag;
    }

    public void setEnableMemberExclusiveTag(Boolean enableMemberExclusiveTag) {
        this.enableMemberExclusiveTag = enableMemberExclusiveTag;
    }

    public String getMemberExclusiveTags() {
        return memberExclusiveTags;
    }

    public void setMemberExclusiveTags(String memberExclusiveTags) {
        this.memberExclusiveTags = memberExclusiveTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellingPlanMemberInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((SellingPlanMemberInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellingPlanMemberInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", subscriptionId=" + getSubscriptionId() +
            ", sellingPlanId=" + getSellingPlanId() +
            ", enableMemberInclusiveTag='" + isEnableMemberInclusiveTag() + "'" +
            ", memberInclusiveTags='" + getMemberInclusiveTags() + "'" +
            ", enableMemberExclusiveTag='" + isEnableMemberExclusiveTag() + "'" +
            ", memberExclusiveTags='" + getMemberExclusiveTags() + "'" +
            "}";
    }
}
