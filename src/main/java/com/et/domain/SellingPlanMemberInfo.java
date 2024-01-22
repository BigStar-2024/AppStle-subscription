package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SellingPlanMemberInfo.
 */
@Entity
@Table(name = "selling_plan_member_info")
public class SellingPlanMemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "selling_plan_id")
    private Long sellingPlanId;

    @Column(name = "enable_member_inclusive_tag")
    private Boolean enableMemberInclusiveTag;

    @Column(name = "member_inclusive_tags")
    private String memberInclusiveTags;

    @Column(name = "enable_member_exclusive_tag")
    private Boolean enableMemberExclusiveTag;

    @Column(name = "member_exclusive_tags")
    private String memberExclusiveTags;

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

    public SellingPlanMemberInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public SellingPlanMemberInfo subscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getSellingPlanId() {
        return sellingPlanId;
    }

    public SellingPlanMemberInfo sellingPlanId(Long sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
        return this;
    }

    public void setSellingPlanId(Long sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    public Boolean isEnableMemberInclusiveTag() {
        return enableMemberInclusiveTag;
    }

    public SellingPlanMemberInfo enableMemberInclusiveTag(Boolean enableMemberInclusiveTag) {
        this.enableMemberInclusiveTag = enableMemberInclusiveTag;
        return this;
    }

    public void setEnableMemberInclusiveTag(Boolean enableMemberInclusiveTag) {
        this.enableMemberInclusiveTag = enableMemberInclusiveTag;
    }

    public String getMemberInclusiveTags() {
        return memberInclusiveTags;
    }

    public SellingPlanMemberInfo memberInclusiveTags(String memberInclusiveTags) {
        this.memberInclusiveTags = memberInclusiveTags;
        return this;
    }

    public void setMemberInclusiveTags(String memberInclusiveTags) {
        this.memberInclusiveTags = memberInclusiveTags;
    }

    public Boolean isEnableMemberExclusiveTag() {
        return enableMemberExclusiveTag;
    }

    public SellingPlanMemberInfo enableMemberExclusiveTag(Boolean enableMemberExclusiveTag) {
        this.enableMemberExclusiveTag = enableMemberExclusiveTag;
        return this;
    }

    public void setEnableMemberExclusiveTag(Boolean enableMemberExclusiveTag) {
        this.enableMemberExclusiveTag = enableMemberExclusiveTag;
    }

    public String getMemberExclusiveTags() {
        return memberExclusiveTags;
    }

    public SellingPlanMemberInfo memberExclusiveTags(String memberExclusiveTags) {
        this.memberExclusiveTags = memberExclusiveTags;
        return this;
    }

    public void setMemberExclusiveTags(String memberExclusiveTags) {
        this.memberExclusiveTags = memberExclusiveTags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellingPlanMemberInfo)) {
            return false;
        }
        return id != null && id.equals(((SellingPlanMemberInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellingPlanMemberInfo{" +
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
