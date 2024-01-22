package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionContractProcessingMapperTest {

    private SubscriptionContractProcessingMapper subscriptionContractProcessingMapper;

    @BeforeEach
    public void setUp() {
        subscriptionContractProcessingMapper = new SubscriptionContractProcessingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subscriptionContractProcessingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(subscriptionContractProcessingMapper.fromId(null)).isNull();
    }
}
