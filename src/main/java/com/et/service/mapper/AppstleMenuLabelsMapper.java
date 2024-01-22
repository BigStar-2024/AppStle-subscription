package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.AppstleMenuLabelsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppstleMenuLabels} and its DTO {@link AppstleMenuLabelsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppstleMenuLabelsMapper extends EntityMapper<AppstleMenuLabelsDTO, AppstleMenuLabels> {



    default AppstleMenuLabels fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppstleMenuLabels appstleMenuLabels = new AppstleMenuLabels();
        appstleMenuLabels.setId(id);
        return appstleMenuLabels;
    }
}
