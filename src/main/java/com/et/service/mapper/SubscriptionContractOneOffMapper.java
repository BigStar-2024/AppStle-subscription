package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionContractOneOffDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionContractOneOff} and its DTO {@link SubscriptionContractOneOffDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionContractOneOffMapper extends EntityMapper<SubscriptionContractOneOffDTO, SubscriptionContractOneOff> {



    default SubscriptionContractOneOff fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionContractOneOff subscriptionContractOneOff = new SubscriptionContractOneOff();
        subscriptionContractOneOff.setId(id);
        return subscriptionContractOneOff;
    }
}
