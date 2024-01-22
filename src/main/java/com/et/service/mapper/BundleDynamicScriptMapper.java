package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.BundleDynamicScriptDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BundleDynamicScript} and its DTO {@link BundleDynamicScriptDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BundleDynamicScriptMapper extends EntityMapper<BundleDynamicScriptDTO, BundleDynamicScript> {



    default BundleDynamicScript fromId(Long id) {
        if (id == null) {
            return null;
        }
        BundleDynamicScript bundleDynamicScript = new BundleDynamicScript();
        bundleDynamicScript.setId(id);
        return bundleDynamicScript;
    }
}
