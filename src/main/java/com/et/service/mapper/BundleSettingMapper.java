package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.BundleSettingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BundleSetting} and its DTO {@link BundleSettingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BundleSettingMapper extends EntityMapper<BundleSettingDTO, BundleSetting> {



    default BundleSetting fromId(Long id) {
        if (id == null) {
            return null;
        }
        BundleSetting bundleSetting = new BundleSetting();
        bundleSetting.setId(id);
        return bundleSetting;
    }
}
