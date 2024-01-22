package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionGroupPlanDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionGroupPlanDTO.class);
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO1 = new SubscriptionGroupPlanDTO();
        subscriptionGroupPlanDTO1.setId(1L);
        SubscriptionGroupPlanDTO subscriptionGroupPlanDTO2 = new SubscriptionGroupPlanDTO();
        assertThat(subscriptionGroupPlanDTO1).isNotEqualTo(subscriptionGroupPlanDTO2);
        subscriptionGroupPlanDTO2.setId(subscriptionGroupPlanDTO1.getId());
        assertThat(subscriptionGroupPlanDTO1).isEqualTo(subscriptionGroupPlanDTO2);
        subscriptionGroupPlanDTO2.setId(2L);
        assertThat(subscriptionGroupPlanDTO1).isNotEqualTo(subscriptionGroupPlanDTO2);
        subscriptionGroupPlanDTO1.setId(null);
        assertThat(subscriptionGroupPlanDTO1).isNotEqualTo(subscriptionGroupPlanDTO2);
    }
}
