package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class OnboardingInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnboardingInfo.class);
        OnboardingInfo onboardingInfo1 = new OnboardingInfo();
        onboardingInfo1.setId(1L);
        OnboardingInfo onboardingInfo2 = new OnboardingInfo();
        onboardingInfo2.setId(onboardingInfo1.getId());
        assertThat(onboardingInfo1).isEqualTo(onboardingInfo2);
        onboardingInfo2.setId(2L);
        assertThat(onboardingInfo1).isNotEqualTo(onboardingInfo2);
        onboardingInfo1.setId(null);
        assertThat(onboardingInfo1).isNotEqualTo(onboardingInfo2);
    }
}
