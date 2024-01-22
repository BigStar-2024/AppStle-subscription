package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class PlanInfoDiscountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanInfoDiscount.class);
        PlanInfoDiscount planInfoDiscount1 = new PlanInfoDiscount();
        planInfoDiscount1.setId(1L);
        PlanInfoDiscount planInfoDiscount2 = new PlanInfoDiscount();
        planInfoDiscount2.setId(planInfoDiscount1.getId());
        assertThat(planInfoDiscount1).isEqualTo(planInfoDiscount2);
        planInfoDiscount2.setId(2L);
        assertThat(planInfoDiscount1).isNotEqualTo(planInfoDiscount2);
        planInfoDiscount1.setId(null);
        assertThat(planInfoDiscount1).isNotEqualTo(planInfoDiscount2);
    }
}
