package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionBundlingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionBundlingDTO.class);
        SubscriptionBundlingDTO subscriptionBundlingDTO1 = new SubscriptionBundlingDTO();
        subscriptionBundlingDTO1.setId(1L);
        SubscriptionBundlingDTO subscriptionBundlingDTO2 = new SubscriptionBundlingDTO();
        assertThat(subscriptionBundlingDTO1).isNotEqualTo(subscriptionBundlingDTO2);
        subscriptionBundlingDTO2.setId(subscriptionBundlingDTO1.getId());
        assertThat(subscriptionBundlingDTO1).isEqualTo(subscriptionBundlingDTO2);
        subscriptionBundlingDTO2.setId(2L);
        assertThat(subscriptionBundlingDTO1).isNotEqualTo(subscriptionBundlingDTO2);
        subscriptionBundlingDTO1.setId(null);
        assertThat(subscriptionBundlingDTO1).isNotEqualTo(subscriptionBundlingDTO2);
    }
}
