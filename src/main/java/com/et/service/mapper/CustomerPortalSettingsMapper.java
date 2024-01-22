package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.CustomerPortalSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerPortalSettings} and its DTO {@link CustomerPortalSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerPortalSettingsMapper extends EntityMapper<CustomerPortalSettingsDTO, CustomerPortalSettings> {



    default CustomerPortalSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerPortalSettings customerPortalSettings = new CustomerPortalSettings();
        customerPortalSettings.setId(id);
        return customerPortalSettings;
    }
}
