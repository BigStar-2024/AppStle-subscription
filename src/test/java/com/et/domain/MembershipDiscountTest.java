package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscount.class);
        MembershipDiscount membershipDiscount1 = new MembershipDiscount();
        membershipDiscount1.setId(1L);
        MembershipDiscount membershipDiscount2 = new MembershipDiscount();
        membershipDiscount2.setId(membershipDiscount1.getId());
        assertThat(membershipDiscount1).isEqualTo(membershipDiscount2);
        membershipDiscount2.setId(2L);
        assertThat(membershipDiscount1).isNotEqualTo(membershipDiscount2);
        membershipDiscount1.setId(null);
        assertThat(membershipDiscount1).isNotEqualTo(membershipDiscount2);
    }
}
