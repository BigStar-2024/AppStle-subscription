package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class VariantInfoMapperTest {

    private VariantInfoMapper variantInfoMapper;

    @BeforeEach
    public void setUp() {
        variantInfoMapper = new VariantInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(variantInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(variantInfoMapper.fromId(null)).isNull();
    }
}
