package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopInfo.class);
        ShopInfo shopInfo1 = new ShopInfo();
        shopInfo1.setId(1L);
        ShopInfo shopInfo2 = new ShopInfo();
        shopInfo2.setId(shopInfo1.getId());
        assertThat(shopInfo1).isEqualTo(shopInfo2);
        shopInfo2.setId(2L);
        assertThat(shopInfo1).isNotEqualTo(shopInfo2);
        shopInfo1.setId(null);
        assertThat(shopInfo1).isNotEqualTo(shopInfo2);
    }
}
