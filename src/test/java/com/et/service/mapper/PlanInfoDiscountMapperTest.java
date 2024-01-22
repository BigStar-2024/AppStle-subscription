package com.et.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PlanInfoDiscountMapperTest {

    private PlanInfoDiscountMapper planInfoDiscountMapper;

    @BeforeEach
    public void setUp() {
        planInfoDiscountMapper = new PlanInfoDiscountMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(planInfoDiscountMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planInfoDiscountMapper.fromId(null)).isNull();
    }
}
