package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.MembershipDiscountProductsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipDiscountProducts} and its DTO {@link MembershipDiscountProductsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipDiscountProductsMapper extends EntityMapper<MembershipDiscountProductsDTO, MembershipDiscountProducts> {



    default MembershipDiscountProducts fromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipDiscountProducts membershipDiscountProducts = new MembershipDiscountProducts();
        membershipDiscountProducts.setId(id);
        return membershipDiscountProducts;
    }
}
