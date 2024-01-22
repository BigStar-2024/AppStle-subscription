package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionContractSettingsMapperTest {

    private SubscriptionContractSettingsMapper subscriptionContractSettingsMapper;

    @BeforeEach
    public void setUp() {
        subscriptionContractSettingsMapper = new SubscriptionContractSettingsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionContractSettingsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionContractSettingsMapper.fromId(null)).isNull();
    }
}
