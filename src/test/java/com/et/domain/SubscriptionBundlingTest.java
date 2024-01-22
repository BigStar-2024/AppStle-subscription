package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBundlingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundling.class);
        SubscriptionBundling subscriptionBundling1 = new SubscriptionBundling();
        subscriptionBundling1.setId(1L);
        SubscriptionBundling subscriptionBundling2 = new SubscriptionBundling();
        subscriptionBundling2.setId(subscriptionBundling1.getId());
        assertThat(subscriptionBundling1).isEqualTo(subscriptionBundling2);
        subscriptionBundling2.setId(2L);
        assertThat(subscriptionBundling1).isNotEqualTo(subscriptionBundling2);
        subscriptionBundling1.setId(null);
        assertThat(subscriptionBundling1).isNotEqualTo(subscriptionBundling2);
    }
}
