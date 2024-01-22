package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionCustomCssMapperTest {

    private SubscriptionCustomCssMapper subscriptionCustomCssMapper;

    @BeforeEach
    public void setUp() {
        subscriptionCustomCssMapper = new SubscriptionCustomCssMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionCustomCssMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionCustomCssMapper.fromId(null)).isNull();
    }
}
