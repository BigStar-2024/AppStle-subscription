package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CartWidgetSettingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartWidgetSettings.class);
        CartWidgetSettings cartWidgetSettings1 = new CartWidgetSettings();
        cartWidgetSettings1.setId(1L);
        CartWidgetSettings cartWidgetSettings2 = new CartWidgetSettings();
        cartWidgetSettings2.setId(cartWidgetSettings1.getId());
        assertThat(cartWidgetSettings1).isEqualTo(cartWidgetSettings2);
        cartWidgetSettings2.setId(2L);
        assertThat(cartWidgetSettings1).isNotEqualTo(cartWidgetSettings2);
        cartWidgetSettings1.setId(null);
        assertThat(cartWidgetSettings1).isNotEqualTo(cartWidgetSettings2);
    }
}
