package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractDetails.class);
        SubscriptionContractDetails subscriptionContractDetails1 = new SubscriptionContractDetails();
        subscriptionContractDetails1.setId(1L);
        SubscriptionContractDetails subscriptionContractDetails2 = new SubscriptionContractDetails();
        subscriptionContractDetails2.setId(subscriptionContractDetails1.getId());
        assertThat(subscriptionContractDetails1).isEqualTo(subscriptionContractDetails2);
        subscriptionContractDetails2.setId(2L);
        assertThat(subscriptionContractDetails1).isNotEqualTo(subscriptionContractDetails2);
        subscriptionContractDetails1.setId(null);
        assertThat(subscriptionContractDetails1).isNotEqualTo(subscriptionContractDetails2);
    }
}
