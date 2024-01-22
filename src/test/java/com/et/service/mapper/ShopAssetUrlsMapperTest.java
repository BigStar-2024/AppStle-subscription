package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ShopAssetUrlsMapperTest {

    private ShopAssetUrlsMapper shopAssetUrlsMapper;

    @BeforeEach
    public void setUp() {
        shopAssetUrlsMapper = new ShopAssetUrlsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(shopAssetUrlsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shopAssetUrlsMapper.fromId(null)).isNull();
    }
}
