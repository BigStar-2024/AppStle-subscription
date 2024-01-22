package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionGroupPlanTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionGroupPlan.class);
        SubscriptionGroupPlan subscriptionGroupPlan1 = new SubscriptionGroupPlan();
        subscriptionGroupPlan1.setId(1L);
        SubscriptionGroupPlan subscriptionGroupPlan2 = new SubscriptionGroupPlan();
        subscriptionGroupPlan2.setId(subscriptionGroupPlan1.getId());
        assertThat(subscriptionGroupPlan1).isEqualTo(subscriptionGroupPlan2);
        subscriptionGroupPlan2.setId(2L);
        assertThat(subscriptionGroupPlan1).isNotEqualTo(subscriptionGroupPlan2);
        subscriptionGroupPlan1.setId(null);
        assertThat(subscriptionGroupPlan1).isNotEqualTo(subscriptionGroupPlan2);
    }
}
