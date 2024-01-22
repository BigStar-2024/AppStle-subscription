package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.ShopSettingsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopSettings} and its DTO {@link ShopSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopSettingsMapper extends EntityMapper<ShopSettingsDTO, ShopSettings> {

    default ShopSettings fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopSettings shopSettings = new ShopSettings();
        shopSettings.setId(id);
        return shopSettings;
    }
}
