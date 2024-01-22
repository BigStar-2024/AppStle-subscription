package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class VariantInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VariantInfoDTO.class);
        VariantInfoDTO variantInfoDTO1 = new VariantInfoDTO();
        variantInfoDTO1.setId(1L);
        VariantInfoDTO variantInfoDTO2 = new VariantInfoDTO();
        assertThat(variantInfoDTO1).isNotEqualTo(variantInfoDTO2);
        variantInfoDTO2.setId(variantInfoDTO1.getId());
        assertThat(variantInfoDTO1).isEqualTo(variantInfoDTO2);
        variantInfoDTO2.setId(2L);
        assertThat(variantInfoDTO1).isNotEqualTo(variantInfoDTO2);
        variantInfoDTO1.setId(null);
        assertThat(variantInfoDTO1).isNotEqualTo(variantInfoDTO2);
    }
}
