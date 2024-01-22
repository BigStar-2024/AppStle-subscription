package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductSwapMapperTest {

    private ProductSwapMapper productSwapMapper;

    @BeforeEach
    public void setUp() {
        productSwapMapper = new ProductSwapMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productSwapMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productSwapMapper.fromId(null)).isNull();
    }
}
