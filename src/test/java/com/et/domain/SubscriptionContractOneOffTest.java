package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractOneOffTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractOneOff.class);
        SubscriptionContractOneOff subscriptionContractOneOff1 = new SubscriptionContractOneOff();
        subscriptionContractOneOff1.setId(1L);
        SubscriptionContractOneOff subscriptionContractOneOff2 = new SubscriptionContractOneOff();
        subscriptionContractOneOff2.setId(subscriptionContractOneOff1.getId());
        assertThat(subscriptionContractOneOff1).isEqualTo(subscriptionContractOneOff2);
        subscriptionContractOneOff2.setId(2L);
        assertThat(subscriptionContractOneOff1).isNotEqualTo(subscriptionContractOneOff2);
        subscriptionContractOneOff1.setId(null);
        assertThat(subscriptionContractOneOff1).isNotEqualTo(subscriptionContractOneOff2);
    }
}
