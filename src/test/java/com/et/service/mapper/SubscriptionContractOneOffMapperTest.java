package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionContractOneOffMapperTest {

    private SubscriptionContractOneOffMapper subscriptionContractOneOffMapper;

    @BeforeEach
    public void setUp() {
        subscriptionContractOneOffMapper = new SubscriptionContractOneOffMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionContractOneOffMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionContractOneOffMapper.fromId(null)).isNull();
    }
}
