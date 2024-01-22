package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionBundlingMapperTest {

    private SubscriptionBundlingMapper subscriptionBundlingMapper;

    @BeforeEach
    public void setUp() {
        subscriptionBundlingMapper = new SubscriptionBundlingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionBundlingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionBundlingMapper.fromId(null)).isNull();
    }
}
