package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.AsyncUpdateEventProcessingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsyncUpdateEventProcessing} and its DTO {@link AsyncUpdateEventProcessingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AsyncUpdateEventProcessingMapper extends EntityMapper<AsyncUpdateEventProcessingDTO, AsyncUpdateEventProcessing> {



    default AsyncUpdateEventProcessing fromId(Long id) {
        if (id == null) {
            return null;
        }
        AsyncUpdateEventProcessing asyncUpdateEventProcessing = new AsyncUpdateEventProcessing();
        asyncUpdateEventProcessing.setId(id);
        return asyncUpdateEventProcessing;
    }
}
