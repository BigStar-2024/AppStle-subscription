package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ShopCustomizationMapperTest {

    private ShopCustomizationMapper shopCustomizationMapper;

    @BeforeEach
    public void setUp() {
        shopCustomizationMapper = new ShopCustomizationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(shopCustomizationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shopCustomizationMapper.fromId(null)).isNull();
    }
}
