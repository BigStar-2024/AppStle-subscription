package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ProductSwapTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductSwap.class);
        ProductSwap productSwap1 = new ProductSwap();
        productSwap1.setId(1L);
        ProductSwap productSwap2 = new ProductSwap();
        productSwap2.setId(productSwap1.getId());
        assertThat(productSwap1).isEqualTo(productSwap2);
        productSwap2.setId(2L);
        assertThat(productSwap1).isNotEqualTo(productSwap2);
        productSwap1.setId(null);
        assertThat(productSwap1).isNotEqualTo(productSwap2);
    }
}
