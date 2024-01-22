package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.CurrencyConversionInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CurrencyConversionInfo} and its DTO {@link CurrencyConversionInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyConversionInfoMapper extends EntityMapper<CurrencyConversionInfoDTO, CurrencyConversionInfo> {



    default CurrencyConversionInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        CurrencyConversionInfo currencyConversionInfo = new CurrencyConversionInfo();
        currencyConversionInfo.setId(id);
        return currencyConversionInfo;
    }
}
