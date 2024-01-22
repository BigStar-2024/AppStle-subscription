package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionWidgetSettingsMapperTest {

    private SubscriptionWidgetSettingsMapper subscriptionWidgetSettingsMapper;

    @BeforeEach
    public void setUp() {
        subscriptionWidgetSettingsMapper = new SubscriptionWidgetSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionWidgetSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionWidgetSettingsMapper.fromId(null)).isNull();
    }
}
