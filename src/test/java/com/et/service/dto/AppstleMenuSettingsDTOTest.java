package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AppstleMenuSettingsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppstleMenuSettingsDTO.class);
        AppstleMenuSettingsDTO appstleMenuSettingsDTO1 = new AppstleMenuSettingsDTO();
        appstleMenuSettingsDTO1.setId(1L);
        AppstleMenuSettingsDTO appstleMenuSettingsDTO2 = new AppstleMenuSettingsDTO();
        assertThat(appstleMenuSettingsDTO1).isNotEqualTo(appstleMenuSettingsDTO2);
        appstleMenuSettingsDTO2.setId(appstleMenuSettingsDTO1.getId());
        assertThat(appstleMenuSettingsDTO1).isEqualTo(appstleMenuSettingsDTO2);
        appstleMenuSettingsDTO2.setId(2L);
        assertThat(appstleMenuSettingsDTO1).isNotEqualTo(appstleMenuSettingsDTO2);
        appstleMenuSettingsDTO1.setId(null);
        assertThat(appstleMenuSettingsDTO1).isNotEqualTo(appstleMenuSettingsDTO2);
    }
}
