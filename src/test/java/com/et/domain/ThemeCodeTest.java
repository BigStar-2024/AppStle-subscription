package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ThemeCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeCode.class);
        ThemeCode themeCode1 = new ThemeCode();
        themeCode1.setId(1L);
        ThemeCode themeCode2 = new ThemeCode();
        themeCode2.setId(themeCode1.getId());
        assertThat(themeCode1).isEqualTo(themeCode2);
        themeCode2.setId(2L);
        assertThat(themeCode1).isNotEqualTo(themeCode2);
        themeCode1.setId(null);
        assertThat(themeCode1).isNotEqualTo(themeCode2);
    }
}
