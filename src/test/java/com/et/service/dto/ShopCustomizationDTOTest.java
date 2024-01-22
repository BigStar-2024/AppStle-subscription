package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopCustomizationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopCustomizationDTO.class);
        ShopCustomizationDTO shopCustomizationDTO1 = new ShopCustomizationDTO();
        shopCustomizationDTO1.setId(1L);
        ShopCustomizationDTO shopCustomizationDTO2 = new ShopCustomizationDTO();
        assertThat(shopCustomizationDTO1).isNotEqualTo(shopCustomizationDTO2);
        shopCustomizationDTO2.setId(shopCustomizationDTO1.getId());
        assertThat(shopCustomizationDTO1).isEqualTo(shopCustomizationDTO2);
        shopCustomizationDTO2.setId(2L);
        assertThat(shopCustomizationDTO1).isNotEqualTo(shopCustomizationDTO2);
        shopCustomizationDTO1.setId(null);
        assertThat(shopCustomizationDTO1).isNotEqualTo(shopCustomizationDTO2);
    }
}
