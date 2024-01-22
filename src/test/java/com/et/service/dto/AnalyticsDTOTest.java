package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AnalyticsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalyticsDTO.class);
        AnalyticsDTO analyticsDTO1 = new AnalyticsDTO();
        analyticsDTO1.setId(1L);
        AnalyticsDTO analyticsDTO2 = new AnalyticsDTO();
        assertThat(analyticsDTO1).isNotEqualTo(analyticsDTO2);
        analyticsDTO2.setId(analyticsDTO1.getId());
        assertThat(analyticsDTO1).isEqualTo(analyticsDTO2);
        analyticsDTO2.setId(2L);
        assertThat(analyticsDTO1).isNotEqualTo(analyticsDTO2);
        analyticsDTO1.setId(null);
        assertThat(analyticsDTO1).isNotEqualTo(analyticsDTO2);
    }
}
