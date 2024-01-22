package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionBillingAttemptMapperTest {

    private SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper;

    @BeforeEach
    public void setUp() {
        subscriptionBillingAttemptMapper = new SubscriptionBillingAttemptMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionBillingAttemptMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionBillingAttemptMapper.fromId(null)).isNull();
    }
}
