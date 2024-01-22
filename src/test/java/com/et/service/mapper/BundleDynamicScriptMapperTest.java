package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BundleDynamicScriptMapperTest {

    private BundleDynamicScriptMapper bundleDynamicScriptMapper;

    @BeforeEach
    public void setUp() {
        bundleDynamicScriptMapper = new BundleDynamicScriptMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bundleDynamicScriptMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bundleDynamicScriptMapper.fromId(null)).isNull();
    }
}
