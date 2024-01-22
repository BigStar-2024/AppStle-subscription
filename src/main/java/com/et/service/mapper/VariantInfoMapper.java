package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.VariantInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link VariantInfo} and its DTO {@link VariantInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VariantInfoMapper extends EntityMapper<VariantInfoDTO, VariantInfo> {



    default VariantInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        VariantInfo variantInfo = new VariantInfo();
        variantInfo.setId(id);
        return variantInfo;
    }
}
