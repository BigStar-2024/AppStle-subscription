package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBillingAttemptDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBillingAttemptDTO.class);
        SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO1 = new SubscriptionBillingAttemptDTO();
        subscriptionBillingAttemptDTO1.setId(1L);
        SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO2 = new SubscriptionBillingAttemptDTO();
        assertThat(subscriptionBillingAttemptDTO1).isNotEqualTo(subscriptionBillingAttemptDTO2);
        subscriptionBillingAttemptDTO2.setId(subscriptionBillingAttemptDTO1.getId());
        assertThat(subscriptionBillingAttemptDTO1).isEqualTo(subscriptionBillingAttemptDTO2);
        subscriptionBillingAttemptDTO2.setId(2L);
        assertThat(subscriptionBillingAttemptDTO1).isNotEqualTo(subscriptionBillingAttemptDTO2);
        subscriptionBillingAttemptDTO1.setId(null);
        assertThat(subscriptionBillingAttemptDTO1).isNotEqualTo(subscriptionBillingAttemptDTO2);
    }
}
