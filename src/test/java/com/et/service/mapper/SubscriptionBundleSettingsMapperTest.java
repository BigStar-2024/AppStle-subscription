package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionBundleSettingsMapperTest {

    private SubscriptionBundleSettingsMapper subscriptionBundleSettingsMapper;

    @BeforeEach
    public void setUp() {
        subscriptionBundleSettingsMapper = new SubscriptionBundleSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionBundleSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionBundleSettingsMapper.fromId(null)).isNull();
    }
}
