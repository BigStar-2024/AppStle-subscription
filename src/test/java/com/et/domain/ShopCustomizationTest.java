package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopCustomizationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopCustomization.class);
        ShopCustomization shopCustomization1 = new ShopCustomization();
        shopCustomization1.setId(1L);
        ShopCustomization shopCustomization2 = new ShopCustomization();
        shopCustomization2.setId(shopCustomization1.getId());
        assertThat(shopCustomization1).isEqualTo(shopCustomization2);
        shopCustomization2.setId(2L);
        assertThat(shopCustomization1).isNotEqualTo(shopCustomization2);
        shopCustomization1.setId(null);
        assertThat(shopCustomization1).isNotEqualTo(shopCustomization2);
    }
}
