package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.BulkAutomationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BulkAutomation} and its DTO {@link BulkAutomationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BulkAutomationMapper extends EntityMapper<BulkAutomationDTO, BulkAutomation> {



    default BulkAutomation fromId(Long id) {
        if (id == null) {
            return null;
        }
        BulkAutomation bulkAutomation = new BulkAutomation();
        bulkAutomation.setId(id);
        return bulkAutomation;
    }
}
