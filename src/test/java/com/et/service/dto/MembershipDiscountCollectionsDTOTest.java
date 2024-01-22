package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class MembershipDiscountCollectionsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipDiscountCollectionsDTO.class);
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO1 = new MembershipDiscountCollectionsDTO();
        membershipDiscountCollectionsDTO1.setId(1L);
        MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO2 = new MembershipDiscountCollectionsDTO();
        assertThat(membershipDiscountCollectionsDTO1).isNotEqualTo(membershipDiscountCollectionsDTO2);
        membershipDiscountCollectionsDTO2.setId(membershipDiscountCollectionsDTO1.getId());
        assertThat(membershipDiscountCollectionsDTO1).isEqualTo(membershipDiscountCollectionsDTO2);
        membershipDiscountCollectionsDTO2.setId(2L);
        assertThat(membershipDiscountCollectionsDTO1).isNotEqualTo(membershipDiscountCollectionsDTO2);
        membershipDiscountCollectionsDTO1.setId(null);
        assertThat(membershipDiscountCollectionsDTO1).isNotEqualTo(membershipDiscountCollectionsDTO2);
    }
}
