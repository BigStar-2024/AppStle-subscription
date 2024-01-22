package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionBillingAttemptDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBillingAttempt} and its DTO {@link SubscriptionBillingAttemptDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionBillingAttemptMapper extends EntityMapper<SubscriptionBillingAttemptDTO, SubscriptionBillingAttempt> {



    default SubscriptionBillingAttempt fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionBillingAttempt subscriptionBillingAttempt = new SubscriptionBillingAttempt();
        subscriptionBillingAttempt.setId(id);
        return subscriptionBillingAttempt;
    }
}
