package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionGroupPlanMapperTest {

    private SubscriptionGroupPlanMapper subscriptionGroupPlanMapper;

    @BeforeEach
    public void setUp() {
        subscriptionGroupPlanMapper = new SubscriptionGroupPlanMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionGroupPlanMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionGroupPlanMapper.fromId(null)).isNull();
    }
}
