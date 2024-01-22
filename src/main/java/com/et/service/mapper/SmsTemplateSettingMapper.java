package com.et.service.mapper;


import com.et.domain.*;
import com.et.service.dto.SmsTemplateSettingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsTemplateSetting} and its DTO {@link SmsTemplateSettingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsTemplateSettingMapper extends EntityMapper<SmsTemplateSettingDTO, SmsTemplateSetting> {



    default SmsTemplateSetting fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsTemplateSetting smsTemplateSetting = new SmsTemplateSetting();
        smsTemplateSetting.setId(id);
        return smsTemplateSetting;
    }
}
