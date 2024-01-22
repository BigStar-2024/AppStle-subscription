package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.BundleRuleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BundleRule} and its DTO {@link BundleRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BundleRuleMapper extends EntityMapper<BundleRuleDTO, BundleRule> {



    default BundleRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        BundleRule bundleRule = new BundleRule();
        bundleRule.setId(id);
        return bundleRule;
    }
}
