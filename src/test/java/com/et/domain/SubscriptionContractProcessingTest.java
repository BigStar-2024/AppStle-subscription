package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractProcessingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractProcessing.class);
        SubscriptionContractProcessing subscriptionContractProcessing1 = new SubscriptionContractProcessing();
        subscriptionContractProcessing1.setId(1L);
        SubscriptionContractProcessing subscriptionContractProcessing2 = new SubscriptionContractProcessing();
        subscriptionContractProcessing2.setId(subscriptionContractProcessing1.getId());
        assertThat(subscriptionContractProcessing1).isEqualTo(subscriptionContractProcessing2);
        subscriptionContractProcessing2.setId(2L);
        assertThat(subscriptionContractProcessing1).isNotEqualTo(subscriptionContractProcessing2);
        subscriptionContractProcessing1.setId(null);
        assertThat(subscriptionContractProcessing1).isNotEqualTo(subscriptionContractProcessing2);
    }
}
