package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OnboardingInfoMapperTest {

    private OnboardingInfoMapper onboardingInfoMapper;

    @BeforeEach
    public void setUp() {
        onboardingInfoMapper = new OnboardingInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(onboardingInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(onboardingInfoMapper.fromId(null)).isNull();
    }
}
