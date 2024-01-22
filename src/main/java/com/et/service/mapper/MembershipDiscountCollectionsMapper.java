package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.MembershipDiscountCollectionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipDiscountCollections} and its DTO {@link MembershipDiscountCollectionsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipDiscountCollectionsMapper extends EntityMapper<MembershipDiscountCollectionsDTO, MembershipDiscountCollections> {



    default MembershipDiscountCollections fromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipDiscountCollections membershipDiscountCollections = new MembershipDiscountCollections();
        membershipDiscountCollections.setId(id);
        return membershipDiscountCollections;
    }
}
