package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.PlanInfoDiscountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanInfoDiscount} and its DTO {@link PlanInfoDiscountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlanInfoDiscountMapper extends EntityMapper<PlanInfoDiscountDTO, PlanInfoDiscount> {



    default PlanInfoDiscount fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanInfoDiscount planInfoDiscount = new PlanInfoDiscount();
        planInfoDiscount.setId(id);
        return planInfoDiscount;
    }
}
