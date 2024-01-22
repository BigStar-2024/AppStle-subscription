package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.ShopAssetUrlsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopAssetUrls} and its DTO {@link ShopAssetUrlsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopAssetUrlsMapper extends EntityMapper<ShopAssetUrlsDTO, ShopAssetUrls> {



    default ShopAssetUrls fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopAssetUrls shopAssetUrls = new ShopAssetUrls();
        shopAssetUrls.setId(id);
        return shopAssetUrls;
    }
}
