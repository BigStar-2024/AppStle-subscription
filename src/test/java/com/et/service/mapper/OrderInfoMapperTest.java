package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class OrderInfoMapperTest {

    private OrderInfoMapper orderInfoMapper;

    @BeforeEach
    public void setUp() {
        orderInfoMapper = new OrderInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(orderInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderInfoMapper.fromId(null)).isNull();
    }
}
