package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class BundleRuleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BundleRuleDTO.class);
        BundleRuleDTO bundleRuleDTO1 = new BundleRuleDTO();
        bundleRuleDTO1.setId(1L);
        BundleRuleDTO bundleRuleDTO2 = new BundleRuleDTO();
        assertThat(bundleRuleDTO1).isNotEqualTo(bundleRuleDTO2);
        bundleRuleDTO2.setId(bundleRuleDTO1.getId());
        assertThat(bundleRuleDTO1).isEqualTo(bundleRuleDTO2);
        bundleRuleDTO2.setId(2L);
        assertThat(bundleRuleDTO1).isNotEqualTo(bundleRuleDTO2);
        bundleRuleDTO1.setId(null);
        assertThat(bundleRuleDTO1).isNotEqualTo(bundleRuleDTO2);
    }
}
