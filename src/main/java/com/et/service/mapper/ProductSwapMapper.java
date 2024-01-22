package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.ProductSwapDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductSwap} and its DTO {@link ProductSwapDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductSwapMapper extends EntityMapper<ProductSwapDTO, ProductSwap> {



    default ProductSwap fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductSwap productSwap = new ProductSwap();
        productSwap.setId(id);
        return productSwap;
    }
}
