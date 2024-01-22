package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AppstleMenuSettingsMapperTest {

    private AppstleMenuSettingsMapper appstleMenuSettingsMapper;

    @BeforeEach
    public void setUp() {
        appstleMenuSettingsMapper = new AppstleMenuSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(appstleMenuSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(appstleMenuSettingsMapper.fromId(null)).isNull();
    }
}
