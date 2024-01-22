package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsMapperTest {

    private AnalyticsMapper analyticsMapper;

    @BeforeEach
    public void setUp() {
        analyticsMapper = new AnalyticsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(analyticsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(analyticsMapper.fromId(null)).isNull();
    }
}
