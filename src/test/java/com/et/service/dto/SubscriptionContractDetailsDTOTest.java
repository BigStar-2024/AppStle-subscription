package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractDetailsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractDetailsDTO.class);
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO1 = new SubscriptionContractDetailsDTO();
        subscriptionContractDetailsDTO1.setId(1L);
        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO2 = new SubscriptionContractDetailsDTO();
        assertThat(subscriptionContractDetailsDTO1).isNotEqualTo(subscriptionContractDetailsDTO2);
        subscriptionContractDetailsDTO2.setId(subscriptionContractDetailsDTO1.getId());
        assertThat(subscriptionContractDetailsDTO1).isEqualTo(subscriptionContractDetailsDTO2);
        subscriptionContractDetailsDTO2.setId(2L);
        assertThat(subscriptionContractDetailsDTO1).isNotEqualTo(subscriptionContractDetailsDTO2);
        subscriptionContractDetailsDTO1.setId(null);
        assertThat(subscriptionContractDetailsDTO1).isNotEqualTo(subscriptionContractDetailsDTO2);
    }
}
