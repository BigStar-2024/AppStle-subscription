package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleSettingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleSettingDTO.class);
        BundleSettingDTO bundleSettingDTO1 = new BundleSettingDTO();
        bundleSettingDTO1.setId(1L);
        BundleSettingDTO bundleSettingDTO2 = new BundleSettingDTO();
        assertThat(bundleSettingDTO1).isNotEqualTo(bundleSettingDTO2);
        bundleSettingDTO2.setId(bundleSettingDTO1.getId());
        assertThat(bundleSettingDTO1).isEqualTo(bundleSettingDTO2);
        bundleSettingDTO2.setId(2L);
        assertThat(bundleSettingDTO1).isNotEqualTo(bundleSettingDTO2);
        bundleSettingDTO1.setId(null);
        assertThat(bundleSettingDTO1).isNotEqualTo(bundleSettingDTO2);
    }
}
