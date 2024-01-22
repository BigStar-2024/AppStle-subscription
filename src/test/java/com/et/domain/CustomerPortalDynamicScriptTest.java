package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomerPortalDynamicScriptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerPortalDynamicScript.class);
        CustomerPortalDynamicScript customerPortalDynamicScript1 = new CustomerPortalDynamicScript();
        customerPortalDynamicScript1.setId(1L);
        CustomerPortalDynamicScript customerPortalDynamicScript2 = new CustomerPortalDynamicScript();
        customerPortalDynamicScript2.setId(customerPortalDynamicScript1.getId());
        assertThat(customerPortalDynamicScript1).isEqualTo(customerPortalDynamicScript2);
        customerPortalDynamicScript2.setId(2L);
        assertThat(customerPortalDynamicScript1).isNotEqualTo(customerPortalDynamicScript2);
        customerPortalDynamicScript1.setId(null);
        assertThat(customerPortalDynamicScript1).isNotEqualTo(customerPortalDynamicScript2);
    }
}
