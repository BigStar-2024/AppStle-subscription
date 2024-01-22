package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionContractProcessingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionContractProcessing} and its DTO {@link SubscriptionContractProcessingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionContractProcessingMapper extends EntityMapper<SubscriptionContractProcessingDTO, SubscriptionContractProcessing> {



    default SubscriptionContractProcessing fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionContractProcessing subscriptionContractProcessing = new SubscriptionContractProcessing();
        subscriptionContractProcessing.setId(id);
        return subscriptionContractProcessing;
    }
}
