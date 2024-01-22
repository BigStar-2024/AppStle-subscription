package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ProductSwapDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductSwapDTO.class);
        ProductSwapDTO productSwapDTO1 = new ProductSwapDTO();
        productSwapDTO1.setId(1L);
        ProductSwapDTO productSwapDTO2 = new ProductSwapDTO();
        assertThat(productSwapDTO1).isNotEqualTo(productSwapDTO2);
        productSwapDTO2.setId(productSwapDTO1.getId());
        assertThat(productSwapDTO1).isEqualTo(productSwapDTO2);
        productSwapDTO2.setId(2L);
        assertThat(productSwapDTO1).isNotEqualTo(productSwapDTO2);
        productSwapDTO1.setId(null);
        assertThat(productSwapDTO1).isNotEqualTo(productSwapDTO2);
    }
}
