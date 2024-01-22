package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.ProductInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductInfo} and its DTO {@link ProductInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductInfoMapper extends EntityMapper<ProductInfoDTO, ProductInfo> {



    default ProductInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(id);
        return productInfo;
    }
}
