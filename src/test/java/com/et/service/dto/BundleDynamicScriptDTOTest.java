package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleDynamicScriptDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleDynamicScriptDTO.class);
        BundleDynamicScriptDTO bundleDynamicScriptDTO1 = new BundleDynamicScriptDTO();
        bundleDynamicScriptDTO1.setId(1L);
        BundleDynamicScriptDTO bundleDynamicScriptDTO2 = new BundleDynamicScriptDTO();
        assertThat(bundleDynamicScriptDTO1).isNotEqualTo(bundleDynamicScriptDTO2);
        bundleDynamicScriptDTO2.setId(bundleDynamicScriptDTO1.getId());
        assertThat(bundleDynamicScriptDTO1).isEqualTo(bundleDynamicScriptDTO2);
        bundleDynamicScriptDTO2.setId(2L);
        assertThat(bundleDynamicScriptDTO1).isNotEqualTo(bundleDynamicScriptDTO2);
        bundleDynamicScriptDTO1.setId(null);
        assertThat(bundleDynamicScriptDTO1).isNotEqualTo(bundleDynamicScriptDTO2);
    }
}
