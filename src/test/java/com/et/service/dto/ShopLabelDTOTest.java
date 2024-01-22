package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ShopLabelDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopLabelDTO.class);
        ShopLabelDTO shopLabelDTO1 = new ShopLabelDTO();
        shopLabelDTO1.setId(1L);
        ShopLabelDTO shopLabelDTO2 = new ShopLabelDTO();
        assertThat(shopLabelDTO1).isNotEqualTo(shopLabelDTO2);
        shopLabelDTO2.setId(shopLabelDTO1.getId());
        assertThat(shopLabelDTO1).isEqualTo(shopLabelDTO2);
        shopLabelDTO2.setId(2L);
        assertThat(shopLabelDTO1).isNotEqualTo(shopLabelDTO2);
        shopLabelDTO1.setId(null);
        assertThat(shopLabelDTO1).isNotEqualTo(shopLabelDTO2);
    }
}
