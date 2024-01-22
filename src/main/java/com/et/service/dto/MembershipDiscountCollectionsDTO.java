package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.et.domain.enumeration.CollectionType;

/**
 * A DTO for the {@link com.et.domain.MembershipDiscountCollections} entity.
 */
public class MembershipDiscountCollectionsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private Long membershipDiscountId;

    private Long collectionId;

    private String collectionTitle;

    private CollectionType collectionType;

    
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

    public Long getMembershipDiscountId() {
        return membershipDiscountId;
    }

    public void setMembershipDiscountId(Long membershipDiscountId) {
        this.membershipDiscountId = membershipDiscountId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public void setCollectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipDiscountCollectionsDTO)) {
            return false;
        }

        return id != null && id.equals(((MembershipDiscountCollectionsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipDiscountCollectionsDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", membershipDiscountId=" + getMembershipDiscountId() +
            ", collectionId=" + getCollectionId() +
            ", collectionTitle='" + getCollectionTitle() + "'" +
            ", collectionType='" + getCollectionType() + "'" +
            "}";
    }
}
