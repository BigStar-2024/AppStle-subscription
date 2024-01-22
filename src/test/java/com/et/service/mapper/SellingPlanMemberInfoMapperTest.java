package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SellingPlanMemberInfoMapperTest {

    private SellingPlanMemberInfoMapper sellingPlanMemberInfoMapper;

    @BeforeEach
    public void setUp() {
        sellingPlanMemberInfoMapper = new SellingPlanMemberInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(sellingPlanMemberInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(sellingPlanMemberInfoMapper.fromId(null)).isNull();
    }
}
