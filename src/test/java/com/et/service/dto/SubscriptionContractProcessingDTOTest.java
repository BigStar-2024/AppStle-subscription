package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractProcessingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractProcessingDTO.class);
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO1 = new SubscriptionContractProcessingDTO();
        subscriptionContractProcessingDTO1.setId(1L);
        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO2 = new SubscriptionContractProcessingDTO();
        assertThat(subscriptionContractProcessingDTO1).isNotEqualTo(subscriptionContractProcessingDTO2);
        subscriptionContractProcessingDTO2.setId(subscriptionContractProcessingDTO1.getId());
        assertThat(subscriptionContractProcessingDTO1).isEqualTo(subscriptionContractProcessingDTO2);
        subscriptionContractProcessingDTO2.setId(2L);
        assertThat(subscriptionContractProcessingDTO1).isNotEqualTo(subscriptionContractProcessingDTO2);
        subscriptionContractProcessingDTO1.setId(null);
        assertThat(subscriptionContractProcessingDTO1).isNotEqualTo(subscriptionContractProcessingDTO2);
    }
}
