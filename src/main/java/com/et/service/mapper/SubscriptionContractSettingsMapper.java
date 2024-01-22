package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionContractSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionContractSettings} and its DTO {@link SubscriptionContractSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionContractSettingsMapper extends EntityMapper<SubscriptionContractSettingsDTO, SubscriptionContractSettings> {



    default SubscriptionContractSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionContractSettings subscriptionContractSettings = new SubscriptionContractSettings();
        subscriptionContractSettings.setId(id);
        return subscriptionContractSettings;
    }
}
