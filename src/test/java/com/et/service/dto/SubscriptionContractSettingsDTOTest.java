package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SubscriptionContractSettingsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionContractSettingsDTO.class);
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO1 = new SubscriptionContractSettingsDTO();
        subscriptionContractSettingsDTO1.setId(1L);
        SubscriptionContractSettingsDTO subscriptionContractSettingsDTO2 = new SubscriptionContractSettingsDTO();
        assertThat(subscriptionContractSettingsDTO1).isNotEqualTo(subscriptionContractSettingsDTO2);
        subscriptionContractSettingsDTO2.setId(subscriptionContractSettingsDTO1.getId());
        assertThat(subscriptionContractSettingsDTO1).isEqualTo(subscriptionContractSettingsDTO2);
        subscriptionContractSettingsDTO2.setId(2L);
        assertThat(subscriptionContractSettingsDTO1).isNotEqualTo(subscriptionContractSettingsDTO2);
        subscriptionContractSettingsDTO1.setId(null);
        assertThat(subscriptionContractSettingsDTO1).isNotEqualTo(subscriptionContractSettingsDTO2);
    }
}
