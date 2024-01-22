package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.CancellationManagementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CancellationManagement} and its DTO {@link CancellationManagementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CancellationManagementMapper extends EntityMapper<CancellationManagementDTO, CancellationManagement> {



    default CancellationManagement fromId(Long id) {
        if (id == null) {
            return null;
        }
        CancellationManagement cancellationManagement = new CancellationManagement();
        cancellationManagement.setId(id);
        return cancellationManagement;
    }
}
