package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBundleSettingsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundleSettingsDTO.class);
        SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO1 = new SubscriptionBundleSettingsDTO();
        subscriptionBundleSettingsDTO1.setId(1L);
        SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO2 = new SubscriptionBundleSettingsDTO();
        assertThat(subscriptionBundleSettingsDTO1).isNotEqualTo(subscriptionBundleSettingsDTO2);
        subscriptionBundleSettingsDTO2.setId(subscriptionBundleSettingsDTO1.getId());
        assertThat(subscriptionBundleSettingsDTO1).isEqualTo(subscriptionBundleSettingsDTO2);
        subscriptionBundleSettingsDTO2.setId(2L);
        assertThat(subscriptionBundleSettingsDTO1).isNotEqualTo(subscriptionBundleSettingsDTO2);
        subscriptionBundleSettingsDTO1.setId(null);
        assertThat(subscriptionBundleSettingsDTO1).isNotEqualTo(subscriptionBundleSettingsDTO2);
    }
}
