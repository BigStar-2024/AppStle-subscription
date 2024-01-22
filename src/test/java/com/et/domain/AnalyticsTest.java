package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class AnalyticsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analytics.class);
        Analytics analytics1 = new Analytics();
        analytics1.setId(1L);
        Analytics analytics2 = new Analytics();
        analytics2.setId(analytics1.getId());
        assertThat(analytics1).isEqualTo(analytics2);
        analytics2.setId(2L);
        assertThat(analytics1).isNotEqualTo(analytics2);
        analytics1.setId(null);
        assertThat(analytics1).isNotEqualTo(analytics2);
    }
}
