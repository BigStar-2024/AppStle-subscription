package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.ShopCustomizationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopCustomization} and its DTO {@link ShopCustomizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopCustomizationMapper extends EntityMapper<ShopCustomizationDTO, ShopCustomization> {



    default ShopCustomization fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopCustomization shopCustomization = new ShopCustomization();
        shopCustomization.setId(id);
        return shopCustomization;
    }
}
