package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountProductsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscountProductsDTO.class);
        MembershipDiscountProductsDTO membershipDiscountProductsDTO1 = new MembershipDiscountProductsDTO();
        membershipDiscountProductsDTO1.setId(1L);
        MembershipDiscountProductsDTO membershipDiscountProductsDTO2 = new MembershipDiscountProductsDTO();
        assertThat(membershipDiscountProductsDTO1).isNotEqualTo(membershipDiscountProductsDTO2);
        membershipDiscountProductsDTO2.setId(membershipDiscountProductsDTO1.getId());
        assertThat(membershipDiscountProductsDTO1).isEqualTo(membershipDiscountProductsDTO2);
        membershipDiscountProductsDTO2.setId(2L);
        assertThat(membershipDiscountProductsDTO1).isNotEqualTo(membershipDiscountProductsDTO2);
        membershipDiscountProductsDTO1.setId(null);
        assertThat(membershipDiscountProductsDTO1).isNotEqualTo(membershipDiscountProductsDTO2);
    }
}
