package com.et.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class OrderInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderInfo.class);
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setId(1L);
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setId(orderInfo1.getId());
        assertThat(orderInfo1).isEqualTo(orderInfo2);
        orderInfo2.setId(2L);
        assertThat(orderInfo1).isNotEqualTo(orderInfo2);
        orderInfo1.setId(null);
        assertThat(orderInfo1).isNotEqualTo(orderInfo2);
    }
}
