package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class OnboardingInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnboardingInfoDTO.class);
        OnboardingInfoDTO onboardingInfoDTO1 = new OnboardingInfoDTO();
        onboardingInfoDTO1.setId(1L);
        OnboardingInfoDTO onboardingInfoDTO2 = new OnboardingInfoDTO();
        assertThat(onboardingInfoDTO1).isNotEqualTo(onboardingInfoDTO2);
        onboardingInfoDTO2.setId(onboardingInfoDTO1.getId());
        assertThat(onboardingInfoDTO1).isEqualTo(onboardingInfoDTO2);
        onboardingInfoDTO2.setId(2L);
        assertThat(onboardingInfoDTO1).isNotEqualTo(onboardingInfoDTO2);
        onboardingInfoDTO1.setId(null);
        assertThat(onboardingInfoDTO1).isNotEqualTo(onboardingInfoDTO2);
    }
}
