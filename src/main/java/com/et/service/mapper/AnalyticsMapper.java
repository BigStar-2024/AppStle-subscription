package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.AnalyticsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Analytics} and its DTO {@link AnalyticsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnalyticsMapper extends EntityMapper<AnalyticsDTO, Analytics> {



    default Analytics fromId(Long id) {
        if (id == null) {
            return null;
        }
        Analytics analytics = new Analytics();
        analytics.setId(id);
        return analytics;
    }
}
