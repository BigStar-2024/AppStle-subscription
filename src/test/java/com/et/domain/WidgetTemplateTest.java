package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class WidgetTemplateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WidgetTemplate.class);
        WidgetTemplate widgetTemplate1 = new WidgetTemplate();
        widgetTemplate1.setId(1L);
        WidgetTemplate widgetTemplate2 = new WidgetTemplate();
        widgetTemplate2.setId(widgetTemplate1.getId());
        assertThat(widgetTemplate1).isEqualTo(widgetTemplate2);
        widgetTemplate2.setId(2L);
        assertThat(widgetTemplate1).isNotEqualTo(widgetTemplate2);
        widgetTemplate1.setId(null);
        assertThat(widgetTemplate1).isNotEqualTo(widgetTemplate2);
    }
}
