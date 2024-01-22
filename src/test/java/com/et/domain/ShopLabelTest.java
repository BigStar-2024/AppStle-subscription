package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopLabelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopLabel.class);
        ShopLabel shopLabel1 = new ShopLabel();
        shopLabel1.setId(1L);
        ShopLabel shopLabel2 = new ShopLabel();
        shopLabel2.setId(shopLabel1.getId());
        assertThat(shopLabel1).isEqualTo(shopLabel2);
        shopLabel2.setId(2L);
        assertThat(shopLabel1).isNotEqualTo(shopLabel2);
        shopLabel1.setId(null);
        assertThat(shopLabel1).isNotEqualTo(shopLabel2);
    }
}
