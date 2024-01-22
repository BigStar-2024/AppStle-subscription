package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionWidgetSettingsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionWidgetSettingsDTO.class);
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO1 = new SubscriptionWidgetSettingsDTO();
        subscriptionWidgetSettingsDTO1.setId(1L);
        SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO2 = new SubscriptionWidgetSettingsDTO();
        assertThat(subscriptionWidgetSettingsDTO1).isNotEqualTo(subscriptionWidgetSettingsDTO2);
        subscriptionWidgetSettingsDTO2.setId(subscriptionWidgetSettingsDTO1.getId());
        assertThat(subscriptionWidgetSettingsDTO1).isEqualTo(subscriptionWidgetSettingsDTO2);
        subscriptionWidgetSettingsDTO2.setId(2L);
        assertThat(subscriptionWidgetSettingsDTO1).isNotEqualTo(subscriptionWidgetSettingsDTO2);
        subscriptionWidgetSettingsDTO1.setId(null);
        assertThat(subscriptionWidgetSettingsDTO1).isNotEqualTo(subscriptionWidgetSettingsDTO2);
    }
}
