package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class CurrencyConversionInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyConversionInfo.class);
        CurrencyConversionInfo currencyConversionInfo1 = new CurrencyConversionInfo();
        currencyConversionInfo1.setId(1L);
        CurrencyConversionInfo currencyConversionInfo2 = new CurrencyConversionInfo();
        currencyConversionInfo2.setId(currencyConversionInfo1.getId());
        assertThat(currencyConversionInfo1).isEqualTo(currencyConversionInfo2);
        currencyConversionInfo2.setId(2L);
        assertThat(currencyConversionInfo1).isNotEqualTo(currencyConversionInfo2);
        currencyConversionInfo1.setId(null);
        assertThat(currencyConversionInfo1).isNotEqualTo(currencyConversionInfo2);
    }
}
