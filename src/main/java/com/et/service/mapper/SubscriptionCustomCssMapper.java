package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SubscriptionCustomCssDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionCustomCss} and its DTO {@link SubscriptionCustomCssDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionCustomCssMapper extends EntityMapper<SubscriptionCustomCssDTO, SubscriptionCustomCss> {



    default SubscriptionCustomCss fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionCustomCss subscriptionCustomCss = new SubscriptionCustomCss();
        subscriptionCustomCss.setId(id);
        return subscriptionCustomCss;
    }
}
