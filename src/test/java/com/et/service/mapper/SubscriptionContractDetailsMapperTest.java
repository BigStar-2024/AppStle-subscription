package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionContractDetailsMapperTest {

    private SubscriptionContractDetailsMapper subscriptionContractDetailsMapper;

    @BeforeEach
    public void setUp() {
        subscriptionContractDetailsMapper = new SubscriptionContractDetailsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionContractDetailsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionContractDetailsMapper.fromId(null)).isNull();
    }
}
