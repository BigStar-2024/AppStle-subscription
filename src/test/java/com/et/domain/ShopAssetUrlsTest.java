package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopAssetUrlsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopAssetUrls.class);
        ShopAssetUrls shopAssetUrls1 = new ShopAssetUrls();
        shopAssetUrls1.setId(1L);
        ShopAssetUrls shopAssetUrls2 = new ShopAssetUrls();
        shopAssetUrls2.setId(shopAssetUrls1.getId());
        assertThat(shopAssetUrls1).isEqualTo(shopAssetUrls2);
        shopAssetUrls2.setId(2L);
        assertThat(shopAssetUrls1).isNotEqualTo(shopAssetUrls2);
        shopAssetUrls1.setId(null);
        assertThat(shopAssetUrls1).isNotEqualTo(shopAssetUrls2);
    }
}
