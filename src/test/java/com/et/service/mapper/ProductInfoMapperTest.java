package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductInfoMapperTest {

    private ProductInfoMapper productInfoMapper;

    @BeforeEach
    public void setUp() {
        productInfoMapper = new ProductInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(productInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productInfoMapper.fromId(null)).isNull();
    }
}
