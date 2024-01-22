package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class SellingPlanMemberInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellingPlanMemberInfoDTO.class);
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO1 = new SellingPlanMemberInfoDTO();
        sellingPlanMemberInfoDTO1.setId(1L);
        SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO2 = new SellingPlanMemberInfoDTO();
        assertThat(sellingPlanMemberInfoDTO1).isNotEqualTo(sellingPlanMemberInfoDTO2);
        sellingPlanMemberInfoDTO2.setId(sellingPlanMemberInfoDTO1.getId());
        assertThat(sellingPlanMemberInfoDTO1).isEqualTo(sellingPlanMemberInfoDTO2);
        sellingPlanMemberInfoDTO2.setId(2L);
        assertThat(sellingPlanMemberInfoDTO1).isNotEqualTo(sellingPlanMemberInfoDTO2);
        sellingPlanMemberInfoDTO1.setId(null);
        assertThat(sellingPlanMemberInfoDTO1).isNotEqualTo(sellingPlanMemberInfoDTO2);
    }
}
