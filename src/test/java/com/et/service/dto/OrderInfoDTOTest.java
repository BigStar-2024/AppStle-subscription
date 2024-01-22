package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class OrderInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderInfoDTO.class);
        OrderInfoDTO orderInfoDTO1 = new OrderInfoDTO();
        orderInfoDTO1.setId(1L);
        OrderInfoDTO orderInfoDTO2 = new OrderInfoDTO();
        assertThat(orderInfoDTO1).isNotEqualTo(orderInfoDTO2);
        orderInfoDTO2.setId(orderInfoDTO1.getId());
        assertThat(orderInfoDTO1).isEqualTo(orderInfoDTO2);
        orderInfoDTO2.setId(2L);
        assertThat(orderInfoDTO1).isNotEqualTo(orderInfoDTO2);
        orderInfoDTO1.setId(null);
        assertThat(orderInfoDTO1).isNotEqualTo(orderInfoDTO2);
    }
}
