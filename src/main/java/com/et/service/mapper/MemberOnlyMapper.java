package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.MemberOnlyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberOnly} and its DTO {@link MemberOnlyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MemberOnlyMapper extends EntityMapper<MemberOnlyDTO, MemberOnly> {



    default MemberOnly fromId(Long id) {
        if (id == null) {
            return null;
        }
        MemberOnly memberOnly = new MemberOnly();
        memberOnly.setId(id);
        return memberOnly;
    }
}
