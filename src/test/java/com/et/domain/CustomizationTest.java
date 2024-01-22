package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomizationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customization.class);
        Customization customization1 = new Customization();
        customization1.setId(1L);
        Customization customization2 = new Customization();
        customization2.setId(customization1.getId());
        assertThat(customization1).isEqualTo(customization2);
        customization2.setId(2L);
        assertThat(customization1).isNotEqualTo(customization2);
        customization1.setId(null);
        assertThat(customization1).isNotEqualTo(customization2);
    }
}
