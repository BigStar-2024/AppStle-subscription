package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomerPortalSettingsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerPortalSettingsDTO.class);
        CustomerPortalSettingsDTO customerPortalSettingsDTO1 = new CustomerPortalSettingsDTO();
        customerPortalSettingsDTO1.setId(1L);
        CustomerPortalSettingsDTO customerPortalSettingsDTO2 = new CustomerPortalSettingsDTO();
        assertThat(customerPortalSettingsDTO1).isNotEqualTo(customerPortalSettingsDTO2);
        customerPortalSettingsDTO2.setId(customerPortalSettingsDTO1.getId());
        assertThat(customerPortalSettingsDTO1).isEqualTo(customerPortalSettingsDTO2);
        customerPortalSettingsDTO2.setId(2L);
        assertThat(customerPortalSettingsDTO1).isNotEqualTo(customerPortalSettingsDTO2);
        customerPortalSettingsDTO1.setId(null);
        assertThat(customerPortalSettingsDTO1).isNotEqualTo(customerPortalSettingsDTO2);
    }
}
