package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleDynamicScriptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleDynamicScript.class);
        BundleDynamicScript bundleDynamicScript1 = new BundleDynamicScript();
        bundleDynamicScript1.setId(1L);
        BundleDynamicScript bundleDynamicScript2 = new BundleDynamicScript();
        bundleDynamicScript2.setId(bundleDynamicScript1.getId());
        assertThat(bundleDynamicScript1).isEqualTo(bundleDynamicScript2);
        bundleDynamicScript2.setId(2L);
        assertThat(bundleDynamicScript1).isNotEqualTo(bundleDynamicScript2);
        bundleDynamicScript1.setId(null);
        assertThat(bundleDynamicScript1).isNotEqualTo(bundleDynamicScript2);
    }
}
