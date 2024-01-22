package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomerPortalSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerPortalSettings.class);
        CustomerPortalSettings customerPortalSettings1 = new CustomerPortalSettings();
        customerPortalSettings1.setId(1L);
        CustomerPortalSettings customerPortalSettings2 = new CustomerPortalSettings();
        customerPortalSettings2.setId(customerPortalSettings1.getId());
        assertThat(customerPortalSettings1).isEqualTo(customerPortalSettings2);
        customerPortalSettings2.setId(2L);
        assertThat(customerPortalSettings1).isNotEqualTo(customerPortalSettings2);
        customerPortalSettings1.setId(null);
        assertThat(customerPortalSettings1).isNotEqualTo(customerPortalSettings2);
    }
}
