package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.CustomizationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customization} and its DTO {@link CustomizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomizationMapper extends EntityMapper<CustomizationDTO, Customization> {



    default Customization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customization customization = new Customization();
        customization.setId(id);
        return customization;
    }
}
