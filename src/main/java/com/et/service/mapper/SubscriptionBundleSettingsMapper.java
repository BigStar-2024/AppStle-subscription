package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionBundleSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundleSettings} and its DTO {@link SubscriptionBundleSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionBundleSettingsMapper extends EntityMapper<SubscriptionBundleSettingsDTO, SubscriptionBundleSettings> {



    default SubscriptionBundleSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionBundleSettings subscriptionBundleSettings = new SubscriptionBundleSettings();
        subscriptionBundleSettings.setId(id);
        return subscriptionBundleSettings;
    }
}
