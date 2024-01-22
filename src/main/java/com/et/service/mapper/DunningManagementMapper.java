package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.DunningManagementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DunningManagement} and its DTO {@link DunningManagementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DunningManagementMapper extends EntityMapper<DunningManagementDTO, DunningManagement> {



    default DunningManagement fromId(Long id) {
        if (id == null) {
            return null;
        }
        DunningManagement dunningManagement = new DunningManagement();
        dunningManagement.setId(id);
        return dunningManagement;
    }
}
