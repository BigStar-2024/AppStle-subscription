package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountCollectionsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscountCollections.class);
        MembershipDiscountCollections membershipDiscountCollections1 = new MembershipDiscountCollections();
        membershipDiscountCollections1.setId(1L);
        MembershipDiscountCollections membershipDiscountCollections2 = new MembershipDiscountCollections();
        membershipDiscountCollections2.setId(membershipDiscountCollections1.getId());
        assertThat(membershipDiscountCollections1).isEqualTo(membershipDiscountCollections2);
        membershipDiscountCollections2.setId(2L);
        assertThat(membershipDiscountCollections1).isNotEqualTo(membershipDiscountCollections2);
        membershipDiscountCollections1.setId(null);
        assertThat(membershipDiscountCollections1).isNotEqualTo(membershipDiscountCollections2);
    }
}
