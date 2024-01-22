package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.ShopInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopInfo} and its DTO {@link ShopInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopInfoMapper extends EntityMapper<ShopInfoDTO, ShopInfo> {



    default ShopInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setId(id);
        return shopInfo;
    }
}
