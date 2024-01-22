package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryProfileMapperTest {

    private DeliveryProfileMapper deliveryProfileMapper;

    @BeforeEach
    public void setUp() {
        deliveryProfileMapper = new DeliveryProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(deliveryProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(deliveryProfileMapper.fromId(null)).isNull();
    }
}
