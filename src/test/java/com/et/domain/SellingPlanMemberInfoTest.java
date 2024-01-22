package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SellingPlanMemberInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellingPlanMemberInfo.class);
        SellingPlanMemberInfo sellingPlanMemberInfo1 = new SellingPlanMemberInfo();
        sellingPlanMemberInfo1.setId(1L);
        SellingPlanMemberInfo sellingPlanMemberInfo2 = new SellingPlanMemberInfo();
        sellingPlanMemberInfo2.setId(sellingPlanMemberInfo1.getId());
        assertThat(sellingPlanMemberInfo1).isEqualTo(sellingPlanMemberInfo2);
        sellingPlanMemberInfo2.setId(2L);
        assertThat(sellingPlanMemberInfo1).isNotEqualTo(sellingPlanMemberInfo2);
        sellingPlanMemberInfo1.setId(null);
        assertThat(sellingPlanMemberInfo1).isNotEqualTo(sellingPlanMemberInfo2);
    }
}
