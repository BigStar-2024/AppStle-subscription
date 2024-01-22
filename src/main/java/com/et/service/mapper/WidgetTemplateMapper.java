package com.et.service.mapper;

import com.et.domain.*;
import com.et.service.dto.WidgetTemplateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WidgetTemplate} and its DTO {@link WidgetTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WidgetTemplateMapper extends EntityMapper<WidgetTemplateDTO, WidgetTemplate> {



    default WidgetTemplate fromId(Long id) {
        if (id == null) {
            return null;
        }
        WidgetTemplate widgetTemplate = new WidgetTemplate();
        widgetTemplate.setId(id);
        return widgetTemplate;
    }
}
