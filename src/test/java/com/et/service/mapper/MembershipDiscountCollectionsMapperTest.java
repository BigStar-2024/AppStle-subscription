package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MembershipDiscountCollectionsMapperTest {

    private MembershipDiscountCollectionsMapper membershipDiscountCollectionsMapper;

    @BeforeEach
    public void setUp() {
        membershipDiscountCollectionsMapper = new MembershipDiscountCollectionsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(membershipDiscountCollectionsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(membershipDiscountCollectionsMapper.fromId(null)).isNull();
    }
}
