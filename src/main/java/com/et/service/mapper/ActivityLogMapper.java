package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.ActivityLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityLog} and its DTO {@link ActivityLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivityLogMapper extends EntityMapper<ActivityLogDTO, ActivityLog> {



    default ActivityLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityLog activityLog = new ActivityLog();
        activityLog.setId(id);
        return activityLog;
    }
}
