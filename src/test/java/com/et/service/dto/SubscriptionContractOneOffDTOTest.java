package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractOneOffDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractOneOffDTO.class);
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO1 = new SubscriptionContractOneOffDTO();
        subscriptionContractOneOffDTO1.setId(1L);
        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO2 = new SubscriptionContractOneOffDTO();
        assertThat(subscriptionContractOneOffDTO1).isNotEqualTo(subscriptionContractOneOffDTO2);
        subscriptionContractOneOffDTO2.setId(subscriptionContractOneOffDTO1.getId());
        assertThat(subscriptionContractOneOffDTO1).isEqualTo(subscriptionContractOneOffDTO2);
        subscriptionContractOneOffDTO2.setId(2L);
        assertThat(subscriptionContractOneOffDTO1).isNotEqualTo(subscriptionContractOneOffDTO2);
        subscriptionContractOneOffDTO1.setId(null);
        assertThat(subscriptionContractOneOffDTO1).isNotEqualTo(subscriptionContractOneOffDTO2);
    }
}
