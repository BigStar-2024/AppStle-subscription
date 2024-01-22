package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractSettings.class);
        SubscriptionContractSettings subscriptionContractSettings1 = new SubscriptionContractSettings();
        subscriptionContractSettings1.setId(1L);
        SubscriptionContractSettings subscriptionContractSettings2 = new SubscriptionContractSettings();
        subscriptionContractSettings2.setId(subscriptionContractSettings1.getId());
        assertThat(subscriptionContractSettings1).isEqualTo(subscriptionContractSettings2);
        subscriptionContractSettings2.setId(2L);
        assertThat(subscriptionContractSettings1).isNotEqualTo(subscriptionContractSettings2);
        subscriptionContractSettings1.setId(null);
        assertThat(subscriptionContractSettings1).isNotEqualTo(subscriptionContractSettings2);
    }
}
