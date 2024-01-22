package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MembershipDiscountMapperTest {

    private MembershipDiscountMapper membershipDiscountMapper;

    @BeforeEach
    public void setUp() {
        membershipDiscountMapper = new MembershipDiscountMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(membershipDiscountMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(membershipDiscountMapper.fromId(null)).isNull();
    }
}
