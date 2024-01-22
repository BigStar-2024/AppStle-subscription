package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionGroupPlanDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionGroupPlan} and its DTO {@link SubscriptionGroupPlanDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionGroupPlanMapper extends EntityMapper<SubscriptionGroupPlanDTO, SubscriptionGroupPlan> {



    default SubscriptionGroupPlan fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionGroupPlan subscriptionGroupPlan = new SubscriptionGroupPlan();
        subscriptionGroupPlan.setId(id);
        return subscriptionGroupPlan;
    }
}
