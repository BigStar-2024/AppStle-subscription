package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBillingAttemptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBillingAttempt.class);
        SubscriptionBillingAttempt subscriptionBillingAttempt1 = new SubscriptionBillingAttempt();
        subscriptionBillingAttempt1.setId(1L);
        SubscriptionBillingAttempt subscriptionBillingAttempt2 = new SubscriptionBillingAttempt();
        subscriptionBillingAttempt2.setId(subscriptionBillingAttempt1.getId());
        assertThat(subscriptionBillingAttempt1).isEqualTo(subscriptionBillingAttempt2);
        subscriptionBillingAttempt2.setId(2L);
        assertThat(subscriptionBillingAttempt1).isNotEqualTo(subscriptionBillingAttempt2);
        subscriptionBillingAttempt1.setId(null);
        assertThat(subscriptionBillingAttempt1).isNotEqualTo(subscriptionBillingAttempt2);
    }
}
