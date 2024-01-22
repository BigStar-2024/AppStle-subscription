package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CustomizationMapperTest {

    private CustomizationMapper customizationMapper;

    @BeforeEach
    public void setUp() {
        customizationMapper = new CustomizationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(customizationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customizationMapper.fromId(null)).isNull();
    }
}
