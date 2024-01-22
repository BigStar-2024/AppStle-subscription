package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AppstleMenuSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppstleMenuSettings.class);
        AppstleMenuSettings appstleMenuSettings1 = new AppstleMenuSettings();
        appstleMenuSettings1.setId(1L);
        AppstleMenuSettings appstleMenuSettings2 = new AppstleMenuSettings();
        appstleMenuSettings2.setId(appstleMenuSettings1.getId());
        assertThat(appstleMenuSettings1).isEqualTo(appstleMenuSettings2);
        appstleMenuSettings2.setId(2L);
        assertThat(appstleMenuSettings1).isNotEqualTo(appstleMenuSettings2);
        appstleMenuSettings1.setId(null);
        assertThat(appstleMenuSettings1).isNotEqualTo(appstleMenuSettings2);
    }
}
