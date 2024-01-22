package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.CustomerPortalDynamicScriptDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerPortalDynamicScript} and its DTO {@link CustomerPortalDynamicScriptDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerPortalDynamicScriptMapper extends EntityMapper<CustomerPortalDynamicScriptDTO, CustomerPortalDynamicScript> {



    default CustomerPortalDynamicScript fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerPortalDynamicScript customerPortalDynamicScript = new CustomerPortalDynamicScript();
        customerPortalDynamicScript.setId(id);
        return customerPortalDynamicScript;
    }
}
