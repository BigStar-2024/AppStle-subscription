package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscountDTO.class);
        MembershipDiscountDTO membershipDiscountDTO1 = new MembershipDiscountDTO();
        membershipDiscountDTO1.setId(1L);
        MembershipDiscountDTO membershipDiscountDTO2 = new MembershipDiscountDTO();
        assertThat(membershipDiscountDTO1).isNotEqualTo(membershipDiscountDTO2);
        membershipDiscountDTO2.setId(membershipDiscountDTO1.getId());
        assertThat(membershipDiscountDTO1).isEqualTo(membershipDiscountDTO2);
        membershipDiscountDTO2.setId(2L);
        assertThat(membershipDiscountDTO1).isNotEqualTo(membershipDiscountDTO2);
        membershipDiscountDTO1.setId(null);
        assertThat(membershipDiscountDTO1).isNotEqualTo(membershipDiscountDTO2);
    }
}
