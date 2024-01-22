package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ThemeSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeSettings.class);
        ThemeSettings themeSettings1 = new ThemeSettings();
        themeSettings1.setId(1L);
        ThemeSettings themeSettings2 = new ThemeSettings();
        themeSettings2.setId(themeSettings1.getId());
        assertThat(themeSettings1).isEqualTo(themeSettings2);
        themeSettings2.setId(2L);
        assertThat(themeSettings1).isNotEqualTo(themeSettings2);
        themeSettings1.setId(null);
        assertThat(themeSettings1).isNotEqualTo(themeSettings2);
    }
}
