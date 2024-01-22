package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.ProductCycleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductCycle} and its DTO {@link ProductCycleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductCycleMapper extends EntityMapper<ProductCycleDTO, ProductCycle> {



    default ProductCycle fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductCycle productCycle = new ProductCycle();
        productCycle.setId(id);
        return productCycle;
    }
}
