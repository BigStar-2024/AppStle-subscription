package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A MemberOnly.
 */
@Entity
@Table(name = "member_only")
public class MemberOnly implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "selling_plan_id")
    private String sellingPlanId;

    @Column(name = "tags")
    private String tags;

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

    public MemberOnly shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSellingPlanId() {
        return sellingPlanId;
    }

    public MemberOnly sellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
        return this;
    }

    public void setSellingPlanId(String sellingPlanId) {
        this.sellingPlanId = sellingPlanId;
    }

    public String getTags() {
        return tags;
    }

    public MemberOnly tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberOnly)) {
            return false;
        }
        return id != null && id.equals(((MemberOnly) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberOnly{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", sellingPlanId='" + getSellingPlanId() + "'" +
            ", tags='" + getTags() + "'" +
            "}";
    }
}
