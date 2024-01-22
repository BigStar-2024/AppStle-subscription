package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionWidgetSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionWidgetSettings} and its DTO {@link SubscriptionWidgetSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionWidgetSettingsMapper extends EntityMapper<SubscriptionWidgetSettingsDTO, SubscriptionWidgetSettings> {



    default SubscriptionWidgetSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionWidgetSettings subscriptionWidgetSettings = new SubscriptionWidgetSettings();
        subscriptionWidgetSettings.setId(id);
        return subscriptionWidgetSettings;
    }
}
