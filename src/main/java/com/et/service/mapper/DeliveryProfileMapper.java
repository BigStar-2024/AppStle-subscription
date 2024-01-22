package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.DeliveryProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryProfile} and its DTO {@link DeliveryProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryProfileMapper extends EntityMapper<DeliveryProfileDTO, DeliveryProfile> {



    default DeliveryProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        DeliveryProfile deliveryProfile = new DeliveryProfile();
        deliveryProfile.setId(id);
        return deliveryProfile;
    }
}
