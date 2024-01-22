package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CurrencyConversionInfoMapperTest {

    private CurrencyConversionInfoMapper currencyConversionInfoMapper;

    @BeforeEach
    public void setUp() {
        currencyConversionInfoMapper = new CurrencyConversionInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(currencyConversionInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(currencyConversionInfoMapper.fromId(null)).isNull();
    }
}
