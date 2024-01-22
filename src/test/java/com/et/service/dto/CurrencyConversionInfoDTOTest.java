package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CurrencyConversionInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyConversionInfoDTO.class);
        CurrencyConversionInfoDTO currencyConversionInfoDTO1 = new CurrencyConversionInfoDTO();
        currencyConversionInfoDTO1.setId(1L);
        CurrencyConversionInfoDTO currencyConversionInfoDTO2 = new CurrencyConversionInfoDTO();
        assertThat(currencyConversionInfoDTO1).isNotEqualTo(currencyConversionInfoDTO2);
        currencyConversionInfoDTO2.setId(currencyConversionInfoDTO1.getId());
        assertThat(currencyConversionInfoDTO1).isEqualTo(currencyConversionInfoDTO2);
        currencyConversionInfoDTO2.setId(2L);
        assertThat(currencyConversionInfoDTO1).isNotEqualTo(currencyConversionInfoDTO2);
        currencyConversionInfoDTO1.setId(null);
        assertThat(currencyConversionInfoDTO1).isNotEqualTo(currencyConversionInfoDTO2);
    }
}
