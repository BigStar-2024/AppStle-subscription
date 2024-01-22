package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class WidgetTemplateMapperTest {

    private WidgetTemplateMapper widgetTemplateMapper;

    @BeforeEach
    public void setUp() {
        widgetTemplateMapper = new WidgetTemplateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(widgetTemplateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(widgetTemplateMapper.fromId(null)).isNull();
    }
}
