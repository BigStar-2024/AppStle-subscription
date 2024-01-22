package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopAssetUrlsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopAssetUrlsDTO.class);
        ShopAssetUrlsDTO shopAssetUrlsDTO1 = new ShopAssetUrlsDTO();
        shopAssetUrlsDTO1.setId(1L);
        ShopAssetUrlsDTO shopAssetUrlsDTO2 = new ShopAssetUrlsDTO();
        assertThat(shopAssetUrlsDTO1).isNotEqualTo(shopAssetUrlsDTO2);
        shopAssetUrlsDTO2.setId(shopAssetUrlsDTO1.getId());
        assertThat(shopAssetUrlsDTO1).isEqualTo(shopAssetUrlsDTO2);
        shopAssetUrlsDTO2.setId(2L);
        assertThat(shopAssetUrlsDTO1).isNotEqualTo(shopAssetUrlsDTO2);
        shopAssetUrlsDTO1.setId(null);
        assertThat(shopAssetUrlsDTO1).isNotEqualTo(shopAssetUrlsDTO2);
    }
}
