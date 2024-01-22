package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountProductsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscountProducts.class);
        MembershipDiscountProducts membershipDiscountProducts1 = new MembershipDiscountProducts();
        membershipDiscountProducts1.setId(1L);
        MembershipDiscountProducts membershipDiscountProducts2 = new MembershipDiscountProducts();
        membershipDiscountProducts2.setId(membershipDiscountProducts1.getId());
        assertThat(membershipDiscountProducts1).isEqualTo(membershipDiscountProducts2);
        membershipDiscountProducts2.setId(2L);
        assertThat(membershipDiscountProducts1).isNotEqualTo(membershipDiscountProducts2);
        membershipDiscountProducts1.setId(null);
        assertThat(membershipDiscountProducts1).isNotEqualTo(membershipDiscountProducts2);
    }
}
