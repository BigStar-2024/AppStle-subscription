package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionGroupDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionGroupDTO.class);
        SubscriptionGroupDTO subscriptionGroupDTO1 = new SubscriptionGroupDTO();
        subscriptionGroupDTO1.setId(1L);
        SubscriptionGroupDTO subscriptionGroupDTO2 = new SubscriptionGroupDTO();
        assertThat(subscriptionGroupDTO1).isNotEqualTo(subscriptionGroupDTO2);
        subscriptionGroupDTO2.setId(subscriptionGroupDTO1.getId());
        assertThat(subscriptionGroupDTO1).isEqualTo(subscriptionGroupDTO2);
        subscriptionGroupDTO2.setId(2L);
        assertThat(subscriptionGroupDTO1).isNotEqualTo(subscriptionGroupDTO2);
        subscriptionGroupDTO1.setId(null);
        assertThat(subscriptionGroupDTO1).isNotEqualTo(subscriptionGroupDTO2);
    }
}
