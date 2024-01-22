package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.PlanInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanInfo} and its DTO {@link PlanInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlanInfoMapper extends EntityMapper<PlanInfoDTO, PlanInfo> {

    @Mapping(target = "paymentPlans", ignore = true)
    @Mapping(target = "removePaymentPlan", ignore = true)
    PlanInfo toEntity(PlanInfoDTO planInfoDTO);

    default PlanInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanInfo planInfo = new PlanInfo();
        planInfo.setId(id);
        return planInfo;
    }
}
