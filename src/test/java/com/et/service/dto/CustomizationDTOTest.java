package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CustomizationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomizationDTO.class);
        CustomizationDTO customizationDTO1 = new CustomizationDTO();
        customizationDTO1.setId(1L);
        CustomizationDTO customizationDTO2 = new CustomizationDTO();
        assertThat(customizationDTO1).isNotEqualTo(customizationDTO2);
        customizationDTO2.setId(customizationDTO1.getId());
        assertThat(customizationDTO1).isEqualTo(customizationDTO2);
        customizationDTO2.setId(2L);
        assertThat(customizationDTO1).isNotEqualTo(customizationDTO2);
        customizationDTO1.setId(null);
        assertThat(customizationDTO1).isNotEqualTo(customizationDTO2);
    }
}
