package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ProductInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInfoDTO.class);
        ProductInfoDTO productInfoDTO1 = new ProductInfoDTO();
        productInfoDTO1.setId(1L);
        ProductInfoDTO productInfoDTO2 = new ProductInfoDTO();
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
        productInfoDTO2.setId(productInfoDTO1.getId());
        assertThat(productInfoDTO1).isEqualTo(productInfoDTO2);
        productInfoDTO2.setId(2L);
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
        productInfoDTO1.setId(null);
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
    }
}
