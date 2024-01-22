package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BundleRuleMapperTest {

    private BundleRuleMapper bundleRuleMapper;

    @BeforeEach
    public void setUp() {
        bundleRuleMapper = new BundleRuleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bundleRuleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bundleRuleMapper.fromId(null)).isNull();
    }
}
