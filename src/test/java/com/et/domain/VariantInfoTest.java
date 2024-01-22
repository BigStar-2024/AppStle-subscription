package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class VariantInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VariantInfo.class);
        VariantInfo variantInfo1 = new VariantInfo();
        variantInfo1.setId(1L);
        VariantInfo variantInfo2 = new VariantInfo();
        variantInfo2.setId(variantInfo1.getId());
        assertThat(variantInfo1).isEqualTo(variantInfo2);
        variantInfo2.setId(2L);
        assertThat(variantInfo1).isNotEqualTo(variantInfo2);
        variantInfo1.setId(null);
        assertThat(variantInfo1).isNotEqualTo(variantInfo2);
    }
}
