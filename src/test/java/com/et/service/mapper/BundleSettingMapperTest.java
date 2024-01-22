package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BundleSettingMapperTest {

    private BundleSettingMapper bundleSettingMapper;

    @BeforeEach
    public void setUp() {
        bundleSettingMapper = new BundleSettingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bundleSettingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bundleSettingMapper.fromId(null)).isNull();
    }
}
