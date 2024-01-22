package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class WidgetTemplateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WidgetTemplateDTO.class);
        WidgetTemplateDTO widgetTemplateDTO1 = new WidgetTemplateDTO();
        widgetTemplateDTO1.setId(1L);
        WidgetTemplateDTO widgetTemplateDTO2 = new WidgetTemplateDTO();
        assertThat(widgetTemplateDTO1).isNotEqualTo(widgetTemplateDTO2);
        widgetTemplateDTO2.setId(widgetTemplateDTO1.getId());
        assertThat(widgetTemplateDTO1).isEqualTo(widgetTemplateDTO2);
        widgetTemplateDTO2.setId(2L);
        assertThat(widgetTemplateDTO1).isNotEqualTo(widgetTemplateDTO2);
        widgetTemplateDTO1.setId(null);
        assertThat(widgetTemplateDTO1).isNotEqualTo(widgetTemplateDTO2);
    }
}
