package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.AppstleMenuSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppstleMenuSettings} and its DTO {@link AppstleMenuSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppstleMenuSettingsMapper extends EntityMapper<AppstleMenuSettingsDTO, AppstleMenuSettings> {



    default AppstleMenuSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppstleMenuSettings appstleMenuSettings = new AppstleMenuSettings();
        appstleMenuSettings.setId(id);
        return appstleMenuSettings;
    }
}
