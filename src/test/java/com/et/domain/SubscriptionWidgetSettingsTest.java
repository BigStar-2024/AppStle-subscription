package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionWidgetSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionWidgetSettings.class);
        SubscriptionWidgetSettings subscriptionWidgetSettings1 = new SubscriptionWidgetSettings();
        subscriptionWidgetSettings1.setId(1L);
        SubscriptionWidgetSettings subscriptionWidgetSettings2 = new SubscriptionWidgetSettings();
        subscriptionWidgetSettings2.setId(subscriptionWidgetSettings1.getId());
        assertThat(subscriptionWidgetSettings1).isEqualTo(subscriptionWidgetSettings2);
        subscriptionWidgetSettings2.setId(2L);
        assertThat(subscriptionWidgetSettings1).isNotEqualTo(subscriptionWidgetSettings2);
        subscriptionWidgetSettings1.setId(null);
        assertThat(subscriptionWidgetSettings1).isNotEqualTo(subscriptionWidgetSettings2);
    }
}
