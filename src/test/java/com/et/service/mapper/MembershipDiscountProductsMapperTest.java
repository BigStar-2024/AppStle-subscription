package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MembershipDiscountProductsMapperTest {

    private MembershipDiscountProductsMapper membershipDiscountProductsMapper;

    @BeforeEach
    public void setUp() {
        membershipDiscountProductsMapper = new MembershipDiscountProductsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(membershipDiscountProductsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(membershipDiscountProductsMapper.fromId(null)).isNull();
    }
}
