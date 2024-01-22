package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ShopLabelMapperTest {

    private ShopLabelMapper shopLabelMapper;

    @BeforeEach
    public void setUp() {
        shopLabelMapper = new ShopLabelMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(shopLabelMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shopLabelMapper.fromId(null)).isNull();
    }
}
