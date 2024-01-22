package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class PlanInfoDiscountDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanInfoDiscountDTO.class);
        PlanInfoDiscountDTO planInfoDiscountDTO1 = new PlanInfoDiscountDTO();
        planInfoDiscountDTO1.setId(1L);
        PlanInfoDiscountDTO planInfoDiscountDTO2 = new PlanInfoDiscountDTO();
        assertThat(planInfoDiscountDTO1).isNotEqualTo(planInfoDiscountDTO2);
        planInfoDiscountDTO2.setId(planInfoDiscountDTO1.getId());
        assertThat(planInfoDiscountDTO1).isEqualTo(planInfoDiscountDTO2);
        planInfoDiscountDTO2.setId(2L);
        assertThat(planInfoDiscountDTO1).isNotEqualTo(planInfoDiscountDTO2);
        planInfoDiscountDTO1.setId(null);
        assertThat(planInfoDiscountDTO1).isNotEqualTo(planInfoDiscountDTO2);
    }
}
