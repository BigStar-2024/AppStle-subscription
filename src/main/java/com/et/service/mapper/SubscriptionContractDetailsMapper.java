package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionContractDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionContractDetails} and its DTO {@link SubscriptionContractDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionContractDetailsMapper extends EntityMapper<SubscriptionContractDetailsDTO, SubscriptionContractDetails> {



    default SubscriptionContractDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionContractDetails subscriptionContractDetails = new SubscriptionContractDetails();
        subscriptionContractDetails.setId(id);
        return subscriptionContractDetails;
    }
}
