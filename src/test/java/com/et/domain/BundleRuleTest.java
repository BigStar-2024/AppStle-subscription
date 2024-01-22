package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleRule.class);
        BundleRule bundleRule1 = new BundleRule();
        bundleRule1.setId(1L);
        BundleRule bundleRule2 = new BundleRule();
        bundleRule2.setId(bundleRule1.getId());
        assertThat(bundleRule1).isEqualTo(bundleRule2);
        bundleRule2.setId(2L);
        assertThat(bundleRule1).isNotEqualTo(bundleRule2);
        bundleRule1.setId(null);
        assertThat(bundleRule1).isNotEqualTo(bundleRule2);
    }
}
