package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.MembershipDiscountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipDiscount} and its DTO {@link MembershipDiscountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipDiscountMapper extends EntityMapper<MembershipDiscountDTO, MembershipDiscount> {



    default MembershipDiscount fromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipDiscount membershipDiscount = new MembershipDiscount();
        membershipDiscount.setId(id);
        return membershipDiscount;
    }
}
