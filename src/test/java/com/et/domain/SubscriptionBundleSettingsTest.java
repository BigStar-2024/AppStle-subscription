package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBundleSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundleSettings.class);
        SubscriptionBundleSettings subscriptionBundleSettings1 = new SubscriptionBundleSettings();
        subscriptionBundleSettings1.setId(1L);
        SubscriptionBundleSettings subscriptionBundleSettings2 = new SubscriptionBundleSettings();
        subscriptionBundleSettings2.setId(subscriptionBundleSettings1.getId());
        assertThat(subscriptionBundleSettings1).isEqualTo(subscriptionBundleSettings2);
        subscriptionBundleSettings2.setId(2L);
        assertThat(subscriptionBundleSettings1).isNotEqualTo(subscriptionBundleSettings2);
        subscriptionBundleSettings1.setId(null);
        assertThat(subscriptionBundleSettings1).isNotEqualTo(subscriptionBundleSettings2);
    }
}
