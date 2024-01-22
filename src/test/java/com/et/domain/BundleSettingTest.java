package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleSettingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleSetting.class);
        BundleSetting bundleSetting1 = new BundleSetting();
        bundleSetting1.setId(1L);
        BundleSetting bundleSetting2 = new BundleSetting();
        bundleSetting2.setId(bundleSetting1.getId());
        assertThat(bundleSetting1).isEqualTo(bundleSetting2);
        bundleSetting2.setId(2L);
        assertThat(bundleSetting1).isNotEqualTo(bundleSetting2);
        bundleSetting1.setId(null);
        assertThat(bundleSetting1).isNotEqualTo(bundleSetting2);
    }
}
