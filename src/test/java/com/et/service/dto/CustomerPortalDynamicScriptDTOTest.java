package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomerPortalDynamicScriptDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerPortalDynamicScriptDTO.class);
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO1 = new CustomerPortalDynamicScriptDTO();
        customerPortalDynamicScriptDTO1.setId(1L);
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO2 = new CustomerPortalDynamicScriptDTO();
        assertThat(customerPortalDynamicScriptDTO1).isNotEqualTo(customerPortalDynamicScriptDTO2);
        customerPortalDynamicScriptDTO2.setId(customerPortalDynamicScriptDTO1.getId());
        assertThat(customerPortalDynamicScriptDTO1).isEqualTo(customerPortalDynamicScriptDTO2);
        customerPortalDynamicScriptDTO2.setId(2L);
        assertThat(customerPortalDynamicScriptDTO1).isNotEqualTo(customerPortalDynamicScriptDTO2);
        customerPortalDynamicScriptDTO1.setId(null);
        assertThat(customerPortalDynamicScriptDTO1).isNotEqualTo(customerPortalDynamicScriptDTO2);
    }
}
