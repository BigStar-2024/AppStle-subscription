package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionBundlingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundling} and its DTO {@link SubscriptionBundlingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionBundlingMapper extends EntityMapper<SubscriptionBundlingDTO, SubscriptionBundling> {



    default SubscriptionBundling fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionBundling subscriptionBundling = new SubscriptionBundling();
        subscriptionBundling.setId(id);
        return subscriptionBundling;
    }
}
