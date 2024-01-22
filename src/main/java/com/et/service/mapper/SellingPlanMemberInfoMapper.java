package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SellingPlanMemberInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SellingPlanMemberInfo} and its DTO {@link SellingPlanMemberInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SellingPlanMemberInfoMapper extends EntityMapper<SellingPlanMemberInfoDTO, SellingPlanMemberInfo> {



    default SellingPlanMemberInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        SellingPlanMemberInfo sellingPlanMemberInfo = new SellingPlanMemberInfo();
        sellingPlanMemberInfo.setId(id);
        return sellingPlanMemberInfo;
    }
}
