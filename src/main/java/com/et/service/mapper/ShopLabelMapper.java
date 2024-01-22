package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.ShopLabelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopLabel} and its DTO {@link ShopLabelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopLabelMapper extends EntityMapper<ShopLabelDTO, ShopLabel> {



    default ShopLabel fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopLabel shopLabel = new ShopLabel();
        shopLabel.setId(id);
        return shopLabel;
    }
}
