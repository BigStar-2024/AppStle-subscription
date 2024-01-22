package com.et.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.et.web.rest.TestUtil;

public class DeliveryProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryProfileDTO.class);
        DeliveryProfileDTO deliveryProfileDTO1 = new DeliveryProfileDTO();
        deliveryProfileDTO1.setId(1L);
        DeliveryProfileDTO deliveryProfileDTO2 = new DeliveryProfileDTO();
        assertThat(deliveryProfileDTO1).isNotEqualTo(deliveryProfileDTO2);
        deliveryProfileDTO2.setId(deliveryProfileDTO1.getId());
        assertThat(deliveryProfileDTO1).isEqualTo(deliveryProfileDTO2);
        deliveryProfileDTO2.setId(2L);
        assertThat(deliveryProfileDTO1).isNotEqualTo(deliveryProfileDTO2);
        deliveryProfileDTO1.setId(null);
        assertThat(deliveryProfileDTO1).isNotEqualTo(deliveryProfileDTO2);
    }
}
