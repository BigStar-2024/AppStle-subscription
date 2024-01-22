package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionCustomCssDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionCustomCssDTO.class);
        SubscriptionCustomCssDTO subscriptionCustomCssDTO1 = new SubscriptionCustomCssDTO();
        subscriptionCustomCssDTO1.setId(1L);
        SubscriptionCustomCssDTO subscriptionCustomCssDTO2 = new SubscriptionCustomCssDTO();
        assertThat(subscriptionCustomCssDTO1).isNotEqualTo(subscriptionCustomCssDTO2);
        subscriptionCustomCssDTO2.setId(subscriptionCustomCssDTO1.getId());
        assertThat(subscriptionCustomCssDTO1).isEqualTo(subscriptionCustomCssDTO2);
        subscriptionCustomCssDTO2.setId(2L);
        assertThat(subscriptionCustomCssDTO1).isNotEqualTo(subscriptionCustomCssDTO2);
        subscriptionCustomCssDTO1.setId(null);
        assertThat(subscriptionCustomCssDTO1).isNotEqualTo(subscriptionCustomCssDTO2);
    }
}
