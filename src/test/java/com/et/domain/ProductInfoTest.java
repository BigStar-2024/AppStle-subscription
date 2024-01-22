package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class ProductInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInfo.class);
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setId(1L);
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setId(productInfo1.getId());
        assertThat(productInfo1).isEqualTo(productInfo2);
        productInfo2.setId(2L);
        assertThat(productInfo1).isNotEqualTo(productInfo2);
        productInfo1.setId(null);
        assertThat(productInfo1).isNotEqualTo(productInfo2);
    }
}
