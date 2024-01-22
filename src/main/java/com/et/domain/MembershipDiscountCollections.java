package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.CollectionType;

/**
 * A MembershipDiscountCollections.
 */
@Entity
@Table(name = "membership_discount_collections")
public class MembershipDiscountCollections implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Column(name = "membership_discount_id")
    private Long membershipDiscountId;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "collection_title")
    private String collectionTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_type")
    private CollectionType collectionType;

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

    public MembershipDiscountCollections shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getMembershipDiscountId() {
        return membershipDiscountId;
    }

    public MembershipDiscountCollections membershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
        return this;
    }

    public void setMembershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public MembershipDiscountCollections collectionId(Long collectionId) {
        this.collectionId = collectionId;
        return this;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public MembershipDiscountCollections collectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
        return this;
    }

    public void setCollectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public MembershipDiscountCollections collectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
        return this;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscountCollections)) {
            return false;
        }
        return id != null && id.equals(((MembershipDiscountCollections) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscountCollections{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", membershipDiscountId=" + getMembershipDiscountId() +
            ", collectionId=" + getCollectionId() +
            ", collectionTitle='" + getCollectionTitle() + "'" +
            ", collectionType='" + getCollectionType() + "'" +
            "}";
    }
}
